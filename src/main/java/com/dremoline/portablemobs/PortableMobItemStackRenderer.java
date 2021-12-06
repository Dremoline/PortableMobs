package com.dremoline.portablemobs;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.supermartijn642.core.ClientUtils;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.vector.Quaternion;

import java.util.Optional;

public class PortableMobItemStackRenderer extends ItemStackTileEntityRenderer {

    public static final int ROTATION_TIME = 5000;

    public static final PortableMobItemStackRenderer INSTANCE = new PortableMobItemStackRenderer();

    public static PortableMobItemStackRenderer getInstance(){
        return INSTANCE;
    }

    @Override
    public void renderByItem(ItemStack itemStack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer typeBuffer, int combinedLight, int combinedOverlay){
        IBakedModel model = ClientUtils.getItemRenderer().getItemModelShaper().getItemModel(itemStack);
        renderDefaultItem(itemStack, matrixStack, transformType, typeBuffer, combinedLight, combinedOverlay, model);

        if(!itemStack.hasTag() || !itemStack.getTag().getBoolean("has_entity")){
            return;
        }

        CompoundNBT compound = itemStack.getTag();
        Optional<EntityType<?>> optional = EntityType.byString(compound.getString("entity_type"));
        if(optional.isPresent()){
            Entity living = optional.get().create(ClientUtils.getWorld());
            if(living != null){
                living.load(compound.getCompound("entity_data"));

                matrixStack.pushPose();
                matrixStack.translate(0.5, 0, 0.5);
                matrixStack.mulPose(new Quaternion(0, (System.currentTimeMillis() % ROTATION_TIME) / (float)ROTATION_TIME * 360, 0, true));

                float width = living.getBbWidth();
                float height = living.getBbHeight();
                float size = 0.9f;
                float length = (float)Math.sqrt(2 * width * width + height * height) * 1.2f;
                float scalar = size / length;
                matrixStack.scale(scalar, scalar, scalar);

                renderEntity(living, matrixStack, typeBuffer, combinedLight);

                matrixStack.popPose();
            }
        }

    }

    private static <T extends Entity> void renderEntity(T living, MatrixStack matrixStack, IRenderTypeBuffer typeBuffer, int combinedLight){
        EntityRenderer<? super T> renderer = ClientUtils.getMinecraft().getEntityRenderDispatcher().getRenderer(living);
        renderer.render(living, 0, 0, matrixStack, typeBuffer, combinedLight);
    }

    private static void renderDefaultItem(ItemStack itemStack, MatrixStack matrixStack, ItemCameraTransforms.TransformType cameraTransforms, IRenderTypeBuffer renderTypeBuffer, int combinedLight, int combinedOverlay, IBakedModel model){
        ItemRenderer renderer = ClientUtils.getMinecraft().getItemRenderer();

        matrixStack.pushPose();

        if(model.isLayered()){
            net.minecraftforge.client.ForgeHooksClient.drawItemLayered(renderer, model, itemStack, matrixStack, renderTypeBuffer, combinedLight, combinedOverlay, true);
        }else{
            RenderType rendertype = RenderTypeLookup.getRenderType(itemStack, true);
            IVertexBuilder ivertexbuilder;

            ivertexbuilder = ItemRenderer.getFoilBufferDirect(renderTypeBuffer, rendertype, true, itemStack.hasFoil());

            renderer.renderModelLists(model, itemStack, combinedLight, combinedOverlay, matrixStack, ivertexbuilder);
        }

        matrixStack.popPose();
    }
}

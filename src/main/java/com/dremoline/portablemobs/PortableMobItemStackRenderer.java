package com.dremoline.portablemobs;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.supermartijn642.core.ClientUtils;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import com.mojang.math.Quaternion;

import java.util.Optional;

public class PortableMobItemStackRenderer extends BlockEntityWithoutLevelRenderer {

    public static final int ROTATION_TIME = 5000;

    public PortableMobItemStackRenderer(BlockEntityRenderDispatcher p_172550_) {
        super(p_172550_, new EntityModelSet());
    }

    @Override
    public void renderByItem(ItemStack itemStack, ItemTransforms.TransformType transformType, PoseStack matrixStack, MultiBufferSource typeBuffer, int combinedLight, int combinedOverlay){
        BakedModel model = ClientUtils.getItemRenderer().getItemModelShaper().getItemModel(itemStack);
        renderDefaultItem(itemStack, matrixStack, transformType, typeBuffer, combinedLight, combinedOverlay, model);

        if(!itemStack.hasTag() || !itemStack.getTag().getBoolean("has_entity")){
            return;
        }

        CompoundTag compound = itemStack.getTag();
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

    private static <T extends Entity> void renderEntity(T living, PoseStack matrixStack, MultiBufferSource typeBuffer, int combinedLight){
        EntityRenderer<? super T> renderer = ClientUtils.getMinecraft().getEntityRenderDispatcher().getRenderer(living);
        renderer.render(living, 0, 0, matrixStack, typeBuffer, combinedLight);
    }

    private static void renderDefaultItem(ItemStack itemStack, PoseStack matrixStack, ItemTransforms.TransformType cameraTransforms, MultiBufferSource renderTypeBuffer, int combinedLight, int combinedOverlay, BakedModel model){
        ItemRenderer renderer = ClientUtils.getMinecraft().getItemRenderer();

        matrixStack.pushPose();

        if(model.isLayered()){
            net.minecraftforge.client.ForgeHooksClient.drawItemLayered(renderer, model, itemStack, matrixStack, renderTypeBuffer, combinedLight, combinedOverlay, true);
        }else{
            RenderType rendertype = ItemBlockRenderTypes.getRenderType(itemStack, true);
            VertexConsumer ivertexbuilder;

            ivertexbuilder = ItemRenderer.getFoilBufferDirect(renderTypeBuffer, rendertype, true, itemStack.hasFoil());

            renderer.renderModelLists(model, itemStack, combinedLight, combinedOverlay, matrixStack, ivertexbuilder);
        }

        matrixStack.popPose();
    }
}

package com.dremoline.portablemobs;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.render.CustomItemRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class PortableMobItemStackRenderer implements CustomItemRenderer {

    public static final int ROTATION_TIME = 5000;

    @Override
    public void render(ItemStack itemStack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        BakedModel model = ClientUtils.getItemRenderer().getItemModelShaper().getItemModel(itemStack);
        renderDefaultItem(itemStack, poseStack, transformType, bufferSource, combinedLight, combinedOverlay, model);

        if (!itemStack.hasTag() || !itemStack.getTag().getBoolean("has_entity")) {
            return;
        }

        CompoundTag compound = itemStack.getTag();
        Optional<EntityType<?>> optional = EntityType.byString(compound.getString("entity_type"));
        if (optional.isPresent()) {
            Entity living = optional.get().create(ClientUtils.getWorld());
            if (living != null) {
                living.load(compound.getCompound("entity_data"));

                poseStack.pushPose();
                poseStack.translate(0.5, 0, 0.5);
                poseStack.mulPose(new Quaternion(0, (System.currentTimeMillis() % ROTATION_TIME) / (float) ROTATION_TIME * 360, 0, true));

                float width = living.getBbWidth();
                float height = living.getBbHeight();
                float size = 0.9f;
                float length = (float) Math.sqrt(2 * width * width + height * height) * 1.2f;
                float scalar = size / length;
                poseStack.scale(scalar, scalar, scalar);

                renderEntity(living, poseStack, bufferSource, combinedLight);

                poseStack.popPose();
            }
        }

    }

    private static <T extends Entity> void renderEntity(T living, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight) {
        EntityRenderer<? super T> renderer = ClientUtils.getMinecraft().getEntityRenderDispatcher().getRenderer(living);
        renderer.render(living, 0, 0, poseStack, bufferSource, combinedLight);
    }

    private static void renderDefaultItem(ItemStack itemStack, PoseStack poseStack, ItemTransforms.TransformType cameraTransforms, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay, BakedModel model) {
        for (BakedModel passModel : model.getRenderPasses(itemStack, true)) {
            for (RenderType renderType : passModel.getRenderTypes(itemStack, true)) {
                VertexConsumer vertexConsumer = ItemRenderer.getFoilBufferDirect(bufferSource, renderType, true, itemStack.hasFoil());
                ClientUtils.getItemRenderer().renderModelLists(passModel, itemStack, combinedLight, combinedOverlay, poseStack, vertexConsumer);
            }
        }
    }
}

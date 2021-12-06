package com.dremoline.portablemobs;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraftforge.client.model.BakedModelWrapper;

public class PortableMobBakedItemModel extends BakedModelWrapper<IBakedModel> {
    public PortableMobBakedItemModel(IBakedModel originalModel) {
        super(originalModel);
    }

    @Override
    public boolean isCustomRenderer() {
        return true;
    }

    @Override
    public IBakedModel handlePerspective(ItemCameraTransforms.TransformType cameraTransformType, MatrixStack mat) {
        super.handlePerspective(cameraTransformType, mat);
        return this;
    }
}

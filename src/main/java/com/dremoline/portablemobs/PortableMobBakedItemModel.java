package com.dremoline.portablemobs;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraftforge.client.model.BakedModelWrapper;

public class PortableMobBakedItemModel extends BakedModelWrapper<BakedModel> {

    public PortableMobBakedItemModel(BakedModel originalModel){
        super(originalModel);
    }

    @Override
    public boolean isCustomRenderer(){
        return true;
    }

    @Override
    public BakedModel applyTransform(ItemTransforms.TransformType cameraTransformType, PoseStack mat, boolean applyLeftHandTransform){
        super.applyTransform(cameraTransformType, mat, applyLeftHandTransform);
        return this;
    }
}

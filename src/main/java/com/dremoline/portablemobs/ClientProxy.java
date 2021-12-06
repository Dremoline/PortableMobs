package com.dremoline.portablemobs;

import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientProxy {

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent e) {
        for (PortableMobTypes type : PortableMobTypes.values()) {
            ResourceLocation location = new ModelResourceLocation("portablemobs:" + type.toSuffix() + "_capture_cell", "inventory");
            IBakedModel model = e.getModelRegistry().get(location);
            System.out.println("model: "+ (model != null));
            if (model != null) {
                e.getModelRegistry().put(location, new PortableMobBakedItemModel(model));
            }
        }
    }
}

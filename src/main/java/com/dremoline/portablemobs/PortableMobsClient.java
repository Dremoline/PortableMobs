package com.dremoline.portablemobs;

import com.supermartijn642.core.registry.ClientRegistrationHandler;
import com.supermartijn642.core.render.CustomRendererBakedModelWrapper;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;

public class PortableMobsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientRegistrationHandler handler = ClientRegistrationHandler.get("portablemobs");
        for (PortableMobTypes type : PortableMobTypes.values()) {
            handler.registerCustomItemRenderer(type::getItem, PortableMobItemStackRenderer::new);
            ResourceLocation location = new ModelResourceLocation(new ResourceLocation("portablemobs:" + type.toSuffix() + "_capture_cell"), "inventory");
            handler.registerModelOverwrite(location, CustomRendererBakedModelWrapper::wrap);
        }
    }
}

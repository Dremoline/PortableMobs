package com.dremoline.portablemobs;

import com.supermartijn642.core.registry.ClientRegistrationHandler;
import com.supermartijn642.core.render.CustomRendererBakedModelWrapper;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;

public class PortableMobsClient {
    public static void register() {
        ClientRegistrationHandler handler = ClientRegistrationHandler.get("portablemobs");
        for (PortableMobTypes type : PortableMobTypes.values()) {
            handler.registerCustomItemRenderer(type::getItem, PortableMobItemStackRenderer::new);
            ResourceLocation location = new ModelResourceLocation("portablemobs:" + type.toSuffix() + "_capture_cell", "inventory");
            handler.registerModelOverwrite(location, CustomRendererBakedModelWrapper::wrap);
        }
    }
}

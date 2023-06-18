package com.dremoline.portablemobs;

import com.dremoline.portablemobs.generators.PortableMobsLanguageGenerator;
import com.dremoline.portablemobs.generators.PortableMobsModelGenerator;
import com.dremoline.portablemobs.generators.PortableMobsRecipeGenerator;
import com.dremoline.portablemobs.generators.PortableMobsTagGenerator;
import com.supermartijn642.core.item.CreativeItemGroup;
import com.supermartijn642.core.registry.GeneratorRegistrationHandler;
import com.supermartijn642.core.registry.RegistrationHandler;
import net.fabricmc.api.ModInitializer;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
public class PortableMobs implements ModInitializer {

    public static final CreativeItemGroup GROUP = CreativeItemGroup.create("portablemobs", PortableMobTypes.BASIC::getItem);

    @Override
    public void onInitialize() {
        RegistrationHandler handler = RegistrationHandler.get("portablemobs");
        for (PortableMobTypes type : PortableMobTypes.values()) {
            handler.registerItemCallback(type::registerItem);
        }
        handler.registerRecipeSerializer("upgrade_capture_cell", PortableMobUpgradeRecipe.SERIALIZER);

        GeneratorRegistrationHandler generatorHandler = GeneratorRegistrationHandler.get("portablemobs");
        generatorHandler.addGenerator(PortableMobsLanguageGenerator::new);
        generatorHandler.addGenerator(PortableMobsTagGenerator::new);
        generatorHandler.addGenerator(PortableMobsRecipeGenerator::new);
        generatorHandler.addGenerator(PortableMobsModelGenerator::new);
    }
}

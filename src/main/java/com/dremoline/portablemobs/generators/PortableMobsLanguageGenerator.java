package com.dremoline.portablemobs.generators;

import com.dremoline.portablemobs.PortableMobTypes;
import com.dremoline.portablemobs.PortableMobs;
import com.supermartijn642.core.generator.LanguageGenerator;
import com.supermartijn642.core.generator.ResourceCache;

public class PortableMobsLanguageGenerator extends LanguageGenerator {
    public PortableMobsLanguageGenerator(ResourceCache cache) {
        super("portablemobs", cache, "en_us");
    }

    @Override
    public void generate() {
        this.item(PortableMobTypes.BASIC.getItem(), "Basic Capture Cell");
        this.item(PortableMobTypes.MASTER.getItem(), "Master Capture Cell");
        this.translation("portablemobs.capture_failed", "It's not possible to capture this mob");
        this.translation("portablemobs.capture_success", "Capture successful");
        this.translation("portablemobs.tooltip_name", "%s");
        this.itemGroup(PortableMobs.GROUP, "Portable Mobs");
    }
}

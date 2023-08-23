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
        this.itemGroup(PortableMobs.GROUP, "Portable Mobs");
        this.translation("portablemobs.capture_failed", "It's not possible to capture this mob");
        this.translation("portablemobs.capture_failed_player", "Cannot capture players!");
        this.translation("portablemobs.capture_success", "Capture successful");
        this.translation("portablemobs.tooltip_name", "%s");
        this.translation("portablemobs.advancement.mastercelladvancement.desc", "Craft a Master Capture Cell");
        this.translation("portablemobs.advancement.mastercelladvancement.title", "Gotta Capture Them All!");
        this.translation("portablemobs.advancement.basiccelladvancement.desc", "Craft a Basic Capture Cell");
        this.translation("portablemobs.advancement.basiccelladvancement.title", "Capturing Mobs");
        this.translation("portablemobs.advancement.playercaptureadvancement.desc", "Try to capture a player using a Capture Cell");
        this.translation("portablemobs.advancement.playercaptureadvancement.title", "Yes, we thought of this...");
        this.translation("portablemobs.advancement.captureadvancement.desc", "Capture a mob using a Capture Cell");
        this.translation("portablemobs.advancement.captureadvancement.title", "Mob in a Box");
    }
}

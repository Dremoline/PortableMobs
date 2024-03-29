package com.dremoline.portablemobs.generators;

import com.dremoline.portablemobs.PortableMobTypes;
import com.dremoline.portablemobs.PortableMobsSimpleTrigger;
import com.supermartijn642.core.generator.AdvancementGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

public class PortableMobsAdvancementGenerator extends AdvancementGenerator {
    public PortableMobsAdvancementGenerator(ResourceCache cache) {
        super("portablemobs", cache);
    }

    @Override
    public void generate() {
        this.advancement("basiccelladvancement")
                .icon(PortableMobTypes.BASIC.getItem())
                .announceToChat(true)
                .description("portablemobs.advancement.basiccelladvancement.desc")
                .title("portablemobs.advancement.basiccelladvancement.title")
                .taskFrame()
                .background("minecraft", "block/smooth_stone")
                .hasItemsCriterion("basiccell", PortableMobTypes.BASIC.getItem());

        this.advancement("mastercelladvancement")
                .icon(PortableMobTypes.MASTER.getItem())
                .announceToChat(true)
                .description("portablemobs.advancement.mastercelladvancement.desc")
                .title("portablemobs.advancement.mastercelladvancement.title")
                .taskFrame()
                .parent("basiccelladvancement")
                .hasItemsCriterion("mastercell", PortableMobTypes.MASTER.getItem());

        this.advancement("playercaptureadvancement")
                .hidden()
                .icon(Items.BARRIER)
                .announceToChat(true)
                .description("portablemobs.advancement.playercaptureadvancement.desc")
                .title("portablemobs.advancement.playercaptureadvancement.title")
                .taskFrame()
                .parent("basiccelladvancement")
                .criterion("playertrigger", new PortableMobsSimpleTrigger.TriggerInstance(new ResourceLocation("portablemobs", "playercapture"), EntityPredicate.AndPredicate.ANY));

        this.advancement("captureadvancement")
                .icon(Items.IRON_BARS)
                .announceToChat(true)
                .description("portablemobs.advancement.captureadvancement.desc")
                .title("portablemobs.advancement.captureadvancement.desc")
                .taskFrame()
                .parent("basiccelladvancement")
                .criterion("capture", new PortableMobsSimpleTrigger.TriggerInstance(new ResourceLocation("portablemobs", "capture"), EntityPredicate.AndPredicate.ANY));
    }
}

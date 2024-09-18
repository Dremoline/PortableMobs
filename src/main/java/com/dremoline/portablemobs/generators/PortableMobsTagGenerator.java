package com.dremoline.portablemobs.generators;

import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.core.generator.TagGenerator;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalEntityTypeTags;
import net.minecraft.world.entity.EntityType;

public class PortableMobsTagGenerator extends TagGenerator {
    public PortableMobsTagGenerator(ResourceCache cache) {
        super("portablemobs", cache);
    }

    @Override
    public void generate() {
        this.entityTag("capture_blacklist").addReference(ConventionalEntityTypeTags.BOSSES).addReference(ConventionalEntityTypeTags.CAPTURING_NOT_SUPPORTED);
    }
}

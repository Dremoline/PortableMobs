package com.dremoline.portablemobs.generators;

import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.core.generator.TagGenerator;
import net.minecraft.entity.EntityType;

public class PortableMobsTagGenerator extends TagGenerator {
    public PortableMobsTagGenerator(ResourceCache cache) {
        super("portablemobs", cache);
    }

    @Override
    public void generate() {
        this.entityTag("forge","bosses").add(EntityType.ENDER_DRAGON).add(EntityType.WITHER);
        this.entityTag("capture_blacklist").addReference("forge", "bosses");
    }
}

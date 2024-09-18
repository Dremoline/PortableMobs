package com.dremoline.portablemobs.generators;

import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.core.generator.TagGenerator;
import net.minecraft.world.entity.EntityType;

public class PortableMobsTagGenerator extends TagGenerator {
    public PortableMobsTagGenerator(ResourceCache cache) {
        super("portablemobs", cache);
    }

    @Override
    public void generate() {
        this.entityTag("c","bosses").add(EntityType.ENDER_DRAGON).add(EntityType.WITHER);
        this.entityTag("c", "teleporting_not_supported");
        this.entityTag("capture_blacklist").addReference("c", "bosses").addReference("c", "teleporting_not_supported");
    }
}

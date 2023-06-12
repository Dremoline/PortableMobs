package com.dremoline.portablemobs.generators;

import com.dremoline.portablemobs.PortableMobTypes;
import com.supermartijn642.core.generator.RecipeGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import net.minecraft.world.item.Items;

public class PortableMobsRecipeGenerator extends RecipeGenerator {
    public PortableMobsRecipeGenerator(ResourceCache cache) {
        super("portablemobs", cache);
    }

    @Override
    public void generate() {
        this.shaped(PortableMobTypes.BASIC.getItem())
                .pattern("ABA")
                .pattern("BCB")
                .pattern("ABA")
                .input('A', Items.STICK)
                .input('B', Items.IRON_BARS)
                .input('C', Items.CHEST)
                .unlockedBy(Items.STICK);
        this.shaped(PortableMobTypes.MASTER.getItem())
                .pattern("ABA")
                .pattern("BCB")
                .pattern("ABA")
                .input('A', Items.OBSIDIAN)
                .input('B', Items.IRON_BARS)
                .input('C', PortableMobTypes.BASIC.getItem())
                .unlockedBy(PortableMobTypes.BASIC.getItem());
    }
}

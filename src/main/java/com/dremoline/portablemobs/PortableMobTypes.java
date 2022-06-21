package com.dremoline.portablemobs;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Locale;

public enum PortableMobTypes {

    BASIC(false), MASTER(true);

    public final boolean reusable;

    private Item item;

    PortableMobTypes(boolean reusable) {
        this.reusable = reusable;
    }

    public void registerItem(IForgeRegistry<Item> registry) {
        this.item = new PortableMobItem(this);
        registry.register(this.toSuffix() + "_capture_cell", this.item);
    }

    public Item getItem() {
        return this.item;
    }

    public String toSuffix() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}

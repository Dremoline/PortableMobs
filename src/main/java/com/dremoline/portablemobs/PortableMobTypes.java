package com.dremoline.portablemobs;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;

import java.util.Locale;

public enum PortableMobTypes {

    BASIC(false), MASTER(true);

    public final boolean reusable;

    private Item item;

    PortableMobTypes(boolean reusable) {
        this.reusable = reusable;
    }

    public void registerItem(RegistryEvent.Register<Item> e) {
        this.item = new PortableMobItem(this);
        e.getRegistry().register(this.item);
    }

    public Item getItem() {
        return this.item;
    }

    public String toSuffix() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}

package com.dremoline.portablemobs;

import com.supermartijn642.core.registry.RegistrationHandler;
import net.minecraft.world.item.Item;

import java.util.Locale;

public enum PortableMobTypes {

    BASIC(false), MASTER(true);

    public final boolean reusable;

    private Item item;

    PortableMobTypes(boolean reusable) {
        this.reusable = reusable;
    }

    public void registerItem(RegistrationHandler.Helper<Item> helper) {
        this.item = new PortableMobItem(this);
        helper.register(this.toSuffix() + "_capture_cell", this.item);
    }

    public Item getItem() {
        return this.item;
    }

    public String toSuffix() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}

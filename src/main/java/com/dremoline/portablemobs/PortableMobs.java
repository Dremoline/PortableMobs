package com.dremoline.portablemobs;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
@Mod("portablemobs")
public class PortableMobs {

    public static final ItemGroup GROUP = new ItemGroup("portablemobs") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(PortableMobTypes.BASIC.getItem());
        }
    };

    public PortableMobs() {
        PortableMobsConfig.init();
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {

        @SubscribeEvent
        public static void onItemRegistry(final RegistryEvent.Register<Item> e) {
            for (PortableMobTypes type : PortableMobTypes.values())
                type.registerItem(e);
        }
    }

}

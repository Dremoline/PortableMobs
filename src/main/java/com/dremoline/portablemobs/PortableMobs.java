package com.dremoline.portablemobs;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
@Mod("portablemobs")
public class PortableMobs {

    public static final CreativeModeTab GROUP = new CreativeModeTab("portablemobs") {
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

        @SubscribeEvent
        public static void onRecipeRegistry(final RegistryEvent.Register<RecipeSerializer<?>> e){
            e.getRegistry().register(PortableMobUpgradeRecipe.SERIALIZER.setRegistryName(new ResourceLocation("portablemobs", "upgrade_capture_cell")));
        }
    }

}

package com.dremoline.portablemobs;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;

import java.util.Objects;

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
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {

        @SubscribeEvent
        public static void onRegisterEvent(RegisterEvent e) {
            if (e.getRegistryKey().equals(ForgeRegistries.Keys.ITEMS))
                onItemRegistry(Objects.requireNonNull(e.getForgeRegistry()));
            else if (e.getRegistryKey().equals(ForgeRegistries.Keys.RECIPE_SERIALIZERS))
                onRecipeRegistry(Objects.requireNonNull(e.getForgeRegistry()));
        }

        public static void onItemRegistry(IForgeRegistry<Item> registry) {
            for (PortableMobTypes type : PortableMobTypes.values())
                type.registerItem(registry);
        }

        public static void onRecipeRegistry(IForgeRegistry<RecipeSerializer<?>> registry) {
            registry.register("upgrade_capture_cell", PortableMobUpgradeRecipe.SERIALIZER);
        }
    }

}

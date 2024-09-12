package com.dremoline.portablemobs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;

/**
 * Created 7/19/2021 by SuperMartijn642
 */
public class PortableMobUpgradeRecipe extends ShapedRecipe {

    public static final RecipeSerializer<PortableMobUpgradeRecipe> SERIALIZER = new PortableMobUpgradeRecipe.Serializer();

    private final String group;
    private final CraftingBookCategory category;
    private final ShapedRecipePattern pattern;
    private final ItemStack result;
    private final boolean showNotification;

    public PortableMobUpgradeRecipe(String group, CraftingBookCategory category, ShapedRecipePattern pattern, ItemStack output, boolean showNotification) {
        super(group, category, pattern, output, showNotification);
        this.group = group;
        this.category = category;
        this.pattern = pattern;
        this.result = output;
        this.showNotification = showNotification;
    }

    @Override
    public ItemStack assemble(CraftingContainer inv, HolderLookup.Provider provider) {
        CompoundTag compound = null;
        loop:
        for (int i = 0; i < inv.getHeight(); i++) {
            for (int j = 0; j < inv.getWidth(); j++) {
                ItemStack stack = inv.getItem(i * inv.getWidth() + j);
                if (stack.getItem() instanceof PortableMobItem) {
                    compound = stack.get(PortableMobItem.CAPTURED_ENTITY);
                    if (compound != null)
                        break loop;
                }
            }
        }

        if (compound != null) {
            ItemStack result = this.getResultItem(provider).copy();
            result.set(PortableMobItem.CAPTURED_ENTITY, compound);
            return result;
        }

        return super.assemble(inv, provider);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    public static class Serializer implements RecipeSerializer<PortableMobUpgradeRecipe> {

        private static final MapCodec<PortableMobUpgradeRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        Codec.STRING.optionalFieldOf("group", "").forGetter(recipe -> recipe.group),
                        CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(recipe -> recipe.category),
                        ShapedRecipePattern.MAP_CODEC.forGetter(recipe -> recipe.pattern),
                        ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
                        Codec.BOOL.optionalFieldOf("show_notification", true).forGetter(recipe -> recipe.showNotification)
                ).apply(instance, PortableMobUpgradeRecipe::new));

        @Override
        public MapCodec<PortableMobUpgradeRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, PortableMobUpgradeRecipe> streamCodec() {
            return StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);
        }

        public static PortableMobUpgradeRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            return fromShapedRecipe(ShapedRecipe.Serializer.fromNetwork(buffer));
        }

        public static void toNetwork(RegistryFriendlyByteBuf buffer, PortableMobUpgradeRecipe recipe) {
            ShapedRecipe.Serializer.toNetwork(buffer, recipe);
        }

        private static PortableMobUpgradeRecipe fromShapedRecipe(ShapedRecipe recipe) {
            return new PortableMobUpgradeRecipe(recipe.getGroup(), recipe.category(), recipe.pattern, recipe.getResultItem(null), recipe.showNotification());
        }
    }
}

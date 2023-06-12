package com.dremoline.portablemobs;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

import javax.annotation.Nullable;

/**
 * Created 7/19/2021 by SuperMartijn642
 */
public class PortableMobUpgradeRecipe extends ShapedRecipe {

    public static final RecipeSerializer<PortableMobUpgradeRecipe> SERIALIZER = new PortableMobUpgradeRecipe.Serializer();

    public PortableMobUpgradeRecipe(ResourceLocation location, String group, CraftingBookCategory category, int recipeWidth, int recipeHeight, NonNullList<Ingredient> ingredients, ItemStack output, boolean showNotification) {
        super(location, group, category, recipeWidth, recipeHeight, ingredients, output, showNotification);
    }

    @Override
    public ItemStack assemble(CraftingContainer inv, RegistryAccess registryAccess) {
        CompoundTag compound = null;
        loop:
        for (int i = 0; i < inv.getHeight(); i++) {
            for (int j = 0; j < inv.getWidth(); j++) {
                ItemStack stack = inv.getItem(i * inv.getWidth() + j);
                if (stack.hasTag() && stack.getItem() instanceof PortableMobItem) {
                    compound = stack.getTag();
                    break loop;
                }
            }
        }

        if (compound != null) {
            ItemStack result = this.getResultItem(registryAccess).copy();
            result.getOrCreateTag().merge(compound);
            return result;
        }

        return super.assemble(inv, registryAccess);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    public static class Serializer implements RecipeSerializer<PortableMobUpgradeRecipe> {

        @Override
        public PortableMobUpgradeRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            ShapedRecipe recipe = RecipeSerializer.SHAPED_RECIPE.fromJson(recipeId, json);
            return new PortableMobUpgradeRecipe(recipeId, recipe.getGroup(), recipe.category(), recipe.getRecipeWidth(), recipe.getRecipeHeight(), recipe.getIngredients(), recipe.getResultItem(null), recipe.showNotification());
        }

        @Nullable
        @Override
        public PortableMobUpgradeRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            ShapedRecipe recipe = RecipeSerializer.SHAPED_RECIPE.fromNetwork(recipeId, buffer);
            return new PortableMobUpgradeRecipe(recipeId, recipe.getGroup(), recipe.category(), recipe.getRecipeWidth(), recipe.getRecipeHeight(), recipe.getIngredients(), recipe.getResultItem(null), recipe.showNotification());
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, PortableMobUpgradeRecipe recipe) {
            RecipeSerializer.SHAPED_RECIPE.toNetwork(buffer, recipe);
        }
    }
}

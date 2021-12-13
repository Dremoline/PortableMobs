package com.dremoline.portablemobs;

import com.google.gson.JsonObject;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

/**
 * Created 7/19/2021 by SuperMartijn642
 */
public class PortableMobUpgradeRecipe extends ShapedRecipe {

    public static final IRecipeSerializer<PortableMobUpgradeRecipe> SERIALIZER = new PortableMobUpgradeRecipe.Serializer();

    public PortableMobUpgradeRecipe(ResourceLocation location, String group, int recipeWidth, int recipeHeight, NonNullList<Ingredient> ingredients, ItemStack output){
        super(location, group, recipeWidth, recipeHeight, ingredients, output);
    }

    @Override
    public ItemStack assemble(CraftingInventory inv){
        CompoundNBT compound = null;
        loop:
        for(int i = 0; i < inv.getHeight(); i++){
            for(int j = 0; j < inv.getWidth(); j++){
                ItemStack stack = inv.getItem(i * inv.getWidth() + j);
                if(stack.hasTag() && stack.getItem() instanceof PortableMobItem){
                    compound = stack.getTag();
                    break loop;
                }
            }
        }

        if(compound != null){
            ItemStack result = this.getResultItem().copy();
            result.getOrCreateTag().merge(compound);
            return result;
        }

        return super.assemble(inv);
    }

    @Override
    public IRecipeSerializer<?> getSerializer(){
        return SERIALIZER;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<PortableMobUpgradeRecipe> {

        @Override
        public PortableMobUpgradeRecipe fromJson(ResourceLocation recipeId, JsonObject json){
            ShapedRecipe recipe = IRecipeSerializer.SHAPED_RECIPE.fromJson(recipeId, json);
            return new PortableMobUpgradeRecipe(recipeId, recipe.getGroup(), recipe.getRecipeWidth(), recipe.getRecipeHeight(), recipe.getIngredients(), recipe.getResultItem());
        }

        @Nullable
        @Override
        public PortableMobUpgradeRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer){
            ShapedRecipe recipe = IRecipeSerializer.SHAPED_RECIPE.fromNetwork(recipeId, buffer);
            return new PortableMobUpgradeRecipe(recipeId, recipe.getGroup(), recipe.getRecipeWidth(), recipe.getRecipeHeight(), recipe.getIngredients(), recipe.getResultItem());
        }

        @Override
        public void toNetwork(PacketBuffer buffer, PortableMobUpgradeRecipe recipe){
            IRecipeSerializer.SHAPED_RECIPE.toNetwork(buffer, recipe);
        }
    }
}

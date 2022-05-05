package com.mraof.minestuck.item.crafting;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class NonMirroredRecipe extends ShapedRecipe
{
    public NonMirroredRecipe(ResourceLocation id, String group, int width, int height, NonNullList<Ingredient> ingredients, ItemStack result)
    {
        super(id, group, width, height, ingredients, result);
    }
    public RecipeSerializer<?> getSerializer() {
        return MSRecipeTypes.NON_MIRRORED;
    }

    @Override
    public boolean matches(CraftingContainer inv, Level level)
    {
        for (int i = 0; i <= 3 - this.getRecipeWidth(); ++i)
            for (int j = 0; j <= 3 - this.getRecipeHeight(); ++j)
                if (this.checkMatch(inv, i, j))
                    return true;

        return false;
    }

    protected boolean checkMatch(CraftingContainer inv, int x, int y)
    {
        for (int invX = 0; invX < 3; invX++) {
            for (int invY = 0; invY < 3; invY++) {
                int posX = invX - x;
                int posY = invY - y;
                Ingredient ingredient = Ingredient.EMPTY;

                if (posX >= 0 && posY >= 0 && posX < this.getRecipeWidth() && posY < this.getRecipeHeight()) {
                    ingredient = this.getIngredients().get(posX + posY * this.getRecipeWidth());
                }

                if (!ingredient.test(inv.getItem(invX + invY * inv.getWidth()))) {
                    return false;
                }
            }
        }
        return true;
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>>  implements RecipeSerializer<NonMirroredRecipe>
    {
        @Override
        public NonMirroredRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            ShapedRecipe recipe = RecipeSerializer.SHAPED_RECIPE.fromJson(recipeId, json);
            
            return new NonMirroredRecipe(recipe.getId(), recipe.getGroup(), recipe.getRecipeWidth(), recipe.getRecipeHeight(), recipe.getIngredients(), recipe.getResultItem());
        }
        @Override
        public NonMirroredRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            ShapedRecipe recipe = RecipeSerializer.SHAPED_RECIPE.fromNetwork(recipeId, buffer);
            return new NonMirroredRecipe(recipe.getId() , recipe.getGroup(), recipe.getRecipeWidth(), recipe.getRecipeHeight(), recipe.getIngredients(), recipe.getResultItem());
        }
        @Override
        public void toNetwork(FriendlyByteBuf buffer, NonMirroredRecipe recipe) {
            RecipeSerializer.SHAPED_RECIPE.toNetwork(buffer, recipe);
        }
    }
}


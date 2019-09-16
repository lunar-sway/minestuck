package com.mraof.minestuck.item.crafting;

import com.google.gson.JsonObject;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class NonMirroredRecipe extends ShapedRecipe
{
    public NonMirroredRecipe(ResourceLocation id, String group, int width, int height, NonNullList<Ingredient> ingredients, ItemStack result)
    {
        super(id, group, width, height, ingredients, result);
    }
    public IRecipeSerializer<?> getSerializer() {
        return MSRecipeTypes.NON_MIRRORED;
    }

    @Override
    public boolean matches(CraftingInventory inv, World world)
    {
        for (int i = 0; i <= 3 - this.getRecipeWidth(); ++i)
            for (int j = 0; j <= 3 - this.getRecipeHeight(); ++j)
                if (this.checkMatch(inv, i, j))
                    return true;

        return false;
    }

    protected boolean checkMatch(CraftingInventory inv, int x, int y)
    {
        for (int invX = 0; invX < 3; invX++) {
            for (int invY = 0; invY < 3; invY++) {
                int posX = invX - x;
                int posY = invY - y;
                Ingredient ingredient = Ingredient.EMPTY;

                if (posX >= 0 && posY >= 0 && posX < this.getRecipeWidth() && posY < this.getRecipeHeight()) {
                    ingredient = this.getIngredients().get(posX + posY * this.getRecipeWidth());
                }

                if (!ingredient.test(inv.getStackInSlot(invX + invY * inv.getWidth()))) {
                    return false;
                }
            }
        }
        return true;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>>  implements IRecipeSerializer<NonMirroredRecipe>
    {
        private static final ResourceLocation NAME = new ResourceLocation("minestuck", "non_mirrored");
        @Override
        public NonMirroredRecipe read(ResourceLocation recipeId, JsonObject json) {
            ShapedRecipe recipe = IRecipeSerializer.CRAFTING_SHAPED.read(recipeId, json);
            return new NonMirroredRecipe(recipe.getId(), recipe.getGroup(), recipe.getRecipeWidth(), recipe.getRecipeHeight(), recipe.getIngredients(), recipe.getRecipeOutput());
        }
        @Override
        public NonMirroredRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            ShapedRecipe recipe = IRecipeSerializer.CRAFTING_SHAPED.read(recipeId, buffer);
            return new NonMirroredRecipe(recipe.getId() , recipe.getGroup(), recipe.getRecipeWidth(), recipe.getRecipeHeight(), recipe.getIngredients(), recipe.getRecipeOutput());
        }
        @Override
        public void write(PacketBuffer buffer, NonMirroredRecipe recipe) {
            IRecipeSerializer.CRAFTING_SHAPED.write(buffer, recipe);
        }
    }
}


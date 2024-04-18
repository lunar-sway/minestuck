package com.mraof.minestuck.item.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.fml.util.ObfuscationReflectionHelper;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class NonMirroredRecipe extends ShapedRecipe
{
    public NonMirroredRecipe(ShapedRecipe recipe)
    {
        this(recipe.getGroup(), recipe.category(), getPattern(recipe), recipe.getResultItem(null), recipe.showNotification());
    }
    
    public NonMirroredRecipe(String group, CraftingBookCategory category, ShapedRecipePattern pattern, ItemStack result, boolean showNotification)
    {
        super(group, category, pattern, result, showNotification);
    }
    public RecipeSerializer<?> getSerializer() {
        return MSRecipeTypes.NON_MIRRORED.get();
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
    
    private static ShapedRecipePattern getPattern(ShapedRecipe recipe)
    {
        ShapedRecipePattern pattern = ObfuscationReflectionHelper.getPrivateValue(ShapedRecipe.class, recipe, "pattern");
        return Objects.requireNonNull(pattern);
    }
    
    @SuppressWarnings("DataFlowIssue")
    public static class Serializer implements RecipeSerializer<NonMirroredRecipe>
    {
        private static final Codec<NonMirroredRecipe> CODEC = ((MapCodec.MapCodecCodec<ShapedRecipe>) ShapedRecipe.Serializer.CODEC).codec().xmap(
				NonMirroredRecipe::new,
                recipe -> new ShapedRecipe(recipe.getGroup(), recipe.category(), getPattern(recipe), recipe.getResultItem(null), recipe.showNotification())).codec();
        
        @Override
        public Codec<NonMirroredRecipe> codec()
        {
            return CODEC;
        }
        
        @Override
        public NonMirroredRecipe fromNetwork(FriendlyByteBuf buffer) {
            ShapedRecipe recipe = RecipeSerializer.SHAPED_RECIPE.fromNetwork(buffer);
            return new NonMirroredRecipe(recipe);
        }
        @Override
        public void toNetwork(FriendlyByteBuf buffer, NonMirroredRecipe recipe) {
            RecipeSerializer.SHAPED_RECIPE.toNetwork(buffer, recipe);
        }
    }
}


package com.mraof.minestuck.item.crafting;

import com.mojang.serialization.MapCodec;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.fml.util.ObfuscationReflectionHelper;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.function.Function;

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
    public boolean matches(CraftingInput input, Level level)
    {
        for (int i = 0; i <= 3 - this.getWidth(); ++i)
            for (int j = 0; j <= 3 - this.getHeight(); ++j)
                if (this.checkMatch(input, i, j))
                    return true;

        return false;
    }

    protected boolean checkMatch(CraftingInput input, int x, int y)
    {
        for (int invX = 0; invX < 3; invX++) {
            for (int invY = 0; invY < 3; invY++) {
                int posX = invX - x;
                int posY = invY - y;
                Ingredient ingredient = Ingredient.EMPTY;

                if (posX >= 0 && posY >= 0 && posX < this.getWidth() && posY < this.getHeight()) {
                    ingredient = this.getIngredients().get(posX + posY * this.getWidth());
                }

                if (!ingredient.test(input.getItem(invX + invY * input.width()))) {
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
        private static final MapCodec<NonMirroredRecipe> CODEC = ShapedRecipe.Serializer.CODEC.xmap(
				NonMirroredRecipe::new,
                recipe -> new ShapedRecipe(recipe.getGroup(), recipe.category(), getPattern(recipe), recipe.getResultItem(null), recipe.showNotification()));
        private static final StreamCodec<RegistryFriendlyByteBuf, NonMirroredRecipe> STREAM_CODEC = RecipeSerializer.SHAPED_RECIPE.streamCodec().map(NonMirroredRecipe::new, Function.identity());
        
        @Override
        public MapCodec<NonMirroredRecipe> codec()
        {
            return CODEC;
        }
        
        @Override
        public StreamCodec<RegistryFriendlyByteBuf, NonMirroredRecipe> streamCodec()
        {
            return STREAM_CODEC;
        }
    }
}


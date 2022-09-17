package com.mraof.minestuck.jei;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.item.crafting.alchemy.AlchemyHelper;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mraof on 2017 January 23 at 6:50 AM.
 */
public class DesignixRecipeCategory implements IRecipeCategory<JeiCombination>
{
    private IDrawable background, icon;

    DesignixRecipeCategory(IGuiHelper guiHelper)
    {
        ResourceLocation punchDesignixBackground = new ResourceLocation("minestuck:textures/gui/designix.png");
        background = guiHelper.createDrawable(punchDesignixBackground, 43, 25, 94, 42);
		icon = guiHelper.createDrawableIngredient(new ItemStack(MSBlocks.PUNCH_DESIGNIX));
    }
	
	@Override
	public RecipeType<JeiCombination> getRecipeType()
	{
		return MinestuckJeiPlugin.DESIGNIX;
	}
	
	@SuppressWarnings("removal")
	@Override
	public Class<? extends JeiCombination> getRecipeClass()
	{
		return getRecipeType().getRecipeClass();
	}
	
	@SuppressWarnings("removal")
	@Override
	public ResourceLocation getUid()
	{
		return getRecipeType().getUid();
	}

	@Override
	public Component getTitle()
	{
		return new TranslatableComponent(MSBlocks.PUNCH_DESIGNIX.KEYBOARD.get().getDescriptionId());
	}

	@Override
	public IDrawable getBackground()
	{
		return background;
	}

	@Override
	public IDrawable getIcon()
	{
		return icon;
	}

	@Override
	public void setIngredients(JeiCombination jeiCombination, IIngredients ingredients)
	{
		ingredients.setInputLists(VanillaTypes.ITEM, Arrays.asList(Arrays.asList(jeiCombination.getInput1().getItems()), Arrays.asList(jeiCombination.getInput2().getItems())));
		ingredients.setOutput(VanillaTypes.ITEM, jeiCombination.getOutput());
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, JeiCombination jeiCombination, IIngredients ingredients)
	{
        IGuiItemStackGroup stackGroup = recipeLayout.getItemStacks();
        stackGroup.init(0, true, 0, 0);
        stackGroup.init(1, true, 0, 24);
        stackGroup.init(2, false, 72, 11);
        List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
        stackGroup.set(0, inputs.get(0));

        List<ItemStack> second = new ArrayList<>();
        for(ItemStack stack : inputs.get(1))
        {
            second.add(AlchemyHelper.createCard(stack, true));
        }
        stackGroup.set(1, second);
        List<ItemStack> outputs = new ArrayList<>(ingredients.getOutputs(VanillaTypes.ITEM).get(0));
        outputs.add(AlchemyHelper.createCard(outputs.get(0), true));
        stackGroup.set(2, outputs);
    }
}
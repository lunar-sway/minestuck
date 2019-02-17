package com.mraof.minestuck.jei;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.alchemy.AlchemyRecipes;
import com.mraof.minestuck.util.ColorCollector;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class UraniumCookerRecipeCategory implements IRecipeCategory<UraniumCookerRecipeWrapper>
{
	private IDrawable background;

	UraniumCookerRecipeCategory(IGuiHelper guiHelper)
	{
		ResourceLocation uraniumCookerBackground = new ResourceLocation("minestuck:textures/gui/uranium_cooker.png");
		background = guiHelper.createDrawable(uraniumCookerBackground, 25, 24, 130, 36);
	}
	
	@Override
	public String getModName()
	{
		return Minestuck.MOD_NAME;
	}
	
	@Override
	public String getUid()
	{
		return "minestuck.uraniumCooker";
	}

	@Override
	public String getTitle()
	{
		return I18n.format("tile.sburbMachine.uraniumCooker.name");
	}

	@Override
	public IDrawable getBackground()
	{
		return background;
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, UraniumCookerRecipeWrapper recipeWrapper, IIngredients ingredients)
	{
		IGuiItemStackGroup stackGroup = recipeLayout.getItemStacks();
		stackGroup.init(0, true, 0, 0);     //Card 1
		stackGroup.init(1, true, 0, 18);    //Card 2
		stackGroup.init(2, true, 36, 9);    //Dowel
		stackGroup.init(3, false, 107, 9);  //Result
		List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);
		List<ItemStack> first = new ArrayList<>();
		for(ItemStack stack : inputs.get(0))
		{
			first.add(AlchemyRecipes.createCard(stack, true));
		}
		stackGroup.set(0, first);

		List<ItemStack> second = new ArrayList<>();
		for(ItemStack stack : inputs.get(1))
		{
			second.add(AlchemyRecipes.createCard(stack, true));
		}
		stackGroup.set(1, second);
		
		stackGroup.set(2, new ItemStack(MinestuckItems.cruxiteDowel, 1, ColorCollector.playerColor + 1));
		
		List<ItemStack> outputs = new ArrayList<>(ingredients.getOutputs(ItemStack.class).get(0));
		ItemStack outputDowel = AlchemyRecipes.createEncodedItem(outputs.get(0), false);
		outputDowel.setItemDamage(ColorCollector.playerColor + 1);
		outputs.add(outputDowel);
		stackGroup.set(3, outputs);
	}
}

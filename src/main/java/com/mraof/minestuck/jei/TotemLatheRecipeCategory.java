package com.mraof.minestuck.jei;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.item.crafting.alchemy.AlchemyHelper;
import com.mraof.minestuck.util.ColorHandler;
import com.mraof.minestuck.player.ClientPlayerData;
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
public class TotemLatheRecipeCategory implements IRecipeCategory<JeiCombination>
{
	private IDrawable background, icon;

	TotemLatheRecipeCategory(IGuiHelper guiHelper)
	{
		ResourceLocation totemLatheBackground = new ResourceLocation("minestuck:textures/gui/totem_lathe.png");
		background = guiHelper.createDrawable(totemLatheBackground, 25, 24, 130, 36);
		icon = guiHelper.createDrawableIngredient(new ItemStack(MSBlocks.TOTEM_LATHE));
	}
	
	@Override
	public RecipeType<JeiCombination> getRecipeType()
	{
		return MinestuckJeiPlugin.LATHE;
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
		return new TranslatableComponent(MSBlocks.TOTEM_LATHE.MIDDLE.get().getDescriptionId());
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
		stackGroup.init(0, true, 0, 0);     //Card 1
		stackGroup.init(1, true, 0, 18);    //Card 2
		stackGroup.init(2, true, 36, 9);    //Dowel
		stackGroup.init(3, false, 107, 9);  //Result
		List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
		List<ItemStack> first = new ArrayList<>();
		for(ItemStack stack : inputs.get(0))
		{
			first.add(AlchemyHelper.createCard(stack, true));
		}
		stackGroup.set(0, first);

		List<ItemStack> second = new ArrayList<>();
		for(ItemStack stack : inputs.get(1))
		{
			second.add(AlchemyHelper.createCard(stack, true));
		}
		stackGroup.set(1, second);

		stackGroup.set(2, ColorHandler.setColor(new ItemStack(MSBlocks.CRUXITE_DOWEL.get()), ClientPlayerData.getPlayerColor()));

		List<ItemStack> outputs = new ArrayList<>(ingredients.getOutputs(VanillaTypes.ITEM).get(0));
		ItemStack outputDowel = ColorHandler.setColor(AlchemyHelper.createEncodedItem(outputs.get(0), false), ClientPlayerData.getPlayerColor());
		outputs.add(outputDowel);
		stackGroup.set(3, outputs);
	}
}
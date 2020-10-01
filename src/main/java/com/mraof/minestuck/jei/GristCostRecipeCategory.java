package com.mraof.minestuck.jei;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.client.util.GuiUtil;
import com.mraof.minestuck.item.crafting.alchemy.AlchemyHelper;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.item.crafting.alchemy.GristTypes;
import com.mraof.minestuck.util.ColorHandler;
import com.mraof.minestuck.world.storage.ClientPlayerData;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by mraof on 2017 January 23 at 2:38 AM.
 */
public class GristCostRecipeCategory implements IRecipeCategory<JeiGristCost>
{
	private IDrawable background, icon;

	GristCostRecipeCategory(IGuiHelper guiHelper)
	{
		ResourceLocation alchemiterBackground = new ResourceLocation("minestuck:textures/gui/alchemiter.png");
		background = guiHelper.createDrawable(alchemiterBackground, 8, 15, 160, 56);
		icon = guiHelper.createDrawableIngredient(new ItemStack(MSBlocks.ALCHEMITER));
	}

	@Override
	public Class<? extends JeiGristCost> getRecipeClass()
	{
		return JeiGristCost.class;
	}

	@Override
	public ResourceLocation getUid()
	{
		return MinestuckJeiPlugin.GRIST_COST_ID;
	}

	@Override
	public String getTitle()
	{
		return I18n.format(JeiGristCost.GRIST_COSTS);
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
	public void setIngredients(JeiGristCost recipe, IIngredients ingredients)
	{
		ingredients.setOutputLists(VanillaTypes.ITEM, Collections.singletonList(Arrays.asList(recipe.getIngredient().getMatchingStacks())));
		if(recipe.getType() == JeiGristCost.Type.GRIST_SET)
			ingredients.setInputs(MinestuckJeiPlugin.GRIST, recipe.getGristSet().getAmounts());
		//TODO Wildcard grist cost
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, JeiGristCost recipe, IIngredients ingredients)
	{
		recipeLayout.getItemStacks().init(0, true, 18, 4);
		recipeLayout.getItemStacks().init(1, false, 126, 4);
		Stream<ItemStack> inputDowels = ingredients.getOutputs(VanillaTypes.ITEM).get(0).stream();
		inputDowels = inputDowels.map(itemStack -> AlchemyHelper.createEncodedItem(itemStack, new ItemStack(MSBlocks.CRUXITE_DOWEL)));
		inputDowels = inputDowels.map(itemStack -> ColorHandler.setColor(itemStack, ClientPlayerData.getPlayerColor()));
		recipeLayout.getItemStacks().set(0, inputDowels.collect(Collectors.toList()));
		recipeLayout.getItemStacks().set(1, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
	}

	@Override
	public void draw(JeiGristCost recipe, double mouseX, double mouseY)
	{
		if(recipe.getType() == JeiGristCost.Type.GRIST_SET)
			GuiUtil.drawGristBoard(recipe.getGristSet(), GuiUtil.GristboardMode.ALCHEMITER, 1, 30, Minecraft.getInstance().fontRenderer);
		else if(recipe.getType() == JeiGristCost.Type.WILDCARD)
			GuiUtil.drawGristBoard(new GristSet(GristTypes.BUILD, recipe.getWildcardAmount()), GuiUtil.GristboardMode.JEI_WILDCARD, 1, 30, Minecraft.getInstance().fontRenderer);
	}

	@Override
	public List<String> getTooltipStrings(JeiGristCost recipe, double mouseX, double mouseY)
	{
		if(recipe.getType() == JeiGristCost.Type.GRIST_SET)
			return GuiUtil.getGristboardTooltip(recipe.getGristSet(), GuiUtil.GristboardMode.ALCHEMITER, mouseX, mouseY, 1, 30, Minecraft.getInstance().fontRenderer);
		else if(recipe.getType() == JeiGristCost.Type.WILDCARD)
			return GuiUtil.getGristboardTooltip(new GristSet(GristTypes.BUILD, recipe.getWildcardAmount()), GuiUtil.GristboardMode.JEI_WILDCARD, mouseX, mouseY, 1, 30, Minecraft.getInstance().fontRenderer);
		return IRecipeCategory.super.getTooltipStrings(recipe, mouseX, mouseY);
	}
}
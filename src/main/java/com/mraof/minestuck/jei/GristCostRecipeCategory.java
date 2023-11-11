package com.mraof.minestuck.jei;

import com.mraof.minestuck.alchemy.AlchemyHelper;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.GristTypes;
import com.mraof.minestuck.api.alchemy.recipe.JeiGristCost;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.client.util.GuiUtil;
import com.mraof.minestuck.player.ClientPlayerData;
import com.mraof.minestuck.util.ColorHandler;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by mraof on 2017 January 23 at 2:38 AM.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GristCostRecipeCategory implements IRecipeCategory<JeiGristCost>
{
	private final IDrawable background, icon;

	GristCostRecipeCategory(IGuiHelper guiHelper)
	{
		ResourceLocation alchemiterBackground = new ResourceLocation("minestuck:textures/gui/alchemiter.png");
		background = guiHelper.createDrawable(alchemiterBackground, 8, 15, 160, 56);
		icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(MSBlocks.ALCHEMITER));
	}
	
	@Override
	public RecipeType<JeiGristCost> getRecipeType()
	{
		return MinestuckJeiPlugin.GRIST_COST;
	}
	
	@Override
	public Component getTitle()
	{
		return Component.translatable(JeiGristCost.GRIST_COSTS);
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
	public void setRecipe(IRecipeLayoutBuilder builder, JeiGristCost recipe, IFocusGroup focuses)
	{
		builder.addSlot(RecipeIngredientRole.CATALYST, 19, 5)
				.addItemStacks(Arrays.stream(recipe.ingredient().getItems())
						.map(itemStack -> AlchemyHelper.createEncodedItem(itemStack, new ItemStack(MSBlocks.CRUXITE_DOWEL.get())))
						.map(itemStack -> ColorHandler.setColor(itemStack, ClientPlayerData.getPlayerColor())).toList());
		builder.addSlot(RecipeIngredientRole.OUTPUT, 127, 5)
				.addIngredients(recipe.ingredient());
		
		if(recipe instanceof JeiGristCost.Set gristSetRecipe)
			builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addIngredients(MinestuckJeiPlugin.GRIST, gristSetRecipe.gristSet().asAmounts());
		//TODO Wildcard grist cost
	}
	
	@Override
	public void draw(JeiGristCost recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY)
	{
		if(recipe instanceof JeiGristCost.Set gristSetRecipe)
			GuiUtil.drawGristBoard(guiGraphics, gristSetRecipe.gristSet(), GuiUtil.GristboardMode.ALCHEMITER, 1, 30, Minecraft.getInstance().font);
		else if(recipe instanceof JeiGristCost.Wildcard wildcardRecipe)
			GuiUtil.drawGristBoard(guiGraphics, GristTypes.BUILD.get().amount(wildcardRecipe.wildcardAmount()), GuiUtil.GristboardMode.JEI_WILDCARD, 1, 30, Minecraft.getInstance().font);
	}
	
	
	@Override
	public List<Component> getTooltipStrings(JeiGristCost recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY)
	{
		Component text = null;
		if(recipe instanceof JeiGristCost.Set gristSetRecipe)
			text = GuiUtil.getGristboardTooltip(gristSetRecipe.gristSet(), GuiUtil.GristboardMode.ALCHEMITER, mouseX, mouseY, 1, 30, Minecraft.getInstance().font);
		else if(recipe instanceof JeiGristCost.Wildcard wildcardRecipe)
			text = GuiUtil.getGristboardTooltip(GristTypes.BUILD.get().amount(wildcardRecipe.wildcardAmount()), GuiUtil.GristboardMode.JEI_WILDCARD, mouseX, mouseY, 1, 30, Minecraft.getInstance().font);
		
		if(text != null)
			return Collections.singletonList(text);
		else
			return Collections.emptyList();
	}
}
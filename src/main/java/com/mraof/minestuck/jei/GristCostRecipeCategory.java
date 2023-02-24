package com.mraof.minestuck.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.client.util.GuiUtil;
import com.mraof.minestuck.alchemy.AlchemyHelper;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristTypes;
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
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by mraof on 2017 January 23 at 2:38 AM.
 */
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
	
	@SuppressWarnings("removal")
	@Override
	public Class<? extends JeiGristCost> getRecipeClass()
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
		return new TranslatableComponent(JeiGristCost.GRIST_COSTS);
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
				.addItemStacks(Arrays.stream(recipe.getIngredient().getItems())
						.map(itemStack -> AlchemyHelper.createEncodedItem(itemStack, new ItemStack(MSBlocks.CRUXITE_DOWEL.get())))
						.map(itemStack -> ColorHandler.setColor(itemStack, ClientPlayerData.getPlayerColor())).toList());
		builder.addSlot(RecipeIngredientRole.OUTPUT, 127, 5)
				.addIngredients(recipe.getIngredient());
		
		if(recipe.getType() == JeiGristCost.Type.GRIST_SET)
			builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addIngredients(MinestuckJeiPlugin.GRIST, recipe.getGristSet().getAmounts());
		//TODO Wildcard grist cost
	}
	
	@Override
	public void draw(JeiGristCost recipe, IRecipeSlotsView recipeSlotsView, PoseStack poseStack, double mouseX, double mouseY)
	{
		if(recipe.getType() == JeiGristCost.Type.GRIST_SET)
			GuiUtil.drawGristBoard(poseStack, recipe.getGristSet(), GuiUtil.GristboardMode.ALCHEMITER, 1, 30, Minecraft.getInstance().font);
		else if(recipe.getType() == JeiGristCost.Type.WILDCARD)
			GuiUtil.drawGristBoard(poseStack, new GristSet(GristTypes.BUILD, recipe.getWildcardAmount()), GuiUtil.GristboardMode.JEI_WILDCARD, 1, 30, Minecraft.getInstance().font);
	}
	
	
	@Override
	public List<Component> getTooltipStrings(JeiGristCost recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY)
	{
		Component text = null;
		if(recipe.getType() == JeiGristCost.Type.GRIST_SET)
			text = GuiUtil.getGristboardTooltip(recipe.getGristSet(), GuiUtil.GristboardMode.ALCHEMITER, mouseX, mouseY, 1, 30, Minecraft.getInstance().font);
		else if(recipe.getType() == JeiGristCost.Type.WILDCARD)
			text = GuiUtil.getGristboardTooltip(new GristSet(GristTypes.BUILD, recipe.getWildcardAmount()), GuiUtil.GristboardMode.JEI_WILDCARD, mouseX, mouseY, 1, 30, Minecraft.getInstance().font);
		
		if(text != null)
			return Collections.singletonList(text);
		else
			return Collections.emptyList();
	}
}
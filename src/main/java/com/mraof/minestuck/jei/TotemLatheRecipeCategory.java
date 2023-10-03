package com.mraof.minestuck.jei;

import com.mraof.minestuck.api.alchemy.recipe.combination.JeiCombination;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.alchemy.AlchemyHelper;
import com.mraof.minestuck.player.ClientPlayerData;
import com.mraof.minestuck.util.ColorHandler;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Created by mraof on 2017 January 23 at 6:50 AM.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TotemLatheRecipeCategory implements IRecipeCategory<JeiCombination>
{
	private final IDrawable background, icon;

	TotemLatheRecipeCategory(IGuiHelper guiHelper)
	{
		ResourceLocation totemLatheBackground = new ResourceLocation("minestuck:textures/gui/totem_lathe.png");
		background = guiHelper.createDrawable(totemLatheBackground, 25, 24, 130, 36);
		icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(MSBlocks.TOTEM_LATHE));
	}
	
	@Override
	public RecipeType<JeiCombination> getRecipeType()
	{
		return MinestuckJeiPlugin.LATHE;
	}
	
	@Override
	public Component getTitle()
	{
		return Component.translatable(MSBlocks.TOTEM_LATHE.MIDDLE.get().getDescriptionId());
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
	public void setRecipe(IRecipeLayoutBuilder builder, JeiCombination recipe, IFocusGroup focuses)
	{
		builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(recipe.input1());
		builder.addSlot(RecipeIngredientRole.INPUT, 1, 19).addIngredients(recipe.input2());
		builder.addSlot(RecipeIngredientRole.INPUT, 37, 10)
				.addItemStack(ColorHandler.setColor(new ItemStack(MSBlocks.CRUXITE_DOWEL.get()), ClientPlayerData.getPlayerColor()));
		builder.addSlot(RecipeIngredientRole.OUTPUT, 108, 10).addItemStack(recipe.output())
				.addItemStack(ColorHandler.setColor(AlchemyHelper.createEncodedItem(recipe.output(), false), ClientPlayerData.getPlayerColor()));
	}
}
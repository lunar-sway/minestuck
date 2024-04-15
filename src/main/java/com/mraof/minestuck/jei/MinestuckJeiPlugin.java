package com.mraof.minestuck.jei;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.api.alchemy.GristAmount;
import com.mraof.minestuck.api.alchemy.recipe.JeiGristCost;
import com.mraof.minestuck.api.alchemy.recipe.combination.CombinationMode;
import com.mraof.minestuck.api.alchemy.recipe.combination.JeiCombination;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;

import java.util.Objects;

/**
 * Created by mraof on 2017 January 23 at 2:11 AM.
 */
@JeiPlugin
public class MinestuckJeiPlugin implements IModPlugin
{
	public static final RecipeType<JeiGristCost> GRIST_COST = RecipeType.create(Minestuck.MOD_ID, "grist_cost", JeiGristCost.class);
	public static final RecipeType<JeiCombination> LATHE = RecipeType.create(Minestuck.MOD_ID, "totem_lathe", JeiCombination.class);
	public static final RecipeType<JeiCombination> DESIGNIX = RecipeType.create(Minestuck.MOD_ID, "punch_designix", JeiCombination.class);
	
	public static final IIngredientType<GristAmount> GRIST = () -> GristAmount.class;
	
	@Override
	public ResourceLocation getPluginUid()
	{
		return new ResourceLocation(Minestuck.MOD_ID, "minestuck");
	}
	
	@Override
	public void registerItemSubtypes(ISubtypeRegistration subtypeRegistry)
	{
		subtypeRegistry.useNbtForSubtypes(MSItems.CAPTCHA_CARD.get());
		subtypeRegistry.useNbtForSubtypes(MSBlocks.CRUXITE_DOWEL.get().asItem());
	}
	
	@Override
	public void registerIngredients(IModIngredientRegistration registry)
	{
		registry.register(GRIST, GristIngredientHelper.createList(), new GristIngredientHelper(), new GristIngredientRenderer());
	}
	
	@Override
	public void registerCategories(IRecipeCategoryRegistration registry)
	{
		GristCostRecipeCategory alchemiterCategory = new GristCostRecipeCategory(registry.getJeiHelpers().getGuiHelper());
		registry.addRecipeCategories(alchemiterCategory);
		TotemLatheRecipeCategory totemLatheCategory = new TotemLatheRecipeCategory(registry.getJeiHelpers().getGuiHelper());
		registry.addRecipeCategories(totemLatheCategory);
		DesignixRecipeCategory designixCategory = new DesignixRecipeCategory(registry.getJeiHelpers().getGuiHelper());
		registry.addRecipeCategories(designixCategory);
	}
	
	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration)
	{
		registration.addRecipeCatalyst(new ItemStack(MSBlocks.ALCHEMITER), GRIST_COST);
		registration.addRecipeCatalyst(new ItemStack(MSBlocks.MINI_ALCHEMITER.get()), GRIST_COST);
		registration.addRecipeCatalyst(new ItemStack(MSBlocks.TOTEM_LATHE), LATHE);
		registration.addRecipeCatalyst(new ItemStack(MSBlocks.MINI_TOTEM_LATHE.get()), LATHE);
		registration.addRecipeCatalyst(new ItemStack(MSBlocks.PUNCH_DESIGNIX), DESIGNIX);
		registration.addRecipeCatalyst(new ItemStack(MSBlocks.MINI_PUNCH_DESIGNIX.get()), DESIGNIX);
		registration.addRecipeCatalyst(new ItemStack(MSBlocks.CRUXITE_DOWEL.get()), GRIST_COST, LATHE);
		registration.addRecipeCatalyst(new ItemStack(MSItems.CAPTCHA_CARD.get()), LATHE, DESIGNIX);
	}
	
	@Override
	public void registerRecipes(IRecipeRegistration registration)
	{
		Level level = Minecraft.getInstance().level;
		RecipeManager recipeManager = Objects.requireNonNull(level).getRecipeManager();
		registration.addRecipes(GRIST_COST, recipeManager.getAllRecipesFor(MSRecipeTypes.GRIST_COST_TYPE.get()).stream()
				.flatMap(holder -> holder.value().getJeiCosts(level).stream()).toList());
		registration.addRecipes(LATHE, recipeManager.getAllRecipesFor(MSRecipeTypes.COMBINATION_TYPE.get()).stream()
				.flatMap(holder -> holder.value().getJeiCombinations().stream())
				.filter(combination -> combination.mode() == CombinationMode.AND).toList());
		registration.addRecipes(DESIGNIX, recipeManager.getAllRecipesFor(MSRecipeTypes.COMBINATION_TYPE.get()).stream()
				.flatMap(holder -> holder.value().getJeiCombinations().stream())
				.filter(combination -> combination.mode() == CombinationMode.OR).toList());
	}
}
package com.mraof.minestuck.jei;

//import com.mraof.minestuck.Minestuck;
//import com.mraof.minestuck.block.MSBlocks;
//import com.mraof.minestuck.item.MSItems;
//import com.mraof.minestuck.item.crafting.MSRecipeTypes;
//import com.mraof.minestuck.item.crafting.alchemy.CombinationMode;
//import com.mraof.minestuck.item.crafting.alchemy.CombinationRecipe;
//import com.mraof.minestuck.item.crafting.alchemy.GristAmount;
//import com.mraof.minestuck.item.crafting.alchemy.GristCostRecipe;
//import mezz.jei.api.IModPlugin;
//import mezz.jei.api.JeiPlugin;
//import mezz.jei.api.ingredients.IIngredientType;
//import mezz.jei.api.registration.*;
//import net.minecraft.client.Minecraft;
//import net.minecraft.item.ItemStack;
//import net.minecraft.item.crafting.IRecipe;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.world.World;
//
//import java.util.Collection;
//import java.util.stream.Collectors;
//
///**
// * Created by mraof on 2017 January 23 at 2:11 AM.
// */
//@JeiPlugin
//public class MinestuckJeiPlugin implements IModPlugin
//{
//	public static final ResourceLocation PLUGIN_ID = new ResourceLocation(Minestuck.MOD_ID, "minestuck");
//	public static final ResourceLocation GRIST_COST_ID = new ResourceLocation(Minestuck.MOD_ID, "grist_cost");
//	public static final ResourceLocation LATHE_ID = new ResourceLocation(Minestuck.MOD_ID, "totem_lathe");
//	public static final ResourceLocation DESIGNIX_ID = new ResourceLocation(Minestuck.MOD_ID, "punch_designix");
//
//	public static final IIngredientType<GristAmount> GRIST = () -> GristAmount.class;
//
//	@Override
//	public ResourceLocation getPluginUid()
//	{
//		return PLUGIN_ID;
//	}
//
//	@Override
//	public void registerItemSubtypes(ISubtypeRegistration subtypeRegistry)
//    {
//        subtypeRegistry.useNbtForSubtypes(MSItems.CAPTCHA_CARD);
//        subtypeRegistry.useNbtForSubtypes(MSBlocks.CRUXITE_DOWEL.asItem());
//    }
//
//	@Override
//    public void registerIngredients(IModIngredientRegistration registry)
//    {
//    	registry.register(GRIST, GristIngredientHelper.createList(), new GristIngredientHelper(), new GristIngredientRenderer());
//    }
//
//	@Override
//	public void registerCategories(IRecipeCategoryRegistration registry)
//	{
//		GristCostRecipeCategory alchemiterCategory = new GristCostRecipeCategory(registry.getJeiHelpers().getGuiHelper());
//		registry.addRecipeCategories(alchemiterCategory);
//		TotemLatheRecipeCategory totemLatheCategory = new TotemLatheRecipeCategory(registry.getJeiHelpers().getGuiHelper());
//		registry.addRecipeCategories(totemLatheCategory);
//		DesignixRecipeCategory designixCategory = new DesignixRecipeCategory(registry.getJeiHelpers().getGuiHelper());
//		registry.addRecipeCategories(designixCategory);
//	}
//
//	@Override
//	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration)
//	{
//		registration.addRecipeCatalyst(new ItemStack(MSBlocks.ALCHEMITER), GRIST_COST_ID);
//		registration.addRecipeCatalyst(new ItemStack(MSBlocks.MINI_ALCHEMITER), GRIST_COST_ID);
//		registration.addRecipeCatalyst(new ItemStack(MSBlocks.TOTEM_LATHE), LATHE_ID);
//		registration.addRecipeCatalyst(new ItemStack(MSBlocks.MINI_TOTEM_LATHE), LATHE_ID);
//		registration.addRecipeCatalyst(new ItemStack(MSBlocks.PUNCH_DESIGNIX), DESIGNIX_ID);
//		registration.addRecipeCatalyst(new ItemStack(MSBlocks.MINI_PUNCH_DESIGNIX), DESIGNIX_ID);
//		registration.addRecipeCatalyst(new ItemStack(MSBlocks.CRUXITE_DOWEL), GRIST_COST_ID, LATHE_ID);
//		registration.addRecipeCatalyst(new ItemStack(MSItems.CAPTCHA_CARD), LATHE_ID, DESIGNIX_ID);
//	}
//
//	@Override
//	public void registerRecipes(IRecipeRegistration registration)
//	{
//		World world = Minecraft.getInstance().world;
//		Collection<IRecipe<?>> recipes = world.getRecipeManager().getRecipes();
//		registration.addRecipes(recipes.stream().filter(recipe -> recipe.getType() == MSRecipeTypes.GRIST_COST_TYPE).flatMap(recipe -> ((GristCostRecipe) recipe).getJeiCosts(world).stream()).collect(Collectors.toList()), GRIST_COST_ID);
//		registration.addRecipes(recipes.stream().filter(recipe -> recipe.getType() == MSRecipeTypes.COMBINATION_TYPE).flatMap(recipe -> ((CombinationRecipe) recipe).getJeiCombinations().stream()).filter(combination -> combination.getMode() == CombinationMode.AND).collect(Collectors.toList()), LATHE_ID);
//		registration.addRecipes(recipes.stream().filter(recipe -> recipe.getType() == MSRecipeTypes.COMBINATION_TYPE).flatMap(recipe -> ((CombinationRecipe) recipe).getJeiCombinations().stream()).filter(combination -> combination.getMode() == CombinationMode.OR).collect(Collectors.toList()), DESIGNIX_ID);
//	}
//}
package com.mraof.minestuck.jei;

import mezz.jei.api.IItemRegistry;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.IRecipeRegistry;
import mezz.jei.api.JEIPlugin;

@JEIPlugin
public class JEIPluginManager implements IModPlugin {
    public static IJeiHelpers jeiHelper;

    @Override
    public void onJeiHelpersAvailable(IJeiHelpers jeiHelpers)
    {
        jeiHelper = jeiHelpers;
    }

    @Override
    public void register(IModRegistry registry)
    {
    	registry.addRecipeCategories(new AlchemiterRecipeCategory());
    	registry.addRecipeHandlers(new AlchemiterRecipeHandler());
    	registry.addRecipes(AlchemiterRecipeMaker.getRecipes());

        registry.addRecipeCategories(new CombinationRecipeCategory());
    	registry.addRecipeHandlers(new CombinationRecipeHandler());
    	registry.addRecipes(CombinationRecipeMaker.getRecipes());
    }

    @Override
    public void onItemRegistryAvailable(IItemRegistry itemRegistry)
    {

    }

    @Override
    public void onRecipeRegistryAvailable(IRecipeRegistry recipeRegistry)
    {

    }
}
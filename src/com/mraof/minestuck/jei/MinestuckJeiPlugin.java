package com.mraof.minestuck.jei;
/*
import com.mraof.minestuck.alchemy.*;
import com.mraof.minestuck.block.BlockSburbMachine;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.Debug;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by mraof on 2017 January 23 at 2:11 AM.
 * /
@JEIPlugin
public class MinestuckJeiPlugin implements IModPlugin
{
	AlchemiterRecipeCategory alchemiterCategory;
	TotemLatheRecipeCategory totemLatheCategory;
	DesignixRecipeCategory designixCategory;
    @Override
    public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry)
    {
        subtypeRegistry.useNbtForSubtypes(MinestuckItems.captchaCard);
        subtypeRegistry.useNbtForSubtypes(MinestuckItems.cruxiteDowel);
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registry)
    {
    	registry.register(GristAmount.class, GristIngredientHelper.createList(), new GristIngredientHelper(), new GristIngredientRenderer());
    }
	
	@Override
	public void registerCategories(IRecipeCategoryRegistration registry)
	{
		alchemiterCategory = new AlchemiterRecipeCategory(registry.getJeiHelpers().getGuiHelper());
		registry.addRecipeCategories(alchemiterCategory);
		totemLatheCategory = new TotemLatheRecipeCategory(registry.getJeiHelpers().getGuiHelper());
		registry.addRecipeCategories(totemLatheCategory);
		designixCategory = new DesignixRecipeCategory(registry.getJeiHelpers().getGuiHelper());
		registry.addRecipeCategories(designixCategory);
	}
	
	@Override
    public void register(IModRegistry registry)
    {
        ArrayList<AlchemiterRecipeWrapper> alchemiterRecipes = new ArrayList<>();
        for(Map.Entry<List<Object>, GristSet> entry : AlchemyCostRegistry.getAllConversions().entrySet())
        {
            for(ItemStack stack : getItemStacks(entry.getKey().get(0), (Integer) entry.getKey().get(1)))
            {
                alchemiterRecipes.add(new AlchemiterRecipeWrapper(stack, entry.getValue()));
            }
        }
        registry.addRecipes(alchemiterRecipes, alchemiterCategory.getUid());
        registry.addRecipeCatalyst(new ItemStack(MinestuckBlocks.sburbMachine, 1, BlockSburbMachine.MachineType.ALCHEMITER.ordinal()), alchemiterCategory.getUid());

        ArrayList<PunchCardRecipeWrapper> latheRecipes = new ArrayList<>();
		ArrayList<PunchCardRecipeWrapper> designixRecipes = new ArrayList<>();
        for(Map.Entry<List<Object>, ItemStack> entry : CombinationRegistry.getAllConversions().entrySet())
        {
            List<ItemStack> firstStacks = getItemStacks(entry.getKey().get(0), (Integer) entry.getKey().get(1));
            List<ItemStack> secondStacks = getItemStacks(entry.getKey().get(2), (Integer) entry.getKey().get(3));
            if(!(firstStacks.isEmpty() || secondStacks.isEmpty()))
            {
                if(entry.getKey().get(4) == CombinationRegistry.Mode.MODE_AND)
                {
					latheRecipes.add(new TotemLatheRecipeWrapper(firstStacks, secondStacks, entry.getValue()));
                }
                else
                {
					designixRecipes.add(new DesignixRecipeWrapper(firstStacks, secondStacks, entry.getValue()));
                }
            }
        }

        Debug.info("Adding " +  (latheRecipes.size() + designixRecipes.size()) + " punch card recipes to the jei plugin");
        registry.addRecipes(latheRecipes, totemLatheCategory.getUid());
        registry.addRecipes(designixRecipes, designixCategory.getUid());
        registry.addRecipeCatalyst(new ItemStack(MinestuckBlocks.sburbMachine, 1, BlockSburbMachine.MachineType.TOTEM_LATHE.ordinal()), totemLatheCategory.getUid());
        registry.addRecipeCatalyst(new ItemStack(MinestuckBlocks.sburbMachine, 1, BlockSburbMachine.MachineType.PUNCH_DESIGNIX.ordinal()), designixCategory.getUid());
    }

    private List<ItemStack> getItemStacks(Object item, int metadata)
    {
        ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
        
        if(item instanceof Item)
        {
            stacks.add(new ItemStack((Item) item, 1, metadata));
        }
        else if(item instanceof Block)
        {
            stacks.add(new ItemStack((Block) item, 1, metadata));
        }
        else if(item instanceof String)
        {
            for(ItemStack stack : OreDictionary.getOres((String) item))
            {
                stacks.add(stack);
            }
        }
        return stacks;
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime)
    {

    }
}
*/
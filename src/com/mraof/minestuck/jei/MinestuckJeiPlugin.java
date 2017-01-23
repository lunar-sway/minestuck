package com.mraof.minestuck.jei;

import com.mraof.minestuck.block.BlockSburbMachine;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.CombinationRegistry;
import com.mraof.minestuck.util.GristRegistry;
import com.mraof.minestuck.util.GristSet;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by mraof on 2017 January 23 at 2:11 AM.
 */
@JEIPlugin
public class MinestuckJeiPlugin implements IModPlugin
{
    @Override
    public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry)
    {
        subtypeRegistry.useNbtForSubtypes(MinestuckItems.captchaCard);
        subtypeRegistry.useNbtForSubtypes(MinestuckItems.cruxiteDowel);
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registry)
    {
    }

    @Override
    public void register(IModRegistry registry)
    {
        AlchemiterRecipeCategory alchemiterRecipeCategory = new AlchemiterRecipeCategory(registry.getJeiHelpers().getGuiHelper());
        registry.addRecipeCategories(alchemiterRecipeCategory);
        registry.addRecipeHandlers(alchemiterRecipeCategory);
        ArrayList<AlchemiterRecipeWrapper> alchemiterRecipes = new ArrayList<AlchemiterRecipeWrapper>();
        for(Map.Entry<List<Object>, GristSet> entry : GristRegistry.getAllConversions().entrySet())
        {
            for(ItemStack stack : getItemStacks(entry.getKey().get(0), (Integer) entry.getKey().get(1)))
            {
                alchemiterRecipes.add(new AlchemiterRecipeWrapper(stack, entry.getValue()));
            }
        }
        registry.addRecipes(alchemiterRecipes);
        registry.addRecipeCategoryCraftingItem(new ItemStack(MinestuckBlocks.sburbMachine, 1, BlockSburbMachine.MachineType.ALCHEMITER.ordinal()), "alchemiter");

        TotemLatheRecipeCategory totemLatheRecipeCategory = new TotemLatheRecipeCategory(registry.getJeiHelpers().getGuiHelper());
        registry.addRecipeCategories(totemLatheRecipeCategory);
        registry.addRecipeHandlers(totemLatheRecipeCategory);

        DesignixRecipeCategory designixRecipeCategory = new DesignixRecipeCategory(registry.getJeiHelpers().getGuiHelper());
        registry.addRecipeCategories(designixRecipeCategory);
        registry.addRecipeHandlers(designixRecipeCategory);

        ArrayList<PunchCardRecipeWrapper> punchCardRecipes = new ArrayList<PunchCardRecipeWrapper>();
        for(Map.Entry<List<Object>, ItemStack> entry : CombinationRegistry.getAllConversions().entrySet())
        {
            List<ItemStack> firstStacks = getItemStacks(entry.getKey().get(0), (Integer) entry.getKey().get(1));
            List<ItemStack> secondStacks = getItemStacks(entry.getKey().get(2), (Integer) entry.getKey().get(3));
            if(!(firstStacks.isEmpty() || secondStacks.isEmpty()))
            {
                if((Boolean) entry.getKey().get(4) == CombinationRegistry.MODE_AND)
                {
                    punchCardRecipes.add(new TotemLatheRecipeWrapper(firstStacks, secondStacks, entry.getValue()));
                }
                else
                {
                    punchCardRecipes.add(new DesignixRecipeWrapper(firstStacks, secondStacks, entry.getValue()));
                }
            }
        }

        System.out.println("Adding " +  punchCardRecipes.size() + " punch card recipes");
        registry.addRecipes(punchCardRecipes);
        registry.addRecipeCategoryCraftingItem(new ItemStack(MinestuckBlocks.sburbMachine, 1, BlockSburbMachine.MachineType.TOTEM_LATHE.ordinal()), "totemLathe");
        registry.addRecipeCategoryCraftingItem(new ItemStack(MinestuckBlocks.sburbMachine, 1, BlockSburbMachine.MachineType.PUNCH_DESIGNIX.ordinal()), "punchDesignix");
    }

    private List<ItemStack> getItemStacks(Object item, int metadata)
    {
        ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
        if(metadata == OreDictionary.WILDCARD_VALUE)
        {
            metadata = 0;
        }
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

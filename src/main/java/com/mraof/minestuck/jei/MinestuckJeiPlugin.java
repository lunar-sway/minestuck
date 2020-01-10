package com.mraof.minestuck.jei;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import com.mraof.minestuck.item.crafting.alchemy.GristAmount;
import com.mraof.minestuck.item.crafting.alchemy.GristCostRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.stream.Collectors;

/**
 * Created by mraof on 2017 January 23 at 2:11 AM.
 */
@JeiPlugin
public class MinestuckJeiPlugin implements IModPlugin
{
	public static final ResourceLocation PLUGIN_ID = new ResourceLocation(Minestuck.MOD_ID, "minestuck");
	public static final ResourceLocation GRIST_COST_ID = new ResourceLocation(Minestuck.MOD_ID, "grist_cost");
	
	public static final IIngredientType<GristAmount> GRIST = () -> GristAmount.class;
	
	private GristCostRecipeCategory alchemiterCategory;
	//TotemLatheRecipeCategory totemLatheCategory;
	//DesignixRecipeCategory designixCategory;
	
	
	@Override
	public ResourceLocation getPluginUid()
	{
		return PLUGIN_ID;
	}
	
	@Override
	public void registerItemSubtypes(ISubtypeRegistration subtypeRegistry)
    {
        subtypeRegistry.useNbtForSubtypes(MSItems.CAPTCHA_CARD);
        subtypeRegistry.useNbtForSubtypes(MSBlocks.CRUXITE_DOWEL.asItem());
    }
	
	@Override
    public void registerIngredients(IModIngredientRegistration registry)
    {
    	registry.register(GRIST, GristIngredientHelper.createList(), new GristIngredientHelper(), new GristIngredientRenderer());
    }
	
	@Override
	public void registerCategories(IRecipeCategoryRegistration registry)
	{
		alchemiterCategory = new GristCostRecipeCategory(registry.getJeiHelpers().getGuiHelper());
		registry.addRecipeCategories(alchemiterCategory);
		/*totemLatheCategory = new TotemLatheRecipeCategory(registry.getJeiHelpers().getGuiHelper());
		registry.addRecipeCategories(totemLatheCategory);
		designixCategory = new DesignixRecipeCategory(registry.getJeiHelpers().getGuiHelper());
		registry.addRecipeCategories(designixCategory);*/
	}
	
	@Override
	public void registerRecipes(IRecipeRegistration registration)
	{
		World world = Minecraft.getInstance().world;
		registration.addRecipes(world.getRecipeManager().getRecipes().stream().filter(recipe -> recipe.getType() == MSRecipeTypes.GRIST_COST_TYPE).flatMap(recipe -> ((GristCostRecipe) recipe).getJeiCosts(world).stream()).collect(Collectors.toList()), GRIST_COST_ID);
	}
	
	/*
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
    }*/
}
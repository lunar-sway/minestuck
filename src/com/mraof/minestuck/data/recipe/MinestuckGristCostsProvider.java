package com.mraof.minestuck.data.recipe;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.crafting.alchemy.GristType;
import com.mraof.minestuck.util.ExtraForgeTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class MinestuckGristCostsProvider extends RecipeProvider
{
	public MinestuckGristCostsProvider(DataGenerator generator)
	{
		super(generator);
	}
	
	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> recipeSaver)
	{
		GristCostRecipeBuilder.of(Tags.Items.STONE).grist(GristType.BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Tags.Items.COBBLESTONE).grist(GristType.BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GRAVEL).grist(GristType.BUILD, 3).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ItemTags.SAND).grist(GristType.BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ExtraForgeTags.Items.TERRACOTTA).grist(GristType.SHALE, 12).grist(GristType.MARBLE, 4).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.DIRT).grist(GristType.BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.GRASS_BLOCK).grist(GristType.BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.PODZOL).grist(GristType.BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(Items.MYCELIUM).grist(GristType.BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		
		GristCostRecipeBuilder.of(ItemTags.LOGS).grist(GristType.BUILD, 8).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ItemTags.LEAVES).grist(GristType.BUILD, 1).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ItemTags.SAPLINGS).grist(GristType.BUILD, 16).buildFor(recipeSaver, Minestuck.MOD_ID);
		GristCostRecipeBuilder.of(ItemTags.PLANKS).grist(GristType.BUILD, 2).buildFor(recipeSaver, Minestuck.MOD_ID);
		
	}
	
	@Override
	public String getName()
	{
		return "Minestuck Grist Costs";
	}
}

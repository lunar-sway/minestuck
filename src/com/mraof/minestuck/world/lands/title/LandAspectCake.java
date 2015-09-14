package com.mraof.minestuck.world.lands.title;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.world.lands.decorator.CakeDecorator;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

public class LandAspectCake extends TitleAspect
{
	
	@Override
	public String getPrimaryName()
	{
		return "Cake";
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {"cake", "dessert"};
	}
	
	@Override
	protected void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
		if(chunkProvider.decorators != null)
		{
			chunkProvider.decorators.add(new CakeDecorator());
			chunkProvider.sortDecorators();
			
			ChestGenHooks chestGen = chunkProvider.lootMap.get(AlchemyRecipeHandler.BASIC_MEDIUM_CHEST);
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(Items.cake, 1, 0), 1, 1, 6));	//Yes, of course
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(Items.cookie, 1, 0), 2, 8, 5));
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(Items.sugar, 1, 0), 1, 5, 4));
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(Items.milk_bucket, 1, 0), 1, 1, 3));
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(Minestuck.silverSpoon, 1, 0), 1, 1, 4));
		}
		
	}
	
}
package com.mraof.minestuck.world.lands.title;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;

import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.world.lands.decorator.structure.BucketDecorator;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

public class LandAspectBuckets extends TitleLandAspect	//Yes, buckets
{

	@Override
	public String getPrimaryName()
	{
		return "Buckets";
	}

	@Override
	public String[] getNames()
	{
		return new String[]{"bucket"};
	}
	
	@Override
	protected void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
		if(chunkProvider.decorators != null)
		{
			chunkProvider.decorators.add(new BucketDecorator());
			chunkProvider.sortDecorators();
			
			ChestGenHooks chestGen = chunkProvider.lootMap.get(AlchemyRecipeHandler.BASIC_MEDIUM_CHEST);
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(MinestuckItems.minestuckBucket, 1, 0), 1, 1, 3));
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(MinestuckItems.minestuckBucket, 1, 1), 1, 1, 3));
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(MinestuckItems.minestuckBucket, 1, 2), 1, 1, 3));
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(Items.lava_bucket, 1, 0), 1, 1, 3));
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(Items.water_bucket, 1, 0), 1, 1, 4));
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(Items.bucket, 1, 0), 1, 4, 5));
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(Items.milk_bucket, 1, 0), 1, 1, 3));
		}
	}
}
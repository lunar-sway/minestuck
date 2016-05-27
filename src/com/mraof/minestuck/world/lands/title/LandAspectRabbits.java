package com.mraof.minestuck.world.lands.title;

import net.minecraft.block.material.Material;

import com.mraof.minestuck.world.lands.decorator.RabbitSpawner;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;

public class LandAspectRabbits extends TitleLandAspect
{
	
	@Override
	public String getPrimaryName()
	{
		return "rabbits";
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {"rabbit", "bunny"};
	}
	
	@Override
	protected void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
		if(chunkProvider.decorators != null)
		{
			chunkProvider.decorators.add(new RabbitSpawner());
			chunkProvider.sortDecorators();
			
			/*ChestGenHooks chestGen = chunkProvider.lootMap.get(AlchemyRecipeHandler.BASIC_MEDIUM_CHEST);
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(Items.rabbit, 1, 0), 2, 8, 6));
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(Items.cooked_rabbit, 1, 0), 1, 5, 5));
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(Items.rabbit_hide, 1, 0), 2, 7, 4));
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(Items.rabbit_foot, 1, 0), 1, 2, 2));
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(Items.rabbit_stew, 1, 0), 1, 1, 2));
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(Items.carrot, 1, 0), 1, 3, 4));*/
		}
	}
	
	@Override
	public boolean isAspectCompatible(TerrainLandAspect aspect)
	{
		return !aspect.getOceanBlock().getMaterial().equals(Material.lava);
	}
	
}

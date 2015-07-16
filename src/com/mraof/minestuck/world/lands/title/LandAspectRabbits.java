package com.mraof.minestuck.world.lands.title;

import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;

import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.world.lands.decorator.RabbitSpawner;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.terrain.TerrainAspect;

public class LandAspectRabbits extends TitleAspect
{
	
	@Override
	public String getPrimaryName()
	{
		return "Rabbits";
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
			
			ChestGenHooks chestGen = chunkProvider.lootMap.get(AlchemyRecipeHandler.BASIC_MEDIUM_CHEST);
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(Items.rabbit, 1, 0), 2, 8, 6));
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(Items.cooked_rabbit, 1, 0), 1, 5, 5));
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(Items.rabbit_hide, 1, 0), 2, 7, 4));
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(Items.rabbit_foot, 1, 0), 1, 2, 2));
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(Items.rabbit_stew, 1, 0), 1, 1, 2));
		}
	}
	
	@Override
	public boolean isAspectCompatible(TerrainAspect aspect)
	{
		return !aspect.getOceanBlock().getBlock().getMaterial().equals(Material.lava);
	}
	
}

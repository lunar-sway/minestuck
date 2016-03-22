package com.mraof.minestuck.world.lands.title;

import static com.mraof.minestuck.item.MinestuckItems.pogoHammer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;

import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.world.lands.decorator.RockDecorator;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.gen.DefaultTerrainGen;

public class LandAspectWind extends TitleLandAspect
{
	
	@Override
	public String getPrimaryName()
	{
		return "Wind";
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {"wind"};
	}
	
	@Override
	protected void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
		if(chunkProvider.weatherType == -1)
			chunkProvider.weatherType = 0;
		if(chunkProvider.decorators != null)
		{
			chunkProvider.decorators.add(new RockDecorator());
			if(chunkProvider.terrainGenerator instanceof DefaultTerrainGen)
				((DefaultTerrainGen) chunkProvider.terrainGenerator).normalVariation *= 0.6F;
			
			ChestGenHooks chestGen = chunkProvider.lootMap.get(AlchemyRecipeHandler.BASIC_MEDIUM_CHEST);
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(pogoHammer, 1, 0), 1, 1, 2));
		}
		
		chunkProvider.mergeFogColor(new Vec3(0.1, 0.2, 0.8), 0.3F);
	}
	
}

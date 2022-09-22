package com.mraof.minestuck.world.lands;

import com.mraof.minestuck.item.crafting.alchemy.GristType;
import com.mraof.minestuck.item.crafting.alchemy.GristTypes;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import net.minecraft.world.level.levelgen.WorldgenRandom;

import javax.annotation.Nullable;
import java.util.List;

public class GristTypeLayer
{
	private final List<WeightedEntry.Wrapper<GristType>> gristTypes;
	private final int weightSum;
	@Nullable
	private final GristType baseType;
	
	private final PositionalRandomFactory selectionRandom;
	
	private GristTypeLayer(GristType.SpawnCategory category, int index, long seed, @Nullable GristType baseType)
	{
		gristTypes = GristTypes.values().stream().filter(GristType::isUnderlingType)
				.filter(gristType -> gristType.isInCategory(category))
				.map(type -> WeightedEntry.wrap(type, Math.round(type.getRarity() * 100))).toList();
		this.baseType = baseType;
		weightSum = WeightedRandom.getTotalWeight(gristTypes);
		
		selectionRandom = WorldgenRandom.Algorithm.XOROSHIRO.newInstance(seed).forkPositional()
				.fromHashOf("minestuck:grist_layer_" + index).forkPositional();
	}
	
	public static GristTypeLayer createLayer(GristType.SpawnCategory category, int index, long seed, int zoomLevel, @Nullable GristType baseType)
	{
		return new GristTypeLayer(category, index, seed, baseType);
	}
	
	public GristType getTypeAt(int posX, int posZ)
	{
		return pickGristWithoutZoom(Math.floorDiv(posX, 8), Math.floorDiv(posZ, 8));
	}
	
	private GristType pickGristWithoutZoom(int x, int z)
	{
		if(baseType != null && x * x + z * z <= 1)
			return baseType;
		
		return WeightedRandom.getWeightedItem(gristTypes, selectionRandom.at(x, 0, z).nextInt(weightSum)).orElseThrow().getData();
	}
}
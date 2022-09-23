package com.mraof.minestuck.world.lands;

import com.mraof.minestuck.item.crafting.alchemy.GristType;
import com.mraof.minestuck.item.crafting.alchemy.GristTypes;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import javax.annotation.Nullable;
import java.util.List;

public class GristTypeLayer
{
	private final List<WeightedEntry.Wrapper<GristType>> gristTypes;
	private final int weightSum;
	@Nullable
	private final GristType baseType;
	private final int areaSize;
	
	private final NormalNoise xShift, zShift;
	private final PositionalRandomFactory selectionRandom;
	
	private GristTypeLayer(GristType.SpawnCategory category, int index, long seed, int zoomLevel, @Nullable GristType baseType)
	{
		this.gristTypes = GristTypes.values().stream().filter(GristType::isUnderlingType)
				.filter(gristType -> gristType.isInCategory(category))
				.map(type -> WeightedEntry.wrap(type, Math.round(type.getRarity() * 100))).toList();
		this.weightSum = WeightedRandom.getTotalWeight(gristTypes);
		this.baseType = baseType;
		this.areaSize = 1 << zoomLevel;
		
		PositionalRandomFactory randomFactory = WorldgenRandom.Algorithm.XOROSHIRO.newInstance(seed).forkPositional();
		this.xShift = NormalNoise.create(randomFactory.fromHashOf("minestuck:grist_x_shift_" + index), -6, 1);
		this.zShift = NormalNoise.create(randomFactory.fromHashOf("minestuck:grist_z_shift_" + index), -6, 1);
		this.selectionRandom = randomFactory.fromHashOf("minestuck:grist_layer_" + index).forkPositional();
	}
	
	public static GristTypeLayer createLayer(GristType.SpawnCategory category, int index, long seed, int zoomLevel, @Nullable GristType baseType)
	{
		return new GristTypeLayer(category, index, seed, zoomLevel, baseType);
	}
	
	public GristType getTypeAt(int posX, int posZ)
	{
		double shiftedX = posX/(double) areaSize + 0.2 * this.xShift.getValue(posX, 0, posZ);
		double shiftedZ = posZ/(double) areaSize + 0.2 * this.zShift.getValue(posX, 0, posZ);
		return pickGristWithoutZoom((int) Math.round(shiftedX), (int) Math.round(shiftedZ));
	}
	
	private GristType pickGristWithoutZoom(int x, int z)
	{
		if(baseType != null && x * x + z * z <= 1)
			return baseType;
		
		return WeightedRandom.getWeightedItem(gristTypes, selectionRandom.at(x, 0, z).nextInt(weightSum)).orElseThrow().getData();
	}
}
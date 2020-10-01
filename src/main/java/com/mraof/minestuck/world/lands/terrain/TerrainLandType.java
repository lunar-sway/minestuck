package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.world.gen.feature.structure.village.ConsortVillageCenter;
import com.mraof.minestuck.world.gen.feature.structure.village.NakagatorVillagePieces;
import com.mraof.minestuck.world.gen.feature.structure.village.SalamanderVillagePieces;
import com.mraof.minestuck.world.gen.feature.structure.village.TurtleVillagePieces;
import com.mraof.minestuck.world.lands.ILandType;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.function.BiConsumer;

public abstract class TerrainLandType extends ForgeRegistryEntry<TerrainLandType> implements ILandType<TerrainLandType>
{
	private final ResourceLocation groupName;
	private final boolean pickedAtRandom;
	
	protected TerrainLandType()
	{
		this(null, true);
	}
	
	protected TerrainLandType(boolean pickedAtRandom)
	{
		this(null, pickedAtRandom);
	}
	
	protected TerrainLandType(ResourceLocation groupName)
	{
		this(groupName, true);
	}
	
	protected TerrainLandType(ResourceLocation groupName, boolean pickedAtRandom)
	{
		this.groupName = groupName;
		this.pickedAtRandom = pickedAtRandom;
	}
	
	public float getSkylightBase()
	{
		return 1F;
	}
	
	public abstract Vec3d getFogColor();
	
	public Vec3d getSkyColor()
	{
		return new Vec3d(0, 0, 0);
	}
	
	@Override
	public boolean canBePickedAtRandom()
	{
		return pickedAtRandom;
	}
	
	@Override
	public ResourceLocation getGroup()
	{
		if(groupName == null)
			return this.getRegistryName();
		else return groupName;
	}
	
	public abstract EntityType<? extends ConsortEntity> getConsortType();
	
	public static void addSalamanderVillageCenters(BiConsumer<ConsortVillageCenter.CenterFactory, Integer> factoryWeightConsumer)
	{
		factoryWeightConsumer.accept(SalamanderVillagePieces.RuinedTowerMushroomCenter::new, 5);
	}
	public static void addTurtleVillageCenters(BiConsumer<ConsortVillageCenter.CenterFactory, Integer> factoryWeightConsumer)
	{
		factoryWeightConsumer.accept(TurtleVillagePieces.TurtleWellCenter::new, 5);
	}
	public static void addNakagatorVillageCenters(BiConsumer<ConsortVillageCenter.CenterFactory, Integer> factoryWeightConsumer)
	{
		factoryWeightConsumer.accept(NakagatorVillagePieces.RadioTowerCenter::new, 5);
	}
	public static void addIguanaVillageCenters(BiConsumer<ConsortVillageCenter.CenterFactory, Integer> factoryWeightConsumer)
	{
	}
}
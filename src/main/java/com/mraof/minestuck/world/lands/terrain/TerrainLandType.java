package com.mraof.minestuck.world.lands.terrain;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.util.CodecUtil;
import com.mraof.minestuck.world.biome.LandBiomeSet;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.gen.feature.structure.village.IguanaVillagePieces;
import com.mraof.minestuck.world.gen.feature.structure.village.NakagatorVillagePieces;
import com.mraof.minestuck.world.gen.feature.structure.village.SalamanderVillagePieces;
import com.mraof.minestuck.world.gen.feature.structure.village.TurtleVillagePieces;
import com.mraof.minestuck.world.lands.ILandType;
import com.mraof.minestuck.world.lands.LandTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.Random;

/**
 * Base class for land types that make up the base of a land.
 */
public abstract class TerrainLandType extends ForgeRegistryEntry<TerrainLandType> implements ILandType<TerrainLandType>
{
	public static final Codec<TerrainLandType> CODEC = CodecUtil.registryCodec(() -> LandTypes.TERRAIN_REGISTRY);
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
	
	public abstract Vector3d getFogColor();
	
	public Vector3d getSkyColor()
	{
		return new Vector3d(0, 0, 0);
	}
	
	@Override
	public final boolean canBePickedAtRandom()
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
	
	public LandBiomeSet getBiomeSet()
	{
		return MSBiomes.DEFAULT_LAND;
	}
	
	/**
	 * Helper function for adding village centers associated with salamanders.
	 */
	public static void addSalamanderVillageCenters(CenterRegister register)
	{
		register.add(SalamanderVillagePieces.RuinedTowerMushroomCenter::new, 5);
	}
	/**
	 * Helper function for adding village centers associated with turtles.
	 */
	public static void addTurtleVillageCenters(CenterRegister register)
	{
		register.add(TurtleVillagePieces.TurtleWellCenter::new, 5);
	}
	/**
	 * Helper function for adding village centers associated with nakagators.
	 */
	public static void addNakagatorVillageCenters(CenterRegister register)
	{
		register.add(NakagatorVillagePieces.RadioTowerCenter::new, 5);
	}
	/**
	 * Helper function for adding village centers associated with iguanas.
	 */
	public static void addIguanaVillageCenters(CenterRegister register)
	{
	}
	/**
	 * Helper function for adding village pieces associated with salamanders.
	 */
	public static void addSalamanderVillagePieces(PieceRegister register, Random random)
	{
		register.add(SalamanderVillagePieces.PipeHouse1::createPiece, 3, MathHelper.nextInt(random, 5, 8));
		register.add(SalamanderVillagePieces.HighPipeHouse1::createPiece, 6, MathHelper.nextInt(random, 2, 4));
		register.add(SalamanderVillagePieces.SmallTowerStore::createPiece, 10, MathHelper.nextInt(random, 1, 3));
	}
	/**
	 * Helper function for adding village pieces associated with turtles.
	 */
	public static void addTurtleVillagePieces(PieceRegister register, Random random)
	{
		register.add(TurtleVillagePieces.ShellHouse1::createPiece, 3, MathHelper.nextInt(random, 5, 8));
		register.add(TurtleVillagePieces.TurtleMarket1::createPiece, 10, MathHelper.nextInt(random, 0, 2));
		register.add(TurtleVillagePieces.TurtleTemple1::createPiece, 10, MathHelper.nextInt(random, 1, 1));
	}
	/**
	 * Helper function for adding village pieces associated with nakagators.
	 */
	public static void addNakagatorVillagePieces(PieceRegister register, Random random)
	{
		register.add(NakagatorVillagePieces.HighNakHousing1::createPiece, 6, MathHelper.nextInt(random, 3, 5));
		register.add(NakagatorVillagePieces.HighNakMarket1::createPiece, 10, MathHelper.nextInt(random, 1, 2));
		register.add(NakagatorVillagePieces.HighNakInn1::createPiece, 15, MathHelper.nextInt(random, 1, 1));
	}
	/**
	 * Helper function for adding village pieces associated with iguanas.
	 */
	public static void addIguanaVillagePieces(PieceRegister register, Random random)
	{
		register.add(IguanaVillagePieces.SmallTent1::createPiece, 3, MathHelper.nextInt(random, 5, 8));
		register.add(IguanaVillagePieces.LargeTent1::createPiece, 10, MathHelper.nextInt(random, 1, 2));
		register.add(IguanaVillagePieces.SmallTentStore::createPiece, 8, MathHelper.nextInt(random, 2, 3));
	}
}
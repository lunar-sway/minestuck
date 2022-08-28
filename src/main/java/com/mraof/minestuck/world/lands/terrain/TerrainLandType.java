package com.mraof.minestuck.world.lands.terrain;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.util.CodecUtil;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeSet;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.gen.structure.village.IguanaVillagePieces;
import com.mraof.minestuck.world.gen.structure.village.NakagatorVillagePieces;
import com.mraof.minestuck.world.gen.structure.village.SalamanderVillagePieces;
import com.mraof.minestuck.world.gen.structure.village.TurtleVillagePieces;
import com.mraof.minestuck.world.lands.ILandType;
import com.mraof.minestuck.world.lands.LandTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.Objects;
import java.util.Random;
import java.util.function.Supplier;

/**
 * Base class for land types that make up the base of a land.
 */
public abstract class TerrainLandType extends ForgeRegistryEntry<TerrainLandType> implements ILandType<TerrainLandType>
{
	public static final Codec<TerrainLandType> CODEC = CodecUtil.registryCodec(() -> LandTypes.TERRAIN_REGISTRY);
	private final ResourceLocation groupName;
	private final boolean pickedAtRandom;
	private final String[] names;
	
	private final Supplier<EntityType<? extends ConsortEntity>> consortType;
	private final float skylightBase;
	private final Vec3 fogColor, skyColor;
	
	private final LandBiomeSet biomeSet;
	private final Biome.BiomeCategory biomeCategory;
	private final Supplier<SoundEvent> backgroundMusic;
	
	protected TerrainLandType(Builder builder)
	{
		this.groupName = builder.group;
		this.pickedAtRandom = builder.pickedAtRandom;
		this.names = Objects.requireNonNull(builder.names);
		
		this.consortType = builder.consortType;
		this.skylightBase = builder.skylightBase;
		this.fogColor = builder.fogColor;
		this.skyColor = builder.skyColor;
		this.biomeSet = builder.biomeSet;
		this.biomeCategory = builder.biomeCategory;
		this.backgroundMusic = builder.backgroundMusic;
	}
	
	public final float getSkylightBase()
	{
		return this.skylightBase;
	}
	
	public final Vec3 getFogColor()
	{
		return this.fogColor;
	}
	
	public final Vec3 getSkyColor()
	{
		return this.skyColor;
	}
	
	@Override
	public final boolean canBePickedAtRandom()
	{
		return pickedAtRandom;
	}
	
	@Override
	public final ResourceLocation getGroup()
	{
		if(groupName == null)
			return this.getRegistryName();
		else return groupName;
	}
	
	@Override
	public final String[] getNames()
	{
		return this.names;
	}
	
	public final EntityType<? extends ConsortEntity> getConsortType()
	{
		return this.consortType.get();
	}
	
	public final LandBiomeSet getBiomeSet()
	{
		return this.biomeSet;
	}
	
	public final Biome.BiomeCategory getBiomeCategory()
	{
		return this.biomeCategory;
	}
	
	@Override
	public final SoundEvent getBackgroundMusic()
	{
		return this.backgroundMusic.get();
	}
	
	public SurfaceRules.RuleSource getSurfaceRule(StructureBlockRegistry blocks)
	{
		ResourceKey<Biome> roughBiome = this.getBiomeSet().ROUGH;
		
		SurfaceRules.RuleSource surfaceBlock = SurfaceRules.sequence(
				SurfaceRules.ifTrue(SurfaceRules.isBiome(roughBiome), SurfaceRules.state(blocks.getBlockState("surface_rough"))),
				SurfaceRules.state(blocks.getBlockState("surface")));
		SurfaceRules.RuleSource surface = SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.ifTrue(SurfaceRules.waterBlockCheck(0, 0), surfaceBlock));
		
		SurfaceRules.RuleSource upper = SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, SurfaceRules.ifTrue(SurfaceRules.waterStartCheck(-6, -1), SurfaceRules.state(blocks.getBlockState("upper"))));
		// This surface rule targets the ocean surface by being positioned after "surface" and "upper", thus only placing blocks on surfaces where "surface" or "upper" doesn't
		SurfaceRules.RuleSource ocean_surface = SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.state(blocks.getBlockState("ocean_surface")));
		
		return SurfaceRules.sequence(surface, upper, ocean_surface);
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
		register.add(SalamanderVillagePieces.PipeHouse1::createPiece, 3, Mth.nextInt(random, 5, 8));
		register.add(SalamanderVillagePieces.HighPipeHouse1::createPiece, 6, Mth.nextInt(random, 2, 4));
		register.add(SalamanderVillagePieces.SmallTowerStore::createPiece, 10, Mth.nextInt(random, 1, 3));
	}
	/**
	 * Helper function for adding village pieces associated with turtles.
	 */
	public static void addTurtleVillagePieces(PieceRegister register, Random random)
	{
		register.add(TurtleVillagePieces.ShellHouse1::createPiece, 3, Mth.nextInt(random, 5, 8));
		register.add(TurtleVillagePieces.TurtleMarket1::createPiece, 10, Mth.nextInt(random, 0, 2));
		register.add(TurtleVillagePieces.TurtleTemple1::createPiece, 10, Mth.nextInt(random, 1, 1));
	}
	/**
	 * Helper function for adding village pieces associated with nakagators.
	 */
	public static void addNakagatorVillagePieces(PieceRegister register, Random random)
	{
		register.add(NakagatorVillagePieces.HighNakHousing1::createPiece, 6, Mth.nextInt(random, 3, 5));
		register.add(NakagatorVillagePieces.HighNakMarket1::createPiece, 10, Mth.nextInt(random, 1, 2));
		register.add(NakagatorVillagePieces.HighNakInn1::createPiece, 15, Mth.nextInt(random, 1, 1));
	}
	/**
	 * Helper function for adding village pieces associated with iguanas.
	 */
	public static void addIguanaVillagePieces(PieceRegister register, Random random)
	{
		register.add(IguanaVillagePieces.SmallTent1::createPiece, 3, Mth.nextInt(random, 5, 8));
		register.add(IguanaVillagePieces.LargeTent1::createPiece, 10, Mth.nextInt(random, 1, 2));
		register.add(IguanaVillagePieces.SmallTentStore::createPiece, 8, Mth.nextInt(random, 2, 3));
	}
	
	public static class Builder
	{
		private boolean pickedAtRandom = true;
		private ResourceLocation group = null;
		private String[] names;
		
		private final Supplier<EntityType<? extends ConsortEntity>> consortType;
		private float skylightBase = 1F;
		private Vec3 fogColor = new Vec3(0, 0, 0);
		private Vec3 skyColor = new Vec3(0, 0, 0);
		private LandBiomeSet biomeSet = MSBiomes.DEFAULT_LAND;
		private Biome.BiomeCategory biomeCategory = Biome.BiomeCategory.NONE;
		private Supplier<SoundEvent> backgroundMusic = () -> MSSoundEvents.MUSIC_DEFAULT;
		
		public Builder(Supplier<EntityType<? extends ConsortEntity>> consortType)
		{
			this.consortType = consortType;
		}
		
		public Builder unavailable()
		{
			this.pickedAtRandom = false;
			return this;
		}
		
		public Builder group(ResourceLocation group)
		{
			this.group = group;
			return this;
		}
		
		public Builder names(String... names)
		{
			this.names = names;
			return this;
		}
		
		public Builder skylight(float skylightBase)
		{
			this.skylightBase = skylightBase;
			return this;
		}
		
		public Builder fogColor(double r, double g, double b)
		{
			this.fogColor = new Vec3(r, g, b);
			return this;
		}
		
		public Builder skyColor(double r, double g, double b)
		{
			this.skyColor = new Vec3(r, g, b);
			return this;
		}
		
		public Builder biomeSet(LandBiomeSet biomeSet)
		{
			this.biomeSet = biomeSet;
			return this;
		}
		
		public Builder category(Biome.BiomeCategory biomeCategory)
		{
			this.biomeCategory = biomeCategory;
			return this;
		}
		
		public Builder music(Supplier<SoundEvent> backgroundMusic)
		{
			this.backgroundMusic = backgroundMusic;
			return this;
		}
	}
}
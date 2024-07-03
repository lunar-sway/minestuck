package com.mraof.minestuck.world.lands.terrain;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeSetType;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.gen.structure.MSStructures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.ILandType;
import com.mraof.minestuck.world.lands.LandBiomeGenBuilder;
import com.mraof.minestuck.world.lands.LandTypes;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Base class for land types that make up the base of a land.
 */
public abstract class TerrainLandType implements ILandType
{
	public static final Codec<TerrainLandType> CODEC = LandTypes.TERRAIN_REGISTRY.byNameCodec();
	
	protected static final RandomSpreadStructurePlacement SMALL_RUIN_PLACEMENT = new RandomSpreadStructurePlacement(16, 4, RandomSpreadType.LINEAR, 59273643);
	protected static final RandomSpreadStructurePlacement IMP_DUNGEON_PLACEMENT = new RandomSpreadStructurePlacement(16, 4, RandomSpreadType.LINEAR, 34527185);
	protected static final RandomSpreadStructurePlacement CONSORT_VILLAGE_PLACEMENT = new RandomSpreadStructurePlacement(24, 5, RandomSpreadType.LINEAR, 10387312);
	
	private final String[] names;
	
	private final Supplier<? extends EntityType<? extends ConsortEntity>> consortType;
	private final float skylightBase;
	private final Vec3 fogColor, skyColor;
	
	private final LandBiomeSetType biomeSet;
	private final Supplier<SoundEvent> backgroundMusic;
	
	protected TerrainLandType(Builder builder)
	{
		this.names = Objects.requireNonNull(builder.names);
		
		this.consortType = builder.consortType;
		this.skylightBase = builder.skylightBase;
		this.fogColor = builder.fogColor;
		this.skyColor = builder.skyColor;
		this.biomeSet = builder.biomeSet;
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
	public final String[] getNames()
	{
		return this.names;
	}
	
	public final EntityType<? extends ConsortEntity> getConsortType()
	{
		return this.consortType.get();
	}
	
	public final LandBiomeSetType getBiomeSet()
	{
		return this.biomeSet;
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
	
	public void addBiomeGeneration(LandBiomeGenBuilder builder, StructureBlockRegistry blocks)
	{}
	
	@Override
	public void addStructureSets(Consumer<StructureSet> consumer, HolderGetter<Structure> structureLookup)
	{
		consumer.accept(new StructureSet(structureLookup.getOrThrow(MSStructures.SMALL_RUIN), SMALL_RUIN_PLACEMENT));
		consumer.accept(new StructureSet(structureLookup.getOrThrow(MSStructures.ImpDungeon.KEY), IMP_DUNGEON_PLACEMENT));
		consumer.accept(new StructureSet(structureLookup.getOrThrow(MSStructures.ConsortVillage.KEY), CONSORT_VILLAGE_PLACEMENT));
	}
	
	public final boolean is(TagKey<TerrainLandType> tag)
	{
		return this.is(LandTypes.TERRAIN_REGISTRY.getOrCreateTag(tag));
	}
	
	public final boolean is(HolderSet<TerrainLandType> set)
	{
		return set.stream().anyMatch(holder -> holder.value() == this);
	}
	
	public static class Builder
	{
		private String[] names;
		
		private final Supplier<? extends EntityType<? extends ConsortEntity>> consortType;
		private float skylightBase = 1F;
		private Vec3 fogColor = new Vec3(0, 0, 0);
		private Vec3 skyColor = new Vec3(0, 0, 0);
		private LandBiomeSetType biomeSet = MSBiomes.DEFAULT_LAND;
		private Supplier<SoundEvent> backgroundMusic = MSSoundEvents.MUSIC_DEFAULT;
		
		public Builder(Supplier<? extends EntityType<? extends ConsortEntity>> consortType)
		{
			this.consortType = consortType;
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
		
		public Builder biomeSet(LandBiomeSetType biomeSet)
		{
			this.biomeSet = biomeSet;
			return this;
		}
		
		public Builder music(Supplier<SoundEvent> backgroundMusic)
		{
			this.backgroundMusic = backgroundMusic;
			return this;
		}
	}
}

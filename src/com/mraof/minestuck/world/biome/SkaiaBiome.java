package com.mraof.minestuck.world.biome;

import com.mraof.minestuck.entity.ModEntityTypes;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;

public class SkaiaBiome extends Biome
{
	protected SkaiaBiome()
	{
		super(new Biome.BiomeBuilder().surfaceBuilder(new CompositeSurfaceBuilder<>(NOOP_SURFACE_BUILDER, AIR_SURFACE)).precipitation(Biome.RainType.NONE).category(Biome.Category.NONE).depth(0.1F).scale(0.2F).temperature(0.5F).downfall(0.5F).waterColor(4159204).waterFogColor(329011));
		this.addSpawn(EnumCreatureType.MONSTER, new SpawnListEntry(ModEntityTypes.DERSITE_PAWN, 2, 1, 10));
		this.addSpawn(EnumCreatureType.MONSTER, new SpawnListEntry(ModEntityTypes.DERSITE_BISHOP, 1, 1, 1));
		this.addSpawn(EnumCreatureType.MONSTER, new SpawnListEntry(ModEntityTypes.DERSITE_ROOK, 1, 1, 1));
		this.addSpawn(EnumCreatureType.MONSTER, new SpawnListEntry(ModEntityTypes.PROSPITIAN_PAWN, 2, 1, 10));
		this.addSpawn(EnumCreatureType.MONSTER, new SpawnListEntry(ModEntityTypes.PROSPITIAN_BISHOP, 1, 1, 1));
		this.addSpawn(EnumCreatureType.MONSTER, new SpawnListEntry(ModEntityTypes.PROSPITIAN_ROOK, 1, 1, 1));
	}
}

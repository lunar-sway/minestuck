package com.mraof.minestuck.world.lands.title;

import java.util.List;
import java.util.function.Consumer;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.world.biome.LandBiomeSetType;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.ILandType;
import com.mraof.minestuck.world.lands.LandBiomeGenBuilder;
import com.mraof.minestuck.world.lands.LandTypeExtensions;
import com.mraof.minestuck.world.lands.LandTypeExtensions.StructureSetExtension;
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;

/**
 * Base class for land types that are associated with some aspect.
 * These land types make up the second half of a land together with a {@link TerrainLandType}.
 */
public abstract class TitleLandType implements ILandType
{
	public static final Codec<TitleLandType> CODEC = LandTypes.TITLE_REGISTRY.byNameCodec();
	public static final StreamCodec<RegistryFriendlyByteBuf, TitleLandType> STREAM_CODEC = ByteBufCodecs.registry(LandTypes.TITLE_KEY);
	
	/**
	 * Returns true if the given land type may be randomly chosen together with this land type.
	 */
	public boolean isAspectCompatible(TerrainLandType otherType)
	{
		return true;
	}
	
	public void addBiomeGeneration(LandBiomeGenBuilder builder, StructureBlockRegistry blocks, LandBiomeSetType biomeSet)
	{}
	
	@Override
	public void addStructureSets(Consumer<StructureSet> consumer, HolderGetter<Structure> structureLookup)
	{
		LandTypeExtensions landTypeExtensions = LandTypeExtensions.get();
		List<StructureSetExtension> structureSets = landTypeExtensions.getStructureSetsFor(this);
		for(StructureSetExtension structureSetExtension : structureSets)
		{
			consumer.accept(structureSetExtension.structureSet());
		}
	}
	
	public final boolean is(TagKey<TitleLandType> tag)
	{
		return this.is(LandTypes.TITLE_REGISTRY.getOrCreateTag(tag));
	}
	
	public final boolean is(HolderSet<TitleLandType> set)
	{
		return set.stream().anyMatch(holder -> holder.value() == this);
	}
}

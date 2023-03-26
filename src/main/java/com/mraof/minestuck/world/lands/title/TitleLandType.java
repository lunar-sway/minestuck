package com.mraof.minestuck.world.lands.title;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.util.CodecUtil;
import com.mraof.minestuck.world.biome.LandBiomeSetType;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.ILandType;
import com.mraof.minestuck.world.lands.LandBiomeGenBuilder;
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Base class for land types that are associated with some aspect.
 * These land types make up the second half of a land together with a {@link TerrainLandType}.
 */
public abstract class TitleLandType implements ILandType
{
	public static final Codec<TitleLandType> CODEC = CodecUtil.registryCodec(LandTypes.TITLE_REGISTRY);
	private final ResourceLocation groupName;
	private final EnumAspect aspectType;
	private final boolean pickedAtRandom;
	
	protected TitleLandType(EnumAspect aspectType)
	{
		this(aspectType, null, true);
	}
	
	protected TitleLandType(EnumAspect aspectType, boolean pickedAtRandom)
	{
		this(aspectType, null, pickedAtRandom);
	}
	
	protected TitleLandType(EnumAspect aspectType, ResourceLocation groupName)
	{
		this(aspectType, groupName, true);
	}
	
	protected TitleLandType(EnumAspect aspectType, ResourceLocation groupName, boolean pickedAtRandom)
	{
		this.aspectType = aspectType;
		this.groupName = groupName;
		this.pickedAtRandom = pickedAtRandom;
	}
	
	/**
	 * Returns true if the given land type may be randomly chosen together with this land type.
	 */
	public boolean isAspectCompatible(TerrainLandType otherType)
	{
		return true;
	}
	
	/**
	 * Returns true if the land type should be available when a land type is picked randomly.
	 */
	public final boolean canBePickedAtRandom()
	{
		return pickedAtRandom;
	}
	
	public final ResourceLocation getGroup()
	{
		if(groupName == null)
			return LandTypes.TITLE_REGISTRY.get().getKey(this);
		else return groupName;
	}
	
	@Nullable
	public EnumAspect getAspect()
	{
		return aspectType;
	}
	
	public void addBiomeGeneration(LandBiomeGenBuilder builder, StructureBlockRegistry blocks, LandBiomeSetType biomeSet)
	{}
	
	public final boolean is(TagKey<TitleLandType> tag)
	{
		return Objects.requireNonNull(LandTypes.TITLE_REGISTRY.get().tags()).getTag(tag).contains(this);
	}
}

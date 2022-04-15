package com.mraof.minestuck.world.lands.title;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.util.CodecUtil;
import com.mraof.minestuck.world.lands.ILandType;
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

/**
 * Base class for land types that are associated with some aspect.
 * These land types make up the second half of a land together with a {@link TerrainLandType}.
 */
public abstract class TitleLandType extends ForgeRegistryEntry<TitleLandType> implements ILandType<TitleLandType>
{
	public static final Codec<TitleLandType> CODEC = CodecUtil.registryCodec(() -> LandTypes.TITLE_REGISTRY);
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
	
	@Nullable
	public EnumAspect getAspect()
	{
		return aspectType;
	}
}

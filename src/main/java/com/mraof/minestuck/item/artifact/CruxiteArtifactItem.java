package com.mraof.minestuck.item.artifact;

import net.minecraft.world.item.Item;

/**
 * A version of {@link CruxiteArtifact} that extends {@link Item}.
 * See {@link CruxiteArtifact} for details.
 */
public abstract class CruxiteArtifactItem extends Item implements CruxiteArtifact
{
	public CruxiteArtifactItem(Properties properties)
	{
		super(properties);
	}
}
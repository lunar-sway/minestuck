package com.mraof.minestuck.world.lands;

import com.mraof.minestuck.util.IdentifierHandler;
import net.minecraft.util.ResourceLocation;

/**
 * Container for land dimension information between info being loaded,
 * and the dimensions actually being registered.
 */
public class LandInfoContainer
{
	public final IdentifierHandler.PlayerIdentifier identifier;
	public final LandAspects landspects;
	public final ResourceLocation name;
	
	public LandInfoContainer(IdentifierHandler.PlayerIdentifier identifier, LandAspects landspects, ResourceLocation name)
	{
		this.identifier = identifier;
		this.landspects = landspects;
		this.name = name;
	}
}
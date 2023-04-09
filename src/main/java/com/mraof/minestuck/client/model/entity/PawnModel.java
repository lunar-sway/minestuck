package com.mraof.minestuck.client.model.entity;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.carapacian.EnumEntityKingdom;
import com.mraof.minestuck.entity.carapacian.PawnEntity;
import net.minecraft.resources.ResourceLocation;

public class PawnModel extends RotatingHeadAnimatedModel<PawnEntity>
{
	@Override
	public ResourceLocation getModelResource(PawnEntity object)
	{
		return new ResourceLocation(Minestuck.MOD_ID, "geo/entity/carapacian/pawn.geo.json");
	}
	
	@Override
	public ResourceLocation getTextureResource(PawnEntity object)
	{
		if(object.getKingdom() == EnumEntityKingdom.DERSITE)
		{
			return new ResourceLocation(Minestuck.MOD_ID, "textures/entity/carapacian/derse_pawn.png");
		} else
		{
			return new ResourceLocation(Minestuck.MOD_ID, "textures/entity/carapacian/prospit_pawn.png");
		}
	}
	
	@Override
	public ResourceLocation getAnimationResource(PawnEntity animatable)
	{
		return new ResourceLocation(Minestuck.MOD_ID, "animations/entity/carapacian/pawn.animation.json");
	}
}

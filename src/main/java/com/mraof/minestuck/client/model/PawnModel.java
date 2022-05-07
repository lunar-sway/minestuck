package com.mraof.minestuck.client.model;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.carapacian.PawnEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PawnModel extends AnimatedGeoModel<PawnEntity>
{
	@Override
	public ResourceLocation getModelLocation(PawnEntity object) {
		return new ResourceLocation(Minestuck.MOD_ID, "geo/entity/carapacian/pawn.geo.json");
	}

	@Override
	public ResourceLocation getTextureLocation(PawnEntity object) {
		return new ResourceLocation(Minestuck.MOD_ID, "textures/entity/carapacian/pawn.png");
	}

	@Override
	public ResourceLocation getAnimationFileLocation(PawnEntity animatable) {
		return new ResourceLocation(Minestuck.MOD_ID, "animations/entity/carapacian/pawn.animation.json");
	}
}

package com.mraof.minestuck.client.model;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.underling.OgreEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class OgreModel extends AnimatedGeoModel<OgreEntity>
{
	@Override
	public ResourceLocation getModelLocation(OgreEntity object) {
		return new ResourceLocation(Minestuck.MOD_ID, "geo/entity/underlings/ogre.geo.json");
	}

	@Override
	public ResourceLocation getTextureLocation(OgreEntity object) {
		return new ResourceLocation(Minestuck.MOD_ID, "textures/entity/underlings/ogre.png");
	}

	@Override
	public ResourceLocation getAnimationFileLocation(OgreEntity animatable) {
		return new ResourceLocation(Minestuck.MOD_ID, "animations/entity/underlings/ogre.animation.json");
	}
}

package com.mraof.minestuck.client.model; // Made with Blockbench 3.8.4 by Riotmode

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class TurtleModel extends AnimatedGeoModel
{
	@Override
	public ResourceLocation getAnimationFileLocation(Object entity) {
		return new ResourceLocation("minestuck", "animations/turtle.animation.json");
	}
	
	@Override
	public ResourceLocation getModelLocation(Object entity) {
		return new ResourceLocation("minestuck", "geo/turtle.geo.json");
	}
	
	@Override
	public ResourceLocation getTextureLocation(Object entity) {
		return new ResourceLocation("minestuck", "textures/entity/turtle.png");
	}
	
	@Override
	public void setLivingAnimations(IAnimatable entity, Integer uniqueID, AnimationEvent customPredicate) {
		super.setLivingAnimations(entity, uniqueID, customPredicate);
	}
}
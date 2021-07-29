package com.mraof.minestuck.client.model; // Made with Blockbench 3.8.4 by Riotmode

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class LotusFlowerModel extends AnimatedGeoModel
{
	@Override
	public ResourceLocation getAnimationFileLocation(Object entity) {
		return new ResourceLocation("minestuck", "animations/lotus_flower.animation.json");
	}
	
	@Override
	public ResourceLocation getModelLocation(Object entity) {
		return new ResourceLocation("minestuck", "geo/lotus_flower.geo.json");
	}
	
	@Override
	public ResourceLocation getTextureLocation(Object entity) {
		return new ResourceLocation("minestuck", "textures/entity/lotus_flower.png");
	}
	
	@Override
	public void setLivingAnimations(IAnimatable entity, Integer uniqueID, AnimationEvent customPredicate) {
		super.setLivingAnimations(entity, uniqueID, customPredicate);
	}
}
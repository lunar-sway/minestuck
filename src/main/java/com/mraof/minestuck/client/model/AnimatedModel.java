package com.mraof.minestuck.client.model;

import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import javax.annotation.Nullable;

public abstract class AnimatedModel<T extends IAnimatable> extends AnimatedGeoModel<T> {
	@Override
	public void setLivingAnimations(T entity, Integer uniqueID, @Nullable AnimationEvent predicate) {
		super.setLivingAnimations(entity, uniqueID, predicate);
		IBone head = this.getAnimationProcessor().getBone("head");
		EntityModelData extraData = (EntityModelData) predicate.getExtraDataOfType(EntityModelData.class).get(0);
		head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
		head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
	}
}

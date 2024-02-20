package com.mraof.minestuck.client.model.entity;

import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

/**
 * Base class for all entity models which implements head movement based on the look direction
 */
public abstract class RotatingHeadAnimatedModel<T extends GeoAnimatable> extends GeoModel<T> {
	@Override
	public void setCustomAnimations(T animatable, long instanceId, AnimationState<T> animationState)
	{
		CoreGeoBone head = this.getAnimationProcessor().getBone("head");
		EntityModelData extraData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
		head.setRotX(extraData.headPitch() * Mth.DEG_TO_RAD);
		head.setRotY(extraData.netHeadYaw() * Mth.DEG_TO_RAD);
	}
}

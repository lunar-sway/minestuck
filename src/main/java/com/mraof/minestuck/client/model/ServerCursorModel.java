package com.mraof.minestuck.client.model;

import com.mraof.minestuck.entity.ServerCursorEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ServerCursorModel extends AnimatedGeoModel<ServerCursorEntity>
{
	@Override
	public ResourceLocation getAnimationResource(ServerCursorEntity entity) {
		return new ResourceLocation("minestuck", "animations/server_cursor.animation.json");
	}
	
	@Override
	public ResourceLocation getModelResource(ServerCursorEntity entity) {
		return new ResourceLocation("minestuck", "geo/server_cursor.geo.json");
	}
	
	@Override
	public ResourceLocation getTextureResource(ServerCursorEntity entity) {
		return new ResourceLocation("minestuck", "textures/entity/server_cursor.png");
	}
	
	@Override
	public void setLivingAnimations(ServerCursorEntity entity, Integer uniqueID, AnimationEvent customPredicate)
	{
		super.setLivingAnimations(entity, uniqueID, customPredicate);
		IBone cursor = this.getAnimationProcessor().getBone("head");
		
		if(!entity.level.isClientSide || !Minecraft.getInstance().isPaused())
		cursor.setRotationZ(cursor.getRotationZ() + entity.getXRot() * ((float) Math.PI / 180F));
	}
}

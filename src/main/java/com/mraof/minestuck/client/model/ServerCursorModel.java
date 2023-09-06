package com.mraof.minestuck.client.model;

import com.mraof.minestuck.entity.ServerCursorEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class ServerCursorModel extends GeoModel<ServerCursorEntity>
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
	public void setCustomAnimations(ServerCursorEntity animatable, long instanceId, AnimationState<ServerCursorEntity> animationState)
	{
		CoreGeoBone cursor = this.getAnimationProcessor().getBone("head");
		
		if(!animatable.level().isClientSide || !Minecraft.getInstance().isPaused())
			cursor.setRotZ(cursor.getRotZ() + animatable.getXRot() * ((float) Math.PI / 180F));
	}
}

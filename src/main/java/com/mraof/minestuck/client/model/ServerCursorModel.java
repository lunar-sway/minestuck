package com.mraof.minestuck.client.model;

import com.mraof.minestuck.entity.ServerCursorEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
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
		
		cursor.setRotZ(animatable.getXRot() * Mth.DEG_TO_RAD);
	}
}

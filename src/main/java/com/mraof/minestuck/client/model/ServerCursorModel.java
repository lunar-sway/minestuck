package com.mraof.minestuck.client.model;

import com.mraof.minestuck.entity.ServerCursorEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ServerCursorModel extends AnimatedGeoModel<ServerCursorEntity>
{
	@Override
	public ResourceLocation getAnimationFileLocation(ServerCursorEntity entity) {
		return new ResourceLocation("minestuck", "animations/server_cursor.animation.json");
	}
	
	@Override
	public ResourceLocation getModelLocation(ServerCursorEntity entity) {
		return new ResourceLocation("minestuck", "geo/server_cursor.geo.json");
	}
	
	@Override
	public ResourceLocation getTextureLocation(ServerCursorEntity entity) {
		return new ResourceLocation("minestuck", "textures/entity/server_cursor.png");
	}
	
	@Override
	public void setLivingAnimations(ServerCursorEntity entity, Integer uniqueID, AnimationEvent customPredicate)
	{
		super.setLivingAnimations(entity, uniqueID, customPredicate);
		IBone cursor = this.getAnimationProcessor().getBone("head");
		
		cursor.setRotationZ(cursor.getRotationZ() + entity.getXRot() * ((float) Math.PI / 180F));
	}
}

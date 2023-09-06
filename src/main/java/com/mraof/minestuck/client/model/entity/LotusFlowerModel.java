package com.mraof.minestuck.client.model.entity; // Made with Blockbench 3.8.4 by Riotmode

import com.mraof.minestuck.entity.LotusFlowerEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class LotusFlowerModel extends GeoModel<LotusFlowerEntity>
{
	@Override
	public ResourceLocation getAnimationResource(LotusFlowerEntity entity) {
		return new ResourceLocation("minestuck", "animations/lotus_flower.animation.json");
	}
	
	@Override
	public ResourceLocation getModelResource(LotusFlowerEntity entity) {
		return new ResourceLocation("minestuck", "geo/lotus_flower.geo.json");
	}
	
	@Override
	public ResourceLocation getTextureResource(LotusFlowerEntity entity) {
		return new ResourceLocation("minestuck", "textures/entity/lotus_flower.png");
	}
}
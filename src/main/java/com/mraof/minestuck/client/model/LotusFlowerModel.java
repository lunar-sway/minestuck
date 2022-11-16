package com.mraof.minestuck.client.model; // Made with Blockbench 3.8.4 by Riotmode

import com.mraof.minestuck.entity.LotusFlowerEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class LotusFlowerModel extends AnimatedGeoModel<LotusFlowerEntity>
{
	@Override
	public ResourceLocation getAnimationFileLocation(LotusFlowerEntity entity) {
		return new ResourceLocation("minestuck", "animations/lotus_flower.animation.json");
	}
	
	@Override
	public ResourceLocation getModelLocation(LotusFlowerEntity entity) {
		return new ResourceLocation("minestuck", "geo/lotus_flower.geo.json");
	}
	
	@Override
	public ResourceLocation getTextureLocation(LotusFlowerEntity entity) {
		return new ResourceLocation("minestuck", "textures/entity/lotus_flower.png");
	}
}
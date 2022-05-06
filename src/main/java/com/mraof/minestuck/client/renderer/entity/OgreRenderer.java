package com.mraof.minestuck.client.renderer.entity;

import com.mraof.minestuck.client.model.OgreModel;
import com.mraof.minestuck.entity.underling.OgreEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class OgreRenderer extends GeoEntityRenderer<OgreEntity> {
	public OgreRenderer(EntityRendererManager renderManager) {
		super(renderManager, new OgreModel());
	}

	@Override
	protected float getDeathMaxRotation(OgreEntity entityLivingBaseIn) {
		return 0;
	}
}
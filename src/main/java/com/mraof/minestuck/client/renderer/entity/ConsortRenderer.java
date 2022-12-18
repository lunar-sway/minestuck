package com.mraof.minestuck.client.renderer.entity;

import com.mraof.minestuck.client.model.entity.ConsortModel;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class ConsortRenderer<T extends ConsortEntity> extends GeoEntityRenderer<T>
{
	public ConsortRenderer(EntityRendererProvider.Context context)
	{
		super(context, new ConsortModel<>());
	}
	
	@Override
	protected float getDeathMaxRotation(T entityLivingBaseIn)
	{
		return 0;
	}
	
}

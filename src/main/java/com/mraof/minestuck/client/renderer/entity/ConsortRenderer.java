package com.mraof.minestuck.client.renderer.entity;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.entity.consort.EnumConsort;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ConsortRenderer<T extends ConsortEntity> extends GeoEntityRenderer<T>
{
	public ConsortRenderer(EntityRendererProvider.Context context, EnumConsort consortType)
	{
		super(context, new DefaultedEntityGeoModel<>(Minestuck.id("consort/" + consortType.getName()), true));
	}
	
	@Override
	protected float getDeathMaxRotation(T entityLivingBaseIn)
	{
		return 0;
	}
}

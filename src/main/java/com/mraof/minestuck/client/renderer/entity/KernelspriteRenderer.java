package com.mraof.minestuck.client.renderer.entity;

import com.mraof.minestuck.client.model.entity.KernelspriteModel;
import com.mraof.minestuck.entity.KernelspriteEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class KernelspriteRenderer extends MobRenderer<KernelspriteEntity, KernelspriteModel<KernelspriteEntity>>
{
	public KernelspriteRenderer(EntityRendererProvider.Context context, KernelspriteModel<KernelspriteEntity> model, float shadowRadius)
	{
		super(context, model, shadowRadius);
	}
	
	@Override
	public ResourceLocation getTextureLocation(KernelspriteEntity kernelspriteEntity)
	{
		return null;
	}
}

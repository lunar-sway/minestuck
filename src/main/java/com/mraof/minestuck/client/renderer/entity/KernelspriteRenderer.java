package com.mraof.minestuck.client.renderer.entity;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.model.entity.KernelspriteModel;
import com.mraof.minestuck.entity.KernelspriteEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class KernelspriteRenderer extends EntityRenderer<KernelspriteEntity>
{
	public KernelspriteRenderer(EntityRendererProvider.Context context)
	{
		super(context);
		this.shadowRadius = 0.15F;
		this.shadowStrength = .75F;
	}
	
	@Override
	public ResourceLocation getTextureLocation(KernelspriteEntity entity)
	{
		return ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID,"kernelsprite/kernelsprite_back");
	}
}

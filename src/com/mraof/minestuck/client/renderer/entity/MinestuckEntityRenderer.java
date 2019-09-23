package com.mraof.minestuck.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;

import com.mraof.minestuck.entity.MinestuckEntity;

public class MinestuckEntityRenderer<T extends MinestuckEntity, M extends EntityModel<T>> extends MobRenderer<T, M>
{
	
	public MinestuckEntityRenderer(EntityRendererManager manager, M par1ModelBase, float par2)
	{
		super(manager, par1ModelBase, par2);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(T entity) 
	{
		return entity.getTextureResource();
	}
}
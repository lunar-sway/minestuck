package com.mraof.minestuck.client.renderer.entity;

import com.mraof.minestuck.entity.SimpleTexturedEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SimpleTexturedEntityRenderer<T extends SimpleTexturedEntity, M extends EntityModel<T>> extends MobRenderer<T, M>
{
	
	public SimpleTexturedEntityRenderer(EntityRendererProvider.Context context, M par1ModelBase, float par2)
	{
		super(context, par1ModelBase, par2);
	}
	
	@Override
	public ResourceLocation getTextureLocation(T entity)
	{
		return entity.getTextureResource();
	}
}
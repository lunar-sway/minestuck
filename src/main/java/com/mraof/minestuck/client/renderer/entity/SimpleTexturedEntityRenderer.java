package com.mraof.minestuck.client.renderer.entity;

import com.mraof.minestuck.entity.SimpleTexturedEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;

public class SimpleTexturedEntityRenderer<T extends SimpleTexturedEntity, M extends EntityModel<T>> extends MobRenderer<T, M>
{
	
	public SimpleTexturedEntityRenderer(EntityRendererManager manager, M par1ModelBase, float par2)
	{
		super(manager, par1ModelBase, par2);
	}
	
	@Override
	public ResourceLocation getEntityTexture(T entity)
	{
		return entity.getTextureResource();
	}
}
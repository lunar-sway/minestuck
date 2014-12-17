package com.mraof.minestuck.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import com.mraof.minestuck.entity.EntityMinestuck;

public class RenderEntityMinestuck extends RenderLiving 
{

	public RenderEntityMinestuck(RenderManager manager, ModelBase par1ModelBase, float par2) 
	{
		super(manager, par1ModelBase, par2);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) 
	{
		return ((EntityMinestuck)entity).getTextureResource();
	}

}

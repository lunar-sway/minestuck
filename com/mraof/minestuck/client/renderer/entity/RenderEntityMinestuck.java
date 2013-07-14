package com.mraof.minestuck.client.renderer.entity;

import com.mraof.minestuck.entity.EntityMinestuck;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.resources.ResourceLocation;
import net.minecraft.entity.Entity;

public class RenderEntityMinestuck extends RenderLiving 
{

	public RenderEntityMinestuck(ModelBase par1ModelBase, float par2) 
	{
		super(par1ModelBase, par2);
	}

	@Override
	protected ResourceLocation func_110775_a(Entity entity) 
	{
		return ((EntityMinestuck)entity).getTextureResource();
	}

}

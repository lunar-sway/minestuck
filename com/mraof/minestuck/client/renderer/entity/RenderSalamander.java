package com.mraof.minestuck.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;

import com.mraof.minestuck.entity.consort.EntitySalamander;

public class RenderSalamander extends RenderLiving 
{

	public RenderSalamander(ModelBase modelbase, float f) 
	{
		super(modelbase, f);
	}
	public void doRender(EntitySalamander entitysalamander, double d, double d1, double d2, float f, float f1) 
	{
		doRenderLiving(entitysalamander, d, d1, d2, f, f1);
	}
	public void doRenderLiving(EntitySalamander entitySalamander, double d, double d1, double d2, float f, float f1) 
	{
		super.doRenderLiving(entitySalamander, d, d1, d2, f, f1);
	}
}

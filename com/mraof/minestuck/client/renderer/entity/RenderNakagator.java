package com.mraof.minestuck.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;

import com.mraof.minestuck.entity.consort.EntityNakagator;

public class RenderNakagator extends RenderLiving {

	public RenderNakagator(ModelBase modelbase, float f) 
	{
		super(modelbase, f);
	}

	public void doRender(EntityNakagator entityNakagator, double d, double d1, double d2, float f, float f1) 
	{
		doRenderLiving(entityNakagator, d, d1, d2, f, f1);
	}

	public void doRenderLiving(EntityNakagator entityNakagator, double d, double d1, double d2, float f, float f1) 
	{
		super.doRenderLiving(entityNakagator, d, d1, d2, f, f1);
	}
}

package com.mraof.minestuck.client.renderer.entity;


import com.mraof.minestuck.entity.EntityImp;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;

public class RenderImp extends RenderLiving
{
	public RenderImp(ModelBase modelbase, float f) 
	{
		super(modelbase, f);
	}
	public void doRender(EntityImp entityImp, double d, double d1, double d2,
			float f, float f1) {
		doRenderLiving(entityImp, d, d1, d2, f, f1);
	}
	public void doRenderLiving(EntityImp entityImp, double d, double d1,
			double d2, float f, float f1) {
		super.doRenderLiving(entityImp, d, d1, d2, f, f1);
	}
}

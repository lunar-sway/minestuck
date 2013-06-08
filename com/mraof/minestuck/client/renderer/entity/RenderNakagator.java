package com.mraof.minestuck.client.renderer.entity;

import com.mraof.minestuck.entity.EntitySalamander;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;

public class RenderNakagator extends RenderLiving {

	public RenderNakagator(ModelBase modelbase, float f) {
		super(modelbase, f);
	}

	public void doRender(EntitySalamander entitysalamander, double d,
			double d1, double d2, float f, float f1) {
		doRenderLiving(entitysalamander, d, d1, d2, f, f1);
	}

	public void doRenderLiving(EntitySalamander entitySalamander, double d,
			double d1, double d2, float f, float f1) {
		super.doRenderLiving(entitySalamander, d, d1, d2, f, f1);
	}
}

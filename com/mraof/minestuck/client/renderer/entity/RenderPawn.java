/**
 * 
 */
package com.mraof.minestuck.client.renderer.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;

import com.mraof.minestuck.entity.carapacian.EntityPawn;

/**
 * @author mraof
 *
 */
public class RenderPawn extends RenderBiped 
{

	public RenderPawn(ModelBiped modelBiped, float par2) 
	{
		super(modelBiped, par2);
	}
	public void doRender(EntityPawn entityPawn, double d, double d1, double d2, float f, float f1) 
	{
		doRenderLiving(entityPawn, d, d1, d2, f, f1);
	}
	public void doRenderLiving(EntityPawn entityPawn, double d, double d1, double d2, float f, float f1) 
	{
		super.doRenderLiving(entityPawn, d, d1, d2, f, f1);
	}

}

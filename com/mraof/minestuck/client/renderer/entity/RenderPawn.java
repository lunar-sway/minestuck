package com.mraof.minestuck.client.renderer.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.resources.ResourceLocation;
import net.minecraft.entity.EntityLiving;

import com.mraof.minestuck.entity.carapacian.EntityPawn;

public class RenderPawn extends RenderBiped 
{

	public RenderPawn(ModelBiped par1ModelBiped, float par2) 
	{
		super(par1ModelBiped, par2);
	}
	@Override
	protected ResourceLocation func_110856_a(EntityLiving par1EntityLiving) 
	{
		return ((EntityPawn)par1EntityLiving).getTextureResource();
	}

}

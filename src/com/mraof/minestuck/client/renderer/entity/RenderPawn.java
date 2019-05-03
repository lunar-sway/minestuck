package com.mraof.minestuck.client.renderer.entity;

import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.model.ModelBiped;
import net.minecraft.util.ResourceLocation;

import com.mraof.minestuck.entity.carapacian.EntityPawn;

public class RenderPawn extends RenderBiped<EntityPawn>
{
	
	public RenderPawn(RenderManager manager, ModelBiped modelBiped, float shadowSize)
	{
		super(manager, modelBiped, shadowSize);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityPawn pawn) 
	{
		return pawn.getTextureResource();
	}
}
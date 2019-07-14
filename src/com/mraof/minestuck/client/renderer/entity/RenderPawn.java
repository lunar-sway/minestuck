package com.mraof.minestuck.client.renderer.entity;

import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.model.ModelBiped;
import net.minecraft.util.ResourceLocation;

import com.mraof.minestuck.entity.carapacian.PawnEntity;

public class RenderPawn extends RenderBiped<PawnEntity>
{
	
	public RenderPawn(RenderManager manager, ModelBiped modelBiped, float shadowSize)
	{
		super(manager, modelBiped, shadowSize);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(PawnEntity pawn)
	{
		return pawn.getTextureResource();
	}
}
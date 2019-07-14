package com.mraof.minestuck.client.renderer.entity;

import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.util.ResourceLocation;

import com.mraof.minestuck.entity.carapacian.PawnEntity;

public class RenderPawn extends BipedRenderer<PawnEntity, BipedModel<PawnEntity>>
{
	
	public RenderPawn(EntityRendererManager manager, BipedModel modelBiped, float shadowSize)
	{
		super(manager, modelBiped, shadowSize);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(PawnEntity pawn)
	{
		return pawn.getTextureResource();
	}
}
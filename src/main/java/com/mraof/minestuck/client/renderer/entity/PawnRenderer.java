package com.mraof.minestuck.client.renderer.entity;

import com.mraof.minestuck.client.model.MSModelLayers;
import com.mraof.minestuck.entity.carapacian.PawnEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;

public class PawnRenderer extends HumanoidMobRenderer<PawnEntity, HumanoidModel<PawnEntity>>
{
	
	public PawnRenderer(EntityRendererProvider.Context context)
	{
		super(context, new HumanoidModel<>(context.bakeLayer(MSModelLayers.PAWN)), 0.5F);
	}
	
	@Override
	public ResourceLocation getTextureLocation(PawnEntity pawn)
	{
		return pawn.getTextureResource();
	}
}
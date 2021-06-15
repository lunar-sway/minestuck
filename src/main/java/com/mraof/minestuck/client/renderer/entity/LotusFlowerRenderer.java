package com.mraof.minestuck.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mraof.minestuck.entity.item.LotusFlowerEntity;
import com.mraof.minestuck.entity.item.VitalityGelEntity;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

public class LotusFlowerRenderer extends EntityRenderer<LotusFlowerEntity>
{
	
	public LotusFlowerRenderer(EntityRendererManager manager)
	{
		super(manager);
		this.shadowSize = 0.15F;
		this.shadowOpaque = .75F;
	}
	
	@Override
	public ResourceLocation getEntityTexture(LotusFlowerEntity entity)
	{
		return new ResourceLocation("minestuck", "textures/entity/lotus_flower.png");
	}

}

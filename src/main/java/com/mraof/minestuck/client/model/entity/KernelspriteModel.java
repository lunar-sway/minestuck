package com.mraof.minestuck.client.model.entity;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class KernelspriteModel extends EntityModel
{
	private final ResourceLocation model;
	private final String layer;
	
	public KernelspriteModel(ResourceLocation model, String layer) {
		
		this.model = model;
		this.layer = layer;
		
		
	}
	
	
	@Override
	public void setupAnim(Entity entity, float v, float v1, float v2, float v3, float v4)
	{
	
	}
	
	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int i, int i1, int i2)
	{
	
	}
}

package com.mraof.minestuck.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mraof.minestuck.client.model.LotusFlowerModel;
import com.mraof.minestuck.entity.LotusFlowerEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class LotusFlowerRenderer extends GeoEntityRenderer<LotusFlowerEntity> {
	public LotusFlowerRenderer(EntityRendererProvider.Context context) {
		super(context, new LotusFlowerModel());
	}
	
	@Override
	public RenderType getRenderType(LotusFlowerEntity animatable, float partialTicks, PoseStack stack,
									MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
									ResourceLocation textureLocation) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}
}
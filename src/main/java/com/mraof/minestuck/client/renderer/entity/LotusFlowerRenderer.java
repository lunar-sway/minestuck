package com.mraof.minestuck.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mraof.minestuck.client.model.LotusFlowerModel;
import com.mraof.minestuck.entity.LotusFlowerEntity;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class LotusFlowerRenderer extends GeoEntityRenderer<LotusFlowerEntity> {
	public LotusFlowerRenderer(EntityRendererManager renderManager) {
		super(renderManager, new LotusFlowerModel());
	}
	
	@Override
	public RenderType getRenderType(LotusFlowerEntity animatable, float partialTicks, MatrixStack stack,
									IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
									ResourceLocation textureLocation) {
		return RenderType.getEntityTranslucent(getTextureLocation(animatable));
	}
}
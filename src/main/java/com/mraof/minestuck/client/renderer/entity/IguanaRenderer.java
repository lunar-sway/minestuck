package com.mraof.minestuck.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mraof.minestuck.client.model.IguanaModel;
import com.mraof.minestuck.client.model.TurtleModel;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class IguanaRenderer extends GeoEntityRenderer<ConsortEntity> {
	public IguanaRenderer(EntityRendererManager renderManager) {
		super(renderManager, new IguanaModel());
	}
	
	@Override
	public RenderType getRenderType(ConsortEntity animatable, float partialTicks, MatrixStack stack,
									IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
									ResourceLocation textureLocation) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}
}
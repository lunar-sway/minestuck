package com.mraof.minestuck.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.model.OgreModel;
import com.mraof.minestuck.entity.underling.OgreEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import javax.annotation.Nullable;

public class OgreRenderer extends GeoEntityRenderer<OgreEntity> {
	public OgreRenderer(EntityRendererManager renderManager) {
		super(renderManager, new OgreModel());
		this.addLayer(new OgreDetailsLayer(this));
	}

	@Override
	protected float getDeathMaxRotation(OgreEntity entityLivingBaseIn) {
		return 0;
	}

	@Override
	public Color getRenderColor(OgreEntity animatable, float partialTicks, MatrixStack stack, @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn) {
		return Color.ofRGBA(0, 255, 0, 255); //TODO that
	}

	public class OgreDetailsLayer extends GeoLayerRenderer {
		public OgreDetailsLayer(IGeoRenderer entityRendererIn) {
			super(entityRendererIn);
		}

		@Override
		public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, Entity entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			RenderType renderType =  RenderType.armorCutoutNoCull(new ResourceLocation(Minestuck.MOD_ID, "textures/entity/underlings/ogre_details.png"));
			matrixStackIn.pushPose();
			matrixStackIn.scale(1.0f, 1.0f, 1.0f);
			matrixStackIn.translate(0.0d, 0.0d, 0.0d);

			GeoModel model = modelProvider.getModel(modelProvider.getModelLocation((OgreEntity)entityLivingBaseIn));
			this.getRenderer().render(model, entityLivingBaseIn, partialTicks, renderType, matrixStackIn, bufferIn, bufferIn.getBuffer(renderType), packedLightIn, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);

			matrixStackIn.popPose();
		}
	}
}
package com.mraof.minestuck.client.renderer.entity;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.KernelspriteEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import software.bernie.geckolib.cache.texture.AnimatableTexture;

public class KernelspriteRenderer extends EntityRenderer<KernelspriteEntity>
{
	public static final ResourceLocation BASE = Minestuck.id("textures/entity/kernelsprite/base.png");
	public static final ResourceLocation FRONT = Minestuck.id("textures/entity/kernelsprite/front.png");
	
	private int animationTick = 0;
	
	public KernelspriteRenderer(EntityRendererProvider.Context context)
	{
		super(context);
		this.shadowRadius = 0.15F;
		this.shadowStrength = .75F;
	}
	
	@Override
	public ResourceLocation getTextureLocation(KernelspriteEntity entity)
	{
		return BASE;
	}
	
	@Override
	public void render(KernelspriteEntity kernelsprite, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight)
	{
		super.render(kernelsprite, entityYaw, partialTick, poseStack, bufferSource, packedLight);
		
		poseStack.pushPose();
		poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
		
		int color = kernelsprite.getColor();
		
		spriteVertex(poseStack, bufferSource, packedLight, BASE, color);
		spriteVertex(poseStack, bufferSource, packedLight, FRONT, color);
		poseStack.popPose();
	}
	
	private void spriteVertex(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, ResourceLocation sprite, int color)
	{
		int r = ((color >> 16) & 255);
		int g = ((color >> 8) & 255);
		int b = (color & 255);
		
		PoseStack.Pose pose = poseStack.last();
		Matrix4f matrix4f = pose.pose();
		AnimatableTexture.setAndUpdate(sprite, animationTick++);
		VertexConsumer iVertexbuilder = bufferSource.getBuffer(RenderType.entityCutoutNoCull(sprite));
		iVertexbuilder.addVertex(matrix4f, 0.0f - 0.5f, 0.0f - 0.25f, 0.0f).setColor(r, g, b, 255).setUv(0, 1)
				.setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, 0.0f, 1.0f, 0.0f);
		iVertexbuilder.addVertex(matrix4f, 1.0f - 0.5f, 0.0f - 0.25f, 0.0f).setColor(r, g, b, 255).setUv(1, 1)
				.setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, 0.0f, 1.0f, 0.0f);
		iVertexbuilder.addVertex(matrix4f, 1.0f - 0.5f, 1.0f - 0.25f, 0.0f).setColor(r, g, b, 255).setUv(1, 0)
				.setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, 0.0f, 1.0f, 0.0f);
		iVertexbuilder.addVertex(matrix4f, 0.0f - 0.5f, 1.0f - 0.25f, 0.0f).setColor(r, g, b, 255).setUv(0, 0)
				.setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, 0.0f, 1.0f, 0.0f);
	}
}

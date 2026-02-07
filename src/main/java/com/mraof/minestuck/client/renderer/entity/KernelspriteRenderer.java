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
import net.minecraft.server.packs.resources.Resource;
import net.neoforged.neoforge.client.extensions.IVertexConsumerExtension;
import org.joml.Matrix4f;
import software.bernie.geckolib.cache.texture.AnimatableTexture;

public class KernelspriteRenderer extends EntityRenderer<KernelspriteEntity>
{
	public static final ResourceLocation BACK = Minestuck.id("textures/entity/kernelsprite/kernelsprite_back.png");
	
	public static final ResourceLocation COLOR = Minestuck.id("textures/entity/kernelsprite/kernelsprite_color.png");
	
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
	 return BACK;
	 
	}
	@Override
	public void render(KernelspriteEntity kernelsprite, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight)
	{
		super.render(kernelsprite, entityYaw, partialTick, poseStack, bufferSource, packedLight);
		
		poseStack.pushPose();
		poseStack.translate(0.0f,1.0f,0.0f);
		poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
		
		PoseStack.Pose matrixstack = poseStack.last();
		Matrix4f matrix4f = matrixstack.pose();
		ResourceLocation sprite = getTextureLocation(kernelsprite);
		AnimatableTexture.setAndUpdate(sprite, animationTick++);
		VertexConsumer iVertexbuilder = bufferSource.getBuffer(RenderType.entityCutoutNoCull(sprite));
		iVertexbuilder.addVertex(matrix4f,0.0f - 0.5f, 0.0f - 0.25f, 0.0f).setColor(225,225,225,225).setUv(0,1)
				.setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(matrixstack,0.0f,1.0f,0.0f);
		iVertexbuilder.addVertex(matrix4f,1.0f - 0.5f, 0.0f - 0.25f, 0.0f).setColor(225,225,225,225).setUv(1,1)
				.setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(matrixstack,0.0f,1.0f,0.0f);
		iVertexbuilder.addVertex(matrix4f,1.0f - 0.5f, 1.0f - 0.25f, 0.0f).setColor(225,225,225,225).setUv(1,0)
				.setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(matrixstack,0.0f,1.0f,0.0f);
		iVertexbuilder.addVertex(matrix4f,0.0f - 0.5f, 1.0f - 0.25f, 0.0f).setColor(225,225,225,225).setUv(0,0)
				.setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(matrixstack,0.0f,1.0f,0.0f);
		poseStack.popPose();
	}
}

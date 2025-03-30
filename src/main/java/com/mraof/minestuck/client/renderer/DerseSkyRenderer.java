package com.mraof.minestuck.client.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;

public final class DerseSkyRenderer
{
	public static void render(Matrix4f modelViewMatrix, ClientLevel level)
	{
		PoseStack poseStack = new PoseStack();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		FogRenderer.levelFogColor();
		RenderSystem.depthMask(false);
		
		poseStack.mulPose(modelViewMatrix);
		
		RenderSystem.setShader(GameRenderer::getPositionShader);
		RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		
		SessionRenderHelper.drawRotatingSkaia(poseStack, level, 5.0F);
		SessionRenderHelper.drawRotatingProspit(poseStack, level, 0.5F, false);
		SessionRenderHelper.drawRotatingVeil(poseStack, level);
		
		//TODO offset location everything is rendered from
		//modelViewMatrix.translate(10.0F, 100.0F, 0.0F);
		//poseStack.translate(0.0F, 12.0F, 0.0F); //offsets all celestial bodies
		
		RenderSystem.disableBlend();
		RenderSystem.depthMask(true);
	}
}

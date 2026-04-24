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
		
		poseStack.pushPose();
		RenderSystem.disableCull();
		
		poseStack.translate(5, 120, 0);
		SessionRenderHelper.drawVeil(poseStack);
		
		poseStack.translate(-5, -49, 0);
		SessionRenderHelper.drawSkaia(poseStack.last().pose(), 5.0F);
		SessionRenderHelper.drawProspit(poseStack, level, 0.5F, false);
		
		poseStack.popPose();
		RenderSystem.enableCull();
		
		RenderSystem.disableBlend();
		RenderSystem.depthMask(true);
	}
}

package com.mraof.minestuck.client.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;

public final class VeilSkyRenderer
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
		
		poseStack.translate(0, 99, 0);
		SessionRenderHelper.drawVeil(poseStack);
		SessionRenderHelper.drawDerse(poseStack, level, 1.25F, true);
		
		poseStack.translate(0, -49, 0);
		SessionRenderHelper.drawProspit(poseStack, level, 0.75F, false);
		SessionRenderHelper.drawSkaia(poseStack.last().pose(), 6.0F);
		
		poseStack.popPose();
		
		RenderSystem.disableBlend();
		RenderSystem.depthMask(true);
	}
}

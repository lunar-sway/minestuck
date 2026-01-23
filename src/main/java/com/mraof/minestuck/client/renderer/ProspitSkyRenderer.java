package com.mraof.minestuck.client.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;

public final class ProspitSkyRenderer
{
	//TODO edges of chunks are visible due to lack of fog
	
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
		
		SessionRenderHelper.drawRotatingVeil(poseStack, level);
		SessionRenderHelper.drawRotatingDerse(poseStack, level, 0.5F);
		SessionRenderHelper.drawRotatingSkaia(poseStack, level, 200.0F);
		
		RenderSystem.disableBlend();
		RenderSystem.depthMask(true);
	}
}

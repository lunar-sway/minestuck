package com.mraof.minestuck.client.renderer;


import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix4f;

public final class ProspitSkyRenderer
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static void render(Matrix4f modelViewMatrix, ClientLevel level)
	{
		PoseStack poseStack = new PoseStack();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.depthMask(false);
		
		poseStack.mulPose(modelViewMatrix);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		
		renderVeil(level, poseStack);
		renderSkaia(level, poseStack);
		
		RenderSystem.disableBlend();
		RenderSystem.depthMask(true);
	}
	
	private static void renderVeil(ClientLevel level, PoseStack poseStack)
	{
		poseStack.pushPose();
		poseStack.mulPose(Axis.ZP.rotationDegrees(SessionRenderHelper.calculateVeilAngle(level) * 360.0F));
		SessionRenderHelper.drawVeil(poseStack.last().pose());
		poseStack.popPose();
	}
	
	private static void renderSkaia(ClientLevel level, PoseStack poseStack)
	{
		RenderSystem.setShader(GameRenderer::getPositionShader);
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		
		SessionRenderHelper.drawRotatingSkaia(poseStack, level, 100F);
	}
}

package com.mraof.minestuck.client.renderer;


import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mraof.minestuck.client.ClientDimensionData;
import com.mraof.minestuck.world.lands.LandProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix4f;

public final class LandSkyRenderer
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static void render(int ticks, float partialTicks, Matrix4f modelViewMatrix, ClientLevel level, Minecraft mc)
	{
		PoseStack poseStack = new PoseStack();
		poseStack.mulPose(modelViewMatrix);
		float heightModifier = (float) Mth.clamp((mc.player.position().y() - 144) / 112, 0, 1);
		float heightModifierDiminish = (1 - heightModifier / 1.5F);
		float skyClearness = 1.0F - level.getRainLevel(partialTicks);
		float starBrightness = level.getStarBrightness(partialTicks) * skyClearness;
		starBrightness += (0.5 - starBrightness) * heightModifier;
		float skaiaBrightness = 0.5F + 0.5F * skyClearness * heightModifier;
		
		Vec3 skyColor = getSkyColor(mc, level, partialTicks);
		float r = (float) skyColor.x * heightModifierDiminish;
		float g = (float) skyColor.y * heightModifierDiminish;
		float b = (float) skyColor.z * heightModifierDiminish;
		
		FogRenderer.levelFogColor();
		Tesselator tesselator = Tesselator.getInstance();
		RenderSystem.depthMask(false);
		RenderSystem.setShaderColor(r, g, b, 1);
		RenderSystem.setShader(GameRenderer::getPositionShader);
		
		Matrix4f matrix = poseStack.last().pose();
		{
			BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
			for(int k = -384; k <= 384; k += 64)
			{
				for(int l = -384; l <= 384; l += 64)
				{
					buffer.addVertex(matrix, k, 16, l);
					buffer.addVertex(matrix, k + 64, 16, l);
					buffer.addVertex(matrix, k + 64, 16, l + 64);
					buffer.addVertex(matrix, k, 16, l + 64);
				}
			}
			BufferUploader.drawWithShader(buffer.buildOrThrow());
			
			RenderSystem.enableBlend();
			RenderSystem.defaultBlendFunc();
		}
		
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, skaiaBrightness);
		
		SessionRenderHelper.drawSkaia(matrix, 20.0F);
		
		if(starBrightness > 0)
		{
			RenderSystem.setShaderColor(starBrightness, starBrightness, starBrightness, starBrightness);
			SessionRenderHelper.drawRotatingVeil(poseStack, level);
			
			RenderSystem.setShaderColor(starBrightness * 2, starBrightness * 2, starBrightness * 2, starBrightness * 2);
			SessionRenderHelper.drawLands(mc, poseStack, level.dimension());
		}
		
		{
			RenderSystem.disableBlend();
			RenderSystem.setShaderColor(0, 0, 0, 1);
			double d3 = mc.player.getEyePosition(partialTicks).y - level.getLevelData().getHorizonHeight(level);
			
			poseStack.pushPose();
			poseStack.translate(0.0, -(d3 - 16.0), 0.0);
			matrix = poseStack.last().pose();
			
			RenderSystem.setShader(GameRenderer::getPositionShader);
			BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
			
			for(int k = -384; k <= 384; k += 64)
			{
				for(int l = -384; l <= 384; l += 64)
				{
					buffer.addVertex(matrix, k + 64, -16, l);
					buffer.addVertex(matrix, k, -16, l);
					buffer.addVertex(matrix, k, -16, l + 64);
					buffer.addVertex(matrix, k + 64, -16, l + 64);
				}
			}
			BufferUploader.drawWithShader(buffer.buildOrThrow());
			poseStack.popPose();
			RenderSystem.setShaderColor(1, 1, 1, 1);
			RenderSystem.depthMask(true);
		}
	}
	
	
	private static Vec3 getSkyColor(Minecraft mc, ClientLevel level, float partialTicks)
	{
		LandProperties properties = ClientDimensionData.getProperties(level);
		if(properties != null)
			return properties.getSkyColor();
		else
			return level.getSkyColor(mc.gameRenderer.getMainCamera().getPosition(), partialTicks);
	}
}

package com.mraof.minestuck.client.renderer;


import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import com.mraof.minestuck.client.ClientDimensionData;
import com.mraof.minestuck.skaianet.LandChain;
import com.mraof.minestuck.skaianet.client.SkaiaClient;
import com.mraof.minestuck.world.lands.LandProperties;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix4f;

import java.util.Random;

public final class ProspitSkyRenderer
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static void render(int ticks, float partialTicks, Matrix4f modelViewMatrix, ClientLevel level, Minecraft mc)
	{
		PoseStack poseStack = new PoseStack();
		poseStack.mulPose(modelViewMatrix);
		float starBrightness = 0.75F;
		float skaiaBrightness = 0.75F;
		
		//FogRenderer.levelFogColor();
		Tesselator tesselator = Tesselator.getInstance();
		RenderSystem.depthMask(false);
		RenderSystem.setShaderColor(1, 1, 1, 1);
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
		float skaiaSize = 50.0F;
		TextureAtlasSprite skaiaSprite = LandSkySpriteUploader.getInstance().getSkaiaSprite();
		drawSprite(matrix, skaiaSize, skaiaSprite);
		
		RenderSystem.setShaderColor(starBrightness, starBrightness, starBrightness, starBrightness);
		poseStack.pushPose();
		poseStack.mulPose(Axis.ZP.rotationDegrees(calculateVeilAngle(level) * 360.0F));
		drawVeil(poseStack.last().pose(), partialTicks, level);
		poseStack.popPose();
		
		//RenderSystem.setShaderColor(starBrightness, starBrightness, starBrightness, starBrightness);
		
		RenderSystem.disableBlend();
		/*RenderSystem.setShaderColor(0, 0, 0, 1);
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
		RenderSystem.setShaderColor(1, 1, 1, 1);*/
		RenderSystem.depthMask(true);
	}
	
	private static float calculateVeilAngle(ClientLevel level)
	{
		double d0 = Mth.frac((double) level.getGameTime() / 24000.0D - 0.25D);
		double d1 = 0.5D - Math.cos(d0 * Math.PI) / 2.0D;
		return (float) (d0 * 2.0D + d1) / 3.0F;
	}
	
	private static void drawVeil(Matrix4f matrix, float partialTicks, ClientLevel level)
	{
		Random random = new Random(10842L);
		
		RenderSystem.setShader(GameRenderer::getPositionShader);
		BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
		
		for(int count = 0; count < 1500; count++)
		{
			float spreadFactor = 0.1F;
			float x = random.nextFloat() * 2 - 1;
			float y = random.nextFloat() * 2 - 1;
			float z = (random.nextFloat() - random.nextFloat()) * spreadFactor;
			float size = 0.15F + random.nextFloat() * 0.1F;
			float l = x * x + y * y + z * z;
			
			if(l < 1.0D && l > 0.01D && Math.abs(z / spreadFactor) < 0.4F)
			{
				l = 1 / (float) Math.sqrt(l);
				x = x * l;
				y = y * l;
				z = z * l;
				float drawnX = x * 100;
				float drawnY = y * 100;
				float drawnZ = z * 100;
				double d8 = Math.atan2(x, z);
				float d9 = (float) Math.sin(d8);
				float d10 = (float) Math.cos(d8);
				double d11 = Math.atan2(Math.sqrt(x * x + z * z), y);
				float d12 = (float) Math.sin(d11);
				float d13 = (float) Math.cos(d11);
				double d14 = random.nextDouble() * Math.PI * 2.0D;
				float d15 = (float) Math.sin(d14);
				float d16 = (float) Math.cos(d14);
				
				for(int vertex = 0; vertex < 4; vertex++)
				{
					float d18 = ((vertex & 2) - 1) * size;
					float d19 = ((vertex + 1 & 2) - 1) * size;
					float d21 = d18 * d16 - d19 * d15;
					float d22 = d19 * d16 + d18 * d15;
					float d24 = -d21 * d13;
					float vertexX = d24 * d9 - d22 * d10;
					float vertexY = d21 * d12;
					float vertexZ = d22 * d9 + d24 * d10;
					buffer.addVertex(matrix, drawnX + vertexX, drawnY + vertexY, drawnZ + vertexZ);
				}
			}
		}
		BufferUploader.drawWithShader(buffer.buildOrThrow());
	}
	
	private static void drawSprite(Matrix4f matrix, float size, TextureAtlasSprite sprite)
	{
		RenderSystem.setShaderTexture(0, sprite.atlasLocation());
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		buffer.addVertex(matrix, -size, 100, -size).setUv(sprite.getU0(), sprite.getV0());
		buffer.addVertex(matrix, size, 100, -size).setUv(sprite.getU1(), sprite.getV0());
		buffer.addVertex(matrix, size, 100, size).setUv(sprite.getU1(), sprite.getV1());
		buffer.addVertex(matrix, -size, 100, size).setUv(sprite.getU0(), sprite.getV1());
		BufferUploader.drawWithShader(buffer.buildOrThrow());
	}
}

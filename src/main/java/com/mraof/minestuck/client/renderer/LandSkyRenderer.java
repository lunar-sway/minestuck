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

public final class LandSkyRenderer
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static void render(int ticks, float partialTicks, PoseStack poseStack, ClientLevel level, Minecraft mc)
	{
		float heightModifier = (float) Mth.clamp((mc.player.position().y() - 144)/112, 0, 1);
		float heightModifierDiminish = (1 - heightModifier/1.5F);
		float skyClearness = 1.0F - level.getRainLevel(partialTicks);
		float starBrightness = level.getStarBrightness(partialTicks) * skyClearness;
		starBrightness += (0.5 - starBrightness)*heightModifier;
		float skaiaBrightness = 0.5F +0.5F*skyClearness*heightModifier;
		
		Vec3 skyColor = getSkyColor(mc, level, partialTicks);
		float r = (float)skyColor.x*heightModifierDiminish;
		float g = (float)skyColor.y*heightModifierDiminish;
		float b = (float)skyColor.z*heightModifierDiminish;
		
		FogRenderer.levelFogColor();
		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder buffer = tesselator.getBuilder();
		RenderSystem.depthMask(false);
		RenderSystem.setShaderColor(r, g, b, 1);
		RenderSystem.setShader(GameRenderer::getPositionShader);
		
		Matrix4f matrix = poseStack.last().pose();
		buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
		for (int k = -384; k <= 384; k += 64)
		{
			for (int l = -384; l <= 384; l += 64)
			{
				buffer.vertex(matrix, k, 16, l).endVertex();
				buffer.vertex(matrix, k + 64, 16, l).endVertex();
				buffer.vertex(matrix, k + 64, 16, l + 64).endVertex();
				buffer.vertex(matrix, k, 16, l + 64).endVertex();
			}
		}
		tesselator.end();
		
		
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, skaiaBrightness);
		
		float skaiaSize = 20.0F;
		TextureAtlasSprite skaiaSprite = LandSkySpriteUploader.getInstance().getSkaiaSprite();
		drawSprite(buffer, matrix, skaiaSize, skaiaSprite);
		
		if(starBrightness > 0)
		{
			RenderSystem.setShaderColor(starBrightness, starBrightness, starBrightness, starBrightness);
			poseStack.pushPose();
			poseStack.mulPose(Axis.ZP.rotationDegrees(calculateVeilAngle(level) * 360.0F));
			drawVeil(poseStack.last().pose(), partialTicks, level);
			poseStack.popPose();
			
			RenderSystem.setShaderColor(starBrightness*2, starBrightness*2, starBrightness*2, starBrightness*2);
			drawLands(mc, poseStack, level.dimension());
		}
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		
		RenderSystem.disableBlend();
		RenderSystem.setShaderColor(0, 0, 0, 1);
		double d3 = mc.player.getEyePosition(partialTicks).y - level.getLevelData().getHorizonHeight(level);
		
		poseStack.pushPose();
		poseStack.translate(0.0, -(d3 - 16.0), 0.0);
		matrix = poseStack.last().pose();
		
		RenderSystem.setShader(GameRenderer::getPositionShader);
		buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
		
		for(int k = -384; k <= 384; k += 64)
		{
			for(int l = -384; l <= 384; l += 64)
			{
				buffer.vertex(matrix, k + 64, -16, l).endVertex();
				buffer.vertex(matrix, k, -16, l).endVertex();
				buffer.vertex(matrix, k, -16, l + 64).endVertex();
				buffer.vertex(matrix, k + 64, -16, l + 64).endVertex();
			}
		}
		tesselator.end();
		poseStack.popPose();
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.depthMask(true);
	}
	
	private static Vec3 getSkyColor(Minecraft mc, ClientLevel level, float partialTicks)
	{
		LandProperties properties = ClientDimensionData.getProperties(level);
		if (properties != null)
			return properties.getSkyColor();
		else
			return level.getSkyColor(mc.gameRenderer.getMainCamera().getPosition(), partialTicks);
	}
	
	private static float calculateVeilAngle(ClientLevel level)
	{
		double d0 = Mth.frac((double)level.getGameTime() / 24000.0D - 0.25D);
		double d1 = 0.5D - Math.cos(d0 * Math.PI) / 2.0D;
		return (float)(d0 * 2.0D + d1) / 3.0F;
	}
	
	private static void drawVeil(Matrix4f matrix, float partialTicks, ClientLevel level)
	{
		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder buffer = tesselator.getBuilder();
		Random random = new Random(10842L);
		
		RenderSystem.setShader(GameRenderer::getPositionShader);
		buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
		
		for(int count = 0; count < 1500; count++)
		{
			float spreadFactor = 0.1F;
			float x = random.nextFloat() * 2 - 1;
			float y = random.nextFloat() * 2 - 1;
			float z = (random.nextFloat() - random.nextFloat())*spreadFactor;
			float size = 0.15F + random.nextFloat() * 0.1F;
			float l = x * x + y * y + z * z;
			
			if (l < 1.0D && l > 0.01D && Math.abs(z/spreadFactor) < 0.4F)
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
					float d24 = - d21 * d13;
					float vertexX = d24 * d9 - d22 * d10;
					float vertexY = d21 * d12;
					float vertexZ = d22 * d9 + d24 * d10;
					buffer.vertex(matrix, drawnX + vertexX, drawnY + vertexY, drawnZ + vertexZ).endVertex();
				}
			}
		}
		tesselator.end();
	}
	
	private static void drawLands(Minecraft mc, PoseStack poseStack, ResourceKey<Level> dim)
	{
		LandChain landChain = SkaiaClient.getLandChain(dim);
		if(landChain == null)
			return;
		
		landChain.relativeAngledLands(dim).forEach(angledLand -> {
			if(angledLand.isZeroAngle() || angledLand.isOppositeAngle())
				return;
			
			drawLand(poseStack, angledLand);
		});
	}
	
	private static void drawLand(PoseStack poseStack, LandChain.AngledLand angledLand)
	{
		Random random = new Random(/*31*mc.world.getSeed() + TODO?*/ angledLand.landId().hashCode());
		LandTypePair landTypes = ClientDimensionData.getLandTypes(angledLand.landId());
		if(landTypes == null)
		{
			LOGGER.warn("Missing land types for dimension {}!", angledLand.landId());
			return;
		}
		int index = random.nextInt(LandSkySpriteUploader.VARIANT_COUNT);
		
		float v = angledLand.skaiaToLandAngle();
		float scale = 1/Mth.cos(v);
		
		BufferBuilder buffer = Tesselator.getInstance().getBuilder();
		poseStack.pushPose();
		poseStack.mulPose(Axis.ZP.rotation(v));
		poseStack.mulPose(Axis.YP.rotationDegrees(90*random.nextInt(4)));
		Matrix4f matrix = poseStack.last().pose();
		
		float planetSize = 4.0F*scale;
		drawSprite(buffer, matrix, planetSize, LandSkySpriteUploader.getInstance().getPlanetSprite(landTypes.getTerrain(), index));
		drawSprite(buffer, matrix, planetSize, LandSkySpriteUploader.getInstance().getOverlaySprite(landTypes.getTitle(), index));
		
		poseStack.popPose();
	}
	
	private static void drawSprite(BufferBuilder buffer, Matrix4f matrix, float size, TextureAtlasSprite sprite)
	{
		RenderSystem.setShaderTexture(0, sprite.atlasLocation());
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		buffer.vertex(matrix, -size, 100, -size).uv(sprite.getU0(), sprite.getV0()).endVertex();
		buffer.vertex(matrix, size, 100, -size).uv(sprite.getU1(), sprite.getV0()).endVertex();
		buffer.vertex(matrix, size, 100, size).uv(sprite.getU1(), sprite.getV1()).endVertex();
		buffer.vertex(matrix, -size, 100, size).uv(sprite.getU0(), sprite.getV1()).endVertex();
		BufferUploader.drawWithShader(buffer.end());
	}
}

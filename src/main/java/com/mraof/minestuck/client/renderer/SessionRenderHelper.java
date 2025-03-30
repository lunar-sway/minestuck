package com.mraof.minestuck.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import com.mraof.minestuck.client.ClientDimensionData;
import com.mraof.minestuck.skaianet.LandChain;
import com.mraof.minestuck.skaianet.client.SkaiaClient;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.util.Random;

/**
 * Holds various rendering functions used for the sky boxes of various dimensions within a Session.
 */
public final class SessionRenderHelper
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static void drawRotatingVeil(PoseStack poseStack, ClientLevel level)
	{
		poseStack.pushPose();
		poseStack.mulPose(getRotationQuaternion(level));
		drawVeil(poseStack.last().pose());
		poseStack.popPose();
	}
	
	public static void drawVeil(Matrix4f matrix)
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
	
	public static void drawRotatingSkaia(PoseStack poseStack, ClientLevel level, float skaiaSize)
	{
		poseStack.pushPose();
		poseStack.mulPose(getRotationQuaternion(level));
		drawSkaia(poseStack.last().pose(), skaiaSize);
		poseStack.popPose();
	}
	
	public static void drawSkaia(Matrix4f matrix, float size)
	{
		TextureAtlasSprite sprite = LandSkySpriteUploader.getInstance().getSkaiaSprite();
		drawSprite(matrix, size, sprite);
	}
	
	public static void drawRotatingProspit(PoseStack poseStack, ClientLevel level, float size)
	{
		poseStack.pushPose();
		poseStack.mulPose(getRotationQuaternion(level));
		drawProspit(poseStack, level, size);
		poseStack.popPose();
	}
	
	public static void drawProspit(PoseStack poseStack, ClientLevel level, float size)
	{
		TextureAtlasSprite sprite = LandSkySpriteUploader.getInstance().getProspitSprite();
		poseStack.pushPose();
		Matrix4f matrix = poseStack.last().pose();
		float rotateSpeed = (level.getGameTime() % 100000F) * 0.000001F;
		matrix.rotateY(rotateSpeed * 360);
		poseStack.mulPose(Axis.XP.rotation(0.26F));
		
		drawSprite(matrix, size, sprite);
		poseStack.popPose();
	}
	
	public static void drawRotatingDerse(PoseStack poseStack, ClientLevel level, float size)
	{
		poseStack.pushPose();
		poseStack.mulPose(getRotationQuaternion(level));
		drawDerse(poseStack, level, size);
		poseStack.popPose();
	}
	
	public static void drawDerse(PoseStack poseStack, ClientLevel level, float size)
	{
		TextureAtlasSprite sprite = LandSkySpriteUploader.getInstance().getDerseSprite();
		poseStack.pushPose();
		Matrix4f matrix = poseStack.last().pose();
		poseStack.mulPose(Axis.ZP.rotationDegrees(180));
		poseStack.mulPose(Axis.XP.rotation(0.05F));
		
		drawSprite(matrix, size, sprite);
		poseStack.popPose();
	}
	
	public static void drawRotatingLands(Minecraft mc, PoseStack poseStack, ClientLevel level)
	{
		poseStack.pushPose();
		poseStack.mulPose(getRotationQuaternion(level));
		drawLands(mc, poseStack, level.dimension());
		poseStack.popPose();
	}
	
	public static void drawLands(Minecraft mc, PoseStack poseStack, ResourceKey<Level> dim)
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
	
	public static void drawLand(PoseStack poseStack, LandChain.AngledLand angledLand)
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
		float scale = 1 / Mth.cos(v);
		
		poseStack.pushPose();
		poseStack.mulPose(Axis.ZP.rotation(v));
		poseStack.mulPose(Axis.YP.rotationDegrees(90 * random.nextInt(4)));
		Matrix4f matrix = poseStack.last().pose();
		
		float planetSize = 4.0F * scale;
		drawSprite(matrix, planetSize, LandSkySpriteUploader.getInstance().getPlanetSprite(landTypes.getTerrain(), index));
		drawSprite(matrix, planetSize, LandSkySpriteUploader.getInstance().getOverlaySprite(landTypes.getTitle(), index));
		
		poseStack.popPose();
	}
	
	public static void drawSprite(Matrix4f matrix, float size, TextureAtlasSprite sprite)
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
	
	public static float calculateRotationDegree(ClientLevel level)
	{
		double d0 = Mth.frac((double) level.getGameTime() / 24000.0D - 0.25D);
		double d1 = 0.5D - Math.cos(d0 * Math.PI) / 2.0D;
		return (float) (d0 * 2.0D + d1) / 3.0F;
	}
	
	private static Quaternionf getRotationQuaternion(ClientLevel level)
	{
		return Axis.ZP.rotationDegrees(calculateRotationDegree(level) * 360.0F);
	}
}

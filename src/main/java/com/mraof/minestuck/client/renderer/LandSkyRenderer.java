package com.mraof.minestuck.client.renderer;


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.client.ClientDimensionData;
import com.mraof.minestuck.skaianet.client.SkaiaClient;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.lands.LandProperties;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.client.ISkyRenderHandler;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.Random;

public class LandSkyRenderer implements ISkyRenderHandler
{
	@Override
	public void render(int ticks, float partialTicks, MatrixStack matrixStack, ClientWorld world, Minecraft mc)
	{
		float heightModifier = (float) MathHelper.clamp((mc.player.position().y() - 144)/112, 0, 1);
		float heightModifierDiminish = (1 - heightModifier/1.5F);
		float skyClearness = 1.0F - world.getRainLevel(partialTicks);
		float starBrightness = world.getStarBrightness(partialTicks) * skyClearness;
		starBrightness += (0.5 - starBrightness)*heightModifier;
		float skaiaBrightness = 0.5F +0.5F*skyClearness*heightModifier;
		
		RenderSystem.disableTexture();
		Vector3d skyColor = getSkyColor(mc, world, partialTicks);
		float r = (float)skyColor.x*heightModifierDiminish;
		float g = (float)skyColor.y*heightModifierDiminish;
		float b = (float)skyColor.z*heightModifierDiminish;
		
		FogRenderer.levelFogColor();
		BufferBuilder buffer = Tessellator.getInstance().getBuilder();
		RenderSystem.depthMask(false);
		RenderSystem.enableFog();
		RenderSystem.color3f(r, g, b);
		
		Matrix4f matrix = matrixStack.last().pose();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
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
		buffer.end();
		WorldVertexBufferUploader.end(buffer);
		
		
		RenderSystem.disableFog();
		RenderSystem.disableAlphaTest();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		
		RenderSystem.enableTexture();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, skaiaBrightness);
		
		float skaiaSize = 20.0F;
		TextureAtlasSprite skaiaSprite = LandSkySpriteUploader.getInstance().getSkaiaSprite();
		drawSprite(mc, buffer, matrix, skaiaSize, skaiaSprite);
		
		if(starBrightness > 0)
		{
			RenderSystem.disableTexture();
			RenderSystem.color4f(starBrightness, starBrightness, starBrightness, starBrightness);
			matrixStack.pushPose();
			matrixStack.mulPose(Vector3f.ZP.rotationDegrees(calculateVeilAngle(world) * 360.0F));
			drawVeil(matrixStack.last().pose(), partialTicks, world);
			matrixStack.popPose();
			
			RenderSystem.color4f(starBrightness*2, starBrightness*2, starBrightness*2, starBrightness*2);
			drawLands(mc, matrixStack, world.dimension());
		}
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		RenderSystem.disableBlend();
		RenderSystem.enableAlphaTest();
		RenderSystem.enableFog();
		RenderSystem.disableTexture();
		RenderSystem.color3f(0.0F, 0.0F, 0.0F);
		double d3 = mc.player.getEyePosition(partialTicks).y - world.getLevelData().getHorizonHeight();
		
		matrixStack.pushPose();
		matrixStack.translate(0.0, -(d3 - 16.0), 0.0);
		matrix = matrixStack.last().pose();
		
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		
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
		buffer.end();
		WorldVertexBufferUploader.end(buffer);
		matrixStack.popPose();
		RenderSystem.enableTexture();
		RenderSystem.depthMask(true);
	}
	
	private Vector3d getSkyColor(Minecraft mc, ClientWorld world, float partialTicks)
	{
		LandProperties properties = ClientDimensionData.getProperties(world);
		if (properties != null)
			return properties.getSkyColor();
		else
			return world.getSkyColor(mc.gameRenderer.getMainCamera().getBlockPosition(), partialTicks);
	}
	
	private static float calculateVeilAngle(ClientWorld world)
	{
		double d0 = MathHelper.frac((double)world.getDayTime() / 24000.0D - 0.25D);
		double d1 = 0.5D - Math.cos(d0 * Math.PI) / 2.0D;
		return (float)(d0 * 2.0D + d1) / 3.0F;
	}
	
	private void drawVeil(Matrix4f matrix, float partialTicks, ClientWorld world)
	{
		BufferBuilder buffer = Tessellator.getInstance().getBuilder();
		Random random = new Random(10842L);
		
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		
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
		buffer.end();
		WorldVertexBufferUploader.end(buffer);
	}
	
	private void drawLands(Minecraft mc, MatrixStack matrixStack, RegistryKey<World> dim)
	{
		List<RegistryKey<World>> list = SkaiaClient.getLandChain(dim);
		if(list == null)
			return;
		int index = list.indexOf(dim);
		RenderSystem.enableTexture();
		for(int i = 1; i < list.size(); i++)
		{
			RegistryKey<World> landName = list.get((index + i)%list.size());
			if(landName != null)
			{
				Random random = new Random(/*31*mc.world.getSeed() + TODO?*/ landName.hashCode());
				LandTypePair landTypes = ClientDimensionData.getLandTypes(landName);
				if(landTypes == null)
					Debug.warnf("Missing land types for dimension %s!", landName);
				else drawLand(mc, matrixStack, landTypes, (i / (float) list.size()), random);
			}
		}
		RenderSystem.disableTexture();
	}
	
	private void drawLand(Minecraft mc, MatrixStack matrixStack, LandTypePair aspects, float pos, Random random)
	{
		if(pos == 0.5F || aspects == null)
			return;
		
		int index = random.nextInt(LandSkySpriteUploader.VARIANT_COUNT);
		
		float v = (float) Math.PI*(0.5F - pos);
		float scale = 1/MathHelper.cos(v);
		
		BufferBuilder buffer = Tessellator.getInstance().getBuilder();
		matrixStack.pushPose();
		matrixStack.mulPose(Vector3f.ZP.rotation(v));
		matrixStack.mulPose(Vector3f.YP.rotationDegrees(90*random.nextInt(4)));
		Matrix4f matrix = matrixStack.last().pose();
		
		float planetSize = 4.0F*scale;
		drawSprite(mc, buffer, matrix, planetSize, LandSkySpriteUploader.getInstance().getPlanetSprite(aspects.getTerrain(), index));
		drawSprite(mc, buffer, matrix, planetSize, LandSkySpriteUploader.getInstance().getOverlaySprite(aspects.getTitle(), index));
		
		matrixStack.popPose();
	}
	
	private void drawSprite(Minecraft mc, BufferBuilder buffer, Matrix4f matrix, float size, TextureAtlasSprite sprite)
	{
		mc.getTextureManager().bind(sprite.atlas().location());
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buffer.vertex(matrix, -size, 100, -size).uv(sprite.getU0(), sprite.getV0()).endVertex();
		buffer.vertex(matrix, size, 100, -size).uv(sprite.getU1(), sprite.getV0()).endVertex();
		buffer.vertex(matrix, size, 100, size).uv(sprite.getU1(), sprite.getV1()).endVertex();
		buffer.vertex(matrix, -size, 100, size).uv(sprite.getU0(), sprite.getV1()).endVertex();
		buffer.end();
		WorldVertexBufferUploader.end(buffer);
	}
}
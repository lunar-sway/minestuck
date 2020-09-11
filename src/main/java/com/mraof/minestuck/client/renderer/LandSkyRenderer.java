package com.mraof.minestuck.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.skaianet.SkaiaClient;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.LandDimension;
import com.mraof.minestuck.world.MSDimensionTypes;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.client.SkyRenderHandler;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.Random;

public class LandSkyRenderer implements SkyRenderHandler
{
	private LandDimension dimension;
	public LandSkyRenderer(LandDimension provider)
	{
		dimension = provider;
	}
	
	@Override
	public void render(int ticks, float partialTicks, MatrixStack matrixStack, ClientWorld world, Minecraft mc)
	{
		float heightModifier = (float) MathHelper.clamp((mc.player.getPosY() - 144)/112, 0, 1);
		float heightModifierDiminish = (1 - heightModifier/1.5F);
		float skyClearness = 1.0F - world.getRainStrength(partialTicks);
		float starBrightness = world.getStarBrightness(partialTicks) * skyClearness;
		starBrightness += (0.5 - starBrightness)*heightModifier;
		float skaiaBrightness = 0.5F +0.5F*skyClearness*heightModifier;
		
		RenderSystem.disableTexture();
		Vec3d vec3d = dimension.getSkyColor();//world.getSkyColor(mc.gameRenderer.getActiveRenderInfo().getBlockPos(), partialTicks);
		float r = (float)vec3d.x*heightModifierDiminish;
		float g = (float)vec3d.y*heightModifierDiminish;
		float b = (float)vec3d.z*heightModifierDiminish;
		
		FogRenderer.applyFog();
		BufferBuilder buffer = Tessellator.getInstance().getBuffer();
		RenderSystem.depthMask(false);
		RenderSystem.enableFog();
		RenderSystem.color3f(r, g, b);
		
		Matrix4f matrix = matrixStack.getLast().getMatrix();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		for (int k = -384; k <= 384; k += 64)
		{
			for (int l = -384; l <= 384; l += 64)
			{
				buffer.pos(matrix, k, 16, l).endVertex();
				buffer.pos(matrix, k + 64, 16, l).endVertex();
				buffer.pos(matrix, k + 64, 16, l + 64).endVertex();
				buffer.pos(matrix, k, 16, l + 64).endVertex();
			}
		}
		buffer.finishDrawing();
		WorldVertexBufferUploader.draw(buffer);
		
		
		RenderSystem.disableFog();
		RenderSystem.disableAlphaTest();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderHelper.disableStandardItemLighting();
		
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
			matrixStack.push();
			matrixStack.rotate(Vector3f.ZP.rotationDegrees(dimension.calculateVeilAngle() * 360.0F));
			drawVeil(matrixStack.getLast().getMatrix(), partialTicks, world);
			matrixStack.pop();
			
			RenderSystem.color4f(starBrightness*2, starBrightness*2, starBrightness*2, starBrightness*2);
			drawLands(mc, matrixStack, world.getDimension().getType());
		}
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		RenderSystem.disableBlend();
		RenderSystem.enableAlphaTest();
		RenderSystem.enableFog();
		RenderSystem.disableTexture();
		RenderSystem.color3f(0.0F, 0.0F, 0.0F);
		double d3 = mc.player.getEyePosition(partialTicks).y - world.getHorizonHeight();
		
		matrixStack.push();
		matrixStack.translate(0.0, -(d3 - 16.0), 0.0);
		matrix = matrixStack.getLast().getMatrix();
		
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		
		for(int k = -384; k <= 384; k += 64)
		{
			for(int l = -384; l <= 384; l += 64)
			{
				buffer.pos(matrix, k + 64, -16, l).endVertex();
				buffer.pos(matrix, k, -16, l).endVertex();
				buffer.pos(matrix, k, -16, l + 64).endVertex();
				buffer.pos(matrix, k + 64, -16, l + 64).endVertex();
			}
		}
		buffer.finishDrawing();
		WorldVertexBufferUploader.draw(buffer);
		matrixStack.pop();
		RenderSystem.enableTexture();
		RenderSystem.depthMask(true);
	}
	
	private void drawVeil(Matrix4f matrix, float partialTicks, ClientWorld world)
	{
		BufferBuilder buffer = Tessellator.getInstance().getBuffer();
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
					buffer.pos(matrix, drawnX + vertexX, drawnY + vertexY, drawnZ + vertexZ).endVertex();
				}
			}
		}
		buffer.finishDrawing();
		WorldVertexBufferUploader.draw(buffer);
	}
	
	private void drawLands(Minecraft mc, MatrixStack matrixStack, DimensionType dim)
	{
		List<ResourceLocation> list = SkaiaClient.getLandChain(dim);
		if(list == null)
			return;
		int index = list.indexOf(dim.getRegistryName());
		RenderSystem.enableTexture();
		for(int i = 1; i < list.size(); i++)
		{
			ResourceLocation landName = list.get((index + i)%list.size());
			if(landName != null)
			{
				Random random = new Random(31*mc.world.getSeed() + landName.hashCode());
				LandTypePair.LazyInstance landTypes = MSDimensionTypes.LANDS.dimToLandTypes.get(landName);
				if(landTypes == null)
					Debug.warnf("Missing land types for dimension %s!", landName);
				else drawLand(mc, matrixStack, landTypes.create(), (i / (float) list.size()), random);
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
		
		BufferBuilder buffer = Tessellator.getInstance().getBuffer();
		matrixStack.push();
		matrixStack.rotate(Vector3f.ZP.rotation(v));
		matrixStack.rotate(Vector3f.YP.rotationDegrees(90*random.nextInt(4)));
		Matrix4f matrix = matrixStack.getLast().getMatrix();
		
		float planetSize = 4.0F*scale;
		drawSprite(mc, buffer, matrix, planetSize, LandSkySpriteUploader.getInstance().getPlanetSprite(aspects.terrain, index));
		drawSprite(mc, buffer, matrix, planetSize, LandSkySpriteUploader.getInstance().getOverlaySprite(aspects.title, index));
		
		matrixStack.pop();
	}
	
	private void drawSprite(Minecraft mc, BufferBuilder buffer, Matrix4f matrix, float size, TextureAtlasSprite sprite)
	{
		mc.getTextureManager().bindTexture(sprite.getAtlasTexture().getTextureLocation());
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(matrix, -size, 100, -size).tex(sprite.getMinU(), sprite.getMinV()).endVertex();
		buffer.pos(matrix, size, 100, -size).tex(sprite.getMaxU(), sprite.getMinV()).endVertex();
		buffer.pos(matrix, size, 100, size).tex(sprite.getMaxU(), sprite.getMaxV()).endVertex();
		buffer.pos(matrix, -size, 100, size).tex(sprite.getMinU(), sprite.getMaxV()).endVertex();
		buffer.finishDrawing();
		WorldVertexBufferUploader.draw(buffer);
	}
}
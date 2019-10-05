package com.mraof.minestuck.client.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mraof.minestuck.network.skaianet.SkaiaClient;
import com.mraof.minestuck.world.lands.LandAspects;
import com.mraof.minestuck.world.lands.LandDimension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.client.IRenderHandler;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class LandSkyRenderer implements IRenderHandler
{
	
	private static final ResourceLocation SKAIA_TEXTURE = new ResourceLocation("minestuck", "textures/environment/skaia.png");
	
	private LandDimension providerLands;
	public LandSkyRenderer(LandDimension provider)
	{
		providerLands = provider;
	}
	
	@Override
	public void render(int ticks, float partialTicks, ClientWorld world, Minecraft mc)
	{
		float heightModifier = (float) MathHelper.clamp((mc.player.posY - 144)/112, 0, 1);
		float heightModifierDiminish = (1 - heightModifier/1.5F);
		float skyClearness = 1.0F - world.getRainStrength(partialTicks);
		float starBrightness = world.getStarBrightness(partialTicks) * skyClearness;
		starBrightness += (0.5 - starBrightness)*heightModifier;
		float skaiaBrightness = 0.5F +0.5F*skyClearness*heightModifier;
		
		GlStateManager.disableTexture();
		Vec3d vec3d = world.getSkyColor(mc.getRenderViewEntity().getPosition(), partialTicks);
		float r = (float)vec3d.x*heightModifierDiminish;
		float g = (float)vec3d.y*heightModifierDiminish;
		float b = (float)vec3d.z*heightModifierDiminish;
		
		GlStateManager.color3f(r, g, b);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		GlStateManager.depthMask(false);
		GlStateManager.enableFog();
		GlStateManager.color3f(r, g, b);
		
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
		for (int k = -384; k <= 384; k += 64)
		{
			for (int l = -384; l <= 384; l += 64)
			{
				bufferbuilder.pos((double)k, (double)16, (double)l).endVertex();
				bufferbuilder.pos((double)k + 64, (double)16, (double)l).endVertex();
				bufferbuilder.pos((double)k + 64, (double)16, (double)(l + 64)).endVertex();
				bufferbuilder.pos((double)k, (double)16, (double)(l + 64)).endVertex();
			}
		}
		tessellator.draw();
		
		GlStateManager.disableFog();
		GlStateManager.disableAlphaTest();
		GlStateManager.enableBlend();
		GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		RenderHelper.disableStandardItemLighting();
		//
		
		GlStateManager.enableTexture();
		GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, skaiaBrightness);
		float skaiaSize = 20.0F;
		mc.getTextureManager().bindTexture(SKAIA_TEXTURE);
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos((double)(-skaiaSize), 100.0D, (double)(-skaiaSize)).tex(0.0D, 0.0D).endVertex();
		bufferbuilder.pos((double)skaiaSize, 100.0D, (double)(-skaiaSize)).tex(1.0D, 0.0D).endVertex();
		bufferbuilder.pos((double)skaiaSize, 100.0D, (double)skaiaSize).tex(1.0D, 1.0D).endVertex();
		bufferbuilder.pos((double)(-skaiaSize), 100.0D, (double)skaiaSize).tex(0.0D, 1.0D).endVertex();
		tessellator.draw();
		GlStateManager.disableTexture();
		
		if(starBrightness > 0)
		{
			GlStateManager.color4f(starBrightness, starBrightness, starBrightness, starBrightness);
			
			GlStateManager.pushMatrix();
			GlStateManager.rotatef(world.getCelestialAngle(partialTicks) * 360.0F, 0, 0, 1);
			drawVeil(partialTicks, world);
			GlStateManager.popMatrix();
			GlStateManager.color4f(starBrightness*2, starBrightness*2, starBrightness*2, starBrightness*2);
			
			drawLands(mc, world.getDimension().getType());
		}
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		GlStateManager.disableBlend();
		GlStateManager.enableAlphaTest();
		GlStateManager.enableFog();
		GlStateManager.disableTexture();
		GlStateManager.color3f(0.0F, 0.0F, 0.0F);
		double d3 = mc.player.getEyePosition(partialTicks).y - world.getHorizon();
		
		//
		
		GlStateManager.pushMatrix();
		GlStateManager.translatef(0.0F, -((float)(d3 - 16.0D)), 0.0F);
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
		
		for (int k = -384; k <= 384; k += 64)
		{
			for (int l = -384; l <= 384; l += 64)
			{
				bufferbuilder.pos((double)k + 64, (double)-16, (double)l).endVertex();
				bufferbuilder.pos((double)k, (double)-16, (double)l).endVertex();
				bufferbuilder.pos((double)k, (double)-16, (double)(l + 64)).endVertex();
				bufferbuilder.pos((double)k + 64, (double)-16, (double)(l + 64)).endVertex();
			}
		}
		tessellator.draw();
		GlStateManager.popMatrix();
		GlStateManager.enableTexture();
		GlStateManager.depthMask(true);
	}
	
	private void drawVeil(float partialTicks, ClientWorld world)
	{
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		Random random = new Random(10842L);
		
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
		
		for (int i = 0; i < 1500; ++i)
		{
			float spreadFactor = 0.1F;
			double x = (double)(random.nextFloat() * 2.0F - 1.0F);
			double y = (double)(random.nextFloat() * 2.0F - 1.0F);
			double z = (double)(random.nextFloat() - random.nextFloat())*spreadFactor;
			double d3 = (double)(0.15F + random.nextFloat() * 0.1F);
			double l = x * x + y * y + z * z;
			
			if (l < 1.0D && l > 0.01D && Math.abs(z/spreadFactor) < 0.4F)
			{
				l = 1.0D / Math.sqrt(l);
				x = x * l;
				y = y * l;
				z = z * l;
				double d5 = x * 100.0D;
				double d6 = y * 100.0D;
				double d7 = z * 100.0D;
				double d8 = Math.atan2(x, z);
				double d9 = Math.sin(d8);
				double d10 = Math.cos(d8);
				double d11 = Math.atan2(Math.sqrt(x * x + z * z), y);
				double d12 = Math.sin(d11);
				double d13 = Math.cos(d11);
				double d14 = random.nextDouble() * Math.PI * 2.0D;
				double d15 = Math.sin(d14);
				double d16 = Math.cos(d14);
				
				for (int j = 0; j < 4; ++j)
				{
					double d17 = 0.0D;
					double d18 = (double)((j & 2) - 1) * d3;
					double d19 = (double)((j + 1 & 2) - 1) * d3;
					double d20 = 0.0D;
					double d21 = d18 * d16 - d19 * d15;
					double d22 = d19 * d16 + d18 * d15;
					double d23 = d21 * d12 + 0.0D * d13;
					double d24 = 0.0D * d12 - d21 * d13;
					double d25 = d24 * d9 - d22 * d10;
					double d26 = d22 * d9 + d24 * d10;
					bufferbuilder.pos(d5 + d25, d6 + d23, d7 + d26).endVertex();
				}
			}
		}
		tessellator.draw();
	}
	
	public void drawLands(Minecraft mc, DimensionType dim)
	{
		List<DimensionType> list = SkaiaClient.getLandChain(dim);
		if(list == null)
			return;
		int index = list.indexOf(dim);
		GlStateManager.enableTexture();
		for(int i = 1; i < list.size(); i++)
		{
			DimensionType type = list.get((index + i)%list.size());
			if(type != null)
			{
				Random random = new Random(mc.world.getSeed() + type.getId());
				//drawLand(mc, getResourceLocations(MinestuckDimensionHandler.getAspects(type), random), (i / (float) list.size()), random);
			}
		}
		GlStateManager.disableTexture();
	}
	
	public void drawLand(Minecraft mc, ResourceLocation[] textures, float pos, Random random)
	{
		if(pos == 0.5F || textures == null)
			return;
		
		float v = (float) Math.PI*(0.5F - pos);
		float scale = 1/MathHelper.cos(v);
		
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		GlStateManager.pushMatrix();
		GlStateManager.rotatef((float) (180/Math.PI * v), 0, 0, 1);
		GlStateManager.rotatef((float) 90*random.nextInt(4), 0, 1, 0);
		
		float planetSize = 4.0F*scale;
		mc.getTextureManager().bindTexture(textures[0]);
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos((double)(-planetSize), 100.0D, (double)(-planetSize)).tex(0.0D, 0.0D).endVertex();
		bufferbuilder.pos((double)planetSize, 100.0D, (double)(-planetSize)).tex(1.0D, 0.0D).endVertex();
		bufferbuilder.pos((double)planetSize, 100.0D, (double)planetSize).tex(1.0D, 1.0D).endVertex();
		bufferbuilder.pos((double)(-planetSize), 100.0D, (double)planetSize).tex(0.0D, 1.0D).endVertex();
		tessellator.draw();
		mc.getTextureManager().bindTexture(textures[1]);
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos((double)(-planetSize), 100.0D, (double)(-planetSize)).tex(0.0D, 0.0D).endVertex();
		bufferbuilder.pos((double)planetSize, 100.0D, (double)(-planetSize)).tex(1.0D, 0.0D).endVertex();
		bufferbuilder.pos((double)planetSize, 100.0D, (double)planetSize).tex(1.0D, 1.0D).endVertex();
		bufferbuilder.pos((double)(-planetSize), 100.0D, (double)planetSize).tex(0.0D, 1.0D).endVertex();
		tessellator.draw();
		GlStateManager.popMatrix();
	}
	
	public ResourceLocation[] getResourceLocations(LandAspects aspects, Random random)
	{
		if(aspects == null)
			return null;
		
		int index = random.nextInt(3);
		ResourceLocation terrainName = Objects.requireNonNull(aspects.terrain.getRegistryName());
		ResourceLocation titleName = Objects.requireNonNull(aspects.title.getRegistryName());
		ResourceLocation terrain = new ResourceLocation(terrainName.getNamespace(), "textures/environment/planets/planet_"+terrainName.getPath()+"_"+index+".png");
		ResourceLocation title = new ResourceLocation(titleName.getNamespace(), "textures/environment/overlays/overlay_"+titleName.getPath()+"_"+index+".png");
		return new ResourceLocation[] {terrain, title};
	}
}
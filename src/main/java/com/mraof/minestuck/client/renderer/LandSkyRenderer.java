package com.mraof.minestuck.client.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.skaianet.SkaiaClient;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.LandDimension;
import com.mraof.minestuck.world.MSDimensionTypes;
import com.mraof.minestuck.world.lands.LandTypePair;
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
		float heightModifier = (float) MathHelper.clamp((mc.player.getPosY() - 144)/112, 0, 1);
		float heightModifierDiminish = (1 - heightModifier/1.5F);
		float skyClearness = 1.0F - world.getRainStrength(partialTicks);
		float starBrightness = world.getStarBrightness(partialTicks) * skyClearness;
		starBrightness += (0.5 - starBrightness)*heightModifier;
		float skaiaBrightness = 0.5F +0.5F*skyClearness*heightModifier;
		
		RenderSystem.disableTexture();
		Vec3d vec3d = world.getSkyColor(mc.getRenderViewEntity().getPosition(), partialTicks);
		float r = (float)vec3d.x*heightModifierDiminish;
		float g = (float)vec3d.y*heightModifierDiminish;
		float b = (float)vec3d.z*heightModifierDiminish;
		
		RenderSystem.color3f(r, g, b);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		RenderSystem.depthMask(false);
		RenderSystem.enableFog();
		RenderSystem.color3f(r, g, b);
		
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
		for (int k = -384; k <= 384; k += 64)
		{
			for (int l = -384; l <= 384; l += 64)
			{
				bufferbuilder.pos(k, 16, l).endVertex();
				bufferbuilder.pos((double)k + 64, 16, l).endVertex();
				bufferbuilder.pos((double)k + 64, 16, l + 64).endVertex();
				bufferbuilder.pos(k, 16, l + 64).endVertex();
			}
		}
		tessellator.draw();
		
		RenderSystem.disableFog();
		RenderSystem.disableAlphaTest();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderHelper.disableStandardItemLighting();
		
		RenderSystem.enableTexture();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, skaiaBrightness);
		float skaiaSize = 20.0F;
		mc.getTextureManager().bindTexture(SKAIA_TEXTURE);
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(-skaiaSize, 100.0D, -skaiaSize).tex(0.0F, 0.0F).endVertex();
		bufferbuilder.pos(skaiaSize, 100.0D, -skaiaSize).tex(1.0F, 0.0F).endVertex();
		bufferbuilder.pos(skaiaSize, 100.0D, skaiaSize).tex(1.0F, 1.0F).endVertex();
		bufferbuilder.pos(-skaiaSize, 100.0D, skaiaSize).tex(0.0F, 1.0F).endVertex();
		tessellator.draw();
		RenderSystem.disableTexture();
		
		if(starBrightness > 0)
		{
			RenderSystem.color4f(starBrightness, starBrightness, starBrightness, starBrightness);
			
			RenderSystem.pushMatrix();
			RenderSystem.rotatef(world.getCelestialAngle(partialTicks) * 360.0F, 0, 0, 1);
			drawVeil(partialTicks, world);
			RenderSystem.popMatrix();
			RenderSystem.color4f(starBrightness*2, starBrightness*2, starBrightness*2, starBrightness*2);
			
			drawLands(mc, world.getDimension().getType());
		}
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		RenderSystem.disableBlend();
		RenderSystem.enableAlphaTest();
		RenderSystem.enableFog();
		RenderSystem.disableTexture();
		RenderSystem.color3f(0.0F, 0.0F, 0.0F);
		double d3 = mc.player.getEyePosition(partialTicks).y - world.getHorizonHeight();
		
		//
		
		RenderSystem.pushMatrix();
		RenderSystem.translatef(0.0F, -((float)(d3 - 16.0D)), 0.0F);
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
		
		for(int k = -384; k <= 384; k += 64)
		{
			for(int l = -384; l <= 384; l += 64)
			{
				bufferbuilder.pos((double)k + 64, -16, l).endVertex();
				bufferbuilder.pos(k, -16, l).endVertex();
				bufferbuilder.pos(k, -16, l + 64).endVertex();
				bufferbuilder.pos((double)k + 64, -16, l + 64).endVertex();
			}
		}
		tessellator.draw();
		RenderSystem.popMatrix();
		RenderSystem.enableTexture();
		RenderSystem.depthMask(true);
	}
	
	private void drawVeil(float partialTicks, ClientWorld world)
	{
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		Random random = new Random(10842L);
		
		buffer.begin(7, DefaultVertexFormats.POSITION);
		
		for(int count = 0; count < 1500; count++)
		{
			float spreadFactor = 0.1F;
			double x = random.nextFloat() * 2.0 - 1.0;
			double y = random.nextFloat() * 2.0 - 1.0;
			double z = (double)(random.nextFloat() - random.nextFloat())*spreadFactor;
			double size = 0.15 + random.nextFloat() * 0.1;
			double l = x * x + y * y + z * z;
			
			if (l < 1.0D && l > 0.01D && Math.abs(z/spreadFactor) < 0.4F)
			{
				l = 1.0D / Math.sqrt(l);
				x = x * l;
				y = y * l;
				z = z * l;
				double drawnX = x * 100.0D;
				double drawnY = y * 100.0D;
				double drawnZ = z * 100.0D;
				double d8 = Math.atan2(x, z);
				double d9 = Math.sin(d8);
				double d10 = Math.cos(d8);
				double d11 = Math.atan2(Math.sqrt(x * x + z * z), y);
				double d12 = Math.sin(d11);
				double d13 = Math.cos(d11);
				double d14 = random.nextDouble() * Math.PI * 2.0D;
				double d15 = Math.sin(d14);
				double d16 = Math.cos(d14);
				
				for(int vertex = 0; vertex < 4; vertex++)
				{
					double d18 = (double)((vertex & 2) - 1) * size;
					double d19 = (double)((vertex + 1 & 2) - 1) * size;
					double d21 = d18 * d16 - d19 * d15;
					double d22 = d19 * d16 + d18 * d15;
					double d24 = - d21 * d13;
					double vertexX = d24 * d9 - d22 * d10;
					double vertexY = d21 * d12;
					double vertexZ = d22 * d9 + d24 * d10;
					buffer.pos(drawnX + vertexX, drawnY + vertexY, drawnZ + vertexZ).endVertex();
				}
			}
		}
		tessellator.draw();
	}
	
	private void drawLands(Minecraft mc, DimensionType dim)
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
				else drawLand(mc, getResourceLocations(landTypes.create(), random), (i / (float) list.size()), random);
			}
		}
		RenderSystem.disableTexture();
	}
	
	private void drawLand(Minecraft mc, ResourceLocation[] textures, float pos, Random random)
	{
		if(pos == 0.5F || textures == null)
			return;
		
		float v = (float) Math.PI*(0.5F - pos);
		float scale = 1/MathHelper.cos(v);
		
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		RenderSystem.pushMatrix();
		RenderSystem.rotatef((float) (180/Math.PI * v), 0, 0, 1);
		RenderSystem.rotatef((float) 90*random.nextInt(4), 0, 1, 0);
		
		float planetSize = 4.0F*scale;
		mc.getTextureManager().bindTexture(textures[0]);
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(-planetSize, 100.0D, -planetSize).tex(0.0F, 0.0F).endVertex();
		bufferbuilder.pos(planetSize, 100.0D, -planetSize).tex(1.0F, 0.0F).endVertex();
		bufferbuilder.pos(planetSize, 100.0D, planetSize).tex(1.0F, 1.0F).endVertex();
		bufferbuilder.pos(-planetSize, 100.0D, planetSize).tex(0.0F, 1.0F).endVertex();
		tessellator.draw();
		mc.getTextureManager().bindTexture(textures[1]);
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(-planetSize, 100.0D, -planetSize).tex(0.0F, 0.0F).endVertex();
		bufferbuilder.pos(planetSize, 100.0D, -planetSize).tex(1.0F, 0.0F).endVertex();
		bufferbuilder.pos(planetSize, 100.0D, planetSize).tex(1.0F, 1.0F).endVertex();
		bufferbuilder.pos(-planetSize, 100.0D, planetSize).tex(0.0F, 1.0F).endVertex();
		tessellator.draw();
		RenderSystem.popMatrix();
	}
	
	private ResourceLocation[] getResourceLocations(LandTypePair aspects, Random random)
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
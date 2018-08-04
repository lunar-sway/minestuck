package com.mraof.minestuck.client.renderer;

import com.mraof.minestuck.world.WorldProviderLands;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.IRenderHandler;

public class LandSkyRender extends IRenderHandler
{
	
	private static final ResourceLocation SKAIA_TEXTURE = new ResourceLocation("minestuck", "textures/environment/skaia.png");
	
	private WorldProviderLands providerLands;
	
	public LandSkyRender(WorldProviderLands provider)
	{
		providerLands = provider;
	}
	
	@Override
	public void render(float partialTicks, WorldClient world, Minecraft mc)
	{
		GlStateManager.disableTexture2D();
		Vec3d vec3d = world.getSkyColor(mc.getRenderViewEntity(), partialTicks);
		float f = (float)vec3d.x;
		float f1 = (float)vec3d.y;
		float f2 = (float)vec3d.z;
		
		GlStateManager.color(f, f1, f2);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		GlStateManager.depthMask(false);
		GlStateManager.enableFog();
		GlStateManager.color(f, f1, f2);
		
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
		GlStateManager.disableAlpha();
		GlStateManager.enableBlend();
		//GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		RenderHelper.disableStandardItemLighting();
		//
		
		GlStateManager.enableTexture2D();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.pushMatrix();
		float f16 = 1.0F - world.getRainStrength(partialTicks);
		GlStateManager.color(1.0F, 1.0F, 1.0F, f16);
		
		float f17 = 30.0F;
		mc.getTextureManager().bindTexture(SKAIA_TEXTURE);
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos((double)(-f17), 100.0D, (double)(-f17)).tex(0.0D, 0.0D).endVertex();
		bufferbuilder.pos((double)f17, 100.0D, (double)(-f17)).tex(1.0D, 0.0D).endVertex();
		bufferbuilder.pos((double)f17, 100.0D, (double)f17).tex(1.0D, 1.0D).endVertex();
		bufferbuilder.pos((double)(-f17), 100.0D, (double)f17).tex(0.0D, 1.0D).endVertex();
		tessellator.draw();
		
		//
		
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableFog();
		GlStateManager.popMatrix();
		GlStateManager.disableTexture2D();
		GlStateManager.color(0.0F, 0.0F, 0.0F);
		double d3 = mc.player.getPositionEyes(partialTicks).y - world.getHorizon();
		
		//
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(0.0F, -((float)(d3 - 16.0D)), 0.0F);
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
		GlStateManager.enableTexture2D();
		GlStateManager.depthMask(true);
	}
}
package com.mraof.minestuck.client.gui.playerStats;

import java.awt.Point;
import java.io.IOException;
import java.util.Arrays;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.common.Loader;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIClientConfig;
import codechicken.nei.recipe.GuiCraftingRecipe;

import com.mraof.minestuck.client.gui.GuiHandler;
import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.GristType;
import com.mraof.minestuck.util.MinestuckPlayerData;

public class GuiGristCache extends GuiPlayerStats
{
	
	private static final ResourceLocation guiGristcache = new ResourceLocation("minestuck", "textures/gui/GristCache.png");
	
	private static final int gristIconX = 21, gristIconY = 32;
	private static final int gristDisplayXOffset = 66, gristDisplayYOffset = 21;
	private static final int gristCountX = 44, gristCountY = 36;
	
	public GuiGristCache()
	{
		super();
		guiWidth = 226;
		guiHeight = 190;
	}
	
	@Override
	public void drawScreen(int xcor, int ycor, float par3) {
		super.drawScreen(xcor, ycor, par3);
		this.drawDefaultBackground();
		
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		
		drawTabs();
		
		this.mc.getTextureManager().bindTexture(guiGristcache);
		this.drawTexturedModalRect(xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		
		String cacheMessage;
		if(ClientEditHandler.isActive() || MinestuckPlayerData.title == null)
			cacheMessage = StatCollector.translateToLocal("gui.gristCache.name");
		else cacheMessage = MinestuckPlayerData.title.getTitleName();
		mc.fontRendererObj.drawString(cacheMessage, (this.width / 2) - mc.fontRendererObj.getStringWidth(cacheMessage) / 2, yOffset + 12, 0x404040);
		
		drawActiveTabAndOther(xcor, ycor);
		
		GlStateManager.color(1,1,1);
		GlStateManager.disableRescaleNormal();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
		
		int tooltip = -1;
		
		GristSet clientGrist = MinestuckPlayerData.getClientGrist();
		for(int gristId = 0; gristId < GristType.allGrists; gristId++)
		{
			int row = (int) (gristId / 7);
			int column = (int) (gristId % 7);
			int gristXOffset = xOffset + (gristDisplayXOffset * row - row);
			int gristYOffset = yOffset + (gristDisplayYOffset * column - column);
			String amount = GuiHandler.addSuffix(clientGrist.getGrist(GristType.values()[gristId]));

			if(this.isPointInRegion(gristXOffset + gristIconX, gristYOffset + gristIconY, 16, 16, xcor, ycor))
			{
				this.drawGradientRect(gristXOffset + gristIconX, gristYOffset + gristIconY, gristXOffset + gristIconX + 16, gristYOffset + gristIconY + 17, -2130706433, -2130706433);
				tooltip = gristId*2;
			}
			if(!String.valueOf(clientGrist.getGrist(GristType.values()[gristId])).equals(amount)
					&& this.isPointInRegion(gristXOffset + gristCountX - 1, gristYOffset + gristCountY - 1, 35, 10, xcor, ycor))
				tooltip = gristId*2 + 1;
			
			this.drawIcon(gristXOffset + gristIconX, gristYOffset + gristIconY, "textures/grist/" + GristType.values()[gristId].getName()+ ".png");
			mc.fontRendererObj.drawString(amount, gristXOffset + gristCountX, gristYOffset + gristCountY, 0xddddee);
			
		}
		
		if (tooltip != -1)
			if(tooltip % 2 == 0)
				drawHoveringText(Arrays.asList(StatCollector.translateToLocalFormatted("grist.format", GristType.values()[tooltip/2].getDisplayName())),
						xcor, ycor, fontRendererObj);
			else drawHoveringText(Arrays.asList(String.valueOf(clientGrist.getGrist(GristType.values()[tooltip/2]))), xcor, ycor, fontRendererObj);
	}
	
	private void drawIcon(int x,int y,String location) 
	{
		this.mc.getTextureManager().bindTexture(new ResourceLocation("minestuck",location));

		float scale = (float) 1/16;

		int iconX = 16;
		int iconY = 16;
		int iconU = 0;
		int iconV = 0;

		WorldRenderer render = Tessellator.getInstance().getWorldRenderer();
		render.startDrawingQuads();
		render.addVertexWithUV((double)(x + 0), (double)(y +  iconY), (double)this.zLevel, (double)((float)(iconU + 0) * scale), (double)((float)(iconV +  iconY) * scale));
		render.addVertexWithUV((double)(x + iconX), (double)(y +  iconY), (double)this.zLevel, (double)((float)(iconU + iconX) * scale), (double)((float)(iconV +  iconY) * scale));
		render.addVertexWithUV((double)(x + iconX), (double)(y + 0), (double)this.zLevel, (double)((float)(iconU + iconX) * scale), (double)((float)(iconV + 0) * scale));
		render.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)this.zLevel, (double)((float)(iconU + 0) * scale), (double)((float)(iconV + 0) * scale));
		Tessellator.getInstance().draw();
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		super.keyTyped(typedChar, keyCode);
		if(Loader.isModLoaded("NotEnoughItems") && keyCode == NEIClientConfig.getKeyBinding("gui.usage"))
		{
			Point mousePos = GuiDraw.getMousePosition();
			for(int gristId = 0; gristId < GristType.allGrists; gristId++)
			{
				int row = (int) (gristId / 7);
				int column = (int) (gristId % 7);
				int gristXOffset = xOffset + gristIconX + (gristDisplayXOffset * row - row);
				int gristYOffset = yOffset + gristIconY + (gristDisplayYOffset * column - column);
				
				if(this.isPointInRegion(gristXOffset, gristYOffset, 16, 16, mousePos.x, mousePos.y))
				{
					GuiCraftingRecipe.openRecipeGui("grist:"+GristType.values()[gristId].getName());
					return;
				}
			}
		}
	}
	
}

package com.mraof.minestuck.client.gui;

import java.io.IOException;
import java.util.Arrays;

import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.GristType;
import com.mraof.minestuck.util.MinestuckPlayerData;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class GuiGristSelector extends GuiScreen
{
	
	private static final ResourceLocation guiGristcache = new ResourceLocation("minestuck", "textures/gui/GristCache.png");
	
	private static final int gristIconX = 21, gristIconY = 32;
	private static final int gristIconXOffset = 66, gristIconYOffset = 21;
	private static final int gristCountX = 44, gristCountY = 36;
	private static final int gristCountXOffset = 66, gristCountYOffset = 21;
	private static final int guiWidth = 226, guiHeight = 190;
	
	private GuiMachine otherGui;
	
	protected GuiGristSelector(GuiMachine guiMachine)
	{
		this.otherGui = guiMachine;
	}

	@Override
	public void drawScreen(int xcor, int ycor, float par3)
	{
		int xOffset = (width - guiWidth)/2;
		int yOffset = (height - guiHeight)/2;
		
		super.drawScreen(xcor, ycor, par3);
		this.drawDefaultBackground();
		
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		
		this.mc.getTextureManager().bindTexture(guiGristcache);
		this.drawTexturedModalRect(xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		
		String cacheMessage = StatCollector.translateToLocal("gui.selectGrist");
		mc.fontRendererObj.drawString(cacheMessage, (this.width / 2) - mc.fontRendererObj.getStringWidth(cacheMessage) / 2, yOffset + 12, 0x404040);
		
		GlStateManager.color(1,1,1);
		GlStateManager.disableRescaleNormal();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
		
		int tooltip = -1;
		
		for(int gristId = 0; gristId < GristType.allGrists; gristId++)
		{
			int row = (int) (gristId / 7);
			int column = (int) (gristId % 7);
			int gristXOffset = xOffset + gristIconX + (gristIconXOffset * row - row);
			int gristYOffset = yOffset + gristIconY + (gristIconYOffset * column - column);

			if(this.isPointInRegion(gristXOffset, gristYOffset, 16, 16, xcor, ycor)) {
				this.drawGradientRect(gristXOffset, gristYOffset, gristXOffset + 16, gristYOffset + 17, -2130706433, -2130706433);
				tooltip = gristId;
			}
			
			this.drawIcon(gristXOffset, gristYOffset, "textures/grist/" + GristType.values()[gristId].getName()+ ".png");
			mc.fontRendererObj.drawString(Integer.toString(MinestuckPlayerData.getClientGrist().getGrist(GristType.values()[gristId])),xOffset + gristCountX + (gristCountXOffset * row - row), yOffset + gristCountY + (gristCountYOffset * column - column), 0xddddee);
			
		}
		
		if (tooltip != -1)
		{
			drawHoveringText(Arrays.asList(StatCollector.translateToLocalFormatted("grist.format", GristType.values()[tooltip].getDisplayName())),
					xcor, ycor, fontRendererObj);
		}
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
	protected void mouseClicked(int xcor, int ycor, int mouseButton) throws IOException
	{
		super.mouseClicked(xcor, ycor, mouseButton);
		if(mouseButton == 0)
		{
			int xOffset = (width - guiWidth)/2;
			int yOffset = (height - guiHeight)/2;
			for(int gristId = 0; gristId < GristType.allGrists; gristId++)
			{
				int row = (int) (gristId / 7);
				int column = (int) (gristId % 7);
				int gristXOffset = xOffset + gristIconX + (gristIconXOffset * row - row);
				int gristYOffset = yOffset + gristIconY + (gristIconYOffset * column - column);
				if(isPointInRegion(gristXOffset, gristYOffset, 16, 16, xcor, ycor))
				{
					otherGui.te.selectedGrist = GristType.values()[gristId];
					otherGui.width = this.width;
					otherGui.height = this.height;
					mc.currentScreen = otherGui;
					MinestuckPacket packet = MinestuckPacket.makePacket(Type.MACHINE_STATE, gristId);
					MinestuckChannelHandler.sendToServer(packet);
					break;
				}
			}
		}
	}
	
	@Override
	public void onGuiClosed()
	{
		mc.currentScreen = otherGui;
		mc.thePlayer.closeScreen();
	}
	
	protected boolean isPointInRegion(int regionX, int regionY, int regionWidth, int regionHeight, int pointX, int pointY)
	{
		return pointX >= regionX && pointX < regionX + regionWidth && pointY >= regionY && pointY < regionY + regionHeight;
	}
	
}

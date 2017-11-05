package com.mraof.minestuck.client.gui;

import com.mraof.minestuck.client.util.GuiUtil;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.GristType;
import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.Arrays;

public class GuiGristSelector extends GuiScreen
{
	
	private static final ResourceLocation guiGristcache = new ResourceLocation("minestuck", "textures/gui/grist_cache.png");
	
	private static final int gristIconX = 21, gristIconY = 32;
	private static final int gristDisplayXOffset = 66, gristDisplayYOffset = 21;
	private static final int gristCountX = 44, gristCountY = 36;
	private static final int guiWidth = 226, guiHeight = 190;
	
	private GuiSburbMachine otherGui;
	
	protected GuiGristSelector(GuiSburbMachine guiMachine)
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
		
		String cacheMessage = I18n.format("gui.selectGrist");
		mc.fontRenderer.drawString(cacheMessage, (this.width / 2) - mc.fontRenderer.getStringWidth(cacheMessage) / 2, yOffset + 12, 0x404040);
		
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
			String amount = GuiUtil.addSuffix(clientGrist.getGrist(GristType.values()[gristId]));

			if(this.isPointInRegion(gristXOffset + gristIconX, gristYOffset + gristIconY, 16, 16, xcor, ycor))
			{
				this.drawGradientRect(gristXOffset + gristIconX, gristYOffset + gristIconY, gristXOffset + gristIconX + 16, gristYOffset + gristIconY + 17, -2130706433, -2130706433);
				tooltip = gristId*2;
			}
			if(!String.valueOf(clientGrist.getGrist(GristType.values()[gristId])).equals(amount)
					&& this.isPointInRegion(gristXOffset + gristCountX - 1, gristYOffset + gristCountY - 1, 35, 10, xcor, ycor))
				tooltip = gristId*2 + 1;
			
			this.drawIcon(gristXOffset + gristIconX, gristYOffset + gristIconY, "textures/grist/" + GristType.values()[gristId].getName()+ ".png");
			mc.fontRenderer.drawString(amount, gristXOffset + gristCountX, gristYOffset + gristCountY, 0xddddee);
			
		}
		
		if (tooltip != -1)
			if(tooltip % 2 == 0)
				drawHoveringText(Arrays.asList(I18n.format("grist.format", GristType.values()[tooltip/2].getDisplayName())),
						xcor, ycor, fontRenderer);
			else drawHoveringText(Arrays.asList(String.valueOf(clientGrist.getGrist(GristType.values()[tooltip/2]))), xcor, ycor, fontRenderer);
	}
	
	private void drawIcon(int x,int y,String location) 
	{
		this.mc.getTextureManager().bindTexture(new ResourceLocation("minestuck",location));
		
		float scale = (float) 1/16;
		
		int iconX = 16;
		int iconY = 16;
		int iconU = 0;
		int iconV = 0;
		
		BufferBuilder render = Tessellator.getInstance().getBuffer();
		render.begin(7, DefaultVertexFormats.POSITION_TEX);
		render.pos(x, y + iconY, 0D).tex((iconU)*scale, (iconV + iconY)*scale).endVertex();
		render.pos(x + iconX, y + iconY, 0D).tex((iconU + iconX)*scale, (iconV + iconY)*scale).endVertex();
		render.pos(x + iconX, y, 0D).tex((iconU + iconX)*scale, (iconV)*scale).endVertex();
		render.pos(x, y, 0D).tex((iconU)*scale, (iconV)*scale).endVertex();
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
				int gristXOffset = xOffset + gristIconX + (gristDisplayXOffset * row - row);
				int gristYOffset = yOffset + gristIconY + (gristDisplayYOffset * column - column);
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
		mc.player.closeScreen();
	}
	
	protected boolean isPointInRegion(int regionX, int regionY, int regionWidth, int regionHeight, int pointX, int pointY)
	{
		return pointX >= regionX && pointX < regionX + regionWidth && pointY >= regionY && pointY < regionY + regionHeight;
	}
	
}

package com.mraof.minestuck.client.gui.playerStats;

import static com.mraof.minestuck.client.gui.playerStats.GuiPlayerStats.*;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.mraof.minestuck.client.gui.playerStats.GuiPlayerStats.EditmodeGuiType;
import com.mraof.minestuck.client.gui.playerStats.GuiPlayerStats.NormalGuiType;
import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.network.skaianet.SkaiaClient;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.UsernameHandler;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.inventory.Container;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public abstract class GuiPlayerStatsContainer extends GuiContainer {
	
	protected int guiWidth, guiHeight;
	protected int xOffset, yOffset;
	
	private boolean mode;
	
	public GuiPlayerStatsContainer(Container container)
	{
		super(container);
		this.mode = !ClientEditHandler.isActive();
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		xOffset = (width - guiWidth)/2;
		yOffset = (height - guiHeight + tabHeight - tabOverlap)/2;
		this.guiTop = yOffset;
		this.guiLeft = xOffset;
	}
	
	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}
	
	protected void drawTabs()
	{
		GL11.glColor3f(1,1,1);
		
		mc.getTextureManager().bindTexture(GuiPlayerStats.icons);
		
		if(mode)
		{
			for(NormalGuiType type : NormalGuiType.values())
				if(type != normalTab && (!type.reqMedium || SkaiaClient.enteredMedium(UsernameHandler.encode(mc.thePlayer.getCommandSenderName()))))
				{
					int i = type.ordinal();
					drawTexturedModalRect(xOffset + i*(tabWidth + 2), yOffset - tabHeight + tabOverlap, i==0? 0:tabWidth, 0, tabWidth, tabHeight);
				}
				
		}
		else
		{
			for(EditmodeGuiType type : EditmodeGuiType.values())
				if(type != editmodeTab)
				{
					int i = type.ordinal();
					drawTexturedModalRect(xOffset + i*(tabWidth + 2), yOffset - tabHeight + tabOverlap, i==0? 0:tabWidth, 0, tabWidth, tabHeight);
				}
		}
	}
	
	protected void drawActiveTabAndIcons()
	{
		GL11.glColor3f(1,1,1);
		
		mc.getTextureManager().bindTexture(GuiPlayerStats.icons);
		
		int index = (mode? normalTab:editmodeTab).ordinal();
		drawTexturedModalRect(xOffset + index*(tabWidth+2), yOffset - tabHeight + tabOverlap,
				index == 0? 0:tabWidth, tabHeight, tabWidth, tabHeight);
		
		for(int i = 0; i < (mode? NormalGuiType.values():EditmodeGuiType.values()).length; i++)
			if(!mode || !NormalGuiType.values()[i].reqMedium || SkaiaClient.enteredMedium(UsernameHandler.encode(mc.thePlayer.getCommandSenderName())))
				drawTexturedModalRect(xOffset + (tabWidth - 16)/2 + (tabWidth+2)*i, yOffset - tabHeight + tabOverlap + 8, i*16, tabHeight*2 + (mode? 0:16), 16, 16);
	}
	
	protected void drawTabTooltip(int xcor, int ycor)
	{
		
//		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		if(ycor < yOffset && ycor > yOffset - tabHeight + 4)
			for(int i = 0; i < (mode? NormalGuiType.values():EditmodeGuiType.values()).length; i++)
				if(xcor < xOffset + i*(tabWidth + 2))
					break;
				else if(xcor < xOffset + i*(tabWidth + 2) + tabWidth
						&& (!mode || !NormalGuiType.values()[i].reqMedium || SkaiaClient.enteredMedium(UsernameHandler.encode(mc.thePlayer.getCommandSenderName()))))
					drawTooltip(StatCollector.translateToLocal(mode? NormalGuiType.values()[i].name:EditmodeGuiType.values()[i].name), xcor - guiLeft, ycor - guiTop,
							EnumChatFormatting.WHITE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
	
	@Override
	protected void mouseClicked(int xcor, int ycor, int mouseButton)
	{
		if(mouseButton == 0 && ycor < (height - guiHeight + tabHeight - tabOverlap)/2 && ycor > (height - guiHeight - tabHeight + tabOverlap)/2)
		{
			for(int i = 0; i < (mode? NormalGuiType.values():EditmodeGuiType.values()).length; i++)
				if(xcor < xOffset + i*(tabWidth + 2))
					break;
				else if(xcor < xOffset + i*(tabWidth + 2) + tabWidth)
				{
					if(mode && NormalGuiType.values()[i].reqMedium && !SkaiaClient.enteredMedium(UsernameHandler.encode(mc.thePlayer.getCommandSenderName())))
						return;
					mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
					if(i != (mode? normalTab:editmodeTab).ordinal())
					{
						if(mode)
							normalTab = NormalGuiType.values()[i];
						else editmodeTab = EditmodeGuiType.values()[i];
						openGui(true);
					}
					return;
				}
		}
		super.mouseClicked(xcor, ycor, mouseButton);
	}
	
	protected void drawTooltip(String text,int par2, int par3, EnumChatFormatting color)
	{
		String[] list = {text};
		
		for (int k = 0; k < list.length; ++k) {
			list[k] = color + list[k];
		}
		
		if (list.length != 0) {
			int k = mc.fontRenderer.getStringWidth(text);
			
			int i1 = par2 + 12;
			int j1 = par3 - 12;
			int k1 = 8;
			
			if (list.length > 1) {
				k1 += 2 + (list.length - 1) * 10;
			}
			
			if (i1 + k > this.width) {
				i1 -= 28 + k;
			}
			
			if (j1 + k1 + 6 > this.height) {
				j1 = this.height - k1 - 6;
			}
			
			this.zLevel = 300.0F;
			itemRenderer.zLevel = 300.0F;
			int l1 = -267386864;
			this.drawGradientRect(i1 - 3, j1 - 4, i1 + k + 3, j1 - 3, l1, l1);
			this.drawGradientRect(i1 - 3, j1 + k1 + 3, i1 + k + 3, j1 + k1 + 4, l1, l1);
			this.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 + k1 + 3, l1, l1);
			this.drawGradientRect(i1 - 4, j1 - 3, i1 - 3, j1 + k1 + 3, l1, l1);
			this.drawGradientRect(i1 + k + 3, j1 - 3, i1 + k + 4, j1 + k1 + 3, l1, l1);
			int i2 = 1347420415;
			int j2 = (i2 & 16711422) >> 1 | i2 & -16777216;
			this.drawGradientRect(i1 - 3, j1 - 3 + 1, i1 - 3 + 1, j1 + k1 + 3 - 1, i2, j2);
			this.drawGradientRect(i1 + k + 2, j1 - 3 + 1, i1 + k + 3, j1 + k1 + 3 - 1, i2, j2);
			this.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 - 3 + 1, i2, i2);
			this.drawGradientRect(i1 - 3, j1 + k1 + 2, i1 + k + 3, j1 + k1 + 3, j2, j2);
			
			for (int k2 = 0; k2 < list.length; ++k2) {
				String s1 = list[k2];
				mc.fontRenderer.drawStringWithShadow(s1, i1, j1, -1);
				
				if (k2 == 0) {
					j1 += 2;
				}
				
				j1 += 10;
			}
			
			this.zLevel = 0.0F;
			itemRenderer.zLevel = 0.0F;
		}
	}
	
}

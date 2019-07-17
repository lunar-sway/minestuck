package com.mraof.minestuck.client.util;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.alchemy.GristAmount;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class GuiUtil
{
	
	public enum GristboardMode
	{
		ALCHEMITER,
		ALCHEMITER_SELECT,
		LARGE_ALCHEMITER,
		LARGE_ALCHEMITER_SELECT,
		GRIST_WIDGET;
	}
	
	public static final int GRIST_BOARD_WIDTH = 158, GRIST_BOARD_HEIGHT = 24;
	
	public static void drawGristBoard(GristSet grist, GristboardMode mode, int boardX, int boardY, FontRenderer fontRenderer)
	{
		if (grist == null)
		{
			fontRenderer.drawString(I18n.format("gui.notAlchemizable"), boardX, boardY, 0xFF0000);
			return;
		}
		
		if (grist.isEmpty())
		{
			fontRenderer.drawString(I18n.format("gui.free"), boardX, boardY, 0x00FF00);
			return;
		}
		
		GristSet playerGrist = MinestuckPlayerData.getClientGrist();
		Iterator<GristAmount> it = grist.getArray().iterator();
		if(!MinestuckConfig.alchemyIcons)
		{
			int place = 0;
			while (it.hasNext())
			{
				GristAmount amount = it.next();
				GristType type = amount.getType();
				int need = amount.getAmount();
				int have = playerGrist.getGrist(type);
				
				int row = place % 3;
				int col = place / 3;
				
				int color = getGristColor(mode, need <= have);
				
				String needStr = addSuffix(need), haveStr = addSuffix(have);
				fontRenderer.drawString(needStr + " " + type.getDisplayName() + " (" + haveStr + ")", boardX + GRIST_BOARD_WIDTH/2*col, boardY + GRIST_BOARD_HEIGHT/3*row, color);
				//ensure that one line is rendered on the large alchemiter
				if(mode==GristboardMode.LARGE_ALCHEMITER||mode==GristboardMode.LARGE_ALCHEMITER_SELECT)
					place+=2;
				
				place++;
				
			}
		} else
		{
			int index = 0;
			while(it.hasNext())
			{
				GristAmount amount = it.next();
				GristType type = amount.getType();
				int need = amount.getAmount();
				int have = playerGrist.getGrist(type);
				int row = index/GRIST_BOARD_WIDTH;
				int color = getGristColor(mode, need <= have);
				
				String needStr = addSuffix(need), haveStr = '('+addSuffix(have)+')';
				int needStrWidth = fontRenderer.getStringWidth(needStr);
				if(index + needStrWidth + 10 + fontRenderer.getStringWidth(haveStr) > (row + 1)*GRIST_BOARD_WIDTH)
				{
					row++;
					index = row*GRIST_BOARD_WIDTH;
				}
				fontRenderer.drawString(needStr, boardX + 1 + index%GRIST_BOARD_WIDTH, boardY + 8*row, color);
				fontRenderer.drawString(haveStr, boardX + needStrWidth + 10 + index%GRIST_BOARD_WIDTH, boardY + 8*row, color);
				
				GlStateManager.color(1, 1, 1);
				GlStateManager.disableLighting();
				Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(type.getIcon().getResourceDomain(), "textures/grist/" + type.getIcon().getResourcePath()+ ".png"));
				Gui.drawModalRectWithCustomSizedTexture(boardX + needStrWidth + 1 + index%GRIST_BOARD_WIDTH, boardY + 8*row, 0, 0, 8, 8, 8, 8);
				
				//ensure the large alchemiter gui has one grist type to a line
				if(mode==GristboardMode.LARGE_ALCHEMITER||mode==GristboardMode.LARGE_ALCHEMITER_SELECT) {
					index=(row+1)*158;
				}else {
					index += needStrWidth + 10 + fontRenderer.getStringWidth(haveStr);
					index = Math.min(index + 6, (row + 1)*158);
				}
			}
		}
	}
	
	public static List<String> getGristboardTooltip(GristSet grist, int mouseX, int mouseY, int boardX, int boardY, FontRenderer fontRenderer)
	{
		if (grist == null || grist.isEmpty())
			return null;
		mouseX -= boardX;
		mouseY -= boardY;
		
		GristSet playerGrist = MinestuckPlayerData.getClientGrist();
		if(!MinestuckConfig.alchemyIcons)
		{
			int place = 0;
			for(GristAmount entry : grist.getArray())
			{
				int row = place % 3;
				int col = place / 3;
				
				if(mouseY >= 8*row && mouseY < 8*row + 8)
				{
					int need = entry.getAmount();
					String needStr = addSuffix(need);
					
					if(!needStr.equals(String.valueOf(need)) && mouseX >= GRIST_BOARD_WIDTH/2*col && mouseX < GRIST_BOARD_WIDTH/2*col + fontRenderer.getStringWidth(needStr))
						return Collections.singletonList(String.valueOf(need));
					
					int width = fontRenderer.getStringWidth(needStr + " " + entry.getType().getDisplayName() + " (");
					int have = playerGrist.getGrist(entry.getType());
					String haveStr = addSuffix(have);
					
					if(!haveStr.equals(String.valueOf(have)) && mouseX >= boardX + GRIST_BOARD_WIDTH/2*col + width && mouseX < boardX + GRIST_BOARD_WIDTH/2*col + width + fontRenderer.getStringWidth(haveStr))
						return Collections.singletonList(String.valueOf(have));
				}
				
				place++;
			}
		} else
		{
			int index = 0;
			for(GristAmount entry : grist.getArray())
			{
				GristType type = entry.getType();
				int need = entry.getAmount();
				int have = playerGrist.getGrist(type);
				int row = index/GRIST_BOARD_WIDTH;
				
				String needStr = addSuffix(need), haveStr = addSuffix(have);
				int needStrWidth = fontRenderer.getStringWidth(needStr);
				int haveStrWidth = fontRenderer.getStringWidth('('+haveStr+')');
				
				if(index + needStrWidth + 10 + haveStrWidth > (row + 1)*GRIST_BOARD_WIDTH)
				{
					row++;
					index = row*GRIST_BOARD_WIDTH;
				}
				
				if(mouseY >= 8*row && mouseY < 8*row + 8)
				{
					if(!needStr.equals(String.valueOf(need)) && mouseX >= index%GRIST_BOARD_WIDTH && mouseX < index%GRIST_BOARD_WIDTH + needStrWidth)
						return Collections.singletonList(String.valueOf(need));
					else if(mouseX >= index%158 + needStrWidth + 1 && mouseX < index%158+ needStrWidth + 9)
						return Collections.singletonList(type.getDisplayName());
					else if(!haveStr.equals(String.valueOf(have)) && mouseX >= index%158 + needStrWidth + 10 + fontRenderer.getCharWidth('(') && mouseX < index%158 + needStrWidth + 10 + fontRenderer.getStringWidth("("+haveStr))
						return Collections.singletonList(String.valueOf(have));
				}
				
				index += needStrWidth + 10 + haveStrWidth;
				index = Math.min(index + 6, (row + 1)*GRIST_BOARD_WIDTH);
			}
		}
		
		return null;
	}
	
	private static int getGristColor(GristboardMode mode, boolean hasEnough)
	{
		switch(mode)
		{
		case LARGE_ALCHEMITER://dont break
		case ALCHEMITER: return hasEnough ? 0x00FF00 : 0xFF0000;
		case LARGE_ALCHEMITER_SELECT://dont break;
		case ALCHEMITER_SELECT: return 0x0000FF;
		case GRIST_WIDGET:
		default: return 0x000000;
		}
	}
	
	public static String addSuffix(long n)
	{
		if(n < 10000)
			return String.valueOf(n);
		else if(n < 10000000)
			return (n/1000) + "K";
		else return (n/1000000) + "M";
	}

}
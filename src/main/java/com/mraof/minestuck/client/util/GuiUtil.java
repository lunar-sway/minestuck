package com.mraof.minestuck.client.util;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.item.crafting.alchemy.GristAmount;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.item.crafting.alchemy.GristType;
import com.mraof.minestuck.world.storage.ClientPlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MutableBoundingBox;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class GuiUtil
{
	public static final String NOT_ALCHEMIZABLE = "minestuck.not_alchemizable";
	public static final String FREE = "minestuck.free";
	
	public enum GristboardMode
	{
		ALCHEMITER,
		ALCHEMITER_SELECT,
		LARGE_ALCHEMITER,
		LARGE_ALCHEMITER_SELECT,
		GRIST_WIDGET,
		JEI_WILDCARD
	}
	
	public static final int GRIST_BOARD_WIDTH = 158, GRIST_BOARD_HEIGHT = 24;
	
	public static void drawGristBoard(GristSet grist, GristboardMode mode, int boardX, int boardY, FontRenderer fontRenderer)
	{
		if (grist == null)
		{
			fontRenderer.drawString(I18n.format(NOT_ALCHEMIZABLE), boardX, boardY, 0xFF0000);
			return;
		}
		
		if (grist.isEmpty())
		{
			fontRenderer.drawString(I18n.format(FREE), boardX, boardY, 0x00FF00);
			return;
		}
		
		GristSet playerGrist = ClientPlayerData.getClientGrist();
		Iterator<GristAmount> it = grist.getAmounts().iterator();
		if(!MinestuckConfig.alchemyIcons.get())
		{
			int place = 0;
			while (it.hasNext())
			{
				GristAmount amount = it.next();
				GristType type = amount.getType();
				long need = amount.getAmount();
				long have = playerGrist.getGrist(type);
				
				int row = place % 3;
				int col = place / 3;
				
				int color = getGristColor(mode, need <= have);
				
				String needStr = addSuffix(need), haveStr = addSuffix(have);
				if(mode == GristboardMode.JEI_WILDCARD)
					fontRenderer.drawString(needStr + " Any Type", boardX + GRIST_BOARD_WIDTH/2F*col, boardY + GRIST_BOARD_HEIGHT/3F*row, color);
				else fontRenderer.drawString(needStr + " " + type.getDisplayName() + " (" + haveStr + ")", boardX + GRIST_BOARD_WIDTH/2F*col, boardY + GRIST_BOARD_HEIGHT/3F*row, color);
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
				long need = amount.getAmount();
				long have = playerGrist.getGrist(type);
				int row = index/GRIST_BOARD_WIDTH;
				int color = getGristColor(mode, need <= have);
				
				String needStr = addSuffix(need), haveStr = '('+addSuffix(have)+')';
				int needOffset = 1, iconSize = 8, haveOffset = 1;
				if(mode == GristboardMode.JEI_WILDCARD)
				{
					haveStr = "";
					haveOffset = 0;
				}
				
				int needStrWidth = fontRenderer.getStringWidth(needStr);
				if(index + needStrWidth + needOffset + iconSize + haveOffset + fontRenderer.getStringWidth(haveStr) > (row + 1)*GRIST_BOARD_WIDTH)
				{
					row++;
					index = row*GRIST_BOARD_WIDTH;
				}
				fontRenderer.drawString(needStr, boardX + needOffset + index%GRIST_BOARD_WIDTH, boardY + 8*row, color);
				fontRenderer.drawString(haveStr, boardX + needStrWidth + needOffset + iconSize + haveOffset + index%GRIST_BOARD_WIDTH, boardY + 8*row, color);
				
				GlStateManager.color3f(1, 1, 1);
				GlStateManager.disableLighting();
				ResourceLocation icon = mode == GristboardMode.JEI_WILDCARD ? GristType.getDummyIcon() : type.getIcon();
				if(icon != null)
				{
					Minecraft.getInstance().getTextureManager().bindTexture(icon);
					AbstractGui.blit(boardX + needStrWidth + needOffset + index % GRIST_BOARD_WIDTH, boardY + 8 * row, 0, 0, iconSize, iconSize, iconSize, iconSize);
				}
				
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
	
	public static List<String> getGristboardTooltip(GristSet grist, GristboardMode mode, double mouseX, double mouseY, int boardX, int boardY, FontRenderer fontRenderer)
	{
		if (grist == null || grist.isEmpty())
			return Collections.emptyList();
		mouseX -= boardX;
		mouseY -= boardY;
		
		GristSet playerGrist = ClientPlayerData.getClientGrist();
		if(!MinestuckConfig.alchemyIcons.get())
		{
			int place = 0;
			for(GristAmount entry : grist.getAmounts())
			{
				int row = place % 3;
				int col = place / 3;
				
				if(mouseY >= 8*row && mouseY < 8*row + 8)
				{
					long need = entry.getAmount();
					String needStr = addSuffix(need);
					
					if(!needStr.equals(String.valueOf(need)) && mouseX >= GRIST_BOARD_WIDTH/2F*col && mouseX < GRIST_BOARD_WIDTH/2F*col + fontRenderer.getStringWidth(needStr))
						return Collections.singletonList(String.valueOf(need));
					
					if(mode == GristboardMode.JEI_WILDCARD)
						continue;
					
					int width = fontRenderer.getStringWidth(needStr + " " + entry.getType().getDisplayName() + " (");
					long have = playerGrist.getGrist(entry.getType());
					String haveStr = addSuffix(have);
					
					if(!haveStr.equals(String.valueOf(have)) && mouseX >= boardX + GRIST_BOARD_WIDTH/2F*col + width && mouseX < boardX + GRIST_BOARD_WIDTH/2F*col + width + fontRenderer.getStringWidth(haveStr))
						return Collections.singletonList(String.valueOf(have));
				}
				
				place++;
			}
		} else
		{
			int index = 0;
			for(GristAmount entry : grist.getAmounts())
			{
				GristType type = entry.getType();
				long need = entry.getAmount();
				long have = playerGrist.getGrist(type);
				int row = index/GRIST_BOARD_WIDTH;
				
				String needStr = addSuffix(need), haveStr = addSuffix(have);
				int needStrWidth = fontRenderer.getStringWidth(needStr);
				int haveStrWidth = fontRenderer.getStringWidth('('+haveStr+')');
				
				int needOffset = 1, iconSize = 8, haveOffset = 1;
				if(mode == GristboardMode.JEI_WILDCARD)
				{
					haveStrWidth = 0;
					haveOffset = 0;
					haveStr = "";
				}
				
				if(index + needStrWidth + needOffset + iconSize + haveOffset + haveStrWidth > (row + 1)*GRIST_BOARD_WIDTH)
				{
					row++;
					index = row*GRIST_BOARD_WIDTH;
				}
				
				if(mouseY >= 8*row && mouseY < 8*row + 8)
				{
					if(!needStr.equals(String.valueOf(need)) && mouseX >= index%GRIST_BOARD_WIDTH && mouseX < index%GRIST_BOARD_WIDTH + needStrWidth)
						return Collections.singletonList(String.valueOf(need));
					else if(mouseX >= index%158 + needStrWidth + needOffset && mouseX < index%158+ needStrWidth + needOffset + iconSize)
						return Collections.singletonList(type.getDisplayName().getFormattedText());
					else if(!haveStr.isEmpty() && !haveStr.equals(String.valueOf(have)) && mouseX >= index%158 + needStrWidth + needOffset + iconSize + haveOffset + fontRenderer.getStringWidth("(") && mouseX < index%158 + needStrWidth + needOffset + iconSize + haveOffset + fontRenderer.getStringWidth("("+haveStr))
						return Collections.singletonList(String.valueOf(have));
				}
				
				index += needStrWidth + 10 + haveStrWidth;
				index = Math.min(index + 6, (row + 1)*GRIST_BOARD_WIDTH);
			}
		}
		
		return Collections.emptyList();
	}
	
	private static int getGristColor(GristboardMode mode, boolean hasEnough)
	{
		switch(mode)
		{
		case LARGE_ALCHEMITER://dont break
		case ALCHEMITER: return hasEnough ? 0x00FF00 : 0xFF0000;
		case LARGE_ALCHEMITER_SELECT://dont break;
		case ALCHEMITER_SELECT:
		case JEI_WILDCARD: return 0x0000FF;
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
	
	public static AxisAlignedBB fromBoundingBox(MutableBoundingBox boundingBox)
	{
		return new AxisAlignedBB(boundingBox.minX, boundingBox.minY, boundingBox.minZ, boundingBox.maxX + 1, boundingBox.maxY + 1, boundingBox.maxZ + 1);
	}
}
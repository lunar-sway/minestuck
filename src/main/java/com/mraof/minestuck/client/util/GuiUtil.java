package com.mraof.minestuck.client.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.api.alchemy.GristAmount;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;

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
	
	public static void drawGristBoard(GuiGraphics guiGraphics, GristSet grist, GristboardMode mode, int boardX, int boardY, Font font)
	{
		drawGristBoard(guiGraphics, grist, mode, boardX, boardY, font, 1.0F);
	}
	
	public static void drawGristBoard(GuiGraphics guiGraphics, GristSet grist, GristboardMode mode, int boardX, int boardY, Font font, float scale)
	{
		if(grist == null)
		{
			guiGraphics.drawString(font, I18n.get(NOT_ALCHEMIZABLE), boardX, boardY, 0xFF0000, false);
			return;
		}
		
		if(grist.isEmpty())
		{
			guiGraphics.drawString(font, I18n.get(FREE), boardX, boardY, 0x00FF00, false);
			return;
		}
		
		ClientPlayerData.ClientCache cache = ClientPlayerData.getGristCache(ClientPlayerData.CacheSource.PLAYER);
		GristSet playerGrist = cache.set();
		if(!MinestuckConfig.CLIENT.alchemyIcons.get())
		{
			int place = 0;
			for(GristAmount amount : grist.asAmounts())
			{
				GristType type = amount.type();
				long need = amount.amount();
				long have = playerGrist.getGrist(type);
				
				int row = place % 3;
				int col = place / 3;
				
				int color = getGristColor(mode, cache.canAfford(amount));
				
				String needStr = addSuffix(need), haveStr = addSuffix(have);
				if(mode == GristboardMode.JEI_WILDCARD)
					guiGraphics.drawString(font, needStr + " Any Type", boardX + GRIST_BOARD_WIDTH / 2F * col, boardY + GRIST_BOARD_HEIGHT / 3F * row, color, false);
				else
					guiGraphics.drawString(font, needStr + " " + type.getDisplayName() + " (" + haveStr + ")", boardX + GRIST_BOARD_WIDTH / 2F * col, boardY + GRIST_BOARD_HEIGHT / 3F * row, color, false);
				//ensure that one line is rendered on the large alchemiter
				if(mode == GristboardMode.LARGE_ALCHEMITER || mode == GristboardMode.LARGE_ALCHEMITER_SELECT)
					place += 2;
				
				place++;
				
			}
		} else
		{
			int index = 0;
			for(GristAmount amount : grist.asAmounts())
			{
				GristType type = amount.type();
				long need = amount.amount();
				long have = playerGrist.getGrist(type);
				int row = index / GRIST_BOARD_WIDTH;
				int color = getGristColor(mode, need <= have);
				
				String needStr = addSuffix(need), haveStr = '(' + addSuffix(have) + ')';
				int needOffset = 1, iconSize = (int) (8 * scale), haveOffset = 1;
				if(mode == GristboardMode.JEI_WILDCARD)
				{
					haveStr = "";
					haveOffset = 0;
				}
				
				int needStrWidth = font.width(needStr);
				if(index + needStrWidth + needOffset + iconSize + haveOffset + font.width(haveStr) > (row + 1) * GRIST_BOARD_WIDTH)
				{
					row++;
					index = row * GRIST_BOARD_WIDTH;
				}
				guiGraphics.drawString(font, needStr, boardX + needOffset + index % GRIST_BOARD_WIDTH, boardY + 8 * row, color, false);
				guiGraphics.drawString(font, haveStr, boardX + needStrWidth + needOffset + iconSize + haveOffset + index % GRIST_BOARD_WIDTH, boardY + 8 * row, color, false);
				
				
				ResourceLocation icon = mode == GristboardMode.JEI_WILDCARD ? GristType.DUMMY_ICON_LOCATION : type.getIcon();
				if(icon != null)
				{
					RenderSystem.setShaderColor(1, 1, 1, 1);
					guiGraphics.blit(icon, boardX + needStrWidth + needOffset + index % GRIST_BOARD_WIDTH, boardY + 8 * row, 0, 0, iconSize, iconSize, iconSize, iconSize);
				}
				
				//ensure the large alchemiter gui has one grist type to a line
				if(mode == GristboardMode.LARGE_ALCHEMITER || mode == GristboardMode.LARGE_ALCHEMITER_SELECT)
				{
					index = (row + 1) * 158;
				} else
				{
					index += needStrWidth + 10 + font.width(haveStr);
					index = Math.min(index + 6, (row + 1) * 158);
				}
			}
		}
	}
	
	public static Component getGristboardTooltip(GristSet grist, GristboardMode mode, double mouseX, double mouseY, int boardX, int boardY, Font font)
	{
		return getGristboardTooltip(grist, mode, mouseX, mouseY, boardX, boardY, font, 1.0F);
	}
	
	public static Component getGristboardTooltip(GristSet grist, GristboardMode mode, double mouseX, double mouseY, int boardX, int boardY, Font font, float scale)
	{
		if(grist == null || grist.isEmpty())
			return null;
		mouseX -= boardX;
		mouseY -= boardY;
		
		GristSet playerGrist = ClientPlayerData.getGristCache(ClientPlayerData.CacheSource.PLAYER).set();
		if(!MinestuckConfig.CLIENT.alchemyIcons.get())
		{
			int place = 0;
			for(GristAmount entry : grist.asAmounts())
			{
				int row = place % 3;
				int col = place / 3;
				
				if(mouseY >= 8 * row && mouseY < 8 * row + 8)
				{
					long need = entry.amount();
					String needStr = addSuffix(need);
					
					if(!needStr.equals(String.valueOf(need)) && mouseX >= GRIST_BOARD_WIDTH / 2F * col && mouseX < GRIST_BOARD_WIDTH / 2F * col + font.width(needStr))
						return Component.literal(String.valueOf(need));
					
					if(mode == GristboardMode.JEI_WILDCARD)
						continue;
					
					int width = font.width(needStr + " " + entry.type().getDisplayName() + " (");
					long have = playerGrist.getGrist(entry.type());
					String haveStr = addSuffix(have);
					
					if(!haveStr.equals(String.valueOf(have)) && mouseX >= boardX + GRIST_BOARD_WIDTH / 2F * col + width && mouseX < boardX + GRIST_BOARD_WIDTH / 2F * col + width + font.width(haveStr))
						return Component.literal(String.valueOf(have));
				}
				
				place++;
			}
		} else
		{
			int index = 0;
			for(GristAmount entry : grist.asAmounts())
			{
				GristType type = entry.type();
				long need = entry.amount();
				long have = playerGrist.getGrist(type);
				int row = index / GRIST_BOARD_WIDTH;
				
				String needStr = addSuffix(need), haveStr = addSuffix(have);
				int needStrWidth = font.width(needStr);
				int haveStrWidth = font.width('(' + haveStr + ')');
				
				int needOffset = 1, iconSize = (int) (8 * scale), haveOffset = 1;
				if(mode == GristboardMode.JEI_WILDCARD)
				{
					haveStrWidth = 0;
					haveOffset = 0;
					haveStr = "";
				}
				
				if(index + needStrWidth + needOffset + iconSize + haveOffset + haveStrWidth > (row + 1) * GRIST_BOARD_WIDTH)
				{
					row++;
					index = row * GRIST_BOARD_WIDTH;
				}
				
				if(mouseY >= 8 * row && mouseY < 8 * row + 8)
				{
					if(!needStr.equals(String.valueOf(need)) && mouseX >= index % GRIST_BOARD_WIDTH && mouseX < index % GRIST_BOARD_WIDTH + needStrWidth)
						return Component.literal(String.valueOf(need));
					else if(mouseX >= index % 158 + needStrWidth + needOffset && mouseX < index % 158 + needStrWidth + needOffset + iconSize)
						return type.getDisplayName();
					else if(!haveStr.isEmpty() && !haveStr.equals(String.valueOf(have)) && mouseX >= index % 158 + needStrWidth + needOffset + iconSize + haveOffset + font.width("(") && mouseX < index % 158 + needStrWidth + needOffset + iconSize + haveOffset + font.width("(" + haveStr))
						return Component.literal(String.valueOf(have));
				}
				
				index += needStrWidth + 10 + haveStrWidth;
				index = Math.min(index + 6, (row + 1) * GRIST_BOARD_WIDTH);
			}
		}
		
		return null;
	}
	
	private static int getGristColor(GristboardMode mode, boolean hasEnough)
	{
		switch(mode)
		{
			case LARGE_ALCHEMITER://dont break
			case ALCHEMITER:
				return hasEnough ? 0x00FF00 : 0xFF0000;
			case LARGE_ALCHEMITER_SELECT://dont break;
			case ALCHEMITER_SELECT:
			case JEI_WILDCARD:
				return 0x0000FF;
			case GRIST_WIDGET:
			default:
				return 0x000000;
		}
	}
	
	public static String addSuffix(long n)
	{
		if(n < 10000)
			return String.valueOf(n);
		else if(n < 10000000)
			return (n / 1000) + "K";
		else return (n / 1000000) + "M";
	}
	
	public static AABB fromBoundingBox(BoundingBox boundingBox)
	{
		return new AABB(boundingBox.minX(), boundingBox.minY(), boundingBox.minZ(), boundingBox.maxX() + 1, boundingBox.maxY() + 1, boundingBox.maxZ() + 1);
	}
	
	public static AABB rotateAround(AABB boundingBox, double x, double z, Rotation rotation)
	{
		switch(rotation)
		{
			case NONE:
				return boundingBox;
			case CLOCKWISE_90:
				return new AABB(x + z - boundingBox.maxZ, boundingBox.minY, z - x + boundingBox.minX, x + z - boundingBox.minZ, boundingBox.maxY, z - x + boundingBox.maxX);
			case CLOCKWISE_180:
				return new AABB(x + x - boundingBox.maxX, boundingBox.minY, z + z - boundingBox.maxZ, x + x - boundingBox.minX, boundingBox.maxY, z + z - boundingBox.minZ);
			case COUNTERCLOCKWISE_90:
				return new AABB(x - z + boundingBox.minZ, boundingBox.minY, z + x - boundingBox.maxX, x - z + boundingBox.maxZ, boundingBox.maxY, z + x - boundingBox.minX);
			default:
				throw new IllegalArgumentException("Invalid rotation");
		}
	}
}
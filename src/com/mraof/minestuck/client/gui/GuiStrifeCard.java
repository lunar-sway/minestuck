package com.mraof.minestuck.client.gui;

import java.util.Collections;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.mraof.minestuck.util.KindAbstratusList;
import com.mraof.minestuck.util.KindAbstratusType;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiStrifeCard extends GuiScreenMinestuck
{
	private static int guiWidth = 147, guiHeight = 185;
	private static int xOffset, yOffset;
	private static final ResourceLocation guiStrifeSelector = new ResourceLocation("minestuck", "textures/gui/strife_specibus/strife_selector.png");
	private static float scale = 1;
	private static final int columnWidth = 50, columns = 2;
	
	@Override
	public void initGui() 
	{
		super.initGui();
		xOffset = (width-guiWidth)/2;
		yOffset = (height-guiHeight)/2;
		mc = Minecraft.getMinecraft();
	}
	
	@Override
	public boolean doesGuiPauseGame() 
	{
		return false;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) 
	{
		scale = 1;
		super.drawScreen(mouseX, mouseY, partialTicks);
		//this.drawDefaultBackground();
		
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(guiStrifeSelector);
		
		this.drawTexturedModalRect(xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		
		
		int listOffsetX = xOffset + 27;
		int listOffsetY = yOffset + 60;
		
		for(int i = 0; i < KindAbstratusList.getTypeList().size(); i++)
		{
			List<KindAbstratusType> list = KindAbstratusList.getTypeList();
			KindAbstratusType type = list.get(i);
			String typeName = type.getDisplayName();
			
			setScale(0.75f);
			int color = 0xFFFFFF;
			int listX = (columnWidth*(i % columns));
			int listY = (mc.fontRenderer.FONT_HEIGHT*(i / columns));
			int xPos = listOffsetX + listX;
			int yPos = listOffsetY + listY;
			int sxPos = (listOffsetX) + (int)(listX/scale);
			int syPos = listOffsetY + (int)(listY/scale);
			int txPos = (sxPos + columnWidth - mc.fontRenderer.getStringWidth(typeName))+ 78;
			int tyPos = syPos + 42;
			if(isPointInRegion(xPos, yPos, columnWidth, mc.fontRenderer.FONT_HEIGHT, mouseX, mouseY))
			{
				setScale(1);
				drawRect(xPos, yPos, xPos+columnWidth, yPos+mc.fontRenderer.FONT_HEIGHT, 0xFFAFAFAF);
				color = 0x000000;
				if(Mouse.getEventButtonState())
				{
					System.out.println(typeName);
				}
			}
			

			setScale(0.75f);
			mc.fontRenderer.drawString(typeName, txPos, tyPos, color);
		}
		//setScale(1);
			
			/*
			 * isPointInRegion()
			 * drawRect()
			 * mc.fontRender.drawString();
			 * Mouse.getEventButtonState()
			 */
			
		
		
		mc.fontRenderer.drawString(""+((int)(listOffsetY/scale)), 10, 10, 0xFFFFFF);
		
	}
	
	public void setScale(float percentage)
	{
		float s = percentage/scale;
		GL11.glScalef(s,s,s);
		scale = percentage;
	}
	
	public void updateScalePos()
	{
		
	}
}

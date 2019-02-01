package com.mraof.minestuck.client.gui;

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
	private static final int columnWidth = 70, columns = 2;
	
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
		//System.out.println("-._-.");
		super.drawScreen(mouseX, mouseY, partialTicks);
		//this.drawDefaultBackground();
		
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(guiStrifeSelector);
		
		this.drawTexturedModalRect(xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		//GL11.glScalef(0.5F, 0.5F, 0.5F);
		
		int i = 0;
		for(KindAbstratusType type : KindAbstratusList.getTypeList()) {
			setScale(0.7F);
			String typeName = type.getDisplayName().toLowerCase();
			int sxOff = (int)((xOffset+28)/scale+(columnWidth)*((i%columns)+1));
			int sxPos = (sxOff-mc.fontRenderer.getStringWidth(typeName));
			int syPos = (int)(((yOffset+62)/scale+(mc.fontRenderer.FONT_HEIGHT+1)*(int)(i/columns)));
			int xOff = (int)(xOffset+(columnWidth)*((i%columns)+1));
			int xPos = (xOff-mc.fontRenderer.getStringWidth(typeName));
			int yPos = (int)((yOffset+61+(mc.fontRenderer.FONT_HEIGHT-2)*(int)(i/columns)));
			
			//GL11.glScalef(0.5F, 0.5F, 0.5F);
			setScale(1F);
			drawRect(xOff-(columnWidth-16), yPos-1, (columnWidth-16)*2, yPos+mc.fontRenderer.FONT_HEIGHT, 0xFFAFAFFF);
			setScale(0.7F);
			if(!isPointInRegion(xOff-(columnWidth-16), -yPos-1, columnWidth-16, mc.fontRenderer.FONT_HEIGHT-1, (int)(mouseX), (int)(mouseY)))
			{
				mc.fontRenderer.drawString(typeName, sxPos, syPos, 0xFFFFFF);

				drawRect(sxPos, syPos-1, xPos+columnWidth-16, yPos-1-mc.fontRenderer.FONT_HEIGHT-2, 0xFFFFFF);
			}
			else {
				drawRect(sxOff, syPos-1, sxOff-columnWidth, syPos+mc.fontRenderer.FONT_HEIGHT, 0xFFAFAFAF);
				mc.fontRenderer.drawString(typeName, sxPos, syPos, 0x000000);
				
				if(Mouse.getEventButtonState())
				{
					System.out.println(typeName);
				}
				
			}
			i++;
			
		}
		
		mc.fontRenderer.drawString(""+mouseX, 10, 10, 0xFFFFFF);
		mc.fontRenderer.drawString(""+(xOffset+16+(columnWidth*(i%columns)+1)), 10, 20, 0xFFFFFF);
		
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

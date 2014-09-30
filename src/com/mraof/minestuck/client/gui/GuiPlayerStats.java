package com.mraof.minestuck.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.KindAbstratusList;
import com.mraof.minestuck.util.KindAbstratusType;
import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.util.GristType;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiPlayerStats extends GuiScreen
{

	private static final ResourceLocation guiGristcache = new ResourceLocation("minestuck", "textures/gui/GristCache.png");
	private static final ResourceLocation guiCaptchaDeckEmpty = new ResourceLocation("minestuck", "textures/gui/CaptchaDeckEmpty.png");
	private static final ResourceLocation guiStrifeSelector = new ResourceLocation("minestuck", "textures/gui/StrifeSelector.png");
	private static final ResourceLocation guiEcheladder = new ResourceLocation("minestuck", "textures/gui/echeladder.png");
	private static final ResourceLocation icons = new ResourceLocation("minestuck", "textures/gui/icons.png");
	
	private static final String[] tabNames = {"gui.captchaDeck.name","gui.strifeSpecibus.name","gui.gristCache.name","gui.echeladder.name"};
	private static final int tabWidth = 28, tabHeight = 32;
	
	private static final int tabs = 4;
	
	//Grist gui
	private static final int gristIconX = 21, gristIconY = 32;
	private static final int gristIconXOffset = 66, gristIconYOffset = 21;
	private static final int gristCountX = 44, gristCountY = 36;
	private static final int gristCountXOffset = 66, gristCountYOffset = 21;
	
	//Abstratus selector
	private static final int columnWidth = 70, columns = 3;
	
	//Echeladder
	private static final int ladderXOffset = 163, ladderYOffset = 25;
	private static final int rows = 12;
	private int scrollIndex;
	
	private static int selectedTab = 2;
	
	private Minecraft mc;
	private FontRenderer fontRenderer;
	private static RenderItem itemRenderer = new RenderItem();
	public static boolean visible = false;
	
	public GuiPlayerStats(Minecraft mc)
	{
		super();
		
		this.mc = mc;
		this.fontRenderer = mc.fontRenderer;
	}
	
	@Override
	public void drawScreen(int xcor, int ycor, float par3) 
	{
		super.drawScreen(xcor, ycor, par3);
		this.drawDefaultBackground();
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		int xOffset = (width-getGuiWidth())/2;
		int yOffset = (height-(getGuiHeight()+tabHeight-4))/2 + tabHeight-4;
		
		int tabX = xOffset;
		int currX = xOffset;
		this.mc.getTextureManager().bindTexture(icons);
		for(int i = 0; i < tabs; i++) {
			if(i != 0)
				currX = currX+tabWidth+2;
			if(selectedTab != i)
				drawTexturedModalRect(currX,yOffset-tabHeight+4,i==0?0:28,0, tabWidth, tabHeight);
			else tabX = currX;
		}
		
		if(selectedTab == 3) {
			if(scrollIndex == 13)
				scrollIndex = 0;
			else scrollIndex++;
			this.mc.getTextureManager().bindTexture(guiEcheladder);
			int index = scrollIndex % 14;
			for(int i = 0; i < rows; i++)
				drawTexturedModalRect(xOffset+90,yOffset+38-index+(i*14),0,212,146,14);
			for(int i = 0; i < rows; i++) {
				String s = "MethodOfScrollIndexAndI";
				fontRenderer.drawString(s, xOffset+ladderXOffset - fontRenderer.getStringWidth(s) / 2, yOffset+40-index+(i*14), 0xFFFFFF);
			}
			GL11.glColor3f(1,1,1);
		}
		
		this.mc.getTextureManager().bindTexture(getGui());
		
		this.drawTexturedModalRect(xOffset, yOffset, 0, 0, getGuiWidth(), getGuiHeight());
		
		this.mc.getTextureManager().bindTexture(icons);
		drawTexturedModalRect(tabX,yOffset-tabHeight+4,selectedTab==0?0:28,32, tabWidth, tabHeight);
		
		for(int i = 0; i < tabs; i++)
			drawTexturedModalRect(xOffset+ (tabWidth/2 - 8) + (tabWidth+2)*i, yOffset - tabHeight + 12, i*16, 64, 16, 16);
		
		if(selectedTab == 0) {
			String message = StatCollector.translateToLocal("gui.captchaDeck.name");
			fontRenderer.drawString(message, (this.width / 2) - fontRenderer.getStringWidth(message) / 2, yOffset + 12, 0x404040);
		} else if(selectedTab == 1) {
			String message = StatCollector.translateToLocal("gui.kindAbstrata.name");
			fontRenderer.drawString(message, (this.width / 2) - fontRenderer.getStringWidth(message) / 2, yOffset + 12, 0x404040);
			
			int i = 0;
			for(KindAbstratusType type : KindAbstratusList.getTypeList()) {
				String typeName = type.getDisplayName().toLowerCase();
				int xPos = xOffset+9+(columnWidth)*((i%columns)+1)-fontRenderer.getStringWidth(typeName);
				int yPos = yOffset+35+(fontRenderer.FONT_HEIGHT+1)*(int)(i/columns);
				
				if(!isPointInRegion(xOffset+9+(columnWidth)*(i%columns)+1, yPos-1, columnWidth-1, fontRenderer.FONT_HEIGHT+1, xcor, ycor))
					fontRenderer.drawString(typeName, xPos, yPos, 0xFFFFFF, false);
				else {
					drawRect(xOffset+9+(columnWidth)*(i%columns)+1, yPos-1, xOffset+9+(columnWidth)*((i%columns)+1), yPos+fontRenderer.FONT_HEIGHT, 0xFFAFAFAF);
					fontRenderer.drawString(typeName, xPos, yPos, 0x000000, false);
				}
				i++;
			}
			
		} else if(selectedTab == 2) {
			String cacheMessage = StatCollector.translateToLocal("gui.gristCache.name");
			fontRenderer.drawString(cacheMessage, (this.width / 2) - fontRenderer.getStringWidth(cacheMessage) / 2, yOffset + 12, 0x404040);
		}
		
		GL11.glColor3f(1,1,1);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		if(selectedTab == 0) {
			if(isPointInRegion(xOffset+81, yOffset+32, 16, 16, xcor, ycor))
				drawGradientRect(xOffset+81, yOffset+32, xOffset+81+16, yOffset+32+16, -2130706433, -2130706433);
		} else if(selectedTab == 2) {
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
				fontRenderer.drawString(Integer.toString(MinestuckPlayerData.getClientGrist().getGrist(GristType.values()[gristId])),xOffset + gristCountX + (gristCountXOffset * row - row), yOffset + gristCountY + (gristCountYOffset * column - column), 0xddddee);
				
			}
			
			if (tooltip != -1)
			{
				drawTooltip(StatCollector.translateToLocalFormatted("grist.format", GristType.values()[tooltip].getDisplayName()), xcor, ycor);
			} 
		}
		
		if(ycor < yOffset && ycor > yOffset-tabHeight+4)
			for(int i = 0; i < tabs; i++)
				if(xcor < xOffset+i*(tabWidth+2))
					break;
				else if(xcor < xOffset+i*(tabWidth+2)+tabWidth)
					drawTooltip(StatCollector.translateToLocal(tabNames[i]), xcor, ycor);
		
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		RenderHelper.enableStandardItemLighting();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);

	}
	
	@Override
	protected void mouseClicked(int xcor, int ycor, int mouseButton) {
		
		if(mouseButton == 0 && ycor < (height/2)-(getGuiHeight()/2)+(tabHeight/2) && ycor > (height/2)-(getGuiHeight()/2)-(tabHeight/2)+4) {
			int xOffset = (width/2)-(getGuiWidth()/2);
			for(int i = 0; i < tabs; i++)
				if(xcor < xOffset+i*(tabWidth+2))
					break;
				else if(xcor < xOffset+i*(tabWidth+2)+tabWidth) {
					selectedTab = i;
					mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
					return;
				}
		}
		super.mouseClicked(xcor, ycor, mouseButton);
	}
	
	private void drawIcon(int x,int y,String location) 
	{
//		this.mc.renderEngine.bindTexture("minestuck:/textures/grist/" + gristType + ".png");
		this.mc.getTextureManager().bindTexture(new ResourceLocation("minestuck",location));

		float scale = (float) 1/16;

		int iconX = 16;
		int iconY = 16;
		int iconU = 0;
		int iconV = 0;

		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV((double)(x + 0), (double)(y +  iconY), (double)this.zLevel, (double)((float)(iconU + 0) * scale), (double)((float)(iconV +  iconY) * scale));
		tessellator.addVertexWithUV((double)(x + iconX), (double)(y +  iconY), (double)this.zLevel, (double)((float)(iconU + iconX) * scale), (double)((float)(iconV +  iconY) * scale));
		tessellator.addVertexWithUV((double)(x + iconX), (double)(y + 0), (double)this.zLevel, (double)((float)(iconU + iconX) * scale), (double)((float)(iconV + 0) * scale));
		tessellator.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)this.zLevel, (double)((float)(iconU + 0) * scale), (double)((float)(iconV + 0) * scale));
		tessellator.draw();
	}

	@Override
	public boolean doesGuiPauseGame() 
	{
		return false;
	}

	private void drawTooltip(String text,int par2, int par3)
	{
		String[] list = {text};

		for (int k = 0; k < list.length; ++k)
		{
			list[k] = EnumChatFormatting.GRAY + list[k];
		}

		if (list.length != 0)
		{
			int k = fontRenderer.getStringWidth(text);

			int i1 = par2 + 12;
			int j1 = par3 - 12;
			int k1 = 8;

			if (list.length > 1)
			{
				k1 += 2 + (list.length - 1) * 10;
			}

			if (i1 + k > this.width)
			{
				i1 -= 28 + k;
			}

			if (j1 + k1 + 6 > this.height)
			{
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

			for (int k2 = 0; k2 < list.length; ++k2)
			{
				String s1 = list[k2];
				fontRenderer.drawStringWithShadow(s1, i1, j1, -1);

				if (k2 == 0)
				{
					j1 += 2;
				}

				j1 += 10;
			}

			this.zLevel = 0.0F;
			itemRenderer.zLevel = 0.0F;
		}
	}
	
	private ResourceLocation getGui() {
		switch(selectedTab) {
		case 0: return guiCaptchaDeckEmpty;
		case 1: return guiStrifeSelector;
		case 2: return guiGristcache;
		case 3: return guiEcheladder;
		default: return null;
		}
	}
	
	private int getGuiWidth() {
		switch(selectedTab) {
		case 0: return 178;
		case 1: return 228;
		case 2: return 226;
		case 3: return 256;
		default: return -1;
		}
	}
	
	private int getGuiHeight() {
		switch(selectedTab) {
		case 0: return 54;
		case 1: return 150;
		case 2: return 190;
		case 3: return 212;
		default: return -1;
		}
	}
	
	protected boolean isPointInRegion(int regionX, int regionY, int regionWidth, int regionHeight, int pointX, int pointY) {
		return pointX >= regionX && pointX < regionX + regionWidth && pointY >= regionY && pointY < regionY + regionHeight;
	}
}

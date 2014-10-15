package com.mraof.minestuck.client.gui.playerStats;

import static com.mraof.minestuck.client.gui.playerStats.GuiPlayerStats.editmodeTab;
import static com.mraof.minestuck.client.gui.playerStats.GuiPlayerStats.normalTab;
import static com.mraof.minestuck.client.gui.playerStats.GuiPlayerStats.tabHeight;
import static com.mraof.minestuck.client.gui.playerStats.GuiPlayerStats.tabOverlap;
import static com.mraof.minestuck.client.gui.playerStats.GuiPlayerStats.tabWidth;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.util.Debug;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public abstract class GuiPlayerStats extends GuiScreen {
	
	static final ResourceLocation icons = new ResourceLocation("minestuck", "textures/gui/icons.png");
	
	static final Class<? extends GuiScreen>[] normalGuis = new Class[]{GuiCaptchaDeck.class, GuiStrifeSpecibus.class, GuiGristCache.class, GuiInventoryEditmode.class};
	static final Class<? extends GuiScreen>[] editmodeGuis = new Class[]{GuiGristCache.class};
	static final String[] normalGuiNames = new String[]{"gui.captchaDeck.name","gui.strifeSpecibus.name","gui.gristCache.name","gui.echeladder.name"};
	static final String[] editmodeGuiNames = new String[]{"gui.deployList.name","gui.blockList.name","gui.gristCache.name"};
	
	static final int tabWidth = 28, tabHeight = 32, tabOverlap = 4;
	
	protected static RenderItem itemRenderer = new RenderItem();
	
	/**
	 * Integers indicating which tab that was last opened.
	 */
	public static int normalTab, editmodeTab;
	
	public Minecraft mc;
	
	protected int guiWidth, guiHeight;
	protected int xOffset, yOffset;
	
	private boolean mode;
	
	public GuiPlayerStats(boolean mode) {
		this.mode = mode;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		xOffset = (width-guiWidth)/2;
		yOffset = (height-guiHeight+tabHeight-tabOverlap)/2;
		mc = Minecraft.getMinecraft();
	}
	
	protected void drawTabs() {
		GL11.glColor3f(1,1,1);
		
		mc.getTextureManager().bindTexture(icons);
		
		for(int i = 0; i < (mode?normalGuis:editmodeGuis).length; i++)
			if((mode?normalTab:editmodeTab) != i)
				drawTexturedModalRect(xOffset+i*(tabWidth+2), yOffset-tabHeight+tabOverlap, i==0?0:tabWidth, 0, tabWidth, tabHeight);
	}
	
	protected void drawActiveTabAndOther(int xcor, int ycor) {
		GL11.glColor3f(1,1,1);
		
		mc.getTextureManager().bindTexture(icons);
		
		drawTexturedModalRect(xOffset+(mode?normalTab:editmodeTab)*(tabWidth+2), yOffset-tabHeight+tabOverlap,
				(mode?normalTab:editmodeTab)==0?0:tabWidth, tabHeight, tabWidth, tabHeight);
		
		for(int i = 0; i < (mode?normalGuis:editmodeGuis).length; i++)
			drawTexturedModalRect(xOffset + (tabWidth - 16)/2 + (tabWidth+2)*i, yOffset - tabHeight + tabOverlap + 8, i*16, tabHeight*2+(mode?0:16), 16, 16);
		
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		if(ycor < yOffset && ycor > yOffset-tabHeight+4)
			for(int i = 0; i < (mode?normalGuis:editmodeGuis).length; i++)
				if(xcor < xOffset+i*(tabWidth+2))
					break;
				else if(xcor < xOffset+i*(tabWidth+2)+tabWidth)
					drawTooltip(StatCollector.translateToLocal((mode?normalGuiNames:editmodeGuiNames)[i]), xcor, ycor);
	}
	
	@Override
	protected void mouseClicked(int xcor, int ycor, int mouseButton) {
		if(mouseButton == 0 && ycor < (height-guiHeight+tabHeight-tabOverlap)/2 && ycor > (height-guiHeight-tabHeight+tabOverlap)/2) {
			for(int i = 0; i < (mode?normalGuis:editmodeGuis).length; i++)
				if(xcor < xOffset+i*(tabWidth+2))
					break;
				else if(xcor < xOffset+i*(tabWidth+2)+tabWidth) {
					mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
					if(i != (mode?normalTab:editmodeTab)) {
						if(mode)
							normalTab = i;
						else editmodeTab = i;
						openGui(true);
					}
					return;
				}
		}
		super.mouseClicked(xcor, ycor, mouseButton);
	}
	
	public static void openGui(boolean reload) {
		Minecraft mc = Minecraft.getMinecraft();
		if(reload || mc.currentScreen == null)
			try {
				mc.displayGuiScreen((ClientEditHandler.isActive()?editmodeGuis[editmodeTab]:normalGuis[normalTab]).newInstance());
			} catch (Exception e) {
				e.printStackTrace();
			}
		else if(mc.currentScreen instanceof GuiPlayerStats || mc.currentScreen instanceof GuiPlayerStatsContainer)
			mc.currentScreen = null;
	}
	
	protected void drawTooltip(String text,int par2, int par3) {
		String[] list = {text};
		
		for (int k = 0; k < list.length; ++k) {
			list[k] = EnumChatFormatting.GRAY + list[k];
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
	
	protected boolean isPointInRegion(int regionX, int regionY, int regionWidth, int regionHeight, int pointX, int pointY) {
		return pointX >= regionX && pointX < regionX + regionWidth && pointY >= regionY && pointY < regionY + regionHeight;
	}
	
}

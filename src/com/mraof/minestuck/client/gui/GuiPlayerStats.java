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

import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.util.GristType;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiPlayerStats extends GuiScreen
{

	private static final ResourceLocation guiGristcache = new ResourceLocation("minestuck", "textures/gui/GristCache.png");
	private static final ResourceLocation icons = new ResourceLocation("minestuck", "textures/gui/icons.png");
	
	private static final String[] tabNames = {"gui.captchaDeck.name","gui.strifeSpecibus.name","gui.gristCache.name",""};
	private static final int[] guiWidths = {-1, -1, 226};
	private static final int[] guiHeights = {-1, -1, 190};
	private static final int tabWidth = 28, tabHeight = 32;
	
	private static final int tabs = 3;
	
	private int guiWidth = 226;
	private int guiHeight = 190;

	private static final int gristIconX = 21;
	private static final int gristIconY = 32;
	private static final int gristIconXOffset = 66;
	private static final int gristIconYOffset = 21;

	private static final int gristCountX = 44;
	private static final int gristCountY = 36;
	private static final int gristCountXOffset = 66;
	private static final int gristCountYOffset = 21;
	
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
		
		int xOffset = (width/2)-(guiWidth/2);
		int yOffset = (height/2)-(guiHeight/2)+(tabHeight/2);
		
		int tabX = xOffset;
		int currX = xOffset;
		this.mc.getTextureManager().bindTexture(icons);
		for(int i = 0; i < tabs; i++) {if(i != 0)
				currX = currX+tabWidth+2;
			if(selectedTab != i)
				drawTexturedModalRect(currX,yOffset-tabHeight+4,i==0?0:28,0, tabWidth, tabHeight);
			else tabX = currX;
		}
		
		this.mc.getTextureManager().bindTexture(guiGristcache);
		
		this.drawTexturedModalRect(xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		
		this.mc.getTextureManager().bindTexture(icons);
		drawTexturedModalRect(tabX,yOffset-tabHeight+4,selectedTab==0?0:28,32, tabWidth, tabHeight);
		
		for(int i = 0; i < tabs; i++)
			drawTexturedModalRect(xOffset+ (tabWidth/2 - 8) + (tabWidth+2)*i, yOffset - tabHeight + 12, i*16, 64, 16, 16);
		
		if(selectedTab == 2) {
			String cacheMessage = StatCollector.translateToLocal("gui.gristCache.name");
			fontRenderer.drawString(cacheMessage, (this.width / 2) - fontRenderer.getStringWidth(cacheMessage) / 2, yOffset + 12, 0x404040);
		}
		
		GL11.glColor3f(1,1,1);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		if(selectedTab == 2) {
			int tooltip = -1;
			
			for(int gristId = 0; gristId < GristType.allGrists; gristId++)
			{
				int row = (int) (gristId / 7);
				int column = (int) (gristId % 7);
				int gristXOffset = xOffset + gristIconX + (gristIconXOffset * row - row);
				int gristYOffset = yOffset + gristIconY + (gristIconYOffset * column - column);
				
				if (xcor > gristXOffset && xcor < gristXOffset + 16 && ycor > gristYOffset && ycor < gristYOffset + 16)
				{
					if(this.isPointInRegion(gristXOffset, gristYOffset, 16, 16, xcor, ycor));
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

		//  		int column = (int) (xcor - (this.width / 2)+(guiWidth/2)-gristIconX) / gristIconXOffset;
		//		int row = (int) (ycor - yOffset-gristIconY) / gristIconYOffset;
		//		int gristKind = 7*column + row;
		//		
		//    	if (gristKind >= 0 && gristKind < GristType.allGrists && row < 7 && row >= 0 && xcor > (this.width / 2)-(guiWidth/2)+gristIconX+(gristIconXOffset*column-column) && xcor < (this.width / 2)-(guiWidth/2)+gristIconX+(gristIconXOffset*column-column)+16 && ycor > yOffset+gristIconY+(gristIconYOffset*row-row) && ycor < yOffset+gristIconY+(gristIconYOffset*row-row)+16)  {
		//   		drawGristTooltip(EntityGrist.gristTypes[gristKind] + " Grist", xcor, ycor);
		//    	}
		////  drawGristTooltip(row + " " + column, xcor, ycor);
	}
	
	@Override
	protected void mouseClicked(int xcor, int ycor, int mouseButton) {
		
		if(mouseButton == 0 && ycor < (height/2)-(guiHeight/2)+(tabHeight/2) && ycor > (height/2)-(guiHeight/2)-(tabHeight/2)+4) {
			int xOffset = (width/2)-(guiWidth/2);
			for(int i = 0; i < tabs; i++)
				if(xcor < xOffset+i*(tabWidth+2))
					break;
				else if(xcor < xOffset+i*(tabWidth+2)+tabWidth) {
					if(guiWidths[i] != -1) {
						selectedTab = i;
						guiWidth = guiWidths[i];
						guiHeight = guiHeights[i];
					}
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
			//		    GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			//		    RenderHelper.disableStandardItemLighting();
			//		    GL11.glDisable(GL11.GL_LIGHTING);
			//		    GL11.glDisable(GL11.GL_DEPTH_TEST);
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
			//		    GL11.glEnable(GL11.GL_LIGHTING);
			//		    GL11.glEnable(GL11.GL_DEPTH_TEST);
			//		    RenderHelper.enableStandardItemLighting();
			//		    GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		}
	}
	protected boolean isPointInRegion(int par1, int par2, int par3, int par4, int par5, int par6)
	{
		int k1 = (this.width - guiWidth) / 2;
		int l1 = (this.height - guiHeight) / 2;
		par5 -= k1;
		par6 -= l1;
		return par5 >= par1 - 1 && par5 < par1 + par3 + 1 && par6 >= par2 - 1 && par6 < par2 + par4 + 1;
	}
}

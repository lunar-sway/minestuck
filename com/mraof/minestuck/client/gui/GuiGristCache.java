package com.mraof.minestuck.client.gui;


import java.util.Iterator;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.mraof.minestuck.entity.item.EntityGrist;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiGristCache extends GuiScreen
{
	private static final int guiWidth = 226;
	private static final int guiHeight = 232;
	private static final int yOffset = 20;
	private static final String cacheMessage = "Grist Cache";
	
	private static final int gristIconX = 21;
	private static final int gristIconY = 76;
	private static final int gristIconXOffset = 66;
	private static final int gristIconYOffset = 21;
	
	private static final int gristCountX = 44;
	private static final int gristCountY = 80;
	private static final int gristCountXOffset = 66;
	private static final int gristCountYOffset = 21;
	
	private Minecraft mc;
	private FontRenderer fontRenderer;
	private static RenderItem itemRenderer = new RenderItem();
    public static boolean visible = false;

    private String title = "CLASS of ASPECT";
    private String titleMessage = "";
    
    
    public GuiGristCache(Minecraft mc)
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
    	
	    if (titleMessage.isEmpty()) {
	    	titleMessage = mc.thePlayer.username.toUpperCase() + " : " + title;
	    }
	    
        this.mc.renderEngine.bindTexture("/gui/GristCache.png");
        this.drawTexturedModalRect((mc.displayWidth/4)-(guiWidth/2), yOffset, 0, 0, 226, 232);
        fontRenderer.drawString(cacheMessage, (mc.displayWidth/4)-fontRenderer.getStringWidth(cacheMessage)/2, yOffset*2, 4210752);
        fontRenderer.drawString(titleMessage, (mc.displayWidth/4)-fontRenderer.getStringWidth(titleMessage)/2, yOffset*2+10, 4210752);
        
        for(int gristId = 0; gristId < EntityGrist.gristTypes.length; gristId++) {
        	int row = (int) (gristId/7);
        	int column = (int) (gristId%7);
        	 this.drawGristIcon((mc.displayWidth/4)-(guiWidth/2)+gristIconX+(gristIconXOffset*row-row), yOffset+gristIconY+(gristIconYOffset*column-column), EntityGrist.gristTypes[gristId]);
        	 fontRenderer.drawString(Integer.toString(mc.thePlayer.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getCompoundTag("Grist").getInteger(EntityGrist.gristTypes[gristId])),(mc.displayWidth/4)-(guiWidth/2)+gristCountX+(gristCountXOffset*row-row), yOffset+gristCountY+(gristCountYOffset*column-column), 0xddddee);
        }
        
  		int column = (int) (xcor - (mc.displayWidth/4)+(guiWidth/2)-gristIconX) / gristIconXOffset;
		int row = (int) (ycor - yOffset-gristIconY) / gristIconYOffset;
		int gristKind = 7*column + row;
		
    	if (gristKind >= 0 && gristKind < EntityGrist.gristTypes.length && row < 7 && row >= 0 && xcor > (mc.displayWidth/4)-(guiWidth/2)+gristIconX+(gristIconXOffset*column-column) && xcor < (mc.displayWidth/4)-(guiWidth/2)+gristIconX+(gristIconXOffset*column-column)+16 && ycor > yOffset+gristIconY+(gristIconYOffset*row-row) && ycor < yOffset+gristIconY+(gristIconYOffset*row-row)+16)  {
    		drawGristTooltip(EntityGrist.gristTypes[gristKind] + " Grist", xcor, ycor);
    	}
	}
	
	private void drawGristIcon(int x,int y,String gristType) {
		
		this.mc.renderEngine.bindTexture("/mods/Minestuck/textures/grist/" + gristType + ".png");
        
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
    
    private void drawGristTooltip(String text,int par2, int par3)
    {
    	String[] list = {text};

        for (int k = 0; k < list.length; ++k)
        {
                list[k] = EnumChatFormatting.GRAY + list[k];
        }

        if (list.length != 0)
		{
		    GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		    RenderHelper.disableStandardItemLighting();
		    GL11.glDisable(GL11.GL_LIGHTING);
		    GL11.glDisable(GL11.GL_DEPTH_TEST);
		    int k = 0;
		
			for (int i = 1; i < list.length; i++)
		    {
		        String s = list[i];
		        int l = fontRenderer.getStringWidth(s);
		
		        if (l > k)
		        {
		            k = l;
		        }
		    }
		
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
		        String s1 = (String)list[k2];
		        fontRenderer.drawStringWithShadow(s1, i1, j1, -1);
		
		        if (k2 == 0)
		        {
		            j1 += 2;
		        }
		
		        j1 += 10;
		    }
		
		    this.zLevel = 0.0F;
		    itemRenderer.zLevel = 0.0F;
		    GL11.glEnable(GL11.GL_LIGHTING);
		    GL11.glEnable(GL11.GL_DEPTH_TEST);
		    RenderHelper.enableStandardItemLighting();
		    GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		}
    }
}

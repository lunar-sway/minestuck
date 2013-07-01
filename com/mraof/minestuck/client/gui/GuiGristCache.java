package com.mraof.minestuck.client.gui;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;

import com.mraof.minestuck.entity.item.EntityGrist;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiGristCache extends GuiScreen
{
	private Minecraft mc;
	private FontRenderer fontRenderer;
    public static boolean visible = false;

    public GuiGristCache(Minecraft mc)
	{
		super();
		
		this.mc = mc;
        this.fontRenderer = mc.fontRenderer;
	}
    @Override
    public void drawScreen(int par1, int par2, float par3) 
    {
    	super.drawScreen(par1, par2, par3);
    	this.drawDefaultBackground();
	    for(int gristId = 0; gristId < EntityGrist.gristTypes.length; gristId++)
	    	drawString(fontRenderer, EntityGrist.gristTypes[gristId] + " Grist: " + mc.thePlayer.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getCompoundTag("Grist").getInteger(EntityGrist.gristTypes[gristId]), 0, 20 + gristId * 8, 0xddddee);
	}
    @Override
    public boolean doesGuiPauseGame() 
    {
    	return false;
    }
}

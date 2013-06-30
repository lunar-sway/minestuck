package com.mraof.minestuck.client.gui;


import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;

import com.mraof.minestuck.entity.item.EntityGrist;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiGristCache extends Gui
{
	private static final int guiWidth = 226;
	private static final int guiHeight = 232;
	private static String cacheMessage = "Grist Cache";
	private Minecraft mc;
	private FontRenderer fontRenderer;
    public static boolean visible = false;

    public GuiGristCache(Minecraft mc)
	{
		super();
		
		this.mc = mc;
        this.fontRenderer = mc.fontRenderer;
	}
	//name of function doesn't actually matter
	@ForgeSubscribe(priority = EventPriority.NORMAL)
	public void onRenderGameOverlay(RenderGameOverlayEvent event)
	{
	    if(event.isCancelable() || event.type != ElementType.ALL  || !this.visible)
	    {
	      return;
	    }
//	    for(int gristId = 0; gristId < EntityGrist.gristTypes.length; gristId++)
//	   		drawString(fontRenderer, EntityGrist.gristTypes[gristId] + " Grist: " + mc.thePlayer.getEntityData().getCompoundTag("Grist").getInteger(EntityGrist.gristTypes[gristId]), 0, 20 + gristId * 8, 0xddddee);

        this.mc.renderEngine.bindTexture("/gui/GristCache.png");
        this.drawTexturedModalRect((mc.displayWidth/4)-(guiWidth/2), 10, 0, 0, 226, 232);
        fontRenderer.drawString(cacheMessage, (mc.displayWidth/4)-fontRenderer.getStringWidth(cacheMessage)/2, 20, 4210752);
	}
}

package com.mraof.minestuck.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;

public class GuiGristCache extends Gui
{
	private Minecraft mc;
	private FontRenderer fontRenderer;
	public GuiGristCache(Minecraft mc)
	{
		super();
		
		this.mc = mc;
        this.fontRenderer = mc.fontRenderer;
	}
	
	@ForgeSubscribe(priority = EventPriority.NORMAL)
	public void onRenderExperienceBar(RenderGameOverlayEvent event)
	{
	    if(event.isCancelable() || event.type != ElementType.EXPERIENCE)
	    {      
	      return;
	    }
		drawString(fontRenderer, "Build Grist: " + mc.thePlayer.getEntityData().getCompoundTag("Grist").getInteger("Build"), 0, 30, 0x00aaff);
		drawString(fontRenderer, "Shale Grist: " + mc.thePlayer.getEntityData().getCompoundTag("Grist").getInteger("Shale"), 0, 38, 0xddddee);
	}
}

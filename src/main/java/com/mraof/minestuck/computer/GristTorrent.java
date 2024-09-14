package com.mraof.minestuck.computer;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.client.gui.ComputerScreen;
import com.mraof.minestuck.client.gui.TorrentScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class GristTorrent extends ComputerProgram
{
	public static final ResourceLocation GUI_MAIN = new ResourceLocation(Minestuck.MOD_ID, "textures/gui/torrent.png");
	public static final ResourceLocation ICON = new ResourceLocation(Minestuck.MOD_ID, "textures/gui/desktop_icon/torrent.png");
	
	/*
	private static final int GUI_WIDTH = 176;
	private static final int GUI_HEIGHT = 166;
	
	private int xOffset;
	private int yOffset;
	 */
	
	@Override
	public void onInitGui(ComputerScreen gui)
	{
		gui.getMinecraft().setScreen(null);
		gui.getMinecraft().setScreen(new TorrentScreen(gui.be));
		
		/*super.onInitGui(gui);
		
		yOffset = (gui.height / 2) - (GUI_HEIGHT / 2);
		xOffset = (gui.width / 2) - (GUI_WIDTH / 2);
		 */
	}
	
	@Override
	public void paintGui(GuiGraphics guiGraphics, ComputerScreen gui, ComputerBlockEntity be)
	{
		//guiGraphics.blit(GUI_MAIN, xOffset, yOffset, 0, 0, GUI_WIDTH, GUI_HEIGHT);
	}
	
	@Override
	public ResourceLocation getIcon()
	{
		return ICON;
	}
}
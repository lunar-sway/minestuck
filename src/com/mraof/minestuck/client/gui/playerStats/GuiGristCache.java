package com.mraof.minestuck.client.gui.playerStats;

import com.mraof.minestuck.editmode.ClientEditHandler;

public class GuiGristCache extends GuiPlayerStats {

	public GuiGristCache() {
		super(!ClientEditHandler.isActive());
		guiWidth = 226;
		guiHeight = 190;
	}

}

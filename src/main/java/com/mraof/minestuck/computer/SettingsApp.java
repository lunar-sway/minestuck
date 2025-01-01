package com.mraof.minestuck.computer;

import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.client.gui.ComputerScreen;
import com.mraof.minestuck.client.gui.ComputerThemeScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.List;

public final class SettingsApp extends ButtonListProgram
{
	public static final String THEME = "minestuck.program.settings.theme";
	public static final String TITLE = "minestuck.program.settings.title";
	
	@Override
	public void onUpdate(ComputerScreen gui)
	{
		updateButtons(List.of(new ButtonData(Component.translatable(THEME), () -> openThemeScreen(gui.be))));
	}
	
	private static void openThemeScreen(ComputerBlockEntity computer)
	{
		Minecraft.getInstance().setScreen(null);
		Minecraft.getInstance().setScreen(new ComputerThemeScreen(computer));
	}
	
	@Override
	public final void render(GuiGraphics guiGraphics, ComputerScreen gui)
	{
		ProgramGui.drawHeaderMessage(Component.translatable(TITLE), guiGraphics, gui);
	}
}

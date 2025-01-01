package com.mraof.minestuck.computer;

import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.client.gui.ComputerThemeScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.List;

public class SettingsApp extends ButtonListProgram
{
	public static final String THEME = "minestuck.program.settings.theme";
	public static final String TITLE = "minestuck.program.settings.title";
	
	@Override
	protected InterfaceData getInterfaceData(ComputerBlockEntity be)
	{
		return new InterfaceData(Component.translatable(TITLE),
				List.of(new ButtonData(Component.translatable(THEME), () -> openThemeScreen(be))));
	}
	
	private static void openThemeScreen(ComputerBlockEntity computer)
	{
		Minecraft.getInstance().setScreen(null);
		Minecraft.getInstance().setScreen(new ComputerThemeScreen(computer));
	}
}

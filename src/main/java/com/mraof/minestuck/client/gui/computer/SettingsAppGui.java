package com.mraof.minestuck.client.gui.computer;

import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.computer.ProgramType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.List;

public final class SettingsAppGui implements ProgramGui<ProgramType.EmptyData>
{
	public static final String NAME = "minestuck.program.settings";
	public static final String THEME = "minestuck.program.settings.theme";
	public static final String DISK_MANAGER = "minestuck.program.settings.disk_manager";
	public static final String TITLE = "minestuck.program.settings.title";
	
	private final ButtonListHelper buttonListHelper = new ButtonListHelper();
	
	@Override
	public void onInit(ComputerScreen gui)
	{
		this.buttonListHelper.init(gui);
	}
	
	@Override
	public void onUpdate(ComputerScreen gui, ProgramType.EmptyData data)
	{
		this.buttonListHelper.updateButtons(List.of(
				new ButtonListHelper.ButtonData(Component.translatable(THEME), () -> openThemeScreen(gui.be)),
				new ButtonListHelper.ButtonData(Component.translatable(DISK_MANAGER), () -> openDiskManagerScreen(gui))
		));
	}
	
	private static void openThemeScreen(ComputerBlockEntity computer)
	{
		Minecraft.getInstance().setScreen(null);
		Minecraft.getInstance().setScreen(new ComputerThemeScreen(computer));
	}
	
	private void openDiskManagerScreen(ComputerScreen gui)
	{
		Minecraft.getInstance().setScreen(null);
		Minecraft.getInstance().setScreen(new DiskManagerScreen(gui));
	}
	
	@Override
	public final void render(GuiGraphics guiGraphics, ComputerScreen gui)
	{
		ProgramGui.drawHeaderMessage(Component.translatable(TITLE), guiGraphics, gui);
	}
}

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
	public void onInit(ThemedScreen gui)
	{
		this.buttonListHelper.init(gui);
	}
	
	@Override
	public void onUpdate(ThemedScreen gui, ProgramType.EmptyData data)
	{
		this.buttonListHelper.updateButtons(List.of(
				new ButtonListHelper.ButtonData(Component.translatable(THEME), () -> openThemeScreen(gui.computer)),
				new ButtonListHelper.ButtonData(Component.translatable(DISK_MANAGER), () -> openDiskManagerScreen(gui.computer))
		));
	}
	
	private static void openThemeScreen(ComputerBlockEntity computer)
	{
		Minecraft.getInstance().setScreen(null);
		Minecraft.getInstance().setScreen(new ComputerThemeScreen(computer));
	}
	
	private void openDiskManagerScreen(ComputerBlockEntity computer)
	{
		Minecraft.getInstance().setScreen(null);
		Minecraft.getInstance().setScreen(new DiskManagerScreen(computer));
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, ThemedScreen gui)
	{
		ProgramGui.drawHeaderMessage(Component.translatable(TITLE), guiGraphics, gui);
	}
}

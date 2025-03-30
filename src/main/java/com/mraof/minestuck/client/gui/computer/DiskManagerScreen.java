package com.mraof.minestuck.client.gui.computer;

import com.mraof.minestuck.computer.ProgramType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

/**
 * Shows the disks loaded into the computer and allows for them to be ejected.
 */
@ParametersAreNonnullByDefault
public class DiskManagerScreen extends ThemedScreen
{
	public static final String TITLE = "minestuck.disk_manager";
	
	private final ButtonListHelper buttonListHelper = new ButtonListHelper();
	private final ComputerScreen gui;
	
	public DiskManagerScreen(ComputerScreen gui)
	{
		super(gui.be, Component.translatable(TITLE));
		
		this.gui = gui;
		this.buttonListHelper.init(gui);
	}
	
	@Override
	public void init()
	{
		super.init();
		
		recreateButtons();
	}
	
	public void recreateButtons()
	{
		//TODO add blank disks
		List<ItemStack> computerDisks = computer.getProgramDisks();
		this.buttonListHelper.updateButtons(List.of(
				//new ButtonListHelper.ButtonData(Component.translatable(THEME), () -> openThemeScreen(gui.be)),
				//new ButtonListHelper.ButtonData(Component.translatable(DISK_MANAGER), () -> openDiskManagerScreen(gui))
		));
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
	{
		super.render(guiGraphics, mouseX, mouseY, partialTick);
		ProgramGui.drawHeaderMessage(Component.translatable(TITLE), guiGraphics, gui);
	}
	
	private void ejectDisk(Item item)
	{
		//computer;
		//computer.dropItems();
		//Containers.dropItemStack(computer.level, this.getBlockPos(), this.blankDisks);
		
		recreateButtons();
	}
}

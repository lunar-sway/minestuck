package com.mraof.minestuck.computer;

import com.mraof.minestuck.client.gui.ComputerScreen;
import com.mraof.minestuck.network.computer.BurnDiskPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public final class DiskBurner extends ButtonListProgram
{
	public static final String NEED_CODE = "minestuck.program.disk_burner.needs_code";
	public static final String NO_DISKS = "minestuck.program.disk_burner.needs_disks";
	public static final String BURN_SERVER_DISK = "minestuck.program.disk_burner.burn_server_disk";
	public static final String BURN_CLIENT_DISK = "minestuck.program.disk_burner.burn_client_disk";
	public static final String CHOOSE = "minestuck.program.disk_burner.choose";
	
	private Component message;
	
	@Override
	public void onUpdate(ComputerScreen gui)
	{
		if(!gui.be.hasAllCode())
		{
			this.message = Component.translatable(NEED_CODE);
			updateButtons(List.of());
		} else if(gui.be.blankDisksStored == 0)
		{
			this.message = Component.translatable(NO_DISKS);
			updateButtons(List.of());
		} else
		{
			this.message = Component.translatable(CHOOSE);
			updateButtons(List.of(
					new ButtonData(Component.translatable(BURN_SERVER_DISK),
							() -> PacketDistributor.sendToServer(BurnDiskPacket.create(gui.be, false))),
					new ButtonData(Component.translatable(BURN_CLIENT_DISK),
							() -> PacketDistributor.sendToServer(BurnDiskPacket.create(gui.be, true)))));
		}
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, ComputerScreen gui)
	{
		ProgramGui.drawHeaderMessage(this.message, guiGraphics, gui);
	}
}

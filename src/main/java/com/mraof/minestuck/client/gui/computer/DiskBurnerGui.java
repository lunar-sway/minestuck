package com.mraof.minestuck.client.gui.computer;

import com.mraof.minestuck.computer.DiskBurnerData;
import com.mraof.minestuck.network.computer.BurnDiskPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public final class DiskBurnerGui implements ProgramGui<DiskBurnerData>
{
	public static final String NAME = "minestuck.program.disk_burner";
	public static final String NEED_CODE = "minestuck.program.disk_burner.needs_code";
	public static final String NO_DISKS = "minestuck.program.disk_burner.needs_disks";
	public static final String BURN_SERVER_DISK = "minestuck.program.disk_burner.burn_server_disk";
	public static final String BURN_CLIENT_DISK = "minestuck.program.disk_burner.burn_client_disk";
	public static final String CHOOSE = "minestuck.program.disk_burner.choose";
	
	private final ButtonListHelper buttonListHelper = new ButtonListHelper();
	private Component message;
	
	@Override
	public void onInit(ThemedScreen gui)
	{
		this.buttonListHelper.init(gui);
	}
	
	@Override
	public void onUpdate(ThemedScreen gui, DiskBurnerData data)
	{
		if(!data.hasAllCode())
		{
			this.message = Component.translatable(NEED_CODE);
			this.buttonListHelper.updateButtons(List.of());
		} else if(gui.computer.getBlankDisks().isEmpty())
		{
			this.message = Component.translatable(NO_DISKS);
			this.buttonListHelper.updateButtons(List.of());
		} else
		{
			this.message = Component.translatable(CHOOSE);
			this.buttonListHelper.updateButtons(List.of(
					new ButtonListHelper.ButtonData(Component.translatable(BURN_SERVER_DISK),
							() -> PacketDistributor.sendToServer(BurnDiskPacket.create(gui.computer, false))),
					new ButtonListHelper.ButtonData(Component.translatable(BURN_CLIENT_DISK),
							() -> PacketDistributor.sendToServer(BurnDiskPacket.create(gui.computer, true)))));
		}
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, ThemedScreen gui)
	{
		ProgramGui.drawHeaderMessage(this.message, guiGraphics, gui);
	}
}

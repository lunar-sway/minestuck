package com.mraof.minestuck.client.gui.computer;

import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.computer.ProgramType;
import com.mraof.minestuck.item.components.MSItemComponents;
import com.mraof.minestuck.network.computer.EjectDiskPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Shows the disks loaded into the computer and allows for them to be ejected.
 */
@ParametersAreNonnullByDefault
public class DiskManagerScreen extends ThemedScreen
{
	public static final String TITLE = "minestuck.disk_manager";
	
	private final ButtonListHelper buttonListHelper = new ButtonListHelper();
	
	public DiskManagerScreen(ComputerBlockEntity computer)
	{
		super(computer, Component.translatable(TITLE));
	}
	
	@Override
	public void init()
	{
		super.init();
		this.buttonListHelper.init(this);
		recreateButtons();
	}
	
	public void recreateButtons()
	{
		List<ButtonListHelper.ButtonData> diskButtons = new ArrayList<>();
		
		AtomicInteger i = new AtomicInteger();
		computer.getDisks().forEach(disk -> {
			int diskIndex = i.getAndIncrement();
			Holder<ProgramType<?>> programTypeHolder = disk.getComponents().get(MSItemComponents.PROGRAM_TYPE.get());
			Component diskName = programTypeHolder != null ? programTypeHolder.value().name() : disk.getDisplayName();
			diskButtons.add(new ButtonListHelper.ButtonData(Component.literal("Eject ").append(diskName), () -> ejectDisk(diskIndex)));
		});
		
		this.buttonListHelper.updateButtons(diskButtons);
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
	{
		super.render(guiGraphics, mouseX, mouseY, partialTick);
		ProgramGui.drawHeaderMessage(Component.translatable(TITLE), guiGraphics, this);
	}
	
	private void ejectDisk(int index)
	{
		PacketDistributor.sendToServer(EjectDiskPacket.create(computer, index));
		minecraft.setScreen(null);
	}
}

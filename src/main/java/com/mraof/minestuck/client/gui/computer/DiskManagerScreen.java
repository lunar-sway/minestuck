package com.mraof.minestuck.client.gui.computer;

import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.computer.ProgramType;
import com.mraof.minestuck.item.components.MSItemComponents;
import com.mraof.minestuck.network.computer.EjectDiskPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

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
		
		computer.getDisks().forEach(disk -> {
			Holder<ProgramType<?>> programTypeHolder = disk.getComponents().get(MSItemComponents.PROGRAM_TYPE.get());
			Component diskName = programTypeHolder != null ? programTypeHolder.value().name() : disk.getDisplayName();
			diskButtons.add(new ButtonListHelper.ButtonData(Component.literal("Eject ").append(diskName), () -> ejectDisk(disk)));
		});
		
		this.buttonListHelper.updateButtons(diskButtons);
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
	{
		super.render(guiGraphics, mouseX, mouseY, partialTick);
		ProgramGui.drawHeaderMessage(Component.translatable(TITLE), guiGraphics, this);
	}
	
	private void ejectDisk(ItemStack stack)
	{
		PacketDistributor.sendToServer(EjectDiskPacket.create(computer, stack));
		minecraft.setScreen(null);
	}
}

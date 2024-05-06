package com.mraof.minestuck.computer;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.network.computer.BurnDiskPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class DiskBurner extends ButtonListProgram
{
	public static final String NEED_CODE = "minestuck.program.disk_burner.needs_code";
	public static final String NO_DISKS = "minestuck.program.disk_burner.needs_disks";
	public static final String BURN_SERVER_DISK = "minestuck.program.disk_burner.burn_server_disk";
	public static final String BURN_CLIENT_DISK = "minestuck.program.disk_burner.burn_client_disk";
	public static final String CHOOSE = "minestuck.program.disk_burner.choose";
	
	public static final ResourceLocation ICON = ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, "textures/gui/desktop_icon/disk_burner.png");
	
	@Override
	protected InterfaceData getInterfaceData(ComputerBlockEntity be)
	{
		if(!be.hasAllCode())
			return new InterfaceData(new UnlocalizedString(NEED_CODE), List.of());
		
		if(be.blankDisksStored == 0)
			return new InterfaceData(new UnlocalizedString(NO_DISKS), List.of());
		
		return new InterfaceData(new UnlocalizedString(CHOOSE), List.of(
				new ButtonData(Component.translatable(BURN_SERVER_DISK),
						() -> PacketDistributor.sendToServer(BurnDiskPacket.create(be, 1))),
				new ButtonData(Component.translatable(BURN_CLIENT_DISK),
						() -> PacketDistributor.sendToServer(BurnDiskPacket.create(be, 0)))));
	}
	
	@Override
	public ResourceLocation getIcon()
	{
		return ICON;
	}
}

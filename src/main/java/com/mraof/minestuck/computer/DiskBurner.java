package com.mraof.minestuck.computer;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.network.computer.BurnDiskPacket;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;

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
			return new InterfaceData(new UnlocalizedString(NEED_CODE), new ArrayList<>());
		
		if(be.blankDisksStored == 0)
			return new InterfaceData(new UnlocalizedString(NO_DISKS), new ArrayList<>());
		
		ArrayList<UnlocalizedString> buttonTexts = new ArrayList<>();
		UnlocalizedString message = new UnlocalizedString(CHOOSE);
		buttonTexts.add(new UnlocalizedString(BURN_SERVER_DISK));
		buttonTexts.add(new UnlocalizedString(BURN_CLIENT_DISK));
		return new InterfaceData(message, buttonTexts);
	}
	
	@Override
	public void onButtonPressed(ComputerBlockEntity be, String buttonName, Object[] data)
	{
		if(buttonName.equals(BURN_CLIENT_DISK))
		{
			PacketDistributor.sendToServer(BurnDiskPacket.create(be, 0));
		} else if(buttonName.equals(BURN_SERVER_DISK))
		{
			PacketDistributor.sendToServer(BurnDiskPacket.create(be, 1));
		}
	}
	
	@Override
	public ResourceLocation getIcon()
	{
		return ICON;
	}
}

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
	
	public static final ResourceLocation ICON = new ResourceLocation(Minestuck.MOD_ID, "textures/gui/desktop_icon/disk_burner.png");
	
	@Override
	public ArrayList<UnlocalizedString> getStringList(ComputerBlockEntity be)
	{
		ArrayList<UnlocalizedString> list = new ArrayList<>();
		
		if(!be.hasAllCode())
			list.add(new UnlocalizedString(NEED_CODE));
		else if (be.blankDisksStored == 0)
			list.add(new UnlocalizedString(NO_DISKS));
		else
		{
			list.add(new UnlocalizedString(CHOOSE));
			
			list.add(new UnlocalizedString(BURN_SERVER_DISK));
			list.add(new UnlocalizedString(BURN_CLIENT_DISK));
		}
		return list;
	}
	
	@Override
	public void onButtonPressed(ComputerBlockEntity be, String buttonName, Object[] data)
	{
		if(buttonName.equals(BURN_CLIENT_DISK))
		{
			PacketDistributor.SERVER.noArg().send(BurnDiskPacket.create(be, 0));
		} else if(buttonName.equals(BURN_SERVER_DISK))
		{
			PacketDistributor.SERVER.noArg().send(BurnDiskPacket.create(be, 1));
		}
	}
	
	@Override
	public ResourceLocation getIcon()
	{
		return ICON;
	}
}
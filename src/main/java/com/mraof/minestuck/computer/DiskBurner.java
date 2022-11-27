package com.mraof.minestuck.computer;

import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.computer.*;
import com.mraof.minestuck.util.MSTags;

import java.util.ArrayList;

public class DiskBurner extends ButtonListProgram
{
	public static final String NAME = "minestuck.program.disk_burner.name";
	public static final String BURN_SERVER_DISK = "minestuck.program.disk_burner.burn_server_disk";
	public static final String BURN_CLIENT_DISK = "minestuck.program.disk_burner.burn_client_disk";
	public static final String CHOOSE = "minestuck.program.disk_burner.choose";
	
	@Override
	public ArrayList<UnlocalizedString> getStringList(ComputerBlockEntity be)
	{
		ArrayList<UnlocalizedString> list = new ArrayList<>();
		list.add(new UnlocalizedString(CHOOSE));
		
		if(be != null && be.hieroglyphsStored.containsAll(MSTags.getBlocksFromTag(MSTags.Blocks.GREEN_HIEROGLYPHS)) && be.blankDisksStored > 0 && be.hasParadoxInfoStored)
		{
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
			MSPacketHandler.sendToServer(BurnDiskPacket.create(be, 0));
		} else if(buttonName.equals(BURN_SERVER_DISK))
		{
			MSPacketHandler.sendToServer(BurnDiskPacket.create(be, 1));
		}
	}
	
	@Override
	public String getName()
	{
		return NAME;
	}
}
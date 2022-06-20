package com.mraof.minestuck.computer;

import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.computer.*;
import com.mraof.minestuck.tileentity.ComputerTileEntity;
import com.mraof.minestuck.util.MSTags;

import java.util.ArrayList;

public class DiskBurner extends ButtonListProgram
{
	public static final String NAME = "minestuck.disk_burner_program";
	public static final String BURN_SERVER_DISK = "minestuck.burn_server_disk";
	public static final String BURN_CLIENT_DISK = "minestuck.burn_client_disk";
	public static final String CHOOSE = "minestuck.choose";
	
	@Override
	public ArrayList<UnlocalizedString> getStringList(ComputerTileEntity te)
	{
		ArrayList<UnlocalizedString> list = new ArrayList<>();
		list.add(new UnlocalizedString(CHOOSE));
		
		//TODO remove the buttons during an earlier check, it takes two result-less clicks to remove them
		if(te != null && te.hieroglyphsStored.containsAll(MSTags.Blocks.GREEN_HIEROGLYPHS.getValues()) && te.blankDisksStored > 0)
		{
			list.add(new UnlocalizedString(BURN_SERVER_DISK));
			list.add(new UnlocalizedString(BURN_CLIENT_DISK));
		}
		
		return list;
	}
	
	@Override
	public void onButtonPressed(ComputerTileEntity te, String buttonName, Object[] data)
	{
		if(buttonName.equals(BURN_CLIENT_DISK))
		{
			MSPacketHandler.sendToServer(BurnDiskPacket.create(te, 0));
		}
		else if(buttonName.equals(BURN_SERVER_DISK))
		{
			MSPacketHandler.sendToServer(BurnDiskPacket.create(te, 1));
		}
	}
	
	@Override
	public String getName()
	{
		return NAME;
	}
}
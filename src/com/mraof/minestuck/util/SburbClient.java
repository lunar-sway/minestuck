package com.mraof.minestuck.util;

import java.util.ArrayList;
import java.util.Map;

import com.mraof.minestuck.client.gui.ColorSelectorScreen;
import com.mraof.minestuck.network.skaianet.ReducedConnection;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;

import com.mraof.minestuck.network.skaianet.SkaiaClient;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.tileentity.ComputerTileEntity;

public class SburbClient extends ButtonListProgram {
	
	@Override
	public ArrayList<UnlocalizedString> getStringList(ComputerTileEntity te)
	{
		ArrayList<UnlocalizedString> list = new ArrayList<>();
		CompoundNBT nbt = te.getData(getId());
		
		ReducedConnection c = SkaiaClient.getClientConnection(te.ownerId);
		if(nbt.getBoolean("connectedToServer") && c != null) //If it is connected to someone.
		{
			String displayPlayer = c.getServerDisplayName();
			list.add(new UnlocalizedString("computer.messageConnect", displayPlayer));
			list.add(new UnlocalizedString("computer.buttonClose"));
		} else if(nbt.getBoolean("isResuming"))
		{
			list.add(new UnlocalizedString("computer.messageResumeClient"));
			list.add(new UnlocalizedString("computer.buttonClose"));
		} else if(!SkaiaClient.isActive(te.ownerId, true)) //If the player doesn't have an other active client
		{
			list.add(new UnlocalizedString("computer.messageSelect"));
			if(SkaiaClient.getAssociatedPartner(te.ownerId, true) != -1) //If it has a resumable connection
				list.add(new UnlocalizedString("computer.buttonResume"));
			for (Map.Entry<Integer, String> entry : SkaiaClient.getAvailableServers(te.ownerId).entrySet())
				list.add(new UnlocalizedString("computer.buttonConnect", entry.getValue(), entry.getKey()));
		} else list.add(new UnlocalizedString("computer.messageClientActive"));
		if(SkaiaClient.canSelect(te.ownerId))
			list.add(new UnlocalizedString("computer.selectColor"));
		return list;
	}
	
	@Override
	public void onButtonPressed(ComputerTileEntity te, String buttonName, Object[] data)
	{
		if(buttonName.equals("computer.buttonResume"))
			SkaiaClient.sendConnectRequest(te, SkaiaClient.getAssociatedPartner(te.ownerId, true), true);
		else if(buttonName.equals("computer.buttonConnect"))
			SkaiaClient.sendConnectRequest(te, (Integer) data[1], true);
		else if(buttonName.equals("computer.buttonClose"))
			SkaiaClient.sendCloseRequest(te, te.getData(getId()).getBoolean("isResuming")?-1:SkaiaClient.getClientConnection(te.ownerId).getServerId(), true);
		else if(buttonName.equals("computer.selectColor"))
			Minecraft.getInstance().displayGuiScreen(new ColorSelectorScreen(false));
	}
	
	@Override
	public String getName()
	{
		return "computer.programClient";
	}
	
	@Override
	public void onClosed(ComputerTileEntity te)
	{
		if(te.getData(0).getBoolean("connectedToServer") && SkaianetHandler.get(te.getWorld()).getActiveConnection(te.owner) != null)
			SkaianetHandler.get(te.getWorld()).closeConnection(te.owner, SkaianetHandler.get(te.getWorld()).getActiveConnection(te.owner).getServerIdentifier(), true);
		else if(te.getData(0).getBoolean("isResuming"))
			SkaianetHandler.get(te.getWorld()).closeConnection(te.owner, null, true);
	}
	
}

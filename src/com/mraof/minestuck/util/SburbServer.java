package com.mraof.minestuck.util;

import java.util.ArrayList;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.network.ClientEditPacket;
import com.mraof.minestuck.network.MinestuckPacketHandler;
import com.mraof.minestuck.network.skaianet.ComputerData;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaiaClient;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.tileentity.ComputerTileEntity;

public class SburbServer extends ButtonListProgram
{
	
	@Override
	public ArrayList<UnlocalizedString> getStringList(ComputerTileEntity te)
	{
		int clientId = te.getData(1).contains("connectedClient")?te.getData(1).getInt("connectedClient"):-1;
		SburbConnection connection = clientId != -1 ? SkaiaClient.getClientConnection(clientId) : null;
		if(connection != null && connection.getServerId() != te.ownerId)
			connection = null;
		
		ArrayList<UnlocalizedString> list = new ArrayList<UnlocalizedString>();
		String displayPlayer= connection==null?"UNDEFINED":connection.getClientDisplayName();
		if (connection != null)
		{
			list.add(new UnlocalizedString("computer.messageConnect", displayPlayer));
			list.add(new UnlocalizedString("computer.buttonClose"));
			list.add(new UnlocalizedString(MinestuckConfig.clientGiveItems ? "computer.buttonGive" : "computer.buttonEdit"));
		} else if (te.getData(getId()).getBoolean("isOpen"))
		{
			list.add(new UnlocalizedString("computer.messageResumeServer"));
			list.add(new UnlocalizedString("computer.buttonClose"));
		} else if(SkaiaClient.isActive(te.ownerId, false))
			list.add(new UnlocalizedString("computer.messageServerActive"));
		else
		{
			list.add(new UnlocalizedString("computer.messageOffline"));
			list.add(new UnlocalizedString("computer.buttonOpen"));
			if(SkaiaClient.getAssociatedPartner(te.ownerId, false) != -1)
				list.add(new UnlocalizedString("computer.buttonResume"));
		}
		return list;
	}
	
	@Override
	public void onButtonPressed(ComputerTileEntity te, String buttonName, Object[] data) {
		if(buttonName.equals("computer.buttonEdit") || buttonName.equals("computer.buttonGive"))
		{
			ClientEditPacket packet = ClientEditPacket.activate(te.ownerId, te.getData(getId()).getInt("connectedClient"));
			MinestuckPacketHandler.sendToServer(packet);
		} else if(buttonName.equals("computer.buttonResume"))
			SkaiaClient.sendConnectRequest(te, SkaiaClient.getAssociatedPartner(te.ownerId, false), false);
		else if(buttonName.equals("computer.buttonOpen"))
			SkaiaClient.sendConnectRequest(te, -1, false);
		else if(buttonName.equals("computer.buttonClose"))
			SkaiaClient.sendCloseRequest(te, te.getData(getId()).getBoolean("isOpen")?-1:te.getData(getId()).getInt("connectedClient"), false);
	}
	
	@Override
	public String getName()
	{
		return "computer.programServer";
	}
	
	@Override
	public void onClosed(ComputerTileEntity te)
	{
		SburbConnection c = SkaianetHandler.get(te.getWorld()).getServerConnection(ComputerData.createData(te));
		if(c != null)
			SkaianetHandler.get(te.getWorld()).closeConnection(te.owner, c.getClientIdentifier(), false);
		else if(te.getData(1).getBoolean("isOpen"))
			SkaianetHandler.get(te.getWorld()).closeConnection(te.owner, null, false);
	}
	
}

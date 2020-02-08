package com.mraof.minestuck.computer;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.network.ClientEditPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.skaianet.ReducedConnection;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.SkaiaClient;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import com.mraof.minestuck.tileentity.ComputerTileEntity;

import java.util.ArrayList;

public class SburbServer extends ButtonListProgram
{
	public static final String NAME = "minestuck.server_program";
	public static final String CLOSE_BUTTON = SburbClient.CLOSE_BUTTON;
	public static final String EDIT_BUTTON = "minestuck.edit_button";
	public static final String GIVE_BUTTON = "minestuck.give_button";
	public static final String OPEN_BUTTON = "minestuck.open_button";
	public static final String RESUME_BUTTON = SburbClient.RESUME_BUTTON;
	public static final String CONNECT = SburbClient.CONNECT;
	public static final String OFFLINE = "minestuck.offline_message";
	public static final String SERVER_ACTIVE = "minestuck.server_active_message";
	public static final String RESUME_SERVER = "minestuck.resume_server_message";
	
	@Override
	public ArrayList<UnlocalizedString> getStringList(ComputerTileEntity te)
	{
		int clientId = te.getData(1).contains("connectedClient")?te.getData(1).getInt("connectedClient"):-1;
		ReducedConnection connection = clientId != -1 ? SkaiaClient.getClientConnection(clientId) : null;
		if(connection != null && connection.getServerId() != te.ownerId)
			connection = null;
		
		ArrayList<UnlocalizedString> list = new ArrayList<>();
		String displayPlayer= connection==null?"UNDEFINED":connection.getClientDisplayName();
		if (connection != null)
		{
			list.add(new UnlocalizedString(CONNECT, displayPlayer));
			list.add(new UnlocalizedString(CLOSE_BUTTON));
			list.add(new UnlocalizedString(MinestuckConfig.giveItems.get() ? GIVE_BUTTON : EDIT_BUTTON));
		} else if (te.getData(getId()).getBoolean("isOpen"))
		{
			list.add(new UnlocalizedString(RESUME_SERVER));
			list.add(new UnlocalizedString(CLOSE_BUTTON));
		} else if(SkaiaClient.isActive(te.ownerId, false))
			list.add(new UnlocalizedString(SERVER_ACTIVE));
		else
		{
			list.add(new UnlocalizedString(OFFLINE));
			if(MinestuckConfig.allowSecondaryConnections.get() || SkaiaClient.getAssociatedPartner(te.ownerId, false) == -1)
				list.add(new UnlocalizedString(OPEN_BUTTON));
			if(SkaiaClient.getAssociatedPartner(te.ownerId, false) != -1)
				list.add(new UnlocalizedString(RESUME_BUTTON));
		}
		return list;
	}
	
	@Override
	public void onButtonPressed(ComputerTileEntity te, String buttonName, Object[] data) {
		switch(buttonName)
		{
			case EDIT_BUTTON:
			case GIVE_BUTTON:
				ClientEditPacket packet = ClientEditPacket.activate(te.ownerId, te.getData(getId()).getInt("connectedClient"));
				MSPacketHandler.sendToServer(packet);
				break;
			case RESUME_BUTTON:
				SkaiaClient.sendConnectRequest(te, SkaiaClient.getAssociatedPartner(te.ownerId, false), false);
				break;
			case OPEN_BUTTON:
				SkaiaClient.sendConnectRequest(te, -1, false);
				break;
			case CLOSE_BUTTON:
				SkaiaClient.sendCloseRequest(te, te.getData(getId()).getBoolean("isOpen") ? -1 : te.getData(getId()).getInt("connectedClient"), false);
				break;
		}
	}
	
	@Override
	public String getName()
	{
		return NAME;
	}
	
	@Override
	public void onClosed(ComputerTileEntity te)
	{
		SburbConnection c = SkaianetHandler.get(te.getWorld()).getServerConnection(te);
		if(c != null)
			SkaianetHandler.get(te.getWorld()).closeConnection(te.owner, c.getClientIdentifier(), false);
		else if(te.getData(1).getBoolean("isOpen"))
			SkaianetHandler.get(te.getWorld()).closeConnection(te.owner, null, false);
	}
	
}

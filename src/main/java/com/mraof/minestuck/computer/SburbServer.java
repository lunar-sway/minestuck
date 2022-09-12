package com.mraof.minestuck.computer;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.network.ClientEditPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.computer.CloseSburbConnectionPacket;
import com.mraof.minestuck.network.computer.OpenSburbServerPacket;
import com.mraof.minestuck.network.computer.ResumeSburbConnectionPacket;
import com.mraof.minestuck.skaianet.client.ReducedConnection;
import com.mraof.minestuck.skaianet.client.SkaiaClient;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;

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
	public ArrayList<UnlocalizedString> getStringList(ComputerBlockEntity be)
	{
		int clientId = be.getData(1).contains("connectedClient")? be.getData(1).getInt("connectedClient"):-1;
		ReducedConnection connection = clientId != -1 ? SkaiaClient.getClientConnection(clientId) : null;
		if(connection != null && connection.getServerId() != be.ownerId)
			connection = null;
		
		ArrayList<UnlocalizedString> list = new ArrayList<>();
		String displayPlayer= connection==null?"UNDEFINED":connection.getClientDisplayName();
		if (connection != null)
		{
			list.add(new UnlocalizedString(CONNECT, displayPlayer));
			list.add(new UnlocalizedString(CLOSE_BUTTON));
			list.add(new UnlocalizedString(MinestuckConfig.SERVER.giveItems.get() ? GIVE_BUTTON : EDIT_BUTTON));
		} else if (be.getData(getId()).getBoolean("isOpen"))
		{
			list.add(new UnlocalizedString(RESUME_SERVER));
			list.add(new UnlocalizedString(CLOSE_BUTTON));
		} else if(SkaiaClient.isActive(be.ownerId, false))
			list.add(new UnlocalizedString(SERVER_ACTIVE));
		else
		{
			list.add(new UnlocalizedString(OFFLINE));
			if(MinestuckConfig.SERVER.allowSecondaryConnections.get() || SkaiaClient.getAssociatedPartner(be.ownerId, false) == -1)
				list.add(new UnlocalizedString(OPEN_BUTTON));
			if(SkaiaClient.getAssociatedPartner(be.ownerId, false) != -1)
				list.add(new UnlocalizedString(RESUME_BUTTON));
		}
		return list;
	}
	
	@Override
	public void onButtonPressed(ComputerBlockEntity be, String buttonName, Object[] data) {
		switch(buttonName)
		{
			case EDIT_BUTTON:
			case GIVE_BUTTON:
				ClientEditPacket packet = ClientEditPacket.activate(be.ownerId, be.getData(getId()).getInt("connectedClient"));
				MSPacketHandler.sendToServer(packet);
				break;
			case RESUME_BUTTON:
				MSPacketHandler.sendToServer(ResumeSburbConnectionPacket.asServer(be));
				break;
			case OPEN_BUTTON:
				MSPacketHandler.sendToServer(OpenSburbServerPacket.create(be));
				break;
			case CLOSE_BUTTON:
				MSPacketHandler.sendToServer(CloseSburbConnectionPacket.asServer(be));
				break;
		}
	}
	
	@Override
	public String getName()
	{
		return NAME;
	}
	
}
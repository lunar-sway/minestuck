package com.mraof.minestuck.computer;

import com.mraof.minestuck.client.gui.ColorSelectorScreen;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.computer.CloseRemoteSburbConnectionPacket;
import com.mraof.minestuck.network.computer.CloseSburbConnectionPacket;
import com.mraof.minestuck.network.computer.ConnectToSburbServerPacket;
import com.mraof.minestuck.network.computer.ResumeSburbConnectionPacket;
import com.mraof.minestuck.skaianet.client.ReducedConnection;
import com.mraof.minestuck.skaianet.client.SkaiaClient;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.Map;

public class SburbClient extends ButtonListProgram
{
	public static final String NAME = "minestuck.program.client.name";
	public static final String CLOSE_BUTTON = "minestuck.program.close_button"; //also used in SburbServer
	public static final String CONNECT_BUTTON = "minestuck.program.client.connect_button";
	public static final String RESUME_BUTTON = "minestuck.program.resume_button"; //also used in SburbServer
	public static final String SELECT_COLOR = "minestuck.program.client.select_color_button";
	public static final String CONNECT = "minestuck.program.connect_message"; //also used in SburbServer
	public static final String CLIENT_ACTIVE = "minestuck.program.client.client_active_message";
	public static final String SELECT = "minestuck.program.client.select_message";
	public static final String RESUME_CLIENT = "minestuck.program.client.resume_client_message";
	
	@Override
	public ArrayList<UnlocalizedString> getStringList(ComputerBlockEntity be)
	{
		ArrayList<UnlocalizedString> list = new ArrayList<>();
		CompoundTag nbt = be.getData(getId());
		
		ReducedConnection c = SkaiaClient.getClientConnection(be.ownerId);
		if(nbt.getBoolean("connectedToServer") && c != null) //If it is connected to someone.
		{
			String displayPlayer = c.getServerDisplayName();
			list.add(new UnlocalizedString(CONNECT, displayPlayer));
			list.add(new UnlocalizedString(CLOSE_BUTTON));
		} else if(nbt.getBoolean("isResuming"))
		{
			list.add(new UnlocalizedString(RESUME_CLIENT));
			list.add(new UnlocalizedString(CLOSE_BUTTON));
		} else if(!SkaiaClient.isActive(be.ownerId, true)) //If the player doesn't have an other active client
		{
			list.add(new UnlocalizedString(SELECT));
			if(SkaiaClient.getAssociatedPartner(be.ownerId, true) != -1) //If it has a resumable connection
				list.add(new UnlocalizedString(RESUME_BUTTON));
			for (Map.Entry<Integer, String> entry : SkaiaClient.getAvailableServers(be.ownerId).entrySet())
				list.add(new UnlocalizedString(CONNECT_BUTTON, entry.getValue(), entry.getKey()));
		} else
		{
			list.add(new UnlocalizedString(CLIENT_ACTIVE));
			list.add(new UnlocalizedString(CLOSE_BUTTON));
		}
		if(SkaiaClient.canSelect(be.ownerId))
			list.add(new UnlocalizedString(SELECT_COLOR));
		return list;
	}
	
	@Override
	public void onButtonPressed(ComputerBlockEntity be, String buttonName, Object[] data)
	{
		if(buttonName.equals(RESUME_BUTTON))
			MSPacketHandler.sendToServer(ResumeSburbConnectionPacket.asClient(be));
		else if(buttonName.equals(CONNECT_BUTTON))
			MSPacketHandler.sendToServer(ConnectToSburbServerPacket.create(be, (Integer) data[1]));
		else if(buttonName.equals(CLOSE_BUTTON))
		{
			CompoundTag nbt = be.getData(getId());
			if(!nbt.getBoolean("isResuming") && !nbt.getBoolean("connectedToServer"))
				MSPacketHandler.sendToServer(CloseRemoteSburbConnectionPacket.asClient(be));
			else MSPacketHandler.sendToServer(CloseSburbConnectionPacket.asClient(be));
		} else if(buttonName.equals(SELECT_COLOR))
			Minecraft.getInstance().setScreen(new ColorSelectorScreen(false));
	}
	
	@Override
	public String getName()
	{
		return NAME;
	}
}
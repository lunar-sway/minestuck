package com.mraof.minestuck.computer;

import com.mraof.minestuck.client.gui.ColorSelectorScreen;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.computer.CloseRemoteSburbConnectionPacket;
import com.mraof.minestuck.network.computer.CloseSburbConnectionPacket;
import com.mraof.minestuck.network.computer.ConnectToSburbServerPacket;
import com.mraof.minestuck.network.computer.ResumeSburbConnectionPacket;
import com.mraof.minestuck.skaianet.client.ReducedConnection;
import com.mraof.minestuck.skaianet.client.SkaiaClient;
import com.mraof.minestuck.tileentity.ComputerTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;

import java.util.ArrayList;
import java.util.Map;

public class SburbClient extends ButtonListProgram
{
	public static final String NAME = "minestuck.client_program";
	public static final String CLOSE_BUTTON = "minestuck.close_button";
	public static final String CONNECT_BUTTON = "minestuck.connect_button";
	public static final String RESUME_BUTTON = "minestuck.resume_button";
	public static final String SELECT_COLOR = "minestuck.select_color_button";
	public static final String CONNECT = "minestuck.connect_message";
	public static final String CLIENT_ACTIVE = "minestuck.client_active_message";
	public static final String SELECT = "minestuck.select_message";
	public static final String RESUME_CLIENT = "minestuck.resume_client_message";
	
	@Override
	public ArrayList<UnlocalizedString> getStringList(ComputerTileEntity te)
	{
		ArrayList<UnlocalizedString> list = new ArrayList<>();
		CompoundNBT nbt = te.getData(getId());
		
		ReducedConnection c = SkaiaClient.getClientConnection(te.ownerId);
		if(nbt.getBoolean("connectedToServer") && c != null) //If it is connected to someone.
		{
			String displayPlayer = c.getServerDisplayName();
			list.add(new UnlocalizedString(CONNECT, displayPlayer));
			list.add(new UnlocalizedString(CLOSE_BUTTON));
		} else if(nbt.getBoolean("isResuming"))
		{
			list.add(new UnlocalizedString(RESUME_CLIENT));
			list.add(new UnlocalizedString(CLOSE_BUTTON));
		} else if(!SkaiaClient.isActive(te.ownerId, true)) //If the player doesn't have an other active client
		{
			list.add(new UnlocalizedString(SELECT));
			if(SkaiaClient.getAssociatedPartner(te.ownerId, true) != -1) //If it has a resumable connection
				list.add(new UnlocalizedString(RESUME_BUTTON));
			for (Map.Entry<Integer, String> entry : SkaiaClient.getAvailableServers(te.ownerId).entrySet())
				list.add(new UnlocalizedString(CONNECT_BUTTON, entry.getValue(), entry.getKey()));
		} else
		{
			list.add(new UnlocalizedString(CLIENT_ACTIVE));
			list.add(new UnlocalizedString(CLOSE_BUTTON));
		}
		if(SkaiaClient.canSelect(te.ownerId))
			list.add(new UnlocalizedString(SELECT_COLOR));
		return list;
	}
	
	@Override
	public void onButtonPressed(ComputerTileEntity te, String buttonName, Object[] data)
	{
		if(buttonName.equals(RESUME_BUTTON))
			MSPacketHandler.sendToServer(ResumeSburbConnectionPacket.asClient(te));
		else if(buttonName.equals(CONNECT_BUTTON))
			MSPacketHandler.sendToServer(ConnectToSburbServerPacket.create(te, (Integer) data[1]));
		else if(buttonName.equals(CLOSE_BUTTON))
		{
			CompoundNBT nbt = te.getData(getId());
			if(!nbt.getBoolean("isResuming") && !nbt.getBoolean("connectedToServer"))
				MSPacketHandler.sendToServer(CloseRemoteSburbConnectionPacket.asClient(te));
			else MSPacketHandler.sendToServer(CloseSburbConnectionPacket.asClient(te));
		} else if(buttonName.equals(SELECT_COLOR))
			Minecraft.getInstance().setScreen(new ColorSelectorScreen(false));
	}
	
	@Override
	public String getName()
	{
		return NAME;
	}
}
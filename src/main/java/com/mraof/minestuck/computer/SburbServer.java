package com.mraof.minestuck.computer;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.network.computer.CloseSburbConnectionPackets;
import com.mraof.minestuck.network.computer.OpenSburbServerPacket;
import com.mraof.minestuck.network.computer.ResumeSburbConnectionPackets;
import com.mraof.minestuck.network.editmode.ClientEditPackets;
import com.mraof.minestuck.skaianet.client.ReducedConnection;
import com.mraof.minestuck.skaianet.client.SkaiaClient;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;

public class SburbServer extends ButtonListProgram
{
	public static final String CLOSE_BUTTON = SburbClient.CLOSE_BUTTON;
	public static final String EDIT_BUTTON = "minestuck.program.server.edit_button";
	public static final String GIVE_BUTTON = "minestuck.program.server.give_button";
	public static final String OPEN_BUTTON = "minestuck.program.server.open_button";
	public static final String RESUME_BUTTON = SburbClient.RESUME_BUTTON;
	public static final String CONNECT = SburbClient.CONNECT;
	public static final String OFFLINE = "minestuck.program.server.offline_message";
	public static final String SERVER_ACTIVE = "minestuck.program.server.server_active_message";
	public static final String RESUME_SERVER = "minestuck.program.server.resume_server_message";
	
	public static final ResourceLocation ICON = new ResourceLocation(Minestuck.MOD_ID, "textures/gui/desktop_icon/sburb_server.png");
	
	@Override
	public ArrayList<UnlocalizedString> getStringList(ComputerBlockEntity be)
	{
		int clientId = be.getData(1).contains("connectedClient") ? be.getData(1).getInt("connectedClient") : -1;
		ReducedConnection connection = clientId != -1 ? SkaiaClient.getClientConnection(clientId) : null;
		if(connection != null && connection.server().id() != be.ownerId)
			connection = null;
		
		ArrayList<UnlocalizedString> list = new ArrayList<>();
		String displayPlayer = connection == null ? "UNDEFINED" : connection.client().name();
		if(connection != null)
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
			if(MinestuckConfig.SERVER.allowSecondaryConnections.get()
					|| !SkaiaClient.hasPrimaryConnectionAsServer(be.ownerId))
				list.add(new UnlocalizedString(OPEN_BUTTON));
			if(SkaiaClient.hasPrimaryConnectionAsServer(be.ownerId))
				list.add(new UnlocalizedString(RESUME_BUTTON));
		}
		return list;
	}
	
	@Override
	public void onButtonPressed(ComputerBlockEntity be, String buttonName, Object[] data)
	{
		switch(buttonName)
		{
			case EDIT_BUTTON, GIVE_BUTTON ->
			{
				CustomPacketPayload packet = new ClientEditPackets.Activate(be.ownerId, be.getData(getId()).getInt("connectedClient"));
				PacketDistributor.SERVER.noArg().send(packet);
			}
			case RESUME_BUTTON -> PacketDistributor.SERVER.noArg().send(ResumeSburbConnectionPackets.asServer(be));
			case OPEN_BUTTON -> PacketDistributor.SERVER.noArg().send(OpenSburbServerPacket.create(be));
			case CLOSE_BUTTON -> PacketDistributor.SERVER.noArg().send(CloseSburbConnectionPackets.asServer(be));
		}
	}
	
	@Override
	public ResourceLocation getIcon()
	{
		return ICON;
	}
}

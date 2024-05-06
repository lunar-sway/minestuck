package com.mraof.minestuck.computer;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.client.gui.ColorSelectorScreen;
import com.mraof.minestuck.network.computer.CloseRemoteSburbConnectionPacket;
import com.mraof.minestuck.network.computer.CloseSburbConnectionPackets;
import com.mraof.minestuck.network.computer.ConnectToSburbServerPacket;
import com.mraof.minestuck.network.computer.ResumeSburbConnectionPackets;
import com.mraof.minestuck.skaianet.client.ReducedConnection;
import com.mraof.minestuck.skaianet.client.SkaiaClient;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SburbClient extends ButtonListProgram
{
	public static final String CLOSE_BUTTON = "minestuck.program.close_button"; //also used in SburbServer
	public static final String CONNECT_BUTTON = "minestuck.program.client.connect_button";
	public static final String RESUME_BUTTON = "minestuck.program.resume_button"; //also used in SburbServer
	public static final String SELECT_COLOR = "minestuck.program.client.select_color_button";
	public static final String CONNECT = "minestuck.program.connect_message"; //also used in SburbServer
	public static final String CLIENT_ACTIVE = "minestuck.program.client.client_active_message";
	public static final String SELECT = "minestuck.program.client.select_message";
	public static final String RESUME_CLIENT = "minestuck.program.client.resume_client_message";
	
	public static final ResourceLocation ICON = ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, "textures/gui/desktop_icon/sburb_client.png");
	
	@Override
	protected InterfaceData getInterfaceData(ComputerBlockEntity be)
	{
		UnlocalizedString message;
		List<ButtonData> list = new ArrayList<>();
		SburbClientData data = be.getSburbClientData();
		
		if(!be.latestmessage.get(this.getId()).isEmpty())
			list.add(new ButtonData(new UnlocalizedString(CLEAR_BUTTON), () -> {}));
		
		ReducedConnection c = SkaiaClient.getClientConnection(be.ownerId);
		if(data.isConnectedToServer() && c != null) //If it is connected to someone.
		{
			String displayPlayer = c.server().name();
			message = new UnlocalizedString(CONNECT, displayPlayer);
			list.add(new ButtonData(new UnlocalizedString(CLOSE_BUTTON), () -> sendCloseConnectionPacket(be)));
		} else if(data.isResuming())
		{
			message = new UnlocalizedString(RESUME_CLIENT);
			list.add(new ButtonData(new UnlocalizedString(CLOSE_BUTTON), () -> sendCloseConnectionPacket(be)));
		} else if(!SkaiaClient.isActive(be.ownerId, true)) //If the player doesn't have an other active client
		{
			message = new UnlocalizedString(SELECT);
			if(SkaiaClient.hasPrimaryConnectionAsClient(be.ownerId))
			{
				list.add(new ButtonData(new UnlocalizedString(RESUME_BUTTON),
						() -> PacketDistributor.sendToServer(ResumeSburbConnectionPackets.asClient(be))));
			}
			for(Map.Entry<Integer, String> entry : SkaiaClient.getAvailableServers(be.ownerId).entrySet())
			{
				list.add(new ButtonData(new UnlocalizedString(CONNECT_BUTTON, entry.getValue()),
						() -> PacketDistributor.sendToServer(ConnectToSburbServerPacket.create(be, entry.getKey()))));
			}
		} else
		{
			message = new UnlocalizedString(CLIENT_ACTIVE);
			list.add(new ButtonData(new UnlocalizedString(CLOSE_BUTTON), () -> sendCloseConnectionPacket(be)));
		}
		if(SkaiaClient.canSelect(be.ownerId))
		{
			list.add(new ButtonData(new UnlocalizedString(SELECT_COLOR),
					() -> Minecraft.getInstance().setScreen(new ColorSelectorScreen(be))));
		}
		
		return new InterfaceData(message, list);
	}
	
	private static void sendCloseConnectionPacket(ComputerBlockEntity computer)
	{
		SburbClientData data = computer.getSburbClientData();
		if(!data.isResuming() && !data.isConnectedToServer())
			PacketDistributor.sendToServer(CloseRemoteSburbConnectionPacket.asClient(computer));
		else
			PacketDistributor.sendToServer(CloseSburbConnectionPackets.asClient(computer));
	}
	
	@Override
	public ResourceLocation getIcon()
	{
		return ICON;
	}
}

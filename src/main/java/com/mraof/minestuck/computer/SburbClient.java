package com.mraof.minestuck.computer;

import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.client.gui.ColorSelectorScreen;
import com.mraof.minestuck.client.gui.ComputerScreen;
import com.mraof.minestuck.network.computer.*;
import com.mraof.minestuck.skaianet.client.ReducedConnection;
import com.mraof.minestuck.skaianet.client.SkaiaClient;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class SburbClient extends ButtonListProgram
{
	public static final String CLOSE_BUTTON = "minestuck.program.close_button"; //also used in SburbServer
	public static final String RESUME_BUTTON = "minestuck.program.resume_button"; //also used in SburbServer
	public static final String SELECT_COLOR = "minestuck.program.client.select_color_button";
	public static final String CONNECT = "minestuck.program.connect_message"; //also used in SburbServer
	public static final String CLIENT_ACTIVE = "minestuck.program.client.client_active_message";
	public static final String SELECT = "minestuck.program.client.select_message";
	public static final String RESUME_CLIENT = "minestuck.program.client.resume_client_message";
	
	@Override
	public void onUpdateGui(ComputerScreen gui)
	{
		ComputerBlockEntity computer = gui.be;
		Component message;
		List<ButtonData> list = new ArrayList<>();
		SburbClientData data = computer.getSburbClientData();
		Optional<String> eventMessage = data.getEventMessage();
		
		if(eventMessage.isPresent())
			list.add(new ButtonData(Component.translatable(CLEAR_BUTTON), () -> sendClearMessagePacketIfRelevant(computer)));
		
		ReducedConnection c = SkaiaClient.getClientConnection(computer.ownerId);
		if(data.isConnectedToServer() && c != null) //If it is connected to someone.
		{
			String displayPlayer = c.server().name();
			message = Component.translatable(CONNECT, displayPlayer);
			list.add(new ButtonData(Component.translatable(CLOSE_BUTTON), () -> {
				sendClearMessagePacketIfRelevant(computer);
				sendCloseConnectionPacket(computer);
			}));
		} else if(data.isResuming())
		{
			message = Component.translatable(RESUME_CLIENT);
			list.add(new ButtonData(Component.translatable(CLOSE_BUTTON), () -> {
				sendClearMessagePacketIfRelevant(computer);
				sendCloseConnectionPacket(computer);
			}));
		} else if(!SkaiaClient.isActive(computer.ownerId, true)) //If the player doesn't have an other active client
		{
			message = Component.translatable(SELECT);
			if(SkaiaClient.hasPrimaryConnectionAsClient(computer.ownerId))
			{
				list.add(new ButtonData(Component.translatable(RESUME_BUTTON), () -> {
					sendClearMessagePacketIfRelevant(computer);
					PacketDistributor.sendToServer(ResumeSburbConnectionPackets.asClient(computer));
				}));
			}
			for(Map.Entry<Integer, String> entry : SkaiaClient.getAvailableServers(computer.ownerId).entrySet())
			{
				list.add(new ButtonData(Component.literal(entry.getValue()), () -> {
					sendClearMessagePacketIfRelevant(computer);
					PacketDistributor.sendToServer(ConnectToSburbServerPacket.create(computer, entry.getKey()));
				}));
			}
		} else
		{
			message = Component.translatable(CLIENT_ACTIVE);
			list.add(new ButtonData(Component.translatable(CLOSE_BUTTON), () -> {
				sendClearMessagePacketIfRelevant(computer);
				sendCloseConnectionPacket(computer);
			}));
		}
		if(SkaiaClient.canSelect(computer.ownerId))
		{
			list.add(new ButtonData(Component.translatable(SELECT_COLOR), () -> {
				sendClearMessagePacketIfRelevant(computer);
				Minecraft.getInstance().setScreen(new ColorSelectorScreen(computer));
			}));
		}
		
		updateMessage(eventMessage.<Component>map(Component::translatable).orElse(message));
		updateButtons(list);
	}
	
	private static void sendClearMessagePacketIfRelevant(ComputerBlockEntity computer)
	{
		if(computer.getSburbServerData().getEventMessage().isPresent())
			PacketDistributor.sendToServer(new ClearMessagePacket(computer.getBlockPos(), 0));
	}
	
	private static void sendCloseConnectionPacket(ComputerBlockEntity computer)
	{
		SburbClientData data = computer.getSburbClientData();
		if(!data.isResuming() && !data.isConnectedToServer())
			PacketDistributor.sendToServer(CloseRemoteSburbConnectionPacket.asClient(computer));
		else
			PacketDistributor.sendToServer(CloseSburbConnectionPackets.asClient(computer));
	}
}

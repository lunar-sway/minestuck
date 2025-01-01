package com.mraof.minestuck.computer;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.client.gui.ComputerScreen;
import com.mraof.minestuck.network.computer.ClearMessagePacket;
import com.mraof.minestuck.network.computer.CloseSburbConnectionPackets;
import com.mraof.minestuck.network.computer.OpenSburbServerPacket;
import com.mraof.minestuck.network.computer.ResumeSburbConnectionPackets;
import com.mraof.minestuck.network.editmode.ClientEditPackets;
import com.mraof.minestuck.skaianet.client.ReducedConnection;
import com.mraof.minestuck.skaianet.client.SkaiaClient;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public final class SburbServer extends ButtonListProgram
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
	
	@Override
	public void onUpdateGui(ComputerScreen gui)
	{
		ComputerBlockEntity computer = gui.be;
		Component message;
		List<ButtonData> list = new ArrayList<>();
		
		SburbServerData data = computer.getSburbServerData();
		Optional<String> eventMessage = data.getEventMessage();
		OptionalInt clientId = data.getConnectedClientId();
		ReducedConnection connection = clientId.isPresent() ? SkaiaClient.getClientConnection(clientId.getAsInt()) : null;
		if(connection != null && connection.server().id() != computer.ownerId)
			connection = null;
		
		if(eventMessage.isPresent())
			list.add(new ButtonData(Component.translatable(CLEAR_BUTTON), () -> sendClearMessagePacketIfRelevant(computer)));
		
		String displayPlayer = connection == null ? "UNDEFINED" : connection.client().name();
		if(connection != null)
		{
			message = Component.translatable(CONNECT, displayPlayer);
			list.add(new ButtonData(Component.translatable(CLOSE_BUTTON), () -> {
				sendClearMessagePacketIfRelevant(computer);
				sendCloseConnectionPacket(computer);
			}));
			list.add(new ButtonData(Component.translatable(MinestuckConfig.SERVER.giveItems.get() ? GIVE_BUTTON : EDIT_BUTTON),
					() -> {
						sendClearMessagePacketIfRelevant(computer);
						sendActivateEditmodePacket(computer);
					}));
		} else if(data.isOpen())
		{
			message = Component.translatable(RESUME_SERVER);
			list.add(new ButtonData(Component.translatable(CLOSE_BUTTON), () -> {
				sendClearMessagePacketIfRelevant(computer);
				sendCloseConnectionPacket(computer);
			}));
		} else if(SkaiaClient.isActive(computer.ownerId, false))
			message = Component.translatable(SERVER_ACTIVE);
		else
		{
			message = Component.translatable(OFFLINE);
			if(MinestuckConfig.SERVER.allowSecondaryConnections.get()
					|| !SkaiaClient.hasPrimaryConnectionAsServer(computer.ownerId))
			{
				list.add(new ButtonData(Component.translatable(OPEN_BUTTON), () -> {
					sendClearMessagePacketIfRelevant(computer);
					PacketDistributor.sendToServer(OpenSburbServerPacket.create(computer));
				}));
			}
			if(SkaiaClient.hasPrimaryConnectionAsServer(computer.ownerId))
			{
				list.add(new ButtonData(Component.translatable(RESUME_BUTTON), () -> {
					sendClearMessagePacketIfRelevant(computer);
					PacketDistributor.sendToServer(ResumeSburbConnectionPackets.asServer(computer));
				}));
			}
		}
		
		updateMessage(eventMessage.<Component>map(Component::translatable).orElse(message));
		updateButtons(list);
	}
	
	private static void sendClearMessagePacketIfRelevant(ComputerBlockEntity computer)
	{
		if(computer.getSburbServerData().getEventMessage().isPresent())
			PacketDistributor.sendToServer(new ClearMessagePacket(computer.getBlockPos(), 1));
	}
	
	private static void sendCloseConnectionPacket(ComputerBlockEntity computer)
	{
		PacketDistributor.sendToServer(CloseSburbConnectionPackets.asServer(computer));
	}
	
	private static void sendActivateEditmodePacket(ComputerBlockEntity computer)
	{
		CustomPacketPayload packet = new ClientEditPackets.Activate(computer.ownerId, computer.getSburbServerData().getConnectedClientId().orElseThrow());
		PacketDistributor.sendToServer(packet);
	}
}

package com.mraof.minestuck.client.gui.computer;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.computer.SburbServerData;
import com.mraof.minestuck.network.computer.ClearMessagePacket;
import com.mraof.minestuck.network.computer.CloseSburbConnectionPackets;
import com.mraof.minestuck.network.computer.OpenSburbServerPacket;
import com.mraof.minestuck.network.computer.ResumeSburbConnectionPackets;
import com.mraof.minestuck.network.editmode.ClientEditPackets;
import com.mraof.minestuck.skaianet.client.ReducedConnection;
import com.mraof.minestuck.skaianet.client.SkaiaClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public final class SburbServerGui implements ProgramGui<SburbServerData>
{
	public static final String NAME = "minestuck.program.sburb_server";
	public static final String CLOSE_BUTTON = SburbClientGui.CLOSE_BUTTON;
	public static final String EDIT_BUTTON = "minestuck.program.server.edit_button";
	public static final String GIVE_BUTTON = "minestuck.program.server.give_button";
	public static final String OPEN_BUTTON = "minestuck.program.server.open_button";
	public static final String RESUME_BUTTON = SburbClientGui.RESUME_BUTTON;
	public static final String CONNECT = SburbClientGui.CONNECT;
	public static final String OFFLINE = "minestuck.program.server.offline_message";
	public static final String SERVER_ACTIVE = "minestuck.program.server.server_active_message";
	public static final String RESUME_SERVER = "minestuck.program.server.resume_server_message";
	
	private final ButtonListHelper buttonListHelper = new ButtonListHelper();
	private Component message;
	
	@Override
	public void onInit(ComputerScreen gui)
	{
		this.buttonListHelper.init(gui);
	}
	
	@Override
	public void onUpdate(ComputerScreen gui, SburbServerData data)
	{
		ComputerBlockEntity computer = gui.computer;
		Component message;
		List<ButtonListHelper.ButtonData> list = new ArrayList<>();
		
		Optional<String> eventMessage = data.getEventMessage();
		OptionalInt clientId = data.getConnectedClientId();
		ReducedConnection connection = clientId.isPresent() ? SkaiaClient.getClientConnection(clientId.getAsInt()) : null;
		if(connection != null && connection.server().id() != computer.clientSideOwnerId())
			connection = null;
		
		if(eventMessage.isPresent())
			list.add(new ButtonListHelper.ButtonData(Component.translatable(ButtonListHelper.CLEAR_BUTTON),
					() -> sendClearMessagePacket(computer)));
		
		String displayPlayer = connection == null ? "UNDEFINED" : connection.client().name();
		if(connection != null)
		{
			message = Component.translatable(CONNECT, displayPlayer);
			list.add(new ButtonListHelper.ButtonData(Component.translatable(CLOSE_BUTTON), () -> {
				if(eventMessage.isPresent())
					sendClearMessagePacket(computer);
				sendCloseConnectionPacket(computer);
			}));
			list.add(new ButtonListHelper.ButtonData(Component.translatable(MinestuckConfig.SERVER.giveItems.get() ? GIVE_BUTTON : EDIT_BUTTON),
					() -> {
						if(eventMessage.isPresent())
							sendClearMessagePacket(computer);
						sendActivateEditmodePacket(computer, clientId.orElseThrow());
					}));
		} else if(data.isOpen())
		{
			message = Component.translatable(RESUME_SERVER);
			list.add(new ButtonListHelper.ButtonData(Component.translatable(CLOSE_BUTTON), () -> {
				if(eventMessage.isPresent())
					sendClearMessagePacket(computer);
				sendCloseConnectionPacket(computer);
			}));
		} else if(SkaiaClient.isActive(computer.clientSideOwnerId(), false))
			message = Component.translatable(SERVER_ACTIVE);
		else
		{
			message = Component.translatable(OFFLINE);
			if(MinestuckConfig.SERVER.allowSecondaryConnections.get()
					|| !SkaiaClient.hasPrimaryConnectionAsServer(computer.clientSideOwnerId()))
			{
				list.add(new ButtonListHelper.ButtonData(Component.translatable(OPEN_BUTTON), () -> {
					if(eventMessage.isPresent())
						sendClearMessagePacket(computer);
					PacketDistributor.sendToServer(OpenSburbServerPacket.create(computer));
				}));
			}
			if(SkaiaClient.hasPrimaryConnectionAsServer(computer.clientSideOwnerId()))
			{
				list.add(new ButtonListHelper.ButtonData(Component.translatable(RESUME_BUTTON), () -> {
					if(eventMessage.isPresent())
						sendClearMessagePacket(computer);
					PacketDistributor.sendToServer(ResumeSburbConnectionPackets.asServer(computer));
				}));
			}
		}
		
		this.message = eventMessage.<Component>map(Component::translatable).orElse(message);
		this.buttonListHelper.updateButtons(list);
	}
	
	private static void sendClearMessagePacket(ComputerBlockEntity computer)
	{
		PacketDistributor.sendToServer(new ClearMessagePacket(computer.getBlockPos(), 1));
	}
	
	private static void sendCloseConnectionPacket(ComputerBlockEntity computer)
	{
		PacketDistributor.sendToServer(CloseSburbConnectionPackets.asServer(computer));
	}
	
	private static void sendActivateEditmodePacket(ComputerBlockEntity computer, int targetClientId)
	{
		CustomPacketPayload packet = new ClientEditPackets.Activate(computer.clientSideOwnerId(), targetClientId);
		PacketDistributor.sendToServer(packet);
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, ComputerScreen gui)
	{
		ProgramGui.drawHeaderMessage(this.message, guiGraphics, gui);
	}
}

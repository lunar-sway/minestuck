package com.mraof.minestuck.client.gui.computer;

import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.client.gui.ColorSelectorScreen;
import com.mraof.minestuck.computer.SburbClientData;
import com.mraof.minestuck.network.computer.*;
import com.mraof.minestuck.skaianet.client.ReducedConnection;
import com.mraof.minestuck.skaianet.client.SkaiaClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class SburbClientGui implements ProgramGui<SburbClientData>
{
	public static final String NAME = "minestuck.program.sburb_client";
	public static final String CLOSE_BUTTON = "minestuck.program.close_button"; //also used in SburbServer
	public static final String RESUME_BUTTON = "minestuck.program.resume_button"; //also used in SburbServer
	public static final String SELECT_COLOR = "minestuck.program.client.select_color_button";
	public static final String CONNECT = "minestuck.program.connect_message"; //also used in SburbServer
	public static final String CLIENT_ACTIVE = "minestuck.program.client.client_active_message";
	public static final String SELECT = "minestuck.program.client.select_message";
	public static final String RESUME_CLIENT = "minestuck.program.client.resume_client_message";
	
	private final ButtonListHelper buttonListHelper = new ButtonListHelper();
	private Component message;
	
	@Override
	public void onInit(ComputerScreen gui)
	{
		this.buttonListHelper.init(gui);
	}
	
	@Override
	public void onUpdate(ComputerScreen gui, SburbClientData data)
	{
		ComputerBlockEntity computer = gui.be;
		Component message;
		List<ButtonListHelper.ButtonData> list = new ArrayList<>();
		Optional<String> eventMessage = data.getEventMessage();
		
		if(eventMessage.isPresent())
			list.add(new ButtonListHelper.ButtonData(Component.translatable(ButtonListHelper.CLEAR_BUTTON),
					() -> sendClearMessagePacket(computer)));
		
		ReducedConnection c = SkaiaClient.getClientConnection(computer.clientSideOwnerId());
		if(data.isConnectedToServer() && c != null) //If it is connected to someone.
		{
			String displayPlayer = c.server().name();
			message = Component.translatable(CONNECT, displayPlayer);
			list.add(new ButtonListHelper.ButtonData(Component.translatable(CLOSE_BUTTON), () -> {
				if(eventMessage.isPresent())
					sendClearMessagePacket(computer);
				sendCloseConnectionPacket(computer, data);
			}));
		} else if(data.isResuming())
		{
			message = Component.translatable(RESUME_CLIENT);
			list.add(new ButtonListHelper.ButtonData(Component.translatable(CLOSE_BUTTON), () -> {
				if(eventMessage.isPresent())
					sendClearMessagePacket(computer);
				sendCloseConnectionPacket(computer, data);
			}));
		} else if(!SkaiaClient.isActive(computer.clientSideOwnerId(), true)) //If the player doesn't have an other active client
		{
			message = Component.translatable(SELECT);
			if(SkaiaClient.hasPrimaryConnectionAsClient(computer.clientSideOwnerId()))
			{
				list.add(new ButtonListHelper.ButtonData(Component.translatable(RESUME_BUTTON), () -> {
					if(eventMessage.isPresent())
						sendClearMessagePacket(computer);
					PacketDistributor.sendToServer(ResumeSburbConnectionPackets.asClient(computer));
				}));
			}
			for(Map.Entry<Integer, String> entry : SkaiaClient.getAvailableServers(computer.clientSideOwnerId()).entrySet())
			{
				list.add(new ButtonListHelper.ButtonData(Component.literal(entry.getValue()), () -> {
					if(eventMessage.isPresent())
						sendClearMessagePacket(computer);
					PacketDistributor.sendToServer(ConnectToSburbServerPacket.create(computer, entry.getKey()));
				}));
			}
		} else
		{
			message = Component.translatable(CLIENT_ACTIVE);
			list.add(new ButtonListHelper.ButtonData(Component.translatable(CLOSE_BUTTON), () -> {
				if(eventMessage.isPresent())
					sendClearMessagePacket(computer);
				sendCloseConnectionPacket(computer, data);
			}));
		}
		if(SkaiaClient.canSelect(computer.clientSideOwnerId()))
		{
			list.add(new ButtonListHelper.ButtonData(Component.translatable(SELECT_COLOR), () -> {
				if(eventMessage.isPresent())
					sendClearMessagePacket(computer);
				Minecraft.getInstance().setScreen(new ColorSelectorScreen(computer));
			}));
		}
		
		this.message = eventMessage.<Component>map(Component::translatable).orElse(message);
		this.buttonListHelper.updateButtons(list);
	}
	
	private static void sendClearMessagePacketIfRelevant(ComputerBlockEntity computer, SburbClientData data)
	{
		if(data.getEventMessage().isPresent())
			sendClearMessagePacket(computer);
	}
	
	private static void sendClearMessagePacket(ComputerBlockEntity computer)
	{
		PacketDistributor.sendToServer(new ClearMessagePacket(computer.getBlockPos(), 0));
	}
	
	private static void sendCloseConnectionPacket(ComputerBlockEntity computer, SburbClientData data)
	{
		if(!data.isResuming() && !data.isConnectedToServer())
			PacketDistributor.sendToServer(CloseRemoteSburbConnectionPacket.asClient(computer));
		else
			PacketDistributor.sendToServer(CloseSburbConnectionPackets.asClient(computer));
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, ComputerScreen gui)
	{
		ProgramGui.drawHeaderMessage(this.message, guiGraphics, gui);
	}
}

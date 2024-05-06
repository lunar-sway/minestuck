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
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

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
	
	public static final ResourceLocation ICON = ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, "textures/gui/desktop_icon/sburb_server.png");
	
	@Override
	protected InterfaceData getInterfaceData(ComputerBlockEntity be)
	{
		OptionalInt clientId = be.getSburbServerData().getConnectedClientId();
		ReducedConnection connection = clientId.isPresent() ? SkaiaClient.getClientConnection(clientId.getAsInt()) : null;
		if(connection != null && connection.server().id() != be.ownerId)
			connection = null;
		
		Component message;
		List<ButtonData> list = new ArrayList<>();
		
		if(!be.latestmessage.get(this.getId()).isEmpty())
			list.add(new ButtonData(Component.translatable(CLEAR_BUTTON), () -> {}));
		
		String displayPlayer = connection == null ? "UNDEFINED" : connection.client().name();
		if(connection != null)
		{
			message = Component.translatable(CONNECT, displayPlayer);
			list.add(new ButtonData(Component.translatable(CLOSE_BUTTON), () -> sendCloseConnectionPacket(be)));
			list.add(new ButtonData(Component.translatable(MinestuckConfig.SERVER.giveItems.get() ? GIVE_BUTTON : EDIT_BUTTON),
					() -> sendActivateEditmodePacket(be)));
		} else if (be.getSburbServerData().isOpen())
		{
			message = Component.translatable(RESUME_SERVER);
			list.add(new ButtonData(Component.translatable(CLOSE_BUTTON), () -> sendCloseConnectionPacket(be)));
		} else if(SkaiaClient.isActive(be.ownerId, false))
			message = Component.translatable(SERVER_ACTIVE);
		else
		{
			message = Component.translatable(OFFLINE);
			if(MinestuckConfig.SERVER.allowSecondaryConnections.get()
					|| !SkaiaClient.hasPrimaryConnectionAsServer(be.ownerId))
			{
				list.add(new ButtonData(Component.translatable(OPEN_BUTTON),
						() -> PacketDistributor.sendToServer(OpenSburbServerPacket.create(be))));
			}
			if(SkaiaClient.hasPrimaryConnectionAsServer(be.ownerId))
			{
				list.add(new ButtonData(Component.translatable(RESUME_BUTTON),
						() -> PacketDistributor.sendToServer(ResumeSburbConnectionPackets.asServer(be))));
			}
		}
		return new InterfaceData(message, list);
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
	
	@Override
	public ResourceLocation getIcon()
	{
		return ICON;
	}
}

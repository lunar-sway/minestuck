package com.mraof.minestuck.util;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.network.packet.Packet250CustomPayload;

import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;

public class SburbConnection {

	private static ArrayList<SburbConnection> serversOpen = new ArrayList<SburbConnection>();
	private static ArrayList<IConnectionListener> listeners = new ArrayList<IConnectionListener>();
	
	private String clientPlayer;
	private String serverPlayer;
	private IConnectionListener listener;
	
	public SburbConnection(String clientPlayer, String serverPlayer) {
		this();
		this.clientPlayer = clientPlayer;
		this.serverPlayer = serverPlayer;
	}
	
	public SburbConnection(String player,boolean isClient) {
		this();
		if (isClient) {
			this.clientPlayer = player;
		} else {
			this.serverPlayer = player;
		}
	}

	public SburbConnection() {
		
	}

	public String getClientPlayer() {
		return clientPlayer;
	}
	
	public String getServerPlayer() {
		return serverPlayer;
	}
	
	private void setClientPlayer(String clientPlayer) {
		this.clientPlayer = clientPlayer;
	}

	private void setServerPlayer(String serverPlayer) {
		this.serverPlayer = serverPlayer;
	}

	public static ArrayList getServersOpen() {
		return serversOpen;
	}
	
	public static SburbConnection addServer(SburbConnection conn) {
		if (Minecraft.getMinecraft().theWorld.isRemote) {
			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "Minestuck";
			packet.data = MinestuckPacket.makePacket(Type.SBURB_OPEN,conn.getServerPlayer());
			packet.length = packet.data.length;
			Minecraft.getMinecraft().getNetHandler().addToSendQueue(packet);
		}
		
		serversOpen.add(conn);
		return conn;
	}
	
	public SburbConnection addListener(IConnectionListener listener) {
		this.listener = listener;
		return this;
	}
	
	public SburbConnection connect(String player) {
		if (Minecraft.getMinecraft().theWorld.isRemote) {
			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "Minestuck";
			packet.data = MinestuckPacket.makePacket(Type.SBURB_CONNECT, this.clientPlayer == null ? player : this.getClientPlayer(),  this.serverPlayer == null ? player : this.getServerPlayer());
			packet.length = packet.data.length;
			Minecraft.getMinecraft().getNetHandler().addToSendQueue(packet);
		}
		
		for (Object conn : serversOpen) {
			if (((SburbConnection)conn).getServerPlayer() == this.getServerPlayer()) {
				serversOpen.remove(conn);
			}
		}
		if (this.clientPlayer == null) {
			this.setClientPlayer(player);
		} else {
			this.setServerPlayer(player);
		}
		if (this.listener != null) {
			this.listener.onConnected(this);
		}
		return this;
	}
	
//	public static SburbConnection connect(String client, String server) {
//		SburbConnection conn = new SburbConnection(client,server);
//		serversOpen.remove(server);
//		for (Object listener : listeners) {
//			((IConnectionListener)listener).onConnected(conn);
//		}
//		return conn;
//	}

}

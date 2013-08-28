package com.mraof.minestuck.util;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.network.packet.Packet250CustomPayload;

import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;

public class SburbConnection {

	private static ArrayList<SburbConnection> serversOpen = new ArrayList<SburbConnection>();
	//private static ArrayList<IConnectionListener> listeners = new ArrayList<IConnectionListener>();
	
	//private String clientPlayer;
	//private String serverPlayer;
	public IConnectionListener listener;
	public String server;
	public String client;
	
//	public SburbConnection(String clientPlayer, String serverPlayer) {
//		this();
//		this.clientPlayer = clientPlayer;
//		this.serverPlayer = serverPlayer;
//	}
//	
//	public SburbConnection(String player,boolean isClient) {
//		this();
//		if (isClient) {
//			this.clientPlayer = player;
//		} else {
//			this.serverPlayer = player;
//		}
//	}

	public SburbConnection(IConnectionListener listener) {
		this.listener = listener;
	}

//	public String getClientPlayer() {
//		return clientPlayer;
//	}
//	
//	public String getServerPlayer() {
//		return serverPlayer;
//	}
//	
//	private void setClientPlayer(String clientPlayer) {
//		this.clientPlayer = clientPlayer;
//	}
//
//	private void setServerPlayer(String serverPlayer) {
//		this.serverPlayer = serverPlayer;
//	}

	public static ArrayList getServersOpen() {
		return serversOpen;
	}
	
	public SburbConnection openServer(String player) {
//		if (Minecraft.getMinecraft().theWorld.isRemote) {
//			Packet250CustomPayload packet = new Packet250CustomPayload();
//			packet.channel = "Minestuck";
//			packet.data = MinestuckPacket.makePacket(Type.SBURB_OPEN,conn.getServerPlayer());
//			packet.length = packet.data.length;
//			Minecraft.getMinecraft().getNetHandler().addToSendQueue(packet);
//		}
		
		this.server = player;
		serversOpen.add(this);
		return this;
	}
	
//	public SburbConnection addListener(IConnectionListener listener) {
//		this.listener = listener;
//		return this;
//	}
	
	public SburbConnection connect(String client) {
//		// NOTE: isRemote doesn't quite do it like that serverside
//		if (Minecraft.getMinecraft().theWorld.isRemote) {
//			Packet250CustomPayload packet = new Packet250CustomPayload();
//			packet.channel = "Minestuck";
//			packet.data = MinestuckPacket.makePacket(Type.SBURB_CONNECT, this.clientPlayer == null ? player : this.getClientPlayer(),  this.serverPlayer == null ? player : this.getServerPlayer());
//			packet.length = packet.data.length;
//			Minecraft.getMinecraft().getNetHandler().addToSendQueue(packet);
//		}
		
		for (Object conn : serversOpen) {
			if (((SburbConnection)conn).server == this.server) {
				serversOpen.remove(conn);
			}
		}

		this.client = client;
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

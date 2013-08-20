package com.mraof.minestuck.util;

public class SburbConnection {

	private String clientPlayer;
	private String serverPlayer;
	
	public SburbConnection(String clientPlayer, String serverPlayer) {
		this.clientPlayer = clientPlayer;
		this.serverPlayer = serverPlayer;
	}
	
	public String getClientPlayer() {
		return clientPlayer;
	}
	public String getServerPlayer() {
		return serverPlayer;
	}

}

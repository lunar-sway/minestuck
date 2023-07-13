package com.mraof.minestuck.alchemy;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mraof.minestuck.network.data.ShareCaptchasPacket;

public class ClientCardCaptchas
{
	//TODO introduction of this class caused a desync in what the client sees when on a dedicated server
	
	private static final BiMap<String, String> REGISTRY_MAP = HashBiMap.create();
	
	public static void receivePacket(ShareCaptchasPacket packet)
	{
		REGISTRY_MAP.clear();
		REGISTRY_MAP.putAll(packet.getCaptchas());
	}
}
package com.mraof.minestuck.alchemy;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mraof.minestuck.network.data.ShareCaptchasPacket;
import net.minecraft.world.item.Item;

public class ClientCardCaptchas
{
	private static final BiMap<Item, String> REGISTRY_MAP = HashBiMap.create();
	
	public static void receivePacket(ShareCaptchasPacket packet)
	{
		REGISTRY_MAP.clear();
		REGISTRY_MAP.putAll(packet.captchas());
	}
	
	/**
	 * Gets the registry name of the item and then returns its captcha or else returns null
	 */
	public static String getCaptcha(Item item)
	{
		return REGISTRY_MAP.getOrDefault(item, null);
	}
	
}
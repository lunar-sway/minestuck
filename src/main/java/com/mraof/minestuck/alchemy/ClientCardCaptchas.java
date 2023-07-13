package com.mraof.minestuck.alchemy;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mraof.minestuck.network.data.ShareCaptchasPacket;
import net.minecraft.world.item.Item;

public class ClientCardCaptchas
{
	private static final BiMap<String, String> REGISTRY_MAP = HashBiMap.create();
	
	public static void receivePacket(ShareCaptchasPacket packet)
	{
		REGISTRY_MAP.clear();
		REGISTRY_MAP.putAll(packet.getCaptchas());
	}
	
	/**
	 * Gets the registry name of the item and then returns its captcha or else returns null
	 */
	public static String getCaptchaFromItem(Item item)
	{
		String registryName = CardCaptchas.getRegistryNameFromItem(item);
		if(registryName != null)
			return ClientCardCaptchas.getCaptcha(registryName);
		else
			return null;
	}
	
	public static String getCaptcha(String registryName)
	{
		return REGISTRY_MAP.getOrDefault(registryName, null);
	}
}
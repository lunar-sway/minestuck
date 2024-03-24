package com.mraof.minestuck.alchemy;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.world.item.Item;


public class PredeterminedCardCaptchas
{
	public static BiMap<Item, String> predefinedCardMap = HashBiMap.create();
	
	public PredeterminedCardCaptchas()
	{
	}
	
	public static void SetData(Item item, String captcha)
	{
		PredeterminedCardCaptchas.predefinedCardMap.put(item, captcha);
	}
}

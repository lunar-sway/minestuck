package com.mraof.minestuck.alchemy;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.level.PistonEvent;


public class PredeterminedCardCaptchas
{
	private static BiMap<Item, String> predefinedCardMap = HashBiMap.create();
	
	public static void SetData(BiMap<Item, String> predefinedCards)
	{
		PredeterminedCardCaptchas.predefinedCardMap = predefinedCards;
	}
	
	public static BiMap<Item, String> GetData()
	{
		return PredeterminedCardCaptchas.predefinedCardMap;
	}
}

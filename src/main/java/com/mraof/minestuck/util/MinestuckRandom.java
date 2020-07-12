package com.mraof.minestuck.util;

import com.mraof.minestuck.player.PlayerIdentifier;

import java.util.HashMap;
import java.util.Random;

/**
 * A singleton wrapper for java.Util.Random
 * By using this instead of the current practice of making a new Random when we need but but don't have one,
 * we can help to prevent some of the RNG issues that clients often see. 
 * @author BenjaminK
 *
 */
public abstract class MinestuckRandom	//TODO This is not good because it carries over between worlds, and is generally not predictable based on world seed. A different solution is needed
{
	private static HashMap<PlayerIdentifier, Random> playerRandMap = new HashMap<PlayerIdentifier, Random>();
	
	public static java.util.Random getPlayerSpecificRandom(PlayerIdentifier p, long worldSeed)
	{
		if(!playerRandMap.containsKey(p))
		{
			playerRandMap.put(p, new java.util.Random(worldSeed ^ p.hashCode()));
		}
		return playerRandMap.get(p);
	}
}

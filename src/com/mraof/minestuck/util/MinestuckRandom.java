package com.mraof.minestuck.util;

import java.util.HashMap;
import java.util.Random;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.IdentifierHandler.PlayerIdentifier;

/**
 * A singleton wrapper for java.Util.Random
 * By using this instead of the current practice of making a new Random when we need but but don't have one,
 * we can help to prevent some of the RNG issues that clients often see. 
 * @author BenjaminK
 *
 */
public abstract class MinestuckRandom	
{
	private static java.util.Random rand;
	private static HashMap<PlayerIdentifier, Random> playerRandMap = new HashMap<PlayerIdentifier, Random>();
	
	public static Random getRandom()
	{
		if(rand==null)
		{
			if(Minestuck.worldSeed!=0L)
			{
				rand = new java.util.Random(Minestuck.worldSeed);
			} else
			{
				rand = new java.util.Random();
			}
		}
		return rand;
	}
	
	public static java.util.Random getPlayerSpecificRandom(PlayerIdentifier p)
	{
		if(!playerRandMap.containsKey(p))
		{
			playerRandMap.put(p, new java.util.Random(Minestuck.worldSeed ^ p.hashCode()));
		}
		return playerRandMap.get(p);
	}
}

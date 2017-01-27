package com.mraof.minestuck.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.annotation.Nonnull;

import java.util.Random;

import com.mraof.minestuck.util.IdentifierHandler.PlayerIdentifier;

import net.minecraft.item.ItemStack;

public class GristHelper {
	private static Random random = new Random();
	
	public static HashMap<GristType, ArrayList<GristType>> secondaryGristMap;

	static
	{
		secondaryGristMap = new HashMap<GristType, ArrayList<GristType>>();
		for(GristType type : GristType.values())
			secondaryGristMap.put(type, new ArrayList<GristType>());
		secondaryGristMap.get(GristType.Amber).add(GristType.Rust);
		secondaryGristMap.get(GristType.Amber).add(GristType.Sulfur);
		secondaryGristMap.get(GristType.Amethyst).add(GristType.Quartz);
		secondaryGristMap.get(GristType.Amethyst).add(GristType.Garnet);
		secondaryGristMap.get(GristType.Caulk).add(GristType.Iodine);
		secondaryGristMap.get(GristType.Caulk).add(GristType.Chalk);
		secondaryGristMap.get(GristType.Chalk).add(GristType.Shale);
		secondaryGristMap.get(GristType.Chalk).add(GristType.Marble);
		secondaryGristMap.get(GristType.Cobalt).add(GristType.Ruby);
		secondaryGristMap.get(GristType.Cobalt).add(GristType.Amethyst);
		secondaryGristMap.get(GristType.Garnet).add(GristType.Ruby);
		secondaryGristMap.get(GristType.Garnet).add(GristType.Gold);
		secondaryGristMap.get(GristType.Iodine).add(GristType.Amber);
		secondaryGristMap.get(GristType.Iodine).add(GristType.Chalk);
		secondaryGristMap.get(GristType.Marble).add(GristType.Caulk);
		secondaryGristMap.get(GristType.Marble).add(GristType.Amethyst);
		secondaryGristMap.get(GristType.Mercury).add(GristType.Cobalt);
		secondaryGristMap.get(GristType.Mercury).add(GristType.Rust);
		secondaryGristMap.get(GristType.Quartz).add(GristType.Marble);
		secondaryGristMap.get(GristType.Quartz).add(GristType.Uranium);
		secondaryGristMap.get(GristType.Ruby).add(GristType.Quartz);
		secondaryGristMap.get(GristType.Ruby).add(GristType.Diamond);
		secondaryGristMap.get(GristType.Rust).add(GristType.Shale);
		secondaryGristMap.get(GristType.Rust).add(GristType.Garnet);
		secondaryGristMap.get(GristType.Shale).add(GristType.Mercury);
		secondaryGristMap.get(GristType.Shale).add(GristType.Tar);
		secondaryGristMap.get(GristType.Sulfur).add(GristType.Iodine);
		secondaryGristMap.get(GristType.Sulfur).add(GristType.Tar);
		secondaryGristMap.get(GristType.Tar).add(GristType.Amber);
		secondaryGristMap.get(GristType.Tar).add(GristType.Cobalt);
		
		secondaryGristMap.get(GristType.Uranium).add(GristType.Diamond);
		secondaryGristMap.get(GristType.Diamond).add(GristType.Gold);
		secondaryGristMap.get(GristType.Gold).add(GristType.Uranium);
	}

	
	/**
	 * Returns a random grist type. Used for creating randomly aligned underlings.
	 */
	public static GristType getPrimaryGrist()
	{
		while (true)
		{
			GristType randGrist = GristType.values()[random.nextInt(GristType.allGrists)];
			if (randGrist.getRarity() > random.nextFloat() && randGrist != GristType.Artifact)
				return randGrist;
		}
	}
	
	/**
	 * Returns a secondary grist type based on primary grist
	 */
	public static GristType getSecondaryGrist(GristType primary)
	{
		if(secondaryGristMap.get(primary).size() != 0 && random.nextInt(secondaryGristMap.get(primary).size() * 2) != 0)
			return secondaryGristMap.get(primary).get(random.nextInt(secondaryGristMap.get(primary).size()));
		else return primary;
	}

	
	/**
	 * Returns a GristSet representing the drops from an underling, given the underling's type and a static loot multiplier.
	 */
	public static GristSet getRandomDrop(GristType primary, double multiplier)
	{
		GristSet set = new GristSet();
		set.addGrist(GristType.Build, (int)(2*multiplier + random.nextDouble()*18*multiplier));
		set.addGrist(primary, (int)(1*multiplier + random.nextDouble()*9*multiplier));
		set.addGrist(getSecondaryGrist(primary), (int)(0.5*multiplier + random.nextDouble()*4*multiplier));
		return set;
		
	}
	
	/**
	 * A shortened statement to obtain a certain grist count.
	 * Uses the encoded version of the username!
	 */
	public static int getGrist(PlayerIdentifier player, GristType type)
	{
		return MinestuckPlayerData.getGristSet(player).getGrist(type);
	}
	
	public static boolean canAfford(PlayerIdentifier player, @Nonnull ItemStack stack, boolean clientSide)
	{
		return canAfford(clientSide ? MinestuckPlayerData.getClientGrist() : MinestuckPlayerData.getGristSet(player), GristRegistry.getGristConversion(stack));
	}
	
	public static boolean canAfford(GristSet base, GristSet cost) {
		if (base == null || cost == null) {return false;}
		Hashtable<Integer, Integer> reqs = cost.getHashtable();
		
		if (reqs != null) {
			Iterator<Entry<Integer, Integer>> it = reqs.entrySet().iterator();
			while (it.hasNext()) {
				Entry<Integer, Integer> pairs = it.next();
				int type = (Integer) pairs.getKey();
				int need = (Integer) pairs.getValue();
				int have = base.getGrist(GristType.values()[type]);
				
				if (need > have) return false;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Uses the encoded version of the username!
	 */
	public static void decrease(PlayerIdentifier player, GristSet set)
	{
		Hashtable<Integer, Integer> reqs = set.getHashtable();
		if (reqs != null) {
			Iterator<Entry<Integer, Integer>> it = reqs.entrySet().iterator();
			while (it.hasNext()) {
				Entry<Integer, Integer> pairs = it.next();
				setGrist(player, GristType.values()[(Integer) pairs.getKey()], getGrist(player, GristType.values()[(Integer)pairs.getKey()]) - (Integer)pairs.getValue());
			}
		}
	}
	
	public static void setGrist(PlayerIdentifier player, GristType type, int i)
	{
		MinestuckPlayerData.getGristSet(player).setGrist(type, i);
	}
	
	/**
	 * This method will probably be used somewhere in the future.
	 */
	public static int getGristValue(GristSet set) {
		int i = 0;
		for(GristType type : GristType.values()) {
			if(type.equals(GristType.Build))
				i += set.getGrist(type);
			else if(type.getRarity() == 0.0F)
				i += set.getGrist(type)*15;
			else i += set.getGrist(type)*type.getPower();
		}
		return i;
	}
	
	public static void increase(PlayerIdentifier player, GristSet set)
	{
		Hashtable<Integer, Integer> reqs = set.getHashtable();
		if (reqs != null)
		{
			Iterator<Entry<Integer, Integer>> it = reqs.entrySet().iterator();
			while (it.hasNext())
			{
				Entry<Integer, Integer> pairs = it.next();
				setGrist(player, GristType.values()[(Integer) pairs.getKey()], getGrist(player, GristType.values()[(Integer)pairs.getKey()]) + (Integer)pairs.getValue());
			}
		}
	}
	
}

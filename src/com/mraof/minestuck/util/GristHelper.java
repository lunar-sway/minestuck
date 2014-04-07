package com.mraof.minestuck.util;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.item.ItemStack;

public class GristHelper {
	private static Random random = new Random();
	
	/**
	 * Returns a random grist type. Used for creating randomly aligned underlings.
	 */
	public static GristType getPrimaryGrist() {
		while (true) {
			//Debug.print("Index is " + index);
			GristType randGrist = GristType.values()[random.nextInt(GristType.allGrists)];
			if (randGrist.getRarity() > random.nextFloat()) {
				return randGrist;
			}
		}
	}
	
	/**
	 * Returns a GristSet representing the drops from an underling, given the underling's type and a static loot multiplier.
	 */
	public static GristSet getRandomDrop(GristType primary,int multiplier) {
		GristSet set = new GristSet();
		set.addGrist(GristType.Build,random.nextInt(20)*multiplier);
		set.addGrist(primary,random.nextInt(10)*multiplier);
		set.addGrist(getPrimaryGrist(),random.nextInt(5)*multiplier);
		return set;
		
	}
	
	/**
	 * A shortened statement to obtain a certain grist count.
	 * Uses the encoded version of the username!
	 */
	public static int getGrist(String player, GristType type) {
		return MinestuckPlayerData.getGristSet(player).getGrist(type);
	}
	
	/**
	 * Uses the encoded version of the username!
	 */
	public static boolean canAfford(String player, ItemStack stack) {
		return canAfford(MinestuckPlayerData.getGristSet(player), GristRegistry.getGristConversion(stack));
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
	public static void decrease(String player, GristSet set) {
		Hashtable<Integer, Integer> reqs = set.getHashtable();
		if (reqs != null) {
			Iterator<Entry<Integer, Integer>> it = reqs.entrySet().iterator();
			while (it.hasNext()) {
				Entry<Integer, Integer> pairs = it.next();
				setGrist(player, GristType.values()[(Integer) pairs.getKey()], getGrist(player, GristType.values()[(Integer)pairs.getKey()]) - (Integer)pairs.getValue());
			}
		}
	}
	
	public static void setGrist(String player, GristType type, int i) {
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
	
	public static void increase(String username, GristSet set) {
		Hashtable<Integer, Integer> reqs = set.getHashtable();
		if (reqs != null) {
			Iterator<Entry<Integer, Integer>> it = reqs.entrySet().iterator();
			while (it.hasNext()) {
				Entry<Integer, Integer> pairs = it.next();
				setGrist(username, GristType.values()[(Integer) pairs.getKey()], getGrist(username, GristType.values()[(Integer)pairs.getKey()]) + (Integer)pairs.getValue());
			}
		}
	}
	
}

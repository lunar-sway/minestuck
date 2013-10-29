package com.mraof.minestuck.grist;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;


import net.minecraft.entity.player.EntityPlayer;
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
	 * A shortned statement to obtain a certain grist count.
	 */
	public static int getGrist(String player, GristType type) {
		return GristStorage.getGristSet(player).getGrist(type);
	}
	
	public static boolean canAfford(String player, ItemStack stack) {
		return canAfford(GristStorage.getGristSet(player), GristRegistry.getGristConversion(stack));
	}
	
	public static boolean canAfford(GristSet base, GristSet cost) {
		if (base == null || cost == null) {return false;}
		Hashtable reqs = cost.getHashtable();
		
		if (reqs != null) {
			Iterator it = reqs.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pairs = (Map.Entry)it.next();
				int type = (Integer) pairs.getKey();
				int need = (Integer) pairs.getValue();
				int have = base.getGrist(GristType.values()[type]);
				
				if (need > have) return false;
			}
			return true;
		}
		return false;
	}
	
	public static void decrease(String player, GristSet set) {
		Hashtable reqs = set.getHashtable();
		if (reqs != null) {
			Iterator it = reqs.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pairs = (Map.Entry)it.next();
				setGrist(player, GristType.values()[(Integer) pairs.getKey()], getGrist(player, GristType.values()[(Integer)pairs.getKey()]) - (Integer)pairs.getValue());
			}
		}
	}
	
	public static void setGrist(String player, GristType type, int i) {
		GristStorage.getGristSet(player).setGrist(type, i);
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
	
}

package com.mraof.minestuck.util;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

import com.mraof.minestuck.entity.item.EntityGrist;

public class GristSet {
	
	private int[] gristTypes ;
	
	/**
	 * Creates a blank set of grist values, used in setting up the Grist Registry.
	 */
	public GristSet() {
		this.gristTypes = new int[GristType.allGrists];
	}
	
	/**
	 * Creates a set of grist values with one grist/amount pair. used in setting up the Grist Registry.
	 */
	public GristSet(GristType type,int amount)
	{
		this();
		this.gristTypes[type.ordinal()] = amount;
	}
	
	/**
	 * Creates a set of grist values with multiple grist/amount pairs. used in setting up the Grist Registry.
	 */
	public GristSet(GristType[] type,int[] amount)
	{
		this();
		
		for (int i = 0; i < type.length; i++) {
			this.gristTypes[type[i].ordinal()] = amount[i];
		}
	}
	
	/**
	 * Gets the amount of grist, given a type of grist.
	 */
	public int getGrist(GristType type) {
		return this.gristTypes[type.ordinal()];
	}
	
	/**
	 * Sets the amount of grist, given a type of grist and the new amount.
	 */
	public GristSet setGrist(GristType type, int amount) {
		this.gristTypes[type.ordinal()] = amount;
		return this;
	}
	
	/**
	 * Adds an amount of grist to a GristSet, given a grist type and amount.
	 */
	public GristSet addGrist(GristType type, int amount) {
		this.gristTypes[type.ordinal()] += amount;
		return this;
	}
	
	/**
	 * Returns a Hashtable with grist->amount pairs.
	 */
	public Hashtable getHashtable() {
		Hashtable hs = new Hashtable();
		for(int i = 0; i < GristType.allGrists; i++) {
			if (gristTypes[i] != 0) {
				hs.put(i,gristTypes[i]);
			}
		}
		return hs;
	}
	
	public ArrayList getArray() {
		ArrayList list = new ArrayList();
		for(int i = 0; i < GristType.allGrists; i++) {
			if (gristTypes[i] != 0) {
				System.out.println("[MINESTUCK] Added "+gristTypes[i]+" of "+GristType.values()[i].getName());
				list.add(new GristAmount(GristType.values()[i],gristTypes[i]));
			}
		}
		return list;
	}
}

package com.mraof.minestuck.alchemy;

import com.mraof.minestuck.entity.item.EntityGrist;

public class GristSet {
	
	public static final int grists = EntityGrist.gristTypes.length;
	private int[] gristTypes ;
	private int[] amounts;
	
	/*
	 * This creates a blank set of grist values, used in setting up the Grist Registry.
	 */
	public GristSet() {
		this.gristTypes = new int[grists];
		this.amounts = new int[grists];
	}
	
	/*
	 * This creates a set of grist values with one grist/amount pair. used in setting up the Grist Registry.
	 */
	public GristSet(int type,int amount)
	{
		this.gristTypes = new int[grists];
		this.amounts = new int[grists];
		
		this.gristTypes[type] = amount;
	}
	
	/*
	 * This creates a set of grist values with multiple grist/amount pairs. used in setting up the Grist Registry.
	 */
	public GristSet(int[] type,int[] amount)
	{
		this.gristTypes = new int[grists];
		this.amounts = new int[grists];
		
		for (int i = 0; i < type.length; i++) {
			this.gristTypes[type[i]] = amount[i];
		}
	}
	
	/*
	 * Gets the amount of grist, given a type of grist.
	 */
	public int getGrist(int type) {
		return this.gristTypes[type];
	}
	
	/*
	 * Sets the amount of grist, given a type of grist and the new amount.
	 */
	public GristSet setGrist(int type, int amount) {
		this.gristTypes[type] = amount;
		return this;
	}
}

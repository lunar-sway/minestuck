package com.mraof.minestuck.util;


import java.util.ArrayList;
import java.util.Hashtable;


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
	public Hashtable<Integer, Integer> getHashtable() {
		Hashtable<Integer, Integer> hs = new Hashtable<Integer, Integer>();
		for(int i = 0; i < GristType.allGrists; i++) {
			if (gristTypes[i] != 0) {
				hs.put(i,gristTypes[i]);
			}
		}
		return hs;
	}
	
	/**
	 * Returns a ArrayList containing GristAmount objects representing the set.
	 * 
	 */
	public ArrayList<GristAmount> getArray() {
		ArrayList<GristAmount> list = new ArrayList<GristAmount>();
		for(int i = 0; i < GristType.allGrists; i++) {
			if (gristTypes[i] != 0) {
				//Debug.print("Added "+gristTypes[i]+" of "+GristType.values()[i].getName());
				list.add(new GristAmount(GristType.values()[i],gristTypes[i]));
			}
		}
		//Debug.print("		Adding "+list.size()+" grist values...");
		return list;
	}

	/**
	 * Adds an amount of grist to a GristSet, given another set of grist.
	 */
	public GristSet addGrist(GristSet set) {
		
		for (Object grist : set.getArray()) {
			this.addGrist((GristAmount) grist);
		}
		return this;
		
	}

	/**
	 * Adds an amount of grist to a GristSet, given a grist type and amount.
	 */
	public GristSet addGrist(GristAmount grist) {
		//Debug.print("		Adding "+grist.getAmount()+" of "+grist.getType().getName());
		this.gristTypes[grist.getType().ordinal()] += grist.getAmount();
		return this;
	}
	
	/**
	 * Multiplies all the grist amounts by a factor.
	 */
	public GristSet scaleGrist(float scale) {
		
		for (int i = 0;i < GristType.allGrists;i++) {
			if (gristTypes[i] != 0) {
				gristTypes[i] = (int) Math.max(gristTypes[i] *scale,1);
			}
		}
		
		return this;
	}
	
	/**
	 * Checks if this grist set is empty.
	 */
	public boolean isEmpty() {
		for(int i : gristTypes)
			if(i != 0)
				return false;
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder build = new StringBuilder();
		build.append("gristSet:[");
		
		for(int i = 0; i < gristTypes.length; i++)
			if(gristTypes[i] != 0)
				build.append(GristType.values()[i].name()+"="+gristTypes[i]+",");
		
		build.append(']');
		return build.toString();
	}
	
}

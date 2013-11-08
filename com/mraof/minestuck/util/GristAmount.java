package com.mraof.minestuck.util;


public class GristAmount {
	private GristType type;
	private int amount;

	public GristAmount(GristType type, int amount) {
		this.type = type;
		this.amount = amount;
	}
	
	public GristType getType() {
		return type;
	}

	public int getAmount() {
		return amount;
	}
}

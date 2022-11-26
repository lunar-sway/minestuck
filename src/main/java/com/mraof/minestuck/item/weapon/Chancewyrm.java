package com.mraof.minestuck.item.weapon;

public class Chancewyrm extends WeaponItem implements ICaegerMaker, ICaegerUser
{
	public Chancewyrm(WeaponItem.Builder add, Properties rarity)
	{
		super(add, rarity);
	}
	
	@Override
	public int getDiceValue()
	{
		return 13;
	}
	
	@Override
	public int getCaegerDiceValue()
	{
		return 8;
	}
}

package com.mraof.minestuck.item.weapon;

public class ChancewyrmItem extends WeaponItem implements ICaegerMaker, ICaegerUser
{
	public ChancewyrmItem(WeaponItem.Builder add, Properties rarity)
	{
		super(add, rarity);
	}
	@Override
	public int getCaegerDiceValue()
	{
		return 8;
	}
}

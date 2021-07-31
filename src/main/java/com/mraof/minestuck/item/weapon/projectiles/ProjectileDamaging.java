package com.mraof.minestuck.item.weapon.projectiles;

import net.minecraft.item.Item;

public interface ProjectileDamaging
{
	int getProjectileDamage();
	static int getDamageFromItem(Item item)
	{
		return item instanceof ProjectileDamaging ? ((ProjectileDamaging) item).getProjectileDamage() : 0;
	}
}

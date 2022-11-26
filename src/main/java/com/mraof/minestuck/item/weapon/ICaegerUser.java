package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.player.PlayerData;

public interface ICaegerUser
{
		default int reductCaeger(PlayerData player, int amount)
		{
			player.takeCaegers(amount);
			return amount;
		}
	
}


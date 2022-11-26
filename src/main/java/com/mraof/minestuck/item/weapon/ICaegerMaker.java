package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.player.PlayerData;

public interface ICaegerMaker
{
		default int getCaegerDiceGroupCount()
		{
			return 1;
		}
		
		default int getCaegerDiceValue()
		{
			return 1;
		}
		
		default int makeCaeger(PlayerData player)
		{
			int diceRoll = 0;
			for(int dice = getCaegerDiceGroupCount(); dice > 0; dice = dice - 1)
			{
				diceRoll += Math.random() * getCaegerDiceValue() + 1;
			}
			player.addCaegers(diceRoll);
			return diceRoll;
		}
	
}


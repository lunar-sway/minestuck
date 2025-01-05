package com.mraof.minestuck.player;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.event.AlchemyEvent;
import com.mraof.minestuck.item.artifact.CruxiteArtifactItem;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

/**
 * <p>
 * This enum list the bonuses to the echeladder applied to the player when performing specific actions.
 * </p>
 * <p>
 * the monster named values refer to how much bonus xp the monster will give you the first time you kill it.
 * the alchemy ones refer to the first time you alchemize something of a certain value.
 * </p>
 */
@EventBusSubscriber(modid = Minestuck.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public enum EcheladderBonusType
{
	IMP(10),
	OGRE(120),
	BASILISK(450),
	LICH(1200),
	GICLOPS(2500),
	ALCHEMY_1(30), //cost value : 0
	ALCHEMY_2(400), //cost value : 50
	ALCHEMY_3(3000); //cost value : 500
	private final int bonus;
	
	EcheladderBonusType(int bonus)
	{
		this.bonus = bonus;
	}
	
	public int getBonus()
	{
		return bonus;
	}
	
	@Override
	public String toString()
	{
		return this.name().toLowerCase();
	}
	
	public static EcheladderBonusType fromString(String string)
	{
		for(EcheladderBonusType c : values())
		{
			if(c.toString().equalsIgnoreCase(string))
				return c;
		}
		return null;
	}
	
	@SubscribeEvent
	private static void onAlchemizedItem(AlchemyEvent event)
	{
		Echeladder e = Echeladder.get(event.getPlayer(), event.getLevel());
		
		if(!(event.getItemResult().getItem() instanceof CruxiteArtifactItem))
		{
			e.checkBonus(ALCHEMY_1);
			GristSet cost = event.getCost();
			double value = cost.getValue();
			if(value >= 50)
				e.checkBonus(ALCHEMY_2);
			if(value >= 500)
				e.checkBonus(ALCHEMY_3);
		}
	}
}

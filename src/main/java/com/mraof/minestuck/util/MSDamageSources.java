package com.mraof.minestuck.util;

import net.minecraft.util.DamageSource;

public class MSDamageSources
{
	public static final DamageSource SPIKE = new DamageSource("minestuck.spike").setScalesWithDifficulty();
	public static final DamageSource DECAPITATION = new DamageSource("minestuck.decapitation").bypassInvul().bypassArmor().bypassMagic();
	
}

package com.mraof.minestuck.entity.animation;

/**
 * Used to help standardize the method in which action cooldown(how long between uses of an animated action) values are maintained
 */
public interface ActionCooldown
{
	int getCooldown();
	
	void setCooldown(int amountTicks);
	
	default boolean existingCooldownIsLonger(int newCooldown)
	{
		return this.getCooldown() > newCooldown;
	}
	
	default boolean hasFinishedCooldown()
	{
		return this.getCooldown() == 0;
	}
}
package com.mraof.minestuck.entity.animation;

import net.minecraft.nbt.CompoundTag;

/**
 * Keeps track of a given animation. Can store stages of the animation in which to perform additional actions/events
 */
public class MSMobAnimations
{
	public static final int LOOPING_ANIMATION = -1; //when set to -1, it will continue looping until another animation overrides it
	public static final MSMobAnimations.Actions IDLE_ACTION = Actions.IDLE;
	public static final MSMobAnimations DEFAULT_IDLE_ANIMATION = new MSMobAnimations(IDLE_ACTION, LOOPING_ANIMATION, false);
	
	private final Actions actions;
	private final int animationLength;
	private final boolean stopsMovement;
	
	public MSMobAnimations(Actions actions, int animationLength, boolean stopsMovement)
	{
		this.actions = actions;
		this.animationLength = animationLength;
		this.stopsMovement = stopsMovement;
	}
	
	public Actions getAction()
	{
		return actions;
	}
	
	public int getAnimationLength()
	{
		return animationLength;
	}
	
	public boolean isLoopingAnimation()
	{
		return animationLength == LOOPING_ANIMATION;
	}
	
	public boolean stopsMovement()
	{
		return stopsMovement;
	}
	
	public CompoundTag getCompoundTag()
	{
		CompoundTag nbt = new CompoundTag();
		nbt.putInt("actions", actions.ordinal());
		nbt.putInt("animationLength", animationLength);
		nbt.putBoolean("stopsMovement", stopsMovement);
		return nbt;
	}
	
	public static MSMobAnimations createTrackerFromCompoundTag(CompoundTag nbtIn)
	{
		Actions actions = Actions.fromInt(nbtIn.getInt("actions"));
		int animationLength = nbtIn.getInt("animationLength");
		boolean stopsMovement = nbtIn.getBoolean("stopsMovement");
		return new MSMobAnimations(actions, animationLength, stopsMovement);
	}
	
	/**
	 * Animations that at least one mob uses. The only kind of animation that is likely to occur at the same time as one of these is walking
	 */
	public enum Actions
	{
		//passive/neutral actions
		IDLE(false),
		TALK(false),
		PANIC(false),
		
		//aggressive actions
		PUNCH(true),
		CLAW(true),
		BITE(true),
		SLAM(true),
		SHOOT(true), //projectile
		SHOVE(true),
		;
		
		private final boolean isAttack; //whether this action represents an attack
		
		Actions(boolean isAttack)
		{
			this.isAttack = isAttack;
		}
		
		/**
		 * Converts int back into enum
		 */
		public static Actions fromInt(int ordinal)
		{
			if(0 <= ordinal && ordinal < Actions.values().length)
				return Actions.values()[ordinal];
			else
				throw new IllegalArgumentException("Invalid ordinal of " + ordinal + " for actions!");
		}
		
		public boolean isAttack()
		{
			return isAttack;
		}
	}
}
package com.mraof.minestuck.entity.animation;

import net.minecraft.nbt.CompoundTag;

/**
 * Keeps track of a given animation. Can store stages of the animation in which to perform additional actions/events
 */
public class MobAnimation
{
	public static final int LOOPING_ANIMATION = -1; //when set to -1, it will continue looping until another animation overrides it
	public static final MobAnimation.Actions IDLE_ACTION = Actions.IDLE;
	public static final MobAnimation DEFAULT_IDLE_ANIMATION = new MobAnimation(IDLE_ACTION, LOOPING_ANIMATION, false, false);
	
	private final Actions actions;
	private final int animationLength;
	private final boolean freezeMovement;
	private final boolean freezeSight;
	
	/**
	 * @param actions category of the animation
	 * @param animationLength last frame of the animation
	 * @param freezeMovement whether the entity can move voluntarily during an animation, handled both in Goals and is updated upon the change of animation in AnimatedPathfinderMob
	 * @param freezeSight whether the entity has their facing direction locked, handled by Goals
	 */
	public MobAnimation(Actions actions, int animationLength, boolean freezeMovement, boolean freezeSight)
	{
		this.actions = actions;
		this.animationLength = animationLength;
		this.freezeMovement = freezeMovement;
		this.freezeSight = freezeSight;
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
	
	public boolean freezesMovement()
	{
		return freezeMovement;
	}
	
	public boolean freezesSight()
	{
		return freezeSight;
	}
	
	public CompoundTag getCompoundTag()
	{
		CompoundTag nbt = new CompoundTag();
		nbt.putInt("actions", actions.ordinal());
		nbt.putInt("animationLength", animationLength);
		nbt.putBoolean("freezeMovement", freezeMovement);
		nbt.putBoolean("freezeSight", freezeSight);
		return nbt;
	}
	
	public static MobAnimation createTrackerFromCompoundTag(CompoundTag nbtIn)
	{
		Actions actions = Actions.fromInt(nbtIn.getInt("actions"));
		int animationLength = nbtIn.getInt("animationLength");
		boolean freezeMovement = nbtIn.getBoolean("freezeMovement");
		boolean freezeSight = nbtIn.getBoolean("freezeSight");
		return new MobAnimation(actions, animationLength, freezeMovement, freezeSight);
	}
	
	/**
	 * Animations that at least one mob uses. The only kind of animation that is likely to occur at the same time as one of these is walking
	 */
	public enum Actions
	{
		//passive/neutral actions
		IDLE,
		TALK,
		PANIC,
		
		//aggressive actions
		PUNCH,
		CLAW,
		BITE,
		MELEE, //weapon
		SLAM,
		SHOOT, //projectile
		SHOVE;
		
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
	}
}
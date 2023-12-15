package com.mraof.minestuck.fluid;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidType;

public class MSFluidType extends FluidType
{
	private final FLUID_STYLE fluidStyle;
	
	public enum FLUID_STYLE
	{
		VISCOUS(0.65D, 0.8D, 0.1F),
		PARTIALLY_VISCOUS(0.8D, 0.8D, 0.5F),
		RUNNY(0.9D, 0.9D, 0.8F);
		
		public final double xzMovement;
		public final double yMovement;
		public final float soundPitch;
		
		FLUID_STYLE(double xzMovement, double yMovement, float soundPitch)
		{
			this.xzMovement = xzMovement;
			this.yMovement = yMovement;
			this.soundPitch = soundPitch;
		}
	}
	
	public MSFluidType(Properties properties, FLUID_STYLE fluidStyle)
	{
		super(properties);
		this.fluidStyle = fluidStyle;
	}
	
	@Override
	public boolean move(FluidState state, LivingEntity entity, Vec3 movementVector, double gravity)
	{
		return handleMovement(this, entity, movementVector, gravity);
	}
	
	protected static boolean handleMovement(MSFluidType fluidType, LivingEntity entity, Vec3 movementVector, double gravity)
	{
		//modified version of code in {https://github.com/RCXcrafter/EmbersRekindled/blob/rekindled/src/main/java/com/rekindled/embers/fluidtypes/ViscousFluidType.java}
		//which is a modified version of code in LivingEntity travel()
		FLUID_STYLE fluidStyle = fluidType.getFluidStyle();
		gravity = gravity * fluidStyle.yMovement;
		double initialY = entity.getY();
		boolean isSinking = entity.getDeltaMovement().y <= 0.0D && !passesMovementThreshold(entity);
		
		entity.moveRelative(0.02F, movementVector);
		entity.move(MoverType.SELF, entity.getDeltaMovement());
		if(entity.getFluidTypeHeight(fluidType) <= entity.getFluidJumpThreshold())
		{
			//barely submerged
			entity.setDeltaMovement(entity.getDeltaMovement().multiply(fluidStyle.xzMovement, fluidStyle.yMovement, fluidStyle.xzMovement));
			Vec3 fallAdjustedMoveVec = entity.getFluidFallingAdjustedMovement(gravity / 8.0D, isSinking, entity.getDeltaMovement());
			entity.setDeltaMovement(fallAdjustedMoveVec);
			if(!entity.isSwimming() && fluidStyle != FLUID_STYLE.VISCOUS)
				splashSounds(entity, fluidStyle);
		} else
		{
			//partially or fully submerged
			entity.setDeltaMovement(entity.getDeltaMovement().scale(fluidStyle.xzMovement));
		}
		
		if(!entity.isNoGravity())
		{
			entity.setDeltaMovement(entity.getDeltaMovement().add(0.0D, -gravity / 8.0D, 0.0D));
		}
		
		Vec3 moveVec = entity.getDeltaMovement();
		if(entity.horizontalCollision && entity.isFree(moveVec.x, moveVec.y + 0.6D - entity.getY() + initialY, moveVec.z))
		{
			entity.setDeltaMovement(moveVec.x, moveVec.y, moveVec.z);
		}
		
		swimSounds(entity, fluidStyle);
		
		return true;
	}
	
	private static void splashSounds(LivingEntity entity, FLUID_STYLE movementType)
	{
		SoundEvent splashSound = entity instanceof Player ? SoundEvents.PLAYER_SPLASH : SoundEvents.GENERIC_SPLASH;
		if(passesMovementThreshold(entity) && entity.yOld > entity.getY())
		{
			entity.playSound(splashSound, 0.25F, movementType.soundPitch);
		}
	}
	
	static boolean passesMovementThreshold(LivingEntity entity)
	{
		Vec3 oldPosVec = new Vec3(entity.xOld, entity.yOld, entity.zOld);
		Vec3 newPosVec = new Vec3(entity.getX(), entity.getY(), entity.getZ());
		return oldPosVec.distanceTo(newPosVec) >= 0.05D;
	}
	
	private static void swimSounds(LivingEntity entity, FLUID_STYLE movementType)
	{
		SoundEvent swimSound = entity instanceof Player ? SoundEvents.PLAYER_SWIM : SoundEvents.GENERIC_SWIM;
		if(passesMovementThreshold(entity))
		{
			//a sound has a 35% chance of playing every half second
			boolean goodSoundFrequency = entity.getRandom().nextFloat() <= 0.35 && entity.level().getGameTime() % 10 == 0;
			if(goodSoundFrequency)
				entity.playSound(swimSound, 0.1F, movementType.soundPitch);
		}
	}
	
	public FLUID_STYLE getFluidStyle()
	{
		return fluidStyle;
	}
}
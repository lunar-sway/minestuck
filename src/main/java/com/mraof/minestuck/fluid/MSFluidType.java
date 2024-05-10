package com.mraof.minestuck.fluid;

import com.mraof.minestuck.util.MSAttachments;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class MSFluidType extends FluidType
{
	private final Style fluidStyle;
	
	public record Style(double xzMovement, double yMovement, float soundPitch, boolean hasSplashSound, SoundEvent ambientSound)
	{
		public static final Style VISCOUS = new Style(0.65D, 0.8D, 0.1F, false, SoundEvents.LAVA_AMBIENT);
		public static final Style PARTIALLY_VISCOUS = new Style(0.8D, 0.8D, 0.5F, true, SoundEvents.WATER_AMBIENT);
		public static final Style RUNNY = new Style(0.9D, 0.9D, 0.8F, true, SoundEvents.WATER_AMBIENT);
	}
	
	public MSFluidType(Properties properties, Style fluidStyle)
	{
		super(properties);
		this.fluidStyle = fluidStyle;
	}
	
	@Override
	public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer)
	{
		consumer.accept(new IClientFluidTypeExtensions()
		{
			public ResourceLocation stillTexture;
			public ResourceLocation flowingTexture;
			
			@Override
			public ResourceLocation getStillTexture()
			{
				if(stillTexture == null)
					stillTexture = Objects.requireNonNull(NeoForgeRegistries.FLUID_TYPES.getKey(MSFluidType.this))
							.withPrefix("block/still_");
				return stillTexture;
			}
			
			@Override
			public ResourceLocation getFlowingTexture()
			{
				if(flowingTexture == null)
					flowingTexture = Objects.requireNonNull(NeoForgeRegistries.FLUID_TYPES.getKey(MSFluidType.this))
							.withPrefix("block/flowing_");
				return flowingTexture;
			}
		});
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
		Style fluidStyle = fluidType.getFluidStyle();
		gravity = gravity * fluidStyle.yMovement;
		double initialY = entity.getY();
		boolean isSinking = entity.getDeltaMovement().y <= 0.0D && !passesMovementThreshold(entity);
		
		entity.moveRelative(0.02F, movementVector);
		entity.move(MoverType.SELF, entity.getDeltaMovement());
		
		entity.setDeltaMovement(entity.getDeltaMovement().multiply(fluidStyle.xzMovement, fluidStyle.yMovement, fluidStyle.xzMovement));
		Vec3 fallAdjustedMoveVec = entity.getFluidFallingAdjustedMovement(gravity, isSinking, entity.getDeltaMovement());
		entity.setDeltaMovement(fallAdjustedMoveVec);
		
		LastFluidTickData data = entity.getData(MSAttachments.LAST_FLUID_TICK);
		long tick = entity.level().getGameTime();
		long lastTick = Objects.requireNonNullElse(data.lastTickMap.get(fluidType), 0L);
		
		if(!entity.isSwimming() && fluidStyle.hasSplashSound && tick != lastTick + 1)
			splashSounds(entity, fluidStyle);
		
		Vec3 moveVec = entity.getDeltaMovement();
		if(entity.horizontalCollision && entity.isFree(moveVec.x, moveVec.y + 0.6D - entity.getY() + initialY, moveVec.z))
		{
			entity.setDeltaMovement(moveVec.x, moveVec.y, moveVec.z);
		}
		
		swimSounds(entity, fluidStyle);
		
		data.lastTickMap.put(fluidType, tick);
		
		return true;
	}
	
	private static void splashSounds(LivingEntity entity, Style movementType)
	{
		//TODO adaptive volume? and conditionally use `SoundEvents.PLAYER_SPLASH_HIGH_SPEED`?
		SoundEvent splashSound = entity instanceof Player ? SoundEvents.PLAYER_SPLASH : SoundEvents.GENERIC_SPLASH;
		entity.playSound(splashSound, 0.25F, movementType.soundPitch);
	}
	
	static boolean passesMovementThreshold(LivingEntity entity)
	{
		Vec3 oldPosVec = new Vec3(entity.xOld, entity.yOld, entity.zOld);
		Vec3 newPosVec = new Vec3(entity.getX(), entity.getY(), entity.getZ());
		return oldPosVec.distanceTo(newPosVec) >= 0.05D;
	}
	
	private static void swimSounds(LivingEntity entity, Style movementType)
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
	
	public Style getFluidStyle()
	{
		return fluidStyle;
	}
	
	public static final class LastFluidTickData
	{
		private final Map<MSFluidType, Long> lastTickMap = new HashMap<>();
	}
	
	/**
	 * Called to apply fall distance resetting for fluids added by us.
	 */
	public static void handleExtraFallReset(LivingEntity entity)
	{
		if(entity.fallDistance <= 0 || !entity.isInFluidType())
			return;
		
		entity.fallDistance *= MSFluids.TYPE_REGISTER.getEntries().stream().map(Supplier::get)
				.filter(entity::isInFluidType)
				.map(entity::getFluidFallDistanceModifier)
				.min(Float::compare).orElse(1F);
	}
}

package com.mraof.minestuck.blockentity;

import com.mraof.minestuck.block.HorseClockBlock;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * Block entity present in the bottom of the horse block multiblock. Keeps track of the time and lets off a tick sound each second, which makes the blocks give off a redstone signal.
 * Chimes at the beginning of the day. It will not give off a redstone If daytime does not advance, it will not give off redstone signals after the chime. It is Geckolib animated.
 */
public class HorseClockBlockEntity extends BlockEntity implements GeoBlockEntity
{
	private static final RawAnimation TICK_ANIMATION = RawAnimation.begin().thenLoop("ticktockontheclock");
	
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
	private boolean hasChimed = false; //prevents the sound from getting stacked if the day time is frozen
	
	public HorseClockBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.HORSE_CLOCK.get(), pos, state);
	}
	
	public static void serverTick(Level level, BlockPos pos, BlockState state, HorseClockBlockEntity blockEntity)
	{
		if(level != null && level.isAreaLoaded(pos, 1))
		{
			//once per second
			if(level.getGameTime() % 40 == 0)
				blockEntity.sendUpdate(true);
			if(level.getGameTime() % 40 == 20)
				blockEntity.sendUpdate(false);
			
			if(level.getDayTime() % 24000 == 0 && isNotMuffled(blockEntity) && !blockEntity.hasChimed) //chimes at the beginning of day
			{
				fullPower(level, blockEntity);
			}
		}
	}
	
	public static void fullPower(Level level, HorseClockBlockEntity blockEntity)
	{
		level.playSound(null, blockEntity.getBlockPos(), MSSoundEvents.BLOCK_HORSE_CLOCK_CHIME.get(), SoundSource.BLOCKS, 2F, 1F);
		blockEntity.hasChimed = true;
		
		if(level.isAreaLoaded(blockEntity.getBlockPos(), 1))
		{
			powerBlocks(blockEntity, 15);
		}
	}
	
	private void sendUpdate(boolean isTick) //versus tock
	{
		if(!hasChimed) //the chime gives off a strong signal that should not be overridden
			powerBlocks(this, isTick ? 4 : 2);
		
		if(isNotMuffled(this))
		{
			if(isTick)
				level.playSound(null, getBlockPos(), MSSoundEvents.BLOCK_CLOCK_TICK.get(), SoundSource.BLOCKS, 0.5F, 1F);
			else
				level.playSound(null, getBlockPos(), MSSoundEvents.BLOCK_CLOCK_TOCK.get(), SoundSource.BLOCKS, 0.5F, 1F);
		}
		
		if(level.getDayTime() % 24000 != 0) //resets if any time of day except the beginning
			hasChimed = false;
	}
	
	private static void powerBlocks(HorseClockBlockEntity blockEntity, int power)
	{
		Level level = blockEntity.level;
		BlockPos pos = blockEntity.getBlockPos();
		BlockState state = blockEntity.getBlockState();
		
		//powers the Bottom block
		level.setBlock(pos, state.setValue(HorseClockBlock.POWER, power), Block.UPDATE_ALL);
		
		//powers the Center block if present
		BlockPos centerPos = pos.above();
		BlockState centerState = level.getBlockState(centerPos);
		if(centerState.is(MSBlocks.HORSE_CLOCK.CENTER.get()))
			level.setBlock(centerPos, centerState.setValue(HorseClockBlock.POWER, power), Block.UPDATE_ALL);
		
		//powers the Top block if present
		BlockPos topPos = pos.above(2);
		BlockState topState = level.getBlockState(topPos);
		if(topState.is(MSBlocks.HORSE_CLOCK.TOP.get()))
			level.setBlock(topPos, topState.setValue(HorseClockBlock.POWER, power), Block.UPDATE_ALL);
		
		level.scheduleTick(new BlockPos(pos), level.getBlockState(pos).getBlock(), 10); //set to half a second
		level.scheduleTick(new BlockPos(centerPos), level.getBlockState(centerPos).getBlock(), 10);
		level.scheduleTick(new BlockPos(topPos), level.getBlockState(topPos).getBlock(), 10);
	}
	
	/**
	 * Will not make a sound if a sound dampening block is below it
	 */
	private static boolean isNotMuffled(HorseClockBlockEntity blockEntity)
	{
		return !blockEntity.level.getBlockState(blockEntity.getBlockPos().below()).is(BlockTags.OCCLUDES_VIBRATION_SIGNALS);
	}
	
	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache()
	{
		return cache;
	}
	
	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers)
	{
		controllers.add(new AnimationController<>(this, "pendulum", 0, this::pendulumAnimation));
	}
	
	private <E extends BlockEntity & GeoAnimatable> PlayState pendulumAnimation(AnimationState<E> state)
	{
		state.getController().setAnimation(TICK_ANIMATION);
		return PlayState.CONTINUE;
	}
}

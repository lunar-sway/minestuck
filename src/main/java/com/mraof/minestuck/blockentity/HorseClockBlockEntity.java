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
import net.minecraft.world.phys.AABB;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class HorseClockBlockEntity extends BlockEntity implements IAnimatable
{
	private final AnimationFactory factory = new AnimationFactory(this);
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
		
		if(level.getDayTime() % 24000 != 0)
			hasChimed = false;
	}
	
	private static void powerBlocks(HorseClockBlockEntity blockEntity, int power)
	{
		Level level = blockEntity.level;
		BlockPos pos = blockEntity.getBlockPos();
		BlockState state = blockEntity.getBlockState();
		
		level.setBlock(pos, state.setValue(HorseClockBlock.POWER, power), Block.UPDATE_ALL);
		
		BlockPos centerPos = pos.above();
		BlockState centerState = level.getBlockState(centerPos);
		if(centerState.is(MSBlocks.HORSE_CLOCK.CENTER.get()))
			level.setBlock(centerPos, centerState.setValue(HorseClockBlock.POWER, power), Block.UPDATE_ALL);
		
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
	public AABB getRenderBoundingBox()
	{
		return INFINITE_EXTENT_AABB; //keeps the model rendered even when the blockpos is no longer viewed by the camera
	}
	
	@Override
	public AnimationFactory getFactory()
	{
		return factory;
	}
	
	@Override
	public void registerControllers(AnimationData data)
	{
		data.addAnimationController(new AnimationController<>(this, "pendulum", 0, this::pendulumAnimation));
	}
	
	private <E extends BlockEntity & IAnimatable> PlayState pendulumAnimation(AnimationEvent<E> event)
	{
		event.getController().setAnimation(new AnimationBuilder().addAnimation("ticktockontheclock", true));
		return PlayState.CONTINUE;
	}
}

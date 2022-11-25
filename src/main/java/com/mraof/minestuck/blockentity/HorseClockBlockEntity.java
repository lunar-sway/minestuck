package com.mraof.minestuck.blockentity;

import net.minecraft.core.BlockPos;
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
	
	public HorseClockBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.HORSE_CLOCK.get(), pos, state);
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

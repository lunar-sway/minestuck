package com.mraof.minestuck.blockentity.machine;

import com.mojang.math.Vector3f;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.blockentity.ItemStackBlockEntity;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.util.ColorHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.ParticleKeyFrameEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.awt.*;

public class TotemLatheDowelBlockEntity extends ItemStackBlockEntity implements IAnimatable
{
	private final AnimationFactory factory = new AnimationFactory(this);
	
	public TotemLatheDowelBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.TOTEM_LATHE_DOWEL.get(), pos, state);
	}
	
	@Nullable
	private TotemLatheBlockEntity getTotemLatheEntity()
	{
		if(level == null)
			return null;
		
		BlockPos pos = MSBlocks.TOTEM_LATHE.getSlotPos(getBlockPos(), getBlockState());
		BlockEntity be = level.getBlockEntity(pos);
		if(be instanceof TotemLatheBlockEntity)
		{
			return (TotemLatheBlockEntity) be;
		}
		
		return null;
	}
	
	@Override
	public AABB getRenderBoundingBox()
	{
		return INFINITE_EXTENT_AABB;
	}
	
	@Override
	public AnimationFactory getFactory()
	{
		return factory;
	}
	
	@Override
	public void registerControllers(AnimationData data)
	{
		AnimationController<TotemLatheDowelBlockEntity> controller = new AnimationController<>(this, "carveAnimation", 0, this::carveAnimation);
		controller.registerParticleListener(this::particleEventListener);
		data.addAnimationController(controller);
	}
	
	private <T extends IAnimatable> void particleEventListener(ParticleKeyFrameEvent<T> event)
	{
		if(level == null || getTotemLatheEntity() == null) {
			return;
		}
		
		Direction dir = getTotemLatheEntity().getFacing();
		BlockPos blockPos = getBlockPos();
		Vector3f pos = new Vector3f(blockPos.getX(), blockPos.getY(), blockPos.getZ());
		pos.add(dir.getCounterClockWise().getStepX() * 0.375f, 0, dir.getCounterClockWise().getStepZ() * 0.375f);
		pos.add(0.5f, 0.3f, 0.5f);
		
		Color stackColor = new Color(ColorHandler.getColorFromStack(getStack()));
		level.addParticle(new DustParticleOptions(new Vector3f(stackColor.getRed() / 255f, stackColor.getGreen() / 255f, stackColor.getBlue() / 255f), 1),
				pos.x(), pos.y(), pos.z(), 1, 1, 1);
	}
	
	private <E extends BlockEntity & IAnimatable> PlayState carveAnimation(AnimationEvent<E> event)
	{
		TotemLatheBlockEntity totemLathe = getTotemLatheEntity();
		if(totemLathe != null && totemLathe.isProcessing())
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("carvetotem", false));
			return PlayState.CONTINUE;
		}
		event.getController().markNeedsReload();
		return PlayState.STOP;
	}
}

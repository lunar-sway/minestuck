package com.mraof.minestuck.tileentity.machine;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.tileentity.ItemStackTileEntity;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
import com.mraof.minestuck.util.ColorHandler;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.ParticleKeyFrameEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.awt.*;

public class TotemLatheDowelTileEntity extends ItemStackTileEntity implements IAnimatable
{
	private final AnimationFactory factory = new AnimationFactory(this);
	
	public TotemLatheDowelTileEntity()
	{
		super(MSTileEntityTypes.TOTEM_LATHE_DOWEL.get());
	}
	
	private TotemLatheTileEntity getTotemLatheEntity()
	{
		if(level == null)
			return null;
		
		BlockPos pos = MSBlocks.TOTEM_LATHE.getSlotPos(getBlockPos(), getBlockState());
		TileEntity te = level.getBlockEntity(pos);
		if(te instanceof TotemLatheTileEntity)
		{
			return (TotemLatheTileEntity) te;
		}
		
		return null;
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox()
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
		AnimationController<TotemLatheDowelTileEntity> controller = new AnimationController<>(this, "carveAnimation", 0, this::carveAnimation);
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
		level.addParticle(new RedstoneParticleData(stackColor.getRed() / 255f, stackColor.getGreen() / 255f, stackColor.getBlue() / 255f, 1),
				pos.x(), pos.y(), pos.z(), 1, 1, 1);
	}
	
	private <E extends TileEntity & IAnimatable> PlayState carveAnimation(AnimationEvent<E> event)
	{
		TotemLatheTileEntity totemLathe = getTotemLatheEntity();
		if(totemLathe != null && totemLathe.isProcessing())
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("carvetotem", false));
			return PlayState.CONTINUE;
		}
		event.getController().markNeedsReload();
		return PlayState.STOP;
	}
}

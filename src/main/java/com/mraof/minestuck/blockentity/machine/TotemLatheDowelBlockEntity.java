package com.mraof.minestuck.blockentity.machine;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.blockentity.ItemStackBlockEntity;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.util.ColorHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.keyframe.event.ParticleKeyframeEvent;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.awt.*;

/**
 * Stores a cruxite dowel. When && combination recipes are triggered, an animation will play that spins the dowel and carves it
 * {@link TotemLatheBlockEntity} handles the storage of the punched cards and logic involved in making the totem from the uncarved dowel.
 * The Totem Lathe is a core Editmode deployable
 */
public class TotemLatheDowelBlockEntity extends ItemStackBlockEntity implements GeoBlockEntity
{
	private static final RawAnimation CARVE_ANIMATION = RawAnimation.begin().then("carvetotem", Animation.LoopType.PLAY_ONCE);
	
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
	
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
	public AnimatableInstanceCache getAnimatableInstanceCache()
	{
		return cache;
	}
	
	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers)
	{
		AnimationController<TotemLatheDowelBlockEntity> controller = new AnimationController<>(this, "carveAnimation", 0, this::carveAnimation);
		controller.setParticleKeyframeHandler(this::particleEventListener);
		controllers.add(controller);
	}
	
	private <T extends GeoAnimatable> void particleEventListener(ParticleKeyframeEvent<T> event)
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
	
	private <E extends BlockEntity & GeoAnimatable> PlayState carveAnimation(AnimationState<E> event)
	{
		TotemLatheBlockEntity totemLathe = getTotemLatheEntity();
		if(totemLathe != null && totemLathe.isProcessing())
		{
			event.getController().setAnimation(CARVE_ANIMATION);
			return PlayState.CONTINUE;
		}
		event.getController().forceAnimationReset();
		return PlayState.STOP;
	}
}

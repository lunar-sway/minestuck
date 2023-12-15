package com.mraof.minestuck.block.fluid;

import com.mraof.minestuck.fluid.IMSFog;
import com.mraof.minestuck.fluid.MSFluidType;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

/**
 * This class allows for implementing most fluid blocks in the game
 */
public class FlowingModFluidBlock extends LiquidBlock implements IMSFog
{
	protected final Vec3 fogColor;
	protected final float fogDensity;
	
	public FlowingModFluidBlock(Supplier<? extends FlowingFluid> fluid, Vec3 fogColor, float fogDensity, Properties properties)
	{
		super(fluid, properties);
		this.fogColor = fogColor;
		this.fogDensity = fogDensity;
	}
	
	@Override
	public float getMSFogDensity()
	{
		return fogDensity;
	}
	
	@Override
	public Vec3 getMSFogColor(LevelReader level, BlockPos pos, Entity entity, Vec3 originalColor, double partialTicks)
	{
		return fogColor;
	}
	
	@Override
	public void animateTick(BlockState state, Level level, BlockPos blockPos, RandomSource rand)
	{
		super.animateTick(state, level, blockPos, rand);
		
		if (rand.nextInt(96) == 0) {
			FlowingFluid fluid = this.getFluid();
			
			if(fluid.getFluidType() instanceof MSFluidType fluidType)
			{
				MSFluidType.FLUID_STYLE fluidStyle = fluidType.getFluidStyle();
				
				//when not a source block, it will make a sound
				if(!state.getFluidState().isSource())
				{
					//TODO create custom sound for subtitle change
					if(fluidStyle == MSFluidType.FLUID_STYLE.VISCOUS)
						level.playLocalSound(blockPos.getX() + 0.5D, blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D, SoundEvents.LAVA_AMBIENT, SoundSource.BLOCKS, rand.nextFloat() * 0.4F, fluidStyle.soundPitch + (rand.nextFloat() - 0.9F), false);
					else
						level.playLocalSound(blockPos.getX() + 0.5D, blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D, SoundEvents.WATER_AMBIENT, SoundSource.BLOCKS, rand.nextFloat() * 0.4F, fluidStyle.soundPitch + (rand.nextFloat() - 0.9F), false);
				}
			}
		}
	}
}
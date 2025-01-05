package com.mraof.minestuck.block.fluid;

import com.mraof.minestuck.fluid.MSFluidType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

public class MSLiquidBlock extends LiquidBlock
{
	protected final boolean underwaterParticles;
	
	public MSLiquidBlock(FlowingFluid fluid, boolean underwaterParticles, Properties properties)
	{
		super(fluid, properties);
		this.underwaterParticles = underwaterParticles;
	}
	
	@Override
	public void animateTick(BlockState state, Level level, BlockPos blockPos, RandomSource rand)
	{
		super.animateTick(state, level, blockPos, rand);
		
		ambientSounds(state, level, blockPos, rand);
		
		if(underwaterParticles && rand.nextInt(20) == 0 && state.getFluidState().isSource())
		{
			level.addParticle(ParticleTypes.UNDERWATER, blockPos.getX() + rand.nextDouble(), blockPos.getY() + rand.nextDouble(), blockPos.getZ() + rand.nextDouble(), 0.0D, 0.0D, 0.0D);
		}
	}
	
	private void ambientSounds(BlockState state, Level level, BlockPos blockPos, RandomSource rand)
	{
		if(rand.nextInt(96) == 0)
		{
			FlowingFluid fluid = this.fluid;
			
			if(fluid.getFluidType() instanceof MSFluidType fluidType)
			{
				MSFluidType.Style fluidStyle = fluidType.getFluidStyle();
				
				//when not a source block, it will make a sound
				if(!state.getFluidState().isSource())
				{
					//TODO create custom sound for subtitle change
					level.playLocalSound(blockPos.getX() + 0.5D, blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D, fluidStyle.ambientSound(), SoundSource.BLOCKS, rand.nextFloat() * 0.4F, fluidStyle.soundPitch() + (rand.nextFloat() - 0.9F), false);
				}
			}
		}
	}
}

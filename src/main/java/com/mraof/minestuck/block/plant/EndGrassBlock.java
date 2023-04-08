package com.mraof.minestuck.block.plant;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class EndGrassBlock extends Block
{
	public EndGrassBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public void animateTick(BlockState stateIn, Level level, BlockPos pos, RandomSource rand)
	{
		super.animateTick(stateIn, level, pos, rand);
		
		if(rand.nextInt(10) == 0)
			level.addParticle(ParticleTypes.PORTAL, (float) pos.getX() + rand.nextFloat(), (float) pos.getY() + 1.1F, (float) pos.getZ() + rand.nextFloat(), 0.0D, 0.0D, 0.0D);
	}
}
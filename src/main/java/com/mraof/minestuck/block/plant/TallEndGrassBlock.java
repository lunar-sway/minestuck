package com.mraof.minestuck.block.plant;

import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoublePlantBlock;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.Random;

public class TallEndGrassBlock extends DoublePlantBlock
{
	public TallEndGrassBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return state.getBlock() == Blocks.END_STONE || state.getBlock() == MSBlocks.END_GRASS_BLOCK || state.getBlock() == MSBlocks.COARSE_END_STONE;
	}
	/*
	@Override
	@SuppressWarnings("deprecation")
	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random)
	{
		if(!worldIn.isRemote)
		{
			List<LivingEntity> list = worldIn.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1));
			for(LivingEntity livingentity : list)
			{
				if(!livingentity.isSneaking())
				{
					double oldPosX = livingentity.getPosX();
					double oldPosY = livingentity.getPosY();
					double oldPosZ = livingentity.getPosZ();
					
					for(int i = 0; i < 16; ++i)
					{
						double newPosX = livingentity.getPosX() + (livingentity.getRNG().nextDouble() - 0.5D) * 16.0D;
						double newPosY = MathHelper.clamp(livingentity.getPosY() + (double) (livingentity.getRNG().nextInt(16) - 8), 0.0D, worldIn.getActualHeight() - 1);
						double newPosZ = livingentity.getPosZ() + (livingentity.getRNG().nextDouble() - 0.5D) * 16.0D;
						if(livingentity.isPassenger())
						{
							livingentity.stopRiding();
						}
						
						if(livingentity.attemptTeleport(newPosX, newPosY, newPosZ, true))
						{
							worldIn.playSound(null, oldPosX, oldPosY, oldPosZ, SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
							livingentity.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
							break;
						}
					}
				}
			}
		}
	}
	*/
	@Override
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		super.animateTick(stateIn, worldIn, pos, rand);
		
		if(rand.nextInt(10) == 0)
			worldIn.addParticle(ParticleTypes.PORTAL, (float) pos.getX() + rand.nextFloat(), (float) pos.getY() + 1.1F, (float) pos.getZ() + rand.nextFloat(), 0.0D, 0.0D, 0.0D);
	}
}
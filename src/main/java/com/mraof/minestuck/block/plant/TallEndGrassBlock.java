package com.mraof.minestuck.block.plant;

import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoublePlantBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
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
		return state.getBlock() == MSBlocks.END_GRASS_BLOCK;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random)
	{
		if(!worldIn.isRemote && random.nextFloat() >= .75F)
		{
			List<LivingEntity> list = worldIn.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY(), pos.getZ() + 1));
			for(LivingEntity livingentity : list)
			{
				if(!livingentity.isSneaking() && !livingentity.isSpectator())
				{
					randomTeleport(worldIn, livingentity);
				}
			}
		}
	}
	
	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player)
	{
		super.onBlockHarvested(worldIn, pos, state, player);
		if(!worldIn.isRemote)
		{
			randomTeleport(worldIn, player);
		}
	}
	
	@Override
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		super.animateTick(stateIn, worldIn, pos, rand);
		
		if(rand.nextInt(10) == 0)
			worldIn.addParticle(ParticleTypes.PORTAL, (float) pos.getX() + rand.nextFloat(), (float) pos.getY() + 1.1F, (float) pos.getZ() + rand.nextFloat(), 0.0D, 0.0D, 0.0D);
	}
	
	public void randomTeleport(World worldIn, LivingEntity livingEntity)
	{
		double oldPosX = livingEntity.getPosX();
		double oldPosY = livingEntity.getPosY();
		double oldPosZ = livingEntity.getPosZ();
		
		for(int i = 0; i < 16; ++i)
		{
			double newPosX = livingEntity.getPosX() + (livingEntity.getRNG().nextDouble() - 0.5D) * 16.0D;
			double newPosY = MathHelper.clamp(livingEntity.getPosY() + (double) (livingEntity.getRNG().nextInt(16) - 8), 0.0D, worldIn.getActualHeight() - 1);
			double newPosZ = livingEntity.getPosZ() + (livingEntity.getRNG().nextDouble() - 0.5D) * 16.0D;
			if(livingEntity.isPassenger())
			{
				livingEntity.stopRiding();
			}
			
			if(livingEntity.attemptTeleport(newPosX, newPosY, newPosZ, true))
			{
				worldIn.playSound(null, oldPosX, oldPosY, oldPosZ, SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
				livingEntity.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
				break;
			}
		}
	}
}
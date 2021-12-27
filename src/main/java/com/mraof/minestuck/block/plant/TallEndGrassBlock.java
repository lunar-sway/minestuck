package com.mraof.minestuck.block.plant;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.FrogEntity;
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
	protected boolean mayPlaceOn(BlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return state.getBlock() == MSBlocks.END_GRASS;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random)
	{
		if(!worldIn.isClientSide && random.nextFloat() >= .75F)
		{
			List<LivingEntity> list = worldIn.getEntitiesOfClass(LivingEntity.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY(), pos.getZ() + 1));
			for(LivingEntity livingentity : list)
			{
				if(!livingentity.isShiftKeyDown() && !livingentity.isSpectator() && !(livingentity instanceof FrogEntity))
				{
					randomTeleport(worldIn, livingentity);
				}
			}
		}
	}
	
	@Override
	public void playerWillDestroy(World worldIn, BlockPos pos, BlockState state, PlayerEntity player)
	{
		super.playerWillDestroy(worldIn, pos, state, player);
		if(!worldIn.isClientSide && !player.isCreative())
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
		double oldPosX = livingEntity.getX();
		double oldPosY = livingEntity.getY();
		double oldPosZ = livingEntity.getZ();
		
		for(int i = 0; i < 16; ++i)
		{
			double newPosX = livingEntity.getX() + (livingEntity.getRandom().nextDouble() - 0.5D) * 16.0D;
			double newPosY = MathHelper.clamp(livingEntity.getY() + (double) (livingEntity.getRandom().nextInt(16) - 8), 0.0D, worldIn.getHeight() - 1);//getAcutalHeight/getLogicalHeight
			double newPosZ = livingEntity.getZ() + (livingEntity.getRandom().nextDouble() - 0.5D) * 16.0D;
			if(livingEntity.isPassenger())
			{
				livingEntity.stopRiding();
			}
			
			if(livingEntity.randomTeleport(newPosX, newPosY, newPosZ, true))
			{
				worldIn.playSound(null, oldPosX, oldPosY, oldPosZ, SoundEvents.CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
				livingEntity.playSound(SoundEvents.CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
				break;
			}
		}
	}
}
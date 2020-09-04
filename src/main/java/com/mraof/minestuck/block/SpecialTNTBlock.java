package com.mraof.minestuck.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TNTBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.Random;

public class SpecialTNTBlock extends TNTBlock
{
	private final boolean primed, unstable, instant;
	
	public SpecialTNTBlock(Properties builder, boolean primed, boolean unstable, boolean instant)
	{
		super(builder);
		this.primed = primed;
		this.unstable = unstable;
		this.instant = instant;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void onBlockClicked(BlockState state, World worldIn, BlockPos pos, PlayerEntity player)
	{
		if(primed)
		{
			this.explode(worldIn, pos, player);
			worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), Constants.BlockFlags.DEFAULT_AND_RERENDER);
		}
	}
	
	@Override
	public void catchFire(BlockState state, World world, BlockPos pos, @Nullable Direction direction, @Nullable LivingEntity igniter)
	{
		explode(world, pos, igniter);
	}
	
	private void explode(World worldIn, BlockPos pos, LivingEntity igniter)
	{
		if(!worldIn.isRemote)
		{
			TNTEntity entity = new TNTEntity(worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, igniter);
			if(instant)
				entity.setFuse(0);
			worldIn.addEntity(entity);
			worldIn.playSound(null, entity.getPosX(), entity.getPosY(), entity.getPosZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
		}
	}
	
	@Override
	public void onExplosionDestroy(World worldIn, BlockPos pos, Explosion explosionIn)
	{
		if(!worldIn.isRemote)
		{
			TNTEntity entity = new TNTEntity(worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, explosionIn.getExplosivePlacedBy());
			entity.setFuse(worldIn.rand.nextInt(entity.getFuse() / 4) + entity.getFuse() / 8);
			if(instant)
				entity.setFuse(entity.getFuse() / 2);
			worldIn.addEntity(entity);
		}
	}
	
	@Override
	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random)
	{
		if(unstable && random.nextDouble() < 0.1)
		{
			this.explode(worldIn, pos, null);
			worldIn.removeBlock(pos, false);
		}
	}
}
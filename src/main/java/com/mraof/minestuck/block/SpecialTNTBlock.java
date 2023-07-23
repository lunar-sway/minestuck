package com.mraof.minestuck.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class SpecialTNTBlock extends TntBlock
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
	public void attack(BlockState state, Level level, BlockPos pos, Player player)
	{
		if(primed)
		{
			this.explode(level, pos, player);
			level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL_IMMEDIATE);
		}
	}
	
	@Override
	public void onCaughtFire(BlockState state, Level level, BlockPos pos, @Nullable Direction direction, @Nullable LivingEntity igniter)
	{
		explode(level, pos, igniter);
	}
	
	private void explode(Level level, BlockPos pos, LivingEntity igniter)
	{
		if(!level.isClientSide)
		{
			PrimedTnt entity = new PrimedTnt(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, igniter);
			if(instant)
				entity.setFuse(0);
			level.addFreshEntity(entity);
			level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
		}
	}
	
	@Override
	public void wasExploded(Level level, BlockPos pos, Explosion explosionIn)
	{
		if(!level.isClientSide)
		{
			PrimedTnt entity = new PrimedTnt(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, explosionIn.getIndirectSourceEntity());
			entity.setFuse(level.random.nextInt(entity.getFuse() / 4) + entity.getFuse() / 8);
			if(instant)
				entity.setFuse(entity.getFuse() / 2);
			level.addFreshEntity(entity);
		}
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
	{
		if(unstable && random.nextDouble() < 0.1)
		{
			this.explode(level, pos, null);
			level.removeBlock(pos, false);
		}
	}
}
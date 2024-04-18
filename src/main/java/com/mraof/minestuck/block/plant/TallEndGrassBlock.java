package com.mraof.minestuck.block.plant;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.FrogEntity;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TallEndGrassBlock extends DoublePlantBlock
{
	public TallEndGrassBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos)
	{
		return state.is(MSBlocks.END_GRASS.get());
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
	{
		if(!level.isClientSide && random.nextFloat() >= .75F)
		{
			List<LivingEntity> list = level.getEntitiesOfClass(LivingEntity.class, new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY(), pos.getZ() + 1));
			for(LivingEntity livingentity : list)
			{
				if(!livingentity.isShiftKeyDown() && !livingentity.isSpectator() && !(livingentity instanceof FrogEntity))
				{
					randomTeleport(level, livingentity);
				}
			}
		}
	}
	
	@Override
	public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player)
	{
		state = super.playerWillDestroy(level, pos, state, player);
		if(!level.isClientSide && !player.isCreative())
		{
			randomTeleport(level, player);
		}
		return state;
	}
	
	@Override
	public void animateTick(BlockState stateIn, Level level, BlockPos pos, RandomSource rand)
	{
		super.animateTick(stateIn, level, pos, rand);
		
		if(rand.nextInt(10) == 0)
			level.addParticle(ParticleTypes.PORTAL, (float) pos.getX() + rand.nextFloat(), (float) pos.getY() + 1.1F, (float) pos.getZ() + rand.nextFloat(), 0.0D, 0.0D, 0.0D);
	}
	
	public void randomTeleport(Level level, LivingEntity livingEntity)
	{
		double oldPosX = livingEntity.getX();
		double oldPosY = livingEntity.getY();
		double oldPosZ = livingEntity.getZ();
		
		for(int i = 0; i < 16; ++i)
		{
			double newPosX = livingEntity.getX() + (livingEntity.getRandom().nextDouble() - 0.5D) * 16.0D;
			double newPosY = Mth.clamp(livingEntity.getY() + (double) (livingEntity.getRandom().nextInt(16) - 8), 0.0D, level.getHeight() - 1);//getAcutalHeight/getLogicalHeight
			double newPosZ = livingEntity.getZ() + (livingEntity.getRandom().nextDouble() - 0.5D) * 16.0D;
			if(livingEntity.isPassenger())
			{
				livingEntity.stopRiding();
			}
			
			if(livingEntity.randomTeleport(newPosX, newPosY, newPosZ, true))
			{
				level.playSound(null, oldPosX, oldPosY, oldPosZ, SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
				livingEntity.playSound(SoundEvents.CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
				break;
			}
		}
	}
}
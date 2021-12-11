package com.mraof.minestuck.block.redstone;

import com.mraof.minestuck.block.MSProperties;
import com.mraof.minestuck.tileentity.redstone.WirelessRedstoneReceiverTileEntity;
import com.mraof.minestuck.util.ParticlesAroundSolidBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class WirelessRedstoneReceiverBlock extends Block
{
	public static final IntegerProperty POWER = BlockStateProperties.POWER_0_15;
	public static final BooleanProperty AUTO_RESET = MSProperties.AUTO_RESET;
	
	public static final String NOW_AUTO = "minestuck.receiver_now_auto_reset";
	public static final String NOW_NOT_AUTO = "minestuck.receiver_now_not_auto_reset";
	
	public WirelessRedstoneReceiverBlock(Properties properties)
	{
		super(properties);
		setDefaultState(getDefaultState().with(POWER, 0).with(AUTO_RESET, true));
	}
	
	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new WirelessRedstoneReceiverTileEntity();
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
	{
		if(!player.isSneaking())
		{
			worldIn.setBlockState(pos, state.with(AUTO_RESET, !state.get(AUTO_RESET)));
			if(state.get(AUTO_RESET))
			{
				if(!worldIn.isRemote)
					player.sendMessage(new TranslationTextComponent(NOW_NOT_AUTO));
				worldIn.playSound(null, pos, SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.BLOCKS, 0.5F, 1.2F);
			} else
			{
				if(!worldIn.isRemote)
					player.sendMessage(new TranslationTextComponent(NOW_AUTO));
				worldIn.playSound(null, pos, SoundEvents.BLOCK_PISTON_CONTRACT, SoundCategory.BLOCKS, 0.5F, 1.2F);
			}
			
			return ActionResultType.SUCCESS;
		}
		
		return ActionResultType.PASS;
	}
	
	@Override
	public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side)
	{
		return blockState.get(POWER);
	}
	
	@Override
	public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side)
	{
		return true;
	}
	
	@Override
	public boolean canProvidePower(BlockState state)
	{
		return true;
	}
	
	@Override
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		if(stateIn.get(POWER) > 0)
			ParticlesAroundSolidBlock.spawnParticles(worldIn, pos, () -> RedstoneParticleData.REDSTONE_DUST);
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		super.fillStateContainer(builder);
		builder.add(POWER);
		builder.add(AUTO_RESET);
	}
}
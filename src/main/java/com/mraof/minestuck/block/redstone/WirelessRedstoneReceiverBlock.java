package com.mraof.minestuck.block.redstone;

import com.mraof.minestuck.block.MSProperties;
import com.mraof.minestuck.effects.CreativeShockEffect;
import com.mraof.minestuck.tileentity.redstone.WirelessRedstoneReceiverTileEntity;
import com.mraof.minestuck.util.ParticlesAroundSolidBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
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
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Receives redstone inputs from wireless redstone transmitters. Has a blockstate that allows the receiver to retain the strongest redstone signal it has received
 */
public class WirelessRedstoneReceiverBlock extends HorizontalBlock
{
	public static final IntegerProperty POWER = BlockStateProperties.POWER;
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED; //used for texture purposes
	public static final BooleanProperty AUTO_RESET = MSProperties.AUTO_RESET;
	
	public static final String NOW_AUTO = "minestuck.receiver_now_auto_reset";
	public static final String NOW_NOT_AUTO = "minestuck.receiver_now_not_auto_reset";
	
	public WirelessRedstoneReceiverBlock(Properties properties)
	{
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(POWER, 0).setValue(POWERED, false).setValue(AUTO_RESET, true).setValue(FACING, Direction.NORTH));
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
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
	{
		if(!CreativeShockEffect.doesCreativeShockLimit(player, CreativeShockEffect.LIMIT_MACHINE_INTERACTIONS))
		{
			worldIn.setBlock(pos, state.cycle(AUTO_RESET), Constants.BlockFlags.DEFAULT);
			if(state.getValue(AUTO_RESET))
			{
				if(!worldIn.isClientSide)
					player.sendMessage(new TranslationTextComponent(NOW_NOT_AUTO), Util.NIL_UUID);
				worldIn.playSound(null, pos, SoundEvents.PISTON_EXTEND, SoundCategory.BLOCKS, 0.5F, 1.2F);
			} else
			{
				if(!worldIn.isClientSide)
					player.sendMessage(new TranslationTextComponent(NOW_AUTO), Util.NIL_UUID);
				worldIn.playSound(null, pos, SoundEvents.PISTON_CONTRACT, SoundCategory.BLOCKS, 0.5F, 1.2F);
			}
			
			return ActionResultType.SUCCESS;
		}
		
		return ActionResultType.PASS;
	}
	
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}
	
	public void updatePower(World worldIn, BlockPos posIn, BlockPos transmitterPos)
	{
		BlockState receiverState = worldIn.getBlockState(posIn);
		int newPower = worldIn.getBlockState(transmitterPos).getValue(WirelessRedstoneTransmitterBlock.POWER);
		//worldIn.setBlock(posIn, worldIn.getBlockState(transmitterPos).setValue(POWER, newPower), Constants.BlockFlags.NOTIFY_NEIGHBORS);
		
		if(receiverState.getValue(POWER) != newPower)
			worldIn.setBlockAndUpdate(posIn, receiverState.setValue(POWER, newPower));
		else worldIn.sendBlockUpdated(posIn, receiverState, receiverState, 2);
		
		if(receiverState.getValue(POWERED) != newPower > 0)
			worldIn.setBlockAndUpdate(posIn, receiverState.setValue(POWERED, newPower > 0));
		else worldIn.sendBlockUpdated(posIn, receiverState, receiverState, 2);
		
		TileEntity tileEntity = worldIn.getBlockEntity(posIn);
		if(tileEntity instanceof WirelessRedstoneReceiverTileEntity)
		{
			WirelessRedstoneReceiverTileEntity te = (WirelessRedstoneReceiverTileEntity) tileEntity;
			te.setLastTransmitterBlockPos(transmitterPos);
		}
	}
	
	@Override
	public int getSignal(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side)
	{
		return blockState.getValue(POWER);
	}
	
	@Override
	public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side)
	{
		return true;
	}
	
	@Override
	public boolean isSignalSource(BlockState state)
	{
		return true;
	}
	
	@Override
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		if(rand.nextInt(15) < stateIn.getValue(POWER))
		{
			ParticlesAroundSolidBlock.spawnParticles(worldIn, pos, () -> RedstoneParticleData.REDSTONE);
		}
	}
	
	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(POWER);
		builder.add(POWERED);
		builder.add(AUTO_RESET);
		builder.add(FACING);
	}
}
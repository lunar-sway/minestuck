package com.mraof.minestuck.block.redstone;

import com.mraof.minestuck.block.MSProperties;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.effects.CreativeShockEffect;
import com.mraof.minestuck.tileentity.redstone.StructureCoreTileEntity;
import com.mraof.minestuck.util.ParticlesAroundSolidBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.Random;

/**
 *
 */
public class StructureCoreBlock extends HorizontalBlock
{
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final BooleanProperty ACTIVE = MSProperties.MACHINE_TOGGLE;
	
	public StructureCoreBlock(Properties properties)
	{
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(POWERED, false).setValue(ACTIVE, false));
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
	{
		if(!CreativeShockEffect.doesCreativeShockLimit(player, CreativeShockEffect.LIMIT_MACHINE_INTERACTIONS))
		{
			TileEntity tileEntity = worldIn.getBlockEntity(pos);
			if(tileEntity instanceof StructureCoreTileEntity)
			{
				if(player.isCrouching())
				{
					worldIn.setBlock(pos, state.cycle(ACTIVE), Constants.BlockFlags.DEFAULT);
					if(state.getValue(ACTIVE))
						worldIn.playSound(null, pos, SoundEvents.PISTON_EXTEND, SoundCategory.BLOCKS, 0.5F, 1.2F);
					else
					{
						worldIn.setBlock(pos, state.setValue(POWERED, false), Constants.BlockFlags.DEFAULT);
						worldIn.playSound(null, pos, SoundEvents.PISTON_CONTRACT, SoundCategory.BLOCKS, 0.5F, 1.2F);
					}
					((StructureCoreTileEntity) tileEntity).prepForUpdate(); //sets tickCycle to 600 so next tick an update will occur
				} else if(worldIn.isClientSide && !player.isCrouching())
				{
					StructureCoreTileEntity te = (StructureCoreTileEntity) tileEntity;
					MSScreenFactories.displayStructureCoreScreen(te);
				}
				
				return ActionResultType.SUCCESS;
			}
		}
		
		return ActionResultType.FAIL;
	}
	
	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
	{
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
		updateTileEntityWithBlock(worldIn, pos);
	}
	
	@Override
	public void onPlace(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
	{
		super.onPlace(state, worldIn, pos, oldState, isMoving);
		updateTileEntityWithBlock(worldIn, pos);
	}
	
	/**
	 * Used for cases in which the action type of the TE would benefit from instant change
	 */
	public void updateTileEntityWithBlock(World worldIn, BlockPos pos)
	{
		TileEntity tileEntity = worldIn.getBlockEntity(pos);
		if(tileEntity instanceof StructureCoreTileEntity)
		{
			StructureCoreTileEntity structureCoreTileEntity = (StructureCoreTileEntity) tileEntity;
			if(structureCoreTileEntity.getBlockState().getValue(ACTIVE) &&
					structureCoreTileEntity.getActionType() == StructureCoreTileEntity.ActionType.WRITE) //having the variable hasBeenCompleted changed to true as soon as possible will improve the response speed of READ_AND_WIPE functionality
			{
				((StructureCoreTileEntity) tileEntity).prepForUpdate();
			}
		}
	}
	
	@Override
	public boolean isSignalSource(BlockState state)
	{
		return state.getValue(POWERED);
	}
	
	@Override
	public int getSignal(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side)
	{
		return blockState.getValue(POWERED) ? 15 : 0;
	}
	
	@Override
	public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side)
	{
		return true;
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
		return new StructureCoreTileEntity();
	}
	
	@Override
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		if(stateIn.getValue(POWERED))
			ParticlesAroundSolidBlock.spawnParticles(worldIn, pos, () -> RedstoneParticleData.REDSTONE);
	}
	
	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(FACING);
		builder.add(POWERED);
		builder.add(ACTIVE);
	}
}
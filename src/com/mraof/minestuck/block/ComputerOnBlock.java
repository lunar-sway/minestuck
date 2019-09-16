package com.mraof.minestuck.block;

import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.network.skaianet.SkaiaClient;
import com.mraof.minestuck.tileentity.ComputerTileEntity;
import com.mraof.minestuck.util.ComputerProgram;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.*;
import java.util.Map.Entry;

public class ComputerOnBlock extends ComputerOffBlock
{
	public static final BooleanProperty BROKEN = MSProperties.BROKEN;
	public final IItemProvider computerOff;
	
	public ComputerOnBlock(Properties properties, Map<Direction, VoxelShape> shape, Map<Direction, VoxelShape> collisionShape, IItemProvider computerOff)
	{
		super(properties, null, shape, collisionShape);
		this.computerOff = computerOff;
		setDefaultState(getDefaultState().with(BROKEN, false));
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		super.fillStateContainer(builder);
		builder.add(BROKEN);
	}
	
	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		ComputerTileEntity tileEntity = (ComputerTileEntity) worldIn.getTileEntity(pos);

		if (tileEntity == null || player.isSneaking())
		{
			return false;
		}

		int id = ComputerProgram.getProgramID(player.getHeldItem(handIn));
		if(id != -2 && !tileEntity.hasProgram(id) && tileEntity.installedPrograms.size() < 2 && !tileEntity.hasProgram(-1)) 
		{
			if(worldIn.isRemote)
				return true;
			player.setHeldItem(handIn, ItemStack.EMPTY);
			if(id == -1) 
			{
				tileEntity.closeAll();
				worldIn.setBlockState(pos, state.with(BROKEN, true), 2);
			}
			else tileEntity.installedPrograms.put(id, true);
			tileEntity.markDirty();
			worldIn.notifyBlockUpdate(pos, state, state, 3);
			return true;
		}

		if(worldIn.isRemote && SkaiaClient.requestData(tileEntity))
			MSScreenFactories.displayComputerScreen(tileEntity);
		
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
		return new ComputerTileEntity();
	}
	
	@Override
	public Item asItem()
	{
		return computerOff.asItem();
	}
	
	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		dropItems(worldIn, pos.getX(), pos.getY(), pos.getZ(), state);
		super.onReplaced(state, worldIn, pos, newState, isMoving);
	}
	
	private void dropItems(World world, int x, int y, int z, BlockState state)
	{
		Random rand = new Random();
		ComputerTileEntity te = (ComputerTileEntity) world.getTileEntity(new BlockPos(x, y, z));
		if (te == null) 
		{
			return;
		}
		te.closeAll();
		float factor = 0.05F;

		Iterator<Entry<Integer, Boolean>> it = te.installedPrograms.entrySet().iterator();
		while(it.hasNext())
		{
			Map.Entry<Integer, Boolean> pairs = it.next();
			if(!pairs.getValue())
				continue;
			int program = pairs.getKey();

			float rx = rand.nextFloat() * 0.8F + 0.1F;
			float ry = rand.nextFloat() * 0.8F + 0.1F;
			float rz = rand.nextFloat() * 0.8F + 0.1F;
			ItemEntity entityItem = new ItemEntity(world, x + rx, y + ry, z + rz, ComputerProgram.getItem(program));
			entityItem.setMotion(rand.nextGaussian() * factor, rand.nextGaussian() * factor + 0.2F, rand.nextGaussian() * factor);
			world.addEntity(entityItem);
		}
		if(state.get(BROKEN))
		{
			float rx = rand.nextFloat() * 0.8F + 0.1F;
			float ry = rand.nextFloat() * 0.8F + 0.1F;
			float rz = rand.nextFloat() * 0.8F + 0.1F;
			ItemEntity entityItem = new ItemEntity(world, x + rx, y + ry, z + rz, ComputerProgram.getItem(-1));
			entityItem.setMotion(rand.nextGaussian() * factor, rand.nextGaussian() * factor + 0.2F, rand.nextGaussian() * factor);
			world.addEntity(entityItem);
		}
	}
}
package com.mraof.minestuck.block;

import java.util.Random;

import com.mraof.minestuck.item.MinestuckItems;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockStem;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMinestuckStem extends BlockStem
{
	private BlockDirectional crop = null;
	private boolean cropHasFacing = false;

	protected BlockMinestuckStem(BlockDirectional crop)
	{
		super(crop);
		this.crop = crop;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		IBlockState out = this.getDefaultState();
		//Metadata 0-7 represent the states where age = metadata and facing = up
		if(meta < 8)
		{
			out = out.withProperty(AGE, Integer.valueOf(meta));
		} else
		{
			//Metadata 8-11 represent the states where age = 7 and facing = south, west, north, and east
			out = out.withProperty(AGE, Integer.valueOf(7)).withProperty(FACING, EnumFacing.getHorizontal(meta));
		}
		return out;
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		int out;
		if(state.getValue(FACING) == EnumFacing.UP)
		{
			out = state.getValue(AGE);
		} else
		{
			//UP has already been accounted for, and DOWN is an illegal value, so the ordinal will be 2 or more.
			switch(state.getValue(FACING).ordinal())
			{
			case 2:		//north (2)
				out = 10;
				break;
			case 3:		//south (0)
				out = 8;
				break;
			case 4:		//west (1)
				out = 9;
				break;
			default:	//east (3)
				out = 11;
				break;
			}
		}
		return out;
	}
	
	@Override
	public void getDrops(net.minecraft.util.NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		Item item = this.getSeedItem();
		
		if (item != null)
		{
			int i = ((Integer)state.getValue(AGE)).intValue();
			if (i <= 1)
			{
				drops.add(new ItemStack(item));
			}
		}
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
		
		//If the block triggering the update is the crop this block is attached to...
		EnumFacing direction = EnumFacing.getFacingFromVector(fromPos.getX() - pos.getX(), fromPos.getY() - pos.getY(), fromPos.getZ() - pos.getZ());
		if(direction != EnumFacing.UP && state.getValue(FACING) == direction)
		{
			//Check is the block triggering the update is still a crop attached to this
			IBlockState fromState = worldIn.getBlockState(fromPos);
			if(fromState.getBlock() != crop || fromState.getValue(BlockDirectional.FACING) != direction.getOpposite())
			{
				//If not, disconnect from that block.
				worldIn.setBlockState(pos, state.withProperty(FACING, EnumFacing.UP));
			}
		}
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		return state;
	}
	
	//Taken from BlockCrops
	protected static float getGrowthChance(Block blockIn, World worldIn, BlockPos pos)
	{
		float f = 1.0F;
		BlockPos blockpos = pos.down();
		
		for (int i = -1; i <= 1; ++i)
		{
			for (int j = -1; j <= 1; ++j)
			{
				float f1 = 0.0F;
				IBlockState iblockstate = worldIn.getBlockState(blockpos.add(i, 0, j));
				
				if (iblockstate.getBlock().canSustainPlant(iblockstate, worldIn, blockpos.add(i, 0, j), net.minecraft.util.EnumFacing.UP, (net.minecraftforge.common.IPlantable)blockIn))
				{
					f1 = 1.0F;
					
					if (iblockstate.getBlock().isFertile(worldIn, blockpos.add(i, 0, j)))
					{
						f1 = 3.0F;
					}
				}
				
				if (i != 0 || j != 0)
				{
					f1 /= 4.0F;
				}
				
				f += f1;
			}
		}
		
		BlockPos blockpos1 = pos.north();
		BlockPos blockpos2 = pos.south();
		BlockPos blockpos3 = pos.west();
		BlockPos blockpos4 = pos.east();
		boolean flag = blockIn == worldIn.getBlockState(blockpos3).getBlock() || blockIn == worldIn.getBlockState(blockpos4).getBlock();
		boolean flag1 = blockIn == worldIn.getBlockState(blockpos1).getBlock() || blockIn == worldIn.getBlockState(blockpos2).getBlock();
		
		if (flag && flag1)
		{
			f /= 2.0F;
		}
		else
		{
			boolean flag2 = blockIn == worldIn.getBlockState(blockpos3.north()).getBlock() || blockIn == worldIn.getBlockState(blockpos4.north()).getBlock() || blockIn == worldIn.getBlockState(blockpos4.south()).getBlock() || blockIn == worldIn.getBlockState(blockpos3.south()).getBlock();
			
			if (flag2)
			{
				f /= 2.0F;
			}
		}
		
		return f;
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		this.checkAndDropBlock(worldIn, pos, state);
		
		if (!worldIn.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
		if (worldIn.getLightFromNeighbors(pos.up()) >= 9)
		{
			float f = getGrowthChance(this, worldIn, pos);
			
			if(net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextInt((int)(25.0F / f) + 1) == 0))
			{
				int i = ((Integer)state.getValue(AGE)).intValue();
				
				if (i < 7)
				{
					IBlockState newState = state.withProperty(AGE, Integer.valueOf(i + 1));
					worldIn.setBlockState(pos, newState, 2);
				}
				else
				{
					if(state.getValue(FACING) != EnumFacing.UP)
					{
						return;
					}
					
					EnumFacing enumFacing = EnumFacing.Plane.HORIZONTAL.random(rand);  
					pos = pos.offset(enumFacing);
					IBlockState soil = worldIn.getBlockState(pos.down());
					Block block = soil.getBlock();
					
					if (worldIn.isAirBlock(pos) && (block.canSustainPlant(soil, worldIn, pos.down(), EnumFacing.UP, this) || block == Blocks.DIRT || block == Blocks.GRASS))
					{
						worldIn.setBlockState(pos, this.crop.getDefaultState().withProperty(BlockDirectional.FACING, enumFacing.getOpposite()), 3);
						worldIn.setBlockState(pos.offset(enumFacing.getOpposite()), state.withProperty(FACING, enumFacing), 2);
					}
				}
				net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.getBlockState(pos));
			}
		}
	}
	
	@Override
	protected Item getSeedItem()
	{
		return MinestuckItems.strawberryChunk;
	}
}

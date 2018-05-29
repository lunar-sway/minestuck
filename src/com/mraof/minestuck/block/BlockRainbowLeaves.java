package com.mraof.minestuck.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mraof.minestuck.block.BlockAspectSapling.BlockType;
import com.mraof.minestuck.item.MinestuckItems;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockRainbowLeaves extends BlockMinestuckLeaves
{
	public static final PropertyInteger DISTANCE = PropertyInteger.create("distance", 0, 7);
	
	public BlockRainbowLeaves()
	{
		super();
		setUnlocalizedName("leavesEnd");
		setDefaultState(blockState.getBaseState());
		setTickRandomly(false);
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return 0;
	}
	
	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(DISTANCE, 0);
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state)
	{
		int i = state.getValue(DISTANCE);
		return i;
	}
	
	/**
	* Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
	* change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
	* block, etc.
	*/
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if (!worldIn.isRemote)
		{
			IBlockState u = worldIn.getBlockState(pos.up(1));
			IBlockState d = worldIn.getBlockState(pos.up(-1));
			IBlockState n = worldIn.getBlockState(pos.north(1));
			IBlockState s = worldIn.getBlockState(pos.north(-1));
			IBlockState e = worldIn.getBlockState(pos.east(1));
			IBlockState w = worldIn.getBlockState(pos.east(-1));
			
			int distance = 10;
			
			IBlockState state2;
			Block block = state.getBlock();
			
			if (	(u.getBlock() == MinestuckBlocks.endLog && (u.getValue(BlockEndLog.LOG_AXIS) == BlockLog.EnumAxis.Y || u.getValue(BlockEndLog.SECOND_AXIS) == BlockLog.EnumAxis.Y)) ||
					(d.getBlock() == MinestuckBlocks.endLog && (d.getValue(BlockEndLog.LOG_AXIS) == BlockLog.EnumAxis.Y || d.getValue(BlockEndLog.SECOND_AXIS) == BlockLog.EnumAxis.Y)) ||
					(n.getBlock() == MinestuckBlocks.endLog && (n.getValue(BlockEndLog.LOG_AXIS) == BlockLog.EnumAxis.Z || n.getValue(BlockEndLog.SECOND_AXIS) == BlockLog.EnumAxis.Z)) ||
					(s.getBlock() == MinestuckBlocks.endLog && (s.getValue(BlockEndLog.LOG_AXIS) == BlockLog.EnumAxis.Z || s.getValue(BlockEndLog.SECOND_AXIS) == BlockLog.EnumAxis.Z)) ||
					(e.getBlock() == MinestuckBlocks.endLog && (e.getValue(BlockEndLog.LOG_AXIS) == BlockLog.EnumAxis.X || e.getValue(BlockEndLog.SECOND_AXIS) == BlockLog.EnumAxis.X)) ||
					(w.getBlock() == MinestuckBlocks.endLog && (w.getValue(BlockEndLog.LOG_AXIS) == BlockLog.EnumAxis.X || w.getValue(BlockEndLog.SECOND_AXIS) == BlockLog.EnumAxis.X)))
			{
				distance = 0;
			} else
			{
				if(u.getBlock() == MinestuckBlocks.endLeaves)
				{
					distance = Math.min(u.getValue(DISTANCE) + 2, distance);
				}
				if(d.getBlock() == MinestuckBlocks.endLeaves)
				{
					distance = Math.min(d.getValue(DISTANCE) + 1, distance);
				}
				if(n.getBlock() == MinestuckBlocks.endLeaves)
				{
					distance = Math.min(n.getValue(DISTANCE) + 1, distance);
				}
				if(s.getBlock() == MinestuckBlocks.endLeaves)
				{
					distance = Math.min(s.getValue(DISTANCE) + 1, distance);
				}
				if(e.getBlock() == MinestuckBlocks.endLeaves)	//Leaves do not extend as far east/west because magnets
				{
					distance = Math.min(e.getValue(DISTANCE) + 2, distance);
				}
				if(w.getBlock() == MinestuckBlocks.endLeaves)
				{
					distance = Math.min(w.getValue(DISTANCE) + 2, distance);
				}
			}
			
			if (distance < 5)
			{
				worldIn.setBlockState(pos, state.withProperty(DISTANCE, distance), 7);
			}
			else
			{
				super.destroy(worldIn, pos);
			}
		}
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		if(placer instanceof EntityPlayer)
		{
			worldIn.setBlockState(pos, state.withProperty(DISTANCE, 3), 2);
		} else
		{
			updateTick(worldIn, pos, state, new Random());
		}
	}
	
	@Override
	public void beginLeavesDecay(IBlockState state, World world, BlockPos pos)
	{
		updateTick(world, pos, state, new Random());
	}

	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
		ArrayList out = new ArrayList<ItemStack>();
		out.add(new ItemStack(this));
		return out;
	}

	@Override
	protected void dropApple(World worldIn, BlockPos pos, IBlockState state, int chance) {}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] {DISTANCE});
	}
}
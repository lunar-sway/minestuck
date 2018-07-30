package com.mraof.minestuck.block;

import com.mraof.minestuck.item.TabMinestuck;

import net.minecraft.block.BlockLog;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockEndLog extends BlockLog
{
	public static final PropertyEnum<EnumAxis> SECOND_AXIS = PropertyEnum.<EnumAxis>create("axis2", EnumAxis.class);
	public static final int LEAF_SUSTAIN_DISTANCE = 5;
	
	public BlockEndLog()
	{
		super();
		setCreativeTab(TabMinestuck.instance);
		setDefaultState(blockState.getBaseState().withProperty(SECOND_AXIS, EnumAxis.NONE).withProperty(LOG_AXIS, EnumAxis.Y));
		setUnlocalizedName("logEnd");
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] {SECOND_AXIS, LOG_AXIS});
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		EnumFacing playerFacing = EnumFacing.getDirectionFromEntityLiving(pos, placer);
		EnumAxis toSecond;
		switch(playerFacing)
		{
		case DOWN:
		case UP:
			toSecond = EnumAxis.Y;
			break;
		case EAST:
		case WEST:
			toSecond = EnumAxis.X;
			break;
		case NORTH:
		case SOUTH:
			toSecond = EnumAxis.Z;
			break;
		default:
			toSecond = EnumAxis.NONE;
			break;
		}
		worldIn.setBlockState(pos, state.withProperty(SECOND_AXIS, toSecond), 2);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		IBlockState iblockstate = this.getDefaultState().withProperty(SECOND_AXIS, EnumAxis.values()[(meta-1)&3]);
		iblockstate = iblockstate.withProperty(LOG_AXIS, EnumAxis.values()[(meta>>2)&3]);
		
		return iblockstate;
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		int i = (state.getValue(SECOND_AXIS).ordinal() + 1) & 3;
		
		i |= state.getValue(LOG_AXIS).ordinal()<<2;
		
		return i;
	}
	
	@Override
	public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		return MapColor.WOOD;
	}
	
	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
	{
		items.add(new ItemStack(this, 1, 0));
	}
	
	@Override
	protected ItemStack getSilkTouchDrop(IBlockState state)
	{
		return new ItemStack(Item.getItemFromBlock(this), 1, 0);
	}
	
	@Override
	public int damageDropped(IBlockState state)
	{
		return 0;
	}
	
	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return 1;
	}
	
	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return 250;
	}
	
	public void generateLeaves(World world, BlockPos pos, IBlockState state)
	{
		EnumAxis primary = state.getValue(LOG_AXIS);
		EnumAxis secondary = state.getValue(SECOND_AXIS);
		
		if(primary == EnumAxis.X || secondary == EnumAxis.X)
		{
			leaves(world, pos.east(), 0);
			leaves(world, pos.west(), 0);
		}
		if(primary == EnumAxis.Y || secondary == EnumAxis.Y)
		{
			leaves(world, pos.up(), 0);
			leaves(world, pos.down(), 0);
		}
		if(primary == EnumAxis.Z || secondary == EnumAxis.Z)
		{
			leaves(world, pos.south(), 0);
			leaves(world, pos.north(), 0);
		}
	}
	
	private void leaves(World world, BlockPos curr, int distance)
	{
		IBlockState blockState = world.getBlockState(curr);
		if(blockState.getBlock().canBeReplacedByLeaves(blockState, world, curr))
		{
			if(distance <= LEAF_SUSTAIN_DISTANCE)
			{
				world.setBlockState(curr, MinestuckBlocks.endLeaves.getDefaultState().withProperty(BlockEndLeaves.DISTANCE, distance), 2);
				leaves(world, curr.south(),	distance + 1);
				leaves(world, curr.north(),	distance + 1);
				leaves(world, curr.up(),	distance + 1);
				leaves(world, curr.down(),	distance + 1);
				leaves(world, curr.east(),	distance + 2);
				leaves(world, curr.west(),	distance + 2);
			}
		} else if (blockState.getBlock() == MinestuckBlocks.endLeaves)
		{
			if(world.getBlockState(curr).getValue(BlockEndLeaves.DISTANCE) > distance)
			{
				world.setBlockState(curr, MinestuckBlocks.endLeaves.getDefaultState().withProperty(BlockEndLeaves.DISTANCE, distance), 2);
				leaves(world, curr.south(),	distance + 1);
				leaves(world, curr.north(),	distance + 1);
				leaves(world, curr.up(),	distance + 1);
				leaves(world, curr.down(),	distance + 1);
				leaves(world, curr.east(),	distance + 2);
				leaves(world, curr.west(),	distance + 2);
			}
		}
	}
}
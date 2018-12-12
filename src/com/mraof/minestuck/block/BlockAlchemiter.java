package com.mraof.minestuck.block;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.GuiHandler;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.tileentity.TileEntityAlchemiter;
import com.mraof.minestuck.alchemy.AlchemyRecipes;
import com.mraof.minestuck.block.BlockAlchemiter.EnumParts;

import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockAlchemiter extends BlockLargeMachine
{
	public static final PropertyEnum<EnumParts> PART1 = PropertyEnum.create("part", EnumParts.class, EnumParts.TOTEM_CORNER, EnumParts.TOTEM_PAD, EnumParts.LOWER_ROD, EnumParts.UPPER_ROD, EnumParts.TOTEM_PAD_DOWEL, EnumParts.TOTEM_PAD_TOTEM);
	public static final PropertyEnum<EnumParts> PART2 = PropertyEnum.create("part", EnumParts.class, EnumParts.SIDE_LEFT, EnumParts.SIDE_RIGHT, EnumParts.CORNER, EnumParts.CENTER_PAD);
	
	public final PropertyEnum<EnumParts> PART;
	public static final PropertyDirection DIRECTION = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	public final int index;
	
	public BlockAlchemiter()
	{
		this(0, PART1);
		
	}
	
	
	
	public BlockAlchemiter(int index, PropertyEnum<EnumParts> property)
	{
		super();
		this.index = index;
		this.PART = property;
		
		setUnlocalizedName("alchemiter");
	}
	
	
	
	//not sure how to do this.
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		EnumParts parts = state.getValue(PART);
		EnumFacing facing = state.getValue(DIRECTION);
		
		return parts.getBoundingBox(facing);
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		if(PART == null)
			return false;
		return state.getValue(PART) == EnumParts.CENTER_PAD;
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return state.getValue(PART) == EnumParts.CENTER_PAD;
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		EnumParts parts = state.getValue(PART);
		EnumFacing facing = state.getValue(DIRECTION);
		if(parts == EnumParts.CENTER_PAD)
			return BlockFaceShape.SOLID;
		if(face == facing.getOpposite() && (parts == EnumParts.SIDE_RIGHT || parts == EnumParts.SIDE_LEFT))
			return BlockFaceShape.SOLID;
		if(face == EnumFacing.DOWN && (parts == EnumParts.SIDE_RIGHT || parts == EnumParts.SIDE_LEFT || parts == EnumParts.CORNER || parts == EnumParts.TOTEM_CORNER))
			return BlockFaceShape.SOLID;
		return BlockFaceShape.UNDEFINED;
	}
	
	@Override
	public  boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		
		BlockPos mainPos = getMainPos(state, pos, worldIn);
		TileEntity te = worldIn.getTileEntity(mainPos);
		EnumParts part = state.getValue(PART);
		
		if (te instanceof TileEntityAlchemiter)
		{
			((TileEntityAlchemiter) te).onRightClick(worldIn, playerIn, state, part);
		}
		
		return true;
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return state.getValue(PART).isTotemPad();
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		if(index == 0 && meta % 4 == EnumParts.TOTEM_PAD.ordinal())
			return new TileEntityAlchemiter();
		return null;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
	
		BlockPos mainPos = getMainPos(state, pos,worldIn);
		TileEntity te = worldIn.getTileEntity(mainPos);
		if(te instanceof TileEntityAlchemiter)
		{
			TileEntityAlchemiter alchemiter = (TileEntityAlchemiter) te;
			alchemiter.breakMachine();
			if(state.getValue(PART).isTotemPad())
				alchemiter.dropItem(true);
		}
		
		super.breakBlock(worldIn, pos, state);
	}
	
	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor)
	{
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TileEntityAlchemiter)
			((TileEntityAlchemiter) te).checkStates();
	}
	
	//Block state handling
	@Override
	protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, PART1, DIRECTION);
    }
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		TileEntity te;
		te = worldIn.getTileEntity((getMainPos(state, pos, worldIn)));
		if(state.getValue(PART).isTotemPad())
		{
			if(te instanceof TileEntityAlchemiter)
			{
				ItemStack dowel = ((TileEntityAlchemiter) te).getDowel();
				EnumParts type = EnumParts.TOTEM_PAD;
				if(!dowel.isEmpty())
					if(AlchemyRecipes.hasDecodedItem(dowel))
						type = EnumParts.TOTEM_PAD_TOTEM;
					else type = EnumParts.TOTEM_PAD_DOWEL;
				return state.withProperty(PART, type);
			}
		}

		return state;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		IBlockState defaultState = getDefaultState();
		EnumParts part = EnumParts.values()[index*4 + meta % 4];
		EnumFacing facing = EnumFacing.getHorizontal(meta/4);

		return defaultState.withProperty(PART, part).withProperty(DIRECTION, facing);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		EnumParts part = state.getValue(PART);
		if(part.isTotemPad())
			part = EnumParts.TOTEM_PAD;
		EnumFacing facing = state.getValue(DIRECTION);
		return (part.ordinal()%4) + facing.getHorizontalIndex()*4;
	}
	
    /**
     *returns the block position of the "Main" block
     *aka the block with the TileEntity for the machine
     */
	public BlockPos getMainPos(IBlockState state, BlockPos pos, IBlockAccess world)
	{
		return getMainPos(state, pos, world, 4);
	}
	private BlockPos getMainPos(IBlockState state, BlockPos pos, IBlockAccess world, int count)
	{
		EnumParts part = state.getValue(PART);
		EnumFacing facing = state.getValue(DIRECTION);
		switch(part)
		{
			case TOTEM_CORNER: return pos.up();
			case TOTEM_PAD: case TOTEM_PAD_DOWEL: case TOTEM_PAD_TOTEM: return pos;
			case LOWER_ROD: return pos.down(1);
			case UPPER_ROD: return pos.down(2);
			default:
				if(count == 0)	//Prevents potential recursion crashes
					return new BlockPos(0, -1, 0);
				if(part == EnumParts.CENTER_PAD)
					pos = pos.offset(facing.rotateYCCW()).offset(facing.getOpposite());
				else pos = pos.offset(facing.rotateYCCW(), part == EnumParts.SIDE_LEFT ? 1 : part == EnumParts.SIDE_RIGHT ? 2 : 3);
				IBlockState newState = world.getBlockState(pos);
				if(newState.equals(getBlockState(EnumParts.TOTEM_CORNER, facing))
						|| newState.equals(getBlockState(EnumParts.CORNER, facing.rotateY())))
					return ((BlockAlchemiter) newState.getBlock()).getMainPos(newState, pos, world, count - 1);
				else return new BlockPos(0, -1, 0);
		}
	}
	
	public static EnumFacing getFacing(IBlockState state)
	{
		return state.getValue(DIRECTION);
	}
	
	public EnumParts getPart(IBlockState state)
	{
		return state.getValue(PART);
	}
	
	public static IBlockState getBlockState(EnumParts parts, EnumFacing direction)
	{
		BlockAlchemiter block = MinestuckBlocks.alchemiter[PART1.getAllowedValues().contains(parts) ? 0 : 1];
		IBlockState state = block.getDefaultState();
		return state.withProperty(block.PART, parts).withProperty(DIRECTION, direction);
	}
	
	public static class BlockAlchemiter2 extends BlockAlchemiter
	{
		public BlockAlchemiter2()
		{
			super(1, PART2);
		}
		
		@Override
		protected BlockStateContainer createBlockState()
		{
			return new BlockStateContainer(this, PART2, DIRECTION);
		}
	}
	
	
	public static BlockAlchemiter[] createBlocks()
	{
		return new BlockAlchemiter[] {new BlockAlchemiter(), new BlockAlchemiter2()};
	}
	
	public enum EnumParts implements IStringSerializable
	{
		TOTEM_CORNER(new AxisAlignedBB(0.0D,0.0D,0.0D,1.0D,1.0D,1.0D)),
		TOTEM_PAD(   new AxisAlignedBB(8/16D,0.0D,2/16D,14/16D,1.0D,1.0D),new AxisAlignedBB(0.0D,0.0D,8/16D,14/16D,1.0D,14/16D),
				     new AxisAlignedBB(2/16D,0.0D,0.0D,8/16D,1.0D,14/16D),new AxisAlignedBB(2/16D,0.0D,2/16D,1.0D,1.0D,8/16D)),
		LOWER_ROD(   new AxisAlignedBB(10/16D,0.0D,2/16D,14/16D,1.0D,1.0D),new AxisAlignedBB(0.0D,0.0D,10/16D,14/16D,1.0D,14/16D),
				     new AxisAlignedBB(2/16D,0.0D,0.0D,6/16D,1.0D,14/16D),new AxisAlignedBB(2/16D,0.0D,2/16D,1.0D,1.0D,6/16D)),
		UPPER_ROD(   new AxisAlignedBB(7/16D,0.0D,2/16D,14/16D,10/16D,1.0D),new AxisAlignedBB(0.0D,0.0D,7/16D,14/16D,10/16D,14/16D),
				     new AxisAlignedBB(2/16D,0.0D,0.0D,9/16D,10/16D,14/16D),new AxisAlignedBB(2/16D,0.0D,2/16D,1.0D,10/16D,9/16D)),
		
		SIDE_LEFT(   new AxisAlignedBB(0.0D,0.0D,0.0D,1.0D,1.0D,1.0D)),
		SIDE_RIGHT(  new AxisAlignedBB(0.0D,0.0D,0.0D,1.0D,1.0D,1.0D)),
		CORNER(      new AxisAlignedBB(0.0D,0.0D,0.0D,1.0D,1.0D,1.0D)),
		CENTER_PAD(  new AxisAlignedBB(0.0D,0.0D,0.0D,1.0D,1.0D,1.0D)),
		TOTEM_PAD_DOWEL(new AxisAlignedBB(6.5/16D,0.0D,2/16D,14/16D,1.0D,13/16D),new AxisAlignedBB(3/16D,0.0D,6.5/16D,14/16D,1.0D,14/16D),
				new AxisAlignedBB(2/16D,0.0D,3/16D,9.5/16D,1.0D,14/16D),new AxisAlignedBB(2/16D,0.0D,2/16D,13/16D,1.0D,9.5/16D)),
		TOTEM_PAD_TOTEM(new AxisAlignedBB(6.5/16D,0.0D,2/16D,14/16D,1.0D,13/16D),new AxisAlignedBB(3/16D,0.0D,6.5/16D,14/16D,1.0D,14/16D),
				     new AxisAlignedBB(2/16D,0.0D,3/16D,9.5/16D,1.0D,14/16D),new AxisAlignedBB(2/16D,0.0D,2/16D,13/16D,1.0D,9.5/16D));
		
		private final AxisAlignedBB[] BOUNDING_BOX;
		
		EnumParts(AxisAlignedBB... bb)
		{
			BOUNDING_BOX = bb;
		}
		
		public AxisAlignedBB getBoundingBox(EnumFacing facing)
		{
			return BOUNDING_BOX[facing.getHorizontalIndex() % BOUNDING_BOX.length];
		}
		
		@Override
		public String toString()
		{
			return getName();
		}
		
		@Override
		public String getName()
		{
			return name().toLowerCase();
		}
		
		public boolean isTotemPad()
		{
			return this == TOTEM_PAD || this == TOTEM_PAD_DOWEL || this == TOTEM_PAD_TOTEM;
		}
	}

	@Override
	public Item getItemFromMachine() 
	{
		
		return new ItemStack(MinestuckBlocks.alchemiter[0]).getItem();
	}
}
package com.mraof.minestuck.block;

import com.mraof.minestuck.tileentity.TileEntityTotemLathe;

import com.mraof.minestuck.util.AlchemyRecipeHandler;
import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTotemLathe extends BlockLargeMachine
{
	
	public static final PropertyEnum<EnumParts> PART1 = PropertyEnum.create("part", EnumParts.class, EnumParts.BOTTOM_LEFT, EnumParts.BOTTOM_MIDLEFT, EnumParts.BOTTOM_MIDRIGHT, EnumParts.BOTTOM_RIGHT);
	public static final PropertyEnum<EnumParts> PART2 = PropertyEnum.create("part", EnumParts.class, EnumParts.MID_LEFT, EnumParts.MID_MIDLEFT, EnumParts.MID_MIDRIGHT, EnumParts.MID_RIGHT);
	public static final PropertyEnum<EnumParts> PART3 = PropertyEnum.create("part", EnumParts.class, EnumParts.TOP_LEFT, EnumParts.TOP_MIDLEFT, EnumParts.TOP_MIDRIGHT);
	public final PropertyEnum<EnumParts> PART;
	public static final PropertyDirection DIRECTION = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyBool HAS_CARD = PropertyBool.create("has_card");
	public static final PropertyEnum<EnumDowel> HAS_DOWEL = PropertyEnum.create("dowel", EnumDowel.class);
	
	private int index;
	
	public BlockTotemLathe()
	{
		this(0, PART1);
	}
	
	public BlockTotemLathe(int index, PropertyEnum<EnumParts> part)
	{
		this.index = index;
		PART = part;
		setUnlocalizedName("totem_lathe");
		
	} 
	//not sure how to do this.
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state,IBlockAccess source,BlockPos pos)
	{
		EnumParts parts = state.getValue(PART);
		EnumFacing facing = state.getValue(DIRECTION);
		
		return parts.BOUNDING_BOX[facing.getHorizontalIndex()];
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(worldIn.isRemote)
			return true;
		
		BlockPos mainPos = getMainPos(state, pos);
		TileEntity te = worldIn.getTileEntity(mainPos);
		if(te instanceof TileEntityTotemLathe)
			((TileEntityTotemLathe) te).onRightClick(playerIn, state);
		return true;
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return state.getValue(PART) == EnumParts.BOTTOM_LEFT;
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		if(index == 0 && meta % 4 == EnumParts.BOTTOM_LEFT.ordinal())
			return new TileEntityTotemLathe();
		else return null;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
	
		BlockPos mainPos = getMainPos(state, pos);
		TileEntity te = worldIn.getTileEntity(mainPos);
		if(te instanceof TileEntityTotemLathe)
		{
			TileEntityTotemLathe lathe = (TileEntityTotemLathe) te;
			lathe.setBroken();
			lathe.dropCard1(true, pos);
			lathe.dropCard2(true, pos);
			lathe.dropDowel(true, pos);

		}
		
		super.breakBlock(worldIn, pos, state);
	}
	
	//Block state handling
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, PART1, DIRECTION, HAS_CARD);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		IBlockState defaultState = getDefaultState();
		EnumParts part = EnumParts.values()[index * 4 + meta / 4];
		EnumFacing facing = EnumFacing.getHorizontal(meta % 4);
		
		return defaultState.withProperty(PART, part).withProperty(DIRECTION, facing);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		EnumParts part = state.getValue(PART);
		EnumFacing facing = state.getValue(DIRECTION);
		return (part.ordinal() % 4) * 4 + facing.getHorizontalIndex();
	}
	
	@Override
	public IBlockState getActualState(IBlockState state,IBlockAccess worldIn,BlockPos pos)
	{
		BlockPos mainPos = getMainPos(state, pos);
		TileEntity te = worldIn.getTileEntity(mainPos);
		
		if(state.getValue(PART) == EnumParts.BOTTOM_LEFT)
		{
			return state.withProperty(HAS_CARD, !((TileEntityTotemLathe) te).getCard1().isEmpty());
		}
		
		if (te instanceof TileEntityTotemLathe && index == 1)
		{
			if(!((TileEntityTotemLathe)te).getDowel().isEmpty())
			{
				if(AlchemyRecipeHandler.getDecodedItem(((TileEntityTotemLathe)te).getDowel()).isEmpty())
				{
					return state.withProperty(HAS_DOWEL, EnumDowel.UNCARVED_DOWEL);
				} else
					{
					return state.withProperty(HAS_DOWEL, EnumDowel.CARVED_DOWEL);
				}
			}
		}
		return state.withProperty(HAS_DOWEL, EnumDowel.NO_DOWEL);
	}
	
    /**
     *returns the block position of the "Main" block
     *aka the block with the TileEntity for the machine
     */
	public BlockPos getMainPos(IBlockState state, BlockPos pos)
	{
		EnumFacing facing = state.getValue(DIRECTION);
		switch(state.getValue(PART))
		{
		
			case BOTTOM_RIGHT:
				return pos.offset(facing.rotateYCCW(),3);
			case BOTTOM_MIDRIGHT:
				return pos.offset(facing.rotateYCCW(),2);
			case BOTTOM_MIDLEFT:
				return pos.offset(facing.rotateYCCW(),1);
			case BOTTOM_LEFT:
				return pos;
			case MID_RIGHT:
				return pos.down(1).offset(facing.rotateYCCW(),3);
			case MID_MIDRIGHT:
				return pos.down(1).offset(facing.rotateYCCW(),2);
			case MID_MIDLEFT:
				return pos.down(1).offset(facing.rotateYCCW(),1);
			case MID_LEFT:
				return pos.down(1);
			case TOP_MIDRIGHT:
				return pos.down(2).offset(facing.rotateYCCW(),2);
			case TOP_MIDLEFT:
				return pos.down(2).offset(facing.rotateYCCW(),1);
			case TOP_LEFT:
				return pos.down(2);
			}
			return pos;
	}
	
	public static EnumParts getPart(IBlockState state)
	{
		if(state.getBlock() instanceof BlockTotemLathe)
			return state.getValue(((BlockTotemLathe) state.getBlock()).PART);
		else return null;
	}
	
	public enum EnumParts implements IStringSerializable
	{
		BOTTOM_LEFT(	new AxisAlignedBB(0/16D, 0/16D, 5/16D, 14/16D, 1.0D, 14/16D),new AxisAlignedBB(2/16D, 0/16D, 0/16D, 11/16D, 1.0D, 14/16D),
						new AxisAlignedBB(2/16D, 0/16D, 2/16D, 16/16D, 1.0D, 11/16D),new AxisAlignedBB(5/16D, 0/16D, 2/16D, 14/16D, 1.0D, 16/16D)),
		BOTTOM_MIDLEFT(	new AxisAlignedBB(0/16D, 0/16D, 5/16D, 16/16D, 1.0D, 14/16D),new AxisAlignedBB(2/16D, 0/16D, 0/16D, 11/16D, 1.0D, 16/16D),
						new AxisAlignedBB(0/16D, 0/16D, 2/16D, 16/16D, 1.0D, 11/16D),new AxisAlignedBB(5/16D, 0/16D, 0/16D, 14/16D, 1.0D, 16/16D)),
		BOTTOM_MIDRIGHT(new AxisAlignedBB(0/16D, 0/16D, 5/16D, 16/16D, 1.0D, 14/16D),new AxisAlignedBB(2/16D, 0/16D, 0/16D, 11/16D, 1.0D, 16/16D),
						new AxisAlignedBB(0/16D, 0/16D, 2/16D, 16/16D, 1.0D, 11/16D),new AxisAlignedBB(5/16D, 0/16D, 0/16D, 14/16D, 1.0D, 16/16D)),
		BOTTOM_RIGHT(	new AxisAlignedBB(6/16D, 0/16D, 5/16D, 16/16D, 1.0D, 14/16D),new AxisAlignedBB(2/16D, 0/16D, 6/16D, 11/16D, 1.0D, 16/16D),
						new AxisAlignedBB(0/16D, 0/16D, 2/16D, 10/16D, 1.0D, 11/16D),new AxisAlignedBB(5/16D, 0/16D, 0/16D, 14/16D, 1.0D, 10/16D)),
		
		MID_LEFT(		new AxisAlignedBB(0/16D, 0/16D,  5/16D, 14/16D,  1.0D, 14/16D),new AxisAlignedBB(2/16D,  0/16D, 0/16D, 11/16D,  1.0D, 14/16D),
						new AxisAlignedBB(2/16D, 0/16D,  2/16D, 16/16D,  1.0D, 11/16D),new AxisAlignedBB(5/16D,  0/16D, 2/16D, 14/16D,  1.0D, 16/16D)),
		MID_MIDLEFT(	new AxisAlignedBB(0/16D, 0/16D,  5/16D, 16/16D,  1.0D, 14/16D),new AxisAlignedBB(2/16D,  0/16D, 0/16D, 11/16D,  1.0D, 16/16D),
						new AxisAlignedBB(0/16D, 0/16D,  2/16D, 16/16D,  1.0D, 11/16D),new AxisAlignedBB(5/16D,  0/16D, 0/16D, 14/16D,  1.0D, 16/16D)),
		MID_MIDRIGHT(	new AxisAlignedBB(9/16D, 12/16D, 7/16D, 16/16D,  1.0D, 12/16D),new AxisAlignedBB(4/16D, 12/16D, 9/16D,  9/16D,  1.0D, 16/16D),
						new AxisAlignedBB(0/16D, 12/16D, 4/16D,  7/16D,  1.0D,  9/16D),new AxisAlignedBB(7/16D, 12/16D, 0/16D, 12/16D,  1.0D,  7/16D)),
		MID_RIGHT(		new AxisAlignedBB(1/16D, 0/16D,  7/16D, 14/16D, 4/16D, 12/16D),new AxisAlignedBB(4/16D,  0/16D, 1/16D,  9/16D, 4/16D, 14/16D),
						new AxisAlignedBB(2/16D, 0/16D,  4/16D, 15/16D, 4/16D,  9/16D),new AxisAlignedBB(7/16D,  0/16D, 2/16D, 12/16D, 4/16D, 15/16D)),
		
		TOP_MIDRIGHT(	new AxisAlignedBB(8/16D, 0/16D, 6/16D, 16/16D,  12/16D, 14/16D),new AxisAlignedBB(2/16D,  0/16D, 8/16D, 10/16D,  12/16D, 16/16D),
					 	new AxisAlignedBB(0/16D, 0/16D, 2/16D,  8/16D,  12/16D, 10/16D),new AxisAlignedBB(6/16D,  0/16D, 0/16D, 14/16D,  12/16D,  8/16D)),
		TOP_MIDLEFT(	new AxisAlignedBB(0/16D, 0/16D, 6/16D, 16/16D,  12/16D, 14/16D),new AxisAlignedBB(2/16D,  0/16D, 0/16D, 10/16D,  12/16D, 16/16D),
						new AxisAlignedBB(0/16D, 0/16D, 2/16D, 16/16D,  12/16D, 10/16D),new AxisAlignedBB(6/16D,  0/16D, 0/16D, 14/16D,  12/16D, 16/16D)),
		TOP_LEFT(		new AxisAlignedBB(0/16D, 0/16D, 6/16D, 16/16D,  12/16D, 14/16D),new AxisAlignedBB(2/16D,  0/16D, 0/16D, 10/16D,  12/16D, 16/16D),
						new AxisAlignedBB(0/16D, 0/16D, 2/16D, 16/16D,  12/16D, 10/16D),new AxisAlignedBB(6/16D,  0/16D, 0/16D, 14/16D,  12/16D, 16/16D));
		
		private final AxisAlignedBB[] BOUNDING_BOX;
		
		EnumParts(AxisAlignedBB... bb)
		{
			BOUNDING_BOX = bb;
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
	}
	
	public enum EnumDowel implements IStringSerializable
	{
		NO_DOWEL,
		UNCARVED_DOWEL,
		CARVED_DOWEL;
		
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
	}
	
	public static Block[] createBlocks()
	{
		return new Block[] {new BlockTotemLathe().setRegistryName("totem_lathe"), new BlockTotemLathe2().setRegistryName("totem_lathe2"), new BlockTotemLathe3().setRegistryName("totem_lathe3")};
	}
	
	private static class BlockTotemLathe2 extends BlockTotemLathe
	{
		public BlockTotemLathe2()
		{
			super(1, PART2);
		}
		
		@Override
		protected BlockStateContainer createBlockState()
		{
			return new BlockStateContainer(this, PART2, DIRECTION, HAS_DOWEL);
		}
	}
	
	private static class BlockTotemLathe3 extends BlockTotemLathe
	{
		public BlockTotemLathe3()
		{
			super(2, PART3);
		}
		
		@Override
		protected BlockStateContainer createBlockState()
		{
			return new BlockStateContainer(this, PART3, DIRECTION);
		}
	}
}
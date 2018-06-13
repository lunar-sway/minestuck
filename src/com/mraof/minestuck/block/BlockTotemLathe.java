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
	
	public static final PropertyEnum<EnumParts> PART1 = PropertyEnum.create("part", EnumParts.class, EnumParts.BOTTOM_LEFT, EnumParts.BOTTOM_MIDLEFT, EnumParts.BOTTOM_MIDRIGHT, EnumParts.BOTTOM_RIGHT, EnumParts.BOTTOM_LEFT_CARD_1, EnumParts.BOTTOM_LEFT_CARD_2);
	public static final PropertyEnum<EnumParts> PART2 = PropertyEnum.create("part", EnumParts.class, EnumParts.MID_LEFT, EnumParts.MID_MIDLEFT, EnumParts.MID_MIDRIGHT, EnumParts.MID_RIGHT);
	public static final PropertyEnum<EnumParts> PART3 = PropertyEnum.create("part", EnumParts.class, EnumParts.TOP_LEFT, EnumParts.TOP_MIDLEFT, EnumParts.TOP_MIDRIGHT);
	public final PropertyEnum<EnumParts> PART;
	public static final PropertyDirection DIRECTION = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
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
		return state.getValue(PART).isBottomLeft();
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		if(meta / 4 + index * 4 == EnumParts.BOTTOM_LEFT.ordinal())
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
		return new BlockStateContainer(this, PART1, DIRECTION);
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
		if(part.isBottomLeft())
			part = EnumParts.BOTTOM_LEFT;
		EnumFacing facing = state.getValue(DIRECTION);
		return (part.ordinal() % 4) * 4 + facing.getHorizontalIndex();
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		BlockPos mainPos = getMainPos(state, pos);
		TileEntity te = worldIn.getTileEntity(mainPos);
		
		if(te instanceof TileEntityTotemLathe)
		{
			TileEntityTotemLathe lathe = (TileEntityTotemLathe) te;
			if(state.getValue(PART).isBottomLeft())
			{
				if(!lathe.getCard2().isEmpty())
					return state.withProperty(PART, EnumParts.BOTTOM_LEFT_CARD_2);
				else if(!lathe.getCard1().isEmpty())
					return state.withProperty(PART, EnumParts.BOTTOM_LEFT_CARD_1);
			} else if(index == 1)
			{
				if(!lathe.getDowel().isEmpty())
				{
					if(AlchemyRecipeHandler.hasDecodedItem(lathe.getDowel()))
					{
						return state.withProperty(HAS_DOWEL, EnumDowel.CARVED_DOWEL);
					} else
					{
						return state.withProperty(HAS_DOWEL, EnumDowel.UNCARVED_DOWEL);
					}
				} else return state.withProperty(HAS_DOWEL, EnumDowel.NO_DOWEL);
			}
		}
		
		return state;
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
				return pos.offset(facing.rotateY(),3);
			case BOTTOM_MIDRIGHT:
				return pos.offset(facing.rotateY(),2);
			case BOTTOM_MIDLEFT:
				return pos.offset(facing.rotateY(),1);
			case BOTTOM_LEFT: case BOTTOM_LEFT_CARD_1: case BOTTOM_LEFT_CARD_2:
				return pos;
			case MID_RIGHT:
				return pos.down(1).offset(facing.rotateY(),3);
			case MID_MIDRIGHT:
				return pos.down(1).offset(facing.rotateY(),2);
			case MID_MIDLEFT:
				return pos.down(1).offset(facing.rotateY(),1);
			case MID_LEFT:
				return pos.down(1);
			case TOP_MIDRIGHT:
				return pos.down(2).offset(facing.rotateY(),2);
			case TOP_MIDLEFT:
				return pos.down(2).offset(facing.rotateY(),1);
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
	
	public static IBlockState getState(EnumParts parts, EnumFacing facing)
	{
		BlockTotemLathe block = MinestuckBlocks.totemlathe[PART1.getAllowedValues().contains(parts) ? 0 : PART2.getAllowedValues().contains(parts) ? 1 : 2];
		return block.getDefaultState().withProperty(block.PART, parts).withProperty(DIRECTION, facing);
	}
	
	public enum EnumParts implements IStringSerializable
	{
		BOTTOM_LEFT(	new AxisAlignedBB(0.0D, 0.0D, 1/16D, 1.0D, 1.0D, 13/16D)),
		BOTTOM_MIDLEFT(	new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 12/16D)),
		BOTTOM_MIDRIGHT(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 12/16D, 12/16D)),
		BOTTOM_RIGHT(	new AxisAlignedBB(0.0D, 0.0D, 1/16D, 12/16D, 1.0D, 11/16D)),
		
		MID_LEFT(		new AxisAlignedBB(0.0D, 0.0D,  1/16D, 1.0D,  1.0D, 12/16D)),
		MID_MIDLEFT(	new AxisAlignedBB(0/16D, 0/16D,  5/16D, 16/16D,  1.0D, 14/16D)),
		MID_MIDRIGHT(	new AxisAlignedBB(9/16D, 12/16D, 7/16D, 16/16D,  1.0D, 12/16D)),
		MID_RIGHT(		new AxisAlignedBB(1/16D, 0/16D,  7/16D, 14/16D, 4/16D, 12/16D)),
		
		TOP_MIDRIGHT(	new AxisAlignedBB(8/16D, 0/16D, 6/16D, 16/16D,  12/16D, 14/16D)),
		TOP_MIDLEFT(	new AxisAlignedBB(0/16D, 0/16D, 6/16D, 16/16D,  12/16D, 14/16D)),
		TOP_LEFT(		new AxisAlignedBB(0/16D, 0/16D, 6/16D, 16/16D,  12/16D, 14/16D)),
		
		BOTTOM_LEFT_CARD_1(new AxisAlignedBB(0.0D, 0.0D, 1/16D, 1.0D, 1.0D, 13/16D)),
		BOTTOM_LEFT_CARD_2(new AxisAlignedBB(0.0D, 0.0D, 1/16D, 1.0D, 1.0D, 13/16D));
		
		private final AxisAlignedBB[] BOUNDING_BOX;
		
		EnumParts(AxisAlignedBB bb)
		{
			BOUNDING_BOX = new AxisAlignedBB[4];
			BOUNDING_BOX[0] = bb;
			BOUNDING_BOX[1] = new AxisAlignedBB(1 - bb.maxZ, bb.minY, bb.minX, 1 - bb.minZ, bb.maxY, bb.maxX);
			BOUNDING_BOX[2] = new AxisAlignedBB(1 - bb.maxX, bb.minY, 1- bb.maxZ, 1 - bb.minX, bb.maxY, 1 - bb.minZ);
			BOUNDING_BOX[3] = new AxisAlignedBB(bb.minZ, bb.minY, 1 - bb.maxX, bb.maxZ, bb.maxY, 1 - bb.minX);
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
		
		public boolean isBottomLeft()
		{
			return this == BOTTOM_LEFT || this == BOTTOM_LEFT_CARD_1 || this == BOTTOM_LEFT_CARD_2;
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
	
	public static BlockTotemLathe[] createBlocks()
	{
		return new BlockTotemLathe[] {new BlockTotemLathe(), new BlockTotemLathe2(), new BlockTotemLathe3()};
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
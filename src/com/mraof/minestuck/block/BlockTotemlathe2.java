package com.mraof.minestuck.block;


import com.mraof.minestuck.tileentity.TileEntityTotemlathe;
import com.mraof.minestuck.util.AlchemyRecipeHandler;

import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockTotemlathe2 extends BlockTotemlathe {

	public static final PropertyEnum<EnumParts> PART = PropertyEnum.create("part", EnumParts.class);
	public static final PropertyDirection DIRECTION = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyEnum<EnumDowel> HAS_DOWEL = PropertyEnum.create("dowel",EnumDowel.class);
	
	public BlockTotemlathe2(){
		setUnlocalizedName("totem_lathe2");
		setDefaultState(blockState.getBaseState().withProperty(HAS_DOWEL, EnumDowel.NO_DOWEL));
	}
	
	
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, PART, DIRECTION, HAS_DOWEL);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		IBlockState defaultState = getDefaultState();
		EnumParts part = EnumParts.values()[meta % 4];
		EnumFacing facing = EnumFacing.getHorizontal(meta/4);
		
		return defaultState.withProperty(PART, part).withProperty(DIRECTION, facing);
	}
	@Override
	public int getMetaFromState(IBlockState state)
	{
		EnumParts part = state.getValue(PART);
		EnumFacing facing = state.getValue(DIRECTION);
		return part.ordinal() + facing.getHorizontalIndex()*4;
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
			case MID_RIGHT:return pos.down(1).offset(facing.rotateYCCW(),3);
			case MID_MIDRIGHT:return pos.down(1).offset(facing.rotateYCCW(),2);
			case MID_MIDLEFT:return pos.down(1).offset(facing.rotateYCCW(),1);
			case MID_LEFT:return pos.down(1);
			}
			return pos;
	}
	
	@Override
	public IBlockState getActualState(IBlockState state,IBlockAccess worldIn,BlockPos pos) {
		BlockPos mainPos = getMainPos(state, pos);
		TileEntity te = worldIn.getTileEntity(mainPos);
		if (te instanceof TileEntityTotemlathe&&state.getBlock()==MinestuckBlocks.totemlathe2 ) {
			if(!((TileEntityTotemlathe)te).getDowel().isEmpty()) {				
				if(AlchemyRecipeHandler.getDecodedItem(((TileEntityTotemlathe)te).getDowel()).isEmpty()){
					return state.withProperty(HAS_DOWEL, EnumDowel.UNCARVED_DOWEL);
				}else {
					return state.withProperty(HAS_DOWEL, EnumDowel.CARVED_DOWEL);
				}
			}
		}
		return state.withProperty(HAS_DOWEL,EnumDowel.NO_DOWEL);
	}

	public static enum EnumDowel implements IStringSerializable
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
	
	public static enum EnumParts implements IStringSerializable
	{
		MID_RIGHT(	 new AxisAlignedBB(1/16D, 0/16D,  7/16D, 14/16D, 4/16D, 12/16D),new AxisAlignedBB(4/16D,  0/16D, 1/16D,  9/16D, 4/16D, 14/16D),
					 new AxisAlignedBB(2/16D, 0/16D,  4/16D, 15/16D, 4/16D,  9/16D),new AxisAlignedBB(7/16D,  0/16D, 2/16D, 12/16D, 4/16D, 15/16D)),
		MID_MIDRIGHT(new AxisAlignedBB(9/16D, 12/16D, 7/16D, 16/16D,  1.0D, 12/16D),new AxisAlignedBB(4/16D, 12/16D, 9/16D,  9/16D,  1.0D, 16/16D),
					 new AxisAlignedBB(0/16D, 12/16D, 4/16D,  7/16D,  1.0D,  9/16D),new AxisAlignedBB(7/16D, 12/16D, 0/16D, 12/16D,  1.0D,  7/16D)),
		MID_MIDLEFT( new AxisAlignedBB(0/16D, 0/16D,  5/16D, 16/16D,  1.0D, 14/16D),new AxisAlignedBB(2/16D,  0/16D, 0/16D, 11/16D,  1.0D, 16/16D),
					 new AxisAlignedBB(0/16D, 0/16D,  2/16D, 16/16D,  1.0D, 11/16D),new AxisAlignedBB(5/16D,  0/16D, 0/16D, 14/16D,  1.0D, 16/16D)),
		MID_LEFT(	 new AxisAlignedBB(0/16D, 0/16D,  5/16D, 14/16D,  1.0D, 14/16D),new AxisAlignedBB(2/16D,  0/16D, 0/16D, 11/16D,  1.0D, 14/16D),
					 new AxisAlignedBB(2/16D, 0/16D,  2/16D, 16/16D,  1.0D, 11/16D),new AxisAlignedBB(5/16D,  0/16D, 2/16D, 14/16D,  1.0D, 16/16D));
		
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

		public AxisAlignedBB getBoundingBox(int i) {
			return BOUNDING_BOX[i];
		}
	}
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return false;
	}

}

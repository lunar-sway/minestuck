package com.mraof.minestuck.block;


import com.mraof.minestuck.tileentity.TileEntityTotemlathe;
import com.mraof.minestuck.util.AlchemyRecipeHandler;

import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockTotemlathe2 extends BlockTotemlathe {

	public static final PropertyEnum<EnumParts> PART = PropertyEnum.create("part", EnumParts.class);
	public static final PropertyDirection DIRECTION = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyEnum<EnumDowel> HASDOWEL = PropertyEnum.create("hasdowel",EnumDowel.class);
	
	public BlockTotemlathe2(){
		setUnlocalizedName("totem_lathe2");
	}
	
	
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, PART, DIRECTION,HASDOWEL);
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
					return state.withProperty(HASDOWEL, EnumDowel.UNCARVED_DOWEL);
				}else {
					return state.withProperty(HASDOWEL, EnumDowel.CARVED_DOWEL);
				}
			}
		}
		return state.withProperty(HASDOWEL,EnumDowel.NO_DOWEL);	
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
		//(new AxisAlignedBB(5/16D, 0.0D, 0.0D, 1.0D, 1.0D, 11/16D), new AxisAlignedBB(5/16D, 0.0D, 5/16D, 1.0D, 1.0D, 1.0D),
		//new AxisAlignedBB(0.0D, 0.0D, 5/16D, 11/16D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 11/16D, 1.0D, 11/16D)),
		// try out code later to see if i can find out how it works

		MID_RIGHT,
		MID_MIDRIGHT,
		MID_MIDLEFT,
		MID_LEFT;
		
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
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return false;
	}

}

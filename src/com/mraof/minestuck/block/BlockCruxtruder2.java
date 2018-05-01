package com.mraof.minestuck.block;

import com.mraof.minestuck.tileentity.TileEntityCruxtruder;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCruxtruder2 extends BlockCruxtruder {


	public static final PropertyEnum<EnumParts> PART = PropertyEnum.<EnumParts>create("part",EnumParts.class);
	public static final PropertyBool HASLID=PropertyBool.create("haslid");
	public static final PropertyBool DOWELOUT=PropertyBool.create("dowelout");
	BlockCruxtruder2(){
		setUnlocalizedName("cruxtruder2");
		setDefaultState(blockState.getBaseState().withProperty(HASLID, true).withProperty(DOWELOUT, false));
	}
	
	@Override
	protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {PART,HASLID,DOWELOUT});
    }
	
	//Block state handling
	public enum EnumParts implements IStringSerializable
	{
		ONE_ONE_ONE(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)),
		ONE_TWO_ONE(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)),
		ONE_THREE_ONE(new AxisAlignedBB(3/16D, 0.0D, 3/16D, 13/16D, 12/16D, 13/16D));
		
		private final AxisAlignedBB[] BOUNDING_BOX;
		public AxisAlignedBB getBoundingBox(int i){
			return BOUNDING_BOX[i];
		}
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
	public BlockPos getMainPos(IBlockState state, BlockPos pos){
		EnumParts part=state.getValue(PART);
		switch(part){
		case ONE_ONE_ONE: 	return pos.up(2);
		case ONE_TWO_ONE:	return pos.up();
		case ONE_THREE_ONE:	return pos;
		}
		return pos;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{

		if(state.getValue(HASLID)&&state.getValue(PART)==EnumParts.ONE_THREE_ONE) {
			worldIn.setBlockState(pos, state.withProperty(HASLID, false));
			//spawn lid entity???(that dosn't exist yet)
		}else {			
			super.breakBlock(worldIn, pos, state);
		}
	}
	
	@Override
	public IBlockState getActualState(IBlockState state,IBlockAccess worldIn,BlockPos pos) {
		if (state.getValue(PART)==EnumParts.ONE_THREE_ONE ) {
			BlockPos mainPos = getMainPos(state, pos);
			TileEntity te = worldIn.getTileEntity(mainPos);
			return state.withProperty(DOWELOUT, ((TileEntityCruxtruder)te).IsDowelOut());
		}
		return state;	
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		IBlockState defaultState = getDefaultState();
		EnumParts part = EnumParts.values()[meta%3];
		Boolean haslid=meta/3==1?true:false;
		return defaultState.withProperty(PART, part).withProperty(HASLID, haslid);
	}
	@Override
	public int getMetaFromState(IBlockState state)
	{
		EnumParts part = state.getValue(PART);
		if(state.getValue(HASLID))
			return part.ordinal();
		else
			return part.ordinal()+3;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		if ((meta%3)==EnumParts.ONE_THREE_ONE.ordinal())
				return new TileEntityCruxtruder();
		return null;
	}
}


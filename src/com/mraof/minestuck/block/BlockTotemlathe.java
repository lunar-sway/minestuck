package com.mraof.minestuck.block;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockTotemlathe extends BlockLargeMachine{

	public static final PropertyEnum<enumParts> PART = PropertyEnum.<enumParts>create("part",enumParts.class);

	public BlockTotemlathe() {
		super(1,3,4);
		this.setUnlocalizedName("totemlathe");
		this.setDefaultState(this.getStateFromMeta(0));
		
	} 
	//not sure how to do this.
	//@Override
	//public AxisAlignedBB getBoundingBox(IBlockState state,IBlockAccess source,BlockPos pos){
		
	//}
	protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {PART});
    }
	@Override
	public void onBlockPlacedBy(World worldIn,BlockPos pos,IBlockState state,EntityLivingBase placer, ItemStack stack){
		if(placer!=null && !(worldIn.isRemote)){
			worldIn.setBlockState(pos, state.withProperty(PART, enumParts.BOTTOM_RIGHT));
			worldIn.setBlockState(pos.south(1), state.withProperty(PART, enumParts.BOTTOM_MIDRIGHT));
			worldIn.setBlockState(pos.south(2), state.withProperty(PART, enumParts.BOTTOM_MIDLEFT));
			worldIn.setBlockState(pos.south(3), state.withProperty(PART, enumParts.BOTTOM_LEFT));
			worldIn.setBlockState(pos.up(1), state.withProperty(PART, enumParts.MID_RIGHT));
			worldIn.setBlockState(pos.south(1).up(1), state.withProperty(PART, enumParts.MID_MIDRIGHT));
			worldIn.setBlockState(pos.south(2).up(1), state.withProperty(PART, enumParts.MID_MIDLEFT));
			worldIn.setBlockState(pos.south(3).up(1), state.withProperty(PART, enumParts.MID_LEFT));
			worldIn.setBlockState(pos.south(1).up(2), state.withProperty(PART, enumParts.TOP_MIDRIGHT));
			worldIn.setBlockState(pos.south(2).up(2), state.withProperty(PART, enumParts.TOP_MIDLEFT));
			worldIn.setBlockState(pos.south(3).up(2), state.withProperty(PART, enumParts.TOP_LEFT));
		}
	}
	@Override
	public boolean onBlockActivated(World worldIn,BlockPos pos,IBlockState state,EntityPlayer playerIn,EnumHand hand,EnumFacing facing,float hitX,float hitY,float hitZ){
		if(worldIn.isRemote){
//			playerIn.openGui(Minestuck.instance, GuiHandler.GuiId.MACHINE.ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());

		}
		return true;
	}
	public static enum enumParts implements IStringSerializable
	{
		BOTTOM_LEFT,
		BOTTOM_MIDLEFT,
		BOTTOM_MIDRIGHT,
		BOTTOM_RIGHT,
		MID_LEFT,
		MID_MIDLEFT,
		MID_MIDRIGHT,
		MID_RIGHT,
		TOP_LEFT,
		TOP_MIDLEFT,
		TOP_MIDRIGHT;
		;
		public String toString()
		{
			return this.getName();
		}
		public String getName()
		{
			switch (this){
			case BOTTOM_LEFT:return"bottom_left";
			case BOTTOM_MIDLEFT:return"bottom_midleft";
			case BOTTOM_MIDRIGHT:return"bottom_midright";
			case BOTTOM_RIGHT:return"bottom_right";
			case MID_LEFT:return"mid_left";
			case MID_MIDLEFT:return"mid_midleft";
			case MID_MIDRIGHT:return"mid_midright";
			case MID_RIGHT:return"mid_right";
			case TOP_LEFT:return"top_left";
			case TOP_MIDLEFT:return"top_midleft";
			case TOP_MIDRIGHT:return"top_midright";			
			}
			return "null";
		}
		
		

	}
	@Override
	public IBlockState getStateFromMeta(int meta){
		IBlockState defaultState=this.getDefaultState();
		switch (meta){
		case 0:return(defaultState.withProperty(PART, enumParts.BOTTOM_LEFT));
		case 1:return(defaultState.withProperty(PART, enumParts.BOTTOM_MIDLEFT));
		case 2:return(defaultState.withProperty(PART, enumParts.BOTTOM_MIDRIGHT));
		case 3:return(defaultState.withProperty(PART, enumParts.BOTTOM_RIGHT));
		case 4:return(defaultState.withProperty(PART, enumParts.MID_LEFT));
		case 5:return(defaultState.withProperty(PART, enumParts.MID_MIDLEFT));
		case 6:return(defaultState.withProperty(PART, enumParts.MID_MIDRIGHT));
		case 7:return(defaultState.withProperty(PART, enumParts.MID_RIGHT));
		case 8:return(defaultState.withProperty(PART, enumParts.TOP_LEFT));
		case 9:return(defaultState.withProperty(PART, enumParts.TOP_MIDLEFT));
		case 10:return(defaultState.withProperty(PART, enumParts.TOP_MIDRIGHT));
		}
		return null;
	}
	@Override
	public int getMetaFromState(IBlockState state){
		IBlockState defaultState=this.getDefaultState();
		if(state==defaultState.withProperty(PART,enumParts.BOTTOM_LEFT)){
			return 0;
		}else if(state==defaultState.withProperty(PART,enumParts.BOTTOM_MIDLEFT)){
			return 1;
		}else if(state==defaultState.withProperty(PART,enumParts.BOTTOM_MIDRIGHT)){
			return 2;
		}else if(state==defaultState.withProperty(PART,enumParts.BOTTOM_RIGHT)){
			return 3;
		}else if(state==defaultState.withProperty(PART,enumParts.MID_LEFT)){
			return 4;
		}else if(state==defaultState.withProperty(PART,enumParts.MID_MIDLEFT)){
			return 5;
		}else if(state==defaultState.withProperty(PART,enumParts.MID_MIDRIGHT)){
			return 6;
		}else if(state==defaultState.withProperty(PART,enumParts.MID_RIGHT)){
			return 7;
		}else if(state==defaultState.withProperty(PART,enumParts.TOP_LEFT)){
			return 8;
		}else if(state==defaultState.withProperty(PART,enumParts.TOP_MIDLEFT)){
			return 9;
		}else if(state==defaultState.withProperty(PART,enumParts.TOP_MIDRIGHT)){
			return 10;
		}else{
			return 0;
		}
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
package com.mraof.minestuck.block;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPunchDesignex extends BlockLargeMachine{

	public static final PropertyEnum<enumParts> PART = PropertyEnum.<enumParts>create("part",enumParts.class);

	public BlockPunchDesignex() {
		super(2,2,1);
		this.setUnlocalizedName("punch_designex");
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
	public boolean isOpaqueCube(IBlockState state){
		return false;
	}
	@Override
	public void onBlockPlacedBy(World worldIn,BlockPos pos,IBlockState state,EntityLivingBase placer, ItemStack stack){
		if(placer!=null && !(worldIn.isRemote)){
			worldIn.setBlockState(pos, state.withProperty(PART, enumParts.BOTTOM_LEFT));
			worldIn.setBlockState(pos.east(), state.withProperty(PART, enumParts.BOTTOM_RIGHT));
			worldIn.setBlockState(pos.up(),state.withProperty(PART, enumParts.TOP_LEFT));
			worldIn.setBlockState(pos.up().east(), state.withProperty(PART, enumParts.TOP_RIGHT));
			
		}
	}
	@Override
	public boolean onBlockActivated(World worldIn,BlockPos pos,IBlockState state,EntityPlayer playerIn,EnumHand hand,EnumFacing facing,float hitX,float hitY,float hitZ){
		if(worldIn.isRemote){
		}
		return true;
	}
	public static enum enumParts implements IStringSerializable
	{
		BOTTOM_LEFT,
		BOTTOM_RIGHT,
		TOP_LEFT,
		TOP_RIGHT;
		;
		public String toString()
		{
			return this.getName();
		}
		public String getName()
		{
			//return this == PARTONE ? "partone" : this== PARTTWO? "parttwo": this==PARTTHREE? "partthree" : "partfour" ;
			switch (this){
			case BOTTOM_LEFT:return"bottom_left";
			case BOTTOM_RIGHT: return"bottom_right";
			case TOP_LEFT: return"top_left";
			case TOP_RIGHT: return"top_right";				
			}
			return "null";
		}
		
		

	}
	@Override
	public IBlockState getStateFromMeta(int meta){
		IBlockState defaultState=this.getDefaultState();
		switch (meta){
		case 0:return(defaultState.withProperty(PART, enumParts.BOTTOM_LEFT));
		case 1:return(defaultState.withProperty(PART, enumParts.BOTTOM_RIGHT));
		case 2:return(defaultState.withProperty(PART, enumParts.TOP_LEFT));
		case 3:return(defaultState.withProperty(PART, enumParts.TOP_RIGHT));
		default:return(defaultState.withProperty(PART, enumParts.BOTTOM_LEFT));
		}
	}
	@Override
	public int getMetaFromState(IBlockState state){
		IBlockState defaultState=this.getDefaultState();
		if(state==defaultState.withProperty(PART,enumParts.BOTTOM_LEFT)){
			return 0;
		}else if(state==defaultState.withProperty(PART,enumParts.BOTTOM_RIGHT)){
			return 1;
		}else if(state==defaultState.withProperty(PART,enumParts.TOP_LEFT)){
			return 2;
		}else if(state==defaultState.withProperty(PART,enumParts.TOP_RIGHT)){
			return 3;
		}else{
			return 0;
		}
	}
	

}
package com.mraof.minestuck.block;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.GuiHandler;
import com.mraof.minestuck.tileentity.TileEntityTotemlathe;

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
	public boolean onBlockActivated(World worldIn,BlockPos pos,IBlockState state,EntityPlayer playerIn,EnumHand hand,EnumFacing facing,float hitX,float hitY,float hitZ){
		
		TileEntityTotemlathe te=(TileEntityTotemlathe)worldIn.getTileEntity(pos);
		BlockPos MasterPos=te.GetMasterPos(state);
		if(!worldIn.isRemote && !((TileEntityTotemlathe)worldIn.getTileEntity(MasterPos)).destroyed){
			if(worldIn.getTileEntity(pos)instanceof TileEntityTotemlathe){				
				if(te.isMaster()){
					playerIn.openGui(Minestuck.instance, GuiHandler.GuiId.MACHINE.ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());
					
				}else{
					playerIn.openGui(Minestuck.instance, GuiHandler.GuiId.MACHINE.ordinal(), worldIn, MasterPos.getX(), MasterPos.getY(), MasterPos.getZ());
				}
			}
		}
		return true;
	}
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityTotemlathe(this.getStateFromMeta(meta));
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
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
	
		BlockPos MasterPos=((TileEntityTotemlathe)worldIn.getTileEntity(pos)).GetMasterPos(state);
		TileEntityTotemlathe te = (TileEntityTotemlathe) worldIn.getTileEntity(MasterPos);
		te.destroyed=true;
		InventoryHelper.dropInventoryItems(worldIn, pos, te);
		
		super.breakBlock(worldIn, pos, state);
	}	
	
	public static enum enumParts implements IStringSerializable
	{
		BOTTOM_RIGHT,
		BOTTOM_MIDRIGHT,
		BOTTOM_MIDLEFT,
		BOTTOM_LEFT,
		MID_RIGHT,
		MID_MIDRIGHT,
		MID_MIDLEFT,
		MID_LEFT,
		TOP_MIDRIGHT,
		TOP_MIDLEFT,
		TOP_LEFT;
		;
		public String toString()
		{
			return this.getName();
		}
		public String getName()
		{
			switch (this){
			case BOTTOM_RIGHT:return"bottom_right";
			case BOTTOM_MIDRIGHT:return"bottom_midright";
			case BOTTOM_MIDLEFT:return"bottom_midleft";
			case BOTTOM_LEFT:return"bottom_left";
			case MID_RIGHT:return"mid_right";
			case MID_MIDRIGHT:return"mid_midright";
			case MID_MIDLEFT:return"mid_midleft";
			case MID_LEFT:return"mid_left";
			case TOP_MIDRIGHT:return"top_midright";	
			case TOP_MIDLEFT:return"top_midleft";	
			case TOP_LEFT:return"top_left";	
			}
			return "null";
		}
		
		

	}
	@Override
	public IBlockState getStateFromMeta(int meta){
		IBlockState defaultState=this.getDefaultState();
		switch (meta){
		case 0:return(defaultState.withProperty(PART, enumParts.BOTTOM_RIGHT));
		case 1:return(defaultState.withProperty(PART, enumParts.BOTTOM_MIDRIGHT));
		case 2:return(defaultState.withProperty(PART, enumParts.BOTTOM_MIDLEFT));
		case 3:return(defaultState.withProperty(PART, enumParts.BOTTOM_LEFT));
		case 4:return(defaultState.withProperty(PART, enumParts.MID_RIGHT));
		case 5:return(defaultState.withProperty(PART, enumParts.MID_MIDRIGHT));
		case 6:return(defaultState.withProperty(PART, enumParts.MID_MIDLEFT));
		case 7:return(defaultState.withProperty(PART, enumParts.MID_LEFT));
		case 8:return(defaultState.withProperty(PART, enumParts.TOP_MIDRIGHT));
		case 9:return(defaultState.withProperty(PART, enumParts.TOP_MIDLEFT));
		case 10:return(defaultState.withProperty(PART, enumParts.TOP_LEFT));
		}
		return null;
	}
	@Override
	public int getMetaFromState(IBlockState state){
		enumParts part=state.getValue(PART);
		
		switch (part){
		case BOTTOM_RIGHT:return(0);
		case BOTTOM_MIDRIGHT:return(1);
		case BOTTOM_MIDLEFT:return(2);
		case BOTTOM_LEFT:return(3);
		case MID_RIGHT:return(4);
		case MID_MIDRIGHT:return(5);
		case MID_MIDLEFT:return(6);
		case MID_LEFT:return(7);
		case TOP_MIDRIGHT:return(8);
		case TOP_MIDLEFT:return(9);
		case TOP_LEFT:return(10);
		default:return(0);		
		}
		
	}


	

}
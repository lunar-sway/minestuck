package com.mraof.minestuck.block;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.BlockSburbMachine.MachineType;
import com.mraof.minestuck.client.gui.GuiHandler;
import com.mraof.minestuck.tileentity.TileEntityMachine;
import com.mraof.minestuck.tileentity.TileEntityPunchDesignix;
import com.mraof.minestuck.tileentity.TileEntitySburbMachine;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class BlockPunchDesignix extends BlockLargeMachine{

	public static final PropertyEnum<enumParts> PART = PropertyEnum.<enumParts>create("part",enumParts.class);

	public BlockPunchDesignix() {
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
	public boolean onBlockActivated(World worldIn,BlockPos pos,IBlockState state,EntityPlayer playerIn,EnumHand hand,EnumFacing facing,float hitX,float hitY,float hitZ){
		
		TileEntityPunchDesignix te=(TileEntityPunchDesignix)worldIn.getTileEntity(pos);
		BlockPos MasterPos=te.GetMasterPos(state);
		if(!worldIn.isRemote && !((TileEntityPunchDesignix)worldIn.getTileEntity(MasterPos)).destroyed){
			if(worldIn.getTileEntity(pos)instanceof TileEntityPunchDesignix){				
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
			return new TileEntityPunchDesignix(this.getStateFromMeta(meta));
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
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
	
		BlockPos MasterPos=((TileEntityPunchDesignix)worldIn.getTileEntity(pos)).GetMasterPos(state);
		TileEntityPunchDesignix te = (TileEntityPunchDesignix) worldIn.getTileEntity(MasterPos);
		te.destroyed=true;
		InventoryHelper.dropInventoryItems(worldIn, pos, te);
		
		super.breakBlock(worldIn, pos, state);
	}
	
	

	
	
	
	
	
	
	
	
	
	
	
	//Block state handling
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
		enumParts part=state.getValue(PART);
		switch(part){
		case BOTTOM_LEFT: return 0;
		case BOTTOM_RIGHT:return 1;
		case TOP_LEFT: return 2;
		case TOP_RIGHT:return 3;	
		default:
			return 0;
		
		}
	}


	

}
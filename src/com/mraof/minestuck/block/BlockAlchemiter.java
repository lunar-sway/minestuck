package com.mraof.minestuck.block;

import org.omg.IOP.Encoding;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.GuiHandler;
import com.mraof.minestuck.tileentity.TileEntityAlchemiter;

import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class BlockAlchemiter extends BlockLargeMachine{
	public static final PropertyEnum<EnumParts> PART = PropertyEnum.<EnumParts>create("part",EnumParts.class);
	public static final PropertyDirection DIRECTION = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	public BlockAlchemiter() 
	{
		setUnlocalizedName("alchemiter");
		setDefaultState(getStateFromMeta(0));
		
	} 
	//not sure how to do this.
	//@Override
	//public AxisAlignedBB getBoundingBox(IBlockState state,IBlockAccess source,BlockPos pos){
		
	//}


	@Override
	public boolean onBlockActivated(World worldIn,BlockPos pos,IBlockState state,EntityPlayer playerIn,EnumHand hand,EnumFacing facing,float hitX,float hitY,float hitZ)
	{
		BlockPos mainPos = getMainPos(state, pos);
		TileEntity te = worldIn.getTileEntity(mainPos);
		if(!worldIn.isRemote && te != null && te instanceof TileEntityAlchemiter && !((TileEntityAlchemiter)te).isBroken())
		{
			playerIn.openGui(Minestuck.instance, GuiHandler.GuiId.MACHINE.ordinal(), worldIn, mainPos.getX(), mainPos.getY(), mainPos.getZ());	
		}
		return true;
	}
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return state.getValue(PART) == EnumParts.ZERO_ONE_ZERO;
	}
	
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		if(meta % 4 == EnumParts.ZERO_ONE_ZERO.ordinal())
			return new TileEntityAlchemiter();
		return null;
	}
	
	////////////////////////////vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
	@Override
	public void onBlockPlacedBy(World worldIn,BlockPos pos,IBlockState state,EntityLivingBase placer, ItemStack stack)
	{
		EnumFacing facing = EnumFacing.getHorizontal(MathHelper.floor((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3);
		state=state.withProperty(DIRECTION, facing);
		IBlockState state2=MinestuckBlocks.alchemiter2.getDefaultState().withProperty(DIRECTION, facing);
		if(placer!=null && !(worldIn.isRemote)){
			worldIn.setBlockState(pos.offset(facing,0).up(0).offset(facing.rotateY(),0), state.withProperty(PART, EnumParts.ZERO_ONE_ZERO));			
			worldIn.setBlockState(pos.offset(facing,0).up(1).offset(facing.rotateY(),0), state.withProperty(PART, EnumParts.ZERO_TWO_ZERO));
			worldIn.setBlockState(pos.offset(facing,0).up(2).offset(facing.rotateY(),0), state.withProperty(PART, EnumParts.ZERO_THREE_ZERO));
			worldIn.setBlockState(pos.offset(facing,0).up(3).offset(facing.rotateY(),0), state.withProperty(PART, EnumParts.ZERO_FOUR_ZERO));
			
			
			worldIn.setBlockState(pos.offset(facing,0).up(0).offset(facing.rotateY(),1), state2.withProperty(BlockAlchemiter2.PART, BlockAlchemiter2.EnumParts.ZERO_ONE_ONE));
			worldIn.setBlockState(pos.offset(facing,0).up(0).offset(facing.rotateY(),2), state2.withProperty(BlockAlchemiter2.PART, BlockAlchemiter2.EnumParts.ZERO_ONE_TWO));
			worldIn.setBlockState(pos.offset(facing,0).up(0).offset(facing.rotateY(),3), state2.withProperty(BlockAlchemiter2.PART, BlockAlchemiter2.EnumParts.ZERO_ONE_THREE));			
			worldIn.setBlockState(pos.offset(facing,1).up(0).offset(facing.rotateY(),1), state2.withProperty(BlockAlchemiter2.PART, BlockAlchemiter2.EnumParts.ONE_ONE_ONE));
			worldIn.setBlockState(pos.offset(facing,1).up(0).offset(facing.rotateY(),0), state2.withProperty(BlockAlchemiter2.PART, BlockAlchemiter2.EnumParts.ZERO_ONE_TWO).withProperty(DIRECTION, facing.rotateY()));
			worldIn.setBlockState(pos.offset(facing,1).up(0).offset(facing.rotateY(),2), state2.withProperty(BlockAlchemiter2.PART, BlockAlchemiter2.EnumParts.ONE_ONE_ONE).withProperty(DIRECTION, facing.rotateYCCW() ));
			worldIn.setBlockState(pos.offset(facing,1).up(0).offset(facing.rotateY(),3), state2.withProperty(BlockAlchemiter2.PART, BlockAlchemiter2.EnumParts.ZERO_ONE_ONE).withProperty(DIRECTION, facing.rotateYCCW() ));
			worldIn.setBlockState(pos.offset(facing,2).up(0).offset(facing.rotateY(),0), state2.withProperty(BlockAlchemiter2.PART, BlockAlchemiter2.EnumParts.ZERO_ONE_ONE).withProperty(DIRECTION, facing.rotateY() ));
			worldIn.setBlockState(pos.offset(facing,2).up(0).offset(facing.rotateY(),1), state2.withProperty(BlockAlchemiter2.PART, BlockAlchemiter2.EnumParts.ONE_ONE_ONE).withProperty(DIRECTION, facing.rotateY() ));
			worldIn.setBlockState(pos.offset(facing,2).up(0).offset(facing.rotateY(),2), state2.withProperty(BlockAlchemiter2.PART, BlockAlchemiter2.EnumParts.ONE_ONE_ONE).withProperty(DIRECTION, facing.rotateY().rotateY() ));
			worldIn.setBlockState(pos.offset(facing,2).up(0).offset(facing.rotateY(),3), state2.withProperty(BlockAlchemiter2.PART, BlockAlchemiter2.EnumParts.ZERO_ONE_TWO).withProperty(DIRECTION, facing.rotateYCCW()));
			worldIn.setBlockState(pos.offset(facing,3).up(0).offset(facing.rotateY(),0), state2.withProperty(BlockAlchemiter2.PART, BlockAlchemiter2.EnumParts.ZERO_ONE_THREE).withProperty(DIRECTION, facing.rotateY().rotateY()));
			worldIn.setBlockState(pos.offset(facing,3).up(0).offset(facing.rotateY(),1), state2.withProperty(BlockAlchemiter2.PART, BlockAlchemiter2.EnumParts.ZERO_ONE_TWO).withProperty(DIRECTION, facing.rotateY().rotateY()));
			worldIn.setBlockState(pos.offset(facing,3).up(0).offset(facing.rotateY(),2), state2.withProperty(BlockAlchemiter2.PART, BlockAlchemiter2.EnumParts.ZERO_ONE_ONE).withProperty(DIRECTION, facing.rotateY().rotateY() ));
			worldIn.setBlockState(pos.offset(facing,3).up(0).offset(facing.rotateY(),3), state2.withProperty(BlockAlchemiter2.PART, BlockAlchemiter2.EnumParts.ZERO_ONE_THREE).withProperty(DIRECTION, facing.rotateYCCW()));

			
		}
	}
	///////////////////////////////^^^^^^^^^^^^^^^^^^^^^^^^^^^
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		BlockPos mainPos=getMainPos(state,pos);
		TileEntity te = worldIn.getTileEntity(mainPos);
		if (te instanceof TileEntityAlchemiter)
		{
			TileEntityAlchemiter alchemiter = (TileEntityAlchemiter) te;
			alchemiter.Break();
			InventoryHelper.dropInventoryItems(worldIn, pos, alchemiter);
		}	
		super.breakBlock(worldIn, pos, state);
	}
	
	

	
	
	
	
	
	
	
	
	
	
	
	//Block state handling

	protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this,PART,DIRECTION);
    }
	
	@Override
	public IBlockState getStateFromMeta(int meta){
		IBlockState defaultState=getDefaultState();		
		EnumParts part = EnumParts.values()[meta % 4];
		EnumFacing facing = EnumFacing.getHorizontal(meta/4);
		

		return defaultState.withProperty(PART, part).withProperty(DIRECTION, facing);
	}
	public int getMetaFromState(IBlockState state){
		EnumParts part = state.getValue(PART);
		EnumFacing facing = state.getValue(DIRECTION);
		return part.ordinal()+facing.getHorizontalIndex()*4;
	}
	
    /**
     *returns the block position of the "Main" block
     *aka the block with the TileEntity for the machine
     */
	public BlockPos getMainPos(IBlockState state, BlockPos pos){
		EnumParts part=state.getValue(BlockAlchemiter.PART);
		switch(part){
		case ZERO_ONE_ZERO: return pos.north(0).down(0).east(0);
		case ZERO_TWO_ZERO:	return pos.north(0).down(1).east(0);
		case ZERO_THREE_ZERO:return pos.north(0).down(2).east(0);
		case ZERO_FOUR_ZERO:return pos.north(0).down(3).east(0);

		
		}
		return pos;
	}
	public static enum EnumParts implements IStringSerializable
	{
		ZERO_ONE_ZERO,	
		ZERO_TWO_ZERO,
		ZERO_THREE_ZERO,
		ZERO_FOUR_ZERO;
		


	
		
		public String toString()
		{
			return getName();
		}
		public String getName()
		{
			return name().toLowerCase();
		}
	}
}
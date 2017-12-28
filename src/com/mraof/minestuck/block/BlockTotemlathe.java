package com.mraof.minestuck.block;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.GuiHandler;
import com.mraof.minestuck.tileentity.TileEntityTotemlathe;
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

public class BlockTotemlathe extends BlockLargeMachine
{
	
	public static final PropertyEnum<EnumParts> PART = PropertyEnum.create("part", EnumParts.class);
	public static final PropertyDirection DIRECTION = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	public BlockTotemlathe() {
		setUnlocalizedName("totem_lathe");
		setDefaultState(blockState.getBaseState());
		
	} 
	//not sure how to do this.
	//@Override
	//public AxisAlignedBB getBoundingBox(IBlockState state,IBlockAccess source,BlockPos pos){
		
	//}
	

	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		BlockPos mainPos = getMainPos(state, pos);
		TileEntity te = worldIn.getTileEntity(mainPos);
		if(!worldIn.isRemote && te != null && te instanceof TileEntityTotemlathe && !((TileEntityTotemlathe)te).isBroken())
		{
			playerIn.openGui(Minestuck.instance, GuiHandler.GuiId.MACHINE.ordinal(), worldIn, mainPos.getX(), mainPos.getY(), mainPos.getZ());
		}
		return true;
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return state.getValue(PART) == EnumParts.TOP_LEFT;
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		if(meta % 11 == EnumParts.TOP_LEFT.ordinal())
			return new TileEntityTotemlathe();
		else return null;
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		
		EnumFacing facing = EnumFacing.getHorizontal(MathHelper.floor((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) + 2 & 3).getOpposite();
		state = state.withProperty(DIRECTION, facing);
		
		if(!(worldIn.isRemote)){
			worldIn.setBlockState(pos, state.withProperty(PART, EnumParts.BOTTOM_RIGHT));
			worldIn.setBlockState(pos.offset(facing.rotateYCCW(),1), state.withProperty(PART, EnumParts.BOTTOM_MIDRIGHT));
			worldIn.setBlockState(pos.offset(facing.rotateYCCW(),2), state.withProperty(PART, EnumParts.BOTTOM_MIDLEFT));
			worldIn.setBlockState(pos.offset(facing.rotateYCCW(),3), state.withProperty(PART, EnumParts.BOTTOM_LEFT));
			worldIn.setBlockState(pos.up(1), state.withProperty(PART, EnumParts.MID_RIGHT));
			worldIn.setBlockState(pos.offset(facing.rotateYCCW(),1).up(1), state.withProperty(PART, EnumParts.MID_MIDRIGHT));
			worldIn.setBlockState(pos.offset(facing.rotateYCCW(),2).up(1), state.withProperty(PART, EnumParts.MID_MIDLEFT));
			worldIn.setBlockState(pos.offset(facing.rotateYCCW(),3).up(1), state.withProperty(PART, EnumParts.MID_LEFT));
			worldIn.setBlockState(pos.offset(facing.rotateYCCW(),1).up(2), state.withProperty(PART, EnumParts.TOP_MIDRIGHT));
			worldIn.setBlockState(pos.offset(facing.rotateYCCW(),2).up(2), state.withProperty(PART, EnumParts.TOP_MIDLEFT));
			worldIn.setBlockState(pos.offset(facing.rotateYCCW(),3).up(2), state.withProperty(PART, EnumParts.TOP_LEFT));
		}
	}
	

	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
	
		BlockPos mainPos = getMainPos(state, pos);
		TileEntity te = worldIn.getTileEntity(mainPos);
		if(te != null && te instanceof TileEntityTotemlathe)
		{
			TileEntityTotemlathe designix = (TileEntityTotemlathe) te;
			designix.Brake();
			InventoryHelper.dropInventoryItems(worldIn, pos, designix);
		}
		
		super.breakBlock(worldIn, pos, state);
	}
	
	
	
	
	
	//Block state handling
	
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, PART, DIRECTION);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		IBlockState defaultState = getDefaultState();
		EnumParts part = EnumParts.values()[meta % 11];
		EnumFacing facing = EnumFacing.getHorizontal(meta/11);
		
		return defaultState.withProperty(PART, part).withProperty(DIRECTION, facing);
	}
	@Override
	public int getMetaFromState(IBlockState state)
	{
		EnumParts part = state.getValue(PART);
		EnumFacing facing = state.getValue(DIRECTION);
		return part.ordinal();
//		return part.ordinal() + facing.getHorizontalIndex()*11;
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
			case BOTTOM_RIGHT:return pos;
			case BOTTOM_MIDRIGHT:return pos.offset(facing.rotateY());
			case BOTTOM_MIDLEFT:return pos.offset(facing.rotateY(),2);
			case BOTTOM_LEFT:return pos.offset(facing.rotateY(),3);
			case MID_RIGHT:return pos.down(1);
			case MID_MIDRIGHT:return pos.down(1).offset(facing.rotateY(),1);
			case MID_MIDLEFT:return pos.down(1).offset(facing.rotateY(),2);
			case MID_LEFT:return pos.down(1).offset(facing.rotateY(),3);
			case TOP_MIDRIGHT:return pos.down(2).offset(facing.rotateY(),1);
			case TOP_MIDLEFT:return pos.down(2).offset(facing.rotateY(),2);
			case TOP_LEFT:return pos.down(2).offset(facing.rotateY(),3);
			}
			return pos;
	}
	
	public static enum EnumParts implements IStringSerializable
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
			return getName();
		}
		public String getName()
		{
			return name().toLowerCase();
		}
	}
}
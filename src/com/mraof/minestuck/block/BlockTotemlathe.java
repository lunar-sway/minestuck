package com.mraof.minestuck.block;

import com.mraof.minestuck.tileentity.TileEntityTotemlathe;

import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTotemlathe extends BlockLargeMachine
{
	
	public static final PropertyEnum<EnumParts> PART = PropertyEnum.create("part", EnumParts.class);
	public static final PropertyDirection DIRECTION = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyBool HASCARD = PropertyBool.create("hascard");
	public BlockTotemlathe() {
		setUnlocalizedName("totem_lathe");
		setDefaultState(blockState.getBaseState());
		
	} 
	//not sure how to do this.
	//@Override
	//public AxisAlignedBB getBoundingBox(IBlockState state,IBlockAccess source,BlockPos pos){
	//EnumParts parts = state.getValue(PART);
	//EnumFacing facing = state.getValue(DIRECTION);
	
	//return parts.BOUNDING_BOX[facing.getHorizontalIndex()];
	//}
	

	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		BlockPos mainPos = getMainPos(state, pos);
		TileEntity te = worldIn.getTileEntity(mainPos);
		if(te != null && te instanceof TileEntityTotemlathe)
			((TileEntityTotemlathe) te).onRightClick(playerIn, state);
		return true;
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return state.getValue(PART) == EnumParts.BOTTOM_LEFT;
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		
		if(meta % 4 == EnumParts.BOTTOM_LEFT.ordinal())
			return new TileEntityTotemlathe();
		else return null;
	}
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
	
		BlockPos mainPos = getMainPos(state, pos);
		TileEntity te = worldIn.getTileEntity(mainPos);
		if(te != null && te instanceof TileEntityTotemlathe)
		{
			TileEntityTotemlathe lathe = (TileEntityTotemlathe) te;
			lathe.Brake();
			lathe.dropCard1(true,pos);
			lathe.dropCard2(true,pos);
			lathe.dropDowel(true,pos);

		}
		
		super.breakBlock(worldIn, pos, state);
		
		
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		
		EnumFacing facing = EnumFacing.getHorizontal(MathHelper.floor((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) + 2 & 3).getOpposite();
		state = state.withProperty(DIRECTION, facing);
		IBlockState state2=MinestuckBlocks.totemlathe2.getDefaultState().withProperty(BlockTotemlathe2.DIRECTION, facing);
		IBlockState state3=MinestuckBlocks.totemlathe3.getDefaultState().withProperty(BlockTotemlathe3.DIRECTION, facing);
		
		
		if(!(worldIn.isRemote)){
			worldIn.setBlockState(pos.offset(facing.rotateY(),3), state.withProperty(PART, EnumParts.BOTTOM_RIGHT));
			worldIn.setBlockState(pos.offset(facing.rotateY(),2), state.withProperty(PART, EnumParts.BOTTOM_MIDRIGHT));
			worldIn.setBlockState(pos.offset(facing.rotateY(),1), state.withProperty(PART, EnumParts.BOTTOM_MIDLEFT));
			worldIn.setBlockState(pos, state.withProperty(PART, EnumParts.BOTTOM_LEFT));
			worldIn.setBlockState(pos.offset(facing.rotateY(),3).up(1), state2.withProperty(BlockTotemlathe2.PART, BlockTotemlathe2.EnumParts.MID_RIGHT));
			worldIn.setBlockState(pos.offset(facing.rotateY(),2).up(1), state2.withProperty(BlockTotemlathe2.PART, BlockTotemlathe2.EnumParts.MID_MIDRIGHT));
			worldIn.setBlockState(pos.offset(facing.rotateY(),1).up(1), state2.withProperty(BlockTotemlathe2.PART, BlockTotemlathe2.EnumParts.MID_MIDLEFT));
			worldIn.setBlockState(pos.up(1), state2.withProperty(BlockTotemlathe2.PART, BlockTotemlathe2.EnumParts.MID_LEFT));
			worldIn.setBlockState(pos.offset(facing.rotateY(),2).up(2), state3.withProperty(BlockTotemlathe3.PART, BlockTotemlathe3.EnumParts.TOP_MIDRIGHT));
			worldIn.setBlockState(pos.offset(facing.rotateY(),1).up(2), state3.withProperty(BlockTotemlathe3.PART, BlockTotemlathe3.EnumParts.TOP_MIDLEFT));
			worldIn.setBlockState(pos.up(2), state3.withProperty(BlockTotemlathe3.PART, BlockTotemlathe3.EnumParts.TOP_LEFT));
		}
	}
	

	

	
	
	
	
	
	//Block state handling
	
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, PART, DIRECTION,HASCARD);
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
		
			case BOTTOM_RIGHT:return pos.offset(facing.rotateYCCW(),3);
			case BOTTOM_MIDRIGHT:return pos.offset(facing.rotateYCCW(),2);
			case BOTTOM_MIDLEFT:return pos.offset(facing.rotateYCCW(),1);
			case BOTTOM_LEFT:return pos;
			}
			return pos;
	}
	
	@Override
	public IBlockState getActualState(IBlockState state,IBlockAccess worldIn,BlockPos pos) {
		if (state.getBlock()==MinestuckBlocks.totemlathe && state.getValue(PART)==EnumParts.BOTTOM_LEFT ) {
			BlockPos mainPos = getMainPos(state, pos);
			TileEntity te = worldIn.getTileEntity(mainPos);
			return state.withProperty(HASCARD, !((TileEntityTotemlathe)te).getCard1().isEmpty());
		}
		
		return state;	
	}
	
	/**
	 * updates the block and tile entities, ensuring that if the server is changed, the client will also be changed as well.
	 * @param hascard
	 * @param hasdowel flag 0 for no dowel, 1 for uncarved dowel, 2 for carved dowel
	 * @param world
	 * @param pos
	 */
	public static void updateItem(boolean hascard,BlockTotemlathe2.EnumDowel hasdowel, World world, BlockPos pos)
	{
		IBlockState oldState = world.getBlockState(pos);
		IBlockState oldState2=world.getBlockState(pos.up());
		if (oldState.getBlock()==MinestuckBlocks.totemlathe) {
			EnumFacing facing =oldState.getValue(DIRECTION);
			//updates all items that should update
			world.notifyBlockUpdate(pos, oldState, oldState.withProperty(HASCARD, hascard), 3);
		
			world.notifyBlockUpdate(pos.up(1), oldState2,oldState2.withProperty(BlockTotemlathe2.HASDOWEL,hasdowel) ,3);
			world.notifyBlockUpdate(pos.offset(facing.rotateY(),1).up(1), oldState2,oldState2.withProperty(BlockTotemlathe2.HASDOWEL,hasdowel) ,3);
			world.notifyBlockUpdate(pos.offset(facing.rotateY(),2).up(1), oldState2,oldState2.withProperty(BlockTotemlathe2.HASDOWEL,hasdowel) ,3);
			world.notifyBlockUpdate(pos.offset(facing.rotateY(),3).up(1), oldState2,oldState2.withProperty(BlockTotemlathe2.HASDOWEL,hasdowel) ,3);
		}
	}

	public static enum EnumParts implements IStringSerializable
	{
		//(new AxisAlignedBB(5/16D, 0.0D, 0.0D, 1.0D, 1.0D, 11/16D), new AxisAlignedBB(5/16D, 0.0D, 5/16D, 1.0D, 1.0D, 1.0D),
		//new AxisAlignedBB(0.0D, 0.0D, 5/16D, 11/16D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 11/16D, 1.0D, 11/16D)),
		// try out code later to see if i can find out how it works
		BOTTOM_RIGHT,
		BOTTOM_MIDRIGHT,
		BOTTOM_MIDLEFT,
		BOTTOM_LEFT;
		
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
}
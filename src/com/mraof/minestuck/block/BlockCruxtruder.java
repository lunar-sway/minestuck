package com.mraof.minestuck.block;

import com.mraof.minestuck.tileentity.TileEntityCruxtruder;

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

public class BlockCruxtruder extends BlockLargeMachine{

	public static final PropertyEnum<EnumParts> PART = PropertyEnum.<EnumParts>create("part",EnumParts.class);

	public BlockCruxtruder() {
		setUnlocalizedName("cruxtruder");
		setDefaultState(blockState.getBaseState());
		
	} 
	//not sure how to do this.
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state,IBlockAccess source,BlockPos pos){
		if( state.getBlock()==MinestuckBlocks.cruxtruder){
			EnumParts parts = state.getValue(PART);
			return parts.BOUNDING_BOX[0];
		}
		else {
			BlockCruxtruder2.EnumParts parts = state.getValue(BlockCruxtruder2.PART);
			return parts.getBoundingBox(0);
		}
		//EnumFacing facing = state.getValue(DIRECTION);
		
	}


	@Override
	public boolean onBlockActivated(World worldIn,BlockPos pos,IBlockState state,EntityPlayer playerIn,EnumHand hand,EnumFacing facing,float hitX,float hitY,float hitZ){
		BlockPos mainPos = getMainPos(state, pos);
		System.out.println(mainPos);
		TileEntity te = worldIn.getTileEntity(mainPos);
		if( te != null && te instanceof TileEntityCruxtruder)
			((TileEntityCruxtruder) te).onRightClick(playerIn, state);
		return true;
	}
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return null;
	}
	
	
	
	
	
	
	
	
	@Override
	public void onBlockPlacedBy(World worldIn,BlockPos pos,IBlockState state,EntityLivingBase placer, ItemStack stack){
		EnumFacing facing = EnumFacing.getHorizontal(MathHelper.floor((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3).getOpposite();
		switch (facing) {

		case EAST:pos = pos.north(2).west(2);
			break;
		case NORTH:pos = pos.west(2);
			break;
		case SOUTH:pos=pos.north(2);
			break;
		case WEST:pos=pos;
			break;
		default:
			break;
		
		}
		
		
		if(placer!=null && !(worldIn.isRemote)){
			

			worldIn.setBlockState(pos.south(1).up(0).east(1), MinestuckBlocks.cruxtruder2.getDefaultState().withProperty(BlockCruxtruder2.PART, BlockCruxtruder2.EnumParts.ONE_ONE_ONE));
			worldIn.setBlockState(pos.south(1).up(1).east(1), MinestuckBlocks.cruxtruder2.getDefaultState().withProperty( BlockCruxtruder2.PART, BlockCruxtruder2.EnumParts.ONE_TWO_ONE));
			worldIn.setBlockState(pos.south(1).up(2).east(1), MinestuckBlocks.cruxtruder2.getDefaultState().withProperty( BlockCruxtruder2.PART,  BlockCruxtruder2.EnumParts.ONE_THREE_ONE).withProperty(BlockCruxtruder2.HASLID, true));
			
			worldIn.setBlockState(pos.south(0).up(1).east(0), state.withProperty(PART, EnumParts.ZERO_TWO_ZERO));
			worldIn.setBlockState(pos.south(0).up(1).east(1), state.withProperty( PART,  EnumParts.ZERO_TWO_ONE));
			worldIn.setBlockState(pos.south(0).up(1).east(2), state.withProperty( PART,  EnumParts.ZERO_TWO_TWO));
			worldIn.setBlockState(pos.south(1).up(1).east(0), state.withProperty( PART,  EnumParts.ONE_TWO_ZERO));
			worldIn.setBlockState(pos.south(1).up(1).east(2), state.withProperty( PART,  EnumParts.ONE_TWO_TWO));
			worldIn.setBlockState(pos.south(2).up(1).east(0), state.withProperty( PART,  EnumParts.TWO_TWO_ZERO));
			worldIn.setBlockState(pos.south(2).up(1).east(1), state.withProperty( PART,  EnumParts.TWO_TWO_ONE));
			worldIn.setBlockState(pos.south(2).up(1).east(2), state.withProperty( PART,  EnumParts.TWO_TWO_TWO));
			
			
			worldIn.setBlockState(pos.south(0).up(0).east(0), state.withProperty(PART, EnumParts.ZERO_ONE_ZERO));
			worldIn.setBlockState(pos.south(0).up(0).east(1), state.withProperty(PART, EnumParts.ZERO_ONE_ONE));
			worldIn.setBlockState(pos.south(0).up(0).east(2), state.withProperty(PART, EnumParts.ZERO_ONE_TWO));;
			worldIn.setBlockState(pos.south(1).up(0).east(0), state.withProperty(PART, EnumParts.ONE_ONE_ZERO));
			worldIn.setBlockState(pos.south(1).up(0).east(2), state.withProperty(PART, EnumParts.ONE_ONE_TWO));
			worldIn.setBlockState(pos.south(2).up(0).east(0), state.withProperty(PART, EnumParts.TWO_ONE_ZERO));
			worldIn.setBlockState(pos.south(2).up(0).east(1), state.withProperty(PART, EnumParts.TWO_ONE_ONE));
			worldIn.setBlockState(pos.south(2).up(0).east(2), state.withProperty(PART, EnumParts.TWO_ONE_TWO));
		}
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		BlockPos MainPos=getMainPos(state,pos);
		if (worldIn.getTileEntity(MainPos) instanceof TileEntityCruxtruder){
			TileEntityCruxtruder te = (TileEntityCruxtruder) worldIn.getTileEntity(MainPos);
			te.destroy();
		}	
		super.breakBlock(worldIn, pos, state);
	}
	

	
	//Block state handling
	public static enum EnumParts implements IStringSerializable
	{
		ZERO_ONE_ZERO(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1D, 14/16D, 1D)),
		ZERO_ONE_ONE(new AxisAlignedBB (0.0D, 0.0D, 0.0D, 1D, 14/16D, 1D)),
		ZERO_ONE_TWO(new AxisAlignedBB (0.0D, 0.0D, 0.0D, 1D, 14/16D, 1D)),
		ONE_ONE_ZERO(new AxisAlignedBB (0.0D, 0.0D, 0.0D, 1D, 14/16D,1D)),
		ONE_ONE_TWO(new AxisAlignedBB  (0.0D, 0.0D, 0.0D, 1D, 14/16D, 1D)),
		TWO_ONE_ZERO(new AxisAlignedBB (0.0D, 0.0D, 0.0D, 1D, 14/16D, 1D)),
		TWO_ONE_ONE(new AxisAlignedBB  (0.0D, 0.0D, 0.0D, 1D, 14/16D, 1D)),
		TWO_ONE_TWO(new AxisAlignedBB  (0.0D, 0.0D, 0.0D, 1D, 14/16D, 1D)),
		
		//AxisAlignedBB(x1, y1, z1, x2, y2, z2)
		ZERO_TWO_ZERO(new AxisAlignedBB(/**/9/16D, -2/16D, 9/16D, 1.0D, 8/16D, 1.0D)),
		ZERO_TWO_ONE(new AxisAlignedBB (0.0D, -2/16D, 2/16D, 1.0D, 8/16D, 1.0D)),
		ZERO_TWO_TWO(new AxisAlignedBB (/**/0.0D, -2/16D, 9/16D, 7/16D, 8/16D, 1.0D)),
		ONE_TWO_ZERO(new AxisAlignedBB (2/16D, -2/16D, 0.0D, 1.0D, 8/16D, 1.0D)),
		ONE_TWO_TWO(new AxisAlignedBB  (0.0D, -2/16D, 0.0D, 14/16D, 8/16D, 1.0D)),
		TWO_TWO_ZERO(new AxisAlignedBB (/**/9/16D, -2/16D, 0.0D, 1.0D, 8/16D, 7/16D)),
		TWO_TWO_ONE(new AxisAlignedBB  (0.0D, -2/16D, 0.0D, 1.0D, 8/16D, 14/16D)),
		TWO_TWO_TWO(new AxisAlignedBB  (/**/0.0D, -2/16D, 0.0D, 7/16D, 8/16D, 7/16D));
		
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
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, PART);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		IBlockState defaultState = getDefaultState();
		EnumParts part = EnumParts.values()[meta];
		
		return defaultState.withProperty(PART, part);
	}
	@Override
	public int getMetaFromState(IBlockState state)
	{
		EnumParts part = state.getValue(PART);
		return part.ordinal();
	}

	public BlockPos getMainPos(IBlockState state, BlockPos pos) {
			EnumParts part=state.getValue(PART);
			switch(part){

			case ZERO_ONE_ZERO:return pos.south().east().up(2);
			case ZERO_ONE_ONE:return pos.south().up(2);
			case ZERO_ONE_TWO:return pos.south().west().up(2);
			case ZERO_TWO_ZERO:return pos.south().east().up();
			case ZERO_TWO_ONE:return pos.south().up();
			case ZERO_TWO_TWO:return pos.south().west().up();

			case ONE_ONE_ZERO:return pos.east().up(2);
			case ONE_ONE_TWO:return pos.west().up(2);
			case ONE_TWO_ZERO:return pos.east().up();
			case ONE_TWO_TWO:return pos.west().up();

			case TWO_ONE_ZERO:return pos.north().east().up(2);
			case TWO_ONE_ONE:return pos.north().up(2);
			case TWO_ONE_TWO:return pos.north().west().up(2);
			case TWO_TWO_ZERO:return pos.north().east().up();
			case TWO_TWO_ONE:return pos.north().up();
			case TWO_TWO_TWO:return pos.north().west().up();
			}
			return pos;
	}
}


package com.mraof.minestuck.block;

import net.minecraft.block.properties.IProperty;
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
import net.minecraft.world.World;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.GuiHandler;
import com.mraof.minestuck.tileentity.TileEntityAlchemiter;

public class BlockAlchemiter extends BlockLargeMachine{

	public static final PropertyEnum<enumParts> PART = PropertyEnum.<enumParts>create("part",enumParts.class);

	public BlockAlchemiter() {
		super(1,1,1);
		setUnlocalizedName("alchemiter");
		setDefaultState(getStateFromMeta(0));
		
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
		BlockPos MasterPos=GetMasterPos(state , pos);
		System.out.println(MasterPos);
		if(worldIn.getTileEntity(MasterPos) instanceof TileEntityAlchemiter){ 
			TileEntityAlchemiter te=(TileEntityAlchemiter)worldIn.getTileEntity(MasterPos);
			//if(!worldIn.isRemote){
				if(!(te.isDestroyed())){		
					playerIn.openGui(Minestuck.instance, GuiHandler.GuiId.MACHINE.ordinal(), worldIn, MasterPos.getX(), MasterPos.getY(), MasterPos.getZ());	
				}
			//}
		}
		return true;
	}
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		if (meta == 0)
			return new TileEntityAlchemiter();
		return null;
	}
	
	
	
	
	
	
	
	
	@Override
	public void onBlockPlacedBy(World worldIn,BlockPos pos,IBlockState state,EntityLivingBase placer, ItemStack stack){
		if(placer!=null && !(worldIn.isRemote)){
			worldIn.setBlockState(pos.south(0).up(1).west(0), MinestuckBlocks.alchemiter2.getDefaultState().withProperty(BlockAlchemiter2.PART, BlockAlchemiter2.enumParts.ZERO_TWO_ZERO));
			worldIn.setBlockState(pos.south(0).up(2).west(0), MinestuckBlocks.alchemiter2.getDefaultState().withProperty(BlockAlchemiter2.PART, BlockAlchemiter2.enumParts.ZERO_THREE_ZERO));
			worldIn.setBlockState(pos.south(0).up(3).west(0), MinestuckBlocks.alchemiter2.getDefaultState().withProperty(BlockAlchemiter2.PART, BlockAlchemiter2.enumParts.ZERO_FOUR_ZERO));
			

			worldIn.setBlockState(pos.south(0).up(0).west(0), state.withProperty(PART, enumParts.ZERO_ONE_ZERO));
			worldIn.setBlockState(pos.south(0).up(0).west(1), state.withProperty(PART, enumParts.ZERO_ONE_ONE));
			worldIn.setBlockState(pos.south(0).up(0).west(2), state.withProperty(PART, enumParts.ZERO_ONE_TWO));
			worldIn.setBlockState(pos.south(0).up(0).west(3), state.withProperty(PART, enumParts.ZERO_ONE_THREE));
			worldIn.setBlockState(pos.south(1).up(0).west(0), state.withProperty(PART, enumParts.ONE_ONE_ZERO));
			worldIn.setBlockState(pos.south(1).up(0).west(1), state.withProperty(PART, enumParts.ONE_ONE_ONE));
			worldIn.setBlockState(pos.south(1).up(0).west(2), state.withProperty(PART, enumParts.ONE_ONE_TWO));
			worldIn.setBlockState(pos.south(1).up(0).west(3), state.withProperty(PART, enumParts.ONE_ONE_THREE));
			worldIn.setBlockState(pos.south(2).up(0).west(0), state.withProperty(PART, enumParts.TWO_ONE_ZERO));
			worldIn.setBlockState(pos.south(2).up(0).west(1), state.withProperty(PART, enumParts.TWO_ONE_ONE));
			worldIn.setBlockState(pos.south(2).up(0).west(2), state.withProperty(PART, enumParts.TWO_ONE_TWO));
			worldIn.setBlockState(pos.south(2).up(0).west(3), state.withProperty(PART, enumParts.TWO_ONE_THREE));
			worldIn.setBlockState(pos.south(3).up(0).west(0), state.withProperty(PART, enumParts.THREE_ONE_ZERO));
			worldIn.setBlockState(pos.south(3).up(0).west(1), state.withProperty(PART, enumParts.THREE_ONE_ONE));
			worldIn.setBlockState(pos.south(3).up(0).west(2), state.withProperty(PART, enumParts.THREE_ONE_TWO));
			worldIn.setBlockState(pos.south(3).up(0).west(3), state.withProperty(PART, enumParts.TRHEE_ONE_THREE));
			
		}
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		BlockPos MasterPos=GetMasterPos(state,pos);
		if (worldIn.getTileEntity(MasterPos) instanceof TileEntityAlchemiter){
			TileEntityAlchemiter te = (TileEntityAlchemiter) worldIn.getTileEntity(MasterPos);
			te.destroy();
			InventoryHelper.dropInventoryItems(worldIn, pos, te);
		}	
		super.breakBlock(worldIn, pos, state);
	}
	
	

	
	
	
	
	
	
	
	
	
	
	
	//Block state handling
	public static enum enumParts implements IStringSerializable
	{
		ZERO_ONE_ZERO,
		ZERO_ONE_ONE,
		ZERO_ONE_TWO,
		ZERO_ONE_THREE,
		ONE_ONE_ZERO,
		ONE_ONE_ONE,
		ONE_ONE_TWO,
		ONE_ONE_THREE,
		TWO_ONE_ZERO,
		TWO_ONE_ONE,
		TWO_ONE_TWO,
		TWO_ONE_THREE,
		THREE_ONE_ZERO,
		THREE_ONE_ONE,
		THREE_ONE_TWO,
		TRHEE_ONE_THREE;
	
		
		public String toString()
		{
			return getName();
		}
		public String getName()
		{
			switch (this){
			case ZERO_ONE_ZERO: return "zero_one_zero";
			case ZERO_ONE_ONE:return "zero_one_one";
			case ZERO_ONE_TWO:return "zero_one_two";
			case ZERO_ONE_THREE:return "zero_one_three";
			case ONE_ONE_ZERO:return "one_one_zero";
			case ONE_ONE_ONE:return "one_one_one";
			case ONE_ONE_TWO:return "one_one_two";
			case ONE_ONE_THREE:return "one_one_three";
			case TWO_ONE_ZERO:return "two_one_zero";
			case TWO_ONE_ONE:return "two_one_one";
			case TWO_ONE_TWO:return "two_one_two";
			case TWO_ONE_THREE:return "two_one_three";
			case THREE_ONE_ZERO:return "three_one_zero";
			case THREE_ONE_ONE:return "three_one_one";
			case THREE_ONE_TWO:return "three_one_two";
			case TRHEE_ONE_THREE:return	"three_one_three";
			}
			return "null";
		}
		
		

	}
	public BlockPos GetMasterPos(IBlockState state, BlockPos pos){
		enumParts part=state.getValue(BlockAlchemiter.PART);
		switch(part){
		case ZERO_ONE_ZERO: return pos.north(0).down(0).east(0);
		case ZERO_ONE_ONE:	return pos.north(0).down(0).east(1);
		case ZERO_ONE_TWO:	return pos.north(0).down(0).east(2);
		case ZERO_ONE_THREE:return pos.north(0).down(0).east(3);
		case ONE_ONE_ZERO:	return pos.north(1).down(0).east(0);
		case ONE_ONE_ONE:	return pos.north(1).down(0).east(1);
		case ONE_ONE_TWO:	return pos.north(1).down(0).east(2);
		case ONE_ONE_THREE:	return pos.north(1).down(0).east(3);
		case TWO_ONE_ZERO:	return pos.north(2).down(0).east(0);
		case TWO_ONE_ONE:	return pos.north(2).down(0).east(1);
		case TWO_ONE_TWO:	return pos.north(2).down(0).east(2);
		case TWO_ONE_THREE:	return pos.north(2).down(0).east(3);
		case THREE_ONE_ZERO:return pos.north(3).down(0).east(0);
		case THREE_ONE_ONE:	return pos.north(3).down(0).east(1);
		case THREE_ONE_TWO:	return pos.north(3).down(0).east(2);
		case TRHEE_ONE_THREE:return pos.north(3).down(0).east(3);
		}
		return pos;
	}
	@Override
	public IBlockState getStateFromMeta(int meta){
		IBlockState defaultState=getDefaultState();
		switch (meta){
		case 0: return defaultState.withProperty(PART, enumParts.ZERO_ONE_ZERO);
		case 1: return defaultState.withProperty(PART, enumParts.ZERO_ONE_ONE);
		case 2: return defaultState.withProperty(PART, enumParts.ZERO_ONE_TWO);
		case 3: return defaultState.withProperty(PART, enumParts.ZERO_ONE_THREE);
		case 4: return defaultState.withProperty(PART, enumParts.ONE_ONE_ZERO);
		case 5: return defaultState.withProperty(PART, enumParts.ONE_ONE_ONE);
		case 6: return defaultState.withProperty(PART, enumParts.ONE_ONE_TWO);
		case 7: return defaultState.withProperty(PART, enumParts.ONE_ONE_THREE);
		case 8: return defaultState.withProperty(PART, enumParts.TWO_ONE_ZERO);
		case 9: return defaultState.withProperty(PART, enumParts.TWO_ONE_ONE);
		case 10: return defaultState.withProperty(PART, enumParts.TWO_ONE_TWO);
		case 11: return defaultState.withProperty(PART, enumParts.TWO_ONE_THREE);
		case 12: return defaultState.withProperty(PART, enumParts.THREE_ONE_ZERO);
		case 13: return defaultState.withProperty(PART, enumParts.THREE_ONE_ONE);
		case 14: return defaultState.withProperty(PART, enumParts.THREE_ONE_TWO);
		case 15: return defaultState.withProperty(PART, enumParts.TRHEE_ONE_THREE);
		
		}
		return null;
	}

	@Override
	public int getMetaFromState(IBlockState state){
		enumParts part=state.getValue(PART);
	switch(part){
	case ZERO_ONE_ZERO:	return 0;
	case ZERO_ONE_ONE:	return 1;
	case ZERO_ONE_TWO:	return 2;
	case ZERO_ONE_THREE:return 3;
	case ONE_ONE_ZERO:	return 4;
	case ONE_ONE_ONE:	return 5;
	case ONE_ONE_TWO:	return 6;
	case ONE_ONE_THREE:	return 7;
	case TWO_ONE_ZERO:	return 8;
	case TWO_ONE_ONE:	return 9;
	case TWO_ONE_TWO:	return 10;
	case TWO_ONE_THREE:	return 11;
	case THREE_ONE_ZERO:return 12;
	case THREE_ONE_ONE:	return 13;
	case THREE_ONE_TWO:	return 14;
	case TRHEE_ONE_THREE:return	15;
		}
		return 0;
	}
	
	
	
	
}
package com.mraof.minestuck.block;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.GuiHandler;
import com.mraof.minestuck.tileentity.TileEntityAlchemiter;
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
		
		//TileEntityPunchDesignix te=(TileEntityPunchDesignix)worldIn.getTileEntity(pos);
		//BlockPos MasterPos=te.GetMasterPos(state);
		if(!worldIn.isRemote /*&& !((TileEntityPunchDesignix)worldIn.getTileEntity(MasterPos)).broken*/){
			//if(worldIn.getTileEntity(pos)instanceof TileEntityPunchDesignix){				
				//if(te.isMaster()){
					playerIn.openGui(Minestuck.instance, GuiHandler.GuiId.MACHINE.ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());
					System.out.println("==========================================");
				//}else{
				//	playerIn.openGui(Minestuck.instance, GuiHandler.GuiId.MACHINE.ordinal(), worldIn, MasterPos.getX(), MasterPos.getY(), MasterPos.getZ());
				//}
			//}
		}
		return true;
	}
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
			return new TileEntityAlchemiter();
		
	}
	
	
	
	
	
	
	
	
	@Override
	public void onBlockPlacedBy(World worldIn,BlockPos pos,IBlockState state,EntityLivingBase placer, ItemStack stack){
		if(placer!=null && !(worldIn.isRemote)){
			worldIn.setBlockState(pos.south(0).up(0).west(0), state.withProperty(PART, enumParts.ZERO_ONE_ZERO));
			worldIn.setBlockState(pos.south(0).up(0).west(1), state.withProperty(PART, enumParts.ZERO_ONE_ONE));
			worldIn.setBlockState(pos.south(0).up(0).west(2), state.withProperty(PART, enumParts.ZERO_ONE_TWO));
			worldIn.setBlockState(pos.south(0).up(0).west(3), state.withProperty(PART, enumParts.ZERO_ONE_THREE));
			worldIn.setBlockState(pos.south(0).up(1).west(0), state.withProperty(PART, enumParts.ZERO_TWO_ZERO));
			worldIn.setBlockState(pos.south(0).up(2).west(0), state.withProperty(PART, enumParts.ZERO_THREE_ZERO));
			worldIn.setBlockState(pos.south(0).up(3).west(0), state.withProperty(PART, enumParts.ZERO_FOUR_ZERO));
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
	
		/*BlockPos MasterPos=((TileEntityPunchDesignix)worldIn.getTileEntity(pos)).GetMasterPos(state);
		TileEntityPunchDesignix te = (TileEntityPunchDesignix) worldIn.getTileEntity(MasterPos);
		te.broken=true;
		InventoryHelper.dropInventoryItems(worldIn, pos, te);*/
		
		super.breakBlock(worldIn, pos, state);
	}
	
	//Block state handling
	public static enum enumParts implements IStringSerializable
	{
		ZERO_ONE_ZERO,
		ZERO_ONE_ONE,
		ZERO_ONE_TWO,
		ZERO_ONE_THREE,
		ZERO_TWO_ZERO,
		ZERO_THREE_ZERO,
		ZERO_FOUR_ZERO,
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
			case ZERO_ONE_ZERO:return "zero_one_zero";
			case ZERO_ONE_ONE:return "zero_one_one";
			case ZERO_ONE_TWO:return "zero_one_two";
			case ZERO_ONE_THREE:return "zero_one_three";
			case ZERO_TWO_ZERO:return "zero_two_zero";
			case ZERO_THREE_ZERO:return "zero_three_zero";
			case ZERO_FOUR_ZERO:return "zero_four_zero";
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
	
	@Override
	public IBlockState getStateFromMeta(int meta){
		IBlockState defaultState=getDefaultState();
		//switch (meta){
		return defaultState;
		//}
	}

	@Override
	public int getMetaFromState(IBlockState state){
		enumParts part=state.getValue(PART);
		switch(part){
/*		case ZERO_ONE_ZERO:return 0;
		case ZERO_ONE_ONE:return 1;
		case ZERO_ONE_TWO:return 2;
		case ZERO_ONE_THREE:return 3; 
		case ZERO_TWO_ZERO:return 4;
		case ZERO_THREE_ZERO:return 5;
		case ZERO_FOUR_ZERO:return 6;
		case ONE_ONE_ZERO:return 7;
		case ONE_ONE_ONE:return 8;
		case ONE_ONE_TWO:return 9 ;
		case ONE_ONE_THREE:return 10; 
		case TWO_ONE_ZERO:return 11;
		case TWO_ONE_ONE:return 12;
		case TWO_ONE_TWO:return 13;
		case TWO_ONE_THREE:return 14;
		case THREE_ONE_ZERO:return 15;
		case THREE_ONE_ONE:return 16;
		case THREE_ONE_TWO:return 17;
		case TRHEE_ONE_THREE:return	18;*/
		}
		return 0;
	}
}
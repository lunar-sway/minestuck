package com.mraof.minestuck.block;

import java.util.List;

import javax.annotation.Nullable;

import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.item.TabMinestuck;
import com.mraof.minestuck.tileentity.TileEntityHolopad;
import com.mraof.minestuck.tileentity.TileEntityJumperBlock;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockHolopad extends BlockContainer
{

	protected static final AxisAlignedBB HOLOPAD_AABB = new AxisAlignedBB(2/16F, 0F, 1.1/16F, 14/16F, 6/16F, 13.1/16F);
	protected static final AxisAlignedBB HOLOPAD_TOP_AABB = new AxisAlignedBB(3/16F, 6/16F, 2.6/16F, 13/16F, 7/16F, 12.6/16F);
	protected static final AxisAlignedBB HOLOPAD_CARDSLOT_AABB = new AxisAlignedBB(4/16F, 0F, 13.8/16F, 12/16F, 10.1/16F, 15.94/16F);
	
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyBool CARD = PropertyBool.create("card");
	
	protected BlockHolopad() 
	{
		super(Material.ROCK, MapColor.SNOW);
		this.setUnlocalizedName("holopad");
		this.setHardness(2);
		this.setCreativeTab(TabMinestuck.instance);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new TileEntityHolopad();
	}

	@Override
	public boolean hasTileEntity() 
	{
		return true;
	}
	
	//Right click
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) 
	{
		if(playerIn.isSneaking()) return false;
		//if(worldIn.isRemote)
			//return true;
		TileEntity te = worldIn.getTileEntity(pos);
		
		if(te instanceof TileEntityHolopad)
			((TileEntityHolopad) te).onRightClick(playerIn);
		return true;
	}
	
	
	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) 
	{
		TileEntityHolopad te = (TileEntityHolopad) worldIn.getTileEntity(pos);
		
		if(te != null && !worldIn.isRemote)
		{
			te.dropItem(true, worldIn, pos, te.getCard());
			te.destroyHologram(pos);
		}
		
		super.onBlockHarvested(worldIn, pos, state, player);
	}
	
	
	//Directional Placement
	
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
    }

    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        return state.withProperty(FACING, mirrorIn.mirror((EnumFacing)state.getValue(FACING)));
    }
    
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }
    
    
    //State from meta
    public IBlockState getStateFromMeta(int meta)
    {
        IBlockState iblockstate = this.getDefaultState();
        iblockstate = iblockstate.withProperty(FACING, EnumFacing.getHorizontal(meta % 4));
        return iblockstate;
    }
    
    //Meta from state
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();
    }

    //more states
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING, CARD});
    }
    
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) 
    {
    	TileEntityHolopad tileEntity = (TileEntityHolopad) worldIn.getTileEntity(pos);
    	if(tileEntity != null && tileEntity.hasCard()) return state.withProperty(CARD, true);
    	else return state.withProperty(CARD, false);
    }
    
	//Bounding Box
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) 
	{
		EnumFacing facing = state.getValue(FACING);
		
		return modifyAABBForDirection(facing, HOLOPAD_AABB);
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_)
	{
		super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, p_185477_7_);
			//AxisAlignedBB bb = modifyAABBForDirection(state.getValue(DIRECTION), HOLOPAD_TOP_AABB).offset(pos);
			EnumFacing facing = state.getValue(FACING);
			AxisAlignedBB top = modifyAABBForDirection(facing, HOLOPAD_TOP_AABB);
			AxisAlignedBB cardSlot = modifyAABBForDirection(facing, HOLOPAD_CARDSLOT_AABB);
			
			if(entityBox.intersects(top))
				collidingBoxes.add(top);
			
			if(entityBox.intersects(cardSlot))
				collidingBoxes.add(cardSlot);
	}
	
	public AxisAlignedBB modifyAABBForDirection(EnumFacing facing, AxisAlignedBB bb)
	{
		AxisAlignedBB out = null;
		switch(facing.ordinal())
		{
		case 2:	//North
			out = new AxisAlignedBB(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ);
			break;
		case 3:	//South
			out = new AxisAlignedBB(1-bb.maxX, bb.minY, 1-bb.maxZ, 1-bb.minX, bb.maxY, 1-bb.minZ);
			break;
		case 4:	//West
			out = new AxisAlignedBB(bb.minZ, bb.minY, 1-bb.maxX, bb.maxZ, bb.maxY, 1-bb.minX);
			break;
		case 5:	//East
			out = new AxisAlignedBB(1-bb.maxZ, bb.minY, bb.minX, 1-bb.minZ, bb.maxY, bb.maxX);
			break;
		}
		return out;
	}
	
	//Renders and stuff
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		return BlockFaceShape.UNDEFINED;
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
	
	
}

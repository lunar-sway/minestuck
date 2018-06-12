package com.mraof.minestuck.block;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.GuiHandler;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.tileentity.TileEntityCrockerMachine;
import com.mraof.minestuck.tileentity.TileEntityMachine;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCrockerMachine extends BlockContainer
{
	protected static final AxisAlignedBB[] GRIST_WIDGET_AABB = {new AxisAlignedBB(0.0D, 0.0D, 1/4D, 1.0D, 1/4D, 3/4D), new AxisAlignedBB(1/4D, 0.0D, 0.0D, 3/4D, 1/4D, 1.0D)};
	
	public static enum MachineType implements IStringSerializable
	{
		GRIST_WIDGET("gristWidget");
		
		private final String unlocalizedName;
		private MachineType(String name)
		{
			unlocalizedName = name;
		}
		
		public String getUnlocalizedName()
		{
			return unlocalizedName;
		}
		
		@Override
		public String getName()
		{
			return name().toLowerCase();
		}
	}
	
	public static final PropertyEnum<MachineType> MACHINE_TYPE = PropertyEnum.create("machine_type", MachineType.class);
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	
	protected BlockCrockerMachine()
	{
		super(Material.ROCK);
		
		setUnlocalizedName("crockerMachine");
		setHardness(3.0F);
		setHarvestLevel("pickaxe", 0);
		setDefaultState(getDefaultState().withProperty(FACING, EnumFacing.SOUTH));
		this.setCreativeTab(MinestuckItems.tabMinestuck);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, MACHINE_TYPE, FACING);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(MACHINE_TYPE).ordinal() + state.getValue(FACING).getHorizontalIndex()*4;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(MACHINE_TYPE, MachineType.values()[meta%4]).withProperty(FACING, EnumFacing.getHorizontal(meta/4));
	}
	
	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
	{
		for(int i = 0; i < MachineType.values().length; i++)
			items.add(new ItemStack(this, 1, i));
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
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		
		if (!(tileEntity instanceof TileEntityCrockerMachine) || playerIn.isSneaking())
			return false;
		
		if(!worldIn.isRemote)
			playerIn.openGui(Minestuck.instance, GuiHandler.GuiId.MACHINE.ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		TileEntityMachine te = (TileEntityMachine) worldIn.getTileEntity(pos);
		InventoryHelper.dropInventoryItems(worldIn, pos, te);
		
		super.breakBlock(worldIn, pos, state);
	}
	
	
	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return this.getStateFromMeta(meta).withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityCrockerMachine();
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		EnumFacing rotation = (EnumFacing) state.getValue(FACING);
		MachineType type = (MachineType) state.getValue(MACHINE_TYPE);
		
		switch(type)
		{
		case GRIST_WIDGET: return GRIST_WIDGET_AABB[rotation.getHorizontalIndex()%2];
		default: return super.getBoundingBox(state, source, pos);
		}
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
	{
		return new ItemStack(Item.getItemFromBlock(this), 1, state.getValue(MACHINE_TYPE).ordinal());
	}
	
}
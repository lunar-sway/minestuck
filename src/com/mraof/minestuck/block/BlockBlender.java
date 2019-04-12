package com.mraof.minestuck.block;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.GuiHandler;
import com.mraof.minestuck.item.TabMinestuck;
import com.mraof.minestuck.tileentity.TileEntityMachine;
import com.mraof.minestuck.tileentity.TileEntityUraniumCooker;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBlender extends BlockContainer
{
	protected static final AxisAlignedBB BLENDER_AABB = new AxisAlignedBB(3/16D, 0.0D, 3/16D, 12/16D, 1D, 12/16D);
	public static final PropertyDirection DIRECTION = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	public enum MachineType implements IStringSerializable
	{
		BLENDER("blender");
		
		private final String unlocalizedName;
		MachineType(String name)
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
	
	//public static final PropertyEnum<MachineType> MACHINE_TYPE = PropertyEnum.create("machine_type", MachineType.class);
	//public static final PropertyDirection FACING = BlockHorizontal.FACING;
	
	protected BlockBlender()
	{
		super(Material.IRON);
		
		setUnlocalizedName("blender");
		setHardness(3.0F);
		setHarvestLevel("pickaxe", 1);
		setDefaultState(getDefaultState());
		this.setCreativeTab(TabMinestuck.instance);
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, DIRECTION);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(DIRECTION).getHorizontalIndex();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(DIRECTION, EnumFacing.getHorizontal(meta % 4));
	}
	
	 @SideOnly(Side.CLIENT)
	    public BlockRenderLayer getBlockLayer()
	    {
	        return BlockRenderLayer.TRANSLUCENT;
	    }
	
	public static void setDefaultDirection(World world, int x, int y, int z)
	{
		if (!world.isRemote)
		{
			IBlockState block = world.getBlockState(new BlockPos(x, y, z - 1));
			IBlockState block1 = world.getBlockState(new BlockPos(x, y, z + 1));
			IBlockState block2 = world.getBlockState(new BlockPos(x - 1, y, z));
			IBlockState block3 = world.getBlockState(new BlockPos(x + 1, y, z));
			byte b0 = 0;

			if (block.isFullBlock() && !block1.isFullBlock())
			{
				b0 = 3;
			}

			if (block1.isFullBlock() && !block.isFullBlock())
			{
				b0 = 2;
			}

			if (block2.isFullBlock() && !block3.isFullBlock())
			{
				b0 = 5;
			}

			if (block3.isFullBlock() && !block2.isFullBlock())
			{
				b0 = 4;
			}
			
			if(b0 == 0)
				b0 = (byte) (block3.isFullBlock() ? 2 : 5);
			
			world.setBlockState(new BlockPos(x, y, z), world.getBlockState(new BlockPos(x, y, z)).withProperty(DIRECTION, EnumFacing.values()[b0]), 2);
		}
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
		
		if (!(tileEntity instanceof TileEntityUraniumCooker) || playerIn.isSneaking())
			return false;
		
		if(!worldIn.isRemote)
		{
			playerIn.openGui(Minestuck.instance, GuiHandler.GuiId.MACHINE.ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		TileEntityMachine te = (TileEntityMachine) worldIn.getTileEntity(pos);
		if(te != null) InventoryHelper.dropInventoryItems(worldIn, pos, te);
		
		super.breakBlock(worldIn, pos, state);
	}
	
	
	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return this.getStateFromMeta(meta);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityUraniumCooker();
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		int l = MathHelper.floor((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		
		EnumFacing facing = new EnumFacing[]{EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST}[l];
		
		worldIn.setBlockState(pos, state.withProperty(DIRECTION, facing), 2);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return BLENDER_AABB;
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
	{
		return new ItemStack(Item.getItemFromBlock(this), 1);
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		return BlockFaceShape.UNDEFINED;
	}
}
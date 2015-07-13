package com.mraof.minestuck.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.GuiHandler;
import com.mraof.minestuck.tileentity.TileEntityMachine;
import com.mraof.minestuck.util.Debug;

public class BlockMachine extends BlockContainer {

	public static final String[] iconNames = {"Cruxtruder","PunchDesignix","TotemLathe","Alchemiter","GristWidget"}; //PhernaliaFrame
	
	public static enum MachineType implements IStringSerializable
	{
		CRUXTRUDER,
		PUNCH_DESIGNIX,
		TOTEM_LATHE,
		ALCHEMITER,
		GRIST_WIDGET;	//TODO: Make gristwidget into a new block and move rotation to the block state from the tile entity when there's no reason to worry about breaking existing save files 
		
		public String getName()
		{
			return name().toLowerCase();
		}
	}
	
	public static final PropertyEnum MACHINE_TYPE = PropertyEnum.create("machine_type", MachineType.class);
	public static final PropertyDirection DIRECTION = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	public BlockMachine()
	{
		super(Material.rock);
		
		setUnlocalizedName("blockMachine");
		setHardness(3.0F);
		setDefaultState(getDefaultState().withProperty(DIRECTION, EnumFacing.SOUTH));
		this.setCreativeTab(Minestuck.tabMinestuck);

	}
	
	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, MACHINE_TYPE, DIRECTION);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return ((MachineType) state.getValue(MACHINE_TYPE)).ordinal();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(MACHINE_TYPE, MachineType.values()[meta]);
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if(tileEntity != null && tileEntity instanceof TileEntityMachine)
		{
			return state.withProperty(DIRECTION, EnumFacing.getHorizontal(((TileEntityMachine) tileEntity).rotation));
		}
		else return state;
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		List<ItemStack> list = new ArrayList<ItemStack>();
		ItemStack stack = new ItemStack(Item.getItemFromBlock(this), 1, ((MachineType)state.getValue(MACHINE_TYPE)).ordinal());
		
		if(((MachineType)state.getValue(MACHINE_TYPE)) == MachineType.CRUXTRUDER && world.getTileEntity(pos) instanceof TileEntityMachine)
		{
			TileEntityMachine machine = (TileEntityMachine) world.getTileEntity(pos);
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setTag("BlockEntityTag", new NBTTagCompound());
			stack.getTagCompound().getCompoundTag("BlockEntityTag").setInteger("color", machine.color);
		}
		list.add(stack);
		
		return list;
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List subItems) 
	{
		for(int i = 0; i < iconNames.length; i++)
			subItems.add(new ItemStack(this, 1, i));
	}
	
	@Override
	public boolean isFullCube()
	{
		return false;
	}
	
	@Override
	public int getRenderType()
	{
		return 3;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		TileEntityMachine tileEntity = (TileEntityMachine) worldIn.getTileEntity(pos);
		if (!worldIn.isRemote)
		{
			tileEntity.owner = playerIn;
		}
		if (tileEntity == null || playerIn.isSneaking())
		{
			return false;
		}
		
		playerIn.openGui(Minestuck.instance, GuiHandler.GuiId.MACHINE.ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}
	
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		dropItems(worldIn, pos);
		super.breakBlock(worldIn, pos, state);
	}
	
	private void dropItems(World world, BlockPos pos)
	{
		Random rand = new Random();

		TileEntity tileEntity = world.getTileEntity(pos);
		if (!(tileEntity instanceof IInventory))
		{
			return;
		}
		IInventory inventory = (IInventory) tileEntity;

		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack item = inventory.getStackInSlot(i);

			if (item != null && item.stackSize > 0) {
				float rx = rand.nextFloat() * 0.8F + 0.1F;
				float ry = rand.nextFloat() * 0.8F + 0.1F;
				float rz = rand.nextFloat() * 0.8F + 0.1F;

				EntityItem entityItem = new EntityItem(world,
						pos.getX() + rx, pos.getY() + ry, pos.getZ() + rz,
						new ItemStack(item.getItem(), item.stackSize, item.getItemDamage()));

				if (item.hasTagCompound()) {
					entityItem.getEntityItem().setTagCompound((NBTTagCompound) item.getTagCompound().copy());
				}

				float factor = 0.05F;
				entityItem.motionX = rand.nextGaussian() * factor;
				entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
				entityItem.motionZ = rand.nextGaussian() * factor;
				world.spawnEntityInWorld(entityItem);
				item.stackSize = 0;
				Debug.print("Spawning item "+entityItem.getEntityItem());
			}
		}
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		byte l = (byte) ((MathHelper.floor_double((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) + 2) & 3);
		TileEntityMachine machine = (TileEntityMachine) worldIn.getTileEntity(pos);
		machine.rotation = l;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metaData) {
		return new TileEntityMachine();
	}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
	{
		AxisAlignedBB bb = getBoundingBox(getActualState(worldIn.getBlockState(pos), worldIn, pos));
		setBlockBounds((float) bb.minX, (float) bb.minY, (float) bb.minZ, (float) bb.maxX, (float) bb.maxY, (float) bb.maxZ);
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
	{
		return getBoundingBox(getActualState(state, worldIn, pos)).offset(pos.getX(), pos.getY(), pos.getZ());
	}
	
	@Override
	public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity)
	{
		super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
		if(state.getValue(MACHINE_TYPE).equals(MachineType.ALCHEMITER))
		{
			AxisAlignedBB bb = new AxisAlignedBB(0, 2/16D, 0, 4.5/16D, 1, 1/8D);
			bb = rotate(bb, (EnumFacing) getActualState(state, worldIn, pos).getValue(DIRECTION)).offset(pos.getX(), pos.getY(), pos.getZ());
			if(mask.intersectsWith(bb))
				list.add(bb);
		}
	}
	
	public AxisAlignedBB getBoundingBox(IBlockState state)
	{
		EnumFacing rotation = (EnumFacing) state.getValue(DIRECTION);
		MachineType type = (MachineType) state.getValue(MACHINE_TYPE);
		AxisAlignedBB bb = null;
		switch(type)
		{
		case ALCHEMITER:
			bb = new AxisAlignedBB(0, 0, 0, 1, 1/2D, 1);
			break;
		case CRUXTRUDER:
			bb = new AxisAlignedBB(0, 0, 0, 1, 15/16D, 1);
			break;
		case GRIST_WIDGET:
			bb = new AxisAlignedBB(0, 0, 1/4D, 1, 1/4D, 3/4D);
			break;
		case PUNCH_DESIGNIX:
			bb = new AxisAlignedBB(0, 0, 0, 1, 1, 5/8D);
			break;
		case TOTEM_LATHE:
			bb = new AxisAlignedBB(0, 0, 5/16D, 1, 1, 11/16D);
			break;
		}
		return rotate(bb, rotation);
	}
	
	public static AxisAlignedBB rotate(AxisAlignedBB bb, EnumFacing facing)
	{
		switch(facing)
		{
		case SOUTH:
			return bb;
		case EAST:
			return new AxisAlignedBB(bb.minZ, bb.minY, 1-bb.maxX, bb.maxZ, bb.maxY, 1-bb.minX);
		case NORTH:
			return new AxisAlignedBB(1-bb.maxX, bb.minY, 1-bb.maxZ, 1-bb.minX, bb.maxY, 1-bb.minZ);
		case WEST:
			return new AxisAlignedBB(1-bb.maxZ, bb.minY, bb.minX, 1-bb.minZ, bb.maxY, bb.maxX);
		default:
			return null;
		}
	}
	
}

package com.mraof.minestuck.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.base.Predicate;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.GuiHandler;
import com.mraof.minestuck.tileentity.TileEntityMachine;

public class BlockMachine extends BlockContainer {

	public static final String[] iconNames = {"Cruxtruder","PunchDesignix","TotemLathe","Alchemiter","GristWidget"}; //PhernaliaFrame
	
	public static enum MachineTypes implements IStringSerializable
	{
		CRUXTRUDER,
		PUNCH_DESIGNIX,
		TOTEM_LATHE,
		ALCHEMITER,
		GRIST_WIDGET;
		
		public String getName()
		{
			return name().toLowerCase();
		}
	}
	
	public static final PropertyEnum MACHINE_TYPE = PropertyEnum.create("machine_type", MachineTypes.class);
	public static final PropertyDirection DIRECTION = PropertyDirection.create("facing", new Predicate<EnumFacing>()
			{
				@Override
				public boolean apply(EnumFacing input)
				{
					return input.getAxis().isHorizontal();
				}
			});
	
	public BlockMachine() {
		super(Material.rock);
		
		setUnlocalizedName("blockMachine");
		setHardness(3.0F);
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
		return ((MachineTypes) state.getValue(MACHINE_TYPE)).ordinal()*4 + ((EnumFacing)state.getValue(DIRECTION)).ordinal() - 2;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(MACHINE_TYPE, MachineTypes.values()[meta/4]).withProperty(DIRECTION, EnumFacing.values()[(meta%4) + 2]);
	}
	
	@Override
	public int damageDropped(IBlockState state)
	{
		return ((MachineTypes)state.getValue(MACHINE_TYPE)).ordinal();
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
	public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn)
	{
		dropItems(worldIn, pos.getX(), pos.getY(), pos.getZ());
		super.onBlockDestroyedByExplosion(worldIn, pos, explosionIn);
	}
	
	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state)
	{
		dropItems(worldIn, pos.getX(), pos.getY(), pos.getZ());
		super.onBlockDestroyedByPlayer(worldIn, pos, state);
	}
	
	private void dropItems(World world, int x, int y, int z){
		Random rand = new Random();

		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
		if (!(tileEntity instanceof IInventory)) {
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
						x + rx, y + ry, z + rz,
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
			}
		}
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		int l = MathHelper.floor_double((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		EnumFacing[] directions = {EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST};
		worldIn.setBlockState(pos, state.withProperty(DIRECTION, directions[l]), 2);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metaData) {
		return new TileEntityMachine();
	}
	
	private int[] harvestLevel = new int[] {-1, -1, -1, -1, -1, -1, -1, -1};
	private String[] harvestTool = new String[harvestLevel.length];
	@Override
	public void setHarvestLevel(String toolClass, int level, IBlockState state)
	{
		super.setHarvestLevel(toolClass, level, state);
		int idx = this.getMetaFromState(state)/4;
		this.harvestTool[idx] = toolClass;
		this.harvestLevel[idx] = level;
	}
	@Override
	public int getHarvestLevel(IBlockState state)
	{
		return harvestLevel[getMetaFromState(state)/4];
	}
	@Override
	public String getHarvestTool(IBlockState state)
	{
		return harvestTool[getMetaFromState(state)/4];
	}
}

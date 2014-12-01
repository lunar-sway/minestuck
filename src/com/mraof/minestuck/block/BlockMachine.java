package com.mraof.minestuck.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
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
import net.minecraft.util.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.GuiHandler;
import com.mraof.minestuck.tileentity.TileEntityMachine;

public class BlockMachine extends BlockContainer {

	public static final String[] iconNames = {"Cruxtruder","PunchDesignix","TotemLathe","Alchemiter","GristWidget"}; //PhernaliaFrame
//	private IIcon[] textures;

	public BlockMachine() {
		super(Material.rock);
		
		setUnlocalizedName("blockMachine");
		setHardness(3.0F);
		this.setCreativeTab(Minestuck.tabMinestuck);

	}

//	@Override
//	public IIcon getIcon(int side, int metadata) 
//	{
//		return textures[metadata];
//	}
	
//	@Override
//	public int damageDropped(int metadata) 
//	{
//		return metadata;
//	}

//	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List subItems) 
	{
		for(int i = 0; i < iconNames.length; i++)
			subItems.add(new ItemStack(this, 1, i));
	}
//	@Override
//	public boolean renderAsNormalBlock()
//	{
//		return false;
//	}

	@Override
	public int getRenderType()
	{
		return -1;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}


//	@Override
//	@SideOnly(Side.CLIENT)
//	public void registerBlockIcons(IIconRegister par1IconRegister)
//	{
//		this.textures = new IIcon[iconNames.length];
//
//		for (int i = 0; i < this.textures.length; i++)
//			this.textures[i] = par1IconRegister.registerIcon("minestuck:" + iconNames[i]);
//	}

//	@Override
//	public boolean onBlockActivated(World world, int x,int y,int z, EntityPlayer player,int par6, float par7, float par8, float par9) {
//		TileEntityMachine tileEntity = (TileEntityMachine) world.getTileEntity(x, y, z);
//		if (!world.isRemote) {
//			tileEntity.owner = player;
//		}
//		if (tileEntity == null || player.isSneaking()) {
//			return false;
//		}
//
//
//		player.openGui(Minestuck.instance, GuiHandler.GuiId.MACHINE.ordinal(), world, x, y, z);
//		return true;
//	}
	
//	@Override
//	public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion explosion) {
//		dropItems(world, x, y, z);
//		super.onBlockDestroyedByExplosion(world, x, y, z, explosion);
//	}
	
//	@Override
//	public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int metaData) {
//		dropItems(world, x, y, z);
//		super.onBlockDestroyedByPlayer(world, x, y, z, metaData);
//	}
	
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
	
//	@Override
	public TileEntity createTileEntity(World world, int metaData) {
		return new TileEntityMachine();
	}
	
//	@Override
//	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) 
//	{
//		super.onBlockPlacedBy(world, x, y, z, par5EntityLivingBase, par6ItemStack);
//		int l = MathHelper.floor_double((double)(par5EntityLivingBase.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
//		TileEntityMachine tileEntity = (TileEntityMachine) world.getTileEntity(x, y, z);
//		tileEntity.rotation = (byte) l;
//
//	}

	@Override
	public TileEntity createNewTileEntity(World world, int metaData) {
		return createTileEntity(world, metaData);
	}
}

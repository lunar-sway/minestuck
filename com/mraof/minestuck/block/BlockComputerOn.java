package com.mraof.minestuck.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.tileentity.TileEntityComputer;
import com.mraof.minestuck.util.Debug;

public class BlockComputerOn extends Block implements ITileEntityProvider {
	
	private Icon frontIcon;
	private Icon sideIcon;

	public BlockComputerOn(int id)
	{
		super(id, Material.rock);
		setUnlocalizedName("computer");
		setHardness(4.0F);
		//this.setCreativeTab(Minestuck.tabMinestuck);
		
	}
	
	@Override
	 public Icon getIcon(int par1, int par2)
	    {
			if (par2 == 0 && par1 == 3) {return this.frontIcon;}
	        return par1 != par2 ? this.sideIcon : this.frontIcon;
	    }

	    private void setDefaultDirection(World par1World, int par2, int par3, int par4)
	    {
	        if (!par1World.isRemote)
	        {
	            int l = par1World.getBlockId(par2, par3, par4 - 1);
	            int i1 = par1World.getBlockId(par2, par3, par4 + 1);
	            int j1 = par1World.getBlockId(par2 - 1, par3, par4);
	            int k1 = par1World.getBlockId(par2 + 1, par3, par4);
	            byte b0 = 3;

	            if (Block.opaqueCubeLookup[l] && !Block.opaqueCubeLookup[i1])
	            {
	                b0 = 3;
	            }

	            if (Block.opaqueCubeLookup[i1] && !Block.opaqueCubeLookup[l])
	            {
	                b0 = 2;
	            }

	            if (Block.opaqueCubeLookup[j1] && !Block.opaqueCubeLookup[k1])
	            {
	                b0 = 5;
	            }

	            if (Block.opaqueCubeLookup[k1] && !Block.opaqueCubeLookup[j1])
	            {
	                b0 = 4;
	            }

	            par1World.setBlockMetadataWithNotify(par2, par3, par4, b0, 2);
	        }
	    }
	    
	    @Override
	    public void registerIcons(IconRegister par1IconRegister)
	    {
	            this.frontIcon = par1IconRegister.registerIcon("minestuck:ComputerFront");
	            this.sideIcon =  par1IconRegister.registerIcon("minestuck:PhernaliaFrame");
	    }
	    
	    @Override
	    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack)
	    {
	        int l = MathHelper.floor_double((double)(par5EntityLivingBase.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

	        if (l == 0)
	        {
	            par1World.setBlockMetadataWithNotify(par2, par3, par4, 2, 2);
	        }

	        if (l == 1)
	        {
	            par1World.setBlockMetadataWithNotify(par2, par3, par4, 5, 2);
	        }

	        if (l == 2)
	        {
	            par1World.setBlockMetadataWithNotify(par2, par3, par4, 3, 2);
	        }

	        if (l == 3)
	        {
	            par1World.setBlockMetadataWithNotify(par2, par3, par4, 4, 2);
	        }
	    }
	    
	    @Override
	    public void onBlockAdded(World par1World, int par2, int par3, int par4)
	    {
	        super.onBlockAdded(par1World, par2, par3, par4);
	        this.setDefaultDirection(par1World, par2, par3, par4);
	    }
	    
		@Override
		public boolean onBlockActivated(World world, int x,int y,int z, EntityPlayer player,int par6, float par7, float par8, float par9) {
			TileEntityComputer tileEntity = (TileEntityComputer) world.getBlockTileEntity(x, y, z);
			ItemStack item = player.getCurrentEquippedItem();
			if (tileEntity == null || player.isSneaking() || item != null && item.itemID == Minestuck.disk.itemID && (item.getItemDamage() == 0 && !tileEntity.hasClient || item.getItemDamage() == 1 && !tileEntity.hasServer)) {
				return false;
			}

			player.openGui(Minestuck.instance, 1, world, x, y, z);
			return true;
		}
		
		@Override
		public TileEntity createNewTileEntity(World world) {
			return new TileEntityComputer();
		}
		
		@Override
		public int idDropped(int par1, Random random, int par2) {
			return Minestuck.blockComputerOff.blockID;
			
		}
		
		@Override
		public int idPicked(World par1World, int par2, int par3, int par4) {
			return Minestuck.blockComputerOff.blockID;
			
		}
		
		@Override
		public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
			dropItems(world, x, y, z);
			super.breakBlock(world, x, y, z, par5, par6);
		}

		private void dropItems(World world, int x, int y, int z){
			Random rand = new Random();
			TileEntityComputer te = (TileEntityComputer) world.getBlockTileEntity(x, y, z);
			if (te == null) {
				return;
			}
			float factor = 0.05F;
			if(te.hasClient) {
				float rx = rand.nextFloat() * 0.8F + 0.1F;
				float ry = rand.nextFloat() * 0.8F + 0.1F;
				float rz = rand.nextFloat() * 0.8F + 0.1F;
				EntityItem entityItem = new EntityItem(world, x + rx, y + ry, z + rz, new ItemStack(Minestuck.disk.itemID, 1, 0));
				entityItem.motionX = rand.nextGaussian() * factor;
				entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
				entityItem.motionZ = rand.nextGaussian() * factor;
				world.spawnEntityInWorld(entityItem);
			}
			if(te.hasServer) {
				float rx = rand.nextFloat() * 0.8F + 0.1F;
				float ry = rand.nextFloat() * 0.8F + 0.1F;
				float rz = rand.nextFloat() * 0.8F + 0.1F;
				EntityItem entityItem = new EntityItem(world, x + rx, y + ry, z + rz, new ItemStack(Minestuck.disk.itemID, 1, 1));
				entityItem.motionX = rand.nextGaussian() * factor;
				entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
				entityItem.motionZ = rand.nextGaussian() * factor;
				world.spawnEntityInWorld(entityItem);
			}
		}
}

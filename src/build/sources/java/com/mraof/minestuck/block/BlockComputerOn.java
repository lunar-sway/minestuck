package com.mraof.minestuck.block;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.GuiHandler;
import com.mraof.minestuck.network.skaianet.SkaiaClient;
import com.mraof.minestuck.tileentity.TileEntityComputer;
import com.mraof.minestuck.util.Debug;

public class BlockComputerOn extends Block implements ITileEntityProvider {
	
	private IIcon frontIcon;
	private IIcon sideIcon;
	private IIcon bsodIcon;

	public BlockComputerOn()
	{
		super(Material.rock);
		
		setBlockName("computer");
		setHardness(4.0F);
	}
	
	@Override
	public IIcon getIcon(int side, int meta)
	{
		int front = meta / 6;	//The texture displayed at front
		int rotation = meta == 0?3:meta % 6;	//Rotation of the computer
		
		if(side != rotation)
			return this.sideIcon;
		else {
			switch(front) {
			case 1: return this.bsodIcon;
			default: return this.frontIcon;
			}
		}
	}
	
	    private void setDefaultDirection(World par1World, int par2, int par3, int par4)
	    {
	    	if (!par1World.isRemote)
			{
				Block block = par1World.getBlock(par2, par3, par4 - 1);
				Block block1 = par1World.getBlock(par2, par3, par4 + 1);
				Block block2 = par1World.getBlock(par2 - 1, par3, par4);
				Block block3 = par1World.getBlock(par2 + 1, par3, par4);
				byte b0 = 3;

				if (block.func_149730_j() && !block1.func_149730_j())
				{
					b0 = 3;
				}

				if (block1.func_149730_j() && !block.func_149730_j())
	            {
	                b0 = 2;
	            }

	            if (block2.func_149730_j() && !block3.func_149730_j())
	            {
	                b0 = 5;
	            }

	            if (block3.func_149730_j() && !block2.func_149730_j())
	            {
	                b0 = 4;
	            }

				par1World.setBlockMetadataWithNotify(par2, par3, par4, b0, 2);
			}
	    }
	    
	    @Override
	    public void registerBlockIcons(IIconRegister par1IconRegister)
	    {
	            this.frontIcon = par1IconRegister.registerIcon("minestuck:ComputerFront");
	            this.sideIcon =  par1IconRegister.registerIcon("minestuck:PhernaliaFrame");
	            this.bsodIcon = par1IconRegister.registerIcon("minestuck:BsodFront");
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
		TileEntityComputer tileEntity = (TileEntityComputer) world.getTileEntity(x, y, z);
		ItemStack item = player.getCurrentEquippedItem();
		if(tileEntity != null && item != null && item.getItem() == Items.record_11 && 
				tileEntity.installedPrograms.size() < 2 && !tileEntity.errored()) {
			player.destroyCurrentEquippedItem();
			tileEntity.installedPrograms.put(-1, true);
			tileEntity.closeConnections();
			world.setBlockMetadataWithNotify(x, y, z, (world.getBlockMetadata(x, y, z) % 6) + 6, 2);
			return true;
		}
		
		if (tileEntity == null || player.isSneaking() || item != null && item.getItem() == Minestuck.disk && (item.getItemDamage() == 0 && !tileEntity.hasClient() || item.getItemDamage() == 1 && !tileEntity.hasServer())) {
			return false;
		}
		
		if(world.isRemote && SkaiaClient.requestData((TileEntityComputer) tileEntity))
			player.openGui(Minestuck.instance, GuiHandler.GuiId.COMPUTER.ordinal(), world, x, y, z);
		
		return true;
	}
	
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityComputer();
	}
	
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		ArrayList list = new ArrayList();
		list.add(new ItemStack(Minestuck.blockComputerOff));
		
		return list;
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int metaData) {
		Debug.print("Break block");
		dropItems(world, x, y, z);
		super.breakBlock(world, x, y, z, block, metaData);
	}
	
	private void dropItems(World world, int x, int y, int z){
		Random rand = new Random();
		TileEntityComputer te = (TileEntityComputer) world.getTileEntity(x, y, z);
		if (te == null) {
			return;
		}
		te.closeConnections();
		float factor = 0.05F;
		Debug.print("DropItems called");
		Iterator it = te.installedPrograms.entrySet().iterator();
	   	int place = 0;
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            int program = (Integer) pairs.getKey();
            
			float rx = rand.nextFloat() * 0.8F + 0.1F;
			float ry = rand.nextFloat() * 0.8F + 0.1F;
			float rz = rand.nextFloat() * 0.8F + 0.1F;
			EntityItem entityItem = new EntityItem(world, x + rx, y + ry, z + rz, program != -1?new ItemStack(Minestuck.disk, 1, program):new ItemStack(Items.record_11,1));
			entityItem.motionX = rand.nextGaussian() * factor;
			entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
			entityItem.motionZ = rand.nextGaussian() * factor;
			world.spawnEntityInWorld(entityItem);
            
            place++;
        }
	}
	
}

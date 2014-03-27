package com.mraof.minestuck.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.FillBucketEvent;

import com.mraof.minestuck.Minestuck;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemMinestuckBucket extends ItemBucket 
{
	public List<Block> fillFluids = new ArrayList<Block>();
	public HashMap<Block, Integer> FillFluidIds = new HashMap<Block, Integer>();
	public HashMap<String, String> textureFiles = new HashMap<String, String>();
	HashMap<String, IIcon> textures = new HashMap<String, IIcon>();
	
	public ItemMinestuckBucket() 
	{
		super(Blocks.air);
		setUnlocalizedName("minestuckBucket");
		setCreativeTab(Minestuck.tabMinestuck);
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
	 */
	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(par2World, par3EntityPlayer, false);

		if (movingobjectposition == null)
		{
			return par1ItemStack;
		}
		else
		{
			FillBucketEvent event = new FillBucketEvent(par3EntityPlayer, par1ItemStack, par2World, movingobjectposition);
			if (MinecraftForge.EVENT_BUS.post(event))
			{
				return par1ItemStack;
			}

			if (event.getResult() == Event.Result.ALLOW)
			{
				if (par3EntityPlayer.capabilities.isCreativeMode)
				{
					return par1ItemStack;
				}

				if (--par1ItemStack.stackSize <= 0)
				{
					return event.result;
				}

				if (!par3EntityPlayer.inventory.addItemStackToInventory(event.result))
				{
					par3EntityPlayer.func_146097_a(event.result, false, false);
				}

				return par1ItemStack;
			}

			if (movingobjectposition.typeOfHit == MovingObjectType.BLOCK)
			{
				int x = movingobjectposition.blockX;
				int y = movingobjectposition.blockY;
				int z = movingobjectposition.blockZ;

				if (!par2World.canMineBlock(par3EntityPlayer, x, y, z))
				{
					return par1ItemStack;
				}

				if (par1ItemStack.getItemDamage() < 0)
				{
					return new ItemStack(Items.bucket);
				}
				
				switch(movingobjectposition.sideHit)
				{
				case 0:
					--y; 
					break;
				case 1:
					++y; 
					break;
				case 2:
					--z; 
					break;
				case 3:
					++z; 
					break;
				case 4:
					--x; 
					break;
				case 5:
					++x; 
					break;
				}


				if (!par3EntityPlayer.canPlayerEdit(x, y, z, movingobjectposition.sideHit, par1ItemStack))
				{
					return par1ItemStack;
				}

				if (this.tryPlaceContainedLiquid(par2World, x, y, z, fillFluids.get(par1ItemStack.getItemDamage())) && !par3EntityPlayer.capabilities.isCreativeMode)
				{
					return new ItemStack(Items.bucket);
				}
			}

			return par1ItemStack;
		}
	}

	/**
	 * Attempts to place the liquid contained inside the bucket.
	 */
	public boolean tryPlaceContainedLiquid(World par1World, int par2, int par3, int par4, Block block)
	{
		Material material = par1World.getBlock(par2, par3, par4).getMaterial();
		boolean flag = !material.isSolid();

		if (!par1World.isAirBlock(par2, par3, par4) && !flag)
		{
			return false;
		}
		else
		{
			if (!par1World.isRemote && flag && !material.isLiquid())
			{
				par1World.func_147480_a(par2, par3, par4, true);
			}

			par1World.setBlock(par2, par3, par4, block, 0, 3);

			return true;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List subItems)
	{
		for(int id = 0; id < fillFluids.size(); id++)
			subItems.add(new ItemStack(this, 1, id));
	}
	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) 
	{
		return getUnlocalizedName() + "." + fillFluids.get(par1ItemStack.getItemDamage()).getUnlocalizedName().replace("tile.", "");
	}
	@Override
	public IIcon getIconFromDamage(int damage) 
	{
		return this.textures.get(fillFluids.get(damage).getUnlocalizedName());
	}
	@Override
	public void registerIcons(IIconRegister par1IconRegister)
    {
        for (Entry<String, String> entry : textureFiles.entrySet())
            this.textures.put(entry.getKey(), par1IconRegister.registerIcon("minestuck:" + entry.getValue()));
    }
	
	public void addBlock(Block block, String textureFile)
	{
		textureFiles.put(block.getUnlocalizedName(), textureFile);
		fillFluids.add(block);
		FillFluidIds.put(block, fillFluids.size() - 1);
		//TODO make it actually work
	}


}

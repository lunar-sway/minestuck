package com.mraof.minestuck.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mraof.minestuck.Minestuck;

public class ItemMinestuckBucket extends ItemBucket 
{
	public List<Block> fillFluids = new ArrayList<Block>();
	
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

			if (event.getResult() == Result.ALLOW)
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
					par3EntityPlayer.dropItem(event.result, false, false);
				}

				return par1ItemStack;
			}

			if (movingobjectposition.typeOfHit == MovingObjectType.BLOCK)
			{
				BlockPos pos = movingobjectposition.getBlockPos();

				if (!par2World.canMineBlockBody(par3EntityPlayer, pos))
				{
					return par1ItemStack;
				}

				if (par1ItemStack.getItemDamage() < 0)
				{
					return new ItemStack(Items.bucket);
				}
				
				pos = pos.offset(movingobjectposition.sideHit);
				
				if (!par3EntityPlayer.canPlayerEdit(pos, movingobjectposition.sideHit, par1ItemStack))
				{
					return par1ItemStack;
				}

				if (this.tryPlaceContainedLiquid(par2World, pos, fillFluids.get(par1ItemStack.getItemDamage())) && !par3EntityPlayer.capabilities.isCreativeMode)
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
	public boolean tryPlaceContainedLiquid(World par1World, BlockPos pos, Block block)
	{
		Material material = par1World.getBlockState(pos).getBlock().getMaterial();
		boolean flag = !material.isSolid();

		if (!par1World.isAirBlock(pos) && !flag)
		{
			return false;
		}
		else
		{
			if (!par1World.isRemote && flag && !material.isLiquid())
			{
				par1World.destroyBlock(pos, true);
			}
			
			par1World.setBlockState(pos, block.getDefaultState(), 3);
			
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
	
	public void addBlock(Block block)
	{
		fillFluids.add(block);
	}


}

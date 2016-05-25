package com.mraof.minestuck.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mraof.minestuck.Minestuck;

public class ItemMinestuckBucket extends ItemBucket	//Unsure if anything more should update for 1.9
{
	public List<IBlockState> fillFluids = new ArrayList<IBlockState>();
	
	public ItemMinestuckBucket() 
	{
		super(Blocks.air);
		setUnlocalizedName("minestuckBucket");
		setCreativeTab(Minestuck.tabMinestuck);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, EnumHand hand)
	{
		RayTraceResult raytraceresult = this.getMovingObjectPositionFromPlayer(world, player, false);
		
		ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(player, world, itemStack, raytraceresult);
		if (ret != null) return ret;
		
		if (raytraceresult == null)
		{
			return new ActionResult(EnumActionResult.PASS, itemStack);
		}
		else if (raytraceresult.typeOfHit != RayTraceResult.Type.BLOCK)
		{
			return new ActionResult(EnumActionResult.PASS, itemStack);
		}
		else
		{
			BlockPos blockpos = raytraceresult.getBlockPos();
			
			if (!world.isBlockModifiable(player, blockpos))
			{
				return new ActionResult(EnumActionResult.FAIL, itemStack);
			}
			else
			{
				boolean flag1 = world.getBlockState(blockpos).getBlock().isReplaceable(world, blockpos);
				BlockPos blockpos1 = flag1 && raytraceresult.sideHit == EnumFacing.UP ? blockpos : blockpos.offset(raytraceresult.sideHit);
				
				if (!player.canPlayerEdit(blockpos1, raytraceresult.sideHit, itemStack))
				{
					return new ActionResult(EnumActionResult.FAIL, itemStack);
				}
				else if (this.tryPlaceContainedLiquid(player, world, blockpos1, fillFluids.get(itemStack.getItemDamage())))
				{
					player.addStat(StatList.func_188057_b(this));
					return new ActionResult(EnumActionResult.SUCCESS, !player.capabilities.isCreativeMode ? new ItemStack(Items.bucket) : itemStack);
				}
				else
				{
					return new ActionResult(EnumActionResult.FAIL, itemStack);
				}
			}
		}
	}

	/**
	 * Attempts to place the liquid contained inside the bucket.
	 */
	public boolean tryPlaceContainedLiquid(EntityPlayer player, World world, BlockPos pos, IBlockState block)
	{
		Material material = world.getBlockState(pos).getMaterial();
		boolean flag = !material.isSolid();

		if (!world.isAirBlock(pos) && !flag)
		{
			return false;
		}
		else
		{
			if (!world.isRemote && flag && !material.isLiquid())
			{
				world.destroyBlock(pos, true);
			}

			world.playSound(player, pos, SoundEvents.item_bucket_empty, SoundCategory.BLOCKS, 1.0F, 1.0F);
			world.setBlockState(pos, block, 3);
			
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
		return getUnlocalizedName() + "." + fillFluids.get(par1ItemStack.getItemDamage()).getBlock().getUnlocalizedName().replace("tile.", "");
	}
	
	public void addBlock(IBlockState block)
	{
		fillFluids.add(block);
	}
	
}
package com.mraof.minestuck.item;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemNet extends Item
{
	
	public ItemNet()
	{
		setUnlocalizedName("net");
		setCreativeTab(TabMinestuck.instance);
		setMaxStackSize(1);
		setMaxDamage(64);
	}
	
	
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos,
			EntityLivingBase entityLiving) {
		
		if(entityLiving instanceof EntityPlayer)
		{
			EntityPlayer playerIn = (EntityPlayer) entityLiving;
			if(!playerIn.isCreative() && worldIn.getBlockState(pos).getBlock() == Blocks.TALLGRASS)
			{
				Random rand = playerIn.getRNG();
				
				if(!worldIn.isRemote)
				{
					if(rand.nextInt(555) == 0)
					{
						EntityItem item = new EntityItem(worldIn, pos.getX(), pos.getY() + 0.5, pos.getZ(), new ItemStack(MinestuckItems.goldenGrasshopper, 1));
						worldIn.spawnEntity(item);
						playerIn.getHeldItemMainhand().damageItem(1, playerIn);
						
						return true;
					}
					else if(rand.nextInt(5) == 0)
					{
						EntityItem item = new EntityItem(worldIn, pos.getX(), pos.getY() + 0.5, pos.getZ(), new ItemStack(MinestuckItems.grasshopper, 1));
						worldIn.spawnEntity(item);
						playerIn.getHeldItemMainhand().damageItem(1, playerIn);
						
						return true;
					}
				}
			}
		}
		return false;
	}
	
}
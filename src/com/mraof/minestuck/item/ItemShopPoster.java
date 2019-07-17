package com.mraof.minestuck.item;

import com.mraof.minestuck.item.enums.EnumShopPoster;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityHanging;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ItemShopPoster  extends ItemHanging
{
	public ItemShopPoster()
    {
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(TabMinestuck.instance);
    }
	
	 public String getUnlocalizedName(ItemStack stack)
	    {
	        int i = stack.getMetadata();
	        return super.getUnlocalizedName() + "." + EnumShopPoster.byMetadata(i).getUnlocalizedName();
	    }
	
	 public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	    {
	        if (this.isInCreativeTab(tab))
	        {
	        	//TODO
	            for (int i = 0; i < 5; /*i += 2*/ ++i)
	            {
	                items.add(new ItemStack(this, 1, i));
	            }
	        }
	    }
	
	@Override
	public abstract EntityHanging createEntity(World worldIn, BlockPos pos, EnumFacing facing, ItemStack stack, int meta);
	
	
}


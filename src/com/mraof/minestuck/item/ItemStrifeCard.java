package com.mraof.minestuck.item;

import java.util.List;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.GuiHandler;
import com.mraof.minestuck.inventory.specibus.StrifePortfolioHandler;
import com.mraof.minestuck.inventory.specibus.StrifeSpecibus;
import com.mraof.minestuck.util.KindAbstratusList;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemStrifeCard extends Item
{
	public ItemStrifeCard()
	{
		setUnlocalizedName("strifeCard");
		setMaxStackSize(1);
		setCreativeTab(TabMinestuck.instance);
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) 
	{
		if(stack.hasTagCompound())
		{
			NBTTagCompound nbt = stack.getTagCompound();
			
			if(nbt.hasKey("abstrata"))
			{
				String kind = KindAbstratusList.getTypeList().get(nbt.getInteger("abstrata")).getDisplayName();
				tooltip.add("("+kind+")");
				if(nbt.hasKey("items"))
				{
					NBTTagCompound items = (NBTTagCompound) nbt.getTag("items");
					for(int i = 0; i < items.getSize(); i++)
						if(items.hasKey("slot"+i))
						{
							ItemStack s = new ItemStack(items.getCompoundTag("slot"+i));
							tooltip.add(s.getDisplayName());
						}
						else break;
				}
				
			}
			else
			{
				tooltip.add("("+I18n.translateToLocal("item.strifeCard.invalid")+")");
			}
		}
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) 
	{
		ItemStack stack = playerIn.getHeldItem(handIn);
		if(!stack.hasTagCompound())
		{
			BlockPos pos = playerIn.getPosition();
			playerIn.openGui(Minestuck.instance, GuiHandler.GuiId.STRIFE_CARD.ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		else if(worldIn.isRemote)
		{
			NBTTagCompound nbt = stack.getTagCompound();
			
			if(nbt.hasKey("abstrata"))
			{
				StrifeSpecibus specibus = new StrifeSpecibus(nbt.getInteger("abstrata"));
				if(nbt.hasKey("items"))
				{
					NBTTagCompound items = (NBTTagCompound) nbt.getTag("items");
					for(int i = 0; i < items.getSize(); i++)
						if(items.hasKey("slot"+i))
							specibus.putItemStack(new ItemStack(items.getCompoundTag("slot"+i)));
						else break;
				}
				
				StrifePortfolioHandler.addSpecibus(playerIn, specibus);
				stack.shrink(1);
			}
			else
			{
				playerIn.sendStatusMessage(new TextComponentTranslation("The specibus' data is corrupted and can't be allocated."), false);
				return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
			}
		}

		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}
}

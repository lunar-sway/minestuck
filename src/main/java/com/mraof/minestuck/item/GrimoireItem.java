package com.mraof.minestuck.item;

import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class GrimoireItem extends Item
{
	
	public GrimoireItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		if(!worldIn.isRemote && playerIn != null)
		{
			ITextComponent message = new TranslationTextComponent("After flipping through some pages, you feel significantly more insignificant.");
			playerIn.sendMessage(message);
			playerIn.world.playSound(null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), MSSoundEvents.ITEM_GRIMOIRE_USE, SoundCategory.AMBIENT, 0.5F, 0.8F);
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
}
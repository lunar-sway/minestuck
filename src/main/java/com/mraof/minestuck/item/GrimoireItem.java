package com.mraof.minestuck.item;

import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.Util;
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
		playerIn.world.playSound(playerIn, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), MSSoundEvents.ITEM_GRIMOIRE_USE, SoundCategory.AMBIENT, 0.5F, 0.8F);
		if(worldIn.isRemote)
			playerIn.sendMessage(new TranslationTextComponent(getTranslationKey() + ".message"), Util.DUMMY_UUID);
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
}
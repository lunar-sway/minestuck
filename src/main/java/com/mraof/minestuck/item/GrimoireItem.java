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
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		playerIn.level.playSound(playerIn, playerIn.getX(), playerIn.getY(), playerIn.getZ(), MSSoundEvents.ITEM_GRIMOIRE_USE, SoundCategory.AMBIENT, 0.5F, 0.8F);
		if(worldIn.isClientSide)
			playerIn.sendMessage(new TranslationTextComponent(getDescriptionId() + ".message"), Util.NIL_UUID);
		return super.use(worldIn, playerIn, handIn);
	}
}
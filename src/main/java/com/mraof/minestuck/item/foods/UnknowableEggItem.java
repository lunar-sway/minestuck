package com.mraof.minestuck.item.foods;

import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class UnknowableEggItem extends Item {
	
	public UnknowableEggItem(Properties properties)
	{
		super(properties);
	}

	
	@Override
	public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity player)
	{
		if(!worldIn.isClientSide)
		{
			player.addEffect(new EffectInstance(Effects.CONFUSION, 160, 2));
			player.addEffect(new EffectInstance(Effects.POISON, 160, 2));
			player.level.playSound(null, player.getX(), player.getY(), player.getZ(), MSSoundEvents.ITEM_GRIMOIRE_USE, SoundCategory.AMBIENT, 1.0F, 0.8F);
		}
		return super.finishUsingItem(stack, worldIn, player);
	}
}
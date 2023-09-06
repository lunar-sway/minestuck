package com.mraof.minestuck.item.foods;

import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class UnknowableEggItem extends Item
{
	
	public UnknowableEggItem(Properties properties)
	{
		super(properties);
	}

	
	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity player)
	{
		if(!level.isClientSide)
		{
			player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 160, 2));
			player.addEffect(new MobEffectInstance(MobEffects.POISON, 160, 2));
			level.playSound(null, player.getX(), player.getY(), player.getZ(), MSSoundEvents.ITEM_GRIMOIRE_USE.get(), SoundSource.AMBIENT, 1.0F, 0.8F);
		}
		return super.finishUsingItem(stack, level, player);
	}
}
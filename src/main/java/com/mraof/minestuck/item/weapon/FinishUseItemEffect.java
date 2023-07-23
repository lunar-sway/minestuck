package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.item.MSItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public interface FinishUseItemEffect
{
	ItemStack onItemUseFinish(ItemStack stack, Level level, LivingEntity entityIn);
	
	
	FinishUseItemEffect SPAWN_BREADCRUMBS = (stack, level, entityIn) -> {
		if(!entityIn.level().isClientSide && entityIn instanceof Player player)
		{
			int num = entityIn.getRandom().nextInt(10);
			ItemStack crumbs = new ItemStack(MSItems.BREADCRUMBS.get(), num);
			
			player.addItem(crumbs);
		}
		return stack;
	};
	
	FinishUseItemEffect SHARPEN_CANDY_CANE = (stack, worldIn, entityIn) -> {
		if(entityIn instanceof Player player && !entityIn.level().isClientSide)
		{
			player.addItem(new ItemStack(MSItems.SHARP_CANDY_CANE.get(), 1));
		}
		stack.hurtAndBreak(999, entityIn, entity -> entity.broadcastBreakEvent(InteractionHand.MAIN_HAND));
		return stack;
	};
	
	static FinishUseItemEffect foodEffect(int healAmount, float saturationModifier)
	{
		return foodEffect(healAmount, saturationModifier, 50);
	}
	
	static FinishUseItemEffect foodEffect(int healAmount, float saturationModifier, int damageTaken)
	{
		return (stack, worldIn, entityIn) -> {
			stack.hurtAndBreak(damageTaken, entityIn, entity -> entity.broadcastBreakEvent(InteractionHand.MAIN_HAND));
			if(entityIn instanceof Player player)
			{
				player.getFoodData().eat(healAmount, saturationModifier);
				worldIn.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 0.5F, worldIn.random.nextFloat() * 0.1F + 0.9F);
			}
			return stack;
		};
	}
	
	static FinishUseItemEffect potionEffect(Supplier<MobEffectInstance> effect, float probability)
	{
		return (stack, worldIn, entityLiving) -> {
			if(!worldIn.isClientSide && worldIn.random.nextFloat() < probability)
				entityLiving.addEffect(effect.get());
			return stack;
		};
	}
}
package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.item.MSItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

import java.util.Random;
import java.util.function.Supplier;

public interface FinishUseItemEffect
{
	ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityIn);
	
	
	FinishUseItemEffect SPAWN_BREADCRUMBS = (stack, worldIn, entityIn) -> {
		if(!entityIn.world.isRemote && entityIn instanceof PlayerEntity)
		{
			Random rand = new Random();
			int num = rand.nextInt(10);
			ItemStack crumbs = new ItemStack(MSItems.BREADCRUMBS, num);
			
			PlayerEntity player = (PlayerEntity) entityIn;
			player.addItemStackToInventory(crumbs);
		}
		return stack;
	};
	
	FinishUseItemEffect SHARPEN_CANDY_CANE = (stack, worldIn, entityIn) -> {
		if(entityIn instanceof PlayerEntity && !entityIn.world.isRemote)
		{
			((PlayerEntity) entityIn).addItemStackToInventory(new ItemStack(MSItems.SHARP_CANDY_CANE, 1));
		}
		stack.damageItem(999, entityIn, entity -> entity.sendBreakAnimation(Hand.MAIN_HAND));
		return stack;
	};
	
	static FinishUseItemEffect foodEffect(int healAmount, float saturationModifier)
	{
		return foodEffect(healAmount, saturationModifier, 50);
	}
	
	static FinishUseItemEffect foodEffect(int healAmount, float saturationModifier, int damageTaken)
	{
		return (stack, worldIn, entityIn) -> {
			stack.damageItem(damageTaken, entityIn, entity -> entity.sendBreakAnimation(Hand.MAIN_HAND));
			if(entityIn instanceof PlayerEntity)
			{
				PlayerEntity player = (PlayerEntity) entityIn;
				player.getFoodStats().addStats(healAmount, saturationModifier);
				worldIn.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
			}
			return stack;
		};
	}
	
	static FinishUseItemEffect potionEffect(Supplier<EffectInstance> effect, float probability)
	{
		return (stack, worldIn, entityLiving) -> {
			if (!worldIn.isRemote && worldIn.rand.nextFloat() < probability)
				entityLiving.addPotionEffect(effect.get());
			return stack;
		};
	}
}
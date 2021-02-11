package com.mraof.minestuck.item.weapon;

import com.google.common.collect.ImmutableList;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import static com.mraof.minestuck.player.EnumAspect.*;

public interface OnHitEffect
{
	void onHit(ItemStack stack, LivingEntity target, LivingEntity attacker);
	
	OnHitEffect RAGE_STRENGTH = aspectEffect(RAGE, () -> new EffectInstance(Effects.STRENGTH, 80, 1), null);
	OnHitEffect HOPE_RESISTANCE = aspectEffect(HOPE, () -> new EffectInstance(Effects.RESISTANCE, 120, 2), null);
	OnHitEffect LIFE_SATURATION = aspectEffect(LIFE, () -> new EffectInstance(Effects.SATURATION, 1, 1), () -> new EffectInstance(Effects.HUNGER, 60, 100));
	
	OnHitEffect BREATH_LEVITATION_AOE = aspectAOE(BREATH, () -> new EffectInstance(Effects.LEVITATION, 30, 2), () -> SoundEvents.ENTITY_ENDER_DRAGON_FLAP,1.4F);
	OnHitEffect TIME_SLOWNESS_AOE = aspectAOE(TIME, () -> new EffectInstance(Effects.SLOWNESS, 100, 4), () -> SoundEvents.BLOCK_BELL_RESONATE, 2F);
	
	OnHitEffect SET_CANDY_DROP_FLAG = (stack, target, attacker) -> {
		if(target instanceof UnderlingEntity)
			((UnderlingEntity) target).dropCandy = true;
	};
	OnHitEffect ICE_SHARD = (stack, target, attacker) -> {
		target.playSound(SoundEvents.BLOCK_GLASS_BREAK, 0.25F, 1.5F);
		if(!target.world.isRemote && attacker instanceof PlayerEntity && attacker.getRNG().nextFloat() < .1)
		{
			target.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1.5F, 1F);
			target.attackEntityFrom(DamageSource.causePlayerDamage((PlayerEntity) attacker), 2);
			stack.damageItem(2, attacker, entity -> entity.sendBreakAnimation(Hand.MAIN_HAND));
			
			ItemEntity shardEntity = new ItemEntity(target.world, target.getPosX(), target.getPosY(), target.getPosZ(), new ItemStack(MSItems.ICE_SHARD, 1));
			target.world.addEntity(shardEntity);
		}
	};
	OnHitEffect KUNDLER_SURPRISE = (stack, target, attacker) -> {
		if(!attacker.world.isRemote && target.getHealth() <= 0 && attacker.getRNG().nextFloat() <= 0.20)
		{
			Random ran = new Random();
			//TODO Make this a loot table
			ItemStack[] items = new ItemStack[]{new ItemStack(Items.MELON_SLICE), new ItemStack(Items.STICK), new ItemStack(Items.EGG),
					new ItemStack(Blocks.DIRT), new ItemStack(Blocks.PUMPKIN), new ItemStack(Blocks.COBBLESTONE), new ItemStack(Items.REDSTONE),
					new ItemStack(MSItems.SURPRISE_EMBRYO), new ItemStack(MSItems.GAMEGRL_MAGAZINE), new ItemStack(MSItems.GAMEBRO_MAGAZINE),
					new ItemStack(Blocks.DEAD_HORN_CORAL)};
			int num = ran.nextInt(items.length);
			ItemEntity item = new ItemEntity(target.world, target.getPosX(), target.getPosY(), target.getPosZ(), items[num].copy());
			target.world.addEntity(item);
			
			ITextComponent message = new TranslationTextComponent(stack.getTranslationKey() + ".message", items[num].getDisplayName());
			attacker.sendMessage(message.applyTextStyle(TextFormatting.GOLD));
		}
	};
	
	OnHitEffect HORRORTERROR = (stack, target, attacker) -> {
		if(!attacker.world.isRemote && attacker instanceof PlayerEntity && attacker.getRNG().nextFloat() < .15)
		{
			List<String> messages = ImmutableList.of("machinations", "stir", "suffering", "will", "done", "conspiracies");
			
			String key = messages.get(attacker.getRNG().nextInt(messages.size()));
			ITextComponent message = new TranslationTextComponent(stack.getTranslationKey() + ".message." + key);
			attacker.sendMessage(message.applyTextStyle(TextFormatting.DARK_PURPLE));
			attacker.addPotionEffect(new EffectInstance(Effects.WITHER, 100, 2));
		}
	};
	
	OnHitEffect SPAWN_BREADCRUMBS = (stack, target, attacker) -> {
		if(!target.world.isRemote)
		{
			ItemStack crumbs = new ItemStack(MSItems.BREADCRUMBS, 1);
			ItemEntity item = new ItemEntity(target.world, target.getPosX(), target.getPosY(), target.getPosZ(), crumbs);
			target.world.addEntity(item);
		}
	};
	
	OnHitEffect DROP_FOE_ITEM = (stack, target, attacker) -> {
		ItemStack heldByTarget = target.getHeldItemMainhand();
		if(!target.world.isRemote && !heldByTarget.isEmpty() && attacker.getRNG().nextFloat() < .05)
		{
			ItemEntity item = new ItemEntity(target.world, target.getPosX(), target.getPosY(), target.getPosZ(), heldByTarget.copy());
			item.getItem().setCount(1);
			item.setPickupDelay(40);
			target.world.addEntity(item);
			heldByTarget.shrink(1);
		}
	};
	
	static OnHitEffect setOnFire(int duration)
	{
		return (itemStack, target, attacker) -> target.setFire(duration);
	}
	
	static OnHitEffect playSound(Supplier<SoundEvent> sound)
	{
		return playSound(sound, 1, 1);
	}
	
	static OnHitEffect playSound(Supplier<SoundEvent> sound, float volume, float pitch)
	{
		return (itemStack, target, attacker) -> attacker.world.playSound(null, attacker.getPosX(), attacker.getPosY(), attacker.getPosZ(), sound.get(), attacker.getSoundCategory(), volume, pitch);
	}
	
	static OnHitEffect enemyKnockback(float knockback)
	{
		return (stack, target, attacker) -> {
			float randFloat = knockback + attacker.getRNG().nextFloat();
			
			if(!attacker.getEntityWorld().isRemote)
			{
				target.setMotion(target.getMotion().x * randFloat, target.getMotion().y, target.getMotion().z * randFloat);
			}
		};
	}
	
	static OnHitEffect aspectEffect(EnumAspect aspect, Supplier<EffectInstance> playerEffect, Supplier<EffectInstance> enemyEffect)
	{
		return (stack, target, attacker) -> {
			boolean critical = attacker.fallDistance > 0.0F && !attacker.onGround && !attacker.isOnLadder() && !attacker.isInWater() && !attacker.isPotionActive(Effects.BLINDNESS) && !attacker.isPassenger() && !attacker.isBeingRidden();
			float randFloat = attacker.getRNG().nextFloat();
			if(attacker instanceof ServerPlayerEntity)
			{
				Title title = PlayerSavedData.getData((ServerPlayerEntity) attacker).getTitle();
				
				if(critical)
					randFloat = randFloat - .1F;
				if(title != null && randFloat < .1)
				{
					if(title.getHeroAspect() == aspect)
					{
						attacker.addPotionEffect(playerEffect.get());
						if(enemyEffect != null)
							target.addPotionEffect(enemyEffect.get());
					}
				}
			}
		};
	}
	
	static OnHitEffect aspectAOE(EnumAspect aspect, Supplier<EffectInstance> effect, Supplier<SoundEvent> sound, float pitch)
	{
		return (stack, target, attacker) -> {
			boolean critical = attacker.fallDistance > 0.0F && !attacker.onGround && !attacker.isOnLadder() && !attacker.isInWater() && !attacker.isPotionActive(Effects.BLINDNESS) && !attacker.isPassenger() && !attacker.isBeingRidden();
			float randFloat = attacker.getRNG().nextFloat();
			if(attacker instanceof ServerPlayerEntity)
			{
				Title title = PlayerSavedData.getData((ServerPlayerEntity) attacker).getTitle();
				
				if(critical)
					randFloat = randFloat - .1F;
				if(title != null && randFloat < .1)
				{
					if(title.getHeroAspect() == aspect){
						AxisAlignedBB axisalignedbb = attacker.getBoundingBox().grow(4.0D, 2.0D, 4.0D);
						List<LivingEntity> list = attacker.world.getEntitiesWithinAABB(LivingEntity.class, axisalignedbb);
						list.remove(attacker);
						if (!list.isEmpty()) {
							attacker.world.playSound(null, attacker.getPosition(), sound.get(), SoundCategory.PLAYERS, 1.5F, pitch);
							for(LivingEntity livingentity : list) {
								livingentity.addPotionEffect(effect.get());
							}
						}
					}
				}
			}
		};
	}
}
package com.mraof.minestuck.item.weapon;

import com.google.common.collect.ImmutableList;
import com.mraof.minestuck.effects.MSEffects;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.event.ServerEventHandler;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import static com.mraof.minestuck.player.EnumAspect.*;

public interface OnHitEffect
{
	void onHit(ItemStack stack, LivingEntity target, LivingEntity attacker);
	
	OnHitEffect RAGE_STRENGTH = requireAspect(RAGE, chanceWithCritMod(
			userPotionEffect(() -> new EffectInstance(Effects.STRENGTH, 80, 1))));
	OnHitEffect HOPE_RESISTANCE = requireAspect(HOPE, chanceWithCritMod(
			userPotionEffect(() -> new EffectInstance(Effects.RESISTANCE, 120, 2))));
	OnHitEffect LIFE_SATURATION = requireAspect(LIFE, chanceWithCritMod(
			userPotionEffect(() -> new EffectInstance(Effects.SATURATION, 1, 1))
					.and(enemyPotionEffect(() -> new EffectInstance(Effects.HUNGER, 60, 100)))));
	
	OnHitEffect BREATH_LEVITATION_AOE = requireAspect(BREATH, chanceWithCritMod(
			potionAOE(() -> new EffectInstance(Effects.LEVITATION, 30, 2), () -> SoundEvents.ENTITY_ENDER_DRAGON_FLAP, 1.4F)));
	OnHitEffect TIME_SLOWNESS_AOE = requireAspect(TIME, chanceWithCritMod(
			potionAOE(() -> new EffectInstance(Effects.SLOWNESS, 100, 4), () -> SoundEvents.BLOCK_BELL_RESONATE, 2F)));
	
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
		
		target.addPotionEffect(new EffectInstance(Effects.WITHER, 100, 2));
		
		if(!attacker.world.isRemote && attacker instanceof PlayerEntity && attacker.getRNG().nextFloat() < .1)
		{
			List<String> messages = ImmutableList.of("machinations", "stir", "suffering", "will", "done", "conspiracies", "waiting", "strife", "search", "blessings", "seek", "shadow");
			
			String key = messages.get(attacker.getRNG().nextInt(messages.size()));
			ITextComponent message = new TranslationTextComponent("message.horrorterror." + key);
			attacker.sendMessage(message.applyTextStyle(TextFormatting.DARK_PURPLE));
			boolean potionBool = attacker.getRNG().nextBoolean();
			if(potionBool)
				attacker.addPotionEffect(new EffectInstance(Effects.WITHER, 100, 2));
			else
				attacker.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 100, 0));
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
	
	String SORD_DROP_MESSAGE = "drop_message";
	
	OnHitEffect SORD_DROP = (stack, target, attacker) -> {
		if(!attacker.getEntityWorld().isRemote && attacker.getRNG().nextFloat() < .25)
		{
			ItemEntity sord = new ItemEntity(attacker.world, attacker.getPosX(), attacker.getPosY(), attacker.getPosZ(), stack.copy());
			sord.getItem().setCount(1);
			sord.setPickupDelay(40);
			attacker.world.addEntity(sord);
			stack.shrink(1);
			attacker.sendMessage(new TranslationTextComponent(sord.getItem().getTranslationKey() + "." + SORD_DROP_MESSAGE));
		}
	};
	
	OnHitEffect RANDOM_DAMAGE = (stack, target, attacker) -> {
		DamageSource source;
		if(attacker instanceof PlayerEntity)
			source = DamageSource.causePlayerDamage((PlayerEntity) attacker);
		else source = DamageSource.causeMobDamage(attacker);
		
		float rng = (float) (attacker.getRNG().nextInt(7) + 1) * (attacker.getRNG().nextInt(7) + 1);
		target.attackEntityFrom(source, rng);
	};
	
	OnHitEffect SWEEP = (stack, target, attacker) -> {
		if(attacker instanceof PlayerEntity)
		{
			PlayerEntity playerAttacker = (PlayerEntity) attacker;
			boolean slowMoving = (double) (playerAttacker.distanceWalkedModified - playerAttacker.prevDistanceWalkedModified) < (double) playerAttacker.getAIMoveSpeed();
			boolean lastHitWasCrit = ServerEventHandler.wasLastHitCrit(playerAttacker);
			if(slowMoving && !lastHitWasCrit && playerAttacker.onGround)
			{
				float attackDamage = (float) playerAttacker.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue();
				float sweepEnchantMod = 1.0F + EnchantmentHelper.getSweepingDamageRatio(playerAttacker) * attackDamage;
				
				for(LivingEntity livingEntity : playerAttacker.world.getEntitiesWithinAABB(LivingEntity.class, target.getBoundingBox().grow(1.0D, 0.25D, 1.0D)))
				{
					if(livingEntity != playerAttacker && livingEntity != target && !playerAttacker.isOnSameTeam(livingEntity) && (!(livingEntity instanceof ArmorStandEntity) || !((ArmorStandEntity) livingEntity).hasMarker()) && playerAttacker.getDistanceSq(livingEntity) < 9.0D)
					{
						livingEntity.knockBack(playerAttacker, 0.4F, (double) MathHelper.sin(playerAttacker.rotationYaw * ((float) Math.PI / 180F)), (double) (-MathHelper.cos(playerAttacker.rotationYaw * ((float) Math.PI / 180F))));
						livingEntity.attackEntityFrom(DamageSource.causePlayerDamage(playerAttacker), sweepEnchantMod);
					}
				}
				
				playerAttacker.world.playSound(null, playerAttacker.getPosX(), playerAttacker.getPosY(), playerAttacker.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, playerAttacker.getSoundCategory(), 1.0F, 1.0F);
				playerAttacker.spawnSweepParticles();
			}
		}
	};
	
	OnHitEffect SPACE_TELEPORT = noCreativeShock(requireAspect(SPACE, onCrit((stack, target, attacker) -> {
		double oldPosX = attacker.getPosX();
		double oldPosY = attacker.getPosY();
		double oldPosZ = attacker.getPosZ();
		World worldIn = attacker.world;
		
		for(int i = 0; i < 16; ++i)
		{
			double newPosX = attacker.getPosX() + (attacker.getRNG().nextDouble() - 0.5D) * 16.0D;
			double newPosY = MathHelper.clamp(attacker.getPosY() + (double) (attacker.getRNG().nextInt(16) - 8), 0.0D, worldIn.getActualHeight() - 1);
			double newPosZ = attacker.getPosZ() + (attacker.getRNG().nextDouble() - 0.5D) * 16.0D;
			if(attacker.isPassenger())
				attacker.stopRiding();
			
			if(attacker.attemptTeleport(newPosX, newPosY, newPosZ, true))
			{
				worldIn.playSound(null, oldPosX, oldPosY, oldPosZ, SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
				attacker.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
				attacker.lookAt(attacker.getCommandSource().getEntityAnchorType(), target.getPositionVec());
				break;
			}
		}
	})));
	
	static OnHitEffect setOnFire(int duration)
	{
		return (itemStack, target, attacker) -> target.setFire(duration);
	}
	
	static OnHitEffect armorBypassingDamageMod(float additionalDamage, EnumAspect aspect)
	{
		return (stack, target, attacker) -> {
			DamageSource source;
			float damage = additionalDamage * 3.3F;
			
			if(attacker instanceof ServerPlayerEntity)
			{
				ServerPlayerEntity serverPlayer = (ServerPlayerEntity) attacker;
				source = DamageSource.causePlayerDamage(serverPlayer);
				Title title = PlayerSavedData.getData(serverPlayer).getTitle();
				
				if(target instanceof UnderlingEntity)
				{
					float modifier = (float) (PlayerSavedData.getData(serverPlayer).getEcheladder().getUnderlingDamageModifier());
					
					if(title == null || title.getHeroAspect() != aspect)
						modifier = modifier / 1.2F;
					
					damage = damage * modifier;
				} else
				{
					if(title == null || title.getHeroAspect() != aspect)
						damage = damage / 1.2F;
				}
			} else
			{
				source = DamageSource.causeMobDamage(attacker);
			}
			
			target.attackEntityFrom(source.setDamageBypassesArmor(), damage);
		};
	}
	
	static OnHitEffect targetSpecificAdditionalDamage(float additionalDamage, Supplier<EntityType<?>> targetEntity)
	{
		return (stack, target, attacker) -> {
			float damage = additionalDamage * 3.3F;
			
			if(attacker instanceof ServerPlayerEntity)
			{
				ServerPlayerEntity serverPlayer = (ServerPlayerEntity) attacker;
				
				if(target.getType() == targetEntity.get())
				{
					target.attackEntityFrom(DamageSource.causePlayerDamage(serverPlayer), damage);
				}
			}
		};
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
	
	static OnHitEffect userPotionEffect(Supplier<EffectInstance> effect)
	{
		return (stack, target, attacker) -> attacker.addPotionEffect(effect.get());
	}
	
	static OnHitEffect enemyPotionEffect(Supplier<EffectInstance> effect)
	{
		return (stack, target, attacker) -> target.addPotionEffect(effect.get());
	}
	
	static OnHitEffect requireAspect(EnumAspect aspect, OnHitEffect effect)
	{
		return (stack, target, attacker) -> {
			if(attacker instanceof ServerPlayerEntity)
			{
				Title title = PlayerSavedData.getData((ServerPlayerEntity) attacker).getTitle();
				
				if((title != null && title.getHeroAspect() == aspect) || ((ServerPlayerEntity) attacker).isCreative())
					effect.onHit(stack, target, attacker);
			}
		};
	}
	
	static OnHitEffect noCreativeShock(OnHitEffect effect)
	{
		return (stack, target, attacker) -> {
			if(!attacker.isPotionActive(MSEffects.CREATIVE_SHOCK.get()))
				effect.onHit(stack, target, attacker);
		};
	}
	
	static OnHitEffect onCrit(OnHitEffect effect)
	{
		return (stack, target, attacker) -> {
			if(ServerEventHandler.wasLastHitCrit(attacker))
				effect.onHit(stack, target, attacker);
		};
	}
	
	static OnHitEffect chanceWithCritMod(OnHitEffect effect)
	{
		return (stack, target, attacker) -> {
			if(!attacker.world.isRemote && attacker.getRNG().nextFloat() < (ServerEventHandler.wasLastHitCrit(attacker) ? 0.2 : 0.1))
				effect.onHit(stack, target, attacker);
		};
	}
	
	static OnHitEffect notAtPlayer(OnHitEffect effect)
	{
		return (stack, target, attacker) -> {
			if(!(target instanceof PlayerEntity))
				effect.onHit(stack, target, attacker);
		};
	}
	
	static OnHitEffect potionAOE(Supplier<EffectInstance> effect, Supplier<SoundEvent> sound, float pitch)
	{
		return (stack, target, attacker) -> {
			AxisAlignedBB axisalignedbb = attacker.getBoundingBox().grow(4.0D, 2.0D, 4.0D);
			List<LivingEntity> list = attacker.world.getEntitiesWithinAABB(LivingEntity.class, axisalignedbb);
			list.remove(attacker);
			if(!list.isEmpty())
			{
				attacker.world.playSound(null, attacker.getPosition(), sound.get(), SoundCategory.PLAYERS, 1.5F, pitch);
				for(LivingEntity livingentity : list)
					livingentity.addPotionEffect(effect.get());
			}
		};
	}
	
	default OnHitEffect and(OnHitEffect effect)
	{
		return (stack, target, attacker) -> {
			this.onHit(stack, target, attacker);
			effect.onHit(stack, target, attacker);
		};
	}
}
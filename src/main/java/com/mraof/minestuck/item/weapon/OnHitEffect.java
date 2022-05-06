package com.mraof.minestuck.item.weapon;

import com.google.common.collect.ImmutableList;
import com.mraof.minestuck.effects.CreativeShockEffect;
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
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import static com.mraof.minestuck.player.EnumAspect.*;

public interface OnHitEffect
{
	void onHit(ItemStack stack, LivingEntity target, LivingEntity attacker);
	
	OnHitEffect RAGE_STRENGTH = requireAspect(RAGE, chanceWithCritMod(
			userPotionEffect(() -> new EffectInstance(Effects.DAMAGE_BOOST, 80, 1))));
	OnHitEffect HOPE_RESISTANCE = requireAspect(HOPE, chanceWithCritMod(
			userPotionEffect(() -> new EffectInstance(Effects.DAMAGE_RESISTANCE, 120, 2))));
	OnHitEffect LIFE_SATURATION = requireAspect(LIFE, chanceWithCritMod(
			userPotionEffect(() -> new EffectInstance(Effects.SATURATION, 1, 1))
					.and(enemyPotionEffect(() -> new EffectInstance(Effects.HUNGER, 60, 100)))));
	
	OnHitEffect BREATH_LEVITATION_AOE = requireAspect(BREATH, chanceWithCritMod(
			potionAOE(() -> new EffectInstance(Effects.LEVITATION, 30, 2), () -> SoundEvents.ENDER_DRAGON_FLAP, 1.4F)));
	OnHitEffect TIME_SLOWNESS_AOE = requireAspect(TIME, chanceWithCritMod(
			potionAOE(() -> new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 100, 4), () -> SoundEvents.BELL_RESONATE, 2F)));
	
	OnHitEffect SET_CANDY_DROP_FLAG = (stack, target, attacker) -> {
		if(target instanceof UnderlingEntity)
			((UnderlingEntity) target).dropCandy = true;
	};
	OnHitEffect ICE_SHARD = (stack, target, attacker) -> {
		target.playSound(SoundEvents.GLASS_BREAK, 0.25F, 1.5F);
		if(!target.level.isClientSide && attacker instanceof PlayerEntity && attacker.getRandom().nextFloat() < .1)
		{
			target.playSound(SoundEvents.GLASS_BREAK, 1.5F, 1F);
			target.hurt(DamageSource.playerAttack((PlayerEntity) attacker), 2);
			stack.hurtAndBreak(2, attacker, entity -> entity.broadcastBreakEvent(Hand.MAIN_HAND));
			
			ItemEntity shardEntity = new ItemEntity(target.level, target.getX(), target.getY(), target.getZ(), new ItemStack(MSItems.ICE_SHARD, 1));
			target.level.addFreshEntity(shardEntity);
		}
	};
	OnHitEffect KUNDLER_SURPRISE = (stack, target, attacker) -> {
		if(!attacker.level.isClientSide && target.getHealth() <= 0 && attacker.getRandom().nextFloat() <= 0.20)
		{
			Random ran = new Random();
			//TODO Make this a loot table
			ItemStack[] items = new ItemStack[]{new ItemStack(Items.MELON_SLICE), new ItemStack(Items.STICK), new ItemStack(Items.EGG),
					new ItemStack(Blocks.DIRT), new ItemStack(Blocks.PUMPKIN), new ItemStack(Blocks.COBBLESTONE), new ItemStack(Items.REDSTONE),
					new ItemStack(MSItems.SURPRISE_EMBRYO), new ItemStack(MSItems.GAMEGRL_MAGAZINE), new ItemStack(MSItems.GAMEBRO_MAGAZINE),
					new ItemStack(Blocks.DEAD_HORN_CORAL)};
			int num = ran.nextInt(items.length);
			ItemEntity item = new ItemEntity(target.level, target.getX(), target.getY(), target.getZ(), items[num].copy());
			target.level.addFreshEntity(item);
			
			IFormattableTextComponent message = new TranslationTextComponent(stack.getDescriptionId() + ".message", items[num].getHoverName());
			attacker.sendMessage(message.withStyle(TextFormatting.GOLD), Util.NIL_UUID);
		}
	};
	
	OnHitEffect HORRORTERROR = (stack, target, attacker) -> {
		
		target.addEffect(new EffectInstance(Effects.WITHER, 100, 2));
		
		if(!attacker.level.isClientSide && attacker instanceof PlayerEntity && attacker.getRandom().nextFloat() < .1)
		{
			List<String> messages = ImmutableList.of("machinations", "stir", "suffering", "will", "done", "conspiracies", "waiting", "strife", "search", "blessings", "seek", "shadow");
			
			String key = messages.get(attacker.getRandom().nextInt(messages.size()));
			IFormattableTextComponent message = new TranslationTextComponent("message.horrorterror." + key);
			attacker.sendMessage(message.withStyle(TextFormatting.DARK_PURPLE), Util.NIL_UUID);
			boolean potionBool = attacker.getRandom().nextBoolean();
			if(potionBool)
				attacker.addEffect(new EffectInstance(Effects.WITHER, 100, 2));
			else
				attacker.addEffect(new EffectInstance(Effects.BLINDNESS, 100, 0));
		}
	};
	
	OnHitEffect SPAWN_BREADCRUMBS = (stack, target, attacker) -> {
		if(!target.level.isClientSide)
		{
			ItemStack crumbs = new ItemStack(MSItems.BREADCRUMBS, 1);
			ItemEntity item = new ItemEntity(target.level, target.getX(), target.getY(), target.getZ(), crumbs);
			target.level.addFreshEntity(item);
		}
	};
	
	OnHitEffect DROP_FOE_ITEM = (stack, target, attacker) -> {
		ItemStack heldByTarget = target.getMainHandItem();
		if(!target.level.isClientSide && !heldByTarget.isEmpty() && attacker.getRandom().nextFloat() < .05)
		{
			ItemEntity item = new ItemEntity(target.level, target.getX(), target.getY(), target.getZ(), heldByTarget.copy());
			item.getItem().setCount(1);
			item.setPickUpDelay(40);
			target.level.addFreshEntity(item);
			heldByTarget.shrink(1);
		}
	};
	
	String SORD_DROP_MESSAGE = "drop_message";
	
	OnHitEffect SORD_DROP = (stack, target, attacker) -> {
		if(!attacker.level.isClientSide && attacker.getRandom().nextFloat() < .25)
		{
			ItemEntity sord = new ItemEntity(attacker.level, attacker.getX(), attacker.getY(), attacker.getZ(), stack.copy());
			sord.getItem().setCount(1);
			sord.setPickUpDelay(40);
			attacker.level.addFreshEntity(sord);
			stack.shrink(1);
			attacker.sendMessage(new TranslationTextComponent(sord.getItem().getDescriptionId() + "." + SORD_DROP_MESSAGE), Util.NIL_UUID);
		}
	};
	
	OnHitEffect RANDOM_DAMAGE = (stack, target, attacker) -> {
		DamageSource source;
		if(attacker instanceof PlayerEntity)
			source = DamageSource.playerAttack((PlayerEntity) attacker);
		else source = DamageSource.mobAttack(attacker);
		
		float rng = (float) (attacker.getRandom().nextInt(7) + 1) * (attacker.getRandom().nextInt(7) + 1);
		target.hurt(source, rng);
	};
	
	OnHitEffect SWEEP = (stack, target, attacker) -> {
		if(attacker instanceof PlayerEntity)
		{
			PlayerEntity playerAttacker = (PlayerEntity) attacker;
			boolean slowMoving = (double) (playerAttacker.walkDist - playerAttacker.walkDistO) < (double) playerAttacker.getSpeed();
			boolean lastHitWasCrit = ServerEventHandler.wasLastHitCrit(playerAttacker);
			if(slowMoving && !lastHitWasCrit && playerAttacker.isOnGround())
			{
				float attackDamage = (float) playerAttacker.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
				float sweepEnchantMod = 1.0F + EnchantmentHelper.getSweepingDamageRatio(playerAttacker) * attackDamage;
				
				for(LivingEntity livingEntity : playerAttacker.level.getEntitiesOfClass(LivingEntity.class, target.getBoundingBox().inflate(1.0D, 0.25D, 1.0D)))
				{
					if(livingEntity != playerAttacker && livingEntity != target && !playerAttacker.isAlliedTo(livingEntity) && (!(livingEntity instanceof ArmorStandEntity) || !((ArmorStandEntity) livingEntity).isMarker()) && playerAttacker.distanceToSqr(livingEntity) < 9.0D)
					{
						livingEntity.knockback(0.4F, MathHelper.sin(playerAttacker.yRot * ((float) Math.PI / 180F)), -MathHelper.cos(playerAttacker.yRot * ((float) Math.PI / 180F)));
						livingEntity.hurt(DamageSource.playerAttack(playerAttacker), sweepEnchantMod);
					}
				}
				
				playerAttacker.level.playSound(null, playerAttacker.getX(), playerAttacker.getY(), playerAttacker.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, playerAttacker.getSoundSource(), 1.0F, 1.0F);
				playerAttacker.sweepAttack();
			}
		}
	};
	
	OnHitEffect SPACE_TELEPORT = withoutCreativeShock(requireAspect(SPACE, onCrit((stack, target, attacker) -> {
		double oldPosX = attacker.getX();
		double oldPosY = attacker.getY();
		double oldPosZ = attacker.getZ();
		World worldIn = attacker.level;
		
		for(int i = 0; i < 16; ++i)
		{
			double newPosX = attacker.getX() + (attacker.getRandom().nextDouble() - 0.5D) * 16.0D;
			double newPosY = MathHelper.clamp(attacker.getY() + (double) (attacker.getRandom().nextInt(16) - 8), 0.0D, worldIn.getHeight() - 1);//getAcutalHeight/getLogicalHeight
			double newPosZ = attacker.getZ() + (attacker.getRandom().nextDouble() - 0.5D) * 16.0D;
			if(attacker.isPassenger())
				attacker.stopRiding();
			
			if(attacker.randomTeleport(newPosX, newPosY, newPosZ, true))
			{
				worldIn.playSound(null, oldPosX, oldPosY, oldPosZ, SoundEvents.CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
				attacker.playSound(SoundEvents.CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
				attacker.lookAt(attacker.createCommandSourceStack().getAnchor(), target.position());
				break;
			}
		}
	})));
	
	static OnHitEffect setOnFire(int duration)
	{
		return (itemStack, target, attacker) -> target.setSecondsOnFire(duration);
	}
	
	static OnHitEffect armorBypassingDamageMod(float additionalDamage, EnumAspect aspect)
	{
		return (stack, target, attacker) -> {
			DamageSource source;
			float damage = additionalDamage * 3.3F;
			
			if(attacker instanceof ServerPlayerEntity)
			{
				ServerPlayerEntity serverPlayer = (ServerPlayerEntity) attacker;
				source = DamageSource.playerAttack(serverPlayer);
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
				source = DamageSource.mobAttack(attacker);
			}
			
			target.hurt(source.bypassArmor(), damage);
		};
	}
	
	/**
	 * Checks for attacks within three blocks of a point three blocks behind the targets back(covering the whole standard attack range of a player)
	 */
	static OnHitEffect backstab(float backstabDamage)
	{
		return (stack, target, attacker) -> {
			Direction direction = target.getDirection().getOpposite();
			Vector3i targetBackVec3i = target.blockPosition().relative(direction, 3); //three blocks behind the targets back
			if(targetBackVec3i.closerThan(attacker.blockPosition(), 3))
			{
				if (target.level instanceof ServerWorld)
				{
					((ServerWorld) target.level).sendParticles(ParticleTypes.DAMAGE_INDICATOR,
							target.blockPosition().relative(direction).getX(), target.getEyePosition(1F).y - 1, target.blockPosition().relative(direction).getZ(),
							4, 0, 0, 0, 0.1);
				}
				
				
				DamageSource source;
				if(attacker instanceof PlayerEntity)
					source = DamageSource.playerAttack((PlayerEntity) attacker);
				else source = DamageSource.mobAttack(attacker);
				
				target.hurt(source, backstabDamage);
			}
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
					target.hurt(DamageSource.playerAttack(serverPlayer), damage);
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
		return (itemStack, target, attacker) -> attacker.level.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), sound.get(), attacker.getSoundSource(), volume, pitch);
	}
	
	static OnHitEffect enemyKnockback(float knockback)
	{
		return (stack, target, attacker) -> {
			float randFloat = knockback + attacker.getRandom().nextFloat();
			
			if(!attacker.level.isClientSide)
			{
				target.setDeltaMovement(target.getDeltaMovement().x * randFloat, target.getDeltaMovement().y, target.getDeltaMovement().z * randFloat);
			}
		};
	}
	
	static OnHitEffect userPotionEffect(Supplier<EffectInstance> effect)
	{
		return (stack, target, attacker) -> attacker.addEffect(effect.get());
	}
	
	static OnHitEffect enemyPotionEffect(Supplier<EffectInstance> effect)
	{
		return (stack, target, attacker) -> target.addEffect(effect.get());
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
	
	/**
	 * Prevents effect from working if the entity is a player subject to the effects of creative shock
	 */
	static OnHitEffect withoutCreativeShock(OnHitEffect effect)
	{
		return (stack, target, attacker) -> {
			if(!(attacker instanceof PlayerEntity) || !CreativeShockEffect.doesCreativeShockLimit((PlayerEntity) attacker, CreativeShockEffect.LIMIT_MOBILITY_ITEMS))
			{
				effect.onHit(stack, target, attacker);
			}
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
			if(!attacker.level.isClientSide && attacker.getRandom().nextFloat() < (ServerEventHandler.wasLastHitCrit(attacker) ? 0.2 : 0.1))
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
			AxisAlignedBB axisalignedbb = attacker.getBoundingBox().inflate(4.0D, 2.0D, 4.0D);
			List<LivingEntity> list = attacker.level.getEntitiesOfClass(LivingEntity.class, axisalignedbb);
			list.remove(attacker);
			if(!list.isEmpty())
			{
				attacker.level.playSound(null, attacker.blockPosition(), sound.get(), SoundCategory.PLAYERS, 1.5F, pitch);
				for(LivingEntity livingentity : list)
					livingentity.addEffect(effect.get());
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
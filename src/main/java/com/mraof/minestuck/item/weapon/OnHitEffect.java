package com.mraof.minestuck.item.weapon;

import com.google.common.collect.ImmutableList;
import com.mraof.minestuck.effects.CreativeShockEffect;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.event.ServerEventHandler;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.loot.MSLootTables;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.function.Supplier;

import static com.mraof.minestuck.player.EnumAspect.*;

public interface OnHitEffect
{
	void onHit(ItemStack stack, LivingEntity target, LivingEntity attacker);
	
	OnHitEffect RAGE_STRENGTH = requireAspect(RAGE, chanceWithCritMod(
			userPotionEffect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 80, 1))));
	OnHitEffect HOPE_RESISTANCE = requireAspect(HOPE, chanceWithCritMod(
			userPotionEffect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 120, 2))));
	OnHitEffect LIFE_SATURATION = requireAspect(LIFE, chanceWithCritMod(
			userPotionEffect(() -> new MobEffectInstance(MobEffects.SATURATION, 1, 1))
					.and(enemyPotionEffect(() -> new MobEffectInstance(MobEffects.HUNGER, 60, 100)))));
	
	OnHitEffect BREATH_LEVITATION_AOE = requireAspect(BREATH, chanceWithCritMod(
			potionAOE(() -> new MobEffectInstance(MobEffects.LEVITATION, 30, 2), () -> SoundEvents.ENDER_DRAGON_FLAP, 1.4F)));
	OnHitEffect TIME_SLOWNESS_AOE = requireAspect(TIME, chanceWithCritMod(
			potionAOE(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 4), () -> SoundEvents.BELL_RESONATE, 2F)));
	
	OnHitEffect SET_CANDY_DROP_FLAG = (stack, target, attacker) -> {
		if(target instanceof UnderlingEntity)
			((UnderlingEntity) target).dropCandy = true;
	};
	OnHitEffect ICE_SHARD = (stack, target, attacker) -> {
		target.playSound(SoundEvents.GLASS_BREAK, 0.25F, 1.5F);
		if(!target.level.isClientSide && attacker instanceof Player player && attacker.getRandom().nextFloat() < .1)
		{
			target.playSound(SoundEvents.GLASS_BREAK, 1.5F, 1F);
			target.hurt(DamageSource.playerAttack(player), 2);
			stack.hurtAndBreak(2, attacker, entity -> entity.broadcastBreakEvent(InteractionHand.MAIN_HAND));
			
			ItemEntity shardEntity = new ItemEntity(target.level, target.getX(), target.getY(), target.getZ(), new ItemStack(MSItems.ICE_SHARD.get(), 1));
			target.level.addFreshEntity(shardEntity);
		}
	};
	OnHitEffect KUNDLER_SURPRISE = (stack, target, attacker) -> {
		if(!attacker.level.isClientSide && target.getHealth() <= 0 && attacker.getRandom().nextFloat() <= 0.20)
		{
			ServerLevel serverWorld = (ServerLevel) target.level;
			LootTable lootTable = serverWorld.getServer().getLootTables().get(MSLootTables.KUNDLER_SUPRISES);
			List<ItemStack> loot = lootTable.getRandomItems(new LootContext.Builder(serverWorld).create(LootContextParamSets.EMPTY));
			if(!loot.isEmpty())
			{
				ItemEntity item = new ItemEntity(target.level, target.getX(), target.getY(), target.getZ(), loot.get(0).copy());
				target.level.addFreshEntity(item);
				
				TranslatableComponent message = new TranslatableComponent(stack.getDescriptionId() + ".message", loot.get(0).getHoverName());
				attacker.sendMessage(message.withStyle(ChatFormatting.GOLD), Util.NIL_UUID);
			}
		}
	};
	
	OnHitEffect HORRORTERROR = (stack, target, attacker) -> {
		
		target.addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 2));
		
		if(!attacker.level.isClientSide && attacker instanceof Player && attacker.getRandom().nextFloat() < .1)
		{
			List<String> messages = ImmutableList.of("machinations", "stir", "suffering", "will", "done", "conspiracies", "waiting", "strife", "search", "blessings", "seek", "shadow");
			
			String key = messages.get(attacker.getRandom().nextInt(messages.size()));
			TranslatableComponent message = new TranslatableComponent("message.horrorterror." + key);
			attacker.sendMessage(message.withStyle(ChatFormatting.DARK_PURPLE), Util.NIL_UUID);
			boolean potionBool = attacker.getRandom().nextBoolean();
			if(potionBool)
				attacker.addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 2));
			else
				attacker.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0));
		}
	};
	
	OnHitEffect SPAWN_BREADCRUMBS = (stack, target, attacker) -> {
		if(!target.level.isClientSide)
		{
			ItemStack crumbs = new ItemStack(MSItems.BREADCRUMBS.get(), 1);
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
		if(!attacker.getCommandSenderWorld().isClientSide && attacker.getRandom().nextFloat() < .25)
		{
			ItemEntity sord = new ItemEntity(attacker.level, attacker.getX(), attacker.getY(), attacker.getZ(), stack.copy());
			sord.getItem().setCount(1);
			sord.setPickUpDelay(40);
			attacker.level.addFreshEntity(sord);
			stack.shrink(1);
			attacker.sendMessage(new TranslatableComponent(sord.getItem().getDescriptionId() + "." + SORD_DROP_MESSAGE), Util.NIL_UUID);
		}
	};
	
	OnHitEffect RANDOM_DAMAGE = (stack, target, attacker) -> {
		DamageSource source;
		if(attacker instanceof Player player)
			source = DamageSource.playerAttack(player);
		else source = DamageSource.mobAttack(attacker);
		
		float rng = (float) (attacker.getRandom().nextInt(7) + 1) * (attacker.getRandom().nextInt(7) + 1);
		target.hurt(source, rng);
	};
	
	OnHitEffect SWEEP = (stack, target, attacker) -> {
		if(attacker instanceof Player playerAttacker)
		{
			boolean slowMoving = (double) (playerAttacker.walkDist - playerAttacker.walkDistO) < (double) playerAttacker.getSpeed();
			boolean lastHitWasCrit = ServerEventHandler.wasLastHitCrit(playerAttacker);
			if(slowMoving && !lastHitWasCrit && playerAttacker.isOnGround())
			{
				float attackDamage = (float) playerAttacker.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
				float sweepEnchantMod = 1.0F + EnchantmentHelper.getSweepingDamageRatio(playerAttacker) * attackDamage;
				
				for(LivingEntity livingEntity : playerAttacker.level.getEntitiesOfClass(LivingEntity.class, target.getBoundingBox().inflate(1.0D, 0.25D, 1.0D)))
				{
					if(livingEntity != playerAttacker && livingEntity != target && !playerAttacker.isAlliedTo(livingEntity) && (!(livingEntity instanceof ArmorStand) || !((ArmorStand) livingEntity).isMarker()) && playerAttacker.distanceToSqr(livingEntity) < 9.0D)
					{
						livingEntity.knockback(0.4F, Mth.sin(playerAttacker.getYRot() * ((float) Math.PI / 180F)), -Mth.cos(playerAttacker.getYRot() * ((float) Math.PI / 180F)));
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
		Level level = attacker.level;
		
		for(int i = 0; i < 16; ++i)
		{
			double newPosX = attacker.getX() + (attacker.getRandom().nextDouble() - 0.5D) * 16.0D;
			double newPosY = Mth.clamp(attacker.getY() + (double) (attacker.getRandom().nextInt(16) - 8), 0.0D, level.getHeight() - 1);//getAcutalHeight/getLogicalHeight
			double newPosZ = attacker.getZ() + (attacker.getRandom().nextDouble() - 0.5D) * 16.0D;
			if(attacker.isPassenger())
				attacker.stopRiding();
			
			if(attacker.randomTeleport(newPosX, newPosY, newPosZ, true))
			{
				level.playSound(null, oldPosX, oldPosY, oldPosZ, SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
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
			
			if(attacker instanceof ServerPlayer serverPlayer)
			{
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
			BlockPos targetBack = target.blockPosition().relative(direction, 3); //three blocks behind the targets back
			if(targetBack.closerThan(attacker.blockPosition(), 3))
			{
				for(int i = 0; i < 4; i++)
				{
					target.level.addParticle(ParticleTypes.DAMAGE_INDICATOR, true, target.blockPosition().relative(direction).getX(), target.getEyePosition(1F).y - 1, target.blockPosition().relative(direction).getZ(), 0.1, 0.1, 0.1);
				}
				
				DamageSource source;
				if(attacker instanceof Player player)
					source = DamageSource.playerAttack(player);
				else source = DamageSource.mobAttack(attacker);
				
				target.hurt(source, backstabDamage);
			}
		};
	}
	
	static OnHitEffect targetSpecificAdditionalDamage(float additionalDamage, Supplier<EntityType<?>> targetEntity)
	{
		return (stack, target, attacker) -> {
			float damage = additionalDamage * 3.3F;
			
			if(attacker instanceof ServerPlayer serverPlayer)
			{
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
			
			if(!attacker.getCommandSenderWorld().isClientSide)
			{
				target.setDeltaMovement(target.getDeltaMovement().x * randFloat, target.getDeltaMovement().y, target.getDeltaMovement().z * randFloat);
			}
		};
	}
	
	static OnHitEffect userPotionEffect(Supplier<MobEffectInstance> effect)
	{
		return (stack, target, attacker) -> attacker.addEffect(effect.get());
	}
	
	static OnHitEffect enemyPotionEffect(Supplier<MobEffectInstance> effect)
	{
		return (stack, target, attacker) -> target.addEffect(effect.get());
	}
	
	static OnHitEffect requireAspect(EnumAspect aspect, OnHitEffect effect)
	{
		return (stack, target, attacker) -> {
			if(attacker instanceof ServerPlayer player)
			{
				Title title = PlayerSavedData.getData(player).getTitle();
				
				if((title != null && title.getHeroAspect() == aspect) || player.isCreative())
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
			if(!(attacker instanceof Player player) || !CreativeShockEffect.doesCreativeShockLimit(player, CreativeShockEffect.LIMIT_MOBILITY_ITEMS))
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
			if(!(target instanceof Player))
				effect.onHit(stack, target, attacker);
		};
	}
	
	static OnHitEffect potionAOE(Supplier<MobEffectInstance> effect, Supplier<SoundEvent> sound, float pitch)
	{
		return (stack, target, attacker) -> {
			AABB axisalignedbb = attacker.getBoundingBox().inflate(4.0D, 2.0D, 4.0D);
			List<LivingEntity> list = attacker.level.getEntitiesOfClass(LivingEntity.class, axisalignedbb);
			list.remove(attacker);
			if(!list.isEmpty())
			{
				attacker.level.playSound(null, attacker.blockPosition(), sound.get(), SoundSource.PLAYERS, 1.5F, pitch);
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
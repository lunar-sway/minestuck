package com.mraof.minestuck.item.weapon;

import com.google.common.collect.ImmutableList;
import com.mraof.minestuck.effects.CreativeShockEffect;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.event.ServerEventHandler;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.loot.MSLootTables;
import com.mraof.minestuck.network.PushPlayerPacket;
import com.mraof.minestuck.player.Echeladder;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.util.MSDamageSources;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;
import java.util.function.Supplier;

import static com.mraof.minestuck.player.EnumAspect.*;

@SuppressWarnings("resource")
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
		if(!target.level().isClientSide && attacker instanceof Player player && attacker.getRandom().nextFloat() < .1)
		{
			target.playSound(SoundEvents.GLASS_BREAK, 1.5F, 1F);
			target.hurt(player.damageSources().playerAttack(player), 2);
			stack.hurtAndBreak(2, attacker, entity -> entity.broadcastBreakEvent(InteractionHand.MAIN_HAND));
			
			ItemEntity shardEntity = new ItemEntity(target.level(), target.getX(), target.getY(), target.getZ(), new ItemStack(MSItems.ICE_SHARD.get(), 1));
			target.level().addFreshEntity(shardEntity);
		}
	};
	OnHitEffect KUNDLER_SURPRISE = (stack, target, attacker) -> {
		if(!attacker.level().isClientSide && target.getHealth() <= 0 && attacker.getRandom().nextFloat() <= 0.20)
		{
			ServerLevel serverWorld = (ServerLevel) target.level();
			LootTable lootTable = serverWorld.getServer().getLootData().getLootTable(MSLootTables.KUNDLER_SUPRISES);
			List<ItemStack> loot = lootTable.getRandomItems(new LootParams.Builder(serverWorld).create(LootContextParamSets.EMPTY));
			if(!loot.isEmpty())
			{
				ItemEntity item = new ItemEntity(target.level(), target.getX(), target.getY(), target.getZ(), loot.get(0).copy());
				target.level().addFreshEntity(item);
				
				attacker.sendSystemMessage(Component.translatable(stack.getDescriptionId() + ".message", loot.get(0).getHoverName()).withStyle(ChatFormatting.GOLD));
			}
		}
	};
	
	OnHitEffect HORRORTERROR = (stack, target, attacker) -> {
		
		target.addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 2));
		
		if(!attacker.level().isClientSide && attacker instanceof Player && attacker.getRandom().nextFloat() < .1)
		{
			List<String> messages = ImmutableList.of("machinations", "stir", "suffering", "will", "done", "conspiracies", "waiting", "strife", "search", "blessings", "seek", "shadow");
			
			String key = messages.get(attacker.getRandom().nextInt(messages.size()));
			attacker.sendSystemMessage(Component.translatable("message.horrorterror." + key).withStyle(ChatFormatting.DARK_PURPLE));
			boolean potionBool = attacker.getRandom().nextBoolean();
			if(potionBool)
				attacker.addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 2));
			else
				attacker.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0));
		}
	};
	
	OnHitEffect SPAWN_BREADCRUMBS = (stack, target, attacker) -> {
		if(!target.level().isClientSide)
		{
			ItemStack crumbs = new ItemStack(MSItems.BREADCRUMBS.get(), 1);
			ItemEntity item = new ItemEntity(target.level(), target.getX(), target.getY(), target.getZ(), crumbs);
			target.level().addFreshEntity(item);
		}
	};
	
	OnHitEffect DROP_FOE_ITEM = (stack, target, attacker) -> {
		ItemStack heldByTarget = target.getMainHandItem();
		if(!target.level().isClientSide && !heldByTarget.isEmpty() && attacker.getRandom().nextFloat() < .05)
		{
			ItemEntity item = new ItemEntity(target.level(), target.getX(), target.getY(), target.getZ(), heldByTarget.copy());
			item.getItem().setCount(1);
			item.setPickUpDelay(40);
			target.level().addFreshEntity(item);
			heldByTarget.shrink(1);
		}
	};
	
	String SORD_DROP_MESSAGE = "drop_message";
	
	OnHitEffect SORD_DROP = (stack, target, attacker) -> {
		if(!attacker.getCommandSenderWorld().isClientSide && attacker.getRandom().nextFloat() < .25)
		{
			ItemEntity sord = new ItemEntity(attacker.level(), attacker.getX(), attacker.getY(), attacker.getZ(), stack.copy());
			sord.getItem().setCount(1);
			sord.setPickUpDelay(40);
			attacker.level().addFreshEntity(sord);
			stack.shrink(1);
			attacker.sendSystemMessage(Component.translatable(sord.getItem().getDescriptionId() + "." + SORD_DROP_MESSAGE));
		}
	};
	
	OnHitEffect RANDOM_DAMAGE = (stack, target, attacker) -> {
		DamageSource source;
		if(attacker instanceof Player player)
			source = attacker.damageSources().playerAttack(player);
		else source = attacker.damageSources().mobAttack(attacker);
		
		float rng = (float) (attacker.getRandom().nextInt(7) + 1) * (attacker.getRandom().nextInt(7) + 1);
		target.hurt(source, rng);
	};
	
	OnHitEffect SWEEP = (stack, target, attacker) -> {
		if(attacker instanceof Player playerAttacker)
		{
			boolean slowMoving = (double) (playerAttacker.walkDist - playerAttacker.walkDistO) < (double) playerAttacker.getSpeed();
			boolean lastHitWasCrit = ServerEventHandler.wasLastHitCrit(playerAttacker);
			if(slowMoving && !lastHitWasCrit && playerAttacker.onGround())
			{
				float attackDamage = (float) playerAttacker.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
				float sweepEnchantMod = 1.0F + EnchantmentHelper.getSweepingDamageRatio(playerAttacker) * attackDamage;
				
				for(LivingEntity livingEntity : playerAttacker.level().getEntitiesOfClass(LivingEntity.class, target.getBoundingBox().inflate(1.0D, 0.25D, 1.0D)))
				{
					if(livingEntity != playerAttacker && livingEntity != target && !playerAttacker.isAlliedTo(livingEntity) && (!(livingEntity instanceof ArmorStand) || !((ArmorStand) livingEntity).isMarker()) && playerAttacker.distanceToSqr(livingEntity) < 9.0D)
					{
						livingEntity.knockback(0.4F, Mth.sin(playerAttacker.getYRot() * ((float) Math.PI / 180F)), -Mth.cos(playerAttacker.getYRot() * ((float) Math.PI / 180F)));
						livingEntity.hurt(attacker.damageSources().playerAttack(playerAttacker), sweepEnchantMod);
					}
				}
				
				playerAttacker.level().playSound(null, playerAttacker.getX(), playerAttacker.getY(), playerAttacker.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, playerAttacker.getSoundSource(), 1.0F, 1.0F);
				playerAttacker.sweepAttack();
			}
		}
	};
	
	/**
	 * A knockback effect that does a right-vector (relative to the attacker's orientation)
	 * and direction to target dot calculation to spread targets further horizontally.
	 */
	OnHitEffect SPREADING_KNOCKBACK = (stack, target, attacker) -> {
		
		//Attacker's right vector (forward value rotated 90°)
		double attackerRadians = Math.toRadians(attacker.getYRot()) - (Math.PI / 2.0);
		Vec3 attackerRight = new Vec3(Math.cos(attackerRadians), 0, Math.sin(attackerRadians));
		
		//Direction vector (attacker -> target)
		Vec3 dirToTarget = attacker.position().vectorTo(target.position()).multiply(1, 0, 1).normalize();
		
		//Dot product of direction and attacker's right
		double dot = attackerRight.dot(dirToTarget);
		double dotFactor = Mth.sign(dot) * 1.5 * Math.pow(1 - (Math.abs(dot)), 0.3);
		
		//Knockback direction vector and application
		Vec3 knockback = dirToTarget.scale(-1).add(attackerRight.scale(dotFactor)).normalize();
		target.knockback(0.7f, knockback.x, knockback.z);
	};
	
	OnHitEffect SPACE_TELEPORT = withoutCreativeShock(requireAspect(SPACE, onCrit((stack, target, attacker) -> {
		double oldPosX = attacker.getX();
		double oldPosY = attacker.getY();
		double oldPosZ = attacker.getZ();
		Level level = attacker.level();
		
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
	
	OnHitEffect BOSS_BUSTER = (stack, target, attacker) -> {
		if(attacker instanceof ServerPlayer player && target.getType().is(MSTags.EntityTypes.BOSS_MOB))
		{
			target.hurt(target.damageSources().playerAttack(player), target.getMaxHealth() / 20);
		}
	};
	
	static OnHitEffect setOnFire(int duration)
	{
		return (itemStack, target, attacker) -> target.setSecondsOnFire(duration);
	}
	
	static OnHitEffect armorBypassingDamageMod(float additionalDamage, EnumAspect aspect)
	{
		return (stack, target, attacker) -> {
			float damage = additionalDamage * 3.3F;
			
			if(attacker instanceof ServerPlayer serverPlayer && !(attacker instanceof FakePlayer))
			{
				boolean isMissingAspectBonus = !Title.isPlayerOfAspect(serverPlayer, aspect);
				
				if(target instanceof UnderlingEntity)
				{
					float modifier = (float) (Echeladder.get(serverPlayer).getUnderlingDamageModifier());
					
					if(isMissingAspectBonus)
						modifier /= 1.2F;
					
					damage *= modifier;
				} else
				{
					if(isMissingAspectBonus)
						damage /= 1.2F;
				}
			}
			
			target.hurt(MSDamageSources.armorPierce(attacker.level().registryAccess(), attacker), damage);
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
					target.level().addParticle(ParticleTypes.DAMAGE_INDICATOR, true, target.blockPosition().relative(direction).getX(), target.getEyePosition(1F).y - 1, target.blockPosition().relative(direction).getZ(), 0.1, 0.1, 0.1);
				}
				
				DamageSource source;
				if(attacker instanceof Player player)
					source = target.damageSources().playerAttack(player);
				else source = target.damageSources().mobAttack(attacker);
				
				target.hurt(source, backstabDamage);
			}
		};
	}
	
	static OnHitEffect targetSpecificAdditionalDamage(float additionalDamage, Supplier<? extends EntityType<?>> targetEntity)
	{
		return (stack, target, attacker) -> {
			float damage = additionalDamage * 3.3F;
			
			if(attacker instanceof ServerPlayer serverPlayer)
			{
				if(target.getType() == targetEntity.get())
				{
					target.hurt(target.damageSources().playerAttack(serverPlayer), damage);
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
		return (itemStack, target, attacker) -> attacker.level().playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), sound.get(), attacker.getSoundSource(), volume, pitch);
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
	
	/**
	 * Flings the target in the direction the attacker was facing, and flings the attacker in the opposite direction of their facing
	 */
	static OnHitEffect mutualKnockback(float knockback)
	{
		return (itemStack, target, attacker) ->
		{
			Vec3 targetVec = attacker.getLookAngle().scale(1F + knockback);
			Vec3 attackerVec = targetVec.reverse();
			
			target.push(targetVec.x, targetVec.y, targetVec.z);
			
			//dismount the attacker
			if(attacker.getVehicle() != null)
				attacker.dismountTo(attacker.getX(), attacker.getY(), attacker.getZ());
			
			attacker.push(attackerVec.x, attackerVec.y, attackerVec.z);
			
			if(attacker instanceof ServerPlayer player)
			{
				PushPlayerPacket packet = PushPlayerPacket.createPacket(attackerVec);
				PacketDistributor.PLAYER.with(player).send(packet);
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
	
	/**
	 * A function that causes a sweep effect (code based off of {@link #SWEEP}) and applies an array of effects on the target.
	 *
	 * @param effects A varargs value, essentially an optional array of hit effects to be applied.
	 */
	static OnHitEffect sweepMultiEffect(OnHitEffect... effects)
	{
		return (stack, target, attacker) -> {
			if(!(attacker instanceof Player playerAttacker))
				return;
			
			boolean slowMoving = (double) (playerAttacker.walkDist - playerAttacker.walkDistO) < (double) playerAttacker.getSpeed();
			boolean lastHitWasCrit = ServerEventHandler.wasLastHitCrit(playerAttacker);
			
			if(!slowMoving
					|| lastHitWasCrit
					|| !playerAttacker.onGround()
					|| playerAttacker.getAttackStrengthScale(0) < 1)
				return;
			
			float attackDamage = (float) playerAttacker.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
			float sweepEnchantMod = 1.0F + EnchantmentHelper.getSweepingDamageRatio(playerAttacker) * attackDamage;
			
			for(LivingEntity livingEntity : playerAttacker.level().getEntitiesOfClass(
					LivingEntity.class, target.getBoundingBox().inflate(2.0D, 0.25D, 2.0D)))
			{
				if(livingEntity == playerAttacker
						|| playerAttacker.isAlliedTo(livingEntity)
						|| playerAttacker.distanceToSqr(livingEntity) >= 9.0D
						|| (livingEntity instanceof ArmorStand armorStand && armorStand.isMarker()))
					continue;
				
				if(livingEntity != target)
					livingEntity.hurt(target.damageSources().playerAttack(playerAttacker), sweepEnchantMod);
				for(OnHitEffect effect : effects)
					effect.onHit(stack, livingEntity, attacker);
			}
			playerAttacker.level().playSound(null, playerAttacker.getX(), playerAttacker.getY(), playerAttacker.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, playerAttacker.getSoundSource(), 1.0F, 1.0F);
			playerAttacker.sweepAttack();
		};
	}
	
	static OnHitEffect spawnParticles(SimpleParticleType particle, int amount, double xMovement, double yMovement, double zMovement, double speed)
	{
		return (stack, target, attacker) -> {
			Level level = target.level();
			if(!level.isClientSide)
			{
				((ServerLevel) level).sendParticles(particle, target.getX(), target.getY(), target.getZ(), amount, xMovement, yMovement, zMovement, speed);
			}
		};
	}
	
	static OnHitEffect requireAspect(EnumAspect aspect, OnHitEffect effect)
	{
		return (stack, target, attacker) -> {
			if(attacker instanceof ServerPlayer player
					&& (player.isCreative() || Title.isPlayerOfAspect(player, aspect)))
			{
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
			if(!attacker.level().isClientSide && attacker.getRandom().nextFloat() < (ServerEventHandler.wasLastHitCrit(attacker) ? 0.2 : 0.1))
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
			List<LivingEntity> list = attacker.level().getEntitiesOfClass(LivingEntity.class, axisalignedbb);
			list.remove(attacker);
			if(!list.isEmpty())
			{
				attacker.level().playSound(null, attacker.blockPosition(), sound.get(), SoundSource.PLAYERS, 1.5F, pitch);
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

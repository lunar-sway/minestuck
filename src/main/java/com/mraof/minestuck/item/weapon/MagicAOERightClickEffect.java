package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.client.util.MagicEffect;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.MagicEffectPacket;
import com.mraof.minestuck.player.PlayerSavedData;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class MagicAOERightClickEffect implements ItemRightClickEffect
{
	private final float radius;
	private final int damage;
	private final Supplier<MobEffectInstance> mobEffect;
	private final Supplier<SoundEvent> sound;
	private final float pitch;
	@Nullable
	private final MagicEffect.RangedType type;
	
	private static final TargetingConditions visiblePredicate = TargetingConditions.forCombat();
	
	public static final MagicAOERightClickEffect STANDARD_MAGIC = new MagicAOERightClickEffect(4, 3, MagicEffect.RangedType.ENCHANT);
	public static final MagicAOERightClickEffect WATER_MAGIC = new WaterMagicEffect(6, 3, MagicEffect.RangedType.WATER);
	public static final MagicAOERightClickEffect FIRE_MAGIC = new FireMagicEffect(6, 3, MagicEffect.RangedType.FIRE);
	
	protected MagicAOERightClickEffect(float radius, int damage, @Nullable MagicEffect.RangedType type)
	{
		this(radius, damage, null, null, 1.0F, type);
	}
	
	protected MagicAOERightClickEffect(float radius, int damage, Supplier<MobEffectInstance> mobEffect, Supplier<SoundEvent> sound, float pitch, @Nullable MagicEffect.RangedType type)
	{
		this.radius = radius;
		this.damage = damage;
		this.mobEffect = mobEffect;
		this.sound = sound;
		this.pitch = pitch;
		this.type = type;
	}
	
	@Override
	public InteractionResultHolder<ItemStack> onRightClick(Level level, Player player, InteractionHand hand)
	{
		ItemStack itemStackIn = player.getItemInHand(hand);
		
		if(player instanceof ServerPlayer serverPlayer)
			magicAttack(serverPlayer);
		
		if(player.isCreative())
			player.getCooldowns().addCooldown(itemStackIn.getItem(), 10);
		else
			player.getCooldowns().addCooldown(itemStackIn.getItem(), 100);
		
		player.swing(hand, true);
		itemStackIn.hurtAndBreak(6, player, playerEntity -> playerEntity.broadcastBreakEvent(InteractionHand.MAIN_HAND));
		player.awardStat(Stats.ITEM_USED.get(itemStackIn.getItem()));
		
		return InteractionResultHolder.success(itemStackIn);
	}
	
	private void magicAttack(ServerPlayer player)
	{
		BlockPos playerPos = player.blockPosition();
		Level level = player.level;
		
		if(sound != null && player.getRandom().nextFloat() < .1F) //optional sound effect adding
			level.playSound(null, player.getX(), player.getY(), player.getZ(), sound.get(), SoundSource.PLAYERS, 0.7F, pitch);
		level.playSound(null, player.getX(), player.getY(), player.getZ(), MSSoundEvents.ITEM_MAGIC_CAST.get(), SoundSource.PLAYERS, 1.0F, 1.2F);
		
		attackerEffect(player);
		
		if(player.isInWall())
			return; //TODO test
		
		List<LivingEntity> targets = level.getNearbyEntities(LivingEntity.class, visiblePredicate, player, new AABB(playerPos).inflate(radius));
		targets.remove(player);
		
		for(LivingEntity target : targets)
		{
			if(target.isAlliedTo(player))
				continue;
			
			float talliedDamage = calculateDamage(player, target, damage);
			target.hurt(DamageSource.playerAttack(player).setMagic(), talliedDamage);
			
			if(mobEffect != null && player.getRandom().nextFloat() < .1F)
				target.addEffect(mobEffect.get());
			
			extraTargetEffect(target);
		}
	}
	
	// If you're an addon that want to use this class with your own effect, override this to use your own network packet
	protected void sendEffectPacket(Level level, Vec3 pos, Vec3 lookVec, int length, boolean collides)
	{
		if(type != null)
			MSPacketHandler.sendToNear(new MagicEffectPacket(type, pos, lookVec, length, collides),
					new PacketDistributor.TargetPoint(pos.x, pos.y, pos.z, 64, level.dimension()));
	}
	
	protected void attackerEffect(ServerPlayer player)
	{
	}
	
	protected void extraTargetEffect(LivingEntity target)
	{
	}
	
	protected float calculateDamage(ServerPlayer player, LivingEntity closestTarget, int damage)
	{
		int playerRung = PlayerSavedData.getData(player).getEcheladder().getRung();
		
		if(closestTarget instanceof UnderlingEntity)
			return damage + playerRung / 5F; //damage increase from rung is higher against underlings
		else
			return damage + playerRung / 10F;
	}
	
	private static class WaterMagicEffect extends MagicAOERightClickEffect
	{
		WaterMagicEffect(int distance, int damage, @Nullable MagicEffect.RangedType type)
		{
			super(distance, damage, type);
		}
		
		@Override
		protected float calculateDamage(ServerPlayer player, LivingEntity closestTarget, int damage)
		{
			if(closestTarget.isSensitiveToWater())
			{
				return super.calculateDamage(player, closestTarget, damage) + 5.0F;
			} else
				return super.calculateDamage(player, closestTarget, damage);
		}
	}
	
	private static class FireMagicEffect extends MagicAOERightClickEffect
	{
		FireMagicEffect(int distance, int damage, @Nullable MagicEffect.RangedType type)
		{
			super(distance, damage, type);
		}
		
		@Override
		protected void extraTargetEffect(LivingEntity target)
		{
			super.extraTargetEffect(target);
			target.setSecondsOnFire(10);
		}
	}
}
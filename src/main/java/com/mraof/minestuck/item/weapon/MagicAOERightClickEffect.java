package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.client.util.MagicEffect;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.network.MagicAOEEffectPacket;
import com.mraof.minestuck.player.Echeladder;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class MagicAOERightClickEffect implements ItemRightClickEffect
{
	private final float radius;
	private final int damage;
	@Nullable
	private final MagicEffect.AOEType type;
	private final Consumer<LivingEntity> targetEffect;
	
	private static final TargetingConditions visiblePredicate = TargetingConditions.forCombat();
	
	public static final MagicAOERightClickEffect STANDARD_MAGIC = new MagicAOERightClickEffect(3, 2, MagicEffect.AOEType.ENCHANT);
	public static final MagicAOERightClickEffect WATER_MAGIC = new MagicAOERightClickEffect(3.25F, 3, MagicEffect.AOEType.WATER)
	{
		@Override
		protected float calculateDamage(ServerPlayer player, LivingEntity target, int damage)
		{
			return super.calculateDamage(player, target, damage)
					+ (target.isSensitiveToWater() ? 5 : 0);
		}
	};
	public static final MagicAOERightClickEffect FIRE_MAGIC = new MagicAOERightClickEffect(3.25F, 3, MagicEffect.AOEType.FIRE, target -> target.setSecondsOnFire(5));
	
	protected MagicAOERightClickEffect(float radius, int damage, @Nullable MagicEffect.AOEType type)
	{
		this(radius, damage, type, ignored -> {});
	}
	
	protected MagicAOERightClickEffect(float radius, int damage, @Nullable MagicEffect.AOEType type,
									   Consumer<LivingEntity> targetEffect)
	{
		this.radius = radius;
		this.damage = damage;
		this.type = type;
		this.targetEffect = targetEffect;
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
		Level level = player.level();
		
		level.playSound(null, player.getX(), player.getY(), player.getZ(), MSSoundEvents.ITEM_MAGIC_CAST.get(), SoundSource.PLAYERS, 1.0F, 1.2F);
		
		if(player.isInWall())
			return;
		
		AABB aabb = new AABB(playerPos).inflate(radius);
		List<LivingEntity> targets = level.getNearbyEntities(LivingEntity.class, visiblePredicate, player, aabb);
		targets.remove(player);
		
		for(LivingEntity target : targets)
		{
			if(target.isAlliedTo(player))
				continue;
			
			float talliedDamage = calculateDamage(player, target, damage);
			target.hurt(level.damageSources().indirectMagic(player, player), talliedDamage);
			
			this.targetEffect.accept(target);
		}
		
		sendEffectPacket(level, aabb);
	}
	
	// If you're an addon that want to use this class with your own effect, override this to use your own network packet
	protected void sendEffectPacket(Level level, AABB aabb)
	{
		if(type != null)
		{
			Vec3 centerPos = aabb.getCenter();
			Vec3 minAOEBound = new Vec3(aabb.minX, aabb.minY, aabb.minZ);
			Vec3 maxAOEBound = new Vec3(aabb.maxX, aabb.maxY, aabb.maxZ);
			
			PacketDistributor.NEAR.with(new PacketDistributor.TargetPoint(centerPos.x, centerPos.y, centerPos.z, 64, level.dimension()))
					.send(new MagicAOEEffectPacket(type, minAOEBound, maxAOEBound));
		}
	}
	
	protected float calculateDamage(ServerPlayer player, LivingEntity target, int damage)
	{
		if(!(player instanceof FakePlayer))
		{
			int playerRung = Echeladder.get(player).getRung();
			
			//damage increase from rung is higher against underlings
			return damage + (target instanceof UnderlingEntity ? playerRung / 5F : playerRung / 10F);
		} else
			return damage;
	}
}
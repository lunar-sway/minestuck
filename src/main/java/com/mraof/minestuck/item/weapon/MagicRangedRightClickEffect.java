package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.client.util.MagicEffect;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.network.MagicRangedEffectPacket;
import com.mraof.minestuck.player.Echeladder;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class MagicRangedRightClickEffect implements ItemRightClickEffect
{
	private final int distance;
	private final int damage;
	private final Supplier<MobEffectInstance> effect;
	private final Supplier<SoundEvent> sound;
	private final float pitch;
	@Nullable
	private final MagicEffect.RangedType type;
	
	private static final TargetingConditions visiblePredicate = TargetingConditions.forCombat();//.setLineOfSiteRequired(); TODO should something else be done with the predicate?
	
	public static final MagicRangedRightClickEffect SBAHJ_AIMBOT_MAGIC = new SbahjMagicEffect(10, 1, null, null, 1.0F, MagicEffect.RangedType.GREEN);
	public static final MagicRangedRightClickEffect AIMBOT_MAGIC = new AimbotMagicEffect(14, 2, null, null, 1.0F, MagicEffect.RangedType.CRIT);
	public static final MagicRangedRightClickEffect STANDARD_MAGIC = new MagicRangedRightClickEffect(15, 3, null, null, 1.0F, MagicEffect.RangedType.ENCHANT);
	public static final MagicRangedRightClickEffect POOL_CUE_MAGIC = new MagicRangedRightClickEffect(18, 4, null, null, 1.0F, MagicEffect.RangedType.RED);
	public static final MagicRangedRightClickEffect HORRORTERROR_MAGIC = new MagicRangedRightClickEffect(20, 5, () -> new MobEffectInstance(MobEffects.WITHER, 100, 0), MSSoundEvents.ITEM_GRIMOIRE_USE, 1.2F, MagicEffect.RangedType.INK);
	public static final MagicRangedRightClickEffect ZILLY_MAGIC = new MagicRangedRightClickEffect(30, 8, null, null, 1.0F, MagicEffect.RangedType.ZILLY);
	public static final MagicRangedRightClickEffect ECHIDNA_MAGIC = new MagicRangedRightClickEffect(50, 9, null, null, 1.0F, MagicEffect.RangedType.ECHIDNA);
	
	protected MagicRangedRightClickEffect(int distance, int damage, Supplier<MobEffectInstance> effect, Supplier<SoundEvent> sound, float pitch, @Nullable MagicEffect.RangedType type)
	{
		this.distance = distance;
		this.damage = damage;
		this.effect = effect;
		this.sound = sound;
		this.pitch = pitch;
		this.type = type;
	}
	
	@Override
	public InteractionResultHolder<ItemStack> onRightClick(Level level, Player player, InteractionHand hand)
	{
		ItemStack itemStackIn = player.getItemInHand(hand);
		
		if(player instanceof ServerPlayer serverPlayer)
			magicAttack(level, serverPlayer);
		
		if(player.isCreative())
			player.getCooldowns().addCooldown(itemStackIn.getItem(), 10);
		else
			player.getCooldowns().addCooldown(itemStackIn.getItem(), 50);
		
		player.swing(hand, true);
		itemStackIn.hurtAndBreak(6, player, playerEntity -> playerEntity.broadcastBreakEvent(InteractionHand.MAIN_HAND));
		player.awardStat(Stats.ITEM_USED.get(itemStackIn.getItem()));
		
		return InteractionResultHolder.success(itemStackIn);
	}
	
	private void magicAttack(Level level, ServerPlayer player)
	{
		if(sound != null && player.getRandom().nextFloat() < .1F) //optional sound effect adding
			level.playSound(null, player.getX(), player.getY(), player.getZ(), sound.get(), SoundSource.PLAYERS, 0.7F, pitch);
		level.playSound(null, player.getX(), player.getY(), player.getZ(), MSSoundEvents.ITEM_MAGIC_CAST.get(), SoundSource.PLAYERS, 1.0F, 1.2F);
		
		targetEffect(player);
		
		Vec3 eyePos = player.getEyePosition(1.0F);
		Vec3 lookVec = player.getLookAngle();
		
		for(int step = 0; step < distance * 2; step++) //uses the float i value to increase the distance away from where the player is looking and creating a sort of raytrace
		{
			Vec3 vecPos = eyePos.add(lookVec.scale(step / 2D));
			
			boolean hitObstacle = checkCollisionInPath(level, player, vecPos);
			
			if(hitObstacle)
			{
				sendEffectPacket(level, eyePos, lookVec, step, true);
				return;
			}
		}
		sendEffectPacket(level, eyePos, lookVec, distance * 2, false);
	}
	
	// If you're an addon that want to use this class with your own effect, override this to use your own network packet
	protected void sendEffectPacket(Level level, Vec3 pos, Vec3 lookVec, int length, boolean collides)
	{
		if(type != null)
			PacketDistributor.NEAR.with(new PacketDistributor.TargetPoint(pos.x, pos.y, pos.z, 64, level.dimension()))
					.send(new MagicRangedEffectPacket(type, pos, lookVec, length, collides));
	}
	
	protected void targetEffect(ServerPlayer player)
	{
	}
	
	private boolean checkCollisionInPath(Level level, ServerPlayer player, Vec3 vecPos)
	{
		BlockPos blockPos = BlockPos.containing(vecPos);
		
		if(!level.getBlockState(blockPos).isPathfindable(level, blockPos, PathComputationType.LAND))
		{
			return true;
		}
		
		AABB axisAlignedBB = new AABB(blockPos);
		// gets entities in a bounding box around each vector position in the for loop
		LivingEntity closestTarget = player.level().getNearestEntity(LivingEntity.class, visiblePredicate, player, vecPos.x, vecPos.y, vecPos.z, axisAlignedBB);
		if(closestTarget != null)
		{
			float talliedDamage = calculateDamage(player, closestTarget, damage);
			closestTarget.hurt(level.damageSources().indirectMagic(player, player), talliedDamage);
			
			if(effect != null && player.getRandom().nextFloat() < .25F)
				closestTarget.addEffect(effect.get());
			
			return true;
		} else return false;
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
	
	private static class SbahjMagicEffect extends MagicRangedRightClickEffect
	{
		SbahjMagicEffect(int distance, int damage, Supplier<MobEffectInstance> effect, Supplier<SoundEvent> sound, float pitch, @Nullable MagicEffect.RangedType type)
		{
			super(distance, damage, effect, sound, pitch, type);
		}
		
		@Override
		protected void targetEffect(ServerPlayer player)
		{
			Vec3 randomFacingVecPos = new Vec3(player.getX() + player.getRandom().nextInt(10) - 5, player.getY() + player.getRandom().nextInt(10) - 5, player.getZ() + player.getRandom().nextInt(10) - 5);
			player.lookAt(player.createCommandSourceStack().getAnchor(), randomFacingVecPos);
		}
	}
	
	private static class AimbotMagicEffect extends MagicRangedRightClickEffect
	{
		AimbotMagicEffect(int distance, int damage, Supplier<MobEffectInstance> effect, Supplier<SoundEvent> sound, float pitch, @Nullable MagicEffect.RangedType type)
		{
			super(distance, damage, effect, sound, pitch, type);
		}
		
		@Override
		protected void targetEffect(ServerPlayer player)
		{
			BlockPos playerEyePos = BlockPos.containing(player.getX(), player.getEyeY(), player.getZ());
			LivingEntity closestVisibleTarget = player.level().getNearestEntity(LivingEntity.class, visiblePredicate, player, player.getX(), player.getEyeY(), player.getZ(), new AABB(playerEyePos).inflate(11));
			if(closestVisibleTarget != null)
				player.lookAt(EntityAnchorArgument.Anchor.EYES, closestVisibleTarget, EntityAnchorArgument.Anchor.EYES);
		}
	}
}


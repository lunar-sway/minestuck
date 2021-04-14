package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.client.util.MagicEffect;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.MagicEffectPacket;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class MagicAttackRightClickEffect implements ItemRightClickEffect
{
	private final int distance;
	private final int damage;
	private final Supplier<EffectInstance> effect;
	private final Supplier<SoundEvent> sound;
	private final float pitch;
	@Nullable
	private final MagicEffect.Type type;
	
	private static final EntityPredicate visiblePredicate = (new EntityPredicate());//.setLineOfSiteRequired(); TODO should something else be done with the predicate?
	
	public static final MagicAttackRightClickEffect SBAHJ_AIMBOT_MAGIC = new SbahjMagicEffect(10, 1, null, null, 1.0F, MagicEffect.Type.GREEN);
	public static final MagicAttackRightClickEffect AIMBOT_MAGIC = new AimbotMagicEffect(14, 2, null, null, 1.0F, MagicEffect.Type.CRIT);
	public static final MagicAttackRightClickEffect STANDARD_MAGIC = new MagicAttackRightClickEffect(15, 3, null, null, 1.0F, MagicEffect.Type.ENCHANT);
	public static final MagicAttackRightClickEffect POOL_CUE_MAGIC = new MagicAttackRightClickEffect(18, 4, null, null, 1.0F, MagicEffect.Type.RED);
	public static final MagicAttackRightClickEffect HORRORTERROR_MAGIC = new MagicAttackRightClickEffect(20, 5, () -> new EffectInstance(Effects.WITHER, 100, 0), () -> MSSoundEvents.ITEM_GRIMOIRE_USE, 1.2F, MagicEffect.Type.INK);
	public static final MagicAttackRightClickEffect ZILLY_MAGIC = new MagicAttackRightClickEffect(30, 8, null, null, 1.0F, MagicEffect.Type.ZILLY);
	public static final MagicAttackRightClickEffect ECHIDNA_MAGIC = new MagicAttackRightClickEffect(50, 9, null, null, 1.0F, MagicEffect.Type.ECHIDNA);
	
	protected MagicAttackRightClickEffect(int distance, int damage, Supplier<EffectInstance> effect, Supplier<SoundEvent> sound, float pitch, @Nullable MagicEffect.Type type)
	{
		this.distance = distance;
		this.damage = damage;
		this.effect = effect;
		this.sound = sound;
		this.pitch = pitch;
		this.type = type;
	}
	
	@Override
	public ActionResult<ItemStack> onRightClick(World world, PlayerEntity player, Hand hand)
	{
		ItemStack itemStackIn = player.getHeldItem(hand);
		
		if(player instanceof ServerPlayerEntity)
			magicAttack(world, (ServerPlayerEntity) player);
		
		if(player.isCreative())
			player.getCooldownTracker().setCooldown(itemStackIn.getItem(), 10);
		else
			player.getCooldownTracker().setCooldown(itemStackIn.getItem(), 50);
		
		player.swing(hand, true);
		itemStackIn.damageItem(6, player, playerEntity -> playerEntity.sendBreakAnimation(Hand.MAIN_HAND));
		player.addStat(Stats.ITEM_USED.get(itemStackIn.getItem()));
		
		return ActionResult.resultSuccess(itemStackIn);
	}
	
	private void magicAttack(World world, ServerPlayerEntity player)
	{
		if(sound != null && player.getRNG().nextFloat() < .1F) //optional sound effect adding
			world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), sound.get(), SoundCategory.PLAYERS, 0.7F, pitch);
		world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_EVOKER_CAST_SPELL, SoundCategory.PLAYERS, 1.0F, 1.6F);
		
		targetEffect(player);
		
		Vector3d eyePos = player.getEyePosition(1.0F);
		Vector3d lookVec = player.getLookVec();
		
		for(int step = 0; step < distance * 2; step++) //uses the float i value to increase the distance away from where the player is looking and creating a sort of raytrace
		{
			Vector3d vecPos = eyePos.add(lookVec.scale(step / 2D));
			
			boolean hitObstacle = checkCollisionInPath(world, player, vecPos);
			
			if(hitObstacle)
			{
				sendEffectPacket(world, eyePos, lookVec, step, true);
				return;
			}
		}
		sendEffectPacket(world, eyePos, lookVec, distance * 2, false);
	}
	
	// If you're an addon that want to use this class with your own effect, override this to use your own network packet
	protected void sendEffectPacket(World world, Vector3d pos, Vector3d lookVec, int length, boolean collides)
	{
		if(type != null)
			MSPacketHandler.sendToNear(new MagicEffectPacket(type, pos, lookVec, length, collides),
					new PacketDistributor.TargetPoint(pos.x, pos.y, pos.z, 64, world.getDimensionKey()));
	}
	
	protected void targetEffect(ServerPlayerEntity player)
	{
	}
	
	private boolean checkCollisionInPath(World world, ServerPlayerEntity player, Vector3d vecPos)
	{
		BlockPos blockPos = new BlockPos(vecPos);
		
		if(!world.getBlockState(blockPos).allowsMovement(world, blockPos, PathType.LAND))
		{
			return true;
		}
		
		AxisAlignedBB axisAlignedBB = new AxisAlignedBB(blockPos);
		// gets entities in a bounding box around each vector position in the for loop
		LivingEntity closestTarget = player.world.getClosestEntityWithinAABB(LivingEntity.class, visiblePredicate, player, vecPos.x, vecPos.y, vecPos.z, axisAlignedBB);
		if(closestTarget != null)
		{
			int playerRung = PlayerSavedData.getData(player).getEcheladder().getRung();
			
			if(closestTarget instanceof UnderlingEntity)
				closestTarget.attackEntityFrom(DamageSource.causePlayerDamage(player).setMagicDamage(), damage + playerRung / 5F); //damage increase from rung is higher against underlings
			else
				closestTarget.attackEntityFrom(DamageSource.causePlayerDamage(player).setMagicDamage(), damage + playerRung / 10F);
			if(effect != null && player.getRNG().nextFloat() < .25F)
				closestTarget.addPotionEffect(effect.get());
			
			return true;
		} else return false;
	}
	
	private static class SbahjMagicEffect extends MagicAttackRightClickEffect
	{
		SbahjMagicEffect(int distance, int damage, Supplier<EffectInstance> effect, Supplier<SoundEvent> sound, float pitch, @Nullable MagicEffect.Type type)
		{
			super(distance, damage, effect, sound, pitch, type);
		}
		
		@Override
		protected void targetEffect(ServerPlayerEntity player)
		{
			Vector3d randomFacingVecPos = new Vector3d(player.getPosX() + player.getRNG().nextInt(10) - 5, player.getPosY() + player.getRNG().nextInt(10) - 5, player.getPosZ() + player.getRNG().nextInt(10) - 5);
			player.lookAt(player.getCommandSource().getEntityAnchorType(), randomFacingVecPos);
		}
	}
	
	private static class AimbotMagicEffect extends MagicAttackRightClickEffect
	{
		AimbotMagicEffect(int distance, int damage, Supplier<EffectInstance> effect, Supplier<SoundEvent> sound, float pitch, @Nullable MagicEffect.Type type)
		{
			super(distance, damage, effect, sound, pitch, type);
		}
		
		@Override
		protected void targetEffect(ServerPlayerEntity player)
		{
			BlockPos playerEyePos = new BlockPos(player.getPosX(), player.getPosYEye(), player.getPosZ());
			LivingEntity closestVisibleTarget = player.world.getClosestEntityWithinAABB(LivingEntity.class, visiblePredicate, player, player.getPosX(), player.getPosYEye(), player.getPosZ(), new AxisAlignedBB(playerEyePos).grow(11));
			if(closestVisibleTarget != null)
				player.lookAt(EntityAnchorArgument.Type.EYES, closestVisibleTarget, EntityAnchorArgument.Type.EYES);
		}
	}
}
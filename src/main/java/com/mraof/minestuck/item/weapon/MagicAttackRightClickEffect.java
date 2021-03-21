package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.pathfinding.PathType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class MagicAttackRightClickEffect implements ItemRightClickEffect
{
	private final int distance;
	private final int damage;
	private final Supplier<EffectInstance> effect;
	private final Supplier<SoundEvent> sound;
	private final float pitch;
	private final Supplier<IParticleData> particle;
	private final boolean explosiveFinish;
	
	EntityPredicate visiblePredicate = (new EntityPredicate()).setLineOfSiteRequired();
	
	public static final MagicAttackRightClickEffect SBAHJ_AIMBOT_MAGIC = new MagicAttackRightClickEffect(10, 1, null, null, 1.0F, () -> new RedstoneParticleData(0F, 1F, 0F, 2F), false);
	public static final MagicAttackRightClickEffect AIMBOT_MAGIC = new MagicAttackRightClickEffect(15, 2, null, null, 1.0F, () -> ParticleTypes.ENCHANTED_HIT, false);
	public static final MagicAttackRightClickEffect STANDARD_MAGIC = new MagicAttackRightClickEffect(15, 3, null, null, 1.0F, () -> ParticleTypes.ENCHANT, false);
	public static final MagicAttackRightClickEffect POOL_CUE_MAGIC = new MagicAttackRightClickEffect(18, 4, null, null, 1.0F, () -> new RedstoneParticleData(20F, 0F, 0F, 2F), false);
	public static final MagicAttackRightClickEffect HORRORTERROR_MAGIC = new MagicAttackRightClickEffect(20, 5, () -> new EffectInstance(Effects.WITHER, 100, 2), () -> MSSoundEvents.ITEM_GRIMOIRE_USE, 1.2F, () -> ParticleTypes.SQUID_INK, true);
	public static final MagicAttackRightClickEffect ZILLY_MAGIC = new MagicAttackRightClickEffect(30, 8, null, null, 1.0F, () -> new RedstoneParticleData(20F, 20F, 20F, 2F), true);
	public static final MagicAttackRightClickEffect ECHIDNA_MAGIC = new MagicAttackRightClickEffect(50, 8, null, null, 1.0F, () -> ParticleTypes.END_ROD, true);
	
	public MagicAttackRightClickEffect(int distance, int damage, Supplier<EffectInstance> effect, Supplier<SoundEvent> sound, float pitch, Supplier<IParticleData> particle, boolean explosiveFinish)
	{
		this.distance = distance;
		this.damage = damage;
		this.effect = effect;
		this.sound = sound;
		this.pitch = pitch;
		this.particle = particle;
		this.explosiveFinish = explosiveFinish;
	}
	
	@Override
	public ActionResult<ItemStack> onRightClick(World world, PlayerEntity player, Hand hand)
	{
		ItemStack itemStackIn = player.getHeldItem(hand);
		
		magicAttack(world, player, itemStackIn);
		
		if(player.isCreative())
			player.getCooldownTracker().setCooldown(itemStackIn.getItem(), 10);
		else
			player.getCooldownTracker().setCooldown(itemStackIn.getItem(), 50);
		
		player.swing(hand, true);
		itemStackIn.damageItem(6, player, playerEntity -> playerEntity.sendBreakAnimation(Hand.MAIN_HAND));
		player.addStat(Stats.ITEM_USED.get(itemStackIn.getItem()));
		return ActionResult.resultPass(itemStackIn);
	}
	
	void magicAttack(World world, PlayerEntity player, ItemStack itemStack)
	{
		if(sound != null && player.getRNG().nextFloat() <= .1F) //optional sound effect adding
			world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), sound.get(), SoundCategory.PLAYERS, 0.7F, pitch);
		world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_EVOKER_CAST_SPELL, SoundCategory.PLAYERS, 1.0F, 1.6F);
		Vec3d vec3dPath;
		Vec3d vecPos;
		BlockPos blockPos;
		
		if(itemStack.getItem() == MSItems.ARTIFUCKER)
		{
			Vec3d randomFacingVecPos = new Vec3d(player.getPosX() + player.getRNG().nextInt(10) - 5, player.getPosY() + player.getRNG().nextInt(10) - 5, player.getPosZ() + player.getRNG().nextInt(10) - 5);
			player.lookAt(player.getCommandSource().getEntityAnchorType(), randomFacingVecPos);
		}
		if(itemStack.getItem() == MSItems.POINTER_WAND)
		{
			LivingEntity closestVisibleTarget = player.world.getClosestEntityWithinAABB(LivingEntity.class, visiblePredicate, player, player.getPosX(), player.getPosY(), player.getPosZ(), player.getBoundingBox().grow(14.5D, 14, 14.5D));
			if(closestVisibleTarget != null)
				player.lookAt(player.getCommandSource().getEntityAnchorType(), closestVisibleTarget.getPositionVec());
		}
		
		for(float i = 0; i < distance * 2; i++) //uses the float i value to increase the distance away from where the player is looking and creating a sort of raytrace
		{
			vec3dPath = player.getLookVec().scale(i / 2D);
			vecPos = new Vec3d(player.getPosX() + vec3dPath.x, player.getPosYEye() + vec3dPath.y, player.getPosZ() + vec3dPath.z);
			blockPos = new BlockPos(vecPos.x, vecPos.y, vecPos.z);
			float randomParticleOffsetX = (player.getRNG().nextFloat() - .5F) / 4;
			float randomParticleOffsetY = (player.getRNG().nextFloat() - .5F) / 4;
			float randomParticleOffsetZ = (player.getRNG().nextFloat() - .5F) / 4;
			
			vecPathParticles(world, player, vecPos, i, randomParticleOffsetX, randomParticleOffsetY, randomParticleOffsetZ);
			
			boolean hitObstacle = vecPathHit(world, player, vecPos, blockPos);
			if(hitObstacle == true)
			{
				i = distance * 2; //prevents ray from travelling farther
				vecPathFinish(world, player, vecPos, blockPos, randomParticleOffsetX, randomParticleOffsetY, randomParticleOffsetZ);
			}
			
		}
	}
	
	void vecPathParticles(World world, PlayerEntity player, Vec3d vecPos, float i, float randomParticleOffsetX, float randomParticleOffsetY, float randomParticleOffsetZ)
	{
		if(i >= 5) //starts creating particle trail along vector path after a few runs, its away from the players vision so they do not obscure everything
		{
			world.addParticle(particle.get(), true, vecPos.x + randomParticleOffsetX, vecPos.y + randomParticleOffsetY, vecPos.z + randomParticleOffsetZ, 0.0D, 0.0D, 0.0D);
			
			if(particle.get() == ParticleTypes.ENCHANT) //particle is hard to see so this increases visibility
			{
				for(float a = 0; a < 4; a++)
				{
					randomParticleOffsetX = (player.getRNG().nextFloat() - .5F) / 5;
					randomParticleOffsetY = (player.getRNG().nextFloat() - .5F) / 5;
					randomParticleOffsetZ = (player.getRNG().nextFloat() - .5F) / 5;
					world.addParticle(particle.get(), true, vecPos.x + randomParticleOffsetX, vecPos.y + randomParticleOffsetY, vecPos.z + randomParticleOffsetZ, 0.0D, 0.0D, 0.0D);
				}
			}
		}
	}
	
	private boolean vecPathHit(World world, PlayerEntity player, Vec3d vecPos, BlockPos blockPos)
	{
		if(!world.getBlockState(blockPos).allowsMovement(world, blockPos, PathType.LAND))
		{
			return true;
		}
		
		AxisAlignedBB axisAlignedBB = new AxisAlignedBB(blockPos);
		LivingEntity closestTarget = player.world.getClosestEntityWithinAABB(LivingEntity.class, visiblePredicate, player, vecPos.x, vecPos.y, vecPos.z, axisAlignedBB); //gets entities in a bounding box around each vector postion in the for loop
		if(closestTarget != null && player instanceof ServerPlayerEntity)
		{
			int playerRung = PlayerSavedData.getData((ServerPlayerEntity) player).getEcheladder().getRung();
			
			if(closestTarget instanceof UnderlingEntity)
				closestTarget.attackEntityFrom(DamageSource.causePlayerDamage(player).setMagicDamage(), damage + playerRung / 5F); //damage increase from rung is higher against underlings
			else
				closestTarget.attackEntityFrom(DamageSource.causePlayerDamage(player).setMagicDamage(), damage + playerRung / 10F);
			if(effect != null && player.getRNG().nextFloat() <= .25F)
				closestTarget.addPotionEffect(effect.get());
			
			return true;
		} else if(closestTarget != null)
		{
			return true;
		}
		return false;
	}
	
	void vecPathFinish(World world, PlayerEntity player, Vec3d vecPos, BlockPos blockPos, float randomParticleOffsetX, float randomParticleOffsetY, float randomParticleOffsetZ)
	{
		Vec3d particleVecMod = player.getLookVec().inverse().scale(0.5D); //returns the vector to a prior position before it was inside a block/entity so that the flash particle is not obscured and particles can fly out
		Vec3d particleVecPos = new Vec3d(vecPos.x + particleVecMod.x, vecPos.y + particleVecMod.y, vecPos.z + particleVecMod.z);
		if(explosiveFinish) //if a block or entity has been hit and the wand is true for explosiveFinish boolean, adds a sound and flash
		{
			world.playSound(null, blockPos, SoundEvents.ENTITY_SHULKER_BULLET_HIT, SoundCategory.BLOCKS, 1.2F, 0.6F);
			
			world.addParticle(ParticleTypes.FLASH, particleVecPos.x, particleVecPos.y, particleVecPos.z, 0.0D, 0.0D, 0.0D);
			for(int a = 0; a < 25 + player.getRNG().nextInt(10); a++)
			{
				world.addParticle(particle.get(), true, particleVecPos.x + randomParticleOffsetX, particleVecPos.y + randomParticleOffsetY, particleVecPos.z + randomParticleOffsetZ, player.getRNG().nextGaussian() * 0.12D, player.getRNG().nextGaussian() * 0.12D, player.getRNG().nextGaussian() * 0.12D);
			}
		} else  //if a block or entity has been hit and the wand is false for explosiveFinish boolean, adds a small particle effect
		{
			for(int a = 0; a < 25 + player.getRNG().nextInt(10); a++)
			{
				world.addParticle(ParticleTypes.CRIT, true, particleVecPos.x + randomParticleOffsetX, particleVecPos.y + randomParticleOffsetY, particleVecPos.z + randomParticleOffsetZ, player.getRNG().nextGaussian(), player.getRNG().nextGaussian(), player.getRNG().nextGaussian());
			}
		}
	}
}
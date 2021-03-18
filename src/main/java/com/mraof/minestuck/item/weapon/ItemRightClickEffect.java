package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Supplier;

public interface ItemRightClickEffect
{
	EntityPredicate visiblePredicate = (new EntityPredicate()).setLineOfSiteRequired();
	
	ItemRightClickEffect HORRORTERROR_MAGIC = summonMagicProjectile(16, 5, () -> new EffectInstance(Effects.WITHER, 100, 2), () -> SoundEvents.ENTITY_EVOKER_CAST_SPELL, 1.2F, ParticleTypes.SMOKE);
	
	ItemRightClickEffect ACTIVE_HAND = (world, player, hand) -> {
		player.setActiveHand(hand);
		return ActionResult.resultConsume(player.getHeldItem(hand));
	};
	
	static ItemRightClickEffect switchTo(Supplier<Item> otherItem)
	{
		return (world, player, hand) -> {
			ItemStack itemStackIn = player.getHeldItem(hand);
			if(player.isSneaking())
			{
				ItemStack newItem = new ItemStack(otherItem.get(), itemStackIn.getCount());
				newItem.setTag(itemStackIn.getTag());
				
				return ActionResult.resultSuccess(newItem);
			}
			return ActionResult.resultPass(itemStackIn);
		};
	}
	
	static ItemRightClickEffect summonFireball()
	{
		return (world, player, hand) -> {
			ItemStack itemStackIn = player.getHeldItem(hand);
			world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.PLAYERS, 1.0F, 0.8F);
			
			AxisAlignedBB axisalignedbb = player.getBoundingBox().grow(32.0D, 32.0D, 32.0D);
			List<LivingEntity> list = player.world.getEntitiesWithinAABB(LivingEntity.class, axisalignedbb);
			list.remove(player);
			if(!list.isEmpty() && !world.isRemote)
			{
				for(LivingEntity livingentity : list)
				{
					FireballEntity fireball = new FireballEntity(world, player, 0, -8.0, 0);
					fireball.explosionPower = 1;
					fireball.setPosition(livingentity.getPosX() + (player.getRNG().nextInt(6) - 3), livingentity.getPosY() + 40, livingentity.getPosZ() + (player.getRNG().nextInt(6) - 3));
					player.getCooldownTracker().setCooldown(itemStackIn.getItem(), 20);
					itemStackIn.damageItem(2, player, playerEntity -> playerEntity.sendBreakAnimation(Hand.MAIN_HAND));
					world.addEntity(fireball);
				}
			} else if(!world.isRemote)
			{
				FireballEntity fireball = new FireballEntity(world, player, 0, -8.0, 0);
				fireball.explosionPower = 1;
				fireball.setPosition(player.getPosX() + (player.getRNG().nextInt(20) - 10), player.getPosY() + 40, player.getPosZ() + (player.getRNG().nextInt(20) - 10));
				player.getCooldownTracker().setCooldown(itemStackIn.getItem(), 20);
				itemStackIn.damageItem(2, player, playerEntity -> playerEntity.sendBreakAnimation(Hand.MAIN_HAND));
				world.addEntity(fireball);
			}
			return ActionResult.resultPass(itemStackIn);
		};
	}
	
	static ItemRightClickEffect summonMagicProjectile(int distance, int damage, Supplier<EffectInstance> effect, Supplier<SoundEvent> sound, float pitch, BasicParticleType particle)
	{
		return (world, player, hand) -> {
			ItemStack itemStackIn = player.getHeldItem(hand);
			world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), sound.get(), SoundCategory.PLAYERS, 1.0F, pitch);
			Vec3d vec3dPath;
			Vec3d vecPos;
			BlockPos blockPos;
			
			for(float i = 0; i < distance * 2; i++)
			{
				vec3dPath = player.getLookVec().scale(i / 2);
				vecPos = new Vec3d(player.getPosX() + vec3dPath.x, player.getPosYEye() + vec3dPath.y, player.getPosZ() + vec3dPath.z);
				blockPos = new BlockPos(vecPos.x, vecPos.y, vecPos.z);
				float randomParticleOffset = (player.getRNG().nextFloat() - .5F)/3;
				world.addParticle(particle, vecPos.x + randomParticleOffset, vecPos.y + randomParticleOffset, vecPos.z + randomParticleOffset, 0.0D, 0.0D, 0.0D);
				if(!world.getBlockState(blockPos).allowsMovement(world, blockPos, PathType.LAND))
				{
					i = distance * 2;
				}
				AxisAlignedBB axisAlignedBB = new AxisAlignedBB(blockPos);
				LivingEntity closestTarget = player.world.getClosestEntityWithinAABB(LivingEntity.class, visiblePredicate, player, vecPos.x, vecPos.y, vecPos.z, axisAlignedBB);
				if(closestTarget != null && player instanceof ServerPlayerEntity)
				{
					if(closestTarget instanceof UnderlingEntity)
						closestTarget.attackEntityFrom(DamageSource.causePlayerDamage(player).setMagicDamage(), damage * PlayerSavedData.getData((ServerPlayerEntity) player).getEcheladder().getRung()/2F);
					else
						closestTarget.attackEntityFrom(DamageSource.causePlayerDamage(player).setMagicDamage(), damage);
					if(effect != null)
						closestTarget.addPotionEffect(effect.get());
					closestTarget.setRevengeTarget(player);
					i = distance * 2;
				}
				
				if(i >= distance * 2 - 1)
				{
					world.playSound(null, blockPos, SoundEvents.ENTITY_SHULKER_BULLET_HIT, SoundCategory.BLOCKS, 1.0F, 0.4F);
					for(int a = 0; a < 25+player.getRNG().nextInt(10); a++)
					{
						//world.addParticle(particle, vecPos.x + randomParticleOffset, vecPos.y + randomParticleOffset, vecPos.z + randomParticleOffset, player.getRNG().nextGaussian() * 0.07D, player.getRNG().nextGaussian() * 0.07D, player.getRNG().nextGaussian() * 0.07D);
						world.addParticle(particle, true, vecPos.x + randomParticleOffset, vecPos.y + randomParticleOffset, vecPos.z + randomParticleOffset, player.getRNG().nextGaussian() * 0.07D, player.getRNG().nextGaussian() * 0.07D, player.getRNG().nextGaussian() * 0.07D);
					}
				}
			}
			
			player.getCooldownTracker().setCooldown(itemStackIn.getItem(), 50);
			if(player.isCreative())
				player.getCooldownTracker().setCooldown(itemStackIn.getItem(), 10);
			player.swing(hand, true);
			itemStackIn.damageItem(6, player, playerEntity -> playerEntity.sendBreakAnimation(Hand.MAIN_HAND));
			player.addStat(Stats.ITEM_USED.get(itemStackIn.getItem()));
			return ActionResult.resultPass(itemStackIn);
		};
	}
	
	static ItemRightClickEffect extinguishFire(int mod)
	{
		return (world, player, hand) -> {
			ItemStack itemStackIn = player.getHeldItem(hand);
			
			if(!world.isRemote)
			{
				world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_ENDER_DRAGON_FLAP, SoundCategory.PLAYERS, 1.0F, 1.4F);
				
				for(BlockPos blockPos : BlockPos.getAllInBoxMutable(player.getPosition().add(2 * mod, mod, 2 * mod), player.getPosition().add(-2 * mod, -1 * mod, -2 * mod)))
				{
					BlockState blockState = world.getBlockState(blockPos);
					if(blockState.getBlock() == Blocks.FIRE)
					{
						world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
						world.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 1.0F);
					}
				}
				
				player.swing(hand, true);
				player.getCooldownTracker().setCooldown(itemStackIn.getItem(), 15);
				itemStackIn.damageItem(1, player, playerEntity -> playerEntity.sendBreakAnimation(Hand.MAIN_HAND));
			}
			return ActionResult.resultPass(itemStackIn);
		};
	}
	
	/*static ItemRightClickEffect magicEffect(Supplier<EffectInstance> effect, Supplier<SoundEvent> sound, float pitch)
	{
		return (world, player, hand) -> {
			world.playSound(null, player.getPosition(), sound.get(), SoundCategory.PLAYERS, 1.5F, pitch);
			//livingentity.addPotionEffect(effect.get());
			return ActionResult.resultPass(itemStackIn);
		};
	}*/
	
	ActionResult<ItemStack> onRightClick(World world, PlayerEntity player, Hand hand);
}
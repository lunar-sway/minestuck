package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.util.Debug;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathType;
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
	
	ActionResult<ItemStack> onRightClick(World world, PlayerEntity player, Hand hand);
	
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
	
	static ItemRightClickEffect summonMagicProjectile(int distance, int damage)
	{
		return (world, player, hand) -> {
			ItemStack itemStackIn = player.getHeldItem(hand);
			world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL, SoundCategory.PLAYERS, 0.8F, 1.6F);
			
			//LivingEntity closestTarget = player.world.getClosestEntityWithinAABB(LivingEntity.class, visiblePredicate, player, player.getPosX(), player.getPosY(), player.getPosZ(), player.getBoundingBox().expand(player.getLookVec().scale(5.0D+velocity)).shrink(1.0D));
			Vec3d vec3d;
			
			
			for(float i = 0; i < distance*2; i++)
			{
				vec3d = player.getLookVec().scale(i/2);
				BlockPos projectilePos = new BlockPos(player.getPosX() + vec3d.x, player.getPosYEye() + vec3d.y, player.getPosZ() + vec3d.z);
				if(!world.getBlockState(projectilePos).allowsMovement(world, projectilePos, PathType.LAND))
				{
					break;
				}
				AxisAlignedBB axisAlignedBB = new AxisAlignedBB(projectilePos);
				//List<Entity> livingEntityList = world.getEntitiesWithinAABBExcludingEntity(player, axisAlignedBB);
				LivingEntity closestTarget = player.world.getClosestEntityWithinAABB(LivingEntity.class, visiblePredicate, player, projectilePos.getX(), projectilePos.getY(), projectilePos.getZ(), axisAlignedBB);
				if(closestTarget != null)
				{
					if(closestTarget instanceof UnderlingEntity)
						closestTarget.attackEntityFrom(DamageSource.MAGIC, damage * 1.5F);
					else
						closestTarget.attackEntityFrom(DamageSource.MAGIC, damage);
					break;
				}
				world.addParticle(ParticleTypes.END_ROD, projectilePos.getX(), projectilePos.getY(), projectilePos.getZ(), 0.0D, 0.0D, 0.0D);
				Debug.debugf("%s", projectilePos);
			}
			
			player.getCooldownTracker().setCooldown(itemStackIn.getItem(), 50);
			if(player.isCreative())
				player.getCooldownTracker().setCooldown(itemStackIn.getItem(), 10);
			player.swing(hand, true);
			itemStackIn.damageItem(4, player, playerEntity -> playerEntity.sendBreakAnimation(Hand.MAIN_HAND));
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
}
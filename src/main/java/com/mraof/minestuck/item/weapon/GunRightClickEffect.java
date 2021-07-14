package com.mraof.minestuck.item.weapon;

import com.google.common.collect.Lists;
import com.mraof.minestuck.client.util.GunEffect;
import com.mraof.minestuck.client.util.MagicEffect;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.item.BulletItem;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.crafting.alchemy.GristType;
import com.mraof.minestuck.item.crafting.alchemy.GristTypes;
import com.mraof.minestuck.network.GunEffectPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.MagicEffectPacket;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.util.MSTags;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class GunRightClickEffect implements ItemRightClickEffect
{
	private final double accuracy;
	private final int damage;
	private final int distance;
	private final int reloadTime;
	private final Supplier<EffectInstance> effect;
	private final Supplier<SoundEvent> sound;
	private final float pitch;
	@Nullable
	private final GunEffect.Type type;
	
	EntityPredicate visiblePredicate = (new EntityPredicate()).setLineOfSiteRequired();
	
	//public static final GunRightClickEffect SBAHJ_AIMBOT_MAGIC = new SbahjMagicEffect(10, 1, null, null, 1.0F, MagicEffect.Type.GREEN);
	//public static final GunRightClickEffect AIMBOT_MAGIC = new AimbotMagicEffect(14, 2, null, null, 1.0F, MagicEffect.Type.CRIT);
	
	public static final GunRightClickEffect SHORT_DISTANCE = new GunRightClickEffect(2, 3, 20, 15, null, null, 1.0F, GunEffect.Type.SMOKE);
	
	public static final GunRightClickEffect STANDARD_DISTANCE_ABYSMAL_SPEED = new GunRightClickEffect(10, 4, 35, 130, null, null, 1.0F, GunEffect.Type.SMOKE);
	public static final GunRightClickEffect STANDARD_DISTANCE_LOW_SPEED = new GunRightClickEffect(10, 4, 35, 70, null, null, 1.0F, GunEffect.Type.SMOKE);
	public static final GunRightClickEffect STANDARD_DISTANCE = new GunRightClickEffect(10, 4, 35, 15, null, null, 1.0F, GunEffect.Type.SMOKE);
	public static final GunRightClickEffect STANDARD_DISTANCE_HIGH_SPEED = new GunRightClickEffect(10, 4, 35, 4, null, null, 1.0F, GunEffect.Type.SMOKE);
	public static final GunRightClickEffect STANDARD_DISTANCE_EXTREME_SPEED = new GunRightClickEffect(10, 4, 35, 0, null, null, 1.0F, GunEffect.Type.SMOKE);
	
	public static final GunRightClickEffect GREEN_SUN_STREETSWEEPER = new GunRightClickEffect(4, 5, 45, 0, () -> new EffectInstance(Effects.WITHER, 120, 1), null, 1.0F, GunEffect.Type.GREEN);
	
	public static final GunRightClickEffect IMPROVED_DISTANCE = new GunRightClickEffect(20, 5, 50, 15, null, null, 1.0F, GunEffect.Type.SMOKE);
	
	public static final GunRightClickEffect LONG_DISTANCE_LOW_SPEED = new GunRightClickEffect(35, 8, 75, 70, null, null, 1.0F, GunEffect.Type.SMOKE);
	public static final GunRightClickEffect LONG_DISTANCE = new GunRightClickEffect(35, 8, 75, 15, null, null, 1.0F, GunEffect.Type.SMOKE);
	public static final GunRightClickEffect LONG_DISTANCE_HIGH_SPEED = new GunRightClickEffect(35, 8, 75, 4, null, null, 1.0F, GunEffect.Type.SMOKE);
	public static final GunRightClickEffect LONG_DISTANCE_EXTREME_SPEED = new GunRightClickEffect(35, 8, 75, 1, null, null, 1.0F, GunEffect.Type.SMOKE);
	
	public static final GunRightClickEffect EXTREME_DISTANCE = new GunRightClickEffect(100, 9, 90, 15, null, null, 1.0F, GunEffect.Type.SMOKE);
	
	protected GunRightClickEffect(double accuracy, int damage, int distance, int reloadTime, Supplier<EffectInstance> effect, Supplier<SoundEvent> sound, float pitch, @Nullable GunEffect.Type type)
	{
		this.accuracy = accuracy;
		this.damage = damage;
		this.distance = distance;
		this.reloadTime = reloadTime;
		this.effect = effect;
		this.sound = sound;
		this.pitch = pitch;
		this.type = type;
	}
	
	@Override
	public ActionResult<ItemStack> onRightClick(World world, PlayerEntity player, Hand hand)
	{
		ItemStack itemStackIn = player.getHeldItem(hand);
		//ItemStack item = player.getHeldItem(hand);
		//List<ItemStack> bulletStack = Lists.newArrayList();
		ItemStack bulletStack = null;
		boolean foundItem = false;
		List<Item> bulletType = new ArrayList<>(MSTags.Items.BULLETS.getAllElements());
		List<Item> gunType = new ArrayList<>(MSTags.Items.GUNS.getAllElements());
		//Collections.sort(types);
		//types = types.stream().skip(page * rows * columns).limit(rows * columns).collect(Collectors.toList());
		
		for(int i = 0; i < MSTags.Items.BULLETS.getAllElements().size(); i++)
		{
			if(!foundItem)
			{
				for(ItemStack invItem : player.inventory.mainInventory)
				{
					Debug.debugf("i = %s, invItem = %s, bulletType = %s", i, invItem, bulletType.get(i).getDefaultInstance());
					if(ItemStack.areItemsEqual(invItem, bulletType.get(i).getDefaultInstance()))
					{
						Debug.debugf("foundItem!");
						foundItem = true;
						bulletStack = invItem;
						if(!player.isCreative())
							invItem.shrink(1);
						break;
					}
				}
			}
		}
		
		if(player instanceof ServerPlayerEntity && foundItem)
			gunShoot(world, (ServerPlayerEntity) player, bulletStack);
		
		if(foundItem)
		{
			for(int i = 0; i < MSTags.Items.GUNS.getAllElements().size(); i++)
			{
				player.getCooldownTracker().setCooldown(gunType.get(i), reloadTime);
			}
			
			itemStackIn.damageItem(1, player, playerEntity -> playerEntity.sendBreakAnimation(Hand.MAIN_HAND));
			player.addStat(Stats.ITEM_USED.get(itemStackIn.getItem()));
		}
		
		return ActionResult.resultPass(itemStackIn);
	}
	
	private void gunShoot(World world, ServerPlayerEntity player, ItemStack bulletStack)
	{
		if(bulletStack.getItem() instanceof BulletItem)
		{
			BulletItem bulletItem = (BulletItem) bulletStack.getItem();
			
			if(sound != null && player.getRNG().nextFloat() < .1F) //optional sound effect adding
				world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), sound.get(), SoundCategory.PLAYERS, 0.7F, pitch);
			world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.AMBIENT, 0.9F, 2F);
			
			targetEffect(player);
			
			/*for(int i = 0; i < this.multiShot; i++) //for shotgun to run several times, will put all of below in it
			{
			
			}*/
			double accuracyFactor = (player.getRNG().nextDouble() - 0.5D) / (bulletItem.getAccuracy() + this.accuracy);
			
			Vec3d eyePos = player.getEyePosition(1.0F);
			Vec3d lookVec = player.getLookVec().add(accuracyFactor, accuracyFactor, accuracyFactor);
			Debug.debugf("accuracyFactor = %s, lookVec = %s, unchanged lookVec = %s", accuracyFactor, lookVec, player.getLookVec());
			int travelDistance = distance + bulletItem.getDistance() + player.getRNG().nextInt(((int) distance / 10));
			
			for(int step = 0; step < (travelDistance) * 2; step++) //uses the float i value to increase the distance away from where the player is looking and creating a sort of raytrace
			{
				Vec3d vecPos = eyePos.add(lookVec.scale(step / 2D));
				
				boolean hitObstacle = checkCollisionInPath(world, player, vecPos, bulletItem);
				
				if(hitObstacle)
				{
					sendEffectPacket(world, eyePos, lookVec, step, true);
					return;
				}
			}
			sendEffectPacket(world, eyePos, lookVec, travelDistance * 2, false);
		}
	}
	
	// If you're an addon that want to use this class with your own effect, override this to use your own network packet
	protected void sendEffectPacket(World world, Vec3d pos, Vec3d lookVec, int length, boolean collides)
	{
		if(type != null)
			MSPacketHandler.sendToNear(new GunEffectPacket(type, pos, lookVec, length, collides),
					new PacketDistributor.TargetPoint(pos.x, pos.y, pos.z, 64, world.getDimension().getType()));
	}
	
	protected void targetEffect(ServerPlayerEntity player)
	{
	}
	
	private boolean checkCollisionInPath(World world, ServerPlayerEntity player, Vec3d vecPos, BulletItem bulletItem)
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
				closestTarget.attackEntityFrom(DamageSource.causePlayerDamage(player).setProjectile(), damage + bulletItem.getDamage() + playerRung / 2F); //damage increase from rung is higher against underlings
			else if(closestTarget instanceof PlayerEntity)
				closestTarget.attackEntityFrom(DamageSource.causePlayerDamage(player).setProjectile(), (damage + bulletItem.getDamage() + playerRung / 5F) / 1.8F);
			else
				closestTarget.attackEntityFrom(DamageSource.causePlayerDamage(player).setProjectile(), damage + bulletItem.getDamage() + playerRung / 5F);
			if(effect != null && player.getRNG().nextFloat() < .1F)
				closestTarget.addPotionEffect(effect.get());
			if(bulletItem.getEffect() != null && player.getRNG().nextFloat() < .75F)
				closestTarget.addPotionEffect(bulletItem.getEffect().get());
			
			return true;
		} else return false;
	}
	
	/*private static class SbahjMagicEffect extends GunRightClickEffect
	{
		SbahjMagicEffect(double accuracy, int damage, int distance, int reloadSpeed, Supplier<EffectInstance> effect, Supplier<SoundEvent> sound, float pitch, @Nullable GunEffect.Type type)
		{
			super(accuracy, damage, distance, reloadSpeed, effect, sound, pitch, type);
		}
		
		@Override
		protected void targetEffect(ServerPlayerEntity player)
		{
			Vec3d randomFacingVecPos = new Vec3d(player.getPosX() + player.getRNG().nextInt(10) - 5, player.getPosY() + player.getRNG().nextInt(10) - 5, player.getPosZ() + player.getRNG().nextInt(10) - 5);
			player.lookAt(player.getCommandSource().getEntityAnchorType(), randomFacingVecPos);
		}
	}*/
}
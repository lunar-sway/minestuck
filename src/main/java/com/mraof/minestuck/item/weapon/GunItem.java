package com.mraof.minestuck.item.weapon;

import com.google.common.collect.ImmutableList;
import com.mraof.minestuck.client.util.GunEffect;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.item.BulletItem;
import com.mraof.minestuck.network.GunEffectPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.MSTags;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.pathfinding.PathType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class GunItem extends Item
{
	private final double accuracy;
	private final int damage;
	private final int distance;
	final int reloadTime;
	
	@Nullable
	private final ItemRightClickEffect itemRightClickEffect;
	private final int useDuration;
	private final UseAction useAction;
	private final List<FinishUseItemEffect> itemUsageEffects;
	private final List<InventoryTickEffect> tickEffects;
	private final int multipleProjectiles;
	private boolean doNotProceedIfCrouch = false;
	@Nullable
	private final Supplier<Items> specificAmmo;
	
	@Nullable
	private final Supplier<EffectInstance> effect;
	@Nullable
	private Supplier<SoundEvent> sound;
	private float pitch = 2.0F;
	public int projectileAmount;
	public int penetratingPower;
	public List<LivingEntity> struckEntities = new ArrayList<>();
	
	EntityPredicate visiblePredicate = (new EntityPredicate()).setLineOfSiteRequired();
	
	@Nullable
	private GunEffect.Type type;
	
	
	@Deprecated
	public GunItem(Properties properties, double accuracy, int damage, int distance, int reloadTime)
	{
		this(new GunItem.Builder(accuracy, damage, distance, reloadTime), properties);
	}
	
	public GunItem(GunItem.Builder builder, Properties properties)
	{
		super(properties);
		accuracy = builder.accuracy;
		damage = builder.damage;
		distance = builder.distance;
		reloadTime = builder.reloadTime;
		itemRightClickEffect = builder.itemRightClickEffect;
		useDuration = builder.useDuration;
		useAction = builder.useAction;
		itemUsageEffects = ImmutableList.copyOf(builder.itemUsageEffects);
		tickEffects = ImmutableList.copyOf(builder.tickEffects);
		multipleProjectiles = builder.multipleProjectiles;
		doNotProceedIfCrouch = builder.doNotProceedIfCrouch;
		specificAmmo = builder.specificAmmo;
		effect = builder.effect;
		sound = builder.sound;
		pitch = builder.pitch;
		type = builder.type;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
	{
		ItemStack itemStackIn = player.getHeldItem(hand);
		ItemStack bulletStack = null;
		boolean foundItem = false;
		List<Item> bulletType = new ArrayList<>(MSTags.Items.BULLETS.getAllElements());
		List<Item> gunType = new ArrayList<>(MSTags.Items.GUNS.getAllElements());
		
		if(!doNotProceedIfCrouch(player)/* || */)
		{
			for(ItemStack invItem : player.inventory.mainInventory)
			{
				if(!foundItem)
				{
					for(int i = 0; i < MSTags.Items.BULLETS.getAllElements().size(); i++)
					{
						if(multipleProjectiles > 0)
						{
							if(ItemStack.areItemsEqual(invItem, bulletType.get(i).getDefaultInstance()) && invItem.getCount() >= projectileAmount)
							{
								foundItem = true;
								bulletStack = invItem;
								if(!player.isCreative())
									invItem.shrink(projectileAmount);
								break;
							}
						} else
						{
							if(ItemStack.areItemsEqual(invItem, bulletType.get(i).getDefaultInstance()))
							{
								foundItem = true;
								bulletStack = invItem;
								if(!player.isCreative())
									invItem.shrink(1);
								break;
							}
						}
					}
				}
			}
			
			if(!foundItem)
			{
				world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 0.8F, 1.6F);
			}
			
			if(foundItem)
			{
				if(player instanceof ServerPlayerEntity)
				{
					projectileAmount = getMultipleProjectiles();
					for(int i = 0; i < projectileAmount + 1; i++)
					{
						gunShoot(world, (ServerPlayerEntity) player, bulletStack);
					}
				}
				for(int i = 0; i < MSTags.Items.GUNS.getAllElements().size(); i++)
				{
					player.getCooldownTracker().setCooldown(gunType.get(i), reloadTime);
				}
				
				itemStackIn.damageItem(1, player, playerEntity -> playerEntity.sendBreakAnimation(Hand.MAIN_HAND));
				player.addStat(Stats.ITEM_USED.get(itemStackIn.getItem()));
			}
		}
		
		if(itemRightClickEffect != null)
			itemRightClickEffect.onRightClick(world, player, hand);
		
		return ActionResult.resultPass(itemStackIn);
	}
	
	void gunShoot(World world, ServerPlayerEntity player, ItemStack bulletStack)
	{
		if(bulletStack.getItem() instanceof BulletItem)
		{
			BulletItem bulletItem = (BulletItem) bulletStack.getItem();
			
			/*if(sound != null && player.getRNG().nextFloat() < .1F) //optional sound effect adding
				world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), sound.get(), SoundCategory.PLAYERS, 0.7F, pitch);*/
			if(sound == null)
			{
				sound = () -> SoundEvents.ENTITY_GENERIC_EXPLODE;
				pitch = 2.0F;
			}
			
			world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), sound.get(), SoundCategory.AMBIENT, 0.9F, pitch);
			
			targetEffect(player);
			
			double accuracyFactor = bulletItem.getAccuracy() + this.accuracy;
			
			Vec3d eyePos = player.getEyePosition(1.0F);
			Vec3d lookVec = player.getLookVec().add((player.getRNG().nextDouble() - 0.5D) / accuracyFactor, (player.getRNG().nextDouble() - 0.5D) / accuracyFactor, (player.getRNG().nextDouble() - 0.5D) / accuracyFactor);
			int travelDistance = distance + bulletItem.getDistance() + player.getRNG().nextInt(((int) distance / 10));
			penetratingPower = bulletItem.getPenetratingPower();
			
			struckEntities.clear();
			
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
		if(type == null)
			type = GunEffect.Type.SMOKE; //TODO not sure if this is where it breaks, but specifically when a bullet hits entities there is no particle effects
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
			
			float strikePowerPercentage = (float) ((penetratingPower + 0.00001) / (bulletItem.getPenetratingPower() + 0.00001));
			Debug.debugf("strikePowerPercentage = %s", strikePowerPercentage);
			int combinedDamages = damage + bulletItem.getDamage();
			
			if(closestTarget instanceof UnderlingEntity)
				closestTarget.attackEntityFrom(DamageSource.causePlayerDamage(player).setProjectile(), (combinedDamages + playerRung / 2F) * strikePowerPercentage); //damage increase from rung is higher against underlings
			else if(closestTarget instanceof PlayerEntity)
				closestTarget.attackEntityFrom(DamageSource.causePlayerDamage(player).setProjectile(), ((combinedDamages + playerRung / 5F) / 1.8F) * strikePowerPercentage);
			else
				closestTarget.attackEntityFrom(DamageSource.causePlayerDamage(player).setProjectile(), (combinedDamages + playerRung / 5F) * strikePowerPercentage);
			
			if(effect != null && player.getRNG().nextFloat() < .1F)
				closestTarget.addPotionEffect(effect.get());
			if(bulletItem.getEffect() != null && player.getRNG().nextFloat() < .75F) //TODO causes non-fatal error with null bullet effects
				closestTarget.addPotionEffect(bulletItem.getEffect().get());
			if(bulletItem.getFlame() > 0 && player.getRNG().nextFloat() < .75F)
				closestTarget.setFire(bulletItem.getFlame());
			
			if(penetratingPower > 0)
			{
				if(!struckEntities.contains(closestTarget))
				{
					penetratingPower--;
					struckEntities.add(closestTarget);
				}
				return false;
			} else
				return true;
		} else return false;
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{
		double reloadTimeInSeconds = ((float) reloadTime) / 20F;
		reloadTimeInSeconds = Math.round(reloadTimeInSeconds * 100.0) / 100.0;
		tooltip.add(new TranslationTextComponent("item.minestuck." + this + ".values", accuracy, damage, distance, reloadTimeInSeconds));
	}
	
	public int getMultipleProjectiles()
	{
		return multipleProjectiles;
	}
	
	public boolean doNotProceedIfCrouch(PlayerEntity player)
	{
		if(doNotProceedIfCrouch)
			return player.isSneaking();
		else
			return false;
	}
	
	public static class Builder
	{
		private final double accuracy;
		private final int damage;
		private final int distance;
		private final int reloadTime;
		
		private final List<OnHitEffect> onHitEffects = new ArrayList<>();
		@Nullable
		private ItemRightClickEffect itemRightClickEffect;
		private int useDuration = 0;
		private UseAction useAction = UseAction.NONE;
		private final List<FinishUseItemEffect> itemUsageEffects = new ArrayList<>();
		private final List<InventoryTickEffect> tickEffects = new ArrayList<>();
		
		private int multipleProjectiles;
		private boolean doNotProceedIfCrouch;
		private Supplier<Items> specificAmmo;
		private Supplier<EffectInstance> effect;
		private Supplier<SoundEvent> sound;
		private float pitch;
		
		@Nullable
		GunEffect.Type type;
		
		public Builder(double accuracy, int damage, int distance, int reloadTime)
		{
			this.accuracy = accuracy;
			this.damage = damage;
			this.distance = distance;
			this.reloadTime = reloadTime;
		}
		
		public GunItem.Builder set(ItemRightClickEffect effect)
		{
			if(itemRightClickEffect != null)
				throw new IllegalStateException("Item right click effect has already been set");
			itemRightClickEffect = effect;
			return this;
		}
		
		public GunItem.Builder add(InventoryTickEffect... effects)
		{
			tickEffects.addAll(Arrays.asList(effects));
			return this;
		}
		
		public GunItem.Builder soundEffect(Supplier<SoundEvent> soundEffect)
		{
			sound = soundEffect;
			return this;
		}
		
		public GunItem.Builder multipleProjectiles(int projectileAmount)
		{
			multipleProjectiles = projectileAmount;
			return this;
		}
		
		public GunItem.Builder doNotProceedIfShiftClick()
		{
			doNotProceedIfCrouch = true;
			return this;
		}
		
		public GunItem.Builder potionEffect(Supplier<EffectInstance> potionEffect)
		{
			effect = potionEffect;
			return this;
		}
		
		public GunItem.Builder gunEffectType(GunEffect.Type gunEffectType)
		{
			type = gunEffectType;
			return this;
		}
		
		public GunItem.Builder needsSpecificAmmo(Supplier<Items> itemIn)
		{
			specificAmmo = itemIn;
			return this;
		}
	}
}

/*
public class GunItem extends Item
{
	private final double accuracy;
	private final int damage;
	private final int distance;
	final int reloadTime;
	
	@Nullable
	private final ItemRightClickEffect itemRightClickEffect;
	private final int useDuration;
	private final UseAction useAction;
	private final List<FinishUseItemEffect> itemUsageEffects;
	private final List<InventoryTickEffect> tickEffects;
	private int multipleProjectiles;
	private boolean doNotProceedIfCrouch = false;
	@Nullable
	private final Supplier<Item> specificAmmo;
	
	@Nullable
	private final Supplier<EffectInstance> effect;
	@Nullable
	private Supplier<SoundEvent> sound;
	private float pitch = 2.0F;
	public int projectileAmount;
	public int penetratingPower;
	public List<LivingEntity> struckEntities = new ArrayList<>();
	
	EntityPredicate visiblePredicate = (new EntityPredicate()).setLineOfSiteRequired();
	
	@Nullable
	private GunEffect.Type type;
	
	
	@Deprecated
	public GunItem(Properties properties, double accuracy, int damage, int distance, int reloadTime)
	{
		this(new GunItem.Builder(accuracy, damage, distance, reloadTime), properties);
	}
	
	public GunItem(GunItem.Builder builder, Properties properties)
	{
		super(properties);
		accuracy = builder.accuracy;
		damage = builder.damage;
		distance = builder.distance;
		reloadTime = builder.reloadTime;
		itemRightClickEffect = builder.itemRightClickEffect;
		useDuration = builder.useDuration;
		useAction = builder.useAction;
		itemUsageEffects = ImmutableList.copyOf(builder.itemUsageEffects);
		tickEffects = ImmutableList.copyOf(builder.tickEffects);
		multipleProjectiles = builder.multipleProjectiles;
		doNotProceedIfCrouch = builder.doNotProceedIfCrouch;
		specificAmmo = builder.specificAmmo;
		effect = builder.effect;
		sound = builder.sound;
		pitch = builder.pitch;
		type = builder.type;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
	{
		ItemStack itemStackIn = player.getHeldItem(hand);
		ItemStack bulletStack = null;
		boolean foundItem = false;
		List<Item> bulletType = new ArrayList<>(MSTags.Items.BULLETS.getAllElements());
		List<Item> gunType = new ArrayList<>(MSTags.Items.GUNS.getAllElements());
		
		if(!doNotProceedIfCrouch(player) || )
		{
		for(ItemStack invItem : player.inventory.mainInventory)
		{
		if(!foundItem)
		{
		if(specificAmmo != null)
		{
		if(ItemStack.areItemsEqual(invItem, specificAmmo.get().getDefaultInstance()) && invItem.getCount() >= projectileAmount)
		{
		foundItem = true;
		bulletStack = invItem;
		if(!player.isCreative())
		invItem.shrink(projectileAmount);
		break;
		}
		} else
		{
		for(int i = 0; i < MSTags.Items.BULLETS.getAllElements().size(); i++)
		{
		if(multipleProjectiles <= 0)
		multipleProjectiles = 1;
		
		if(ItemStack.areItemsEqual(invItem, bulletType.get(i).getDefaultInstance()) && invItem.getCount() >= projectileAmount)
		{
		foundItem = true;
		bulletStack = invItem;
		if(!player.isCreative())
		invItem.shrink(projectileAmount);
		break;
		}
		} else
						{
							if(ItemStack.areItemsEqual(invItem, bulletType.get(i).getDefaultInstance()))
							{
								foundItem = true;
								bulletStack = invItem;
								if(!player.isCreative())
									invItem.shrink(1);
								break;
							}
						}
		}
		}
		}
		}
		
		if(!foundItem)
		{
		world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 0.8F, 1.6F);
		}
		
		if(foundItem)
		{
		if(player instanceof ServerPlayerEntity)
		{
		projectileAmount = getMultipleProjectiles();
		for(int i = 0; i < projectileAmount; i++)
		{
		gunShoot(world, (ServerPlayerEntity) player, bulletStack);
		}
		}
		for(int i = 0; i < MSTags.Items.GUNS.getAllElements().size(); i++)
		{
		player.getCooldownTracker().setCooldown(gunType.get(i), reloadTime);
		}
		
		itemStackIn.damageItem(1, player, playerEntity -> playerEntity.sendBreakAnimation(Hand.MAIN_HAND));
		player.addStat(Stats.ITEM_USED.get(itemStackIn.getItem()));
		}
		
		if(itemRightClickEffect != null)
		itemRightClickEffect.onRightClick(world, player, hand);
		
		return ActionResult.resultPass(itemStackIn);
		}
		
		void gunShoot(World world, ServerPlayerEntity player, ItemStack bulletStack)
		{
		if(bulletStack.getItem() instanceof BulletItem)
		{
		BulletItem bulletItem = (BulletItem) bulletStack.getItem();
		
		if(sound == null)
		{
		sound = () -> SoundEvents.ENTITY_GENERIC_EXPLODE;
		pitch = 2.0F;
		}
		
		world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), sound.get(), SoundCategory.AMBIENT, 1.2F, pitch);
		
		targetEffect(player);
		
		double accuracyFactor = bulletItem.getAccuracy() + this.accuracy;
		
		Vec3d eyePos = player.getEyePosition(1.0F);
		Vec3d lookVec = player.getLookVec().add((player.getRNG().nextDouble() - 0.5D) / accuracyFactor, (player.getRNG().nextDouble() - 0.5D) / accuracyFactor, (player.getRNG().nextDouble() - 0.5D) / accuracyFactor);
		int travelDistance = distance + bulletItem.getDistance() + player.getRNG().nextInt(((int) distance / 10));
		penetratingPower = bulletItem.getPenetratingPower();
		
		struckEntities.clear();
		
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
		if(type == null)
		type = GunEffect.Type.SMOKE;
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
		
		float strikePowerPercentage = (float) ((penetratingPower + 0.00001) / (bulletItem.getPenetratingPower() + 0.00001));
		Debug.debugf("strikePowerPercentage = %s", strikePowerPercentage);
		int combinedDamages = damage + bulletItem.getDamage();
		
		if(closestTarget instanceof UnderlingEntity)
		closestTarget.attackEntityFrom(DamageSource.causePlayerDamage(player).setProjectile(), (combinedDamages + playerRung / 2F) * strikePowerPercentage); //damage increase from rung is higher against underlings
		else if(closestTarget instanceof PlayerEntity)
		closestTarget.attackEntityFrom(DamageSource.causePlayerDamage(player).setProjectile(), ((combinedDamages + playerRung / 5F) / 1.8F) * strikePowerPercentage);
		else
		closestTarget.attackEntityFrom(DamageSource.causePlayerDamage(player).setProjectile(), (combinedDamages + playerRung / 5F) * strikePowerPercentage);
		
		if(effect != null && player.getRNG().nextFloat() < .1F)
		closestTarget.addPotionEffect(effect.get());
		if(bulletItem.getEffect().get() != null && player.getRNG().nextFloat() < .75F)
		closestTarget.addPotionEffect(bulletItem.getEffect().get());
		if(bulletItem.getFlame() > 0 && player.getRNG().nextFloat() < .75F)
		closestTarget.setFire(bulletItem.getFlame());
		
		if(penetratingPower > 0)
		{
		if(!struckEntities.contains(closestTarget))
		{
		penetratingPower--;
		struckEntities.add(closestTarget);
		}
		return false;
		} else
		return true;
		} else return false;
		}

@Override
public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
		{
		double reloadTimeInSeconds = ((float) reloadTime) / 20F;
		reloadTimeInSeconds = Math.round(reloadTimeInSeconds * 100.0) / 100.0;
		tooltip.add(new TranslationTextComponent("item.minestuck." + this + ".values", accuracy, damage, distance, reloadTimeInSeconds));
		}

public int getMultipleProjectiles()
		{
		return multipleProjectiles;
		}

public boolean doNotProceedIfCrouch(PlayerEntity player)
		{
		if(doNotProceedIfCrouch)
		return player.isSneaking();
		else
		return false;
		}

public static class Builder
{
	private final double accuracy;
	private final int damage;
	private final int distance;
	private final int reloadTime;
	
	private final List<OnHitEffect> onHitEffects = new ArrayList<>();
	@Nullable
	private ItemRightClickEffect itemRightClickEffect;
	private int useDuration = 0;
	private UseAction useAction = UseAction.NONE;
	private final List<FinishUseItemEffect> itemUsageEffects = new ArrayList<>();
	private final List<InventoryTickEffect> tickEffects = new ArrayList<>();
	
	private int multipleProjectiles;
	private boolean doNotProceedIfCrouch;
	private Supplier<Item> specificAmmo;
	private Supplier<EffectInstance> effect;
	private Supplier<SoundEvent> sound;
	private float pitch;
	
	@Nullable
	GunEffect.Type type;
	
	public Builder(double accuracy, int damage, int distance, int reloadTime)
	{
		this.accuracy = accuracy;
		this.damage = damage;
		this.distance = distance;
		this.reloadTime = reloadTime;
	}
	
	public GunItem.Builder set(ItemRightClickEffect effect)
	{
		if(itemRightClickEffect != null)
			throw new IllegalStateException("Item right click effect has already been set");
		itemRightClickEffect = effect;
		return this;
	}
	
	public GunItem.Builder add(InventoryTickEffect... effects)
	{
		tickEffects.addAll(Arrays.asList(effects));
		return this;
	}
	
	public GunItem.Builder soundEffect(Supplier<SoundEvent> soundEffect)
	{
		sound = soundEffect;
		return this;
	}
	
	public GunItem.Builder multipleProjectiles(int projectileAmount)
	{
		multipleProjectiles = projectileAmount;
		return this;
	}
	
	public GunItem.Builder doNotProceedIfShiftClick()
	{
		doNotProceedIfCrouch = true;
		return this;
	}
	
	public GunItem.Builder potionEffect(Supplier<EffectInstance> potionEffect)
	{
		effect = potionEffect;
		return this;
	}
	
	public GunItem.Builder gunEffectType(GunEffect.Type gunEffectType)
	{
		type = gunEffectType;
		return this;
	}
	
	public GunItem.Builder needsSpecificAmmo(Supplier<Item> itemIn)
	{
		specificAmmo = itemIn;
		return this;
	}
}
}

 */

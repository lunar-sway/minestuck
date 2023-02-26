package com.mraof.minestuck.item.weapon.projectiles;

import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.item.ConsumableProjectileEntity;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ConsumableProjectileWeaponItem extends Item implements ProjectileDamaging
{
	public final float velocity;
	public final float accuracy;
	public final int damage;
	
	public ConsumableProjectileWeaponItem(Properties properties, float velocity, float accuracy, int damage)
	{
		super(properties);
		this.velocity = velocity;
		this.accuracy = accuracy;
		this.damage = damage;
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn)
	{
		ItemStack item = playerIn.getItemInHand(handIn);
		
		level.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), MSSoundEvents.ITEM_PROJECTILE_THROW.get(), SoundSource.PLAYERS, 0.8F, 1.5F);
		
		if(!level.isClientSide)
		{
			ConsumableProjectileEntity projectileEntity = new ConsumableProjectileEntity(MSEntityTypes.CONSUMABLE_PROJECTILE.get(), playerIn, level);
			projectileEntity.setItem(item);
			projectileEntity.shootFromRotation(playerIn, playerIn.getXRot(), playerIn.getYRot(), 0.0F, velocity, accuracy);
			level.addFreshEntity(projectileEntity);
		}
		if(!playerIn.isCreative())
		{
			item.shrink(1);
		}
		
		playerIn.getCooldowns().addCooldown(this, 7);
		playerIn.awardStat(Stats.ITEM_USED.get(this));
		return InteractionResultHolder.success(item);
	}
	
	@Override
	public int getProjectileDamage()
	{
		return damage;
	}
}
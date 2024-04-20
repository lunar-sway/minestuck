package com.mraof.minestuck.item.weapon.projectiles;

import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.item.ReturningProjectileEntity;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ReturningProjectileWeaponItem extends ConsumableProjectileWeaponItem
{
	protected final int maxTick;
	
	public ReturningProjectileWeaponItem(Properties properties, float velocity, float accuracy, int damage, int maxTick)
	{
		super(properties, velocity, accuracy, damage);
		this.maxTick = maxTick;
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn)
	{
		ItemStack item = playerIn.getItemInHand(handIn);
		
		level.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), MSSoundEvents.ITEM_PROJECTILE_THROW.get(), SoundSource.PLAYERS, 1.0F, 1.2F);
		
		if(playerIn instanceof ServerPlayer serverPlayer)
		{
			boolean noBlockCollision = item.is(MSItems.UMBRAL_INFILTRATOR)
					&& (playerIn.isCreative() || Title.isPlayerOfAspect(serverPlayer, EnumAspect.VOID));
			
			ReturningProjectileEntity projectileEntity = new ReturningProjectileEntity(MSEntityTypes.RETURNING_PROJECTILE.get(), playerIn, level, maxTick, noBlockCollision);
			projectileEntity.setItem(item);
			projectileEntity.shootFromRotation(playerIn, playerIn.getXRot(), playerIn.getYRot(), 0.0F, velocity, accuracy);
			projectileEntity.setNoGravity(true);
			if(noBlockCollision)
				projectileEntity.setGlowingTag(true);
			level.addFreshEntity(projectileEntity);
		}
		
		item.hurtAndBreak(1, playerIn, playerEntity -> playerEntity.broadcastBreakEvent(InteractionHand.MAIN_HAND));
		
		playerIn.getCooldowns().addCooldown(this, maxTick);
		playerIn.awardStat(Stats.ITEM_USED.get(this));
		return InteractionResultHolder.success(item);
	}
}
package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.world.storage.ClientPlayerData;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PropelEffect implements ItemRightClickEffect
{
	public static final PropelEffect BREATH_PROPEL = new PropelEffect(3, EnumAspect.BREATH);
	
	private final double velocity;
	private final EnumAspect aspect;
	
	public PropelEffect(double velocity, EnumAspect aspect)
	{
		this.velocity = velocity;
		this.aspect = aspect;
	}
	
	@Override
	public ActionResult<ItemStack> onRightClick(World world, PlayerEntity player, Hand hand)
	{
		ItemStack itemStack = player.getHeldItem(hand);
		propelAction(player, itemStack, getVelocityMod(), hand);
		return ActionResult.resultPass(itemStack);
	}
	
	private double getVelocityMod()
	{
		return velocity;
	}
	
	void propelAction(PlayerEntity player, ItemStack stack, double velocity, Hand hand)
	{
		Title title = null;
		if(player.world.isRemote)
		{
			title = ClientPlayerData.getTitle();
		} else if(player instanceof ServerPlayerEntity)
		{
			title = PlayerSavedData.getData((ServerPlayerEntity) player).getTitle();
			if(player.getCooldownTracker().getCooldown(stack.getItem(), 1F) <= 0 && ((title != null && title.getHeroAspect() == aspect) || player.isCreative()))
				propelActionSound(player.world, player);
		}
		
		if((title != null && title.getHeroAspect() == aspect) || player.isCreative())
		{
			Vec3d lookVec = player.getLookVec().scale(velocity);
			if(player.isElytraFlying())
			{
				lookVec = lookVec.scale(velocity / 12D);
			}
			player.addVelocity(lookVec.x, lookVec.y * 0.4D, lookVec.z);
			
			player.swing(hand, true);
			player.getCooldownTracker().setCooldown(stack.getItem(), 100);
			stack.damageItem(4, player, playerEntity -> playerEntity.sendBreakAnimation(Hand.MAIN_HAND));
		}
	}
	
	void propelActionSound(World world, PlayerEntity player)
	{
		world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ITEM_TRIDENT_RIPTIDE_2, SoundCategory.PLAYERS, 1.75F, 1.6F);
	}
}

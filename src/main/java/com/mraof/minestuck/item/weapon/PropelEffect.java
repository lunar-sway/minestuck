package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.effects.CreativeShockEffect;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.world.storage.ClientPlayerData;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
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
		ItemStack itemStack = player.getItemInHand(hand);
		if(!CreativeShockEffect.doesCreativeShockLimit(player, CreativeShockEffect.LIMIT_MOBILITY_ITEMS))
			propelAction(player, itemStack, getVelocityMod(), hand);
		return ActionResult.pass(itemStack);
	}
	
	private double getVelocityMod()
	{
		return velocity;
	}
	
	void propelAction(PlayerEntity player, ItemStack stack, double velocity, Hand hand)
	{
		Title title = null;
		if(player.level.isClientSide)
		{
			title = ClientPlayerData.getTitle();
		} else if(player instanceof ServerPlayerEntity)
		{
			title = PlayerSavedData.getData((ServerPlayerEntity) player).getTitle();
			if(player.getCooldowns().getCooldownPercent(stack.getItem(), 1F) <= 0 && ((title != null && title.getHeroAspect() == aspect) || player.isCreative()))
				propelActionSound(player.level, player);
		}
		
		if((title != null && title.getHeroAspect() == aspect) || player.isCreative())
		{
			Vector3d lookVec = player.getLookAngle().scale(velocity);
			if(player.isFallFlying())
			{
				lookVec = lookVec.scale(velocity / 12D);
			}
			player.push(lookVec.x, lookVec.y * 0.4D, lookVec.z);
			
			player.swing(hand, true);
			player.getCooldowns().addCooldown(stack.getItem(), 100);
			stack.hurtAndBreak(4, player, playerEntity -> playerEntity.broadcastBreakEvent(Hand.MAIN_HAND));
		}
	}
	
	void propelActionSound(World world, PlayerEntity player)
	{
		world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.TRIDENT_RIPTIDE_2, SoundCategory.PLAYERS, 1.75F, 1.6F);
	}
}

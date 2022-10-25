package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.effects.CreativeShockEffect;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.player.ClientPlayerData;
import com.mraof.minestuck.player.PlayerSavedData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

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
	public InteractionResultHolder<ItemStack> onRightClick(Level level, Player player, InteractionHand hand)
	{
		ItemStack itemStack = player.getItemInHand(hand);
		if(!CreativeShockEffect.doesCreativeShockLimit(player, CreativeShockEffect.LIMIT_MOBILITY_ITEMS))
			propelAction(player, itemStack, getVelocityMod(), hand);
		return InteractionResultHolder.pass(itemStack);
	}
	
	private double getVelocityMod()
	{
		return velocity;
	}
	
	void propelAction(Player player, ItemStack stack, double velocity, InteractionHand hand)
	{
		Title title = null;
		if(player.level.isClientSide)
		{
			title = ClientPlayerData.getTitle();
		} else if(player instanceof ServerPlayer)
		{
			title = PlayerSavedData.getData((ServerPlayer) player).getTitle();
			if(player.getCooldowns().getCooldownPercent(stack.getItem(), 1F) <= 0 && ((title != null && title.getHeroAspect() == aspect) || player.isCreative()))
				propelActionSound(player.level, player);
		}
		
		if((title != null && title.getHeroAspect() == aspect) || player.isCreative())
		{
			Vec3 lookVec = player.getLookAngle().scale(velocity);
			if(player.isFallFlying())
			{
				lookVec = lookVec.scale(velocity / 12D);
			}
			player.push(lookVec.x, lookVec.y * 0.4D, lookVec.z);
			
			player.swing(hand, true);
			player.getCooldowns().addCooldown(stack.getItem(), 100);
			stack.hurtAndBreak(4, player, playerEntity -> playerEntity.broadcastBreakEvent(InteractionHand.MAIN_HAND));
		}
	}
	
	void propelActionSound(Level level, Player player)
	{
		level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.TRIDENT_RIPTIDE_2, SoundSource.PLAYERS, 1.75F, 1.6F);
	}
}

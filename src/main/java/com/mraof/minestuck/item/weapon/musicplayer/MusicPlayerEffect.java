package com.mraof.minestuck.item.weapon.musicplayer;

import com.mraof.minestuck.item.weapon.ItemRightClickEffect;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MusicPlayerEffect implements ItemRightClickEffect
{
	@Override
	public InteractionResultHolder<ItemStack> onRightClick(Level level, Player player, InteractionHand hand)
	{
		ItemStack itemStackIn = player.getItemInHand(hand);
		return InteractionResultHolder.success(itemStackIn);
	}
	
	public MusicPlayerEffect()
	{
	}
}

package com.mraof.minestuck.item;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AncientThumbDrive extends Item
{
	public AncientThumbDrive(Properties pProperties)
	{
		super(pProperties);
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand)
	{
		pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.GRAVEL_STEP, SoundSource.PLAYERS, 0.01F, 0.3F);
		pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.ARMOR_EQUIP_IRON, SoundSource.PLAYERS, 0.02F, 0.6F);
		
		
		return super.use(pLevel, pPlayer, pUsedHand);
	}
	
	@Override
	public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced)
	{
		if(Screen.hasShiftDown())
		{
			pTooltipComponents.add(new TranslatableComponent("item.minestuck.ancient_thumb_drive.desc"));
		} else {
			pTooltipComponents.add(new TranslatableComponent("item.minestuck.ancient_thumb_drive.press_shift"));
		}
	}
}



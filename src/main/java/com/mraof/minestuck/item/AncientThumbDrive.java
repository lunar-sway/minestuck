package com.mraof.minestuck.item;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AncientThumbDrive extends Item
{
	public AncientThumbDrive(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand)
	{
		level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.GRAVEL_STEP, SoundSource.PLAYERS, 0.01F, 0.3F);
		level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.ARMOR_EQUIP_IRON, SoundSource.PLAYERS, 0.02F, 0.6F);
		
		return InteractionResultHolder.sidedSuccess(player.getItemInHand(usedHand), level.isClientSide());
	}
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced)
	{
		if(Screen.hasShiftDown())
			tooltipComponents.add(Component.translatable(getDescriptionId() + ".desc"));
		else
			tooltipComponents.add(Component.translatable(getDescriptionId() + ".press_shift"));
	}
}



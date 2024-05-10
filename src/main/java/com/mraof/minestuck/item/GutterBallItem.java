package com.mraof.minestuck.item;

import com.mraof.minestuck.player.PlayerData;
import com.mraof.minestuck.util.MSAttachments;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GutterBallItem extends Item
{
	public static final String MINOR_INCREASE = "message.gutter.minor_increase";
	public GutterBallItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand)
	{
		ItemStack itemStack = player.getItemInHand(usedHand);
		level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BELL_RESONATE, SoundSource.PLAYERS, 0.5F, 0.3F);
		itemStack.shrink(1);
		player.displayClientMessage(Component.translatable(MINOR_INCREASE).withStyle(ChatFormatting.BOLD), true);
		
		if(player instanceof ServerPlayer serverPlayer)
		{
			PlayerData.get(serverPlayer).ifPresent(playerData -> {
				double newMultiplier = playerData.getData(MSAttachments.GUTTER_MULTIPLIER) + 0.2;
				playerData.setData(MSAttachments.GUTTER_MULTIPLIER, newMultiplier);
			});
		}
		
		return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
	}
	
	@Override
	public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced)
	{
		if(Screen.hasShiftDown())
			pTooltipComponents.add(Component.translatable(getDescriptionId() + ".desc"));
		else
			pTooltipComponents.add(Component.translatable(getDescriptionId() + ".press_shift"));
	}
}
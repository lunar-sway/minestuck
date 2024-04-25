package com.mraof.minestuck.item;

import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.player.PlayerData;
import com.mraof.minestuck.util.MSAttachments;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GutterThumbDriveItem extends Item
{
	public static final String GREATER_INCREASE = "message.gutter.increase";
	public GutterThumbDriveItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public InteractionResult useOn(UseOnContext context)
	{
		Level level = context.getLevel();
		BlockPos blockPos = context.getClickedPos();
		Player player = context.getPlayer();
		
		if(player != null && level.getBlockEntity(blockPos) instanceof ComputerBlockEntity)
		{
			ItemStack itemStack = player.getItemInHand(player.getUsedItemHand());
			level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BELL_RESONATE, SoundSource.PLAYERS, 0.5F, 0.3F);
			level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.ARMOR_EQUIP_IRON, SoundSource.PLAYERS, 0.2F, 0.6F);
			itemStack.shrink(1);
			player.displayClientMessage(Component.translatable(GREATER_INCREASE).withStyle(ChatFormatting.BOLD), true);
			
			if(player instanceof ServerPlayer serverPlayer)
			{
				PlayerData.get(serverPlayer).ifPresent(playerData -> {
					double newMultiplier = playerData.getData(MSAttachments.GUTTER_MULTIPLIER) + 2.0;
					playerData.setData(MSAttachments.GUTTER_MULTIPLIER, newMultiplier);
				});
			}
			
			return InteractionResult.sidedSuccess(level.isClientSide());
		}
		
		return super.useOn(context);
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


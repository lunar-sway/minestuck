package com.mraof.minestuck.item;

import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.skaianet.Session;
import com.mraof.minestuck.skaianet.SessionHandler;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
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
		
		if(!level.isClientSide)
		{
			Session playerSession = SessionHandler.get(player.getServer()).getPlayerSession(IdentifierHandler.encode(player));
			playerSession.getGristGutter().increaseGutterMultiplier(0.2);
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
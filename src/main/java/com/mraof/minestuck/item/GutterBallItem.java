package com.mraof.minestuck.item;

import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.skaianet.Session;
import com.mraof.minestuck.skaianet.SessionHandler;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
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

import java.util.List;

public class GutterBallItem extends Item
{
	public GutterBallItem(Properties pProperties)
	{
		super(pProperties);
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand)
	{
		if(!pPlayer.getLevel().isClientSide)
		{
			ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
			Session playerSession = SessionHandler.get(pPlayer.getServer()).getPlayerSession(IdentifierHandler.encode(pPlayer));
			pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.BELL_RESONATE, SoundSource.PLAYERS, 0.5F, 0.3F);
			
			playerSession.increaseGutterMultiplier(0.2);
			itemStack.shrink(1);
		}
		
		return super.use(pLevel, pPlayer, pUsedHand);
	}
	
	@Override
	public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced)
	{
		if(Screen.hasShiftDown())
		{
			pTooltipComponents.add(Component.translatable("item.minestuck.gutter_ball.desc"));
		} else {
			pTooltipComponents.add(Component.translatable("item.minestuck.gutter_ball.press_shift"));
		}
	}
}
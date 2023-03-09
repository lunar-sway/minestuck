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
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;


public class GutterThumbDriveItem extends Item
{
	public GutterThumbDriveItem(Properties pProperties)
	{
		super(pProperties);
	}
	@Override
	public InteractionResult useOn(UseOnContext pContext)
	{
		if(!pContext.getLevel().isClientSide)
		{
			Player player = pContext.getPlayer();
			Level level = pContext.getLevel();
			InteractionHand pUsedHand = player.getUsedItemHand();
			int mod = 1;
			
			for(BlockPos blockPos : BlockPos.betweenClosed(player.blockPosition().offset(2 * mod, mod, 2 * mod), player.blockPosition().offset(-2 * mod, -1 * mod, -2 * mod)))
			{
				if(level.getBlockEntity(blockPos) instanceof ComputerBlockEntity)
				{
					ItemStack itemStack = player.getItemInHand(pUsedHand);
					Session playerSession = SessionHandler.get(level).getPlayerSession(IdentifierHandler.encode(player));
					level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BELL_RESONATE, SoundSource.PLAYERS, 0.5F, 0.3F);
					level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARMOR_EQUIP_IRON, SoundSource.PLAYERS, 0.2F, 0.6F);
					
					playerSession.increaseGutterMultiplier(2.0);
					itemStack.shrink(1);
				}
			}
		}
		
		return super.useOn(pContext);
	}
	
	@Override
	public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced)
	{
		if(Screen.hasShiftDown())
		{
			pTooltipComponents.add(new TranslatableComponent("item.minestuck.gutter_thumb_drive.desc"));
		} else {
			pTooltipComponents.add(new TranslatableComponent("item.minestuck.gutter_thumb_drive.press_shift"));
		}
	}
}


package com.mraof.minestuck.item;

import com.mraof.minestuck.item.components.MSItemComponents;
import com.mraof.minestuck.player.PlayerBoondollars;
import com.mraof.minestuck.player.PlayerData;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class BoondollarsItem extends Item
{
	public BoondollarsItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn)
	{
		playerIn.level().playSound(playerIn,playerIn.blockPosition(), MSSoundEvents.ITEM_BOONDOLLARS_USE.get(), SoundSource.PLAYERS,1,(playerIn.getRandom().nextFloat()/2)+0.75f);
		
		if(playerIn instanceof ServerPlayer serverPlayer)
		{
			PlayerData.get(serverPlayer).ifPresent(
					playerData -> PlayerBoondollars.addBoondollars(playerData, getCount(playerIn.getItemInHand(handIn)))
			);
		}
		return InteractionResultHolder.success(ItemStack.EMPTY);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flagIn)
	{
		long amount = getCount(stack);
		tooltip.add(Component.translatable("item.minestuck.boondollars.amount", amount));
	}
	
	public static long getCount(ItemStack stack)
	{
		return stack.getOrDefault(MSItemComponents.VALUE, 1L);
	}
	
	public static ItemStack setCount(ItemStack stack, long value)
	{
		stack.set(MSItemComponents.VALUE, value);
		return stack;
	}
}

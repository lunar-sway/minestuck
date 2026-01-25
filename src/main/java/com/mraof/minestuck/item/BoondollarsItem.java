package com.mraof.minestuck.item;

import com.mraof.minestuck.item.components.MSItemComponents;
import com.mraof.minestuck.player.PlayerBoondollars;
import com.mraof.minestuck.player.PlayerData;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
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
	
	@Override
	public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player)
	{
		if(stack.getCount() == 1)
		{
			ItemStack other = slot.getItem();
			if(action == ClickAction.SECONDARY)
			{
				// Transfer 1 boondollar to stack below (or create a stack of boondollars if there's nothing)
				if(other.isEmpty() || other.is(MSItems.BOONDOLLARS))
				{
					if(getCount(stack) > 1)
					{
						setCount(stack, getCount(stack) - 1);
					} else
					{
						stack.setCount(0);
					}
				}
				if(other.isEmpty())
				{
					slot.set(setCount(MSItems.BOONDOLLARS.toStack(), 1));
					return true;
				} else if(other.is(MSItems.BOONDOLLARS))
				{
					setCount(other, getCount(other) + 1);
					return true;
				}
			} else if(other.is(MSItems.BOONDOLLARS))
			{
				// Add all boondollars together
				setCount(other, getCount(other) + getCount(stack));
				stack.setCount(0);
				return true;
			}
		}
		
		return super.overrideStackedOnOther(stack, slot, action, player);
	}
	
	@Override
	public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access)
	{
		if(stack.getCount() == 1 && slot.allowModification(player) && other.isEmpty() && action == ClickAction.SECONDARY && getCount(stack) > 1)
		{
			// Split boondollars like vanilla stacks, giving the larger half to the held amount
			long count = getCount(stack);
			long half = count / 2;
			access.set(setCount(MSItems.BOONDOLLARS.toStack(), count - half));
			setCount(stack, half);
			return true;
		}
		return super.overrideOtherStackedOnMe(stack, other, slot, action, player, access);
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

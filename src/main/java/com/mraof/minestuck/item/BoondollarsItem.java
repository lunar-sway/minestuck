package com.mraof.minestuck.item;

import com.mraof.minestuck.player.PlayerBoondollars;
import com.mraof.minestuck.player.PlayerData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
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
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn)
	{
		long amount = getCount(stack);
		tooltip.add(Component.translatable("item.minestuck.boondollars.amount", amount));
	}
	
	public static long getCount(ItemStack stack)
	{
		if(!stack.hasTag() || !stack.getTag().contains("value", Tag.TAG_ANY_NUMERIC))
			return 1;
		else return stack.getTag().getInt("value");
	}
	
	public static ItemStack setCount(ItemStack stack, int value)
	{
		CompoundTag nbt = stack.getTag();
		if(nbt == null)
		{
			nbt = new CompoundTag();
			stack.setTag(nbt);
		}
		nbt.putInt("value", value);
		return stack;
	}
}
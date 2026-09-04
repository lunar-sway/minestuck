package com.mraof.minestuck.item;

import com.mraof.minestuck.item.components.MSItemComponents;
import com.mraof.minestuck.network.StrifePackets;
import com.mraof.minestuck.player.StrifeSpecibus;
import com.mraof.minestuck.strife.StrifePortfolioHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class StrifeCardItem extends Item
{
	public StrifeCardItem(Properties properties)
	{
		super(properties);
	}
	
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
	{
		ItemStack stack = player.getItemInHand(hand);
		
		if(level.isClientSide())
		{
			return InteractionResultHolder.success(stack);
		}
		
		if(!(player instanceof ServerPlayer serverPlayer)) return InteractionResultHolder.pass(stack);
		
		if(StrifePortfolioHandler.isFull(player))
		{
			player.displayClientMessage(Component.translatable("status.strife.portfolioFull"), true);
			return InteractionResultHolder.fail(stack);
		}
		
		StrifeSpecibus specibus = stack.get(MSItemComponents.STRIFE_SPECIBUS_DATA.get());
		
		if(specibus != null && specibus.isAssigned())
		{
			StrifePortfolioHandler.assignStrife(serverPlayer, hand);
		} else
		{
			net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(serverPlayer, new StrifePackets.OpenStrifeCardGuiPacket(hand));
		}
		
		return InteractionResultHolder.success(stack);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag)
	{
		super.appendHoverText(stack, context, tooltip, flag);
		
		StrifeSpecibus specibus = stack.get(MSItemComponents.STRIFE_SPECIBUS_DATA.get());
		if(specibus == null) return;
		
		if(!specibus.isAssigned())
		{
			tooltip.add(Component.translatable("item.minestuck.strife_card.blank").withStyle(ChatFormatting.GRAY));
			return;
		}
		
		// Abstrata type name
		tooltip.add(Component.literal("(" + specibus.getDisplayNameForCard() + ")").withStyle(ChatFormatting.GREEN));
		
		// Weapon contents (show up to 5)
		List<ItemStack> contents = specibus.getContents();
		int toShow = Math.min(contents.size(), 5);
		int remaining = contents.size() - toShow;
		
		for(int i = 0; i < toShow; i++)
		{
			ItemStack weapon = contents.get(i);
			tooltip.add(Component.literal(weapon.getHoverName().getString() + " x" + weapon.getCount()).withStyle(ChatFormatting.WHITE));
		}
		
		if(remaining > 0)
			tooltip.add(Component.translatable("container.shulkerBox.more", remaining).withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
	}
	
	public static StrifeSpecibus getStrifeSpecibus(ItemStack stack)
	{
		return stack.get(MSItemComponents.STRIFE_SPECIBUS_DATA.get());
	}
	
	/**
	 * Returns true if the card has a non-null specibus stored in it.
	 */
	public static boolean hasSpecibus(ItemStack stack)
	{
		StrifeSpecibus sp = getStrifeSpecibus(stack);
		return sp != null && sp.isAssigned();
	}
	
	public static ItemStack injectStrifeSpecibus(StrifeSpecibus specibus, ItemStack card)
	{
		if(specibus != null) card.set(MSItemComponents.STRIFE_SPECIBUS_DATA.get(), specibus);
		return card;
	}
}
package com.mraof.minestuck.network;

import com.mraof.minestuck.item.MSItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class StoneTabletPacket implements PlayToServerPacket
{
	private final String text;
	private final InteractionHand hand;
	
	public StoneTabletPacket(String text, InteractionHand hand)
	{
		this.text = text;
		this.hand = hand;
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		ItemStack tablet = player.getItemInHand(hand);
		ItemStack tool = player.getItemInHand(hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
		
		if(tablet.getItem() == MSItems.STONE_SLAB9j &&
				tool.getItem() == MSItems.CARVING_TOOL9j)
		{
			CompoundTag nbt = tablet.getOrCreateTag();
			nbt.putString("text", text);
			tablet.setTag(nbt);
		}
		
	}
	
	public static StoneTabletPacket decode(FriendlyByteBuf buffer)
	{
		return new StoneTabletPacket(buffer.readUtf(32767), InteractionHand.values()[buffer.readInt()]);
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeUtf(text);
		buffer.writeInt(hand.ordinal());
	}
}

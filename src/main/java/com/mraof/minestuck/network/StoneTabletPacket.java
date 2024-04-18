package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class StoneTabletPacket implements MSPacket.PlayToServer
{
	public static final ResourceLocation ID = Minestuck.id("stone_tablet");
	
	private final String text;
	private final InteractionHand hand;
	
	public StoneTabletPacket(String text, InteractionHand hand)
	{
		this.text = text;
		this.hand = hand;
	}
	
	@Override
	public ResourceLocation id()
	{
		return ID;
	}
	
	@Override
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeUtf(text);
		buffer.writeInt(hand.ordinal());
	}
	
	public static StoneTabletPacket read(FriendlyByteBuf buffer)
	{
		return new StoneTabletPacket(buffer.readUtf(), InteractionHand.values()[buffer.readInt()]);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		ItemStack tablet = player.getItemInHand(hand);
		ItemStack tool = player.getItemInHand(hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
		
		if(tablet.getItem() == MSItems.STONE_TABLET.get() &&
				tool.getItem() == MSItems.CARVING_TOOL.get())
		{
			CompoundTag nbt = tablet.getOrCreateTag();
			nbt.putString("text", text);
			tablet.setTag(nbt);
		}
	}
}

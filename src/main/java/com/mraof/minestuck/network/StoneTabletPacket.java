package com.mraof.minestuck.network;

import com.mraof.minestuck.item.MSItems;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;

public class StoneTabletPacket implements PlayToServerPacket
{
	private final String text;
	private final Hand hand;
	
	public StoneTabletPacket(String text, Hand hand)
	{
		this.text = text;
		this.hand = hand;
	}
	
	@Override
	public void execute(ServerPlayerEntity player)
	{
		ItemStack tablet = player.getHeldItem(hand);
		ItemStack tool = player.getHeldItem(hand == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND);
		
		if(tablet.getItem() == MSItems.STONE_SLAB &&
				tool.getItem() == MSItems.CARVING_TOOL)
		{
			CompoundNBT nbt = tablet.getOrCreateTag();
			nbt.putString("text", text);
			tablet.setTag(nbt);
		}
		
	}
	
	public static StoneTabletPacket decode(PacketBuffer buffer)
	{
		return new StoneTabletPacket(buffer.readString(32767), Hand.values()[buffer.readInt()]);
	}
	
	@Override
	public void encode(PacketBuffer buffer)
	{
		buffer.writeString(text);
		buffer.writeInt(hand.ordinal());
	}
}

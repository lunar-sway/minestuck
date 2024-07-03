package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public record CarveStoneTabletPacket(String text, InteractionHand hand) implements MSPacket.PlayToServer
{
	public static final ResourceLocation ID = Minestuck.id("carve_stone_tablet");
	
	@Override
	public ResourceLocation id()
	{
		return ID;
	}
	
	@Override
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeUtf(text);
		buffer.writeEnum(hand);
	}
	
	public static CarveStoneTabletPacket read(FriendlyByteBuf buffer)
	{
		return new CarveStoneTabletPacket(buffer.readUtf(), buffer.readEnum(InteractionHand.class));
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		ItemStack tablet = player.getItemInHand(hand);
		ItemStack tool = player.getItemInHand(hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
		
		if(tablet.is(MSItems.STONE_TABLET) &&
				tool.is(MSItems.CARVING_TOOL))
		{
			CompoundTag nbt = tablet.getOrCreateTag();
			nbt.putString("text", text);
			tablet.setTag(nbt);
		}
	}
}

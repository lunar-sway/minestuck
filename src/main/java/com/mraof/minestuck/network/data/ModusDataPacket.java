package com.mraof.minestuck.network.data;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.network.MSPacket;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public record ModusDataPacket(@Nullable CompoundTag nbt) implements MSPacket.PlayToClient
{
	public static final ResourceLocation ID = Minestuck.id("modus_data");
	
	public static ModusDataPacket create(@Nullable Modus modus)
	{
		return new ModusDataPacket(CaptchaDeckHandler.writeToNBT(modus));
	}
	
	@Override
	public ResourceLocation id()
	{
		return ID;
	}
	
	@Override
	public void write(FriendlyByteBuf buffer)
	{
		if(nbt != null)
		{
			buffer.writeNbt(nbt);
		}
	}
	
	public static ModusDataPacket read(FriendlyByteBuf buffer)
	{
		if(buffer.readableBytes() > 0)
		{
			CompoundTag nbt = buffer.readNbt();
			return new ModusDataPacket(nbt);
		} else return new ModusDataPacket(null);
	}
	
	@Override
	public void execute()
	{
		ClientPlayerData.handleDataPacket(this);
	}
}
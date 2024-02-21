package com.mraof.minestuck.network;

import com.mraof.minestuck.client.EntryEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public final class EntryEffectPackets
{
	public record Effect(ResourceKey<Level> level, BlockPos center, int range) implements MSPacket.PlayToClient
	{
		@Override
		public void encode(FriendlyByteBuf buffer)
		{
			buffer.writeResourceLocation(level.location());
			buffer.writeBlockPos(center);
			buffer.writeInt(range);
		}
		
		public static Effect decode(FriendlyByteBuf buffer)
		{
			ResourceKey<Level> level = ResourceKey.create(Registries.DIMENSION, buffer.readResourceLocation());
			BlockPos pos = buffer.readBlockPos();
			int range = buffer.readInt();
			return new Effect(level, pos, range);
		}
		
		@Override
		public void execute()
		{
			EntryEffect.handlePacket(this);
		}
	}
	
	public record Clear() implements MSPacket.PlayToClient
	{
		@Override
		public void encode(FriendlyByteBuf buffer)
		{
		}
		
		public static Clear decode(FriendlyByteBuf ignored)
		{
			return new Clear();
		}
		
		@Override
		public void execute()
		{
			EntryEffect.reset();
		}
	}
}

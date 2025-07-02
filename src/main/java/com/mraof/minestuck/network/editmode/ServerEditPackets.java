package com.mraof.minestuck.network.editmode;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.computer.editmode.ClientDeployList;
import com.mraof.minestuck.computer.editmode.ClientEditmodeData;
import com.mraof.minestuck.network.MSPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class ServerEditPackets
{
	public record Activate() implements MSPacket.PlayToClient
	{
		public static final Type<Activate> ID = new Type<>(Minestuck.id("server_edit/activate"));
		public static final StreamCodec<FriendlyByteBuf, Activate> STREAM_CODEC = StreamCodec.unit(new Activate());
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context)
		{
			ClientEditmodeData.onActivatePacket();
		}
	}
	
	public record UpdateDeployList(CompoundTag data) implements MSPacket.PlayToClient
	{
		public static final Type<UpdateDeployList> ID = new Type<>(Minestuck.id("server_edit/update_deploy_list"));
		public static final StreamCodec<ByteBuf, UpdateDeployList> STREAM_CODEC = ByteBufCodecs.COMPOUND_TAG.map(UpdateDeployList::new, UpdateDeployList::data);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context)
		{
			ClientDeployList.load(this, context.player().registryAccess());
		}
	}
	
	public record Exit() implements MSPacket.PlayToClient
	{
		public static final Type<Exit> ID = new Type<>(Minestuck.id("server_edit/exit"));
		public static final StreamCodec<FriendlyByteBuf, Exit> STREAM_CODEC = StreamCodec.unit(new Exit());
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		
		@Override
		public void execute(IPayloadContext context)
		{
			ClientEditmodeData.onExitPacket(this);
		}
	}
}

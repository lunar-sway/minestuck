package com.mraof.minestuck.network.editmode;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.computer.editmode.*;
import com.mraof.minestuck.network.MSPacket;
import com.mraof.minestuck.skaianet.SburbPlayerData;
import com.mraof.minestuck.util.MSAttachments;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.mraof.minestuck.computer.editmode.ServerEditHandler.getData;
import static com.mraof.minestuck.computer.editmode.ServerEditHandler.updateInventory;

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
	
	public record SelectionUpdate(boolean cleared, BlockPos newMin, BlockPos newMax) implements MSPacket.PlayToClient
	{
		public static final Type<SelectionUpdate> ID = new Type<>(Minestuck.id("server_edit/selection_update"));
		public static final StreamCodec<FriendlyByteBuf, SelectionUpdate> STREAM_CODEC = StreamCodec.composite(
				ByteBufCodecs.BOOL, SelectionUpdate::cleared,
				BlockPos.STREAM_CODEC, SelectionUpdate::newMin,
				BlockPos.STREAM_CODEC, SelectionUpdate::newMax,
				SelectionUpdate::new
		);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context)
		{
			Player player = context.player();
			if(player == null)
				return;
			
			EditTools cap = player.getData(MSAttachments.EDIT_TOOLS);
			if(cleared)
			{
				cap.clearSelection();
				ClientSelectionCache.clear();
			}
			else
			{
				cap.setSelectionPos1(newMin);
				cap.setSelectionPos2(newMax);
				cap.setPreviewRotation(0);
				ClientSelectionCache.scheduleRecapture(newMin, newMax);
			}
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

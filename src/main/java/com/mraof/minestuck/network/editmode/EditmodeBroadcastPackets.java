package com.mraof.minestuck.network.editmode;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.computer.editmode.ClientMoveTransitions;
import com.mraof.minestuck.computer.editmode.RemoteEditSessions;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.network.MSPacket;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class EditmodeBroadcastPackets
{
	private static final StreamCodec<FriendlyByteBuf, UUID> UUID_STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_LONG, UUID::getMostSignificantBits, ByteBufCodecs.VAR_LONG, UUID::getLeastSignificantBits, UUID::new);
	
	private static final StreamCodec<FriendlyByteBuf, BlockState> BLOCK_STATE_STREAM_CODEC = ByteBufCodecs.VAR_INT.map(Block.BLOCK_STATE_REGISTRY::byIdOrThrow, Block.BLOCK_STATE_REGISTRY::getId).cast();
	
	private static final StreamCodec<FriendlyByteBuf, RemoteEditSessions.PreviewEntry> PREVIEW_ENTRY_STREAM_CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, RemoteEditSessions.PreviewEntry::localOffset, BLOCK_STATE_STREAM_CODEC, RemoteEditSessions.PreviewEntry::state, RemoteEditSessions.PreviewEntry::new);
	
	private static final StreamCodec<FriendlyByteBuf, List<RemoteEditSessions.PreviewEntry>> PREVIEW_LIST_STREAM_CODEC = ByteBufCodecs.collection(ArrayList::new, PREVIEW_ENTRY_STREAM_CODEC);
	
	private static boolean broadcastEnabled(ServerPlayer player)
	{
		return MinestuckConfig.SERVER.visualsToOthers.get() && ServerEditHandler.isInEditmode(player);
	}
	
	public record BroadcastDragBox(boolean active, int toolKind, BlockPos pos1,
	                               BlockPos pos2) implements MSPacket.PlayToServer
	{
		public static final Type<BroadcastDragBox> ID = new Type<>(Minestuck.id("editmode_broadcast/drag_box"));
		public static final StreamCodec<FriendlyByteBuf, BroadcastDragBox> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.BOOL, BroadcastDragBox::active, ByteBufCodecs.VAR_INT, BroadcastDragBox::toolKind, BlockPos.STREAM_CODEC, BroadcastDragBox::pos1, BlockPos.STREAM_CODEC, BroadcastDragBox::pos2, BroadcastDragBox::new);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context, ServerPlayer player)
		{
			if(!broadcastEnabled(player)) return;
			PacketDistributor.sendToPlayersTrackingEntity(player, new ClientDragBox(player.getUUID(), active, toolKind, pos1, pos2));
		}
	}
	
	public record ClientDragBox(UUID editorId, boolean active, int toolKind, BlockPos pos1,
	                            BlockPos pos2) implements MSPacket.PlayToClient
	{
		public static final Type<ClientDragBox> ID = new Type<>(Minestuck.id("editmode_broadcast/client_drag_box"));
		public static final StreamCodec<FriendlyByteBuf, ClientDragBox> STREAM_CODEC = StreamCodec.composite(UUID_STREAM_CODEC, ClientDragBox::editorId, ByteBufCodecs.BOOL, ClientDragBox::active, ByteBufCodecs.VAR_INT, ClientDragBox::toolKind, BlockPos.STREAM_CODEC, ClientDragBox::pos1, BlockPos.STREAM_CODEC, ClientDragBox::pos2, ClientDragBox::new);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context)
		{
			RemoteEditSessions.updateDragBox(editorId, active, toolKind, active ? pos1 : null, active ? pos2 : null);
		}
	}
	
	public record BroadcastSelectionBox(boolean active, BlockPos pos1, BlockPos pos2) implements MSPacket.PlayToServer
	{
		public static final Type<BroadcastSelectionBox> ID = new Type<>(Minestuck.id("editmode_broadcast/selection_box"));
		public static final StreamCodec<FriendlyByteBuf, BroadcastSelectionBox> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.BOOL, BroadcastSelectionBox::active, BlockPos.STREAM_CODEC, BroadcastSelectionBox::pos1, BlockPos.STREAM_CODEC, BroadcastSelectionBox::pos2, BroadcastSelectionBox::new);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context, ServerPlayer player)
		{
			if(!broadcastEnabled(player)) return;
			PacketDistributor.sendToPlayersTrackingEntity(player, new ClientSelectionBox(player.getUUID(), active, pos1, pos2));
		}
	}
	
	public record ClientSelectionBox(UUID editorId, boolean active, BlockPos pos1,
	                                 BlockPos pos2) implements MSPacket.PlayToClient
	{
		public static final Type<ClientSelectionBox> ID = new Type<>(Minestuck.id("editmode_broadcast/client_selection_box"));
		public static final StreamCodec<FriendlyByteBuf, ClientSelectionBox> STREAM_CODEC = StreamCodec.composite(UUID_STREAM_CODEC, ClientSelectionBox::editorId, ByteBufCodecs.BOOL, ClientSelectionBox::active, BlockPos.STREAM_CODEC, ClientSelectionBox::pos1, BlockPos.STREAM_CODEC, ClientSelectionBox::pos2, ClientSelectionBox::new);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context)
		{
			RemoteEditSessions.updateSelectionBox(editorId, active, active ? pos1 : null, active ? pos2 : null);
		}
	}
	
	public record BroadcastPreviewStart(BlockPos corner1, BlockPos corner2) implements MSPacket.PlayToServer
	{
		public static final Type<BroadcastPreviewStart> ID = new Type<>(Minestuck.id("editmode_broadcast/preview_start"));
		public static final StreamCodec<FriendlyByteBuf, BroadcastPreviewStart> STREAM_CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, BroadcastPreviewStart::corner1, BlockPos.STREAM_CODEC, BroadcastPreviewStart::corner2, BroadcastPreviewStart::new);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context, ServerPlayer player)
		{
			if(!broadcastEnabled(player)) return;
			
			Level level = player.level();
			BlockPos min = new BlockPos(Math.min(corner1.getX(), corner2.getX()), Math.min(corner1.getY(), corner2.getY()), Math.min(corner1.getZ(), corner2.getZ()));
			BlockPos max = new BlockPos(Math.max(corner1.getX(), corner2.getX()), Math.max(corner1.getY(), corner2.getY()), Math.max(corner1.getZ(), corner2.getZ()));
			
			int sizeX = max.getX() - min.getX() + 1;
			int sizeY = max.getY() - min.getY() + 1;
			int sizeZ = max.getZ() - min.getZ() + 1;
			long volume = (long) sizeX * sizeY * sizeZ;
			
			if(volume > MinestuckConfig.SERVER.maxSelectionVolume.get()) return;
			
			List<RemoteEditSessions.PreviewEntry> entries = new ArrayList<>();
			for(BlockPos pos : BlockPos.betweenClosed(min, max))
			{
				BlockState state = level.getBlockState(pos);
				if(!state.isAir()) entries.add(new RemoteEditSessions.PreviewEntry(pos.subtract(min), state));
			}
			
			if(entries.size() > 512) return;
			
			PacketDistributor.sendToPlayersTrackingEntity(player, new ClientPreviewBlocks(player.getUUID(), sizeX, sizeY, sizeZ, entries));
		}
	}
	
	public record BroadcastMoveTransition(BlockPos oldPos1, BlockPos oldPos2, BlockPos anchor, int rotation) implements MSPacket.PlayToServer
	{
		public static final Type<BroadcastMoveTransition> ID = new Type<>(Minestuck.id("editmode_broadcast/move_transition"));
		public static final StreamCodec<FriendlyByteBuf, BroadcastMoveTransition> STREAM_CODEC = StreamCodec.composite(
				BlockPos.STREAM_CODEC, BroadcastMoveTransition::oldPos1,
				BlockPos.STREAM_CODEC, BroadcastMoveTransition::oldPos2,
				BlockPos.STREAM_CODEC, BroadcastMoveTransition::anchor,
				ByteBufCodecs.VAR_INT, BroadcastMoveTransition::rotation,
				BroadcastMoveTransition::new
		);
		
		@Override
		public Type<? extends CustomPacketPayload> type() { return ID; }
		
		@Override
		public void execute(IPayloadContext context, ServerPlayer player)
		{
			if(!broadcastEnabled(player))
				return;
			PacketDistributor.sendToPlayersTrackingEntity(player,
					new ClientMoveTransition(player.getUUID(), oldPos1, oldPos2, anchor, rotation));
		}
	}
	
	public record ClientMoveTransition(UUID editorId, BlockPos oldPos1, BlockPos oldPos2, BlockPos anchor, int rotation) implements MSPacket.PlayToClient
	{
		public static final Type<ClientMoveTransition> ID = new Type<>(Minestuck.id("editmode_broadcast/client_move_transition"));
		public static final StreamCodec<FriendlyByteBuf, ClientMoveTransition> STREAM_CODEC = StreamCodec.composite(
				UUID_STREAM_CODEC, ClientMoveTransition::editorId,
				BlockPos.STREAM_CODEC, ClientMoveTransition::oldPos1,
				BlockPos.STREAM_CODEC, ClientMoveTransition::oldPos2,
				BlockPos.STREAM_CODEC, ClientMoveTransition::anchor,
				ByteBufCodecs.VAR_INT, ClientMoveTransition::rotation,
				ClientMoveTransition::new
		);
		
		@Override
		public Type<? extends CustomPacketPayload> type() { return ID; }
		
		@Override
		public void execute(IPayloadContext context)
		{
			RemoteEditSessions.Session session = RemoteEditSessions.allSessions().get(editorId);
			if(session == null || session.previewBlocks.isEmpty())
				return;
			
			BlockPos min = new BlockPos(Math.min(oldPos1.getX(), oldPos2.getX()), Math.min(oldPos1.getY(), oldPos2.getY()), Math.min(oldPos1.getZ(), oldPos2.getZ()));
			Rotation rot = Rotation.values()[Math.floorMod(rotation, 4)];
			
			List<BlockPos> from = new ArrayList<>();
			List<BlockPos> to = new ArrayList<>();
			for(RemoteEditSessions.PreviewEntry entry : session.previewBlocks)
			{
				from.add(min.offset(entry.localOffset()));
				to.add(anchor.offset(EditmodeDragPackets.rotateOffset(entry.localOffset(), session.previewSizeX, session.previewSizeZ, rot)));
			}
			ClientMoveTransitions.start(from, to);
		}
	}
	
	public record ClientPreviewBlocks(UUID editorId, int sizeX, int sizeY, int sizeZ,
	                                  List<RemoteEditSessions.PreviewEntry> blocks) implements MSPacket.PlayToClient
	{
		public static final Type<ClientPreviewBlocks> ID = new Type<>(Minestuck.id("editmode_broadcast/client_preview_blocks"));
		public static final StreamCodec<FriendlyByteBuf, ClientPreviewBlocks> STREAM_CODEC = StreamCodec.composite(UUID_STREAM_CODEC, ClientPreviewBlocks::editorId, ByteBufCodecs.VAR_INT, ClientPreviewBlocks::sizeX, ByteBufCodecs.VAR_INT, ClientPreviewBlocks::sizeY, ByteBufCodecs.VAR_INT, ClientPreviewBlocks::sizeZ, PREVIEW_LIST_STREAM_CODEC, ClientPreviewBlocks::blocks, ClientPreviewBlocks::new);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context)
		{
			RemoteEditSessions.setPreviewBlocks(editorId, sizeX, sizeY, sizeZ, blocks);
		}
	}
	
	public record BroadcastPreviewTransform(boolean active, BlockPos anchor, int rotation,
	                                        boolean isCopy) implements MSPacket.PlayToServer
	{
		public static final Type<BroadcastPreviewTransform> ID = new Type<>(Minestuck.id("editmode_broadcast/preview_transform"));
		public static final StreamCodec<FriendlyByteBuf, BroadcastPreviewTransform> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.BOOL, BroadcastPreviewTransform::active, BlockPos.STREAM_CODEC, BroadcastPreviewTransform::anchor, ByteBufCodecs.VAR_INT, BroadcastPreviewTransform::rotation, ByteBufCodecs.BOOL, BroadcastPreviewTransform::isCopy, BroadcastPreviewTransform::new);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context, ServerPlayer player)
		{
			if(!broadcastEnabled(player)) return;
			PacketDistributor.sendToPlayersTrackingEntity(player, new ClientPreviewTransform(player.getUUID(), active, anchor, rotation, isCopy));
		}
	}
	
	public record ClientPreviewTransform(UUID editorId, boolean active, BlockPos anchor, int rotation,
	                                     boolean isCopy) implements MSPacket.PlayToClient
	{
		public static final Type<ClientPreviewTransform> ID = new Type<>(Minestuck.id("editmode_broadcast/client_preview_transform"));
		public static final StreamCodec<FriendlyByteBuf, ClientPreviewTransform> STREAM_CODEC = StreamCodec.composite(UUID_STREAM_CODEC, ClientPreviewTransform::editorId, ByteBufCodecs.BOOL, ClientPreviewTransform::active, BlockPos.STREAM_CODEC, ClientPreviewTransform::anchor, ByteBufCodecs.VAR_INT, ClientPreviewTransform::rotation, ByteBufCodecs.BOOL, ClientPreviewTransform::isCopy, ClientPreviewTransform::new);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context)
		{
			RemoteEditSessions.updatePreviewTransform(editorId, active, active ? anchor : null, rotation, isCopy);
		}
	}
	
	public record BroadcastToolSound(int kind) implements MSPacket.PlayToServer //0 = select, 1 = clear
	{
		public static final Type<BroadcastToolSound> ID = new Type<>(Minestuck.id("editmode_broadcast/tool_sound"));
		public static final StreamCodec<FriendlyByteBuf, BroadcastToolSound> STREAM_CODEC = ByteBufCodecs.VAR_INT.map(BroadcastToolSound::new, BroadcastToolSound::kind).cast();
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context, ServerPlayer player)
		{
			if(!broadcastEnabled(player)) return;
			
			var sound = kind == 0 ? MSSoundEvents.EVENT_EDIT_TOOL_SELECT.get() : MSSoundEvents.EVENT_EDIT_TOOL_CLEAR.get();
			player.level().playSound(player, player.blockPosition(), sound, SoundSource.AMBIENT, 1.0f, 1.0f);
		}
	}
	
	public record ClientSessionClear(UUID editorId) implements MSPacket.PlayToClient
	{
		public static final Type<ClientSessionClear> ID = new Type<>(Minestuck.id("editmode_broadcast/client_session_clear"));
		public static final StreamCodec<FriendlyByteBuf, ClientSessionClear> STREAM_CODEC = UUID_STREAM_CODEC.map(ClientSessionClear::new, ClientSessionClear::editorId);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context)
		{
			RemoteEditSessions.clearSession(editorId);
		}
	}
}

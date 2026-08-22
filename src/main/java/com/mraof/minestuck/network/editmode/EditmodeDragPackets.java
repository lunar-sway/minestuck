package com.mraof.minestuck.network.editmode;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.api.alchemy.GristAmount;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.GristTypes;
import com.mraof.minestuck.api.alchemy.MutableGristSet;
import com.mraof.minestuck.api.alchemy.recipe.GristCostRecipe;
import com.mraof.minestuck.computer.editmode.*;
import com.mraof.minestuck.item.components.EncodedItemComponent;
import com.mraof.minestuck.item.components.MSItemComponents;
import com.mraof.minestuck.network.MSPacket;
import com.mraof.minestuck.player.GristCache;
import com.mraof.minestuck.skaianet.SburbPlayerData;
import com.mraof.minestuck.util.MSAttachments;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.mraof.minestuck.network.MSPayloads.VEC3_STREAM_CODEC;

public final class EditmodeDragPackets
{
	private static final int MAX_SELECTION_VOLUME = 1024;
	private static final int MAX_CONTAINER_RECURSION_DEPTH = 8;
	
	public static BlockPos rotateOffset(BlockPos offset, int sizeX, int sizeZ, Rotation rotation)
	{
		int x = offset.getX(), y = offset.getY(), z = offset.getZ();
		return switch(rotation)
		{
			case NONE -> offset;
			case CLOCKWISE_90 -> new BlockPos(sizeZ - 1 - z, y, x);
			case CLOCKWISE_180 -> new BlockPos(sizeX - 1 - x, y, sizeZ - 1 - z);
			case COUNTERCLOCKWISE_90 -> new BlockPos(z, y, sizeX - 1 - x);
		};
	}
	
	private static boolean editModePlaceCheck(EditData data, Player player, GristSet cost, BlockPos pos, Consumer<GristSet> missingGristTracker)
	{
		if(!player.level().getBlockState(pos).canBeReplaced())
			return false;
		
		if(cost == null)
			return false;
		
		if(!data.getGristCache().canAfford(cost))
		{
			missingGristTracker.accept(cost);
			return false;
		}
		
		return true;
	}
	
	private static boolean editModeDestroyCheck(EditData data, Player player, BlockPos pos, Consumer<GristSet> missingGristTracker)
	{
		BlockState block = player.level().getBlockState(pos);
		ItemStack stack = block.getCloneItemStack(null, player.level(), pos, player);
		DeployEntry entry = DeployList.getEntryForItem(stack, data.sburbData(), player.level(), DeployList.EntryLists.ATHENEUM);
		
		if(block.isAir())
			return false;
		else if(!MinestuckConfig.SERVER.gristRefund.get() && entry == null)
		{
			GristSet cost = GristTypes.BUILD.get().amount(1);
			if(!data.getGristCache().canAfford(cost))
			{
				missingGristTracker.accept(cost);
				return false;
			}
		}
		
		return true;
	}
	
	
	public record Fill(boolean isDown, BlockPos positionStart, BlockPos positionEnd, Vec3 hitVector, Direction side) implements MSPacket.PlayToServer
	{
		public static final Type<Fill> ID = new Type<>(Minestuck.id("editmode_drag/fill"));
		public static final StreamCodec<FriendlyByteBuf, Fill> STREAM_CODEC = StreamCodec.composite(
				ByteBufCodecs.BOOL,
				Fill::isDown,
				BlockPos.STREAM_CODEC,
				Fill::positionStart,
				BlockPos.STREAM_CODEC,
				Fill::positionEnd,
				VEC3_STREAM_CODEC,
				Fill::hitVector,
				Direction.STREAM_CODEC,
				Fill::side,
				Fill::new
		);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context, ServerPlayer player)
		{
			EditData data = ServerEditHandler.getData(player);
			
			if(data == null)
				return;
			
			EditTools cap = player.getData(MSAttachments.EDIT_TOOLS);
			
			cap.setEditPos1(positionStart);
			cap.setEditPos2(positionEnd);
			cap.setEditTrace(hitVector, side);
			
			InteractionHand hand = player.getMainHandItem().isEmpty() ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
			ItemStack stack = player.getItemInHand(hand);
			
			if(stack.isEmpty() || !(stack.getItem() instanceof BlockItem))
				return;
			
			DeployEntry entry = DeployList.getEntryForItem(stack, data.sburbData(), player.level());
			GristSet cost = entry != null ? entry.getCurrentCost(data.sburbData()) : GristCostRecipe.findCostForItem(stack, null, false, player.level());
			
			MutableGristSet missingCost = MutableGristSet.newDefault();
			boolean anyBlockPlaced = false;
			for(BlockPos pos : BlockPos.betweenClosed(positionStart, positionEnd))
			{
				int c = stack.getCount();
				//Will add the block's grist cost to the running tally of how much more grist you need, if you cannot afford it in editModePlaceCheck().
				if(editModePlaceCheck(data, player, cost, pos, missingCost::add) && stack.useOn(new UseOnContext(player, hand, new BlockHitResult(hitVector, side, pos, false))) != InteractionResult.FAIL)
				{
					//Check exists in-case we ever let non-editmode players use this tool for whatever reason.
					if(player.isCreative())
						stack.setCount(c);
					
					//broadcasts the block-place sounds to other players.
					SoundType soundType = ((BlockItem) stack.getItem()).getBlock().defaultBlockState().getSoundType();
					player.level().playSound(player, pos, soundType.getPlaceSound(), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
					
					anyBlockPlaced = true;
				}
			}
			
			if(anyBlockPlaced)
			{
				//broadcasts edit sound to other players.
				player.level().playSound(player, positionEnd, MSSoundEvents.EVENT_EDIT_TOOL_REVISE.get(), SoundSource.AMBIENT, 1.0f, 1.0f);
				player.swing(hand);
			}
			
			if(!missingCost.isEmpty())
				player.sendSystemMessage(GristCache.createMissingMessage(missingCost), true);
			
			ServerEditHandler.removeCursorEntity(player, !anyBlockPlaced);
		}
	}
	
	private record Captured(BlockPos sourcePos, BlockState state, CompoundTag blockEntityTag, GristSet.Immutable blockCost) {}
	
	private record ItemCostResult(GristSet.Immutable cost, boolean truncated) {}
	
	private static ItemCostResult computeItemStackCost(ItemStack stack, SburbPlayerData playerData, Level level, int depth)
	{
		if(stack.isEmpty())
			return new ItemCostResult(MutableGristSet.newDefault().asImmutable(), false);
		if(depth > MAX_CONTAINER_RECURSION_DEPTH)
			return new ItemCostResult(MutableGristSet.newDefault().asImmutable(), true);
		
		MutableGristSet total = MutableGristSet.newDefault();
		boolean truncated = false;
		
		DeployEntry entry = DeployList.getEntryForItem(stack, playerData, level);
		GristSet baseCost = entry != null ? entry.getCurrentCost(playerData) : GristCostRecipe.findCostForItem(stack, null, false, level);
		if(baseCost == null)
			return new ItemCostResult(MutableGristSet.newDefault().asImmutable(), true);
		total.add(baseCost.asImmutable());
		
		ItemContainerContents containerComponent = stack.get(DataComponents.CONTAINER);
		if(containerComponent != null)
		{
			for(ItemStack inner : containerComponent.nonEmptyItems())
			{
				ItemCostResult innerResult = computeItemStackCost(inner, playerData, level, depth + 1);
				truncated |= innerResult.truncated();
				addScaled(total, innerResult.cost(), inner.getCount());
			}
		}
		
		EncodedItemComponent encoded = stack.get(MSItemComponents.ENCODED_ITEM);
		if(encoded != null)
		{
			ItemStack inner = encoded.asItemStack();
			if(!inner.isEmpty())
			{
				ItemCostResult innerResult = computeItemStackCost(inner, playerData, level, depth + 1);
				truncated |= innerResult.truncated();
				addScaled(total, innerResult.cost(), inner.getCount());
			}
		}
		
		return new ItemCostResult(total.asImmutable(), truncated);
	}
	
	private static void addScaled(MutableGristSet target, GristSet.Immutable cost, int count)
	{
		for(GristAmount amount : cost.asAmounts())
			target.add(amount.type(), amount.amount() * count);
	}
	
	private static void executeSelectionTransfer(ServerPlayer player, EditData data, BlockPos corner1, BlockPos corner2, BlockPos anchor, boolean isCopy, Rotation rotation)
	{
		Level level = player.level();
		
		BlockPos min = new BlockPos(Math.min(corner1.getX(), corner2.getX()), Math.min(corner1.getY(), corner2.getY()), Math.min(corner1.getZ(), corner2.getZ()));
		BlockPos max = new BlockPos(Math.max(corner1.getX(), corner2.getX()), Math.max(corner1.getY(), corner2.getY()), Math.max(corner1.getZ(), corner2.getZ()));
		int sizeX = max.getX() - min.getX() + 1;
		int sizeZ = max.getZ() - min.getZ() + 1;
		
		long volume = (long) sizeX * (max.getY() - min.getY() + 1) * sizeZ;
		if(volume > MAX_SELECTION_VOLUME)
		{
			player.sendSystemMessage(Component.literal("Selection too large (" + volume + " blocks, max " + MAX_SELECTION_VOLUME + ")"), true);
			ServerEditHandler.removeCursorEntity(player, true);
			return;
		}
		
		List<Captured> captured = new ArrayList<>();
		MutableGristSet worstCaseCost = MutableGristSet.newDefault();
		
		for(BlockPos pos : BlockPos.betweenClosed(min, max))
		{
			BlockState state = level.getBlockState(pos);
			if(state.isAir())
				continue;
			if(state.getDestroySpeed(level, pos) < 0 || state.is(MSTags.Blocks.EDITMODE_BREAK_BLACKLIST))
			{
				player.sendSystemMessage(Component.literal("Selection contains a block that can't be moved!"), true);
				ServerEditHandler.removeCursorEntity(player, true);
				return;
			}
			
			var blockEntity = level.getBlockEntity(pos);
			CompoundTag beTag = blockEntity != null ? blockEntity.saveWithFullMetadata(level.registryAccess()) : null;
			
			ItemStack stack = state.getCloneItemStack(null, level, pos, player);
			ItemStack bareStack = stack.copy();
			bareStack.remove(DataComponents.BLOCK_ENTITY_DATA);
			bareStack.remove(DataComponents.CONTAINER);
			bareStack.remove(DataComponents.CONTAINER_LOOT);
			bareStack.remove(DataComponents.LOCK);
			
			DeployEntry entry = DeployList.getEntryForItem(bareStack, data.sburbData(), level);
			GristSet blockCostRaw = entry != null ? entry.getCurrentCost(data.sburbData()) : GristCostRecipe.findCostForItem(bareStack, null, false, level);
			if(blockCostRaw == null && isCopy)
				continue;
			MutableGristSet blockCost = blockCostRaw != null ? blockCostRaw.mutableCopy() : MutableGristSet.newDefault();
			
			// calculate the cost of items within container
			// and adding it to the total price (to avoid dupe abuse)
			if(isCopy && blockEntity instanceof Container container)
			{
				for(int slot = 0; slot < container.getContainerSize(); slot++)
				{
					ItemStack contained = container.getItem(slot);
					if(contained.isEmpty())
						continue;
					
					ItemCostResult containedResult = computeItemStackCost(contained, data.sburbData(), level, 0);
					if(containedResult.truncated())
					{
						player.sendSystemMessage(Component.literal("Selection contains an item nested too deeply to safely evaluate (or it does not have a grist cost yet)!"), true);
						ServerEditHandler.removeCursorEntity(player, true);
						return;
					}
					addScaled(blockCost, containedResult.cost(), contained.getCount());
				}
			}
			
			GristSet.Immutable blockCostImmutable = blockCost.asImmutable();
			captured.add(new Captured(pos.immutable(), state, beTag, blockCostImmutable));
			worstCaseCost.add(blockCostImmutable);
		}
		
		if(captured.isEmpty())
		{
			ServerEditHandler.removeCursorEntity(player, true);
			return;
		}
		
		for(Captured c : captured)
		{
			BlockPos localOffset = c.sourcePos().subtract(min);
			BlockPos rotatedOffset = rotateOffset(localOffset, sizeX, sizeZ, rotation);
			BlockPos dest = anchor.offset(rotatedOffset);
			
			boolean destInsideSelection = dest.getX() >= min.getX() && dest.getX() <= max.getX()
					&& dest.getY() >= min.getY() && dest.getY() <= max.getY()
					&& dest.getZ() >= min.getZ() && dest.getZ() <= max.getZ();
			
			if(!destInsideSelection && !level.getBlockState(dest).canBeReplaced())
			{
				player.sendSystemMessage(Component.literal("Can't fit the selection there!"), true);
				ServerEditHandler.removeCursorEntity(player, true);
				return;
			}
		}
		
		GristSet.Immutable worstCase = isCopy ? worstCaseCost.asImmutable() : moveCost(worstCaseCost.asImmutable());
		
		if(!data.getGristCache().canAfford(worstCase))
		{
			player.sendSystemMessage(GristCache.createMissingMessage(worstCase), true);
			ServerEditHandler.removeCursorEntity(player, true);
			return;
		}
		
		if(!isCopy)
		{
			for(Captured c : captured)
			{
				if(c.blockEntityTag() != null)
					level.removeBlockEntity(c.sourcePos());
				level.setBlock(c.sourcePos(), Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE);
			}
		}
		
		List<BlockPos> broadcastFrom = new ArrayList<>();
		List<BlockPos> broadcastTo = new ArrayList<>();
		List<BlockPos> placedPositions = new ArrayList<>(captured.size());
		
		for(Captured c : captured)
		{
			BlockPos localOffset = c.sourcePos().subtract(min);
			BlockPos rotatedOffset = rotateOffset(localOffset, sizeX, sizeZ, rotation);
			BlockPos dest = anchor.offset(rotatedOffset);
			
			BlockState toPlace = c.state().rotate(rotation);
			if(toPlace.hasProperty(BlockStateProperties.EXTENDED))
				toPlace = toPlace.setValue(BlockStateProperties.EXTENDED, false);
			level.setBlock(dest, toPlace, Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE);
			
			if(c.blockEntityTag() != null && level.getBlockEntity(dest) != null)
			{
				CompoundTag movedTag = c.blockEntityTag().copy();
				movedTag.putInt("x", dest.getX());
				movedTag.putInt("y", dest.getY());
				movedTag.putInt("z", dest.getZ());
				level.getBlockEntity(dest).loadWithComponents(movedTag, level.registryAccess());
			}
			
			placedPositions.add(dest);
			
			if(!isCopy)
			{
				broadcastFrom.add(c.sourcePos());
				broadcastTo.add(dest);
			}
		}
		
		for(BlockPos dest : placedPositions)
			level.updateNeighborsAt(dest, level.getBlockState(dest).getBlock());
		
		if(!isCopy)
		{
			for(Captured c : captured)
				level.updateNeighborsAt(c.sourcePos(), Blocks.AIR);
		}
		
		for(BlockPos dest : placedPositions)
		{
			BlockState current = level.getBlockState(dest);
			BlockState updated = Block.updateFromNeighbourShapes(current, level, dest);
			if(updated != current)
				level.setBlock(dest, updated, 3);
		}
		
		MutableGristSet actualCost = MutableGristSet.newDefault();
		for(int i = 0; i < captured.size(); i++)
		{
			Captured c = captured.get(i);
			BlockPos dest = placedPositions.get(i);
			
			BlockState finalState = level.getBlockState(dest);
			boolean stillCorrectBlock = !finalState.isAir() && finalState.getBlock() == c.state().getBlock();
			
			if(stillCorrectBlock && !finalState.canSurvive(level, dest))
			{
				if(finalState.hasBlockEntity())
					level.removeBlockEntity(dest);
				level.setBlock(dest, Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS | Block.UPDATE_SUPPRESS_DROPS);
				stillCorrectBlock = false;
			}
			
			if(stillCorrectBlock)
			{
				GristSet.Immutable pieceCost = isCopy ? c.blockCost() : moveCost(c.blockCost());
				actualCost.add(pieceCost);
			}
		}
		
		data.getGristCache().tryTake(actualCost.asImmutable(), GristHelper.EnumSource.SERVER);
		
		SoundEvent commitSound = isCopy ? MSSoundEvents.EVENT_EDIT_TOOL_COPY.get() : MSSoundEvents.EVENT_EDIT_TOOL_MOVE.get();
		level.playSound(player, anchor, commitSound, SoundSource.AMBIENT, 1.0f, 1.0f);
		player.swing(InteractionHand.MAIN_HAND);
		
		ServerEditHandler.removeCursorEntity(player, false);
		
		if(isCopy)
		{
			boolean swapXZ = rotation == Rotation.CLOCKWISE_90 || rotation == Rotation.COUNTERCLOCKWISE_90;
			int rotatedSizeX = swapXZ ? sizeZ : sizeX;
			int rotatedSizeZ = swapXZ ? sizeX : sizeZ;
			int sizeY = max.getY() - min.getY() + 1;
			
			BlockPos newMin = anchor;
			BlockPos newMax = anchor.offset(rotatedSizeX - 1, sizeY - 1, rotatedSizeZ - 1);
			PacketDistributor.sendToPlayer(player, new ServerEditPackets.SelectionUpdate(false, newMin, newMax));
		}
		else
		{
			PacketDistributor.sendToPlayer(player, new ServerEditPackets.SelectionUpdate(true, BlockPos.ZERO, BlockPos.ZERO));
		}
	}
	
	/** 5% of the item normal cost per grist type rounded; floor of 1 per type present. */
	private static GristSet.Immutable moveCost(GristSet fullCost)
	{
		long totalValue = 0;
		for(GristAmount amount : fullCost.asAmounts()) totalValue += amount.amount();
		long buildAmount = Math.max(1, Math.round(totalValue * 0.05));
		return GristTypes.BUILD.get().amount(buildAmount);
	}
	
	public record MoveSelection(BlockPos corner1, BlockPos corner2, BlockPos anchor, int rotation) implements MSPacket.PlayToServer
	{
		public static final Type<MoveSelection> ID = new Type<>(Minestuck.id("editmode_drag/move_selection"));
		public static final StreamCodec<FriendlyByteBuf, MoveSelection> STREAM_CODEC = StreamCodec.composite(
				BlockPos.STREAM_CODEC, MoveSelection::corner1,
				BlockPos.STREAM_CODEC, MoveSelection::corner2,
				BlockPos.STREAM_CODEC, MoveSelection::anchor,
				ByteBufCodecs.VAR_INT, MoveSelection::rotation,
				MoveSelection::new
		);
		
		@Override
		public Type<? extends CustomPacketPayload> type() { return ID; }
		
		@Override
		public void execute(IPayloadContext context, ServerPlayer player)
		{
			EditData data = ServerEditHandler.getData(player);
			if(data == null)
				return;
			executeSelectionTransfer(player, data, corner1, corner2, anchor, false, Rotation.values()[Math.floorMod(rotation, 4)]);
		}
	}
	
	public record CopySelection(BlockPos corner1, BlockPos corner2, BlockPos anchor, int rotation) implements MSPacket.PlayToServer
	{
		public static final Type<CopySelection> ID = new Type<>(Minestuck.id("editmode_drag/copy_selection"));
		public static final StreamCodec<FriendlyByteBuf, CopySelection> STREAM_CODEC = StreamCodec.composite(
				BlockPos.STREAM_CODEC, CopySelection::corner1,
				BlockPos.STREAM_CODEC, CopySelection::corner2,
				BlockPos.STREAM_CODEC, CopySelection::anchor,
				ByteBufCodecs.VAR_INT, CopySelection::rotation,
				CopySelection::new
		);
		
		@Override
		public Type<? extends CustomPacketPayload> type() { return ID; }
		
		@Override
		public void execute(IPayloadContext context, ServerPlayer player)
		{
			EditData data = ServerEditHandler.getData(player);
			if(data == null)
				return;
			executeSelectionTransfer(player, data, corner1, corner2, anchor, true, Rotation.values()[Math.floorMod(rotation, 4)]);
		}
	}
	
	public record Destroy(boolean isDown, BlockPos positionStart, BlockPos positionEnd, Vec3 hitVector, Direction side) implements MSPacket.PlayToServer
	{
		public static final Type<Destroy> ID = new Type<>(Minestuck.id("editmode_drag/destroy"));
		public static final StreamCodec<FriendlyByteBuf, Destroy> STREAM_CODEC = StreamCodec.composite(
				ByteBufCodecs.BOOL,
				Destroy::isDown,
				BlockPos.STREAM_CODEC,
				Destroy::positionStart,
				BlockPos.STREAM_CODEC,
				Destroy::positionEnd,
				VEC3_STREAM_CODEC,
				Destroy::hitVector,
				Direction.STREAM_CODEC,
				Destroy::side,
				Destroy::new
		);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context, ServerPlayer player)
		{
			EditData data = ServerEditHandler.getData(player);
			
			if(data == null)
				return;
			
			EditTools cap = player.getData(MSAttachments.EDIT_TOOLS);
			
			cap.setEditPos1(positionStart);
			cap.setEditPos2(positionEnd);
			cap.setEditTrace(hitVector, side);
			
			MutableGristSet missingCost = MutableGristSet.newDefault();
			boolean anyBlockDestroyed = false;
			for(BlockPos pos : BlockPos.betweenClosed(positionStart, positionEnd))
			{
				BlockState block = player.level().getBlockState(pos);
				
				Consumer<GristSet> missingCostTracker = missingCost::add; //Will add the block's grist cost to the running tally of how much more grist you need, if you cannot afford it in editModeDestroyCheck().
				if(editModeDestroyCheck(data, player, pos, missingCostTracker))
				{
					player.gameMode.destroyAndAck(pos, 3, "creative destroy");
					
					//broadcasts block-break particles and sounds to other players.
					player.level().levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(block));
					player.level().gameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(player, block));
					
					anyBlockDestroyed = true;
				}
			}
			
			if(anyBlockDestroyed)
			{
				//broadcasts edit sound to other players.
				player.level().playSound(player, positionEnd, MSSoundEvents.EVENT_EDIT_TOOL_RECYCLE.get(), SoundSource.AMBIENT, 1.0f, 0.85f);
				player.swing(InteractionHand.MAIN_HAND);
			}
			
			if(!missingCost.isEmpty())
				player.sendSystemMessage(GristCache.createMissingMessage(missingCost), true);
			
			ServerEditHandler.removeCursorEntity(player, !anyBlockDestroyed);
		}
	}
	
	public record Cursor(boolean isDown, BlockPos positionStart, BlockPos positionEnd) implements MSPacket.PlayToServer
	{
		public static final Type<Cursor> ID = new Type<>(Minestuck.id("editmode_drag/cursor"));
		public static final StreamCodec<FriendlyByteBuf, Cursor> STREAM_CODEC = StreamCodec.composite(
				ByteBufCodecs.BOOL,
				Cursor::isDown,
				BlockPos.STREAM_CODEC,
				Cursor::positionStart,
				BlockPos.STREAM_CODEC,
				Cursor::positionEnd,
				Cursor::new
		);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context, ServerPlayer player)
		{
			if(ServerEditHandler.isInEditmode(player))
			{
				EditTools cap = player.getData(MSAttachments.EDIT_TOOLS);
				
				cap.setEditPos1(positionStart);
				cap.setEditPos2(positionEnd);
				
				ServerEditHandler.updateEditToolsServer(player, isDown, positionStart, positionEnd);
			}
		}
	}
	
	public record Reset() implements MSPacket.PlayToServer
	{
		public static final Type<Reset> ID = new Type<>(Minestuck.id("editmode_drag/reset"));
		public static final StreamCodec<FriendlyByteBuf, Reset> STREAM_CODEC = StreamCodec.unit(new Reset());
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context, ServerPlayer player)
		{
			if(!player.level().isClientSide())
			{
				EditTools cap = player.getData(MSAttachments.EDIT_TOOLS);
				
				ServerEditHandler.removeCursorEntity(player, true);
				cap.resetDragTools();
			}
		}
	}
}
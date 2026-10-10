package com.mraof.minestuck.network.editmode;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.api.alchemy.GristAmount;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.GristTypes;
import com.mraof.minestuck.api.alchemy.MutableGristSet;
import com.mraof.minestuck.api.alchemy.recipe.GristCostRecipe;
import com.mraof.minestuck.block.machine.MachineMultiblock;
import com.mraof.minestuck.computer.editmode.*;
import com.mraof.minestuck.item.components.EncodedItemComponent;
import com.mraof.minestuck.item.block.MultiblockItem;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.piston.MovingPistonBlock;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.piston.PistonHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static com.mraof.minestuck.network.MSPayloads.VEC3_STREAM_CODEC;

public final class EditmodeDragPackets
{
	private static final int MAX_CONTAINER_RECURSION_DEPTH = 8;
	
	public static final String SELECTION_TOO_LARGE = "minestuck.editmode.selection_too_large";
	public static final String UNMOVABLE_BLOCK = "minestuck.editmode.unmovable_block";
	public static final String MOVING_PISTON = "minestuck.editmode.moving_piston";
	public static final String PARTIAL_MULTIBLOCK = "minestuck.editmode.partial_multiblock";
	public static final String NO_GRIST_COST = "minestuck.editmode.no_grist_cost";
	public static final String ITEM_NO_GRIST_COST = "minestuck.editmode.item_no_grist_cost";
	public static final String CANT_FIT = "minestuck.editmode.cant_fit";
	public static final String ENTITY_IN_THE_WAY = "minestuck.editmode.entity_in_the_way";
	
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
	
	private record Captured(BlockPos sourcePos, BlockState state, CompoundTag blockEntityTag, GristSet.Immutable blockCost, boolean secondaryPart) {}
	
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
		GristSet baseCost = entry != null ? entry.getRepeatCost(playerData) : GristCostRecipe.findCostForItem(stack, null, false, level);
		if(baseCost == null)
			return new ItemCostResult(MutableGristSet.newDefault().asImmutable(), true);
		total.add(baseCost.asImmutable());
		
		ItemContainerContents containerComponent = stack.get(DataComponents.CONTAINER);
		if(containerComponent != null)
		{
			for(ItemStack inner : containerComponent.nonEmptyItems())
				truncated |= accumulateInnerCost(inner, playerData, level, depth, total);
		}
		
		EncodedItemComponent encoded = stack.get(MSItemComponents.ENCODED_ITEM);
		if(encoded != null)
			truncated |= accumulateInnerCost(encoded.asItemStack(), playerData, level, depth, total);
		
		return new ItemCostResult(total.asImmutable(), truncated);
	}
	
	private static boolean accumulateInnerCost(ItemStack inner, SburbPlayerData playerData, Level level, int depth, MutableGristSet total)
	{
		if(inner.isEmpty())
			return false;
		ItemCostResult innerResult = computeItemStackCost(inner, playerData, level, depth + 1);
		addScaled(total, innerResult.cost(), inner.getCount());
		return innerResult.truncated();
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
		
		if(!validateVolume(player, min, max, sizeX, sizeZ))
			return;
		
		CaptureResult captureResult = captureBlocks(player, data, level, min, max, isCopy);
		if(captureResult == null) // hard failure already messaged inside
			return;
		
		List<Captured> captured = captureResult.captured();
		if(captured.isEmpty())
		{
			ServerEditHandler.removeCursorEntity(player, true);
			return;
		}
		
		if(!validateDestinations(player, level, captured, min, max, anchor, sizeX, sizeZ, rotation))
			return;
		
		GristSet.Immutable worstCase = isCopy ? captureResult.worstCaseCost() : moveCost(countObjects(captured));
		if(!data.getGristCache().canAfford(worstCase))
		{
			player.sendSystemMessage(GristCache.createMissingMessage(worstCase), true);
			ServerEditHandler.removeCursorEntity(player, true);
			return;
		}
		
		if(!isCopy)
			clearSourceBlocks(level, captured);
		
		List<BlockPos> placedPositions = placeBlocks(level, captured, min, anchor, sizeX, sizeZ, rotation);
		
		finalizeWorld(level, captured, placedPositions, isCopy);
		
		calculateActualCost(level, data, captured, placedPositions, isCopy);
		
		announceResult(player, level, min, max, anchor, sizeX, sizeZ, isCopy, rotation);
	}
	
	//counts multipart objects
	private static int countObjects(List<Captured> captured)
	{
		int count = 0;
		for(Captured c : captured)
			if(!c.secondaryPart())
				count++;
		return count;
	}
	
	private static boolean validateVolume(ServerPlayer player, BlockPos min, BlockPos max, int sizeX, int sizeZ)
	{
		long volume = (long) sizeX * (max.getY() - min.getY() + 1) * sizeZ;
		if(volume > MinestuckConfig.SERVER.maxSelectionVolume.get())
		{
			player.sendSystemMessage(Component.translatable(SELECTION_TOO_LARGE, volume, MinestuckConfig.SERVER.maxSelectionVolume.get()), true);
			ServerEditHandler.removeCursorEntity(player, true);
			return false;
		}
		return true;
	}
	
	private record CaptureResult(List<Captured> captured, GristSet.Immutable worstCaseCost) {}
	
	private static CaptureResult captureBlocks(ServerPlayer player, EditData data, Level level, BlockPos min, BlockPos max, boolean isCopy)
	{
		boolean hasBlockWithoutCost = false;
		List<Captured> captured = new ArrayList<>();
		MutableGristSet worstCaseCost = MutableGristSet.newDefault();
		Set<Object> chargedParts = new HashSet<>();
		
		for(BlockPos pos : BlockPos.betweenClosed(min, max))
		{
			BlockState state = level.getBlockState(pos);
			if(state.isAir())
				continue;
			if(state.getDestroySpeed(level, pos) < 0 || state.is(MSTags.Blocks.EDITMODE_BREAK_BLACKLIST))
			{
				player.sendSystemMessage(Component.translatable(UNMOVABLE_BLOCK), true);
				ServerEditHandler.removeCursorEntity(player, true);
				return null;
			}
			
			if(state.getBlock() instanceof MovingPistonBlock)
			{
				player.sendSystemMessage(Component.translatable(MOVING_PISTON), true);
				ServerEditHandler.removeCursorEntity(player, true);
				return null;
			}
			
			Multipart multipart = findMultipart(level, pos, state);
			if(multipart != null && !multipart.isFullyInside(min, max))
			{
				player.sendSystemMessage(Component.translatable(PARTIAL_MULTIBLOCK), true);
				ServerEditHandler.removeCursorEntity(player, true);
				return null;
			}
			
			var blockEntity = level.getBlockEntity(pos);
			CompoundTag beTag = blockEntity != null ? blockEntity.saveWithFullMetadata(level.registryAccess()) : null;
			
			Object partKey = multipart != null ? multipart.key() : null;
			boolean secondaryPart = partKey != null && chargedParts.contains(partKey);
			
			MutableGristSet blockCost;
			if(secondaryPart)
				blockCost = MutableGristSet.newDefault();
			else
			{
				ItemStack stack = state.getCloneItemStack(null, level, pos, player);
				ItemStack bareStack = stack.copy();
				bareStack.remove(DataComponents.BLOCK_ENTITY_DATA);
				bareStack.remove(DataComponents.CONTAINER);
				bareStack.remove(DataComponents.CONTAINER_LOOT);
				bareStack.remove(DataComponents.LOCK);
				
				DeployEntry entry = DeployList.getEntryForItem(bareStack, data.sburbData(), level);
				GristSet blockCostRaw = entry != null ? entry.getRepeatCost(data.sburbData()) : GristCostRecipe.findCostForItem(bareStack, null, false, level);
				if(blockCostRaw == null && isCopy)
				{
					hasBlockWithoutCost = true;
					continue;
				}
				blockCost = blockCostRaw != null ? blockCostRaw.mutableCopy() : MutableGristSet.newDefault();
				
				if(partKey != null)
					chargedParts.add(partKey);
			}
			
			if(isCopy && blockEntity instanceof Container container)
				beTag = accumulateContainerCost(player, level, pos, state, container, beTag, data, blockCost);
			
			GristSet.Immutable blockCostImmutable = blockCost.asImmutable();
			captured.add(new Captured(pos.immutable(), state, beTag, blockCostImmutable, secondaryPart));
			worstCaseCost.add(blockCostImmutable);
		}
		
		if(hasBlockWithoutCost)
			player.sendSystemMessage(Component.translatable(NO_GRIST_COST), true);
		
		return new CaptureResult(captured, worstCaseCost.asImmutable());
	}
	
	private static boolean isInside(BlockPos pos, BlockPos min, BlockPos max)
	{
		return pos.getX() >= min.getX() && pos.getX() <= max.getX() && pos.getY() >= min.getY() && pos.getY() <= max.getY() && pos.getZ() >= min.getZ() && pos.getZ() <= max.getZ();
	}
	
	private record Multipart(Object key, List<BlockPos> positions)
	{
		boolean isFullyInside(BlockPos min, BlockPos max)
		{
			for(BlockPos pos : positions)
				if(!isInside(pos, min, max)) return false;
			return true;
		}
	}
	
	@Nullable
	private static Multipart findMultipart(Level level, BlockPos pos, BlockState state)
	{
		Block block = state.getBlock();
		
		if(block.asItem() instanceof MultiblockItem multiblockItem)
		{
			MachineMultiblock multiblock = multiblockItem.getMultiblock();
			return multiblock.findValidPlacement(level, pos, state).map(placement -> new Multipart(new MachineKey(multiblock, placement), multiblock.getRequiredPositions(placement))).orElse(null);
		}
		
		if(state.hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF))
		{
			BlockPos lowerPos = (state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER ? pos.below() : pos).immutable();
			BlockPos upperPos = lowerPos.above();
			BlockState lower = level.getBlockState(lowerPos), upper = level.getBlockState(upperPos);
			if(lower.is(block) && upper.is(block) && lower.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER && upper.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER)
				return new Multipart(lowerPos, List.of(lowerPos, upperPos));
			return null;
		}
		
		if(state.hasProperty(BlockStateProperties.BED_PART) && state.hasProperty(BlockStateProperties.HORIZONTAL_FACING))
		{
			Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
			BlockPos footPos = (state.getValue(BlockStateProperties.BED_PART) == BedPart.HEAD ? pos.relative(facing.getOpposite()) : pos).immutable();
			BlockPos headPos = footPos.relative(facing);
			BlockState foot = level.getBlockState(footPos), head = level.getBlockState(headPos);
			if(foot.is(block) && head.is(block) && foot.getValue(BlockStateProperties.BED_PART) == BedPart.FOOT && head.getValue(BlockStateProperties.BED_PART) == BedPart.HEAD)
				return new Multipart(footPos, List.of(footPos, headPos));
			return null;
		}
		
		if(block instanceof PistonHeadBlock)
		{
			Direction facing = state.getValue(BlockStateProperties.FACING);
			BlockPos basePos = pos.relative(facing.getOpposite()).immutable();
			BlockState base = level.getBlockState(basePos);
			if(base.getBlock() instanceof PistonBaseBlock && base.getValue(BlockStateProperties.EXTENDED) && base.getValue(BlockStateProperties.FACING) == facing)
				return new Multipart(basePos, List.of(basePos, pos.immutable()));
			return null;
		}
		if(block instanceof PistonBaseBlock && state.getValue(BlockStateProperties.EXTENDED))
		{
			Direction facing = state.getValue(BlockStateProperties.FACING);
			BlockPos headPos = pos.relative(facing).immutable();
			BlockState head = level.getBlockState(headPos);
			if(head.getBlock() instanceof PistonHeadBlock && head.getValue(BlockStateProperties.FACING) == facing)
				return new Multipart(pos.immutable(), List.of(pos.immutable(), headPos));
			return null;
		}
		
		return null;
	}
	
	private record MachineKey(MachineMultiblock multiblock, MachineMultiblock.Placement placement)
	{
	}
	
	private static CompoundTag accumulateContainerCost(ServerPlayer player, Level level, BlockPos pos, BlockState state, Container container, CompoundTag beTag, EditData data, MutableGristSet blockCost)
	{
		List<Integer> slotsToStrip = new ArrayList<>();
		for(int slot = 0; slot < container.getContainerSize(); slot++)
		{
			ItemStack contained = container.getItem(slot);
			if(contained.isEmpty())
				continue;
			
			ItemCostResult containedResult = computeItemStackCost(contained, data.sburbData(), level, 0);
			if(containedResult.truncated())
			{
				player.sendSystemMessage(Component.translatable(ITEM_NO_GRIST_COST), true);
				slotsToStrip.add(slot);
				continue;
			}
			addScaled(blockCost, containedResult.cost(), contained.getCount());
		}
		
		if(!slotsToStrip.isEmpty() && state.getBlock() instanceof EntityBlock entityBlock)
		{
			BlockEntity ghostEntity = entityBlock.newBlockEntity(pos, state);
			if(ghostEntity instanceof Container ghostContainer && beTag != null)
			{
				ghostEntity.setLevel(level);
				ghostEntity.loadWithComponents(beTag, level.registryAccess());
				for(int slot : slotsToStrip)
					ghostContainer.setItem(slot, ItemStack.EMPTY);
				beTag = ghostEntity.saveWithFullMetadata(level.registryAccess());
			}
		}
		
		return beTag;
	}
	
	private static boolean validateDestinations(ServerPlayer player, Level level, List<Captured> captured, BlockPos min, BlockPos max, BlockPos anchor, int sizeX, int sizeZ, Rotation rotation)
	{
		for(Captured c : captured)
		{
			BlockPos dest = computeDest(c.sourcePos(), min, anchor, sizeX, sizeZ, rotation);
			
			boolean destInsideSelection = dest.getX() >= min.getX() && dest.getX() <= max.getX()
					&& dest.getY() >= min.getY() && dest.getY() <= max.getY()
					&& dest.getZ() >= min.getZ() && dest.getZ() <= max.getZ();
			
			if(!destInsideSelection && !level.getBlockState(dest).canBeReplaced())
			{
				player.sendSystemMessage(Component.translatable(CANT_FIT), true);
				ServerEditHandler.removeCursorEntity(player, true);
				return false;
			}
			
			if(!destInsideSelection && wouldSuffocateEntity(level, dest, c.state().rotate(rotation)))
			{
				player.sendSystemMessage(Component.translatable(ENTITY_IN_THE_WAY), true);
				ServerEditHandler.removeCursorEntity(player, true);
				return false;
			}
		}
		return true;
	}
	
	private static BlockPos computeDest(BlockPos sourcePos, BlockPos min, BlockPos anchor, int sizeX, int sizeZ, Rotation rotation)
	{
		BlockPos localOffset = sourcePos.subtract(min);
		BlockPos rotatedOffset = rotateOffset(localOffset, sizeX, sizeZ, rotation);
		return anchor.offset(rotatedOffset);
	}
	
	private static void clearSourceBlocks(Level level, List<Captured> captured)
	{
		for(Captured c : captured)
			if(c.state().getBlock() instanceof PistonBaseBlock) clearSourceBlock(level, c);
		for(Captured c : captured)
			if(!(c.state().getBlock() instanceof PistonBaseBlock)) clearSourceBlock(level, c);
	}
	
	private static void clearSourceBlock(Level level, Captured c)
	{
		if(c.blockEntityTag() != null) level.removeBlockEntity(c.sourcePos());
		level.setBlock(c.sourcePos(), Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE);
	}
	
	private static List<BlockPos> placeBlocks(Level level, List<Captured> captured, BlockPos min, BlockPos anchor, int sizeX, int sizeZ, Rotation rotation)
	{
		List<BlockPos> placedPositions = new ArrayList<>(captured.size());
		
		for(Captured c : captured)
		{
			BlockPos dest = computeDest(c.sourcePos(), min, anchor, sizeX, sizeZ, rotation);
			
			BlockState toPlace = c.state().rotate(rotation);
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
		}
		
		return placedPositions;
	}
	
	private static void finalizeWorld(Level level, List<Captured> captured, List<BlockPos> placedPositions, boolean isCopy)
	{
		Set<BlockPos> affected = new HashSet<>(placedPositions);
		if(!isCopy)
		{
			for(Captured c : captured)
				for(Direction dir : Direction.values())
					affected.add(c.sourcePos().relative(dir));
		}
		
		if(!isCopy)
		{
			for(Captured c : captured)
			{
				BlockPos source = c.sourcePos();
				level.updateNeighborsAt(source, level.getBlockState(source).getBlock());
			}
		}
		
		for(BlockPos pos : affected)
			finalizeUpdate(level, pos);
	}
	
	private static MutableGristSet calculateActualCost(Level level, EditData data, List<Captured> captured, List<BlockPos> placedPositions, boolean isCopy)
	{
		MutableGristSet actualCost = MutableGristSet.newDefault();
		int successfullyMovedCount = 0;
		for(int i = 0; i < captured.size(); i++)
		{
			Captured c = captured.get(i);
			BlockPos dest = placedPositions.get(i);
			BlockState finalState = level.getBlockState(dest);
			boolean survived = !finalState.isAir() && finalState.getBlock() == c.state().getBlock();
			
			if(survived)
			{
				if(isCopy)
					actualCost.add(c.blockCost());
				else if(!c.secondaryPart())
					successfullyMovedCount++;
			}
		}
		
		if(!isCopy && successfullyMovedCount > 0)
			actualCost.add(moveCost(successfullyMovedCount));
		
		data.getGristCache().tryTake(actualCost.asImmutable(), GristHelper.EnumSource.SERVER);
		return actualCost;
	}
	
	private static void announceResult(ServerPlayer player, Level level, BlockPos min, BlockPos max, BlockPos anchor, int sizeX, int sizeZ, boolean isCopy, Rotation rotation)
	{
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
			
			if(MinestuckConfig.SERVER.visualsToOthers.get())
				PacketDistributor.sendToPlayersTrackingEntity(player, new EditmodeBroadcastPackets.ClientSelectionBox(player.getUUID(), true, newMin, newMax));
		}
		else
		{
			PacketDistributor.sendToPlayer(player, new ServerEditPackets.SelectionUpdate(true, BlockPos.ZERO, BlockPos.ZERO));
			
			if(MinestuckConfig.SERVER.visualsToOthers.get())
				PacketDistributor.sendToPlayersTrackingEntity(player, new EditmodeBroadcastPackets.ClientSelectionBox(player.getUUID(), false, BlockPos.ZERO, BlockPos.ZERO));
		}
	}
	
	private static boolean wouldSuffocateEntity(Level level, BlockPos pos, BlockState state)
	{
		VoxelShape collisionShape = state.getCollisionShape(level, pos);
		if(collisionShape.isEmpty())
			return false;
		
		AABB box = collisionShape.bounds().move(pos.getX(), pos.getY(), pos.getZ());
		return !level.getEntities((Entity) null, box, entity -> !entity.isSpectator()).isEmpty();
	}
	
/*	*//** 5% of the item normal cost per grist type rounded; floor of 1 per type present. *//*
	private static GristSet.Immutable moveCost(GristSet fullCost)
	{
		long totalValue = 0;
		for(GristAmount amount : fullCost.asAmounts()) totalValue += amount.amount();
		long buildAmount = Math.max(1, Math.round(totalValue * 0.05));
		return GristTypes.BUILD.get().amount(1);
	}*/
	
	/**
	 * Cost of the entire move operation using floor(log2(blockCount))
	 */
	private static GristSet.Immutable moveCost(int blockCount)
	{
		if(blockCount <= 1)
			return MutableGristSet.newDefault().asImmutable();
		int amount = 31 - Integer.numberOfLeadingZeros(blockCount);
		return GristTypes.BUILD.get().amount(amount);
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
				
				Consumer<GristSet> missingCostTracker = missingCost::add;
				if(editModeDestroyCheck(data, player, pos, missingCostTracker))
				{
					player.gameMode.destroyAndAck(pos, 3, "creative destroy");
					
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
	
	public record Reset(boolean rejected) implements MSPacket.PlayToServer
	{
		public static final Type<Reset> ID = new Type<>(Minestuck.id("editmode_drag/reset"));
		public static final StreamCodec<FriendlyByteBuf, Reset> STREAM_CODEC =
				ByteBufCodecs.BOOL.map(Reset::new, Reset::rejected).cast();
		
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
				ServerEditHandler.removeCursorEntity(player, rejected);
				cap.resetDragTools();
			}
		}
	}
	
	private static void finalizeUpdate(Level level, BlockPos pos)
	{
		BlockState state = level.getBlockState(pos);
		if(state.isAir())
			return;
		
		BlockState updated = Block.updateFromNeighbourShapes(state, level, pos);
		if(updated != state)
			Block.updateOrDestroy(state, updated, level, pos, Block.UPDATE_ALL);
		
		level.updateNeighborsAt(pos, level.getBlockState(pos).getBlock());
	}
}
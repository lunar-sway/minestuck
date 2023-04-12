package com.mraof.minestuck.network;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.alchemy.*;
import com.mraof.minestuck.computer.editmode.*;
import com.mraof.minestuck.player.PlayerSavedData;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.util.MSCapabilities;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Tuple;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class EditmodeDragPacket
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private static Tuple<Boolean, GristSet> editModePlaceCheck(EditData data, Player player, GristSet cost, BlockPos pos)
	{
		SburbConnection connection = data.getConnection();
		ItemStack stack = player.getMainHandItem();
		
		if(!player.level.getBlockState(pos).getMaterial().isReplaceable())
			return new Tuple<>(false, new GristSet());

		
		if(!GristHelper.canAfford(PlayerSavedData.getData(connection.getClientIdentifier(), player.level).getGristCache(), cost))
			return new Tuple<>(false, cost);
		
		return new Tuple<>(true, new GristSet());
	}
	
	private static Tuple<Boolean, GristSet> editModeDestroyCheck(EditData data, Player player, BlockPos pos)
	{
		SburbConnection connection = data.getConnection();
		BlockState block = player.level.getBlockState(pos);
		ItemStack stack = block.getCloneItemStack(null, player.level, pos, player);
		DeployEntry entry = DeployList.getEntryForItem(stack, data.getConnection(), player.level, DeployList.EntryLists.ATHENEUM);
		
		if(block.isAir())
			return new Tuple<>(false, new GristSet());
		else if(!MinestuckConfig.SERVER.gristRefund.get() && entry == null)
		{
			GristSet cost = new GristSet(GristTypes.BUILD,1);
			if(!GristHelper.canAfford(PlayerSavedData.getData(connection.getClientIdentifier(), player.level).getGristCache(), cost))
				return new Tuple<>(false, cost);
		}
		return new Tuple<>(true, new GristSet());
	}
	
	
	public record Fill(boolean isDown, BlockPos positionStart, BlockPos positionEnd, Vec3 hitVector, Direction side) implements PlayToServerPacket
	{
		@Override
		public void encode(FriendlyByteBuf buffer)
		{
			buffer.writeBoolean(isDown);
			buffer.writeBlockPos(positionStart);
			buffer.writeBlockPos(positionEnd);
			buffer.writeDouble(hitVector.x);
			buffer.writeDouble(hitVector.y);
			buffer.writeDouble(hitVector.z);
			buffer.writeEnum(side);
		}
		
		public static Fill decode(FriendlyByteBuf buffer)
		{
			boolean isDragging = buffer.readBoolean();
			BlockPos positionStart = buffer.readBlockPos();
			BlockPos positionEnd = buffer.readBlockPos();
			Vec3 hitVector = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
			Direction side = buffer.readEnum(Direction.class);
			
			return new Fill(isDragging, positionStart, positionEnd, hitVector, side);
		}
		
		@Override
		public void execute(ServerPlayer player)
		{
			EditData data = ServerEditHandler.getData(player);
			if(!player.getLevel().isClientSide() && data != null)
			{
				IEditTools cap = player.getCapability(MSCapabilities.EDIT_TOOLS_CAPABILITY).orElseThrow(() -> LOGGER.throwing(new IllegalStateException("EditTool Capability is missing on player " + player.getDisplayName().getString() + " on server-side (during packet execution)!")));
				
				cap.setEditPos1(positionStart);
				cap.setEditPos2(positionEnd);
				cap.setEditTrace(hitVector, side);
				
				InteractionHand hand = player.getMainHandItem().isEmpty() ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
				ItemStack stack = player.getItemInHand(hand);
				
				if(stack.isEmpty() || !(stack.getItem() instanceof BlockItem))
					return;
				
				DeployEntry entry = DeployList.getEntryForItem(stack, data.getConnection(), player.level);
				GristSet cost = entry != null ? entry.getCurrentCost(data.getConnection()) : GristCost.findCostForItem(stack, null, false, player.level);
				
				GristSet missingCost = new GristSet();
				boolean anyBlockPlaced = false;
				for(BlockPos pos : BlockPos.betweenClosed(positionStart, positionEnd))
				{
					int c = stack.getCount();
					Tuple<Boolean, GristSet> conditionAndCostPair = editModePlaceCheck(data, player, cost, pos);
					if(conditionAndCostPair.getA() && stack.useOn(new UseOnContext(player, hand, new BlockHitResult(hitVector, side, pos, false))) != InteractionResult.FAIL)
					{
						//Check exists in-case we ever let non-editmode players use this tool for whatever reason.
						if(player.isCreative())
							stack.setCount(c);
						
						//broadcasts the block-place sounds to other players.
						SoundType soundType = ((BlockItem) stack.getItem()).getBlock().defaultBlockState().getSoundType();
						player.getLevel().playSound(player, pos, soundType.getPlaceSound(), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
						
						anyBlockPlaced = true;
					}
					else if (!conditionAndCostPair.getB().isEmpty())
						missingCost.addGrist(conditionAndCostPair.getB());
				}
				
				if(anyBlockPlaced)
				{
					//broadcasts edit sound to other players.
					player.getLevel().playSound(player, positionEnd, MSSoundEvents.EVENT_EDIT_TOOL_REVISE.get(), SoundSource.AMBIENT, 1.0f, 1.0f);
					player.swing(hand);
				}
				
				if(!missingCost.isEmpty())
					player.sendSystemMessage(missingCost.createMissingMessage(), true);
				
				ServerEditHandler.removeCursorEntity(player, !anyBlockPlaced);
			}
		}
	}
	
	public record Destroy(boolean isDown, BlockPos positionStart, BlockPos positionEnd, Vec3 hitVector, Direction side) implements PlayToServerPacket
	{
		@Override
		public void encode(FriendlyByteBuf buffer)
		{
			buffer.writeBoolean(isDown);
			buffer.writeBlockPos(positionStart);
			buffer.writeBlockPos(positionEnd);
			buffer.writeDouble(hitVector.x);
			buffer.writeDouble(hitVector.y);
			buffer.writeDouble(hitVector.z);
			buffer.writeEnum(side);
		}
		
		public static Destroy decode(FriendlyByteBuf buffer)
		{
			boolean isDragging = buffer.readBoolean();
			BlockPos positionStart = buffer.readBlockPos();
			BlockPos positionEnd = buffer.readBlockPos();
			Vec3 hitVector = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
			Direction side = buffer.readEnum(Direction.class);
			
			return new Destroy(isDragging, positionStart, positionEnd, hitVector, side);
		}
		
		@Override
		public void execute(ServerPlayer player)
		{
			EditData data = ServerEditHandler.getData(player);
			if(!player.getLevel().isClientSide() && data != null)
			{
				IEditTools cap = player.getCapability(MSCapabilities.EDIT_TOOLS_CAPABILITY).orElseThrow(() -> LOGGER.throwing(new IllegalStateException("EditTool Capability is missing on player " + player.getDisplayName().getString() + " on server-side (during packet execution)!")));
				
				cap.setEditPos1(positionStart);
				cap.setEditPos2(positionEnd);
				cap.setEditTrace(hitVector, side);
				
				GristSet missingCost = new GristSet();
				boolean anyBlockDestroyed = false;
				for(BlockPos pos : BlockPos.betweenClosed(positionStart, positionEnd))
				{
					Tuple<Boolean, GristSet> conditionAndCostPair = editModeDestroyCheck(data, player, pos);
					if(conditionAndCostPair.getA())
					{
						player.gameMode.destroyAndAck(pos, 3, "creative destroy");
						
						//broadcasts block-break particles and sounds to other players.
						player.level.levelEvent(2001, pos, Block.getId(player.getLevel().getBlockState(pos)));
						player.level.gameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(player, player.getLevel().getBlockState(pos)));
						
						anyBlockDestroyed = true;
					}
					else if (!conditionAndCostPair.getB().isEmpty())
						missingCost.addGrist(conditionAndCostPair.getB());
				}
				
				if(anyBlockDestroyed)
				{
					//broadcasts edit sound to other players.
					player.getLevel().playSound(player, positionEnd, MSSoundEvents.EVENT_EDIT_TOOL_RECYCLE.get(), SoundSource.AMBIENT, 1.0f, 0.85f);
					player.swing(InteractionHand.MAIN_HAND);
				}
				
				if(!missingCost.isEmpty())
					player.sendSystemMessage(missingCost.createMissingMessage(), true);
				
				ServerEditHandler.removeCursorEntity(player, !anyBlockDestroyed);
			}
		}
	}
	
	public record Cursor(boolean isDown, BlockPos positionStart, BlockPos positionEnd) implements PlayToServerPacket
	{
		@Override
		public void encode(FriendlyByteBuf buffer)
		{
			buffer.writeBoolean(isDown);
			buffer.writeBlockPos(positionStart);
			buffer.writeBlockPos(positionEnd);
		}
		
		public static Cursor decode(FriendlyByteBuf buffer)
		{
			boolean isDragging = buffer.readBoolean();
			BlockPos positionStart = buffer.readBlockPos();
			BlockPos positionEnd = buffer.readBlockPos();
			return new Cursor(isDragging, positionStart, positionEnd);
		}
		
		@Override
		public void execute(ServerPlayer player)
		{
			if(!player.getLevel().isClientSide() && ServerEditHandler.getData(player) != null)
			{
				IEditTools cap = player.getCapability(MSCapabilities.EDIT_TOOLS_CAPABILITY).orElseThrow(() -> LOGGER.throwing(new IllegalStateException("EditTool Capability is missing on player " + player.getDisplayName().getString() + " on server-side (during packet execution)!")));
				
				cap.setEditPos1(positionStart);
				cap.setEditPos2(positionEnd);
				
				ServerEditHandler.updateEditToolsServer(player, isDown, positionStart, positionEnd);
			}
		}
	}
	
	public record Reset() implements PlayToServerPacket
	{
		@Override
		public void encode(FriendlyByteBuf buffer)
		{
		}
		
		public static Reset decode(FriendlyByteBuf buffer)
		{
			return new Reset();
		}
		
		@Override
		public void execute(ServerPlayer player)
		{
			if(!player.getLevel().isClientSide())
			{
				IEditTools cap = player.getCapability(MSCapabilities.EDIT_TOOLS_CAPABILITY).orElseThrow(() -> LOGGER.throwing(new IllegalStateException("EditTool Capability is missing on player " + player.getDisplayName().getString() + " on server-side (during packet execution)!")));
				
				ServerEditHandler.removeCursorEntity(player, true);
				cap.resetDragTools();
			}
		}
	}
}

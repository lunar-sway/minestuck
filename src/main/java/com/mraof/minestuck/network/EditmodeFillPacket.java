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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class EditmodeFillPacket implements PlayToServerPacket
{
	public enum UpdateMode {
		FILL, //0 Fills blocks in and removes cursor.
		DESTROY, //1 Recycles blocks and removes cursor.
		CURSOR, //2 Updates Cursor and capability.
		RESET //3 Removes cursor without modifying blocks.
	}
	
	final UpdateMode updateMode;
	final boolean isDown;
	final BlockPos positionStart;
	final BlockPos positionEnd;
	final Vec3 hitVector;
	final Direction side;
	
	public static EditmodeFillPacket Fill(boolean isDown, BlockPos positionStart, BlockPos positionEnd, Vec3 hitVector, Direction side) { return new EditmodeFillPacket(UpdateMode.FILL, isDown, positionStart, positionEnd, hitVector, side); }
	
	public static EditmodeFillPacket Destroy(boolean isDown, BlockPos positionStart, BlockPos positionEnd, Vec3 hitVector, Direction side) { return new EditmodeFillPacket(UpdateMode.DESTROY, isDown, positionStart, positionEnd, hitVector, side); }
	
	public static EditmodeFillPacket Cursor(boolean isDown, BlockPos positionStart, BlockPos positionEnd) { return new EditmodeFillPacket(UpdateMode.CURSOR, isDown, positionStart, positionEnd, null, null); }
	
	public static EditmodeFillPacket Reset() { return new EditmodeFillPacket(UpdateMode.RESET, false, null, null, null, null);}
	
	public EditmodeFillPacket(UpdateMode updateMode, boolean isDown, BlockPos positionStart, BlockPos positionEnd, Vec3 hitVector, Direction side)
	{
		this.updateMode = updateMode;
		this.isDown = isDown;
		this.positionStart = positionStart;
		this.positionEnd = positionEnd;
		this.hitVector = hitVector;
		this.side = side;
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeEnum(updateMode);
		if(updateMode == UpdateMode.FILL || updateMode == UpdateMode.DESTROY)
		{
			buffer.writeBoolean(isDown);
			buffer.writeInt(positionStart.getX());
			buffer.writeInt(positionStart.getY());
			buffer.writeInt(positionStart.getZ());
			buffer.writeInt(positionEnd.getX());
			buffer.writeInt(positionEnd.getY());
			buffer.writeInt(positionEnd.getZ());
			buffer.writeDouble(hitVector.x);
			buffer.writeDouble(hitVector.y);
			buffer.writeDouble(hitVector.z);
			buffer.writeEnum(side);
		}
		else if(updateMode == UpdateMode.CURSOR)
		{
			buffer.writeBoolean(isDown);
			buffer.writeInt(positionStart.getX());
			buffer.writeInt(positionStart.getY());
			buffer.writeInt(positionStart.getZ());
			buffer.writeInt(positionEnd.getX());
			buffer.writeInt(positionEnd.getY());
			buffer.writeInt(positionEnd.getZ());
		}
		else if(updateMode != UpdateMode.RESET)
			throw new IllegalStateException("UpdateMode in EditmodeFillPacket during encode() isn't set correctly! Probably null.");
			
	}
	
	public static EditmodeFillPacket decode(FriendlyByteBuf buffer)
	{
		UpdateMode updateMode = buffer.readEnum(UpdateMode.class);
		boolean isDragging = false;
		BlockPos positionStart = null;
		BlockPos positionEnd = null;
		Vec3 hitVector = new Vec3(0,0,0);
		Direction side = Direction.NORTH;
		if(updateMode == UpdateMode.FILL || updateMode == UpdateMode.DESTROY)
		{
			isDragging = buffer.readBoolean();
			positionStart = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
			positionEnd = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
			hitVector = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
			side = buffer.readEnum(Direction.class);
		}
		else if(updateMode == UpdateMode.CURSOR)
		{
			isDragging = buffer.readBoolean();
			positionStart = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
			positionEnd = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
		}
		else if(updateMode != UpdateMode.RESET)
			throw new IllegalStateException("UpdateMode in EditmodeFillPacket during decode() isn't set correctly! Probably null.");
		
		return new EditmodeFillPacket(updateMode, isDragging, positionStart, positionEnd, hitVector, side);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		IEditTools cap = player.getCapability(MSCapabilities.EDIT_TOOLS_CAPABILITY).orElseThrow(() -> new IllegalStateException("EditTool Capability is empty on player " + player.getDisplayName().toString() + " on server-side (during packet execution)!"));
		
		if(updateMode == UpdateMode.FILL)
		{
			cap.setEditDragging(isDown);
			cap.setEditPos1(positionStart);
			cap.setEditPos2(positionEnd);
			cap.setEditTrace(hitVector, side);
			
			InteractionHand hand = player.getMainHandItem().isEmpty() ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
			ItemStack stack = player.getItemInHand(hand);
			
			if(stack.isEmpty() || !(stack.getItem() instanceof BlockItem))
				return;
			
			float interactionPositionX = (float) (hitVector.x - (double) positionStart.getX());
			float interactionPositionY = (float) (hitVector.y - (double) positionStart.getY());
			float interactionPositionZ = (float) (hitVector.z - (double) positionStart.getZ());
			
			GristSet missingCost = new GristSet();
			boolean anyBlockPlaced = false;
			for(int x = Math.min(positionStart.getX(), positionEnd.getX()); x <= Math.max(positionStart.getX(), positionEnd.getX()); x++)
			{
				for(int y = Math.min(positionStart.getY(), positionEnd.getY()); y <= Math.max(positionStart.getY(), positionEnd.getY()); y++)
				{
					for(int z = Math.min(positionStart.getZ(), positionEnd.getZ()); z <= Math.max(positionStart.getZ(), positionEnd.getZ()); z++)
					{
						
						int c = stack.getCount();
						BlockPos pos = new BlockPos(x, y, z);
						if(player.getLevel().getBlockState(pos).getMaterial().isReplaceable() && editModePlaceCheck(player.getLevel(), player, hand) && stack.useOn(new UseOnContext(player, hand, new BlockHitResult(new Vec3(interactionPositionX, interactionPositionY, interactionPositionZ), side, pos, false))) != InteractionResult.FAIL)
						{
							//Check exists in-case we ever let non-editmode players use this tool for whatever reason.
							if(player.isCreative())
								stack.setCount(c);
							
							//broadcasts the block-place sounds to other players.
							SoundType soundType = ((BlockItem)stack.getItem()).getBlock().defaultBlockState().getSoundType();
							player.getLevel().playSound(player, pos, soundType.getPlaceSound(), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
							
							anyBlockPlaced = true;
						}
						else
							missingCost.addGrist(editModePlaceCost(player.getLevel(), player, hand, pos));
					}
				}
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
		else if(updateMode == UpdateMode.DESTROY)
		{
			cap.setEditDragging(isDown);
			cap.setEditPos1(positionStart);
			cap.setEditPos2(positionEnd);
			cap.setEditTrace(hitVector, side);
			
			GristSet missingCost = new GristSet();
			boolean anyBlockDestroyed = false;
			for(int x = Math.min(positionStart.getX(), positionEnd.getX()); x <= Math.max(positionStart.getX(), positionEnd.getX()); x++)
			{
				for(int y = Math.min(positionStart.getY(), positionEnd.getY()); y <= Math.max(positionStart.getY(), positionEnd.getY()); y++)
				{
					for(int z = Math.min(positionStart.getZ(), positionEnd.getZ()); z <= Math.max(positionStart.getZ(), positionEnd.getZ()); z++)
					{
						
						BlockPos pos = new BlockPos(x, y, z);
						
						if(editModeDestroyCheck(player.getLevel(), player, pos) && !player.getLevel().getBlockState(pos).isAir())
						{
							player.gameMode.destroyAndAck(pos, 3, "creative destroy");
							
							//broadcasts block-break particles and sounds to other players.
							player.level.levelEvent(2001, pos, Block.getId(player.getLevel().getBlockState(pos)));
							player.level.gameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(player, player.getLevel().getBlockState(pos)));
							
							anyBlockDestroyed = true;
						}
						else
							missingCost.addGrist(editModeDestroyCost(player.getLevel(), player, pos));
					}
				}
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
		else if(updateMode == UpdateMode.CURSOR)
		{
			cap.setEditDragging(isDown);
			cap.setEditPos1(positionStart);
			cap.setEditPos2(positionEnd);
			
			ServerEditHandler.updateEditToolsServer(player, isDown, positionStart, positionEnd);
		}
		else if(updateMode == UpdateMode.RESET)
		{
			ServerEditHandler.removeCursorEntity(player, true);
			cap.resetDragTools();
		}
		else
			throw new IllegalStateException("UpdateMode in EditmodeFillPacket isn't set correctly! Probably null.");
		
	}
	
	private static boolean editModePlaceCheck(Level level, Player player, InteractionHand hand)
	{
		if(!level.isClientSide() && ServerEditHandler.getData(player) != null)
		{
			EditData data = ServerEditHandler.getData(player);
			SburbConnection connection = data.getConnection();
			
			ItemStack stack = player.getMainHandItem();
			
			if(stack.isEmpty() || hand.equals(InteractionHand.OFF_HAND))
				return false;
			
			DeployEntry entry = DeployList.getEntryForItem(stack, connection, level);
			if(entry != null)
			{
				GristSet cost = entry.getCurrentCost(connection);
				if(!GristHelper.canAfford(PlayerSavedData.getData(connection.getClientIdentifier(), level).getGristCache(), cost))
					return false;
				
			}
			else if(!(stack.getItem() instanceof BlockItem) || !GristHelper.canAfford(level, connection.getClientIdentifier(), GristCost.findCostForItem(stack, null, false, level)))
				return false;
		}
		
		return true;
	}
	
	private static GristSet editModePlaceCost(Level level, Player player, InteractionHand hand, BlockPos pos)
	{
		EditData data = ServerEditHandler.getData(player);
		SburbConnection connection = data.getConnection();
		
		ItemStack stack = player.getMainHandItem();
		
		if(stack.isEmpty() || hand.equals(InteractionHand.OFF_HAND))
			return new GristSet();
		
		DeployEntry entry = DeployList.getEntryForItem(stack, connection, level);
		if(entry != null)
		{
			GristSet cost = entry.getCurrentCost(connection);
			if(level.getBlockState(pos).getMaterial().isReplaceable() && !GristHelper.canAfford(PlayerSavedData.getData(connection.getClientIdentifier(), level).getGristCache(), cost))
				return cost;
			
		}
		else if(level.getBlockState(pos).getMaterial().isReplaceable() && !GristHelper.canAfford(level, connection.getClientIdentifier(), GristCost.findCostForItem(stack, null, false, level)))
			return GristCost.findCostForItem(stack, null, false, level);
		
		return new GristSet();
	}
	
	private static boolean editModeDestroyCheck(Level level, Player player, BlockPos pos)
	{
		if(!level.isClientSide() && ServerEditHandler.getData(player) != null)
		{
			EditData data = ServerEditHandler.getData(player);
			SburbConnection connection = data.getConnection();
			
			BlockState block = level.getBlockState(pos);
			ItemStack stack = block.getCloneItemStack(null, level, pos, player);
			DeployEntry entry = DeployList.getEntryForItem(stack, data.getConnection(), level, DeployList.EntryLists.ATHENEUM);
			if(!MinestuckConfig.SERVER.gristRefund.get() && entry == null)
			{
				
				GristSet cost = new GristSet(GristTypes.BUILD,1);
				if(!GristHelper.canAfford(PlayerSavedData.getData(connection.getClientIdentifier(), level).getGristCache(), cost))
					return false;
				
			}
		}
		return true;
	}
	
	private static GristSet editModeDestroyCost(Level level, Player player, BlockPos pos)
	{
		EditData data = ServerEditHandler.getData(player);
		SburbConnection connection = data.getConnection();
		
		BlockState block = level.getBlockState(pos);
		ItemStack stack = block.getCloneItemStack(null, level, pos, player);
		DeployEntry entry = DeployList.getEntryForItem(stack, data.getConnection(), level, DeployList.EntryLists.ATHENEUM);
		if(!MinestuckConfig.SERVER.gristRefund.get() && entry == null)
		{
			
			GristSet cost = new GristSet(GristTypes.BUILD,1);
			if(!block.isAir() && !GristHelper.canAfford(PlayerSavedData.getData(connection.getClientIdentifier(), level).getGristCache(), cost))
				return cost;
			
		}
		else if (block.isAir() && (MinestuckConfig.SERVER.gristRefund.get() || entry.getCategory() == DeployList.EntryLists.ATHENEUM))
			return entry.getCurrentCost(connection).scale(-1);
	
		return new GristSet();
	}
}

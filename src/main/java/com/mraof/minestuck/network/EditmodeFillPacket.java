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

import static com.mraof.minestuck.computer.editmode.ServerEditHandler.isBlockItem;

public class EditmodeFillPacket implements PlayToServerPacket
{
	final boolean fill;
	final boolean isDown;
	final BlockPos positionStart;
	final BlockPos positionEnd;
	final Vec3 hitVector;
	final Direction side;
	
	public EditmodeFillPacket(boolean fill, boolean isDown, BlockPos positionStart, BlockPos positionEnd, Vec3 hitVector, Direction side)
	{
		this.fill = fill;
		this.isDown = isDown;
		this.positionStart = positionStart;
		this.positionEnd = positionEnd;
		this.hitVector = hitVector;
		this.side = side;
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeBoolean(fill);
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
	
	public static EditmodeFillPacket decode(FriendlyByteBuf buffer)
	{
		boolean fill = buffer.readBoolean();
		boolean isDragging = buffer.readBoolean();
		BlockPos positionStart = new BlockPos(buffer.readInt(),buffer.readInt(),buffer.readInt());
		BlockPos positionEnd = new BlockPos(buffer.readInt(),buffer.readInt(),buffer.readInt());
		Vec3 hitVector = new Vec3(buffer.readDouble(),buffer.readDouble(),buffer.readDouble());
		Direction side = buffer.readEnum(Direction.class);
		
		return new EditmodeFillPacket(fill, isDragging, positionStart, positionEnd, hitVector, side);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		IEditTools cap = player.getCapability(MSCapabilities.EDIT_TOOLS_CAPABILITY).orElse(new EditTools());
		cap.setEditDragging(isDown);
		cap.setEditPos1(positionStart);
		cap.setEditPos2(positionEnd);
		cap.setEditTrace(hitVector, side);
		
		if(!isDown)
		{
			InteractionHand hand = player.getMainHandItem().getItem() instanceof BlockItem ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
			ItemStack stack = fill ? player.getItemInHand(hand) : ItemStack.EMPTY;
			
			if(fill && !(stack.getItem() instanceof BlockItem))
				return;
			
			float f = (float) (hitVector.x - (double) positionStart.getX());
			float f1 = (float) (hitVector.y - (double) positionStart.getY());
			float f2 = (float) (hitVector.z - (double) positionStart.getZ());
			
			
			boolean swingArm = false;
			for(int x = Math.min(positionStart.getX(), positionEnd.getX()); x <= Math.max(positionStart.getX(), positionEnd.getX()); x++)
			{
				for(int y = Math.min(positionStart.getY(), positionEnd.getY()); y <= Math.max(positionStart.getY(), positionEnd.getY()); y++)
				{
					for(int z = Math.min(positionStart.getZ(), positionEnd.getZ()); z <= Math.max(positionStart.getZ(), positionEnd.getZ()); z++)
					{
						if(fill && stack.isEmpty())
							return;
						
						
						int c = stack.getCount();
						BlockPos pos = new BlockPos(x, y, z);
						if(fill)
						{
							if(player.getLevel().getBlockState(pos).getMaterial().isReplaceable() && editModePlaceCheck(player.getLevel(), player, hand) && stack.useOn(new UseOnContext(player, hand, new BlockHitResult(new Vec3(f, f1, f2), side, pos, false))) == InteractionResult.SUCCESS)
							{
								if(player.isCreative())
									stack.setCount(c);
								
								//broadcasts block-place sounds to other players.
								SoundType soundType = ((BlockItem)stack.getItem()).getBlock().defaultBlockState().getSoundType();
								player.getLevel().playSound(player, pos, soundType.getPlaceSound(), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
								
								swingArm = true;
							}
						} else
						{
							if(editModeDestroyCheck(player.getLevel(), player, pos) && !player.getLevel().getBlockState(pos).isAir())
							{
								player.gameMode.destroyAndAck(pos, 3, "creative destroy");
								
								//broadcasts block-break particles and sounds to other players.
								player.level.levelEvent(2001, pos, Block.getId(player.getLevel().getBlockState(pos)));
								player.level.gameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(player, player.getLevel().getBlockState(pos)));
								swingArm = true;
							}
						}
					}
				}
			}
			if(swingArm)
			{
				//broadcasts edit sound to other players.
				player.getLevel().playSound(player, positionEnd, fill ? MSSoundEvents.EVENT_EDIT_TOOL_REVISE.get() : MSSoundEvents.EVENT_EDIT_TOOL_RECYCLE.get(), SoundSource.AMBIENT, 1.0f, 1.0f);
				
				player.swing(hand);
			}
			
			ServerEditHandler.removeCursorEntity(player);
		}
		else
		{
			ServerEditHandler.updateEditToolsServer(player, isDown, positionStart, positionEnd);
		}
	}
	
	private static boolean editModePlaceCheck(Level level, Player player, InteractionHand hand)
	{
		if(!level.isClientSide() && ServerEditHandler.getData(player) != null)
		{
			EditData data = ServerEditHandler.getData(player);
			SburbConnection connection = data.getConnection();
			
			ItemStack stack = player.getMainHandItem();
			
			if(stack.isEmpty() || !isBlockItem(stack.getItem()) || hand.equals(InteractionHand.OFF_HAND))
				return false;
			
			DeployEntry entry = DeployList.getEntryForItem(stack, connection, level);
			if(entry != null)
			{
				GristSet cost = entry.getCurrentCost(connection);
				if(!GristHelper.canAfford(PlayerSavedData.getData(connection.getClientIdentifier(), level).getGristCache(), cost))
				{
					if(cost != null)
					{
						player.sendSystemMessage(cost.createMissingMessage());
					}
					return false;
				}
			}
			else if(!(stack.getItem() instanceof BlockItem) || !GristHelper.canAfford(level, connection.getClientIdentifier(), GristCost.findCostForItem(stack, null, false, level)))
				return false;
		}
		return true;
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
				{
					if(cost != null)
					{
						player.sendSystemMessage(cost.createMissingMessage());
					}
					return false;
				}
			}
		}
		return true;
	}
}

package com.mraof.minestuck.network;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.alchemy.*;
import com.mraof.minestuck.computer.editmode.*;
import com.mraof.minestuck.player.PlayerSavedData;
import com.mraof.minestuck.skaianet.SburbConnection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.EventBus;

import static com.mraof.minestuck.computer.editmode.ServerEditHandler.isBlockItem;

public class EditmodeFillPacket implements PlayToServerPacket
{
	final boolean fill;
	final boolean isDragging;
	final BlockPos positionStart;
	final BlockPos positionEnd;
	final Vec3 hitVector;
	final Direction side;
	
	public EditmodeFillPacket(boolean fill, boolean isDragging, BlockPos positionStart, BlockPos positionEnd, Vec3 hitVector, Direction side)
	{
		this.fill = fill;
		this.isDragging = isDragging;
		this.positionStart = positionStart;
		this.positionEnd = positionEnd;
		this.hitVector = hitVector;
		this.side = side;
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeBoolean(fill);
		buffer.writeBoolean(isDragging);
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
		if(!isDragging)
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
								swingArm = true;
							}
						} else
						{
							if(editModeDestroyCheck(player.getLevel(), player) && !player.getLevel().getBlockState(pos).isAir())
							{
								PlayerInteractEvent.LeftClickBlock evt = new PlayerInteractEvent.LeftClickBlock(player, pos, side);
								ServerEditHandler.onLeftClickBlockControl(evt);
								if(!evt.isCanceled())
								{
									ServerEditHandler.onBlockBreak(evt);
									player.swinging = false;
									player.getLevel().destroyBlock(pos, false, player);
								}
								//swingArm = true;
							}
						}
					}
				}
			}
			if(swingArm)
				player.swing(hand);
			
			EditToolDrag.removeCursorEntity(player);
		}
		else
		{
			EditToolDrag.updateEditToolsServer(player, isDragging, positionStart, positionEnd);
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
	
	private static boolean editModeDestroyCheck(Level level, Player player)
	{
		if(!level.isClientSide() && ServerEditHandler.getData(player) != null)
		{
			EditData data = ServerEditHandler.getData(player);
			SburbConnection connection = data.getConnection();
			
			if(!MinestuckConfig.SERVER.gristRefund.get())
			{
				
				GristSet cost = new GristSet(GristTypes.BUILD,1);
				if(!GristHelper.canAfford(PlayerSavedData.getData(connection.getClientIdentifier(), level).getGristCache(), cost))
				{
					StringBuilder str = new StringBuilder();
					if(cost != null)
					{
						for(GristAmount grist : cost.getAmounts())
						{
							if(cost.getAmounts().indexOf(grist) != 0)
								str.append(", ");
							str.append(grist.getAmount()+" "+grist.getType().getDisplayName());
						}
						player.sendSystemMessage(Component.translatable("grist.missing",str.toString()));
					}
					return false;
				}
			}
		}
		return true;
	}
}

package com.mraof.minestuck.network;

import com.mraof.minestuck.alchemy.GristAmount;
import com.mraof.minestuck.alchemy.GristCost;
import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.computer.editmode.DeployEntry;
import com.mraof.minestuck.computer.editmode.DeployList;
import com.mraof.minestuck.computer.editmode.EditData;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.player.PlayerSavedData;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
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
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import static com.mraof.minestuck.computer.editmode.ServerEditHandler.isBlockItem;

public class EditmodeFillPacket implements PlayToServerPacket
{
	final BlockPos positionStart;
	final BlockPos positionEnd;
	final Vec3 hitVector;
	final Direction side;
	
	public EditmodeFillPacket(BlockPos positionStart, BlockPos positionEnd, Vec3 hitVector, Direction side)
	{
		this.positionStart = positionStart;
		this.positionEnd = positionEnd;
		this.hitVector = hitVector;
		this.side = side;
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
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
		BlockPos positionStart = new BlockPos(buffer.readInt(),buffer.readInt(),buffer.readInt());
		BlockPos positionEnd = new BlockPos(buffer.readInt(),buffer.readInt(),buffer.readInt());
		Vec3 hitVector = new Vec3(buffer.readDouble(),buffer.readDouble(),buffer.readDouble());
		Direction side = buffer.readEnum(Direction.class);
		
		return new EditmodeFillPacket(positionStart,positionEnd,hitVector,side);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		InteractionHand hand = player.getMainHandItem().getItem() instanceof BlockItem ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
		ItemStack stack = player.getItemInHand(hand);
		
		if(!(stack.getItem() instanceof BlockItem))
			return;
		
		float f = (float)(hitVector.x - (double)positionStart.getX());
		float f1 = (float)(hitVector.y - (double)positionStart.getY());
		float f2 = (float)(hitVector.z - (double)positionStart.getZ());
		
		
		
		boolean swingArm = false;
		for (int x = Math.min(positionStart.getX(), positionEnd.getX()); x <= Math.max(positionStart.getX(), positionEnd.getX()); x++)
			for (int y = Math.min(positionStart.getY(), positionEnd.getY()); y <= Math.max(positionStart.getY(), positionEnd.getY()); y++)
				for (int z = Math.min(positionStart.getZ(), positionEnd.getZ()); z <= Math.max(positionStart.getZ(), positionEnd.getZ()); z++)
				{
					if(stack.isEmpty())
						return;
					
					
					
					int c = stack.getCount();
					BlockPos pos = new BlockPos(x, y, z);
					if(player.getLevel().getBlockState(pos).getMaterial().isReplaceable() && editModePlaceCheck(player.getLevel(), player, hand) && stack.onItemUseFirst(new UseOnContext(player, hand, new BlockHitResult(new Vec3(f, f1, f2), side, pos, true))) == InteractionResult.SUCCESS)
					{
						if(player.isCreative())
							stack.setCount(c);
						swingArm = true;
					}
				}
		
		if(swingArm)
			player.swing(hand);
	}
	
	private static boolean editModePlaceCheck(Level level, Player player, InteractionHand hand)
	{
		if(level.isClientSide() && ServerEditHandler.getData(player) != null)
		{
			EditData data = ServerEditHandler.getData(player);
			SburbConnection connection = ObfuscationReflectionHelper.getPrivateValue(EditData.class, data, "connection");
			
			ItemStack stack = player.getMainHandItem();
			
			if(stack.isEmpty() || !isBlockItem(stack.getItem()) || hand.equals(InteractionHand.OFF_HAND))
				return false;
			
			DeployEntry entry = DeployList.getEntryForItem(stack, connection, level);
			if(entry != null)
			{
				GristSet cost = entry.getCurrentCost(connection);
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
						player.sendMessage(new TranslatableComponent("grist.missing",str.toString()), null);
					}
					return false;
				}
			}
			else if(!isBlockItem(stack.getItem()) || !GristHelper.canAfford(level, connection.getClientIdentifier(), GristCost.findCostForItem(stack, null, false, level)))
				return false;
		}
		return true;
	}
}

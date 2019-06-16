package com.mraof.minestuck.network;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.editmode.DeployList;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.IdentifierHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.management.UserListOpsEntry;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientEditPacket
{
	
	int user = -1;
	int target;
	
	public ClientEditPacket exit()
	{
		return new ClientEditPacket();
	}
	
	public ClientEditPacket activate(int user, int target)
	{
		ClientEditPacket packet = new ClientEditPacket();
		packet.user = user;
		packet.target = target;
		return packet;
	}
	
	public void encode(PacketBuffer buffer)
	{
		if(user != -1)
		{
			buffer.writeInt(user);
			buffer.writeInt(target);
		}
	}
	
	public static ClientEditPacket decode(PacketBuffer buffer)
	{
		ClientEditPacket packet = new ClientEditPacket();
		if(buffer.readableBytes() > 0)
		{
			packet.user = buffer.readInt();
			packet.target = buffer.readInt();
		}
		
		return packet;
	}
	
	public void consume(Supplier<NetworkEvent.Context> ctx)
	{
		if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER)
			ctx.get().enqueueWork(() -> this.execute(ctx.get().getSender()));
		
		ctx.get().setPacketHandled(true);
	}
	
	public void execute(EntityPlayerMP player)
	{
		UserListOpsEntry opsEntry = player == null ? null : player.getServer().getPlayerList().getOppedPlayers().getEntry(player.getGameProfile());
		if(!MinestuckConfig.giveItems)
		{
			if(user == -1)
				ServerEditHandler.onPlayerExit(player);
			else if(!MinestuckConfig.privateComputers || IdentifierHandler.encode(player).getId() == this.user || opsEntry != null && opsEntry.getPermissionLevel() >= 2)
			{
				IdentifierHandler.PlayerIdentifier user = IdentifierHandler.getById(this.user);
				IdentifierHandler.PlayerIdentifier target = IdentifierHandler.getById(this.target);
				if(user != null && target != null)
					ServerEditHandler.newServerEditor(player, user, target);
			}
			return;
		}
		
		IdentifierHandler.PlayerIdentifier user = IdentifierHandler.getById(this.user);
		IdentifierHandler.PlayerIdentifier target = IdentifierHandler.getById(this.target);
		if(user != null && target != null)
		{
			EntityPlayerMP targetPlayer = target.getPlayer();
			
			if(targetPlayer != null && (!MinestuckConfig.privateComputers || user.appliesTo(player) || opsEntry != null && opsEntry.getPermissionLevel() >= 2))
			{
				SburbConnection c = SkaianetHandler.getClientConnection(target);
				if(c == null || c.getServerIdentifier() != user || !(c.isMain() || SkaianetHandler.giveItems(target)))
					return;
				
				for(DeployList.DeployEntry entry : DeployList.getItemList(c))
				{
					if(!c.givenItems()[entry.getOrdinal()])
					{
						ItemStack item = entry.getItemStack(c);
						if(!targetPlayer.inventory.hasItemStack(item))
							c.givenItems()[entry.getOrdinal()] = targetPlayer.inventory.addItemStackToInventory(item);
					}
				}
				player.getServer().getPlayerList().sendInventory(targetPlayer);
			}
		}
	}
}
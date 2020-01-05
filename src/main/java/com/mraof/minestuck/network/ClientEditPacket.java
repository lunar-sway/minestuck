package com.mraof.minestuck.network;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.editmode.DeployList;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.IdentifierHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.management.OpEntry;

public class ClientEditPacket implements PlayToServerPacket
{
	
	int user = -1;
	int target;
	
	public static ClientEditPacket exit()
	{
		return new ClientEditPacket();
	}
	
	public static ClientEditPacket activate(int user, int target)
	{
		ClientEditPacket packet = new ClientEditPacket();
		packet.user = user;
		packet.target = target;
		return packet;
	}
	
	@Override
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
	
	@Override
	public void execute(ServerPlayerEntity player)
	{
		OpEntry opsEntry = player == null ? null : player.getServer().getPlayerList().getOppedPlayers().getEntry(player.getGameProfile());
		if(!MinestuckConfig.giveItems.get())
		{
			if(user == -1)
				ServerEditHandler.onPlayerExit(player);
			else if(!MinestuckConfig.privateComputers.get() || IdentifierHandler.encode(player).getId() == this.user || opsEntry != null && opsEntry.getPermissionLevel() >= 2)
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
			ServerPlayerEntity targetPlayer = target.getPlayer(player.getServer());
			
			if(targetPlayer != null && (!MinestuckConfig.privateComputers.get() || user.appliesTo(player) || opsEntry != null && opsEntry.getPermissionLevel() >= 2))
			{
				SburbConnection c = SkaianetHandler.get(player.world).getActiveConnection(target);
				if(c == null || c.getServerIdentifier() != user || !(c.isMain() || SkaianetHandler.get(player.world).giveItems(target)))
					return;
				
				for(DeployList.DeployEntry entry : DeployList.getItemList(player.getServer(), c))
				{
					if(!c.givenItems()[entry.getOrdinal()])
					{
						ItemStack item = entry.getItemStack(c, player.world);
						if(!targetPlayer.inventory.hasItemStack(item))
							c.givenItems()[entry.getOrdinal()] = targetPlayer.inventory.addItemStackToInventory(item);
					}
				}
				player.getServer().getPlayerList().sendInventory(targetPlayer);
			}
		}
	}
}
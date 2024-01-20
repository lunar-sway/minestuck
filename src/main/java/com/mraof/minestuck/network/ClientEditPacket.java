package com.mraof.minestuck.network;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.computer.editmode.DeployEntry;
import com.mraof.minestuck.computer.editmode.DeployList;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.skaianet.ActiveConnection;
import com.mraof.minestuck.skaianet.SburbHandler;
import com.mraof.minestuck.skaianet.SburbPlayerData;
import com.mraof.minestuck.skaianet.SkaianetData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.ServerOpListEntry;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class ClientEditPacket implements MSPacket.PlayToServer
{
	private final int user;
	private final int target;
	
	private ClientEditPacket(int user, int target)
	{
		this.user = user;
		this.target = target;
	}
	
	public static ClientEditPacket exit()
	{
		return new ClientEditPacket(-1, 0);
	}
	
	public static ClientEditPacket activate(int user, int target)
	{
		return new ClientEditPacket(user, target);
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		if(user != -1)
		{
			buffer.writeInt(user);
			buffer.writeInt(target);
		}
	}
	
	public static ClientEditPacket decode(FriendlyByteBuf buffer)
	{
		if(buffer.readableBytes() > 0)
		{
			int user = buffer.readInt();
			int target = buffer.readInt();
			return activate(user, target);
		}
		
		return exit();
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		if(player == null || player.getServer() == null)
			return;
		ServerOpListEntry opsEntry = player.getServer().getPlayerList().getOps().get(player.getGameProfile());
		if(!MinestuckConfig.SERVER.giveItems.get())
		{
			if(user == -1)
				ServerEditHandler.onPlayerExit(player);
			else if(!MinestuckConfig.SERVER.privateComputers.get() || IdentifierHandler.encode(player).getId() == this.user || opsEntry != null && opsEntry.getLevel() >= 2)
			{
				PlayerIdentifier user = IdentifierHandler.getById(this.user);
				PlayerIdentifier target = IdentifierHandler.getById(this.target);
				if(user != null && target != null)
					ServerEditHandler.newServerEditor(player, user, target);
			}
			return;
		}
		
		PlayerIdentifier user = IdentifierHandler.getById(this.user);
		PlayerIdentifier target = IdentifierHandler.getById(this.target);
		if(user != null && target != null)
		{
			ServerPlayer targetPlayer = target.getPlayer(player.getServer());
			
			if(targetPlayer != null && (!MinestuckConfig.SERVER.privateComputers.get() || user.appliesTo(player) || opsEntry != null && opsEntry.getLevel() >= 2))
			{
				Optional<ActiveConnection> c = SkaianetData.get(player.level()).getActiveConnection(target);
				if(c.isEmpty() || c.get().server() != user)
					return;
				
				SburbHandler.onEntryItemsDeployed(player.server, target);
				
				SburbPlayerData targetData = SburbPlayerData.get(target, player.server);
				for(DeployEntry entry : DeployList.getItemList(player.server, targetData, DeployList.EntryLists.DEPLOY))
				{
					if(!targetData.hasGivenItem(entry))
					{
						ItemStack item = entry.getItemStack(targetData, player.level());
						if(!targetPlayer.getInventory().contains(item) && targetPlayer.getInventory().add(item))
							targetData.setHasGivenItem(entry);
					}
				}
				player.getServer().getPlayerList().sendAllPlayerInfo(targetPlayer);
			}
		}
	}
}
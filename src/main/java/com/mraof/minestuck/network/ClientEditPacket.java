package com.mraof.minestuck.network;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.computer.editmode.DeployEntry;
import com.mraof.minestuck.computer.editmode.DeployList;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.SburbHandler;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.management.OpEntry;

public class ClientEditPacket implements PlayToServerPacket
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
		if(buffer.readableBytes() > 0)
		{
			int user = buffer.readInt();
			int target = buffer.readInt();
			return activate(user, target);
		}
		
		return exit();
	}
	
	@Override
	public void execute(ServerPlayerEntity player)
	{
		if(player == null || player.getServer() == null)
			return;
		OpEntry opsEntry = player.getServer().getPlayerList().getOps().get(player.getGameProfile());
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
			ServerPlayerEntity targetPlayer = target.getPlayer(player.getServer());
			
			if(targetPlayer != null && (!MinestuckConfig.SERVER.privateComputers.get() || user.appliesTo(player) || opsEntry != null && opsEntry.getLevel() >= 2))
			{
				SburbConnection c = SkaianetHandler.get(player.level).getActiveConnection(target);
				if(c == null || c.getServerIdentifier() != user || !(c.isMain() || SburbHandler.giveItems(player.server, target)))
					return;
				
				for(DeployEntry entry : DeployList.getItemList(player.getServer(), c))
				{
					if(!c.hasGivenItem(entry))
					{
						ItemStack item = entry.getItemStack(c, player.level);
						if(!targetPlayer.inventory.contains(item) && targetPlayer.inventory.add(item))
							c.setHasGivenItem(entry);
					}
				}
				player.getServer().getPlayerList().sendAllPlayerInfo(targetPlayer);
			}
		}
	}
}
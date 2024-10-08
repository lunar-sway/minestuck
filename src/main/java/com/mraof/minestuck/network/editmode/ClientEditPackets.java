package com.mraof.minestuck.network.editmode;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.computer.editmode.DeployEntry;
import com.mraof.minestuck.computer.editmode.DeployList;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.network.MSPacket;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.skaianet.ActiveConnection;
import com.mraof.minestuck.skaianet.SburbConnections;
import com.mraof.minestuck.skaianet.SburbHandler;
import com.mraof.minestuck.skaianet.SburbPlayerData;
import net.minecraft.commands.Commands;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public final class ClientEditPackets
{
	public record Exit() implements MSPacket.PlayToServer
	{
		public static final ResourceLocation ID = Minestuck.id("client_edit/exit");
		
		@Override
		public ResourceLocation id()
		{
			return ID;
		}
		
		@Override
		public void write(FriendlyByteBuf buffer)
		{
		}
		
		public static Exit read(FriendlyByteBuf ignored)
		{
			return new Exit();
		}
		
		@Override
		public void execute(ServerPlayer player)
		{
			ServerEditHandler.onPlayerExit(player);
		}
	}
	
	//todo should reference a computer and get data from there instead
	public record Activate(int userId, int targetId) implements MSPacket.PlayToServer
	{
		public static final ResourceLocation ID = Minestuck.id("client_edit/activate");
		
		@Override
		public ResourceLocation id()
		{
			return ID;
		}
		
		@Override
		public void write(FriendlyByteBuf buffer)
		{
			buffer.writeInt(this.userId);
			buffer.writeInt(this.targetId);
		}
		
		public static Activate read(FriendlyByteBuf buffer)
		{
			int userId = buffer.readInt();
			int targetId = buffer.readInt();
			return new Activate(userId, targetId);
		}
		
		@Override
		public void execute(ServerPlayer player)
		{
			PlayerIdentifier user = IdentifierHandler.getById(this.userId);
			PlayerIdentifier target = IdentifierHandler.getById(this.targetId);
			if(user == null || target == null)
				return;
			
			if(isMissingPermission(player, user))
				return;
			
			if(MinestuckConfig.SERVER.giveItems.get())
				runGiveItems(player, user, target);
			else
				ServerEditHandler.newServerEditor(player, user, target);
		}
		
		private static boolean isMissingPermission(ServerPlayer player, PlayerIdentifier user)
		{
			return MinestuckConfig.SERVER.privateComputers.get() && !user.appliesTo(player) && !player.hasPermissions(Commands.LEVEL_GAMEMASTERS);
		}
		
		private void runGiveItems(ServerPlayer player, PlayerIdentifier user, PlayerIdentifier target)
		{
			ServerPlayer targetPlayer = target.getPlayer(player.getServer());
			
			if(targetPlayer == null)
				return;
			
			Optional<ActiveConnection> c = SburbConnections.get(player.server).getCheckedActiveConnection(target);
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

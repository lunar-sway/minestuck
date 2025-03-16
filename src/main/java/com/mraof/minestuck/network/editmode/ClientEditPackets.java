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
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Optional;

public final class ClientEditPackets
{
	public record Exit() implements MSPacket.PlayToServer
	{
		
		public static final Type<ClientEditPackets.Exit> ID = new Type<>(Minestuck.id("client_edit/exit"));
		public static final StreamCodec<FriendlyByteBuf, ClientEditPackets.Exit> STREAM_CODEC = StreamCodec.unit(new Exit());
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context, ServerPlayer player)
		{
			ServerEditHandler.onPlayerExit(player);
		}
	}
	
	//TODO should reference a computer and get data from there instead
	public record Activate(int userId, int targetId) implements MSPacket.PlayToServer
	{
		public static final Type<Activate> ID = new Type<>(Minestuck.id("client_edit/activate"));
		public static final StreamCodec<FriendlyByteBuf, Activate> STREAM_CODEC = StreamCodec.composite(
				ByteBufCodecs.INT,
				Activate::userId,
				ByteBufCodecs.INT,
				Activate::targetId,
				Activate::new
		);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		
		@Override
		public void execute(IPayloadContext context, ServerPlayer player)
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

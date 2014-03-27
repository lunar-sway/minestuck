package com.mraof.minestuck.tracker;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

//import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.GristHelper;
import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.GristStorage;
import com.mraof.minestuck.util.GristType;
import com.mraof.minestuck.util.Title;
import com.mraof.minestuck.util.TitleHelper;
import com.mraof.minestuck.util.UpdateChecker;
import com.mraof.minestuck.util.UsernameHandler;
import com.mraof.minestuck.world.storage.MinestuckSaveHandler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

public class MinestuckPlayerTracker {
	
	public static MinestuckPlayerTracker instance = new MinestuckPlayerTracker();
	
	@SubscribeEvent
	public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) 
	{
		EntityPlayer player = event.player;
		Debug.print(player.getCommandSenderName()+" joined the game. Sending packets.");
		MinecraftServer server = MinecraftServer.getServer();
		if(!server.isDedicatedServer() && UsernameHandler.host == null)
			UsernameHandler.host = event.player.getCommandSenderName();
		
		sendConfigPacket(player);
		
		SkaianetHandler.playerConnected(player.getCommandSenderName());
		
		if(GristStorage.getGristSet(UsernameHandler.encode(player.getCommandSenderName())) == null) {
			Debug.printf("Grist set is null for player %s.", player.getCommandSenderName());
			if(player.getEntityData().hasKey("Grist")) {	//Load old grist format
				NBTTagCompound nbt = player.getEntityData().getCompoundTag("Grist");
				GristSet set = new GristSet();
				for(GristType type : GristType.values())
					set.addGrist(type, nbt.getInteger(type.getName()));
				if(set.isEmpty())
					set.addGrist(GristType.Build, 20);

				GristStorage.setGrist(UsernameHandler.encode(player.getCommandSenderName()), set);
				player.getEntityData().removeTag("Grist");
			} else GristStorage.setGrist(UsernameHandler.encode(player.getCommandSenderName()), new GristSet(GristType.Build, 20));
		}

		updateGristCache(UsernameHandler.encode(player.getCommandSenderName()));
		updateTitle(player);
		updateLands(player);
		if(UpdateChecker.outOfDate)
			player.addChatMessage(new ChatComponentText("New version of Minestuck: " + UpdateChecker.latestVersion + "\nChanges: " + UpdateChecker.updateChanges));
	}

	@SubscribeEvent
	public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
		
//		ServerEditHandler.onPlayerExit(event.player);
	}
	
	/**
	 * Uses an "encoded" username as parameter.
	 */
	public static void updateGristCache(String player) {

		int[] gristValues = new int[GristType.allGrists];
		for(int typeInt = 0; typeInt < gristValues.length; typeInt++)
			gristValues[typeInt] = GristHelper.getGrist(player,GristType.values()[typeInt]);

		//The player
		if(!player.equals(".client") || UsernameHandler.host != null) {
			EntityPlayerMP playerMP = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(UsernameHandler.decode(player));
			if(playerMP != null) {
				MinestuckPacket packet = MinestuckPacket.makePacket(Type.GRISTCACHE, gristValues, false);
				MinestuckChannelHandler.sendToPlayer(packet, playerMP);
			}
		}

		//The editing player, if there is any.
		/*SburbConnection c = */SkaianetHandler.getClientConnection(player);
//		if(c != null && ServerEditHandler.getData(c) != null) {
//			EntityPlayerMP editor = ServerEditHandler.getData(c).getEditor();
//			MinestuckPacket packet = MinestuckPacket.makePacket(Type.GRISTCACHE, gristValues, true);
//			MinestuckChannelHandler.sendToPlayer(packet, editor);
//		}
	}

	public void updateTitle(EntityPlayer player) {
		Title newTitle;
		if (!player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).hasKey("Class") || !player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).hasKey("Aspect")) {
			newTitle = TitleHelper.randomTitle();
		} else {
			newTitle = new Title(TitleHelper.getClassFromInt(((EntityPlayer)player).getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getInteger("Class")),
					TitleHelper.getAspectFromInt(((EntityPlayer)player).getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getInteger("Aspect")));
		}
		MinestuckPacket packet = MinestuckPacket.makePacket(Type.TITLE, newTitle.getHeroClass(), newTitle.getHeroAspect());
		MinestuckChannelHandler.sendToPlayer(packet, player);
		if(player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).hasNoTags())
			player.getEntityData().setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
		player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).setInteger("Class", TitleHelper.getIntFromClass(newTitle.getHeroClass()));
		player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).setInteger("Aspect", TitleHelper.getIntFromAspect(newTitle.getHeroAspect()));
	}
	public static void updateLands(EntityPlayer player)
	{
		byte[] lands = new byte[MinestuckSaveHandler.lands.size()];
		for(int i = 0; i < lands.length; i++)
			lands[i] = MinestuckSaveHandler.lands.get(i);
		
		MinestuckPacket packet = MinestuckPacket.makePacket(Type.LANDREGISTER, lands);
		Debug.printf("Sending land packets to %s.", player == null ? "all players" : player.getCommandSenderName());
		if(player == null)
			MinestuckChannelHandler.sendToAllPlayers(packet);
		else
			MinestuckChannelHandler.sendToPlayer(packet, player);
	}
	public static void updateLands()
	{
		updateLands(null);
	}

	public static void sendConfigPacket(EntityPlayer player) {
		MinestuckPacket packet = MinestuckPacket.makePacket(Type.CONFIG);
		MinestuckChannelHandler.sendToPlayer(packet, player);
	}

}

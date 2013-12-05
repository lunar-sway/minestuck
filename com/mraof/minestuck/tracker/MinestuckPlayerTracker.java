package com.mraof.minestuck.tracker;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;

import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.GristHelper;
import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.GristStorage;
import com.mraof.minestuck.util.GristType;
import com.mraof.minestuck.util.ServerEditHandler;
import com.mraof.minestuck.util.Title;
import com.mraof.minestuck.util.TitleHelper;
import com.mraof.minestuck.util.UpdateChecker;
import com.mraof.minestuck.util.UsernameHandler;
import com.mraof.minestuck.world.storage.MinestuckSaveHandler;

import cpw.mods.fml.common.IPlayerTracker;
import cpw.mods.fml.common.network.PacketDispatcher;

public class MinestuckPlayerTracker implements IPlayerTracker {
	
	@Override
	public void onPlayerLogin(EntityPlayer player) 
	{
		MinecraftServer server = MinecraftServer.getServer();
		if(!server.isDedicatedServer() && server.getConfigurationManager().playerEntityList.size() == 1)
			UsernameHandler.host = ((EntityPlayer)server.getConfigurationManager().playerEntityList.get(0)).username;
		
		if(GristStorage.getGristSet(UsernameHandler.encode(player.username)) == null) {
			if(player.getEntityData().hasKey("Grist")) {	//Load old grist format
				NBTTagCompound nbt = player.getEntityData().getCompoundTag("Grist");
				GristSet set = new GristSet();
				for(GristType type : GristType.values())
					set.addGrist(type, nbt.getInteger(type.getName()));
				if(set.isEmpty())
					set.addGrist(GristType.Build, 20);
				
				GristStorage.setGrist(UsernameHandler.encode(player.username), set);
				player.getEntityData().removeTag("Grist");
			} else GristStorage.setGrist(UsernameHandler.encode(player.username), new GristSet(GristType.Build, 20));
		}
		
		updateGristCache(UsernameHandler.encode(player.username));
		updateTitle(player);
		updateLands(player);
		sendConfigPacket(player);
		if(UpdateChecker.outOfDate)
			player.addChatMessage("New version of Minestuck: " + UpdateChecker.latestVersion + "\nChanges: " + UpdateChecker.updateChanges);
	}

	@Override
	public void onPlayerLogout(EntityPlayer player) 
	{
		ServerEditHandler.onPlayerExit(player);
	}

	@Override
	public void onPlayerChangedDimension(EntityPlayer player) 
	{
		updateGristCache(UsernameHandler.encode(player.username));
		updateTitle(player);
	}

	@Override
	public void onPlayerRespawn(EntityPlayer player) 
	{
		updateGristCache(UsernameHandler.encode(player.username));
		updateTitle(player);
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
				Packet250CustomPayload packet = new Packet250CustomPayload();
				packet.channel = "Minestuck";
				packet.data = MinestuckPacket.makePacket(Type.GRISTCACHE, gristValues, false);
				packet.length = packet.data.length;
				playerMP.playerNetServerHandler.sendPacketToPlayer(packet);
			}
		}
		
		//The editing player, if there is any.
		SburbConnection c = SkaianetHandler.getClientConnection(player);
		if(c != null && ServerEditHandler.getData(c) != null) {
			EntityPlayerMP editor = ServerEditHandler.getData(c).getEditor();
			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "Minestuck";
			packet.data = MinestuckPacket.makePacket(Type.GRISTCACHE, gristValues, true);
			packet.length = packet.data.length;
			editor.playerNetServerHandler.sendPacketToPlayer(packet);
		}
	}
	
	public void updateTitle(EntityPlayer player) {
			Title newTitle;
			if (((EntityPlayer)player).getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getInteger("Class") == 0 || ((EntityPlayer)player).getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getInteger("Aspect") == 0) {
				newTitle = TitleHelper.randomTitle();
			} else {
				newTitle = new Title(TitleHelper.getClassFromInt(((EntityPlayer)player).getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getInteger("Class")),
						TitleHelper.getAspectFromInt(((EntityPlayer)player).getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getInteger("Aspect")));
			}
	        Packet250CustomPayload packet = new Packet250CustomPayload();
	        packet.channel = "Minestuck";
	        packet.data = MinestuckPacket.makePacket(Type.TITLE, newTitle.getHeroClass(), newTitle.getHeroAspect());
	        packet.length = packet.data.length;
	        ((EntityPlayerMP)player).playerNetServerHandler.sendPacketToPlayer(packet);
			if(player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getTags().size() == 0)
				player.getEntityData().setCompoundTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
			player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).setInteger("Class", TitleHelper.getIntFromClass(newTitle.getHeroClass()));
			player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).setInteger("Aspect", TitleHelper.getIntFromAspect(newTitle.getHeroAspect()));
	}
	public static void updateLands(EntityPlayer player)
	{
		byte[] lands = new byte[MinestuckSaveHandler.lands.size()];
		for(int i = 0; i < lands.length; i++)
			lands[i] = MinestuckSaveHandler.lands.get(i);
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "Minestuck";
		packet.data = MinestuckPacket.makePacket(Type.LANDREGISTER, lands);
		packet.length = packet.data.length;
		Debug.printf("Sending land packets to %s.", player == null ? "all players" : player.username);
		if(player == null)
			PacketDispatcher.sendPacketToAllPlayers(packet);
		else
			((EntityPlayerMP)player).playerNetServerHandler.sendPacketToPlayer(packet);
	}
	public static void updateLands()
	{
		updateLands(null);
	}
	
	public static void sendConfigPacket(EntityPlayer player) {
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "Minestuck";
		packet.data = MinestuckPacket.makePacket(Type.CONFIG);
		packet.length = packet.data.length;
		((EntityPlayerMP)player).playerNetServerHandler.sendPacketToPlayer(packet);
	}
	
}

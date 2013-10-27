package com.mraof.minestuck.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.mraof.minestuck.entity.EntityDecoy;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;

public class EditHandler {
	
	//Client sided stuff
	static NBTTagCompound capabilities;
	
	
	
	public static void activate(String username, String target) {
		Minecraft mc = Minecraft.getMinecraft();
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "Minestuck";
		packet.data = MinestuckPacket.makePacket(Type.SBURB_EDIT, username, target);
		packet.length = packet.data.length;
		PacketDispatcher.sendPacketToServer(packet);
	}
	
	public static void onKeyPressed() {
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "Minestuck";
		packet.data = MinestuckPacket.makePacket(Type.SBURB_EDIT);
		packet.length = packet.data.length;
		PacketDispatcher.sendPacketToServer(packet);
	}
	
	public static void onClientPackage(boolean mode) {
		if(mode) {	//Enable edit mode
			if(capabilities == null) {
				capabilities = new NBTTagCompound();
				Minecraft.getMinecraft().thePlayer.capabilities.writeCapabilitiesToNBT(capabilities);
			}
		} else {	//Disable edit mode
			if(capabilities != null) {
				Minecraft.getMinecraft().thePlayer.capabilities.readCapabilitiesFromNBT(capabilities);
				capabilities = null;
			}
		}
	}
	
	//Server sided stuff
	
	static Map<String,EntityDecoy> map = new HashMap();
	
	/**
	 * Called both when any player logged out and when a player pressed the exit button.
	 * @param player
	 */
	public static void onPlayerExit(EntityPlayer player) {
		if(!player.worldObj.isRemote) {
			EntityDecoy decoy = getPlayerEntry(player.username) == null?null:getPlayerEntry(player.username).getValue();
			if(decoy != null) reset(null, 0, decoy, (EntityPlayerMP)player);
		}
	}
	
	/**
	 * Called when the server stops editing the clients house.
	 * @param damageSource If the process was cancelled by the decoy taking damage, this parameter will be the damage source. Else null.
	 * @param damage If the damageSource isn't null, this is the damage taken, else this parameter is ignored.
	 * @param decoy The decoy entity used.
	 * @param player The player.
	 */
	public static void reset(DamageSource damageSource, float damage, EntityDecoy decoy, EntityPlayerMP player) {
		if(player == null) {
			Debug.print("Player is null! Can't reset the player's stats!");
			decoy.setDead();
			return;
		}
		
		Entry<String, EntityDecoy> entry = getPlayerEntry(decoy.username);
		if(entry != null)
			map.remove(entry.getKey());
		if(player.dimension != decoy.dimension)
			player.travelToDimension(decoy.dimension);
		player.playerNetServerHandler.setPlayerLocation(decoy.posX, decoy.posY, decoy.posZ, decoy.rotationYaw, decoy.rotationPitch);
		if(!player.theItemInWorldManager.getGameType().equals(decoy.gameType))
			player.setGameType(decoy.gameType);
		player.capabilities.readCapabilitiesFromNBT(decoy.capabilities);
		player.setHealth(decoy.getHealth());
		NBTTagCompound nbt = new NBTTagCompound();
		decoy.foodStats.writeNBT(nbt);
		player.getFoodStats().readNBT(nbt);
		decoy.setDead();
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "Minestuck";
		packet.data = MinestuckPacket.makePacket(Type.SBURB_EDIT, false);
		packet.length = packet.data.length;
		player.playerNetServerHandler.sendPacketToPlayer(packet);
		if(damageSource != null && damageSource.getSourceOfDamage() != player)
			player.attackEntityFrom(damageSource, damage);
	}
	
	public static void newServerEditor(EntityPlayerMP player, String computerOwner, String computerTarget) {
		if(player.isRiding())
			return;	//Don't want to bother making the decoy able to ride anything right now.
		SburbConnection c = SkaianetHandler.getClientConnection(computerTarget);
		Debug.print("Adding new decoy..");
		if(c != null && c.getServerName().equals(computerOwner) && !map.containsKey(c) && getPlayerEntry(player.username) == null) {
			EntityDecoy decoy = new EntityDecoy(player.worldObj, player);
			if(!setPlayerStats(player, c))
				return;
			decoy.worldObj.spawnEntityInWorld(decoy);
			decoy.worldObj.playerEntities.remove(decoy);
			map.put(c.getClientName(), decoy);
			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "Minestuck";
			packet.data = MinestuckPacket.makePacket(Type.SBURB_EDIT, true);
			packet.length = packet.data.length;
			player.playerNetServerHandler.sendPacketToPlayer(packet);
		}
	}
	
	static boolean setPlayerStats(EntityPlayerMP player, SburbConnection c) {
		//TODO Teleport the server player to the correct position at the client land/overworld position.
		return true;
	}
	
	public static void onDisconnect(String client, String server) {
		
	}
	
	public static Entry<String, EntityDecoy> getPlayerEntry(String player) {
		for(Entry<String, EntityDecoy> entry : map.entrySet())
			if(entry.getValue().username.equals(player))
				return entry;
		return null;
	}
	
}

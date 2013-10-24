package com.mraof.minestuck.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.mraof.minestuck.entity.EntityDecoy;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;

public class EditHandler {
	
	static Map<String,EntityDecoy> map = new HashMap();
	
	public static void onPlayerLogout(EntityPlayer player){
		if(!player.worldObj.isRemote){
			EntityDecoy decoy = null;	//this to avoid a ConcurrentModificationException, as the entry is removed in reset();
			for(Entry<String,EntityDecoy> entry : map.entrySet())
				if(entry.getValue().username.equals(player.username)){
					decoy = entry.getValue();
					break;
				}
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
	public static void reset(DamageSource damageSource, float damage, EntityDecoy decoy, EntityPlayerMP player){
		if(player == null){
			Debug.print("Player is null! Can't reset the player's stats!");
			decoy.setDead();
			return;
		}
		String client = null;
		for(Entry<String,EntityDecoy> entry : map.entrySet())
			if(decoy.equals(entry.getValue())){
				client = entry.getKey();
				break;
			}
		if(client != null)
			map.remove(client);
		player.setPositionAndRotation(decoy.posX, decoy.posY, decoy.posZ, decoy.rotationYaw, decoy.rotationPitch);
		player.cameraPitch = decoy.cameraPitch;
		player.cameraYaw = decoy.cameraYaw;
		if(!player.theItemInWorldManager.getGameType().equals(decoy.gameType))
			player.setGameType(decoy.gameType);
		player.setHealth(decoy.getHealth());
		player.getFoodStats().setFoodLevel(decoy.getFoodStats().getFoodLevel());
		player.getFoodStats().setFoodSaturationLevel(decoy.getFoodStats().getSaturationLevel());
		player.worldObj = decoy.worldObj;
		decoy.setDead();
		if(damageSource != null)
			player.attackEntityFrom(damageSource, damage);
	}
	
	public static void newServerEditor(EntityPlayerMP player, String computerOwner, String computerTarget){
		SburbConnection c = SkaianetHandler.getClientConnection(computerTarget);
		Debug.print("Adding new decoy..");
		if(c != null && c.getServerName().equals(computerOwner)){
			EntityDecoy decoy = new EntityDecoy(player.worldObj, player);
			if(!setPlayerStats(player, c))
				return;
			decoy.worldObj.spawnEntityInWorld(decoy);
			decoy.worldObj.playerEntities.remove(decoy);
			map.put(c.getClientName(), decoy);
		}
	}
	
	static boolean setPlayerStats(EntityPlayerMP player, SburbConnection c){
		//TODO Teleport the server player to the correct position at the client land/overworld position.
		return true;
	}
	
	public static void onDisconnect(String client, String server){
		
	}
	
}

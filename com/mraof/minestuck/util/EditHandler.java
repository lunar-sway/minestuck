package com.mraof.minestuck.util;

import java.util.HashMap;
import java.util.Map;

import com.mraof.minestuck.entity.EntityDecoy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;

public class EditHandler {
	
	static Map<String,EntityDecoy> map = new HashMap();
	
	public static void onPlayerLogout(EntityPlayer player){
		if(!player.worldObj.isRemote && map.get(player.username) != null)
			reset(null, 0, map.remove(player.username), (EntityPlayerMP)player);
	}
	
	public static void reset(DamageSource par1DamageSource, float par2, EntityDecoy decoy, EntityPlayerMP player){
		if(player == null){
			decoy.setDead();
			return;
		}
		player.posX = decoy.posX;
		player.chunkCoordX = decoy.chunkCoordX;
		player.posY = decoy.posY;
		player.chunkCoordY = decoy.chunkCoordY;
		player.posZ = decoy.posZ;
		player.chunkCoordZ = decoy.chunkCoordZ;
		player.rotationPitch = decoy.rotationPitch;
		player.rotationYaw = decoy.rotationYaw;
		player.rotationYawHead = decoy.rotationYawHead;
		player.setGameType(decoy.gameType);
		player.setHealth(decoy.getHealth());
	}
	
	public static boolean newServerEditor(EntityPlayerMP player, String computerOwner, String computerTarget){
		return false;
	}
	
}

package com.mraof.minestuck.util;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemInWorldManager;

import com.mraof.minestuck.entity.EntityDecoy;
import com.mraof.minestuck.network.skaianet.SburbConnection;

/**
 * Data structure used by the server sided EditHandler
 * @author Kirderf1
 */
public class EditData {
	
	public EditData(EntityDecoy decoy, EntityPlayerMP player, SburbConnection c) {
		this.decoy = decoy;
		this.player = player;
		this.connection = c;
		this.manager = player.theItemInWorldManager;
	}
	
	EntityDecoy decoy;
	
	ItemInWorldManager manager;
	
	SburbConnection connection;
	
	EntityPlayerMP player;
	
}

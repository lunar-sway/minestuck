//package com.mraof.minestuck.editmode;
//
//import net.minecraft.entity.player.EntityPlayerMP;
//import net.minecraft.server.management.ItemInWorldManager;
//
//import com.mraof.minestuck.entity.EntityDecoy;
//import com.mraof.minestuck.network.skaianet.SburbConnection;
//
///**
// * Data structure used by the server sided EditHandler
// * @author Kirderf1
// */
//public class EditData {
//	
//	public EditData(EntityDecoy decoy, EntityPlayerMP player, SburbConnection c) {
//		this.decoy = decoy;
//		this.player = player;
//		this.connection = c;
//		this.manager = player.theItemInWorldManager;
//	}
//	
//	EntityDecoy decoy;
//	
//	ItemInWorldManager manager;	//Old manager, that was used before entering edit mode.
//	
//	SburbConnection connection;
//	
//	EntityPlayerMP player;
//	
//	public String getTarget() {
//		return connection.getClientName();
//	}
//
//	public EntityPlayerMP getEditor() {
//		return player;
//	}
//	
//}

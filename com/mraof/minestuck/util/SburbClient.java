package com.mraof.minestuck.util;

import java.util.LinkedHashMap;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaiaClient;
import com.mraof.minestuck.tileentity.TileEntityComputer;

public class SburbClient extends ButtonListProgram {
	
	@Override
	public LinkedHashMap<String, Object[]> getStringList(TileEntityComputer te) {
		LinkedHashMap map = new LinkedHashMap();
		String displayPlayer= "UNDEFINED";
		if(te.serverConnected && SkaiaClient.getClientConnection(te.owner) != null)
			displayPlayer = UsernameHandler.decode(SkaiaClient.getClientConnection(te.owner).getServerName());
		SburbConnection c = SkaiaClient.getClientConnection(te.owner);
		if (te.serverConnected && c != null) { //If it is connected to someone.
			map.put("displayMessage", new Object[]{"computer.messageConnect", displayPlayer});
			map.put("computer.buttonClose", null);
		} else if(te.resumingClient){
			map.put("displayMessage", new Object[]{"computer.messageResumeClient"});
			map.put("computer.buttonClose", null);
		} else if(!SkaiaClient.isActive(te.owner, true)){ //If the player doesn't have an other active client
			map.put("displayMessage", "computer.messageSelect");
			if(!SkaiaClient.getAssociatedPartner(te.owner, true).isEmpty()) //If it has a resumable connection
				map.put("computer.buttonResume", null);
			for (String server : SkaiaClient.getAvailableServers(te.owner))
				map.put("computer.buttonConnect",new Object[]{UsernameHandler.decode(server)});
		} else 
			map.put("displayMessage", new Object[]{"computer.messageClientActive"});
		return map;
	}
	
	@Override
	public void onButtonPressed(TileEntityComputer te, String buttonName, Object[] data) {
		if(buttonName.equals("computer.buttonResume"))
			SkaiaClient.sendConnectRequest(te, SkaiaClient.getAssociatedPartner(te.owner, true), true);
		else if(buttonName.equals("computer.buttonConnect"))
			SkaiaClient.sendConnectRequest(te, UsernameHandler.encode((String)data[0]), true);
	}
	
}

package com.mraof.minestuck.util;

import java.util.LinkedHashMap;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.network.skaianet.SkaiaClient;
import com.mraof.minestuck.tileentity.TileEntityComputer;

public class SburbServer extends ButtonListProgram {
	
	@Override
	public LinkedHashMap<String, Object[]> getStringList(TileEntityComputer te) {
		LinkedHashMap map = new LinkedHashMap();
		String displayPlayer= te.clientName.isEmpty()?"UNDEFINED":UsernameHandler.decode(te.clientName);
		if (!te.clientName.isEmpty() && SkaiaClient.getClientConnection(te.clientName) != null) {
			map.put("displayMessage", new Object[]{"computer.messageConnect", displayPlayer});
			map.put("computer.buttonClose", null);
			map.put("computer.buttonEdit", null);
		} else if (te.openToClients) {
			map.put("displayMessage", new Object[]{"computer.messageResumeServer"});
			map.put("computer.buttonClose", null);
		} else if(SkaiaClient.isActive(te.owner, false))
			map.put("displayMessage", new Object[]{"computer.messageServerActive"});
		else {
			map.put("displayMessage", new Object[]{"computer.messageOffline"});
			map.put("computer.buttonOpen", null);
			if(!SkaiaClient.getAssociatedPartner(te.owner, false).isEmpty() && SkaiaClient.getClientConnection(SkaiaClient.getAssociatedPartner(te.owner, false)) == null)
				map.put("computer.buttonResume", null);
		}
		return map;
	}
	
	@Override
	public void onButtonPressed(TileEntityComputer te, String buttonName, Object[] data) {
		if(buttonName.equals("computer.buttonEdit"))
			ClientEditHandler.activate(te.owner,te.clientName);
		else if(buttonName.equals("computer.buttonResume"))
			SkaiaClient.sendConnectRequest(te, SkaiaClient.getAssociatedPartner(te.owner, false), false);
		else if(buttonName.equals("computer.buttonOpen"))
			SkaiaClient.sendConnectRequest(te, "", false);
		
	}
	
}

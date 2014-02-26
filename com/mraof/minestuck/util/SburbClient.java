package com.mraof.minestuck.util;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaiaClient;
import com.mraof.minestuck.tileentity.TileEntityComputer;

public class SburbClient extends ButtonListProgram {
	
	@Override
	public ArrayList<UnlocalizedString> getStringList(TileEntityComputer te) {
		ArrayList list = new ArrayList();
		String displayPlayer= "UNDEFINED";
		if(te.serverConnected && SkaiaClient.getClientConnection(te.owner) != null)
			displayPlayer = UsernameHandler.decode(SkaiaClient.getClientConnection(te.owner).getServerName());
		SburbConnection c = SkaiaClient.getClientConnection(te.owner);
		if (te.serverConnected && c != null) { //If it is connected to someone.
			list.add(new UnlocalizedString("computer.messageConnect", displayPlayer));
			list.add(new UnlocalizedString("computer.buttonClose"));
		} else if(te.resumingClient) {
			list.add(new UnlocalizedString("computer.messageResumeClient"));
			list.add(new UnlocalizedString("computer.buttonClose"));
		} else if(!SkaiaClient.isActive(te.owner, true)){ //If the player doesn't have an other active client
			list.add(new UnlocalizedString("computer.messageSelect"));
			if(!SkaiaClient.getAssociatedPartner(te.owner, true).isEmpty()) //If it has a resumable connection
				list.add(new UnlocalizedString("computer.buttonResume"));
			for (String server : SkaiaClient.getAvailableServers(te.owner))
				list.add(new UnlocalizedString("computer.buttonConnect", UsernameHandler.decode(server)));
		} else list.add(new UnlocalizedString("computer.messageClientActive"));
		return list;
	}
	
	@Override
	public void onButtonPressed(TileEntityComputer te, String buttonName, Object[] data) {
		if(buttonName.equals("computer.buttonResume"))
			SkaiaClient.sendConnectRequest(te, SkaiaClient.getAssociatedPartner(te.owner, true), true);
		else if(buttonName.equals("computer.buttonConnect"))
			SkaiaClient.sendConnectRequest(te, UsernameHandler.encode((String)data[0]), true);
		else if(buttonName.equals("computer.buttonClose"))
			SkaiaClient.sendCloseRequest(te, te.resumingClient?"":SkaiaClient.getClientConnection(te.owner).getServerName(), true);
	}
	
	@Override
	public String getName() {
		return "computer.programClient";
	}
	
}

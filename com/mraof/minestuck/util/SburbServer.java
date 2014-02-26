package com.mraof.minestuck.util;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.network.skaianet.SkaiaClient;
import com.mraof.minestuck.tileentity.TileEntityComputer;

public class SburbServer extends ButtonListProgram {
	
	@Override
	public ArrayList<UnlocalizedString> getStringList(TileEntityComputer te) {
		ArrayList list = new ArrayList();
		String displayPlayer= te.clientName.isEmpty()?"UNDEFINED":UsernameHandler.decode(te.clientName);
		if (!te.clientName.isEmpty() && SkaiaClient.getClientConnection(te.clientName) != null) {
			list.add(new UnlocalizedString("computer.messageConnect", displayPlayer));
			list.add(new UnlocalizedString("computer.buttonClose"));
			list.add(new UnlocalizedString("computer.buttonEdit"));
		} else if (te.openToClients) {
			list.add(new UnlocalizedString("computer.messageResumeServer"));
			list.add(new UnlocalizedString("computer.buttonClose"));
		} else if(SkaiaClient.isActive(te.owner, false))
			list.add(new UnlocalizedString("computer.messageServerActive"));
		else {
			list.add(new UnlocalizedString("computer.messageOffline"));
			list.add(new UnlocalizedString("computer.buttonOpen"));
			if(!SkaiaClient.getAssociatedPartner(te.owner, false).isEmpty() && SkaiaClient.getClientConnection(SkaiaClient.getAssociatedPartner(te.owner, false)) == null)
				list.add(new UnlocalizedString("computer.buttonResume"));
		}
		return list;
	}
	
	@Override
	public void onButtonPressed(TileEntityComputer te, String buttonName, Object[] data) {
		if(buttonName.equals("computer.buttonEdit"))
			ClientEditHandler.activate(te.owner,te.clientName);
		else if(buttonName.equals("computer.buttonResume"))
			SkaiaClient.sendConnectRequest(te, SkaiaClient.getAssociatedPartner(te.owner, false), false);
		else if(buttonName.equals("computer.buttonOpen"))
			SkaiaClient.sendConnectRequest(te, "", false);
		else if(buttonName.equals("computer.buttonClose"))
			SkaiaClient.sendCloseRequest(te, te.openToClients?"":te.clientName, false);
	}
	
	@Override
	public String getName() {
		return "computer.programServer";
	}
	
}

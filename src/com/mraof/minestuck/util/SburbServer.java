package com.mraof.minestuck.util;

import java.util.ArrayList;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.skaianet.SkaiaClient;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.tileentity.TileEntityComputer;

public class SburbServer extends ButtonListProgram {
	
	@Override
	public ArrayList<UnlocalizedString> getStringList(TileEntityComputer te) {
		String clientName = te.getData(1).getString("connectedClient");
		ArrayList<UnlocalizedString> list = new ArrayList<UnlocalizedString>();
		String displayPlayer= clientName.isEmpty()?"UNDEFINED":UsernameHandler.decode(clientName);
		if (!clientName.isEmpty() && SkaiaClient.getClientConnection(clientName) != null) {
			list.add(new UnlocalizedString("computer.messageConnect", displayPlayer));
			list.add(new UnlocalizedString("computer.buttonClose"));
			list.add(new UnlocalizedString(Minestuck.clientGiveItems?"computer.buttonGive":"computer.buttonEdit"));
		} else if (te.getData(getId()).getBoolean("isOpen")) {
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
		if(buttonName.equals("computer.buttonEdit") || buttonName.equals("computer.buttonGive")) {
			MinestuckPacket packet = MinestuckPacket.makePacket(MinestuckPacket.Type.CLIENT_EDIT, te.owner, te.getData(getId()).getString("connectedClient"));
			MinestuckChannelHandler.sendToServer(packet);
		} else if(buttonName.equals("computer.buttonResume"))
			SkaiaClient.sendConnectRequest(te, SkaiaClient.getAssociatedPartner(te.owner, false), false);
		else if(buttonName.equals("computer.buttonOpen"))
			SkaiaClient.sendConnectRequest(te, "", false);
		else if(buttonName.equals("computer.buttonClose"))
			SkaiaClient.sendCloseRequest(te, te.getData(getId()).getBoolean("isOpen")?"":te.getData(getId()).getString("connectedClient"), false);
	}
	
	@Override
	public String getName() {
		return "computer.programServer";
	}
	
	@Override
	public void onClosed(TileEntityComputer te) {
		if(!te.getData(1).getString("connectedClient").isEmpty())
			SkaianetHandler.closeConnection(te.owner, te.getData(1).getString("connectedClient"), false);
		else if(te.getData(1).getBoolean("isOpen"))
			SkaianetHandler.closeConnection(te.owner, "", false);
	}
	
}

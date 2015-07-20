package com.mraof.minestuck.util;

import java.util.ArrayList;

import net.minecraft.nbt.NBTTagCompound;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.ClientProxy;
import com.mraof.minestuck.client.gui.GuiHandler;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaiaClient;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.tileentity.TileEntityComputer;

public class SburbClient extends ButtonListProgram {
	
	@Override
	public ArrayList<UnlocalizedString> getStringList(TileEntityComputer te) {
		ArrayList<UnlocalizedString> list = new ArrayList<UnlocalizedString>();
		NBTTagCompound nbt = te.getData(getId());
		
		SburbConnection c = SkaiaClient.getClientConnection(te.owner);
		if(nbt.getBoolean("connectedToServer") && c != null) //If it is connected to someone.
		{
			String displayPlayer = UsernameHandler.decode(c.getServerName());
			list.add(new UnlocalizedString("computer.messageConnect", displayPlayer));
			list.add(new UnlocalizedString("computer.buttonClose"));
		} else if(nbt.getBoolean("isResuming"))
		{
			list.add(new UnlocalizedString("computer.messageResumeClient"));
			list.add(new UnlocalizedString("computer.buttonClose"));
		} else if(!SkaiaClient.isActive(te.owner, true)){ //If the player doesn't have an other active client
			list.add(new UnlocalizedString("computer.messageSelect"));
			if(!SkaiaClient.getAssociatedPartner(te.owner, true).isEmpty()) //If it has a resumable connection
				list.add(new UnlocalizedString("computer.buttonResume"));
			for (String server : SkaiaClient.getAvailableServers(te.owner))
				list.add(new UnlocalizedString("computer.buttonConnect", UsernameHandler.decode(server)));
		} else list.add(new UnlocalizedString("computer.messageClientActive"));
		if(SkaiaClient.canSelect(te.owner))
			list.add(new UnlocalizedString("computer.selectColor"));
		return list;
	}
	
	@Override
	public void onButtonPressed(TileEntityComputer te, String buttonName, Object[] data) {
		if(buttonName.equals("computer.buttonResume"))
			SkaiaClient.sendConnectRequest(te, SkaiaClient.getAssociatedPartner(te.owner, true), true);
		else if(buttonName.equals("computer.buttonConnect"))
			SkaiaClient.sendConnectRequest(te, UsernameHandler.encode((String)data[0]), true);
		else if(buttonName.equals("computer.buttonClose"))
			SkaiaClient.sendCloseRequest(te, te.getData(getId()).getBoolean("isResuming")?"":SkaiaClient.getClientConnection(te.owner).getServerName(), true);
		else if(buttonName.equals("computer.selectColor"))
			ClientProxy.getClientPlayer().openGui(Minestuck.instance, GuiHandler.GuiId.COLOR.ordinal(), te.getWorld(), te.getPos().getX(), te.getPos().getY(), te.getPos().getZ());
	}
	
	@Override
	public String getName() {
		return "computer.programClient";
	}
	
	@Override
	public void onClosed(TileEntityComputer te) {
		if(te.getData(0).getBoolean("connectedToServer") && SkaianetHandler.getClientConnection(te.owner) != null)
			SkaianetHandler.closeConnection(te.owner, SkaianetHandler.getClientConnection(te.owner).getServerName(), true);
		else if(te.getData(0).getBoolean("isResuming"))
			SkaianetHandler.closeConnection(te.owner, "", true);
	}
	
}

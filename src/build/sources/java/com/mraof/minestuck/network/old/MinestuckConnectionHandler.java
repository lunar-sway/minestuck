/*package com.mraof.minestuck.network.old;

import ibxm.Player;

import java.util.Iterator;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.DimensionManager;

import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.storage.MinestuckSaveHandler;

public class MinestuckConnectionHandler implements IConnectionHandler {

	@Override
	public void playerLoggedIn(Player player, NetHandler netHandler,INetworkManager manager) {
		Debug.print("This is when playerLoggedIn happens");
//		MinestuckPlayerTracker.updateLands((EntityPlayer) player);
	}

	@Override
	public String connectionReceived(NetLoginHandler netHandler,INetworkManager manager) {
		Debug.print("This is when connectionReceived happens");
		Debug.print("Connection Recieved, sending lands");
		byte[] lands = new byte[MinestuckSaveHandler.lands.size()];
		for(int i = 0; i < lands.length; i++)
			lands[i] = MinestuckSaveHandler.lands.get(i);
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "Minestuck";
		packet.data = MinestuckPacket.makePacket(Type.LANDREGISTER, lands);
		packet.length = packet.data.length;
		
		manager.addToSendQueue(packet);
//		Debug.printf("The player is %s", netHandler.getPlayer() == null ? "null" : netHandler.getPlayer().username);
//		MinestuckPlayerTracker.updateLands(netHandler.getPlayer());
		return null;
	}

	@Override
	public void connectionOpened(NetHandler netClientHandler, String server, int port, INetworkManager manager) 
	{
		for(Iterator iterator = MinestuckSaveHandler.lands.iterator(); iterator.hasNext(); )
		{
			int dim = ((Number)iterator.next()).intValue();
			if(DimensionManager.isDimensionRegistered(dim))
			{
				DimensionManager.unregisterDimension(dim);
				Debug.print("Connection opened, Unregistering " + dim);
			}
			iterator.remove();
		}
	}

	@Override
	public void connectionOpened(NetHandler netClientHandler, MinecraftServer server, INetworkManager manager) 
	{
	}

	@Override
	public void connectionClosed(INetworkManager manager) 
	{
		
	}

	@Override
	public void clientLoggedIn(NetHandler clientHandler, INetworkManager manager, Packet1Login login) {
	}

}*/

package com.mraof.minestuck.modSupport;

import com.raoulvdberge.refinedstorage.api.IRSAPI;
import com.raoulvdberge.refinedstorage.api.RSAPIInject;
import com.raoulvdberge.refinedstorage.api.network.node.INetworkNodeProxy;

import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class RefinedStorageSupport
{
	public static final String NBT_DIRECTION = "Direction";
	public static final String NBT_NODE = "Node";
	public static final String NBT_NODE_ID = "NodeID";
	public static final String NBT_BLOCK = "Block";
	public static final String NBT_META = "Meta";
	public static final String NBT_TILE = "Tile";

	@RSAPIInject
	public static IRSAPI API;
	
//	@CapabilityInject(INetworkNodeProxy.class)
//	public static final Capability<INetworkNodeProxy> NETWORK_NODE_PROXY_CAPABILITY = null;
//
//	public static Object getNetworkNodeManager(WorldServer worldserver0) {
//		// TODO Auto-generated method stub
//		return null;
//	}
}

package com.mraof.minestuck.entry;

//import com.raoulvdberge.refinedstorage.api.network.node.INetworkNode;
//import com.raoulvdberge.refinedstorage.api.network.node.INetworkNodeFactory;
//import com.raoulvdberge.refinedstorage.api.network.node.INetworkNodeManager;
//import com.raoulvdberge.refinedstorage.apiimpl.API;
//import com.raoulvdberge.refinedstorage.util.NetworkUtils;
//import net.minecraft.block.BlockState;
//import net.minecraft.nbt.CompoundNBT;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.server.ServerWorld;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//import javax.annotation.Nullable;
//
//public class RSEntryBlockProcess implements EntryBlockProcessing
//{
//	private static final Logger LOGGER = LogManager.getLogger();
//
//	@Override
//	public void copyOver(ServerWorld oldWorld, BlockPos oldPos, ServerWorld newWorld, BlockPos newPos, BlockState state, @Nullable TileEntity oldTE, @Nullable TileEntity newTE)
//	{
//		//1 get the old node
//		INetworkNode node = NetworkUtils.getNodeFromTile(oldTE);
//
//		if(node != null)
//		{
//			//2 create new node from old node data
//			INetworkNode newNode = createNewNode(node, newWorld, newPos);
//			if(newNode != null)
//			{
//				//3 set new node to node manager
//				INetworkNodeManager manager = API.instance().getNetworkNodeManager(newWorld);
//				manager.setNode(newPos, newNode);
//
//				//4 TODO perhaps do something with networks?
//
//			}
//		}
//	}
//
//	private static INetworkNode createNewNode(INetworkNode oldNode, ServerWorld newWorld, BlockPos newPos)
//	{
//		CompoundNBT nbt = oldNode.write(new CompoundNBT());
//		ResourceLocation id = oldNode.getId();
//
//		INetworkNodeFactory factory = API.instance().getNetworkNodeRegistry().get(id);
//		if (factory != null)
//		{
//			INetworkNode newNode = null;
//
//			try
//			{
//				newNode = factory.create(nbt, newWorld, newPos);
//			} catch(Throwable exception)
//			{
//				LOGGER.warn("Got exception when attempting to make a copy of RE-node {}", id, exception);
//			}
//
//			if(newNode != null)
//			{
//				return newNode;
//			} else LOGGER.warn("Unable to make a copy of RE-node {}: factory failed to produce a node", id);
//		} else LOGGER.warn("Unable to make a copy of RE-node {}: no associated factory", id);
//
//		return null;
//	}
//}
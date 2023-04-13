package com.mraof.minestuck.entry;

import com.refinedmods.refinedstorage.api.network.node.INetworkNode;
import com.refinedmods.refinedstorage.api.network.node.INetworkNodeFactory;
import com.refinedmods.refinedstorage.api.network.node.INetworkNodeManager;
import com.refinedmods.refinedstorage.apiimpl.API;
import com.refinedmods.refinedstorage.util.NetworkUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

public class RSEntryBlockProcess implements BlockCopier.CopyStep
{
	private static final Logger LOGGER = LogManager.getLogger();

	@Override
	public void copyOver(ServerLevel oldLevel, BlockPos oldPos, ServerLevel newLevel, BlockPos newPos, BlockState state, @Nullable BlockEntity oldBE, @Nullable BlockEntity newBE)
	{
		//1 get the old node
		INetworkNode node = NetworkUtils.getNodeFromBlockEntity(oldBE);

		if(node != null)
		{
			//2 create new node from old node data
			INetworkNode newNode = createNewNode(node, newLevel, newPos);
			if(newNode != null)
			{
				//3 set new node to node manager
				INetworkNodeManager manager = API.instance().getNetworkNodeManager(newLevel);
				manager.setNode(newPos, newNode);

				//4 TODO perhaps do something with networks?

			}
		}
	}

	private static INetworkNode createNewNode(INetworkNode oldNode, ServerLevel newLevel, BlockPos newPos)
	{
		CompoundTag nbt = oldNode.write(new CompoundTag());
		ResourceLocation id = oldNode.getId();

		INetworkNodeFactory factory = API.instance().getNetworkNodeRegistry().get(id);
		if (factory != null)
		{
			INetworkNode newNode = null;

			try
			{
				newNode = factory.create(nbt, newLevel, newPos);
			} catch(Throwable exception)
			{
				LOGGER.warn("Got exception when attempting to make a copy of RE-node {}", id, exception);
			}

			if(newNode != null)
			{
				return newNode;
			} else LOGGER.warn("Unable to make a copy of RE-node {}: factory failed to produce a node", id);
		} else LOGGER.warn("Unable to make a copy of RE-node {}: no associated factory", id);

		return null;
	}
}
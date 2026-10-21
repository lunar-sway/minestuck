package com.mraof.minestuck.item.artifact;

import com.mraof.minestuck.entry.EntryProcess;
import com.mraof.minestuck.item.AlchemizedColored;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;

/**
 * Marker interface for cruxite artifact items.
 * Use this instead of {@link CruxiteArtifactItem} when your item doesn't directly extend {@link Item}.
 * @see #onArtifactActivated(ServerPlayer)
 */
public interface CruxiteArtifact extends AlchemizedColored
{
	/**
	 * Called when a cruxite artifact is used correctly, to begin entry or teleport the player to their land (if they've already entered).
	 * @param player The player that used the cruxite artifact.
	 */
	public default void onArtifactActivated(ServerPlayer player)
	{
		EntryProcess.enter(player);
	}
}
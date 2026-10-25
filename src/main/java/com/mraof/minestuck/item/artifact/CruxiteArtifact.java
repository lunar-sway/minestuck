package com.mraof.minestuck.item.artifact;

import com.mraof.minestuck.entry.EntryProcess;
import com.mraof.minestuck.item.AlchemizedColored;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;

/**
 * <p>
 * Marker interface for cruxite artifact items.
 * Use this instead of {@link CruxiteArtifactItem} when your item doesn't directly extend {@link Item}.
 * </p>
 * <p>
 * To implement a custom cruxite artifact, implement this interface on your item (or extend {@link CruxiteArtifactItem})
 * and call {@link #onArtifactActivated(ServerPlayer)} when the player uses the artifact correctly.
 * The cruxite artifact must be added to the {@code minestuck:cruxite_artifacts} item tag in order to be randomly chosen for players.
 * </p>
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
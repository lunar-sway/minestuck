package com.mraof.minestuck.item.artifact;

import com.mraof.minestuck.item.AlchemizedColored;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;

/**
 * Marker interface for cruxite artifact items.
 * Use this instead of {@link CruxiteArtifactItem} when your item doesn't directly extend {@link Item}.
 * @see CruxiteArtifactItem#onArtifactActivated(ServerPlayer)
 */
public interface CruxiteArtifact extends AlchemizedColored
{

}
package com.mraof.minestuck.event;

import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.ImmutableGristSet;
import com.mraof.minestuck.blockentity.machine.AlchemiterBlockEntity;
import com.mraof.minestuck.blockentity.machine.MiniAlchemiterBlockEntity;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.Event;

public class AlchemyEvent extends Event
{
	private final PlayerIdentifier player;
	private final BlockEntity alchemiter;
	private final ItemStack dowel;
	private ItemStack result;
	private final ImmutableGristSet cost;
	
	public AlchemyEvent(PlayerIdentifier player, BlockEntity alchemiter, ItemStack dowel, ItemStack result, GristSet cost)
	{
		this.player = player;
		this.alchemiter = alchemiter;
		this.dowel = dowel;
		this.result = result;
		this.cost = cost.asImmutable();
	}
	
	public PlayerIdentifier getPlayer()
	{
		return player;
	}
	
	/**
	 * Returns the alchemiter tile entity that this is happening on. Either an instance of {@link AlchemiterBlockEntity} or {@link MiniAlchemiterBlockEntity}.
	 */
	public BlockEntity getAlchemiter()
	{
		return alchemiter;
	}
	
	public Level getLevel()
	{
		return alchemiter.getLevel();
	}
	
	public ItemStack getDowel()
	{
		return dowel.copy();
	}
	
	public ItemStack getItemResult()
	{
		return result.copy();
	}
	
	public void setItemResult(ItemStack result)
	{
		this.result = result.copy();
	}
	
	public GristSet getCost()
	{
		return cost;
	}
}
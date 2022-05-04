package com.mraof.minestuck.event;

import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.tileentity.machine.AlchemiterTileEntity;
import com.mraof.minestuck.tileentity.machine.MiniAlchemiterTileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.Event;

public class AlchemyEvent extends Event
{
	private final PlayerIdentifier player;
	private final TileEntity alchemiter;
	private final ItemStack dowel;
	private ItemStack result;
	private final GristSet cost;
	
	public AlchemyEvent(PlayerIdentifier player, TileEntity alchemiter, ItemStack dowel, ItemStack result, GristSet cost)
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
	 * Returns the alchemiter tile entity that this is happening on. Either an instance of {@link AlchemiterTileEntity} or {@link MiniAlchemiterTileEntity}.
	 */
	public TileEntity getAlchemiter()
	{
		return alchemiter;
	}
	
	public World getWorld()
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
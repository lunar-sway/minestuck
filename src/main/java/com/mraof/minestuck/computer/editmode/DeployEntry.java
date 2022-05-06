package com.mraof.minestuck.computer.editmode;


import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.skaianet.SburbConnection;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.World;

import java.util.function.BiFunction;

public class DeployEntry
{
	private String name;
	
	private int tier;
	private DeployList.IAvailabilityCondition condition;
	private BiFunction<SburbConnection, World, ItemStack> item;
	private BiFunction<Boolean, SburbConnection, GristSet> grist;
	
	DeployEntry(String name, int tier, DeployList.IAvailabilityCondition condition, BiFunction<SburbConnection, World, ItemStack> item, BiFunction<Boolean, SburbConnection, GristSet> grist)
	{
		this.name = name;
		this.tier = tier;
		this.condition = condition;
		this.item = item;
		this.grist = grist;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getTier()
	{
		return tier;
	}
	
	public boolean isAvailable(SburbConnection c, int tier)
	{
		return (condition == null || condition.test(c)) && this.tier <= tier && getCurrentCost(c) != null;
	}
	
	public ItemStack getItemStack(SburbConnection c, World world)
	{
		return item.apply(c, world).copy();
	}
	
	public GristSet getPrimaryGristCost(SburbConnection c)
	{
		return grist.apply(true, c);
	}
	
	public GristSet getSecondaryGristCost(SburbConnection c)
	{
		return grist.apply(false, c);
	}
	
	public GristSet getCurrentCost(SburbConnection c)
	{
		return c.hasGivenItem(this) ? getSecondaryGristCost(c) : getPrimaryGristCost(c);
	}
	
	void tryAddDeployTag(SburbConnection c, World world, int tier, ListNBT list, int i)
	{
		if(isAvailable(c, tier))
		{
			ItemStack stack = getItemStack(c, world);
			GristSet cost = getCurrentCost(c);
			CompoundNBT tag = new CompoundNBT();
			stack.save(tag);
			tag.putInt("i", i);
			tag.put("cost", cost.write(new ListNBT()));
			list.add(tag);
		}
	}
}
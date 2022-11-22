package com.mraof.minestuck.computer.editmode;


import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.skaianet.SburbConnection;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.function.BiFunction;

public class DeployEntry
{
	private String name;
	
	private int tier;
	private DeployList.IAvailabilityCondition condition;
	private BiFunction<SburbConnection, Level, ItemStack> item;
	private BiFunction<Boolean, SburbConnection, GristSet> grist;
	
	DeployEntry(String name, int tier, DeployList.IAvailabilityCondition condition, BiFunction<SburbConnection, Level, ItemStack> item, BiFunction<Boolean, SburbConnection, GristSet> grist)
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
	
	public ItemStack getItemStack(SburbConnection c, Level level)
	{
		return item.apply(c, level).copy();
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
	
	void tryAddDeployTag(SburbConnection c, Level level, int tier, ListTag list, int i)
	{
		if(isAvailable(c, tier))
		{
			ItemStack stack = getItemStack(c, level);
			GristSet cost = getCurrentCost(c);
			CompoundTag tag = new CompoundTag();
			stack.save(tag);
			tag.putInt("i", i);
			tag.put("cost", cost.write(new ListTag()));
			list.add(tag);
		}
	}
}
package com.mraof.minestuck.computer.editmode;


import com.mraof.minestuck.alchemy.MutableGristSet;
import com.mraof.minestuck.skaianet.SburbConnection;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.BiFunction;

public class DeployEntry
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final String name;
	
	private final int tier;
	private final DeployList.IAvailabilityCondition condition;
	private final BiFunction<SburbConnection, Level, ItemStack> item;
	private final BiFunction<Boolean, SburbConnection, MutableGristSet> grist;
	private final DeployList.EntryLists category;
	
	DeployEntry(String name, int tier, DeployList.IAvailabilityCondition condition, BiFunction<SburbConnection, Level, ItemStack> item, BiFunction<Boolean, SburbConnection, MutableGristSet> grist, DeployList.EntryLists entryList)
	{
		this.name = name;
		this.tier = tier;
		this.condition = condition;
		this.item = item;
		this.grist = grist;
		this.category = entryList;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getTier()
	{
		return tier;
	}

	public DeployList.EntryLists getCategory() { return category; }
	
	public boolean isAvailable(SburbConnection c, int tier)
	{
		return (condition == null || condition.test(c)) && this.tier <= tier && getCurrentCost(c) != null;
	}
	
	public ItemStack getItemStack(SburbConnection c, Level level)
	{
		return item.apply(c, level).copy();
	}
	
	public MutableGristSet getPrimaryGristCost(SburbConnection c)
	{
		return grist.apply(true, c);
	}
	
	public MutableGristSet getSecondaryGristCost(SburbConnection c)
	{
		return grist.apply(false, c);
	}
	
	public MutableGristSet getCurrentCost(SburbConnection c)
	{
		return c.hasGivenItem(this) ? getSecondaryGristCost(c) : getPrimaryGristCost(c);
	}
	
	void tryAddDeployTag(SburbConnection c, Level level, int tier, ListTag list, int i)
	{
		if(isAvailable(c, tier))
		{
			ItemStack stack = getItemStack(c, level);
			MutableGristSet cost = getCurrentCost(c);
			CompoundTag tag = new CompoundTag();
			stack.save(tag);
			tag.putInt("i", i);
			tag.put("cost", MutableGristSet.CODEC.encodeStart(NbtOps.INSTANCE, cost).getOrThrow(false, LOGGER::error));
			tag.putInt("cat", category.ordinal());
			list.add(tag);
		}
	}
}
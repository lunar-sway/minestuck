package com.mraof.minestuck.computer.editmode;


import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.ImmutableGristSet;
import com.mraof.minestuck.skaianet.SburbPlayerData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.function.BiFunction;

public class DeployEntry
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final String name;
	
	private final int tier;
	private final DeployList.IAvailabilityCondition condition;
	private final BiFunction<SburbPlayerData, Level, ItemStack> item;
	private final BiFunction<Boolean, SburbPlayerData, GristSet> grist;
	private final DeployList.EntryLists category;
	
	DeployEntry(String name, int tier, DeployList.IAvailabilityCondition condition,
				BiFunction<SburbPlayerData, Level, ItemStack> item,
				BiFunction<Boolean, SburbPlayerData, GristSet> grist, DeployList.EntryLists entryList)
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
	
	public boolean isAvailable(SburbPlayerData playerData, int tier)
	{
		return (condition == null || condition.test(playerData)) && this.tier <= tier && getCurrentCost(playerData) != null;
	}
	
	public ItemStack getItemStack(SburbPlayerData playerData, Level level)
	{
		return item.apply(playerData, level).copy();
	}
	
	@Nullable
	public GristSet getCurrentCost(SburbPlayerData playerData)
	{
		boolean usePrimaryCost = !playerData.hasGivenItem(this);
		return grist.apply(usePrimaryCost, playerData);
	}
	
	void tryAddDeployTag(SburbPlayerData playerData, Level level, int tier, ListTag list, int i)
	{
		if(isAvailable(playerData, tier))
		{
			ItemStack stack = getItemStack(playerData, level);
			GristSet cost = getCurrentCost(playerData);
			CompoundTag tag = new CompoundTag();
			stack.save(tag);
			tag.putInt("i", i);
			tag.put("cost", ImmutableGristSet.LIST_CODEC.encodeStart(NbtOps.INSTANCE, cost.asImmutable()).getOrThrow(false, LOGGER::error));
			tag.putInt("cat", category.ordinal());
			list.add(tag);
		}
	}
}
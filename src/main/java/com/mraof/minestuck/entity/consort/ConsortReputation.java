package com.mraof.minestuck.entity.consort;

import com.mraof.minestuck.player.PlayerData;
import com.mraof.minestuck.util.MSAttachments;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.INBTSerializable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;

@ParametersAreNonnullByDefault
public final class ConsortReputation implements INBTSerializable<ListTag>
{
	private final Map<ResourceLocation, Integer> consortReputation = new HashMap<>();
	
	public int getConsortReputation(ResourceKey<Level> dim)
	{
		return consortReputation.getOrDefault(dim.location(), 0);
	}
	
	public void addConsortReputation(int amount, ResourceKey<Level> dim)
	{
		int oldRep = getConsortReputation(dim);
		int newRep = Mth.clamp(oldRep + amount, -10000, 10000);
		
		if(newRep != oldRep)
			consortReputation.put(dim.location(), newRep);
	}
	
	@Override
	public ListTag serializeNBT(HolderLookup.Provider provider)
	{
		ListTag list = new ListTag();
		for(Map.Entry<ResourceLocation, Integer> entry : consortReputation.entrySet())
		{
			CompoundTag dimensionRep = new CompoundTag();
			dimensionRep.putString("dim", entry.getKey().toString());
			dimensionRep.putInt("rep", entry.getValue());
			list.add(dimensionRep);
		}
		return list;
	}
	
	@Override
	public void deserializeNBT(HolderLookup.Provider provider, ListTag list)
	{
		for(int i = 0; i < list.size(); i++)
		{
			CompoundTag dimensionRep = list.getCompound(i);
			ResourceLocation dimension = ResourceLocation.tryParse(dimensionRep.getString("dim"));
			if(dimension != null)
				consortReputation.put(dimension, dimensionRep.getInt("rep"));
		}
	}
	
	public static ConsortReputation get(ServerPlayer player)
	{
		return get(PlayerData.get(player).orElseThrow());
	}
	
	public static ConsortReputation get(PlayerData playerData)
	{
		return playerData.getData(MSAttachments.CONSORT_REPUTATION);
	}
}

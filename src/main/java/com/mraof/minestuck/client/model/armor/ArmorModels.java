package com.mraof.minestuck.client.model.armor;

import com.mraof.minestuck.item.MSArmorItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public final class ArmorModels
{
	private static final Map<ResourceLocation, HumanoidModel<?>> map = new HashMap<>();
	
	public static void register(MSArmorItem item, HumanoidModel<?> model)
	{
		if (map.containsKey(item.getRegistryName()))
			throw new IllegalArgumentException("A model has already been registered with this item.");
		map.put(item.getRegistryName(), model);
	}
	
	@Nullable
	public static HumanoidModel<?> get(Item item)
	{
		return map.get(item.getRegistryName());
	}
}
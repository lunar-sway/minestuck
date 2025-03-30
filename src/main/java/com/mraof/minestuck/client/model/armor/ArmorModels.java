package com.mraof.minestuck.client.model.armor;

import com.mraof.minestuck.item.armor.MSArmorItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.registries.BuiltInRegistries;
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
		ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(item);
		if (map.containsKey(itemId))
			throw new IllegalArgumentException("A model has already been registered with this item.");
		map.put(itemId, model);
	}
	
	@Nullable
	public static HumanoidModel<?> get(Item item)
	{
		return map.get(BuiltInRegistries.ITEM.getKey(item));
	}
}
package com.mraof.minestuck.api.uranium;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.Minestuck;

import net.minecraft.core.registries.Registries;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

@EventBusSubscriber(modid = Minestuck.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public record UraniumPower(int power)
{
	public static final Codec<UraniumPower> POWER_CODEC = ExtraCodecs.POSITIVE_INT.xmap(UraniumPower::new, UraniumPower::power);
	
	public static final Codec<UraniumPower> CODEC = Codec.withAlternative(RecordCodecBuilder.create(instance -> instance.group(ExtraCodecs.POSITIVE_INT.fieldOf("uranium_power").forGetter(UraniumPower::power)).apply(instance, UraniumPower::new)), POWER_CODEC);
	
	public static final DataMapType<Item, UraniumPower> URANIUM_POWER_MAP = DataMapType.builder(Minestuck.id("uranium_power"), Registries.ITEM, CODEC).synced(CODEC, true).build();
	
	@SubscribeEvent
	public static void registerDataMap(final RegisterDataMapTypesEvent event)
	{
		event.register(URANIUM_POWER_MAP);
	}
	
	/**
	 * Returns the available uranium power for a single item in the stack
	 */
	public static int getUraniumPower(ItemStack stack)
	{
		var power = stack.getItemHolder().getData(UraniumPower.URANIUM_POWER_MAP);
		if(power == null) return 0;
		return power.power();
	}
	
	/**
	 * Checks if the stack can provide uranium power
	 */
	public static boolean hasUraniumPower(ItemStack stack)
	{
		return stack.getItemHolder().getData(UraniumPower.URANIUM_POWER_MAP) != null;
	}
}
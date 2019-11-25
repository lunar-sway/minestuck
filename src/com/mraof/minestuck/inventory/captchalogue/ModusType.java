package com.mraof.minestuck.inventory.captchalogue;

import net.minecraft.item.Item;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.Objects;
import java.util.function.BiFunction;

public class ModusType<T extends Modus> extends ForgeRegistryEntry<ModusType<?>>
{
	private final BiFunction<ModusType<T>, LogicalSide, T> factory;
	private final Item modusItem;
	
	public ModusType(BiFunction<ModusType<T>, LogicalSide, T> factory, Item modusItem)
	{
		this.factory = Objects.requireNonNull(factory);
		this.modusItem = Objects.requireNonNull(modusItem);
	}
	
	public T create(LogicalSide side)
	{
		return factory.apply(this, side);
	}
	
	public Item getItem()
	{
		return modusItem;
	}
}
package com.mraof.minestuck.inventory.captchalogue;

import net.minecraft.world.item.Item;
import net.neoforged.fml.LogicalSide;

import java.util.Objects;
import java.util.function.Supplier;

public final class ModusType<T extends Modus>
{
	private final ModusFactory<T> factory;
	private final Supplier<Item> modusItem;
	
	public ModusType(ModusFactory<T> factory, Supplier<Item> modusItem)
	{
		this.factory = Objects.requireNonNull(factory);
		this.modusItem = Objects.requireNonNull(modusItem);
	}
	
	public T createClientSide()
	{
		return factory.create(this, LogicalSide.CLIENT);
	}
	
	public T createServerSide()
	{
		return factory.create(this, LogicalSide.SERVER);
	}
	
	public Item getItem()
	{
		return modusItem.get();
	}
	
	public interface ModusFactory<T extends Modus>
	{
		T create(ModusType<T> type, LogicalSide side);
	}
}
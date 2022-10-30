package com.mraof.minestuck.inventory.captchalogue;

import com.mraof.minestuck.player.PlayerSavedData;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.Objects;
import java.util.function.Supplier;

public final class ModusType<T extends Modus> extends ForgeRegistryEntry<ModusType<?>>
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
		return factory.create(this, null, LogicalSide.CLIENT);
	}
	
	public T createServerSide(PlayerSavedData savedData)
	{
		return factory.create(this, savedData, LogicalSide.SERVER);
	}
	
	public Item getItem()
	{
		return modusItem.get();
	}
	
	public interface ModusFactory<T extends Modus>
	{
		T create(ModusType<T> type, PlayerSavedData savedData, LogicalSide side);
	}
}
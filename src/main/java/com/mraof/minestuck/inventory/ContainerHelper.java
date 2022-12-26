package com.mraof.minestuck.inventory;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

import java.util.function.Consumer;

public class ContainerHelper
{
	public static void addPlayerInventorySlots(Consumer<Slot> slotAdder, int x, int y, Inventory playerInventory)
	{
		for (int row = 0; row < 3; row++)
			for (int column = 0; column < 9; column++)
				slotAdder.accept(new Slot(playerInventory, column + row * 9 + 9,
						x + column * 18, y + row * 18));
		
		for (int column = 0; column < 9; column++)
			slotAdder.accept(new Slot(playerInventory, column, x + column * 18, y + 58));
	}
}

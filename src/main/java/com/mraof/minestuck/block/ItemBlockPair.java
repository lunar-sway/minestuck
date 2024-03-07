package com.mraof.minestuck.block;

import com.mraof.minestuck.item.MSItems;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;
import java.util.function.Supplier;

@MethodsReturnNonnullByDefault
public record ItemBlockPair<B extends Block, I extends Item>(RegistryObject<B> blockHolder, RegistryObject<I> itemHolder) implements ItemLike
{
	public B asBlock()
	{
		return this.blockHolder.get();
	}
	
	@Override
	public I asItem()
	{
		return this.itemHolder.get();
	}
	
	public static <B extends Block> ItemBlockPair<B, BlockItem> register(String name, Supplier<B> blockSupplier)
	{
		return register(name, blockSupplier, Item.Properties::new);
	}
	
	public static <B extends Block> ItemBlockPair<B, BlockItem> register(String name, Supplier<B> blockSupplier, Supplier<Item.Properties> properties)
	{
		return register(name, blockSupplier, block -> new BlockItem(block, properties.get()));
	}
	
	public static <B extends Block, I extends Item> ItemBlockPair<B, I> register(String name, Supplier<B> blockSupplier, Function<B, I> itemSupplier)
	{
		RegistryObject<B> blockHolder = MSBlocks.REGISTER.register(name, blockSupplier);
		RegistryObject<I> itemHolder = MSItems.REGISTER.register(name, () -> itemSupplier.apply(blockHolder.get()));
		return new ItemBlockPair<>(blockHolder, itemHolder);
	}
}

package com.mraof.minestuck.block;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LootBlockTables
{
	public static ItemStack[] TIER_ONE_GENERIC = new ItemStack[]{new ItemStack(Items.MELON_SLICE), new ItemStack(Items.STICK), new ItemStack(Items.STICK), new ItemStack(Items.STICK), new ItemStack(Items.STICK)
			, new ItemStack(Items.STICK), new ItemStack(Items.STICK), new ItemStack(Items.STICK), new ItemStack(Items.STICK), new ItemStack(Items.STICK), new ItemStack(Items.STICK), new ItemStack(Items.STICK)
			, new ItemStack(Items.STICK), new ItemStack(Items.STICK), new ItemStack(Items.STICK), new ItemStack(Items.STICK), new ItemStack(Items.STICK), new ItemStack(Items.STICK), new ItemStack(Items.STICK)};
	
	public static ItemStack[] TIER_ONE_TEST = new ItemStack[]{new ItemStack(Items.DIAMOND), new ItemStack(Items.GOLD_INGOT)};
	
	public static ItemStack[] concatWithCollection(ItemStack[] array1, ItemStack[] array2)
	{
		List<ItemStack> resultList = new ArrayList<>(array1.length + array2.length);
		Collections.addAll(resultList, array1);
		Collections.addAll(resultList, array2);
		
		@SuppressWarnings("unchecked")
		//the type cast is safe as the array1 has the type T[]
		ItemStack[] resultArray = (ItemStack[]) Array.newInstance(array1.getClass().getComponentType(), 0);
		return resultList.toArray(resultArray);
	}
	
	public static void givePlayerItemFromArrays(World world, LivingEntity livingEntity, ItemStack[] itemStackArray1, ItemStack[] itemStackArray2)
	{
		ItemStack[] combinedItemStackArray = concatWithCollection(itemStackArray1, itemStackArray2);
		
		int num = world.rand.nextInt(combinedItemStackArray.length);
		ItemEntity item = new ItemEntity(world, livingEntity.getPosX(), livingEntity.getPosY(), livingEntity.getPosZ(), combinedItemStackArray[num].copy());
		livingEntity.world.addEntity(item);
	}
	
	public static ItemStack[] concatWithCollectionLandVariables(ItemStack[] genericStackArray, ItemStack[] terrainStackArray, ItemStack[] aspectStackArray, ItemStack[] classStackArray)
	{
		ItemStack[] combinedItemStackArray = concatWithCollection(genericStackArray, terrainStackArray);
		combinedItemStackArray = concatWithCollection(combinedItemStackArray, aspectStackArray);
		return concatWithCollection(combinedItemStackArray, classStackArray);
	}
	
	public static void givePlayerItemFromLandTableArrays(World world, LivingEntity livingEntity, ItemStack[] genericStackArray, ItemStack[] terrainStackArray, ItemStack[] aspectStackArray, ItemStack[] classStackArray)
	{
		ItemStack[] combinedItemStackArray = concatWithCollectionLandVariables(genericStackArray, terrainStackArray, aspectStackArray, classStackArray);
		
		int num = world.rand.nextInt(combinedItemStackArray.length);
		ItemEntity item = new ItemEntity(world, livingEntity.getPosX(), livingEntity.getPosY(), livingEntity.getPosZ(), combinedItemStackArray[num].copy());
		livingEntity.world.addEntity(item);
	}
	
	//public ItemStack[] TEST = Stream.concat(Arrays.stream(TIER_ONE_GENERIC), Arrays.stream(TIER_ONE_TEST));
}

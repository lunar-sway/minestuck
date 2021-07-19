package com.mraof.minestuck.data.loot_table;

import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;

public class WheatSeedsConverterModifier extends LootModifier {
	private final int numSeedsToConvert;
	private final Item itemToCheck;
	private final Item itemReward;
	public WheatSeedsConverterModifier(ILootCondition[] conditionsIn, int numSeeds, Item itemCheck, Item reward) {
		super(conditionsIn);
		numSeedsToConvert = numSeeds;
		itemToCheck = itemCheck;
		itemReward = reward;
	}
	
	@Nonnull
	@Override
	public List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
		//
		// Additional conditions can be checked, though as much as possible should be parameterized via JSON data.
		// It is better to write a new ILootCondition implementation than to do things here.
		//
		int numSeeds = 0;
		for (ItemStack stack : generatedLoot) {
			if (stack.getItem() == itemToCheck)
				numSeeds += stack.getCount();
		}
		if (numSeeds >= numSeedsToConvert) {
			generatedLoot.removeIf(x -> x.getItem() == itemToCheck);
			generatedLoot.add(new ItemStack(itemReward, (numSeeds / numSeedsToConvert)));
			numSeeds = numSeeds % numSeedsToConvert;
			if (numSeeds > 0)
				generatedLoot.add(new ItemStack(itemToCheck, numSeeds));
		}
		return generatedLoot;
	}
	
	public static class Serializer extends GlobalLootModifierSerializer<WheatSeedsConverterModifier> {
		
		@Override
		public WheatSeedsConverterModifier read(ResourceLocation location, JsonObject object, ILootCondition[] ailootcondition)
		{
			int numSeeds = JSONUtils.getInt(object, "numSeeds");
			Item seed = ForgeRegistries.ITEMS.getValue(new ResourceLocation((JSONUtils.getString(object, "seedItem"))));
			Item wheat = ForgeRegistries.ITEMS.getValue(new ResourceLocation(JSONUtils.getString(object, "replacement")));
			return new WheatSeedsConverterModifier(ailootcondition, numSeeds, seed, wheat);
		}
	}
}
package com.mraof.minestuck.data.loot_table;

public class MSLootModifier extends LootModifier {
	private final int numSeedsToConvert;
	private final Item itemToCheck;
	private final Item itemReward;
	public MSLootModifier(ILootCondition[] conditionsIn, int numSeeds, Item itemCheck, Item reward) {
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
	
	public static class Serializer extends GlobalLootModifierSerializer<MSLootModifier> {
		
		@Override
		public MSLootModifier read(ResourceLocation name, JsonObject object, ILootCondition[] conditionsIn) {
			int numSeeds = JSONUtils.getInt(object, "numSeeds");
			Item seed = ForgeRegistries.ITEMS.getValue(new ResourceLocation((JSONUtils.getString(object, "seedItem"))));
			Item wheat = ForgeRegistries.ITEMS.getValue(new ResourceLocation(JSONUtils.getString(object, "replacement")));
			return new WheatSeedsConverterModifier(conditionsIn, numSeeds, seed, wheat);
		}
	}
}

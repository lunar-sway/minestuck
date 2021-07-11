package com.mraof.minestuck.data.loot_table;

import com.mraof.minestuck.Minestuck;
import net.minecraftforge.fml.common.Mod;

/*//@Mod(Minestuck.MOD_ID) //TODO DONT USE THIS
public class MSGlobalLootModifier {
	public static final String MODID = "minestuck";
	public static final boolean ENABLE = true;
	
	public MSGlobalLootModifier()
	{
		if(ENABLE)
		{
			GLM.register(FMLJavaModLoadingContext.get().getModEventBus());
			ENCHANTS.register(FMLJavaModLoadingContext.get().getModEventBus());
		}
	}
	
	private static final DeferredRegister<GlobalLootModifierSerializer<?>> GLM = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, MODID);
	private static final DeferredRegister<Enchantment> ENCHANTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, MODID);
	
	private static final RegistryObject<DungeonLootEnhancerModifier.Serializer> DUNGEON_LOOT = GLM.register("dungeon_loot", DungeonLootEnhancerModifier.Serializer::new);
	private static final RegistryObject<SmeltingEnchantmentModifier.Serializer> SMELTING = GLM.register("smelting", SmeltingEnchantmentModifier.Serializer::new);
	private static final RegistryObject<WheatSeedsConverterModifier.Serializer> WHEATSEEDS = GLM.register("wheat_harvest", WheatSeedsConverterModifier.Serializer::new);
	private static final RegistryObject<SilkTouchTestModifier.Serializer> SILKTOUCH = GLM.register("silk_touch_bamboo", SilkTouchTestModifier.Serializer::new);
	private static final RegistryObject<Enchantment> SMELT = ENCHANTS.register("smelt", () -> new SmelterEnchantment(Rarity.UNCOMMON, EnchantmentType.DIGGER, EquipmentSlotType.MAINHAND));
	
	@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD)
	public static class EventHandlers {
		@SubscribeEvent
		public static void runData(GatherDataEvent event)
		{
			if(ENABLE)
				event.getGenerator().addProvider(new DataProvider(event.getGenerator(), MODID));
		}
	}
	
	private static class DataProvider extends GlobalLootModifierProvider
	{
		public DataProvider(DataGenerator gen, String modid)
		{
			super(gen, modid);
		}
		
		@Override
		protected void start()
		{
			add("smelting", SMELTING.get(), new SmeltingEnchantmentModifier(
					new ILootCondition[]{
							MatchTool.toolMatches(
									ItemPredicate.Builder.item().hasEnchantment(
											new EnchantmentPredicate(SMELT.get(), MinMaxBounds.IntBound.atLeast(1))))
									.build()
					})
			);
			
			add("wheat_harvest", WHEATSEEDS.get(), new WheatSeedsConverterModifier(
					new ILootCondition[] {
							MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS)).build(),
							BlockStateProperty.hasBlockStateProperties(Blocks.WHEAT).build()
					},
					3, Items.WHEAT_SEEDS, Items.WHEAT)
			);
			
			add("dungeon_loot", DUNGEON_LOOT.get(), new DungeonLootEnhancerModifier(
					new ILootCondition[] { LootTableIdCondition.builder(new ResourceLocation("chests/simple_dungeon")).build() })
			);
		}
	}
	
	private static class SmelterEnchantment extends Enchantment {
		protected SmelterEnchantment(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType... slots) {
			super(rarityIn, typeIn, slots);
		}
	}
	
	/**
	 * The smelting enchantment causes this modifier to be invoked, via the smelting loot_modifier json
	 *
	 */
	/*private static class SmeltingEnchantmentModifier extends LootModifier {
		public SmeltingEnchantmentModifier(ILootCondition[] conditionsIn) {
			super(conditionsIn);
		}
		
		@Nonnull
		@Override
		public List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
			ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
			generatedLoot.forEach((stack) -> ret.add(smelt(stack, context)));
			return ret;
		}
		
		private static ItemStack smelt(ItemStack stack, LootContext context) {
			return context.getLevel().getRecipeManager().getRecipeFor(IRecipeType.SMELTING, new Inventory(stack), context.getLevel())
					.map(FurnaceRecipe::getResultItem)
					.filter(itemStack -> !itemStack.isEmpty())
					.map(itemStack -> ItemHandlerHelper.copyStackWithSize(itemStack, stack.getCount() * itemStack.getCount()))
					.orElse(stack);
		}
		
		private static class Serializer extends GlobalLootModifierSerializer<SmeltingEnchantmentModifier> {
			@Override
			public SmeltingEnchantmentModifier read(ResourceLocation name, JsonObject json, ILootCondition[] conditionsIn) {
				return new SmeltingEnchantmentModifier(conditionsIn);
			}
			
			@Override
			public JsonObject write(SmeltingEnchantmentModifier instance) {
				return makeConditions(instance.conditions);
			}
		}
	}
	
	/**
	 * When harvesting blocks with bamboo, this modifier is invoked, via the silk_touch_bamboo loot_modifier json
	 *
	 */
	/*private static class SilkTouchTestModifier extends LootModifier {
		public SilkTouchTestModifier(ILootCondition[] conditionsIn) {
			super(conditionsIn);
		}
		
		@Nonnull
		@Override
		public List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
			ItemStack ctxTool = context.getParamOrNull(LootParameters.TOOL);
			//return early if silk-touch is already applied (otherwise we'll get stuck in an infinite loop).
			if(EnchantmentHelper.getEnchantments(ctxTool).containsKey(Enchantments.SILK_TOUCH)) return generatedLoot;
			ItemStack fakeTool = ctxTool.copy();
			fakeTool.enchant(Enchantments.SILK_TOUCH, 1);
			LootContext.Builder builder = new LootContext.Builder(context);
			builder.withParameter(LootParameters.TOOL, fakeTool);
			LootContext ctx = builder.create(LootParameterSets.BLOCK);
			LootTable loottable = context.getLevel().getServer().getLootTables().get(context.getParamOrNull(LootParameters.BLOCK_STATE).getBlock().getLootTable());
			return loottable.getRandomItems(ctx);
		}
		
		private static class Serializer extends GlobalLootModifierSerializer<SilkTouchTestModifier> {
			@Override
			public SilkTouchTestModifier read(ResourceLocation name, JsonObject json, ILootCondition[] conditionsIn) {
				return new SilkTouchTestModifier(conditionsIn);
			}
			
			@Override
			public JsonObject write(SilkTouchTestModifier instance) {
				return makeConditions(instance.conditions);
			}
		}
	}
	
	/**
	 * When harvesting wheat with shears, this modifier is invoked via the wheat_harvest loot_modifier json<br/>
	 * This modifier checks how many seeds were harvested and turns X seeds into Y wheat (3:1)
	 *
	 */
	/*private static class WheatSeedsConverterModifier extends LootModifier {
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
			for(ItemStack stack : generatedLoot) {
				if(stack.getItem() == itemToCheck)
					numSeeds+=stack.getCount();
			}
			if(numSeeds >= numSeedsToConvert) {
				generatedLoot.removeIf(x -> x.getItem() == itemToCheck);
				generatedLoot.add(new ItemStack(itemReward, (numSeeds/numSeedsToConvert)));
				numSeeds = numSeeds%numSeedsToConvert;
				if(numSeeds > 0)
					generatedLoot.add(new ItemStack(itemToCheck, numSeeds));
			}
			return generatedLoot;
		}
		
		private static class Serializer extends GlobalLootModifierSerializer<WheatSeedsConverterModifier> {
			
			@Override
			public WheatSeedsConverterModifier read(ResourceLocation name, JsonObject object, ILootCondition[] conditionsIn) {
				int numSeeds = JSONUtils.getAsInt(object, "numSeeds");
				Item seed = ForgeRegistries.ITEMS.getValue(new ResourceLocation((JSONUtils.getAsString(object, "seedItem"))));
				Item wheat = ForgeRegistries.ITEMS.getValue(new ResourceLocation(JSONUtils.getAsString(object, "replacement")));
				return new WheatSeedsConverterModifier(conditionsIn, numSeeds, seed, wheat);
			}
			
			@Override
			public JsonObject write(WheatSeedsConverterModifier instance) {
				JsonObject json = makeConditions(instance.conditions);
				json.addProperty("numSeeds", instance.numSeedsToConvert);
				json.addProperty("seedItem", ForgeRegistries.ITEMS.getKey(instance.itemToCheck).toString());
				json.addProperty("replacement", ForgeRegistries.ITEMS.getKey(instance.itemReward).toString());
				return json;
			}
		}
	}
	
	private static class DungeonLootEnhancerModifier extends LootModifier {
		private final int multiplicationFactor;
		
		public DungeonLootEnhancerModifier(final ILootCondition[] conditionsIn, final int multiplicationFactor) {
			super(conditionsIn);
			this.multiplicationFactor = multiplicationFactor;
		}
		
		public DungeonLootEnhancerModifier(final ILootCondition[] conditionsIn) {
			this(conditionsIn, 2);
		}
		
		@Override
		protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
			return generatedLoot.stream()
					.map(ItemStack::copy)
					.peek(stack -> stack.setCount(Math.min(stack.getMaxStackSize(), stack.getCount() * this.multiplicationFactor)))
					.collect(Collectors.toList());
		}
		
		private static class Serializer extends GlobalLootModifierSerializer<DungeonLootEnhancerModifier> {
			@Override
			public DungeonLootEnhancerModifier read(ResourceLocation location, JsonObject object, ILootCondition[] conditions) {
				final int multiplicationFactor = JSONUtils.getAsInt(object, "multiplication_factor", 2);
				if (multiplicationFactor <= 0) throw new JsonParseException("Unable to set a multiplication factor to a number lower than 1");
				return new DungeonLootEnhancerModifier(conditions, multiplicationFactor);
			}
			
			@Override
			public JsonObject write(DungeonLootEnhancerModifier instance) {
				final JsonObject obj = this.makeConditions(instance.conditions);
				obj.addProperty("multiplication_factor", instance.multiplicationFactor);
				return obj;
			}
		}
	}*//*
}*/

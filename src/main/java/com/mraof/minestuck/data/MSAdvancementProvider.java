package com.mraof.minestuck.data;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.advancements.*;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.inventory.captchalogue.ModusType;
import com.mraof.minestuck.inventory.captchalogue.ModusTypes;
import com.mraof.minestuck.item.BoondollarsItem;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.ColorHandler;
import com.mraof.minestuck.util.MSTags;
import com.mraof.minestuck.world.gen.structure.MSStructures;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class MSAdvancementProvider implements AdvancementProvider.AdvancementGenerator
{
	public static final String ROOT = "minestuck.root";
	public static final String SEARCHING = "minestuck.searching";
	public static final String LONG_TIME_COMING = "minestuck.long_time_coming";
	public static final String CONNECT = "minestuck.connect";
	public static final String ENTRY = "minestuck.entry";
	public static final String ALCHEMY = "minestuck.alchemy";
	public static final String NEW_MODUS = "minestuck.new_modus";
	public static final String ALL_MODI = "minestuck.all_modi";
	public static final String GOLD_SEEDS = "minestuck.gold_seeds";
	public static final String FRENCH_FRY = "minestuck.french_fry";
	public static final String MELON_OVERLOAD = "minestuck.melon_overload";
	public static final String TREE_MODUS = "minestuck.tree_modus";
	public static final String KILL_OGRE = "minestuck.kill_ogre";
	public static final String RETURN_NODE = "minestuck.return_node";
	public static final String DUNGEON = "minestuck.dungeon";
	public static final String COMMUNE = "minestuck.commune";
	public static final String BUGS = "minestuck.bugs";
	public static final String SHADY_BUYER = "minestuck.shady_buyer";
	public static final String FIRST_STEP = "minestuck.first_step";
	public static final String DOUBLE_DIGITS = "minestuck.double_digits";
	public static final String HALFWAY_POINT = "minestuck.halfway_point";
	public static final String BIG_ONE_MIL = "minestuck.big_one_mil";
	public static final String INTELLIBEAM = "minestuck.intellibeam";
	public static final String LEGENDARY_WEAPON = "minestuck.legendary_weapon";
	public static final String BUY_OUT_SHOP = "minestuck.buy_out_shop";
	
	public static DataProvider create(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper)
	{
		return new AdvancementProvider(output, registries, existingFileHelper, List.of(new MSAdvancementProvider()));
	}
	
	@SuppressWarnings("unused")
	@Override
	public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper)
	{
		AdvancementHolder root = Advancement.Builder.advancement()
				.display(MSItems.RAW_CRUXITE.get(), Component.translatable(title(ROOT)), Component.translatable(desc(ROOT)), new ResourceLocation("minestuck:textures/gui/advancement_bg.png"), AdvancementType.TASK, false, false, false)
				.addCriterion("raw_cruxite", InventoryChangeTrigger.TriggerInstance.hasItems(MSItems.RAW_CRUXITE.get())).save(saver, save_loc(ROOT));
		AdvancementHolder searching = Advancement.Builder.advancement().parent(root)
				.display(Items.COMPASS, Component.translatable(title(SEARCHING)), Component.translatable(desc(SEARCHING)), null, AdvancementType.TASK, true, true, false)
				.addCriterion("possess_scanner", InventoryChangeTrigger.TriggerInstance.hasItems(MSItems.TEMPLE_SCANNER.get())).save(saver, save_loc(SEARCHING));
		AdvancementHolder longTimeComing = Advancement.Builder.advancement().parent(root)
				.display(MSItems.SBURB_CODE.get(), Component.translatable(title(LONG_TIME_COMING)), Component.translatable(desc(LONG_TIME_COMING)), null, AdvancementType.TASK, true, true, false)
				.addCriterion("possess_code", InventoryChangeTrigger.TriggerInstance.hasItems(MSItems.SBURB_CODE.get())).save(saver, save_loc(LONG_TIME_COMING));
		AdvancementHolder connect = Advancement.Builder.advancement().parent(root)
				.display(MSItems.CLIENT_DISK.get(), Component.translatable(title(CONNECT)), Component.translatable(desc(CONNECT)), null, AdvancementType.TASK, true, true, false)
				.addCriterion("connection", EventTrigger.Instance.sburbConnection()).save(saver, save_loc(CONNECT));
		AdvancementHolder entry = Advancement.Builder.advancement().parent(connect)
				.display(ColorHandler.setDefaultColor(new ItemStack(MSItems.CRUXITE_APPLE.get())), Component.translatable(title(ENTRY)), Component.translatable(desc(ENTRY)), null, AdvancementType.TASK, true, true, false)
				.addCriterion("use_artifact", EventTrigger.Instance.cruxiteArtifact()).save(saver, save_loc(ENTRY));
		AdvancementHolder alchemy = Advancement.Builder.advancement().parent(entry)
				.display(MSItems.CAPTCHA_CARD.get(), Component.translatable(title(ALCHEMY)), Component.translatable(desc(ALCHEMY)), null, AdvancementType.TASK, true, true, false)
				.addCriterion("use_punch_designix", PunchDesignixTrigger.Instance.any()).save(saver, save_loc(ALCHEMY));
		AdvancementHolder newModus = Advancement.Builder.advancement().parent(alchemy)
				.display(MSItems.HASHMAP_MODUS_CARD.get(), Component.translatable(title(NEW_MODUS)), Component.translatable(desc(NEW_MODUS)), null, AdvancementType.TASK, true, true, false)
				.addCriterion("change_modus_type", ChangeModusTrigger.Instance.any()).save(saver, save_loc(NEW_MODUS));
		AdvancementHolder allModi = changeModusCriteria(Advancement.Builder.advancement().parent(newModus)
				.display(MSItems.QUEUESTACK_MODUS_CARD.get(), Component.translatable(title(ALL_MODI)), Component.translatable(desc(ALL_MODI)), null, AdvancementType.GOAL, true, true, false)
				.requirements(AdvancementRequirements.Strategy.AND)).save(saver, save_loc(ALL_MODI));
		AdvancementHolder goldSeeds = Advancement.Builder.advancement().parent(alchemy)
				.display(MSBlocks.GOLD_SEEDS.get(), Component.translatable(title(GOLD_SEEDS)), Component.translatable(desc(GOLD_SEEDS)), null, AdvancementType.TASK, true, true, false)
				.addCriterion("plant_gold_seeds", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(MSBlocks.GOLD_SEEDS.get())).save(saver, save_loc(GOLD_SEEDS));
		AdvancementHolder melonOverload = Advancement.Builder.advancement().parent(alchemy)
				.display(MSItems.MELONSBANE.get(), Component.translatable(title(MELON_OVERLOAD)), Component.translatable(desc(MELON_OVERLOAD)), null, AdvancementType.TASK, true, true, true)
				.addCriterion("melon_overload", EventTrigger.Instance.melonOverload()).save(saver, save_loc(MELON_OVERLOAD));
		AdvancementHolder treeModus = Advancement.Builder.advancement().parent(newModus)
				.display(MSItems.TREE_MODUS_CARD.get(), Component.translatable(title(TREE_MODUS)), Component.translatable(desc(TREE_MODUS)), null, AdvancementType.TASK, true, true, false)
				.addCriterion("tree_root", TreeModusRootTrigger.Instance.count(MinMaxBounds.Ints.atLeast(16))).save(saver, save_loc(TREE_MODUS));
		AdvancementHolder killOgre = Advancement.Builder.advancement().parent(entry)
				.display(MSItems.POGO_HAMMER.get(), Component.translatable(title(KILL_OGRE)), Component.translatable(desc(KILL_OGRE)), null, AdvancementType.TASK, true, true, false)
				.addCriterion("kill_ogre", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(MSEntityTypes.OGRE.get()))).save(saver, save_loc(KILL_OGRE));
		AdvancementHolder returnNode = Advancement.Builder.advancement().parent(entry)
				.display(Items.RED_BED, Component.translatable(title(RETURN_NODE)), Component.translatable(desc(RETURN_NODE)), null, AdvancementType.TASK, true, true, false)
				.addCriterion("touch_return_node", EventTrigger.Instance.returnNode()).save(saver, save_loc(RETURN_NODE));
		AdvancementHolder dungeon = Advancement.Builder.advancement().parent(returnNode)
				.display(MSBlocks.FROST_BRICKS.get(), Component.translatable(title(DUNGEON)), Component.translatable(desc(DUNGEON)), null, AdvancementType.TASK, true, true, false)
				.addCriterion("imp_dungeon", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure(MSStructures.ImpDungeon.KEY))).save(saver, save_loc(DUNGEON));
		AdvancementHolder commune = Advancement.Builder.advancement().parent(entry)
				.display(MSItems.STONE_TABLET.get(), Component.translatable(title(COMMUNE)), Component.translatable(desc(COMMUNE)), null, AdvancementType.TASK, true, true, false)
				.requirements(AdvancementRequirements.Strategy.AND).addCriterion("talk_to_consort", ConsortTalkTrigger.Instance.any()).addCriterion("visit_village", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure(MSStructures.ConsortVillage.KEY))).save(saver, save_loc(COMMUNE));
		AdvancementHolder frenchFry = Advancement.Builder.advancement().parent(commune)
				.display(MSItems.FRENCH_FRY.get(), Component.translatable(title(FRENCH_FRY)), Component.translatable(desc(FRENCH_FRY)), null, AdvancementType.TASK, true, true, false)
				.addCriterion("has_french_fry", ConsumeItemTrigger.TriggerInstance.usedItem(MSItems.FRENCH_FRY.get())).save(saver, save_loc(FRENCH_FRY));
		AdvancementHolder bugs = consumeBugCriteria(Advancement.Builder.advancement()
				.parent(commune).display(MSItems.CHOCOLATE_BEETLE.get(), Component.translatable(title(BUGS)), Component.translatable(desc(BUGS)), null, AdvancementType.TASK, true, true, false)
				.requirements(AdvancementRequirements.Strategy.OR)).save(saver, save_loc(BUGS));
		AdvancementHolder shadyBuyer = Advancement.Builder.advancement().parent(commune)
				.display(MSItems.ROCK_COOKIE.get(), Component.translatable(title(SHADY_BUYER)), Component.translatable(desc(SHADY_BUYER)), null, AdvancementType.TASK, true, true, false)
				.addCriterion("buy_item", ConsortItemTrigger.Instance.forType(EnumConsort.MerchantType.SHADY)).save(saver, save_loc(SHADY_BUYER));
		AdvancementHolder firstStep = Advancement.Builder.advancement().parent(root)
				.display(BoondollarsItem.setCount(new ItemStack(MSItems.BOONDOLLARS.get()), 1), Component.translatable(title(FIRST_STEP)), Component.translatable(desc(FIRST_STEP)), null, AdvancementType.TASK, true, true, false)
				.addCriterion("reach_rung", EcheladderTrigger.Instance.rung(MinMaxBounds.Ints.atLeast(1))).save(saver, save_loc(FIRST_STEP));
		AdvancementHolder doubleDigits = Advancement.Builder.advancement().parent(firstStep)
				.display(BoondollarsItem.setCount(new ItemStack(MSItems.BOONDOLLARS.get()), 100), Component.translatable(title(DOUBLE_DIGITS)), Component.translatable(desc(DOUBLE_DIGITS)), null, AdvancementType.TASK, true, true, false)
				.addCriterion("reach_rung", EcheladderTrigger.Instance.rung(MinMaxBounds.Ints.atLeast(10))).save(saver, save_loc(DOUBLE_DIGITS));
		AdvancementHolder halfwayPoint = Advancement.Builder.advancement().parent(doubleDigits)
				.display(BoondollarsItem.setCount(new ItemStack(MSItems.BOONDOLLARS.get()), 1000), Component.translatable(title(HALFWAY_POINT)), Component.translatable(desc(HALFWAY_POINT)), null, AdvancementType.TASK, true, true, false)
				.addCriterion("reach_rung", EcheladderTrigger.Instance.rung(MinMaxBounds.Ints.atLeast(25))).save(saver, save_loc(HALFWAY_POINT));
		AdvancementHolder bigOneMil = Advancement.Builder.advancement().parent(halfwayPoint)
				.display(BoondollarsItem.setCount(new ItemStack(MSItems.BOONDOLLARS.get()), 1_000_000), Component.translatable(title(BIG_ONE_MIL)), Component.translatable(desc(BIG_ONE_MIL)), null, AdvancementType.GOAL, true, true, false)
				.addCriterion("reach_rung", EcheladderTrigger.Instance.rung(MinMaxBounds.Ints.atLeast(44))).save(saver, save_loc(BIG_ONE_MIL));
		AdvancementHolder intellibeam = Advancement.Builder.advancement().parent(alchemy)
				.display(MSItems.INTELLIBEAM_LASERSTATION.get(), Component.translatable(title(INTELLIBEAM)), Component.translatable(desc(INTELLIBEAM)), null, AdvancementType.TASK, true, true, false)
				.addCriterion("use_intellibeam", IntellibeamLaserstationTrigger.Instance.any()).save(saver, save_loc(INTELLIBEAM));
		AdvancementHolder strongWeapon = Advancement.Builder.advancement().parent(alchemy)
				.display(MSItems.CALEDFWLCH.get(), Component.translatable(title(LEGENDARY_WEAPON)), Component.translatable(desc(LEGENDARY_WEAPON)), null, AdvancementType.TASK, true, true, false)
				.addCriterion("get_max_tier_weapon", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(MSTags.Items.LEGENDARY).build())).save(saver, save_loc(LEGENDARY_WEAPON));
		AdvancementHolder buyOutShop = Advancement.Builder.advancement().parent(commune)
				.display(MSItems.CONE_OF_FLIES.get(), Component.translatable(title(BUY_OUT_SHOP)), Component.translatable(desc(BUY_OUT_SHOP)), null, AdvancementType.TASK, true, true, false)
				.addCriterion("buy_everything", EventTrigger.Instance.buyOutShop()).save(saver, save_loc(BUY_OUT_SHOP));
	}
	
	private static Advancement.Builder changeModusCriteria(Advancement.Builder builder)
	{
		for(Supplier<? extends ModusType<?>> type : Arrays.asList(ModusTypes.STACK, ModusTypes.QUEUE, ModusTypes.QUEUE_STACK, ModusTypes.TREE, ModusTypes.HASH_MAP, ModusTypes.SET))
		{
			ResourceLocation id = Objects.requireNonNull(ModusTypes.REGISTRY.getKey(type.get()));
			builder = builder.addCriterion(id.getPath(), InventoryChangeTrigger.TriggerInstance.hasItems(type.get().getItem()));
		}
		return builder;
	}
	
	private static Advancement.Builder consumeBugCriteria(Advancement.Builder builder)
	{
		for(ItemLike item : Arrays.asList(MSItems.BUG_ON_A_STICK.get(), MSItems.CHOCOLATE_BEETLE.get(), MSItems.CONE_OF_FLIES.get(), MSItems.GRASSHOPPER.get(), MSItems.JAR_OF_BUGS.get()))
		{
			builder = builder.addCriterion(BuiltInRegistries.ITEM.getKey(item.asItem()).getPath(), ConsumeItemTrigger.TriggerInstance.usedItem(item));
		}
		return builder;
	}
	
	private static String title(String name)
	{
		return "advancements."+name+".title";
	}
	private static String desc(String name)
	{
		return "advancements."+name+".description";
	}
	private static String save_loc(String name)
	{
		return Minestuck.MOD_ID + ":" + name.replace('.', '/');
	}
}

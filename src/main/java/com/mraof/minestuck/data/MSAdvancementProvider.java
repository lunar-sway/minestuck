package com.mraof.minestuck.data;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.advancements.*;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.inventory.captchalogue.ModusType;
import com.mraof.minestuck.inventory.captchalogue.ModusTypes;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.ColorHandler;
import com.mraof.minestuck.world.gen.structure.MSConfiguredStructures;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.function.Consumer;

public class MSAdvancementProvider extends AdvancementProvider
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
	
	public MSAdvancementProvider(DataGenerator generator, ExistingFileHelper fileHelper)
	{
		super(generator, fileHelper);
	}
	
	@SuppressWarnings("unused")
	@Override
	protected void registerAdvancements(Consumer<Advancement> advancementSaver, ExistingFileHelper fileHelper)
	{
		Advancement root = Advancement.Builder.advancement().display(MSItems.RAW_CRUXITE.get(), Component.translatable(title(ROOT)), Component.translatable(desc(ROOT)), new ResourceLocation("minestuck:textures/gui/advancement_bg.png"), FrameType.TASK, false, false, false).addCriterion("raw_cruxite", InventoryChangeTrigger.TriggerInstance.hasItems(MSItems.RAW_CRUXITE.get())).save(advancementSaver, Minestuck.MOD_ID+":minestuck/root");
		Advancement searching = Advancement.Builder.advancement().parent(root).display(Items.COMPASS, Component.translatable(title(SEARCHING)), Component.translatable(desc(SEARCHING)), null, FrameType.TASK, true, true, false).addCriterion("possess_scanner", InventoryChangeTrigger.TriggerInstance.hasItems(MSItems.TEMPLE_SCANNER.get())).save(advancementSaver, Minestuck.MOD_ID+":minestuck/searching");
		Advancement longTimeComing = Advancement.Builder.advancement().parent(root).display(MSItems.SBURB_CODE.get(), Component.translatable(title(LONG_TIME_COMING)), Component.translatable(desc(LONG_TIME_COMING)), null, FrameType.TASK, true, true, false).addCriterion("possess_code", InventoryChangeTrigger.TriggerInstance.hasItems(MSItems.SBURB_CODE.get())).save(advancementSaver, Minestuck.MOD_ID+":minestuck/long_time_coming");
		Advancement connect = Advancement.Builder.advancement().parent(root).display(MSItems.CLIENT_DISK.get(), Component.translatable(title(CONNECT)), Component.translatable(desc(CONNECT)), null, FrameType.TASK, true, true, false).addCriterion("connection", EventTrigger.Instance.sburbConnection()).save(advancementSaver, Minestuck.MOD_ID+":minestuck/connect");
		Advancement entry = Advancement.Builder.advancement().parent(connect).display(ColorHandler.setDefaultColor(new ItemStack(MSItems.CRUXITE_APPLE.get())), Component.translatable(title(ENTRY)), Component.translatable(desc(ENTRY)), null, FrameType.TASK, true, true, false).addCriterion("use_artifact", EventTrigger.Instance.cruxiteArtifact()).save(advancementSaver, Minestuck.MOD_ID+":minestuck/entry");
		Advancement alchemy = Advancement.Builder.advancement().parent(entry).display(MSItems.CAPTCHA_CARD.get(), Component.translatable(title(ALCHEMY)), Component.translatable(desc(ALCHEMY)), null, FrameType.TASK, true, true, false).addCriterion("use_punch_designix", PunchDesignixTrigger.Instance.any()).save(advancementSaver, Minestuck.MOD_ID+":minestuck/alchemy");
		Advancement newModus = Advancement.Builder.advancement().parent(alchemy).display(MSItems.HASHMAP_MODUS_CARD.get(), Component.translatable(title(NEW_MODUS)), Component.translatable(desc(NEW_MODUS)), null, FrameType.TASK, true, true, false).addCriterion("change_modus_type", ChangeModusTrigger.Instance.any()).save(advancementSaver, Minestuck.MOD_ID+":minestuck/new_modus");
		Advancement allModi = changeModusCriteria(Advancement.Builder.advancement().parent(newModus).display(MSItems.QUEUESTACK_MODUS_CARD.get(), Component.translatable(title(ALL_MODI)), Component.translatable(desc(ALL_MODI)), null, FrameType.TASK, true, true, false).requirements(RequirementsStrategy.AND)).save(advancementSaver, Minestuck.MOD_ID+":minestuck/all_modi");
		Advancement goldSeeds = Advancement.Builder.advancement().parent(alchemy).display(MSBlocks.GOLD_SEEDS.get(), Component.translatable(title(GOLD_SEEDS)), Component.translatable(desc(GOLD_SEEDS)), null, FrameType.TASK, true, true, false).addCriterion("plant_gold_seeds", PlacedBlockTrigger.TriggerInstance.placedBlock(MSBlocks.GOLD_SEEDS.get())).save(advancementSaver, Minestuck.MOD_ID+":minestuck/gold_seeds");
		Advancement frenchFry = Advancement.Builder.advancement().parent(alchemy).display(MSItems.FRENCH_FRY.get(), Component.translatable(title(FRENCH_FRY)), Component.translatable(desc(FRENCH_FRY)), null, FrameType.TASK, true, true, false).addCriterion("has_french_fry", ConsumeItemTrigger.TriggerInstance.usedItem(MSItems.FRENCH_FRY.get())).save(advancementSaver, Minestuck.MOD_ID+":minestuck/french_fry");
		Advancement melonOverload = Advancement.Builder.advancement().parent(alchemy).display(MSItems.MELONSBANE.get(), Component.translatable(title(MELON_OVERLOAD)), Component.translatable(desc(MELON_OVERLOAD)), null, FrameType.TASK, true, true, true).addCriterion("melon_overload", EventTrigger.Instance.melonOverload()).save(advancementSaver, Minestuck.MOD_ID+":minestuck/melon_overload");
		Advancement treeModus = Advancement.Builder.advancement().parent(newModus).display(MSItems.TREE_MODUS_CARD.get(), Component.translatable(title(TREE_MODUS)), Component.translatable(desc(TREE_MODUS)), null, FrameType.TASK, true, true, false).addCriterion("tree_root", TreeModusRootTrigger.Instance.count(MinMaxBounds.Ints.atLeast(16))).save(advancementSaver, Minestuck.MOD_ID+":minestuck/tree_modus");
		Advancement killOgre = Advancement.Builder.advancement().parent(entry).display(MSItems.POGO_HAMMER.get(), Component.translatable(title(KILL_OGRE)), Component.translatable(desc(KILL_OGRE)), null, FrameType.TASK, true, true, false).addCriterion("kill_ogre", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(MSEntityTypes.OGRE.get()))).save(advancementSaver, Minestuck.MOD_ID+":minestuck/kill_ogre");
		Advancement returnNode = Advancement.Builder.advancement().parent(entry).display(Items.RED_BED, Component.translatable(title(RETURN_NODE)), Component.translatable(desc(RETURN_NODE)), null, FrameType.TASK, true, true, false).addCriterion("touch_return_node", EventTrigger.Instance.returnNode()).save(advancementSaver, Minestuck.MOD_ID+":minestuck/return_node");
		Advancement dungeon = Advancement.Builder.advancement().parent(returnNode).display(MSBlocks.FROST_BRICKS.get(), Component.translatable(title(DUNGEON)), Component.translatable(desc(DUNGEON)), null, FrameType.TASK, true, true, false).addCriterion("imp_dungeon", PlayerTrigger.TriggerInstance.located(LocationPredicate.inStructure(MSConfiguredStructures.IMP_DUNGEON))).save(advancementSaver, Minestuck.MOD_ID+":minestuck/dungeon");
		Advancement commune = Advancement.Builder.advancement().parent(entry).display(MSItems.STONE_TABLET.get(), Component.translatable(title(COMMUNE)), Component.translatable(desc(COMMUNE)), null, FrameType.TASK, true, true, false).requirements(RequirementsStrategy.AND).addCriterion("talk_to_consort", ConsortTalkTrigger.Instance.any()).addCriterion("visit_village", PlayerTrigger.TriggerInstance.located(LocationPredicate.inStructure(MSConfiguredStructures.CONSORT_VILLAGE))).save(advancementSaver, Minestuck.MOD_ID+":minestuck/commune");
		Advancement bugs = consumeBugCriteria(Advancement.Builder.advancement().parent(commune).display(MSItems.CHOCOLATE_BEETLE.get(), Component.translatable(title(BUGS)), Component.translatable(desc(BUGS)), null, FrameType.TASK, true, true, false).requirements(RequirementsStrategy.OR)).save(advancementSaver, Minestuck.MOD_ID+":minestuck/bugs");
		Advancement shadyBuyer = Advancement.Builder.advancement().parent(commune).display(MSItems.ROCK_COOKIE.get(), Component.translatable(title(SHADY_BUYER)), Component.translatable(desc(SHADY_BUYER)), null, FrameType.TASK, true, true, false).addCriterion("buy_item", ConsortItemTrigger.Instance.forType(EnumConsort.MerchantType.SHADY)).save(advancementSaver, Minestuck.MOD_ID+":minestuck/shady_buyer");
	}
	
	private static Advancement.Builder changeModusCriteria(Advancement.Builder builder)
	{
		for(RegistryObject<? extends ModusType<?>> type : Arrays.asList(ModusTypes.STACK, ModusTypes.QUEUE, ModusTypes.QUEUE_STACK, ModusTypes.TREE, ModusTypes.HASH_MAP, ModusTypes.SET))
		{
			builder = builder.addCriterion(type.getId().getPath(), InventoryChangeTrigger.TriggerInstance.hasItems(type.get().getItem()));
		}
		return builder;
	}
	
	private static Advancement.Builder consumeBugCriteria(Advancement.Builder builder)
	{
		for(ItemLike item : Arrays.asList(MSItems.BUG_ON_A_STICK.get(), MSItems.CHOCOLATE_BEETLE.get(), MSItems.CONE_OF_FLIES.get(), MSItems.GRASSHOPPER.get(), MSItems.JAR_OF_BUGS.get()))
		{
			builder = builder.addCriterion(ForgeRegistries.ITEMS.getKey(item.asItem()).getPath(), ConsumeItemTrigger.TriggerInstance.usedItem(item));
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
	
	@Override
	public String getName()
	{
		return "Minestuck Advancements";
	}
}
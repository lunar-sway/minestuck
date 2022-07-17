package com.mraof.minestuck.data;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.advancements.*;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.inventory.captchalogue.ModusType;
import com.mraof.minestuck.inventory.captchalogue.ModusTypes;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.ColorHandler;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.gen.feature.structure.MSStructureFeatures;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

public class MSAdvancementProvider implements DataProvider
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
	
	private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
	private final DataGenerator generator;
	
	public MSAdvancementProvider(DataGenerator generator)
	{
		this.generator = generator;
	}
	
	protected void buildAdvancements(Consumer<Advancement> advancementSaver)
	{
		Advancement root = Advancement.Builder.advancement().display(MSItems.RAW_CRUXITE, new TranslatableComponent(title(ROOT)), new TranslatableComponent(desc(ROOT)), new ResourceLocation("minestuck:textures/gui/advancement_bg.png"), FrameType.TASK, false, false, false).addCriterion("raw_cruxite", InventoryChangeTrigger.TriggerInstance.hasItems(MSItems.RAW_CRUXITE)).save(advancementSaver, Minestuck.MOD_ID+":minestuck/root");
		Advancement searching = Advancement.Builder.advancement().parent(root).display(Items.COMPASS, new TranslatableComponent(title(SEARCHING)), new TranslatableComponent(desc(SEARCHING)), null, FrameType.TASK, true, true, false).addCriterion("possess_scanner", InventoryChangeTrigger.TriggerInstance.hasItems(MSItems.TEMPLE_SCANNER)).save(advancementSaver, Minestuck.MOD_ID+":minestuck/searching");
		Advancement longTimeComing = Advancement.Builder.advancement().parent(root).display(MSItems.SBURB_CODE, new TranslatableComponent(title(LONG_TIME_COMING)), new TranslatableComponent(desc(LONG_TIME_COMING)), null, FrameType.TASK, true, true, false).addCriterion("possess_code", InventoryChangeTrigger.TriggerInstance.hasItems(MSItems.SBURB_CODE)).save(advancementSaver, Minestuck.MOD_ID+":minestuck/long_time_coming");
		Advancement connect = Advancement.Builder.advancement().parent(root).display(MSItems.CLIENT_DISK, new TranslatableComponent(title(CONNECT)), new TranslatableComponent(desc(CONNECT)), null, FrameType.TASK, true, true, false).addCriterion("connection", EventTrigger.Instance.sburbConnection()).save(advancementSaver, Minestuck.MOD_ID+":minestuck/connect");
		Advancement entry = Advancement.Builder.advancement().parent(connect).display(ColorHandler.setDefaultColor(new ItemStack(MSItems.CRUXITE_APPLE)), new TranslatableComponent(title(ENTRY)), new TranslatableComponent(desc(ENTRY)), null, FrameType.TASK, true, true, false).addCriterion("use_artifact", EventTrigger.Instance.cruxiteArtifact()).save(advancementSaver, Minestuck.MOD_ID+":minestuck/entry");
		Advancement alchemy = Advancement.Builder.advancement().parent(entry).display(MSItems.CAPTCHA_CARD, new TranslatableComponent(title(ALCHEMY)), new TranslatableComponent(desc(ALCHEMY)), null, FrameType.TASK, true, true, false).addCriterion("use_punch_designix", PunchDesignixTrigger.Instance.any()).save(advancementSaver, Minestuck.MOD_ID+":minestuck/alchemy");
		Advancement newModus = Advancement.Builder.advancement().parent(alchemy).display(MSItems.HASHMAP_MODUS_CARD, new TranslatableComponent(title(NEW_MODUS)), new TranslatableComponent(desc(NEW_MODUS)), null, FrameType.TASK, true, true, false).addCriterion("change_modus_type", ChangeModusTrigger.Instance.any()).save(advancementSaver, Minestuck.MOD_ID+":minestuck/new_modus");
		Advancement allModi = changeModusCriteria(Advancement.Builder.advancement().parent(newModus).display(MSItems.QUEUESTACK_MODUS_CARD, new TranslatableComponent(title(ALL_MODI)), new TranslatableComponent(desc(ALL_MODI)), null, FrameType.TASK, true, true, false).requirements(RequirementsStrategy.AND)).save(advancementSaver, Minestuck.MOD_ID+":minestuck/all_modi");
		Advancement goldSeeds = Advancement.Builder.advancement().parent(alchemy).display(MSBlocks.GOLD_SEEDS, new TranslatableComponent(title(GOLD_SEEDS)), new TranslatableComponent(desc(GOLD_SEEDS)), null, FrameType.TASK, true, true, false).addCriterion("plant_gold_seeds", PlacedBlockTrigger.TriggerInstance.placedBlock(MSBlocks.GOLD_SEEDS)).save(advancementSaver, Minestuck.MOD_ID+":minestuck/gold_seeds");
		Advancement frenchFry = Advancement.Builder.advancement().parent(alchemy).display(MSItems.FRENCH_FRY, new TranslatableComponent(title(FRENCH_FRY)), new TranslatableComponent(desc(FRENCH_FRY)), null, FrameType.TASK, true, true, false).addCriterion("has_french_fry", ConsumeItemTrigger.TriggerInstance.usedItem(MSItems.FRENCH_FRY)).save(advancementSaver, Minestuck.MOD_ID+":minestuck/french_fry");
		Advancement melonOverload = Advancement.Builder.advancement().parent(alchemy).display(MSItems.MELONSBANE, new TranslatableComponent(title(MELON_OVERLOAD)), new TranslatableComponent(desc(MELON_OVERLOAD)), null, FrameType.TASK, true, true, true).addCriterion("melon_overload", EventTrigger.Instance.melonOverload()).save(advancementSaver, Minestuck.MOD_ID+":minestuck/melon_overload");
		Advancement treeModus = Advancement.Builder.advancement().parent(newModus).display(MSItems.TREE_MODUS_CARD, new TranslatableComponent(title(TREE_MODUS)), new TranslatableComponent(desc(TREE_MODUS)), null, FrameType.TASK, true, true, false).addCriterion("tree_root", TreeModusRootTrigger.Instance.count(MinMaxBounds.Ints.atLeast(16))).save(advancementSaver, Minestuck.MOD_ID+":minestuck/tree_modus");
		Advancement killOgre = Advancement.Builder.advancement().parent(entry).display(MSItems.POGO_HAMMER, new TranslatableComponent(title(KILL_OGRE)), new TranslatableComponent(desc(KILL_OGRE)), null, FrameType.TASK, true, true, false).addCriterion("kill_ogre", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(MSEntityTypes.OGRE))).save(advancementSaver, Minestuck.MOD_ID+":minestuck/kill_ogre");
		Advancement returnNode = Advancement.Builder.advancement().parent(entry).display(Items.RED_BED, new TranslatableComponent(title(RETURN_NODE)), new TranslatableComponent(desc(RETURN_NODE)), null, FrameType.TASK, true, true, false).addCriterion("touch_return_node", EnterBlockTrigger.TriggerInstance.entersBlock(MSBlocks.RETURN_NODE)).save(advancementSaver, Minestuck.MOD_ID+":minestuck/return_node");
		Advancement dungeon = Advancement.Builder.advancement().parent(returnNode).display(MSBlocks.FROST_BRICKS, new TranslatableComponent(title(DUNGEON)), new TranslatableComponent(desc(DUNGEON)), null, FrameType.TASK, true, true, false).addCriterion("imp_dungeon", LocationTrigger.TriggerInstance.located(LocationPredicate.inFeature(Objects.requireNonNull(MSStructureFeatures.IMP_DUNGEON.getKey())))).save(advancementSaver, Minestuck.MOD_ID+":minestuck/dungeon");
		Advancement commune = Advancement.Builder.advancement().parent(entry).display(MSItems.STONE_SLAB, new TranslatableComponent(title(COMMUNE)), new TranslatableComponent(desc(COMMUNE)), null, FrameType.TASK, true, true, false).requirements(RequirementsStrategy.AND).addCriterion("talk_to_consort", ConsortTalkTrigger.Instance.any()).addCriterion("visit_village", LocationTrigger.TriggerInstance.located(LocationPredicate.inFeature(Objects.requireNonNull(MSStructureFeatures.CONSORT_VILLAGE.getKey())))).save(advancementSaver, Minestuck.MOD_ID+":minestuck/commune");
		Advancement bugs = consumeBugCriteria(Advancement.Builder.advancement().parent(commune).display(MSItems.CHOCOLATE_BEETLE, new TranslatableComponent(title(BUGS)), new TranslatableComponent(desc(BUGS)), null, FrameType.TASK, true, true, false).requirements(RequirementsStrategy.OR)).save(advancementSaver, Minestuck.MOD_ID+":minestuck/bugs");
		Advancement shadyBuyer = Advancement.Builder.advancement().parent(commune).display(MSItems.ROCK_COOKIE, new TranslatableComponent(title(SHADY_BUYER)), new TranslatableComponent(desc(SHADY_BUYER)), null, FrameType.TASK, true, true, false).addCriterion("buy_item", ConsortItemTrigger.Instance.forType(EnumConsort.MerchantType.SHADY)).save(advancementSaver, Minestuck.MOD_ID+":minestuck/shady_buyer");
	}
	
	private static Advancement.Builder changeModusCriteria(Advancement.Builder builder)
	{
		for(ModusType<?> type : Arrays.asList(ModusTypes.STACK, ModusTypes.QUEUE, ModusTypes.QUEUE_STACK, ModusTypes.TREE, ModusTypes.HASH_MAP, ModusTypes.SET))
		{
			builder = builder.addCriterion(type.getRegistryName().getPath(), InventoryChangeTrigger.TriggerInstance.hasItems(type.getItem()));
		}
		return builder;
	}
	
	private static Advancement.Builder consumeBugCriteria(Advancement.Builder builder)
	{
		for(ItemLike item : Arrays.asList(MSItems.BUG_ON_A_STICK, MSItems.CHOCOLATE_BEETLE, MSItems.CONE_OF_FLIES, MSItems.GRASSHOPPER, MSItems.JAR_OF_BUGS))
		{
			builder = builder.addCriterion(item.asItem().getRegistryName().getPath(), ConsumeItemTrigger.TriggerInstance.usedItem(item));
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
	public void run(HashCache cache)
	{
		Path outputFolder = generator.getOutputFolder();
		Set<ResourceLocation> savedAdvancements = Sets.newHashSet();
		
		buildAdvancements(advancement -> save(cache, outputFolder, savedAdvancements, advancement));
	}
	
	private static void save(HashCache cache, Path outputFolder, Set<ResourceLocation> savedAdvancements, Advancement advancement)
	{
		if(!savedAdvancements.add(advancement.getId()))
		{
			throw new IllegalStateException("Duplicate advancement " + advancement.getId());
		} else
		{
			Path jsonPath = outputFolder.resolve("data/" + advancement.getId().getNamespace() + "/advancements/" + advancement.getId().getPath() + ".json");
			
			try
			{
				DataProvider.save(GSON, cache, advancement.deconstruct().serializeToJson(), jsonPath);
			} catch(IOException e)
			{
				Debug.logger.error("Couldn't save advancement {}", jsonPath, e);
			}
		}
	}
	
	@Override
	public String getName()
	{
		return "Minestuck Advancements";
	}
}
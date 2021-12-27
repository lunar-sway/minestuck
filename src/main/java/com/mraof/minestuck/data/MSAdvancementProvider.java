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
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Consumer;

public class MSAdvancementProvider implements IDataProvider
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
	public static final String SURFNTURF = "minestuck.surf_n_turf";
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
		Advancement root = Advancement.Builder.advancement().display(MSItems.RAW_CRUXITE, new TranslationTextComponent(title(ROOT)), new TranslationTextComponent(desc(ROOT)), new ResourceLocation("minestuck:textures/gui/advancement_bg.png"), FrameType.TASK, false, false, false).addCriterion("raw_cruxite", InventoryChangeTrigger.Instance.hasItems(MSItems.RAW_CRUXITE)).save(advancementSaver, Minestuck.MOD_ID+":minestuck/root");
		Advancement searching = Advancement.Builder.advancement().parent(root).display(Items.COMPASS, new TranslationTextComponent(title(SEARCHING)), new TranslationTextComponent(desc(SEARCHING)), null, FrameType.TASK, true, true, false).addCriterion("possess_scanner", InventoryChangeTrigger.Instance.hasItems(MSItems.TEMPLE_SCANNER)).save(advancementSaver, Minestuck.MOD_ID+":minestuck/searching");
		Advancement longTimeComing = Advancement.Builder.advancement().parent(root).display(MSItems.SBURB_CODE, new TranslationTextComponent(title(LONG_TIME_COMING)), new TranslationTextComponent(desc(LONG_TIME_COMING)), null, FrameType.TASK, true, true, false).addCriterion("possess_code", InventoryChangeTrigger.Instance.hasItems(MSItems.SBURB_CODE)).save(advancementSaver, Minestuck.MOD_ID+":minestuck/long_time_coming");
		Advancement connect = Advancement.Builder.advancement().parent(root).display(MSItems.CLIENT_DISK, new TranslationTextComponent(title(CONNECT)), new TranslationTextComponent(desc(CONNECT)), null, FrameType.TASK, true, true, false).addCriterion("connection", EventTrigger.Instance.sburbConnection()).save(advancementSaver, Minestuck.MOD_ID+":minestuck/connect");
		Advancement entry = Advancement.Builder.advancement().parent(connect).display(ColorHandler.setDefaultColor(new ItemStack(MSItems.CRUXITE_APPLE)), new TranslationTextComponent(title(ENTRY)), new TranslationTextComponent(desc(ENTRY)), null, FrameType.TASK, true, true, false).addCriterion("use_artifact", EventTrigger.Instance.cruxiteArtifact()).save(advancementSaver, Minestuck.MOD_ID+":minestuck/entry");
		Advancement alchemy = Advancement.Builder.advancement().parent(entry).display(MSItems.CAPTCHA_CARD, new TranslationTextComponent(title(ALCHEMY)), new TranslationTextComponent(desc(ALCHEMY)), null, FrameType.TASK, true, true, false).addCriterion("use_punch_designix", PunchDesignixTrigger.Instance.any()).save(advancementSaver, Minestuck.MOD_ID+":minestuck/alchemy");
		Advancement newModus = Advancement.Builder.advancement().parent(alchemy).display(MSItems.HASHMAP_MODUS_CARD, new TranslationTextComponent(title(NEW_MODUS)), new TranslationTextComponent(desc(NEW_MODUS)), null, FrameType.TASK, true, true, false).addCriterion("change_modus_type", ChangeModusTrigger.Instance.any()).save(advancementSaver, Minestuck.MOD_ID+":minestuck/new_modus");
		Advancement allModi = changeModusCriteria(Advancement.Builder.advancement().parent(newModus).display(MSItems.QUEUESTACK_MODUS_CARD, new TranslationTextComponent(title(ALL_MODI)), new TranslationTextComponent(desc(ALL_MODI)), null, FrameType.TASK, true, true, false).requirements(IRequirementsStrategy.AND)).save(advancementSaver, Minestuck.MOD_ID+":minestuck/all_modi");
		Advancement goldSeeds = Advancement.Builder.advancement().parent(alchemy).display(MSBlocks.GOLD_SEEDS, new TranslationTextComponent(title(GOLD_SEEDS)), new TranslationTextComponent(desc(GOLD_SEEDS)), null, FrameType.TASK, true, true, false).addCriterion("plant_gold_seeds", PlacedBlockTrigger.Instance.placedBlock(MSBlocks.GOLD_SEEDS)).save(advancementSaver, Minestuck.MOD_ID+":minestuck/gold_seeds");
		Advancement frenchFry = Advancement.Builder.advancement().parent(alchemy).display(MSItems.FRENCH_FRY, new TranslationTextComponent(title(FRENCH_FRY)), new TranslationTextComponent(desc(FRENCH_FRY)), null, FrameType.TASK, true, true, false).addCriterion("has_french_fry", ConsumeItemTrigger.Instance.usedItem(MSItems.FRENCH_FRY)).save(advancementSaver, Minestuck.MOD_ID+":minestuck/french_fry");
		Advancement melonOverload = Advancement.Builder.advancement().parent(alchemy).display(MSItems.MELONSBANE, new TranslationTextComponent(title(MELON_OVERLOAD)), new TranslationTextComponent(desc(MELON_OVERLOAD)), null, FrameType.TASK, true, true, true).addCriterion("melon_overload", EventTrigger.Instance.melonOverload()).save(advancementSaver, Minestuck.MOD_ID+":minestuck/melon_overload");
		Advancement surfNTurf = Advancement.Builder.advancement().parent(alchemy).display(Items.COOKED_BEEF, new TranslationTextComponent(title(SURFNTURF)), new TranslationTextComponent(desc(SURFNTURF)), null, FrameType.TASK, true, true, true).addCriterion("surf_n_turf", EventTrigger.Instance.surfNTurf()).save(advancementSaver, Minestuck.MOD_ID+":minestuck/surf_n_turf");
		Advancement treeModus = Advancement.Builder.advancement().parent(newModus).display(MSItems.TREE_MODUS_CARD, new TranslationTextComponent(title(TREE_MODUS)), new TranslationTextComponent(desc(TREE_MODUS)), null, FrameType.TASK, true, true, false).addCriterion("tree_root", TreeModusRootTrigger.Instance.count(MinMaxBounds.IntBound.atLeast(16))).save(advancementSaver, Minestuck.MOD_ID+":minestuck/tree_modus");
		Advancement killOgre = Advancement.Builder.advancement().parent(entry).display(MSItems.POGO_HAMMER, new TranslationTextComponent(title(KILL_OGRE)), new TranslationTextComponent(desc(KILL_OGRE)), null, FrameType.TASK, true, true, false).addCriterion("kill_ogre", KilledTrigger.Instance.playerKilledEntity(EntityPredicate.Builder.entity().of(MSEntityTypes.OGRE))).save(advancementSaver, Minestuck.MOD_ID+":minestuck/kill_ogre");
		Advancement returnNode = Advancement.Builder.advancement().parent(entry).display(Items.RED_BED, new TranslationTextComponent(title(RETURN_NODE)), new TranslationTextComponent(desc(RETURN_NODE)), null, FrameType.TASK, true, true, false).addCriterion("touch_return_node", EnterBlockTrigger.Instance.entersBlock(MSBlocks.RETURN_NODE)).save(advancementSaver, Minestuck.MOD_ID+":minestuck/return_node");
		Advancement dungeon = Advancement.Builder.advancement().parent(returnNode).display(MSBlocks.FROST_BRICKS, new TranslationTextComponent(title(DUNGEON)), new TranslationTextComponent(desc(DUNGEON)), null, FrameType.TASK, true, true, false).addCriterion("imp_dungeon", PositionTrigger.Instance.located(LocationPredicate.inFeature(MSFeatures.IMP_DUNGEON))).save(advancementSaver, Minestuck.MOD_ID+":minestuck/dungeon");
		Advancement commune = Advancement.Builder.advancement().parent(entry).display(MSItems.STONE_SLAB, new TranslationTextComponent(title(COMMUNE)), new TranslationTextComponent(desc(COMMUNE)), null, FrameType.TASK, true, true, false).requirements(IRequirementsStrategy.AND).addCriterion("talk_to_consort", ConsortTalkTrigger.Instance.any()).addCriterion("visit_village", PositionTrigger.Instance.located(LocationPredicate.inFeature(MSFeatures.CONSORT_VILLAGE))).save(advancementSaver, Minestuck.MOD_ID+":minestuck/commune");
		Advancement bugs = consumeBugCriteria(Advancement.Builder.advancement().parent(commune).display(MSItems.CHOCOLATE_BEETLE, new TranslationTextComponent(title(BUGS)), new TranslationTextComponent(desc(BUGS)), null, FrameType.TASK, true, true, false).requirements(IRequirementsStrategy.OR)).save(advancementSaver, Minestuck.MOD_ID+":minestuck/bugs");
		Advancement shadyBuyer = Advancement.Builder.advancement().parent(commune).display(MSItems.ROCK_COOKIE, new TranslationTextComponent(title(SHADY_BUYER)), new TranslationTextComponent(desc(SHADY_BUYER)), null, FrameType.TASK, true, true, false).addCriterion("buy_item", ConsortItemTrigger.Instance.forType(EnumConsort.MerchantType.SHADY)).save(advancementSaver, Minestuck.MOD_ID+":minestuck/shady_buyer");
	}
	
	private static Advancement.Builder changeModusCriteria(Advancement.Builder builder)
	{
		for(ModusType<?> type : Arrays.asList(ModusTypes.STACK, ModusTypes.QUEUE, ModusTypes.QUEUE_STACK, ModusTypes.TREE, ModusTypes.HASH_MAP, ModusTypes.SET))
		{
			builder = builder.addCriterion(type.getRegistryName().getPath(), InventoryChangeTrigger.Instance.hasItems(type.getItem()));
		}
		return builder;
	}
	
	private static Advancement.Builder consumeBugCriteria(Advancement.Builder builder)
	{
		for(IItemProvider item : Arrays.asList(MSItems.BUG_ON_A_STICK, MSItems.CHOCOLATE_BEETLE, MSItems.CONE_OF_FLIES, MSItems.GRASSHOPPER, MSItems.JAR_OF_BUGS))
		{
			builder = builder.addCriterion(item.asItem().getRegistryName().getPath(), ConsumeItemTrigger.Instance.usedItem(item));
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
	public void run(DirectoryCache cache)
	{
		Path outputFolder = generator.getOutputFolder();
		Set<ResourceLocation> savedAdvancements = Sets.newHashSet();
		
		buildAdvancements(advancement -> save(cache, outputFolder, savedAdvancements, advancement));
	}
	
	private static void save(DirectoryCache cache, Path outputFolder, Set<ResourceLocation> savedAdvancements, Advancement advancement)
	{
		if(!savedAdvancements.add(advancement.getId()))
		{
			throw new IllegalStateException("Duplicate advancement " + advancement.getId());
		} else
		{
			Path jsonPath = outputFolder.resolve("data/" + advancement.getId().getNamespace() + "/advancements/" + advancement.getId().getPath() + ".json");
			
			try
			{
				IDataProvider.save(GSON, cache, advancement.deconstruct().serializeToJson(), jsonPath);
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
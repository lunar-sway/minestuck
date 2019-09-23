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
import com.mraof.minestuck.util.ColorCollector;
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

public class MinestuckAdvancementProvider implements IDataProvider
{
	private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
	private final DataGenerator generator;
	
	public MinestuckAdvancementProvider(DataGenerator generator)
	{
		this.generator = generator;
	}
	
	protected void buildAdvancements(Consumer<Advancement> advancementSaver)
	{
		Advancement root = Advancement.Builder.builder().withDisplay(MSItems.RAW_CRUXITE, new TranslationTextComponent("advancements.minestuck.root.title"), new TranslationTextComponent("advancements.minestuck.root.description"), new ResourceLocation("minestuck:textures/gui/advancement_bg.png"), FrameType.TASK, false, false, false).withCriterion("raw_cruxite", InventoryChangeTrigger.Instance.forItems(MSItems.RAW_CRUXITE)).register(advancementSaver, Minestuck.MOD_ID+":minestuck/root");
		Advancement connect = Advancement.Builder.builder().withParent(root).withDisplay(MSItems.CLIENT_DISK, new TranslationTextComponent("advancements.minestuck.connect.title"), new TranslationTextComponent("advancements.minestuck.connect.description"), null, FrameType.TASK, true, true, false).withCriterion("connection", EventTrigger.Instance.sburbConnection()).register(advancementSaver, Minestuck.MOD_ID+":minestuck/connect");
		Advancement entry = Advancement.Builder.builder().withParent(connect).func_215092_a(ColorCollector.setDefaultColor(new ItemStack(MSItems.CRUXITE_APPLE)), new TranslationTextComponent("advancements.minestuck.entry.title"), new TranslationTextComponent("advancements.minestuck.entry.description"), null, FrameType.TASK, true, true, false).withCriterion("use_artifact", EventTrigger.Instance.cruxiteArtifact()).register(advancementSaver, Minestuck.MOD_ID+":minestuck/entry");
		Advancement alchemy = Advancement.Builder.builder().withParent(entry).withDisplay(MSItems.CAPTCHA_CARD, new TranslationTextComponent("advancements.minestuck.alchemy.title"), new TranslationTextComponent("advancements.minestuck.alchemy.description"), null, FrameType.TASK, true, true, false).withCriterion("use_punch_designix", PunchDesignixTrigger.Instance.any()).register(advancementSaver, Minestuck.MOD_ID+":minestuck/alchemy");
		Advancement newModus = Advancement.Builder.builder().withParent(alchemy).withDisplay(MSItems.HASHMAP_MODUS_CARD, new TranslationTextComponent("advancements.minestuck.new_modus.title"), new TranslationTextComponent("advancements.minestuck.new_modus.description"), null, FrameType.TASK, true, true, false).withCriterion("change_modus_type", ChangeModusTrigger.Instance.any()).register(advancementSaver, Minestuck.MOD_ID+":minestuck/new_modus");
		Advancement allModi = changeModusCriteria(Advancement.Builder.builder().withParent(newModus).withDisplay(MSItems.QUEUESTACK_MODUS_CARD, new TranslationTextComponent("advancements.minestuck.all_modi.title"), new TranslationTextComponent("advancements.minestuck.all_modi.description"), null, FrameType.TASK, true, true, false).withRequirementsStrategy(IRequirementsStrategy.AND)).register(advancementSaver, Minestuck.MOD_ID+":minestuck/all_modi");
		Advancement goldSeeds = Advancement.Builder.builder().withParent(alchemy).withDisplay(MSBlocks.GOLD_SEEDS, new TranslationTextComponent("advancements.minestuck.gold_seeds.title"), new TranslationTextComponent("advancements.minestuck.gold_seeds.description"), null, FrameType.TASK, true, true, false).withCriterion("plant_gold_seeds", PlacedBlockTrigger.Instance.placedBlock(MSBlocks.GOLD_SEEDS)).register(advancementSaver, Minestuck.MOD_ID+":minestuck/gold_seeds");
		Advancement frenchFry = Advancement.Builder.builder().withParent(alchemy).withDisplay(MSItems.FRENCH_FRY, new TranslationTextComponent("advancements.minestuck.french_fry.title"), new TranslationTextComponent("advancements.minestuck.french_fry.description"), null, FrameType.TASK, true, true, false).withCriterion("has_french_fry", ConsumeItemTrigger.Instance.forItem(MSItems.FRENCH_FRY)).register(advancementSaver, Minestuck.MOD_ID+":minestuck/french_fry");
		Advancement treeModus = Advancement.Builder.builder().withParent(newModus).withDisplay(MSItems.TREE_MODUS_CARD, new TranslationTextComponent("advancements.minestuck.tree_modus.title"), new TranslationTextComponent("advancements.minestuck.tree_modus.description"), null, FrameType.TASK, true, true, false).withCriterion("tree_root", TreeModusRootTrigger.Instance.count(MinMaxBounds.IntBound.atLeast(16))).register(advancementSaver, Minestuck.MOD_ID+":minestuck/tree_modus");
		Advancement killOgre = Advancement.Builder.builder().withParent(entry).withDisplay(MSItems.POGO_HAMMER, new TranslationTextComponent("advancements.minestuck.kill_ogre.title"), new TranslationTextComponent("advancements.minestuck.kill_ogre.description"), null, FrameType.TASK, true, true, false).withCriterion("kill_ogre", KilledTrigger.Instance.playerKilledEntity(EntityPredicate.Builder.create().type(MSEntityTypes.OGRE))).register(advancementSaver, Minestuck.MOD_ID+":minestuck/kill_ogre");
		Advancement returnNode = Advancement.Builder.builder().withParent(entry).withDisplay(Items.RED_BED, new TranslationTextComponent("advancements.minestuck.return_node.title"), new TranslationTextComponent("advancements.minestuck.return_node.description"), null, FrameType.TASK, true, true, false).withCriterion("touch_return_node", EnterBlockTrigger.Instance.forBlock(MSBlocks.RETURN_NODE)).register(advancementSaver, Minestuck.MOD_ID+":minestuck/return_node");
		Advancement dungeon = Advancement.Builder.builder().withParent(returnNode).withDisplay(MSBlocks.FROST_BRICKS, new TranslationTextComponent("advancements.minestuck.dungeon.title"), new TranslationTextComponent("advancements.minestuck.dungeon.description"), null, FrameType.TASK, true, true, false).withCriterion("imp_dungeon", PositionTrigger.Instance.forLocation(LocationPredicate.forFeature(MSFeatures.IMP_DUNGEON))).register(advancementSaver, Minestuck.MOD_ID+":minestuck/dungeon");
		Advancement commune = Advancement.Builder.builder().withParent(entry).withDisplay(MSItems.STONE_SLAB, new TranslationTextComponent("advancements.minestuck.commune.title"), new TranslationTextComponent("advancements.minestuck.commune.description"), null, FrameType.TASK, true, true, false).withRequirementsStrategy(IRequirementsStrategy.AND).withCriterion("talk_to_consort", ConsortTalkTrigger.Instance.any()).withCriterion("visit_village", PositionTrigger.Instance.forLocation(LocationPredicate.forFeature(MSFeatures.CONSORT_VILLAGE))).register(advancementSaver, Minestuck.MOD_ID+":minestuck/commune");
		Advancement bugs = consumeBugCriteria(Advancement.Builder.builder().withParent(commune).withDisplay(MSItems.CHOCOLATE_BEETLE, new TranslationTextComponent("advancements.minestuck.bugs.title"), new TranslationTextComponent("advancements.minestuck.bugs.description"), null, FrameType.TASK, true, true, false).withRequirementsStrategy(IRequirementsStrategy.OR)).register(advancementSaver, Minestuck.MOD_ID+":minestuck/bugs");
		Advancement shadyBuyer = Advancement.Builder.builder().withParent(commune).withDisplay(MSItems.ROCK_COOKIE, new TranslationTextComponent("advancements.minestuck.shady_buyer.title"), new TranslationTextComponent("advancements.minestuck.shady_buyer.description"), null, FrameType.TASK, true, true, false).withCriterion("buy_item", ConsortItemTrigger.Instance.forType(EnumConsort.MerchantType.SHADY)).register(advancementSaver, Minestuck.MOD_ID+":minestuck/shady_buyer");
	}
	
	private static Advancement.Builder changeModusCriteria(Advancement.Builder builder)
	{
		for(ModusType<?> type : Arrays.asList(ModusTypes.STACK, ModusTypes.QUEUE, ModusTypes.QUEUE_STACK, ModusTypes.TREE, ModusTypes.HASH_MAP, ModusTypes.SET))
		{
			builder = builder.withCriterion(type.getRegistryName().getPath(), InventoryChangeTrigger.Instance.forItems(type.getStack().getItem()));
		}
		return builder;
	}
	
	private static Advancement.Builder consumeBugCriteria(Advancement.Builder builder)
	{
		for(IItemProvider item : Arrays.asList(MSItems.BUG_ON_A_STICK, MSItems.CHOCOLATE_BEETLE, MSItems.CONE_OF_FLIES, MSItems.GRASSHOPPER, MSItems.JAR_OF_BUGS))
		{
			builder = builder.withCriterion(item.asItem().getRegistryName().getPath(), ConsumeItemTrigger.Instance.forItem(item));
		}
		return builder;
	}
	
	@Override
	public void act(DirectoryCache cache)
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
				IDataProvider.save(GSON, cache, advancement.copy().serialize(), jsonPath);
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
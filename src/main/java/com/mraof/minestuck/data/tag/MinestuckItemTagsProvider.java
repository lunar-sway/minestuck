package com.mraof.minestuck.data.tag;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.weapon.MSToolType;
import com.mraof.minestuck.item.weapon.MagicAOERightClickEffect;
import com.mraof.minestuck.item.weapon.MagicRangedRightClickEffect;
import com.mraof.minestuck.item.weapon.WeaponItem;
import com.mraof.minestuck.item.weapon.projectiles.ConsumableProjectileWeaponItem;
import com.mraof.minestuck.item.weapon.projectiles.ReturningProjectileWeaponItem;
import com.mraof.minestuck.util.ExtraModTags;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.mraof.minestuck.item.MSItemTypes.*;
import static com.mraof.minestuck.item.MSItems.*;
import static com.mraof.minestuck.util.MSTags.Items.*;
import static net.minecraft.tags.ItemTags.*;
import static net.neoforged.neoforge.common.Tags.Items.*;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MinestuckItemTagsProvider extends ItemTagsProvider
{
	public MinestuckItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTagProvider, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(output, lookupProvider, blockTagProvider, Minestuck.MOD_ID, existingFileHelper);
	}
	
	@Override
	protected void addTags(HolderLookup.Provider provider)
	{
		copy(BlockTags.PLANKS, PLANKS);
		copy(BlockTags.STONE_BRICKS, STONE_BRICKS);
		copy(BlockTags.WOODEN_BUTTONS, WOODEN_BUTTONS);
		copy(BlockTags.BUTTONS, BUTTONS);
		copy(BlockTags.WOODEN_STAIRS, WOODEN_STAIRS);
		copy(BlockTags.WOODEN_SLABS, WOODEN_SLABS);
		copy(BlockTags.SAPLINGS, SAPLINGS);
		copy(BlockTags.STAIRS, STAIRS);
		copy(BlockTags.SLABS, SLABS);
		copy(BlockTags.LOGS, LOGS);
		copy(BlockTags.LEAVES, LEAVES);
		copy(Tags.Blocks.COBBLESTONES, COBBLESTONES);
		copy(Tags.Blocks.ORES, ORES);
		copy(BlockTags.COAL_ORES, ORES_COAL);
		copy(BlockTags.DIAMOND_ORES, ORES_DIAMOND);
		copy(BlockTags.GOLD_ORES, ORES_GOLD);
		copy(BlockTags.IRON_ORES, ORES_IRON);
		copy(BlockTags.LAPIS_ORES, ORES_LAPIS);
		copy(Tags.Blocks.ORES_QUARTZ, ORES_QUARTZ);
		copy(BlockTags.REDSTONE_ORES, ORES_REDSTONE);
		copy(BlockTags.EMERALD_ORES, ORES_EMERALD);
		copy(Tags.Blocks.STONES, STONES);
		copy(Tags.Blocks.STORAGE_BLOCKS, STORAGE_BLOCKS);
		copy(ExtraModTags.Blocks.URANIUM_ORES, ExtraModTags.Items.URANIUM_ORES);
		copy(ExtraModTags.Blocks.URANIUM_STORAGE_BLOCKS, ExtraModTags.Items.URANIUM_STORAGE_BLOCKS);
		copy(MSTags.Blocks.GLOWING_LOGS, GLOWING_LOGS);
		copy(MSTags.Blocks.FROST_LOGS, FROST_LOGS);
		copy(MSTags.Blocks.RAINBOW_LOGS, RAINBOW_LOGS);
		copy(MSTags.Blocks.END_LOGS, END_LOGS);
		copy(MSTags.Blocks.VINE_LOGS, VINE_LOGS);
		copy(MSTags.Blocks.FLOWERY_VINE_LOGS, FLOWERY_VINE_LOGS);
		copy(MSTags.Blocks.DEAD_LOGS, DEAD_LOGS);
		copy(MSTags.Blocks.CINDERED_LOGS, CINDERED_LOGS);
		copy(MSTags.Blocks.PETRIFIED_LOGS, PETRIFIED_LOGS);
		copy(MSTags.Blocks.ASPECT_LOGS, ASPECT_LOGS);
		copy(MSTags.Blocks.ASPECT_WOOD, ASPECT_WOOD);
		copy(MSTags.Blocks.ASPECT_PLANKS, ASPECT_PLANKS);
		copy(MSTags.Blocks.ASPECT_SLABS, ASPECT_SLABS);
		copy(MSTags.Blocks.ASPECT_LEAVES, ASPECT_LEAVES);
		copy(MSTags.Blocks.ASPECT_SAPLINGS, ASPECT_SAPLINGS);
		copy(MSTags.Blocks.SHADEWOOD_LOGS, SHADEWOOD_LOGS);
		copy(MSTags.Blocks.SHADEWOOD_LEAVES, MSTags.Items.SHADEWOOD_LEAVES);
		copy(MSTags.Blocks.CRUXITE_ORES, CRUXITE_ORES);
		copy(MSTags.Blocks.URANIUM_ORES, URANIUM_ORES);
		copy(MSTags.Blocks.COAL_ORES, MSTags.Items.COAL_ORES);
		copy(MSTags.Blocks.IRON_ORES, MSTags.Items.IRON_ORES);
		copy(MSTags.Blocks.GOLD_ORES, MSTags.Items.GOLD_ORES);
		copy(MSTags.Blocks.REDSTONE_ORES, MSTags.Items.REDSTONE_ORES);
		copy(MSTags.Blocks.QUARTZ_ORES, QUARTZ_ORES);
		copy(MSTags.Blocks.LAPIS_ORES, MSTags.Items.LAPIS_ORES);
		copy(MSTags.Blocks.DIAMOND_ORES, MSTags.Items.DIAMOND_ORES);
		copy(MSTags.Blocks.EMERALD_ORES, MSTags.Items.EMERALD_ORES);
		copy(MSTags.Blocks.CRUXITE_STORAGE_BLOCKS, CRUXITE_STORAGE_BLOCKS);
		
		//handles items that are in tool types added by Minestuck which would otherwise not be added to relevant tags for weapons or tools
		List<MSToolType> minestuckMiningToolTypes = List.of(SICKLE_TOOL, SCYTHE_TOOL, CLAWS_TOOL, HAMMER_TOOL, FORK_TOOL);
		List<MSToolType> minestuckMeleeToolTypes = List.of(LANCE_TOOL, CLUB_TOOL, KNIFE_TOOL, KEY_TOOL, FAN_TOOL, BATON_TOOL, STAFF_TOOL, CANE_TOOL, WAND_TOOL);
		List<MSToolType> minestuckToolTypes = Stream.concat(minestuckMiningToolTypes.stream(), minestuckMeleeToolTypes.stream()).toList();
		
		tag(SWORDS).add(EMERALD_SWORD.get()).add(relevantWeapons(item -> hasToolType(item, List.of(SWORD_TOOL))));
		tag(AXES).add(EMERALD_AXE.get()).add(relevantWeapons(item -> hasToolType(item, List.of(AXE_TOOL, CHAINSAW_TOOL))));
		tag(PICKAXES).add(EMERALD_PICKAXE.get()).add(relevantWeapons(item -> hasToolType(item, List.of(PICKAXE_TOOL))));
		tag(SHOVELS).add(EMERALD_SHOVEL.get()).add(relevantWeapons(item -> hasToolType(item, List.of(SHOVEL_TOOL, SPOON_TOOL))));
		tag(HOES).add(EMERALD_HOE.get(), HELLBRINGERS_HOE_INACTIVE.get(), HELLBRINGERS_HOE_ACTIVE.get());
		tag(HEAD_ARMOR).add(PRISMARINE_HELMET.get(), IRON_LASS_GLASSES.get(), PROSPIT_CIRCLET.get(), DERSE_CIRCLET.get(), AMPHIBEANIE.get(), NOSTRILDAMUS.get(), PONYTAIL.get());
		tag(CHEST_ARMOR).add(PRISMARINE_CHESTPLATE.get(), IRON_LASS_CHESTPLATE.get(), PROSPIT_SHIRT.get(), DERSE_SHIRT.get());
		tag(LEG_ARMOR).add(PRISMARINE_LEGGINGS.get(), IRON_LASS_SKIRT.get(), PROSPIT_PANTS.get(), DERSE_PANTS.get());
		tag(FOOT_ARMOR).add(PRISMARINE_BOOTS.get(), IRON_LASS_SHOES.get(), PROSPIT_SHOES.get(), DERSE_SHOES.get());
		tag(CLUSTER_MAX_HARVESTABLES).add(relevantWeapons(item -> hasToolType(item, List.of(PICKAXE_TOOL, HAMMER_TOOL))));
		tag(BREAKS_DECORATED_POTS).add(relevantWeapons(item -> hasToolType(item, minestuckToolTypes)));
		
		tag(MINING_TOOL_TOOLS).add(relevantWeapons(item -> hasToolType(item, List.of(PICKAXE_TOOL, HAMMER_TOOL))));
		tag(MELEE_WEAPON_TOOLS).add(relevantWeapons(item -> item instanceof WeaponItem));
		tag(TOOLS_SPEAR).add(relevantWeapons(item -> hasToolType(item, List.of(LANCE_TOOL))));
		tag(RANGED_WEAPON_TOOLS).add(relevantWeapons(item -> item instanceof ConsumableProjectileWeaponItem || (item instanceof WeaponItem weapon && (weapon.getItemRightClickEffect() instanceof MagicRangedRightClickEffect || weapon.getItemRightClickEffect() instanceof MagicAOERightClickEffect))));
		
		tag(DURABILITY_ENCHANTABLE).add(relevantWeapons(item -> (item instanceof WeaponItem && item.isDamageable(item.getDefaultInstance())) || item instanceof ReturningProjectileWeaponItem));
		tag(MINING_ENCHANTABLE).add(relevantWeapons(item -> hasToolType(item, minestuckMiningToolTypes)));
		tag(MINING_LOOT_ENCHANTABLE).add(relevantWeapons(item -> hasToolType(item, minestuckMiningToolTypes)));
		tag(SWORD_ENCHANTABLE).add(relevantWeapons(item -> item instanceof WeaponItem)); //TODO weapons without sweep can get sweeping edge
		tag(SHARP_WEAPON_ENCHANTABLE).add(relevantWeapons(item -> item instanceof WeaponItem));
		tag(VANISHING_ENCHANTABLE).add(relevantWeapons(item -> item instanceof WeaponItem));
		
		tag(Tags.Items.MUSIC_DISCS).add(MUSIC_DISC_DANCE_STAB_DANCE.get(), MUSIC_DISC_EMISSARY_OF_DANCE.get(), MUSIC_DISC_RETRO_BATTLE.get());
		tag(DUSTS).add(MSBlocks.GLOWYSTONE_DUST.get().asItem());
		tag(RODS).add(URANIUM_POWERED_STICK.get());
		tag(ExtraModTags.Items.URANIUM_CHUNKS).add(RAW_URANIUM.get());
		tag(ExtraModTags.Items.TIN_ORES);
		tag(ExtraModTags.Items.SILVER_ORES);
		tag(ExtraModTags.Items.LEAD_ORES);
		tag(ExtraModTags.Items.GALENA_ORES);
		tag(ExtraModTags.Items.ZINC_ORES);
		tag(ExtraModTags.Items.NICKEL_ORES);
		tag(ExtraModTags.Items.ALUMINIUM_ORES);
		tag(ExtraModTags.Items.ALUMINUM_ORES);
		tag(ExtraModTags.Items.COBALT_ORES);
		tag(ExtraModTags.Items.ARDITE_ORES);
		tag(ExtraModTags.Items.TIN_RAW_MATERIALS);
		tag(ExtraModTags.Items.SILVER_RAW_MATERIALS);
		tag(ExtraModTags.Items.LEAD_RAW_MATERIALS);
		tag(ExtraModTags.Items.GALENA_RAW_MATERIALS);
		tag(ExtraModTags.Items.ZINC_RAW_MATERIALS);
		tag(ExtraModTags.Items.NICKEL_RAW_MATERIALS);
		tag(ExtraModTags.Items.ALUMINIUM_RAW_MATERIALS);
		tag(ExtraModTags.Items.ALUMINUM_RAW_MATERIALS);
		tag(ExtraModTags.Items.COBALT_RAW_MATERIALS);
		tag(ExtraModTags.Items.ARDITE_RAW_MATERIALS);
		tag(ExtraModTags.Items.URANIUM_INGOTS);
		tag(ExtraModTags.Items.TIN_INGOTS);
		tag(ExtraModTags.Items.BRASS_INGOTS);
		tag(ExtraModTags.Items.SILVER_INGOTS);
		tag(ExtraModTags.Items.ELECTRUM_INGOTS);
		tag(ExtraModTags.Items.LEAD_INGOTS);
		tag(ExtraModTags.Items.NICKEL_INGOTS);
		tag(ExtraModTags.Items.ZINC_INGOTS);
		tag(ExtraModTags.Items.INVAR_INGOTS);
		tag(ExtraModTags.Items.ALUMINIUM_INGOTS);
		tag(ExtraModTags.Items.COBALT_INGOTS);
		tag(ExtraModTags.Items.ARDITE_INGOTS);
		tag(ExtraModTags.Items.RED_ALLOY_INGOTS);
		
		tag(GRIST_CANDY).add(BUILD_GUSHERS.get(), AMBER_GUMMY_WORM.get(), CAULK_PRETZEL.get(), CHALK_CANDY_CIGARETTE.get(), IODINE_LICORICE.get(), SHALE_PEEP.get(), TAR_LICORICE.get(), COBALT_GUM.get(), MARBLE_JAWBREAKER.get(), MERCURY_SIXLETS.get(), QUARTZ_JELLY_BEAN.get(), SULFUR_CANDY_APPLE.get(), AMETHYST_HARD_CANDY.get(), GARNET_TWIX.get(), RUBY_LOLLIPOP.get(), RUST_GUMMY_EYE.get(), DIAMOND_MINT.get(), GOLD_CANDY_RIBBON.get(), URANIUM_GUMMY_BEAR.get(), ARTIFACT_WARHEAD.get(), ZILLIUM_SKITTLES.get());
		tag(MSTags.Items.FAYGO).add(MSItems.ORANGE_FAYGO.get(), CANDY_APPLE_FAYGO.get(), FAYGO_COLA.get(), COTTON_CANDY_FAYGO.get(), CREME_SODA_FAYGO.get(), GRAPE_FAYGO.get(), MOON_MIST_FAYGO.get(), PEACH_FAYGO.get(), REDPOP_FAYGO.get());
		tag(MODUS_CARD).add(STACK_MODUS_CARD.get(), QUEUE_MODUS_CARD.get(), QUEUESTACK_MODUS_CARD.get(), TREE_MODUS_CARD.get(), HASHMAP_MODUS_CARD.get(), SET_MODUS_CARD.get());
		tag(CASSETTES).add(MSItems.CASSETTE_MELLOHI.get(), CASSETTE_13.get(), CASSETTE_BLOCKS.get(), CASSETTE_CAT.get(), CASSETTE_CHIRP.get(), CASSETTE_FAR.get(), CASSETTE_MALL.get(), CASSETTE_DANCE_STAB.get(), CASSETTE_RETRO_BATTLE.get(), CASSETTE_EMISSARY.get(), CASSETTE_11.get(), CASSETTE_PIGSTEP.get(), CASSETTE_STAL.get(), CASSETTE_STRAD.get(), CASSETTE_WAIT.get(), CASSETTE_WARD.get(), CASSETTE_OTHERSIDE.get(), CASSETTE_5.get(), CASSETTE_RELIC.get(), CASSETTE_PRECIPICE.get(), CASSETTE_CREATOR.get(), CASSETTE_CREATOR_MUSIC_BOX.get());
		tag(BUGS).add(BUG_ON_A_STICK.get(), CHOCOLATE_BEETLE.get(), CONE_OF_FLIES.get(), GRASSHOPPER.get(), CICADA.get(), JAR_OF_BUGS.get());
		tag(CONSORT_SNACKS).add(Items.COOKIE).addTag(BUGS);
		tag(MAGIC_WEAPON).add(relevantWeapons(item ->
				item instanceof WeaponItem weapon && (weapon.getItemRightClickEffect() instanceof MagicRangedRightClickEffect || weapon.getItemRightClickEffect() instanceof MagicAOERightClickEffect)));
		tag(CREATIVE_SHOCK_RIGHT_CLICK_LIMIT).add(Items.CHORUS_FRUIT);
		tag(UNREADABLE).add(CRUEL_FATE_CRUCIBLE.get(), ROYAL_DERINGER.get(), TRANSPORTALIZER.get(), TRANS_PORTALIZER.get(), FEAR_NO_ANVIL.get(), TYPHONIC_TRIVIALIZER.get(), QUILL_OF_ECHIDNA.get(), UMBRAL_INFILTRATOR.get(), FLUORITE_OCTET.get(), CLIENT_DISK.get(), SERVER_DISK.get(), GUTTER_THUMB_DRIVE.get(), GUTTER_BALL.get(), CAPTCHA_CARD.get(), CUEBALL.get(), BLACK_QUEENS_RING.get(), WHITE_QUEENS_RING.get(), BLACK_KINGS_SCEPTER.get(), WHITE_KINGS_SCEPTER.get()).add(Items.DRAGON_EGG).add(Items.DRAGON_HEAD).add(Items.DRAGON_BREATH).add(Items.NETHER_STAR).add(Items.COMMAND_BLOCK).add(Items.COMMAND_BLOCK_MINECART).add(Items.CHAIN_COMMAND_BLOCK).add(Items.REPEATING_COMMAND_BLOCK).add(Items.END_CRYSTAL).add(Items.EXPERIENCE_BOTTLE).add(Items.ELYTRA).add(Items.TOTEM_OF_UNDYING).add(Items.BEACON).add(Items.BEDROCK).add(Items.PUMPKIN);
		tag(LEGENDARY).add(relevantWeapons(item ->
				item instanceof WeaponItem weapon && (weapon.getTier() == DENIZEN_TIER || weapon.getTier() == ZILLY_TIER || weapon.getTier() == WELSH_TIER)));
	}
	
	private static Item[] relevantWeapons(Predicate<Item> predicate)
	{
		return MSItems.REGISTER.getEntries().stream().map(Supplier::get).filter(predicate).toArray(Item[]::new);
	}
	
	private static boolean hasToolType(Item item, List<MSToolType> toolTypes)
	{
		if(!(item instanceof WeaponItem weaponItem) || weaponItem.getToolTypes() == null)
			return false;
		
		List<MSToolType> weaponTypes = weaponItem.getToolTypes();
		
		for(MSToolType toolType : toolTypes)
			if(weaponTypes.contains(toolType))
				return true;
		
		return false;
	}
	
	@Override
	public String getName()
	{
		return "Minestuck Item Tags";
	}
}

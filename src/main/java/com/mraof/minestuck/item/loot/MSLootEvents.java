package com.mraof.minestuck.item.loot;

import com.google.common.collect.Sets;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.MSTags;
import com.mraof.minestuck.world.MSDimensions;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@EventBusSubscriber(modid = Minestuck.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class MSLootEvents
{
	private static final Set<ResourceLocation> BLANK_DISK_DUNGEON_LOOT_INJECT = Sets.newHashSet(BuiltInLootTables.SIMPLE_DUNGEON.location(), BuiltInLootTables.ABANDONED_MINESHAFT.location(), BuiltInLootTables.DESERT_PYRAMID.location(),
			BuiltInLootTables.JUNGLE_TEMPLE.location(), BuiltInLootTables.WOODLAND_MANSION.location(), BuiltInLootTables.UNDERWATER_RUIN_BIG.location(), BuiltInLootTables.SPAWN_BONUS_CHEST.location());
	private static final Set<ResourceLocation> SBURB_CODE_LIBRARY_LOOT_INJECT = Sets.newHashSet(BuiltInLootTables.STRONGHOLD_LIBRARY.location(), BuiltInLootTables.LIBRARIAN_GIFT.location(), BuiltInLootTables.STRONGHOLD_CORRIDOR.location());
	
	public static final String FROG_TEMPLE_MAP = "filled_map.frog_temple";
	
	@SubscribeEvent
	public static void onLootLoad(LootTableLoadEvent event) //created using Upgrade Aquatic "LootEvents" and Mystical World "LootHandler" for reference
	{
		inject(event, BLANK_DISK_DUNGEON_LOOT_INJECT, MSLootTables.BLANK_DISK_DUNGEON_LOOT_INJECT, "blank_disk_dungeon_inject");
		inject(event, SBURB_CODE_LIBRARY_LOOT_INJECT, MSLootTables.SBURB_CODE_LIBRARY_LOOT_INJECT, "sburb_code_library_inject");
	}
	
	private static void inject(LootTableLoadEvent event, Set<ResourceLocation> lootTableSet, ResourceKey<LootTable> injectionLootTable, String address)
	{
		if(lootTableSet.contains(event.getName()))
		{
			LootPool pool = LootPool.lootPool().add(NestedLootTable.lootTableReference(injectionLootTable)).name(address).build();
			event.getTable().addPool(pool);
		}
	}
	
	@SubscribeEvent
	public static void addCustomVillagerTrade(VillagerTradesEvent event)
	{
		Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
		
		if(event.getType() == VillagerProfession.CARTOGRAPHER)
		{
			trades.get(2).add((villager, random) -> createFrogTempleMapTrade(villager));
		}
		
		if(event.getType() == VillagerProfession.LEATHERWORKER)
		{
			trades.get(3).add((villager, random) -> new MerchantOffer(new ItemCost(Items.EMERALD, 5), new ItemStack(MSItems.CRUMPLY_HAT.get()), 5, 3, 0.2F));
		}
		
		if(event.getType() == VillagerProfession.MASON)
		{
			trades.get(2).add((villager, random) -> new MerchantOffer(new ItemCost(Items.EMERALD, 3), new ItemStack(MSItems.CARVING_TOOL.get()), 4, 2, 0.05F));
			trades.get(2).add((villager, random) -> new MerchantOffer(new ItemCost(Items.EMERALD, 2), new ItemStack(MSItems.STONE_TABLET.get()), 6, 2, 0.05F));
			trades.get(3).add((villager, random) -> new MerchantOffer(new ItemCost(Items.EMERALD, 5), Optional.of(new ItemCost(MSItems.GREEN_STONE_BRICK_FROG.get())),
					new ItemStack(MSItems.GREEN_STONE_BRICK_FROG.get(), 2), 12, 7, 0.2F));
		}
		
		if(event.getType() == VillagerProfession.WEAPONSMITH)
		{
			trades.get(2).add((villager, random) -> createEnchantedItemOffer(random, new ItemStack(MSItems.LUCERNE_HAMMER.get()), 4, 3, 1, 0.05F, villager.registryAccess()));
		}
		
		if(event.getType() == VillagerProfession.FISHERMAN)
		{
			trades.get(2).add((villager, random) -> new MerchantOffer(new ItemCost(MSItems.CICADA, 7), new ItemStack(Items.EMERALD, 1), 9, 2, 0.05F));
			trades.get(2).add((villager, random) -> new MerchantOffer(new ItemCost(MSItems.GRASSHOPPER, 9), new ItemStack(Items.EMERALD, 1), 9, 2, 0.05F));
		}
		
		if(event.getType() == VillagerProfession.LIBRARIAN)
		{
			ItemCost[] mediumBookTypes = new ItemCost[]{new ItemCost(MSItems.NONBINARY_CODE), new ItemCost(MSItems.BINARY_CODE), new ItemCost(MSItems.TILLDEATH_HANDBOOK), new ItemCost(MSItems.TABLESTUCK_MANUAL.get()),
					new ItemCost(MSItems.WISEGUY), new ItemCost(MSItems.SASSACRE_TEXT), new ItemCost(MSItems.FLARP_MANUAL)};
			ItemCost[] overworldBookTypes = new ItemCost[]{new ItemCost(MSItems.COMPLETED_SBURB_CODE)};
			trades.get(2).add((villager, random) -> {
				ItemCost randomBook = MSDimensions.isInMedium(villager.getServer(), villager.level().dimension()) ? mediumBookTypes[random.nextInt(mediumBookTypes.length)] : overworldBookTypes[random.nextInt(overworldBookTypes.length)];
				return new MerchantOffer(randomBook, new ItemStack(Items.EMERALD, 3), 4, 2, 0.05F);
			});
		}
	}
	
	/**
	 * Checks for nearby frog temples and creates a map based on an un-generated one if it can be found, seems to cause momentary lag because of this stage at which the map's data is being collected,
	 * uses TreasureMapForEmeralds in {@link VillagerTrades} as a base
	 */
	public static MerchantOffer createFrogTempleMapTrade(Entity villagerEntity)
	{
		Level level = villagerEntity.level();
		if(level instanceof ServerLevel serverLevel)
		{
			BlockPos templePos = serverLevel.findNearestMapStructure(MSTags.Structures.SCANNER_LOCATED, villagerEntity.blockPosition(), 100, true);
			if(templePos != null)
			{
				ItemStack itemstack = MapItem.create(serverLevel, templePos.getX(), templePos.getZ(), (byte) 2, true, true);
				MapItem.renderBiomePreviewMap(serverLevel, itemstack);
				MapItemSavedData.addTargetDecoration(itemstack, templePos, "+", MapDecorationTypes.RED_X);
				itemstack.set(DataComponents.ITEM_NAME, Component.translatable(FROG_TEMPLE_MAP));
				
				return new MerchantOffer(new ItemCost(Items.EMERALD, 8), Optional.of(new ItemCost(Items.COMPASS)), itemstack, 12, 7, 0.2F);
			}
		}
		
		return null;
	}
	
	/**
	 * Creates a enchanted weapon, uses EnchantedItemForEmeraldsTrade in {@link VillagerTrades} as a base
	 */
	public static MerchantOffer createEnchantedItemOffer(RandomSource random, ItemStack weaponStack, int baseEmeraldCost, int maxUses, int villagerXp, float priceMultiplier, RegistryAccess registryAccess)
	{
		int emeraldCostMod = 5 + random.nextInt(15);
		Optional<HolderSet.Named<Enchantment>> availableEnchantments = registryAccess.registryOrThrow(Registries.ENCHANTMENT)
				.getTag(EnchantmentTags.ON_TRADED_EQUIPMENT);
		EnchantmentHelper.enchantItem(random, weaponStack, emeraldCostMod, registryAccess, availableEnchantments);
		int emeraldCost = Math.min(baseEmeraldCost + emeraldCostMod, 64);
		return new MerchantOffer(new ItemCost(Items.EMERALD, emeraldCost), weaponStack, maxUses, villagerXp, priceMultiplier);
	}
	
	@SubscribeEvent
	public static void addCustomWanderingVillagerTrade(WandererTradesEvent event)
	{
		List<VillagerTrades.ItemListing> trades = event.getGenericTrades();
		//between the two new trades, there is an approximately 25% chance for a blank disk to appear in a wandering trader slot(as of 1.18 with no further modifications to the trades)
		trades.add((villager, random) -> new MerchantOffer(new ItemCost(Items.EMERALD, 3), new ItemStack(MSItems.BLANK_DISK.get()), 3, 12, 0.05F));
		trades.add((villager, random) -> new MerchantOffer(new ItemCost(Items.EMERALD, 3), new ItemStack(MSItems.BLANK_DISK.get()), 3, 12, 0.05F));
	}
}

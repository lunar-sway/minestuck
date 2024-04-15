package com.mraof.minestuck.item.loot;

import com.google.common.collect.Sets;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.MSTags;
import com.mraof.minestuck.world.MSDimensions;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;

import java.util.List;
import java.util.Set;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MSLootEvents
{
	private static final Set<ResourceLocation> BLANK_DISK_DUNGEON_LOOT_INJECT = Sets.newHashSet(BuiltInLootTables.SIMPLE_DUNGEON, BuiltInLootTables.ABANDONED_MINESHAFT, BuiltInLootTables.DESERT_PYRAMID, BuiltInLootTables.JUNGLE_TEMPLE, BuiltInLootTables.WOODLAND_MANSION, BuiltInLootTables.UNDERWATER_RUIN_BIG, BuiltInLootTables.SPAWN_BONUS_CHEST);
	private static final Set<ResourceLocation> SBURB_CODE_LIBRARY_LOOT_INJECT = Sets.newHashSet(BuiltInLootTables.STRONGHOLD_LIBRARY, BuiltInLootTables.LIBRARIAN_GIFT, BuiltInLootTables.STRONGHOLD_CORRIDOR);
	
	public static final String FROG_TEMPLE_MAP = "filled_map.frog_temple";
	
	@SubscribeEvent
	public static void onLootLoad(LootTableLoadEvent event) //created using Upgrade Aquatic "LootEvents" and Mystical World "LootHandler" for reference
	{
		inject(event, BLANK_DISK_DUNGEON_LOOT_INJECT, MSLootTables.BLANK_DISK_DUNGEON_LOOT_INJECT, "blank_disk_dungeon_inject");
		inject(event, SBURB_CODE_LIBRARY_LOOT_INJECT, MSLootTables.SBURB_CODE_LIBRARY_LOOT_INJECT, "sburb_code_library_inject");
	}
	
	private static void inject(LootTableLoadEvent event, Set<ResourceLocation> lootTableSet, ResourceLocation injectionLootTable, String address)
	{
		if(lootTableSet.contains(event.getName()))
		{
			LootPool pool = LootPool.lootPool().add(LootTableReference.lootTableReference(injectionLootTable)).name(address).build();
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
			trades.get(3).add((villager, random) -> new MerchantOffer(new ItemStack(Items.EMERALD, 5), new ItemStack(MSItems.CRUMPLY_HAT.get()), 5, 3, 0.2F));
		}
		
		if(event.getType() == VillagerProfession.MASON)
		{
			trades.get(2).add((villager, random) -> new MerchantOffer(new ItemStack(Items.EMERALD, 3), new ItemStack(MSItems.CARVING_TOOL.get()), 4, 2, 0.05F));
			trades.get(2).add((villager, random) -> new MerchantOffer(new ItemStack(Items.EMERALD, 2), new ItemStack(MSItems.STONE_TABLET.get()), 6, 2, 0.05F));
			trades.get(3).add((villager, random) -> new MerchantOffer(new ItemStack(Items.EMERALD, 5), new ItemStack(MSItems.GREEN_STONE_BRICK_FROG.get()), new ItemStack(MSItems.GREEN_STONE_BRICK_FROG.get(), 2), 12, 7, 0.2F));
		}
		
		if(event.getType() == VillagerProfession.WEAPONSMITH)
		{
			trades.get(2).add((villager, random) -> createEnchantedItemOffer(random, new ItemStack(MSItems.LUCERNE_HAMMER.get()), 4, 3, 1, 0.05F));
		}
		
		if(event.getType() == VillagerProfession.FISHERMAN)
		{
			trades.get(2).add((villager, random) -> new MerchantOffer(new ItemStack(MSItems.CICADA.get(), 7), new ItemStack(Items.EMERALD, 1), 9, 2, 0.05F));
			trades.get(2).add((villager, random) -> new MerchantOffer(new ItemStack(MSItems.GRASSHOPPER.get(), 9), new ItemStack(Items.EMERALD, 1), 9, 2, 0.05F));
		}
		
		if(event.getType() == VillagerProfession.LIBRARIAN)
		{
			ItemStack[] mediumBookTypes = new ItemStack[]{new ItemStack(MSItems.NONBINARY_CODE.get()), new ItemStack(MSItems.BINARY_CODE.get()), new ItemStack(MSItems.TILLDEATH_HANDBOOK.get()), new ItemStack(MSItems.TABLESTUCK_MANUAL.get()), new ItemStack(MSItems.WISEGUY.get()), new ItemStack(MSItems.SASSACRE_TEXT.get()), new ItemStack(MSItems.FLARP_MANUAL.get())};
			ItemStack[] overworldBookTypes = new ItemStack[]{new ItemStack(MSItems.COMPLETED_SBURB_CODE.get())};
			trades.get(2).add((villager, random) -> new MerchantOffer(MSDimensions.isInMedium(villager.getServer(), villager.level().dimension()) ? mediumBookTypes[random.nextInt(mediumBookTypes.length)] : overworldBookTypes[random.nextInt(overworldBookTypes.length)], new ItemStack(Items.EMERALD, 3), 4, 2, 0.05F));
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
				MapItemSavedData.addTargetDecoration(itemstack, templePos, "+", MapDecoration.Type.RED_X);
				itemstack.setHoverName(Component.translatable(FROG_TEMPLE_MAP));
				
				return new MerchantOffer(new ItemStack(Items.EMERALD, 8), new ItemStack(Items.COMPASS), itemstack, 12, 7, 0.2F);
			}
		}
		
		return null;
	}
	
	/**
	 * Creates a enchanted weapon, uses EnchantedItemForEmeraldsTrade in {@link VillagerTrades} as a base
	 */
	public static MerchantOffer createEnchantedItemOffer(RandomSource random, ItemStack weaponStack, int baseEmeraldCost, int maxUses, int villagerXp, float priceMultiplier)
	{
		int emeraldCostMod = 5 + random.nextInt(15);
		EnchantmentHelper.enchantItem(random, weaponStack, emeraldCostMod, false);
		int emeraldCost = Math.min(baseEmeraldCost + emeraldCostMod, 64);
		ItemStack emeraldStack = new ItemStack(Items.EMERALD, emeraldCost);
		return new MerchantOffer(emeraldStack, weaponStack, maxUses, villagerXp, priceMultiplier);
	}
	
	@SubscribeEvent
	public static void addCustomWanderingVillagerTrade(WandererTradesEvent event)
	{
		List<VillagerTrades.ItemListing> trades = event.getGenericTrades();
		//between the two new trades, there is an approximately 25% chance for a blank disk to appear in a wandering trader slot(as of 1.18 with no further modifications to the trades)
		trades.add((villager, random) -> new MerchantOffer(new ItemStack(Items.EMERALD, 3), new ItemStack(MSItems.BLANK_DISK.get()), 3, 12, 0.05F));
		trades.add((villager, random) -> new MerchantOffer(new ItemStack(Items.EMERALD, 3), new ItemStack(MSItems.BLANK_DISK.get()), 3, 12, 0.05F));
	}
}

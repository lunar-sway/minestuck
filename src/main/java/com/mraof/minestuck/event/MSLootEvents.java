package com.mraof.minestuck.event;

import com.google.common.collect.Sets;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import com.mraof.minestuck.world.storage.loot.MSLootTables;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.*;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.TableLootEntry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

/**
 * Adds additional loot to preexisting loot tables. Also handles additions to villager trades
 */
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MSLootEvents
{
	private static final Set<ResourceLocation> BLANK_DISC_LOOT_INJECT = Sets.newHashSet(LootTables.SIMPLE_DUNGEON, LootTables.ABANDONED_MINESHAFT, LootTables.DESERT_PYRAMID, LootTables.JUNGLE_TEMPLE, LootTables.WOODLAND_MANSION, LootTables.UNDERWATER_RUIN_BIG, LootTables.SPAWN_BONUS_CHEST, LootTables.SHIPWRECK_TREASURE, LootTables.BURIED_TREASURE, LootTables.STRONGHOLD_CORRIDOR, LootTables.STRONGHOLD_CROSSING);
	
	@SubscribeEvent
	public static void onLootLoad(LootTableLoadEvent event) //created using Upgrade Aquatic "LootEvents" and Mystical World "LootHandler" for reference
	{
		if(BLANK_DISC_LOOT_INJECT.contains(event.getName()))
		{
			//TODO test in server setting for increased drops
			LootPool pool = LootPool.lootPool().add(TableLootEntry.lootTableReference(MSLootTables.BLANK_DISK_DUNGEON_LOOT_INJECT)).name("dungeon_loot_inject").build();
			event.getTable().addPool(pool);
		}
	}
	
	@SubscribeEvent
	public static void addCustomVillagerTrade(VillagerTradesEvent event)
	{
		//TODO test in server setting for bugs
		Int2ObjectMap<List<VillagerTrades.ITrade>> trades = event.getTrades();
		
		if(event.getType() == VillagerProfession.CARTOGRAPHER)
		{
			trades.get(1).add((villager, random) -> new MerchantOffer(new ItemStack(Items.EMERALD, 8), new ItemStack(Items.COMPASS), createFrogTempleMap(villager), 12, 7, 0.2F));
		}
		
		if(event.getType() == VillagerProfession.LEATHERWORKER)
		{
			trades.get(3).add((villager, random) -> new MerchantOffer(new ItemStack(Items.EMERALD, 5), new ItemStack(MSItems.CRUMPLY_HAT), 5, 3, 0.2F));
		}
		
		if(event.getType() == VillagerProfession.MASON)
		{
			trades.get(2).add((villager, random) -> new MerchantOffer(new ItemStack(Items.EMERALD, 3), new ItemStack(MSItems.CARVING_TOOL), 4, 2, 0.05F));
			trades.get(2).add((villager, random) -> new MerchantOffer(new ItemStack(Items.EMERALD, 2), new ItemStack(MSItems.STONE_SLAB), 6, 2, 0.05F));
		}
		
		if(event.getType() == VillagerProfession.WEAPONSMITH)
		{
			trades.get(2).add((villager, random) -> createEnchantedItemOffer(random, new ItemStack(MSItems.LUCERNE_HAMMER), 4, 3, 1, 0.05F));
		}
		
		if(event.getType() == VillagerProfession.LIBRARIAN)
		{
			ItemStack[] bookTypes = new ItemStack[]{new ItemStack(MSItems.SBURB_CODE), new ItemStack(MSItems.NONBINARY_CODE), new ItemStack(MSItems.BINARY_CODE), new ItemStack(MSItems.TILLDEATH_HANDBOOK), new ItemStack(MSItems.TABLESTUCK_MANUAL), new ItemStack(MSItems.WISEGUY), new ItemStack(MSItems.SASSACRE_TEXT), new ItemStack(MSItems.FLARP_MANUAL)};
			trades.get(2).add((villager, random) -> new MerchantOffer(bookTypes[random.nextInt(bookTypes.length)], new ItemStack(Items.EMERALD, 3), 4, 2, 0.05F)); //TODO look into a way to only get this to occur if the librarian is in a Minestuck dimension or is trading with an entered player
		}
	}
	
	/**
	 * Checks for nearby frog temples and creates a map based on an un-generated one if it can be found, seems to cause momentary lag because of this stage at which the map's data is being collected,
	 * uses EmeraldForMapTrade in VillagerTrades as a base
	 */
	public static ItemStack createFrogTempleMap(Entity villagerEntity)
	{
		World world = villagerEntity.level;
		if(world instanceof ServerWorld)
		{
			ServerWorld serverWorld = (ServerWorld) world;
			BlockPos templePos = serverWorld.findNearestMapFeature(MSFeatures.FROG_TEMPLE, villagerEntity.blockPosition(), 100, true);
			if(templePos != null)
			{
				ItemStack itemstack = FilledMapItem.create(serverWorld, templePos.getX(), templePos.getZ(), (byte) 2, true, true);
				FilledMapItem.renderBiomePreviewMap(serverWorld, itemstack);
				MapData.addTargetDecoration(itemstack, templePos, "+", MapDecoration.Type.RED_X);
				itemstack.setHoverName(new TranslationTextComponent("filled_map." + MSFeatures.FROG_TEMPLE.getFeatureName().toLowerCase(Locale.ROOT))); //TODO have a proper name show up, either make a translatable text here or in lang file
				
				return itemstack;
			}
		}
		
		return new ItemStack(MSItems.DICE);
	}
	
	/**
	 * Creates a enchanted weapon, uses EnchantedItemForEmeraldsTrade in VillagerTrades as a base
	 */
	public static MerchantOffer createEnchantedItemOffer(Random random, ItemStack weaponStack, int baseEmeraldCost, int maxUses, int villagerXp, float priceMultiplier)
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
		List<VillagerTrades.ITrade> trades = event.getGenericTrades();
		//between the two new trades, there is an approximately 25% chance for a blank disk to appear in a wandering trader slot(as of 1.16 with no further modifications to the trades)
		trades.add(1, (villager, random) -> new MerchantOffer(new ItemStack(Items.EMERALD, 3), new ItemStack(MSItems.BLANK_DISK), 3, 12, 0.05F));
		trades.add(1, (villager, random) -> new MerchantOffer(new ItemStack(Items.EMERALD, 3), new ItemStack(MSItems.BLANK_DISK), 3, 12, 0.05F));
	}
}

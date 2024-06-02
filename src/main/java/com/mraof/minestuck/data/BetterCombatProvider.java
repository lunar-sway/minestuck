package com.mraof.minestuck.data;

import com.google.gson.*;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.weapon.MSToolType;
import com.mraof.minestuck.item.weapon.WeaponItem;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;

import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static com.mraof.minestuck.item.MSItemTypes.*;

/**
 * Generates json files for compatibility with the Better Combat mod, using our weapons tool types to determine what parent weapon file to use.
 * Weapons without a tool type or a tool type of misc are not automatically assigned a parent weapon.
 * Custom entries have been included for weapons whose style does not conform to their default parent (or had no parent).
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BetterCombatProvider implements DataProvider
{
	private static final String SICKLE = "bettercombat:sickle";
	private static final String SCYTHE = "bettercombat:scythe";
	private static final String CLAW = "bettercombat:claw";
	private static final String PICKAXE = "bettercombat:pickaxe";
	private static final String HAMMER = "bettercombat:hammer";
	private static final String AXE = "bettercombat:axe";
	private static final String HEAVY_AXE = "bettercombat:heavy_axe";
	private static final String CHAINSAW = "minestuck:chainsaw_base";
	private static final String MACE = "bettercombat:mace";
	private static final String SWORD = "bettercombat:sword";
	private static final String KATANA = "bettercombat:katana";
	private static final String CUTLASS = "bettercombat:cutlass";
	private static final String CLAYMORE = "bettercombat:claymore";
	private static final String RAPIER = "bettercombat:rapier";
	private static final String DAGGER = "bettercombat:dagger";
	private static final String LANCE = "bettercombat:lance";
	private static final String STAFF = "bettercombat:battlestaff";
	private static final String CANE = "minestuck:cane_base";
	private static final String FORK = "minestuck:fork_base";
	private static final String WAND = "bettercombat:wand";
	private static final String HALBERD = "bettercombat:halberd";
	private static final String SPEAR = "bettercombat:spear";
	private static final String TRIDENT = "bettercombat:trident";
	
	private final PackOutput output;
	
	private final Map<ResourceKey<Item>, String> weaponWithParent = new HashMap<>();
	
	public BetterCombatProvider(PackOutput output)
	{
		this.output = output;
	}
	
	@Override
	public final CompletableFuture<?> run(CachedOutput cache)
	{
		List<CompletableFuture<?>> futures = new ArrayList<>();
		Path basePath = this.output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(Minestuck.MOD_ID).resolve("weapon_attributes");
		
		addAllWeapons();
		
		for(Map.Entry<ResourceKey<Item>, String> entry : weaponWithParent.entrySet())
		{
			JsonObject object = new JsonObject();
			
			object.addProperty("parent", entry.getValue());
			
			futures.add(DataProvider.saveStable(cache, object, basePath.resolve(entry.getKey().location().getPath() + ".json")));
		}
		
		return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
	}
	
	public void addAllWeapons()
	{
		addWeaponDefaults();
		
		addWeapon(MSItems.MAILBOX, HAMMER);
		addWeapon(MSItems.ESTROGEN_EMPOWERED_EVERYTHING_ERADICATOR, HAMMER);
		addWeapon(MSItems.BOOMBOX_BEATER, HAMMER);
		
		addWeapon(MSItems.CLAW_HAMMER, MACE);
		addWeapon(MSItems.BLACKSMITH_HAMMER, MACE);
		
		addWeapon(MSItems.SWONGE, SWORD);
		addWeapon(MSItems.WET_SWONGE, SWORD);
		addWeapon(MSItems.PUMORD, SWORD);
		addWeapon(MSItems.WET_PUMORD, SWORD);
		addWeapon(MSItems.MUSIC_SWORD, SWORD);
		
		addWeapon(MSItems.CHAINSAW_KATANA, KATANA);
		addWeapon(MSItems.KATANA, KATANA);
		addWeapon(MSItems.UNBREAKABLE_KATANA, KATANA);
		
		addWeapon(MSItems.CACTACEAE_CUTLASS, CUTLASS);
		addWeapon(MSItems.CUTLASS_OF_ZILLYWAIR, CUTLASS);
		addWeapon(MSItems.SCARLET_RIBBITAR, CUTLASS);
		addWeapon(MSItems.TOO_HOT_TO_HANDLE, CUTLASS);
		addWeapon(MSItems.COBALT_SABRE, CUTLASS);
		
		addWeapon(MSItems.CLAYMORE, CLAYMORE);
		addWeapon(MSItems.MACUAHUITL, CLAYMORE);
		addWeapon(MSItems.FROSTY_MACUAHUITL, CLAYMORE);
		addWeapon(MSItems.CRUEL_FATE_CRUCIBLE, CLAYMORE);
		addWeapon(MSItems.SUBTRACTSHUMIDIRE_ZOMORRODNEGATIVE, CLAYMORE);
		
		addWeapon(MSItems.ANGEL_APOCALYPSE, RAPIER);
		addWeapon(MSItems.FIRE_POKER, RAPIER);
		
		addWeapon(MSItems.NIFE, DAGGER);
		
		addWeapon(MSItems.BATTLEAXE, HEAVY_AXE);
		addWeapon(MSItems.CANDY_BATTLEAXE, HEAVY_AXE);
		addWeapon(MSItems.CHOCO_LOCO_WOODSPLITTER, HEAVY_AXE);
		addWeapon(MSItems.STEEL_EDGE_CANDYCUTTER, HEAVY_AXE);
		addWeapon(MSItems.PISTON_POWERED_POGO_AXEHAMMER, HEAVY_AXE);
		addWeapon(MSItems.FISSION_FOCUSED_FAULT_FELLER, HEAVY_AXE);
		addWeapon(MSItems.BISECTOR, HEAVY_AXE);
		addWeapon(MSItems.FINE_CHINA_AXE, HEAVY_AXE);
		
		addWeapon(MSItems.LUCERNE_HAMMER, HALBERD);
		addWeapon(MSItems.LUCERNE_HAMMER_OF_UNDYING, HALBERD);
		
		addWeapon(MSItems.OBSIDIAN_AXE_KNIFE, CLAW);
		
		addWeapon(MSItems.PROSPECTING_PICKSCYTHE, SCYTHE);
		
		addWeapon(MSItems.CUESTICK, LANCE);
		addWeapon(MSItems.TV_ANTENNA, SWORD);
		
		addWeapon(MSItems.CROWBAR, HEAVY_AXE);
		addWeapon(MSItems.UMBRELLA, FORK);
		addWeapon(MSItems.BARBERS_BEST_FRIEND, FORK);
		addWeapon(MSItems.SPEAR_CANE, SPEAR);
		
		addWeapon(MSItems.MEATFORK, TRIDENT);
		addWeapon(MSItems.BIDENT, TRIDENT);
		addWeapon(MSItems.DOUBLE_ENDED_TRIDENT, TRIDENT);
	}
	
	private void addWeaponDefaults()
	{
		MSItems.REGISTER.getEntries().forEach(weaponHolder ->
		{
			if(weaponHolder.get() instanceof WeaponItem weaponItem && weaponItem.getToolType() != null)
			{
				String parent = getWeaponParent(weaponItem.getToolType());
				
				if(!parent.isEmpty())
					weaponWithParent.put(weaponHolder.getKey(), parent);
			}
		});
	}
	
	public String getWeaponParent(MSToolType toolType)
	{
		if(toolType == SICKLE_TOOL)
			return SICKLE;
		else if(toolType == SCYTHE_TOOL)
			return SCYTHE;
		else if(toolType == CLAWS_TOOL)
			return CLAW;
		else if(toolType == PICKAXE_TOOL)
			return PICKAXE;
		else if(toolType == HAMMER_TOOL)
			return HAMMER;
		else if(toolType == AXE_TOOL)
			return AXE;
		else if(toolType == CHAINSAW_TOOL)
			return CHAINSAW;
		else if(toolType == CLUB_TOOL || toolType == SHOVEL_TOOL)
			return MACE;
		else if(toolType == SWORD_TOOL || toolType == KEY_TOOL || toolType == BATON_TOOL)
			return SWORD;
		else if(toolType == KNIFE_TOOL || toolType == FAN_TOOL)
			return DAGGER;
		else if(toolType == LANCE_TOOL)
			return LANCE;
		else if(toolType == STAFF_TOOL)
			return STAFF;
		else if(toolType == CANE_TOOL)
			return CANE;
		else if(toolType == FORK_TOOL)
			return FORK;
		else if(toolType == WAND_TOOL)
			return WAND;
		return "";
	}
	
	public void addWeapon(DeferredItem<Item> weapon, String parent)
	{
		weaponWithParent.put(weapon.getKey(), parent);
	}
	
	@Override
	public String getName()
	{
		return "Minestuck Better Combat Weapon Files";
	}
}
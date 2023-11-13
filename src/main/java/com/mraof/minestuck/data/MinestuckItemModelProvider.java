package com.mraof.minestuck.data;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MinestuckItemModelProvider extends ItemModelProvider
{
	public MinestuckItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper)
	{
		super(output, Minestuck.MOD_ID, existingFileHelper);
	}
	
	@Override
	protected void registerModels()
	{
		//Hammers
		handheldItem(MSItems.CLAW_HAMMER);
		handheldItem(MSItems.SLEDGE_HAMMER);
		handheldItem(MSItems.MAILBOX);
		handheldItem(MSItems.BLACKSMITH_HAMMER);
		handheldItem(MSItems.POGO_HAMMER);
		handheldItem(MSItems.WRINKLEFUCKER);
		handheldItem(MSItems.DEMOCRATIC_DEMOLITIONER);
		handheldItem(MSItems.REGI_HAMMER);
		handheldItem(MSItems.FEAR_NO_ANVIL);
		handheldItem(MSItems.MELT_MASHER);
		handheldItem(MSItems.ESTROGEN_EMPOWERED_EVERYTHING_ERADICATOR);
		handheldItem(MSItems.EEEEEEEEEEEE);
		handheldItem(MSItems.ZILLYHOO_HAMMER);
		handheldItem(MSItems.POPAMATIC_VRILLYHOO, "vrillyhoo");
		handheldItem(MSItems.SCARLET_ZILLYHOO);
		handheldItem(MSItems.MWRTHWL);
		
		//Blades
		handheldItem(MSItems.SORD);
		handheldItem(MSItems.PAPER_SWORD);
		handheldItem(MSItems.SWONGE);
		handheldItem(MSItems.WET_SWONGE);
		handheldItem(MSItems.PUMORD);
		handheldItem(MSItems.WET_PUMORD);
		handheldItem(MSItems.CACTACEAE_CUTLASS);
		handheldItem(MSItems.STEAK_SWORD);
		handheldItem(MSItems.BEEF_SWORD, "raw_beef_sword");
		handheldItem(MSItems.IRRADIATED_STEAK_SWORD);
		handheldItem(MSItems.MACUAHUITL);
		handheldItem(MSItems.FROSTY_MACUAHUITL);
		handheldItem(MSItems.KATANA);
		handheldItem(MSItems.UNBREAKABLE_KATANA);
		handheldItem(MSItems.ANGEL_APOCALYPSE);
		handheldItem(MSItems.FIRE_POKER);
		handheldItem(MSItems.TOO_HOT_TO_HANDLE);
		handheldItem(MSItems.CALEDSCRATCH);
		handheldItem(MSItems.CALEDFWLCH);
		handheldItem(MSItems.ROYAL_DERINGER);
		handheldItem(MSItems.CUTLASS_OF_ZILLYWAIR);
		handheldItem(MSItems.REGISWORD);
		handheldItem(MSItems.SCARLET_RIBBITAR);
		handheldItem(MSItems.DOGG_MACHETE, "snoop_dogg_machete");
		handheldItem(MSItems.COBALT_SABRE);
		handheldItem(MSItems.QUANTUM_SABRE);
		handheldItem(MSItems.SHATTER_BEACON);
		handheldItem(MSItems.SHATTER_BACON);
		handheldItem(MSItems.PILLOW_TALK);
		handheldItem(MSItems.CINNAMON_SWORD);
		handheldItem(MSItems.UNION_BUSTER);
		handheldItem(MSItems.CHAINSAW_KATANA);
		handheldItem(MSItems.THORN_IN_YOUR_SIDE);
		handheldItem(MSItems.ROSE_PROTOCOL);
		
		//Keys
		handheldItem(MSItems.HOUSE_KEY);
		handheldItem(MSItems.KEYBLADE);
		handheldItem(MSItems.CANDY_KEY);
		handheldItem(MSItems.LOCKSOFTENER);
		handheldItem(MSItems.BISEKEYAL);
		handheldItem(MSItems.LATCHMELTER);
		handheldItem(MSItems.KEY_TO_THE_MACHINE);
		handheldItem(MSItems.KEY_TO_THE_CITY);
		handheldItem(MSItems.INNER_HEART);
		handheldItem(MSItems.CRIMSON_LEAP);
		handheldItem(MSItems.LOCH_PICK);
		handheldItem(MSItems.DRAGON_KEY);
		handheldItem(MSItems.TRUE_BLUE);
		handheldItem(MSItems.BLUE_BEAMS);
		handheldItem(MSItems.INKSPLOCKER_UNLOCKER);
		handheldItem(MSItems.INKSQUIDDER_DEPTHKEY);
		handheldItem(MSItems.REGIKEY);
		handheldItem(MSItems.CLOCKKEEPER);
		handheldItem(MSItems.HOME_BY_MIDNIGHT);
		handheldItem(MSItems.NO_TIME_FOR_FLIES);
		handheldItem(MSItems.NATURES_HEART);
		handheldItem(MSItems.YALDABAOTHS_KEYTON);
		handheldItem(MSItems.KEYTAR);
		handheldItem(MSItems.ALLWEDDOL);
		
		//Batons
		handheldItem(MSItems.CONDUCTORS_BATON);
		handheldItem(MSItems.SHARP_NOTE);
		handheldItem(MSItems.URANIUM_BATON);
		handheldItem(MSItems.WIND_WAKER);
		handheldItem(MSItems.CELESTIAL_FULCRUM);
		handheldItem(MSItems.HYMN_FOR_HORRORTERRORS);
		
		//Axes
		handheldItem(MSItems.BATLEACKS);
		handheldItem(MSItems.COPSE_CRUSHER);
		handheldItem(MSItems.QUENCH_CRUSHER);
		handheldItem(MSItems.MELONSBANE);
		handheldItem(MSItems.CROP_CHOP);
		handheldItem(MSItems.THE_LAST_STRAW);
		handheldItem(MSItems.CANDY_BATTLEAXE, "candy_axe");
		handheldItem(MSItems.CHOCO_LOCO_WOODSPLITTER);
		handheldItem(MSItems.STEEL_EDGE_CANDYCUTTER);
		handheldItem(MSItems.BLACKSMITH_BANE);
		handheldItem(MSItems.REGIAXE);
		handheldItem(MSItems.GOTHY_AXE);
		handheldItem(MSItems.SURPRISE_AXE);
		handheldItem(MSItems.SHOCK_AXE);
		handheldItem(MSItems.SHOCK_AXE_UNPOWERED);
		handheldItem(MSItems.SCRAXE);
		handheldItem(MSItems.LORENTZ_DISTRANSFORMATIONER);
		handheldItem(MSItems.PISTON_POWERED_POGO_AXEHAMMER);
		handheldItem(MSItems.RUBY_CROAK);
		handheldItem(MSItems.HEPHAESTUS_LUMBERJACK);
		handheldItem(MSItems.FISSION_FOCUSED_FAULT_FELLER);
		
		//Dice
		handheldItem(MSItems.FLUORITE_OCTET);
		
		//Chainsaws
		handheldItem(MSItems.LIPSTICK_CHAINSAW, "chainsaw");
		handheldItem(MSItems.LIPSTICK);
		handheldItem(MSItems.CAKESAW);
		handheldItem(MSItems.CAKESAW_LIPSTICK);
		handheldItem(MSItems.MAGENTA_MAULER);
		handheldItem(MSItems.MAGENTA_MAULER_LIPSTICK);
		handheldItem(MSItems.THISTLEBLOWER);
		handheldItem(MSItems.THISTLEBLOWER_LIPSTICK);
		handheldItem(MSItems.HAND_CRANKED_VAMPIRE_ERASER);
		handheldItem(MSItems.HAND_CRANKED_VAMPIRE_ERASER_LIPSTICK);
		handheldItem(MSItems.EMERALD_IMMOLATOR);
		handheldItem(MSItems.EMERALD_IMMOLATOR_LIPSTICK);
		handheldItem(MSItems.OBSIDIATOR);
		handheldItem(MSItems.OBSIDIATOR_LIPSTICK);
		handheldItem(MSItems.DEVILS_DELIGHT);
		handheldItem(MSItems.DEVILS_DELIGHT_LIPSTICK);
		handheldItem(MSItems.DEMONBANE_RAGRIPPER);
		handheldItem(MSItems.DEMONBANE_RAGRIPPER_LIPSTICK);
		handheldItem(MSItems.FROSTTOOTH);
		handheldItem(MSItems.FROSTTOOTH_LIPSTICK);
		
		//Lances
		handheldItem(MSItems.LUCERNE_HAMMER);
		handheldItem(MSItems.LUCERNE_HAMMER_OF_UNDYING);
		
		//Fans
		handheldItem(MSItems.FAN);
		handheldItem(MSItems.CANDY_FAN);
		handheldItem(MSItems.SPINES_OF_FLUTHLU);
		handheldItem(MSItems.RAZOR_FAN);
		handheldItem(MSItems.MOTOR_FAN);
		handheldItem(MSItems.ATOMIC_VAPORIZER);
		handheldItem(MSItems.SHAVING_FAN);
		handheldItem(MSItems.FIRESTARTER);
		handheldItem(MSItems.STAR_RAY);
		handheldItem(MSItems.TYPHONIC_TRIVIALIZER);
		
		//Sickles
		handheldItem(MSItems.SICKLE);
		handheldItem(MSItems.BISICKLE);
		handheldItem(MSItems.OW_THE_EDGE);
		handheldItem(MSItems.HEMEOREAPER);
		handheldItem(MSItems.THORNY_SUBJECT);
		handheldItem(MSItems.SNOW_WHITE_DREAM);
		handheldItem(MSItems.HOMES_SMELL_YA_LATER);
		handheldItem(MSItems.FUDGESICKLE);
		handheldItem(MSItems.REGISICKLE);
		handheldItem(MSItems.HERETICUS_AURURM);
		handheldItem(MSItems.CLAW_SICKLE);
		handheldItem(MSItems.CLAW_OF_NRUBYIGLITH);
		handheldItem(MSItems.CANDY_SICKLE);
		
		handheldItem(MSItems.HELLBRINGERS_HOE_INACTIVE);
		handheldItem(MSItems.HELLBRINGERS_HOE_ACTIVE);
		
		//Clubs
		handheldItem(MSItems.DEUCE_CLUB);
		handheldItem(MSItems.STALE_BAGUETTE);
		handheldItem(MSItems.GLUB_CLUB);
		handheldItem(MSItems.NIGHT_CLUB);
		handheldItem(MSItems.NIGHTSTICK);
		handheldItem(MSItems.RED_EYES);
		handheldItem(MSItems.PRISMARINE_BASHER);
		handheldItem(MSItems.CLUB_ZERO);
		handheldItem(MSItems.POGO_CLUB);
		handheldItem(MSItems.BARBER_BASHER);
		handheldItem(MSItems.METAL_BAT);
		handheldItem(MSItems.CRICKET_BAT);
		handheldItem(MSItems.CLOWN_CLUB);
		handheldItem(MSItems.DOCTOR_DETERRENT);
		handheldItem(MSItems.MACE);
		handheldItem(MSItems.M_ACE);
		handheldItem(MSItems.M_ACE_OF_CLUBS);
		handheldItem(MSItems.DESOLATOR_MACE);
		handheldItem(MSItems.BLAZING_GLORY);
		handheldItem(MSItems.SPIKED_CLUB);
		handheldItem(MSItems.RUBIKS_MACE);
		handheldItem(MSItems.HOME_GROWN_MACE);
		handheldItem(MSItems.CARNIE_CLUB);
		handheldItem(MSItems.TOFFEE_CLUB);
		
		handheldItem(MSItems.HORSE_HITCHER);
		handheldItem(MSItems.ACE_OF_SPADES, "ace_spades");
		handheldItem(MSItems.CLUB_OF_FELONY);
		handheldItem(MSItems.ACE_OF_CLUBS, "ace_clubs");
		handheldItem(MSItems.CUESTICK);
		handheldItem(MSItems.ACE_OF_DIAMONDS, "ace_diamonds");
		handheldItem(MSItems.ACE_OF_HEARTS, "ace_hearts");
		
		//Staffs
		handheldItem(MSItems.BO_STAFF);
		handheldItem(MSItems.BAMBOO_BEATSTICK);
		handheldItem(MSItems.TELESCOPIC_BEATDOWN_BRUISER);
		handheldItem(MSItems.ION_DESTABILIZER);
		
		handheldItem(MSItems.WIZARD_STAFF);
		handheldItem(MSItems.BARBERS_MAGIC_TOUCH);
		handheldItem(MSItems.WATER_STAFF);
		handheldItem(MSItems.FIRE_STAFF);
		handheldItem(MSItems.WHITE_KINGS_SCEPTER);
		handheldItem(MSItems.BLACK_KINGS_SCEPTER);
		handheldItem(MSItems.PRIME_STAFF);
		
		//Canes
		handheldItem(MSItems.CANE);
		handheldItem(MSItems.VAUDEVILLE_HOOK);
		handheldItem(MSItems.BEAR_POKING_STICK);
		handheldItem(MSItems.CROWBAR);
		handheldItem(MSItems.UMBRELLA);
		handheldItem(MSItems.BARBERS_BEST_FRIEND);
		handheldItem(MSItems.UPPER_CRUST_CRUST_CANE);
		handheldItem(MSItems.IRON_CANE);
		handheldItem(MSItems.ZEPHYR_CANE);
		handheldItem(MSItems.SPEAR_CANE);
		handheldItem(MSItems.PARADISES_PORTABELLO);
		handheldItem(MSItems.REGI_CANE);
		handheldItem(MSItems.POGO_CANE);
		handheldItem(MSItems.CANDY_CANE);
		handheldItem(MSItems.SHARP_CANDY_CANE);
		handheldItem(MSItems.PRIM_AND_PROPER_WALKING_POLE);
		handheldItem(MSItems.DRAGON_CANE);
		handheldItem(MSItems.DRAGON_CANE_UNSHEATHED);
		handheldItem(MSItems.CHANCEWYRMS_EXTRA_FORTUNATE_STABBING_IMPLEMENT);
		handheldItem(MSItems.CHANCEWYRMS_EXTRA_FORTUNATE_STABBING_IMPLEMENT_UNSHEATHED);
		handheldItem(MSItems.LESS_PROPER_WALKING_STICK);
		handheldItem(MSItems.LESS_PROPER_WALKING_STICK_SHEATHED);
		handheldItem(MSItems.ROCKEFELLERS_WALKING_BLADECANE, "rockefellers_walking_bladecane_unsheathed");
		handheldItem(MSItems.ROCKEFELLERS_WALKING_BLADECANE_SHEATHED, "rockefellers_walking_bladecane");
		
		//Spoons/Forks
		handheldItem(MSItems.WOODEN_SPOON);
		handheldItem(MSItems.SILVER_SPOON);
		handheldItem(MSItems.MELONBALLER);
		handheldItem(MSItems.SIGHTSEEKER);
		handheldItem(MSItems.TERRAIN_FLATENATOR);
		handheldItem(MSItems.NOSFERATU_SPOON);
		handheldItem(MSItems.THRONGLER);
		handheldItem(MSItems.WET_MEAT_SHIT_THRONGLER);
		handheldItem(MSItems.CROCKER_SPOON);
		handheldItem(MSItems.CROCKER_FORK);
		handheldItem(MSItems.EDISONS_FURY);
		handheldItem(MSItems.EDISONS_SERENITY);
		
		handheldItem(MSItems.FORK);
		handheldItem(MSItems.CANDY_FORK);
		handheldItem(MSItems.TUNING_FORK);
		handheldItem(MSItems.ELECTRIC_FORK);
		handheldItem(MSItems.EATING_FORK_GEM);
		handheldItem(MSItems.DEVIL_FORK);
		
		handheldItem(MSItems.SPORK);
		handheldItem(MSItems.GOLDEN_SPORK);
		
		handheldItem(MSItems.MEATFORK);
		handheldItem(MSItems.BIDENT);
		
		//Needles/Wands
		handheldItem(MSItems.POINTY_STICK);
		handheldItem(MSItems.KNITTING_NEEDLE);
		
		handheldItem(MSItems.WAND);
		handheldItem(MSItems.NEEDLE_WAND);
		handheldItem(MSItems.ARTIFUCKER);
		handheldItem(MSItems.POINTER_WAND);
		handheldItem(MSItems.POOL_CUE_WAND);
		handheldItem(MSItems.THORN_OF_OGLOGOTH);
		handheldItem(MSItems.THISTLE_OF_ZILLYWICH);
		handheldItem(MSItems.QUILL_OF_ECHIDNA);
		
		//Projectiles
		simpleItem(MSItems.SBAHJARANG);
		handheldItem(MSItems.SHURIKEN);
		simpleItem(MSItems.CLUBS_SUITARANG);
		simpleItem(MSItems.DIAMONDS_SUITARANG);
		simpleItem(MSItems.HEARTS_SUITARANG);
		simpleItem(MSItems.SPADES_SUITARANG);
		
		handheldItem(MSItems.CHAKRAM);
		simpleItem(MSItems.UMBRAL_INFILTRATOR);
		
		simpleItem(MSItems.SORCERERS_PINBALL);
		
		//Material Tools
		handheldItem(MSItems.EMERALD_SWORD);
		handheldItem(MSItems.EMERALD_AXE);
		handheldItem(MSItems.EMERALD_PICKAXE);
		handheldItem(MSItems.EMERALD_SHOVEL);
		handheldItem(MSItems.EMERALD_HOE);
		handheldItem(MSItems.MINE_AND_GRIST);
		
		//Armor
		simpleItem(MSItems.PRISMARINE_HELMET);
		simpleItem(MSItems.PRISMARINE_CHESTPLATE);
		simpleItem(MSItems.PRISMARINE_LEGGINGS);
		simpleItem(MSItems.PRISMARINE_BOOTS);
		simpleItem(MSItems.IRON_LASS_GLASSES);
		simpleItem(MSItems.IRON_LASS_CHESTPLATE);
		simpleItem(MSItems.IRON_LASS_SKIRT);
		simpleItem(MSItems.IRON_LASS_SHOES);
		
		simpleItem(MSItems.PROSPIT_CIRCLET);
		simpleItem(MSItems.PROSPIT_SHIRT);
		simpleItem(MSItems.PROSPIT_PANTS);
		simpleItem(MSItems.PROSPIT_SHOES);
		simpleItem(MSItems.DERSE_CIRCLET);
		simpleItem(MSItems.DERSE_SHIRT);
		simpleItem(MSItems.DERSE_PANTS);
		simpleItem(MSItems.DERSE_SHOES);
		
		simpleItem(MSItems.AMPHIBEANIE);
		simpleItem(MSItems.NOSTRILDAMUS);
		simpleItem(MSItems.PONYTAIL);
		
		//Scalemates
		simpleItem(MSItems.SCALEMATE_APPLESCAB);
		simpleItem(MSItems.SCALEMATE_BERRYBREATH);
		simpleItem(MSItems.SCALEMATE_CINNAMONWHIFF);
		simpleItem(MSItems.SCALEMATE_HONEYTONGUE);
		simpleItem(MSItems.SCALEMATE_LEMONSNOUT);
		simpleItem(MSItems.SCALEMATE_PINESNORT);
		simpleItem(MSItems.SCALEMATE_PUCEFOOT);
		simpleItem(MSItems.SCALEMATE_PUMPKINSNUFFLE);
		simpleItem(MSItems.SCALEMATE_PYRALSPITE);
		simpleItem(MSItems.SCALEMATE_WITNESS);
		
		simpleItem(MSItems.PLUSH_MUTATED_CAT);
		
		
	}
	
	private ItemModelBuilder simpleItem(RegistryObject<? extends Item> item)
	{
		return withExistingParent(item.getId().getPath(),
				new ResourceLocation("item/generated")).texture("layer0",
				new ResourceLocation(Minestuck.MOD_ID, "item/" + item.getId().getPath()));
	}
	
	private ItemModelBuilder simpleItem(RegistryObject<? extends Item> item, String textureName)
	{
		return withExistingParent(item.getId().getPath(),
				new ResourceLocation("item/generated")).texture("layer0",
				new ResourceLocation(Minestuck.MOD_ID, "item/" + textureName));
	}
	
	private ItemModelBuilder handheldItem(RegistryObject<? extends Item> item)
	{
		return handheldItem(item, item.getId().getPath());
	}
	
	private ItemModelBuilder handheldItem(RegistryObject<? extends Item> item, String textureName)
	{
		return withExistingParent(item.getId().getPath(),
				new ResourceLocation("item/handheld")).texture("layer0",
				new ResourceLocation(Minestuck.MOD_ID, "item/" + textureName));
	}
	
	private ItemModelBuilder blockItem(RegistryObject<Block> block)
	{
		return withExistingParent(block.getId().getPath(),
				new ResourceLocation("minestuck:block/" + block.getId().getPath()));
	}
	
	public ItemModelBuilder itemModelBlockItem(RegistryObject<Block> block)
	{
		return withExistingParent(block.getId().getPath(), new ResourceLocation("item/generated")).texture("layer0", new ResourceLocation(Minestuck.MOD_ID, "block/" + block.getId().getPath()));
	}
	
	public void buttonItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock)
	{
		this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/button_inventory")).texture("texture", new ResourceLocation(Minestuck.MOD_ID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
	}
	
	public void fenceItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock)
	{
		this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/fence_inventory")).texture("texture",  new ResourceLocation(Minestuck.MOD_ID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
	}
	
	public void wallItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock)
	{
		this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/wall_inventory")).texture("wall",  new ResourceLocation(Minestuck.MOD_ID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
	}
}

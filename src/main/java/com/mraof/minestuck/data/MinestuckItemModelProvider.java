package com.mraof.minestuck.data;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
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
		
		//Core Items
		simpleItem(MSItems.RAW_CRUXITE);
		simpleItem(MSItems.RAW_URANIUM);
		simpleItem(MSItems.ENERGY_CORE);
		
		simpleItem(MSItems.SBURB_CODE);
		simpleItem(MSItems.COMPLETED_SBURB_CODE, "sburb_code");
		simpleItem(MSItems.COMPUTER_PARTS);
		simpleItem(MSItems.BLANK_DISK);
		simpleItem(MSItems.CLIENT_DISK);
		simpleItem(MSItems.SERVER_DISK);
		
		simpleItem(MSItems.STACK_MODUS_CARD);
		simpleItem(MSItems.QUEUE_MODUS_CARD);
		simpleItem(MSItems.QUEUESTACK_MODUS_CARD);
		simpleItem(MSItems.TREE_MODUS_CARD);
		simpleItem(MSItems.HASHMAP_MODUS_CARD);
		simpleItem(MSItems.SET_MODUS_CARD);
		
		//Food
		simpleItem(MSItems.PHLEGM_GUSHERS);
		simpleItem(MSItems.SORROW_GUSHERS);
		simpleItem(MSItems.BUG_ON_A_STICK);
		simpleItem(MSItems.CHOCOLATE_BEETLE, "chocolate_covered_beetle");
		simpleItem(MSItems.CONE_OF_FLIES);
		simpleItem(MSItems.GRASSHOPPER);
		simpleItem(MSItems.CICADA);
		simpleItem(MSItems.JAR_OF_BUGS);
		simpleItem(MSItems.BUG_MAC);
		simpleItem(MSItems.ONION);
		simpleItem(MSItems.SALAD);
		simpleItem(MSItems.DESERT_FRUIT);
		simpleItem(MSItems.ROCK_COOKIE);
		simpleItem(MSItems.WOODEN_CARROT);
		simpleItem(MSItems.FUNGAL_SPORE);
		simpleItem(MSItems.SPOREO);
		simpleItem(MSItems.MOREL_MUSHROOM);
		simpleItem(MSItems.SUSHROOM);
		simpleItem(MSItems.FRENCH_FRY);
		simpleItem(MSItems.STRAWBERRY_CHUNK, "strawberry_meat");
		simpleItem(MSItems.FOOD_CAN);
		simpleItem(MSItems.CANDY_CORN);
		simpleItem(MSItems.TUIX_BAR);
		simpleItem(MSItems.BUILD_GUSHERS);
		simpleItem(MSItems.AMBER_GUMMY_WORM);
		simpleItem(MSItems.CAULK_PRETZEL);
		simpleItem(MSItems.CHALK_CANDY_CIGARETTE);
		simpleItem(MSItems.IODINE_LICORICE);
		simpleItem(MSItems.SHALE_PEEP);
		simpleItem(MSItems.TAR_LICORICE, "tar_black_licorice");
		simpleItem(MSItems.COBALT_GUM);
		simpleItem(MSItems.MARBLE_JAWBREAKER);
		simpleItem(MSItems.MERCURY_SIXLETS);
		simpleItem(MSItems.QUARTZ_JELLY_BEAN);
		simpleItem(MSItems.SULFUR_CANDY_APPLE);
		simpleItem(MSItems.AMETHYST_HARD_CANDY);
		simpleItem(MSItems.GARNET_TWIX);
		simpleItem(MSItems.RUBY_LOLLIPOP);
		simpleItem(MSItems.RUST_GUMMY_EYE);
		simpleItem(MSItems.DIAMOND_MINT);
		simpleItem(MSItems.GOLD_CANDY_RIBBON);
		simpleItem(MSItems.URANIUM_GUMMY_BEAR);
		simpleItem(MSItems.ARTIFACT_WARHEAD);
		simpleItem(MSItems.ZILLIUM_SKITTLES);
		simpleItem(MSItems.APPLE_JUICE);
		simpleItem(MSItems.TAB);
		simpleItem(MSItems.ORANGE_FAYGO);
		simpleItem(MSItems.CANDY_APPLE_FAYGO);
		simpleItem(MSItems.FAYGO_COLA);
		simpleItem(MSItems.COTTON_CANDY_FAYGO);
		simpleItem(MSItems.CREME_SODA_FAYGO);
		simpleItem(MSItems.GRAPE_FAYGO);
		simpleItem(MSItems.MOON_MIST_FAYGO);
		simpleItem(MSItems.PEACH_FAYGO);
		simpleItem(MSItems.REDPOP_FAYGO);
		simpleItem(MSItems.GRUB_SAUCE);
		simpleItem(MSItems.IRRADIATED_STEAK);
		simpleItem(MSItems.SURPRISE_EMBRYO, "kundler_surprise_embryo");
		simpleItem(MSItems.UNKNOWABLE_EGG);
		simpleItem(MSItems.BREADCRUMBS);
		
		//Other Land Items
		simpleItem(MSItems.GOLDEN_GRASSHOPPER);
		handheldItem(MSItems.BUG_NET, "frog_net");
		simpleItem(MSItems.CARVING_TOOL);
		simpleItem(MSItems.CRUMPLY_HAT);
		simpleItem(MSItems.STONE_EYEBALLS);
		simpleItem(MSItems.SHOP_POSTER, "shop_poster1");
		simpleItem(MSItems.GUTTER_THUMB_DRIVE);
		simpleItem(MSItems.ANCIENT_THUMB_DRIVE);
		simpleItem(MSItems.GUTTER_BALL);
		
		//Buckets
		simpleItem(MSItems.OIL_BUCKET);
		simpleItem(MSItems.BLOOD_BUCKET);
		simpleItem(MSItems.BRAIN_JUICE_BUCKET);
		simpleItem(MSItems.WATER_COLORS_BUCKET);
		simpleItem(MSItems.ENDER_BUCKET);
		simpleItem(MSItems.LIGHT_WATER_BUCKET);
		simpleItem(MSItems.OBSIDIAN_BUCKET);
		
		
		//Alchemy Items
		handheldItem(MSItems.DICE);
		simpleItem(MSItems.PLUTONIUM_CORE);
		simpleItem(MSItems.GRIMOIRE);
		simpleItem(MSItems.BATTERY);
		simpleItem(MSItems.BARBASOL);
		simpleItem(MSItems.CLOTHES_IRON);
		simpleItem(MSItems.INK_SQUID_PRO_QUO);
		simpleItem(MSItems.CUEBALL, "magic_cueball");
		simpleItem(MSItems.EIGHTBALL);
		simpleItem(MSItems.FLARP_MANUAL);
		simpleItem(MSItems.SASSACRE_TEXT);
		simpleItem(MSItems.WISEGUY);
		simpleItem(MSItems.TABLESTUCK_MANUAL);
		simpleItem(MSItems.TILLDEATH_HANDBOOK);
		simpleItem(MSItems.BINARY_CODE);
		simpleItem(MSItems.NONBINARY_CODE);
		simpleItem(MSItems.THRESH_DVD);
		simpleItem(MSItems.GAMEBRO_MAGAZINE, "game_bro");
		simpleItem(MSItems.GAMEGRL_MAGAZINE, "game_grl");
		simpleItem(MSItems.CREW_POSTER);
		simpleItem(MSItems.SBAHJ_POSTER);
		simpleItem(MSItems.BI_DYE);
		handheldItem(MSItems.LIP_BALM);
		simpleItem(MSItems.ELECTRIC_AUTOHARP);
		handheldItem(MSItems.CARDBOARD_TUBE);
		simpleItem(MSItems.CRYPTID_PHOTO);
		simpleItem(MSItems.PARTICLE_ACCELERATOR);
		
		//Other
		simpleItem(MSItems.CAPTCHAROID_CAMERA);
		handheldItem(MSItems.LONG_FORGOTTEN_WARHORN);
		simpleItem(MSItems.BLACK_QUEENS_RING, "queens_ring");
		simpleItem(MSItems.WHITE_QUEENS_RING, "queens_ring");
		simpleItem(MSItems.BARBASOL_BOMB);
		simpleItem(MSItems.RAZOR_BLADE);
		simpleItem(MSItems.ICE_SHARD);
		simpleItem(MSItems.HORN);
		simpleItem(MSItems.CAKE_MIX);
		
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
		
		//Incredibly Useful Items
		handheldItem(MSItems.URANIUM_POWERED_STICK);
		simpleItem(MSItems.IRON_BOAT);
		simpleItem(MSItems.GOLD_BOAT);
		simpleItem(MSItems.COCOA_WART);
		simpleItem(MSItems.HORSE_CLOCK);
		
		//Music Discs/Cassettes
		simpleItem(MSItems.MUSIC_DISC_EMISSARY_OF_DANCE, "record_emissary");
		simpleItem(MSItems.MUSIC_DISC_DANCE_STAB_DANCE, "record_dance_stab");
		simpleItem(MSItems.MUSIC_DISC_RETRO_BATTLE, "record_retro_battle");
		
		simpleItem(MSItems.CASSETTE_13, "cassette/cassette_13");
		simpleItem(MSItems.CASSETTE_CAT, "cassette/cassette_cat");
		simpleItem(MSItems.CASSETTE_BLOCKS, "cassette/cassette_blocks");
		simpleItem(MSItems.CASSETTE_CHIRP, "cassette/cassette_chirp");
		simpleItem(MSItems.CASSETTE_FAR, "cassette/cassette_far");
		simpleItem(MSItems.CASSETTE_MALL, "cassette/cassette_mall");
		simpleItem(MSItems.CASSETTE_MELLOHI, "cassette/cassette_mellohi");
		simpleItem(MSItems.CASSETTE_DANCE_STAB, "cassette/cassette_dance_stab");
		simpleItem(MSItems.CASSETTE_RETRO_BATTLE, "cassette/cassette_retro_battle");
		simpleItem(MSItems.CASSETTE_EMISSARY, "cassette/cassette_emissary");
		simpleItem(MSItems.CASSETTE_11, "cassette/cassette_11");
		simpleItem(MSItems.CASSETTE_PIGSTEP, "cassette/cassette_pigstep");
		simpleItem(MSItems.CASSETTE_STAL, "cassette/cassette_stal");
		simpleItem(MSItems.CASSETTE_STRAD, "cassette/cassette_strad");
		simpleItem(MSItems.CASSETTE_WAIT, "cassette/cassette_wait");
		simpleItem(MSItems.CASSETTE_WARD, "cassette/cassette_ward");
		simpleItem(MSItems.CASSETTE_OTHERSIDE, "cassette/cassette_otherside");
		simpleItem(MSItems.CASSETTE_5, "cassette/cassette_5");
		
	}
	
	private ItemModelBuilder simpleItem(RegistryObject<? extends Item> item)
	{
		return simpleItem(item, item.getId().getPath());
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
}

package com.mraof.minestuck.item;

import com.mraof.minestuck.block.BlockColoredDirt;
import com.mraof.minestuck.block.BlockCrockerMachine;
import com.mraof.minestuck.block.BlockMinestuckLog;
import com.mraof.minestuck.block.BlockMinestuckStone;
import com.mraof.minestuck.entity.item.EntityCrewPoster;
import com.mraof.minestuck.entity.item.EntitySbahjPoster;
import com.mraof.minestuck.item.block.ItemBlockCraftingTab;
import com.mraof.minestuck.item.block.ItemBlockLayered;
import com.mraof.minestuck.item.block.ItemSburbMachine;
import com.mraof.minestuck.item.weapon.*;
import com.mraof.minestuck.util.MinestuckSoundHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityHanging;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import static com.mraof.minestuck.block.MinestuckBlocks.*;

public class MinestuckItems
{
	
	public static final CreativeTabs tabMinestuck = new CreativeTabs("tabMinestuck")
{
	@Override
	public ItemStack getTabIconItem()
	{
		return new ItemStack(MinestuckItems.zillyhooHammer);
	}
};
	public static Item.ToolMaterial toolEmerald = EnumHelper.addToolMaterial("EMERALD", 3, 1220, 12.0F, 4.0F, 12).setRepairItem(new ItemStack(Items.EMERALD));;
	public static ItemArmor.ArmorMaterial armorPrismarine = EnumHelper.addArmorMaterial("PRISMARINE", "minestuck:prismarine", 20, new int[]{3, 7, 6, 2}, 15, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);
	
	//hammers
	public static Item clawHammer = new ItemWeapon(131, 4.0D, -2.4D, 10, "clawHammer").setTool("pickaxe", 0, 1.0F).setRegistryName("claw_hammer");
	public static Item sledgeHammer = new ItemWeapon(250, 6.0D, -2.8D, 8, "sledgeHammer").setTool("pickaxe", 2, 4.0F).setRegistryName("sledge_hammer");
	public static Item blacksmithHammer = new ItemWeapon(450, 7.0D, -2.8D, 10, "blacksmithHammer").setTool("pickaxe", 2, 3.5F).setRegistryName("blacksmith_hammer");
	public static Item pogoHammer = new ItemPogoWeapon(400, 7.0D, -2.8D, 8, "pogoHammer", 0.7).setTool("pickaxe", 1, 2.0F).setRegistryName("pogo_hammer");
	public static Item telescopicSassacrusher = new ItemWeapon(1024, 9.0D, -2.9D, 15, "telescopicSassacrusher").setTool("pickaxe", 2, 5.0F).setRegistryName("telescopic_sassacrusher");
	public static Item fearNoAnvil = new ItemPotionWeapon(2048, 10.0D, -2.8D, 12, "fearNoAnvil", new PotionEffect(MobEffects.SLOWNESS,100,3)).setTool("pickaxe", 3, 7.0F).setRegistryName("fear_no_anvil");
	public static Item zillyhooHammer = new ItemWeapon(3000, 11.0D, -2.8D, 30, "zillyhooHammer").setTool("pickaxe", 4, 15.0F).setRegistryName("zillyhoo_hammer");
	public static Item popamaticVrillyhoo = new ItemWeapon(3000, 0.0D, -2.8D, 30, "popamaticVrillyhoo").setTool("pickaxe", 4, 15.0F).setRegistryName("popamatic_vrillyhoo");
	public static Item scarletZillyhoo = new ItemFireWeapon(2000, 11.0D, -2.8D, 16, "scarletZillyhoo", 50).setTool("pickaxe", 3, 4.0F).setRegistryName("scarlet_zillyhoo");
	public static Item mwrthwl = new ItemWeapon(2000, 10.5D, -2.8D, 16, "mwrthwl").setTool("pickaxe", 3, 4.0F).setRegistryName("mwrthwl");
	//blades
	public static Item sord = new ItemSord(59, 2, -2.4D, 5, "sord").setRegistryName("sord");
	public static Item cactusCutlass = new ItemWeapon(104, 4, -2.4D, 10, "cactaceaeCutlass").setRegistryName("cactaceae_cutlass");
	public static Item katana = new ItemWeapon(250, 5, -2.4D, 15, "ninjaSword").setRegistryName("katana");
	public static Item unbreakableKatana = new ItemWeapon(2200, 7, -2.4D, 20, "katana").setRegistryName("unbreakable_katana");	//Not actually unbreakable
	public static Item firePoker = new ItemFireWeapon(250, 6, -2.4D, 15, "firePoker", 30).setRegistryName("fire_poker");
	public static Item hotHandle = new ItemFireWeapon(350, 5, -2.4D, 15, "hotHandle", 10).setRegistryName("too_hot_to_handle");
	public static Item caledscratch = new ItemWeapon(1561, 6, -2.4D, 30, "caledscratch").setRegistryName("caledscratch");
	public static Item caledfwlch = new ItemWeapon(1025, 6, -2.4D, 30, "caledfwlch").setRegistryName("caledfwlch");
	public static Item royalDeringer = new ItemWeapon(1561, 7, -2.4D, 30, "royalDeringer").setRegistryName("royal_deringer");
	public static Item zillywairCutlass = new ItemWeapon(2500, 8, -2.4D, 30, "zillywairCutlass").setRegistryName("cutlass_of_zillywair");
	public static Item regisword = new ItemWeapon(812, 6, -2.4D, 10, "regisword").setRegistryName("regisword");
	public static Item scarletRibbitar = new ItemWeapon(2000, 7, -2.4D, 30, "scarletRibbitar").setRegistryName("scarlet_ribbitar");
	public static Item doggMachete = new ItemWeapon(1000, 5, -2.4D, 30, "doggMachete").setRegistryName("dogg_machete");
	public static Item cobaltSabre = new ItemFireWeapon(300, 7, -2.4D, 10, "cobaltSabre", 30).setRegistryName("cobalt_sabre");
	//axes
	public static Item copseCrusher = new ItemWeapon(400, 6.0D, -3.0D, 20, "copseCrusher", -1, 20).setTool("axe", 2, 7.0F).setRegistryName("copse_crusher");
	public static Item blacksmithBane = new ItemWeapon(413, 9.0D, -3.0D, 15, "blacksmithBane").setTool("axe", 2, 6.0F).setRegistryName("blacksmith_bane");
	public static Item scraxe = new ItemWeapon(500, 10.0D, -3.0D, 20, "scraxe").setTool("axe", 2, 7.0F).setRegistryName("scraxe");
	public static Item qPHammerAxe = new ItemPogoWeapon(800, 8.0D, -3.0D, 30, "qPHammerAxe", 0.6).setTool("axe", 2, 7.0F).setTool("pickaxe", 1, 2.0F).setTerminus(-1, 50).setRegistryName("piston_powered_pogo_axehammer");
	public static Item rubyCroak = new ItemWeapon(2000, 11.0D, -2.9D, 30, "rubyCroak").setTool("axe", 3, 8.0F).setRegistryName("ruby_croak");
	public static Item hephaestusLumber = new ItemFireWeapon(3000, 11.0D, -3.0D, 30, "hephaestusLumber", 30).setTool("axe", 3, 9.0F).setRegistryName("hephaestus_lumberjack");
	//Dice
	public static Item dice = new ItemWeapon(51, 6, 3, 6, "dice").setRegistryName("dice");
	public static Item fluoriteOctet = new ItemWeapon(67, 15, 6, 8, "fluoriteOctet").setRegistryName("fluorite_octet");
	//mic weapons
	public static Item catClaws = new ItemDualWeapon(500, 4.0D, 1.0D, -1.5D,-1.0D, 6, "catclaws").setRegistryName("catclaws");
	//sickles
	public static Item sickle = new ItemWeapon(220, 4.0D, -2.4D, 8, "sickle").setRegistryName("sickle");
	public static Item homesSmellYaLater = new ItemWeapon(400, 5.5D, -2.4D, 10, "homesSmellYaLater").setRegistryName("homes_smell_ya_later");
	public static Item fudgeSickle = new ItemWeapon(450, 5.5D, -2.4D, 10, "fudgeSickle").setRegistryName("fudgesickle");
	public static Item regiSickle = new ItemWeapon(812, 6.0D, -2.4D, 5, "regiSickle").setRegistryName("regisickle");
	public static Item clawSickle = new ItemWeapon(2048, 7.0D, -2.4D, 15, "clawSickle").setRegistryName("claw_sickle");
	public static Item candySickle = new ItemCandyWeapon(96, 6.0D, -2.4D, 15, "candySickle").setRegistryName("candy_sickle");
	//clubs
	public static Item deuceClub = new ItemWeapon(1024, 2.5D, -2.2D, 15, "deuceClub").setRegistryName("deuce_club");
	public static Item nightClub = new ItemWeapon(600, 4.0D, -2.2D, 20, "nightClub").setRegistryName("nightclub");
	public static Item pogoClub = new ItemPogoWeapon(600, 3.5D, -2.2D, 15, "pogoClub", 0.5).setRegistryName("pogo_club");
	public static Item metalBat = new ItemWeapon(750, 5.0D, -2.2D, 5, "metalBat").setRegistryName("metal_bat");
	public static Item spikedClub = new ItemWeapon(500, 5.5D, -2.2D, 5, "spikedClub").setRegistryName("spiked_club");
	//canes
	public static Item cane = new ItemWeapon(100, 2.0D, -2.0D, 15, "cane").setRegistryName("cane");
	public static Item spearCane = new ItemWeapon(300, 4.0D, -2.0D, 13, "spearCane").setRegistryName("spear_cane");
	public static Item dragonCane = new ItemWeapon(300, 6.5D, -2.0D, 20, "dragonCane").setRegistryName("dragon_cane");
	//Spoons/forks
	public static Item woodenSpoon = new ItemWeapon(59, 2.0D, -2.2D, 5, "woodenSpoon").setRegistryName("wooden_spoon");
	public static Item silverSpoon = new ItemWeapon(250, 2.5D, -2.2D, 12, "silverSpoon").setRegistryName("silver_spoon");
	public static ItemSpork crockerSpork = (ItemSpork) new ItemSpork(512, 4.0D, -2.2D, 15, "crocker").setRegistryName("crocker_spork");
	public static Item skaiaFork = new ItemWeapon(2048, 8.5D, -2.2D, 10, "skaiaFork").setRegistryName("skaia_fork");
	public static Item fork = new ItemWeapon(100, 4.0D, -2.2D, 3, "fork").setRegistryName("fork");
	public static Item spork = new ItemWeapon(120, 4.5D, -2.3D, 5, "spork").setRegistryName("spork");
	//Material tools
	public static Item emeraldSword = new ItemSword(toolEmerald).setRegistryName("emerald_sword").setUnlocalizedName("swordEmerald").setCreativeTab(tabMinestuck);
	public static Item emeraldAxe = new ItemMinestuckAxe(toolEmerald, 9.0F, -3.0F).setRegistryName("emerald_axe").setUnlocalizedName("hatchetEmerald").setCreativeTab(tabMinestuck);
	public static Item emeraldPickaxe = new ItemMinestuckPickaxe(toolEmerald).setRegistryName("emerald_pickaxe").setUnlocalizedName("pickaxeEmerald").setCreativeTab(tabMinestuck);
	public static Item emeraldShovel = new ItemSpade(toolEmerald).setRegistryName("emerald_shovel").setUnlocalizedName("shovelEmerald").setCreativeTab(tabMinestuck);
	public static Item emeraldHoe = new ItemHoe(toolEmerald).setRegistryName("emerald_hoe").setUnlocalizedName("hoeEmerald").setCreativeTab(tabMinestuck);
	//Armor
	public static Item prismarineHelmet = new ItemArmor(armorPrismarine, 0, EntityEquipmentSlot.HEAD).setRegistryName("prismarine_helmet").setUnlocalizedName("helmetPrismarine").setCreativeTab(tabMinestuck);
	public static Item prismarineChestplate = new ItemArmor(armorPrismarine, 0, EntityEquipmentSlot.CHEST).setRegistryName("prismarine_chestplate").setUnlocalizedName("chestplatePrismarine").setCreativeTab(tabMinestuck);
	public static Item prismarineLeggings = new ItemArmor(armorPrismarine, 0, EntityEquipmentSlot.LEGS).setRegistryName("prismarine_leggings").setUnlocalizedName("leggingsPrismarine").setCreativeTab(tabMinestuck);
	public static Item prismarineBoots = new ItemArmor(armorPrismarine, 0, EntityEquipmentSlot.FEET).setRegistryName("prismarine_boots").setUnlocalizedName("bootsPrismarine").setCreativeTab(tabMinestuck);
	//Food
	public static Item candy = new ItemMinestuckCandy().setRegistryName("candy");
	public static Item beverage = new ItemMinestuckBeverage().setRegistryName("beverage");
	public static Item bugOnAStick = new ItemFood(1, 0.1F, false).setRegistryName("bug_on_stick").setUnlocalizedName("bugOnAStick").setCreativeTab(tabMinestuck);
	public static Item chocolateBeetle = new ItemFood(3, 0.4F, false).setRegistryName("chocolate_beetle").setUnlocalizedName("chocolateBeetle").setCreativeTab(tabMinestuck);
	public static Item coneOfFlies = new ItemFood(2, 0.1F, false).setRegistryName("cone_of_flies").setUnlocalizedName("coneOfFlies").setCreativeTab(tabMinestuck);
	public static Item grasshopper = new ItemFood(4, 0.5F, false).setRegistryName("grasshopper").setUnlocalizedName("grasshopper").setCreativeTab(tabMinestuck);
	public static Item jarOfBugs = new ItemFood(3, 0.2F, false).setRegistryName("jar_of_bugs").setUnlocalizedName("jarOfBugs").setCreativeTab(tabMinestuck);
	public static Item onion = new ItemFood(2, 0.2F, false).setRegistryName("onion").setUnlocalizedName("onion").setCreativeTab(tabMinestuck);
	public static Item salad = new ItemSoup(1).setRegistryName("salad").setUnlocalizedName("salad").setCreativeTab(tabMinestuck);
	//Other
	public static Item rawCruxite = new Item().setRegistryName("raw_cruxite").setUnlocalizedName("rawCruxite").setCreativeTab(tabMinestuck);
	public static Item cruxiteDowel = new ItemDowel().setRegistryName("cruxite_dowel");
	public static Item captchaCard = new ItemCaptchaCard().setRegistryName("captcha_card");
	public static ItemCruxiteArtifact cruxiteApple = (ItemCruxiteArtifact) new ItemCruxiteApple().setRegistryName("cruxite_apple");
	public static Item cruxitePotion = new ItemCruxitePotion().setRegistryName("cruxite_potion");
	public static Item disk = new ItemDisk().setRegistryName("computer_disk");
	public static Item chessboard = new Item().setRegistryName("chessboard").setUnlocalizedName("chessboard").setMaxStackSize(1).setCreativeTab(tabMinestuck);
	public static ItemMinestuckBucket minestuckBucket = (ItemMinestuckBucket) new ItemMinestuckBucket().setRegistryName("minestuck_bucket");
	public static Item obsidianBucket = new ItemObsidianBucket().setRegistryName("bucket_obsidian");
	public static Item modusCard = new ItemModus().setRegistryName("modus_card");
	public static Item goldSeeds = new ItemGoldSeeds().setRegistryName("gold_seeds");
	public static Item metalBoat = new ItemMetalBoat().setRegistryName("metal_boat");
	public static Item threshDvd = new Item().setRegistryName("thresh_dvd").setUnlocalizedName("threshDvd").setMaxStackSize(1).setCreativeTab(tabMinestuck);
	public static Item crewPoster = new ItemHanging(){
		@Override
		public EntityHanging createEntity(World worldIn, BlockPos pos, EnumFacing facing, ItemStack stack)
		{
			return new EntityCrewPoster(worldIn, pos, facing);
		}
	}.setRegistryName("crew_poster").setUnlocalizedName("crewPoster").setMaxStackSize(1).setCreativeTab(tabMinestuck);
	public static Item sbahjPoster = new ItemHanging(){
		@Override
		public EntityHanging createEntity(World worldIn, BlockPos pos, EnumFacing facing, ItemStack stack)
		{
			return new EntitySbahjPoster(worldIn, pos, facing);
		}
	}.setRegistryName("sbahj_poster").setUnlocalizedName("sbahjPoster").setMaxStackSize(1).setCreativeTab(tabMinestuck);
	public static Item carvingTool = new Item().setRegistryName("carving_tool").setUnlocalizedName("carvingTool").setMaxStackSize(1).setCreativeTab(tabMinestuck);
	public static Item crumplyHat = new Item().setRegistryName("crumply_hat").setUnlocalizedName("crumplyHat").setMaxStackSize(1).setCreativeTab(tabMinestuck);
	public static Item frogStatueReplica = new Item().setRegistryName("frog_statue_replica").setUnlocalizedName("frogStatueReplica").setCreativeTab(tabMinestuck);
	public static Item stoneSlab = new Item().setRegistryName("stone_slab").setUnlocalizedName("stoneSlab").setCreativeTab(tabMinestuck);
	public static Item glowystoneDust = new ItemGlowystoneDust().setRegistryName("glowystone_dust").setUnlocalizedName("glowystoneDust").setCreativeTab(tabMinestuck);
	//Music disks
	public static Item recordEmissaryOfDance = new ItemMinestuckRecord("emissary", MinestuckSoundHandler.soundEmissaryOfDance).setRegistryName("record_emissary").setUnlocalizedName("record");
	public static Item recordDanceStab = new ItemMinestuckRecord("danceStab", MinestuckSoundHandler.soundDanceStabDance).setRegistryName("record_dance_stab").setUnlocalizedName("record");
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		armorPrismarine.repairMaterial = new ItemStack(Items.PRISMARINE_SHARD);
		
		IForgeRegistry<Item> registry = event.getRegistry();
		registerItemBlock(registry, new ItemMultiTexture(chessTile, chessTile, new String[] {"black", "white", "darkgrey", "lightgrey"}));
		registerItemBlock(registry, new ItemBlock(skaiaPortal));
		
		registerItemBlock(registry, new ItemMultiTexture(oreCruxite, oreCruxite, new String[0])
		{
			@Override
			public String getUnlocalizedName(ItemStack stack)
			{
				return block.getUnlocalizedName();
			}
		});
		registerItemBlock(registry, new ItemBlock(coalOreNetherrack));
		registerItemBlock(registry, new ItemBlock(ironOreSandstone));
		registerItemBlock(registry, new ItemBlock(ironOreSandstoneRed));
		registerItemBlock(registry, new ItemBlock(goldOreSandstone));
		registerItemBlock(registry, new ItemBlock(goldOreSandstoneRed));
		
		registerItemBlock(registry, new ItemBlockCraftingTab(cruxiteBlock, CreativeTabs.BUILDING_BLOCKS));
		registerItemBlock(registry, new ItemBlock(genericObject));
		registerItemBlock(registry, new ItemSburbMachine(sburbMachine));
		registerItemBlock(registry, new ItemMultiTexture(crockerMachine, crockerMachine, new ItemMultiTexture.Mapper()
				{
					@Override
					public String apply(ItemStack input)
					{
						return BlockCrockerMachine.MachineType.values()[input.getItemDamage()].getUnlocalizedName();
					}
				}));
		registerItemBlock(registry, new ItemBlock(blockComputerOff));
		registerItemBlock(registry, new ItemBlock(transportalizer));
		
		registerItemBlock(registry, new ItemBlockLayered(layeredSand));
		registerItemBlock(registry, new ItemMultiTexture(coloredDirt, coloredDirt, new ItemMultiTexture.Mapper()
				{
					@Override
					public String apply(ItemStack input)
					{
						return BlockColoredDirt.BlockType.values()[input.getItemDamage()].getName();
					}
				}));
		registerItemBlock(registry, new ItemBlock(glowingMushroom));
		registerItemBlock(registry, new ItemBlock(glowingLog));
		registerItemBlock(registry, new ItemBlockCraftingTab(glowingPlanks, CreativeTabs.BUILDING_BLOCKS));
		registerItemBlock(registry, new ItemMultiTexture(stone, stone, new ItemMultiTexture.Mapper()
		{
			@Override
			public String apply(ItemStack input)
			{
				return BlockMinestuckStone.BlockType.getFromMeta(input.getMetadata()).getUnlocalizedName();
			}
		}));
		registerItemBlock(registry, new ItemBlockCraftingTab(coarseStoneStairs, CreativeTabs.BUILDING_BLOCKS));
		registerItemBlock(registry, new ItemBlockCraftingTab(shadeBrickStairs, CreativeTabs.BUILDING_BLOCKS));
		registerItemBlock(registry, new ItemBlockCraftingTab(frostBrickStairs, CreativeTabs.BUILDING_BLOCKS));
		registerItemBlock(registry, new ItemBlockCraftingTab(castIronStairs, CreativeTabs.BUILDING_BLOCKS));
		registerItemBlock(registry, new ItemMultiTexture(log, log, new ItemMultiTexture.Mapper()
		{
			@Override
			public String apply(ItemStack input)
			{
				return BlockMinestuckLog.BlockType.values()[input.getItemDamage()].getUnlocalizedName();
			}
		}));
		registerItemBlock(registry, new ItemBlock(woodenCactus));
		
		registerItemBlock(registry, new ItemBlock(primedTnt));
		registerItemBlock(registry, new ItemBlock(unstableTnt));
		registerItemBlock(registry, new ItemBlock(instantTnt));
		registerItemBlock(registry, new ItemBlock(woodenExplosiveButton));
		registerItemBlock(registry, new ItemBlock(stoneExplosiveButton));
		
		//hammers
		registry.register(clawHammer);
		registry.register(sledgeHammer);
		registry.register(blacksmithHammer);
		registry.register(pogoHammer);
		registry.register(telescopicSassacrusher);
		registry.register(fearNoAnvil);
		registry.register(zillyhooHammer);
		registry.register(popamaticVrillyhoo);
		registry.register(scarletZillyhoo);
		registry.register(mwrthwl);
		
		//blades
		registry.register(sord);
		registry.register(cactusCutlass);
		registry.register(katana);
		registry.register(unbreakableKatana);
		registry.register(firePoker);
		registry.register(hotHandle);
		registry.register(caledscratch);
		registry.register(caledfwlch);
		registry.register(royalDeringer);
		registry.register(zillywairCutlass);
		registry.register(regisword);
		registry.register(scarletRibbitar);
		registry.register(doggMachete);
		registry.register(cobaltSabre);
		
		//axes
		registry.register(copseCrusher);
		registry.register(blacksmithBane);
		registry.register(scraxe);
		registry.register(qPHammerAxe);
		registry.register(rubyCroak);
		registry.register(hephaestusLumber);
		
		//Dice
		registry.register(dice);
		registry.register(fluoriteOctet);
		//misc weapons
		registry.register(catClaws);
		//sickles
		registry.register(sickle);
		registry.register(homesSmellYaLater);
		registry.register(fudgeSickle);
		registry.register(regiSickle);
		registry.register(clawSickle);
		registry.register(candySickle);
		
		//clubs
		registry.register(deuceClub);
		registry.register(nightClub);
		registry.register(pogoClub);
		registry.register(metalBat);
		registry.register(spikedClub);

		//canes
		registry.register(cane);
		registry.register(spearCane);
		registry.register(dragonCane);
		
		//Spoons/forks
		registry.register(woodenSpoon);
		registry.register(silverSpoon);
		registry.register(crockerSpork);
		registry.register(skaiaFork);
		registry.register(fork);
		registry.register(spork);
		
		registry.register(emeraldSword);
		registry.register(emeraldAxe);
		registry.register(emeraldPickaxe);
		registry.register(emeraldShovel);
		registry.register(emeraldHoe);
		
		//armor
		registry.register(prismarineHelmet);
		registry.register(prismarineChestplate);
		registry.register(prismarineLeggings);
		registry.register(prismarineBoots);
		
		//food
		registry.register(candy);
		registry.register(beverage);
		registry.register(bugOnAStick);
		registry.register(chocolateBeetle);
		registry.register(coneOfFlies);
		registry.register(grasshopper);
		registry.register(jarOfBugs);
		registry.register(onion);
		registry.register(salad);
		
		//misc
		registry.register(rawCruxite);
		registry.register(cruxiteDowel);
		registry.register(captchaCard);
		registry.register(cruxiteApple);
		registry.register(cruxitePotion);
		registry.register(disk);
		registry.register(chessboard);
		registry.register(minestuckBucket);
		registry.register(obsidianBucket);
		registry.register(modusCard);
		registry.register(goldSeeds);
		registry.register(metalBoat);
		registry.register(threshDvd);
		registry.register(crewPoster);
		registry.register(sbahjPoster);
		registry.register(carvingTool);
		registry.register(crumplyHat);
		registry.register(frogStatueReplica);
		registry.register(stoneSlab);
		registry.register(glowystoneDust);
		
		//Music disks
		registry.register(recordEmissaryOfDance);
		registry.register(recordDanceStab);
		
		minestuckBucket.addBlock(blockOil.getDefaultState());
		minestuckBucket.addBlock(blockBlood.getDefaultState());
		minestuckBucket.addBlock(blockBrainJuice.getDefaultState());
		/*for(Block block : liquidGrists)
		{
			minestuckBucket.addBlock(block.getDefaultState());
		}*/
	}
	
	private static Item registerItemBlock(IForgeRegistry<Item> registry, ItemBlock item)
	{
		registry.register(item.setRegistryName(item.getBlock().getRegistryName()));
		return item;
	}
}

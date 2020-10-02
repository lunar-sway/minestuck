package com.mraof.minestuck.entity.consort;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.mraof.minestuck.entity.consort.EnumConsort.MerchantType;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.skaianet.SburbHandler;
import com.mraof.minestuck.world.MSDimensions;
import com.mraof.minestuck.world.lands.LandTypePair;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import com.mraof.minestuck.world.storage.loot.MSLootTables;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import static com.mraof.minestuck.entity.consort.MessageType.*;
import static com.mraof.minestuck.world.lands.LandTypes.*;
import static com.mraof.minestuck.world.storage.loot.MSLootTables.CONSORT_FOOD_STOCK;
import static com.mraof.minestuck.world.storage.loot.MSLootTables.CONSORT_GENERAL_STOCK;

/**
 * Handles message registry, message selection and contains the main message
 * class, which combines conditioning and a MessageType
 * 
 * @author Kirderf1
 */
public class ConsortDialogue
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private static final List<DialogueWrapper> messages = new LinkedList<>();
	
	/**
	 * Make sure to call after land registry
	 */
	public static void init()	//TODO Could likely be exported to a json format
	{
		// Wind
		addMessage("dad_wind").landTitle(WIND);
		addMessage(new ChainMessage(new SingleMessage("pyre.1"), new SingleMessage("pyre.2"))).landTitle(WIND).consort(EnumConsort.SALAMANDER, EnumConsort.TURTLE);
		
		// Pulse
		addMessage("koolaid").landTitle(PULSE).consort(EnumConsort.SALAMANDER, EnumConsort.TURTLE);
		addMessage("murder_rain").landTitle(PULSE);
		addMessage("swimming").landTitle(PULSE).consort(EnumConsort.IGUANA, EnumConsort.TURTLE);
		addMessage("blood_surprise").landTitle(PULSE).consort(EnumConsort.IGUANA, EnumConsort.NAKAGATOR);
		
		// Thunder
		addMessage("skeleton_horse").landTitle(THUNDER);
		addMessage("blue_moon").landTitle(THUNDER).consort(EnumConsort.SALAMANDER, EnumConsort.IGUANA);
		addMessage("lightning_strike").landTitle(THUNDER).consort(EnumConsort.TURTLE);
		addMessage(new ChainMessage(new SingleMessage("reckoning.1"), new SingleMessage("reckoning.2"), new SingleMessage("reckoning.3", "consort_type"))).landTitle(THUNDER);
		addMessage(new ChainMessage(new SingleMessage("thunder_death.1"), new SingleMessage("thunder_death.2"))).landTitle(THUNDER).landTerrain(WOOD);
		addMessage("hardcore").landTitle(THUNDER).landTerrain(HEAT);
		addMessage(new ChainMessage(new SingleMessage("thunder_throw.1"), new SingleMessage("thunder_throw.2"))).landTitle(THUNDER).consort(EnumConsort.TURTLE, EnumConsort.SALAMANDER);
		
		//Rabbits
		addMessage("bunny_birthday").landTitle(RABBITS).consort(EnumConsort.NAKAGATOR, EnumConsort.SALAMANDER);
		addMessage("rabbit_eating").landTitle(RABBITS).consort(EnumConsort.TURTLE, EnumConsort.SALAMANDER);
		addMessage("edgy_life_hatred").landTitle(RABBITS).consort(EnumConsort.IGUANA, EnumConsort.NAKAGATOR);
		addMessage("rabbit.food_shortage.1").landTitle(RABBITS).landTerrain(SAND, SANDSTONE);
		addMessage(new ChainMessage(0, "rabbit.foodShortage.2", new SingleMessage("rabbit.food_shortage.1"), new SingleMessage("rabbit.food_shortage.2"))).landTitle(RABBITS).landTerrain(ROCK);
		addMessage(new ChainMessage(0, "rabbit.food.1", new SingleMessage("rabbit.food.1"), new SingleMessage("rabbit.food.2a"))).landTitle(RABBITS).landTerrain(ROCK, SANDSTONE);
		addMessage(new ChainMessage(0, "rabbit.food.2", new SingleMessage("rabbit.food.1"), new SingleMessage("rabbit.food.2a"), new SingleMessage("rabbit.food.3a"))).landTitle(RABBITS).landTerrain(SAND);
		addMessage(new ChainMessage(0, "rabbit.food.3", new SingleMessage("rabbit.food.1"), new SingleMessage("rabbit.food.2b"))).landTitle(RABBITS).landTerrain(WOOD, SHADE);
		
		//Monsters
		addMessage(new SingleMessage("pet_zombie")).landTitle(MONSTERS).consort(EnumConsort.NAKAGATOR, EnumConsort.SALAMANDER);
		addMessage("spider_raid").landTitleSpecific(MONSTERS);
		addMessage("monstersona").landTitle(MONSTERS).consort(EnumConsort.IGUANA, EnumConsort.NAKAGATOR);
		
		//Towers
		addMessage("bug_treasure").landTitle(TOWERS).consort(EnumConsort.SALAMANDER, EnumConsort.IGUANA);
		addMessage("tower_gone").landTitle(TOWERS).consort(EnumConsort.TURTLE, EnumConsort.SALAMANDER);
		addMessage("no_tower_treasure").landTitle(TOWERS).consort(EnumConsort.IGUANA, EnumConsort.NAKAGATOR);
		
		//Thought
		addMessage("glass_books").landTitle(THOUGHT).consort(EnumConsort.TURTLE, EnumConsort.IGUANA);
		addMessage("book_food").landTitle(THOUGHT).consort(EnumConsort.SALAMANDER, EnumConsort.NAKAGATOR);
		addMessage("to_eat").landTitle(THOUGHT).consort(EnumConsort.IGUANA, EnumConsort.NAKAGATOR);
		
		//Cake
		addMessage("mystery_recipe").landTitle(CAKE).consort(EnumConsort.TURTLE, EnumConsort.NAKAGATOR);
		addMessage("cake_regen").landTitle(CAKE).consort(EnumConsort.TURTLE, EnumConsort.SALAMANDER);
		addMessage("cake_recipe").landTitle(CAKE).consort(EnumConsort.IGUANA, EnumConsort.SALAMANDER);
		addMessage("fire_cakes").landTerrain(HEAT).landTitle(CAKE);
		addMessage("frosting").landTitle(CAKE).landTerrain(FROST);
		
		//Clockwork
		addMessage("gear_technology").landTitle(CLOCKWORK).consort(EnumConsort.SALAMANDER, EnumConsort.IGUANA);
		addMessage("evil_gears").landTitle(CLOCKWORK).consort(EnumConsort.NAKAGATOR, EnumConsort.IGUANA);
		addMessage("ticking").landTitle(CLOCKWORK).consort(EnumConsort.TURTLE, EnumConsort.SALAMANDER);
		
		//Frogs
		addMessage("frog_creation").landTitle(FROGS);
		addMessage("frog_imitation").landTitle(FROGS);
		addMessage(new ChainMessage(new SingleMessage("frog_variants.1"), new SingleMessage("frog_variants.2", "land_name"))).landTitle(FROGS);
		addMessage("frog_hatred").landTitle(FROGS);
		addMessage(new ChainMessage(new SingleMessage("grasshopper_fishing.1"), new SingleMessage("grasshopper_fishing.2"))).landTitle(FROGS).consort(EnumConsort.SALAMANDER, EnumConsort.IGUANA);
		addMessage("gay_frogs").landTitle(FROGS).landTerrainSpecific(RAINBOW);
		
		//Buckets
		addMessage("lewd_buckets").landTitle(BUCKETS);
		addMessage("water_buckets").landTitle(BUCKETS).landTerrain(SAND);
		addMessage("warm_buckets").landTitle(BUCKETS).landTerrain(FROST);
		addMessage(new ChainMessage(new SingleMessage("oil_buckets.1"), new SingleMessage("oil_buckets.2"))).landTitle(BUCKETS).landTerrain(SHADE);
		
		//Light
		addMessage("blindness").landTitle(LIGHT);
		addMessage("doctors_inside").landTitle(LIGHT).consort(EnumConsort.TURTLE);
		addMessage("staring").landTitle(LIGHT);
		addMessage(new ChainMessage(new SingleMessage("sunglasses.1"), new SingleMessage("sunglasses.2"))).landTitle(LIGHT).landTerrain(HEAT);
		addMessage(new ChainMessage(new SingleMessage("bright_snow.1"), new SingleMessage("bright_snow.2"))).landTitle(LIGHT).landTerrain(FROST);
		addMessage("glimmering_snow").landTitle(LIGHT).landTerrain(FROST);
		addMessage("glimmering_sand").landTitle(LIGHT).landTerrain(SAND);
		addMessage("light_pillars").landTitle(LIGHT).consort(EnumConsort.IGUANA, EnumConsort.TURTLE);
		
		//Silence
		addMessage("murder_silence").landTitle(SILENCE).consort(EnumConsort.NAKAGATOR, EnumConsort.SALAMANDER);
		addMessage("silent_underlings").landTitle(SILENCE);
		addMessage(new ChainMessage(new SingleMessage("listening.1"), new SingleMessage("listening.2"))).landTitle(SILENCE).consort(EnumConsort.IGUANA, EnumConsort.SALAMANDER);
		addMessage("calmness").landTitle(SILENCE).consort(EnumConsort.TURTLE, EnumConsort.IGUANA);
		
		//Towers
		addMessage("climb_high").landTitle(TOWERS, WIND).consort(EnumConsort.IGUANA);
		addMessage(new ConditionedMessage((ConsortEntity consort, ServerPlayerEntity player) -> consort.posY < 78, new ChainMessage(new SingleMessage("height_fear.towers.1"), new SingleMessage("height_fear.towers.2")),
				new SingleMessage("height_fear.panic"))).landTitle(TOWERS).consort(EnumConsort.TURTLE);
		addMessage(new ConditionedMessage((ConsortEntity consort, ServerPlayerEntity player) -> consort.posY < 78, new ChainMessage(new SingleMessage("height_fear.rock.1"), new SingleMessage("height_fear.rock.2")),
				new SingleMessage("height_fear.panic"))).landTitle(WIND).consort(EnumConsort.TURTLE);
		
		//Shade
		addMessage(new ChainMessage(2, new SingleMessage("mush_farm.1"), new SingleMessage("mush_farm.2"), new SingleMessage("mush_farm.3"),
				new SingleMessage("mush_farm.4"), new SingleMessage("mush_farm.5"), new SingleMessage("mush_farm.6"),
				new SingleMessage("mush_farm.7"))).landTerrain(SHADE);
		addMessage(new ChoiceMessage(new SingleMessage("mushroom_pizza"),
				new SingleMessage[]{new SingleMessage("mushroom_pizza.on"), new SingleMessage("mushroom_pizza.off")},
				new MessageType[]{new SingleMessage("mushroom_pizza.on.consort_reply"), new SingleMessage("mushroom_pizza.off.consort_reply")})).landTerrain(SHADE);
		addMessage("fire_hazard").landTerrain(SHADE).landTitle(allExcept(THUNDER));
		
		//Heat
		addMessage("getting_hot").landTerrain(HEAT);
		addMessage("step_into_fire").landTerrain(HEAT);
		addMessage("lava_crickets").landTerrain(HEAT);
		
		//Wood
		addMessage("wood_treatments").landTerrain(WOOD);
		addMessage(new ChainMessage(new SingleMessage("splinters.1"), new SingleMessage("splinters.2"))).landTerrain(WOOD);
		
		//Sand & Sandstone
		addMessage("sand_surfing").landTerrain(SAND);
		addMessage(new ChoiceMessage(new SingleMessage("camel"), new SingleMessage[]{new SingleMessage("camel.yes"), new SingleMessage("camel.no")},
				new MessageType[]{new SingleMessage("camel.no_camel"), new SingleMessage("camel.dancing_camel")})).landTerrain(SAND);
		addMessage("knockoff").landTerrain(SANDSTONE);
		addMessage(new ChainMessage(new SingleMessage("sandless.1", "denizen"), new SingleMessage("sandless.2"))).landTerrain(SANDSTONE);
		addMessage("red_better").landTerrainSpecific(RED_SAND, RED_SANDSTONE);
		addMessage("yellow_better").landTerrainSpecific(SAND, SANDSTONE);
		
		//Frost
		addMessage(new ChainMessage(new SingleMessage("frozen.1"), new DescriptionMessage("frozen.2"))).landTerrain(FROST);
		addMessage(new ChoiceMessage(new SingleMessage("fur_coat"), new SingleMessage[]{new SingleMessage("fur_coat.pay"), new SingleMessage("fur_coat.ignore")},
				new MessageType[]{new PurchaseMessage(MSLootTables.CONSORT_JUNK_REWARD, 100, new ChainMessage(1, new SingleMessage("fur_coat.grattitude"), new SingleMessage("thank_you"))),
						new SingleMessage("fur_coat.death")})).landTerrain(FROST);
		addMessage("tent_protection").landTerrain(FROST).consortReq(ConsortEntity::detachHome);
		addMessage("all_ores").landTerrain(ROCK);
		addMessage("rockfu", "land_name").landTerrain(ROCK);
		addMessage("all_trees").landTerrain(FOREST);
		addMessage("really_likes_trees").landTerrain(FOREST);
		
		//Fungi
		addMessage(new ChainMessage(new SingleMessage("mycelium.1"), new SingleMessage("mycelium.2"))).landTerrain(FUNGI);
		addMessage(new ChainMessage(new SingleMessage("adaptation.1"), new SingleMessage("adaptation.2"))).landTerrain(FUNGI);
		addMessage("mushroom_curse", "denizen").landTerrain(FUNGI);
		addMessage("jacket").landTerrain(FUNGI);
		addMessage("mildew").landTerrain(FUNGI);
		addMessage("fungus_destroyer", "player_title_land", "denizen").landTerrain(FUNGI);
		
		//Rainbow Terrain
		addMessage("generic_green").landTerrain(RAINBOW);
		addMessage("overwhelming_colors").landTerrain(RAINBOW);
		addMessage("saw_rainbow").landTerrain(RAINBOW);
		addMessage("sunglasses").landTerrain(RAINBOW);
		addMessage("what_is_wool").landTerrain(RAINBOW);
		addMessage("love_colors").landTerrain(RAINBOW);
		addMessage(new ChainMessage(new SingleMessage("types_of_colors.1"), new SingleMessage("types_of_colors.2"), new SingleMessage("types_of_colors.3"),
				new SingleMessage("types_of_colors.4"), new SingleMessage("types_of_colors.5"), new SingleMessage("types_of_colors.6"), new SingleMessage("types_of_colors.7"), new SingleMessage("types_of_colors.8"),
				new SingleMessage("types_of_colors.9"), new SingleMessage("types_of_colors.10"), new SingleMessage("types_of_colors.11"), new SingleMessage("types_of_colors.12"), new SingleMessage("types_of_colors.13"),
				new SingleMessage("types_of_colors.14"), new SingleMessage("types_of_colors.15"), new SingleMessage("types_of_colors.16"), new SingleMessage("types_of_colors.17"), new SingleMessage("types_of_colors.18")))
				.landTerrain(RAINBOW);
		
		//End Terrain
		addMessage("at_the_end").landTerrain(END);
		addMessage("chorus_fruit").landTerrain(END);
		addMessage("end_grass").landTerrain(END);
		addMessage("grass_curse", "denizen").landTerrain(END);
		addMessage("useless_pogo").landTerrain(END);
		addMessage("useless_elytra").landTerrain(END);
		
		//Rain terrain
		addMessage("empty_ocean", "denizen").landTerrain(RAIN);
		addMessage("forbidden_snack").landTerrain(RAIN);
		addMessage("cotton_candy").landTerrain(RAIN);
		addMessage("monsters_below").landTerrain(RAIN);
		addMessage("keep_swimming").landTerrain(RAIN);
		
		//Flora Terrain
		addMessage("battle_site").landTerrain(FLORA);
		addMessage("blood_oceans").landTerrain(FLORA);
		addMessage("giant_swords").landTerrain(FLORA);
		addMessage(new ChainMessage(new SingleMessage("bloodberries.1"), new SingleMessage("bloodberries.2"))).landTerrain(FLORA);
		addMessage("sharp_slide").landTerrain(FLORA);
		addMessage(new ChainMessage(new SingleMessage("immortality_herb.1"), new SingleMessage("immortality_herb.2"), new ExplosionMessage("immortality_herb.3"))).landTerrain(FLORA).lockToConsort();
		addMessage(new ChainMessage(new SingleMessage("spices.1"), new SingleMessage("spices.2", "land_name"))).landTerrain(FLORA);

		//Misc
		addMessage("denizen_mention").reqLand();
		addMessage("floating_island").consortReq(consort -> consort.getDistanceSq(new Vec3d(consort.world.getSpawnPoint())) < 65536).reqLand();
		addMessage("ring_fishing").consort(EnumConsort.SALAMANDER, EnumConsort.IGUANA);
		addMessage("frog_walk").consort(EnumConsort.TURTLE);
		addMessage("delicious_hair").consort(EnumConsort.IGUANA);
		addMessage("lazy_king").landTerrain(SHADE);
		addMessage("music_invention").consort(EnumConsort.NAKAGATOR, EnumConsort.SALAMANDER);
		addMessage("wyrm").consort(EnumConsort.TURTLE, EnumConsort.IGUANA);
		addMessage(new ConditionedMessage((consort, player) -> SburbHandler.hasEntered(player),
				new SingleMessage("heroic_stench"), new SingleMessage("leech_stench"))).reqLand();
		
		MessageType raps = new RandomMessage("rap_battles", RandomKeepResult.KEEP_CONSORT,
				new DelayMessage(new int[] {17, 17, 30},
					new SingleMessage("rap_battle.a1"), new SingleMessage("rap_battle.a2"),
					new SingleMessage("rap_battle.a3"), new SingleMessage("rap_battle.a4")
				), new DelayMessage(new int[] {25},
					new SingleMessage("rap_battle.b1"),new SingleMessage("rap_battle.b2"),
					new SingleMessage("rap_battle.b3"),new SingleMessage("rap_battle.b4")
				), new DelayMessage(new int[] {17},
					new SingleMessage("rap_battle.c1"),new SingleMessage("rap_battle.c2"),
					new SingleMessage("rap_battle.c3", "consort_sound"), new SingleMessage("rap_battle.c4")
				), new DelayMessage(new int[] {25, 20, 30},
					new SingleMessage("rap_battle.d1"),new SingleMessage("rap_battle.d2"),
					new SingleMessage("rap_battle.d3"),new SingleMessage("rap_battle.d4")
				), new DelayMessage(new int[] {17, 20, 30},
					new SingleMessage("rap_battle.e1"),new SingleMessage("rap_battle.e2"),
					new SingleMessage("rap_battle.e3"),new SingleMessage("rap_battle.e4")
				), new DelayMessage(new int[] {25},
					new SingleMessage("rap_battle.f1"),new SingleMessage("rap_battle.f2"),
					new SingleMessage("rap_battle.f3"),new SingleMessage("rap_battle.f4")));
		addMessage(new ChoiceMessage(false, new SingleMessage("rap_battle"),
				new SingleMessage[]
				{
					new SingleMessage("rap_battle.accept"),
					new SingleMessage("rap_battle.deny")
				},
				new MessageType[] {
					//If you accepted the challenge
					new ChoiceMessage(false,
							new DescriptionMessage(raps, "rap_battle.raps_desc"),
							new SingleMessage[] {
									new SingleMessage("rap_battle_school"),
									new SingleMessage("rap_battle_concede")
							},
							new MessageType[] {
									new DoubleMessage(new DescriptiveMessage("rap_battle_school.rap", "player_title", "land_name"),
											new SingleMessage("rap_battle_school.final", "consort_sound")).setSayFirstOnce(),
									new SingleMessage("rap_battle_concede.final", "consort_sound")
							}
					),
					//If you didn't accept the challenge
					new SingleMessage("rap_battle.deny_answer")
				}
			).setAcceptNull()
		).consort(EnumConsort.NAKAGATOR, EnumConsort.IGUANA);
		
		addMessage("await_hero", "land_name", "consort_types", "player_title_land").reqLand();
		addMessage(new ConditionedMessage("skaia", (ConsortEntity consort, ServerPlayerEntity player) -> !consort.visitedSkaia, new SingleMessage("watch_skaia"),
				new ConditionedMessage((ConsortEntity consort, ServerPlayerEntity player) -> MSDimensions.isSkaia(consort.dimension),
						new SingleMessage("at_skaia.1", "consort_sound_2"), new SingleMessage("visited_skaia")))).consort(EnumConsort.SALAMANDER, EnumConsort.IGUANA, EnumConsort.NAKAGATOR).reqLand();
		addMessage(new ConditionedMessage("skaia_turtle", (ConsortEntity consort, ServerPlayerEntity player) -> !consort.visitedSkaia, new SingleMessage("watch_skaia"),
				new ConditionedMessage((ConsortEntity consort, ServerPlayerEntity player) -> MSDimensions.isSkaia(consort.dimension),
						new SingleMessage("at_skaia.2"), new SingleMessage("visited_skaia")))).consort(EnumConsort.TURTLE).reqLand();
		
		addMessage(new SingleMessage("zazzerpan")).consort(EnumConsort.TURTLE);
		addMessage(new SingleMessage("texas_history", "land_name")).consort(EnumConsort.TURTLE);
		addMessage(new SingleMessage("disks")).consort(EnumConsort.IGUANA);
		addMessage(new SingleMessage("whoops")).consort(EnumConsort.IGUANA);
		addMessage(new SingleMessage("fourth_wall")).consort(EnumConsort.IGUANA);
		addMessage(new SingleMessage("hats")).consort(EnumConsort.SALAMANDER);
		addMessage(new SingleMessage("wwizard")).consort(EnumConsort.TURTLE);
		addMessage(new SingleMessage("stock_market")).consort(EnumConsort.NAKAGATOR);
		addMessage(new SingleMessage("identity", "player_title_land", "player_name_land")).consort(EnumConsort.SALAMANDER).reqLand();
		addMessage(new ChainMessage(0, new SingleMessage("college.1"), new SingleMessage("college.2")));
		addMessage(new ChainMessage(1, new SingleMessage("unknown.1"), new SingleMessage("unknown.2"))).consort(EnumConsort.TURTLE);
		addMessage(new ChainMessage(1, new SingleMessage("cult.1", "player_title"), new SingleMessage("cult.2"))).consort(EnumConsort.TURTLE, EnumConsort.SALAMANDER);

		addMessage(new ChoiceMessage(new DescriptionMessage("peppy_offer"),
				new SingleMessage[] { new SingleMessage("peppy_offer.buy"), new SingleMessage("peppy_offer.deny") },
				new MessageType[] {
						new PurchaseMessage(false, MSLootTables.CONSORT_JUNK_REWARD, 1000, "purchase",
								new ChainMessage(1, new SingleMessage("peppy_offer.item"), new SingleMessage("peppy_offer.purchase"))),
						new ChoiceMessage(new SingleMessage("peppy_offer.next"),
								new SingleMessage[] { new SingleMessage("peppy_offer.deny_again"), new SingleMessage("peppy_offer.buy_2") },
								new MessageType[] { new SingleMessage("dots"),
										new PurchaseMessage(false, MSLootTables.CONSORT_JUNK_REWARD, 500, "purchase",
												new SingleMessage("peppy_offer.purchase")) }) })).type(MerchantType.SHADY).consort(EnumConsort.NAKAGATOR, EnumConsort.IGUANA);


		addMessage(new ChoiceMessage(true, new SingleMessage("title_presence", "player_title"),
				new SingleMessage[] { new SingleMessage("title_presence.iam", "player_title"), new SingleMessage("title_presence.agree") },
				new MessageType[] { new SingleMessage("title_presence.iam_answer", "consort_sound_2"), new SingleMessage("thanks") })).consort(EnumConsort.IGUANA, EnumConsort.SALAMANDER).reqLand();
		
		addMessage(new ChoiceMessage(new DescriptionMessage("shady_offer"),
				new SingleMessage[]
						{
								new SingleMessage("shady_offer.buy"),
								new SingleMessage("shady_offer.deny")
						},
				new MessageType[] {
						new PurchaseMessage(false, MSLootTables.CONSORT_JUNK_REWARD, 1000, "purchase",
								new ChainMessage(1,
										new SingleMessage("shady_offer.item"),
										new SingleMessage("shady_offer.purchase")
								)
						),
						new ChoiceMessage(new SingleMessage("shady_offer.next", "consort_sound"),
								new SingleMessage[]
								{
										new SingleMessage("shady_offer.deny_again"),
										new SingleMessage("shady_offer.buy_2")
								},
								new MessageType[]
								{
										new SingleMessage("dots"),
										new PurchaseMessage(false, MSLootTables.CONSORT_JUNK_REWARD, 500, "purchase",
												new SingleMessage("shady_offer.purchase")
										)
								}
						)
				}
		)).type(MerchantType.SHADY).consort(EnumConsort.SALAMANDER, EnumConsort.TURTLE);
		
		addMessage(new ChoiceMessage(true, new SingleMessage("denizen", "denizen"),
				new SingleMessage[] { new SingleMessage("denizen.what"), new SingleMessage("denizen.ask_alignment") },
				new MessageType[] { new SingleMessage("denizen.explain", "player_class_land"), new SingleMessage("denizen.alignment") })).consort(EnumConsort.SALAMANDER, EnumConsort.IGUANA, EnumConsort.TURTLE).reqLand();
		
		List<ItemStack> hungryList = ImmutableList.of(new ItemStack(Items.COOKIE), new ItemStack(MSItems.BUG_ON_A_STICK),
				new ItemStack(MSItems.GRASSHOPPER), new ItemStack(MSItems.CHOCOLATE_BEETLE),	//TODO Use item tags for these kind of things
				new ItemStack(MSItems.CONE_OF_FLIES));
		addMessage(new ItemRequirement(hungryList, false, true, new SingleMessage("hungry"),
						new ChoiceMessage(new SingleMessage("hungry.ask_food", "nbt_item:hungry.item"),
								new SingleMessage[] { new SingleMessage("hungry.accept"), new SingleMessage("hungry.deny") },
								new MessageType[] { new GiveItemMessage("hungry.item", 0, new SingleMessage("hungry.thanks")),
										new SingleMessage("sadface") }))).consort(EnumConsort.SALAMANDER, EnumConsort.IGUANA);
		addMessage(new ItemRequirement("hungry2", hungryList, false, true, false,
						new SingleMessage(
								"hungry"),
						new ChoiceMessage(
								new SingleMessage("hungry.ask_food",
										"nbt_item:hungry2.item"),
								new SingleMessage[] {
										new SingleMessage(
												"hungry.accept"),
										new SingleMessage("hungry.deny") },
								new MessageType[] { new GiveItemMessage("hungry2.item", 0, new SingleMessage("hungry.thanks")),
										new ChoiceMessage(new SingleMessage("hungry.starving"),
												new SingleMessage[] { new SingleMessage("hungry.agree"),
														new SingleMessage("hungry.too_cheap") },
												new MessageType[] { new GiveItemMessage("hungry.sell_item", "hungry2.item", 10,
														new ChainMessage(1, new DescriptionMessage("hungry.finally", "nbt_item:hungry2.item"),
																new SingleMessage("hungry.finally"))),
														new SingleMessage("hungry.end") }) }))).consort(EnumConsort.SALAMANDER, EnumConsort.NAKAGATOR);
		
		addMessage(new MerchantGuiMessage(new SingleMessage("food_shop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).consort(EnumConsort.SALAMANDER);
		addMessage(new MerchantGuiMessage(new SingleMessage("fast_food"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).consort(EnumConsort.NAKAGATOR);
		addMessage(new MerchantGuiMessage(new SingleMessage("grocery_store"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).consort(EnumConsort.IGUANA);
		addMessage(new MerchantGuiMessage(new SingleMessage("tasty_welcome"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).consort(EnumConsort.TURTLE);
		addMessage(new MerchantGuiMessage(new SingleMessage("breath_food_shop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(WIND);
		addMessage(new MerchantGuiMessage(new SingleMessage("blood_food_shop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(PULSE);
		addMessage(new MerchantGuiMessage(new SingleMessage("life_food_shop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(RABBITS);
		addMessage(new MerchantGuiMessage(new SingleMessage("doom_food_shop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(THUNDER);
		//addMessage(new MerchantGuiMessage(new SingleMessage("frog_food_shop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(FROGS);
		addMessage(new MerchantGuiMessage(new SingleMessage("space_food_shop", "land_name"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(FROGS);
		addMessage(new MerchantGuiMessage(new SingleMessage("time_food_shop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(CLOCKWORK);
		addMessage(new MerchantGuiMessage(new SingleMessage("thyme_food_shop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(CLOCKWORK, THOUGHT);
		addMessage(new MerchantGuiMessage(new SingleMessage("mind_food_shop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(THOUGHT);
		addMessage(new MerchantGuiMessage(new SingleMessage("heart_food_shop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(CAKE);
		addMessage(new MerchantGuiMessage(new SingleMessage("light_food_shop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(LIGHT);
		addMessage(new MerchantGuiMessage(new SingleMessage("void_food_shop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(SILENCE);
		addMessage(new MerchantGuiMessage(new SingleMessage("rage_food_shop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(MONSTERS);
		addMessage(new MerchantGuiMessage(new DescriptionMessage("hope_food_shop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(TOWERS);
		addMessage(new MerchantGuiMessage(new SingleMessage("buckets_food_shop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(BUCKETS);
		
		addMessage(new MerchantGuiMessage(new SingleMessage("general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL);
		addMessage(new MerchantGuiMessage(new SingleMessage("got_the_goods"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL);
		addMessage(new MerchantGuiMessage(new SingleMessage("rising_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL);
		addMessage(new MerchantGuiMessage(new SingleMessage("breath_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).landTitle(WIND);
		addMessage(new MerchantGuiMessage(new SingleMessage("blood_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).landTitle(PULSE);
		addMessage(new MerchantGuiMessage(new SingleMessage("life_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).landTitle(RABBITS);
		addMessage(new MerchantGuiMessage(new SingleMessage("doom_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).landTitle(THUNDER);
		//addMessage(new MerchantGuiMessage(new SingleMessage("frog_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).landTitle(FROGS);
		addMessage(new MerchantGuiMessage(new SingleMessage("space_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).landTitle(FROGS);
		addMessage(new MerchantGuiMessage(new SingleMessage("time_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).landTitle(CLOCKWORK);
		addMessage(new MerchantGuiMessage(new SingleMessage("mind_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).landTitle(THOUGHT);
		addMessage(new MerchantGuiMessage(new SingleMessage("heart_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).landTitle(CAKE);
		addMessage(new MerchantGuiMessage(new SingleMessage("light_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).landTitle(LIGHT);
		addMessage(new MerchantGuiMessage(new SingleMessage("void_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).landTitle(SILENCE);
		addMessage(new MerchantGuiMessage(new SingleMessage("rage_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).landTitle(MONSTERS);
		addMessage(new MerchantGuiMessage(new SingleMessage("hope_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).landTitle(TOWERS);
		addMessage(new MerchantGuiMessage(new SingleMessage("buckets_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).landTitle(BUCKETS);
		
		addMessage(new MerchantGuiMessage(new SingleMessage("boring_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).landTerrain(RAINBOW);
	}
	
	public static DialogueWrapper addMessage(String message, String... args)
	{
		return addMessage(new SingleMessage(message, args));
	}
	
	public static DialogueWrapper addMessage(MessageType message)
	{
		return addMessage(10, message);
	}
	
	public static DialogueWrapper addMessage(int weight, MessageType message)
	{
		DialogueWrapper msg = new DialogueWrapper(weight);
		msg.messageStart = message;
		messages.add(msg);
		return msg;
	}
	
	public static TitleLandType[] allExceptSpecific(TitleLandType... aspects)
	{
		Set<TitleLandType> set = new HashSet<>(TITLE_REGISTRY.getValues());
		for(TitleLandType exceptedAspect : aspects)
			set.remove(exceptedAspect);
		
		return set.toArray(new TitleLandType[0]);
	}
	
	public static TitleLandType[] allExcept(TitleLandType... aspects)
	{
		Set<TitleLandType> set = new HashSet<>(TITLE_REGISTRY.getValues());
		for(TitleLandType exceptedAspect : aspects)
			set.removeIf(aspect -> aspect.getGroup().equals(exceptedAspect.getGroup()));
		
		return set.toArray(new TitleLandType[0]);
	}
	
	public static DialogueWrapper getRandomMessage(ConsortEntity consort, ServerPlayerEntity player, boolean hasHadMessage)
	{
		LandTypePair aspects = MSDimensions.getAspects(player.getServer(), consort.homeDimension);
		
		List<DialogueWrapper> list = new ArrayList<>();
		
		for(DialogueWrapper message : messages)
		{
			if(message.lockToConsort && hasHadMessage)
				continue;
			if(message.reqLand && aspects == null)
				continue;
			if(message.consortRequirement != null && !message.consortRequirement.contains(consort.getConsortType()))
				continue;
			if(message.aspect1Requirement != null && aspects != null && !message.aspect1Requirement.contains(aspects.terrain.getGroup()))
				continue;
			if(message.aspect2Requirement != null && aspects != null && !message.aspect2Requirement.contains(aspects.title.getGroup()))
				continue;
			if(message.aspect1RequirementS != null && aspects != null && !message.aspect1RequirementS.contains(aspects.terrain))
				continue;
			if(message.aspect2RequirementS != null && aspects != null && !message.aspect2RequirementS.contains(aspects.title))
				continue;
			if(message.merchantRequirement == null && consort.merchantType != EnumConsort.MerchantType.NONE
					|| message.merchantRequirement != null && !message.merchantRequirement.contains(consort.merchantType))
				continue;
			if(message.additionalRequirement != null && !message.additionalRequirement.apply(consort))
				continue;
			list.add(message);
		}
		
		return WeightedRandom.getRandomItem(consort.world.rand, list);
	}
	
	public static DialogueWrapper getMessageFromString(String name)
	{
		for(DialogueWrapper message : messages)
			if(message.getString().equals(name))
				return message;
		return null;
	}
	
	public static class DialogueWrapper extends WeightedRandom.Item
	{
		private DialogueWrapper(int weight)
		{
			super(weight);
		}
		
		private boolean reqLand;
		private boolean lockToConsort;
		
		private MessageType messageStart;
		
		private Set<ResourceLocation> aspect1Requirement;
		private Set<ResourceLocation> aspect2Requirement;
		private Set<TerrainLandType> aspect1RequirementS;
		private Set<TitleLandType> aspect2RequirementS;
		private EnumSet<EnumConsort> consortRequirement;
		private EnumSet<MerchantType> merchantRequirement;
		private ConsortRequirement additionalRequirement;
		
		public DialogueWrapper reqLand()
		{
			reqLand = true;
			return this;
		}
		
		public DialogueWrapper lockToConsort()
		{
			lockToConsort = true;
			return this;
		}
		
		public boolean isLockedToConsort()
		{
			return lockToConsort;
		}
		
		public DialogueWrapper landTerrain(TerrainLandType... aspects)
		{
			if(isAnyNull(aspects))
				LOGGER.warn("Land aspect is null for consort message {}, this is probably not intended", messageStart.getString());
			reqLand = true;
			aspect1Requirement = Sets.newHashSet();
			for(TerrainLandType aspect : aspects)
			{
				if(aspect != null)
					aspect1Requirement.add(aspect.getGroup());
			}
			
			return this;
		}
		
		public DialogueWrapper landTerrainSpecific(TerrainLandType... aspects)
		{
			
			if(isAnyNull(aspects))
				LOGGER.warn("Land aspect is null for consort message {}, this is probably not intended", messageStart.getString());
			reqLand = true;
			aspect1RequirementS = Sets.newHashSet(aspects);
			return this;
		}
		
		public DialogueWrapper landTitle(TitleLandType... aspects)
		{
			if(isAnyNull(aspects))
				LOGGER.warn("Land aspect is null for consort message {}, this is probably not intended", messageStart.getString());
			reqLand = true;
			aspect2Requirement = Sets.newHashSet();
			for(TitleLandType aspect : aspects)
				if(aspect != null)
					aspect2Requirement.add(aspect.getGroup());
			return this;
		}
		
		public DialogueWrapper landTitleSpecific(TitleLandType... aspects)
		{
			if(isAnyNull(aspects))
				LOGGER.warn("Land aspect is null for consort message {}, this is probably not intended", messageStart.getString());
			reqLand = true;
			aspect2RequirementS = Sets.newHashSet(aspects);
			return this;
		}
		
		public DialogueWrapper consort(EnumConsort... types)
		{
			consortRequirement = EnumSet.of(types[0], types);
			return this;
		}
		
		public DialogueWrapper type(MerchantType... types)
		{
			merchantRequirement = EnumSet.of(types[0], types);
			return this;
		}
		
		public DialogueWrapper consortReq(ConsortRequirement req)
		{
			additionalRequirement = req;
			return this;
		}
		
		public ITextComponent getMessage(ConsortEntity consort, ServerPlayerEntity player)
		{
			return messageStart.getMessage(consort, player, "");
		}
		
		public ITextComponent getFromChain(ConsortEntity consort, ServerPlayerEntity player, String fromChain)
		{
			return messageStart.getFromChain(consort, player, "", fromChain);
		}
		
		public String getString()
		{
			return messageStart.getString();
		}
		
	}
	
	private static boolean isAnyNull(Object[] objects)
	{
		for(Object obj : objects)
		{
			if(obj == null)
				return true;
		}
		return false;
	}
	
	public interface ConsortRequirement
	{
		boolean apply(ConsortEntity consort);
	}
	
	public static void serverStarting()
	{
		//debugPrintAll();
	}
	
	private static void debugPrintAll()
	{
		List<ITextComponent> list = new ArrayList<>();
		for(DialogueWrapper wrapper : messages)
		{
			wrapper.messageStart.debugAddAllMessages(list);
		}
		
		for(ITextComponent textComponent : list)
			LOGGER.info(textComponent.getFormattedText());
	}
}
package com.mraof.minestuck.entity.consort;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.mraof.minestuck.entity.consort.EnumConsort.MerchantType;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.network.skaianet.SburbHandler;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.LandAspects;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;
import com.mraof.minestuck.world.lands.title.TitleLandAspect;
import com.mraof.minestuck.world.storage.loot.MinestuckLoot;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;

import java.util.*;

import static com.mraof.minestuck.entity.consort.MessageType.*;
import static com.mraof.minestuck.world.storage.loot.MinestuckLoot.CONSORT_FOOD_STOCK;
import static com.mraof.minestuck.world.storage.loot.MinestuckLoot.CONSORT_GENERAL_STOCK;
import static com.mraof.minestuck.world.lands.LandAspectRegistry.fromNameTerrain;
import static com.mraof.minestuck.world.lands.LandAspectRegistry.fromNameTitle;

/**
 * Handles message registry, message selection and contains the main message
 * class, which combines conditioning and a MessageType
 * 
 * @author Kirderf1
 */
public class ConsortDialogue
{
	
	private static final List<DialogueWrapper> messages = new LinkedList<>();
	
	/**
	 * Make sure to call after land registry
	 */
	public static void init()
	{
		// Wind
		addMessage("dad_wind").landTitle(fromNameTitle("wind"));
		addMessage(new ChainMessage(new SingleMessage("pyre1"), new SingleMessage("pyre2"))).landTitle(fromNameTitle("wind")).consort(EnumConsort.SALAMANDER, EnumConsort.TURTLE);
		addMessage("koolaid").landTitle(fromNameTitle("pulse")).consort(EnumConsort.SALAMANDER, EnumConsort.TURTLE);
		addMessage("murder_rain").landTitle(fromNameTitle("pulse"));
		addMessage("swimming").landTitle(fromNameTitle("pulse")).consort(EnumConsort.IGUANA, EnumConsort.TURTLE);
		addMessage("blood_surprise").landTitle(fromNameTitle("pulse")).consort(EnumConsort.IGUANA, EnumConsort.NAKAGATOR);
		
		// Thunder
		addMessage("skeleton_horse").landTitle(fromNameTitle("thunder"));
		addMessage("blue_moon").landTitle(fromNameTitle("thunder")).consort(EnumConsort.SALAMANDER, EnumConsort.IGUANA);
		addMessage("lightning_strike").landTitle(fromNameTitle("thunder")).consort(EnumConsort.TURTLE);
		addMessage(new ChainMessage(new SingleMessage("reckoning1"), new SingleMessage("reckoning2"), new SingleMessage("reckoning3", "consort_type"))).landTitle(fromNameTitle("thunder"));
		addMessage(new ChainMessage(new SingleMessage("thunder_death.1"), new SingleMessage("thunder_death.2"))).landTitle(fromNameTitle("thunder")).landTerrain(fromNameTerrain("woods"));
		addMessage("hardcore").landTitle(fromNameTitle("thunder")).landTerrain(fromNameTerrain("heat"));
		addMessage(new ChainMessage(new SingleMessage("thunder_throw.1"), new SingleMessage("thunder_throw.2"))).landTitle(fromNameTitle("thunder")).consort(EnumConsort.TURTLE, EnumConsort.SALAMANDER);
		
		//Rabbits
		addMessage("bunny_birthday").landTitle(fromNameTitle("rabbits")).consort(EnumConsort.NAKAGATOR, EnumConsort.SALAMANDER);
		addMessage("rabbit_eating").landTitle(fromNameTitle("rabbits")).consort(EnumConsort.TURTLE, EnumConsort.SALAMANDER);
		addMessage("edgyLifeHatred").landTitle(fromNameTitle("rabbits")).consort(EnumConsort.IGUANA, EnumConsort.NAKAGATOR);
		addMessage("rabbit.food_shortage.1").landTitle(fromNameTitle("rabbits")).landTerrain(fromNameTerrain("sand"), fromNameTerrain("sandstone"));
		addMessage(new ChainMessage(0, "rabbit.foodShortage.2", new SingleMessage("rabbit.food_shortage.1"), new SingleMessage("rabbit.food_shortage.2"))).landTitle(fromNameTitle("rabbits")).landTerrain(fromNameTerrain("rock"));
		addMessage(new ChainMessage(0, "rabbit.food.1", new SingleMessage("rabbit.food.1"), new SingleMessage("rabbit.food.2a"))).landTitle(fromNameTitle("rabbits")).landTerrain(fromNameTerrain("rock"), fromNameTerrain("sandstone"));
		addMessage(new ChainMessage(0, "rabbit.food.2", new SingleMessage("rabbit.food.1"), new SingleMessage("rabbit.food.2a"), new SingleMessage("rabbit.food.3a"))).landTitle(fromNameTitle("rabbits")).landTerrain(fromNameTerrain("sand"));
		addMessage(new ChainMessage(0, "rabbit.food.3", new SingleMessage("rabbit.food.1"), new SingleMessage("rabbit.food.2b"))).landTitle(fromNameTitle("rabbits")).landTerrain(fromNameTerrain("woods"), fromNameTerrain("shade"));
		
		//Monsters
		addMessage(new SingleMessage("pet_zombie")).landTitle(fromNameTitle("monsters")).consort(EnumConsort.NAKAGATOR, EnumConsort.SALAMANDER);
		addMessage("spider_raid").landTitleSpecific(fromNameTitle("monsters"));
		addMessage("monstersona").landTitle(fromNameTitle("monsters")).consort(EnumConsort.IGUANA, EnumConsort.NAKAGATOR);
		
		//Towers
		addMessage("bug_treasure").landTitleSpecific(fromNameTitle("towers")).consort(EnumConsort.SALAMANDER, EnumConsort.IGUANA);
		addMessage("tower_gone").landTitleSpecific(fromNameTitle("towers")).consort(EnumConsort.TURTLE, EnumConsort.SALAMANDER);
		addMessage("no_tower_treasure").landTitle(fromNameTitle("towers")).consort(EnumConsort.IGUANA, EnumConsort.NAKAGATOR);
		
		//Thought
		addMessage("glass_books").landTitleSpecific(fromNameTitle("thought")).consort(EnumConsort.TURTLE, EnumConsort.IGUANA);
		addMessage("book_food").landTitleSpecific(fromNameTitle("thought")).consort(EnumConsort.SALAMANDER, EnumConsort.NAKAGATOR);
		addMessage("to_eat").landTitle(fromNameTitle("thought")).consort(EnumConsort.IGUANA, EnumConsort.NAKAGATOR);
		
		//Cake
		addMessage("mystery_recipe").landTitleSpecific(fromNameTitle("cake")).consort(EnumConsort.TURTLE, EnumConsort.NAKAGATOR);
		addMessage("cake_regen").landTitleSpecific(fromNameTitle("cake")).consort(EnumConsort.TURTLE, EnumConsort.SALAMANDER);
		addMessage("cake_recipe").landTitleSpecific(fromNameTitle("cake")).consort(EnumConsort.IGUANA, EnumConsort.SALAMANDER);
		addMessage("frosting").landTitleSpecific(fromNameTitle("cake")).landTerrain(fromNameTerrain("frost"));
		
		//Clockwork
		addMessage("gear_technology").landTitleSpecific(fromNameTitle("clockwork")).consort(EnumConsort.SALAMANDER, EnumConsort.IGUANA);
		addMessage("evil_gears").landTitleSpecific(fromNameTitle("clockwork")).consort(EnumConsort.NAKAGATOR, EnumConsort.IGUANA);
		addMessage("ticking").landTitleSpecific(fromNameTitle("clockwork")).consort(EnumConsort.TURTLE, EnumConsort.SALAMANDER);
		
		//Frogs
		addMessage("frog_creation").landTitleSpecific(fromNameTitle("frogs"));
		addMessage("frog_imitation").landTitleSpecific(fromNameTitle("frogs"));
		addMessage(new ChainMessage(new SingleMessage("frog_variants1"), new SingleMessage("frog_variants2", "land_name"))).landTitle(fromNameTitle("frogs")).landTitleSpecific(LandAspectRegistry.frogAspect);
		addMessage("frog_hatred").landTitleSpecific(fromNameTitle("frogs"));
		addMessage(new ChainMessage(new SingleMessage("grasshopper_fishing1"), new SingleMessage("grasshopper_fishing2"))).landTitleSpecific(fromNameTitle("frogs")).consort(EnumConsort.SALAMANDER, EnumConsort.IGUANA);
		addMessage("gay_frogs").landTitleSpecific(fromNameTitle("frogs")).landTerrainSpecific(fromNameTerrain("rainbow"));
		
		//Buckets
		addMessage("lewd_buckets").landTitleSpecific(fromNameTitle("buckets"));
		addMessage("water_buckets").landTitleSpecific(fromNameTitle("buckets")).landTerrain(fromNameTerrain("sand"));
		addMessage("warm_buckets").landTitleSpecific(fromNameTitle("buckets")).landTerrain(fromNameTerrain("frost"));
		addMessage(new ChainMessage(new SingleMessage("oil_buckets.1"), new SingleMessage("oil_buckets.2"))).landTitleSpecific(fromNameTitle("buckets")).landTerrain(fromNameTerrain("shade"));
		
		//Light
		addMessage("blindness").landTitleSpecific(fromNameTitle("light"));
		addMessage("doctors_inside").landTitleSpecific(fromNameTitle("light")).consort(EnumConsort.TURTLE);
		addMessage("staring").landTitleSpecific(fromNameTitle("light"));
		addMessage(new ChainMessage(new SingleMessage("sunglasses.1"), new SingleMessage("sunglasses.2"))).landTitle(fromNameTitle("light")).landTerrain(fromNameTerrain("heat"));
		addMessage(new ChainMessage(new SingleMessage("bright_snow.1"), new SingleMessage("bright_snow.2"))).landTitle(fromNameTitle("light")).landTerrain(fromNameTerrain("frost"));
		addMessage("glimmering_snow").landTitleSpecific(fromNameTitle("light")).landTerrain(fromNameTerrain("frost"));
		addMessage("glimmering_sand").landTitleSpecific(fromNameTitle("light")).landTerrain(fromNameTerrain("sand"));
		addMessage("light_pillars").landTitleSpecific(fromNameTitle("light")).consort(EnumConsort.IGUANA, EnumConsort.TURTLE);
		
		//Silence
		addMessage("murder_silence").landTitleSpecific(fromNameTitle("silence")).consort(EnumConsort.NAKAGATOR, EnumConsort.SALAMANDER);
		addMessage("silent_underlings").landTitleSpecific(fromNameTitle("silence"));
		addMessage(new ChainMessage(new SingleMessage("listening.1"), new SingleMessage("listening.2"))).landTitle(fromNameTitle("silence")).consort(EnumConsort.IGUANA, EnumConsort.SALAMANDER);
		addMessage("calmness").landTitleSpecific(fromNameTitle("silence")).consort(EnumConsort.TURTLE, EnumConsort.IGUANA);
		
		//Towers
		addMessage("climb_high").landTitleSpecific(fromNameTitle("towers"), fromNameTitle("wind")).consort(EnumConsort.IGUANA);
		addMessage(new ConditionedMessage((ConsortEntity consort, ServerPlayerEntity player) -> consort.posY < 78, new ChainMessage(new SingleMessage("height_fear.towers.1"), new SingleMessage("height_fear.towers.2")),
				new SingleMessage("height_fear.panic"))).landTitle(fromNameTitle("towers")).consort(EnumConsort.TURTLE);
		addMessage(new ConditionedMessage((ConsortEntity consort, ServerPlayerEntity player) -> consort.posY < 78, new ChainMessage(new SingleMessage("height_fear.rock.1"), new SingleMessage("height_fear.rock.2")),
				new SingleMessage("height_fear.panic"))).landTitle(fromNameTitle("wind")).consort(EnumConsort.TURTLE);
		
		//Shade
		addMessage(new ChainMessage(2, new SingleMessage("mush_farm.1"), new SingleMessage("mush_farm.2"), new SingleMessage("mush_farm.3"),
				new SingleMessage("mush_farm.4"), new SingleMessage("mush_farm.5"), new SingleMessage("mush_farm.6"),
				new SingleMessage("mush_farm.7"))).landTerrain(fromNameTerrain("shade"));
		addMessage(new ChoiceMessage(new SingleMessage("mushroom_pizza"),
				new SingleMessage[]{new SingleMessage("mushroom_pizza.on"), new SingleMessage("mushroom_pizza.off")},
				new MessageType[]{new SingleMessage("mushroom_pizza.on.consort_reply"), new SingleMessage("mushroom_pizza.off.consort_reply")})).landTerrain(fromNameTerrain("shade"));
		addMessage("fire_hazard").landTerrain(fromNameTerrain("shade")).landTitle(allExcept(fromNameTitle("thunder")));
		addMessage("getting_hot").landTerrain(fromNameTerrain("heat"));
		addMessage("step_into_fire").landTerrain(fromNameTerrain("heat"));
		addMessage("lava_crickets").landTerrain(fromNameTerrain("heat"));
		
		//Wood
		addMessage("wood_treatments").landTerrain(fromNameTerrain("woods"));
		addMessage(new ChainMessage(new SingleMessage("splinters.1"), new SingleMessage("splinters.2"))).landTerrain(fromNameTerrain("woods"));
		
		//Sand & Sandstone
		addMessage("sand_surfing").landTerrain(fromNameTerrain("sand"));
		addMessage(new ChoiceMessage(new SingleMessage("camel"), new SingleMessage[]{new SingleMessage("camel.yes"), new SingleMessage("camel.no")},
				new MessageType[]{new SingleMessage("camel.no_camel"), new SingleMessage("camel.dancing_camel")})).landTerrain(fromNameTerrain("sand"));
		addMessage("knockoff").landTerrain(fromNameTerrain("sandstone"));
		addMessage(new ChainMessage(new SingleMessage("sandless.1", "denizen"), new SingleMessage("sandless.2"))).landTerrain(fromNameTerrain("sandstone"));
		addMessage("red_better").landTerrainSpecific(fromNameTerrain("sand_red"), fromNameTerrain("sandstone_red"));
		addMessage("yellow_better").landTerrainSpecific(fromNameTerrain("sand"), fromNameTerrain("sandstone"));
		
		//Frost
		addMessage(new ChainMessage(new SingleMessage("frozen1"), new DescriptionMessage("frozen2"))).landTerrain(fromNameTerrain("frost"));
		addMessage(new ChoiceMessage(new SingleMessage("fur_coat"), new SingleMessage[]{new SingleMessage("fur_coat.pay"), new SingleMessage("fur_coat.ignore")},
				new MessageType[]{new PurchaseMessage(MinestuckLoot.CONSORT_JUNK_REWARD, 100, new ChainMessage(1, new SingleMessage("fur_coat.grattitude"), new SingleMessage("thank_you"))),
						new SingleMessage("fur_coat.death")})).landTerrain(fromNameTerrain("frost"));
		addMessage("tent_protection").landTerrain(fromNameTerrain("frost")).consortReq(ConsortEntity::detachHome);
		addMessage("all_ores").landTerrain(fromNameTerrain("rock"));
		addMessage("rockfu", "landName").landTerrain(fromNameTerrain("rock"));
		addMessage("all_trees").landTerrain(fromNameTerrain("forest"));
		addMessage("really_likes_trees").landTerrain(fromNameTerrain("forest"));
		
		//Fungi
		addMessage(new ChainMessage(new SingleMessage("mycelium1"), new SingleMessage("mycelium2"))).landTerrain(fromNameTerrain("fungi"));
		addMessage(new ChainMessage(new SingleMessage("adaptation1"), new SingleMessage("adaptation2"))).landTerrain(fromNameTerrain("fungi"));
		addMessage("mushroom_curse", "denizen").landTerrain(fromNameTerrain("fungi"));
		addMessage("jacket").landTerrain(fromNameTerrain("fungi"));
		addMessage("mildew").landTerrain(fromNameTerrain("fungi"));
		addMessage("fungus_destroyer", "playerTitle", "denizen").landTerrain(fromNameTerrain("fungi"));
		
		//Rainbow Terrain
		addMessage("generic_green").landTerrain(fromNameTerrain("rainbow"));
		addMessage("overwhelming_colors").landTerrain(fromNameTerrain("rainbow"));
		addMessage("saw_rainbow").landTerrain(fromNameTerrain("rainbow"));
		addMessage("sunglasses").landTerrain(fromNameTerrain("rainbow"));
		addMessage("what_is_wool").landTerrain(fromNameTerrain("rainbow"));
		addMessage("love_colors").landTerrain(fromNameTerrain("rainbow"));
		addMessage(new ChainMessage(new SingleMessage("types_of_colors.1"), new SingleMessage("types_of_colors.2"), new SingleMessage("types_of_colors.3"),
				new SingleMessage("types_of_colors.4"), new SingleMessage("types_of_colors.5"), new SingleMessage("types_of_colors.6"), new SingleMessage("types_of_colors.7"), new SingleMessage("types_of_colors.8"),
				new SingleMessage("types_of_colors.9"), new SingleMessage("types_of_colors.10"), new SingleMessage("types_of_colors.11"), new SingleMessage("types_of_colors.12"), new SingleMessage("types_of_colors.13"),
				new SingleMessage("types_of_colors.14"), new SingleMessage("types_of_colors.15"), new SingleMessage("types_of_colors.16"), new SingleMessage("types_of_colors.17"), new SingleMessage("types_of_colors.18")))
				.landTerrain(fromNameTerrain("rainbow"));
		
		//End Terrain
		addMessage("at_the_end").landTerrain(fromNameTerrain("end"));
		addMessage("chorus_fruit").landTerrain(fromNameTerrain("end"));
		addMessage("end_grass").landTerrain(fromNameTerrain("end"));
		addMessage("grass_curse", "denizen").landTerrain(fromNameTerrain("end"));
		addMessage("useless_pogo").landTerrain(fromNameTerrain("end"));
		addMessage("useless_elytra").landTerrain(fromNameTerrain("end"));
		
		//Rain terrain
		addMessage("empty_ocean", "denizen").landTerrain(fromNameTerrain("rain"));
		addMessage("forbidden_snack").landTerrain(fromNameTerrain("rain"));
		addMessage("cotton_candy").landTerrain(fromNameTerrain("rain"));
		addMessage("monsters_below").landTerrain(fromNameTerrain("rain"));
		addMessage("keep_swimming").landTerrain(fromNameTerrain("rain"));
		
		//Flora Terrain
		addMessage("battle_site").landTerrain(fromNameTerrain("flora"));
		addMessage("blood_oceans").landTerrain(fromNameTerrain("flora"));
		addMessage("giant_swords").landTerrain(fromNameTerrain("flora"));
		addMessage(new ChainMessage(new SingleMessage("bloodberries.1"), new SingleMessage("bloodberries.2"))).landTerrain(fromNameTerrain("flora"));
		addMessage("sharp_slide").landTerrain(fromNameTerrain("flora"));
		addMessage(new ChainMessage(new SingleMessage("immortality_herb.1"), new SingleMessage("immortality_herb.2"), new SingleMessage("immortality_herb.3"))).landTerrain(fromNameTerrain("flora"));
		addMessage(new ChainMessage(new SingleMessage("spices.1"), new SingleMessage("spices.2", "land_name"))).landTerrain(fromNameTerrain("flora"));

		//Misc
		addMessage("denizen_mention").reqLand();
		addMessage("floating_island").consortReq(consort -> consort.getDistanceSq(new Vec3d(consort.world.getSpawnPoint())) < 65536).reqLand();
		addMessage("ring_fishing").consort(EnumConsort.SALAMANDER, EnumConsort.IGUANA);
		addMessage("frog_walk").consort(EnumConsort.TURTLE);
		addMessage("delicious_hair").consort(EnumConsort.IGUANA);
		//		addMessage("village"); Did not work as intended
		addMessage("lazy_king").landTerrain(fromNameTerrain("shade"));
		addMessage("music_invention").consort(EnumConsort.NAKAGATOR, EnumConsort.SALAMANDER);
		addMessage("wyrm").consort(EnumConsort.TURTLE, EnumConsort.IGUANA);
		addMessage(new ConditionedMessage((ConsortEntity consort, ServerPlayerEntity player) -> SburbHandler.hasEntered((ServerPlayerEntity) player),
				new SingleMessage("heroic_stench"), new SingleMessage("leech_stench"))).reqLand();
		addMessage(new SingleMessage("fire_cakes")).landTerrain(fromNameTerrain("heat")).landTitle(fromNameTitle("cake"));
		
		MessageType raps = new RandomMessage("rap_battles", RandomKeepResult.KEEP_CONSORT,
				new DelayMessage(new int[] {17, 17, 30},
					new SingleMessage("rap_battle.A1"), new SingleMessage("rap_battle.A2"),
					new SingleMessage("rap_battle.A3"), new SingleMessage("rap_battle.A4")
				), new DelayMessage(new int[] {25},
					new SingleMessage("rap_battle.B1"),new SingleMessage("rap_battle.B2"),
					new SingleMessage("rap_battle.B3"),new SingleMessage("rap_battle.B4")
				), new DelayMessage(new int[] {17},
					new SingleMessage("rap_battle.C1"),new SingleMessage("rap_battle.C2"),
					new SingleMessage("rap_battle.C3", "consort_sound"), new SingleMessage("rap_battle.C4")
				), new DelayMessage(new int[] {25, 20, 30},
					new SingleMessage("rap_battle.D1"),new SingleMessage("rap_battle.D2"),
					new SingleMessage("rap_battle.D3"),new SingleMessage("rap_battle.D4")
				), new DelayMessage(new int[] {17, 20, 30},
					new SingleMessage("rap_battle.E1"),new SingleMessage("rap_battle.E2"),
					new SingleMessage("rap_battle.E3"),new SingleMessage("rap_battle.E4")
				), new DelayMessage(new int[] {25},
					new SingleMessage("rap_battle.F1"),new SingleMessage("rap_battle.F2"),
					new SingleMessage("rap_battle.F3"),new SingleMessage("rap_battle.F4")));
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
				new ConditionedMessage((ConsortEntity consort, ServerPlayerEntity player) -> MinestuckDimensionHandler.isSkaia(consort.dimension),
						new SingleMessage("at_skaia.1", "consort_sound2"), new SingleMessage("visited_skaia")))).consort(EnumConsort.SALAMANDER, EnumConsort.IGUANA, EnumConsort.NAKAGATOR).reqLand();
		addMessage(new ConditionedMessage("skaia_turtle", (ConsortEntity consort, ServerPlayerEntity player) -> !consort.visitedSkaia, new SingleMessage("watch_skaia"),
				new ConditionedMessage((ConsortEntity consort, ServerPlayerEntity player) -> MinestuckDimensionHandler.isSkaia(consort.dimension),
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
		addMessage(new ChainMessage(1, new SingleMessage("cult.1", "playerTitle"), new SingleMessage("cult.2"))).consort(EnumConsort.TURTLE, EnumConsort.SALAMANDER);

		addMessage(new ChoiceMessage(new DescriptionMessage("peppy_offer"),
				new SingleMessage[] { new SingleMessage("peppy_offer.buy"), new SingleMessage("peppy_offer.deny") },
				new MessageType[] {
						new PurchaseMessage(false, MinestuckLoot.CONSORT_JUNK_REWARD, 1000, "purchase",
								new ChainMessage(1, new SingleMessage("peppy_offer.item"), new SingleMessage("peppy_offer.purchase"))),
						new ChoiceMessage(new SingleMessage("peppy_offer.next"),
								new SingleMessage[] { new SingleMessage("peppy_offer.deny_again"), new SingleMessage("peppy_offer.buy2") },
								new MessageType[] { new SingleMessage("dots"),
										new PurchaseMessage(false, MinestuckLoot.CONSORT_JUNK_REWARD, 500, "purchase",
												new SingleMessage("peppy_offer.purchase")) }) })).type(MerchantType.SHADY).consort(EnumConsort.NAKAGATOR, EnumConsort.IGUANA);


		addMessage(new ChoiceMessage(true, new SingleMessage("title_presence", "player_title"),
				new SingleMessage[] { new SingleMessage("title_presence.iam", "player_title"), new SingleMessage("title_presence.agree") },
				new MessageType[] { new SingleMessage("title_presence.iam_answer", "consort_sound2"), new SingleMessage("thanks") })).consort(EnumConsort.IGUANA, EnumConsort.SALAMANDER).reqLand();
		
		addMessage(new ChoiceMessage(new DescriptionMessage("shady_offer"),
				new SingleMessage[]
						{
								new SingleMessage("shady_offer.buy"),
								new SingleMessage("shady_offer.deny")
						},
				new MessageType[] {
						new PurchaseMessage(false, MinestuckLoot.CONSORT_JUNK_REWARD, 1000, "purchase",
								new ChainMessage(1,
										new SingleMessage("shady_offer.item"),
										new SingleMessage("shady_offer.purchase")
								)
						),
						new ChoiceMessage(new SingleMessage("shady_offer.next", "consort_sound"),
								new SingleMessage[]
								{
										new SingleMessage("shady_offer.deny_again"),
										new SingleMessage("shady_offer.buy2")
								},
								new MessageType[]
								{
										new SingleMessage("dots"),
										new PurchaseMessage(false, MinestuckLoot.CONSORT_JUNK_REWARD, 500, "purchase",
												new SingleMessage("shady_offer.purchase")
										)
								}
						)
				}
		)).type(MerchantType.SHADY).consort(EnumConsort.SALAMANDER, EnumConsort.TURTLE);
		
		addMessage(new ChoiceMessage(true, new SingleMessage("denizen", "denizen"),
				new SingleMessage[] { new SingleMessage("denizen.what"), new SingleMessage("denizen.ask_alignment") },
				new MessageType[] { new SingleMessage("denizen.explain", "player_class_land"), new SingleMessage("denizen.alignment") })).consort(EnumConsort.SALAMANDER, EnumConsort.IGUANA, EnumConsort.TURTLE).reqLand();
		
		List<ItemStack> hungryList = ImmutableList.of(new ItemStack(Items.COOKIE), new ItemStack(MinestuckItems.BUG_ON_A_STICK),
				new ItemStack(MinestuckItems.GRASSHOPPER), new ItemStack(MinestuckItems.CHOCOLATE_BEETLE),	//TODO Use item tags for these kind of things
				new ItemStack(MinestuckItems.CONE_OF_FLIES));
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
														new ChainMessage(1, new DescriptionMessage("hungry.finally", "nbtItem:hungry2.item"),
																new SingleMessage("hungry.finally"))),
														new SingleMessage("hungry.end") }) }))).consort(EnumConsort.SALAMANDER, EnumConsort.NAKAGATOR);
		
		addMessage(new MerchantGuiMessage(new SingleMessage("food_shop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).consort(EnumConsort.SALAMANDER);
		addMessage(new MerchantGuiMessage(new SingleMessage("fast_food"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).consort(EnumConsort.NAKAGATOR);
		addMessage(new MerchantGuiMessage(new SingleMessage("grocery_store"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).consort(EnumConsort.IGUANA);
		addMessage(new MerchantGuiMessage(new SingleMessage("tasty_welcome"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).consort(EnumConsort.TURTLE);
		addMessage(new MerchantGuiMessage(new SingleMessage("breath_food_shop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(fromNameTitle("wind"));
		addMessage(new MerchantGuiMessage(new SingleMessage("blood_food_shop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(fromNameTitle("pulse"));
		addMessage(new MerchantGuiMessage(new SingleMessage("life_Food_shop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(fromNameTitle("rabbits"));
		addMessage(new MerchantGuiMessage(new SingleMessage("doom_food_shop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(fromNameTitle("thunder"));
		//addMessage(new MerchantGuiMessage(new SingleMessage("frogFoodShop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(LandAspectRegistry.frogAspect);
		addMessage(new MerchantGuiMessage(new SingleMessage("space_food_shop", "land_name"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(fromNameTitle("frogs"));
		addMessage(new MerchantGuiMessage(new SingleMessage("time_food_shop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(fromNameTitle("clockwork"));
		addMessage(new MerchantGuiMessage(new SingleMessage("thyme_food_shop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(fromNameTitle("clockwork"), fromNameTitle("thought"));
		addMessage(new MerchantGuiMessage(new SingleMessage("mind_food_shop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(fromNameTitle("thought"));
		addMessage(new MerchantGuiMessage(new SingleMessage("heart_food_shop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(fromNameTitle("cake"));
		addMessage(new MerchantGuiMessage(new SingleMessage("light_food_shop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(fromNameTitle("light"));
		addMessage(new MerchantGuiMessage(new SingleMessage("void_food_shop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(fromNameTitle("silence"));
		addMessage(new MerchantGuiMessage(new SingleMessage("rage_food_shop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(fromNameTitle("monsters"));
		addMessage(new MerchantGuiMessage(new DescriptionMessage("hope_food_shop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(fromNameTitle("towers"));
		addMessage(new MerchantGuiMessage(new SingleMessage("buckets_food_shop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(fromNameTitle("buckets"));
		
		addMessage(new MerchantGuiMessage(new SingleMessage("general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL);
		addMessage(new MerchantGuiMessage(new SingleMessage("got_the_goods"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL);
		addMessage(new MerchantGuiMessage(new SingleMessage("rising_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL);
		addMessage(new MerchantGuiMessage(new SingleMessage("breath_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).landTitle(fromNameTitle("wind"));
		addMessage(new MerchantGuiMessage(new SingleMessage("blood_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).landTitle(fromNameTitle("pulse"));
		addMessage(new MerchantGuiMessage(new SingleMessage("life_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).landTitle(fromNameTitle("rabbits"));
		addMessage(new MerchantGuiMessage(new SingleMessage("doom_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).landTitle(fromNameTitle("thunder"));
		//addMessage(new MerchantGuiMessage(new SingleMessage("frog_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).landTitle(LandAspectRegistry.frogAspect);
		addMessage(new MerchantGuiMessage(new SingleMessage("space_general_Shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).landTitle(fromNameTitle("frogs"));
		addMessage(new MerchantGuiMessage(new SingleMessage("time_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).landTitle(fromNameTitle("clockwork"));
		addMessage(new MerchantGuiMessage(new SingleMessage("mind_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).landTitle(fromNameTitle("thought"));
		addMessage(new MerchantGuiMessage(new SingleMessage("heart_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).landTitle(fromNameTitle("cake"));
		addMessage(new MerchantGuiMessage(new SingleMessage("light_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).landTitle(fromNameTitle("light"));
		addMessage(new MerchantGuiMessage(new SingleMessage("void_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).landTitle(fromNameTitle("silence"));
		addMessage(new MerchantGuiMessage(new SingleMessage("rage_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).landTitle(fromNameTitle("monsters"));
		addMessage(new MerchantGuiMessage(new DescriptionMessage("hope_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).landTitle(fromNameTitle("towers"));
		addMessage(new MerchantGuiMessage(new SingleMessage("buckets_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).landTitle(fromNameTitle("buckets"));
		
		addMessage(new MerchantGuiMessage(new SingleMessage("boring_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).landTerrain(fromNameTerrain("rainbow"));
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
	
	public static TitleLandAspect[] allExcept(TitleLandAspect... aspects)
	{
		Set<TitleLandAspect> set = new HashSet<>();
		names: for (String name : LandAspectRegistry.getNamesTitle())
		{
			for (TitleLandAspect aspect : aspects)
				if (aspect.getPrimaryName().equals(name))
					continue names;
			set.add(LandAspectRegistry.fromNameTitle(name));
		}
		return set.toArray(new TitleLandAspect[set.size()]);
	}
	
	public static DialogueWrapper getRandomMessage(ConsortEntity consort, ServerPlayerEntity player)
	{
		LandAspects aspects = MinestuckDimensionHandler.getAspects(player.getServer(), consort.homeDimension);
		
		List<DialogueWrapper> list = new ArrayList<>();
		
		for(DialogueWrapper message : messages)
		{
			if(message.reqLand && aspects == null)
				continue;
			if(message.consortRequirement != null && !message.consortRequirement.contains(consort.getConsortType()))
				continue;
			if(message.aspect1Requirement != null && !message.aspect1Requirement.contains(aspects.aspectTerrain.getPrimaryVariant()))
				continue;
			if(message.aspect2Requirement != null && !message.aspect2Requirement.contains(aspects.aspectTitle.getPrimaryVariant()))
				continue;
			if(message.aspect1RequirementS != null && !message.aspect1RequirementS.contains(aspects.aspectTerrain))
				continue;
			if(message.aspect2RequirementS != null && !message.aspect2RequirementS.contains(aspects.aspectTitle))
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
		
		private MessageType messageStart;
		
		private Set<TerrainLandAspect> aspect1Requirement;
		private Set<TitleLandAspect> aspect2Requirement;
		private Set<TerrainLandAspect> aspect1RequirementS;
		private Set<TitleLandAspect> aspect2RequirementS;
		private EnumSet<EnumConsort> consortRequirement;
		private EnumSet<MerchantType> merchantRequirement;
		private ConsortRequirement additionalRequirement;
		
		public DialogueWrapper reqLand()
		{
			reqLand = true;
			return this;
		}
		
		public DialogueWrapper landTerrain(TerrainLandAspect... aspects)
		{
			for(TerrainLandAspect aspect : aspects)
				if(aspect == null)
				{
					Debug.warn("Land aspect is null for consort message " + messageStart.getString() + ", this is probably not intended");
					break;
				}
			reqLand = true;
			aspect1Requirement = Sets.newHashSet(aspects);
			return this;
		}
		
		public DialogueWrapper landTerrainSpecific(TerrainLandAspect... aspects)
		{
			for(TerrainLandAspect aspect : aspects)
				if(aspect == null)
				{
					Debug.warn("Land aspect is null for consort message " + messageStart.getString() + ", this is probably not intended");
					break;
				}
			reqLand = true;
			aspect1RequirementS = Sets.newHashSet(aspects);
			return this;
		}
		
		public DialogueWrapper landTitle(TitleLandAspect... aspects)
		{
			for(TitleLandAspect aspect : aspects)
				if(aspect == null)
				{
					Debug.warn("Land aspect is null for consort message " + messageStart.getString() + ", this is probably not intended");
					break;
				}
			reqLand = true;
			aspect2Requirement = Sets.newHashSet(aspects);
			return this;
		}
		
		public DialogueWrapper landTitleSpecific(TitleLandAspect... aspects)
		{
			for(TitleLandAspect aspect : aspects)
				if(aspect == null)
				{
					Debug.warn("Land aspect is null for consort message " + messageStart.getString() + ", this is probably not intended");
					break;
				}
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
	
	public interface ConsortRequirement
	{
		boolean apply(ConsortEntity consort);
	}
}
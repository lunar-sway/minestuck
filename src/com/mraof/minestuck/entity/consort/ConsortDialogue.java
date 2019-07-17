package com.mraof.minestuck.entity.consort;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.mraof.minestuck.entity.consort.EnumConsort.MerchantType;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.network.skaianet.SburbHandler;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;
import com.mraof.minestuck.world.lands.title.TitleLandAspect;
import com.mraof.minestuck.world.storage.loot.MinestuckLoot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;
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
	
	private static final List<DialogueWrapper> messages = new LinkedList<DialogueWrapper>();
	
	/**
	 * Make sure to call after land registry
	 */
	public static void init()
	{
		// Wind
		addMessage("dadWind").landTitle(fromNameTitle("wind"));
		addMessage(new ChainMessage(new SingleMessage("pyre1"), new SingleMessage("pyre2"))).landTitle(fromNameTitle("wind")).consort(EnumConsort.SALAMANDER, EnumConsort.TURTLE);
		addMessage("koolaid").landTitle(fromNameTitle("pulse")).consort(EnumConsort.SALAMANDER, EnumConsort.TURTLE);
		addMessage("murderRain").landTitle(fromNameTitle("pulse"));
		addMessage("swimming").landTitle(fromNameTitle("pulse")).consort(EnumConsort.IGUANA, EnumConsort.TURTLE);
		addMessage("bloodSurprise").landTitle(fromNameTitle("pulse")).consort(EnumConsort.IGUANA, EnumConsort.NAKAGATOR);
		
		// Thunder
		addMessage("skeletonHorse").landTitle(fromNameTitle("thunder"));
		addMessage("blueMoon").landTitle(fromNameTitle("thunder")).consort(EnumConsort.SALAMANDER, EnumConsort.IGUANA);
		addMessage("lightningStrike").landTitle(fromNameTitle("thunder")).consort(EnumConsort.TURTLE);
		addMessage(new ChainMessage(new SingleMessage("reckoning1"), new SingleMessage("reckoning2"), new SingleMessage("reckoning3", "consortType"))).landTitle(fromNameTitle("thunder"));
		addMessage(new ChainMessage(new SingleMessage("thunderDeath.1"), new SingleMessage("thunderDeath.2"))).landTitle(fromNameTitle("thunder")).landTerrain(fromNameTerrain("wood"));
		addMessage("hardcore").landTitle(fromNameTitle("thunder")).landTerrain(fromNameTerrain("heat"));
		addMessage(new ChainMessage(new SingleMessage("thunderThrow.1"), new SingleMessage("thunderThrow.2"))).landTitle(fromNameTitle("thunder")).consort(EnumConsort.TURTLE, EnumConsort.SALAMANDER);
		
		//Rabbits
		addMessage("bunnyBirthday").landTitle(fromNameTitle("rabbits")).consort(EnumConsort.NAKAGATOR, EnumConsort.SALAMANDER);
		addMessage("rabbitEating").landTitle(fromNameTitle("rabbits")).consort(EnumConsort.TURTLE, EnumConsort.SALAMANDER);
		addMessage("edgyLifeHatred").landTitle(fromNameTitle("rabbits")).consort(EnumConsort.IGUANA, EnumConsort.NAKAGATOR);
		addMessage("rabbit.foodShortage.1").landTitle(fromNameTitle("rabbits")).landTerrain(fromNameTerrain("sand"), fromNameTerrain("sandstone"));
		addMessage(new ChainMessage(0, "rabbit.foodShortage.2", new SingleMessage("rabbit.foodShortage.1"), new SingleMessage("rabbit.foodShortage.2"))).landTitle(fromNameTitle("rabbits")).landTerrain(fromNameTerrain("rock"));
		addMessage(new ChainMessage(0, "rabbit.food.1", new SingleMessage("rabbit.food.1"), new SingleMessage("rabbit.food.2a"))).landTitle(fromNameTitle("rabbits")).landTerrain(fromNameTerrain("rock"), fromNameTerrain("sandstone"));
		addMessage(new ChainMessage(0, "rabbit.food.2", new SingleMessage("rabbit.food.1"), new SingleMessage("rabbit.food.2a"), new SingleMessage("rabbit.food.3a"))).landTitle(fromNameTitle("rabbits")).landTerrain(fromNameTerrain("sand"));
		addMessage(new ChainMessage(0, "rabbit.food.3", new SingleMessage("rabbit.food.1"), new SingleMessage("rabbit.food.2b"))).landTitle(fromNameTitle("rabbits")).landTerrain(fromNameTerrain("wood"), fromNameTerrain("shade"));
		
		//Monsters
		addMessage(new SingleMessage("petZombie")).landTitle(fromNameTitle("monsters")).consort(EnumConsort.NAKAGATOR, EnumConsort.SALAMANDER);
		addMessage("spiderRaid").landTitleSpecific(fromNameTitle("monsters"));
		addMessage("monstersona").landTitle(fromNameTitle("monsters")).consort(EnumConsort.IGUANA, EnumConsort.NAKAGATOR);
		
		//Towers
		addMessage("bugTreasure").landTitleSpecific(fromNameTitle("towers")).consort(EnumConsort.SALAMANDER, EnumConsort.IGUANA);
		addMessage("towerGone").landTitleSpecific(fromNameTitle("towers")).consort(EnumConsort.TURTLE, EnumConsort.SALAMANDER);
		addMessage("noTowerTreasure").landTitle(fromNameTitle("towers")).consort(EnumConsort.IGUANA, EnumConsort.NAKAGATOR);
		
		//Thought
		addMessage("glassBooks").landTitleSpecific(fromNameTitle("thought")).consort(EnumConsort.TURTLE, EnumConsort.IGUANA);
		addMessage("bookFood").landTitleSpecific(fromNameTitle("thought")).consort(EnumConsort.SALAMANDER, EnumConsort.NAKAGATOR);
		addMessage("toEat").landTitle(fromNameTitle("thought")).consort(EnumConsort.IGUANA, EnumConsort.NAKAGATOR);
		
		//Cake
		addMessage("mysteryRecipe").landTitleSpecific(fromNameTitle("cake")).consort(EnumConsort.TURTLE, EnumConsort.NAKAGATOR);
		addMessage("cakeRegen").landTitleSpecific(fromNameTitle("cake")).consort(EnumConsort.TURTLE, EnumConsort.SALAMANDER);
		addMessage("cakeRecipe").landTitleSpecific(fromNameTitle("cake")).consort(EnumConsort.IGUANA, EnumConsort.SALAMANDER);
		addMessage("frosting").landTitleSpecific(fromNameTitle("cake")).landTerrain(fromNameTerrain("frost"));
		
		//Clockwork
		addMessage("gearTechnology").landTitleSpecific(fromNameTitle("clockwork")).consort(EnumConsort.SALAMANDER, EnumConsort.IGUANA);
		addMessage("evilGears").landTitleSpecific(fromNameTitle("clockwork")).consort(EnumConsort.NAKAGATOR, EnumConsort.IGUANA);
		addMessage("ticking").landTitleSpecific(fromNameTitle("clockwork")).consort(EnumConsort.TURTLE, EnumConsort.SALAMANDER);
		
		//Frogs
		addMessage("frogCreation").landTitleSpecific(fromNameTitle("frogs"));
		addMessage("frogImitation").landTitleSpecific(fromNameTitle("frogs"));
		addMessage(new ChainMessage(new SingleMessage("frogVariants1"), new SingleMessage("frogVariants2", "landName"))).landTitle(fromNameTitle("frogs")).landTitleSpecific(LandAspectRegistry.frogAspect);
		addMessage("frogHatred").landTitleSpecific(fromNameTitle("frogs"));
		addMessage(new ChainMessage(new SingleMessage("grasshopperFishing1"), new SingleMessage("grasshopperFishing2"))).landTitleSpecific(fromNameTitle("frogs")).consort(EnumConsort.SALAMANDER, EnumConsort.IGUANA);
		addMessage("gayFrogs").landTitleSpecific(fromNameTitle("frogs")).landTerrainSpecific(fromNameTerrain("rainbow"));
		
		//Buckets
		addMessage("lewdBuckets").landTitleSpecific(fromNameTitle("buckets"));
		addMessage("waterBuckets").landTitleSpecific(fromNameTitle("buckets")).landTerrain(fromNameTerrain("sand"));
		addMessage("warmBuckets").landTitleSpecific(fromNameTitle("buckets")).landTerrain(fromNameTerrain("frost"));
		addMessage(new ChainMessage(new SingleMessage("oilBuckets.1"), new SingleMessage("oilBuckets.2"))).landTitleSpecific(fromNameTitle("buckets")).landTerrain(fromNameTerrain("shade"));
		
		//Light
		addMessage("blindness").landTitleSpecific(fromNameTitle("light"));
		addMessage("doctorsInside").landTitleSpecific(fromNameTitle("light")).consort(EnumConsort.TURTLE);
		addMessage("staring").landTitleSpecific(fromNameTitle("light"));
		addMessage(new ChainMessage(new SingleMessage("sunglasses.1"), new SingleMessage("sunglasses.2"))).landTitle(fromNameTitle("light")).landTerrain(fromNameTerrain("heat"));
		addMessage(new ChainMessage(new SingleMessage("brightSnow.1"), new SingleMessage("brightSnow.2"))).landTitle(fromNameTitle("light")).landTerrain(fromNameTerrain("frost"));
		addMessage("glimmeringSnow").landTitleSpecific(fromNameTitle("light")).landTerrain(fromNameTerrain("frost"));
		addMessage("glimmeringSand").landTitleSpecific(fromNameTitle("light")).landTerrain(fromNameTerrain("sand"));
		addMessage("lightPillars").landTitleSpecific(fromNameTitle("light")).consort(EnumConsort.IGUANA, EnumConsort.TURTLE);
		
		//Silence
		addMessage("murderSilence").landTitleSpecific(fromNameTitle("silence")).consort(EnumConsort.NAKAGATOR, EnumConsort.SALAMANDER);
		addMessage("silentUnderlings").landTitleSpecific(fromNameTitle("silence"));
		addMessage(new ChainMessage(new SingleMessage("listening.1"), new SingleMessage("listening.2"))).landTitle(fromNameTitle("silence")).consort(EnumConsort.IGUANA, EnumConsort.SALAMANDER);
		addMessage("calmness").landTitleSpecific(fromNameTitle("silence")).consort(EnumConsort.TURTLE, EnumConsort.IGUANA);
		
		//Towers
		addMessage("climbHigh").landTitleSpecific(fromNameTitle("towers"), fromNameTitle("wind")).consort(EnumConsort.IGUANA);
		addMessage(new ConditionedMessage((EntityConsort consort, EntityPlayer player) -> consort.posY < 78, new ChainMessage(new SingleMessage("heightFear.towers.1"), new SingleMessage("heightFear.towers.2")),
				new SingleMessage("heightFear.panic"))).landTitle(fromNameTitle("towers")).consort(EnumConsort.TURTLE);
		addMessage(new ConditionedMessage((EntityConsort consort, EntityPlayer player) -> consort.posY < 78, new ChainMessage(new SingleMessage("heightFear.rock.1"), new SingleMessage("heightFear.rock.2")),
				new SingleMessage("heightFear.panic"))).landTitle(fromNameTitle("wind")).consort(EnumConsort.TURTLE);
		
		//Shade
		addMessage(new ChainMessage(2, new SingleMessage("mushFarm.1"), new SingleMessage("mushFarm.2"), new SingleMessage("mushFarm.3"),
				new SingleMessage("mushFarm.4"), new SingleMessage("mushFarm.5"), new SingleMessage("mushFarm.6"),
				new SingleMessage("mushFarm.7"))).landTerrain(fromNameTerrain("shade"));
		addMessage(new ChoiceMessage(new SingleMessage("mushroomPizza"),
				new SingleMessage[]{new SingleMessage("mushroomPizza.on"), new SingleMessage("mushroomPizza.off")},
				new MessageType[]{new SingleMessage("mushroomPizza.on.consortReply"), new SingleMessage("mushroomPizza.off.consortReply")})).landTerrain(fromNameTerrain("shade"));
		addMessage("fireHazard").landTerrain(fromNameTerrain("shade")).landTitle(allExcept(fromNameTitle("thunder")));
		addMessage("gettingHot").landTerrain(fromNameTerrain("heat"));
		addMessage("stepIntoFire").landTerrain(fromNameTerrain("heat"));
		addMessage("lavaCrickets").landTerrain(fromNameTerrain("heat"));
		
		//Wood
		addMessage("woodTreatments").landTerrain(fromNameTerrain("wood"));
		addMessage(new ChainMessage(new SingleMessage("splinters.1"), new SingleMessage("splinters.2"))).landTerrain(fromNameTerrain("wood"));
		
		//Sand & Sandstone
		addMessage("sandSurfing").landTerrain(fromNameTerrain("sand"));
		addMessage(new ChoiceMessage(new SingleMessage("camel"), new SingleMessage[]{new SingleMessage("camel.yes"), new SingleMessage("camel.no")},
				new MessageType[]{new SingleMessage("camel.noCamel"), new SingleMessage("camel.dancingCamel")})).landTerrain(fromNameTerrain("sand"));
		addMessage("knockoff").landTerrain(fromNameTerrain("sandstone"));
		addMessage(new ChainMessage(new SingleMessage("sandless.1", "denizen"), new SingleMessage("sandless.2"))).landTerrain(fromNameTerrain("sandstone"));
		addMessage("redBetter").landTerrainSpecific(fromNameTerrain("sand_red"), fromNameTerrain("sandstone_red"));
		addMessage("yellowBetter").landTerrainSpecific(fromNameTerrain("sand"), fromNameTerrain("sandstone"));
		
		//Frost
		addMessage(new ChainMessage(new SingleMessage("frozen1"), new DescriptionMessage("frozen2"))).landTerrain(fromNameTerrain("frost"));
		addMessage(new ChoiceMessage(new SingleMessage("furCoat"), new SingleMessage[]{new SingleMessage("furCoat.pay"), new SingleMessage("furCoat.ignore")},
				new MessageType[]{new PurchaseMessage(MinestuckLoot.CONSORT_JUNK_REWARD, 100, new ChainMessage(1, new SingleMessage("furCoat.grattitude"), new SingleMessage("thankYou"))),
						new SingleMessage("furCoat.death")})).landTerrain(fromNameTerrain("frost"));
		addMessage("tentProtection").landTerrain(fromNameTerrain("frost")).consortReq((EntityConsort::hasHome));
		addMessage("allOres").landTerrain(fromNameTerrain("rock"));
		addMessage("rockfu", "landName").landTerrain(fromNameTerrain("rock"));
		addMessage("allTrees").landTerrain(fromNameTerrain("forest"));
		addMessage("reallyLikesTrees").landTerrain(fromNameTerrain("forest"));
		
		//Fungi
		addMessage(new ChainMessage(new SingleMessage("mycelium1"), new SingleMessage("mycelium2"))).landTerrain(fromNameTerrain("fungi"));
		addMessage(new ChainMessage(new SingleMessage("adaptation1"), new SingleMessage("adaptation2"))).landTerrain(fromNameTerrain("fungi"));
		addMessage("mushroomCurse", "denizen").landTerrain(fromNameTerrain("fungi"));
		addMessage("jacket").landTerrain(fromNameTerrain("fungi"));
		addMessage("mildew").landTerrain(fromNameTerrain("fungi"));
		addMessage("fungusDestroyer", "playerTitle", "denizen").landTerrain(fromNameTerrain("fungi"));
		
		//Rainbow Terrain
		addMessage("genericGreen").landTerrain(fromNameTerrain("rainbow"));
		addMessage("overwhelmingColors").landTerrain(fromNameTerrain("rainbow"));
		addMessage("sawRainbow").landTerrain(fromNameTerrain("rainbow"));
		addMessage("sunglasses").landTerrain(fromNameTerrain("rainbow"));
		addMessage("whatIsWool").landTerrain(fromNameTerrain("rainbow"));
		addMessage("loveColors").landTerrain(fromNameTerrain("rainbow"));
		addMessage(new ChainMessage(new SingleMessage("typesOfColors.1"), new SingleMessage("typesOfColors.2"), new SingleMessage("typesOfColors.3"),
				new SingleMessage("typesOfColors.4"), new SingleMessage("typesOfColors.5"), new SingleMessage("typesOfColors.6"), new SingleMessage("typesOfColors.7"), new SingleMessage("typesOfColors.8"),
				new SingleMessage("typesOfColors.9"), new SingleMessage("typesOfColors.10"), new SingleMessage("typesOfColors.11"), new SingleMessage("typesOfColors.12"), new SingleMessage("typesOfColors.13"),
				new SingleMessage("typesOfColors.14"), new SingleMessage("typesOfColors.15"), new SingleMessage("typesOfColors.16"), new SingleMessage("typesOfColors.17"), new SingleMessage("typesOfColors.18")))
				.landTerrain(fromNameTerrain("rainbow"));
		
		//End Terrain
		addMessage("atTheEnd").landTerrain(fromNameTerrain("end"));
		addMessage("chorusFruit").landTerrain(fromNameTerrain("end"));
		addMessage("endGrass").landTerrain(fromNameTerrain("end"));
		addMessage("grassCurse", "denizen").landTerrain(fromNameTerrain("end"));
		addMessage("uselessPogo").landTerrain(fromNameTerrain("end"));
		addMessage("uselessElytra").landTerrain(fromNameTerrain("end"));
		
		//Rain terrain
		addMessage("emptyOcean", "denizen").landTerrain(fromNameTerrain("rain"));
		addMessage("forbiddenSnack").landTerrain(fromNameTerrain("rain"));
		addMessage("cottonCandy").landTerrain(fromNameTerrain("rain"));
		addMessage("monstersBelow").landTerrain(fromNameTerrain("rain"));
		addMessage("keepSwimming").landTerrain(fromNameTerrain("rain"));
		
		//Flora Terrain
		addMessage("battleSite").landTerrain(fromNameTerrain("flora"));
		addMessage("bloodOceans").landTerrain(fromNameTerrain("flora"));
		addMessage("giantSwords").landTerrain(fromNameTerrain("flora"));
		addMessage(new ChainMessage(new SingleMessage("bloodberries.1"), new SingleMessage("bloodberries.2"))).landTerrain(fromNameTerrain("flora"));
		addMessage("sharpSlide").landTerrain(fromNameTerrain("flora"));
		addMessage(new ChainMessage(new SingleMessage("immortalityHerb.1"), new SingleMessage("immortalityHerb.2"), new SingleMessage("immortalityHerb.3"))).landTerrain(fromNameTerrain("flora"));
		addMessage(new ChainMessage(new SingleMessage("spices.1"), new SingleMessage("spices.2", "landName"))).landTerrain(fromNameTerrain("flora"));

		//Misc
		addMessage("denizenMention").reqLand();
		addMessage("floatingIsland").consortReq(consort -> consort.getDistanceSq(consort.world.getSpawnPoint()) < 65536).reqLand();
		addMessage("ringFishing").consort(EnumConsort.SALAMANDER, EnumConsort.IGUANA);
		addMessage("frogWalk").consort(EnumConsort.TURTLE);
		addMessage("deliciousHair").consort(EnumConsort.IGUANA);
		//		addMessage("village"); Did not work as intended
		addMessage("lazyKing").landTerrain(fromNameTerrain("shade"));
		addMessage("musicInvention").consort(EnumConsort.NAKAGATOR, EnumConsort.SALAMANDER);
		addMessage("wyrm").consort(EnumConsort.TURTLE, EnumConsort.IGUANA);
		addMessage(new ConditionedMessage((EntityConsort consort, EntityPlayer player) -> SburbHandler.hasEntered((EntityPlayerMP) player),
				new SingleMessage("heroicStench"), new SingleMessage("leechStench"))).reqLand();
		addMessage(new SingleMessage("fireCakes")).landTerrain(fromNameTerrain("heat")).landTitle(fromNameTitle("cake"));
		
		MessageType raps = new RandomMessage("rapBattles", RandomKeepResult.KEEP_CONSORT,
				new DelayMessage(new int[] {17, 17, 30},
					new SingleMessage("rapBattle.A1"), new SingleMessage("rapBattle.A2"),
					new SingleMessage("rapBattle.A3"), new SingleMessage("rapBattle.A4")
				), new DelayMessage(new int[] {25},
					new SingleMessage("rapBattle.B1"),new SingleMessage("rapBattle.B2"),
					new SingleMessage("rapBattle.B3"),new SingleMessage("rapBattle.B4")
				), new DelayMessage(new int[] {17},
					new SingleMessage("rapBattle.C1"),new SingleMessage("rapBattle.C2"),
					new SingleMessage("rapBattle.C3", "consortSound"), new SingleMessage("rapBattle.C4")
				), new DelayMessage(new int[] {25, 20, 30},
					new SingleMessage("rapBattle.D1"),new SingleMessage("rapBattle.D2"),
					new SingleMessage("rapBattle.D3"),new SingleMessage("rapBattle.D4")
				), new DelayMessage(new int[] {17, 20, 30},
					new SingleMessage("rapBattle.E1"),new SingleMessage("rapBattle.E2"),
					new SingleMessage("rapBattle.E3"),new SingleMessage("rapBattle.E4")
				), new DelayMessage(new int[] {25},
					new SingleMessage("rapBattle.F1"),new SingleMessage("rapBattle.F2"),
					new SingleMessage("rapBattle.F3"),new SingleMessage("rapBattle.F4")));
		addMessage(new ChoiceMessage(false, new SingleMessage("rapBattle"),
				new SingleMessage[]
				{
					new SingleMessage("rapBattle.accept"),
					new SingleMessage("rapBattle.deny")
				},
				new MessageType[] {
					//If you accepted the challenge
					new ChoiceMessage(false,
							new DescriptionMessage(raps, "rapBattle.rapsDesc"),
							new SingleMessage[] {
									new SingleMessage("rapBattleSchool"),
									new SingleMessage("rapBattleConcede")
							},
							new MessageType[] {
									new DoubleMessage(new DescriptiveMessage("rapBattleSchool.rap", "playerTitle", "landName"),
											new SingleMessage("rapBattleSchool.final", "consortSound")).setSayFirstOnce(),
									new SingleMessage("rapBattleConcede.final", "consortSound")
							}
					),
					//If you didn't accept the challenge
					new SingleMessage("rapBattle.denyAnswer")
				}
			).setAcceptNull()
		).consort(EnumConsort.NAKAGATOR, EnumConsort.IGUANA);
		
		addMessage("awaitHero", "landName", "consortTypes", "playerTitleLand").reqLand();
		addMessage(new ConditionedMessage("skaia", (EntityConsort consort, EntityPlayer player) -> !consort.visitedSkaia, new SingleMessage("watchSkaia"),
				new ConditionedMessage((EntityConsort consort, EntityPlayer player) -> MinestuckDimensionHandler.isSkaia(consort.dimension),
						new SingleMessage("atSkaia.1", "consortSound2"), new SingleMessage("visitedSkaia")))).consort(EnumConsort.SALAMANDER, EnumConsort.IGUANA, EnumConsort.NAKAGATOR).reqLand();
		addMessage(new ConditionedMessage("skaiaTurtle", (EntityConsort consort, EntityPlayer player) -> !consort.visitedSkaia, new SingleMessage("watchSkaia"),
				new ConditionedMessage((EntityConsort consort, EntityPlayer player) -> MinestuckDimensionHandler.isSkaia(consort.dimension),
						new SingleMessage("atSkaia.2"), new SingleMessage("visitedSkaia")))).consort(EnumConsort.TURTLE).reqLand();
		
		addMessage(new SingleMessage("zazzerpan")).consort(EnumConsort.TURTLE);
		addMessage(new SingleMessage("texasHistory", "landName")).consort(EnumConsort.TURTLE);
		addMessage(new SingleMessage("disks")).consort(EnumConsort.IGUANA);
		addMessage(new SingleMessage("whoops")).consort(EnumConsort.IGUANA);
		addMessage(new SingleMessage("fourthWall")).consort(EnumConsort.IGUANA);
		addMessage(new SingleMessage("hats")).consort(EnumConsort.SALAMANDER);
		addMessage(new SingleMessage("wwizard")).consort(EnumConsort.TURTLE);
		addMessage(new SingleMessage("stockMarket")).consort(EnumConsort.NAKAGATOR);
		addMessage(new SingleMessage("identity", "playerTitleLand", "playerNameLand")).consort(EnumConsort.SALAMANDER).reqLand();
		addMessage(new ChainMessage(0, new SingleMessage("college.1"), new SingleMessage("college.2")));
		addMessage(new ChainMessage(1, new SingleMessage("unknown.1"), new SingleMessage("unknown.2"))).consort(EnumConsort.TURTLE);
		addMessage(new ChainMessage(1, new SingleMessage("cult.1", "playerTitle"), new SingleMessage("cult.2"))).consort(EnumConsort.TURTLE, EnumConsort.SALAMANDER);

		addMessage(new ChoiceMessage(new DescriptionMessage("peppyOffer"),
				new SingleMessage[] { new SingleMessage("peppyOffer.buy"), new SingleMessage("peppyOffer.deny") },
				new MessageType[] {
						new PurchaseMessage(false, MinestuckLoot.CONSORT_JUNK_REWARD, 1000, "purchase",
								new ChainMessage(1, new SingleMessage("peppyOffer.item"), new SingleMessage("peppyOffer.purchase"))),
						new ChoiceMessage(new SingleMessage("peppyOffer.next"),
								new SingleMessage[] { new SingleMessage("peppyOffer.denyAgain"), new SingleMessage("peppyOffer.buy2") },
								new MessageType[] { new SingleMessage("dots"),
										new PurchaseMessage(false, MinestuckLoot.CONSORT_JUNK_REWARD, 500, "purchase",
												new SingleMessage("peppyOffer.purchase")) }) })).type(MerchantType.SHADY).consort(EnumConsort.NAKAGATOR, EnumConsort.IGUANA);


		addMessage(new ChoiceMessage(true, new SingleMessage("titlePresence", "playerTitle"),
				new SingleMessage[] { new SingleMessage("titlePresence.iam", "playerTitle"), new SingleMessage("titlePresence.agree") },
				new MessageType[] { new SingleMessage("titlePresence.iamAnswer", "consortSound2"), new SingleMessage("thanks") })).consort(EnumConsort.IGUANA, EnumConsort.SALAMANDER).reqLand();
		
		addMessage(new ChoiceMessage(new DescriptionMessage("shadyOffer"),
				new SingleMessage[]
						{
								new SingleMessage("shadyOffer.buy"),
								new SingleMessage("shadyOffer.deny")
						},
				new MessageType[] {
						new PurchaseMessage(false, MinestuckLoot.CONSORT_JUNK_REWARD, 1000, "purchase",
								new ChainMessage(1,
										new SingleMessage("shadyOffer.item"),
										new SingleMessage("shadyOffer.purchase")
								)
						),
						new ChoiceMessage(new SingleMessage("shadyOffer.next", "consortSound"),
								new SingleMessage[]
								{
										new SingleMessage("shadyOffer.denyAgain"),
										new SingleMessage("shadyOffer.buy2")
								},
								new MessageType[]
								{
										new SingleMessage("dots"),
										new PurchaseMessage(false, MinestuckLoot.CONSORT_JUNK_REWARD, 500, "purchase",
												new SingleMessage("shadyOffer.purchase")
										)
								}
						)
				}
		)).type(MerchantType.SHADY).consort(EnumConsort.SALAMANDER, EnumConsort.TURTLE);
		
		addMessage(new ChoiceMessage(true, new SingleMessage("denizen", "denizen"),
				new SingleMessage[] { new SingleMessage("denizen.what"), new SingleMessage("denizen.askAlignment") },
				new MessageType[] { new SingleMessage("denizen.explain", "playerClassLand"), new SingleMessage("denizen.alignment") })).consort(EnumConsort.SALAMANDER, EnumConsort.IGUANA, EnumConsort.TURTLE).reqLand();
		
		List<ItemStack> hungryList = ImmutableList.of(new ItemStack(Items.COOKIE), new ItemStack(MinestuckItems.bugOnAStick),
				new ItemStack(MinestuckItems.grasshopper), new ItemStack(MinestuckItems.chocolateBeetle),
				new ItemStack(MinestuckItems.coneOfFlies));
		addMessage(new ItemRequirement(hungryList, false, true, new SingleMessage("hungry"),
						new ChoiceMessage(new SingleMessage("hungry.askFood", "nbtItem:hungry.item"),
								new SingleMessage[] { new SingleMessage("hungry.accept"), new SingleMessage("hungry.deny") },
								new MessageType[] { new GiveItemMessage("hungry.item", 0, new SingleMessage("hungry.thanks")),
										new SingleMessage("sadface") }))).consort(EnumConsort.SALAMANDER, EnumConsort.IGUANA);
		addMessage(new ItemRequirement("hungry2", hungryList, false, true, false,
						new SingleMessage(
								"hungry"),
						new ChoiceMessage(
								new SingleMessage("hungry.askFood",
										"nbtItem:hungry2.item"),
								new SingleMessage[] {
										new SingleMessage(
												"hungry.accept"),
										new SingleMessage("hungry.deny") },
								new MessageType[] { new GiveItemMessage("hungry2.item", 0, new SingleMessage("hungry.thanks")),
										new ChoiceMessage(new SingleMessage("hungry.starving"),
												new SingleMessage[] { new SingleMessage("hungry.agree"),
														new SingleMessage("hungry.tooCheap") },
												new MessageType[] { new GiveItemMessage("hungry.sellItem", "hungry2.item", 10,
														new ChainMessage(1, new DescriptionMessage("hungry.finally", "nbtItem:hungry2.item"),
																new SingleMessage("hungry.finally"))),
														new SingleMessage("hungry.end") }) }))).consort(EnumConsort.SALAMANDER, EnumConsort.NAKAGATOR);
		
		addMessage(new MerchantGuiMessage(new SingleMessage("foodShop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).consort(EnumConsort.SALAMANDER);
		addMessage(new MerchantGuiMessage(new SingleMessage("fastFood"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).consort(EnumConsort.NAKAGATOR);
		addMessage(new MerchantGuiMessage(new SingleMessage("groceryStore"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).consort(EnumConsort.IGUANA);
		addMessage(new MerchantGuiMessage(new SingleMessage("tastyWelcome"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).consort(EnumConsort.TURTLE);
		addMessage(new MerchantGuiMessage(new SingleMessage("breathFoodShop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(fromNameTitle("wind"));
		addMessage(new MerchantGuiMessage(new SingleMessage("bloodFoodShop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(fromNameTitle("pulse"));
		addMessage(new MerchantGuiMessage(new SingleMessage("lifeFoodShop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(fromNameTitle("rabbits"));
		addMessage(new MerchantGuiMessage(new SingleMessage("doomFoodShop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(fromNameTitle("thunder"));
		//addMessage(new MerchantGuiMessage(new SingleMessage("frogFoodShop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(LandAspectRegistry.frogAspect);
		addMessage(new MerchantGuiMessage(new SingleMessage("spaceFoodShop", "landName"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(fromNameTitle("frogs"));
		addMessage(new MerchantGuiMessage(new SingleMessage("timeFoodShop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(fromNameTitle("clockwork"));
		addMessage(new MerchantGuiMessage(new SingleMessage("thymeFoodShop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(fromNameTitle("clockwork"), fromNameTitle("thought"));
		addMessage(new MerchantGuiMessage(new SingleMessage("mindFoodShop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(fromNameTitle("thought"));
		addMessage(new MerchantGuiMessage(new SingleMessage("heartFoodShop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(fromNameTitle("cake"));
		addMessage(new MerchantGuiMessage(new SingleMessage("lightFoodShop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(fromNameTitle("light"));
		addMessage(new MerchantGuiMessage(new SingleMessage("voidFoodShop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(fromNameTitle("silence"));
		addMessage(new MerchantGuiMessage(new SingleMessage("rageFoodShop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(fromNameTitle("monsters"));
		addMessage(new MerchantGuiMessage(new DescriptionMessage("hopeFoodShop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(fromNameTitle("towers"));
		addMessage(new MerchantGuiMessage(new SingleMessage("bucketsFoodShop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(fromNameTitle("buckets"));
		
		addMessage(new MerchantGuiMessage(new SingleMessage("generalShop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL);
		addMessage(new MerchantGuiMessage(new SingleMessage("gotTheGoods"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL);
		addMessage(new MerchantGuiMessage(new SingleMessage("risingShop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL);
		addMessage(new MerchantGuiMessage(new SingleMessage("breathGeneralShop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).landTitle(fromNameTitle("wind"));
		addMessage(new MerchantGuiMessage(new SingleMessage("bloodGeneralShop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).landTitle(fromNameTitle("pulse"));
		addMessage(new MerchantGuiMessage(new SingleMessage("lifeGeneralShop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).landTitle(fromNameTitle("rabbits"));
		addMessage(new MerchantGuiMessage(new SingleMessage("doomGeneralShop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).landTitle(fromNameTitle("thunder"));
		//addMessage(new MerchantGuiMessage(new SingleMessage("frogGeneralShop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).landTitle(LandAspectRegistry.frogAspect);
		addMessage(new MerchantGuiMessage(new SingleMessage("spaceGeneralShop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).landTitle(fromNameTitle("frogs"));
		addMessage(new MerchantGuiMessage(new SingleMessage("timeGeneralShop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).landTitle(fromNameTitle("clockwork"));
		addMessage(new MerchantGuiMessage(new SingleMessage("mindGeneralShop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).landTitle(fromNameTitle("thought"));
		addMessage(new MerchantGuiMessage(new SingleMessage("heartGeneralShop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).landTitle(fromNameTitle("cake"));
		addMessage(new MerchantGuiMessage(new SingleMessage("lightGeneralShop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).landTitle(fromNameTitle("light"));
		addMessage(new MerchantGuiMessage(new SingleMessage("voidGeneralShop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).landTitle(fromNameTitle("silence"));
		addMessage(new MerchantGuiMessage(new SingleMessage("rageGeneralShop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).landTitle(fromNameTitle("monsters"));
		addMessage(new MerchantGuiMessage(new DescriptionMessage("hopeGeneralShop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).landTitle(fromNameTitle("towers"));
		addMessage(new MerchantGuiMessage(new SingleMessage("bucketsGeneralShop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).landTitle(fromNameTitle("buckets"));
		
		addMessage(new MerchantGuiMessage(new SingleMessage("boringShop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).landTerrain(fromNameTerrain("rainbow"));
	}
	
	public static DialogueWrapper addMessage(String message, String... args)
	{
		return addMessage(new SingleMessage(message, args));
	}
	public static DialogueWrapper addMessage(MessageType message)
	{
		DialogueWrapper msg = new DialogueWrapper();
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
	
	public static DialogueWrapper getRandomMessage(EntityConsort consort, EntityPlayer player)
	{
		LandAspectRegistry.AspectCombination aspects = MinestuckDimensionHandler.getAspects(consort.homeDimension);
		
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
		private DialogueWrapper()
		{
			super(10);
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
		
		public DialogueWrapper weight(int weight)
		{
			itemWeight = weight;
			return this;
		}
		
		public ITextComponent getMessage(EntityConsort consort, EntityPlayer player)
		{
			return messageStart.getMessage(consort, player, "");
		}
		
		public ITextComponent getFromChain(EntityConsort consort, EntityPlayer player, String fromChain)
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
		boolean apply(EntityConsort consort);
	}
}
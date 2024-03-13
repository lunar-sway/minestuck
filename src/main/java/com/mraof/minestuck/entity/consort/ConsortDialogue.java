package com.mraof.minestuck.entity.consort;

import com.mraof.minestuck.entity.consort.EnumConsort.MerchantType;
import com.mraof.minestuck.entity.consort.MessageType.*;
import com.mraof.minestuck.item.loot.MSLootTables;
import com.mraof.minestuck.skaianet.SburbHandler;
import com.mraof.minestuck.util.MSTags;
import com.mraof.minestuck.world.MSDimensions;
import com.mraof.minestuck.world.lands.LandTypeConditions;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

import static com.mraof.minestuck.item.loot.MSLootTables.CONSORT_FOOD_STOCK;
import static com.mraof.minestuck.item.loot.MSLootTables.CONSORT_GENERAL_STOCK;
import static com.mraof.minestuck.world.lands.LandTypeConditions.terrainLand;
import static com.mraof.minestuck.world.lands.LandTypeConditions.titleLand;
import static com.mraof.minestuck.world.lands.LandTypes.*;

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
	public static void init()
	{
		//Monsters
		//addMessage(new SingleMessage("pet_zombie")).condition(titleLand(MSTags.TitleLandTypes.MONSTERS)).consort(EnumConsort.NAKAGATOR, EnumConsort.SALAMANDER);
		
		//Towers
		//addMessage("tower_gone").condition(titleLand(TOWERS)).consort(EnumConsort.TURTLE, EnumConsort.SALAMANDER);
		
		//Frost
		addMessage(new ChoiceMessage(new SingleMessage("fur_coat"), new SingleMessage[]{new SingleMessage("fur_coat.pay"), new SingleMessage("fur_coat.ignore")},
				new MessageType[]{new PurchaseMessage(MSLootTables.CONSORT_JUNK_REWARD, 100, new ChainMessage(1, new SingleMessage("fur_coat.grattitude"), new SingleMessage("thank_you"))),
						new SingleMessage("fur_coat.death")})).condition(terrainLand(FROST));
		addMessage("tent_protection").condition(terrainLand(FROST)).consortReq(ConsortEntity::hasRestriction);
		
		//Misc
		addMessage("denizen_mention").reqLand();
		addMessage("floating_island").consortReq(consort -> consort.distanceToSqr(new Vec3(consort.level().getLevelData().getXSpawn(), consort.level().getLevelData().getYSpawn(), consort.level().getLevelData().getZSpawn())) < 65536).reqLand();
		addMessage("ring_fishing").consort(EnumConsort.SALAMANDER, EnumConsort.IGUANA);
		addMessage("frog_walk").consort(EnumConsort.TURTLE);
		addMessage("delicious_hair").consort(EnumConsort.IGUANA);
		addMessage("lazy_king").condition(terrainLand(SHADE));
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
		
		addMessage("useless_pogo");
		addMessage("await_hero", "land_name", "consort_types", "player_title_land").reqLand();
		addMessage(new ConditionedMessage("skaia", (ConsortEntity consort, ServerPlayer player) -> !consort.visitedSkaia, new SingleMessage("watch_skaia"),
				new ConditionedMessage((ConsortEntity consort, ServerPlayer player) -> MSDimensions.isSkaia(consort.level().dimension()),
						new SingleMessage("at_skaia.1", "consort_sound_2"), new SingleMessage("visited_skaia")))).consort(EnumConsort.SALAMANDER, EnumConsort.IGUANA, EnumConsort.NAKAGATOR).reqLand();
		addMessage(new ConditionedMessage("skaia_turtle", (ConsortEntity consort, ServerPlayer player) -> !consort.visitedSkaia, new SingleMessage("watch_skaia"),
				new ConditionedMessage((ConsortEntity consort, ServerPlayer player) -> MSDimensions.isSkaia(consort.level().dimension()),
						new SingleMessage("at_skaia.2"), new SingleMessage("visited_skaia")))).consort(EnumConsort.TURTLE).reqLand();
		
		addMessage(new SingleMessage("zazzerpan")).consort(EnumConsort.TURTLE);
		addMessage(new SingleMessage("texas_history", "land_name")).consort(EnumConsort.TURTLE);
		addMessage(new SingleMessage("disks")).consort(EnumConsort.IGUANA);
		addMessage(new SingleMessage("whoops")).consort(EnumConsort.IGUANA);
		addMessage(new SingleMessage("fourth_wall")).consort(EnumConsort.IGUANA);
		addMessage(new SingleMessage("consort_scoliosis")).consort(EnumConsort.TURTLE);
		addMessage(new SingleMessage("oh_to_be_ugly")).consort(EnumConsort.NAKAGATOR);
		addMessage(new SingleMessage("no_to_podcasting")).consort(EnumConsort.IGUANA);
		addMessage(new SingleMessage("bats")).consort(EnumConsort.TURTLE);
		addMessage(new SingleMessage("so_what")).consort(EnumConsort.SALAMANDER);
		addMessage(new SingleMessage("trolly_problem")).consort(EnumConsort.NAKAGATOR);
		addMessage(new SingleMessage("a_little_lampshading")).consort(EnumConsort.IGUANA);
		addMessage(new SingleMessage("hats")).consort(EnumConsort.SALAMANDER);
		addMessage(new SingleMessage("wwizard")).consort(EnumConsort.TURTLE);
		addMessage(new SingleMessage("stock_market")).consort(EnumConsort.NAKAGATOR);
		addMessage(new SingleMessage("identity", "player_title_land", "player_name_land")).consort(EnumConsort.SALAMANDER).reqLand();
		addMessage(new ChainMessage(0, new SingleMessage("college.1"), new SingleMessage("college.2")));
		addMessage(new ChainMessage(1, new SingleMessage("unknown.1"), new SingleMessage("unknown.2"))).consort(EnumConsort.TURTLE);
		addMessage(new ChainMessage(1, new SingleMessage("cult.1", "player_title"), new SingleMessage("cult.2"))).consort(EnumConsort.TURTLE, EnumConsort.SALAMANDER);
		
		addMessage(new ChoiceMessage(new DescriptionMessage("peppy_offer"),
				new SingleMessage[]{new SingleMessage("peppy_offer.buy"), new SingleMessage("peppy_offer.deny")},
				new MessageType[]{
						new PurchaseMessage(false, MSLootTables.CONSORT_JUNK_REWARD, 1000, -15, "purchase",
								new ChainMessage(1, new SingleMessage("peppy_offer.item"), new SingleMessage("peppy_offer.purchase"))),
						new ChoiceMessage(new SingleMessage("peppy_offer.next"),
								new SingleMessage[]{new SingleMessage("peppy_offer.deny_again"), new SingleMessage("peppy_offer.buy_2")},
								new MessageType[]{new SingleMessage("dots"),
										new PurchaseMessage(false, MSLootTables.CONSORT_JUNK_REWARD, 500, -35, "purchase",
												new SingleMessage("peppy_offer.purchase"))})})).type(MerchantType.SHADY).consort(EnumConsort.NAKAGATOR, EnumConsort.IGUANA);
		
		
		addMessage(new ChoiceMessage(true, new SingleMessage("title_presence", "player_title"),
				new SingleMessage[]{new SingleMessage("title_presence.iam", "player_title"), new SingleMessage("title_presence.agree")},
				new MessageType[]{new SingleMessage("title_presence.iam_answer", "consort_sound_2"), new SingleMessage("thanks")})).consort(EnumConsort.IGUANA, EnumConsort.SALAMANDER).reqLand();
		
		addMessage(new ChoiceMessage(new DescriptionMessage("shady_offer"),
				new SingleMessage[]
						{
								new SingleMessage("shady_offer.buy"),
								new SingleMessage("shady_offer.deny")
						},
				new MessageType[]{
						new PurchaseMessage(false, MSLootTables.CONSORT_JUNK_REWARD, 1000, -15, "purchase",
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
												new PurchaseMessage(false, MSLootTables.CONSORT_JUNK_REWARD, 500, -35, "purchase",
														new SingleMessage("shady_offer.purchase")
												)
										}
						)
				}
		)).type(MerchantType.SHADY).consort(EnumConsort.SALAMANDER, EnumConsort.TURTLE);
		
		addMessage(new ChoiceMessage(true, new SingleMessage("denizen", "denizen"),
				new SingleMessage[]{new SingleMessage("denizen.what"), new SingleMessage("denizen.ask_alignment")},
				new MessageType[]{new SingleMessage("denizen.explain", "player_class_land"), new SingleMessage("denizen.alignment")})).consort(EnumConsort.SALAMANDER, EnumConsort.IGUANA, EnumConsort.TURTLE).reqLand();
		
		addMessage(new ItemRequirement(MSTags.Items.CONSORT_SNACKS, false, true, new SingleMessage("hungry"),
						new ChoiceMessage(new SingleMessage("hungry.ask_food", "nbt_item:hungry.item"),
								new SingleMessage[] { new SingleMessage("hungry.accept"), new SingleMessage("hungry.deny") },
								new MessageType[] { new GiveItemMessage("hungry.thanks", "hungry.item", 0, 15, new SingleMessage("hungry.thanks")),
										new SingleMessage("sadface") }))).consort(EnumConsort.SALAMANDER, EnumConsort.IGUANA);
		addMessage(new ItemRequirement("hungry2", MSTags.Items.CONSORT_SNACKS, false, true, false,
						new SingleMessage(
								"hungry"),
						new ChoiceMessage(
								new SingleMessage("hungry.ask_food",
										"nbt_item:hungry2.item"),
								new SingleMessage[] {
										new SingleMessage(
												"hungry.accept"),
										new SingleMessage("hungry.deny") },
								new MessageType[] { new GiveItemMessage("hungry.thanks", "hungry2.item", 0, 15, new SingleMessage("hungry.thanks")),
										new ChoiceMessage(new SingleMessage("hungry.starving"),
												new SingleMessage[] { new SingleMessage("hungry.agree"),
														new SingleMessage("hungry.too_cheap") },
												new MessageType[] { new GiveItemMessage("hungry.sell_item", "hungry2.item", 10, 0,
														new ChainMessage(1, new DescriptionMessage("hungry.finally", "nbt_item:hungry2.item"),
																new SingleMessage("hungry.finally"))),
														new SingleMessage("hungry.end") }) }))).consort(EnumConsort.SALAMANDER, EnumConsort.NAKAGATOR);
		
		addMessage(new MerchantGuiMessage(new SingleMessage("food_shop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).consort(EnumConsort.SALAMANDER).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("fast_food"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).consort(EnumConsort.NAKAGATOR).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("grocery_store"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).consort(EnumConsort.IGUANA).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("tasty_welcome"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).consort(EnumConsort.TURTLE).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("breeze_food_shop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).condition(titleLand(WIND)).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("blood_food_shop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).condition(titleLand(PULSE)).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("rabbit_food_shop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).condition(titleLand(RABBITS)).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("lightning_food_shop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).condition(titleLand(THUNDER)).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("frog_leg_food_shop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).condition(titleLand(FROGS)).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("frog_food_shop", "land_name"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).condition(titleLand(FROGS)).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("time_food_shop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).condition(titleLand(CLOCKWORK)).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("thyme_food_shop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).condition(titleLand(CLOCKWORK).or(THOUGHT)).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("library_food_shop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).condition(titleLand(THOUGHT)).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("cake_food_shop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).condition(titleLand(CAKE)).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("light_food_shop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).condition(titleLand(LIGHT)).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("silence_food_shop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).condition(titleLand(SILENCE)).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("rage_food_shop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).condition(titleLand(MSTags.TitleLandTypes.MONSTERS)).lockToConsort();
		addMessage(new MerchantGuiMessage(new DescriptionMessage("hope_food_shop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).condition(titleLand(TOWERS)).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("buckets_food_shop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).condition(titleLand(BUCKETS)).lockToConsort();
		
		addMessage(new MerchantGuiMessage(new SingleMessage("general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("got_the_goods"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("rising_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("breeze_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).condition(titleLand(WIND)).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("blood_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).condition(titleLand(PULSE)).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("life_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).condition(titleLand(RABBITS)).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("doom_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).condition(titleLand(THUNDER)).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("frog_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).condition(titleLand(FROGS)).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("time_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).condition(titleLand(CLOCKWORK)).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("book_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).condition(titleLand(THOUGHT)).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("cake_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).condition(titleLand(CAKE)).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("light_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).condition(titleLand(LIGHT)).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("silence_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).condition(titleLand(SILENCE)).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("rage_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).condition(titleLand(MSTags.TitleLandTypes.MONSTERS)).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("tower_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).condition(titleLand(TOWERS)).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("buckets_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).condition(titleLand(BUCKETS)).lockToConsort();
		
		addMessage(new MerchantGuiMessage(new SingleMessage("boring_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).condition(terrainLand(RAINBOW));
	}
	
	/**
	 * Not thread-safe. Make sure to only call this on the main thread
	 */
	public static DialogueWrapper addMessage(String message, String... args)
	{
		return addMessage(new SingleMessage(message, args));
	}
	
	/**
	 * Not thread-safe. Make sure to only call this on the main thread
	 */
	public static DialogueWrapper addMessage(MessageType message)
	{
		return addMessage(10, message);
	}
	
	/**
	 * Not thread-safe. Make sure to only call this on the main thread
	 */
	public static DialogueWrapper addMessage(int weight, MessageType message)
	{
		DialogueWrapper msg = new DialogueWrapper(weight);
		msg.messageStart = message;
		messages.add(msg);
		return msg;
	}
	
	public static DialogueWrapper getRandomMessage(ConsortEntity consort, boolean hasHadMessage)
	{
		LandTypePair aspects = LandTypePair.getTypes(consort.getServer(), consort.homeDimension).orElse(null);
		
		List<DialogueWrapper> list = new ArrayList<>();
		
		for(DialogueWrapper message : messages)
		{
			if(message.lockToConsort && hasHadMessage)
				continue;
			if(message.reqLand && aspects == null)
				continue;
			if(message.consortRequirement != null && !message.consortRequirement.contains(consort.getConsortType()))
				continue;
			if(message.terrainCondition != null && !(aspects != null && message.terrainCondition.test(aspects.getTerrain())))
				continue;
			if(message.titleCondition != null && !(aspects != null && message.titleCondition.test(aspects.getTitle())))
				continue;
			if(message.merchantRequirement == null && consort.merchantType != EnumConsort.MerchantType.NONE
					|| message.merchantRequirement != null && !message.merchantRequirement.contains(consort.merchantType))
				continue;
			if(message.additionalRequirement != null && !message.additionalRequirement.apply(consort))
				continue;
			list.add(message);
		}
		
		return WeightedRandom.getRandomItem(consort.level().random, list).orElseThrow();
	}
	
	public static DialogueWrapper getMessageFromString(String name)
	{
		for(DialogueWrapper message : messages)
			if(message.getString().equals(name))
				return message;
		return null;
	}
	
	public static class DialogueWrapper extends WeightedEntry.IntrusiveBase
	{
		
		private DialogueWrapper(int weight)
		{
			super(weight);
		}
		
		private boolean reqLand;
		private boolean lockToConsort;
		
		private MessageType messageStart;
		
		@Nullable
		private LandTypeConditions.Terrain terrainCondition;
		@Nullable
		private LandTypeConditions.Title titleCondition;
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
		
		public DialogueWrapper condition(LandTypeConditions.Terrain condition)
		{
			this.terrainCondition = condition;
			return this.reqLand();
		}
		
		public DialogueWrapper condition(LandTypeConditions.Title condition)
		{
			this.titleCondition = condition;
			return this.reqLand();
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
		
		public Component getMessage(ConsortEntity consort, ServerPlayer player)
		{
			return messageStart.getMessage(consort, player, "");
		}
		
		public Component getFromChain(ConsortEntity consort, ServerPlayer player, String fromChain)
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
	
	public static void serverStarting()
	{
		//debugPrintAll();
	}
	
	@SuppressWarnings("unused")
	private static void debugPrintAll()
	{
		List<Component> list = new ArrayList<>();
		for(DialogueWrapper wrapper : messages)
		{
			wrapper.messageStart.debugAddAllMessages(list);
		}
		
		for(Component textComponent : list)
			LOGGER.info(textComponent.getString());
	}
}
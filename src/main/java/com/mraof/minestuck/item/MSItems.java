package com.mraof.minestuck.item;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.EnumCassetteType;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.item.CrewPosterEntity;
import com.mraof.minestuck.entity.item.MetalBoatEntity;
import com.mraof.minestuck.entity.item.SbahjPosterEntity;
import com.mraof.minestuck.entity.item.ShopPosterEntity;
import com.mraof.minestuck.fluid.MSFluids;
import com.mraof.minestuck.item.artifact.CruxiteAppleItem;
import com.mraof.minestuck.item.artifact.CruxitePotionItem;
import com.mraof.minestuck.item.block.*;
import com.mraof.minestuck.item.foods.DrinkableItem;
import com.mraof.minestuck.item.foods.HealingFoodItem;
import com.mraof.minestuck.item.foods.SurpriseEmbryoItem;
import com.mraof.minestuck.item.foods.UnknowableEggItem;
import com.mraof.minestuck.item.weapon.*;
import com.mraof.minestuck.item.weapon.projectiles.BouncingProjectileWeaponItem;
import com.mraof.minestuck.item.weapon.projectiles.ConsumableProjectileWeaponItem;
import com.mraof.minestuck.item.weapon.projectiles.ReturningProjectileWeaponItem;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.*;

import java.util.function.Function;

import static com.mraof.minestuck.block.MSBlocks.*;

/**
 * This class contains all non-ItemBlock items that minestuck adds,
 * and is responsible for initializing and registering these.
 */
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MSItems
{
	//TODO removed @ObjectHolder(Minestuck.MOD_ID), ensure that was okay to do
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Minestuck.MOD_ID);
	
	//hammers
	public static final RegistryObject<Item> CLAW_HAMMER = ITEMS.register("claw_hammer", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 2, -2.8F).efficiency(1.0F).set(MSItemTypes.HAMMER_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> SLEDGE_HAMMER = ITEMS.register("sledge_hammer", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 4, -3.2F).efficiency(4.0F).set(MSItemTypes.HAMMER_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> MAILBOX = ITEMS.register("mailbox", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 4, -3.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> BLACKSMITH_HAMMER = ITEMS.register("blacksmith_hammer", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 5, -3.2F).efficiency(3.5F).set(MSItemTypes.HAMMER_TOOL), new Item.Properties().defaultDurability(450).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> POGO_HAMMER = ITEMS.register("pogo_hammer", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.POGO_TIER, 5, -3.2F).efficiency(2.0F).set(MSItemTypes.HAMMER_TOOL).set(PogoEffect.EFFECT_07).add(PogoEffect.EFFECT_07), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> WRINKLEFUCKER = ITEMS.register("wrinklefucker", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.POGO_TIER, 7, -3.2F).efficiency(2.0F).set(MSItemTypes.HAMMER_TOOL).set(PogoEffect.EFFECT_04).add(PogoEffect.EFFECT_04), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> TELESCOPIC_SASSACRUSHER = ITEMS.register("telescopic_sassacrusher", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.BOOK_TIER, 11, -3.4F).efficiency(5.0F).set(MSItemTypes.HAMMER_TOOL), new Item.Properties().defaultDurability(1024).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> DEMOCRATIC_DEMOLITIONER = ITEMS.register("democratic_demolitioner", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 4, -3.0F).efficiency(1.0F).set(MSItemTypes.HAMMER_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> REGI_HAMMER = ITEMS.register("regi_hammer", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 7, -3.2F).efficiency(8.0F).set(MSItemTypes.HAMMER_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> FEAR_NO_ANVIL = ITEMS.register("fear_no_anvil", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.DENIZEN_TIER, 7, -3.2F).efficiency(7.0F).set(MSItemTypes.HAMMER_TOOL).add(OnHitEffect.TIME_SLOWNESS_AOE).add(OnHitEffect.enemyPotionEffect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1))), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> MELT_MASHER = ITEMS.register("melt_masher", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 8, -3.2F).efficiency(12.0F).set(MSItemTypes.HAMMER_TOOL).add(OnHitEffect.setOnFire(25)), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> ESTROGEN_EMPOWERED_EVERYTHING_ERADICATOR = ITEMS.register("estrogen_empowered_everything_eradicator", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 7, -3.2F).efficiency(9.0F).set(MSItemTypes.MULTI_TOOL).set(new FarmineEffect(Integer.MAX_VALUE, 200)).set(PogoEffect.EFFECT_07).add(PogoEffect.EFFECT_07), new Item.Properties().defaultDurability(6114).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> EEEEEEEEEEEE = ITEMS.register("eeeeeeeeeeee", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.SBAHJ_TIER, 9, -3.2F).efficiency(9.1F).set(MSItemTypes.HAMMER_TOOL).set(PogoEffect.EFFECT_02).add(PogoEffect.EFFECT_02, OnHitEffect.playSound(() -> MSSoundEvents.ITEM_EEEEEEEEEEEE_HIT, 1.5F, 1.0F)), new Item.Properties().defaultDurability(6114).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> ZILLYHOO_HAMMER = ITEMS.register("zillyhoo_hammer", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ZILLY_TIER, 8, -3.2F).efficiency(15.0F).set(MSItemTypes.HAMMER_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> POPAMATIC_VRILLYHOO = ITEMS.register("popamatic_vrillyhoo", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ZILLY_TIER, 6, -3.2F).efficiency(15.0F).set(MSItemTypes.HAMMER_TOOL).add(OnHitEffect.RANDOM_DAMAGE), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> SCARLET_ZILLYHOO = ITEMS.register("scarlet_zillyhoo", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CORUNDUM_TIER, 9, -3.2F).efficiency(4.0F).set(MSItemTypes.HAMMER_TOOL).add(OnHitEffect.setOnFire(50)), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> MWRTHWL = ITEMS.register("mwrthwl", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.WELSH_TIER, 8, -3.2F).efficiency(4.0F).set(MSItemTypes.HAMMER_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.EPIC)));
	
	//blades
	public static final RegistryObject<Item> SORD = ITEMS.register("sord", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.SBAHJ_TIER, 3, -3.0F).efficiency(1.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SORD_DROP), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> PAPER_SWORD = ITEMS.register("paper_sword", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.PAPER_TIER, 2, -2.4F).efficiency(3.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> SWONGE = ITEMS.register("swonge", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 3, -2.4F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SWEEP).set(ItemRightClickEffect.absorbFluid(() -> Blocks.WATER, MSItems.WET_SWONGE)), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	//public static final RegistryObject<Item> SWONGE = REGISTER.register("swonge", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 3, -2.4F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SWEEP).set(ItemRightClickEffect.absorbFluid(() -> Blocks.WATER, () -> MSItems.WET_SWONGE)), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> WET_SWONGE = ITEMS.register("wet_swonge", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 3, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SWEEP).set(RightClickBlockEffect.placeFluid(() -> Blocks.WATER, MSItems.SWONGE)).add(OnHitEffect.playSound(() -> SoundEvents.GUARDIAN_FLOP)), new Item.Properties()));
	//public static final RegistryObject<Item> WET_SWONGE = REGISTER.register("wet_swonge", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 3, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SWEEP).set(RightClickBlockEffect.placeFluid(() -> Blocks.WATER, () -> MSItems.SWONGE)).add(OnHitEffect.playSound(() -> SoundEvents.GUARDIAN_FLOP)), new Item.Properties()));
	public static final RegistryObject<Item> PUMORD = ITEMS.register("pumord", () -> new WeaponItem(new WeaponItem.Builder(Tiers.STONE, 3, -2.4F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SWEEP).set(ItemRightClickEffect.absorbFluid(() -> Blocks.LAVA, MSItems.WET_PUMORD)), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	//public static final RegistryObject<Item> PUMORD = REGISTER.register("pumord", () -> new WeaponItem(new WeaponItem.Builder(Tiers.STONE, 3, -2.4F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SWEEP).set(ItemRightClickEffect.absorbFluid(() -> Blocks.LAVA, () -> MSItems.WET_PUMORD)), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> WET_PUMORD = ITEMS.register("wet_pumord", () -> new WeaponItem(new WeaponItem.Builder(Tiers.STONE, 3, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SWEEP).set(RightClickBlockEffect.placeFluid(() -> Blocks.LAVA, MSItems.PUMORD)).add(OnHitEffect.playSound(() -> SoundEvents.BUCKET_EMPTY_LAVA, 1.0F, 0.2F)).add(OnHitEffect.setOnFire(10)), new Item.Properties()));
	//public static final RegistryObject<Item> WET_PUMORD = REGISTER.register("wet_pumord", () -> new WeaponItem(new WeaponItem.Builder(Tiers.STONE, 3, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SWEEP).set(RightClickBlockEffect.placeFluid(() -> Blocks.LAVA, () -> MSItems.PUMORD)).add(OnHitEffect.playSound(() -> SoundEvents.BUCKET_EMPTY_LAVA, 1.0F, 0.2F)).add(OnHitEffect.setOnFire(10)), new Item.Properties()));
	public static final RegistryObject<Item> CACTACEAE_CUTLASS = ITEMS.register("cactaceae_cutlass", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CACTUS_TIER, 3, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().tab(MSItemGroup.WEAPONS))); //The sword harvestTool is only used against webs, hence the high efficiency.
	public static final RegistryObject<Item> STEAK_SWORD = ITEMS.register("steak_sword", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.MEAT_TIER, 4, -2.4F).efficiency(5.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).setEating(FinishUseItemEffect.foodEffect(8, 1F)), new Item.Properties().defaultDurability(250).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> BEEF_SWORD = ITEMS.register("beef_sword", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.MEAT_TIER, 2, -2.4F).efficiency(5.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).setEating(FinishUseItemEffect.foodEffect(3, 0.8F, 75)), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> IRRADIATED_STEAK_SWORD = ITEMS.register("irradiated_steak_sword", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.MEAT_TIER, 5, -2.4F).efficiency(5.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).setEating(FinishUseItemEffect.potionEffect(() -> new MobEffectInstance(MobEffects.WITHER, 100, 1), 0.9F), FinishUseItemEffect.foodEffect(4, 0.4F, 25)), new Item.Properties().defaultDurability(300).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> MACUAHUITL = ITEMS.register("macuahuitl", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.PRISMARINE_TIER, 3, -2.4F).efficiency(1.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().defaultDurability(100).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> FROSTY_MACUAHUITL = ITEMS.register("frosty_macuahuitl", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ICE_TIER, 6, -2.4F).efficiency(1.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).add(OnHitEffect.ICE_SHARD), new Item.Properties().defaultDurability(200).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> KATANA = ITEMS.register("katana", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 3, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> UNBREAKABLE_KATANA = ITEMS.register("unbreakable_katana", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ZILLY_TIER, 6, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().defaultDurability(-1).tab(MSItemGroup.WEAPONS).rarity(Rarity.RARE))); //Actually unbreakable
	public static final RegistryObject<Item> ANGEL_APOCALYPSE = ITEMS.register("angel_apocalypse", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 5, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).add(OnHitEffect.HOPE_RESISTANCE), new Item.Properties().defaultDurability(2048).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> FIRE_POKER = ITEMS.register("fire_poker", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 4, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).add(OnHitEffect.setOnFire(30)), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> TOO_HOT_TO_HANDLE = ITEMS.register("too_hot_to_handle", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 3, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).add(OnHitEffect.setOnFire(10)), new Item.Properties().defaultDurability(350).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> CALEDSCRATCH = ITEMS.register("caledscratch", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 7, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> CALEDFWLCH = ITEMS.register("caledfwlch", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.WELSH_TIER, 6, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> ROYAL_DERINGER = ITEMS.register("royal_deringer", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.DENIZEN_TIER, 7, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> CLAYMORE = ITEMS.register("claymore", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 5, -2.6F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().defaultDurability(600).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> CUTLASS_OF_ZILLYWAIR = ITEMS.register("cutlass_of_zillywair", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ZILLY_TIER, 6, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> REGISWORD = ITEMS.register("regisword", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 5, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> CRUEL_FATE_CRUCIBLE = ITEMS.register("cruel_fate_crucible", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.DENIZEN_TIER, 4, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().defaultDurability(4096).tab(MSItemGroup.WEAPONS).rarity(Rarity.RARE))); //Special property in ServerEventHandler
	public static final RegistryObject<Item> SCARLET_RIBBITAR = ITEMS.register("scarlet_ribbitar", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CORUNDUM_TIER, 7, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> DOGG_MACHETE = ITEMS.register("dogg_machete", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 6, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().defaultDurability(1000).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> COBALT_SABRE = ITEMS.register("cobalt_sabre", () -> new WeaponItem(new WeaponItem.Builder(Tiers.GOLD, 7, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).add(OnHitEffect.setOnFire(30)), new Item.Properties().defaultDurability(300).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> QUANTUM_SABRE = ITEMS.register("quantum_sabre", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.URANIUM_TIER, 4, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).add(OnHitEffect.onCrit(OnHitEffect.enemyPotionEffect(() -> new MobEffectInstance(MobEffects.WITHER, 100, 1)))), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> SHATTER_BEACON = ITEMS.register("shatter_beacon", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 7, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> SHATTER_BACON = ITEMS.register("shatter_bacon", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.MEAT_TIER, 7, -2.4F).efficiency(5.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).add(OnHitEffect.SORD_DROP), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> SUBTRACTSHUMIDIRE_ZOMORRODNEGATIVE = ITEMS.register("subtractshumidire_zomorrodnegative", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.URANIUM_TIER, 6, -2.6F).efficiency(5.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).add(OnHitEffect.setOnFire(30)), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	
	public static final RegistryObject<Item> DAGGER = ITEMS.register("dagger", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 0, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.backstab(3)), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> NIFE = ITEMS.register("nife", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.SBAHJ_TIER, 1, -2.0F).add(OnHitEffect.SORD_DROP), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> LIGHT_OF_MY_KNIFE = ITEMS.register("light_of_my_knife", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.EMERALD_TIER, 1, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.backstab(4)), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> STARSHARD_TRI_BLADE = ITEMS.register("starshard_tri_blade", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CORUNDUM_TIER, 1, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.backstab(9)), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> TOOTHRIPPER = ITEMS.register("toothripper", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 3, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.backstab(4)), new Item.Properties().defaultDurability(1200).tab(MSItemGroup.WEAPONS)));
	
	//axes
	public static final RegistryObject<Item> BATLEACKS = ITEMS.register("batleacks", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.SBAHJ_TIER, 3, -3.5F).efficiency(1.0F).set(MSItemTypes.AXE_TOOL).add(OnHitEffect.SORD_DROP), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> COPSE_CRUSHER = ITEMS.register("copse_crusher", () -> new WeaponItem(new WeaponItem.Builder(Tiers.STONE, 5, -3.0F).efficiency(6.0F).disableShield().set(MSItemTypes.AXE_TOOL).set(new FarmineEffect(Integer.MAX_VALUE, 20)), new Item.Properties().defaultDurability(400).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> QUENCH_CRUSHER = ITEMS.register("quench_crusher", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 7, -3.0F).efficiency(2.0F).disableShield().set(MSItemTypes.AXE_TOOL).setEating(FinishUseItemEffect.foodEffect(6, 0.6F, 75)), new Item.Properties().defaultDurability(500).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> MELONSBANE = ITEMS.register("melonsbane", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 8, -3.0F).efficiency(3.0F).disableShield().set(MSItemTypes.AXE_TOOL).set(DestroyBlockEffect.extraHarvests(true, 0.6F, 20, () -> Items.MELON_SLICE, () -> Blocks.MELON)), new Item.Properties().defaultDurability(400).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> CROP_CHOP = ITEMS.register("crop_chop", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 7, -3.0F).efficiency(3.0F).disableShield().set(MSItemTypes.AXE_TOOL).set(DestroyBlockEffect.DOUBLE_FARM), new Item.Properties().defaultDurability(800).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> THE_LAST_STRAW = ITEMS.register("the_last_straw", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 9, -3.0F).efficiency(3.0F).disableShield().set(MSItemTypes.AXE_TOOL).set(DestroyBlockEffect.DOUBLE_FARM), new Item.Properties().defaultDurability(950).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> BATTLEAXE = ITEMS.register("battleaxe", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 8, -3.0F).efficiency(3.0F).disableShield().set(MSItemTypes.AXE_TOOL), new Item.Properties().defaultDurability(600).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> CANDY_BATTLEAXE = ITEMS.register("candy_battleaxe", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CANDY_TIER, 8, -3.0F).efficiency(2.0F).disableShield().set(MSItemTypes.AXE_TOOL).add(OnHitEffect.SET_CANDY_DROP_FLAG), new Item.Properties().defaultDurability(111).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> CHOCO_LOCO_WOODSPLITTER = ITEMS.register("choco_loco_woodsplitter", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CANDY_TIER, 9, -3.0F).efficiency(2.0F).disableShield().set(MSItemTypes.AXE_TOOL).setEating(FinishUseItemEffect.foodEffect(8, 0.4F)), new Item.Properties().defaultDurability(350).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> STEEL_EDGE_CANDYCUTTER = ITEMS.register("steel_edge_candycutter", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 8, -3.0F).efficiency(3.0F).disableShield().set(MSItemTypes.AXE_TOOL).add(OnHitEffect.SET_CANDY_DROP_FLAG), new Item.Properties().defaultDurability(800).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> BLACKSMITH_BANE = ITEMS.register("blacksmith_bane", () -> new WeaponItem(new WeaponItem.Builder(Tiers.STONE, 8, -3.0F).efficiency(6.0F).disableShield().set(MSItemTypes.AXE_TOOL), new Item.Properties().defaultDurability(413).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> REGIAXE = ITEMS.register("regiaxe", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 6, -3.0F).disableShield().efficiency(6.0F).set(MSItemTypes.AXE_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> GOTHY_AXE = ITEMS.register("gothy_axe", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 8, -3.0F).efficiency(6.0F).disableShield().set(MSItemTypes.AXE_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> SURPRISE_AXE = ITEMS.register("surprise_axe", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 7, -3.0F).efficiency(6.0F).disableShield().set(MSItemTypes.AXE_TOOL).add(OnHitEffect.KUNDLER_SURPRISE), new Item.Properties().defaultDurability(600).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> SHOCK_AXE = ITEMS.register("shock_axe", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 8, -3.0F).efficiency(6.0F).disableShield().set(MSItemTypes.AXE_TOOL).set(ItemRightClickEffect.switchTo(MSItems.SHOCK_AXE_UNPOWERED)).add(OnHitEffect.DROP_FOE_ITEM).add(InventoryTickEffect.DROP_WHEN_IN_WATER).add(OnHitEffect.playSound(() -> MSSoundEvents.EVENT_ELECTRIC_SHOCK, 0.6F, 1.0F)), new Item.Properties().defaultDurability(800).tab(MSItemGroup.WEAPONS)));
	//public static final RegistryObject<Item> SHOCK_AXE = REGISTER.register("shock_axe", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 8, -3.0F).efficiency(6.0F).disableShield().set(MSItemTypes.AXE_TOOL).set(ItemRightClickEffect.switchTo(() -> SHOCK_AXE_UNPOWERED)).add(OnHitEffect.DROP_FOE_ITEM).add(InventoryTickEffect.DROP_WHEN_IN_WATER).add(OnHitEffect.playSound(() -> MSSoundEvents.EVENT_ELECTRIC_SHOCK, 0.6F, 1.0F)), new Item.Properties().defaultDurability(800).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> SHOCK_AXE_UNPOWERED = ITEMS.register("shock_axe_unpowered", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 8, -3.0F).efficiency(6.0F).disableShield().set(MSItemTypes.AXE_TOOL).set(ItemRightClickEffect.switchTo(MSItems.SHOCK_AXE)), new Item.Properties().defaultDurability(800)));
	//public static final RegistryObject<Item> SHOCK_AXE_UNPOWERED = REGISTER.register("shock_axe_unpowered", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 8, -3.0F).efficiency(6.0F).disableShield().set(MSItemTypes.AXE_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.SHOCK_AXE)), new Item.Properties().defaultDurability(800)));
	public static final RegistryObject<Item> SCRAXE = ITEMS.register("scraxe", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 8, -3.0F).efficiency(7.0F).disableShield().set(MSItemTypes.AXE_TOOL), new Item.Properties().defaultDurability(500).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> LORENTZ_DISTRANSFORMATIONER = ITEMS.register("lorentz_distransformationer", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 6, -3.0F).efficiency(7.0F).disableShield().set(MSItemTypes.AXE_TOOL).add(OnHitEffect.SPACE_TELEPORT), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> PISTON_POWERED_POGO_AXEHAMMER = ITEMS.register("piston_powered_pogo_axehammer", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.POGO_TIER, 6, -3.0F).efficiency(2.0F).disableShield().set(MSItemTypes.AXE_HAMMER_TOOL).set(new FarmineEffect(Integer.MAX_VALUE, 50)).set(PogoEffect.EFFECT_06).add(PogoEffect.EFFECT_06), new Item.Properties().defaultDurability(800).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> RUBY_CROAK = ITEMS.register("ruby_croak", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CORUNDUM_TIER, 8, -3.0F).efficiency(8.0F).disableShield().set(MSItemTypes.AXE_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> HEPHAESTUS_LUMBERJACK = ITEMS.register("hephaestus_lumberjack", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CORUNDUM_TIER, 7, -3.0F).efficiency(9.0F).disableShield().set(MSItemTypes.AXE_TOOL).add(OnHitEffect.setOnFire(30)), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> FISSION_FOCUSED_FAULT_FELLER = ITEMS.register("fission_focused_fault_feller", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 7, -3.0F).efficiency(5.0F).disableShield().set(MSItemTypes.AXE_HAMMER_TOOL).set(new FarmineEffect(Integer.MAX_VALUE, 100)).set(PogoEffect.EFFECT_07).add(PogoEffect.EFFECT_07), new Item.Properties().defaultDurability(2048).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> BISECTOR = ITEMS.register("bisector", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 9, -3.2F).efficiency(5.0F).disableShield().set(MSItemTypes.AXE_TOOL), new Item.Properties().defaultDurability(600).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> FINE_CHINA_AXE = ITEMS.register("fine_china_axe", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 9, -3.2F).efficiency(1.0F).disableShield().set(MSItemTypes.AXE_TOOL), new Item.Properties().defaultDurability(8).tab(MSItemGroup.WEAPONS)));
	
	//misc weapons
	public static final RegistryObject<Item> FLUORITE_OCTET = ITEMS.register("fluorite_octet", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 4, -3.0F).efficiency(1.0F).add(OnHitEffect.RANDOM_DAMAGE), new Item.Properties().tab(MSItemGroup.WEAPONS).defaultDurability(4096).rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> CAT_CLAWS_DRAWN = ITEMS.register("cat_claws_drawn", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 2, -1.5F).efficiency(10.0F).set(MSItemTypes.CLAWS_TOOL).set(ItemRightClickEffect.switchTo(MSItems.CAT_CLAWS_SHEATHED)), new Item.Properties().defaultDurability(500).tab(MSItemGroup.WEAPONS)));
	//public static final RegistryObject<Item> CAT_CLAWS_DRAWN = REGISTER.register("cat_claws_drawn", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 2, -1.5F).efficiency(10.0F).set(MSItemTypes.CLAWS_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.CAT_CLAWS_SHEATHED)), new Item.Properties().defaultDurability(500).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> CAT_CLAWS_SHEATHED = ITEMS.register("cat_claws_sheathed", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, -1, -1.0F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(MSItems.CAT_CLAWS_DRAWN)), new Item.Properties().defaultDurability(500)));
	//public static final RegistryObject<Item> CAT_CLAWS_SHEATHED = REGISTER.register("cat_claws_sheathed", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, -1, -1.0F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.CAT_CLAWS_DRAWN)), new Item.Properties().defaultDurability(500)));
	public static final RegistryObject<Item> SKELETONIZER_DRAWN = ITEMS.register("skeletonizer_drawn", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 5, -1.5F).efficiency(10.0F).set(MSItemTypes.CLAWS_TOOL).set(ItemRightClickEffect.switchTo(MSItems.SKELETONIZER_SHEATHED)), new Item.Properties().defaultDurability(750).tab(MSItemGroup.WEAPONS)));
	//public static final RegistryObject<Item> SKELETONIZER_DRAWN = REGISTER.register("skeletonizer_drawn", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 5, -1.5F).efficiency(10.0F).set(MSItemTypes.CLAWS_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.SKELETONIZER_SHEATHED)), new Item.Properties().defaultDurability(750).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> SKELETONIZER_SHEATHED = ITEMS.register("skeletonizer_sheathed", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, -1, -1.0F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(MSItems.SKELETONIZER_DRAWN)), new Item.Properties().defaultDurability(750)));
	//public static final RegistryObject<Item> SKELETONIZER_SHEATHED = REGISTER.register("skeletonizer_sheathed", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, -1, -1.0F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.SKELETONIZER_DRAWN)), new Item.Properties().defaultDurability(750)));
	public static final RegistryObject<Item> SKELETON_DISPLACER_DRAWN = ITEMS.register("skeleton_displacer_drawn", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 6, -1.5F).efficiency(10.0F).set(MSItemTypes.CLAWS_TOOL).set(ItemRightClickEffect.switchTo(MSItems.SKELETON_DISPLACER_SHEATHED)).add(OnHitEffect.targetSpecificAdditionalDamage(4, () -> EntityType.SKELETON)), new Item.Properties().defaultDurability(1250).tab(MSItemGroup.WEAPONS)));
	//public static final RegistryObject<Item> SKELETON_DISPLACER_DRAWN = REGISTER.register("skeleton_displacer_drawn", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 6, -1.5F).efficiency(10.0F).set(MSItemTypes.CLAWS_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.SKELETON_DISPLACER_SHEATHED)).add(OnHitEffect.targetSpecificAdditionalDamage(4, () -> EntityType.SKELETON)), new Item.Properties().defaultDurability(1250).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> SKELETON_DISPLACER_SHEATHED = ITEMS.register("skeleton_displacer_sheathed", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, -1, -1.0F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(MSItems.SKELETON_DISPLACER_DRAWN)), new Item.Properties().defaultDurability(1250)));
	//public static final RegistryObject<Item> SKELETON_DISPLACER_SHEATHED = REGISTER.register("skeleton_displacer_sheathed", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, -1, -1.0F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.SKELETON_DISPLACER_DRAWN)), new Item.Properties().defaultDurability(1250)));
	public static final RegistryObject<Item> TEARS_OF_THE_ENDERLICH_DRAWN = ITEMS.register("tears_of_the_enderlich_drawn", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CORUNDUM_TIER, 4, -1.5F).efficiency(10.0F).set(MSItemTypes.CLAWS_TOOL).set(ItemRightClickEffect.switchTo(MSItems.TEARS_OF_THE_ENDERLICH_SHEATHED)).add(OnHitEffect.targetSpecificAdditionalDamage(6, () -> MSEntityTypes.LICH)), new Item.Properties().defaultDurability(2000).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	//public static final RegistryObject<Item> TEARS_OF_THE_ENDERLICH_DRAWN = REGISTER.register("tears_of_the_enderlich_drawn", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CORUNDUM_TIER, 4, -1.5F).efficiency(10.0F).set(MSItemTypes.CLAWS_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.TEARS_OF_THE_ENDERLICH_SHEATHED)).add(OnHitEffect.targetSpecificAdditionalDamage(6, () -> MSEntityTypes.LICH)), new Item.Properties().defaultDurability(2000).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> TEARS_OF_THE_ENDERLICH_SHEATHED = ITEMS.register("tears_of_the_enderlich_sheathed", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CORUNDUM_TIER, -4, -1.0F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(MSItems.TEARS_OF_THE_ENDERLICH_DRAWN)), new Item.Properties().defaultDurability(2000).rarity(Rarity.UNCOMMON)));
	//public static final RegistryObject<Item> TEARS_OF_THE_ENDERLICH_SHEATHED = REGISTER.register("tears_of_the_enderlich_sheathed", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CORUNDUM_TIER, -4, -1.0F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.TEARS_OF_THE_ENDERLICH_DRAWN)), new Item.Properties().defaultDurability(2000).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> ACTION_CLAWS_DRAWN = ITEMS.register("action_claws_drawn", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 5, -1.5F).efficiency(10.0F).set(MSItemTypes.CLAWS_TOOL).set(ItemRightClickEffect.switchTo(MSItems.ACTION_CLAWS_SHEATHED)), new Item.Properties().defaultDurability(4096).tab(MSItemGroup.WEAPONS).rarity(Rarity.RARE)));
	//public static final RegistryObject<Item> ACTION_CLAWS_DRAWN = REGISTER.register("action_claws_drawn", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 5, -1.5F).efficiency(10.0F).set(MSItemTypes.CLAWS_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.ACTION_CLAWS_SHEATHED)), new Item.Properties().defaultDurability(4096).tab(MSItemGroup.WEAPONS).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> ACTION_CLAWS_SHEATHED = ITEMS.register("action_claws_sheathed", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, -1, -1.0F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(MSItems.ACTION_CLAWS_DRAWN)), new Item.Properties().defaultDurability(4096).rarity(Rarity.RARE)));
	//public static final RegistryObject<Item> ACTION_CLAWS_SHEATHED = REGISTER.register("action_claws_sheathed", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, -1, -1.0F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.ACTION_CLAWS_DRAWN)), new Item.Properties().defaultDurability(4096).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> LIPSTICK_CHAINSAW = ITEMS.register("lipstick_chainsaw", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 2, -1.5F).efficiency(10.0F).set(MSItemTypes.AXE_TOOL).set(ItemRightClickEffect.switchTo(MSItems.LIPSTICK)), new Item.Properties().defaultDurability(250).tab(MSItemGroup.WEAPONS)));
	//public static final RegistryObject<Item> LIPSTICK_CHAINSAW = REGISTER.register("lipstick_chainsaw", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 2, -1.5F).efficiency(10.0F).set(MSItemTypes.AXE_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.LIPSTICK)), new Item.Properties().defaultDurability(250).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> LIPSTICK = ITEMS.register("lipstick", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, -1, -0.5F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(MSItems.LIPSTICK_CHAINSAW)), new Item.Properties().defaultDurability(250)));
	//public static final RegistryObject<Item> LIPSTICK = REGISTER.register("lipstick", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, -1, -0.5F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.LIPSTICK_CHAINSAW)), new Item.Properties().defaultDurability(250)));
	public static final RegistryObject<Item> THISTLEBLOWER = ITEMS.register("thistleblower", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 4, -1.0F).efficiency(2.0F).set(ItemRightClickEffect.switchTo(MSItems.THISTLEBLOWER_LIPSTICK)), new Item.Properties().defaultDurability(500).tab(MSItemGroup.WEAPONS)));
	//public static final RegistryObject<Item> THISTLEBLOWER = REGISTER.register("thistleblower", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 4, -1.0F).efficiency(2.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.THISTLEBLOWER_LIPSTICK)), new Item.Properties().defaultDurability(500).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> THISTLEBLOWER_LIPSTICK = ITEMS.register("thistleblower_lipstick", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, -1, -0.5F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(MSItems.THISTLEBLOWER)), new Item.Properties().defaultDurability(500)));
	//public static final RegistryObject<Item> THISTLEBLOWER_LIPSTICK = REGISTER.register("thistleblower_lipstick", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, -1, -0.5F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.THISTLEBLOWER)), new Item.Properties().defaultDurability(500)));
	public static final RegistryObject<Item> EMERALD_IMMOLATOR = ITEMS.register("emerald_immolator", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.EMERALD_TIER, 3, -1.5F).efficiency(10.0F).set(MSItemTypes.AXE_TOOL).set(ItemRightClickEffect.switchTo(MSItems.EMERALD_IMMOLATOR_LIPSTICK)).add(OnHitEffect.setOnFire(5)), new Item.Properties().defaultDurability(1024).tab(MSItemGroup.WEAPONS)));
	//public static final RegistryObject<Item> EMERALD_IMMOLATOR = REGISTER.register("emerald_immolator", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.EMERALD_TIER, 3, -1.5F).efficiency(10.0F).set(MSItemTypes.AXE_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.EMERALD_IMMOLATOR_LIPSTICK)).add(OnHitEffect.setOnFire(5)), new Item.Properties().defaultDurability(1024).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> EMERALD_IMMOLATOR_LIPSTICK = ITEMS.register("emerald_immolator_lipstick", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, -1, -0.5F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(MSItems.EMERALD_IMMOLATOR)), new Item.Properties().defaultDurability(1024)));
	//public static final RegistryObject<Item> EMERALD_IMMOLATOR_LIPSTICK = REGISTER.register("emerald_immolator_lipstick", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, -1, -0.5F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.EMERALD_IMMOLATOR)), new Item.Properties().defaultDurability(1024)));
	public static final RegistryObject<Item> OBSIDIATOR = ITEMS.register("obsidiator", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CORUNDUM_TIER, 4, -1.5F).efficiency(10.0F).set(MSItemTypes.AXE_TOOL).set(ItemRightClickEffect.switchTo(MSItems.OBSIDIATOR_LIPSTICK)), new Item.Properties().defaultDurability(2048).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	//public static final RegistryObject<Item> OBSIDIATOR = REGISTER.register("obsidiator", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CORUNDUM_TIER, 4, -1.5F).efficiency(10.0F).set(MSItemTypes.AXE_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.OBSIDIATOR_LIPSTICK)), new Item.Properties().defaultDurability(2048).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> OBSIDIATOR_LIPSTICK = ITEMS.register("obsidiator_lipstick", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, -1, -0.5F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(MSItems.OBSIDIATOR)), new Item.Properties().defaultDurability(2048).rarity(Rarity.UNCOMMON)));
	//public static final RegistryObject<Item> OBSIDIATOR_LIPSTICK = REGISTER.register("obsidiator_lipstick", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, -1, -0.5F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.OBSIDIATOR)), new Item.Properties().defaultDurability(2048).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> FROSTTOOTH = ITEMS.register("frosttooth", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ICE_TIER, 5, -1.5F).efficiency(10.0F).set(MSItemTypes.AXE_TOOL).set(ItemRightClickEffect.switchTo(MSItems.FROSTTOOTH_LIPSTICK)).add(OnHitEffect.ICE_SHARD), new Item.Properties().defaultDurability(1536).tab(MSItemGroup.WEAPONS)));
	//public static final RegistryObject<Item> FROSTTOOTH = REGISTER.register("frosttooth", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ICE_TIER, 5, -1.5F).efficiency(10.0F).set(MSItemTypes.AXE_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.FROSTTOOTH_LIPSTICK)).add(OnHitEffect.ICE_SHARD), new Item.Properties().defaultDurability(1536).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> FROSTTOOTH_LIPSTICK = ITEMS.register("frosttooth_lipstick", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, -1, -0.5F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(MSItems.FROSTTOOTH)), new Item.Properties().defaultDurability(1536)));
	//public static final RegistryObject<Item> FROSTTOOTH_LIPSTICK = REGISTER.register("frosttooth_lipstick", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, -1, -0.5F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.FROSTTOOTH)), new Item.Properties().defaultDurability(1536)));
	public static final RegistryObject<Item> JOUSTING_LANCE = ITEMS.register("jousting_lance", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 4, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> CIGARETTE_LANCE = ITEMS.register("cigarette_lance", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 4, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> LUCERNE_HAMMER = ITEMS.register("lucerne_hammer", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 3, -2.5F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> LUCERNE_HAMMER_OF_UNDYING = ITEMS.register("lucerne_hammer_of_undying", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 4, -2.5F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().defaultDurability(2048).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON))); //Special property in ServerEventHandler
	public static final RegistryObject<Item> OBSIDIAN_AXE_KNIFE = ITEMS.register("obsidian_axe_knife", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.PRISMARINE_TIER, 2, -2.0F).efficiency(1.5F).set(MSItemTypes.MISC_TOOL), new Item.Properties().durability(100).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> FAN = ITEMS.register("fan", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.PAPER_TIER, 1, -1.0F).efficiency(1.5F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.extinguishFire(1)).add(OnHitEffect.enemyKnockback(1.5F)), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> TYPHONIC_TRIVIALIZER = ITEMS.register("typhonic_trivializer", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.DENIZEN_TIER, 2, -1.0F).efficiency(1.5F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.extinguishFire(3)).add(OnHitEffect.BREATH_LEVITATION_AOE).add(OnHitEffect.enemyKnockback(2.0F)), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.RARE)));
	
	//sickles
	public static final RegistryObject<Item> SICKLE = ITEMS.register("sickle", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 2, -2.2F).efficiency(1.5F).disableShield().set(MSItemTypes.SICKLE_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> BISICKLE = ITEMS.register("bisickle", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 2, -2.2F).efficiency(1.5F).disableShield().set(MSItemTypes.SICKLE_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> OW_THE_EDGE = ITEMS.register("ow_the_edge", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.SBAHJ_TIER, 3, -3.0F).efficiency(1.0F).set(MSItemTypes.SICKLE_TOOL).add(OnHitEffect.SORD_DROP), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> HEMEOREAPER = ITEMS.register("hemeoreaper", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 5, -2.2F).efficiency(1.0F).disableShield().set(MSItemTypes.SICKLE_TOOL), new Item.Properties().defaultDurability(550).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> THORNY_SUBJECT = ITEMS.register("thorny_subject", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CACTUS_TIER, 4, -2.2F).efficiency(1.0F).disableShield().set(MSItemTypes.SICKLE_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> HOMES_SMELL_YA_LATER = ITEMS.register("homes_smell_ya_later", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 4, -2.2F).efficiency(3.0F).disableShield().set(MSItemTypes.SICKLE_TOOL), new Item.Properties().defaultDurability(400).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> FUDGESICKLE = ITEMS.register("fudgesickle", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CANDY_TIER, 5, -2.2F).efficiency(1.0F).disableShield().set(MSItemTypes.SICKLE_TOOL).setEating(FinishUseItemEffect.foodEffect(7, 0.6F)), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> REGISICKLE = ITEMS.register("regisickle", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 4, -2.2F).efficiency(4.0F).disableShield().set(MSItemTypes.SICKLE_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> HERETICUS_AURURM = ITEMS.register("hereticus_aururm", () -> new WeaponItem(new WeaponItem.Builder(Tiers.GOLD, 9, -2.2F).efficiency(4.0F).disableShield().set(MSItemTypes.SICKLE_TOOL), new Item.Properties().defaultDurability(1024).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> CLAW_SICKLE = ITEMS.register("claw_sickle", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 8, -2.2F).efficiency(4.0F).disableShield().set(MSItemTypes.SICKLE_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> CLAW_OF_NRUBYIGLITH = ITEMS.register("claw_of_nrubyiglith", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.HORRORTERROR_TIER, 6, -2.2F).efficiency(4.0F).disableShield().set(MSItemTypes.SICKLE_TOOL).add(OnHitEffect.HORRORTERROR), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> CANDY_SICKLE = ITEMS.register("candy_sickle", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CANDY_TIER, 6, -2.2F).efficiency(2.5F).disableShield().set(MSItemTypes.SICKLE_TOOL).add(OnHitEffect.SET_CANDY_DROP_FLAG), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> SCYTHE = ITEMS.register("scythe", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 4, -2.6F).efficiency(1.5F).disableShield().set(MSItemTypes.SICKLE_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> EIGHTBALL_SCYTHE = ITEMS.register("eightball_scythe", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 3, -2.6F).efficiency(1.5F).disableShield().set(MSItemTypes.SICKLE_TOOL).add(OnHitEffect.RANDOM_DAMAGE).set(ItemRightClickEffect.EIGHTBALL), new Item.Properties().defaultDurability(600).tab(MSItemGroup.WEAPONS)));
	
	//clubs
	public static final RegistryObject<Item> DEUCE_CLUB = ITEMS.register("deuce_club", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 3, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> STALE_BAGUETTE = ITEMS.register("stale_baguette", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 3, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SPAWN_BREADCRUMBS).setEating(FinishUseItemEffect.SPAWN_BREADCRUMBS, FinishUseItemEffect.foodEffect(3, 0.2F)), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> GLUB_CLUB = ITEMS.register("glub_club", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.MEAT_TIER, 4, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.playSound(() -> SoundEvents.GUARDIAN_FLOP)), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> NIGHT_CLUB = ITEMS.register("night_club", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 1, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> NIGHTSTICK = ITEMS.register("nightstick", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 2, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().defaultDurability(2500).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> RED_EYES = ITEMS.register("red_eyes", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 6, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.enemyPotionEffect(() -> new MobEffectInstance(MobEffects.POISON, 140, 0))), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> PRISMARINE_BASHER = ITEMS.register("prismarine_basher", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.PRISMARINE_TIER, 3, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> CLUB_ZERO = ITEMS.register("club_zero", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ICE_TIER, 5, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.ICE_SHARD), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> POGO_CLUB = ITEMS.register("pogo_club", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.POGO_TIER, 4, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).set(PogoEffect.EFFECT_05).add(PogoEffect.EFFECT_05), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> BARBER_BASHER = ITEMS.register("barber_basher", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 3, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().defaultDurability(350).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> METAL_BAT = ITEMS.register("metal_bat", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 4, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> CLOWN_CLUB = ITEMS.register("clown_club", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 5, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.RAGE_STRENGTH, OnHitEffect.playSound(() -> MSSoundEvents.ITEM_HORN_USE, 1.5F, 1)), new Item.Properties().defaultDurability(2048).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> MACE = ITEMS.register("mace", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 5, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().defaultDurability(500).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> M_ACE = ITEMS.register("m_ace", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 6, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().defaultDurability(750).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> DESOLATOR_MACE = ITEMS.register("desolator_mace", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 1, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.armorBypassingDamageMod(4, EnumAspect.VOID)), new Item.Properties().defaultDurability(2048).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> BLAZING_GLORY = ITEMS.register("blazing_glory", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 7, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.setOnFire(35)), new Item.Properties().defaultDurability(750).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> SPIKED_CLUB = ITEMS.register("spiked_club", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 5, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().defaultDurability(100).tab(MSItemGroup.WEAPONS)));
	
	public static final RegistryObject<Item> HORSE_HITCHER = ITEMS.register("horse_hitcher", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 7, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(MSItems.ACE_OF_SPADES)), new Item.Properties().defaultDurability(500).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	//public static final RegistryObject<Item> HORSE_HITCHER = REGISTER.register("horse_hitcher", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 7, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.ACE_OF_SPADES)), new Item.Properties().defaultDurability(500).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> ACE_OF_SPADES = ITEMS.register("ace_of_spades", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.PAPER_TIER, 0, -1.8F).efficiency(0.0F).set(ItemRightClickEffect.switchTo(MSItems.HORSE_HITCHER)), new Item.Properties().defaultDurability(500)));
	//public static final RegistryObject<Item> ACE_OF_SPADES = REGISTER.register("ace_of_spades", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.PAPER_TIER, 0, -1.8F).efficiency(0.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.HORSE_HITCHER)), new Item.Properties().defaultDurability(500)));
	public static final RegistryObject<Item> CLUB_OF_FELONY = ITEMS.register("club_of_felony", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 7, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(MSItems.ACE_OF_CLUBS)), new Item.Properties().defaultDurability(500).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	//public static final RegistryObject<Item> CLUB_OF_FELONY = REGISTER.register("club_of_felony", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 7, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.ACE_OF_CLUBS)), new Item.Properties().defaultDurability(500).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> ACE_OF_CLUBS = ITEMS.register("ace_of_clubs", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.PAPER_TIER, 0, -1.8F).efficiency(0.0F).set(ItemRightClickEffect.switchTo(MSItems.CLUB_OF_FELONY)), new Item.Properties().defaultDurability(500)));
	//public static final RegistryObject<Item> ACE_OF_CLUBS = REGISTER.register("ace_of_clubs", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.PAPER_TIER, 0, -1.8F).efficiency(0.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.CLUB_OF_FELONY)), new Item.Properties().defaultDurability(500)));
	public static final RegistryObject<Item> CUESTICK = ITEMS.register("cuestick", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 5, -2.0F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(MSItems.ACE_OF_DIAMONDS)), new Item.Properties().defaultDurability(500).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	//public static final RegistryObject<Item> CUESTICK = REGISTER.register("cuestick", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 5, -2.0F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.ACE_OF_DIAMONDS)), new Item.Properties().defaultDurability(500).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> ACE_OF_DIAMONDS = ITEMS.register("ace_of_diamonds", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.PAPER_TIER, 0, -1.8F).efficiency(0.0F).set(ItemRightClickEffect.switchTo(MSItems.CUESTICK)), new Item.Properties().defaultDurability(500)));
	//public static final RegistryObject<Item> ACE_OF_DIAMONDS = REGISTER.register("ace_of_diamonds", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.PAPER_TIER, 0, -1.8F).efficiency(0.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.CUESTICK)), new Item.Properties().defaultDurability(500)));
	public static final RegistryObject<Item> ACE_OF_HEARTS = ITEMS.register("ace_of_hearts", () -> new Item(new Item.Properties().defaultDurability(500)));
	public static final RegistryObject<Item> WHITE_KINGS_SCEPTER = ITEMS.register("white_kings_scepter", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 8, -2.8F).efficiency(4.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.summonFireball()), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> BLACK_KINGS_SCEPTER = ITEMS.register("black_kings_scepter", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 8, -2.8F).efficiency(4.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.summonFireball()), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.EPIC)));
	
	//canes
	public static final RegistryObject<Item> CANE = ITEMS.register("cane", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 2, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().defaultDurability(100).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> VAUDEVILLE_HOOK = ITEMS.register("vaudeville_hook", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 2, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().defaultDurability(150).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> BEAR_POKING_STICK = ITEMS.register("bear_poking_stick", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 3, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.notAtPlayer(OnHitEffect.enemyPotionEffect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 140, 1)))), new Item.Properties().defaultDurability(150).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> CROWBAR = ITEMS.register("crowbar", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 6, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.playSound(() -> SoundEvents.ANVIL_PLACE)), new Item.Properties().defaultDurability(-1).tab(MSItemGroup.WEAPONS).rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> UMBRELLA = ITEMS.register("umbrella", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 2, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(InventoryTickEffect.BREATH_SLOW_FALLING), new Item.Properties().defaultDurability(350).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> UPPER_CRUST_CRUST_CANE = ITEMS.register("upper_crust_crust_cane", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 3, -2.0F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SPAWN_BREADCRUMBS).setEating(FinishUseItemEffect.SPAWN_BREADCRUMBS, FinishUseItemEffect.foodEffect(4, 0.5F)), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> IRON_CANE = ITEMS.register("iron_cane", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 2, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> ZEPHYR_CANE = ITEMS.register("zephyr_cane", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 3, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(PropelEffect.BREATH_PROPEL), new Item.Properties().tab(MSItemGroup.WEAPONS).defaultDurability(2048).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> SPEAR_CANE = ITEMS.register("spear_cane", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 3, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> PARADISES_PORTABELLO = ITEMS.register("paradises_portabello", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 3, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> REGI_CANE = ITEMS.register("regi_cane", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 4, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> POGO_CANE = ITEMS.register("pogo_cane", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.POGO_TIER, 2, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(PogoEffect.EFFECT_06).add(PogoEffect.EFFECT_06), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> CANDY_CANE = ITEMS.register("candy_cane", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CANDY_TIER, 3, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SET_CANDY_DROP_FLAG).setEating(FinishUseItemEffect.foodEffect(2, 0.3F), FinishUseItemEffect.SHARPEN_CANDY_CANE), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> SHARP_CANDY_CANE = ITEMS.register("sharp_candy_cane", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CANDY_TIER, 5, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SET_CANDY_DROP_FLAG), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> PRIM_AND_PROPER_WALKING_POLE = ITEMS.register("prim_and_proper_walking_pole", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 3, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> DRAGON_CANE = ITEMS.register("dragon_cane", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 7, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(MSItems.DRAGON_CANE_UNSHEATHED)), new Item.Properties().defaultDurability(2048).tab(MSItemGroup.WEAPONS).rarity(Rarity.RARE)));
	//public static final RegistryObject<Item> DRAGON_CANE = REGISTER.register("dragon_cane", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 7, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.DRAGON_CANE_UNSHEATHED)), new Item.Properties().defaultDurability(2048).tab(MSItemGroup.WEAPONS).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> DRAGON_CANE_UNSHEATHED = ITEMS.register("dragon_cane_unsheathed", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 6, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(MSItems.DRAGON_CANE)), new Item.Properties().defaultDurability(2048).rarity(Rarity.RARE)));
	//public static final RegistryObject<Item> DRAGON_CANE_UNSHEATHED = REGISTER.register("dragon_cane_unsheathed", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 6, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.DRAGON_CANE)), new Item.Properties().defaultDurability(2048).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> CHANCEWYRMS_EXTRA_FORTUNATE_STABBING_IMPLEMENT = ITEMS.register("chancewyrms_extra_fortunate_stabbing_implement", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 6, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(MSItems.CHANCEWYRMS_EXTRA_FORTUNATE_STABBING_IMPLEMENT_UNSHEATHED)), new Item.Properties().defaultDurability(4096).tab(MSItemGroup.WEAPONS).rarity(Rarity.RARE)));
	//public static final RegistryObject<Item> CHANCEWYRMS_EXTRA_FORTUNATE_STABBING_IMPLEMENT = REGISTER.register("chancewyrms_extra_fortunate_stabbing_implement", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 6, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.CHANCEWYRMS_EXTRA_FORTUNATE_STABBING_IMPLEMENT_UNSHEATHED)), new Item.Properties().defaultDurability(4096).tab(MSItemGroup.WEAPONS).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> CHANCEWYRMS_EXTRA_FORTUNATE_STABBING_IMPLEMENT_UNSHEATHED = ITEMS.register("chancewyrms_extra_fortunate_stabbing_implement_unsheathed", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 5, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(MSItems.CHANCEWYRMS_EXTRA_FORTUNATE_STABBING_IMPLEMENT)).add(OnHitEffect.RANDOM_DAMAGE), new Item.Properties().defaultDurability(4096).rarity(Rarity.RARE)));
	//public static final RegistryObject<Item> CHANCEWYRMS_EXTRA_FORTUNATE_STABBING_IMPLEMENT_UNSHEATHED = REGISTER.register("chancewyrms_extra_fortunate_stabbing_implement_unsheathed", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 5, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.CHANCEWYRMS_EXTRA_FORTUNATE_STABBING_IMPLEMENT)).add(OnHitEffect.RANDOM_DAMAGE), new Item.Properties().defaultDurability(4096).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> LESS_PROPER_WALKING_STICK = ITEMS.register("less_proper_walking_stick", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 3, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(MSItems.LESS_PROPER_WALKING_STICK_SHEATHED)), new Item.Properties().defaultDurability(600).tab(MSItemGroup.WEAPONS)));
	//public static final RegistryObject<Item> LESS_PROPER_WALKING_STICK = REGISTER.register("less_proper_walking_stick", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 3, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.LESS_PROPER_WALKING_STICK_SHEATHED)), new Item.Properties().defaultDurability(600).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> LESS_PROPER_WALKING_STICK_SHEATHED = ITEMS.register("less_proper_walking_stick_sheathed", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 3, -2.0F).efficiency(1.0F).set(ItemRightClickEffect.switchTo(MSItems.LESS_PROPER_WALKING_STICK)), new Item.Properties().defaultDurability(600)));
	//public static final RegistryObject<Item> LESS_PROPER_WALKING_STICK_SHEATHED = REGISTER.register("less_proper_walking_stick_sheathed", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 3, -2.0F).efficiency(1.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.LESS_PROPER_WALKING_STICK)), new Item.Properties().defaultDurability(600)));
	public static final RegistryObject<Item> ROCKEFELLERS_WALKING_BLADECANE = ITEMS.register("rockefellers_walking_bladecane", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 5, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(MSItems.ROCKEFELLERS_WALKING_BLADECANE_SHEATHED)), new Item.Properties().defaultDurability(800).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	//public static final RegistryObject<Item> ROCKEFELLERS_WALKING_BLADECANE = REGISTER.register("rockefellers_walking_bladecane", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 5, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.ROCKEFELLERS_WALKING_BLADECANE_SHEATHED)), new Item.Properties().defaultDurability(800).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> ROCKEFELLERS_WALKING_BLADECANE_SHEATHED = ITEMS.register("rockefellers_walking_bladecane_sheathed", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 5, -2.0F).efficiency(1.0F).set(ItemRightClickEffect.switchTo(MSItems.ROCKEFELLERS_WALKING_BLADECANE)), new Item.Properties().defaultDurability(800).rarity(Rarity.UNCOMMON)));
	//public static final RegistryObject<Item> ROCKEFELLERS_WALKING_BLADECANE_SHEATHED = REGISTER.register("rockefellers_walking_bladecane_sheathed", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 5, -2.0F).efficiency(1.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.ROCKEFELLERS_WALKING_BLADECANE)), new Item.Properties().defaultDurability(800).rarity(Rarity.UNCOMMON)));
	
	//Spoons/forks
	public static final RegistryObject<Item> WOODEN_SPOON = ITEMS.register("wooden_spoon", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 2, -2.4F).efficiency(1.0F).set(MSItemTypes.SHOVEL_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> SILVER_SPOON = ITEMS.register("silver_spoon", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 1, -2.4F).efficiency(1.0F).set(MSItemTypes.SHOVEL_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> MELONBALLER = ITEMS.register("melonballer", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 2, -2.4F).efficiency(1.0F).set(MSItemTypes.SHOVEL_TOOL).set(RightClickBlockEffect.scoopBlock(() -> Blocks.MELON)), new Item.Properties().tab(MSItemGroup.WEAPONS).defaultDurability(500)));
	public static final RegistryObject<Item> SIGHTSEEKER = ITEMS.register("sightseeker", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 6, -2.4F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> TERRAIN_FLATENATOR = ITEMS.register("terrain_flatenator", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 3, -2.4F).efficiency(10.0F).set(MSItemTypes.SHOVEL_TOOL).set(new FarmineEffect(Integer.MAX_VALUE, 50)), new Item.Properties().defaultDurability(1024).tab(MSItemGroup.WEAPONS))); //TODO fix inability to use for terrain flattenation
	public static final RegistryObject<Item> NOSFERATU_SPOON = ITEMS.register("nosferatu_spoon", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 5, -2.4F).efficiency(1.0F).set(MSItemTypes.SHOVEL_TOOL).add(OnHitEffect.LIFE_SATURATION), new Item.Properties().defaultDurability(2048).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> CROCKER_SPOON = ITEMS.register("crocker_spoon", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 5, -2.4F).efficiency(1.0F).set(MSItemTypes.SHOVEL_TOOL).set(ItemRightClickEffect.switchTo(MSItems.CROCKER_FORK)), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	//public static final RegistryObject<Item> CROCKER_SPOON = REGISTER.register("crocker_spoon", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 5, -2.4F).efficiency(1.0F).set(MSItemTypes.SHOVEL_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.CROCKER_FORK)), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> CROCKER_FORK = ITEMS.register("crocker_fork", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 6, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(MSItems.CROCKER_SPOON)), new Item.Properties().rarity(Rarity.UNCOMMON)));
	//public static final RegistryObject<Item> CROCKER_FORK = REGISTER.register("crocker_fork", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 6, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.CROCKER_SPOON)), new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> SKAIA_FORK = ITEMS.register("skaia_fork", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 9, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> FORK = ITEMS.register("fork", () -> new WeaponItem(new WeaponItem.Builder(Tiers.STONE, 3, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> CANDY_FORK = ITEMS.register("candy_fork", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CANDY_TIER, 5, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SET_CANDY_DROP_FLAG), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> TUNING_FORK = ITEMS.register("tuning_fork", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 3, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.playSound(() -> SoundEvents.NOTE_BLOCK_CHIME)), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> ELECTRIC_FORK = ITEMS.register("electric_fork", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 6, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.DROP_FOE_ITEM).add(InventoryTickEffect.DROP_WHEN_IN_WATER).add(OnHitEffect.playSound(() -> MSSoundEvents.EVENT_ELECTRIC_SHOCK, 0.6F, 1.0F)), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> EATING_FORK_GEM = ITEMS.register("eating_fork_gem", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 3, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> DEVIL_FORK = ITEMS.register("devil_fork", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.URANIUM_TIER, 6, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.setOnFire(35)), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> SPORK = ITEMS.register("spork", () -> new WeaponItem(new WeaponItem.Builder(Tiers.STONE, 4, -2.5F).efficiency(1.0F).set(MSItemTypes.SHOVEL_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> GOLDEN_SPORK = ITEMS.register("golden_spork", () -> new WeaponItem(new WeaponItem.Builder(Tiers.GOLD, 5, -2.5F).efficiency(1.0F).set(MSItemTypes.SHOVEL_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> BIDENT = ITEMS.register("bident", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 7, -2.9F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> EDISONS_FURY = ITEMS.register("edisons_fury", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 7, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(MSItems.EDISONS_SERENITY)).add(OnHitEffect.DROP_FOE_ITEM).add(InventoryTickEffect.DROP_WHEN_IN_WATER).add(OnHitEffect.playSound(() -> MSSoundEvents.EVENT_ELECTRIC_SHOCK, 0.6F, 1.0F)), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	//public static final RegistryObject<Item> EDISONS_FURY = REGISTER.register("edisons_fury", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 7, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(() -> EDISONS_SERENITY)).add(OnHitEffect.DROP_FOE_ITEM).add(InventoryTickEffect.DROP_WHEN_IN_WATER).add(OnHitEffect.playSound(() -> MSSoundEvents.EVENT_ELECTRIC_SHOCK, 0.6F, 1.0F)), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> EDISONS_SERENITY = ITEMS.register("edisons_serenity", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 7, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(MSItems.EDISONS_FURY)), new Item.Properties()));
	//public static final RegistryObject<Item> EDISONS_SERENITY = REGISTER.register("edisons_serenity", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 7, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(() -> EDISONS_FURY)), new Item.Properties()));
	
	//needles/wands
	public static final RegistryObject<Item> POINTY_STICK = ITEMS.register("pointy_stick", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 0, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> KNITTING_NEEDLE = ITEMS.register("knitting_needle", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, -1, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> NEEDLE_WAND = ITEMS.register("needle_wand", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 0, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(MagicAttackRightClickEffect.STANDARD_MAGIC), new Item.Properties().defaultDurability(1024).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> ARTIFUCKER = ITEMS.register("artifucker", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.SBAHJ_TIER, 1, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(MagicAttackRightClickEffect.SBAHJ_AIMBOT_MAGIC), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> POINTER_WAND = ITEMS.register("pointer_wand", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, -1, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(MagicAttackRightClickEffect.AIMBOT_MAGIC), new Item.Properties().defaultDurability(512).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> POOL_CUE_WAND = ITEMS.register("pool_cue_wand", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, -1, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(MagicAttackRightClickEffect.POOL_CUE_MAGIC), new Item.Properties().defaultDurability(1250).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> THORN_OF_OGLOGOTH = ITEMS.register("thorn_of_oglogoth", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.HORRORTERROR_TIER, 0, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.HORRORTERROR).set(MagicAttackRightClickEffect.HORRORTERROR_MAGIC), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> THISTLE_OF_ZILLYWICH = ITEMS.register("thistle_of_zillywich", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ZILLY_TIER, 0, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(MagicAttackRightClickEffect.ZILLY_MAGIC), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> QUILL_OF_ECHIDNA = ITEMS.register("quill_of_echidna", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.DENIZEN_TIER, -1, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(MagicAttackRightClickEffect.ECHIDNA_MAGIC), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.EPIC)));
	/**/
	
	//projectiles
	public static final RegistryObject<Item> SBAHJARANG = ITEMS.register("sbahjarang", () -> new ConsumableProjectileWeaponItem(new Item.Properties().tab(MSItemGroup.WEAPONS), 0.5F, 20.0F, 1));
	public static final RegistryObject<Item> SHURIKEN = ITEMS.register("shuriken", () -> new ConsumableProjectileWeaponItem(new Item.Properties().tab(MSItemGroup.WEAPONS), 1.0F, 2.8F, 2));
	public static final RegistryObject<Item> CLUBS_SUITARANG = ITEMS.register("clubs_suitarang", () -> new ConsumableProjectileWeaponItem(new Item.Properties().tab(MSItemGroup.WEAPONS), 1.5F, 2.4F, 3));
	public static final RegistryObject<Item> DIAMONDS_SUITARANG = ITEMS.register("diamonds_suitarang", () -> new ConsumableProjectileWeaponItem(new Item.Properties().tab(MSItemGroup.WEAPONS), 1.5F, 2.4F, 3));
	public static final RegistryObject<Item> HEARTS_SUITARANG = ITEMS.register("hearts_suitarang", () -> new ConsumableProjectileWeaponItem(new Item.Properties().tab(MSItemGroup.WEAPONS), 1.5F, 2.4F, 3));
	public static final RegistryObject<Item> SPADES_SUITARANG = ITEMS.register("spades_suitarang", () -> new ConsumableProjectileWeaponItem(new Item.Properties().tab(MSItemGroup.WEAPONS), 1.5F, 2.4F, 3));
	public static final RegistryObject<Item> CHAKRAM = ITEMS.register("chakram", () -> new ReturningProjectileWeaponItem(new Item.Properties().tab(MSItemGroup.WEAPONS).durability(250), 1.3F, 1.0F,5, 30));
	public static final RegistryObject<Item> UMBRAL_INFILTRATOR = ITEMS.register("umbral_infiltrator", () -> new ReturningProjectileWeaponItem(new Item.Properties().tab(MSItemGroup.WEAPONS).durability(2048), 1.5F, 0.6F,12, 20));
	public static final RegistryObject<Item> SORCERERS_PINBALL = ITEMS.register("sorcerers_pinball", () -> new BouncingProjectileWeaponItem(new Item.Properties().tab(MSItemGroup.WEAPONS).durability(250), 1.5F, 1.0F, 5, 20));
	
	//Material tools
	public static final RegistryObject<Item> EMERALD_SWORD = ITEMS.register("emerald_sword", () -> new SwordItem(MSItemTypes.EMERALD_TIER, 3, -2.4F, new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> EMERALD_AXE = ITEMS.register("emerald_axe", () -> new AxeItem(MSItemTypes.EMERALD_TIER, 5, -3.0F, new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> EMERALD_PICKAXE = ITEMS.register("emerald_pickaxe", () -> new PickaxeItem(MSItemTypes.EMERALD_TIER, 1, -2.8F, new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> EMERALD_SHOVEL = ITEMS.register("emerald_shovel", () -> new ShovelItem(MSItemTypes.EMERALD_TIER, 1.5F, -3.0F, new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> EMERALD_HOE = ITEMS.register("emerald_hoe", () -> new HoeItem(MSItemTypes.EMERALD_TIER, -3, 0.0F, new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> MINE_AND_GRIST = ITEMS.register("mine_and_grist", () -> new PickaxeItem(Tiers.DIAMOND, 1, -2.8F, new Item.Properties()/*.tab(MSItemGroup.WEAPONS)*/));
	
	//Armor
	public static final RegistryObject<Item> PRISMARINE_HELMET = ITEMS.register("prismarine_helmet", () -> new ArmorItem(MSItemTypes.PRISMARINE_ARMOR, EquipmentSlot.HEAD, new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> PRISMARINE_CHESTPLATE = ITEMS.register("prismarine_chestplate", () -> new ArmorItem(MSItemTypes.PRISMARINE_ARMOR, EquipmentSlot.CHEST, new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> PRISMARINE_LEGGINGS = ITEMS.register("prismarine_leggings", () -> new ArmorItem(MSItemTypes.PRISMARINE_ARMOR, EquipmentSlot.LEGS, new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> PRISMARINE_BOOTS = ITEMS.register("prismarine_boots", () -> new ArmorItem(MSItemTypes.PRISMARINE_ARMOR, EquipmentSlot.FEET, new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> IRON_LASS_GLASSES = ITEMS.register("iron_lass_glasses", () -> new ArmorItem(MSItemTypes.IRON_LASS_ARMOR, EquipmentSlot.HEAD, new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> IRON_LASS_CHESTPLATE = ITEMS.register("iron_lass_chestplate", () -> new ArmorItem(MSItemTypes.IRON_LASS_ARMOR, EquipmentSlot.CHEST, new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> IRON_LASS_SKIRT = ITEMS.register("iron_lass_skirt", () -> new ArmorItem(MSItemTypes.IRON_LASS_ARMOR, EquipmentSlot.LEGS, new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> IRON_LASS_SHOES = ITEMS.register("iron_lass_shoes", () -> new ArmorItem(MSItemTypes.IRON_LASS_ARMOR, EquipmentSlot.FEET, new Item.Properties().tab(MSItemGroup.WEAPONS)));
	
	public static final RegistryObject<MSArmorItem> PROSPIT_CIRCLET = ITEMS.register("prospit_circlet", () -> new MSArmorItem(MSItemTypes.DREAM_PAJAMAS, EquipmentSlot.HEAD, new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<MSArmorItem> PROSPIT_SHIRT = ITEMS.register("prospit_shirt", () -> new MSArmorItem(MSItemTypes.DREAM_PAJAMAS, EquipmentSlot.CHEST, new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<MSArmorItem> PROSPIT_PANTS = ITEMS.register("prospit_pants", () -> new MSArmorItem(MSItemTypes.DREAM_PAJAMAS, EquipmentSlot.LEGS, new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<MSArmorItem> PROSPIT_SHOES = ITEMS.register("prospit_shoes", () -> new MSArmorItem(MSItemTypes.DREAM_PAJAMAS, EquipmentSlot.FEET, new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<MSArmorItem> DERSE_CIRCLET = ITEMS.register("derse_circlet", () -> new MSArmorItem(MSItemTypes.DREAM_PAJAMAS, EquipmentSlot.HEAD, new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<MSArmorItem> DERSE_SHIRT = ITEMS.register("derse_shirt", () -> new MSArmorItem(MSItemTypes.DREAM_PAJAMAS, EquipmentSlot.CHEST, new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<MSArmorItem> DERSE_PANTS = ITEMS.register("derse_pants", () -> new MSArmorItem(MSItemTypes.DREAM_PAJAMAS, EquipmentSlot.LEGS, new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<MSArmorItem> DERSE_SHOES = ITEMS.register("derse_shoes", () -> new MSArmorItem(MSItemTypes.DREAM_PAJAMAS, EquipmentSlot.FEET, new Item.Properties().tab(MSItemGroup.WEAPONS)));
	
	//Core Items
	public static final RegistryObject<Item> BOONDOLLARS = ITEMS.register("boondollars", () -> new BoondollarsItem(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> RAW_CRUXITE = ITEMS.register("raw_cruxite", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> RAW_URANIUM = ITEMS.register("raw_uranium", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> ENERGY_CORE = ITEMS.register("energy_core", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN)));
	//have to fix Cruxite artifact classes
	public static final RegistryObject<Item> CRUXITE_APPLE = ITEMS.register("cruxite_apple", () -> new CruxiteAppleItem(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> CRUXITE_POTION = ITEMS.register("cruxite_potion", () -> new CruxitePotionItem(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> SBURB_CODE = ITEMS.register("sburb_code", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> COMPUTER_PARTS = ITEMS.register("computer_parts", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> BLANK_DISK = ITEMS.register("blank_disk", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> CLIENT_DISK = ITEMS.register("client_disk", () -> new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> SERVER_DISK = ITEMS.register("server_disk", () -> new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> CAPTCHA_CARD = ITEMS.register("captcha_card", () -> new CaptchaCardItem(new Item.Properties().tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> STACK_MODUS_CARD = ITEMS.register("stack_modus_card", () -> new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> QUEUE_MODUS_CARD = ITEMS.register("queue_modus_card", () -> new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> QUEUESTACK_MODUS_CARD = ITEMS.register("queuestack_modus_card", () -> new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> TREE_MODUS_CARD = ITEMS.register("tree_modus_card", () -> new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> HASHMAP_MODUS_CARD = ITEMS.register("hashmap_modus_card", () -> new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> SET_MODUS_CARD = ITEMS.register("set_modus_card", () -> new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> SHUNT = ITEMS.register("shunt", () -> new ShuntItem(new Item.Properties().stacksTo(1)));
	
	//Food
	public static final RegistryObject<Item> PHLEGM_GUSHERS = ITEMS.register("phlegm_gushers", () -> new HealingFoodItem(4F, new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.PHLEGM_GUSHERS)));
	public static final RegistryObject<Item> SORROW_GUSHERS = ITEMS.register("sorrow_gushers", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.SORROW_GUSHERS)));
	
	public static final RegistryObject<Item> BUG_ON_A_STICK = ITEMS.register("bug_on_a_stick", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.BUG_ON_A_STICK)));
	public static final RegistryObject<Item> CHOCOLATE_BEETLE = ITEMS.register("chocolate_beetle", () -> new Item(new Item.Properties().tab(MSItemGroup.LANDS).food(MSFoods.CHOCOLATE_BEETLE)));
	public static final RegistryObject<Item> CONE_OF_FLIES = ITEMS.register("cone_of_flies", () -> new Item(new Item.Properties().tab(MSItemGroup.LANDS).food(MSFoods.CONE_OF_FLIES)));
	public static final RegistryObject<Item> GRASSHOPPER = ITEMS.register("grasshopper", () -> new Item(new Item.Properties().tab(MSItemGroup.LANDS).food(MSFoods.GRASSHOPPER)));
	public static final RegistryObject<Item> CICADA = ITEMS.register("cicada", () -> new Item(new Item.Properties().tab(MSItemGroup.LANDS).food(MSFoods.CICADA)));
	public static final RegistryObject<Item> JAR_OF_BUGS = ITEMS.register("jar_of_bugs", () -> new Item(new Item.Properties().tab(MSItemGroup.LANDS).food(MSFoods.JAR_OF_BUGS)));
	public static final RegistryObject<Item> BUG_MAC = ITEMS.register("bug_mac", () -> new Item(new Item.Properties().tab(MSItemGroup.LANDS).food(MSFoods.BUG_MAC)));
	public static final RegistryObject<Item> ONION = ITEMS.register("onion", () -> new Item(new Item.Properties().tab(MSItemGroup.LANDS).food(MSFoods.ONION)));
	public static final RegistryObject<Item> SALAD = ITEMS.register("salad", () -> new BowlFoodItem(new Item.Properties().tab(MSItemGroup.LANDS).food(MSFoods.SALAD).stacksTo(1)));
	public static final RegistryObject<Item> DESERT_FRUIT = ITEMS.register("desert_fruit", () -> new Item(new Item.Properties().tab(MSItemGroup.LANDS).food(MSFoods.DESERT_FRUIT)));
	public static final RegistryObject<Item> ROCK_COOKIE = ITEMS.register("rock_cookie", () -> new Item(new Item.Properties().tab(MSItemGroup.LANDS))); //Not actually food, but let's pretend it is
	public static final RegistryObject<Item> WOODEN_CARROT = ITEMS.register("wooden_carrot", () -> new Item(new Item.Properties().tab(MSItemGroup.LANDS).food(MSFoods.WOODEN_CARROT)));
	public static final RegistryObject<Item> FUNGAL_SPORE = ITEMS.register("fungal_spore", () -> new Item(new Item.Properties().tab(MSItemGroup.LANDS).food(MSFoods.FUNGAL_SPORE)));
	public static final RegistryObject<Item> SPOREO = ITEMS.register("sporeo", () -> new Item(new Item.Properties().tab(MSItemGroup.LANDS).food(MSFoods.SPOREO)));
	public static final RegistryObject<Item> MOREL_MUSHROOM = ITEMS.register("morel_mushroom", () -> new Item(new Item.Properties().tab(MSItemGroup.LANDS).food(MSFoods.MOREL_MUSHROOM)));
	public static final RegistryObject<Item> SUSHROOM = ITEMS.register("sushroom", () -> new Item(new Item.Properties().tab(MSItemGroup.LANDS)));
	public static final RegistryObject<Item> FRENCH_FRY = ITEMS.register("french_fry", () -> new Item(new Item.Properties().tab(MSItemGroup.LANDS).food(MSFoods.FRENCH_FRY)));
	public static final RegistryObject<Item> STRAWBERRY_CHUNK = ITEMS.register("strawberry_chunk", () -> new ItemNameBlockItem(STRAWBERRY_STEM.get(), new Item.Properties().tab(MSItemGroup.LANDS).food(MSFoods.STRAWBERRY_CHUNK)));
	public static final RegistryObject<Item> FOOD_CAN = ITEMS.register("food_can", () -> new Item(new Item.Properties().stacksTo(16).tab(MSItemGroup.MAIN).food(MSFoods.FOOD_CAN)));
	
	public static final RegistryObject<Item> CANDY_CORN = ITEMS.register("candy_corn", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.CANDY_CORN)));
	public static final RegistryObject<Item> TUIX_BAR = ITEMS.register("tuix_bar", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.TUIX_BAR)));
	public static final RegistryObject<Item> BUILD_GUSHERS = ITEMS.register("build_gushers", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.BUILD_GUSHERS)));
	public static final RegistryObject<Item> AMBER_GUMMY_WORM = ITEMS.register("amber_gummy_worm", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.AMBER_GUMMY_WORM)));
	public static final RegistryObject<Item> CAULK_PRETZEL = ITEMS.register("caulk_pretzel", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.CAULK_PRETZEL)));
	public static final RegistryObject<Item> CHALK_CANDY_CIGARETTE = ITEMS.register("chalk_candy_cigarette", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.CHALK_CANDY_CIGARETTE)));
	public static final RegistryObject<Item> IODINE_LICORICE = ITEMS.register("iodine_licorice", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.IODINE_LICORICE)));
	public static final RegistryObject<Item> SHALE_PEEP = ITEMS.register("shale_peep", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.SHALE_PEEP)));
	public static final RegistryObject<Item> TAR_LICORICE = ITEMS.register("tar_licorice", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.TAR_LICORICE)));
	public static final RegistryObject<Item> COBALT_GUM = ITEMS.register("cobalt_gum", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.COBALT_GUM)));
	public static final RegistryObject<Item> MARBLE_JAWBREAKER = ITEMS.register("marble_jawbreaker", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.MARBLE_JAWBREAKER)));
	public static final RegistryObject<Item> MERCURY_SIXLETS = ITEMS.register("mercury_sixlets", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.MERCURY_SIXLETS)));
	public static final RegistryObject<Item> QUARTZ_JELLY_BEAN = ITEMS.register("quartz_jelly_bean", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.QUARTZ_JELLY_BEAN)));
	public static final RegistryObject<Item> SULFUR_CANDY_APPLE = ITEMS.register("sulfur_candy_apple", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.SULFUR_CANDY_APPLE)));
	public static final RegistryObject<Item> AMETHYST_HARD_CANDY = ITEMS.register("amethyst_hard_candy", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.AMETHYST_HARD_CANDY)));
	public static final RegistryObject<Item> GARNET_TWIX = ITEMS.register("garnet_twix", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.GARNET_TWIX)));
	public static final RegistryObject<Item> RUBY_LOLLIPOP = ITEMS.register("ruby_lollipop", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.RUBY_LOLLIPOP)));
	public static final RegistryObject<Item> RUST_GUMMY_EYE = ITEMS.register("rust_gummy_eye", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.RUST_GUMMY_EYE)));
	public static final RegistryObject<Item> DIAMOND_MINT = ITEMS.register("diamond_mint", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.DIAMOND_MINT)));
	public static final RegistryObject<Item> GOLD_CANDY_RIBBON = ITEMS.register("gold_candy_ribbon", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.GOLD_CANDY_RIBBON)));
	public static final RegistryObject<Item> URANIUM_GUMMY_BEAR = ITEMS.register("uranium_gummy_bear", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.URANIUM_GUMMY_BEAR)));
	public static final RegistryObject<Item> ARTIFACT_WARHEAD = ITEMS.register("artifact_warhead", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.ARTIFACT_WARHEAD).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> ZILLIUM_SKITTLES = ITEMS.register("zillium_skittles", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.ZILLIUM_SKITTLES).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> APPLE_JUICE = ITEMS.register("apple_juice", () -> new DrinkableItem(new Item.Properties().stacksTo(16).tab(MSItemGroup.MAIN).food(MSFoods.APPLE_JUICE)));
	public static final RegistryObject<Item> TAB = ITEMS.register("tab", () -> new DrinkableItem(new Item.Properties().stacksTo(16).tab(MSItemGroup.MAIN).food(MSFoods.TAB)));
	public static final RegistryObject<Item> ORANGE_FAYGO = ITEMS.register("orange_faygo", () -> new DrinkableItem(new Item.Properties().stacksTo(16).tab(MSItemGroup.MAIN).food(MSFoods.FAYGO)));
	public static final RegistryObject<Item> CANDY_APPLE_FAYGO = ITEMS.register("candy_apple_faygo", () -> new DrinkableItem(new Item.Properties().stacksTo(16).tab(MSItemGroup.MAIN).food(MSFoods.FAYGO_CANDY_APPLE)));
	public static final RegistryObject<Item> FAYGO_COLA = ITEMS.register("faygo_cola", () -> new DrinkableItem(new Item.Properties().stacksTo(16).tab(MSItemGroup.MAIN).food(MSFoods.FAYGO_COLA)));
	public static final RegistryObject<Item> COTTON_CANDY_FAYGO = ITEMS.register("cotton_candy_faygo", () -> new DrinkableItem(new Item.Properties().stacksTo(16).tab(MSItemGroup.MAIN).food(MSFoods.FAYGO_COTTON_CANDY)));
	public static final RegistryObject<Item> CREME_SODA_FAYGO = ITEMS.register("creme_soda_faygo", () -> new DrinkableItem(new Item.Properties().stacksTo(16).tab(MSItemGroup.MAIN).food(MSFoods.FAYGO_CREME)));
	public static final RegistryObject<Item> GRAPE_FAYGO = ITEMS.register("grape_faygo", () -> new DrinkableItem(new Item.Properties().stacksTo(16).tab(MSItemGroup.MAIN).food(MSFoods.FAYGO_GRAPE)));
	public static final RegistryObject<Item> MOON_MIST_FAYGO = ITEMS.register("moon_mist_faygo", () -> new DrinkableItem(new Item.Properties().stacksTo(16).tab(MSItemGroup.MAIN).food(MSFoods.FAYGO_MOON_MIST)));
	public static final RegistryObject<Item> PEACH_FAYGO = ITEMS.register("peach_faygo", () -> new DrinkableItem(new Item.Properties().stacksTo(16).tab(MSItemGroup.MAIN).food(MSFoods.FAYGO_PEACH)));
	public static final RegistryObject<Item> REDPOP_FAYGO = ITEMS.register("redpop_faygo", () -> new DrinkableItem(new Item.Properties().stacksTo(16).tab(MSItemGroup.MAIN).food(MSFoods.FAYGO_REDPOP)));
	public static final RegistryObject<Item> GRUB_SAUCE = ITEMS.register("grub_sauce", () -> new DrinkableItem(new Item.Properties().stacksTo(16).tab(MSItemGroup.LANDS).food(MSFoods.GRUB_SAUCE)));
	public static final RegistryObject<Item> IRRADIATED_STEAK = ITEMS.register("irradiated_steak", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.IRRADIATED_STEAK)));
	public static final RegistryObject<Item> SURPRISE_EMBRYO = ITEMS.register("surprise_embryo", () -> new SurpriseEmbryoItem(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.SURPRISE_EMBRYO)));
	public static final RegistryObject<Item> UNKNOWABLE_EGG = ITEMS.register("unknowable_egg", () -> new UnknowableEggItem(new Item.Properties().stacksTo(16).tab(MSItemGroup.MAIN).food(MSFoods.UNKNOWABLE_EGG)));
	public static final RegistryObject<Item> BREADCRUMBS = ITEMS.register("breadcrumbs", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.BREADCRUMBS)));
	
	//Other Land Items
	public static final RegistryObject<Item> GOLDEN_GRASSHOPPER = ITEMS.register("golden_grasshopper", () -> new Item(new Item.Properties().tab(MSItemGroup.LANDS)));
	public static final RegistryObject<Item> BUG_NET = ITEMS.register("bug_net", () -> new BugNetItem(new Item.Properties().defaultDurability(64).tab(MSItemGroup.LANDS)));
	public static final RegistryObject<Item> FROG = ITEMS.register("frog", () -> new FrogItem(new Item.Properties().stacksTo(1).tab(MSItemGroup.LANDS)));
	public static final RegistryObject<Item> CARVING_TOOL = ITEMS.register("carving_tool", () -> new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.LANDS)));
	public static final RegistryObject<MSArmorItem> CRUMPLY_HAT = ITEMS.register("crumply_hat", () -> new MSArmorItem(MSItemTypes.CLOTH_ARMOR, EquipmentSlot.HEAD, new Item.Properties().stacksTo(1).tab(MSItemGroup.LANDS)));
	public static final RegistryObject<Item> STONE_EYEBALLS = ITEMS.register("stone_eyeballs", () -> new Item(new Item.Properties().tab(MSItemGroup.LANDS)));
	public static final RegistryObject<Item> STONE_SLAB = ITEMS.register("stone_slab", () -> new StoneTabletItem(MSBlocks.STONE_SLAB.get(), new Item.Properties().tab(MSItemGroup.LANDS)));
	public static /*final*/ RegistryObject<Item> SHOP_POSTER = ITEMS.register("shop_poster", () -> new HangingItem(ShopPosterEntity::new, new Item.Properties().stacksTo(1).tab(MSItemGroup.LANDS)));
	
	//Buckets
	public static final RegistryObject<Item> OIL_BUCKET = ITEMS.register("oil_bucket", () -> new BucketItem(MSFluids.OIL, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> BLOOD_BUCKET = ITEMS.register("blood_bucket", () -> new BucketItem(MSFluids.BLOOD, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> BRAIN_JUICE_BUCKET = ITEMS.register("brain_juice_bucket", () -> new BucketItem(MSFluids.BRAIN_JUICE, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> WATER_COLORS_BUCKET = ITEMS.register("water_colors_bucket", () -> new BucketItem(MSFluids.WATER_COLORS, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> ENDER_BUCKET = ITEMS.register("ender_bucket", () -> new BucketItem(MSFluids.ENDER, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> LIGHT_WATER_BUCKET = ITEMS.register("light_water_bucket", () -> new BucketItem(MSFluids.LIGHT_WATER, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> OBSIDIAN_BUCKET = ITEMS.register("obsidian_bucket", () -> new ObsidianBucketItem(new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET).tab(MSItemGroup.MAIN)));
	
	//Alchemy Items
	public static final RegistryObject<Item> DICE = ITEMS.register("dice", () -> new RightClickMessageItem(new Item.Properties().tab(MSItemGroup.MAIN), RightClickMessageItem.Type.DICE));
	public static final RegistryObject<Item> PLUTONIUM_CORE = ITEMS.register("plutonium_core", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> GRIMOIRE = ITEMS.register("grimoire", () -> new GrimoireItem(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> BATTERY = ITEMS.register("battery", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> BARBASOL = ITEMS.register("barbasol", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> CLOTHES_IRON = ITEMS.register("clothes_iron", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> INK_SQUID_PRO_QUO = ITEMS.register("ink_squid_pro_quo", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> CUEBALL = ITEMS.register("cueball", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> EIGHTBALL = ITEMS.register("eightball", () -> new RightClickMessageItem(new Item.Properties().tab(MSItemGroup.MAIN), RightClickMessageItem.Type.EIGHTBALL));
	public static final RegistryObject<Item> FLARP_MANUAL = ITEMS.register("flarp_manual", () -> new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.LANDS)));
	public static final RegistryObject<Item> SASSACRE_TEXT = ITEMS.register("sassacre_text", () -> new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.LANDS)));
	public static final RegistryObject<Item> WISEGUY = ITEMS.register("wiseguy", () -> new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.LANDS)));
	public static final RegistryObject<Item> TABLESTUCK_MANUAL = ITEMS.register("tablestuck_manual", () -> new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.LANDS)));
	public static final RegistryObject<Item> TILLDEATH_HANDBOOK = ITEMS.register("tilldeath_handbook", () -> new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.LANDS)));
	public static final RegistryObject<Item> BINARY_CODE = ITEMS.register("binary_code", () -> new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.LANDS)));
	public static final RegistryObject<Item> NONBINARY_CODE = ITEMS.register("nonbinary_code", () -> new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.LANDS)));
	public static final RegistryObject<Item> THRESH_DVD = ITEMS.register("thresh_dvd", () -> new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> GAMEBRO_MAGAZINE = ITEMS.register("gamebro_magazine", () -> new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> GAMEGRL_MAGAZINE = ITEMS.register("gamegrl_magazine", () -> new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> CREW_POSTER = ITEMS.register("crew_poster", () -> new HangingItem((world, pos, facing, stack) -> new CrewPosterEntity(world, pos, facing), new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> SBAHJ_POSTER = ITEMS.register("sbahj_poster", () -> new HangingItem((world, pos, facing, stack) -> new SbahjPosterEntity(world, pos, facing), new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> BI_DYE = ITEMS.register("bi_dye", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> LIP_BALM = ITEMS.register("lip_balm", () -> new RightClickMessageItem(new Item.Properties().tab(MSItemGroup.MAIN), RightClickMessageItem.Type.DEFAULT));
	public static final RegistryObject<Item> ELECTRIC_AUTOHARP = ITEMS.register("electric_autoharp", () -> new RightClickMusicItem(new Item.Properties().tab(MSItemGroup.MAIN), RightClickMusicItem.Type.ELECTRIC_AUTOHARP));
	public static final RegistryObject<Item> CARDBOARD_TUBE = ITEMS.register("cardboard_tube", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN)));
	
	//Other
	public static final RegistryObject<Item> CAPTCHAROID_CAMERA = ITEMS.register("captcharoid_camera", () -> new CaptcharoidCameraItem(new Item.Properties().defaultDurability(64).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> LONG_FORGOTTEN_WARHORN = ITEMS.register("long_forgotten_warhorn", () -> new LongForgottenWarhornItem(new Item.Properties().defaultDurability(100).tab(MSItemGroup.MAIN).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> BLACK_QUEENS_RING = ITEMS.register("black_queens_ring", () -> new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> WHITE_QUEENS_RING = ITEMS.register("white_queens_ring", () -> new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> BARBASOL_BOMB = ITEMS.register("barbasol_bomb", () -> new BarbasolBombItem(new Item.Properties().stacksTo(16).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> RAZOR_BLADE = ITEMS.register("razor_blade", () -> new RazorBladeItem(new Item.Properties().tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> ICE_SHARD = ITEMS.register("ice_shard", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> HORN = ITEMS.register("horn", () -> new SoundItem(() -> MSSoundEvents.ITEM_HORN_USE, new Item.Properties().tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> CAKE_MIX = ITEMS.register("cake_mix", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> TEMPLE_SCANNER = ITEMS.register("temple_scanner", () -> new StructureScannerItem(new Item.Properties().tab(MSItemGroup.MAIN).stacksTo(1), MSTags.ConfiguredFeatures.SCANNER_LOCATED, MSItems.RAW_URANIUM));
	
	public static final RegistryObject<Item> SCALEMATE_APPLESCAB = ITEMS.register("scalemate_applescab", () -> new ScalemateItem(new Item.Properties().tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> SCALEMATE_BERRYBREATH = ITEMS.register("scalemate_berrybreath", () -> new ScalemateItem(new Item.Properties()));
	public static final RegistryObject<Item> SCALEMATE_CINNAMONWHIFF = ITEMS.register("scalemate_cinnamonwhiff", () -> new ScalemateItem(new Item.Properties()));
	public static final RegistryObject<Item> SCALEMATE_HONEYTONGUE = ITEMS.register("scalemate_honeytongue", () -> new ScalemateItem(new Item.Properties()));
	public static final RegistryObject<Item> SCALEMATE_LEMONSNOUT = ITEMS.register("scalemate_lemonsnout", () -> new ScalemateItem(new Item.Properties()));
	public static final RegistryObject<Item> SCALEMATE_PINESNOUT = ITEMS.register("scalemate_pinesnout", () -> new ScalemateItem(new Item.Properties()));
	public static final RegistryObject<Item> SCALEMATE_PUCEFOOT = ITEMS.register("scalemate_pucefoot", () -> new ScalemateItem(new Item.Properties()));
	public static final RegistryObject<Item> SCALEMATE_PUMPKINSNUFFLE = ITEMS.register("scalemate_pumpkinsnuffle", () -> new ScalemateItem(new Item.Properties()));
	public static final RegistryObject<Item> SCALEMATE_PYRALSPITE = ITEMS.register("scalemate_pyralspite", () -> new ScalemateItem(new Item.Properties()));
	public static final RegistryObject<Item> SCALEMATE_WITNESS = ITEMS.register("scalemate_witness", () -> new ScalemateItem(new Item.Properties()));
	
	//Incredibly Useful Items
	public static final RegistryObject<Item> URANIUM_POWERED_STICK = ITEMS.register("uranium_powered_stick", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).stacksTo(1)));
	public static final RegistryObject<Item> IRON_BOAT = ITEMS.register("iron_boat", () -> new CustomBoatItem((stack, world, x, y, z) -> new MetalBoatEntity(world, x, y, z, MetalBoatEntity.Type.IRON), new Item.Properties().tab(MSItemGroup.MAIN).stacksTo(1)));
	public static final RegistryObject<Item> GOLD_BOAT = ITEMS.register("gold_boat", () -> new CustomBoatItem((stack, world, x, y, z) -> new MetalBoatEntity(world, x, y, z, MetalBoatEntity.Type.GOLD), new Item.Properties().tab(MSItemGroup.MAIN).stacksTo(1)));
	public static final RegistryObject<Item> COCOA_WART = ITEMS.register("cocoa_wart", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN)));
	
	//Music Discs
	public static final RegistryObject<Item> MUSIC_DISC_EMISSARY_OF_DANCE = ITEMS.register("music_disc_emissary_of_dance", () -> new RecordItem(1, () -> MSSoundEvents.MUSIC_DISC_EMISSARY_OF_DANCE, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> MUSIC_DISC_DANCE_STAB_DANCE = ITEMS.register("music_disc_dance_stab_dance", () -> new RecordItem(2, () -> MSSoundEvents.MUSIC_DISC_DANCE_STAB_DANCE, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> MUSIC_DISC_RETRO_BATTLE = ITEMS.register("music_disc_retro_battle", () -> new RecordItem(3, () -> MSSoundEvents.MUSIC_DISC_RETRO_BATTLE_THEME, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).tab(MSItemGroup.MAIN)));
	//Cassettes
	public static final RegistryObject<Item> CASSETTE_13 = ITEMS.register("cassette_13", () -> new CassetteItem(1, () -> SoundEvents.MUSIC_DISC_13, EnumCassetteType.THIRTEEN, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> CASSETTE_CAT = ITEMS.register("cassette_cat", () -> new CassetteItem(2, () -> SoundEvents.MUSIC_DISC_CAT, EnumCassetteType.CAT, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> CASSETTE_BLOCKS = ITEMS.register("cassette_blocks", () -> new CassetteItem(3, () -> SoundEvents.MUSIC_DISC_BLOCKS, EnumCassetteType.BLOCKS, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> CASSETTE_CHIRP = ITEMS.register("cassette_chirp", () -> new CassetteItem(4, () -> SoundEvents.MUSIC_DISC_CHIRP, EnumCassetteType.CHIRP, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> CASSETTE_FAR = ITEMS.register("cassette_far", () -> new CassetteItem(5, () -> SoundEvents.MUSIC_DISC_FAR, EnumCassetteType.FAR, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> CASSETTE_MALL = ITEMS.register("cassette_mall", () -> new CassetteItem(6, () -> SoundEvents.MUSIC_DISC_MALL, EnumCassetteType.MALL, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> CASSETTE_MELLOHI = ITEMS.register("cassette_mellohi", () -> new CassetteItem(7, () -> SoundEvents.MUSIC_DISC_MELLOHI, EnumCassetteType.MELLOHI, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> CASSETTE_DANCE_STAB = ITEMS.register("cassette_dance_stab", () -> new CassetteItem(2, () -> MSSoundEvents.MUSIC_DISC_DANCE_STAB_DANCE, EnumCassetteType.DANCE_STAB_DANCE, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> CASSETTE_RETRO_BATTLE = ITEMS.register("cassette_retro_battle", () -> new CassetteItem(3, () -> MSSoundEvents.MUSIC_DISC_RETRO_BATTLE_THEME, EnumCassetteType.RETRO_BATTLE_THEME, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> CASSETTE_EMISSARY = ITEMS.register("cassette_emissary", () -> new CassetteItem(1, () -> MSSoundEvents.MUSIC_DISC_EMISSARY_OF_DANCE, EnumCassetteType.EMISSARY_OF_DANCE, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).tab(MSItemGroup.MAIN)));
	/**/
	
	public static final RegistryObject<MultiblockItem> CRUXTRUDER = ITEMS.register("cruxtruder", () -> new CruxtruderItem(MSBlocks.CRUXTRUDER, new Item.Properties().tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> CRUXTRUDER_LID = registerBlockAsItem(MSBlocks.CRUXTRUDER_LID, MSItemGroup.MAIN);
	public static final RegistryObject<MultiblockItem> TOTEM_LATHE = ITEMS.register("totem_lathe", () -> new MultiblockItem(MSBlocks.TOTEM_LATHE, new Item.Properties().tab(MSItemGroup.MAIN)));
	public static final RegistryObject<MultiblockItem> ALCHEMITER = ITEMS.register("alchemiter", () -> new MultiblockItem(MSBlocks.ALCHEMITER, new Item.Properties().tab(MSItemGroup.MAIN)));
	public static final RegistryObject<MultiblockItem> PUNCH_DESIGNIX = ITEMS.register("punch_designix", () -> new MultiblockItem(MSBlocks.PUNCH_DESIGNIX, new Item.Properties().tab(MSItemGroup.MAIN)));
	
	public static final RegistryObject<Item> MINI_CRUXTRUDER = ITEMS.register("mini_cruxtruder", () -> new MiniCruxtruderItem(MSBlocks.MINI_CRUXTRUDER.get(), new Item.Properties().tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> MINI_TOTEM_LATHE = registerBlockAsItem(MSBlocks.MINI_TOTEM_LATHE, MSItemGroup.MAIN);
	public static final RegistryObject<Item> MINI_ALCHEMITER = registerBlockAsItem(MSBlocks.MINI_ALCHEMITER, MSItemGroup.MAIN);
	public static final RegistryObject<Item> MINI_PUNCH_DESIGNIX = registerBlockAsItem(MSBlocks.MINI_PUNCH_DESIGNIX, MSItemGroup.MAIN);
	public static final RegistryObject<Item> HOLOPAD = registerBlockAsItem(MSBlocks.HOLOPAD, MSItemGroup.MAIN);
	
	public static final RegistryObject<Item> CRUXITE_DOWEL = registerBlockItem(MSBlocks.CRUXITE_DOWEL, block -> new DowelItem(block, new Item.Properties().tab(MSItemGroup.MAIN)));
	
	public static final RegistryObject<MultiblockItem> LOTUS_TIME_CAPSULE = ITEMS.register("lotus_time_capsule", () -> new MultiblockItem(MSBlocks.LOTUS_TIME_CAPSULE_BLOCK, new Item.Properties().tab(MSItemGroup.MAIN)));
	
	
	
	/**/
	public static final RegistryObject<Item> BLACK_CHESS_DIRT = registerBlockAsItem(MSBlocks.BLACK_CHESS_DIRT, MSItemGroup.MAIN);
	public static final RegistryObject<Item> WHITE_CHESS_DIRT = registerBlockAsItem(MSBlocks.WHITE_CHESS_DIRT, MSItemGroup.MAIN);
	public static final RegistryObject<Item> DARK_GRAY_CHESS_DIRT = registerBlockAsItem(MSBlocks.DARK_GRAY_CHESS_DIRT, MSItemGroup.MAIN);
	public static final RegistryObject<Item> LIGHT_GRAY_CHESS_DIRT = registerBlockAsItem(MSBlocks.LIGHT_GRAY_CHESS_DIRT, MSItemGroup.MAIN);
	public static final RegistryObject<Item> SKAIA_PORTAL = registerBlockAsItem(MSBlocks.SKAIA_PORTAL, new Item.Properties().tab(MSItemGroup.MAIN).rarity(Rarity.EPIC));
	
	public static final RegistryObject<Item> BLACK_CHESS_BRICKS = registerBlockAsItem(MSBlocks.BLACK_CHESS_BRICKS, MSItemGroup.MAIN);
	public static final RegistryObject<Item> DARK_GRAY_CHESS_BRICKS = registerBlockAsItem(MSBlocks.DARK_GRAY_CHESS_BRICKS, MSItemGroup.MAIN);
	public static final RegistryObject<Item> LIGHT_GRAY_CHESS_BRICKS = registerBlockAsItem(MSBlocks.LIGHT_GRAY_CHESS_BRICKS, MSItemGroup.MAIN);
	public static final RegistryObject<Item> WHITE_CHESS_BRICKS = registerBlockAsItem(MSBlocks.WHITE_CHESS_BRICKS, MSItemGroup.MAIN);
	public static final RegistryObject<Item> BLACK_CHESS_BRICK_SMOOTH = registerBlockAsItem(MSBlocks.BLACK_CHESS_BRICK_SMOOTH, MSItemGroup.MAIN);
	public static final RegistryObject<Item> DARK_GRAY_CHESS_BRICK_SMOOTH = registerBlockAsItem(MSBlocks.DARK_GRAY_CHESS_BRICK_SMOOTH, MSItemGroup.MAIN);
	public static final RegistryObject<Item> LIGHT_GRAY_CHESS_BRICK_SMOOTH = registerBlockAsItem(MSBlocks.LIGHT_GRAY_CHESS_BRICK_SMOOTH, MSItemGroup.MAIN);
	public static final RegistryObject<Item> WHITE_CHESS_BRICK_SMOOTH = registerBlockAsItem(MSBlocks.WHITE_CHESS_BRICK_SMOOTH, MSItemGroup.MAIN);
	public static final RegistryObject<Item> BLACK_CHESS_BRICK_TRIM = registerBlockAsItem(MSBlocks.BLACK_CHESS_BRICK_TRIM, MSItemGroup.MAIN);
	public static final RegistryObject<Item> DARK_GRAY_CHESS_BRICK_TRIM = registerBlockAsItem(MSBlocks.DARK_GRAY_CHESS_BRICK_TRIM, MSItemGroup.MAIN);
	public static final RegistryObject<Item> LIGHT_GRAY_CHESS_BRICK_TRIM = registerBlockAsItem(MSBlocks.LIGHT_GRAY_CHESS_BRICK_TRIM, MSItemGroup.MAIN);
	public static final RegistryObject<Item> WHITE_CHESS_BRICK_TRIM = registerBlockAsItem(MSBlocks.WHITE_CHESS_BRICK_TRIM, MSItemGroup.MAIN);
	public static final RegistryObject<Item> CHECKERED_STAINED_GLASS = registerBlockAsItem(MSBlocks.CHECKERED_STAINED_GLASS, MSItemGroup.MAIN);
	public static final RegistryObject<Item> BLACK_CROWN_STAINED_GLASS = registerBlockAsItem(MSBlocks.BLACK_CROWN_STAINED_GLASS, MSItemGroup.MAIN);
	public static final RegistryObject<Item> BLACK_PAWN_STAINED_GLASS = registerBlockAsItem(MSBlocks.BLACK_PAWN_STAINED_GLASS, MSItemGroup.MAIN);
	public static final RegistryObject<Item> WHITE_CROWN_STAINED_GLASS = registerBlockAsItem(MSBlocks.WHITE_CROWN_STAINED_GLASS, MSItemGroup.MAIN);
	public static final RegistryObject<Item> WHITE_PAWN_STAINED_GLASS = registerBlockAsItem(MSBlocks.WHITE_PAWN_STAINED_GLASS, MSItemGroup.MAIN);
	
	public static final RegistryObject<Item> STONE_CRUXITE_ORE = registerBlockAsItem(MSBlocks.STONE_CRUXITE_ORE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> NETHERRACK_CRUXITE_ORE = registerBlockAsItem(MSBlocks.NETHERRACK_CRUXITE_ORE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> COBBLESTONE_CRUXITE_ORE = registerBlockAsItem(MSBlocks.COBBLESTONE_CRUXITE_ORE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> SANDSTONE_CRUXITE_ORE = registerBlockAsItem(MSBlocks.SANDSTONE_CRUXITE_ORE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> RED_SANDSTONE_CRUXITE_ORE = registerBlockAsItem(MSBlocks.RED_SANDSTONE_CRUXITE_ORE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> END_STONE_CRUXITE_ORE = registerBlockAsItem(MSBlocks.END_STONE_CRUXITE_ORE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> SHADE_STONE_CRUXITE_ORE = registerBlockAsItem(MSBlocks.SHADE_STONE_CRUXITE_ORE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> PINK_STONE_CRUXITE_ORE = registerBlockAsItem(MSBlocks.PINK_STONE_CRUXITE_ORE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> STONE_URANIUM_ORE = registerBlockAsItem(MSBlocks.STONE_URANIUM_ORE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> NETHERRACK_URANIUM_ORE = registerBlockAsItem(MSBlocks.NETHERRACK_URANIUM_ORE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> COBBLESTONE_URANIUM_ORE = registerBlockAsItem(MSBlocks.COBBLESTONE_URANIUM_ORE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> SANDSTONE_URANIUM_ORE = registerBlockAsItem(MSBlocks.SANDSTONE_URANIUM_ORE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> RED_SANDSTONE_URANIUM_ORE = registerBlockAsItem(MSBlocks.RED_SANDSTONE_URANIUM_ORE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> END_STONE_URANIUM_ORE = registerBlockAsItem(MSBlocks.END_STONE_URANIUM_ORE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> SHADE_STONE_URANIUM_ORE = registerBlockAsItem(MSBlocks.SHADE_STONE_URANIUM_ORE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> PINK_STONE_URANIUM_ORE = registerBlockAsItem(MSBlocks.PINK_STONE_URANIUM_ORE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> NETHERRACK_COAL_ORE = registerBlockAsItem(MSBlocks.NETHERRACK_COAL_ORE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> SHADE_STONE_COAL_ORE = registerBlockAsItem(MSBlocks.SHADE_STONE_COAL_ORE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> PINK_STONE_COAL_ORE = registerBlockAsItem(MSBlocks.PINK_STONE_COAL_ORE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> END_STONE_IRON_ORE = registerBlockAsItem(MSBlocks.END_STONE_IRON_ORE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> SANDSTONE_IRON_ORE = registerBlockAsItem(MSBlocks.SANDSTONE_IRON_ORE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> RED_SANDSTONE_IRON_ORE = registerBlockAsItem(MSBlocks.RED_SANDSTONE_IRON_ORE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> SANDSTONE_GOLD_ORE = registerBlockAsItem(MSBlocks.SANDSTONE_GOLD_ORE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> RED_SANDSTONE_GOLD_ORE = registerBlockAsItem(MSBlocks.RED_SANDSTONE_GOLD_ORE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> SHADE_STONE_GOLD_ORE = registerBlockAsItem(MSBlocks.SHADE_STONE_GOLD_ORE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> PINK_STONE_GOLD_ORE = registerBlockAsItem(MSBlocks.PINK_STONE_GOLD_ORE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> END_STONE_REDSTONE_ORE = registerBlockAsItem(MSBlocks.END_STONE_REDSTONE_ORE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> STONE_QUARTZ_ORE = registerBlockAsItem(MSBlocks.STONE_QUARTZ_ORE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> PINK_STONE_LAPIS_ORE = registerBlockAsItem(MSBlocks.PINK_STONE_LAPIS_ORE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> PINK_STONE_DIAMOND_ORE = registerBlockAsItem(MSBlocks.PINK_STONE_DIAMOND_ORE, MSItemGroup.LANDS);
	
	public static final RegistryObject<Item> CRUXITE_BLOCK = registerBlockAsItem(MSBlocks.CRUXITE_BLOCK, MSItemGroup.MAIN);
	public static final RegistryObject<Item> URANIUM_BLOCK = registerBlockAsItem(MSBlocks.URANIUM_BLOCK, MSItemGroup.MAIN);
	public static final RegistryObject<Item> GENERIC_OBJECT = registerBlockAsItem(MSBlocks.GENERIC_OBJECT, MSItemGroup.MAIN);
	
	public static final RegistryObject<Item> BLUE_DIRT = registerBlockAsItem(MSBlocks.BLUE_DIRT, MSItemGroup.LANDS);
	public static final RegistryObject<Item> THOUGHT_DIRT = registerBlockAsItem(MSBlocks.THOUGHT_DIRT, MSItemGroup.LANDS);
	public static final RegistryObject<Item> COARSE_STONE = registerBlockAsItem(MSBlocks.COARSE_STONE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> CHISELED_COARSE_STONE = registerBlockAsItem(MSBlocks.CHISELED_COARSE_STONE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> COARSE_STONE_BRICKS = registerBlockAsItem(MSBlocks.COARSE_STONE_BRICKS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> COARSE_STONE_COLUMN = registerBlockAsItem(MSBlocks.COARSE_STONE_COLUMN, MSItemGroup.LANDS);
	public static final RegistryObject<Item> CHISELED_COARSE_STONE_BRICKS = registerBlockAsItem(MSBlocks.CHISELED_COARSE_STONE_BRICKS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> CRACKED_COARSE_STONE_BRICKS = registerBlockAsItem(MSBlocks.CRACKED_COARSE_STONE_BRICKS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> MOSSY_COARSE_STONE_BRICKS = registerBlockAsItem(MSBlocks.MOSSY_COARSE_STONE_BRICKS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> SHADE_STONE = registerBlockAsItem(MSBlocks.SHADE_STONE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> SMOOTH_SHADE_STONE = registerBlockAsItem(MSBlocks.SMOOTH_SHADE_STONE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> SHADE_BRICKS = registerBlockAsItem(MSBlocks.SHADE_BRICKS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> SHADE_COLUMN = registerBlockAsItem(MSBlocks.SHADE_COLUMN, MSItemGroup.LANDS);
	public static final RegistryObject<Item> CHISELED_SHADE_BRICKS = registerBlockAsItem(MSBlocks.CHISELED_SHADE_BRICKS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> CRACKED_SHADE_BRICKS = registerBlockAsItem(MSBlocks.CRACKED_SHADE_BRICKS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> MOSSY_SHADE_BRICKS = registerBlockAsItem(MSBlocks.MOSSY_SHADE_BRICKS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> BLOOD_SHADE_BRICKS = registerBlockAsItem(MSBlocks.BLOOD_SHADE_BRICKS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> TAR_SHADE_BRICKS = registerBlockAsItem(MSBlocks.TAR_SHADE_BRICKS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> FROST_TILE = registerBlockAsItem(MSBlocks.FROST_TILE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> CHISELED_FROST_TILE = registerBlockAsItem(MSBlocks.CHISELED_FROST_TILE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> FROST_BRICKS = registerBlockAsItem(MSBlocks.FROST_BRICKS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> FROST_COLUMN = registerBlockAsItem(MSBlocks.FROST_COLUMN, MSItemGroup.LANDS);
	public static final RegistryObject<Item> CHISELED_FROST_BRICKS = registerBlockAsItem(MSBlocks.CHISELED_FROST_BRICKS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> CRACKED_FROST_BRICKS = registerBlockAsItem(MSBlocks.CRACKED_FROST_BRICKS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> FLOWERY_FROST_BRICKS = registerBlockAsItem(MSBlocks.FLOWERY_FROST_BRICKS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> CAST_IRON = registerBlockAsItem(MSBlocks.CAST_IRON, MSItemGroup.LANDS);
	public static final RegistryObject<Item> CHISELED_CAST_IRON = registerBlockAsItem(MSBlocks.CHISELED_CAST_IRON, MSItemGroup.LANDS);
	public static final RegistryObject<Item> STEEL_BEAM = registerBlockAsItem(MSBlocks.STEEL_BEAM, MSItemGroup.LANDS);
	public static final RegistryObject<Item> MYCELIUM_COBBLESTONE = registerBlockAsItem(MSBlocks.MYCELIUM_COBBLESTONE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> MYCELIUM_STONE = registerBlockAsItem(MSBlocks.MYCELIUM_STONE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> POLISHED_MYCELIUM_STONE = registerBlockAsItem(MSBlocks.POLISHED_MYCELIUM_STONE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> MYCELIUM_BRICKS = registerBlockAsItem(MSBlocks.MYCELIUM_BRICKS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> MYCELIUM_COLUMN = registerBlockAsItem(MSBlocks.MYCELIUM_COLUMN, MSItemGroup.LANDS);
	public static final RegistryObject<Item> CHISELED_MYCELIUM_BRICKS = registerBlockAsItem(MSBlocks.CHISELED_MYCELIUM_BRICKS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> CRACKED_MYCELIUM_BRICKS = registerBlockAsItem(MSBlocks.CRACKED_MYCELIUM_BRICKS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> MOSSY_MYCELIUM_BRICKS = registerBlockAsItem(MSBlocks.MOSSY_MYCELIUM_BRICKS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> FLOWERY_MYCELIUM_BRICKS = registerBlockAsItem(MSBlocks.FLOWERY_MYCELIUM_BRICKS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> BLACK_STONE = registerBlockAsItem(MSBlocks.BLACK_STONE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> POLISHED_BLACK_STONE = registerBlockAsItem(MSBlocks.POLISHED_BLACK_STONE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> BLACK_COBBLESTONE = registerBlockAsItem(MSBlocks.BLACK_COBBLESTONE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> BLACK_STONE_BRICKS = registerBlockAsItem(MSBlocks.BLACK_STONE_BRICKS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> BLACK_STONE_COLUMN = registerBlockAsItem(MSBlocks.BLACK_STONE_COLUMN, MSItemGroup.LANDS);
	public static final RegistryObject<Item> CHISELED_BLACK_STONE_BRICKS = registerBlockAsItem(MSBlocks.CHISELED_BLACK_STONE_BRICKS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> CRACKED_BLACK_STONE_BRICKS = registerBlockAsItem(MSBlocks.CRACKED_BLACK_STONE_BRICKS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> BLACK_SAND = registerBlockAsItem(MSBlocks.BLACK_SAND, MSItemGroup.LANDS);
	public static final RegistryObject<Item> DECREPIT_STONE_BRICKS = registerBlockAsItem(MSBlocks.DECREPIT_STONE_BRICKS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> FLOWERY_MOSSY_COBBLESTONE = registerBlockAsItem(MSBlocks.FLOWERY_MOSSY_COBBLESTONE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> MOSSY_DECREPIT_STONE_BRICKS = registerBlockAsItem(MSBlocks.MOSSY_DECREPIT_STONE_BRICKS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> FLOWERY_MOSSY_STONE_BRICKS = registerBlockAsItem(MSBlocks.FLOWERY_MOSSY_STONE_BRICKS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> COARSE_END_STONE = registerBlockAsItem(MSBlocks.COARSE_END_STONE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> END_GRASS = registerBlockAsItem(MSBlocks.END_GRASS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> CHALK = registerBlockAsItem(MSBlocks.CHALK, MSItemGroup.LANDS);
	public static final RegistryObject<Item> POLISHED_CHALK = registerBlockAsItem(MSBlocks.POLISHED_CHALK, MSItemGroup.LANDS);
	public static final RegistryObject<Item> CHALK_BRICKS = registerBlockAsItem(MSBlocks.CHALK_BRICKS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> CHALK_COLUMN = registerBlockAsItem(MSBlocks.CHALK_COLUMN, MSItemGroup.LANDS);
	public static final RegistryObject<Item> CHISELED_CHALK_BRICKS = registerBlockAsItem(MSBlocks.CHISELED_CHALK_BRICKS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> MOSSY_CHALK_BRICKS = registerBlockAsItem(MSBlocks.MOSSY_CHALK_BRICKS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> FLOWERY_CHALK_BRICKS = registerBlockAsItem(MSBlocks.FLOWERY_CHALK_BRICKS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> PINK_STONE = registerBlockAsItem(MSBlocks.PINK_STONE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> PINK_STONE_BRICKS = registerBlockAsItem(MSBlocks.PINK_STONE_BRICKS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> CHISELED_PINK_STONE_BRICKS = registerBlockAsItem(MSBlocks.CHISELED_PINK_STONE_BRICKS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> CRACKED_PINK_STONE_BRICKS = registerBlockAsItem(MSBlocks.CRACKED_PINK_STONE_BRICKS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> MOSSY_PINK_STONE_BRICKS = registerBlockAsItem(MSBlocks.MOSSY_PINK_STONE_BRICKS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> POLISHED_PINK_STONE = registerBlockAsItem(MSBlocks.POLISHED_PINK_STONE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> PINK_STONE_COLUMN = registerBlockAsItem(MSBlocks.PINK_STONE_COLUMN, MSItemGroup.LANDS);
	public static final RegistryObject<Item> BROWN_STONE = registerBlockAsItem(MSBlocks.BROWN_STONE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> BROWN_STONE_BRICKS = registerBlockAsItem(MSBlocks.BROWN_STONE_BRICKS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> BROWN_STONE_COLUMN = registerBlockAsItem(MSBlocks.BROWN_STONE_COLUMN, MSItemGroup.LANDS);
	public static final RegistryObject<Item> CRACKED_BROWN_STONE_BRICKS = registerBlockAsItem(MSBlocks.CRACKED_BROWN_STONE_BRICKS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> POLISHED_BROWN_STONE = registerBlockAsItem(MSBlocks.POLISHED_BROWN_STONE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> GREEN_STONE = registerBlockAsItem(MSBlocks.GREEN_STONE, MSItemGroup.MAIN);
	public static final RegistryObject<Item> GREEN_STONE_BRICKS = registerBlockAsItem(MSBlocks.GREEN_STONE_BRICKS, MSItemGroup.MAIN);
	public static final RegistryObject<Item> GREEN_STONE_COLUMN = registerBlockAsItem(MSBlocks.GREEN_STONE_COLUMN, MSItemGroup.MAIN);
	public static final RegistryObject<Item> POLISHED_GREEN_STONE = registerBlockAsItem(MSBlocks.POLISHED_GREEN_STONE, MSItemGroup.MAIN);
	public static final RegistryObject<Item> CHISELED_GREEN_STONE_BRICKS = registerBlockAsItem(MSBlocks.CHISELED_GREEN_STONE_BRICKS, MSItemGroup.MAIN);
	public static final RegistryObject<Item> HORIZONTAL_GREEN_STONE_BRICKS = registerBlockAsItem(MSBlocks.HORIZONTAL_GREEN_STONE_BRICKS, MSItemGroup.MAIN);
	public static final RegistryObject<Item> VERTICAL_GREEN_STONE_BRICKS = registerBlockAsItem(MSBlocks.VERTICAL_GREEN_STONE_BRICKS, MSItemGroup.MAIN);
	public static final RegistryObject<Item> GREEN_STONE_BRICK_TRIM = registerBlockAsItem(MSBlocks.GREEN_STONE_BRICK_TRIM, MSItemGroup.MAIN);
	public static final RegistryObject<Item> GREEN_STONE_BRICK_FROG = registerBlockAsItem(MSBlocks.GREEN_STONE_BRICK_FROG, MSItemGroup.MAIN);
	public static final RegistryObject<Item> GREEN_STONE_BRICK_IGUANA_LEFT = registerBlockAsItem(MSBlocks.GREEN_STONE_BRICK_IGUANA_LEFT, MSItemGroup.MAIN);
	public static final RegistryObject<Item> GREEN_STONE_BRICK_IGUANA_RIGHT = registerBlockAsItem(MSBlocks.GREEN_STONE_BRICK_IGUANA_RIGHT, MSItemGroup.MAIN);
	public static final RegistryObject<Item> GREEN_STONE_BRICK_LOTUS = registerBlockAsItem(MSBlocks.GREEN_STONE_BRICK_LOTUS, MSItemGroup.MAIN);
	public static final RegistryObject<Item> GREEN_STONE_BRICK_NAK_LEFT = registerBlockAsItem(MSBlocks.GREEN_STONE_BRICK_NAK_LEFT, MSItemGroup.MAIN);
	public static final RegistryObject<Item> GREEN_STONE_BRICK_NAK_RIGHT = registerBlockAsItem(MSBlocks.GREEN_STONE_BRICK_NAK_RIGHT, MSItemGroup.MAIN);
	public static final RegistryObject<Item> GREEN_STONE_BRICK_SALAMANDER_LEFT = registerBlockAsItem(MSBlocks.GREEN_STONE_BRICK_SALAMANDER_LEFT, MSItemGroup.MAIN);
	public static final RegistryObject<Item> GREEN_STONE_BRICK_SALAMANDER_RIGHT = registerBlockAsItem(MSBlocks.GREEN_STONE_BRICK_SALAMANDER_RIGHT, MSItemGroup.MAIN);
	public static final RegistryObject<Item> GREEN_STONE_BRICK_SKAIA = registerBlockAsItem(MSBlocks.GREEN_STONE_BRICK_SKAIA, MSItemGroup.MAIN);
	public static final RegistryObject<Item> GREEN_STONE_BRICK_TURTLE = registerBlockAsItem(MSBlocks.GREEN_STONE_BRICK_TURTLE, MSItemGroup.MAIN);
	public static final RegistryObject<Item> SANDSTONE_COLUMN = registerBlockAsItem(MSBlocks.SANDSTONE_COLUMN, MSItemGroup.LANDS);
	public static final RegistryObject<Item> CHISELED_SANDSTONE_COLUMN = registerBlockAsItem(MSBlocks.CHISELED_SANDSTONE_COLUMN, MSItemGroup.LANDS);
	public static final RegistryObject<Item> RED_SANDSTONE_COLUMN = registerBlockAsItem(MSBlocks.RED_SANDSTONE_COLUMN, MSItemGroup.LANDS);
	public static final RegistryObject<Item> CHISELED_RED_SANDSTONE_COLUMN = registerBlockAsItem(MSBlocks.CHISELED_RED_SANDSTONE_COLUMN, MSItemGroup.LANDS);
	public static final RegistryObject<Item> UNCARVED_WOOD = registerBlockAsItem(MSBlocks.UNCARVED_WOOD, MSItemGroup.LANDS);
	public static final RegistryObject<Item> CHIPBOARD = registerBlockAsItem(MSBlocks.CHIPBOARD, MSItemGroup.LANDS);
	public static final RegistryObject<Item> WOOD_SHAVINGS = registerBlockAsItem(MSBlocks.WOOD_SHAVINGS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> DENSE_CLOUD = registerBlockAsItem(MSBlocks.DENSE_CLOUD, MSItemGroup.LANDS);
	public static final RegistryObject<Item> BRIGHT_DENSE_CLOUD = registerBlockAsItem(MSBlocks.BRIGHT_DENSE_CLOUD, MSItemGroup.LANDS);
	public static final RegistryObject<Item> SUGAR_CUBE = registerBlockAsItem(MSBlocks.SUGAR_CUBE, MSItemGroup.LANDS);
	
	public static final RegistryObject<Item> GLOWING_LOG = registerBlockAsItem(MSBlocks.GLOWING_LOG, MSItemGroup.LANDS);
	public static final RegistryObject<Item> FROST_LOG = registerBlockAsItem(MSBlocks.FROST_LOG, MSItemGroup.LANDS);
	public static final RegistryObject<Item> RAINBOW_LOG = registerBlockAsItem(MSBlocks.RAINBOW_LOG, MSItemGroup.LANDS);
	public static final RegistryObject<Item> END_LOG = registerBlockAsItem(MSBlocks.END_LOG, MSItemGroup.LANDS);
	public static final RegistryObject<Item> VINE_LOG = registerBlockAsItem(MSBlocks.VINE_LOG, MSItemGroup.LANDS);
	public static final RegistryObject<Item> FLOWERY_VINE_LOG = registerBlockAsItem(MSBlocks.FLOWERY_VINE_LOG, MSItemGroup.LANDS);
	public static final RegistryObject<Item> DEAD_LOG = registerBlockAsItem(MSBlocks.DEAD_LOG, MSItemGroup.LANDS);
	public static final RegistryObject<Item> PETRIFIED_LOG = registerBlockAsItem(MSBlocks.PETRIFIED_LOG, MSItemGroup.LANDS);
	public static final RegistryObject<Item> GLOWING_WOOD = registerBlockAsItem(MSBlocks.GLOWING_WOOD, MSItemGroup.LANDS);
	public static final RegistryObject<Item> FROST_WOOD = registerBlockAsItem(MSBlocks.FROST_WOOD, MSItemGroup.LANDS);
	public static final RegistryObject<Item> RAINBOW_WOOD = registerBlockAsItem(MSBlocks.RAINBOW_WOOD, MSItemGroup.LANDS);
	public static final RegistryObject<Item> END_WOOD = registerBlockAsItem(MSBlocks.END_WOOD, MSItemGroup.LANDS);
	public static final RegistryObject<Item> VINE_WOOD = registerBlockAsItem(MSBlocks.VINE_WOOD, MSItemGroup.LANDS);
	public static final RegistryObject<Item> FLOWERY_VINE_WOOD = registerBlockAsItem(MSBlocks.FLOWERY_VINE_WOOD, MSItemGroup.LANDS);
	public static final RegistryObject<Item> DEAD_WOOD = registerBlockAsItem(MSBlocks.DEAD_WOOD, MSItemGroup.LANDS);
	public static final RegistryObject<Item> PETRIFIED_WOOD = registerBlockAsItem(MSBlocks.PETRIFIED_WOOD, MSItemGroup.LANDS);
	public static final RegistryObject<Item> GLOWING_PLANKS = registerBlockAsItem(MSBlocks.GLOWING_PLANKS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> FROST_PLANKS = registerBlockAsItem(MSBlocks.FROST_PLANKS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> RAINBOW_PLANKS = registerBlockAsItem(MSBlocks.RAINBOW_PLANKS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> END_PLANKS = registerBlockAsItem(MSBlocks.END_PLANKS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> DEAD_PLANKS = registerBlockAsItem(MSBlocks.DEAD_PLANKS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> TREATED_PLANKS = registerBlockAsItem(MSBlocks.TREATED_PLANKS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> FROST_LEAVES = registerBlockAsItem(MSBlocks.FROST_LEAVES, MSItemGroup.LANDS);
	public static final RegistryObject<Item> RAINBOW_LEAVES = registerBlockAsItem(MSBlocks.RAINBOW_LEAVES, MSItemGroup.LANDS);
	public static final RegistryObject<Item> END_LEAVES = registerBlockAsItem(MSBlocks.END_LEAVES, MSItemGroup.LANDS);
	public static final RegistryObject<Item> RAINBOW_SAPLING = registerBlockAsItem(MSBlocks.RAINBOW_SAPLING, MSItemGroup.LANDS);
	public static final RegistryObject<Item> END_SAPLING = registerBlockAsItem(MSBlocks.END_SAPLING, MSItemGroup.LANDS);
	
	public static final RegistryObject<Item> BLOOD_ASPECT_LOG = registerBlockAsItem(MSBlocks.BLOOD_ASPECT_LOG);
	public static final RegistryObject<Item> BREATH_ASPECT_LOG = registerBlockAsItem(MSBlocks.BREATH_ASPECT_LOG);
	public static final RegistryObject<Item> DOOM_ASPECT_LOG = registerBlockAsItem(MSBlocks.DOOM_ASPECT_LOG);
	public static final RegistryObject<Item> HEART_ASPECT_LOG = registerBlockAsItem(MSBlocks.HEART_ASPECT_LOG);
	public static final RegistryObject<Item> HOPE_ASPECT_LOG = registerBlockAsItem(MSBlocks.HOPE_ASPECT_LOG);
	public static final RegistryObject<Item> LIFE_ASPECT_LOG = registerBlockAsItem(MSBlocks.LIFE_ASPECT_LOG);
	public static final RegistryObject<Item> LIGHT_ASPECT_LOG = registerBlockAsItem(MSBlocks.LIGHT_ASPECT_LOG);
	public static final RegistryObject<Item> MIND_ASPECT_LOG = registerBlockAsItem(MSBlocks.MIND_ASPECT_LOG);
	public static final RegistryObject<Item> RAGE_ASPECT_LOG = registerBlockAsItem(MSBlocks.RAGE_ASPECT_LOG);
	public static final RegistryObject<Item> SPACE_ASPECT_LOG = registerBlockAsItem(MSBlocks.SPACE_ASPECT_LOG);
	public static final RegistryObject<Item> TIME_ASPECT_LOG = registerBlockAsItem(MSBlocks.TIME_ASPECT_LOG);
	public static final RegistryObject<Item> VOID_ASPECT_LOG = registerBlockAsItem(MSBlocks.VOID_ASPECT_LOG);
	public static final RegistryObject<Item> BLOOD_ASPECT_PLANKS = registerBlockAsItem(MSBlocks.BLOOD_ASPECT_PLANKS);
	public static final RegistryObject<Item> BREATH_ASPECT_PLANKS = registerBlockAsItem(MSBlocks.BREATH_ASPECT_PLANKS);
	public static final RegistryObject<Item> DOOM_ASPECT_PLANKS = registerBlockAsItem(MSBlocks.DOOM_ASPECT_PLANKS);
	public static final RegistryObject<Item> HEART_ASPECT_PLANKS = registerBlockAsItem(MSBlocks.HEART_ASPECT_PLANKS);
	public static final RegistryObject<Item> HOPE_ASPECT_PLANKS = registerBlockAsItem(MSBlocks.HOPE_ASPECT_PLANKS);
	public static final RegistryObject<Item> LIFE_ASPECT_PLANKS = registerBlockAsItem(MSBlocks.LIFE_ASPECT_PLANKS);
	public static final RegistryObject<Item> LIGHT_ASPECT_PLANKS = registerBlockAsItem(MSBlocks.LIGHT_ASPECT_PLANKS);
	public static final RegistryObject<Item> MIND_ASPECT_PLANKS = registerBlockAsItem(MSBlocks.MIND_ASPECT_PLANKS);
	public static final RegistryObject<Item> RAGE_ASPECT_PLANKS = registerBlockAsItem(MSBlocks.RAGE_ASPECT_PLANKS);
	public static final RegistryObject<Item> SPACE_ASPECT_PLANKS = registerBlockAsItem(MSBlocks.SPACE_ASPECT_PLANKS);
	public static final RegistryObject<Item> TIME_ASPECT_PLANKS = registerBlockAsItem(MSBlocks.TIME_ASPECT_PLANKS);
	public static final RegistryObject<Item> VOID_ASPECT_PLANKS = registerBlockAsItem(MSBlocks.VOID_ASPECT_PLANKS);
	public static final RegistryObject<Item> BLOOD_ASPECT_LEAVES = registerBlockAsItem(MSBlocks.BLOOD_ASPECT_LEAVES);
	public static final RegistryObject<Item> BREATH_ASPECT_LEAVES = registerBlockAsItem(MSBlocks.BREATH_ASPECT_LEAVES);
	public static final RegistryObject<Item> DOOM_ASPECT_LEAVES = registerBlockAsItem(MSBlocks.DOOM_ASPECT_LEAVES);
	public static final RegistryObject<Item> HEART_ASPECT_LEAVES = registerBlockAsItem(MSBlocks.HEART_ASPECT_LEAVES);
	public static final RegistryObject<Item> HOPE_ASPECT_LEAVES = registerBlockAsItem(MSBlocks.HOPE_ASPECT_LEAVES);
	public static final RegistryObject<Item> LIFE_ASPECT_LEAVES = registerBlockAsItem(MSBlocks.LIFE_ASPECT_LEAVES);
	public static final RegistryObject<Item> LIGHT_ASPECT_LEAVES = registerBlockAsItem(MSBlocks.LIGHT_ASPECT_LEAVES);
	public static final RegistryObject<Item> MIND_ASPECT_LEAVES = registerBlockAsItem(MSBlocks.MIND_ASPECT_LEAVES);
	public static final RegistryObject<Item> RAGE_ASPECT_LEAVES = registerBlockAsItem(MSBlocks.RAGE_ASPECT_LEAVES);
	public static final RegistryObject<Item> SPACE_ASPECT_LEAVES = registerBlockAsItem(MSBlocks.SPACE_ASPECT_LEAVES);
	public static final RegistryObject<Item> TIME_ASPECT_LEAVES = registerBlockAsItem(MSBlocks.TIME_ASPECT_LEAVES);
	public static final RegistryObject<Item> VOID_ASPECT_LEAVES = registerBlockAsItem(MSBlocks.VOID_ASPECT_LEAVES);
	public static final RegistryObject<Item> BLOOD_ASPECT_SAPLING = registerBlockAsItem(MSBlocks.BLOOD_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final RegistryObject<Item> BREATH_ASPECT_SAPLING = registerBlockAsItem(MSBlocks.BREATH_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final RegistryObject<Item> DOOM_ASPECT_SAPLING = registerBlockAsItem(MSBlocks.DOOM_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final RegistryObject<Item> HEART_ASPECT_SAPLING = registerBlockAsItem(MSBlocks.HEART_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final RegistryObject<Item> HOPE_ASPECT_SAPLING = registerBlockAsItem(MSBlocks.HOPE_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final RegistryObject<Item> LIFE_ASPECT_SAPLING = registerBlockAsItem(MSBlocks.LIFE_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final RegistryObject<Item> LIGHT_ASPECT_SAPLING = registerBlockAsItem(MSBlocks.LIGHT_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final RegistryObject<Item> MIND_ASPECT_SAPLING = registerBlockAsItem(MSBlocks.MIND_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final RegistryObject<Item> RAGE_ASPECT_SAPLING = registerBlockAsItem(MSBlocks.RAGE_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final RegistryObject<Item> SPACE_ASPECT_SAPLING = registerBlockAsItem(MSBlocks.SPACE_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final RegistryObject<Item> TIME_ASPECT_SAPLING = registerBlockAsItem(MSBlocks.TIME_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final RegistryObject<Item> VOID_ASPECT_SAPLING = registerBlockAsItem(MSBlocks.VOID_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	
	public static final RegistryObject<Item> GLOWING_MUSHROOM = registerBlockAsItem(MSBlocks.GLOWING_MUSHROOM, MSItemGroup.LANDS);
	public static final RegistryObject<Item> DESERT_BUSH = registerBlockAsItem(MSBlocks.DESERT_BUSH, MSItemGroup.LANDS);
	public static final RegistryObject<Item> BLOOMING_CACTUS = registerBlockAsItem(MSBlocks.BLOOMING_CACTUS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> PETRIFIED_GRASS = registerBlockAsItem(MSBlocks.PETRIFIED_GRASS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> PETRIFIED_POPPY = registerBlockAsItem(MSBlocks.PETRIFIED_POPPY, MSItemGroup.LANDS);
	//public static final RegistryObject<Item> STRAWBERRY = ITEMS.register("strawberry", () -> new BlockItem(MSBlocks.STRAWBERRY.get(), new Item.Properties().tab(MSItemGroup.LANDS)));
	public static final RegistryObject<Item> STRAWBERRY = registerBlockItem(MSBlocks.STRAWBERRY, new Item.Properties().tab(MSItemGroup.LANDS));
	public static final RegistryObject<Item> TALL_END_GRASS = registerBlockAsItem(MSBlocks.TALL_END_GRASS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> GLOWFLOWER = registerBlockAsItem(MSBlocks.GLOWFLOWER, MSItemGroup.LANDS);
	
	public static final RegistryObject<Item> GLOWY_GOOP = registerBlockAsItem(MSBlocks.GLOWY_GOOP, MSItemGroup.LANDS);
	public static final RegistryObject<Item> COAGULATED_BLOOD = registerBlockAsItem(MSBlocks.COAGULATED_BLOOD, MSItemGroup.LANDS);
	public static final RegistryObject<Item> PIPE = registerBlockAsItem(MSBlocks.PIPE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> PIPE_INTERSECTION = registerBlockAsItem(MSBlocks.PIPE_INTERSECTION, MSItemGroup.LANDS);
	public static final RegistryObject<Item> PARCEL_PYXIS = registerBlockAsItem(MSBlocks.PARCEL_PYXIS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> PYXIS_LID = registerBlockAsItem(MSBlocks.PYXIS_LID, MSItemGroup.LANDS);
	public static final RegistryObject<Item> NAKAGATOR_STATUE = registerBlockAsItem(MSBlocks.NAKAGATOR_STATUE, MSItemGroup.LANDS);
	
	public static final RegistryObject<Item> BLACK_CHESS_BRICK_STAIRS = registerBlockAsItem(MSBlocks.BLACK_CHESS_BRICK_STAIRS, MSItemGroup.MAIN);
	public static final RegistryObject<Item> DARK_GRAY_CHESS_BRICK_STAIRS = registerBlockAsItem(MSBlocks.DARK_GRAY_CHESS_BRICK_STAIRS, MSItemGroup.MAIN);
	public static final RegistryObject<Item> LIGHT_GRAY_CHESS_BRICK_STAIRS = registerBlockAsItem(MSBlocks.LIGHT_GRAY_CHESS_BRICK_STAIRS, MSItemGroup.MAIN);
	public static final RegistryObject<Item> WHITE_CHESS_BRICK_STAIRS = registerBlockAsItem(MSBlocks.WHITE_CHESS_BRICK_STAIRS, MSItemGroup.MAIN);
	public static final RegistryObject<Item> COARSE_STONE_STAIRS = registerBlockAsItem(MSBlocks.COARSE_STONE_STAIRS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> COARSE_STONE_BRICK_STAIRS = registerBlockAsItem(MSBlocks.COARSE_STONE_BRICK_STAIRS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> SHADE_STAIRS = registerBlockAsItem(MSBlocks.SHADE_STAIRS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> SHADE_BRICK_STAIRS = registerBlockAsItem(MSBlocks.SHADE_BRICK_STAIRS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> FROST_TILE_STAIRS = registerBlockAsItem(MSBlocks.FROST_TILE_STAIRS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> FROST_BRICK_STAIRS = registerBlockAsItem(MSBlocks.FROST_BRICK_STAIRS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> CAST_IRON_STAIRS = registerBlockAsItem(MSBlocks.CAST_IRON_STAIRS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> BLACK_STONE_STAIRS = registerBlockAsItem(MSBlocks.BLACK_STONE_STAIRS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> BLACK_STONE_BRICK_STAIRS = registerBlockAsItem(MSBlocks.BLACK_STONE_BRICK_STAIRS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> MYCELIUM_STAIRS = registerBlockAsItem(MSBlocks.MYCELIUM_STAIRS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> MYCELIUM_BRICK_STAIRS = registerBlockAsItem(MSBlocks.MYCELIUM_BRICK_STAIRS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> CHALK_STAIRS = registerBlockAsItem(MSBlocks.CHALK_STAIRS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> CHALK_BRICK_STAIRS = registerBlockAsItem(MSBlocks.CHALK_BRICK_STAIRS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> FLOWERY_MOSSY_STONE_BRICK_STAIRS = registerBlockAsItem(MSBlocks.FLOWERY_MOSSY_STONE_BRICK_STAIRS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> PINK_STONE_STAIRS = registerBlockAsItem(MSBlocks.PINK_STONE_STAIRS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> PINK_STONE_BRICK_STAIRS = registerBlockAsItem(MSBlocks.PINK_STONE_BRICK_STAIRS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> BROWN_STONE_STAIRS = registerBlockAsItem(MSBlocks.BROWN_STONE_STAIRS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> BROWN_STONE_BRICK_STAIRS = registerBlockAsItem(MSBlocks.BROWN_STONE_BRICK_STAIRS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> GREEN_STONE_STAIRS = registerBlockAsItem(MSBlocks.GREEN_STONE_STAIRS, MSItemGroup.MAIN);
	public static final RegistryObject<Item> GREEN_STONE_BRICK_STAIRS = registerBlockAsItem(MSBlocks.GREEN_STONE_BRICK_STAIRS, MSItemGroup.MAIN);
	public static final RegistryObject<Item> RAINBOW_PLANKS_STAIRS = registerBlockAsItem(MSBlocks.RAINBOW_PLANKS_STAIRS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> END_PLANKS_STAIRS = registerBlockAsItem(MSBlocks.END_PLANKS_STAIRS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> DEAD_PLANKS_STAIRS = registerBlockAsItem(MSBlocks.DEAD_PLANKS_STAIRS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> TREATED_PLANKS_STAIRS = registerBlockAsItem(MSBlocks.TREATED_PLANKS_STAIRS, MSItemGroup.LANDS);
	public static final RegistryObject<Item> STEEP_GREEN_STONE_BRICK_STAIRS_BASE = registerBlockAsItem(MSBlocks.STEEP_GREEN_STONE_BRICK_STAIRS_BASE, MSItemGroup.MAIN);
	public static final RegistryObject<Item> STEEP_GREEN_STONE_BRICK_STAIRS_TOP = registerBlockAsItem(MSBlocks.STEEP_GREEN_STONE_BRICK_STAIRS_TOP, MSItemGroup.MAIN);
	public static final RegistryObject<Item> BLACK_CHESS_BRICK_SLAB = registerBlockAsItem(MSBlocks.BLACK_CHESS_BRICK_SLAB, MSItemGroup.MAIN);
	public static final RegistryObject<Item> DARK_GRAY_CHESS_BRICK_SLAB = registerBlockAsItem(MSBlocks.DARK_GRAY_CHESS_BRICK_SLAB, MSItemGroup.MAIN);
	public static final RegistryObject<Item> LIGHT_GRAY_CHESS_BRICK_SLAB = registerBlockAsItem(MSBlocks.LIGHT_GRAY_CHESS_BRICK_SLAB, MSItemGroup.MAIN);
	public static final RegistryObject<Item> WHITE_CHESS_BRICK_SLAB = registerBlockAsItem(MSBlocks.WHITE_CHESS_BRICK_SLAB, MSItemGroup.MAIN);
	public static final RegistryObject<Item> COARSE_STONE_SLAB = registerBlockAsItem(MSBlocks.COARSE_STONE_SLAB, MSItemGroup.LANDS);
	public static final RegistryObject<Item> COARSE_STONE_BRICK_SLAB = registerBlockAsItem(MSBlocks.COARSE_STONE_BRICK_SLAB, MSItemGroup.LANDS);
	public static final RegistryObject<Item> CHALK_SLAB = registerBlockAsItem(MSBlocks.CHALK_SLAB, MSItemGroup.LANDS);
	public static final RegistryObject<Item> CHALK_BRICK_SLAB = registerBlockAsItem(MSBlocks.CHALK_BRICK_SLAB, MSItemGroup.LANDS);
	public static final RegistryObject<Item> PINK_STONE_SLAB = registerBlockAsItem(MSBlocks.PINK_STONE_SLAB, MSItemGroup.LANDS);
	public static final RegistryObject<Item> PINK_STONE_BRICK_SLAB = registerBlockAsItem(MSBlocks.PINK_STONE_BRICK_SLAB, MSItemGroup.LANDS);
	public static final RegistryObject<Item> BROWN_STONE_SLAB = registerBlockAsItem(MSBlocks.BROWN_STONE_SLAB, MSItemGroup.LANDS);
	public static final RegistryObject<Item> BROWN_STONE_BRICK_SLAB = registerBlockAsItem(MSBlocks.BROWN_STONE_BRICK_SLAB, MSItemGroup.LANDS);
	public static final RegistryObject<Item> GREEN_STONE_SLAB = registerBlockAsItem(MSBlocks.GREEN_STONE_SLAB, MSItemGroup.MAIN);
	public static final RegistryObject<Item> GREEN_STONE_BRICK_SLAB = registerBlockAsItem(MSBlocks.GREEN_STONE_BRICK_SLAB, MSItemGroup.MAIN);
	public static final RegistryObject<Item> RAINBOW_PLANKS_SLAB = registerBlockAsItem(MSBlocks.RAINBOW_PLANKS_SLAB, MSItemGroup.LANDS);
	public static final RegistryObject<Item> END_PLANKS_SLAB = registerBlockAsItem(MSBlocks.END_PLANKS_SLAB, MSItemGroup.LANDS);
	public static final RegistryObject<Item> DEAD_PLANKS_SLAB = registerBlockAsItem(MSBlocks.DEAD_PLANKS_SLAB, MSItemGroup.LANDS);
	public static final RegistryObject<Item> TREATED_PLANKS_SLAB = registerBlockAsItem(MSBlocks.TREATED_PLANKS_SLAB, MSItemGroup.LANDS);
	public static final RegistryObject<Item> BLACK_STONE_SLAB = registerBlockAsItem(MSBlocks.BLACK_STONE_SLAB, MSItemGroup.LANDS);
	public static final RegistryObject<Item> BLACK_STONE_BRICK_SLAB = registerBlockAsItem(MSBlocks.BLACK_STONE_BRICK_SLAB, MSItemGroup.LANDS);
	public static final RegistryObject<Item> MYCELIUM_SLAB = registerBlockAsItem(MSBlocks.MYCELIUM_SLAB, MSItemGroup.LANDS);
	public static final RegistryObject<Item> MYCELIUM_BRICK_SLAB = registerBlockAsItem(MSBlocks.MYCELIUM_BRICK_SLAB, MSItemGroup.LANDS);
	public static final RegistryObject<Item> FLOWERY_MOSSY_STONE_BRICK_SLAB = registerBlockAsItem(MSBlocks.FLOWERY_MOSSY_STONE_BRICK_SLAB, MSItemGroup.LANDS);
	public static final RegistryObject<Item> FROST_TILE_SLAB = registerBlockAsItem(MSBlocks.FROST_TILE_SLAB, MSItemGroup.LANDS);
	public static final RegistryObject<Item> FROST_BRICK_SLAB = registerBlockAsItem(MSBlocks.FROST_BRICK_SLAB, MSItemGroup.LANDS);
	public static final RegistryObject<Item> SHADE_SLAB = registerBlockAsItem(MSBlocks.SHADE_SLAB, MSItemGroup.LANDS);
	public static final RegistryObject<Item> SHADE_BRICK_SLAB = registerBlockAsItem(MSBlocks.SHADE_BRICK_SLAB, MSItemGroup.LANDS);
	
	public static final RegistryObject<Item> TRAJECTORY_BLOCK = registerBlockAsItem(MSBlocks.TRAJECTORY_BLOCK, MSItemGroup.LANDS);
	public static final RegistryObject<Item> STAT_STORER = registerBlockAsItem(MSBlocks.STAT_STORER, MSItemGroup.LANDS);
	public static final RegistryObject<Item> REMOTE_OBSERVER = registerBlockAsItem(MSBlocks.REMOTE_OBSERVER, MSItemGroup.LANDS);
	public static final RegistryObject<Item> WIRELESS_REDSTONE_TRANSMITTER = registerBlockAsItem(MSBlocks.WIRELESS_REDSTONE_TRANSMITTER, MSItemGroup.MAIN);
	public static final RegistryObject<Item> WIRELESS_REDSTONE_RECEIVER = registerBlockAsItem(MSBlocks.WIRELESS_REDSTONE_RECEIVER, MSItemGroup.MAIN);
	public static final RegistryObject<Item> SOLID_SWITCH = registerBlockAsItem(MSBlocks.SOLID_SWITCH, MSItemGroup.MAIN);
	public static final RegistryObject<Item> VARIABLE_SOLID_SWITCH = registerBlockAsItem(MSBlocks.VARIABLE_SOLID_SWITCH, MSItemGroup.MAIN);
	public static final RegistryObject<Item> ONE_SECOND_INTERVAL_TIMED_SOLID_SWITCH = registerBlockAsItem(MSBlocks.ONE_SECOND_INTERVAL_TIMED_SOLID_SWITCH, MSItemGroup.MAIN);
	public static final RegistryObject<Item> TWO_SECOND_INTERVAL_TIMED_SOLID_SWITCH = registerBlockAsItem(MSBlocks.TWO_SECOND_INTERVAL_TIMED_SOLID_SWITCH, MSItemGroup.MAIN);
	public static final RegistryObject<Item> SUMMONER = registerBlockAsItem(MSBlocks.SUMMONER, MSItemGroup.LANDS);
	public static final RegistryObject<Item> AREA_EFFECT_BLOCK = registerBlockAsItem(MSBlocks.AREA_EFFECT_BLOCK, MSItemGroup.LANDS);
	public static final RegistryObject<Item> PLATFORM_GENERATOR = registerBlockAsItem(MSBlocks.PLATFORM_GENERATOR, MSItemGroup.LANDS);
	public static final RegistryObject<Item> PLATFORM_RECEPTACLE = registerBlockAsItem(MSBlocks.PLATFORM_RECEPTACLE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> ITEM_MAGNET = registerBlockAsItem(MSBlocks.ITEM_MAGNET, MSItemGroup.MAIN);
	public static final RegistryObject<Item> REDSTONE_CLOCK = registerBlockAsItem(MSBlocks.REDSTONE_CLOCK, MSItemGroup.MAIN);
	public static final RegistryObject<Item> ROTATOR = registerBlockAsItem(MSBlocks.ROTATOR, MSItemGroup.MAIN);
	public static final RegistryObject<Item> TOGGLER = registerBlockAsItem(MSBlocks.TOGGLER, MSItemGroup.MAIN);
	public static final RegistryObject<Item> REMOTE_COMPARATOR = registerBlockAsItem(MSBlocks.REMOTE_COMPARATOR, MSItemGroup.LANDS);
	public static final RegistryObject<Item> STRUCTURE_CORE = registerBlockAsItem(MSBlocks.STRUCTURE_CORE, MSItemGroup.LANDS);
	public static final RegistryObject<Item> FALL_PAD = registerBlockAsItem(MSBlocks.FALL_PAD, MSItemGroup.MAIN);
	public static final RegistryObject<Item> FRAGILE_STONE = registerBlockAsItem(MSBlocks.FRAGILE_STONE, MSItemGroup.MAIN);
	public static final RegistryObject<Item> SPIKES = registerBlockAsItem(MSBlocks.SPIKES, MSItemGroup.MAIN);
	public static final RegistryObject<Item> RETRACTABLE_SPIKES = registerBlockAsItem(MSBlocks.RETRACTABLE_SPIKES, MSItemGroup.MAIN);
	public static final RegistryObject<Item> BLOCK_PRESSURE_PLATE = registerBlockAsItem(MSBlocks.BLOCK_PRESSURE_PLATE, MSItemGroup.MAIN);
	public static final RegistryObject<Item> PUSHABLE_BLOCK = registerBlockAsItem(MSBlocks.PUSHABLE_BLOCK, MSItemGroup.LANDS);
	public static final RegistryObject<Item> AND_GATE_BLOCK = registerBlockAsItem(MSBlocks.AND_GATE_BLOCK, MSItemGroup.LANDS);
	public static final RegistryObject<Item> OR_GATE_BLOCK = registerBlockAsItem(MSBlocks.OR_GATE_BLOCK, MSItemGroup.LANDS);
	public static final RegistryObject<Item> XOR_GATE_BLOCK = registerBlockAsItem(MSBlocks.XOR_GATE_BLOCK, MSItemGroup.LANDS);
	public static final RegistryObject<Item> NAND_GATE_BLOCK = registerBlockAsItem(MSBlocks.NAND_GATE_BLOCK, MSItemGroup.LANDS);
	public static final RegistryObject<Item> NOR_GATE_BLOCK = registerBlockAsItem(MSBlocks.NOR_GATE_BLOCK, MSItemGroup.LANDS);
	public static final RegistryObject<Item> XNOR_GATE_BLOCK = registerBlockAsItem(MSBlocks.XNOR_GATE_BLOCK, MSItemGroup.LANDS);
	
	public static final RegistryObject<Item> COMPUTER = registerBlockAsItem(MSBlocks.COMPUTER, MSItemGroup.MAIN);
	public static final RegistryObject<Item> LAPTOP = registerBlockAsItem(MSBlocks.LAPTOP, MSItemGroup.MAIN);
	public static final RegistryObject<Item> CROCKERTOP = registerBlockAsItem(MSBlocks.CROCKERTOP, MSItemGroup.MAIN);
	public static final RegistryObject<Item> HUBTOP = registerBlockAsItem(MSBlocks.HUBTOP, MSItemGroup.MAIN);
	public static final RegistryObject<Item> LUNCHTOP = registerBlockAsItem(MSBlocks.LUNCHTOP, MSItemGroup.MAIN);
	public static final RegistryObject<Item> OLD_COMPUTER = registerBlockAsItem(MSBlocks.OLD_COMPUTER, MSItemGroup.MAIN);
	public static final RegistryObject<Item> TRANSPORTALIZER = registerBlockItem(MSBlocks.TRANSPORTALIZER, block -> new TransportalizerItem(block, new Item.Properties().tab(MSItemGroup.MAIN)));
	//public static final RegistryObject<Item> TRANSPORTALIZER = ITEMS.register("transportalizer", block -> new TransportalizerItem(MSBlocks.TRANSPORTALIZER.get(), new Item.Properties().tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> TRANS_PORTALIZER = registerBlockItem(MSBlocks.TRANS_PORTALIZER, block -> new TransportalizerItem(block, new Item.Properties().tab(MSItemGroup.MAIN)));
	//public static final RegistryObject<Item> TRANS_PORTALIZER = ITEMS.register("trans_portalizer", () -> new TransportalizerItem(MSBlocks.TRANS_PORTALIZER.get(), new Item.Properties().tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> SENDIFICATOR = registerBlockItem(MSBlocks.SENDIFICATOR, block -> new SendificatorBlockItem(block, new Item.Properties().tab(MSItemGroup.MAIN).stacksTo(1)));
	//public static final RegistryObject<Item> SENDIFICATOR = ITEMS.register("sendificator", () -> new SendificatorBlockItem(MSBlocks.SENDIFICATOR.get(), new Item.Properties().tab(MSItemGroup.MAIN).stacksTo(1)));
	public static final RegistryObject<Item> GRIST_WIDGET = registerBlockAsItem(MSBlocks.GRIST_WIDGET, new Item.Properties().tab(MSItemGroup.MAIN).rarity(Rarity.UNCOMMON));
	public static final RegistryObject<Item> URANIUM_COOKER = registerBlockAsItem(MSBlocks.URANIUM_COOKER, MSItemGroup.MAIN);
	
	public static final RegistryObject<Item> GOLD_SEEDS = registerBlockAsItem(MSBlocks.GOLD_SEEDS, MSItemGroup.MAIN);
	public static final RegistryObject<Item> WOODEN_CACTUS = registerBlockAsItem(MSBlocks.WOODEN_CACTUS, MSItemGroup.MAIN);
	
	public static final RegistryObject<Item> APPLE_CAKE = registerBlockAsItem(MSBlocks.APPLE_CAKE, new Item.Properties().tab(MSItemGroup.MAIN).stacksTo(1));
	public static final RegistryObject<Item> BLUE_CAKE = registerBlockAsItem(MSBlocks.BLUE_CAKE, new Item.Properties().tab(MSItemGroup.MAIN).stacksTo(1));
	public static final RegistryObject<Item> COLD_CAKE = registerBlockAsItem(MSBlocks.COLD_CAKE, new Item.Properties().tab(MSItemGroup.MAIN).stacksTo(1));
	public static final RegistryObject<Item> RED_CAKE = registerBlockAsItem(MSBlocks.RED_CAKE, new Item.Properties().tab(MSItemGroup.MAIN).stacksTo(1));
	public static final RegistryObject<Item> HOT_CAKE = registerBlockAsItem(MSBlocks.HOT_CAKE, new Item.Properties().tab(MSItemGroup.MAIN).stacksTo(1));
	public static final RegistryObject<Item> REVERSE_CAKE = registerBlockAsItem(MSBlocks.REVERSE_CAKE, new Item.Properties().tab(MSItemGroup.MAIN).stacksTo(1));
	public static final RegistryObject<Item> FUCHSIA_CAKE = registerBlockAsItem(MSBlocks.FUCHSIA_CAKE, new Item.Properties().tab(MSItemGroup.MAIN).stacksTo(1));
	public static final RegistryObject<Item> NEGATIVE_CAKE = registerBlockAsItem(MSBlocks.NEGATIVE_CAKE, new Item.Properties().tab(MSItemGroup.MAIN).stacksTo(1));
	public static final RegistryObject<Item> CARROT_CAKE = registerBlockAsItem(MSBlocks.CARROT_CAKE, new Item.Properties().tab(MSItemGroup.MAIN).stacksTo(1));
	
	public static final RegistryObject<Item> PRIMED_TNT = registerBlockAsItem(MSBlocks.PRIMED_TNT, MSItemGroup.MAIN);
	public static final RegistryObject<Item> UNSTABLE_TNT = registerBlockAsItem(MSBlocks.UNSTABLE_TNT, MSItemGroup.MAIN);
	public static final RegistryObject<Item> INSTANT_TNT = registerBlockAsItem(MSBlocks.INSTANT_TNT, MSItemGroup.MAIN);
	public static final RegistryObject<Item> WOODEN_EXPLOSIVE_BUTTON = registerBlockAsItem(MSBlocks.WOODEN_EXPLOSIVE_BUTTON, MSItemGroup.MAIN);
	public static final RegistryObject<Item> STONE_EXPLOSIVE_BUTTON = registerBlockAsItem(MSBlocks.STONE_EXPLOSIVE_BUTTON, MSItemGroup.MAIN);
	
	public static final RegistryObject<Item> BLENDER = registerBlockAsItem(MSBlocks.BLENDER, MSItemGroup.MAIN);
	public static final RegistryObject<Item> CHESSBOARD = registerBlockAsItem(MSBlocks.CHESSBOARD, MSItemGroup.MAIN);
	public static final RegistryObject<Item> MINI_FROG_STATUE = registerBlockAsItem(MSBlocks.MINI_FROG_STATUE, MSItemGroup.MAIN);
	public static final RegistryObject<Item> MINI_WIZARD_STATUE = registerBlockAsItem(MSBlocks.MINI_WIZARD_STATUE, MSItemGroup.MAIN);
	public static final RegistryObject<Item> MINI_TYPHEUS_STATUE = registerBlockAsItem(MSBlocks.MINI_TYPHEUS_STATUE, MSItemGroup.MAIN);
	public static final RegistryObject<Item> CASSETTE_PLAYER = registerBlockItem(MSBlocks.CASSETTE_PLAYER, new Item.Properties().tab(MSItemGroup.MAIN));
	public static final RegistryObject<Item> GLOWYSTONE_DUST = registerBlockAsItem(MSBlocks.GLOWYSTONE_DUST, MSItemGroup.MAIN);
	/**/
	
	
	/**/
	private static RegistryObject<Item> registerBlockAsItem(RegistryObject<Block> block)
	{
		return registerBlockAsItem(block, new Item.Properties());
	}
	
	private static RegistryObject<Item> registerBlockAsItem(RegistryObject<Block> block, CreativeModeTab tab)
	{
		return registerBlockAsItem(block, new Item.Properties().tab(tab));
	}
	
	private static RegistryObject<Item> registerBlockAsItem(RegistryObject<Block> block, Item.Properties properties)
	{
		if(block.getKey() == null)
			throw new IllegalArgumentException(String.format("The provided RegistryObject<Block> %s has a block without a registry name!", block));
		return ITEMS.register(block.getKey().getRegistryName().getPath(), () -> new Item(properties));
	}
	
	/**
	 * Helper function to register a standard BlockItem
	 */
	private static RegistryObject<Item> registerBlockItem(RegistryObject<? extends Block> block, Item.Properties properties)
	{
		return registerBlockItem(block, block1 -> new BlockItem(block.get(), properties));
	}
	
	/**
	 * Helper function to register custom classes extending BlockItem, or is fed a standard BlockItem through the other registerBlockItem()
	 */
	private static RegistryObject<Item> registerBlockItem(RegistryObject<? extends Block> block, Function<Block, ? extends BlockItem> function)
	{
		return ITEMS.register(block.getKey().getRegistryName().getPath(), () -> function.apply(block.get())); //assumed getRegistryName will occur without fail as it works through DeferredRegistry
	}
}
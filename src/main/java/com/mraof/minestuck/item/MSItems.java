package com.mraof.minestuck.item;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.EnumCassetteType;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.effects.MSEffects;
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
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;

/**
 * This class initializes and registers all items and blockitems in the mod. Utilizes a DeferredRegister, so utilizations of items from this class will often require .get()
 */
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MSItems
{
	public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, Minestuck.MOD_ID);
	
	//Hammers
	public static final RegistryObject<Item> CLAW_HAMMER = REGISTER.register("claw_hammer", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 2, -2.8F).efficiency(1.0F).set(MSItemTypes.HAMMER_TOOL), new Item.Properties()));
	public static final RegistryObject<Item> SLEDGE_HAMMER = REGISTER.register("sledge_hammer", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 4, -3.2F).efficiency(4.0F).set(MSItemTypes.HAMMER_TOOL), new Item.Properties()));
	public static final RegistryObject<Item> MAILBOX = REGISTER.register("mailbox", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 4, -3.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties()));
	public static final RegistryObject<Item> BLACKSMITH_HAMMER = REGISTER.register("blacksmith_hammer", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 5, -3.2F).efficiency(3.5F).set(MSItemTypes.HAMMER_TOOL), new Item.Properties().defaultDurability(450)));
	public static final RegistryObject<Item> POGO_HAMMER = REGISTER.register("pogo_hammer", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.POGO_TIER, 5, -3.2F).efficiency(2.0F).set(MSItemTypes.HAMMER_TOOL).set(PogoEffect.EFFECT_07).add(PogoEffect.EFFECT_07), new Item.Properties()));
	public static final RegistryObject<Item> WRINKLEFUCKER = REGISTER.register("wrinklefucker", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.POGO_TIER, 7, -3.2F).efficiency(2.0F).set(MSItemTypes.HAMMER_TOOL).set(PogoEffect.EFFECT_04).add(PogoEffect.EFFECT_04), new Item.Properties()));
	public static final RegistryObject<Item> TELESCOPIC_SASSACRUSHER = REGISTER.register("telescopic_sassacrusher", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.BOOK_TIER, 11, -3.4F).efficiency(5.0F).set(MSItemTypes.HAMMER_TOOL), new Item.Properties().defaultDurability(1024)));
	public static final RegistryObject<Item> DEMOCRATIC_DEMOLITIONER = REGISTER.register("democratic_demolitioner", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 4, -3.0F).efficiency(1.0F).set(MSItemTypes.HAMMER_TOOL), new Item.Properties()));
	public static final RegistryObject<Item> BOOMBOX_BEATER = REGISTER.register("boombox_beater", () -> new MusicPlayerWeapon(new WeaponItem.Builder(Tiers.IRON, 7, -3.2F).efficiency(4.0F).set(MSItemTypes.HAMMER_TOOL), new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> REGI_HAMMER = REGISTER.register("regi_hammer", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 7, -3.2F).efficiency(8.0F).set(MSItemTypes.HAMMER_TOOL), new Item.Properties()));
	public static final RegistryObject<Item> FEAR_NO_ANVIL = REGISTER.register("fear_no_anvil", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.DENIZEN_TIER, 7, -3.2F).efficiency(7.0F).set(MSItemTypes.HAMMER_TOOL).add(OnHitEffect.TIME_SLOWNESS_AOE).add(OnHitEffect.enemyPotionEffect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1))), new Item.Properties().rarity(Rarity.RARE)));
	public static final RegistryObject<Item> MELT_MASHER = REGISTER.register("melt_masher", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 8, -3.2F).efficiency(12.0F).set(MSItemTypes.HAMMER_TOOL).add(OnHitEffect.setOnFire(25)), new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> ESTROGEN_EMPOWERED_EVERYTHING_ERADICATOR = REGISTER.register("estrogen_empowered_everything_eradicator", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 7, -3.2F).efficiency(9.0F).set(MSItemTypes.MULTI_TOOL).set(new FarmineEffect(Integer.MAX_VALUE, 200)).set(PogoEffect.EFFECT_07).add(PogoEffect.EFFECT_07), new Item.Properties().defaultDurability(6114)));
	public static final RegistryObject<Item> EEEEEEEEEEEE = REGISTER.register("eeeeeeeeeeee", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.SBAHJ_TIER, 9, -3.2F).efficiency(9.1F).set(MSItemTypes.HAMMER_TOOL).set(PogoEffect.EFFECT_02).add(PogoEffect.EFFECT_02, OnHitEffect.playSound(MSSoundEvents.ITEM_EEEEEEEEEEEE_HIT, 1.5F, 1.0F)), new Item.Properties().defaultDurability(6114)));
	public static final RegistryObject<Item> ZILLYHOO_HAMMER = REGISTER.register("zillyhoo_hammer", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ZILLY_TIER, 8, -3.2F).efficiency(15.0F).set(MSItemTypes.HAMMER_TOOL), new Item.Properties().rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> POPAMATIC_VRILLYHOO = REGISTER.register("popamatic_vrillyhoo", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ZILLY_TIER, 6, -3.2F).efficiency(15.0F).set(MSItemTypes.HAMMER_TOOL).add(OnHitEffect.RANDOM_DAMAGE), new Item.Properties().rarity(Rarity.RARE)));
	public static final RegistryObject<Item> SCARLET_ZILLYHOO = REGISTER.register("scarlet_zillyhoo", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CORUNDUM_TIER, 9, -3.2F).efficiency(4.0F).set(MSItemTypes.HAMMER_TOOL).add(OnHitEffect.setOnFire(50)), new Item.Properties().rarity(Rarity.RARE)));
	public static final RegistryObject<Item> MWRTHWL = REGISTER.register("mwrthwl", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.WELSH_TIER, 8, -3.2F).efficiency(4.0F).set(MSItemTypes.HAMMER_TOOL), new Item.Properties().rarity(Rarity.EPIC)));
	
	
	//Blades
	public static final RegistryObject<Item> SORD = REGISTER.register("sord", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.SBAHJ_TIER, 3, -3.0F).efficiency(1.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SORD_DROP), new Item.Properties()));
	public static final RegistryObject<Item> PAPER_SWORD = REGISTER.register("paper_sword", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.PAPER_TIER, 2, -2.4F).efficiency(3.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties()));
	public static final RegistryObject<Item> SWONGE = REGISTER.register("swonge", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 3, -2.4F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SWEEP).set(ItemRightClickEffect.absorbFluid(() -> Blocks.WATER, MSItems.WET_SWONGE)), new Item.Properties()));
	public static final RegistryObject<Item> WET_SWONGE = REGISTER.register("wet_swonge", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 3, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SWEEP).set(RightClickBlockEffect.placeFluid(() -> Blocks.WATER, MSItems.SWONGE)).add(OnHitEffect.playSound(() -> SoundEvents.GUARDIAN_FLOP)), new Item.Properties()));
	public static final RegistryObject<Item> PUMORD = REGISTER.register("pumord", () -> new WeaponItem(new WeaponItem.Builder(Tiers.STONE, 3, -2.4F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SWEEP).set(ItemRightClickEffect.absorbFluid(() -> Blocks.LAVA, MSItems.WET_PUMORD)), new Item.Properties()));
	public static final RegistryObject<Item> WET_PUMORD = REGISTER.register("wet_pumord", () -> new WeaponItem(new WeaponItem.Builder(Tiers.STONE, 3, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SWEEP).set(RightClickBlockEffect.placeFluid(() -> Blocks.LAVA, MSItems.PUMORD)).add(OnHitEffect.playSound(() -> SoundEvents.BUCKET_EMPTY_LAVA, 1.0F, 0.2F)).add(OnHitEffect.setOnFire(10)), new Item.Properties()));
	public static final RegistryObject<Item> CACTACEAE_CUTLASS = REGISTER.register("cactaceae_cutlass", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CACTUS_TIER, 3, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties())); //The sword harvestTool is only used against webs, hence the high efficiency.
	public static final RegistryObject<Item> STEAK_SWORD = REGISTER.register("steak_sword", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.MEAT_TIER, 4, -2.4F).efficiency(5.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).setEating(FinishUseItemEffect.foodEffect(8, 1F)), new Item.Properties().defaultDurability(250)));
	public static final RegistryObject<Item> BEEF_SWORD = REGISTER.register("beef_sword", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.MEAT_TIER, 2, -2.4F).efficiency(5.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).setEating(FinishUseItemEffect.foodEffect(3, 0.8F, 75)), new Item.Properties()));
	public static final RegistryObject<Item> IRRADIATED_STEAK_SWORD = REGISTER.register("irradiated_steak_sword", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.MEAT_TIER, 5, -2.4F).efficiency(5.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).setEating(FinishUseItemEffect.potionEffect(() -> new MobEffectInstance(MobEffects.WITHER, 100, 1), 0.9F), FinishUseItemEffect.foodEffect(4, 0.4F, 25)), new Item.Properties().defaultDurability(300)));
	public static final RegistryObject<Item> MACUAHUITL = REGISTER.register("macuahuitl", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.PRISMARINE_TIER, 3, -2.4F).efficiency(1.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().defaultDurability(100)));
	public static final RegistryObject<Item> FROSTY_MACUAHUITL = REGISTER.register("frosty_macuahuitl", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ICE_TIER, 6, -2.4F).efficiency(1.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).add(OnHitEffect.ICE_SHARD), new Item.Properties().defaultDurability(200)));
	public static final RegistryObject<Item> KATANA = REGISTER.register("katana", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 3, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties()));
	public static final RegistryObject<Item> UNBREAKABLE_KATANA = REGISTER.register("unbreakable_katana", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ZILLY_TIER, 6, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().defaultDurability(-1).rarity(Rarity.RARE))); //Actually unbreakable
	public static final RegistryObject<Item> ANGEL_APOCALYPSE = REGISTER.register("angel_apocalypse", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 5, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).add(OnHitEffect.HOPE_RESISTANCE), new Item.Properties().defaultDurability(2048).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> FIRE_POKER = REGISTER.register("fire_poker", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 4, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).add(OnHitEffect.setOnFire(30)), new Item.Properties()));
	public static final RegistryObject<Item> TOO_HOT_TO_HANDLE = REGISTER.register("too_hot_to_handle", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 3, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).add(OnHitEffect.setOnFire(10)), new Item.Properties().defaultDurability(350)));
	public static final RegistryObject<Item> CALEDSCRATCH = REGISTER.register("caledscratch", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 7, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().rarity(Rarity.RARE)));
	public static final RegistryObject<Item> CALEDFWLCH = REGISTER.register("caledfwlch", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.WELSH_TIER, 6, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> ROYAL_DERINGER = REGISTER.register("royal_deringer", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.DENIZEN_TIER, 7, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> CLAYMORE = REGISTER.register("claymore", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 5, -2.6F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().defaultDurability(600)));
	public static final RegistryObject<Item> CUTLASS_OF_ZILLYWAIR = REGISTER.register("cutlass_of_zillywair", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ZILLY_TIER, 6, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> REGISWORD = REGISTER.register("regisword", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 5, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties()));
	public static final RegistryObject<Item> CRUEL_FATE_CRUCIBLE = REGISTER.register("cruel_fate_crucible", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.DENIZEN_TIER, 4, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().defaultDurability(4096).rarity(Rarity.RARE))); //Special property in ServerEventHandler
	public static final RegistryObject<Item> SCARLET_RIBBITAR = REGISTER.register("scarlet_ribbitar", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CORUNDUM_TIER, 7, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().rarity(Rarity.RARE)));
	public static final RegistryObject<Item> DOGG_MACHETE = REGISTER.register("dogg_machete", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 6, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().defaultDurability(1000).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> COBALT_SABRE = REGISTER.register("cobalt_sabre", () -> new WeaponItem(new WeaponItem.Builder(Tiers.GOLD, 7, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).add(OnHitEffect.setOnFire(30)), new Item.Properties().defaultDurability(300)));
	public static final RegistryObject<Item> QUANTUM_SABRE = REGISTER.register("quantum_sabre", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.URANIUM_TIER, 4, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).add(OnHitEffect.onCrit(OnHitEffect.enemyPotionEffect(() -> new MobEffectInstance(MobEffects.WITHER, 100, 1)))), new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> SHATTER_BEACON = REGISTER.register("shatter_beacon", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 7, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().rarity(Rarity.RARE)));
	public static final RegistryObject<Item> SHATTER_BACON = REGISTER.register("shatter_bacon", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.MEAT_TIER, 7, -2.4F).efficiency(5.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).add(OnHitEffect.SORD_DROP), new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> SUBTRACTSHUMIDIRE_ZOMORRODNEGATIVE = REGISTER.register("subtractshumidire_zomorrodnegative", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.URANIUM_TIER, 6, -2.6F).efficiency(5.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).add(OnHitEffect.setOnFire(30)), new Item.Properties()));
	public static final RegistryObject<Item> MUSIC_SWORD = REGISTER.register("music_sword", () -> new MusicPlayerWeapon(new WeaponItem.Builder(Tiers.DIAMOND, 6, -2.4F).efficiency(5.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> PILLOW_TALK = REGISTER.register("pillow_talk", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ZILLY_TIER, 6, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).add(OnHitEffect.setOnFire(10)), new Item.Properties().defaultDurability(2031).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> KRAKENS_EYE = REGISTER.register("krakens_eye", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.PRISMARINE_TIER, 5, -2.6F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).set(ItemRightClickEffect.playerPotionEffect(() -> new MobEffectInstance(MobEffects.CONDUIT_POWER, 120, 0), 1, 120)), new Item.Properties().defaultDurability(650)));
	public static final RegistryObject<Item> CINNAMON_SWORD = REGISTER.register("cinnamon_sword", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CANDY_TIER, 4, -2.4F).efficiency(1.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).add(OnHitEffect.SET_CANDY_DROP_FLAG), new Item.Properties()));
	public static final RegistryObject<Item> UNION_BUSTER = REGISTER.register("union_buster", () -> new WeaponItem(new WeaponItem.Builder(Tiers.GOLD, 9, -3.5F).efficiency(1.0f).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.sweepMultiEffect(OnHitEffect.SPREADING_KNOCKBACK, OnHitEffect.enemyPotionEffect(() -> new MobEffectInstance(MSEffects.SUSPICION.get(), 1200, 3, false, false)))), new Item.Properties().defaultDurability(505).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> CHAINSAW_KATANA = REGISTER.register("chainsaw_katana", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 4, -2.4F).efficiency(3.0F).set(MSItemTypes.AXE_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().defaultDurability(700)));
	public static final RegistryObject<Item> THORN_IN_YOUR_SIDE = REGISTER.register("thorn_in_your_side", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 3, -2.4F).efficiency(1.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties()));
	public static final RegistryObject<Item> ROSE_PROTOCOL = REGISTER.register("rose_protocol", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 3, -2.4F).efficiency(1.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).add(OnHitEffect.setOnFire(6)), new Item.Properties().rarity(Rarity.UNCOMMON)));
	
	//Knives
	public static final RegistryObject<Item> DAGGER = REGISTER.register("dagger", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 0, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.backstab(3)), new Item.Properties()));
	public static final RegistryObject<Item> DIAMOND_DAGGER = REGISTER.register("diamond_dagger", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 0, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.backstab(4)), new Item.Properties()));
	public static final RegistryObject<Item> PIGLINS_PRIDE = REGISTER.register("piglins_pride", () -> new WeaponItem(new WeaponItem.Builder(Tiers.NETHERITE, 0, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.backstab(4)).add(OnHitEffect.targetSpecificAdditionalDamage(4, () -> EntityType.PIGLIN)), new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> BASILISK_BREATH_DRAGONSLAYER = REGISTER.register("basilisk_breath_dragonslayer", () -> new WeaponItem(new WeaponItem.Builder(Tiers.NETHERITE, 1, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.backstab(4)).add(OnHitEffect.targetSpecificAdditionalDamage(6, MSEntityTypes.BASILISK)), new Item.Properties().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> HALLOWED_SKEWER = REGISTER.register("hallowed_skewer", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 1, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.backstab(9)), new Item.Properties().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> GENESIS_GODSTABBER = REGISTER.register("genesis_godstabber", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.WELSH_TIER, 1, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.backstab(9)), new Item.Properties().defaultDurability(1561).fireResistant().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> NIFE = REGISTER.register("nife", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.SBAHJ_TIER, 1, -2.0F).add(OnHitEffect.SORD_DROP), new Item.Properties()));
	public static final RegistryObject<Item> LIGHT_OF_MY_KNIFE = REGISTER.register("light_of_my_knife", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.EMERALD_TIER, 1, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.backstab(4)).add(OnHitEffect.setOnFire(35)), new Item.Properties()));
	public static final RegistryObject<Item> THOUSAND_DEGREE_KNIFE = REGISTER.register("thousand_degree_knife", () -> new WeaponItem(new WeaponItem.Builder(Tiers.NETHERITE, 0, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.backstab(4)).add(OnHitEffect.setOnFire(35)), new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> STARSHARD_TRI_BLADE = REGISTER.register("starshard_tri_blade", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CORUNDUM_TIER, 1, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.backstab(9)), new Item.Properties()));
	public static final RegistryObject<Item> TOOTHRIPPER = REGISTER.register("toothripper", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 3, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.backstab(4)), new Item.Properties().defaultDurability(1200)));
	public static final RegistryObject<Item> SHADOWRAZOR = REGISTER.register("shadowrazor", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 4, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.backstab(4)).add(OnHitEffect.onCrit(OnHitEffect.enemyPotionEffect(() -> new MobEffectInstance(MobEffects.POISON, 100, 0)))), new Item.Properties().defaultDurability(1500)));
	public static final RegistryObject<Item> PRINCESS_PERIL = REGISTER.register("princess_peril", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 5, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.backstab(4)).add(OnHitEffect.onCrit(OnHitEffect.enemyPotionEffect(() -> new MobEffectInstance(MobEffects.POISON, 100, 0)))), new Item.Properties().defaultDurability(1650)));
	
	
	//Keys
	public static final RegistryObject<Item> HOUSE_KEY = REGISTER.register("house_key", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, -1, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties()));
	public static final RegistryObject<Item> KEYBLADE = REGISTER.register("keyblade", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 0, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties()));
	public static final RegistryObject<Item> CANDY_KEY = REGISTER.register("candy_key", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CANDY_TIER, 1, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SET_CANDY_DROP_FLAG), new Item.Properties()));
	public static final RegistryObject<Item> LOCKSOFTENER = REGISTER.register("locksoftener", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 1, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().defaultDurability(500)));
	public static final RegistryObject<Item> BISEKEYAL = REGISTER.register("bisekeyal", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 2, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().defaultDurability(450)));
	public static final RegistryObject<Item> LATCHMELTER = REGISTER.register("latchmelter", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CORUNDUM_TIER, 1, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.setOnFire(6)), new Item.Properties().defaultDurability(450)));
	public static final RegistryObject<Item> KEY_TO_THE_MACHINE = REGISTER.register("key_to_the_machine", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.EMERALD_TIER, 1, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties()));
	public static final RegistryObject<Item> KEY_TO_THE_CITY = REGISTER.register("key_to_the_city", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 1, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().defaultDurability(650).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> INNER_HEART = REGISTER.register("inner_heart", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 1, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.spawnParticles(ParticleTypes.HEART, 15, 2.0, 1.0, 2.0, 2.0)), new Item.Properties().defaultDurability(460)));
	public static final RegistryObject<Item> CRIMSON_LEAP = REGISTER.register("crimson_leap", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CORUNDUM_TIER, 2, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().defaultDurability(2100).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> LOCH_PICK = REGISTER.register("loch_pick", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.HORRORTERROR_TIER, 1, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties()));
	public static final RegistryObject<Item> DRAGON_KEY = REGISTER.register("dragon_key", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 1, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties()));
	public static final RegistryObject<Item> TRUE_BLUE = REGISTER.register("true_blue", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 1, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.RANDOM_DAMAGE), new Item.Properties().defaultDurability(888)));
	public static final RegistryObject<Item> BLUE_BEAMS = REGISTER.register("blue_beams", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 1, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.RANDOM_DAMAGE), new Item.Properties().defaultDurability(1616)));
	public static final RegistryObject<Item> INKSPLOCKER_UNLOCKER = REGISTER.register("inksplocker_unlocker", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.PAPER_TIER, 2, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.onCrit(OnHitEffect.enemyPotionEffect(() -> new MobEffectInstance(MobEffects.POISON, 100, 0)))), new Item.Properties()));
	public static final RegistryObject<Item> INKSQUIDDER_DEPTHKEY = REGISTER.register("inksquidder_depthkey", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.PRISMARINE_TIER, 0, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.onCrit(OnHitEffect.enemyPotionEffect(() -> new MobEffectInstance(MobEffects.POISON, 100, 0)))), new Item.Properties()));
	public static final RegistryObject<Item> REGIKEY = REGISTER.register("regikey", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 2, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties()));
	public static final RegistryObject<Item> CLOCKKEEPER = REGISTER.register("clockkeeper", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 3, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().defaultDurability(3500).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> HOME_BY_MIDNIGHT = REGISTER.register("home_by_midnight", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 3, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(PogoEffect.EFFECT_07).add(PogoEffect.EFFECT_07), new Item.Properties().defaultDurability(4000).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> NO_TIME_FOR_FLIES = REGISTER.register("no_time_for_flies", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CORUNDUM_TIER, 3, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().defaultDurability(2500).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> NATURES_HEART = REGISTER.register("natures_heart", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CACTUS_TIER, 4, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().defaultDurability(2005)));
	public static final RegistryObject<Item> YALDABAOTHS_KEYTON = REGISTER.register("yaldabaoths_keyton", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.DENIZEN_TIER, 3, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.enemyKnockback(2.0F)).add(OnHitEffect.BOSS_BUSTER).add(OnHitEffect.playSound(MSSoundEvents.ITEM_BATON_ORCHESTRA)), new Item.Properties().defaultDurability(4000).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> KEYTAR = REGISTER.register("keytar", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 2, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.playSound(SoundEvents.NOTE_BLOCK_GUITAR)).add(OnHitEffect.spawnParticles(ParticleTypes.NOTE, 10, 1.0, 2.0, 1.0, 2.0)), new Item.Properties().defaultDurability(265)));
	public static final RegistryObject<Item> ALLWEDDOL = REGISTER.register("allweddol", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.WELSH_TIER, 6, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties()));
	
	//Batons
	public static final RegistryObject<Item> CONDUCTORS_BATON = REGISTER.register("conductors_baton", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 1, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.playSound(MSSoundEvents.ITEM_BATON_ORCHESTRA, 1, 1.0F)).add(OnHitEffect.spawnParticles(ParticleTypes.NOTE, 10, 1.0, 2.0, 1.0, 2.0)), new Item.Properties().defaultDurability(200)));
	public static final RegistryObject<Item> SHARP_NOTE = REGISTER.register("sharp_note", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 2, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.playSound(MSSoundEvents.ITEM_BATON_ORCHESTRA, 1, 2.0F)).add(OnHitEffect.spawnParticles(ParticleTypes.NOTE, 12, 1.0, 2.0, 1.0, 2.0)), new Item.Properties().defaultDurability(350)));
	public static final RegistryObject<Item> URANIUM_BATON = REGISTER.register("uranium_baton", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.URANIUM_TIER, 0, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.playSound(MSSoundEvents.ITEM_BATON_ORCHESTRA, 1, 0.8F)).add(OnHitEffect.spawnParticles(ParticleTypes.ITEM_SLIME, 10, 1.0, 2.0, 1.0, 2.0)), new Item.Properties().defaultDurability(285)));
	public static final RegistryObject<Item> WIND_WAKER = REGISTER.register("wind_waker", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CORUNDUM_TIER, 1, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(MagicRangedRightClickEffect.STANDARD_MAGIC).add(OnHitEffect.enemyKnockback(1.3F)).add(OnHitEffect.playSound(MSSoundEvents.ITEM_BATON_ORCHESTRA, 1, 1.2F)).add(OnHitEffect.spawnParticles(ParticleTypes.NOTE, 8, 1.0, 2.0, 1.0, 2.0)), new Item.Properties().defaultDurability(750)));
	public static final RegistryObject<Item> CELESTIAL_FULCRUM = REGISTER.register("celestial_fulcrum", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 3, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(MagicRangedRightClickEffect.STANDARD_MAGIC).add(OnHitEffect.enemyKnockback(2.0F)).add(OnHitEffect.playSound(MSSoundEvents.ITEM_BATON_ORCHESTRA, 1, 1.5F)).add(OnHitEffect.spawnParticles(ParticleTypes.NOTE, 14, 1.0, 2.0, 1.0, 2.0)), new Item.Properties().defaultDurability(1200)));
	public static final RegistryObject<Item> HYMN_FOR_HORRORTERRORS = REGISTER.register("hymn_for_horrorterrors", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.HORRORTERROR_TIER, 1, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.HORRORTERROR).set(MagicRangedRightClickEffect.HORRORTERROR_MAGIC).add(OnHitEffect.enemyKnockback(2.0F)).add(OnHitEffect.playSound(MSSoundEvents.ITEM_BATON_ORCHESTRA, 1, 0.2F)).add(OnHitEffect.spawnParticles(ParticleTypes.SCULK_SOUL, 15, 1.0, 2.0, 1.0, 2.0)), new Item.Properties().defaultDurability(2500).rarity(Rarity.RARE)));
	
	//Axes
	public static final RegistryObject<Item> BATLEACKS = REGISTER.register("batleacks", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.SBAHJ_TIER, 3, -3.5F).efficiency(1.0F).set(MSItemTypes.AXE_TOOL).add(OnHitEffect.SORD_DROP), new Item.Properties()));
	public static final RegistryObject<Item> COPSE_CRUSHER = REGISTER.register("copse_crusher", () -> new WeaponItem(new WeaponItem.Builder(Tiers.STONE, 5, -3.0F).efficiency(6.0F).disableShield().set(MSItemTypes.AXE_TOOL).set(new FarmineEffect(Integer.MAX_VALUE, 20)), new Item.Properties().defaultDurability(400)));
	public static final RegistryObject<Item> QUENCH_CRUSHER = REGISTER.register("quench_crusher", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 7, -3.0F).efficiency(2.0F).disableShield().set(MSItemTypes.AXE_TOOL).setEating(FinishUseItemEffect.foodEffect(6, 0.6F, 75)), new Item.Properties().defaultDurability(500)));
	public static final RegistryObject<Item> MELONSBANE = REGISTER.register("melonsbane", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 8, -3.0F).efficiency(3.0F).disableShield().set(MSItemTypes.AXE_TOOL).set(DestroyBlockEffect.extraHarvests(true, 0.6F, 20, () -> Items.MELON_SLICE, () -> Blocks.MELON)), new Item.Properties().defaultDurability(400)));
	public static final RegistryObject<Item> CROP_CHOP = REGISTER.register("crop_chop", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 7, -3.0F).efficiency(3.0F).disableShield().set(MSItemTypes.AXE_TOOL).set(DestroyBlockEffect.DOUBLE_FARM), new Item.Properties().defaultDurability(800)));
	public static final RegistryObject<Item> THE_LAST_STRAW = REGISTER.register("the_last_straw", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 9, -3.0F).efficiency(3.0F).disableShield().set(MSItemTypes.AXE_TOOL).set(DestroyBlockEffect.DOUBLE_FARM), new Item.Properties().defaultDurability(950)));
	public static final RegistryObject<Item> BATTLEAXE = REGISTER.register("battleaxe", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 8, -3.0F).efficiency(3.0F).disableShield().set(MSItemTypes.AXE_TOOL), new Item.Properties().defaultDurability(600)));
	public static final RegistryObject<Item> CANDY_BATTLEAXE = REGISTER.register("candy_battleaxe", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CANDY_TIER, 8, -3.0F).efficiency(2.0F).disableShield().set(MSItemTypes.AXE_TOOL).add(OnHitEffect.SET_CANDY_DROP_FLAG), new Item.Properties().defaultDurability(111)));
	public static final RegistryObject<Item> CHOCO_LOCO_WOODSPLITTER = REGISTER.register("choco_loco_woodsplitter", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CANDY_TIER, 9, -3.0F).efficiency(2.0F).disableShield().set(MSItemTypes.AXE_TOOL).setEating(FinishUseItemEffect.foodEffect(8, 0.4F)), new Item.Properties().defaultDurability(350)));
	public static final RegistryObject<Item> STEEL_EDGE_CANDYCUTTER = REGISTER.register("steel_edge_candycutter", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 8, -3.0F).efficiency(3.0F).disableShield().set(MSItemTypes.AXE_TOOL).add(OnHitEffect.SET_CANDY_DROP_FLAG), new Item.Properties().defaultDurability(800).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> BLACKSMITH_BANE = REGISTER.register("blacksmith_bane", () -> new WeaponItem(new WeaponItem.Builder(Tiers.STONE, 8, -3.0F).efficiency(6.0F).disableShield().set(MSItemTypes.AXE_TOOL), new Item.Properties().defaultDurability(413)));
	public static final RegistryObject<Item> REGIAXE = REGISTER.register("regiaxe", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 6, -3.0F).disableShield().efficiency(6.0F).set(MSItemTypes.AXE_TOOL), new Item.Properties()));
	public static final RegistryObject<Item> GOTHY_AXE = REGISTER.register("gothy_axe", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 8, -3.0F).efficiency(6.0F).disableShield().set(MSItemTypes.AXE_TOOL), new Item.Properties()));
	public static final RegistryObject<Item> SURPRISE_AXE = REGISTER.register("surprise_axe", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 7, -3.0F).efficiency(6.0F).disableShield().set(MSItemTypes.AXE_TOOL).add(OnHitEffect.KUNDLER_SURPRISE), new Item.Properties().defaultDurability(600)));
	public static final RegistryObject<Item> SHOCK_AXE = REGISTER.register("shock_axe", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 8, -3.0F).efficiency(6.0F).disableShield().set(MSItemTypes.AXE_TOOL).set(ItemRightClickEffect.switchTo(MSItems.SHOCK_AXE_UNPOWERED)).add(OnHitEffect.DROP_FOE_ITEM).add(InventoryTickEffect.DROP_WHEN_IN_WATER).add(OnHitEffect.playSound(MSSoundEvents.EVENT_ELECTRIC_SHOCK, 0.6F, 1.0F)), new Item.Properties().defaultDurability(800)));
	public static final RegistryObject<Item> SHOCK_AXE_UNPOWERED = REGISTER.register("shock_axe_unpowered", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 8, -3.0F).efficiency(6.0F).disableShield().set(MSItemTypes.AXE_TOOL).set(ItemRightClickEffect.switchTo(MSItems.SHOCK_AXE)), new Item.Properties().defaultDurability(800)));
	public static final RegistryObject<Item> SCRAXE = REGISTER.register("scraxe", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 8, -3.0F).efficiency(7.0F).disableShield().set(MSItemTypes.AXE_TOOL), new Item.Properties().defaultDurability(500)));
	public static final RegistryObject<Item> LORENTZ_DISTRANSFORMATIONER = REGISTER.register("lorentz_distransformationer", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 6, -3.0F).efficiency(7.0F).disableShield().set(MSItemTypes.AXE_TOOL).add(OnHitEffect.SPACE_TELEPORT), new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> PISTON_POWERED_POGO_AXEHAMMER = REGISTER.register("piston_powered_pogo_axehammer", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.POGO_TIER, 6, -3.0F).efficiency(2.0F).disableShield().set(MSItemTypes.AXE_HAMMER_TOOL).set(new FarmineEffect(Integer.MAX_VALUE, 50)).set(PogoEffect.EFFECT_06).add(PogoEffect.EFFECT_06), new Item.Properties().defaultDurability(800)));
	public static final RegistryObject<Item> RUBY_CROAK = REGISTER.register("ruby_croak", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CORUNDUM_TIER, 8, -3.0F).efficiency(8.0F).disableShield().set(MSItemTypes.AXE_TOOL), new Item.Properties().rarity(Rarity.RARE)));
	public static final RegistryObject<Item> HEPHAESTUS_LUMBERJACK = REGISTER.register("hephaestus_lumberjack", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CORUNDUM_TIER, 7, -3.0F).efficiency(9.0F).disableShield().set(MSItemTypes.AXE_TOOL).add(OnHitEffect.setOnFire(30)), new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> FISSION_FOCUSED_FAULT_FELLER = REGISTER.register("fission_focused_fault_feller", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 7, -3.0F).efficiency(5.0F).disableShield().set(MSItemTypes.AXE_HAMMER_TOOL).set(new FarmineEffect(Integer.MAX_VALUE, 100)).set(PogoEffect.EFFECT_07).add(PogoEffect.EFFECT_07), new Item.Properties().defaultDurability(2048).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> BISECTOR = REGISTER.register("bisector", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 9, -3.2F).efficiency(5.0F).disableShield().set(MSItemTypes.AXE_TOOL), new Item.Properties().defaultDurability(600)));
	public static final RegistryObject<Item> FINE_CHINA_AXE = REGISTER.register("fine_china_axe", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 9, -3.2F).efficiency(1.0F).disableShield().set(MSItemTypes.AXE_TOOL), new Item.Properties().defaultDurability(8)));
	
	
	
	//Dice
	public static final RegistryObject<Item> FLUORITE_OCTET = REGISTER.register("fluorite_octet", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 4, -3.0F).efficiency(1.0F).add(OnHitEffect.RANDOM_DAMAGE), new Item.Properties().defaultDurability(4096).rarity(Rarity.EPIC)));
	
	
	//Claws
	public static final RegistryObject<Item> MAKESHIFT_CLAWS_DRAWN = REGISTER.register("makeshift_claws_drawn", () -> new WeaponItem(new WeaponItem.Builder(Tiers.STONE, 1, -1.5F).efficiency(10.0F).set(MSItemTypes.CLAWS_TOOL).set(ItemRightClickEffect.switchTo(MSItems.MAKESHIFT_CLAWS_SHEATHED)), new Item.Properties().defaultDurability(200)));
	public static final RegistryObject<Item> MAKESHIFT_CLAWS_SHEATHED = REGISTER.register("makeshift_claws_sheathed", () -> new WeaponItem(new WeaponItem.Builder(Tiers.STONE, -1, -1.0F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(MSItems.MAKESHIFT_CLAWS_DRAWN)), new Item.Properties().defaultDurability(200)));
	public static final RegistryObject<Item> CAT_CLAWS_DRAWN = REGISTER.register("cat_claws_drawn", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 2, -1.5F).efficiency(10.0F).set(MSItemTypes.CLAWS_TOOL).set(ItemRightClickEffect.switchTo(MSItems.CAT_CLAWS_SHEATHED)), new Item.Properties().defaultDurability(500)));
	public static final RegistryObject<Item> CAT_CLAWS_SHEATHED = REGISTER.register("cat_claws_sheathed", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, -1, -1.0F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(MSItems.CAT_CLAWS_DRAWN)), new Item.Properties().defaultDurability(500)));
	public static final RegistryObject<Item> POGO_CLAWS = REGISTER.register("pogo_claws", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.POGO_TIER, 2, -1.5F).efficiency(10.0F).set(MSItemTypes.CLAWS_TOOL).set(PogoEffect.EFFECT_07).add(PogoEffect.EFFECT_07), new Item.Properties()));
	public static final RegistryObject<Item> ATOMIKITTY_KATAR_DRAWN = REGISTER.register("atomikitty_katar_drawn", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.URANIUM_TIER, 1, -1.5F).efficiency(10.0F).set(MSItemTypes.CLAWS_TOOL).set(ItemRightClickEffect.switchTo(MSItems.ATOMIKITTY_KATAR_SHEATHED)).add(OnHitEffect.onCrit(OnHitEffect.enemyPotionEffect(() -> new MobEffectInstance(MobEffects.WITHER, 100, 1)))), new Item.Properties().defaultDurability(600)));
	public static final RegistryObject<Item> ATOMIKITTY_KATAR_SHEATHED = REGISTER.register("atomikitty_katar_sheathed", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.URANIUM_TIER, -1, -1.0F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(MSItems.ATOMIKITTY_KATAR_DRAWN)), new Item.Properties().defaultDurability(600)));
	public static final RegistryObject<Item> SKELETONIZER_DRAWN = REGISTER.register("skeletonizer_drawn", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 5, -1.5F).efficiency(10.0F).set(MSItemTypes.CLAWS_TOOL).set(ItemRightClickEffect.switchTo(MSItems.SKELETONIZER_SHEATHED)), new Item.Properties().defaultDurability(750)));
	public static final RegistryObject<Item> SKELETONIZER_SHEATHED = REGISTER.register("skeletonizer_sheathed", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, -1, -1.0F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(MSItems.SKELETONIZER_DRAWN)), new Item.Properties().defaultDurability(750)));
	public static final RegistryObject<Item> SKELETON_DISPLACER_DRAWN = REGISTER.register("skeleton_displacer_drawn", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 6, -1.5F).efficiency(10.0F).set(MSItemTypes.CLAWS_TOOL).set(ItemRightClickEffect.switchTo(MSItems.SKELETON_DISPLACER_SHEATHED)).add(OnHitEffect.targetSpecificAdditionalDamage(4, () -> EntityType.SKELETON)), new Item.Properties().defaultDurability(1250)));
	public static final RegistryObject<Item> SKELETON_DISPLACER_SHEATHED = REGISTER.register("skeleton_displacer_sheathed", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, -1, -1.0F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(MSItems.SKELETON_DISPLACER_DRAWN)), new Item.Properties().defaultDurability(1250)));
	public static final RegistryObject<Item> TEARS_OF_THE_ENDERLICH_DRAWN = REGISTER.register("tears_of_the_enderlich_drawn", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CORUNDUM_TIER, 4, -1.5F).efficiency(10.0F).set(MSItemTypes.CLAWS_TOOL).set(ItemRightClickEffect.switchTo(MSItems.TEARS_OF_THE_ENDERLICH_SHEATHED)).add(OnHitEffect.targetSpecificAdditionalDamage(6, MSEntityTypes.LICH)), new Item.Properties().defaultDurability(2000).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> TEARS_OF_THE_ENDERLICH_SHEATHED = REGISTER.register("tears_of_the_enderlich_sheathed", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CORUNDUM_TIER, -4, -1.0F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(MSItems.TEARS_OF_THE_ENDERLICH_DRAWN)), new Item.Properties().defaultDurability(2000).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> LION_LACERATORS_DRAWN = REGISTER.register("lion_lacerators_drawn", () -> new WeaponItem(new WeaponItem.Builder(Tiers.NETHERITE, 4, -1.5F).efficiency(10.0F).set(MSItemTypes.CLAWS_TOOL).set(ItemRightClickEffect.switchTo(MSItems.LION_LACERATORS_SHEATHED)).add(OnHitEffect.targetSpecificAdditionalDamage(4, MSEntityTypes.LICH)), new Item.Properties().defaultDurability(2031).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> LION_LACERATORS_SHEATHED = REGISTER.register("lion_lacerators_sheathed", () -> new WeaponItem(new WeaponItem.Builder(Tiers.NETHERITE, -4, -1.0F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(MSItems.LION_LACERATORS_DRAWN)), new Item.Properties().defaultDurability(2031).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> ACTION_CLAWS_DRAWN = REGISTER.register("action_claws_drawn", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 5, -1.5F).efficiency(10.0F).set(MSItemTypes.CLAWS_TOOL).set(ItemRightClickEffect.switchTo(MSItems.ACTION_CLAWS_SHEATHED)), new Item.Properties().defaultDurability(4096).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> ACTION_CLAWS_SHEATHED = REGISTER.register("action_claws_sheathed", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, -1, -1.0F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(MSItems.ACTION_CLAWS_DRAWN)), new Item.Properties().defaultDurability(4096).rarity(Rarity.RARE)));
	
	
	//Chainsaws
	public static final RegistryObject<Item> LIPSTICK_CHAINSAW = REGISTER.register("lipstick_chainsaw", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 2, -1.5F).efficiency(10.0F).set(MSItemTypes.AXE_TOOL).set(ItemRightClickEffect.switchTo(MSItems.LIPSTICK)), new Item.Properties().defaultDurability(250)));
	public static final RegistryObject<Item> LIPSTICK = REGISTER.register("lipstick", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, -1, -0.5F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(MSItems.LIPSTICK_CHAINSAW)), new Item.Properties().defaultDurability(250)));
	public static final RegistryObject<Item> CAKESAW = REGISTER.register("cakesaw", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CANDY_TIER, 3, -1.5F).efficiency(10.0F).set(MSItemTypes.AXE_TOOL).set(ItemRightClickEffect.switchTo(MSItems.CAKESAW_LIPSTICK)).add(OnHitEffect.SET_CANDY_DROP_FLAG), new Item.Properties()));
	public static final RegistryObject<Item> CAKESAW_LIPSTICK = REGISTER.register("cakesaw_lipstick", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, -1, -0.5F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(CAKESAW)), new Item.Properties().defaultDurability(450)));
	public static final RegistryObject<Item> MAGENTA_MAULER = REGISTER.register("magenta_mauler", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 5, -1.5F).efficiency(10.0F).set(MSItemTypes.AXE_TOOL).set(ItemRightClickEffect.switchTo(MSItems.MAGENTA_MAULER_LIPSTICK)), new Item.Properties().defaultDurability(500)));
	public static final RegistryObject<Item> MAGENTA_MAULER_LIPSTICK = REGISTER.register("magenta_mauler_lipstick", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, -1, -0.5F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(MSItems.MAGENTA_MAULER)), new Item.Properties().defaultDurability(500)));
	public static final RegistryObject<Item> THISTLEBLOWER = REGISTER.register("thistleblower", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 4, -1.0F).efficiency(10.0F).set(MSItemTypes.AXE_TOOL).set(ItemRightClickEffect.switchTo(MSItems.THISTLEBLOWER_LIPSTICK)), new Item.Properties().defaultDurability(500)));
	public static final RegistryObject<Item> THISTLEBLOWER_LIPSTICK = REGISTER.register("thistleblower_lipstick", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, -1, -0.5F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(MSItems.THISTLEBLOWER)), new Item.Properties().defaultDurability(500)));
	public static final RegistryObject<Item> HAND_CRANKED_VAMPIRE_ERASER = REGISTER.register("hand_cranked_vampire_eraser", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 4, -1.5F).efficiency(10.0F).set(MSItemTypes.AXE_TOOL).set(ItemRightClickEffect.switchTo(MSItems.HAND_CRANKED_VAMPIRE_ERASER_LIPSTICK)), new Item.Properties().defaultDurability(250)));
	public static final RegistryObject<Item> HAND_CRANKED_VAMPIRE_ERASER_LIPSTICK = REGISTER.register("hand_cranked_vampire_eraser_lipstick", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 1, -0.5F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(MSItems.HAND_CRANKED_VAMPIRE_ERASER)), new Item.Properties().defaultDurability(250)));
	public static final RegistryObject<Item> EMERALD_IMMOLATOR = REGISTER.register("emerald_immolator", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.EMERALD_TIER, 3, -1.5F).efficiency(10.0F).set(MSItemTypes.AXE_TOOL).set(ItemRightClickEffect.switchTo(MSItems.EMERALD_IMMOLATOR_LIPSTICK)).add(OnHitEffect.setOnFire(5)), new Item.Properties().defaultDurability(1024)));
	public static final RegistryObject<Item> EMERALD_IMMOLATOR_LIPSTICK = REGISTER.register("emerald_immolator_lipstick", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, -1, -0.5F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(MSItems.EMERALD_IMMOLATOR)), new Item.Properties().defaultDurability(1024)));
	public static final RegistryObject<Item> OBSIDIATOR = REGISTER.register("obsidiator", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CORUNDUM_TIER, 4, -1.5F).efficiency(10.0F).set(MSItemTypes.AXE_TOOL).set(ItemRightClickEffect.switchTo(MSItems.OBSIDIATOR_LIPSTICK)), new Item.Properties().defaultDurability(2048).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> OBSIDIATOR_LIPSTICK = REGISTER.register("obsidiator_lipstick", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, -1, -0.5F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(MSItems.OBSIDIATOR)), new Item.Properties().defaultDurability(2048).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> DEVILS_DELIGHT = REGISTER.register("devils_delight", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CORUNDUM_TIER, 3, -1.5F).efficiency(10.0F).set(MSItemTypes.AXE_TOOL).set(ItemRightClickEffect.switchTo(MSItems.DEVILS_DELIGHT_LIPSTICK)).add(OnHitEffect.onCrit(OnHitEffect.enemyPotionEffect(() -> new MobEffectInstance(MobEffects.POISON, 100, 0)))), new Item.Properties().defaultDurability(1536).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> DEVILS_DELIGHT_LIPSTICK = REGISTER.register("devils_delight_lipstick", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, -1, -0.5F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(MSItems.DEVILS_DELIGHT)), new Item.Properties().defaultDurability(1536).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> DEMONBANE_RAGRIPPER = REGISTER.register("demonbane_ragripper", () -> new WeaponItem(new WeaponItem.Builder(Tiers.NETHERITE, 3, -1.5F).efficiency(10.0F).set(MSItemTypes.AXE_TOOL).set(ItemRightClickEffect.switchTo(MSItems.DEMONBANE_RAGRIPPER_LIPSTICK)).add(OnHitEffect.onCrit(OnHitEffect.enemyPotionEffect(() -> new MobEffectInstance(MobEffects.POISON, 100, 0)))), new Item.Properties().defaultDurability(2048).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> DEMONBANE_RAGRIPPER_LIPSTICK = REGISTER.register("demonbane_ragripper_lipstick", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, -1, -0.5F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(MSItems.DEMONBANE_RAGRIPPER)), new Item.Properties().defaultDurability(2048).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> FROSTTOOTH = REGISTER.register("frosttooth", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ICE_TIER, 5, -1.5F).efficiency(10.0F).set(MSItemTypes.AXE_TOOL).set(ItemRightClickEffect.switchTo(MSItems.FROSTTOOTH_LIPSTICK)).add(OnHitEffect.ICE_SHARD), new Item.Properties().defaultDurability(1536)));
	public static final RegistryObject<Item> FROSTTOOTH_LIPSTICK = REGISTER.register("frosttooth_lipstick", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, -1, -0.5F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(MSItems.FROSTTOOTH)), new Item.Properties().defaultDurability(1536)));
	
	
	//Lances
	public static final RegistryObject<Item> WOODEN_LANCE = REGISTER.register("wooden_lance", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 4, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties()));
	public static final RegistryObject<Item> LANEC = REGISTER.register("lanec", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.SBAHJ_TIER, 3, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SORD_DROP), new Item.Properties()));
	public static final RegistryObject<Item> JOUSTING_LANCE = REGISTER.register("jousting_lance", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 4, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties()));
	public static final RegistryObject<Item> LANCELOTS_LOLLY = REGISTER.register("lancelots_lolly", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CANDY_TIER, 6, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SET_CANDY_DROP_FLAG), new Item.Properties()));
	public static final RegistryObject<Item> DRAGON_LANCE = REGISTER.register("dragon_lance", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 5, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties()));
	public static final RegistryObject<Item> SKY_PIERCER = REGISTER.register("sky_piercer", () -> new WeaponItem(new WeaponItem.Builder(Tiers.NETHERITE, 5, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.setOnFire(30)), new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> FIDUSPAWN_LANCE = REGISTER.register("fiduspawn_lance", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 7, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().defaultDurability(2500)));
	public static final RegistryObject<Item> REGILANCE = REGISTER.register("regilance", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 5, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties()));
	public static final RegistryObject<Item> CIGARETTE_LANCE = REGISTER.register("cigarette_lance", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 5, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.RANDOM_DAMAGE), new Item.Properties().rarity(Rarity.UNCOMMON)));
	
	public static final RegistryObject<Item> LUCERNE_HAMMER = REGISTER.register("lucerne_hammer", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 3, -2.5F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties()));
	public static final RegistryObject<Item> LUCERNE_HAMMER_OF_UNDYING = REGISTER.register("lucerne_hammer_of_undying", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 4, -2.5F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().defaultDurability(2048).rarity(Rarity.UNCOMMON))); //Special property in ServerEventHandler
	
	public static final RegistryObject<Item> OBSIDIAN_AXE_KNIFE = REGISTER.register("obsidian_axe_knife", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.PRISMARINE_TIER, 2, -2.0F).efficiency(1.5F).set(MSItemTypes.MISC_TOOL), new Item.Properties().durability(100)));
	
	
	//Fans
	public static final RegistryObject<Item> FAN = REGISTER.register("fan", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.PAPER_TIER, 1, -1.0F).efficiency(1.5F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.extinguishFire(1)).add(OnHitEffect.enemyKnockback(1.5F)), new Item.Properties()));
	public static final RegistryObject<Item> CANDY_FAN = REGISTER.register("candy_fan", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CANDY_TIER, 1, -1.0F).efficiency(1.5F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.extinguishFire(1)).add(OnHitEffect.enemyKnockback(1.5F)).add(OnHitEffect.SET_CANDY_DROP_FLAG), new Item.Properties()));
	public static final RegistryObject<Item> SPINES_OF_FLUTHLU = REGISTER.register("spines_of_fluthlu", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.HORRORTERROR_TIER, 1, -1.0F).efficiency(1.5F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.extinguishFire(1)).add(OnHitEffect.enemyKnockback(1.5F)).add(OnHitEffect.HORRORTERROR), new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> RAZOR_FAN = REGISTER.register("razor_fan", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 1, -1.0F).efficiency(1.5F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.extinguishFire(1)).add(OnHitEffect.enemyKnockback(1.5F)), new Item.Properties()));
	public static final RegistryObject<Item> MOTOR_FAN = REGISTER.register("motor_fan", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CORUNDUM_TIER, 1, -1.0F).efficiency(1.5F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.extinguishFire(1)).add(OnHitEffect.enemyKnockback(2.0F)), new Item.Properties()));
	public static final RegistryObject<Item> ATOMIC_VAPORIZER = REGISTER.register("atomic_vaporizer", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.URANIUM_TIER, 1, -1.0F).efficiency(1.5F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.extinguishFire(1)).add(OnHitEffect.enemyKnockback(2.0F)).add(OnHitEffect.enemyPotionEffect(() -> new MobEffectInstance(MobEffects.WITHER, 100, 1))), new Item.Properties().defaultDurability(2048)));
	public static final RegistryObject<Item> SHAVING_FAN = REGISTER.register("shaving_fan", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 1, -1.0F).efficiency(1.5F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.extinguishFire(1)).add(OnHitEffect.enemyKnockback(1.5F)), new Item.Properties()));
	public static final RegistryObject<Item> FIRESTARTER = REGISTER.register("firestarter", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 1, -1.0F).efficiency(1.5F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.enemyKnockback(1.5F)).add(OnHitEffect.setOnFire(35)), new Item.Properties().durability(300)));
	public static final RegistryObject<Item> STAR_RAY = REGISTER.register("star_ray", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 1, -1.0F).efficiency(1.5F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.extinguishFire(1)).add(OnHitEffect.enemyKnockback(1.5F)).add(OnHitEffect.setOnFire(35)), new Item.Properties()));
	public static final RegistryObject<Item> TYPHONIC_TRIVIALIZER = REGISTER.register("typhonic_trivializer", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.DENIZEN_TIER, 2, -1.0F).efficiency(1.5F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.extinguishFire(3)).add(OnHitEffect.BREATH_LEVITATION_AOE).add(OnHitEffect.enemyKnockback(2.0F)), new Item.Properties().rarity(Rarity.RARE)));
	
	
	
	//Sickles
	public static final RegistryObject<Item> SICKLE = REGISTER.register("sickle", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 2, -2.2F).efficiency(1.5F).disableShield().set(MSItemTypes.SICKLE_TOOL), new Item.Properties()));
	public static final RegistryObject<Item> BISICKLE = REGISTER.register("bisickle", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 2, -2.2F).efficiency(1.5F).disableShield().set(MSItemTypes.SICKLE_TOOL), new Item.Properties()));
	public static final RegistryObject<Item> OW_THE_EDGE = REGISTER.register("ow_the_edge", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.SBAHJ_TIER, 3, -3.0F).efficiency(1.0F).set(MSItemTypes.SICKLE_TOOL).add(OnHitEffect.SORD_DROP), new Item.Properties()));
	public static final RegistryObject<Item> HEMEOREAPER = REGISTER.register("hemeoreaper", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 5, -2.2F).efficiency(1.0F).disableShield().set(MSItemTypes.SICKLE_TOOL), new Item.Properties().defaultDurability(550)));
	public static final RegistryObject<Item> THORNY_SUBJECT = REGISTER.register("thorny_subject", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CACTUS_TIER, 4, -2.2F).efficiency(1.0F).disableShield().set(MSItemTypes.SICKLE_TOOL), new Item.Properties()));
	public static final RegistryObject<Item> SNOW_WHITE_DREAM = REGISTER.register("snow_white_dream", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 6, -2.2F).efficiency(2.0F).set(MSItemTypes.SICKLE_TOOL).add(OnHitEffect.enemyPotionEffect(() -> new MobEffectInstance(MobEffects.POISON, 140, 0))), new Item.Properties().defaultDurability(2048)));
	public static final RegistryObject<Item> HOMES_SMELL_YA_LATER = REGISTER.register("homes_smell_ya_later", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 4, -2.2F).efficiency(3.0F).disableShield().set(MSItemTypes.SICKLE_TOOL), new Item.Properties().defaultDurability(400)));
	public static final RegistryObject<Item> FUDGESICKLE = REGISTER.register("fudgesickle", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CANDY_TIER, 5, -2.2F).efficiency(1.0F).disableShield().set(MSItemTypes.SICKLE_TOOL).setEating(FinishUseItemEffect.foodEffect(7, 0.6F)), new Item.Properties()));
	public static final RegistryObject<Item> REGISICKLE = REGISTER.register("regisickle", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 4, -2.2F).efficiency(4.0F).disableShield().set(MSItemTypes.SICKLE_TOOL), new Item.Properties()));
	public static final RegistryObject<Item> HERETICUS_AURURM = REGISTER.register("hereticus_aururm", () -> new WeaponItem(new WeaponItem.Builder(Tiers.GOLD, 9, -2.2F).efficiency(4.0F).disableShield().set(MSItemTypes.SICKLE_TOOL), new Item.Properties().defaultDurability(1024)));
	public static final RegistryObject<Item> CLAW_SICKLE = REGISTER.register("claw_sickle", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 8, -2.2F).efficiency(4.0F).disableShield().set(MSItemTypes.SICKLE_TOOL), new Item.Properties()));
	public static final RegistryObject<Item> CLAW_OF_NRUBYIGLITH = REGISTER.register("claw_of_nrubyiglith", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.HORRORTERROR_TIER, 6, -2.2F).efficiency(4.0F).disableShield().set(MSItemTypes.SICKLE_TOOL).add(OnHitEffect.HORRORTERROR), new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> CANDY_SICKLE = REGISTER.register("candy_sickle", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CANDY_TIER, 6, -2.2F).efficiency(2.5F).disableShield().set(MSItemTypes.SICKLE_TOOL).add(OnHitEffect.SET_CANDY_DROP_FLAG), new Item.Properties()));
	
	
	//Scythes
	public static final RegistryObject<Item> SCYTHE = REGISTER.register("scythe", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 4, -2.6F).efficiency(1.5F).disableShield().set(MSItemTypes.SICKLE_TOOL), new Item.Properties()));
	public static final RegistryObject<Item> MARASCHINO_CHERRY_SCYTHE = REGISTER.register("maraschino_cherry_scythe", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CANDY_TIER, 6, -2.6F).efficiency(1.5F).disableShield().set(MSItemTypes.SICKLE_TOOL).add(OnHitEffect.SET_CANDY_DROP_FLAG), new Item.Properties()));
	public static final RegistryObject<Item> KISSY_CUTIE_HEART_SPLITTER = REGISTER.register("kissy_cutie_heart_splitter", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 4, -2.6F).efficiency(10.0F).disableShield().set(MSItemTypes.SICKLE_TOOL).set(ItemRightClickEffect.switchTo(MSItems.KISSY_CUTIE_HEART_HITTER)), new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> MUTANT_CUTIE_CELL_CUTTER = REGISTER.register("mutant_cutie_cell_cutter", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.URANIUM_TIER, 4, -2.6F).efficiency(10.0F).disableShield().set(MSItemTypes.SICKLE_TOOL).set(ItemRightClickEffect.switchTo(MSItems.MUTANT_CUTIE_CELL_PUTTER)).add(OnHitEffect.playSound(() -> SoundEvents.CAT_PURREOW)), new Item.Properties().defaultDurability(2000).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> PROSPECTING_PICKSCYTHE = REGISTER.register("prospecting_pickscythe", () -> new WeaponItem(new WeaponItem.Builder(Tiers.GOLD, 7, -2.6F).disableShield().set(MSItemTypes.PICKAXE_TOOL), new Item.Properties().defaultDurability(100)));
	public static final RegistryObject<Item> EIGHTBALL_SCYTHE = REGISTER.register("eightball_scythe", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 3, -2.6F).efficiency(1.5F).disableShield().set(MSItemTypes.SICKLE_TOOL).add(OnHitEffect.RANDOM_DAMAGE).set(ItemRightClickEffect.EIGHTBALL), new Item.Properties().defaultDurability(600)));
	public static final RegistryObject<Item> TIME_FLAYER = REGISTER.register("time_flayer", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 3, -2.6F).efficiency(1.5F).disableShield().set(MSItemTypes.SICKLE_TOOL).add(OnHitEffect.RANDOM_DAMAGE), new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> DESTINY_DECIMATOR = REGISTER.register("destiny_decimator", () -> new WeaponItem(new WeaponItem.Builder(Tiers.NETHERITE, 4, -2.6F).efficiency(1.5F).disableShield().set(MSItemTypes.SICKLE_TOOL), new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> SUNRAY_HARVESTER = REGISTER.register("sunray_harvester", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 4, -2.6F).efficiency(1.5F).disableShield().set(MSItemTypes.SICKLE_TOOL).add(OnHitEffect.setOnFire(35)), new Item.Properties().defaultDurability(1200)));
	public static final RegistryObject<Item> GREEN_SUN_RAYREAPER = REGISTER.register("green_sun_rayreaper", () -> new WeaponItem(new WeaponItem.Builder(Tiers.NETHERITE, 7, -2.6F).efficiency(1.5F).disableShield().set(MSItemTypes.SICKLE_TOOL).add(OnHitEffect.setOnFire(10)), new Item.Properties().fireResistant().rarity(Rarity.RARE)));
	public static final RegistryObject<Item> SKAITHE = REGISTER.register("skaithe", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 8, -2.6F).efficiency(1.5F).disableShield().set(MSItemTypes.SICKLE_TOOL), new Item.Properties().rarity(Rarity.UNCOMMON)));
	
	public static final RegistryObject<Item> HELLBRINGERS_HOE_INACTIVE = REGISTER.register("hellbringers_hoe_inactive", () -> new WeaponItem(new WeaponItem.Builder(Tiers.NETHERITE, 3, -2.4F).efficiency(10.0F).set(MSItemTypes.SICKLE_TOOL).set(ItemRightClickEffect.switchTo(MSItems.HELLBRINGERS_HOE_ACTIVE)), new Item.Properties()));
	public static final RegistryObject<Item> HELLBRINGERS_HOE_ACTIVE = REGISTER.register("hellbringers_hoe_active", () -> new WeaponItem(new WeaponItem.Builder(Tiers.NETHERITE, 2, -2.4F).efficiency(10.0F).set(MSItemTypes.SICKLE_TOOL).set(ItemRightClickEffect.switchTo(MSItems.HELLBRINGERS_HOE_INACTIVE)).add(OnHitEffect.setOnFire(10)), new Item.Properties()));
	
	
	//Clubs
	public static final RegistryObject<Item> DEUCE_CLUB = REGISTER.register("deuce_club", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 3, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties()));
	public static final RegistryObject<Item> STALE_BAGUETTE = REGISTER.register("stale_baguette", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 3, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SPAWN_BREADCRUMBS).setEating(FinishUseItemEffect.SPAWN_BREADCRUMBS, FinishUseItemEffect.foodEffect(3, 0.2F)), new Item.Properties()));
	public static final RegistryObject<Item> GLUB_CLUB = REGISTER.register("glub_club", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.MEAT_TIER, 4, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.playSound(() -> SoundEvents.GUARDIAN_FLOP)), new Item.Properties()));
	public static final RegistryObject<Item> NIGHT_CLUB = REGISTER.register("night_club", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 1, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties()));
	public static final RegistryObject<Item> NIGHTSTICK = REGISTER.register("nightstick", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 2, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().defaultDurability(2500)));
	public static final RegistryObject<Item> RED_EYES = REGISTER.register("red_eyes", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 6, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.enemyPotionEffect(() -> new MobEffectInstance(MobEffects.POISON, 140, 0))), new Item.Properties()));
	public static final RegistryObject<Item> PRISMARINE_BASHER = REGISTER.register("prismarine_basher", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.PRISMARINE_TIER, 3, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties()));
	public static final RegistryObject<Item> CLUB_ZERO = REGISTER.register("club_zero", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ICE_TIER, 5, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.ICE_SHARD), new Item.Properties()));
	public static final RegistryObject<Item> POGO_CLUB = REGISTER.register("pogo_club", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.POGO_TIER, 4, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).set(PogoEffect.EFFECT_05).add(PogoEffect.EFFECT_05), new Item.Properties()));
	public static final RegistryObject<Item> BARBER_BASHER = REGISTER.register("barber_basher", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 3, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().defaultDurability(350)));
	public static final RegistryObject<Item> METAL_BAT = REGISTER.register("metal_bat", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 4, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties()));
	public static final RegistryObject<Item> CRICKET_BAT = REGISTER.register("cricket_bat", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 7, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties()));
	public static final RegistryObject<Item> CLOWN_CLUB = REGISTER.register("clown_club", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 5, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.RAGE_STRENGTH, OnHitEffect.playSound(MSSoundEvents.ITEM_HORN_USE, 1.5F, 1)), new Item.Properties().defaultDurability(2048).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> DOCTOR_DETERRENT = REGISTER.register("doctor_deterrent", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 9, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).setEating(64, FinishUseItemEffect.potionEffect(() -> new MobEffectInstance(MobEffects.HEAL, 1, 0), 1F), FinishUseItemEffect.foodEffect(1, 0.6F, 250)), new Item.Properties().defaultDurability(2048)));
	public static final RegistryObject<Item> MACE = REGISTER.register("mace", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 5, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().defaultDurability(500)));
	public static final RegistryObject<Item> M_ACE = REGISTER.register("m_ace", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 6, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().defaultDurability(750)));
	public static final RegistryObject<Item> M_ACE_OF_CLUBS = REGISTER.register("m_ace_of_clubs", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 7, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().defaultDurability(750)));
	public static final RegistryObject<Item> DESOLATOR_MACE = REGISTER.register("desolator_mace", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 1, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.armorBypassingDamageMod(4, EnumAspect.VOID)), new Item.Properties().defaultDurability(2048).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> BLAZING_GLORY = REGISTER.register("blazing_glory", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 7, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.setOnFire(35)), new Item.Properties().defaultDurability(750)));
	public static final RegistryObject<Item> SPIKED_CLUB = REGISTER.register("spiked_club", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 5, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().defaultDurability(100)));
	public static final RegistryObject<Item> RUBIKS_MACE = REGISTER.register("rubiks_mace", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 6, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().defaultDurability(500)));
	public static final RegistryObject<Item> HOME_GROWN_MACE = REGISTER.register("home_grown_mace", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD , 6, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().defaultDurability(500)));
	public static final RegistryObject<Item> CARNIE_CLUB = REGISTER.register("carnie_club", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 6, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.playSound(MSSoundEvents.ITEM_HORN_USE)), new Item.Properties().defaultDurability(3666).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> TOFFEE_CLUB = REGISTER.register("toffee_club", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CANDY_TIER, 6, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SET_CANDY_DROP_FLAG), new Item.Properties()));
	
	public static final RegistryObject<Item> HORSE_HITCHER = REGISTER.register("horse_hitcher", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 7, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(MSItems.ACE_OF_SPADES)), new Item.Properties().defaultDurability(500).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> ACE_OF_SPADES = REGISTER.register("ace_of_spades", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.PAPER_TIER, 0, -1.8F).efficiency(0.0F).set(ItemRightClickEffect.switchTo(MSItems.HORSE_HITCHER)), new Item.Properties().defaultDurability(500)));
	public static final RegistryObject<Item> CLUB_OF_FELONY = REGISTER.register("club_of_felony", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 7, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(MSItems.ACE_OF_CLUBS)), new Item.Properties().defaultDurability(500).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> ACE_OF_CLUBS = REGISTER.register("ace_of_clubs", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.PAPER_TIER, 0, -1.8F).efficiency(0.0F).set(ItemRightClickEffect.switchTo(MSItems.CLUB_OF_FELONY)), new Item.Properties().defaultDurability(500)));
	public static final RegistryObject<Item> CUESTICK = REGISTER.register("cuestick", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 5, -2.0F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(MSItems.ACE_OF_DIAMONDS)), new Item.Properties().defaultDurability(500).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> ACE_OF_DIAMONDS = REGISTER.register("ace_of_diamonds", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.PAPER_TIER, 0, -1.8F).efficiency(0.0F).set(ItemRightClickEffect.switchTo(MSItems.CUESTICK)), new Item.Properties().defaultDurability(500)));
	public static final RegistryObject<Item> ACE_OF_HEARTS = REGISTER.register("ace_of_hearts", () -> new Item(new Item.Properties().defaultDurability(500)));
	
	
	//Staffs
	public static final RegistryObject<Item> BO_STAFF = REGISTER.register("bo_staff", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 0, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SWEEP).add(OnHitEffect.enemyKnockback(0.5F)), new Item.Properties().defaultDurability(100)));
	public static final RegistryObject<Item> BAMBOO_BEATSTICK = REGISTER.register("bamboo_beatstick", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 4, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SWEEP).add(OnHitEffect.enemyKnockback(0.5F)), new Item.Properties().defaultDurability(350)));
	public static final RegistryObject<Item> TELESCOPIC_BEATDOWN_BRUISER = REGISTER.register("telescopic_beatdown_bruiser", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 3, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.enemyKnockback(0.5F)), new Item.Properties().defaultDurability(550)));
	public static final RegistryObject<Item> ION_DESTABILIZER = REGISTER.register("ion_destabilizer", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.URANIUM_TIER, 2, -2.0F).efficiency(4.0F).set(MSItemTypes.MULTI_TOOL).add(OnHitEffect.SWEEP).add(OnHitEffect.enemyKnockback(0.5F)).add(OnHitEffect.setOnFire(4)).add(OnHitEffect.spawnParticles(ParticleTypes.ITEM_SLIME, 6, 2.0, 1.0, 2.0, 1.0)), new Item.Properties().defaultDurability(1400).rarity(Rarity.UNCOMMON)));
	
	public static final RegistryObject<Item> WIZARD_STAFF = REGISTER.register("wizard_staff", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 5, -2.8F).efficiency(4.0F).set(MSItemTypes.MISC_TOOL).set(MagicAOERightClickEffect.STANDARD_MAGIC), new Item.Properties().defaultDurability(1024)));
	public static final RegistryObject<Item> BARBERS_MAGIC_TOUCH = REGISTER.register("barbers_magic_touch", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 4, -2.8F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SWEEP).add(OnHitEffect.enemyKnockback(0.5F)).set(MagicAOERightClickEffect.STANDARD_MAGIC), new Item.Properties().defaultDurability(660)));
	public static final RegistryObject<Item> WATER_STAFF = REGISTER.register("water_staff", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.EMERALD_TIER, 5, -2.8F).efficiency(4.0F).set(MSItemTypes.MISC_TOOL).set(MagicAOERightClickEffect.WATER_MAGIC), new Item.Properties().defaultDurability(1250)));
	public static final RegistryObject<Item> FIRE_STAFF = REGISTER.register("fire_staff", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.EMERALD_TIER, 5, -2.8F).efficiency(4.0F).set(MSItemTypes.MISC_TOOL).set(MagicAOERightClickEffect.FIRE_MAGIC), new Item.Properties().defaultDurability(1250)));
	public static final RegistryObject<Item> WHITE_KINGS_SCEPTER = REGISTER.register("white_kings_scepter", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 8, -2.8F).efficiency(4.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.summonFireball()), new Item.Properties().rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> BLACK_KINGS_SCEPTER = REGISTER.register("black_kings_scepter", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 8, -2.8F).efficiency(4.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.summonFireball()), new Item.Properties().rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> PRIME_STAFF = REGISTER.register("prime_staff", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.WELSH_TIER, 8, -2.8F).efficiency(4.0F).set(MSItemTypes.MISC_TOOL).set(MagicRangedRightClickEffect.ZILLY_MAGIC), new Item.Properties().fireResistant().rarity(Rarity.RARE)));
	
	
	//Canes
	public static final RegistryObject<Item> CANE = REGISTER.register("cane", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 2, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().defaultDurability(100)));
	public static final RegistryObject<Item> VAUDEVILLE_HOOK = REGISTER.register("vaudeville_hook", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 2, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().defaultDurability(150)));
	public static final RegistryObject<Item> BEAR_POKING_STICK = REGISTER.register("bear_poking_stick", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 3, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.notAtPlayer(OnHitEffect.enemyPotionEffect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 140, 1)))), new Item.Properties().defaultDurability(150)));
	public static final RegistryObject<Item> CROWBAR = REGISTER.register("crowbar", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 6, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.playSound(() -> SoundEvents.ANVIL_PLACE)), new Item.Properties().defaultDurability(-1).rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> UMBRELLA = REGISTER.register("umbrella", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 2, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(InventoryTickEffect.BREATH_SLOW_FALLING), new Item.Properties().defaultDurability(350)));
	public static final RegistryObject<Item> BARBERS_BEST_FRIEND = REGISTER.register("barbers_best_friend", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 1, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties()));
	public static final RegistryObject<Item> UPPER_CRUST_CRUST_CANE = REGISTER.register("upper_crust_crust_cane", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 3, -2.0F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SPAWN_BREADCRUMBS).setEating(FinishUseItemEffect.SPAWN_BREADCRUMBS, FinishUseItemEffect.foodEffect(4, 0.5F)), new Item.Properties()));
	public static final RegistryObject<Item> IRON_CANE = REGISTER.register("iron_cane", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 2, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties()));
	public static final RegistryObject<Item> KISSY_CUTIE_HEART_HITTER = REGISTER.register("kissy_cutie_heart_hitter", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 2, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(MSItems.KISSY_CUTIE_HEART_SPLITTER)), new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> MUTANT_CUTIE_CELL_PUTTER = REGISTER.register("mutant_cutie_cell_putter", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.URANIUM_TIER, 2, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(MSItems.MUTANT_CUTIE_CELL_CUTTER)).add(OnHitEffect.playSound(() -> SoundEvents.CAT_PURREOW)), new Item.Properties().defaultDurability(2000).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> ZEPHYR_CANE = REGISTER.register("zephyr_cane", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 3, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(PropelEffect.BREATH_PROPEL), new Item.Properties().defaultDurability(2048).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> SPEAR_CANE = REGISTER.register("spear_cane", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 3, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties()));
	public static final RegistryObject<Item> PARADISES_PORTABELLO = REGISTER.register("paradises_portabello", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 3, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties()));
	public static final RegistryObject<Item> REGI_CANE = REGISTER.register("regi_cane", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 4, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties()));
	public static final RegistryObject<Item> POGO_CANE = REGISTER.register("pogo_cane", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.POGO_TIER, 2, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(PogoEffect.EFFECT_06).add(PogoEffect.EFFECT_06), new Item.Properties()));
	public static final RegistryObject<Item> CANDY_CANE = REGISTER.register("candy_cane", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CANDY_TIER, 3, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SET_CANDY_DROP_FLAG).setEating(FinishUseItemEffect.foodEffect(2, 0.3F), FinishUseItemEffect.SHARPEN_CANDY_CANE), new Item.Properties()));
	public static final RegistryObject<Item> SHARP_CANDY_CANE = REGISTER.register("sharp_candy_cane", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CANDY_TIER, 5, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SET_CANDY_DROP_FLAG), new Item.Properties()));
	public static final RegistryObject<Item> PRIM_AND_PROPER_WALKING_POLE = REGISTER.register("prim_and_proper_walking_pole", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 3, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties()));
	public static final RegistryObject<Item> DRAGON_CANE = REGISTER.register("dragon_cane", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 7, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(MSItems.DRAGON_CANE_UNSHEATHED)), new Item.Properties().defaultDurability(2048).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> DRAGON_CANE_UNSHEATHED = REGISTER.register("dragon_cane_unsheathed", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 6, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(MSItems.DRAGON_CANE)), new Item.Properties().defaultDurability(2048).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> CHANCEWYRMS_EXTRA_FORTUNATE_STABBING_IMPLEMENT = REGISTER.register("chancewyrms_extra_fortunate_stabbing_implement", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 6, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(MSItems.CHANCEWYRMS_EXTRA_FORTUNATE_STABBING_IMPLEMENT_UNSHEATHED)), new Item.Properties().defaultDurability(4096).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> CHANCEWYRMS_EXTRA_FORTUNATE_STABBING_IMPLEMENT_UNSHEATHED = REGISTER.register("chancewyrms_extra_fortunate_stabbing_implement_unsheathed", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 5, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(MSItems.CHANCEWYRMS_EXTRA_FORTUNATE_STABBING_IMPLEMENT)).add(OnHitEffect.RANDOM_DAMAGE), new Item.Properties().defaultDurability(4096).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> LESS_PROPER_WALKING_STICK = REGISTER.register("less_proper_walking_stick", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 3, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(MSItems.LESS_PROPER_WALKING_STICK_SHEATHED)), new Item.Properties().defaultDurability(600)));
	public static final RegistryObject<Item> LESS_PROPER_WALKING_STICK_SHEATHED = REGISTER.register("less_proper_walking_stick_sheathed", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 3, -2.0F).efficiency(1.0F).set(ItemRightClickEffect.switchTo(MSItems.LESS_PROPER_WALKING_STICK)), new Item.Properties().defaultDurability(600)));
	public static final RegistryObject<Item> ROCKEFELLERS_WALKING_BLADECANE = REGISTER.register("rockefellers_walking_bladecane", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 5, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(MSItems.ROCKEFELLERS_WALKING_BLADECANE_SHEATHED)), new Item.Properties().defaultDurability(800).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> ROCKEFELLERS_WALKING_BLADECANE_SHEATHED = REGISTER.register("rockefellers_walking_bladecane_sheathed", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 5, -2.0F).efficiency(1.0F).set(ItemRightClickEffect.switchTo(MSItems.ROCKEFELLERS_WALKING_BLADECANE)), new Item.Properties().defaultDurability(800).rarity(Rarity.UNCOMMON)));
	
	
	
	//Spoons/Forks
	public static final RegistryObject<Item> WOODEN_SPOON = REGISTER.register("wooden_spoon", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 2, -2.4F).efficiency(1.0F).set(MSItemTypes.SHOVEL_TOOL), new Item.Properties()));
	public static final RegistryObject<Item> SILVER_SPOON = REGISTER.register("silver_spoon", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 1, -2.4F).efficiency(1.0F).set(MSItemTypes.SHOVEL_TOOL), new Item.Properties()));
	public static final RegistryObject<Item> MELONBALLER = REGISTER.register("melonballer", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 2, -2.4F).efficiency(1.0F).set(MSItemTypes.SHOVEL_TOOL).set(RightClickBlockEffect.scoopBlock(() -> Blocks.MELON)), new Item.Properties().defaultDurability(500)));
	public static final RegistryObject<Item> SIGHTSEEKER = REGISTER.register("sightseeker", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 6, -2.4F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties()));
	public static final RegistryObject<Item> TERRAIN_FLATENATOR = REGISTER.register("terrain_flatenator", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 3, -2.4F).efficiency(10.0F).set(MSItemTypes.SHOVEL_TOOL).set(new FarmineEffect(Integer.MAX_VALUE, 50)), new Item.Properties().defaultDurability(1024))); //TODO fix inability to use for terrain flattenation
	public static final RegistryObject<Item> NOSFERATU_SPOON = REGISTER.register("nosferatu_spoon", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 5, -2.4F).efficiency(1.0F).set(MSItemTypes.SHOVEL_TOOL).add(OnHitEffect.LIFE_SATURATION), new Item.Properties().defaultDurability(2048).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> THRONGLER = REGISTER.register("throngler", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 5, -2.4F).efficiency(1.0F).set(MSItemTypes.SHOVEL_TOOL), new Item.Properties().defaultDurability(3200).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> WET_MEAT_SHIT_THRONGLER = REGISTER.register("wet_meat_shit_throngler", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.MEAT_TIER, 10, -2.9F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().defaultDurability(3500).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> CROCKER_SPOON = REGISTER.register("crocker_spoon", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 5, -2.4F).efficiency(1.0F).set(MSItemTypes.SHOVEL_TOOL).set(ItemRightClickEffect.switchTo(MSItems.CROCKER_FORK)), new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> CROCKER_FORK = REGISTER.register("crocker_fork", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 6, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(MSItems.CROCKER_SPOON)), new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> EDISONS_FURY = REGISTER.register("edisons_fury", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 7, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(MSItems.EDISONS_SERENITY)).add(OnHitEffect.DROP_FOE_ITEM).add(InventoryTickEffect.DROP_WHEN_IN_WATER).add(OnHitEffect.playSound(MSSoundEvents.EVENT_ELECTRIC_SHOCK, 0.6F, 1.0F)), new Item.Properties()));
	public static final RegistryObject<Item> EDISONS_SERENITY = REGISTER.register("edisons_serenity", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 7, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(MSItems.EDISONS_FURY)), new Item.Properties()));
	
	public static final RegistryObject<Item> SKAIA_FORK = REGISTER.register("skaia_fork", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 9, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().rarity(Rarity.RARE)));
	public static final RegistryObject<Item> SKAIAN_CROCKER_ROCKER = REGISTER.register("skaian_crocker_rocker", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 10, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().defaultDurability(2048).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> FORK = REGISTER.register("fork", () -> new WeaponItem(new WeaponItem.Builder(Tiers.STONE, 3, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties()));
	public static final RegistryObject<Item> CANDY_FORK = REGISTER.register("candy_fork", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CANDY_TIER, 5, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SET_CANDY_DROP_FLAG), new Item.Properties()));
	public static final RegistryObject<Item> TUNING_FORK = REGISTER.register("tuning_fork", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 3, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.playSound(SoundEvents.NOTE_BLOCK_CHIME)), new Item.Properties()));
	public static final RegistryObject<Item> ELECTRIC_FORK = REGISTER.register("electric_fork", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 6, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.DROP_FOE_ITEM).add(InventoryTickEffect.DROP_WHEN_IN_WATER).add(OnHitEffect.playSound(MSSoundEvents.EVENT_ELECTRIC_SHOCK, 0.6F, 1.0F)), new Item.Properties()));
	public static final RegistryObject<Item> EATING_FORK_GEM = REGISTER.register("eating_fork_gem", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 3, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties()));
	public static final RegistryObject<Item> DEVIL_FORK = REGISTER.register("devil_fork", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.URANIUM_TIER, 6, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.setOnFire(35)), new Item.Properties()));
	
	public static final RegistryObject<Item> SPORK = REGISTER.register("spork", () -> new WeaponItem(new WeaponItem.Builder(Tiers.STONE, 4, -2.5F).efficiency(1.0F).set(MSItemTypes.SHOVEL_TOOL), new Item.Properties()));
	public static final RegistryObject<Item> GOLDEN_SPORK = REGISTER.register("golden_spork", () -> new WeaponItem(new WeaponItem.Builder(Tiers.GOLD, 5, -2.5F).efficiency(1.0F).set(MSItemTypes.SHOVEL_TOOL), new Item.Properties()));
	
	public static final RegistryObject<Item> MEATFORK = REGISTER.register("meatfork", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.MEAT_TIER, 7, -2.9F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).setEating(FinishUseItemEffect.foodEffect(3, 0.8F, 75)), new Item.Properties()));
	public static final RegistryObject<Item> BIDENT = REGISTER.register("bident", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 7, -2.9F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties()));
	public static final RegistryObject<Item> DOUBLE_ENDED_TRIDENT = REGISTER.register("double_ended_trident", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.PRISMARINE_TIER, 6, -2.9F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties()));
	
	
	//Needles/Wands
	public static final RegistryObject<Item> POINTY_STICK = REGISTER.register("pointy_stick", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 0, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties()));
	public static final RegistryObject<Item> KNITTING_NEEDLE = REGISTER.register("knitting_needle", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, -1, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties()));
	
	public static final RegistryObject<Item> WAND = REGISTER.register("wand", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, -2, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(MagicRangedRightClickEffect.STANDARD_MAGIC), new Item.Properties()));
	public static final RegistryObject<Item> NEEDLE_WAND = REGISTER.register("needle_wand", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 0, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(MagicRangedRightClickEffect.STANDARD_MAGIC), new Item.Properties().defaultDurability(1024)));
	public static final RegistryObject<Item> ARTIFUCKER = REGISTER.register("artifucker", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.SBAHJ_TIER, 1, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(MagicRangedRightClickEffect.SBAHJ_AIMBOT_MAGIC), new Item.Properties()));
	public static final RegistryObject<Item> POINTER_WAND = REGISTER.register("pointer_wand", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, -1, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(MagicRangedRightClickEffect.AIMBOT_MAGIC), new Item.Properties().defaultDurability(512)));
	public static final RegistryObject<Item> POOL_CUE_WAND = REGISTER.register("pool_cue_wand", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, -1, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(MagicRangedRightClickEffect.POOL_CUE_MAGIC), new Item.Properties().defaultDurability(1250)));
	public static final RegistryObject<Item> THORN_OF_OGLOGOTH = REGISTER.register("thorn_of_oglogoth", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.HORRORTERROR_TIER, 0, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.HORRORTERROR).set(MagicRangedRightClickEffect.HORRORTERROR_MAGIC), new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> THISTLE_OF_ZILLYWICH = REGISTER.register("thistle_of_zillywich", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ZILLY_TIER, 0, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(MagicRangedRightClickEffect.ZILLY_MAGIC), new Item.Properties().rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> QUILL_OF_ECHIDNA = REGISTER.register("quill_of_echidna", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.DENIZEN_TIER, -1, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(MagicRangedRightClickEffect.ECHIDNA_MAGIC), new Item.Properties().rarity(Rarity.EPIC)));
	
	//Projectiles
	public static final RegistryObject<Item> SBAHJARANG = REGISTER.register("sbahjarang", () -> new ConsumableProjectileWeaponItem(new Item.Properties(), 0.5F, 20.0F, 1));
	public static final RegistryObject<Item> SHURIKEN = REGISTER.register("shuriken", () -> new ConsumableProjectileWeaponItem(new Item.Properties(), 1.0F, 2.8F, 2));
	public static final RegistryObject<Item> CLUBS_SUITARANG = REGISTER.register("clubs_suitarang", () -> new ConsumableProjectileWeaponItem(new Item.Properties(), 1.5F, 2.4F, 3));
	public static final RegistryObject<Item> DIAMONDS_SUITARANG = REGISTER.register("diamonds_suitarang", () -> new ConsumableProjectileWeaponItem(new Item.Properties(), 1.5F, 2.4F, 3));
	public static final RegistryObject<Item> HEARTS_SUITARANG = REGISTER.register("hearts_suitarang", () -> new ConsumableProjectileWeaponItem(new Item.Properties(), 1.5F, 2.4F, 3));
	public static final RegistryObject<Item> SPADES_SUITARANG = REGISTER.register("spades_suitarang", () -> new ConsumableProjectileWeaponItem(new Item.Properties(), 1.5F, 2.4F, 3));
	
	public static final RegistryObject<Item> CHAKRAM = REGISTER.register("chakram", () -> new ReturningProjectileWeaponItem(new Item.Properties().durability(250), 1.3F, 1.0F, 5, 30));
	public static final RegistryObject<Item> UMBRAL_INFILTRATOR = REGISTER.register("umbral_infiltrator", () -> new ReturningProjectileWeaponItem(new Item.Properties().durability(2048), 1.5F, 0.6F, 12, 20));
	
	public static final RegistryObject<Item> SORCERERS_PINBALL = REGISTER.register("sorcerers_pinball", () -> new BouncingProjectileWeaponItem(new Item.Properties().durability(250), 1.5F, 1.0F, 5, 20));
	
	
	
	//Material Tools
	public static final RegistryObject<Item> EMERALD_SWORD = REGISTER.register("emerald_sword", () -> new SwordItem(MSItemTypes.EMERALD_TIER, 3, -2.4F, new Item.Properties()));
	public static final RegistryObject<Item> EMERALD_AXE = REGISTER.register("emerald_axe", () -> new AxeItem(MSItemTypes.EMERALD_TIER, 5, -3.0F, new Item.Properties()));
	public static final RegistryObject<Item> EMERALD_PICKAXE = REGISTER.register("emerald_pickaxe", () -> new PickaxeItem(MSItemTypes.EMERALD_TIER, 1, -2.8F, new Item.Properties()));
	public static final RegistryObject<Item> EMERALD_SHOVEL = REGISTER.register("emerald_shovel", () -> new ShovelItem(MSItemTypes.EMERALD_TIER, 1.5F, -3.0F, new Item.Properties()));
	public static final RegistryObject<Item> EMERALD_HOE = REGISTER.register("emerald_hoe", () -> new HoeItem(MSItemTypes.EMERALD_TIER, -3, 0.0F, new Item.Properties()));
	public static final RegistryObject<Item> MINE_AND_GRIST = REGISTER.register("mine_and_grist", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 1, -2.8F).efficiency(10.0F).set(MSItemTypes.PICKAXE_TOOL).add(MSItemTypes.GRIST_HARVEST), new Item.Properties()));
	
	
	//Armor
	public static final RegistryObject<Item> PRISMARINE_HELMET = REGISTER.register("prismarine_helmet", () -> new ArmorItem(MSItemTypes.PRISMARINE_ARMOR, ArmorItem.Type.HELMET, new Item.Properties()));
	public static final RegistryObject<Item> PRISMARINE_CHESTPLATE = REGISTER.register("prismarine_chestplate", () -> new ArmorItem(MSItemTypes.PRISMARINE_ARMOR, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
	public static final RegistryObject<Item> PRISMARINE_LEGGINGS = REGISTER.register("prismarine_leggings", () -> new ArmorItem(MSItemTypes.PRISMARINE_ARMOR, ArmorItem.Type.LEGGINGS, new Item.Properties()));
	public static final RegistryObject<Item> PRISMARINE_BOOTS = REGISTER.register("prismarine_boots", () -> new ArmorItem(MSItemTypes.PRISMARINE_ARMOR, ArmorItem.Type.BOOTS, new Item.Properties()));
	public static final RegistryObject<Item> IRON_LASS_GLASSES = REGISTER.register("iron_lass_glasses", () -> new ArmorItem(MSItemTypes.IRON_LASS_ARMOR, ArmorItem.Type.HELMET, new Item.Properties()));
	public static final RegistryObject<Item> IRON_LASS_CHESTPLATE = REGISTER.register("iron_lass_chestplate", () -> new ArmorItem(MSItemTypes.IRON_LASS_ARMOR, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
	public static final RegistryObject<Item> IRON_LASS_SKIRT = REGISTER.register("iron_lass_skirt", () -> new ArmorItem(MSItemTypes.IRON_LASS_ARMOR, ArmorItem.Type.LEGGINGS, new Item.Properties()));
	public static final RegistryObject<Item> IRON_LASS_SHOES = REGISTER.register("iron_lass_shoes", () -> new ArmorItem(MSItemTypes.IRON_LASS_ARMOR, ArmorItem.Type.BOOTS, new Item.Properties()));
	
	public static final RegistryObject<MSArmorItem> PROSPIT_CIRCLET = REGISTER.register("prospit_circlet", () -> new MSArmorItem(MSItemTypes.DREAM_PAJAMAS, ArmorItem.Type.HELMET, new Item.Properties()));
	public static final RegistryObject<MSArmorItem> PROSPIT_SHIRT = REGISTER.register("prospit_shirt", () -> new MSArmorItem(MSItemTypes.DREAM_PAJAMAS, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
	public static final RegistryObject<MSArmorItem> PROSPIT_PANTS = REGISTER.register("prospit_pants", () -> new MSArmorItem(MSItemTypes.DREAM_PAJAMAS, ArmorItem.Type.LEGGINGS, new Item.Properties()));
	public static final RegistryObject<MSArmorItem> PROSPIT_SHOES = REGISTER.register("prospit_shoes", () -> new MSArmorItem(MSItemTypes.DREAM_PAJAMAS, ArmorItem.Type.BOOTS, new Item.Properties()));
	public static final RegistryObject<MSArmorItem> DERSE_CIRCLET = REGISTER.register("derse_circlet", () -> new MSArmorItem(MSItemTypes.DREAM_PAJAMAS, ArmorItem.Type.HELMET, new Item.Properties()));
	public static final RegistryObject<MSArmorItem> DERSE_SHIRT = REGISTER.register("derse_shirt", () -> new MSArmorItem(MSItemTypes.DREAM_PAJAMAS, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
	public static final RegistryObject<MSArmorItem> DERSE_PANTS = REGISTER.register("derse_pants", () -> new MSArmorItem(MSItemTypes.DREAM_PAJAMAS, ArmorItem.Type.LEGGINGS, new Item.Properties()));
	public static final RegistryObject<MSArmorItem> DERSE_SHOES = REGISTER.register("derse_shoes", () -> new MSArmorItem(MSItemTypes.DREAM_PAJAMAS, ArmorItem.Type.BOOTS, new Item.Properties()));
	
	public static final RegistryObject<MSArmorItem> AMPHIBEANIE = REGISTER.register("amphibeanie", () -> new MSArmorItem(MSItemTypes.CLOTH_ARMOR, ArmorItem.Type.HELMET, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<MSArmorItem> NOSTRILDAMUS = REGISTER.register("nostrildamus", () -> new MSArmorItem(MSItemTypes.CLOTH_ARMOR, ArmorItem.Type.HELMET, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<MSArmorItem> PONYTAIL = REGISTER.register("ponytail", () -> new MSArmorItem(MSItemTypes.CLOTH_ARMOR, ArmorItem.Type.HELMET, new Item.Properties().stacksTo(1)));
	
	
	
	//Core Items
	public static final RegistryObject<Item> BOONDOLLARS = REGISTER.register("boondollars", () -> new BoondollarsItem(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> RAW_CRUXITE = REGISTER.register("raw_cruxite", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> RAW_URANIUM = REGISTER.register("raw_uranium", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> ENERGY_CORE = REGISTER.register("energy_core", () -> new Item(new Item.Properties()));
	
	public static final RegistryObject<Item> CRUXITE_APPLE = REGISTER.register("cruxite_apple", () -> new CruxiteAppleItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON))); //TODO have to fix Cruxite artifact classes
	public static final RegistryObject<Item> CRUXITE_POTION = REGISTER.register("cruxite_potion", () -> new CruxitePotionItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
	
	public static final RegistryObject<Item> SBURB_CODE = REGISTER.register("sburb_code", () -> new IncompleteSburbCodeItem(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> COMPLETED_SBURB_CODE = REGISTER.register("completed_sburb_code", () -> new ReadableSburbCodeItem.Completed(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> COMPUTER_PARTS = REGISTER.register("computer_parts", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> BLANK_DISK = REGISTER.register("blank_disk", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> CLIENT_DISK = REGISTER.register("client_disk", () -> new Item(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> SERVER_DISK = REGISTER.register("server_disk", () -> new Item(new Item.Properties().stacksTo(1)));
	
	public static final RegistryObject<Item> CAPTCHA_CARD = REGISTER.register("captcha_card", () -> new CaptchaCardItem(new Item.Properties()));
	public static final RegistryObject<Item> STACK_MODUS_CARD = REGISTER.register("stack_modus_card", () -> new Item(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> QUEUE_MODUS_CARD = REGISTER.register("queue_modus_card", () -> new Item(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> QUEUESTACK_MODUS_CARD = REGISTER.register("queuestack_modus_card", () -> new Item(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> TREE_MODUS_CARD = REGISTER.register("tree_modus_card", () -> new Item(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> HASHMAP_MODUS_CARD = REGISTER.register("hashmap_modus_card", () -> new Item(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> SET_MODUS_CARD = REGISTER.register("set_modus_card", () -> new Item(new Item.Properties().stacksTo(1)));
	
	public static final RegistryObject<Item> SHUNT = REGISTER.register("shunt", () -> new ShuntItem(new Item.Properties().stacksTo(1)));
	
	
	
	//Food
	public static final RegistryObject<Item> PHLEGM_GUSHERS = REGISTER.register("phlegm_gushers", () -> new HealingFoodItem(4F, new Item.Properties().food(MSFoods.PHLEGM_GUSHERS)));
	public static final RegistryObject<Item> SORROW_GUSHERS = REGISTER.register("sorrow_gushers", () -> new Item(new Item.Properties().food(MSFoods.SORROW_GUSHERS)));
	
	public static final RegistryObject<Item> BUG_ON_A_STICK = REGISTER.register("bug_on_a_stick", () -> new Item(new Item.Properties().food(MSFoods.BUG_ON_A_STICK)));
	public static final RegistryObject<Item> CHOCOLATE_BEETLE = REGISTER.register("chocolate_beetle", () -> new Item(new Item.Properties().food(MSFoods.CHOCOLATE_BEETLE)));
	public static final RegistryObject<Item> CONE_OF_FLIES = REGISTER.register("cone_of_flies", () -> new Item(new Item.Properties().food(MSFoods.CONE_OF_FLIES)));
	public static final RegistryObject<Item> GRASSHOPPER = REGISTER.register("grasshopper", () -> new Item(new Item.Properties().food(MSFoods.GRASSHOPPER)));
	public static final RegistryObject<Item> CICADA = REGISTER.register("cicada", () -> new Item(new Item.Properties().food(MSFoods.CICADA)));
	public static final RegistryObject<Item> JAR_OF_BUGS = REGISTER.register("jar_of_bugs", () -> new Item(new Item.Properties().food(MSFoods.JAR_OF_BUGS)));
	public static final RegistryObject<Item> BUG_MAC = REGISTER.register("bug_mac", () -> new Item(new Item.Properties().food(MSFoods.BUG_MAC)));
	public static final RegistryObject<Item> ONION = REGISTER.register("onion", () -> new Item(new Item.Properties().food(MSFoods.ONION)));
	public static final RegistryObject<Item> SALAD = REGISTER.register("salad", () -> new BowlFoodItem(new Item.Properties().food(MSFoods.SALAD).stacksTo(1)));
	public static final RegistryObject<Item> DESERT_FRUIT = REGISTER.register("desert_fruit", () -> new Item(new Item.Properties().food(MSFoods.DESERT_FRUIT)));
	public static final RegistryObject<Item> ROCK_COOKIE = REGISTER.register("rock_cookie", () -> new Item(new Item.Properties())); //Not actually food, but let's pretend it is
	public static final RegistryObject<Item> WOODEN_CARROT = REGISTER.register("wooden_carrot", () -> new Item(new Item.Properties().food(MSFoods.WOODEN_CARROT)));
	public static final RegistryObject<Item> FUNGAL_SPORE = REGISTER.register("fungal_spore", () -> new Item(new Item.Properties().food(MSFoods.FUNGAL_SPORE)));
	public static final RegistryObject<Item> SPOREO = REGISTER.register("sporeo", () -> new Item(new Item.Properties().food(MSFoods.SPOREO)));
	public static final RegistryObject<Item> MOREL_MUSHROOM = REGISTER.register("morel_mushroom", () -> new Item(new Item.Properties().food(MSFoods.MOREL_MUSHROOM)));
	public static final RegistryObject<Item> SUSHROOM = REGISTER.register("sushroom", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> FRENCH_FRY = REGISTER.register("french_fry", () -> new Item(new Item.Properties().food(MSFoods.FRENCH_FRY)));
	public static final RegistryObject<Item> STRAWBERRY_CHUNK = REGISTER.register("strawberry_chunk", () -> new ItemNameBlockItem(MSBlocks.STRAWBERRY_STEM.get(), new Item.Properties().food(MSFoods.STRAWBERRY_CHUNK)));
	public static final RegistryObject<Item> FOOD_CAN = REGISTER.register("food_can", () -> new Item(new Item.Properties().stacksTo(16).food(MSFoods.FOOD_CAN)));
	
	public static final RegistryObject<Item> CANDY_CORN = REGISTER.register("candy_corn", () -> new Item(new Item.Properties().food(MSFoods.CANDY_CORN)));
	public static final RegistryObject<Item> TUIX_BAR = REGISTER.register("tuix_bar", () -> new Item(new Item.Properties().food(MSFoods.TUIX_BAR)));
	public static final RegistryObject<Item> BUILD_GUSHERS = REGISTER.register("build_gushers", () -> new Item(new Item.Properties().food(MSFoods.BUILD_GUSHERS)));
	public static final RegistryObject<Item> AMBER_GUMMY_WORM = REGISTER.register("amber_gummy_worm", () -> new Item(new Item.Properties().food(MSFoods.AMBER_GUMMY_WORM)));
	public static final RegistryObject<Item> CAULK_PRETZEL = REGISTER.register("caulk_pretzel", () -> new Item(new Item.Properties().food(MSFoods.CAULK_PRETZEL)));
	public static final RegistryObject<Item> CHALK_CANDY_CIGARETTE = REGISTER.register("chalk_candy_cigarette", () -> new Item(new Item.Properties().food(MSFoods.CHALK_CANDY_CIGARETTE)));
	public static final RegistryObject<Item> IODINE_LICORICE = REGISTER.register("iodine_licorice", () -> new Item(new Item.Properties().food(MSFoods.IODINE_LICORICE)));
	public static final RegistryObject<Item> SHALE_PEEP = REGISTER.register("shale_peep", () -> new Item(new Item.Properties().food(MSFoods.SHALE_PEEP)));
	public static final RegistryObject<Item> TAR_LICORICE = REGISTER.register("tar_licorice", () -> new Item(new Item.Properties().food(MSFoods.TAR_LICORICE)));
	public static final RegistryObject<Item> COBALT_GUM = REGISTER.register("cobalt_gum", () -> new Item(new Item.Properties().food(MSFoods.COBALT_GUM)));
	public static final RegistryObject<Item> MARBLE_JAWBREAKER = REGISTER.register("marble_jawbreaker", () -> new Item(new Item.Properties().food(MSFoods.MARBLE_JAWBREAKER)));
	public static final RegistryObject<Item> MERCURY_SIXLETS = REGISTER.register("mercury_sixlets", () -> new Item(new Item.Properties().food(MSFoods.MERCURY_SIXLETS)));
	public static final RegistryObject<Item> QUARTZ_JELLY_BEAN = REGISTER.register("quartz_jelly_bean", () -> new Item(new Item.Properties().food(MSFoods.QUARTZ_JELLY_BEAN)));
	public static final RegistryObject<Item> SULFUR_CANDY_APPLE = REGISTER.register("sulfur_candy_apple", () -> new Item(new Item.Properties().food(MSFoods.SULFUR_CANDY_APPLE)));
	public static final RegistryObject<Item> AMETHYST_HARD_CANDY = REGISTER.register("amethyst_hard_candy", () -> new Item(new Item.Properties().food(MSFoods.AMETHYST_HARD_CANDY)));
	public static final RegistryObject<Item> GARNET_TWIX = REGISTER.register("garnet_twix", () -> new Item(new Item.Properties().food(MSFoods.GARNET_TWIX)));
	public static final RegistryObject<Item> RUBY_LOLLIPOP = REGISTER.register("ruby_lollipop", () -> new Item(new Item.Properties().food(MSFoods.RUBY_LOLLIPOP)));
	public static final RegistryObject<Item> RUST_GUMMY_EYE = REGISTER.register("rust_gummy_eye", () -> new Item(new Item.Properties().food(MSFoods.RUST_GUMMY_EYE)));
	public static final RegistryObject<Item> DIAMOND_MINT = REGISTER.register("diamond_mint", () -> new Item(new Item.Properties().food(MSFoods.DIAMOND_MINT)));
	public static final RegistryObject<Item> GOLD_CANDY_RIBBON = REGISTER.register("gold_candy_ribbon", () -> new Item(new Item.Properties().food(MSFoods.GOLD_CANDY_RIBBON)));
	public static final RegistryObject<Item> URANIUM_GUMMY_BEAR = REGISTER.register("uranium_gummy_bear", () -> new Item(new Item.Properties().food(MSFoods.URANIUM_GUMMY_BEAR)));
	public static final RegistryObject<Item> ARTIFACT_WARHEAD = REGISTER.register("artifact_warhead", () -> new Item(new Item.Properties().food(MSFoods.ARTIFACT_WARHEAD).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> ZILLIUM_SKITTLES = REGISTER.register("zillium_skittles", () -> new Item(new Item.Properties().food(MSFoods.ZILLIUM_SKITTLES).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> APPLE_JUICE = REGISTER.register("apple_juice", () -> new DrinkableItem(new Item.Properties().stacksTo(16).food(MSFoods.APPLE_JUICE)));
	public static final RegistryObject<Item> TAB = REGISTER.register("tab", () -> new DrinkableItem(new Item.Properties().stacksTo(16).food(MSFoods.TAB)));
	public static final RegistryObject<Item> ORANGE_FAYGO = REGISTER.register("orange_faygo", () -> new DrinkableItem(new Item.Properties().stacksTo(16).food(MSFoods.FAYGO)));
	public static final RegistryObject<Item> CANDY_APPLE_FAYGO = REGISTER.register("candy_apple_faygo", () -> new DrinkableItem(new Item.Properties().stacksTo(16).food(MSFoods.FAYGO_CANDY_APPLE)));
	public static final RegistryObject<Item> FAYGO_COLA = REGISTER.register("faygo_cola", () -> new DrinkableItem(new Item.Properties().stacksTo(16).food(MSFoods.FAYGO_COLA)));
	public static final RegistryObject<Item> COTTON_CANDY_FAYGO = REGISTER.register("cotton_candy_faygo", () -> new DrinkableItem(new Item.Properties().stacksTo(16).food(MSFoods.FAYGO_COTTON_CANDY)));
	public static final RegistryObject<Item> CREME_SODA_FAYGO = REGISTER.register("creme_soda_faygo", () -> new DrinkableItem(new Item.Properties().stacksTo(16).food(MSFoods.FAYGO_CREME)));
	public static final RegistryObject<Item> GRAPE_FAYGO = REGISTER.register("grape_faygo", () -> new DrinkableItem(new Item.Properties().stacksTo(16).food(MSFoods.FAYGO_GRAPE)));
	public static final RegistryObject<Item> MOON_MIST_FAYGO = REGISTER.register("moon_mist_faygo", () -> new DrinkableItem(new Item.Properties().stacksTo(16).food(MSFoods.FAYGO_MOON_MIST)));
	public static final RegistryObject<Item> PEACH_FAYGO = REGISTER.register("peach_faygo", () -> new DrinkableItem(new Item.Properties().stacksTo(16).food(MSFoods.FAYGO_PEACH)));
	public static final RegistryObject<Item> REDPOP_FAYGO = REGISTER.register("redpop_faygo", () -> new DrinkableItem(new Item.Properties().stacksTo(16).food(MSFoods.FAYGO_REDPOP)));
	public static final RegistryObject<Item> GRUB_SAUCE = REGISTER.register("grub_sauce", () -> new DrinkableItem(new Item.Properties().stacksTo(16).food(MSFoods.GRUB_SAUCE)));
	public static final RegistryObject<Item> IRRADIATED_STEAK = REGISTER.register("irradiated_steak", () -> new Item(new Item.Properties().food(MSFoods.IRRADIATED_STEAK)));
	public static final RegistryObject<Item> SURPRISE_EMBRYO = REGISTER.register("surprise_embryo", () -> new SurpriseEmbryoItem(new Item.Properties().food(MSFoods.SURPRISE_EMBRYO)));
	public static final RegistryObject<Item> UNKNOWABLE_EGG = REGISTER.register("unknowable_egg", () -> new UnknowableEggItem(new Item.Properties().stacksTo(16).food(MSFoods.UNKNOWABLE_EGG)));
	public static final RegistryObject<Item> BREADCRUMBS = REGISTER.register("breadcrumbs", () -> new Item(new Item.Properties().food(MSFoods.BREADCRUMBS)));
	
	
	
	//Other Land Items
	public static final RegistryObject<Item> GOLDEN_GRASSHOPPER = REGISTER.register("golden_grasshopper", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> BUG_NET = REGISTER.register("bug_net", () -> new BugNetItem(new Item.Properties().defaultDurability(64)));
	public static final RegistryObject<Item> FROG = REGISTER.register("frog", () -> new FrogItem(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> CARVING_TOOL = REGISTER.register("carving_tool", () -> new Item(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<MSArmorItem> CRUMPLY_HAT = REGISTER.register("crumply_hat", () -> new MSArmorItem(MSItemTypes.CLOTH_ARMOR, ArmorItem.Type.HELMET, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> STONE_EYEBALLS = REGISTER.register("stone_eyeballs", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> STONE_TABLET = REGISTER.register("stone_tablet", () -> new StoneTabletItem(MSBlocks.STONE_TABLET.get(), new Item.Properties()));
	public static final RegistryObject<Item> SHOP_POSTER = REGISTER.register("shop_poster", () -> new HangingItem(ShopPosterEntity::new, new Item.Properties().stacksTo(1))); //not used
	public static final RegistryObject<Item> GUTTER_THUMB_DRIVE = REGISTER.register("gutter_thumb_drive", () -> new GutterThumbDriveItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> ANCIENT_THUMB_DRIVE = REGISTER.register("ancient_thumb_drive", () -> new AncientThumbDrive(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> GUTTER_BALL = REGISTER.register("gutter_ball", () -> new GutterBallItem(new Item.Properties().stacksTo(5).rarity(Rarity.UNCOMMON)));
	
	//Buckets
	public static final RegistryObject<Item> OIL_BUCKET = REGISTER.register("oil_bucket", () -> new BucketItem(MSFluids.OIL, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
	public static final RegistryObject<Item> BLOOD_BUCKET = REGISTER.register("blood_bucket", () -> new BucketItem(MSFluids.BLOOD, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
	public static final RegistryObject<Item> BRAIN_JUICE_BUCKET = REGISTER.register("brain_juice_bucket", () -> new BucketItem(MSFluids.BRAIN_JUICE, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
	public static final RegistryObject<Item> WATER_COLORS_BUCKET = REGISTER.register("water_colors_bucket", () -> new BucketItem(MSFluids.WATER_COLORS, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
	public static final RegistryObject<Item> ENDER_BUCKET = REGISTER.register("ender_bucket", () -> new BucketItem(MSFluids.ENDER, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
	public static final RegistryObject<Item> LIGHT_WATER_BUCKET = REGISTER.register("light_water_bucket", () -> new BucketItem(MSFluids.LIGHT_WATER, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
	public static final RegistryObject<Item> OBSIDIAN_BUCKET = REGISTER.register("obsidian_bucket", () -> new ObsidianBucketItem(new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));
	
	
	
	//Alchemy Items
	public static final RegistryObject<Item> DICE = REGISTER.register("dice", () -> new RightClickMessageItem(new Item.Properties(), RightClickMessageItem.Type.DICE));
	public static final RegistryObject<Item> PLUTONIUM_CORE = REGISTER.register("plutonium_core", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> GRIMOIRE = REGISTER.register("grimoire", () -> new GrimoireItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> BATTERY = REGISTER.register("battery", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> BARBASOL = REGISTER.register("barbasol", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> CLOTHES_IRON = REGISTER.register("clothes_iron", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> INK_SQUID_PRO_QUO = REGISTER.register("ink_squid_pro_quo", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> CUEBALL = REGISTER.register("cueball", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> EIGHTBALL = REGISTER.register("eightball", () -> new RightClickMessageItem(new Item.Properties(), RightClickMessageItem.Type.EIGHTBALL));
	public static final RegistryObject<Item> FLARP_MANUAL = REGISTER.register("flarp_manual", () -> new Item(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> SASSACRE_TEXT = REGISTER.register("sassacre_text", () -> new Item(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> WISEGUY = REGISTER.register("wiseguy", () -> new Item(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> TABLESTUCK_MANUAL = REGISTER.register("tablestuck_manual", () -> new Item(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> TILLDEATH_HANDBOOK = REGISTER.register("tilldeath_handbook", () -> new Item(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> BINARY_CODE = REGISTER.register("binary_code", () -> new Item(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> NONBINARY_CODE = REGISTER.register("nonbinary_code", () -> new Item(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> THRESH_DVD = REGISTER.register("thresh_dvd", () -> new Item(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> GAMEBRO_MAGAZINE = REGISTER.register("gamebro_magazine", () -> new Item(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> GAMEGRL_MAGAZINE = REGISTER.register("gamegrl_magazine", () -> new Item(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> CREW_POSTER = REGISTER.register("crew_poster", () -> new HangingItem((world, pos, facing, stack) -> new CrewPosterEntity(world, pos, facing), new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> SBAHJ_POSTER = REGISTER.register("sbahj_poster", () -> new HangingItem((world, pos, facing, stack) -> new SbahjPosterEntity(world, pos, facing), new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> BI_DYE = REGISTER.register("bi_dye", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> LIP_BALM = REGISTER.register("lip_balm", () -> new RightClickMessageItem(new Item.Properties(), RightClickMessageItem.Type.DEFAULT));
	public static final RegistryObject<Item> ELECTRIC_AUTOHARP = REGISTER.register("electric_autoharp", () -> new RightClickMusicItem(new Item.Properties(), RightClickMusicItem.Type.ELECTRIC_AUTOHARP));
	public static final RegistryObject<Item> CARDBOARD_TUBE = REGISTER.register("cardboard_tube", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> CRYPTID_PHOTO = REGISTER.register("cryptid_photo",() -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> PARTICLE_ACCELERATOR = REGISTER.register("particle_accelerator", () -> new Item(new Item.Properties()));
	
	
	
	//Other
	public static final RegistryObject<Item> CAPTCHAROID_CAMERA = REGISTER.register("captcharoid_camera", () -> new CaptcharoidCameraItem(new Item.Properties().defaultDurability(64)));
	public static final RegistryObject<Item> LONG_FORGOTTEN_WARHORN = REGISTER.register("long_forgotten_warhorn", () -> new LongForgottenWarhornItem(new Item.Properties().defaultDurability(100).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> BLACK_QUEENS_RING = REGISTER.register("black_queens_ring", () -> new Item(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> WHITE_QUEENS_RING = REGISTER.register("white_queens_ring", () -> new Item(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> BARBASOL_BOMB = REGISTER.register("barbasol_bomb", () -> new BarbasolBombItem(new Item.Properties().stacksTo(16)));
	public static final RegistryObject<Item> RAZOR_BLADE = REGISTER.register("razor_blade", () -> new RazorBladeItem(new Item.Properties()));
	public static final RegistryObject<Item> ICE_SHARD = REGISTER.register("ice_shard", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> HORN = REGISTER.register("horn", () -> new SoundItem(MSSoundEvents.ITEM_HORN_USE, new Item.Properties()));
	public static final RegistryObject<Item> CAKE_MIX = REGISTER.register("cake_mix", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> TEMPLE_SCANNER = REGISTER.register("temple_scanner", () -> new StructureScannerItem(new Item.Properties().stacksTo(1), MSTags.Structures.SCANNER_LOCATED, MSItems.RAW_URANIUM));
	
	public static final RegistryObject<Item> SCALEMATE_APPLESCAB = REGISTER.register("scalemate_applescab", () -> new ScalemateItem(new Item.Properties()));
	public static final RegistryObject<Item> SCALEMATE_BERRYBREATH = REGISTER.register("scalemate_berrybreath", () -> new ScalemateItem(new Item.Properties()));
	public static final RegistryObject<Item> SCALEMATE_CINNAMONWHIFF = REGISTER.register("scalemate_cinnamonwhiff", () -> new ScalemateItem(new Item.Properties()));
	public static final RegistryObject<Item> SCALEMATE_HONEYTONGUE = REGISTER.register("scalemate_honeytongue", () -> new ScalemateItem(new Item.Properties()));
	public static final RegistryObject<Item> SCALEMATE_LEMONSNOUT = REGISTER.register("scalemate_lemonsnout", () -> new ScalemateItem(new Item.Properties()));
	public static final RegistryObject<Item> SCALEMATE_PINESNORT = REGISTER.register("scalemate_pinesnort", () -> new ScalemateItem(new Item.Properties()));
	public static final RegistryObject<Item> SCALEMATE_PUCEFOOT = REGISTER.register("scalemate_pucefoot", () -> new ScalemateItem(new Item.Properties()));
	public static final RegistryObject<Item> SCALEMATE_PUMPKINSNUFFLE = REGISTER.register("scalemate_pumpkinsnuffle", () -> new ScalemateItem(new Item.Properties()));
	public static final RegistryObject<Item> SCALEMATE_PYRALSPITE = REGISTER.register("scalemate_pyralspite", () -> new ScalemateItem(new Item.Properties()));
	public static final RegistryObject<Item> SCALEMATE_WITNESS = REGISTER.register("scalemate_witness", () -> new ScalemateItem(new Item.Properties()));
	
	//Consort Plushies
	public static final RegistryObject<Item> PLUSH_SALAMANDER = REGISTER.register("plush_salamander", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> PLUSH_NAKAGATOR = REGISTER.register("plush_nakagator", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> PLUSH_IGUANA = REGISTER.register("plush_iguana", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> PLUSH_TURTLE = REGISTER.register("plush_turtle", () -> new Item(new Item.Properties()));
	
	public static final RegistryObject<Item> PLUSH_MUTATED_CAT = REGISTER.register("plush_mutated_cat", () -> new Item(new Item.Properties()));
	
	//Incredibly Useful Items
	public static final RegistryObject<Item> URANIUM_POWERED_STICK = REGISTER.register("uranium_powered_stick", () -> new Item(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> IRON_BOAT = REGISTER.register("iron_boat", () -> new CustomBoatItem((stack, world, x, y, z) -> new MetalBoatEntity(world, x, y, z, MetalBoatEntity.Type.IRON), new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> GOLD_BOAT = REGISTER.register("gold_boat", () -> new CustomBoatItem((stack, world, x, y, z) -> new MetalBoatEntity(world, x, y, z, MetalBoatEntity.Type.GOLD), new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> COCOA_WART = REGISTER.register("cocoa_wart", () -> new Item(new Item.Properties()));
	public static final RegistryObject<MultiblockItem> HORSE_CLOCK = REGISTER.register("horse_clock", () -> new MultiblockItem(MSBlocks.HORSE_CLOCK, new Item.Properties()));
	
	
	//Music Discs/Cassettes
	public static final RegistryObject<Item> MUSIC_DISC_EMISSARY_OF_DANCE = REGISTER.register("music_disc_emissary_of_dance", () -> new RecordItem(1, MSSoundEvents.MUSIC_DISC_EMISSARY_OF_DANCE, new Item.Properties().rarity(Rarity.RARE).stacksTo(1), 11040));
	public static final RegistryObject<Item> MUSIC_DISC_DANCE_STAB_DANCE = REGISTER.register("music_disc_dance_stab_dance", () -> new RecordItem(2, MSSoundEvents.MUSIC_DISC_DANCE_STAB_DANCE, new Item.Properties().rarity(Rarity.RARE).stacksTo(1), 13440));
	public static final RegistryObject<Item> MUSIC_DISC_RETRO_BATTLE = REGISTER.register("music_disc_retro_battle", () -> new RecordItem(3, MSSoundEvents.MUSIC_DISC_RETRO_BATTLE_THEME, new Item.Properties().rarity(Rarity.RARE).stacksTo(1), 2760));
	
	public static final RegistryObject<Item> CASSETTE_13 = REGISTER.register("cassette_13", () -> new CassetteItem(1, EnumCassetteType.THIRTEEN, new Item.Properties().rarity(Rarity.RARE).stacksTo(1), 3560));
	public static final RegistryObject<Item> CASSETTE_CAT = REGISTER.register("cassette_cat", () -> new CassetteItem(2, EnumCassetteType.CAT, new Item.Properties().rarity(Rarity.RARE).stacksTo(1), 3700));
	public static final RegistryObject<Item> CASSETTE_BLOCKS = REGISTER.register("cassette_blocks", () -> new CassetteItem(3, EnumCassetteType.BLOCKS, new Item.Properties().rarity(Rarity.RARE).stacksTo(1), 6900));
	public static final RegistryObject<Item> CASSETTE_CHIRP = REGISTER.register("cassette_chirp", () -> new CassetteItem(4, EnumCassetteType.CHIRP, new Item.Properties().rarity(Rarity.RARE).stacksTo(1), 3700));
	public static final RegistryObject<Item> CASSETTE_FAR = REGISTER.register("cassette_far", () -> new CassetteItem(5, EnumCassetteType.FAR, new Item.Properties().rarity(Rarity.RARE).stacksTo(1), 3480));
	public static final RegistryObject<Item> CASSETTE_MALL = REGISTER.register("cassette_mall", () -> new CassetteItem(6, EnumCassetteType.MALL, new Item.Properties().rarity(Rarity.RARE).stacksTo(1), 3940));
	public static final RegistryObject<Item> CASSETTE_MELLOHI = REGISTER.register("cassette_mellohi", () -> new CassetteItem(7, EnumCassetteType.MELLOHI, new Item.Properties().rarity(Rarity.RARE).stacksTo(1), 1920));
	public static final RegistryObject<Item> CASSETTE_DANCE_STAB = REGISTER.register("cassette_dance_stab", () -> new CassetteItem(2, EnumCassetteType.DANCE_STAB_DANCE, new Item.Properties().rarity(Rarity.RARE).stacksTo(1), 13440));
	public static final RegistryObject<Item> CASSETTE_RETRO_BATTLE = REGISTER.register("cassette_retro_battle", () -> new CassetteItem(3, EnumCassetteType.RETRO_BATTLE_THEME, new Item.Properties().rarity(Rarity.RARE).stacksTo(1), 2760));
	public static final RegistryObject<Item> CASSETTE_EMISSARY = REGISTER.register("cassette_emissary", () -> new CassetteItem(1, EnumCassetteType.EMISSARY_OF_DANCE, new Item.Properties().rarity(Rarity.RARE).stacksTo(1), 11040));
	public static final RegistryObject<Item> CASSETTE_11 = REGISTER.register("cassette_11", () -> new CassetteItem(11, EnumCassetteType.ELEVEN, new Item.Properties().rarity(Rarity.RARE).stacksTo(1), 1420));
	public static final RegistryObject<Item> CASSETTE_PIGSTEP = REGISTER.register("cassette_pigstep", () -> new CassetteItem(13, EnumCassetteType.PIGSTEP, new Item.Properties().rarity(Rarity.RARE).stacksTo(1), 2980));
	public static final RegistryObject<Item> CASSETTE_STAL = REGISTER.register("cassette_stal", () -> new CassetteItem(8, EnumCassetteType.STAL, new Item.Properties().rarity(Rarity.RARE).stacksTo(1), 3000));
	public static final RegistryObject<Item> CASSETTE_STRAD = REGISTER.register("cassette_strad", () -> new CassetteItem(9, EnumCassetteType.STRAD, new Item.Properties().rarity(Rarity.RARE).stacksTo(1), 3760));
	public static final RegistryObject<Item> CASSETTE_WAIT = REGISTER.register("cassette_wait", () -> new CassetteItem(12, EnumCassetteType.WAIT, new Item.Properties().rarity(Rarity.RARE).stacksTo(1), 4760));
	public static final RegistryObject<Item> CASSETTE_WARD = REGISTER.register("cassette_ward", () -> new CassetteItem(10, EnumCassetteType.WARD, new Item.Properties().rarity(Rarity.RARE).stacksTo(1), 5020));
	public static final RegistryObject<Item> CASSETTE_OTHERSIDE = REGISTER.register("cassette_otherside", () -> new CassetteItem(14, EnumCassetteType.OTHERSIDE, new Item.Properties().rarity(Rarity.EPIC).stacksTo(1), 3900));
	public static final RegistryObject<Item> CASSETTE_5 = REGISTER.register("cassette_5", () -> new CassetteItem(15, EnumCassetteType.FIVE, new Item.Properties().rarity(Rarity.RARE).stacksTo(1), 3560));
	
	
	/** Start of block items **/
	
	//Sburb Machines
	public static final RegistryObject<MultiblockItem> CRUXTRUDER = REGISTER.register("cruxtruder", () -> new CruxtruderItem(MSBlocks.CRUXTRUDER, new Item.Properties()));
	public static final RegistryObject<BlockItem> CRUXTRUDER_LID = registerBlockItem(MSBlocks.CRUXTRUDER_LID);
	public static final RegistryObject<MultiblockItem> TOTEM_LATHE = REGISTER.register("totem_lathe", () -> new MultiblockItem(MSBlocks.TOTEM_LATHE, new Item.Properties()));
	public static final RegistryObject<MultiblockItem> ALCHEMITER = REGISTER.register("alchemiter", () -> new MultiblockItem(MSBlocks.ALCHEMITER, new Item.Properties()));
	public static final RegistryObject<MultiblockItem> PUNCH_DESIGNIX = REGISTER.register("punch_designix", () -> new MultiblockItem(MSBlocks.PUNCH_DESIGNIX, new Item.Properties()));
	
	public static final RegistryObject<BlockItem> MINI_CRUXTRUDER = REGISTER.register("mini_cruxtruder", () -> new MiniCruxtruderItem(MSBlocks.MINI_CRUXTRUDER.get(), new Item.Properties()));
	public static final RegistryObject<BlockItem> MINI_TOTEM_LATHE = registerBlockItem(MSBlocks.MINI_TOTEM_LATHE);
	public static final RegistryObject<BlockItem> MINI_ALCHEMITER = registerBlockItem(MSBlocks.MINI_ALCHEMITER);
	public static final RegistryObject<BlockItem> MINI_PUNCH_DESIGNIX = registerBlockItem(MSBlocks.MINI_PUNCH_DESIGNIX);
	public static final RegistryObject<BlockItem> HOLOPAD = registerBlockItem(MSBlocks.HOLOPAD);
	public static final RegistryObject<BlockItem> INTELLIBEAM_LASERSTATION = registerBlockItem(MSBlocks.INTELLIBEAM_LASERSTATION);
	
	
	//Misc Core Objects
	public static final RegistryObject<BlockItem> CRUXITE_DOWEL = registerBlockItem(MSBlocks.CRUXITE_DOWEL, block -> new DowelItem(block, new Item.Properties()));
	public static final RegistryObject<MultiblockItem> LOTUS_TIME_CAPSULE = REGISTER.register("lotus_time_capsule", () -> new MultiblockItem(MSBlocks.LOTUS_TIME_CAPSULE_BLOCK, new Item.Properties()));
	
	
	
	//Skaia
	public static final RegistryObject<BlockItem> BLACK_CHESS_DIRT = registerBlockItem(MSBlocks.BLACK_CHESS_DIRT);
	public static final RegistryObject<BlockItem> WHITE_CHESS_DIRT = registerBlockItem(MSBlocks.WHITE_CHESS_DIRT);
	public static final RegistryObject<BlockItem> DARK_GRAY_CHESS_DIRT = registerBlockItem(MSBlocks.DARK_GRAY_CHESS_DIRT);
	public static final RegistryObject<BlockItem> LIGHT_GRAY_CHESS_DIRT = registerBlockItem(MSBlocks.LIGHT_GRAY_CHESS_DIRT);
	public static final RegistryObject<BlockItem> SKAIA_PORTAL = registerBlockItem(MSBlocks.SKAIA_PORTAL, new Item.Properties().rarity(Rarity.EPIC));
	
	public static final RegistryObject<BlockItem> BLACK_CHESS_BRICKS = registerBlockItem(MSBlocks.BLACK_CHESS_BRICKS);
	public static final RegistryObject<BlockItem> DARK_GRAY_CHESS_BRICKS = registerBlockItem(MSBlocks.DARK_GRAY_CHESS_BRICKS);
	public static final RegistryObject<BlockItem> LIGHT_GRAY_CHESS_BRICKS = registerBlockItem(MSBlocks.LIGHT_GRAY_CHESS_BRICKS);
	public static final RegistryObject<BlockItem> WHITE_CHESS_BRICKS = registerBlockItem(MSBlocks.WHITE_CHESS_BRICKS);
	public static final RegistryObject<BlockItem> BLACK_CHESS_BRICK_SMOOTH = registerBlockItem(MSBlocks.BLACK_CHESS_BRICK_SMOOTH);
	public static final RegistryObject<BlockItem> DARK_GRAY_CHESS_BRICK_SMOOTH = registerBlockItem(MSBlocks.DARK_GRAY_CHESS_BRICK_SMOOTH);
	public static final RegistryObject<BlockItem> LIGHT_GRAY_CHESS_BRICK_SMOOTH = registerBlockItem(MSBlocks.LIGHT_GRAY_CHESS_BRICK_SMOOTH);
	public static final RegistryObject<BlockItem> WHITE_CHESS_BRICK_SMOOTH = registerBlockItem(MSBlocks.WHITE_CHESS_BRICK_SMOOTH);
	public static final RegistryObject<BlockItem> BLACK_CHESS_BRICK_TRIM = registerBlockItem(MSBlocks.BLACK_CHESS_BRICK_TRIM);
	public static final RegistryObject<BlockItem> DARK_GRAY_CHESS_BRICK_TRIM = registerBlockItem(MSBlocks.DARK_GRAY_CHESS_BRICK_TRIM);
	public static final RegistryObject<BlockItem> LIGHT_GRAY_CHESS_BRICK_TRIM = registerBlockItem(MSBlocks.LIGHT_GRAY_CHESS_BRICK_TRIM);
	public static final RegistryObject<BlockItem> WHITE_CHESS_BRICK_TRIM = registerBlockItem(MSBlocks.WHITE_CHESS_BRICK_TRIM);
	public static final RegistryObject<BlockItem> CHECKERED_STAINED_GLASS = registerBlockItem(MSBlocks.CHECKERED_STAINED_GLASS);
	public static final RegistryObject<BlockItem> BLACK_CROWN_STAINED_GLASS = registerBlockItem(MSBlocks.BLACK_CROWN_STAINED_GLASS);
	public static final RegistryObject<BlockItem> BLACK_PAWN_STAINED_GLASS = registerBlockItem(MSBlocks.BLACK_PAWN_STAINED_GLASS);
	public static final RegistryObject<BlockItem> WHITE_CROWN_STAINED_GLASS = registerBlockItem(MSBlocks.WHITE_CROWN_STAINED_GLASS);
	public static final RegistryObject<BlockItem> WHITE_PAWN_STAINED_GLASS = registerBlockItem(MSBlocks.WHITE_PAWN_STAINED_GLASS);
	
	
	
	//Ores
	public static final RegistryObject<BlockItem> STONE_CRUXITE_ORE = registerBlockItem(MSBlocks.STONE_CRUXITE_ORE);
	public static final RegistryObject<BlockItem> NETHERRACK_CRUXITE_ORE = registerBlockItem(MSBlocks.NETHERRACK_CRUXITE_ORE);
	public static final RegistryObject<BlockItem> COBBLESTONE_CRUXITE_ORE = registerBlockItem(MSBlocks.COBBLESTONE_CRUXITE_ORE);
	public static final RegistryObject<BlockItem> SANDSTONE_CRUXITE_ORE = registerBlockItem(MSBlocks.SANDSTONE_CRUXITE_ORE);
	public static final RegistryObject<BlockItem> RED_SANDSTONE_CRUXITE_ORE = registerBlockItem(MSBlocks.RED_SANDSTONE_CRUXITE_ORE);
	public static final RegistryObject<BlockItem> END_STONE_CRUXITE_ORE = registerBlockItem(MSBlocks.END_STONE_CRUXITE_ORE);
	public static final RegistryObject<BlockItem> SHADE_STONE_CRUXITE_ORE = registerBlockItem(MSBlocks.SHADE_STONE_CRUXITE_ORE);
	public static final RegistryObject<BlockItem> PINK_STONE_CRUXITE_ORE = registerBlockItem(MSBlocks.PINK_STONE_CRUXITE_ORE);
	public static final RegistryObject<BlockItem> MYCELIUM_STONE_CRUXITE_ORE = registerBlockItem(MSBlocks.MYCELIUM_STONE_CRUXITE_ORE);
	public static final RegistryObject<BlockItem> STONE_URANIUM_ORE = registerBlockItem(MSBlocks.STONE_URANIUM_ORE);
	public static final RegistryObject<BlockItem> DEEPSLATE_URANIUM_ORE = registerBlockItem(MSBlocks.DEEPSLATE_URANIUM_ORE);
	public static final RegistryObject<BlockItem> NETHERRACK_URANIUM_ORE = registerBlockItem(MSBlocks.NETHERRACK_URANIUM_ORE);
	public static final RegistryObject<BlockItem> COBBLESTONE_URANIUM_ORE = registerBlockItem(MSBlocks.COBBLESTONE_URANIUM_ORE);
	public static final RegistryObject<BlockItem> SANDSTONE_URANIUM_ORE = registerBlockItem(MSBlocks.SANDSTONE_URANIUM_ORE);
	public static final RegistryObject<BlockItem> RED_SANDSTONE_URANIUM_ORE = registerBlockItem(MSBlocks.RED_SANDSTONE_URANIUM_ORE);
	public static final RegistryObject<BlockItem> END_STONE_URANIUM_ORE = registerBlockItem(MSBlocks.END_STONE_URANIUM_ORE);
	public static final RegistryObject<BlockItem> SHADE_STONE_URANIUM_ORE = registerBlockItem(MSBlocks.SHADE_STONE_URANIUM_ORE);
	public static final RegistryObject<BlockItem> PINK_STONE_URANIUM_ORE = registerBlockItem(MSBlocks.PINK_STONE_URANIUM_ORE);
	public static final RegistryObject<BlockItem> MYCELIUM_STONE_URANIUM_ORE = registerBlockItem(MSBlocks.MYCELIUM_STONE_URANIUM_ORE);
	public static final RegistryObject<BlockItem> NETHERRACK_COAL_ORE = registerBlockItem(MSBlocks.NETHERRACK_COAL_ORE);
	public static final RegistryObject<BlockItem> SHADE_STONE_COAL_ORE = registerBlockItem(MSBlocks.SHADE_STONE_COAL_ORE);
	public static final RegistryObject<BlockItem> PINK_STONE_COAL_ORE = registerBlockItem(MSBlocks.PINK_STONE_COAL_ORE);
	public static final RegistryObject<BlockItem> END_STONE_IRON_ORE = registerBlockItem(MSBlocks.END_STONE_IRON_ORE);
	public static final RegistryObject<BlockItem> SANDSTONE_IRON_ORE = registerBlockItem(MSBlocks.SANDSTONE_IRON_ORE);
	public static final RegistryObject<BlockItem> RED_SANDSTONE_IRON_ORE = registerBlockItem(MSBlocks.RED_SANDSTONE_IRON_ORE);
	public static final RegistryObject<BlockItem> SANDSTONE_GOLD_ORE = registerBlockItem(MSBlocks.SANDSTONE_GOLD_ORE);
	public static final RegistryObject<BlockItem> RED_SANDSTONE_GOLD_ORE = registerBlockItem(MSBlocks.RED_SANDSTONE_GOLD_ORE);
	public static final RegistryObject<BlockItem> SHADE_STONE_GOLD_ORE = registerBlockItem(MSBlocks.SHADE_STONE_GOLD_ORE);
	public static final RegistryObject<BlockItem> PINK_STONE_GOLD_ORE = registerBlockItem(MSBlocks.PINK_STONE_GOLD_ORE);
	public static final RegistryObject<BlockItem> END_STONE_REDSTONE_ORE = registerBlockItem(MSBlocks.END_STONE_REDSTONE_ORE);
	public static final RegistryObject<BlockItem> STONE_QUARTZ_ORE = registerBlockItem(MSBlocks.STONE_QUARTZ_ORE);
	public static final RegistryObject<BlockItem> PINK_STONE_LAPIS_ORE = registerBlockItem(MSBlocks.PINK_STONE_LAPIS_ORE);
	public static final RegistryObject<BlockItem> PINK_STONE_DIAMOND_ORE = registerBlockItem(MSBlocks.PINK_STONE_DIAMOND_ORE);
	
	
	
	//Resource Blocks
	public static final RegistryObject<BlockItem> CRUXITE_BLOCK = registerBlockItem(MSBlocks.CRUXITE_BLOCK);
	public static final RegistryObject<BlockItem> URANIUM_BLOCK = registerBlockItem(MSBlocks.URANIUM_BLOCK);
	public static final RegistryObject<BlockItem> GENERIC_OBJECT = registerBlockItem(MSBlocks.GENERIC_OBJECT);
	
	
	
	//Land Environment
	public static final RegistryObject<BlockItem> BLUE_DIRT = registerBlockItem(MSBlocks.BLUE_DIRT);
	public static final RegistryObject<BlockItem> THOUGHT_DIRT = registerBlockItem(MSBlocks.THOUGHT_DIRT);
	public static final RegistryObject<BlockItem> COARSE_STONE = registerBlockItem(MSBlocks.COARSE_STONE);
	public static final RegistryObject<BlockItem> CHISELED_COARSE_STONE = registerBlockItem(MSBlocks.CHISELED_COARSE_STONE);
	public static final RegistryObject<BlockItem> COARSE_STONE_BRICKS = registerBlockItem(MSBlocks.COARSE_STONE_BRICKS);
	public static final RegistryObject<BlockItem> COARSE_STONE_COLUMN = registerBlockItem(MSBlocks.COARSE_STONE_COLUMN);
	public static final RegistryObject<BlockItem> CHISELED_COARSE_STONE_BRICKS = registerBlockItem(MSBlocks.CHISELED_COARSE_STONE_BRICKS);
	public static final RegistryObject<BlockItem> CRACKED_COARSE_STONE_BRICKS = registerBlockItem(MSBlocks.CRACKED_COARSE_STONE_BRICKS);
	public static final RegistryObject<BlockItem> MOSSY_COARSE_STONE_BRICKS = registerBlockItem(MSBlocks.MOSSY_COARSE_STONE_BRICKS);
	public static final RegistryObject<BlockItem> SHADE_STONE = registerBlockItem(MSBlocks.SHADE_STONE);
	public static final RegistryObject<BlockItem> SMOOTH_SHADE_STONE = registerBlockItem(MSBlocks.SMOOTH_SHADE_STONE);
	public static final RegistryObject<BlockItem> SHADE_BRICKS = registerBlockItem(MSBlocks.SHADE_BRICKS);
	public static final RegistryObject<BlockItem> SHADE_COLUMN = registerBlockItem(MSBlocks.SHADE_COLUMN);
	public static final RegistryObject<BlockItem> CHISELED_SHADE_BRICKS = registerBlockItem(MSBlocks.CHISELED_SHADE_BRICKS);
	public static final RegistryObject<BlockItem> CRACKED_SHADE_BRICKS = registerBlockItem(MSBlocks.CRACKED_SHADE_BRICKS);
	public static final RegistryObject<BlockItem> MOSSY_SHADE_BRICKS = registerBlockItem(MSBlocks.MOSSY_SHADE_BRICKS);
	public static final RegistryObject<BlockItem> BLOOD_SHADE_BRICKS = registerBlockItem(MSBlocks.BLOOD_SHADE_BRICKS);
	public static final RegistryObject<BlockItem> TAR_SHADE_BRICKS = registerBlockItem(MSBlocks.TAR_SHADE_BRICKS);
	public static final RegistryObject<BlockItem> FROST_TILE = registerBlockItem(MSBlocks.FROST_TILE);
	public static final RegistryObject<BlockItem> CHISELED_FROST_TILE = registerBlockItem(MSBlocks.CHISELED_FROST_TILE);
	public static final RegistryObject<BlockItem> FROST_BRICKS = registerBlockItem(MSBlocks.FROST_BRICKS);
	public static final RegistryObject<BlockItem> FROST_COLUMN = registerBlockItem(MSBlocks.FROST_COLUMN);
	public static final RegistryObject<BlockItem> CHISELED_FROST_BRICKS = registerBlockItem(MSBlocks.CHISELED_FROST_BRICKS);
	public static final RegistryObject<BlockItem> CRACKED_FROST_BRICKS = registerBlockItem(MSBlocks.CRACKED_FROST_BRICKS);
	public static final RegistryObject<BlockItem> FLOWERY_FROST_BRICKS = registerBlockItem(MSBlocks.FLOWERY_FROST_BRICKS);
	public static final RegistryObject<BlockItem> CAST_IRON = registerBlockItem(MSBlocks.CAST_IRON);
	public static final RegistryObject<BlockItem> CHISELED_CAST_IRON = registerBlockItem(MSBlocks.CHISELED_CAST_IRON);
	public static final RegistryObject<BlockItem> STEEL_BEAM = registerBlockItem(MSBlocks.STEEL_BEAM);
	public static final RegistryObject<BlockItem> MYCELIUM_COBBLESTONE = registerBlockItem(MSBlocks.MYCELIUM_COBBLESTONE);
	public static final RegistryObject<BlockItem> MYCELIUM_STONE = registerBlockItem(MSBlocks.MYCELIUM_STONE);
	public static final RegistryObject<BlockItem> POLISHED_MYCELIUM_STONE = registerBlockItem(MSBlocks.POLISHED_MYCELIUM_STONE);
	public static final RegistryObject<BlockItem> MYCELIUM_BRICKS = registerBlockItem(MSBlocks.MYCELIUM_BRICKS);
	public static final RegistryObject<BlockItem> MYCELIUM_COLUMN = registerBlockItem(MSBlocks.MYCELIUM_COLUMN);
	public static final RegistryObject<BlockItem> CHISELED_MYCELIUM_BRICKS = registerBlockItem(MSBlocks.CHISELED_MYCELIUM_BRICKS);
	public static final RegistryObject<BlockItem> SUSPICIOUS_CHISELED_MYCELIUM_BRICKS = registerBlockItem(MSBlocks.SUSPICIOUS_CHISELED_MYCELIUM_BRICKS);
	public static final RegistryObject<BlockItem> CRACKED_MYCELIUM_BRICKS = registerBlockItem(MSBlocks.CRACKED_MYCELIUM_BRICKS);
	public static final RegistryObject<BlockItem> MOSSY_MYCELIUM_BRICKS = registerBlockItem(MSBlocks.MOSSY_MYCELIUM_BRICKS);
	public static final RegistryObject<BlockItem> FLOWERY_MYCELIUM_BRICKS = registerBlockItem(MSBlocks.FLOWERY_MYCELIUM_BRICKS);
	public static final RegistryObject<BlockItem> BLACK_STONE = registerBlockItem(MSBlocks.BLACK_STONE);
	public static final RegistryObject<BlockItem> POLISHED_BLACK_STONE = registerBlockItem(MSBlocks.POLISHED_BLACK_STONE);
	public static final RegistryObject<BlockItem> BLACK_COBBLESTONE = registerBlockItem(MSBlocks.BLACK_COBBLESTONE);
	public static final RegistryObject<BlockItem> BLACK_STONE_BRICKS = registerBlockItem(MSBlocks.BLACK_STONE_BRICKS);
	public static final RegistryObject<BlockItem> BLACK_STONE_COLUMN = registerBlockItem(MSBlocks.BLACK_STONE_COLUMN);
	public static final RegistryObject<BlockItem> CHISELED_BLACK_STONE_BRICKS = registerBlockItem(MSBlocks.CHISELED_BLACK_STONE_BRICKS);
	public static final RegistryObject<BlockItem> CRACKED_BLACK_STONE_BRICKS = registerBlockItem(MSBlocks.CRACKED_BLACK_STONE_BRICKS);
	public static final RegistryObject<BlockItem> BLACK_SAND = registerBlockItem(MSBlocks.BLACK_SAND);
	public static final RegistryObject<BlockItem> DECREPIT_STONE_BRICKS = registerBlockItem(MSBlocks.DECREPIT_STONE_BRICKS);
	public static final RegistryObject<BlockItem> FLOWERY_MOSSY_COBBLESTONE = registerBlockItem(MSBlocks.FLOWERY_MOSSY_COBBLESTONE);
	public static final RegistryObject<BlockItem> MOSSY_DECREPIT_STONE_BRICKS = registerBlockItem(MSBlocks.MOSSY_DECREPIT_STONE_BRICKS);
	public static final RegistryObject<BlockItem> FLOWERY_MOSSY_STONE_BRICKS = registerBlockItem(MSBlocks.FLOWERY_MOSSY_STONE_BRICKS);
	public static final RegistryObject<BlockItem> COARSE_END_STONE = registerBlockItem(MSBlocks.COARSE_END_STONE);
	public static final RegistryObject<BlockItem> END_GRASS = registerBlockItem(MSBlocks.END_GRASS);
	public static final RegistryObject<BlockItem> CHALK = registerBlockItem(MSBlocks.CHALK);
	public static final RegistryObject<BlockItem> POLISHED_CHALK = registerBlockItem(MSBlocks.POLISHED_CHALK);
	public static final RegistryObject<BlockItem> CHALK_BRICKS = registerBlockItem(MSBlocks.CHALK_BRICKS);
	public static final RegistryObject<BlockItem> CHALK_COLUMN = registerBlockItem(MSBlocks.CHALK_COLUMN);
	public static final RegistryObject<BlockItem> CHISELED_CHALK_BRICKS = registerBlockItem(MSBlocks.CHISELED_CHALK_BRICKS);
	public static final RegistryObject<BlockItem> MOSSY_CHALK_BRICKS = registerBlockItem(MSBlocks.MOSSY_CHALK_BRICKS);
	public static final RegistryObject<BlockItem> FLOWERY_CHALK_BRICKS = registerBlockItem(MSBlocks.FLOWERY_CHALK_BRICKS);
	public static final RegistryObject<BlockItem> PINK_STONE = registerBlockItem(MSBlocks.PINK_STONE);
	public static final RegistryObject<BlockItem> PINK_STONE_BRICKS = registerBlockItem(MSBlocks.PINK_STONE_BRICKS);
	public static final RegistryObject<BlockItem> CHISELED_PINK_STONE_BRICKS = registerBlockItem(MSBlocks.CHISELED_PINK_STONE_BRICKS);
	public static final RegistryObject<BlockItem> CRACKED_PINK_STONE_BRICKS = registerBlockItem(MSBlocks.CRACKED_PINK_STONE_BRICKS);
	public static final RegistryObject<BlockItem> MOSSY_PINK_STONE_BRICKS = registerBlockItem(MSBlocks.MOSSY_PINK_STONE_BRICKS);
	public static final RegistryObject<BlockItem> POLISHED_PINK_STONE = registerBlockItem(MSBlocks.POLISHED_PINK_STONE);
	public static final RegistryObject<BlockItem> PINK_STONE_COLUMN = registerBlockItem(MSBlocks.PINK_STONE_COLUMN);
	public static final RegistryObject<BlockItem> BROWN_STONE = registerBlockItem(MSBlocks.BROWN_STONE);
	public static final RegistryObject<BlockItem> BROWN_STONE_BRICKS = registerBlockItem(MSBlocks.BROWN_STONE_BRICKS);
	public static final RegistryObject<BlockItem> BROWN_STONE_COLUMN = registerBlockItem(MSBlocks.BROWN_STONE_COLUMN);
	public static final RegistryObject<BlockItem> CRACKED_BROWN_STONE_BRICKS = registerBlockItem(MSBlocks.CRACKED_BROWN_STONE_BRICKS);
	public static final RegistryObject<BlockItem> POLISHED_BROWN_STONE = registerBlockItem(MSBlocks.POLISHED_BROWN_STONE);
	public static final RegistryObject<BlockItem> GREEN_STONE = registerBlockItem(MSBlocks.GREEN_STONE);
	public static final RegistryObject<BlockItem> GREEN_STONE_BRICKS = registerBlockItem(MSBlocks.GREEN_STONE_BRICKS);
	public static final RegistryObject<BlockItem> GREEN_STONE_COLUMN = registerBlockItem(MSBlocks.GREEN_STONE_COLUMN);
	public static final RegistryObject<BlockItem> POLISHED_GREEN_STONE = registerBlockItem(MSBlocks.POLISHED_GREEN_STONE);
	public static final RegistryObject<BlockItem> CHISELED_GREEN_STONE_BRICKS = registerBlockItem(MSBlocks.CHISELED_GREEN_STONE_BRICKS);
	public static final RegistryObject<BlockItem> HORIZONTAL_GREEN_STONE_BRICKS = registerBlockItem(MSBlocks.HORIZONTAL_GREEN_STONE_BRICKS);
	public static final RegistryObject<BlockItem> VERTICAL_GREEN_STONE_BRICKS = registerBlockItem(MSBlocks.VERTICAL_GREEN_STONE_BRICKS);
	public static final RegistryObject<BlockItem> GREEN_STONE_BRICK_EMBEDDED_LADDER = registerBlockItem(MSBlocks.GREEN_STONE_BRICK_EMBEDDED_LADDER);
	public static final RegistryObject<BlockItem> GREEN_STONE_BRICK_TRIM = registerBlockItem(MSBlocks.GREEN_STONE_BRICK_TRIM);
	public static final RegistryObject<BlockItem> GREEN_STONE_BRICK_FROG = registerBlockItem(MSBlocks.GREEN_STONE_BRICK_FROG);
	public static final RegistryObject<BlockItem> GREEN_STONE_BRICK_IGUANA_LEFT = registerBlockItem(MSBlocks.GREEN_STONE_BRICK_IGUANA_LEFT);
	public static final RegistryObject<BlockItem> GREEN_STONE_BRICK_IGUANA_RIGHT = registerBlockItem(MSBlocks.GREEN_STONE_BRICK_IGUANA_RIGHT);
	public static final RegistryObject<BlockItem> GREEN_STONE_BRICK_LOTUS = registerBlockItem(MSBlocks.GREEN_STONE_BRICK_LOTUS);
	public static final RegistryObject<BlockItem> GREEN_STONE_BRICK_NAK_LEFT = registerBlockItem(MSBlocks.GREEN_STONE_BRICK_NAK_LEFT);
	public static final RegistryObject<BlockItem> GREEN_STONE_BRICK_NAK_RIGHT = registerBlockItem(MSBlocks.GREEN_STONE_BRICK_NAK_RIGHT);
	public static final RegistryObject<BlockItem> GREEN_STONE_BRICK_SALAMANDER_LEFT = registerBlockItem(MSBlocks.GREEN_STONE_BRICK_SALAMANDER_LEFT);
	public static final RegistryObject<BlockItem> GREEN_STONE_BRICK_SALAMANDER_RIGHT = registerBlockItem(MSBlocks.GREEN_STONE_BRICK_SALAMANDER_RIGHT);
	public static final RegistryObject<BlockItem> GREEN_STONE_BRICK_SKAIA = registerBlockItem(MSBlocks.GREEN_STONE_BRICK_SKAIA);
	public static final RegistryObject<BlockItem> GREEN_STONE_BRICK_TURTLE = registerBlockItem(MSBlocks.GREEN_STONE_BRICK_TURTLE);
	public static final RegistryObject<BlockItem> SANDSTONE_COLUMN = registerBlockItem(MSBlocks.SANDSTONE_COLUMN);
	public static final RegistryObject<BlockItem> CHISELED_SANDSTONE_COLUMN = registerBlockItem(MSBlocks.CHISELED_SANDSTONE_COLUMN);
	public static final RegistryObject<BlockItem> RED_SANDSTONE_COLUMN = registerBlockItem(MSBlocks.RED_SANDSTONE_COLUMN);
	public static final RegistryObject<BlockItem> CHISELED_RED_SANDSTONE_COLUMN = registerBlockItem(MSBlocks.CHISELED_RED_SANDSTONE_COLUMN);
	public static final RegistryObject<BlockItem> UNCARVED_WOOD = registerBlockItem(MSBlocks.UNCARVED_WOOD);
	public static final RegistryObject<BlockItem> CHIPBOARD = registerBlockItem(MSBlocks.CHIPBOARD);
	public static final RegistryObject<BlockItem> WOOD_SHAVINGS = registerBlockItem(MSBlocks.WOOD_SHAVINGS);
	public static final RegistryObject<BlockItem> CARVED_HEAVY_PLANKS = registerBlockItem(MSBlocks.CARVED_HEAVY_PLANKS);
	public static final RegistryObject<BlockItem> CARVED_PLANKS = registerBlockItem(MSBlocks.CARVED_PLANKS);
	public static final RegistryObject<BlockItem> POLISHED_UNCARVED_WOOD = registerBlockItem(MSBlocks.POLISHED_UNCARVED_WOOD);
	public static final RegistryObject<BlockItem> CARVED_KNOTTED_WOOD = registerBlockItem(MSBlocks.CARVED_KNOTTED_WOOD);
	public static final RegistryObject<BlockItem> DENSE_CLOUD = registerBlockItem(MSBlocks.DENSE_CLOUD);
	public static final RegistryObject<BlockItem> BRIGHT_DENSE_CLOUD = registerBlockItem(MSBlocks.BRIGHT_DENSE_CLOUD);
	public static final RegistryObject<BlockItem> SUGAR_CUBE = registerBlockItem(MSBlocks.SUGAR_CUBE);
	public static final RegistryObject<BlockItem> NATIVE_SULFUR = registerBlockItem(MSBlocks.NATIVE_SULFUR);
	
	
	
	//Land Tree Blocks
	public static final RegistryObject<BlockItem> GLOWING_LOG = registerBlockItem(MSBlocks.GLOWING_LOG);
	public static final RegistryObject<BlockItem> SHADEWOOD_LOG = registerBlockItem(MSBlocks.SHADEWOOD_LOG);
	public static final RegistryObject<BlockItem> SCARRED_SHADEWOOD_LOG = registerBlockItem(MSBlocks.SCARRED_SHADEWOOD_LOG);
	public static final RegistryObject<BlockItem> ROTTED_SHADEWOOD_LOG = registerBlockItem(MSBlocks.ROTTED_SHADEWOOD_LOG);
	public static final RegistryObject<BlockItem> STRIPPED_SHADEWOOD_LOG = registerBlockItem(MSBlocks.STRIPPED_SHADEWOOD_LOG);
	public static final RegistryObject<BlockItem> STRIPPED_SCARRED_SHADEWOOD_LOG = registerBlockItem(MSBlocks.STRIPPED_SCARRED_SHADEWOOD_LOG);
	public static final RegistryObject<BlockItem> STRIPPED_ROTTED_SHADEWOOD_LOG = registerBlockItem(MSBlocks.STRIPPED_ROTTED_SHADEWOOD_LOG);
	public static final RegistryObject<BlockItem> FROST_LOG = registerBlockItem(MSBlocks.FROST_LOG);
	public static final RegistryObject<BlockItem> RAINBOW_LOG = registerBlockItem(MSBlocks.RAINBOW_LOG);
	public static final RegistryObject<BlockItem> END_LOG = registerBlockItem(MSBlocks.END_LOG);
	public static final RegistryObject<BlockItem> VINE_LOG = registerBlockItem(MSBlocks.VINE_LOG);
	public static final RegistryObject<BlockItem> FLOWERY_VINE_LOG = registerBlockItem(MSBlocks.FLOWERY_VINE_LOG);
	public static final RegistryObject<BlockItem> DEAD_LOG = registerBlockItem(MSBlocks.DEAD_LOG);
	public static final RegistryObject<BlockItem> PETRIFIED_LOG = registerBlockItem(MSBlocks.PETRIFIED_LOG);
	public static final RegistryObject<BlockItem> GLOWING_WOOD = registerBlockItem(MSBlocks.GLOWING_WOOD);
	public static final RegistryObject<BlockItem> SHADEWOOD = registerBlockItem(MSBlocks.SHADEWOOD);
	public static final RegistryObject<BlockItem> SCARRED_SHADEWOOD = registerBlockItem(MSBlocks.SCARRED_SHADEWOOD);
	public static final RegistryObject<BlockItem> ROTTED_SHADEWOOD = registerBlockItem(MSBlocks.ROTTED_SHADEWOOD);
	public static final RegistryObject<BlockItem> STRIPPED_SHADEWOOD = registerBlockItem(MSBlocks.STRIPPED_SHADEWOOD);
	public static final RegistryObject<BlockItem> STRIPPED_SCARRED_SHADEWOOD = registerBlockItem(MSBlocks.STRIPPED_SCARRED_SHADEWOOD);
	public static final RegistryObject<BlockItem> STRIPPED_ROTTED_SHADEWOOD = registerBlockItem(MSBlocks.STRIPPED_ROTTED_SHADEWOOD);
	public static final RegistryObject<BlockItem> FROST_WOOD = registerBlockItem(MSBlocks.FROST_WOOD);
	public static final RegistryObject<BlockItem> RAINBOW_WOOD = registerBlockItem(MSBlocks.RAINBOW_WOOD);
	public static final RegistryObject<BlockItem> END_WOOD = registerBlockItem(MSBlocks.END_WOOD);
	public static final RegistryObject<BlockItem> VINE_WOOD = registerBlockItem(MSBlocks.VINE_WOOD);
	public static final RegistryObject<BlockItem> FLOWERY_VINE_WOOD = registerBlockItem(MSBlocks.FLOWERY_VINE_WOOD);
	public static final RegistryObject<BlockItem> DEAD_WOOD = registerBlockItem(MSBlocks.DEAD_WOOD);
	public static final RegistryObject<BlockItem> PETRIFIED_WOOD = registerBlockItem(MSBlocks.PETRIFIED_WOOD);
	public static final RegistryObject<BlockItem> GLOWING_PLANKS = registerBlockItem(MSBlocks.GLOWING_PLANKS);
	public static final RegistryObject<BlockItem> FROST_PLANKS = registerBlockItem(MSBlocks.FROST_PLANKS);
	public static final RegistryObject<BlockItem> RAINBOW_PLANKS = registerBlockItem(MSBlocks.RAINBOW_PLANKS);
	public static final RegistryObject<BlockItem> END_PLANKS = registerBlockItem(MSBlocks.END_PLANKS);
	public static final RegistryObject<BlockItem> DEAD_PLANKS = registerBlockItem(MSBlocks.DEAD_PLANKS);
	public static final RegistryObject<BlockItem> TREATED_PLANKS = registerBlockItem(MSBlocks.TREATED_PLANKS);
	public static final RegistryObject<BlockItem> SHADEWOOD_PLANKS = registerBlockItem(MSBlocks.SHADEWOOD_PLANKS);
	public static final RegistryObject<BlockItem> FROST_LEAVES = registerBlockItem(MSBlocks.FROST_LEAVES);
	public static final RegistryObject<BlockItem> RAINBOW_LEAVES = registerBlockItem(MSBlocks.RAINBOW_LEAVES);
	public static final RegistryObject<BlockItem> END_LEAVES = registerBlockItem(MSBlocks.END_LEAVES);
	public static final RegistryObject<BlockItem> SHADEWOOD_LEAVES = registerBlockItem(MSBlocks.SHADEWOOD_LEAVES);
	public static final RegistryObject<BlockItem> SHROOMY_SHADEWOOD_LEAVES = registerBlockItem(MSBlocks.SHROOMY_SHADEWOOD_LEAVES);
	public static final RegistryObject<BlockItem> RAINBOW_SAPLING = registerBlockItem(MSBlocks.RAINBOW_SAPLING);
	public static final RegistryObject<BlockItem> END_SAPLING = registerBlockItem(MSBlocks.END_SAPLING);
	public static final RegistryObject<BlockItem> SHADEWOOD_SAPLING = registerBlockItem(MSBlocks.SHADEWOOD_SAPLING);
	public static final RegistryObject<BlockItem> GLOWING_BOOKSHELF = registerBlockItem(MSBlocks.GLOWING_BOOKSHELF);
	public static final RegistryObject<BlockItem> FROST_BOOKSHELF = registerBlockItem(MSBlocks.FROST_BOOKSHELF);
	public static final RegistryObject<BlockItem> RAINBOW_BOOKSHELF = registerBlockItem(MSBlocks.RAINBOW_BOOKSHELF);
	public static final RegistryObject<BlockItem> END_BOOKSHELF = registerBlockItem(MSBlocks.END_BOOKSHELF);
	public static final RegistryObject<BlockItem> DEAD_BOOKSHELF = registerBlockItem(MSBlocks.DEAD_BOOKSHELF);
	public static final RegistryObject<BlockItem> TREATED_BOOKSHELF = registerBlockItem(MSBlocks.TREATED_BOOKSHELF);
	public static final RegistryObject<BlockItem> GLOWING_LADDER = registerBlockItem(MSBlocks.GLOWING_LADDER);
	public static final RegistryObject<BlockItem> FROST_LADDER = registerBlockItem(MSBlocks.FROST_LADDER);
	public static final RegistryObject<BlockItem> RAINBOW_LADDER = registerBlockItem(MSBlocks.RAINBOW_LADDER);
	public static final RegistryObject<BlockItem> END_LADDER = registerBlockItem(MSBlocks.END_LADDER);
	public static final RegistryObject<BlockItem> DEAD_LADDER = registerBlockItem(MSBlocks.DEAD_LADDER);
	public static final RegistryObject<BlockItem> TREATED_LADDER = registerBlockItem(MSBlocks.TREATED_LADDER);
	
	
	//Aspect Tree Blocks
	public static final RegistryObject<BlockItem> BLOOD_ASPECT_LOG = registerBlockItem(MSBlocks.BLOOD_ASPECT_LOG);
	public static final RegistryObject<BlockItem> BREATH_ASPECT_LOG = registerBlockItem(MSBlocks.BREATH_ASPECT_LOG);
	public static final RegistryObject<BlockItem> DOOM_ASPECT_LOG = registerBlockItem(MSBlocks.DOOM_ASPECT_LOG);
	public static final RegistryObject<BlockItem> HEART_ASPECT_LOG = registerBlockItem(MSBlocks.HEART_ASPECT_LOG);
	public static final RegistryObject<BlockItem> HOPE_ASPECT_LOG = registerBlockItem(MSBlocks.HOPE_ASPECT_LOG);
	public static final RegistryObject<BlockItem> LIFE_ASPECT_LOG = registerBlockItem(MSBlocks.LIFE_ASPECT_LOG);
	public static final RegistryObject<BlockItem> LIGHT_ASPECT_LOG = registerBlockItem(MSBlocks.LIGHT_ASPECT_LOG);
	public static final RegistryObject<BlockItem> MIND_ASPECT_LOG = registerBlockItem(MSBlocks.MIND_ASPECT_LOG);
	public static final RegistryObject<BlockItem> RAGE_ASPECT_LOG = registerBlockItem(MSBlocks.RAGE_ASPECT_LOG);
	public static final RegistryObject<BlockItem> SPACE_ASPECT_LOG = registerBlockItem(MSBlocks.SPACE_ASPECT_LOG);
	public static final RegistryObject<BlockItem> TIME_ASPECT_LOG = registerBlockItem(MSBlocks.TIME_ASPECT_LOG);
	public static final RegistryObject<BlockItem> VOID_ASPECT_LOG = registerBlockItem(MSBlocks.VOID_ASPECT_LOG);
	public static final RegistryObject<BlockItem> BLOOD_ASPECT_PLANKS = registerBlockItem(MSBlocks.BLOOD_ASPECT_PLANKS);
	public static final RegistryObject<BlockItem> BREATH_ASPECT_PLANKS = registerBlockItem(MSBlocks.BREATH_ASPECT_PLANKS);
	public static final RegistryObject<BlockItem> DOOM_ASPECT_PLANKS = registerBlockItem(MSBlocks.DOOM_ASPECT_PLANKS);
	public static final RegistryObject<BlockItem> HEART_ASPECT_PLANKS = registerBlockItem(MSBlocks.HEART_ASPECT_PLANKS);
	public static final RegistryObject<BlockItem> HOPE_ASPECT_PLANKS = registerBlockItem(MSBlocks.HOPE_ASPECT_PLANKS);
	public static final RegistryObject<BlockItem> LIFE_ASPECT_PLANKS = registerBlockItem(MSBlocks.LIFE_ASPECT_PLANKS);
	public static final RegistryObject<BlockItem> LIGHT_ASPECT_PLANKS = registerBlockItem(MSBlocks.LIGHT_ASPECT_PLANKS);
	public static final RegistryObject<BlockItem> MIND_ASPECT_PLANKS = registerBlockItem(MSBlocks.MIND_ASPECT_PLANKS);
	public static final RegistryObject<BlockItem> RAGE_ASPECT_PLANKS = registerBlockItem(MSBlocks.RAGE_ASPECT_PLANKS);
	public static final RegistryObject<BlockItem> SPACE_ASPECT_PLANKS = registerBlockItem(MSBlocks.SPACE_ASPECT_PLANKS);
	public static final RegistryObject<BlockItem> TIME_ASPECT_PLANKS = registerBlockItem(MSBlocks.TIME_ASPECT_PLANKS);
	public static final RegistryObject<BlockItem> VOID_ASPECT_PLANKS = registerBlockItem(MSBlocks.VOID_ASPECT_PLANKS);
	public static final RegistryObject<BlockItem> BLOOD_ASPECT_LEAVES = registerBlockItem(MSBlocks.BLOOD_ASPECT_LEAVES);
	public static final RegistryObject<BlockItem> BREATH_ASPECT_LEAVES = registerBlockItem(MSBlocks.BREATH_ASPECT_LEAVES);
	public static final RegistryObject<BlockItem> DOOM_ASPECT_LEAVES = registerBlockItem(MSBlocks.DOOM_ASPECT_LEAVES);
	public static final RegistryObject<BlockItem> HEART_ASPECT_LEAVES = registerBlockItem(MSBlocks.HEART_ASPECT_LEAVES);
	public static final RegistryObject<BlockItem> HOPE_ASPECT_LEAVES = registerBlockItem(MSBlocks.HOPE_ASPECT_LEAVES);
	public static final RegistryObject<BlockItem> LIFE_ASPECT_LEAVES = registerBlockItem(MSBlocks.LIFE_ASPECT_LEAVES);
	public static final RegistryObject<BlockItem> LIGHT_ASPECT_LEAVES = registerBlockItem(MSBlocks.LIGHT_ASPECT_LEAVES);
	public static final RegistryObject<BlockItem> MIND_ASPECT_LEAVES = registerBlockItem(MSBlocks.MIND_ASPECT_LEAVES);
	public static final RegistryObject<BlockItem> RAGE_ASPECT_LEAVES = registerBlockItem(MSBlocks.RAGE_ASPECT_LEAVES);
	public static final RegistryObject<BlockItem> SPACE_ASPECT_LEAVES = registerBlockItem(MSBlocks.SPACE_ASPECT_LEAVES);
	public static final RegistryObject<BlockItem> TIME_ASPECT_LEAVES = registerBlockItem(MSBlocks.TIME_ASPECT_LEAVES);
	public static final RegistryObject<BlockItem> VOID_ASPECT_LEAVES = registerBlockItem(MSBlocks.VOID_ASPECT_LEAVES);
	public static final RegistryObject<BlockItem> BLOOD_ASPECT_SAPLING = registerBlockItem(MSBlocks.BLOOD_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final RegistryObject<BlockItem> BREATH_ASPECT_SAPLING = registerBlockItem(MSBlocks.BREATH_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final RegistryObject<BlockItem> DOOM_ASPECT_SAPLING = registerBlockItem(MSBlocks.DOOM_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final RegistryObject<BlockItem> HEART_ASPECT_SAPLING = registerBlockItem(MSBlocks.HEART_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final RegistryObject<BlockItem> HOPE_ASPECT_SAPLING = registerBlockItem(MSBlocks.HOPE_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final RegistryObject<BlockItem> LIFE_ASPECT_SAPLING = registerBlockItem(MSBlocks.LIFE_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final RegistryObject<BlockItem> LIGHT_ASPECT_SAPLING = registerBlockItem(MSBlocks.LIGHT_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final RegistryObject<BlockItem> MIND_ASPECT_SAPLING = registerBlockItem(MSBlocks.MIND_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final RegistryObject<BlockItem> RAGE_ASPECT_SAPLING = registerBlockItem(MSBlocks.RAGE_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final RegistryObject<BlockItem> SPACE_ASPECT_SAPLING = registerBlockItem(MSBlocks.SPACE_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final RegistryObject<BlockItem> TIME_ASPECT_SAPLING = registerBlockItem(MSBlocks.TIME_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final RegistryObject<BlockItem> VOID_ASPECT_SAPLING = registerBlockItem(MSBlocks.VOID_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final RegistryObject<BlockItem> BLOOD_ASPECT_BOOKSHELF = registerBlockItem(MSBlocks.BLOOD_ASPECT_BOOKSHELF);
	public static final RegistryObject<BlockItem> BREATH_ASPECT_BOOKSHELF = registerBlockItem(MSBlocks.BREATH_ASPECT_BOOKSHELF);
	public static final RegistryObject<BlockItem> DOOM_ASPECT_BOOKSHELF = registerBlockItem(MSBlocks.DOOM_ASPECT_BOOKSHELF);
	public static final RegistryObject<BlockItem> HEART_ASPECT_BOOKSHELF = registerBlockItem(MSBlocks.HEART_ASPECT_BOOKSHELF);
	public static final RegistryObject<BlockItem> HOPE_ASPECT_BOOKSHELF = registerBlockItem(MSBlocks.HOPE_ASPECT_BOOKSHELF);
	public static final RegistryObject<BlockItem> LIFE_ASPECT_BOOKSHELF = registerBlockItem(MSBlocks.LIFE_ASPECT_BOOKSHELF);
	public static final RegistryObject<BlockItem> LIGHT_ASPECT_BOOKSHELF = registerBlockItem(MSBlocks.LIGHT_ASPECT_BOOKSHELF);
	public static final RegistryObject<BlockItem> MIND_ASPECT_BOOKSHELF = registerBlockItem(MSBlocks.MIND_ASPECT_BOOKSHELF);
	public static final RegistryObject<BlockItem> RAGE_ASPECT_BOOKSHELF = registerBlockItem(MSBlocks.RAGE_ASPECT_BOOKSHELF);
	public static final RegistryObject<BlockItem> SPACE_ASPECT_BOOKSHELF = registerBlockItem(MSBlocks.SPACE_ASPECT_BOOKSHELF);
	public static final RegistryObject<BlockItem> TIME_ASPECT_BOOKSHELF = registerBlockItem(MSBlocks.TIME_ASPECT_BOOKSHELF);
	public static final RegistryObject<BlockItem> VOID_ASPECT_BOOKSHELF = registerBlockItem(MSBlocks.VOID_ASPECT_BOOKSHELF);
	public static final RegistryObject<BlockItem> BLOOD_ASPECT_LADDER = registerBlockItem(MSBlocks.BLOOD_ASPECT_LADDER);
	public static final RegistryObject<BlockItem> BREATH_ASPECT_LADDER = registerBlockItem(MSBlocks.BREATH_ASPECT_LADDER);
	public static final RegistryObject<BlockItem> DOOM_ASPECT_LADDER = registerBlockItem(MSBlocks.DOOM_ASPECT_LADDER);
	public static final RegistryObject<BlockItem> HEART_ASPECT_LADDER = registerBlockItem(MSBlocks.HEART_ASPECT_LADDER);
	public static final RegistryObject<BlockItem> HOPE_ASPECT_LADDER = registerBlockItem(MSBlocks.HOPE_ASPECT_LADDER);
	public static final RegistryObject<BlockItem> LIFE_ASPECT_LADDER = registerBlockItem(MSBlocks.LIFE_ASPECT_LADDER);
	public static final RegistryObject<BlockItem> LIGHT_ASPECT_LADDER = registerBlockItem(MSBlocks.LIGHT_ASPECT_LADDER);
	public static final RegistryObject<BlockItem> MIND_ASPECT_LADDER = registerBlockItem(MSBlocks.MIND_ASPECT_LADDER);
	public static final RegistryObject<BlockItem> RAGE_ASPECT_LADDER = registerBlockItem(MSBlocks.RAGE_ASPECT_LADDER);
	public static final RegistryObject<BlockItem> SPACE_ASPECT_LADDER = registerBlockItem(MSBlocks.SPACE_ASPECT_LADDER);
	public static final RegistryObject<BlockItem> TIME_ASPECT_LADDER = registerBlockItem(MSBlocks.TIME_ASPECT_LADDER);
	public static final RegistryObject<BlockItem> VOID_ASPECT_LADDER = registerBlockItem(MSBlocks.VOID_ASPECT_LADDER);
	
	//Land Plant Blocks
	public static final RegistryObject<BlockItem> GLOWING_MUSHROOM = registerBlockItem(MSBlocks.GLOWING_MUSHROOM);
	public static final RegistryObject<BlockItem> GLOWING_MUSHROOM_VINES = registerBlockItem(MSBlocks.GLOWING_MUSHROOM_VINES);
	public static final RegistryObject<BlockItem> DESERT_BUSH = registerBlockItem(MSBlocks.DESERT_BUSH);
	public static final RegistryObject<BlockItem> BLOOMING_CACTUS = registerBlockItem(MSBlocks.BLOOMING_CACTUS);
	public static final RegistryObject<BlockItem> PETRIFIED_GRASS = registerBlockItem(MSBlocks.PETRIFIED_GRASS);
	public static final RegistryObject<BlockItem> PETRIFIED_POPPY = registerBlockItem(MSBlocks.PETRIFIED_POPPY);
	public static final RegistryObject<BlockItem> STRAWBERRY = registerBlockItem(MSBlocks.STRAWBERRY, new Item.Properties());
	public static final RegistryObject<BlockItem> TALL_END_GRASS = registerBlockItem(MSBlocks.TALL_END_GRASS);
	public static final RegistryObject<BlockItem> GLOWFLOWER = registerBlockItem(MSBlocks.GLOWFLOWER);
	
	
	
	//Special Land Blocks
	public static final RegistryObject<BlockItem> GLOWY_GOOP = registerBlockItem(MSBlocks.GLOWY_GOOP);
	public static final RegistryObject<BlockItem> COAGULATED_BLOOD = registerBlockItem(MSBlocks.COAGULATED_BLOOD);
	public static final RegistryObject<BlockItem> PIPE = registerBlockItem(MSBlocks.PIPE);
	public static final RegistryObject<BlockItem> PIPE_INTERSECTION = registerBlockItem(MSBlocks.PIPE_INTERSECTION);
	public static final RegistryObject<BlockItem> PARCEL_PYXIS = registerBlockItem(MSBlocks.PARCEL_PYXIS);
	public static final RegistryObject<BlockItem> PYXIS_LID = registerBlockItem(MSBlocks.PYXIS_LID);
	public static final RegistryObject<BlockItem> NAKAGATOR_STATUE = registerBlockItem(MSBlocks.NAKAGATOR_STATUE);
	
	
	
	//Structure Land Blocks
	public static final RegistryObject<BlockItem> BLACK_CHESS_BRICK_STAIRS = registerBlockItem(MSBlocks.BLACK_CHESS_BRICK_STAIRS);
	public static final RegistryObject<BlockItem> DARK_GRAY_CHESS_BRICK_STAIRS = registerBlockItem(MSBlocks.DARK_GRAY_CHESS_BRICK_STAIRS);
	public static final RegistryObject<BlockItem> LIGHT_GRAY_CHESS_BRICK_STAIRS = registerBlockItem(MSBlocks.LIGHT_GRAY_CHESS_BRICK_STAIRS);
	public static final RegistryObject<BlockItem> WHITE_CHESS_BRICK_STAIRS = registerBlockItem(MSBlocks.WHITE_CHESS_BRICK_STAIRS);
	public static final RegistryObject<BlockItem> COARSE_STONE_STAIRS = registerBlockItem(MSBlocks.COARSE_STONE_STAIRS);
	public static final RegistryObject<BlockItem> COARSE_STONE_BRICK_STAIRS = registerBlockItem(MSBlocks.COARSE_STONE_BRICK_STAIRS);
	public static final RegistryObject<BlockItem> SHADE_STAIRS = registerBlockItem(MSBlocks.SHADE_STAIRS);
	public static final RegistryObject<BlockItem> SHADE_BRICK_STAIRS = registerBlockItem(MSBlocks.SHADE_BRICK_STAIRS);
	public static final RegistryObject<BlockItem> FROST_TILE_STAIRS = registerBlockItem(MSBlocks.FROST_TILE_STAIRS);
	public static final RegistryObject<BlockItem> FROST_BRICK_STAIRS = registerBlockItem(MSBlocks.FROST_BRICK_STAIRS);
	public static final RegistryObject<BlockItem> CAST_IRON_STAIRS = registerBlockItem(MSBlocks.CAST_IRON_STAIRS);
	public static final RegistryObject<BlockItem> BLACK_STONE_STAIRS = registerBlockItem(MSBlocks.BLACK_STONE_STAIRS);
	public static final RegistryObject<BlockItem> BLACK_STONE_BRICK_STAIRS = registerBlockItem(MSBlocks.BLACK_STONE_BRICK_STAIRS);
	public static final RegistryObject<BlockItem> MYCELIUM_STAIRS = registerBlockItem(MSBlocks.MYCELIUM_STAIRS);
	public static final RegistryObject<BlockItem> MYCELIUM_BRICK_STAIRS = registerBlockItem(MSBlocks.MYCELIUM_BRICK_STAIRS);
	public static final RegistryObject<BlockItem> CHALK_STAIRS = registerBlockItem(MSBlocks.CHALK_STAIRS);
	public static final RegistryObject<BlockItem> CHALK_BRICK_STAIRS = registerBlockItem(MSBlocks.CHALK_BRICK_STAIRS);
	public static final RegistryObject<BlockItem> FLOWERY_MOSSY_STONE_BRICK_STAIRS = registerBlockItem(MSBlocks.FLOWERY_MOSSY_STONE_BRICK_STAIRS);
	public static final RegistryObject<BlockItem> PINK_STONE_STAIRS = registerBlockItem(MSBlocks.PINK_STONE_STAIRS);
	public static final RegistryObject<BlockItem> PINK_STONE_BRICK_STAIRS = registerBlockItem(MSBlocks.PINK_STONE_BRICK_STAIRS);
	public static final RegistryObject<BlockItem> BROWN_STONE_STAIRS = registerBlockItem(MSBlocks.BROWN_STONE_STAIRS);
	public static final RegistryObject<BlockItem> BROWN_STONE_BRICK_STAIRS = registerBlockItem(MSBlocks.BROWN_STONE_BRICK_STAIRS);
	public static final RegistryObject<BlockItem> GREEN_STONE_STAIRS = registerBlockItem(MSBlocks.GREEN_STONE_STAIRS);
	public static final RegistryObject<BlockItem> GREEN_STONE_BRICK_STAIRS = registerBlockItem(MSBlocks.GREEN_STONE_BRICK_STAIRS);
	public static final RegistryObject<BlockItem> RAINBOW_PLANKS_STAIRS = registerBlockItem(MSBlocks.RAINBOW_PLANKS_STAIRS);
	public static final RegistryObject<BlockItem> END_PLANKS_STAIRS = registerBlockItem(MSBlocks.END_PLANKS_STAIRS);
	public static final RegistryObject<BlockItem> DEAD_PLANKS_STAIRS = registerBlockItem(MSBlocks.DEAD_PLANKS_STAIRS);
	public static final RegistryObject<BlockItem> TREATED_PLANKS_STAIRS = registerBlockItem(MSBlocks.TREATED_PLANKS_STAIRS);
	
	public static final RegistryObject<BlockItem> STEEP_GREEN_STONE_BRICK_STAIRS_BASE = registerBlockItem(MSBlocks.STEEP_GREEN_STONE_BRICK_STAIRS_BASE);
	public static final RegistryObject<BlockItem> STEEP_GREEN_STONE_BRICK_STAIRS_TOP = registerBlockItem(MSBlocks.STEEP_GREEN_STONE_BRICK_STAIRS_TOP);
	
	public static final RegistryObject<BlockItem> BLACK_CHESS_BRICK_SLAB = registerBlockItem(MSBlocks.BLACK_CHESS_BRICK_SLAB);
	public static final RegistryObject<BlockItem> DARK_GRAY_CHESS_BRICK_SLAB = registerBlockItem(MSBlocks.DARK_GRAY_CHESS_BRICK_SLAB);
	public static final RegistryObject<BlockItem> LIGHT_GRAY_CHESS_BRICK_SLAB = registerBlockItem(MSBlocks.LIGHT_GRAY_CHESS_BRICK_SLAB);
	public static final RegistryObject<BlockItem> WHITE_CHESS_BRICK_SLAB = registerBlockItem(MSBlocks.WHITE_CHESS_BRICK_SLAB);
	public static final RegistryObject<BlockItem> COARSE_STONE_SLAB = registerBlockItem(MSBlocks.COARSE_STONE_SLAB);
	public static final RegistryObject<BlockItem> COARSE_STONE_BRICK_SLAB = registerBlockItem(MSBlocks.COARSE_STONE_BRICK_SLAB);
	public static final RegistryObject<BlockItem> CHALK_SLAB = registerBlockItem(MSBlocks.CHALK_SLAB);
	public static final RegistryObject<BlockItem> CHALK_BRICK_SLAB = registerBlockItem(MSBlocks.CHALK_BRICK_SLAB);
	public static final RegistryObject<BlockItem> PINK_STONE_SLAB = registerBlockItem(MSBlocks.PINK_STONE_SLAB);
	public static final RegistryObject<BlockItem> PINK_STONE_BRICK_SLAB = registerBlockItem(MSBlocks.PINK_STONE_BRICK_SLAB);
	public static final RegistryObject<BlockItem> BROWN_STONE_SLAB = registerBlockItem(MSBlocks.BROWN_STONE_SLAB);
	public static final RegistryObject<BlockItem> BROWN_STONE_BRICK_SLAB = registerBlockItem(MSBlocks.BROWN_STONE_BRICK_SLAB);
	public static final RegistryObject<BlockItem> GREEN_STONE_SLAB = registerBlockItem(MSBlocks.GREEN_STONE_SLAB);
	public static final RegistryObject<BlockItem> GREEN_STONE_BRICK_SLAB = registerBlockItem(MSBlocks.GREEN_STONE_BRICK_SLAB);
	public static final RegistryObject<BlockItem> RAINBOW_PLANKS_SLAB = registerBlockItem(MSBlocks.RAINBOW_PLANKS_SLAB);
	public static final RegistryObject<BlockItem> END_PLANKS_SLAB = registerBlockItem(MSBlocks.END_PLANKS_SLAB);
	public static final RegistryObject<BlockItem> DEAD_PLANKS_SLAB = registerBlockItem(MSBlocks.DEAD_PLANKS_SLAB);
	public static final RegistryObject<BlockItem> TREATED_PLANKS_SLAB = registerBlockItem(MSBlocks.TREATED_PLANKS_SLAB);
	public static final RegistryObject<BlockItem> BLACK_STONE_SLAB = registerBlockItem(MSBlocks.BLACK_STONE_SLAB);
	public static final RegistryObject<BlockItem> BLACK_STONE_BRICK_SLAB = registerBlockItem(MSBlocks.BLACK_STONE_BRICK_SLAB);
	public static final RegistryObject<BlockItem> MYCELIUM_SLAB = registerBlockItem(MSBlocks.MYCELIUM_SLAB);
	public static final RegistryObject<BlockItem> MYCELIUM_BRICK_SLAB = registerBlockItem(MSBlocks.MYCELIUM_BRICK_SLAB);
	public static final RegistryObject<BlockItem> FLOWERY_MOSSY_STONE_BRICK_SLAB = registerBlockItem(MSBlocks.FLOWERY_MOSSY_STONE_BRICK_SLAB);
	public static final RegistryObject<BlockItem> FROST_TILE_SLAB = registerBlockItem(MSBlocks.FROST_TILE_SLAB);
	public static final RegistryObject<BlockItem> FROST_BRICK_SLAB = registerBlockItem(MSBlocks.FROST_BRICK_SLAB);
	public static final RegistryObject<BlockItem> SHADE_SLAB = registerBlockItem(MSBlocks.SHADE_SLAB);
	public static final RegistryObject<BlockItem> SHADE_BRICK_SLAB = registerBlockItem(MSBlocks.SHADE_BRICK_SLAB);
	
	
	
	//Dungeon Functional Blocks
	public static final RegistryObject<BlockItem> TRAJECTORY_BLOCK = registerBlockItem(MSBlocks.TRAJECTORY_BLOCK);
	public static final RegistryObject<BlockItem> STAT_STORER = registerBlockItem(MSBlocks.STAT_STORER);
	public static final RegistryObject<BlockItem> REMOTE_OBSERVER = registerBlockItem(MSBlocks.REMOTE_OBSERVER);
	public static final RegistryObject<BlockItem> WIRELESS_REDSTONE_TRANSMITTER = registerBlockItem(MSBlocks.WIRELESS_REDSTONE_TRANSMITTER);
	public static final RegistryObject<BlockItem> WIRELESS_REDSTONE_RECEIVER = registerBlockItem(MSBlocks.WIRELESS_REDSTONE_RECEIVER);
	public static final RegistryObject<BlockItem> SOLID_SWITCH = registerBlockItem(MSBlocks.SOLID_SWITCH);
	public static final RegistryObject<BlockItem> VARIABLE_SOLID_SWITCH = registerBlockItem(MSBlocks.VARIABLE_SOLID_SWITCH);
	public static final RegistryObject<BlockItem> ONE_SECOND_INTERVAL_TIMED_SOLID_SWITCH = registerBlockItem(MSBlocks.ONE_SECOND_INTERVAL_TIMED_SOLID_SWITCH);
	public static final RegistryObject<BlockItem> TWO_SECOND_INTERVAL_TIMED_SOLID_SWITCH = registerBlockItem(MSBlocks.TWO_SECOND_INTERVAL_TIMED_SOLID_SWITCH);
	public static final RegistryObject<BlockItem> SUMMONER = registerBlockItem(MSBlocks.SUMMONER, block -> new ExtraInfoBlockItem(block, new Item.Properties()));
	public static final RegistryObject<BlockItem> AREA_EFFECT_BLOCK = registerBlockItem(MSBlocks.AREA_EFFECT_BLOCK, block -> new ExtraInfoBlockItem(block, new Item.Properties()));
	public static final RegistryObject<BlockItem> PLATFORM_GENERATOR = registerBlockItem(MSBlocks.PLATFORM_GENERATOR, block -> new ExtraInfoBlockItem(block, new Item.Properties()));
	public static final RegistryObject<BlockItem> PLATFORM_RECEPTACLE = registerBlockItem(MSBlocks.PLATFORM_RECEPTACLE);
	public static final RegistryObject<BlockItem> ITEM_MAGNET = registerBlockItem(MSBlocks.ITEM_MAGNET, block -> new ExtraInfoBlockItem(block, new Item.Properties()));
	public static final RegistryObject<BlockItem> REDSTONE_CLOCK = registerBlockItem(MSBlocks.REDSTONE_CLOCK);
	public static final RegistryObject<BlockItem> ROTATOR = registerBlockItem(MSBlocks.ROTATOR);
	public static final RegistryObject<BlockItem> TOGGLER = registerBlockItem(MSBlocks.TOGGLER, block -> new ExtraInfoBlockItem(block, new Item.Properties()));
	public static final RegistryObject<BlockItem> REMOTE_COMPARATOR = registerBlockItem(MSBlocks.REMOTE_COMPARATOR, block -> new ExtraInfoBlockItem(block, new Item.Properties()));
	public static final RegistryObject<BlockItem> STRUCTURE_CORE = registerBlockItem(MSBlocks.STRUCTURE_CORE);
	public static final RegistryObject<BlockItem> FALL_PAD = registerBlockItem(MSBlocks.FALL_PAD);
	public static final RegistryObject<BlockItem> FRAGILE_STONE = registerBlockItem(MSBlocks.FRAGILE_STONE);
	public static final RegistryObject<BlockItem> SPIKES = registerBlockItem(MSBlocks.SPIKES);
	public static final RegistryObject<BlockItem> RETRACTABLE_SPIKES = registerBlockItem(MSBlocks.RETRACTABLE_SPIKES, block -> new ExtraInfoBlockItem(block, new Item.Properties()));
	public static final RegistryObject<BlockItem> BLOCK_PRESSURE_PLATE = registerBlockItem(MSBlocks.BLOCK_PRESSURE_PLATE);
	public static final RegistryObject<BlockItem> PUSHABLE_BLOCK = registerBlockItem(MSBlocks.PUSHABLE_BLOCK);
	public static final RegistryObject<BlockItem> AND_GATE_BLOCK = registerBlockItem(MSBlocks.AND_GATE_BLOCK);
	public static final RegistryObject<BlockItem> OR_GATE_BLOCK = registerBlockItem(MSBlocks.OR_GATE_BLOCK);
	public static final RegistryObject<BlockItem> XOR_GATE_BLOCK = registerBlockItem(MSBlocks.XOR_GATE_BLOCK);
	public static final RegistryObject<BlockItem> NAND_GATE_BLOCK = registerBlockItem(MSBlocks.NAND_GATE_BLOCK);
	public static final RegistryObject<BlockItem> NOR_GATE_BLOCK = registerBlockItem(MSBlocks.NOR_GATE_BLOCK);
	public static final RegistryObject<BlockItem> XNOR_GATE_BLOCK = registerBlockItem(MSBlocks.XNOR_GATE_BLOCK);
	
	
	
	//Misc Machines
	public static final RegistryObject<BlockItem> COMPUTER = registerBlockItem(MSBlocks.COMPUTER);
	public static final RegistryObject<BlockItem> LAPTOP = registerBlockItem(MSBlocks.LAPTOP);
	public static final RegistryObject<BlockItem> CROCKERTOP = registerBlockItem(MSBlocks.CROCKERTOP);
	public static final RegistryObject<BlockItem> HUBTOP = registerBlockItem(MSBlocks.HUBTOP);
	public static final RegistryObject<BlockItem> LUNCHTOP = registerBlockItem(MSBlocks.LUNCHTOP);
	public static final RegistryObject<BlockItem> OLD_COMPUTER = registerBlockItem(MSBlocks.OLD_COMPUTER);
	public static final RegistryObject<BlockItem> TRANSPORTALIZER = registerBlockItem(MSBlocks.TRANSPORTALIZER, block -> new TransportalizerItem(block, new Item.Properties()));
	public static final RegistryObject<BlockItem> TRANS_PORTALIZER = registerBlockItem(MSBlocks.TRANS_PORTALIZER, block -> new TransportalizerItem(block, new Item.Properties()));
	public static final RegistryObject<BlockItem> SENDIFICATOR = registerBlockItem(MSBlocks.SENDIFICATOR, block -> new SendificatorBlockItem(block, new Item.Properties().stacksTo(1)));
	public static final RegistryObject<BlockItem> GRIST_WIDGET = registerBlockItem(MSBlocks.GRIST_WIDGET, new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final RegistryObject<BlockItem> URANIUM_COOKER = registerBlockItem(MSBlocks.URANIUM_COOKER);
	public static final RegistryObject<BlockItem> GRIST_COLLECTOR = registerBlockItem(MSBlocks.GRIST_COLLECTOR);
	public static final RegistryObject<BlockItem> ANTHVIL = registerBlockItem(MSBlocks.ANTHVIL);
	public static final RegistryObject<BlockItem> SKAIANET_DENIER = registerBlockItem(MSBlocks.SKAIANET_DENIER);
	public static final RegistryObject<BlockItem> POWER_HUB = registerBlockItem(MSBlocks.POWER_HUB);
	
	
	
	//Misc Alchemy Semi-Plants
	public static final RegistryObject<BlockItem> GOLD_SEEDS = registerBlockItem(MSBlocks.GOLD_SEEDS);
	public static final RegistryObject<BlockItem> WOODEN_CACTUS = registerBlockItem(MSBlocks.WOODEN_CACTUS);
	
	
	
	//Cakes
	public static final RegistryObject<BlockItem> APPLE_CAKE = registerBlockItem(MSBlocks.APPLE_CAKE, new Item.Properties().stacksTo(1));
	public static final RegistryObject<BlockItem> BLUE_CAKE = registerBlockItem(MSBlocks.BLUE_CAKE, new Item.Properties().stacksTo(1));
	public static final RegistryObject<BlockItem> COLD_CAKE = registerBlockItem(MSBlocks.COLD_CAKE, new Item.Properties().stacksTo(1));
	public static final RegistryObject<BlockItem> RED_CAKE = registerBlockItem(MSBlocks.RED_CAKE, new Item.Properties().stacksTo(1));
	public static final RegistryObject<BlockItem> HOT_CAKE = registerBlockItem(MSBlocks.HOT_CAKE, new Item.Properties().stacksTo(1));
	public static final RegistryObject<BlockItem> REVERSE_CAKE = registerBlockItem(MSBlocks.REVERSE_CAKE, new Item.Properties().stacksTo(1));
	public static final RegistryObject<BlockItem> FUCHSIA_CAKE = registerBlockItem(MSBlocks.FUCHSIA_CAKE, new Item.Properties().stacksTo(1));
	public static final RegistryObject<BlockItem> NEGATIVE_CAKE = registerBlockItem(MSBlocks.NEGATIVE_CAKE, new Item.Properties().stacksTo(1));
	public static final RegistryObject<BlockItem> CARROT_CAKE = registerBlockItem(MSBlocks.CARROT_CAKE, new Item.Properties().stacksTo(1));
	public static final RegistryObject<BlockItem> LARGE_CAKE = registerBlockItem(MSBlocks.LARGE_CAKE);
	public static final RegistryObject<BlockItem> PINK_FROSTED_TOP_LARGE_CAKE = registerBlockItem(MSBlocks.PINK_FROSTED_TOP_LARGE_CAKE);
	
	
	
	//Explosives
	public static final RegistryObject<BlockItem> PRIMED_TNT = registerBlockItem(MSBlocks.PRIMED_TNT);
	public static final RegistryObject<BlockItem> UNSTABLE_TNT = registerBlockItem(MSBlocks.UNSTABLE_TNT);
	public static final RegistryObject<BlockItem> INSTANT_TNT = registerBlockItem(MSBlocks.INSTANT_TNT);
	public static final RegistryObject<BlockItem> WOODEN_EXPLOSIVE_BUTTON = registerBlockItem(MSBlocks.WOODEN_EXPLOSIVE_BUTTON);
	public static final RegistryObject<BlockItem> STONE_EXPLOSIVE_BUTTON = registerBlockItem(MSBlocks.STONE_EXPLOSIVE_BUTTON);
	
	
	
	//Misc Alchemy Objects
	public static final RegistryObject<BlockItem> BLENDER = registerBlockItem(MSBlocks.BLENDER);
	public static final RegistryObject<BlockItem> CHESSBOARD = registerBlockItem(MSBlocks.CHESSBOARD);
	public static final RegistryObject<BlockItem> MINI_FROG_STATUE = registerBlockItem(MSBlocks.MINI_FROG_STATUE);
	public static final RegistryObject<BlockItem> MINI_WIZARD_STATUE = registerBlockItem(MSBlocks.MINI_WIZARD_STATUE);
	public static final RegistryObject<BlockItem> MINI_TYPHEUS_STATUE = registerBlockItem(MSBlocks.MINI_TYPHEUS_STATUE);
	public static final RegistryObject<BlockItem> CASSETTE_PLAYER = registerBlockItem(MSBlocks.CASSETTE_PLAYER, new Item.Properties());
	public static final RegistryObject<BlockItem> GLOWYSTONE_DUST = registerBlockItem(MSBlocks.GLOWYSTONE_DUST);
	public static final RegistryObject<BlockItem> MIRROR = registerBlockItem(MSBlocks.MIRROR);
	
	/**
	 * Helper function to register a standard BlockItem with just the source block, no item properties
	 */
	private static RegistryObject<BlockItem> registerBlockItem(RegistryObject<? extends Block> block)
	{
		return registerBlockItem(block, block1 -> new BlockItem(block1, new Item.Properties()));
	}
	
	/**
	 * Helper function to register a standard BlockItem with a parameter for a unique item property
	 */
	private static RegistryObject<BlockItem> registerBlockItem(RegistryObject<? extends Block> block, Item.Properties properties)
	{
		return registerBlockItem(block, block1 -> new BlockItem(block1, properties));
	}
	
	/**
	 * Helper function to register custom classes extending BlockItem, or is fed a standard BlockItem through the other registerBlockItem() function
	 */
	private static RegistryObject<BlockItem> registerBlockItem(RegistryObject<? extends Block> block, Function<Block, ? extends BlockItem> function)
	{
		return REGISTER.register(block.getKey().location().getPath(), () -> function.apply(block.get())); //assumed getKey() will be non-null due to the way DeferredRegistry works
	}
}
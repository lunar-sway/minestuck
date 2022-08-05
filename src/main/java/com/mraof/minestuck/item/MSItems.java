package com.mraof.minestuck.item;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.EnumCassetteType;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.item.CrewPosterEntity;
import com.mraof.minestuck.entity.item.MetalBoatEntity;
import com.mraof.minestuck.entity.item.SbahjPosterEntity;
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
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.*;

import javax.annotation.Nonnull;

import static com.mraof.minestuck.block.MSBlocks.*;

/**
 * This class contains all non-ItemBlock items that minestuck adds,
 * and is responsible for initializing and registering these.
 */
@ObjectHolder(Minestuck.MOD_ID)
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MSItems
{
	public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, Minestuck.MOD_ID);
	
	/**/
	public static final MultiblockItem CRUXTRUDER = REGISTER.register("template", () -> new WeaponItem(); //getnull
	public static final MultiblockItem TOTEM_LATHE = REGISTER.register("template", () -> new WeaponItem();
	public static final MultiblockItem ALCHEMITER = REGISTER.register("template", () -> new WeaponItem();
	public static final MultiblockItem PUNCH_DESIGNIX = REGISTER.register("template", () -> new WeaponItem();
	public static final MultiblockItem LOTUS_TIME_CAPSULE = REGISTER.register("template", () -> new WeaponItem();
	
	public static final RegistryObject<Item> CRUXITE_DOWEL = REGISTER.register("template", () -> new WeaponItem();
	
	//hammers
	public static final RegistryObject<Item> CLAW_HAMMER9j = REGISTER.register("claw_hammer", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 2, -2.8F).efficiency(1.0F).set(MSItemTypes.HAMMER_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> SLEDGE_HAMMER9j = REGISTER.register("sledge_hammer", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 4, -3.2F).efficiency(4.0F).set(MSItemTypes.HAMMER_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> MAILBOX9j = REGISTER.register("mailbox", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 4, -3.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> BLACKSMITH_HAMMER9j = REGISTER.register("blacksmith_hammer", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 5, -3.2F).efficiency(3.5F).set(MSItemTypes.HAMMER_TOOL), new Item.Properties().defaultDurability(450).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> POGO_HAMMER9j = REGISTER.register("pogo_hammer", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.POGO_TIER, 5, -3.2F).efficiency(2.0F).set(MSItemTypes.HAMMER_TOOL).set(PogoEffect.EFFECT_07).add(PogoEffect.EFFECT_07), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> WRINKLEFUCKER9j = REGISTER.register("wrinklefucker", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.POGO_TIER, 7, -3.2F).efficiency(2.0F).set(MSItemTypes.HAMMER_TOOL).set(PogoEffect.EFFECT_04).add(PogoEffect.EFFECT_04), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> TELESCOPIC_SASSACRUSHER9j = REGISTER.register("telescopic_sassacrusher", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.BOOK_TIER, 11, -3.4F).efficiency(5.0F).set(MSItemTypes.HAMMER_TOOL), new Item.Properties().defaultDurability(1024).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> DEMOCRATIC_DEMOLITIONER9j = REGISTER.register("democratic_demolitioner", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 4, -3.0F).efficiency(1.0F).set(MSItemTypes.HAMMER_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> REGI_HAMMER9j = REGISTER.register("regi_hammer", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 7, -3.2F).efficiency(8.0F).set(MSItemTypes.HAMMER_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> FEAR_NO_ANVIL9j = REGISTER.register("fear_no_anvil", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.DENIZEN_TIER, 7, -3.2F).efficiency(7.0F).set(MSItemTypes.HAMMER_TOOL).add(OnHitEffect.TIME_SLOWNESS_AOE).add(OnHitEffect.enemyPotionEffect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1))), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> MELT_MASHER9j = REGISTER.register("melt_masher", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 8, -3.2F).efficiency(12.0F).set(MSItemTypes.HAMMER_TOOL).add(OnHitEffect.setOnFire(25)), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> ESTROGEN_EMPOWERED_EVERYTHING_ERADICATOR9j = REGISTER.register("estrogen_empowered_everything_eradicator", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 7, -3.2F).efficiency(9.0F).set(MSItemTypes.MULTI_TOOL).set(new FarmineEffect(Integer.MAX_VALUE, 200)).set(PogoEffect.EFFECT_07).add(PogoEffect.EFFECT_07), new Item.Properties().defaultDurability(6114).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> EEEEEEEEEEEE9j = REGISTER.register("eeeeeeeeeeee", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.SBAHJ_TIER, 9, -3.2F).efficiency(9.1F).set(MSItemTypes.HAMMER_TOOL).set(PogoEffect.EFFECT_02).add(PogoEffect.EFFECT_02, OnHitEffect.playSound(() -> MSSoundEvents.ITEM_EEEEEEEEEEEE_HIT, 1.5F, 1.0F)), new Item.Properties().defaultDurability(6114).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> ZILLYHOO_HAMMER9j = REGISTER.register("zillyhoo_hammer", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ZILLY_TIER, 8, -3.2F).efficiency(15.0F).set(MSItemTypes.HAMMER_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> POPAMATIC_VRILLYHOO9j = REGISTER.register("popamatic_vrillyhoo", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ZILLY_TIER, 6, -3.2F).efficiency(15.0F).set(MSItemTypes.HAMMER_TOOL).add(OnHitEffect.RANDOM_DAMAGE), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> SCARLET_ZILLYHOO9j = REGISTER.register("scarlet_zillyhoo", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CORUNDUM_TIER, 9, -3.2F).efficiency(4.0F).set(MSItemTypes.HAMMER_TOOL).add(OnHitEffect.setOnFire(50)), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> MWRTHWL9j = REGISTER.register("mwrthwl", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.WELSH_TIER, 8, -3.2F).efficiency(4.0F).set(MSItemTypes.HAMMER_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.EPIC)));
	
	//blades
	public static final RegistryObject<Item> SORD9j = REGISTER.register("sord", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.SBAHJ_TIER, 3, -3.0F).efficiency(1.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SORD_DROP), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> PAPER_SWORD9j = REGISTER.register("paper_sword", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.PAPER_TIER, 2, -2.4F).efficiency(3.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> SWONGE9j = REGISTER.register("swonge", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 3, -2.4F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SWEEP).set(ItemRightClickEffect.absorbFluid(() -> Blocks.WATER, () -> MSItems.WET_SWONGE9j)), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> WET_SWONGE9j = REGISTER.register("wet_swonge", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 3, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SWEEP).set(RightClickBlockEffect.placeFluid(() -> Blocks.WATER, () -> MSItems.SWONGE9j)).add(OnHitEffect.playSound(() -> SoundEvents.GUARDIAN_FLOP)), new Item.Properties()));
	public static final RegistryObject<Item> PUMORD9j = REGISTER.register("pumord", () -> new WeaponItem(new WeaponItem.Builder(Tiers.STONE, 3, -2.4F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SWEEP).set(ItemRightClickEffect.absorbFluid(() -> Blocks.LAVA, () -> MSItems.WET_PUMORD9j)), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> WET_PUMORD9j = REGISTER.register("wet_pumord", () -> new WeaponItem(new WeaponItem.Builder(Tiers.STONE, 3, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SWEEP).set(RightClickBlockEffect.placeFluid(() -> Blocks.LAVA, () -> MSItems.PUMORD9j)).add(OnHitEffect.playSound(() -> SoundEvents.BUCKET_EMPTY_LAVA, 1.0F, 0.2F)).add(OnHitEffect.setOnFire(10)), new Item.Properties()));
	public static final RegistryObject<Item> CACTACEAE_CUTLASS9j = REGISTER.register("cactaceae_cutlass", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CACTUS_TIER, 3, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().tab(MSItemGroup.WEAPONS))); //The sword harvestTool is only used against webs, hence the high efficiency.
	public static final RegistryObject<Item> STEAK_SWORD9j = REGISTER.register("steak_sword", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.MEAT_TIER, 4, -2.4F).efficiency(5.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).setEating(FinishUseItemEffect.foodEffect(8, 1F)), new Item.Properties().defaultDurability(250).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> BEEF_SWORD9j = REGISTER.register("beef_sword", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.MEAT_TIER, 2, -2.4F).efficiency(5.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).setEating(FinishUseItemEffect.foodEffect(3, 0.8F, 75)), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> IRRADIATED_STEAK_SWORD9j = REGISTER.register("irradiated_steak_sword", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.MEAT_TIER, 5, -2.4F).efficiency(5.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).setEating(FinishUseItemEffect.potionEffect(() -> new MobEffectInstance(MobEffects.WITHER, 100, 1), 0.9F), FinishUseItemEffect.foodEffect(4, 0.4F, 25)), new Item.Properties().defaultDurability(300).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> MACUAHUITL9j = REGISTER.register("macuahuitl", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.PRISMARINE_TIER, 3, -2.4F).efficiency(1.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().defaultDurability(100).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> FROSTY_MACUAHUITL9j = REGISTER.register("frosty_macuahuitl", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ICE_TIER, 6, -2.4F).efficiency(1.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).add(OnHitEffect.ICE_SHARD), new Item.Properties().defaultDurability(200).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> KATANA9j = REGISTER.register("katana", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 3, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> UNBREAKABLE_KATANA9j = REGISTER.register("unbreakable_katana", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ZILLY_TIER, 6, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().defaultDurability(-1).tab(MSItemGroup.WEAPONS).rarity(Rarity.RARE))); //Actually unbreakable
	public static final RegistryObject<Item> ANGEL_APOCALYPSE9j = REGISTER.register("angel_apocalypse", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 5, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).add(OnHitEffect.HOPE_RESISTANCE), new Item.Properties().defaultDurability(2048).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> FIRE_POKER9j = REGISTER.register("fire_poker", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 4, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).add(OnHitEffect.setOnFire(30)), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> TOO_HOT_TO_HANDLE9j = REGISTER.register("too_hot_to_handle", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 3, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).add(OnHitEffect.setOnFire(10)), new Item.Properties().defaultDurability(350).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> CALEDSCRATCH9j = REGISTER.register("caledscratch", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 7, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> CALEDFWLCH9j = REGISTER.register("caledfwlch", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.WELSH_TIER, 6, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> ROYAL_DERINGER9j = REGISTER.register("royal_deringer", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.DENIZEN_TIER, 7, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> CLAYMORE9j = REGISTER.register("claymore", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 5, -2.6F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().defaultDurability(600).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> CUTLASS_OF_ZILLYWAIR9j = REGISTER.register("cutlass_of_zillywair", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ZILLY_TIER, 6, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> REGISWORD9j = REGISTER.register("regisword", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 5, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> CRUEL_FATE_CRUCIBLE9j = REGISTER.register("cruel_fate_crucible", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.DENIZEN_TIER, 4, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().defaultDurability(4096).tab(MSItemGroup.WEAPONS).rarity(Rarity.RARE))); //Special property in ServerEventHandler
	public static final RegistryObject<Item> SCARLET_RIBBITAR9j = REGISTER.register("scarlet_ribbitar", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CORUNDUM_TIER, 7, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> DOGG_MACHETE9j = REGISTER.register("dogg_machete", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 6, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().defaultDurability(1000).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> COBALT_SABRE9j = REGISTER.register("cobalt_sabre", () -> new WeaponItem(new WeaponItem.Builder(Tiers.GOLD, 7, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).add(OnHitEffect.setOnFire(30)), new Item.Properties().defaultDurability(300).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> QUANTUM_SABRE9j = REGISTER.register("quantum_sabre", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.URANIUM_TIER, 4, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).add(OnHitEffect.onCrit(OnHitEffect.enemyPotionEffect(() -> new MobEffectInstance(MobEffects.WITHER, 100, 1)))), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> SHATTER_BEACON9j = REGISTER.register("shatter_beacon", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 7, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> SHATTER_BACON9j = REGISTER.register("shatter_bacon", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.MEAT_TIER, 7, -2.4F).efficiency(5.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).add(OnHitEffect.SORD_DROP), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> SUBTRACTSHUMIDIRE_ZOMORRODNEGATIVE9j = REGISTER.register("subtractshumidire_zomorrodnegative", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.URANIUM_TIER, 6, -2.6F).efficiency(5.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).add(OnHitEffect.setOnFire(30)), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	
	public static final RegistryObject<Item> DAGGER9j = REGISTER.register("dagger", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 0, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.backstab(3)), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> NIFE9j = REGISTER.register("nife", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.SBAHJ_TIER, 1, -2.0F).add(OnHitEffect.SORD_DROP), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> LIGHT_OF_MY_KNIFE9j = REGISTER.register("light_of_my_knife", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.EMERALD_TIER, 1, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.backstab(4)), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> STARSHARD_TRI_BLADE9j = REGISTER.register("starshard_tri_blade", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CORUNDUM_TIER, 1, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.backstab(9)), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> TOOTHRIPPER9j = REGISTER.register("toothripper", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 3, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.backstab(4)), new Item.Properties().defaultDurability(1200).tab(MSItemGroup.WEAPONS)));
	
	//axes
	public static final RegistryObject<Item> BATLEACKS9j = REGISTER.register("batleacks", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.SBAHJ_TIER, 3, -3.5F).efficiency(1.0F).set(MSItemTypes.AXE_TOOL).add(OnHitEffect.SORD_DROP), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> COPSE_CRUSHER9j = REGISTER.register("copse_crusher", () -> new WeaponItem(new WeaponItem.Builder(Tiers.STONE, 5, -3.0F).efficiency(6.0F).disableShield().set(MSItemTypes.AXE_TOOL).set(new FarmineEffect(Integer.MAX_VALUE, 20)), new Item.Properties().defaultDurability(400).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> QUENCH_CRUSHER9j = REGISTER.register("quench_crusher", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 7, -3.0F).efficiency(2.0F).disableShield().set(MSItemTypes.AXE_TOOL).setEating(FinishUseItemEffect.foodEffect(6, 0.6F, 75)), new Item.Properties().defaultDurability(500).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> MELONSBANE9j = REGISTER.register("melonsbane", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 8, -3.0F).efficiency(3.0F).disableShield().set(MSItemTypes.AXE_TOOL).set(DestroyBlockEffect.extraHarvests(true, 0.6F, 20, () -> Items.MELON_SLICE, () -> Blocks.MELON)), new Item.Properties().defaultDurability(400).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> CROP_CHOP9j = REGISTER.register("crop_chop", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 7, -3.0F).efficiency(3.0F).disableShield().set(MSItemTypes.AXE_TOOL).set(DestroyBlockEffect.DOUBLE_FARM), new Item.Properties().defaultDurability(800).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> THE_LAST_STRAW9j = REGISTER.register("the_last_straw", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 9, -3.0F).efficiency(3.0F).disableShield().set(MSItemTypes.AXE_TOOL).set(DestroyBlockEffect.DOUBLE_FARM), new Item.Properties().defaultDurability(950).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> BATTLEAXE9j = REGISTER.register("battleaxe", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 8, -3.0F).efficiency(3.0F).disableShield().set(MSItemTypes.AXE_TOOL), new Item.Properties().defaultDurability(600).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> CANDY_BATTLEAXE9j = REGISTER.register("candy_battleaxe", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CANDY_TIER, 8, -3.0F).efficiency(2.0F).disableShield().set(MSItemTypes.AXE_TOOL).add(OnHitEffect.SET_CANDY_DROP_FLAG), new Item.Properties().defaultDurability(111).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> CHOCO_LOCO_WOODSPLITTER9j = REGISTER.register("choco_loco_woodsplitter", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CANDY_TIER, 9, -3.0F).efficiency(2.0F).disableShield().set(MSItemTypes.AXE_TOOL).setEating(FinishUseItemEffect.foodEffect(8, 0.4F)), new Item.Properties().defaultDurability(350).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> STEEL_EDGE_CANDYCUTTER9j = REGISTER.register("steel_edge_candycutter", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 8, -3.0F).efficiency(3.0F).disableShield().set(MSItemTypes.AXE_TOOL).add(OnHitEffect.SET_CANDY_DROP_FLAG), new Item.Properties().defaultDurability(800).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> BLACKSMITH_BANE9j = REGISTER.register("blacksmith_bane", () -> new WeaponItem(new WeaponItem.Builder(Tiers.STONE, 8, -3.0F).efficiency(6.0F).disableShield().set(MSItemTypes.AXE_TOOL), new Item.Properties().defaultDurability(413).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> REGIAXE9j = REGISTER.register("regiaxe", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 6, -3.0F).disableShield().efficiency(6.0F).set(MSItemTypes.AXE_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> GOTHY_AXE9j = REGISTER.register("gothy_axe", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 8, -3.0F).efficiency(6.0F).disableShield().set(MSItemTypes.AXE_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> SURPRISE_AXE9j = REGISTER.register("surprise_axe", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 7, -3.0F).efficiency(6.0F).disableShield().set(MSItemTypes.AXE_TOOL).add(OnHitEffect.KUNDLER_SURPRISE), new Item.Properties().defaultDurability(600).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> SHOCK_AXE9j = REGISTER.register("shock_axe", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 8, -3.0F).efficiency(6.0F).disableShield().set(MSItemTypes.AXE_TOOL).set(ItemRightClickEffect.switchTo(() -> SHOCK_AXE_UNPOWERED9j)).add(OnHitEffect.DROP_FOE_ITEM).add(InventoryTickEffect.DROP_WHEN_IN_WATER).add(OnHitEffect.playSound(() -> MSSoundEvents.EVENT_ELECTRIC_SHOCK, 0.6F, 1.0F)), new Item.Properties().defaultDurability(800).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> SHOCK_AXE_UNPOWERED9j = REGISTER.register("shock_axe_unpowered", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 8, -3.0F).efficiency(6.0F).disableShield().set(MSItemTypes.AXE_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.SHOCK_AXE9j)), new Item.Properties().defaultDurability(800)));
	public static final RegistryObject<Item> SCRAXE9j = REGISTER.register("scraxe", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 8, -3.0F).efficiency(7.0F).disableShield().set(MSItemTypes.AXE_TOOL), new Item.Properties().defaultDurability(500).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> LORENTZ_DISTRANSFORMATIONER9j = REGISTER.register("lorentz_distransformationer", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 6, -3.0F).efficiency(7.0F).disableShield().set(MSItemTypes.AXE_TOOL).add(OnHitEffect.SPACE_TELEPORT), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> PISTON_POWERED_POGO_AXEHAMMER9j = REGISTER.register("piston_powered_pogo_axehammer", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.POGO_TIER, 6, -3.0F).efficiency(2.0F).disableShield().set(MSItemTypes.AXE_HAMMER_TOOL).set(new FarmineEffect(Integer.MAX_VALUE, 50)).set(PogoEffect.EFFECT_06).add(PogoEffect.EFFECT_06), new Item.Properties().defaultDurability(800).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> RUBY_CROAK9j = REGISTER.register("ruby_croak", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CORUNDUM_TIER, 8, -3.0F).efficiency(8.0F).disableShield().set(MSItemTypes.AXE_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> HEPHAESTUS_LUMBERJACK9j = REGISTER.register("hephaestus_lumberjack", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CORUNDUM_TIER, 7, -3.0F).efficiency(9.0F).disableShield().set(MSItemTypes.AXE_TOOL).add(OnHitEffect.setOnFire(30)), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> FISSION_FOCUSED_FAULT_FELLER9j = REGISTER.register("fission_focused_fault_feller", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 7, -3.0F).efficiency(5.0F).disableShield().set(MSItemTypes.AXE_HAMMER_TOOL).set(new FarmineEffect(Integer.MAX_VALUE, 100)).set(PogoEffect.EFFECT_07).add(PogoEffect.EFFECT_07), new Item.Properties().defaultDurability(2048).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> BISECTOR9j = REGISTER.register("bisector", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 9, -3.2F).efficiency(5.0F).disableShield().set(MSItemTypes.AXE_TOOL), new Item.Properties().defaultDurability(600).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> FINE_CHINA_AXE9j = REGISTER.register("fine_china_axe", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 9, -3.2F).efficiency(1.0F).disableShield().set(MSItemTypes.AXE_TOOL), new Item.Properties().defaultDurability(8).tab(MSItemGroup.WEAPONS)));
	
	//misc weapons
	public static final RegistryObject<Item> FLUORITE_OCTET9j = REGISTER.register("fluorite_octet", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 4, -3.0F).efficiency(1.0F).add(OnHitEffect.RANDOM_DAMAGE), new Item.Properties().tab(MSItemGroup.WEAPONS).defaultDurability(4096).rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> CAT_CLAWS_DRAWN9j = REGISTER.register("cat_claws_drawn", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 2, -1.5F).efficiency(10.0F).set(MSItemTypes.CLAWS_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.CAT_CLAWS_SHEATHED9j)), new Item.Properties().defaultDurability(500).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> CAT_CLAWS_SHEATHED9j = REGISTER.register("cat_claws_sheathed", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, -1, -1.0F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.CAT_CLAWS_DRAWN9j)), new Item.Properties().defaultDurability(500)));
	public static final RegistryObject<Item> SKELETONIZER_DRAWN9j = REGISTER.register("skeletonizer_drawn", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 5, -1.5F).efficiency(10.0F).set(MSItemTypes.CLAWS_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.SKELETONIZER_SHEATHED9j)), new Item.Properties().defaultDurability(750).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> SKELETONIZER_SHEATHED9j = REGISTER.register("skeletonizer_sheathed", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, -1, -1.0F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.SKELETONIZER_DRAWN9j)), new Item.Properties().defaultDurability(750)));
	public static final RegistryObject<Item> SKELETON_DISPLACER_DRAWN9j = REGISTER.register("skeleton_displacer_drawn", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 6, -1.5F).efficiency(10.0F).set(MSItemTypes.CLAWS_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.SKELETON_DISPLACER_SHEATHED9j)).add(OnHitEffect.targetSpecificAdditionalDamage(4, () -> EntityType.SKELETON)), new Item.Properties().defaultDurability(1250).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> SKELETON_DISPLACER_SHEATHED9j = REGISTER.register("skeleton_displacer_sheathed", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, -1, -1.0F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.SKELETON_DISPLACER_DRAWN9j)), new Item.Properties().defaultDurability(1250)));
	public static final RegistryObject<Item> TEARS_OF_THE_ENDERLICH_DRAWN9j = REGISTER.register("tears_of_the_enderlich_drawn", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CORUNDUM_TIER, 4, -1.5F).efficiency(10.0F).set(MSItemTypes.CLAWS_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.TEARS_OF_THE_ENDERLICH_SHEATHED9j)).add(OnHitEffect.targetSpecificAdditionalDamage(6, () -> MSEntityTypes.LICH)), new Item.Properties().defaultDurability(2000).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> TEARS_OF_THE_ENDERLICH_SHEATHED9j = REGISTER.register("tears_of_the_enderlich_sheathed", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CORUNDUM_TIER, -4, -1.0F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.TEARS_OF_THE_ENDERLICH_DRAWN9j)), new Item.Properties().defaultDurability(2000).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> ACTION_CLAWS_DRAWN9j = REGISTER.register("action_claws_drawn", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 5, -1.5F).efficiency(10.0F).set(MSItemTypes.CLAWS_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.ACTION_CLAWS_SHEATHED9j)), new Item.Properties().defaultDurability(4096).tab(MSItemGroup.WEAPONS).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> ACTION_CLAWS_SHEATHED9j = REGISTER.register("action_claws_sheathed", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, -1, -1.0F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.ACTION_CLAWS_DRAWN9j)), new Item.Properties().defaultDurability(4096).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> LIPSTICK_CHAINSAW9j = REGISTER.register("lipstick_chainsaw", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 2, -1.5F).efficiency(10.0F).set(MSItemTypes.AXE_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.LIPSTICK9j)), new Item.Properties().defaultDurability(250).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> LIPSTICK9j = REGISTER.register("lipstick", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, -1, -0.5F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.LIPSTICK_CHAINSAW9j)), new Item.Properties().defaultDurability(250)));
	public static final RegistryObject<Item> THISTLEBLOWER9j = REGISTER.register("thistleblower", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 4, -1.0F).efficiency(2.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.THISTLEBLOWER_LIPSTICK9j)), new Item.Properties().defaultDurability(500).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> THISTLEBLOWER_LIPSTICK9j = REGISTER.register("thistleblower_lipstick", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, -1, -0.5F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.THISTLEBLOWER9j)), new Item.Properties().defaultDurability(500)));
	public static final RegistryObject<Item> EMERALD_IMMOLATOR9j = REGISTER.register("emerald_immolator", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.EMERALD_TIER, 3, -1.5F).efficiency(10.0F).set(MSItemTypes.AXE_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.EMERALD_IMMOLATOR_LIPSTICK9j)).add(OnHitEffect.setOnFire(5)), new Item.Properties().defaultDurability(1024).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> EMERALD_IMMOLATOR_LIPSTICK9j = REGISTER.register("emerald_immolator_lipstick", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, -1, -0.5F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.EMERALD_IMMOLATOR9j)), new Item.Properties().defaultDurability(1024)));
	public static final RegistryObject<Item> OBSIDIATOR9j = REGISTER.register("obsidiator", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CORUNDUM_TIER, 4, -1.5F).efficiency(10.0F).set(MSItemTypes.AXE_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.OBSIDIATOR_LIPSTICK9j)), new Item.Properties().defaultDurability(2048).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> OBSIDIATOR_LIPSTICK9j = REGISTER.register("obsidiator_lipstick", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, -1, -0.5F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.OBSIDIATOR9j)), new Item.Properties().defaultDurability(2048).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> FROSTTOOTH9j = REGISTER.register("frosttooth", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ICE_TIER, 5, -1.5F).efficiency(10.0F).set(MSItemTypes.AXE_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.FROSTTOOTH_LIPSTICK9j)).add(OnHitEffect.ICE_SHARD), new Item.Properties().defaultDurability(1536).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> FROSTTOOTH_LIPSTICK9j = REGISTER.register("frosttooth_lipstick", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, -1, -0.5F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.FROSTTOOTH9j)), new Item.Properties().defaultDurability(1536)));
	public static final RegistryObject<Item> JOUSTING_LANCE9j = REGISTER.register("jousting_lance", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 4, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> CIGARETTE_LANCE9j = REGISTER.register("cigarette_lance", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 4, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> LUCERNE_HAMMER9j = REGISTER.register("lucerne_hammer", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 3, -2.5F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> LUCERNE_HAMMER_OF_UNDYING9j = REGISTER.register("lucerne_hammer_of_undying", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 4, -2.5F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().defaultDurability(2048).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON))); //Special property in ServerEventHandler
	public static final RegistryObject<Item> OBSIDIAN_AXE_KNIFE9j = REGISTER.register("obsidian_axe_knife", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.PRISMARINE_TIER, 2, -2.0F).efficiency(1.5F).set(MSItemTypes.MISC_TOOL), new Item.Properties().durability(100).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> FAN9j = REGISTER.register("fan", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.PAPER_TIER, 1, -1.0F).efficiency(1.5F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.extinguishFire(1)).add(OnHitEffect.enemyKnockback(1.5F)), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> TYPHONIC_TRIVIALIZER9j = REGISTER.register("typhonic_trivializer", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.DENIZEN_TIER, 2, -1.0F).efficiency(1.5F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.extinguishFire(3)).add(OnHitEffect.BREATH_LEVITATION_AOE).add(OnHitEffect.enemyKnockback(2.0F)), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.RARE)));
	
	//sickles
	public static final RegistryObject<Item> SICKLE9j = REGISTER.register("sickle", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 2, -2.2F).efficiency(1.5F).disableShield().set(MSItemTypes.SICKLE_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> BISICKLE9j = REGISTER.register("bisickle", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 2, -2.2F).efficiency(1.5F).disableShield().set(MSItemTypes.SICKLE_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> OW_THE_EDGE9j = REGISTER.register("ow_the_edge", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.SBAHJ_TIER, 3, -3.0F).efficiency(1.0F).set(MSItemTypes.SICKLE_TOOL).add(OnHitEffect.SORD_DROP), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> HEMEOREAPER9j = REGISTER.register("hemeoreaper", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 5, -2.2F).efficiency(1.0F).disableShield().set(MSItemTypes.SICKLE_TOOL), new Item.Properties().defaultDurability(550).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> THORNY_SUBJECT9j = REGISTER.register("thorny_subject", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CACTUS_TIER, 4, -2.2F).efficiency(1.0F).disableShield().set(MSItemTypes.SICKLE_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> HOMES_SMELL_YA_LATER9j = REGISTER.register("homes_smell_ya_later", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 4, -2.2F).efficiency(3.0F).disableShield().set(MSItemTypes.SICKLE_TOOL), new Item.Properties().defaultDurability(400).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> FUDGESICKLE9j = REGISTER.register("fudgesickle", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CANDY_TIER, 5, -2.2F).efficiency(1.0F).disableShield().set(MSItemTypes.SICKLE_TOOL).setEating(FinishUseItemEffect.foodEffect(7, 0.6F)), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> REGISICKLE9j = REGISTER.register("regisickle", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 4, -2.2F).efficiency(4.0F).disableShield().set(MSItemTypes.SICKLE_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> HERETICUS_AURURM9j = REGISTER.register("hereticus_aururm", () -> new WeaponItem(new WeaponItem.Builder(Tiers.GOLD, 9, -2.2F).efficiency(4.0F).disableShield().set(MSItemTypes.SICKLE_TOOL), new Item.Properties().defaultDurability(1024).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> CLAW_SICKLE9j = REGISTER.register("claw_sickle", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 8, -2.2F).efficiency(4.0F).disableShield().set(MSItemTypes.SICKLE_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> CLAW_OF_NRUBYIGLITH9j = REGISTER.register("claw_of_nrubyiglith", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.HORRORTERROR_TIER, 6, -2.2F).efficiency(4.0F).disableShield().set(MSItemTypes.SICKLE_TOOL).add(OnHitEffect.HORRORTERROR), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> CANDY_SICKLE9j = REGISTER.register("candy_sickle", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CANDY_TIER, 6, -2.2F).efficiency(2.5F).disableShield().set(MSItemTypes.SICKLE_TOOL).add(OnHitEffect.SET_CANDY_DROP_FLAG), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> SCYTHE9j = REGISTER.register("scythe", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 4, -2.6F).efficiency(1.5F).disableShield().set(MSItemTypes.SICKLE_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> EIGHTBALL_SCYTHE9j = REGISTER.register("eightball_scythe", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 3, -2.6F).efficiency(1.5F).disableShield().set(MSItemTypes.SICKLE_TOOL).add(OnHitEffect.RANDOM_DAMAGE).set(ItemRightClickEffect.EIGHTBALL), new Item.Properties().defaultDurability(600).tab(MSItemGroup.WEAPONS)));
	
	//clubs
	public static final RegistryObject<Item> DEUCE_CLUB9j = REGISTER.register("deuce_club", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 3, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> STALE_BAGUETTE9j = REGISTER.register("stale_baguette", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 3, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SPAWN_BREADCRUMBS).setEating(FinishUseItemEffect.SPAWN_BREADCRUMBS, FinishUseItemEffect.foodEffect(3, 0.2F)), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> GLUB_CLUB9j = REGISTER.register("glub_club", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.MEAT_TIER, 4, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.playSound(() -> SoundEvents.GUARDIAN_FLOP)), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> NIGHT_CLUB9j = REGISTER.register("night_club", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 1, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> NIGHTSTICK9j = REGISTER.register("nightstick", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 2, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().defaultDurability(2500).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> RED_EYES9j = REGISTER.register("red_eyes", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 6, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.enemyPotionEffect(() -> new MobEffectInstance(MobEffects.POISON, 140, 0))), new Item.Properties().tab(MSItemGroup.WEAPONS)).);
	public static final RegistryObject<Item> PRISMARINE_BASHER9j = REGISTER.register("prismarine_basher", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.PRISMARINE_TIER, 3, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> CLUB_ZERO9j = REGISTER.register("club_zero", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ICE_TIER, 5, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.ICE_SHARD), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> POGO_CLUB9j = REGISTER.register("pogo_club", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.POGO_TIER, 4, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).set(PogoEffect.EFFECT_05).add(PogoEffect.EFFECT_05), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> BARBER_BASHER9j = REGISTER.register("barber_basher", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 3, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().defaultDurability(350).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> METAL_BAT9j = REGISTER.register("metal_bat", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 4, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> CLOWN_CLUB9j = REGISTER.register("clown_club", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 5, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.RAGE_STRENGTH, OnHitEffect.playSound(() -> MSSoundEvents.ITEM_HORN_USE, 1.5F, 1)), new Item.Properties().defaultDurability(2048).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> MACE9j = REGISTER.register("mace", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 5, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().defaultDurability(500).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> M_ACE9j = REGISTER.register("m_ace", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 6, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().defaultDurability(750).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> DESOLATOR_MACE9j = REGISTER.register("desolator_mace", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 1, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.armorBypassingDamageMod(4, EnumAspect.VOID)), new Item.Properties().defaultDurability(2048).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> BLAZING_GLORY9j = REGISTER.register("blazing_glory", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 7, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.setOnFire(35)), new Item.Properties().defaultDurability(750).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> SPIKED_CLUB9j = REGISTER.register("spiked_club", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 5, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().defaultDurability(100).tab(MSItemGroup.WEAPONS)));
	
	public static final RegistryObject<Item> HORSE_HITCHER9j = REGISTER.register("horse_hitcher", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 7, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.ACE_OF_SPADES9j)), new Item.Properties().defaultDurability(500).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> ACE_OF_SPADES9j = REGISTER.register("ace_of_spades", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.PAPER_TIER, 0, -1.8F).efficiency(0.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.HORSE_HITCHER9j)), new Item.Properties().defaultDurability(500)));
	public static final RegistryObject<Item> CLUB_OF_FELONY9j = REGISTER.register("club_of_felony", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 7, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.ACE_OF_CLUBS9j)), new Item.Properties().defaultDurability(500).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> ACE_OF_CLUBS9j = REGISTER.register("ace_of_clubs", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.PAPER_TIER, 0, -1.8F).efficiency(0.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.CLUB_OF_FELONY9j)), new Item.Properties().defaultDurability(500)));
	public static final RegistryObject<Item> CUESTICK9j = REGISTER.register("cuestick", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 5, -2.0F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.ACE_OF_DIAMONDS9j)), new Item.Properties().defaultDurability(500).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> ACE_OF_DIAMONDS9j = REGISTER.register("ace_of_diamonds", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.PAPER_TIER, 0, -1.8F).efficiency(0.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.CUESTICK9j)), new Item.Properties().defaultDurability(500)));
	public static final RegistryObject<Item> ACE_OF_HEARTS9j = REGISTER.register("ace_of_hearts", () -> new Item(new Item.Properties().defaultDurability(500)));
	public static final RegistryObject<Item> WHITE_KINGS_SCEPTER9j = REGISTER.register("white_kings_scepter", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 8, -2.8F).efficiency(4.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.summonFireball()), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> BLACK_KINGS_SCEPTER9j = REGISTER.register("black_kings_scepter", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 8, -2.8F).efficiency(4.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.summonFireball()), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.EPIC)));
	
	//canes
	public static final RegistryObject<Item> CANE9j = REGISTER.register("cane", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 2, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().defaultDurability(100).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> VAUDEVILLE_HOOK9j = REGISTER.register("vaudeville_hook", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 2, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().defaultDurability(150).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> BEAR_POKING_STICK9j = REGISTER.register("bear_poking_stick", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 3, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.notAtPlayer(OnHitEffect.enemyPotionEffect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 140, 1)))), new Item.Properties().defaultDurability(150).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> CROWBAR9j = REGISTER.register("crowbar", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 6, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.playSound(() -> SoundEvents.ANVIL_PLACE)), new Item.Properties().defaultDurability(-1).tab(MSItemGroup.WEAPONS).rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> UMBRELLA9j = REGISTER.register("umbrella", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 2, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(InventoryTickEffect.BREATH_SLOW_FALLING), new Item.Properties().defaultDurability(350).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> UPPER_CRUST_CRUST_CANE9j = REGISTER.register("upper_crust_crust_cane", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 3, -2.0F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SPAWN_BREADCRUMBS).setEating(FinishUseItemEffect.SPAWN_BREADCRUMBS, FinishUseItemEffect.foodEffect(4, 0.5F)), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> IRON_CANE9j = REGISTER.register("iron_cane", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 2, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> ZEPHYR_CANE9j = REGISTER.register("zephyr_cane", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 3, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(PropelEffect.BREATH_PROPEL), new Item.Properties().tab(MSItemGroup.WEAPONS).defaultDurability(2048).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> SPEAR_CANE9j = REGISTER.register("spear_cane", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 3, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> PARADISES_PORTABELLO9j = REGISTER.register("paradises_portabello", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 3, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> REGI_CANE9j = REGISTER.register("regi_cane", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 4, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> POGO_CANE9j = REGISTER.register("pogo_cane", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.POGO_TIER, 2, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(PogoEffect.EFFECT_06).add(PogoEffect.EFFECT_06), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> CANDY_CANE9j = REGISTER.register("candy_cane", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CANDY_TIER, 3, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SET_CANDY_DROP_FLAG).setEating(FinishUseItemEffect.foodEffect(2, 0.3F), FinishUseItemEffect.SHARPEN_CANDY_CANE), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> SHARP_CANDY_CANE9j = REGISTER.register("sharp_candy_cane", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CANDY_TIER, 5, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SET_CANDY_DROP_FLAG), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> PRIM_AND_PROPER_WALKING_POLE9j = REGISTER.register("prim_and_proper_walking_pole", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 3, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> DRAGON_CANE9j = REGISTER.register("dragon_cane", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 7, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.DRAGON_CANE_UNSHEATHED9j)), new Item.Properties().defaultDurability(2048).tab(MSItemGroup.WEAPONS).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> DRAGON_CANE_UNSHEATHED9j = REGISTER.register("dragon_cane_unsheathed", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 6, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.DRAGON_CANE9j)), new Item.Properties().defaultDurability(2048).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> CHANCEWYRMS_EXTRA_FORTUNATE_STABBING_IMPLEMENT9j = REGISTER.register("chancewyrms_extra_fortunate_stabbing_implement", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 6, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.CHANCEWYRMS_EXTRA_FORTUNATE_STABBING_IMPLEMENT_UNSHEATHED9j)), new Item.Properties().defaultDurability(4096).tab(MSItemGroup.WEAPONS).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> CHANCEWYRMS_EXTRA_FORTUNATE_STABBING_IMPLEMENT_UNSHEATHED9j = REGISTER.register("chancewyrms_extra_fortunate_stabbing_implement_unsheathed", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 5, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.CHANCEWYRMS_EXTRA_FORTUNATE_STABBING_IMPLEMENT9j)).add(OnHitEffect.RANDOM_DAMAGE), new Item.Properties().defaultDurability(4096).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> LESS_PROPER_WALKING_STICK9j = REGISTER.register("less_proper_walking_stick", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 3, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.LESS_PROPER_WALKING_STICK_SHEATHED9j)), new Item.Properties().defaultDurability(600).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> LESS_PROPER_WALKING_STICK_SHEATHED9j = REGISTER.register("less_proper_walking_stick_sheathed", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 3, -2.0F).efficiency(1.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.LESS_PROPER_WALKING_STICK9j)), new Item.Properties().defaultDurability(600)));
	public static final RegistryObject<Item> ROCKEFELLERS_WALKING_BLADECANE9j = REGISTER.register("rockefellers_walking_bladecane", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 5, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.ROCKEFELLERS_WALKING_BLADECANE_SHEATHED9j)), new Item.Properties().defaultDurability(800).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> ROCKEFELLERS_WALKING_BLADECANE_SHEATHED9j = REGISTER.register("rockefellers_walking_bladecane_sheathed", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 5, -2.0F).efficiency(1.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.ROCKEFELLERS_WALKING_BLADECANE9j)), new Item.Properties().defaultDurability(800).rarity(Rarity.UNCOMMON)));
	
	//Spoons/forks
	public static final RegistryObject<Item> WOODEN_SPOON9j = REGISTER.register("wooden_spoon", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 2, -2.4F).efficiency(1.0F).set(MSItemTypes.SHOVEL_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> SILVER_SPOON9j = REGISTER.register("silver_spoon", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 1, -2.4F).efficiency(1.0F).set(MSItemTypes.SHOVEL_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> MELONBALLER9j = REGISTER.register("melonballer", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 2, -2.4F).efficiency(1.0F).set(MSItemTypes.SHOVEL_TOOL).set(RightClickBlockEffect.scoopBlock(() -> Blocks.MELON)), new Item.Properties().tab(MSItemGroup.WEAPONS).defaultDurability(500)));
	public static final RegistryObject<Item> SIGHTSEEKER9j = REGISTER.register("sightseeker", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 6, -2.4F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> TERRAIN_FLATENATOR9j = REGISTER.register("terrain_flatenator", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 3, -2.4F).efficiency(10.0F).set(MSItemTypes.SHOVEL_TOOL).set(new FarmineEffect(Integer.MAX_VALUE, 50)), new Item.Properties().defaultDurability(1024).tab(MSItemGroup.WEAPONS))); //TODO fix inability to use for terrain flattenation
	public static final RegistryObject<Item> NOSFERATU_SPOON9j = REGISTER.register("nosferatu_spoon", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 5, -2.4F).efficiency(1.0F).set(MSItemTypes.SHOVEL_TOOL).add(OnHitEffect.LIFE_SATURATION), new Item.Properties().defaultDurability(2048).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> CROCKER_SPOON9j = REGISTER.register("crocker_spoon", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 5, -2.4F).efficiency(1.0F).set(MSItemTypes.SHOVEL_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.CROCKER_FORK9j)), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> CROCKER_FORK9j = REGISTER.register("crocker_fork", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 6, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.CROCKER_SPOON9j)), new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> SKAIA_FORK9j = REGISTER.register("skaia_fork", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 9, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> FORK9j = REGISTER.register("fork", () -> new WeaponItem(new WeaponItem.Builder(Tiers.STONE, 3, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> CANDY_FORK9j = REGISTER.register("candy_fork", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.CANDY_TIER, 5, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SET_CANDY_DROP_FLAG), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> TUNING_FORK9j = REGISTER.register("tuning_fork", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 3, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.playSound(() -> SoundEvents.NOTE_BLOCK_CHIME)), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> ELECTRIC_FORK9j = REGISTER.register("electric_fork", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 6, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.DROP_FOE_ITEM).add(InventoryTickEffect.DROP_WHEN_IN_WATER).add(OnHitEffect.playSound(() -> MSSoundEvents.EVENT_ELECTRIC_SHOCK, 0.6F, 1.0F)), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> EATING_FORK_GEM9j = REGISTER.register("eating_fork_gem", () -> new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 3, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> DEVIL_FORK9j = REGISTER.register("devil_fork", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.URANIUM_TIER, 6, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.setOnFire(35)), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> SPORK9j = REGISTER.register("spork", () -> new WeaponItem(new WeaponItem.Builder(Tiers.STONE, 4, -2.5F).efficiency(1.0F).set(MSItemTypes.SHOVEL_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> GOLDEN_SPORK9j = REGISTER.register("golden_spork", () -> new WeaponItem(new WeaponItem.Builder(Tiers.GOLD, 5, -2.5F).efficiency(1.0F).set(MSItemTypes.SHOVEL_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> BIDENT9j = REGISTER.register("bident", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 7, -2.9F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> EDISONS_FURY9j = REGISTER.register("edisons_fury", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 7, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(() -> EDISONS_SERENITY9j)).add(OnHitEffect.DROP_FOE_ITEM).add(InventoryTickEffect.DROP_WHEN_IN_WATER).add(OnHitEffect.playSound(() -> MSSoundEvents.EVENT_ELECTRIC_SHOCK, 0.6F, 1.0F)), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> EDISONS_SERENITY9j = REGISTER.register("edisons_serenity", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 7, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(() -> EDISONS_FURY9j)), new Item.Properties()));
	
	//needles/wands
	public static final RegistryObject<Item> POINTY_STICK9j = REGISTER.register("pointy_stick", () -> new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 0, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> KNITTING_NEEDLE9j = REGISTER.register("knitting_needle", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, -1, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> NEEDLE_WAND9j = REGISTER.register("needle_wand", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 0, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(MagicAttackRightClickEffect.STANDARD_MAGIC), new Item.Properties().defaultDurability(1024).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> ARTIFUCKER9j = REGISTER.register("artifucker", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.SBAHJ_TIER, 1, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(MagicAttackRightClickEffect.SBAHJ_AIMBOT_MAGIC), new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> POINTER_WAND9j = REGISTER.register("pointer_wand", () -> new WeaponItem(new WeaponItem.Builder(Tiers.IRON, -1, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(MagicAttackRightClickEffect.AIMBOT_MAGIC), new Item.Properties().defaultDurability(512).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> POOL_CUE_WAND9j = REGISTER.register("pool_cue_wand", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, -1, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(MagicAttackRightClickEffect.POOL_CUE_MAGIC), new Item.Properties().defaultDurability(1250).tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> THORN_OF_OGLOGOTH9j = REGISTER.register("thorn_of_oglogoth", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.HORRORTERROR_TIER, 0, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.HORRORTERROR).set(MagicAttackRightClickEffect.HORRORTERROR_MAGIC), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> THISTLE_OF_ZILLYWICH9j = REGISTER.register("thistle_of_zillywich", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ZILLY_TIER, 0, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(MagicAttackRightClickEffect.ZILLY_MAGIC), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> QUILL_OF_ECHIDNA9j = REGISTER.register("quill_of_echidna", () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.DENIZEN_TIER, -1, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(MagicAttackRightClickEffect.ECHIDNA_MAGIC), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.EPIC)));
	/**/
	
	//projectiles
	public static final RegistryObject<Item> SBAHJARANG9j = REGISTER.register("sbahjarang", () -> new ConsumableProjectileWeaponItem(new Item.Properties().tab(MSItemGroup.WEAPONS), 0.5F, 20.0F, 1));
	public static final RegistryObject<Item> SHURIKEN9j = REGISTER.register("shuriken", () -> new ConsumableProjectileWeaponItem(new Item.Properties().tab(MSItemGroup.WEAPONS), 1.0F, 2.8F, 2));
	public static final RegistryObject<Item> CLUBS_SUITARANG9j = REGISTER.register("clubs_suitarang", () -> new ConsumableProjectileWeaponItem(new Item.Properties().tab(MSItemGroup.WEAPONS), 1.5F, 2.4F, 3));
	public static final RegistryObject<Item> DIAMONDS_SUITARANG9j = REGISTER.register("diamonds_suitarang", () -> new ConsumableProjectileWeaponItem(new Item.Properties().tab(MSItemGroup.WEAPONS), 1.5F, 2.4F, 3));
	public static final RegistryObject<Item> HEARTS_SUITARANG9j = REGISTER.register("hearts_suitarang", () -> new ConsumableProjectileWeaponItem(new Item.Properties().tab(MSItemGroup.WEAPONS), 1.5F, 2.4F, 3));
	public static final RegistryObject<Item> SPADES_SUITARANG9j = REGISTER.register("spades_suitarang", () -> new ConsumableProjectileWeaponItem(new Item.Properties().tab(MSItemGroup.WEAPONS), 1.5F, 2.4F, 3));
	public static final RegistryObject<Item> CHAKRAM9j = REGISTER.register("chakram", () -> new ReturningProjectileWeaponItem(new Item.Properties().tab(MSItemGroup.WEAPONS).durability(250), 1.3F, 1.0F,5, 30));
	public static final RegistryObject<Item> UMBRAL_INFILTRATOR9j = REGISTER.register("umbral_infiltrator", () -> new ReturningProjectileWeaponItem(new Item.Properties().tab(MSItemGroup.WEAPONS).durability(2048), 1.5F, 0.6F,12, 20));
	public static final RegistryObject<Item> SORCERERS_PINBALL9j = REGISTER.register("sorcerers_pinball", () -> new BouncingProjectileWeaponItem(new Item.Properties().tab(MSItemGroup.WEAPONS).durability(250), 1.5F, 1.0F, 5, 20));
	
	//Material tools
	public static final RegistryObject<Item> EMERALD_SWORD9j = REGISTER.register("emerald_sword", () -> new SwordItem(MSItemTypes.EMERALD_TIER, 3, -2.4F, new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> EMERALD_AXE9j = REGISTER.register("emerald_axe", () -> new AxeItem(MSItemTypes.EMERALD_TIER, 5, -3.0F, new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> EMERALD_PICKAXE9j = REGISTER.register("emerald_pickaxe", () -> new PickaxeItem(MSItemTypes.EMERALD_TIER, 1, -2.8F, new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> EMERALD_SHOVEL9j = REGISTER.register("emerald_shovel", () -> new ShovelItem(MSItemTypes.EMERALD_TIER, 1.5F, -3.0F, new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> EMERALD_HOE9j = REGISTER.register("emerald_hoe", () -> new HoeItem(MSItemTypes.EMERALD_TIER, -3, 0.0F, new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> MINE_AND_GRIST9j = REGISTER.register("mine_and_grist", () -> new PickaxeItem(Tiers.DIAMOND, 1, -2.8F, new Item.Properties()/*.tab(MSItemGroup.WEAPONS)*/));
	
	//Armor
	public static final RegistryObject<Item> PRISMARINE_HELMET9j = REGISTER.register("prismarine_helmet", () -> new ArmorItem(MSItemTypes.PRISMARINE_ARMOR, EquipmentSlot.HEAD, new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> PRISMARINE_CHESTPLATE9j = REGISTER.register("prismarine_chestplate", () -> new ArmorItem(MSItemTypes.PRISMARINE_ARMOR, EquipmentSlot.CHEST, new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> PRISMARINE_LEGGINGS9j = REGISTER.register("prismarine_leggings", () -> new ArmorItem(MSItemTypes.PRISMARINE_ARMOR, EquipmentSlot.LEGS, new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> PRISMARINE_BOOTS9j = REGISTER.register("prismarine_boots", () -> new ArmorItem(MSItemTypes.PRISMARINE_ARMOR, EquipmentSlot.FEET, new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> IRON_LASS_GLASSES9j = REGISTER.register("iron_lass_glasses", () -> new ArmorItem(MSItemTypes.IRON_LASS_ARMOR, EquipmentSlot.HEAD, new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> IRON_LASS_CHESTPLATE9j = REGISTER.register("iron_lass_chestplate", () -> new ArmorItem(MSItemTypes.IRON_LASS_ARMOR, EquipmentSlot.CHEST, new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> IRON_LASS_SKIRT9j = REGISTER.register("iron_lass_skirt", () -> new ArmorItem(MSItemTypes.IRON_LASS_ARMOR, EquipmentSlot.LEGS, new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<Item> IRON_LASS_SHOES9j = REGISTER.register("iron_lass_shoes", () -> new ArmorItem(MSItemTypes.IRON_LASS_ARMOR, EquipmentSlot.FEET, new Item.Properties().tab(MSItemGroup.WEAPONS)));
	
	public static final RegistryObject<MSArmorItem> PROSPIT_CIRCLET9j = REGISTER.register("prospit_circlet", () -> new MSArmorItem(MSItemTypes.DREAM_PAJAMAS, EquipmentSlot.HEAD, new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<MSArmorItem> PROSPIT_SHIRT9j = REGISTER.register("prospit_shirt", () -> new MSArmorItem(MSItemTypes.DREAM_PAJAMAS, EquipmentSlot.CHEST, new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<MSArmorItem> PROSPIT_PANTS9j = REGISTER.register("prospit_pants", () -> new MSArmorItem(MSItemTypes.DREAM_PAJAMAS, EquipmentSlot.LEGS, new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<MSArmorItem> PROSPIT_SHOES9j = REGISTER.register("prospit_shoes", () -> new MSArmorItem(MSItemTypes.DREAM_PAJAMAS, EquipmentSlot.FEET, new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<MSArmorItem> DERSE_CIRCLET9j = REGISTER.register("derse_circlet", () -> new MSArmorItem(MSItemTypes.DREAM_PAJAMAS, EquipmentSlot.HEAD, new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<MSArmorItem> DERSE_SHIRT9j = REGISTER.register("derse_shirt", () -> new MSArmorItem(MSItemTypes.DREAM_PAJAMAS, EquipmentSlot.CHEST, new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<MSArmorItem> DERSE_PANTS9j = REGISTER.register("derse_pants", () -> new MSArmorItem(MSItemTypes.DREAM_PAJAMAS, EquipmentSlot.LEGS, new Item.Properties().tab(MSItemGroup.WEAPONS)));
	public static final RegistryObject<MSArmorItem> DERSE_SHOES9j = REGISTER.register("derse_shoes", () -> new MSArmorItem(MSItemTypes.DREAM_PAJAMAS, EquipmentSlot.FEET, new Item.Properties().tab(MSItemGroup.WEAPONS)));
	
	//Core Items
	public static final RegistryObject<Item> BOONDOLLARS9j = REGISTER.register("boondollars", () -> new BoondollarsItem(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> RAW_CRUXITE9j = REGISTER.register("raw_cruxite", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> RAW_URANIUM9j = REGISTER.register("raw_uranium", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> ENERGY_CORE9j = REGISTER.register("energy_core", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN)));
	//have to fix Cruxite artifact classes
	public static final RegistryObject<Item> CRUXITE_APPLE9j = REGISTER.register("cruxite_apple", () -> new CruxiteAppleItem(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> CRUXITE_POTION9j = REGISTER.register("cruxite_potion", () -> new CruxitePotionItem(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> SBURB_CODE9j = REGISTER.register("sburb_code", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> COMPUTER_PARTS9j = REGISTER.register("computer_parts", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> BLANK_DISK9j = REGISTER.register("blank_disk", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> CLIENT_DISK9j = REGISTER.register("client_disk", () -> new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> SERVER_DISK9j = REGISTER.register("server_disk", () -> new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> CAPTCHA_CARD9j = REGISTER.register("captcha_card", () -> new CaptchaCardItem(new Item.Properties().tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> STACK_MODUS_CARD9j = REGISTER.register("stack_modus_card", () -> new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> QUEUE_MODUS_CARD9j = REGISTER.register("queue_modus_card", () -> new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> QUEUESTACK_MODUS_CARD9j = REGISTER.register("queuestack_modus_card", () -> new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> TREE_MODUS_CARD9j = REGISTER.register("tree_modus_card", () -> new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> HASHMAP_MODUS_CARD9j = REGISTER.register("hashmap_modus_card", () -> new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> SET_MODUS_CARD9j = REGISTER.register("set_modus_card", () -> new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> SHUNT9j = REGISTER.register("shunt", () -> new ShuntItem(new Item.Properties().stacksTo(1)));
	
	//Food
	public static final RegistryObject<Item> PHLEGM_GUSHERS9j = REGISTER.register("phlegm_gushers", () -> new HealingFoodItem(4F, new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.PHLEGM_GUSHERS)));
	public static final RegistryObject<Item> SORROW_GUSHERS9j = REGISTER.register("sorrow_gushers", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.SORROW_GUSHERS)));
	
	public static final RegistryObject<Item> BUG_ON_A_STICK9j = REGISTER.register("bug_on_a_stick", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.BUG_ON_A_STICK)));
	public static final RegistryObject<Item> CHOCOLATE_BEETLE9j = REGISTER.register("chocolate_beetle", () -> new Item(new Item.Properties().tab(MSItemGroup.LANDS).food(MSFoods.CHOCOLATE_BEETLE)));
	public static final RegistryObject<Item> CONE_OF_FLIES9j = REGISTER.register("cone_of_flies", () -> new Item(new Item.Properties().tab(MSItemGroup.LANDS).food(MSFoods.CONE_OF_FLIES)));
	public static final RegistryObject<Item> GRASSHOPPER9j = REGISTER.register("grasshopper", () -> new Item(new Item.Properties().tab(MSItemGroup.LANDS).food(MSFoods.GRASSHOPPER)));
	public static final RegistryObject<Item> CICADA9j = REGISTER.register("cicada", () -> new Item(new Item.Properties().tab(MSItemGroup.LANDS).food(MSFoods.CICADA)));
	public static final RegistryObject<Item> JAR_OF_BUGS9j = REGISTER.register("jar_of_bugs", () -> new Item(new Item.Properties().tab(MSItemGroup.LANDS).food(MSFoods.JAR_OF_BUGS)));
	public static final RegistryObject<Item> BUG_MAC9j = REGISTER.register("bug_mac", () -> new Item(new Item.Properties().tab(MSItemGroup.LANDS).food(MSFoods.BUG_MAC)));
	public static final RegistryObject<Item> ONION9j = REGISTER.register("onion", () -> new Item(new Item.Properties().tab(MSItemGroup.LANDS).food(MSFoods.ONION)));
	public static final RegistryObject<Item> SALAD9j = REGISTER.register("salad", () -> new BowlFoodItem(new Item.Properties().tab(MSItemGroup.LANDS).food(MSFoods.SALAD).stacksTo(1)));
	public static final RegistryObject<Item> DESERT_FRUIT9j = REGISTER.register("desert_fruit", () -> new Item(new Item.Properties().tab(MSItemGroup.LANDS).food(MSFoods.DESERT_FRUIT)));
	public static final RegistryObject<Item> ROCK_COOKIE9j = REGISTER.register("rock_cookie", () -> new Item(new Item.Properties().tab(MSItemGroup.LANDS))); //Not actually food, but let's pretend it is
	public static final RegistryObject<Item> WOODEN_CARROT9j = REGISTER.register("wooden_carrot", () -> new Item(new Item.Properties().tab(MSItemGroup.LANDS).food(MSFoods.WOODEN_CARROT)));
	public static final RegistryObject<Item> FUNGAL_SPORE9j = REGISTER.register("fungal_spore", () -> new Item(new Item.Properties().tab(MSItemGroup.LANDS).food(MSFoods.FUNGAL_SPORE)));
	public static final RegistryObject<Item> SPOREO9j = REGISTER.register("sporeo", () -> new Item(new Item.Properties().tab(MSItemGroup.LANDS).food(MSFoods.SPOREO)));
	public static final RegistryObject<Item> MOREL_MUSHROOM9j = REGISTER.register("morel_mushroom", () -> new Item(new Item.Properties().tab(MSItemGroup.LANDS).food(MSFoods.MOREL_MUSHROOM)));
	public static final RegistryObject<Item> SUSHROOM9j = REGISTER.register("sushroom", () -> new Item(new Item.Properties().tab(MSItemGroup.LANDS)));
	public static final RegistryObject<Item> FRENCH_FRY9j = REGISTER.register("french_fry", () -> new Item(new Item.Properties().tab(MSItemGroup.LANDS).food(MSFoods.FRENCH_FRY)));
	public static final RegistryObject<Item> STRAWBERRY_CHUNK9j = REGISTER.register("strawberry_chunk", () -> new ItemNameBlockItem(STRAWBERRY_STEM.get(), new Item.Properties().tab(MSItemGroup.LANDS).food(MSFoods.STRAWBERRY_CHUNK)));
	public static final RegistryObject<Item> FOOD_CAN9j = REGISTER.register("food_can", () -> new Item(new Item.Properties().stacksTo(16).tab(MSItemGroup.MAIN).food(MSFoods.FOOD_CAN)));
	
	public static final RegistryObject<Item> CANDY_CORN9j = REGISTER.register("candy_corn", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.CANDY_CORN)));
	public static final RegistryObject<Item> TUIX_BAR9j = REGISTER.register("tuix_bar", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.TUIX_BAR)));
	public static final RegistryObject<Item> BUILD_GUSHERS9j = REGISTER.register("build_gushers", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.BUILD_GUSHERS)));
	public static final RegistryObject<Item> AMBER_GUMMY_WORM9j = REGISTER.register("amber_gummy_worm", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.AMBER_GUMMY_WORM)));
	public static final RegistryObject<Item> CAULK_PRETZEL9j = REGISTER.register("caulk_pretzel", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.CAULK_PRETZEL)));
	public static final RegistryObject<Item> CHALK_CANDY_CIGARETTE9j = REGISTER.register("chalk_candy_cigarette", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.CHALK_CANDY_CIGARETTE)));
	public static final RegistryObject<Item> IODINE_LICORICE9j = REGISTER.register("iodine_licorice", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.IODINE_LICORICE)));
	public static final RegistryObject<Item> SHALE_PEEP9j = REGISTER.register("shale_peep", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.SHALE_PEEP)));
	public static final RegistryObject<Item> TAR_LICORICE9j = REGISTER.register("tar_licorice", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.TAR_LICORICE)));
	public static final RegistryObject<Item> COBALT_GUM9j = REGISTER.register("cobalt_gum", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.COBALT_GUM)));
	public static final RegistryObject<Item> MARBLE_JAWBREAKER9j = REGISTER.register("marble_jawbreaker", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.MARBLE_JAWBREAKER)));
	public static final RegistryObject<Item> MERCURY_SIXLETS9j = REGISTER.register("mercury_sixlets", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.MERCURY_SIXLETS)));
	public static final RegistryObject<Item> QUARTZ_JELLY_BEAN9j = REGISTER.register("quartz_jelly_bean", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.QUARTZ_JELLY_BEAN)));
	public static final RegistryObject<Item> SULFUR_CANDY_APPLE9j = REGISTER.register("sulfur_candy_apple", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.SULFUR_CANDY_APPLE)));
	public static final RegistryObject<Item> AMETHYST_HARD_CANDY9j = REGISTER.register("amethyst_hard_candy", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.AMETHYST_HARD_CANDY)));
	public static final RegistryObject<Item> GARNET_TWIX9j = REGISTER.register("garnet_twix", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.GARNET_TWIX)));
	public static final RegistryObject<Item> RUBY_LOLLIPOP9j = REGISTER.register("ruby_lollipop", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.RUBY_LOLLIPOP)));
	public static final RegistryObject<Item> RUST_GUMMY_EYE9j = REGISTER.register("rust_gummy_eye", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.RUST_GUMMY_EYE)));
	public static final RegistryObject<Item> DIAMOND_MINT9j = REGISTER.register("diamond_mint", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.DIAMOND_MINT)));
	public static final RegistryObject<Item> GOLD_CANDY_RIBBON9j = REGISTER.register("gold_candy_ribbon", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.GOLD_CANDY_RIBBON)));
	public static final RegistryObject<Item> URANIUM_GUMMY_BEAR9j = REGISTER.register("uranium_gummy_bear", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.URANIUM_GUMMY_BEAR)));
	public static final RegistryObject<Item> ARTIFACT_WARHEAD9j = REGISTER.register("artifact_warhead", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.ARTIFACT_WARHEAD).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> ZILLIUM_SKITTLES9j = REGISTER.register("zillium_skittles", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.ZILLIUM_SKITTLES).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> APPLE_JUICE9j = REGISTER.register("apple_juice", () -> new DrinkableItem(new Item.Properties().stacksTo(16).tab(MSItemGroup.MAIN).food(MSFoods.APPLE_JUICE)));
	public static final RegistryObject<Item> TAB9j = REGISTER.register("tab", () -> new DrinkableItem(new Item.Properties().stacksTo(16).tab(MSItemGroup.MAIN).food(MSFoods.TAB)));
	public static final RegistryObject<Item> ORANGE_FAYGO9j = REGISTER.register("orange_faygo", () -> new DrinkableItem(new Item.Properties().stacksTo(16).tab(MSItemGroup.MAIN).food(MSFoods.FAYGO)));
	public static final RegistryObject<Item> CANDY_APPLE_FAYGO9j = REGISTER.register("candy_apple_faygo", () -> new DrinkableItem(new Item.Properties().stacksTo(16).tab(MSItemGroup.MAIN).food(MSFoods.FAYGO_CANDY_APPLE)));
	public static final RegistryObject<Item> FAYGO_COLA9j = REGISTER.register("faygo_cola", () -> new DrinkableItem(new Item.Properties().stacksTo(16).tab(MSItemGroup.MAIN).food(MSFoods.FAYGO_COLA)));
	public static final RegistryObject<Item> COTTON_CANDY_FAYGO9j = REGISTER.register("cotton_candy_faygo", () -> new DrinkableItem(new Item.Properties().stacksTo(16).tab(MSItemGroup.MAIN).food(MSFoods.FAYGO_COTTON_CANDY)));
	public static final RegistryObject<Item> CREME_SODA_FAYGO9j = REGISTER.register("creme_soda_faygo", () -> new DrinkableItem(new Item.Properties().stacksTo(16).tab(MSItemGroup.MAIN).food(MSFoods.FAYGO_CREME)));
	public static final RegistryObject<Item> GRAPE_FAYGO9j = REGISTER.register("grape_faygo", () -> new DrinkableItem(new Item.Properties().stacksTo(16).tab(MSItemGroup.MAIN).food(MSFoods.FAYGO_GRAPE)));
	public static final RegistryObject<Item> MOON_MIST_FAYGO9j = REGISTER.register("moon_mist_faygo", () -> new DrinkableItem(new Item.Properties().stacksTo(16).tab(MSItemGroup.MAIN).food(MSFoods.FAYGO_MOON_MIST)));
	public static final RegistryObject<Item> PEACH_FAYGO9j = REGISTER.register("peach_faygo", () -> new DrinkableItem(new Item.Properties().stacksTo(16).tab(MSItemGroup.MAIN).food(MSFoods.FAYGO_PEACH)));
	public static final RegistryObject<Item> REDPOP_FAYGO9j = REGISTER.register("redpop_faygo", () -> new DrinkableItem(new Item.Properties().stacksTo(16).tab(MSItemGroup.MAIN).food(MSFoods.FAYGO_REDPOP)));
	public static final RegistryObject<Item> GRUB_SAUCE9j = REGISTER.register("grub_sauce", () -> new DrinkableItem(new Item.Properties().stacksTo(16).tab(MSItemGroup.LANDS).food(MSFoods.GRUB_SAUCE)));
	public static final RegistryObject<Item> IRRADIATED_STEAK9j = REGISTER.register("irradiated_steak", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.IRRADIATED_STEAK)));
	public static final RegistryObject<Item> SURPRISE_EMBRYO9j = REGISTER.register("surprise_embryo", () -> new SurpriseEmbryoItem(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.SURPRISE_EMBRYO)));
	public static final RegistryObject<Item> UNKNOWABLE_EGG9j = REGISTER.register("unknowable_egg", () -> new UnknowableEggItem(new Item.Properties().stacksTo(16).tab(MSItemGroup.MAIN).food(MSFoods.UNKNOWABLE_EGG)));
	public static final RegistryObject<Item> BREADCRUMBS9j = REGISTER.register("breadcrumbs", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.BREADCRUMBS)));
	
	//Other Land Items
	public static final RegistryObject<Item> GOLDEN_GRASSHOPPER9j = REGISTER.register("golden_grasshopper", () -> new Item(new Item.Properties().tab(MSItemGroup.LANDS)));
	public static final RegistryObject<Item> BUG_NET9j = REGISTER.register("bug_net", () -> new BugNetItem(new Item.Properties().defaultDurability(64).tab(MSItemGroup.LANDS)));
	public static final RegistryObject<Item> FROG9j = REGISTER.register("frog", () -> new FrogItem(new Item.Properties().stacksTo(1).tab(MSItemGroup.LANDS)));
	public static final RegistryObject<Item> CARVING_TOOL9j = REGISTER.register("carving_tool", () -> new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.LANDS)));
	public static final RegistryObject<MSArmorItem> CRUMPLY_HAT9j = REGISTER.register("crumply_hat", () -> new MSArmorItem(MSItemTypes.CLOTH_ARMOR, EquipmentSlot.HEAD, new Item.Properties().stacksTo(1).tab(MSItemGroup.LANDS)));
	public static final RegistryObject<Item> STONE_EYEBALLS9j = REGISTER.register("stone_eyeballs", () -> new Item(new Item.Properties().tab(MSItemGroup.LANDS)));
	//public static final RegistryObject<Item> STONE_SLAB9j = REGISTER.register("template", () -> new Item();
	//public static final RegistryObject<Item> SHOP_POSTER9j = REGISTER.register("template", () -> new Item(); //remove final //TODO figure out how entity/item works out
	//registry.register(new HangingItem((world, pos, facing, stack) -> new EntityShopPoster(world, pos, facing, stack, 0), new Item.Properties().maxStackSize(1).tab(ModItemGroup.LANDS)).setRegistryName("shop_poster"));
	
	//Buckets
	public static final RegistryObject<Item> OIL_BUCKET9j = REGISTER.register("oil_bucket", () -> new BucketItem(MSFluids.OIL, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> BLOOD_BUCKET9j = REGISTER.register("blood_bucket", () -> new BucketItem(MSFluids.BLOOD, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> BRAIN_JUICE_BUCKET9j = REGISTER.register("brain_juice_bucket", () -> new BucketItem(MSFluids.BRAIN_JUICE, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> WATER_COLORS_BUCKET9j = REGISTER.register("water_colors_bucket", () -> new BucketItem(MSFluids.WATER_COLORS, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> ENDER_BUCKET9j = REGISTER.register("ender_bucket", () -> new BucketItem(MSFluids.ENDER, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> LIGHT_WATER_BUCKET9j = REGISTER.register("light_water_bucket", () -> new BucketItem(MSFluids.LIGHT_WATER, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> OBSIDIAN_BUCKET9j = REGISTER.register("obsidian_bucket", () -> new ObsidianBucketItem(new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET).tab(MSItemGroup.MAIN)));
	
	//Alchemy Items
	public static final RegistryObject<Item> DICE9j = REGISTER.register("dice", () -> new RightClickMessageItem(new Item.Properties().tab(MSItemGroup.MAIN), RightClickMessageItem.Type.DICE));
	public static final RegistryObject<Item> PLUTONIUM_CORE9j = REGISTER.register("plutonium_core", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> GRIMOIRE9j = REGISTER.register("grimoire", () -> new GrimoireItem(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> BATTERY9j = REGISTER.register("battery", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> BARBASOL9j = REGISTER.register("barbasol", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> CLOTHES_IRON9j = REGISTER.register("clothes_iron", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> INK_SQUID_PRO_QUO9j = REGISTER.register("ink_squid_pro_quo", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> CUEBALL9j = REGISTER.register("cueball", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> EIGHTBALL9j = REGISTER.register("eightball", () -> new RightClickMessageItem(new Item.Properties().tab(MSItemGroup.MAIN), RightClickMessageItem.Type.EIGHTBALL));
	public static final RegistryObject<Item> FLARP_MANUAL9j = REGISTER.register("flarp_manual", () -> new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.LANDS)));
	public static final RegistryObject<Item> SASSACRE_TEXT9j = REGISTER.register("sassacre_text", () -> new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.LANDS)));
	public static final RegistryObject<Item> WISEGUY9j = REGISTER.register("wiseguy", () -> new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.LANDS)));
	public static final RegistryObject<Item> TABLESTUCK_MANUAL9j = REGISTER.register("tablestuck_manual", () -> new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.LANDS)));
	public static final RegistryObject<Item> TILLDEATH_HANDBOOK9j = REGISTER.register("tilldeath_handbook", () -> new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.LANDS)));
	public static final RegistryObject<Item> BINARY_CODE9j = REGISTER.register("binary_code", () -> new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.LANDS)));
	public static final RegistryObject<Item> NONBINARY_CODE9j = REGISTER.register("nonbinary_code", () -> new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.LANDS)));
	public static final RegistryObject<Item> THRESH_DVD9j = REGISTER.register("thresh_dvd", () -> new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> GAMEBRO_MAGAZINE9j = REGISTER.register("gamebro_magazine", () -> new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> GAMEGRL_MAGAZINE9j = REGISTER.register("gamegrl_magazine", () -> new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> CREW_POSTER9j = REGISTER.register("crew_poster", () -> new HangingItem((world, pos, facing, stack) -> new CrewPosterEntity(world, pos, facing), new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> SBAHJ_POSTER9j = REGISTER.register("sbahj_poster", () -> new HangingItem((world, pos, facing, stack) -> new SbahjPosterEntity(world, pos, facing), new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> BI_DYE9j = REGISTER.register("bi_dye", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> LIP_BALM9j = REGISTER.register("lip_balm", () -> new RightClickMessageItem(new Item.Properties().tab(MSItemGroup.MAIN), RightClickMessageItem.Type.DEFAULT));
	public static final RegistryObject<Item> ELECTRIC_AUTOHARP9j = REGISTER.register("electric_autoharp", () -> new RightClickMusicItem(new Item.Properties().tab(MSItemGroup.MAIN), RightClickMusicItem.Type.ELECTRIC_AUTOHARP));
	public static final RegistryObject<Item> CARDBOARD_TUBE9j = REGISTER.register("cardboard_tube", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN)));
	
	//Other
	public static final RegistryObject<Item> CAPTCHAROID_CAMERA9j = REGISTER.register("captcharoid_camera", () -> new CaptcharoidCameraItem(new Item.Properties().defaultDurability(64).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> LONG_FORGOTTEN_WARHORN9j = REGISTER.register("long_forgotten_warhorn", () -> new LongForgottenWarhornItem(new Item.Properties().defaultDurability(100).tab(MSItemGroup.MAIN).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> BLACK_QUEENS_RING9j = REGISTER.register("black_queens_ring", () -> new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> WHITE_QUEENS_RING9j = REGISTER.register("white_queens_ring", () -> new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> BARBASOL_BOMB9j = REGISTER.register("barbasol_bomb", () -> new BarbasolBombItem(new Item.Properties().stacksTo(16).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> RAZOR_BLADE9j = REGISTER.register("razor_blade", () -> new RazorBladeItem(new Item.Properties().tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> ICE_SHARD9j = REGISTER.register("ice_shard", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> HORN9j = REGISTER.register("horn", () -> new SoundItem(() -> MSSoundEvents.ITEM_HORN_USE, new Item.Properties().tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> CAKE_MIX9j = REGISTER.register("cake_mix", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> TEMPLE_SCANNER9j = REGISTER.register("temple_scanner", () -> new StructureScannerItem(new Item.Properties().tab(MSItemGroup.MAIN).stacksTo(1), MSTags.ConfiguredFeatures.SCANNER_LOCATED, () -> MSItems.RAW_URANIUM9j));
	
	public static final RegistryObject<Item> SCALEMATE_APPLESCAB9j = REGISTER.register("scalemate_applescab", () -> new ScalemateItem(new Item.Properties().tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> SCALEMATE_BERRYBREATH9j = REGISTER.register("scalemate_berrybreath", () -> new ScalemateItem(new Item.Properties()));
	public static final RegistryObject<Item> SCALEMATE_CINNAMONWHIFF9j = REGISTER.register("scalemate_cinnamonwhiff", () -> new ScalemateItem(new Item.Properties()));
	public static final RegistryObject<Item> SCALEMATE_HONEYTONGUE9j = REGISTER.register("scalemate_honeytongue", () -> new ScalemateItem(new Item.Properties()));
	public static final RegistryObject<Item> SCALEMATE_LEMONSNOUT9j = REGISTER.register("scalemate_lemonsnout", () -> new ScalemateItem(new Item.Properties()));
	public static final RegistryObject<Item> SCALEMATE_PINESNOUT9j = REGISTER.register("scalemate_pinesnout", () -> new ScalemateItem(new Item.Properties()));
	public static final RegistryObject<Item> SCALEMATE_PUCEFOOT9j = REGISTER.register("scalemate_pucefoot", () -> new ScalemateItem(new Item.Properties()));
	public static final RegistryObject<Item> SCALEMATE_PUMPKINSNUFFLE9j = REGISTER.register("scalemate_pumpkinsnuffle", () -> new ScalemateItem(new Item.Properties()));
	public static final RegistryObject<Item> SCALEMATE_PYRALSPITE9j = REGISTER.register("scalemate_pyralspite", () -> new ScalemateItem(new Item.Properties()));
	public static final RegistryObject<Item> SCALEMATE_WITNESS9j = REGISTER.register("scalemate_witness", () -> new ScalemateItem(new Item.Properties()));
	
	//Incredibly Useful Items
	public static final RegistryObject<Item> URANIUM_POWERED_STICK9j = REGISTER.register("uranium_powered_stick", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN).stacksTo(1)));
	public static final RegistryObject<Item> IRON_BOAT9j = REGISTER.register("iron_boat", () -> new CustomBoatItem((stack, world, x, y, z) -> new MetalBoatEntity(world, x, y, z, MetalBoatEntity.Type.IRON), new Item.Properties().tab(MSItemGroup.MAIN).stacksTo(1)));
	public static final RegistryObject<Item> GOLD_BOAT9j = REGISTER.register("gold_boat", () -> new CustomBoatItem((stack, world, x, y, z) -> new MetalBoatEntity(world, x, y, z, MetalBoatEntity.Type.GOLD), new Item.Properties().tab(MSItemGroup.MAIN).stacksTo(1)));
	public static final RegistryObject<Item> COCOA_WART9j = REGISTER.register("cocoa_wart", () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN)));
	
	//Music Discs
	public static final RegistryObject<Item> MUSIC_DISC_EMISSARY_OF_DANCE9j = REGISTER.register("music_disc_emissary_of_dance", () -> new RecordItem(1, () -> MSSoundEvents.MUSIC_DISC_EMISSARY_OF_DANCE, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> MUSIC_DISC_DANCE_STAB_DANCE9j = REGISTER.register("music_disc_dance_stab_dance", () -> new RecordItem(2, () -> MSSoundEvents.MUSIC_DISC_DANCE_STAB_DANCE, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> MUSIC_DISC_RETRO_BATTLE9j = REGISTER.register("music_disc_retro_battle", () -> new RecordItem(3, () -> MSSoundEvents.MUSIC_DISC_RETRO_BATTLE_THEME, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).tab(MSItemGroup.MAIN)));
	//Cassettes
	public static final RegistryObject<Item> CASSETTE_139j = REGISTER.register("cassette_13", () -> new CassetteItem(1, () -> SoundEvents.MUSIC_DISC_13, EnumCassetteType.THIRTEEN, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> CASSETTE_CAT9j = REGISTER.register("cassette_cat", () -> new CassetteItem(2, () -> SoundEvents.MUSIC_DISC_CAT, EnumCassetteType.CAT, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> CASSETTE_BLOCKS9j = REGISTER.register("cassette_blocks", () -> new CassetteItem(3, () -> SoundEvents.MUSIC_DISC_BLOCKS, EnumCassetteType.BLOCKS, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> CASSETTE_CHIRP9j = REGISTER.register("cassette_chirp", () -> new CassetteItem(4, () -> SoundEvents.MUSIC_DISC_CHIRP, EnumCassetteType.CHIRP, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> CASSETTE_FAR9j = REGISTER.register("cassette_far", () -> new CassetteItem(5, () -> SoundEvents.MUSIC_DISC_FAR, EnumCassetteType.FAR, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> CASSETTE_MALL9j = REGISTER.register("cassette_mall", () -> new CassetteItem(6, () -> SoundEvents.MUSIC_DISC_MALL, EnumCassetteType.MALL, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> CASSETTE_MELLOHI9j = REGISTER.register("cassette_mellohi", () -> new CassetteItem(7, () -> SoundEvents.MUSIC_DISC_MELLOHI, EnumCassetteType.MELLOHI, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> CASSETTE_DANCE_STAB9j = REGISTER.register("cassette_dance_stab", () -> new CassetteItem(2, () -> MSSoundEvents.MUSIC_DISC_DANCE_STAB_DANCE, EnumCassetteType.DANCE_STAB_DANCE, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> CASSETTE_RETRO_BATTLE9j = REGISTER.register("cassette_retro_battle", () -> new CassetteItem(3, () -> MSSoundEvents.MUSIC_DISC_RETRO_BATTLE_THEME, EnumCassetteType.RETRO_BATTLE_THEME, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).tab(MSItemGroup.MAIN)));
	public static final RegistryObject<Item> CASSETTE_EMISSARY9j = REGISTER.register("cassette_emissary", () -> new CassetteItem(1, () -> MSSoundEvents.MUSIC_DISC_EMISSARY_OF_DANCE, EnumCassetteType.EMISSARY_OF_DANCE, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).tab(MSItemGroup.MAIN)));
	/**/

	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T getNull()
	{
		return null;
	}
	
	@SubscribeEvent
	public static void registerItems(final RegistryEvent.Register<Item> event)
	{
		IForgeRegistry<Item> registry = event.getRegistry();
		
		registerItemBlock(registry, BLACK_CHESS_DIRT.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, WHITE_CHESS_DIRT.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, DARK_GRAY_CHESS_DIRT.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, LIGHT_GRAY_CHESS_DIRT.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, SKAIA_PORTAL.get(), new Item.Properties().tab(MSItemGroup.MAIN).rarity(Rarity.EPIC));
		
		registerItemBlock(registry, BLACK_CHESS_BRICKS.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, DARK_GRAY_CHESS_BRICKS.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, LIGHT_GRAY_CHESS_BRICKS.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, WHITE_CHESS_BRICKS.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, BLACK_CHESS_BRICK_SMOOTH.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, DARK_GRAY_CHESS_BRICK_SMOOTH.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, LIGHT_GRAY_CHESS_BRICK_SMOOTH.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, WHITE_CHESS_BRICK_SMOOTH.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, BLACK_CHESS_BRICK_TRIM.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, DARK_GRAY_CHESS_BRICK_TRIM.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, LIGHT_GRAY_CHESS_BRICK_TRIM.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, WHITE_CHESS_BRICK_TRIM.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, CHECKERED_STAINED_GLASS.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, BLACK_CROWN_STAINED_GLASS.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, BLACK_PAWN_STAINED_GLASS.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, WHITE_CROWN_STAINED_GLASS.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, WHITE_PAWN_STAINED_GLASS.get(), MSItemGroup.MAIN);
		
		registerItemBlock(registry, STONE_CRUXITE_ORE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, NETHERRACK_CRUXITE_ORE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, COBBLESTONE_CRUXITE_ORE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, SANDSTONE_CRUXITE_ORE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, RED_SANDSTONE_CRUXITE_ORE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, END_STONE_CRUXITE_ORE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, SHADE_STONE_CRUXITE_ORE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, PINK_STONE_CRUXITE_ORE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, STONE_URANIUM_ORE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, NETHERRACK_URANIUM_ORE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, COBBLESTONE_URANIUM_ORE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, SANDSTONE_URANIUM_ORE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, RED_SANDSTONE_URANIUM_ORE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, END_STONE_URANIUM_ORE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, SHADE_STONE_URANIUM_ORE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, PINK_STONE_URANIUM_ORE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, NETHERRACK_COAL_ORE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, SHADE_STONE_COAL_ORE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, PINK_STONE_COAL_ORE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, END_STONE_IRON_ORE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, SANDSTONE_IRON_ORE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, RED_SANDSTONE_IRON_ORE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, SANDSTONE_GOLD_ORE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, RED_SANDSTONE_GOLD_ORE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, SHADE_STONE_GOLD_ORE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, PINK_STONE_GOLD_ORE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, END_STONE_REDSTONE_ORE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, STONE_QUARTZ_ORE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, PINK_STONE_LAPIS_ORE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, PINK_STONE_DIAMOND_ORE.get(), MSItemGroup.LANDS);
		
		registerItemBlock(registry, CRUXITE_BLOCK.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, URANIUM_BLOCK.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, GENERIC_OBJECT.get(), MSItemGroup.MAIN);
		
		registerItemBlock(registry, BLUE_DIRT.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, THOUGHT_DIRT.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, COARSE_STONE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, CHISELED_COARSE_STONE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, COARSE_STONE_BRICKS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, COARSE_STONE_COLUMN.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, CHISELED_COARSE_STONE_BRICKS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, CRACKED_COARSE_STONE_BRICKS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, MOSSY_COARSE_STONE_BRICKS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, SHADE_STONE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, SMOOTH_SHADE_STONE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, SHADE_BRICKS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, SHADE_COLUMN.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, CHISELED_SHADE_BRICKS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, CRACKED_SHADE_BRICKS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, MOSSY_SHADE_BRICKS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, BLOOD_SHADE_BRICKS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, TAR_SHADE_BRICKS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, FROST_TILE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, CHISELED_FROST_TILE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, FROST_BRICKS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, FROST_COLUMN.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, CHISELED_FROST_BRICKS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, CRACKED_FROST_BRICKS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, FLOWERY_FROST_BRICKS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, CAST_IRON.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, CHISELED_CAST_IRON.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, STEEL_BEAM.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, MYCELIUM_COBBLESTONE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, MYCELIUM_STONE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, POLISHED_MYCELIUM_STONE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, MYCELIUM_BRICKS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, MYCELIUM_COLUMN.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, CHISELED_MYCELIUM_BRICKS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, CRACKED_MYCELIUM_BRICKS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, MOSSY_MYCELIUM_BRICKS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, FLOWERY_MYCELIUM_BRICKS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, BLACK_STONE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, POLISHED_BLACK_STONE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, BLACK_COBBLESTONE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, BLACK_STONE_BRICKS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, BLACK_STONE_COLUMN.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, CHISELED_BLACK_STONE_BRICKS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, CRACKED_BLACK_STONE_BRICKS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, BLACK_SAND.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, DECREPIT_STONE_BRICKS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, FLOWERY_MOSSY_COBBLESTONE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, MOSSY_DECREPIT_STONE_BRICKS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, FLOWERY_MOSSY_STONE_BRICKS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, COARSE_END_STONE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, END_GRASS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, CHALK.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, POLISHED_CHALK.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, CHALK_BRICKS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, CHALK_COLUMN.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, CHISELED_CHALK_BRICKS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, MOSSY_CHALK_BRICKS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, FLOWERY_CHALK_BRICKS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, PINK_STONE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, PINK_STONE_BRICKS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, CHISELED_PINK_STONE_BRICKS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, CRACKED_PINK_STONE_BRICKS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, MOSSY_PINK_STONE_BRICKS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, POLISHED_PINK_STONE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, PINK_STONE_COLUMN.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, BROWN_STONE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, BROWN_STONE_BRICKS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, BROWN_STONE_COLUMN.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, CRACKED_BROWN_STONE_BRICKS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, POLISHED_BROWN_STONE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, GREEN_STONE.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, GREEN_STONE_BRICKS.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, GREEN_STONE_COLUMN.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, POLISHED_GREEN_STONE.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, CHISELED_GREEN_STONE_BRICKS.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, HORIZONTAL_GREEN_STONE_BRICKS.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, VERTICAL_GREEN_STONE_BRICKS.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, GREEN_STONE_BRICK_TRIM.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, GREEN_STONE_BRICK_FROG.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, GREEN_STONE_BRICK_IGUANA_LEFT.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, GREEN_STONE_BRICK_IGUANA_RIGHT.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, GREEN_STONE_BRICK_LOTUS.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, GREEN_STONE_BRICK_NAK_LEFT.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, GREEN_STONE_BRICK_NAK_RIGHT.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, GREEN_STONE_BRICK_SALAMANDER_LEFT.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, GREEN_STONE_BRICK_SALAMANDER_RIGHT.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, GREEN_STONE_BRICK_SKAIA.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, GREEN_STONE_BRICK_TURTLE.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, SANDSTONE_COLUMN.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, CHISELED_SANDSTONE_COLUMN.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, RED_SANDSTONE_COLUMN.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, CHISELED_RED_SANDSTONE_COLUMN.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, UNCARVED_WOOD.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, CHIPBOARD.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, WOOD_SHAVINGS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, DENSE_CLOUD.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, BRIGHT_DENSE_CLOUD.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, SUGAR_CUBE.get(), MSItemGroup.LANDS);
		
		registerItemBlock(registry, GLOWING_LOG.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, FROST_LOG.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, RAINBOW_LOG.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, END_LOG.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, VINE_LOG.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, FLOWERY_VINE_LOG.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, DEAD_LOG.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, PETRIFIED_LOG.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, GLOWING_WOOD.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, FROST_WOOD.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, RAINBOW_WOOD.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, END_WOOD.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, VINE_WOOD.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, FLOWERY_VINE_WOOD.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, DEAD_WOOD.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, PETRIFIED_WOOD.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, GLOWING_PLANKS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, FROST_PLANKS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, RAINBOW_PLANKS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, END_PLANKS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, DEAD_PLANKS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, TREATED_PLANKS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, FROST_LEAVES.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, RAINBOW_LEAVES.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, END_LEAVES.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, RAINBOW_SAPLING.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, END_SAPLING.get(), MSItemGroup.LANDS);
		
		registerItemBlock(registry, BLOOD_ASPECT_LOG.get());
		registerItemBlock(registry, BREATH_ASPECT_LOG.get());
		registerItemBlock(registry, DOOM_ASPECT_LOG.get());
		registerItemBlock(registry, HEART_ASPECT_LOG.get());
		registerItemBlock(registry, HOPE_ASPECT_LOG.get());
		registerItemBlock(registry, LIFE_ASPECT_LOG.get());
		registerItemBlock(registry, LIGHT_ASPECT_LOG.get());
		registerItemBlock(registry, MIND_ASPECT_LOG.get());
		registerItemBlock(registry, RAGE_ASPECT_LOG.get());
		registerItemBlock(registry, SPACE_ASPECT_LOG.get());
		registerItemBlock(registry, TIME_ASPECT_LOG.get());
		registerItemBlock(registry, VOID_ASPECT_LOG.get());
		registerItemBlock(registry, BLOOD_ASPECT_PLANKS.get());
		registerItemBlock(registry, BREATH_ASPECT_PLANKS.get());
		registerItemBlock(registry, DOOM_ASPECT_PLANKS.get());
		registerItemBlock(registry, HEART_ASPECT_PLANKS.get());
		registerItemBlock(registry, HOPE_ASPECT_PLANKS.get());
		registerItemBlock(registry, LIFE_ASPECT_PLANKS.get());
		registerItemBlock(registry, LIGHT_ASPECT_PLANKS.get());
		registerItemBlock(registry, MIND_ASPECT_PLANKS.get());
		registerItemBlock(registry, RAGE_ASPECT_PLANKS.get());
		registerItemBlock(registry, SPACE_ASPECT_PLANKS.get());
		registerItemBlock(registry, TIME_ASPECT_PLANKS.get());
		registerItemBlock(registry, VOID_ASPECT_PLANKS.get());
		registerItemBlock(registry, BLOOD_ASPECT_LEAVES.get());
		registerItemBlock(registry, BREATH_ASPECT_LEAVES.get());
		registerItemBlock(registry, DOOM_ASPECT_LEAVES.get());
		registerItemBlock(registry, HEART_ASPECT_LEAVES.get());
		registerItemBlock(registry, HOPE_ASPECT_LEAVES.get());
		registerItemBlock(registry, LIFE_ASPECT_LEAVES.get());
		registerItemBlock(registry, LIGHT_ASPECT_LEAVES.get());
		registerItemBlock(registry, MIND_ASPECT_LEAVES.get());
		registerItemBlock(registry, RAGE_ASPECT_LEAVES.get());
		registerItemBlock(registry, SPACE_ASPECT_LEAVES.get());
		registerItemBlock(registry, TIME_ASPECT_LEAVES.get());
		registerItemBlock(registry, VOID_ASPECT_LEAVES.get());
		registerItemBlock(registry, BLOOD_ASPECT_SAPLING.get(), new Item.Properties().rarity(Rarity.UNCOMMON));
		registerItemBlock(registry, BREATH_ASPECT_SAPLING.get(), new Item.Properties().rarity(Rarity.UNCOMMON));
		registerItemBlock(registry, DOOM_ASPECT_SAPLING.get(), new Item.Properties().rarity(Rarity.UNCOMMON));
		registerItemBlock(registry, HEART_ASPECT_SAPLING.get(), new Item.Properties().rarity(Rarity.UNCOMMON));
		registerItemBlock(registry, HOPE_ASPECT_SAPLING.get(), new Item.Properties().rarity(Rarity.UNCOMMON));
		registerItemBlock(registry, LIFE_ASPECT_SAPLING.get(), new Item.Properties().rarity(Rarity.UNCOMMON));
		registerItemBlock(registry, LIGHT_ASPECT_SAPLING.get(), new Item.Properties().rarity(Rarity.UNCOMMON));
		registerItemBlock(registry, MIND_ASPECT_SAPLING.get(), new Item.Properties().rarity(Rarity.UNCOMMON));
		registerItemBlock(registry, RAGE_ASPECT_SAPLING.get(), new Item.Properties().rarity(Rarity.UNCOMMON));
		registerItemBlock(registry, SPACE_ASPECT_SAPLING.get(), new Item.Properties().rarity(Rarity.UNCOMMON));
		registerItemBlock(registry, TIME_ASPECT_SAPLING.get(), new Item.Properties().rarity(Rarity.UNCOMMON));
		registerItemBlock(registry, VOID_ASPECT_SAPLING.get(), new Item.Properties().rarity(Rarity.UNCOMMON));
		
		registerItemBlock(registry, GLOWING_MUSHROOM.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, DESERT_BUSH.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, BLOOMING_CACTUS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, PETRIFIED_GRASS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, PETRIFIED_POPPY.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, STRAWBERRY.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, TALL_END_GRASS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, GLOWFLOWER.get(), MSItemGroup.LANDS);
		
		registerItemBlock(registry, GLOWY_GOOP.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, COAGULATED_BLOOD.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, PIPE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, PIPE_INTERSECTION.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, PARCEL_PYXIS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, PYXIS_LID.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, new StoneTabletItem(MSBlocks.STONE_SLAB.get(), new Item.Properties().tab(MSItemGroup.LANDS)));
		registerItemBlock(registry, NAKAGATOR_STATUE.get(), MSItemGroup.LANDS);
		
		registerItemBlock(registry, BLACK_CHESS_BRICK_STAIRS.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, DARK_GRAY_CHESS_BRICK_STAIRS.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, LIGHT_GRAY_CHESS_BRICK_STAIRS.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, WHITE_CHESS_BRICK_STAIRS.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, COARSE_STONE_STAIRS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, COARSE_STONE_BRICK_STAIRS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, SHADE_STAIRS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, SHADE_BRICK_STAIRS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, FROST_TILE_STAIRS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, FROST_BRICK_STAIRS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, CAST_IRON_STAIRS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, BLACK_STONE_STAIRS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, BLACK_STONE_BRICK_STAIRS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, MYCELIUM_STAIRS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, MYCELIUM_BRICK_STAIRS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, CHALK_STAIRS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, CHALK_BRICK_STAIRS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, FLOWERY_MOSSY_STONE_BRICK_STAIRS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, PINK_STONE_STAIRS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, PINK_STONE_BRICK_STAIRS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, BROWN_STONE_STAIRS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, BROWN_STONE_BRICK_STAIRS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, GREEN_STONE_STAIRS.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, GREEN_STONE_BRICK_STAIRS.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, RAINBOW_PLANKS_STAIRS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, END_PLANKS_STAIRS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, DEAD_PLANKS_STAIRS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, TREATED_PLANKS_STAIRS.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, STEEP_GREEN_STONE_BRICK_STAIRS_BASE.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, STEEP_GREEN_STONE_BRICK_STAIRS_TOP.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, BLACK_CHESS_BRICK_SLAB.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, DARK_GRAY_CHESS_BRICK_SLAB.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, LIGHT_GRAY_CHESS_BRICK_SLAB.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, WHITE_CHESS_BRICK_SLAB.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, COARSE_STONE_SLAB.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, COARSE_STONE_BRICK_SLAB.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, CHALK_SLAB.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, CHALK_BRICK_SLAB.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, PINK_STONE_SLAB.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, PINK_STONE_BRICK_SLAB.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, BROWN_STONE_SLAB.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, BROWN_STONE_BRICK_SLAB.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, GREEN_STONE_SLAB.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, GREEN_STONE_BRICK_SLAB.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, RAINBOW_PLANKS_SLAB.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, END_PLANKS_SLAB.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, DEAD_PLANKS_SLAB.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, TREATED_PLANKS_SLAB.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, BLACK_STONE_SLAB.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, BLACK_STONE_BRICK_SLAB.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, MYCELIUM_SLAB.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, MYCELIUM_BRICK_SLAB.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, FLOWERY_MOSSY_STONE_BRICK_SLAB.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, FROST_TILE_SLAB.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, FROST_BRICK_SLAB.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, SHADE_SLAB.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, SHADE_BRICK_SLAB.get(), MSItemGroup.LANDS);
		
		registerItemBlock(registry, TRAJECTORY_BLOCK.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, STAT_STORER.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, REMOTE_OBSERVER.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, WIRELESS_REDSTONE_TRANSMITTER.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, WIRELESS_REDSTONE_RECEIVER.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, SOLID_SWITCH.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, VARIABLE_SOLID_SWITCH.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, ONE_SECOND_INTERVAL_TIMED_SOLID_SWITCH.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, TWO_SECOND_INTERVAL_TIMED_SOLID_SWITCH.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, SUMMONER.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, AREA_EFFECT_BLOCK.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, PLATFORM_GENERATOR.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, PLATFORM_RECEPTACLE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, ITEM_MAGNET.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, REDSTONE_CLOCK.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, ROTATOR.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, TOGGLER.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, REMOTE_COMPARATOR.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, STRUCTURE_CORE.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, FALL_PAD.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, FRAGILE_STONE.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, SPIKES.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, RETRACTABLE_SPIKES.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, BLOCK_PRESSURE_PLATE.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, PUSHABLE_BLOCK.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, AND_GATE_BLOCK.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, OR_GATE_BLOCK.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, XOR_GATE_BLOCK.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, NAND_GATE_BLOCK.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, NOR_GATE_BLOCK.get(), MSItemGroup.LANDS);
		registerItemBlock(registry, XNOR_GATE_BLOCK.get(), MSItemGroup.LANDS);
		
		registry.register(new CruxtruderItem(MSBlocks.CRUXTRUDER, new Item.Properties().tab(MSItemGroup.MAIN)).setRegistryName("cruxtruder"));
		registerItemBlock(registry, CRUXTRUDER_LID.get(), MSItemGroup.MAIN);
		registry.register(new MultiblockItem(MSBlocks.TOTEM_LATHE, new Item.Properties().tab(MSItemGroup.MAIN)).setRegistryName("totem_lathe"));
		registry.register(new MultiblockItem(MSBlocks.ALCHEMITER, new Item.Properties().tab(MSItemGroup.MAIN)).setRegistryName("alchemiter"));
		registry.register(new MultiblockItem(MSBlocks.PUNCH_DESIGNIX, new Item.Properties().tab(MSItemGroup.MAIN)).setRegistryName("punch_designix"));
		registerItemBlock(registry, new MiniCruxtruderItem(MINI_CRUXTRUDER.get(), new Item.Properties().tab(MSItemGroup.MAIN)));
		registerItemBlock(registry, MINI_TOTEM_LATHE.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, MINI_ALCHEMITER.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, MINI_PUNCH_DESIGNIX.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, HOLOPAD.get(), MSItemGroup.MAIN);
		
		registerItemBlock(registry, COMPUTER.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, LAPTOP.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, CROCKERTOP.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, HUBTOP.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, LUNCHTOP.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, OLD_COMPUTER.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, new TransportalizerItem(TRANSPORTALIZER.get(), new Item.Properties().tab(MSItemGroup.MAIN)));
		registerItemBlock(registry, new TransportalizerItem(TRANS_PORTALIZER.get(), new Item.Properties().tab(MSItemGroup.MAIN)));
		registerItemBlock(registry, new SendificatorBlockItem(SENDIFICATOR.get(), new Item.Properties().tab(MSItemGroup.MAIN).stacksTo(1)));
		registerItemBlock(registry, GRIST_WIDGET.get(), new Item.Properties().tab(MSItemGroup.MAIN).rarity(Rarity.UNCOMMON));
		registerItemBlock(registry, URANIUM_COOKER.get(), MSItemGroup.MAIN);
		
		registerItemBlock(registry, new DowelItem(MSBlocks.CRUXITE_DOWEL.get(), new Item.Properties().tab(MSItemGroup.MAIN)));
		
		registerItemBlock(registry, GOLD_SEEDS.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, WOODEN_CACTUS.get(), MSItemGroup.MAIN);
		
		registerItemBlock(registry, new BlockItem(APPLE_CAKE.get(), new Item.Properties().tab(MSItemGroup.MAIN).stacksTo(1)));
		registerItemBlock(registry, new BlockItem(BLUE_CAKE.get(), new Item.Properties().tab(MSItemGroup.MAIN).stacksTo(1)));
		registerItemBlock(registry, new BlockItem(COLD_CAKE.get(), new Item.Properties().tab(MSItemGroup.MAIN).stacksTo(1)));
		registerItemBlock(registry, new BlockItem(RED_CAKE.get(), new Item.Properties().tab(MSItemGroup.MAIN).stacksTo(1)));
		registerItemBlock(registry, new BlockItem(HOT_CAKE.get(), new Item.Properties().tab(MSItemGroup.MAIN).stacksTo(1)));
		registerItemBlock(registry, new BlockItem(REVERSE_CAKE.get(), new Item.Properties().tab(MSItemGroup.MAIN).stacksTo(1)));
		registerItemBlock(registry, new BlockItem(FUCHSIA_CAKE.get(), new Item.Properties().tab(MSItemGroup.MAIN).stacksTo(1)));
		registerItemBlock(registry, new BlockItem(NEGATIVE_CAKE.get(), new Item.Properties().tab(MSItemGroup.MAIN).stacksTo(1)));
		registerItemBlock(registry, new BlockItem(CARROT_CAKE.get(), new Item.Properties().tab(MSItemGroup.MAIN).stacksTo(1)));
		
		registerItemBlock(registry, PRIMED_TNT.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, UNSTABLE_TNT.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, INSTANT_TNT.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, WOODEN_EXPLOSIVE_BUTTON.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, STONE_EXPLOSIVE_BUTTON.get(), MSItemGroup.MAIN);
		
		registerItemBlock(registry, BLENDER.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, CHESSBOARD.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, MINI_FROG_STATUE.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, MINI_WIZARD_STATUE.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, MINI_TYPHEUS_STATUE.get(), MSItemGroup.MAIN);
		registerItemBlock(registry, CASSETTE_PLAYER.get(), MSItemGroup.MAIN);
		
		registry.register(new MultiblockItem(MSBlocks.LOTUS_TIME_CAPSULE_BLOCK, new Item.Properties().tab(MSItemGroup.MAIN)).setRegistryName("lotus_time_capsule"));
		
		registerItemBlock(registry, GLOWYSTONE_DUST.get(), MSItemGroup.MAIN);
		
		//hammers
		/*
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 2, -2.8F).efficiency(1.0F).set(MSItemTypes.HAMMER_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("claw_hammer"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 4, -3.2F).efficiency(4.0F).set(MSItemTypes.HAMMER_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("sledge_hammer"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 4, -3.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("mailbox"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 5, -3.2F).efficiency(3.5F).set(MSItemTypes.HAMMER_TOOL), new Item.Properties().defaultDurability(450).tab(MSItemGroup.WEAPONS)).setRegistryName("blacksmith_hammer"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.POGO_TIER, 5, -3.2F).efficiency(2.0F).set(MSItemTypes.HAMMER_TOOL).set(PogoEffect.EFFECT_07).add(PogoEffect.EFFECT_07), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("pogo_hammer"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.POGO_TIER, 7, -3.2F).efficiency(2.0F).set(MSItemTypes.HAMMER_TOOL).set(PogoEffect.EFFECT_04).add(PogoEffect.EFFECT_04), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("wrinklefucker"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.BOOK_TIER, 11, -3.4F).efficiency(5.0F).set(MSItemTypes.HAMMER_TOOL), new Item.Properties().defaultDurability(1024).tab(MSItemGroup.WEAPONS)).setRegistryName("telescopic_sassacrusher"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 4, -3.0F).efficiency(1.0F).set(MSItemTypes.HAMMER_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("democratic_demolitioner"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 7, -3.2F).efficiency(8.0F).set(MSItemTypes.HAMMER_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("regi_hammer"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.DENIZEN_TIER, 7, -3.2F).efficiency(7.0F).set(MSItemTypes.HAMMER_TOOL).add(OnHitEffect.TIME_SLOWNESS_AOE).add(OnHitEffect.enemyPotionEffect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1))), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.RARE)).setRegistryName("fear_no_anvil"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 8, -3.2F).efficiency(12.0F).set(MSItemTypes.HAMMER_TOOL).add(OnHitEffect.setOnFire(25)), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)).setRegistryName("melt_masher"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 7, -3.2F).efficiency(9.0F).set(MSItemTypes.MULTI_TOOL).set(new FarmineEffect(Integer.MAX_VALUE, 200)).set(PogoEffect.EFFECT_07).add(PogoEffect.EFFECT_07), new Item.Properties().defaultDurability(6114).tab(MSItemGroup.WEAPONS)).setRegistryName("estrogen_empowered_everything_eradicator"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.SBAHJ_TIER, 9, -3.2F).efficiency(9.1F).set(MSItemTypes.HAMMER_TOOL).set(PogoEffect.EFFECT_02).add(PogoEffect.EFFECT_02, OnHitEffect.playSound(() -> MSSoundEvents.ITEM_EEEEEEEEEEEE_HIT, 1.5F, 1.0F)), new Item.Properties().defaultDurability(6114).tab(MSItemGroup.WEAPONS)).setRegistryName("eeeeeeeeeeee"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.ZILLY_TIER, 8, -3.2F).efficiency(15.0F).set(MSItemTypes.HAMMER_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.EPIC)).setRegistryName("zillyhoo_hammer"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.ZILLY_TIER, 6, -3.2F).efficiency(15.0F).set(MSItemTypes.HAMMER_TOOL).add(OnHitEffect.RANDOM_DAMAGE), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.RARE)).setRegistryName("popamatic_vrillyhoo"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.CORUNDUM_TIER, 9, -3.2F).efficiency(4.0F).set(MSItemTypes.HAMMER_TOOL).add(OnHitEffect.setOnFire(50)), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.RARE)).setRegistryName("scarlet_zillyhoo"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.WELSH_TIER, 8, -3.2F).efficiency(4.0F).set(MSItemTypes.HAMMER_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.EPIC)).setRegistryName("mwrthwl"));
		/**/
		
		//blades
		/*
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.SBAHJ_TIER, 3, -3.0F).efficiency(1.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SORD_DROP), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("sord"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.PAPER_TIER, 2, -2.4F).efficiency(3.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("paper_sword"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 3, -2.4F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SWEEP).set(ItemRightClickEffect.absorbFluid(() -> Blocks.WATER, () -> MSItems.WET_SWONGE9j)), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("swonge"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 3, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SWEEP).set(RightClickBlockEffect.placeFluid(() -> Blocks.WATER, () -> MSItems.SWONGE9j)).add(OnHitEffect.playSound(() -> SoundEvents.GUARDIAN_FLOP)), new Item.Properties()).setRegistryName("wet_swonge"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.STONE, 3, -2.4F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SWEEP).set(ItemRightClickEffect.absorbFluid(() -> Blocks.LAVA, () -> MSItems.WET_PUMORD9j)), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("pumord"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.STONE, 3, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SWEEP).set(RightClickBlockEffect.placeFluid(() -> Blocks.LAVA, () -> MSItems.PUMORD9j)).add(OnHitEffect.playSound(() -> SoundEvents.BUCKET_EMPTY_LAVA, 1.0F, 0.2F)).add(OnHitEffect.setOnFire(10)), new Item.Properties()).setRegistryName("wet_pumord"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.CACTUS_TIER, 3, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("cactaceae_cutlass"));    //The sword harvestTool is only used against webs, hence the high efficiency.
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.MEAT_TIER, 4, -2.4F).efficiency(5.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).setEating(FinishUseItemEffect.foodEffect(8, 1F)), new Item.Properties().defaultDurability(250).tab(MSItemGroup.WEAPONS)).setRegistryName("steak_sword"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.MEAT_TIER, 2, -2.4F).efficiency(5.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).setEating(FinishUseItemEffect.foodEffect(3, 0.8F, 75)), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("beef_sword"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.MEAT_TIER, 5, -2.4F).efficiency(5.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).setEating(FinishUseItemEffect.potionEffect(() -> new MobEffectInstance(MobEffects.WITHER, 100, 1), 0.9F), FinishUseItemEffect.foodEffect(4, 0.4F, 25)), new Item.Properties().defaultDurability(300).tab(MSItemGroup.WEAPONS)).setRegistryName("irradiated_steak_sword"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.PRISMARINE_TIER, 3, -2.4F).efficiency(1.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().defaultDurability(100).tab(MSItemGroup.WEAPONS)).setRegistryName("macuahuitl"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.ICE_TIER, 6, -2.4F).efficiency(1.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).add(OnHitEffect.ICE_SHARD), new Item.Properties().defaultDurability(200).tab(MSItemGroup.WEAPONS)).setRegistryName("frosty_macuahuitl"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 3, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("katana"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.ZILLY_TIER, 6, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().defaultDurability(-1).tab(MSItemGroup.WEAPONS).rarity(Rarity.RARE)).setRegistryName("unbreakable_katana"));    //Actually unbreakable
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 5, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).add(OnHitEffect.HOPE_RESISTANCE), new Item.Properties().defaultDurability(2048).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)).setRegistryName("angel_apocalypse"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 4, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).add(OnHitEffect.setOnFire(30)), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("fire_poker"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 3, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).add(OnHitEffect.setOnFire(10)), new Item.Properties().defaultDurability(350).tab(MSItemGroup.WEAPONS)).setRegistryName("too_hot_to_handle"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 7, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.RARE)).setRegistryName("caledscratch"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.WELSH_TIER, 6, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.EPIC)).setRegistryName("caledfwlch"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.DENIZEN_TIER, 7, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.EPIC)).setRegistryName("royal_deringer"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 5, -2.6F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().defaultDurability(600).tab(MSItemGroup.WEAPONS)).setRegistryName("claymore"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.ZILLY_TIER, 6, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.EPIC)).setRegistryName("cutlass_of_zillywair"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 5, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("regisword"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.DENIZEN_TIER, 4, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().defaultDurability(4096).tab(MSItemGroup.WEAPONS).rarity(Rarity.RARE)).setRegistryName("cruel_fate_crucible")); //Special property in ServerEventHandler
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.CORUNDUM_TIER, 7, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.RARE)).setRegistryName("scarlet_ribbitar"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 6, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().defaultDurability(1000).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)).setRegistryName("dogg_machete"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.GOLD, 7, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).add(OnHitEffect.setOnFire(30)), new Item.Properties().defaultDurability(300).tab(MSItemGroup.WEAPONS)).setRegistryName("cobalt_sabre"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.URANIUM_TIER, 4, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).add(OnHitEffect.onCrit(OnHitEffect.enemyPotionEffect(() -> new MobEffectInstance(MobEffects.WITHER, 100, 1)))), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)).setRegistryName("quantum_sabre"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 7, -2.4F).efficiency(15.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.RARE)).setRegistryName("shatter_beacon"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.MEAT_TIER, 7, -2.4F).efficiency(5.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).add(OnHitEffect.SORD_DROP), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)).setRegistryName("shatter_bacon"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.URANIUM_TIER, 6, -2.6F).efficiency(5.0F).set(MSItemTypes.SWORD_TOOL).add(OnHitEffect.SWEEP).add(OnHitEffect.setOnFire(30)), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("subtractshumidire_zomorrodnegative"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 0, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.backstab(3)), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("dagger"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.SBAHJ_TIER, 1, -2.0F).add(OnHitEffect.SORD_DROP), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("nife"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.EMERALD_TIER, 1, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.backstab(4)), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("light_of_my_knife"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.CORUNDUM_TIER, 1, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.backstab(9)), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("starshard_tri_blade"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 3, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.backstab(4)), new Item.Properties().defaultDurability(1200).tab(MSItemGroup.WEAPONS)).setRegistryName("toothripper"));
		/**/
		
		/*
		//axes
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.SBAHJ_TIER, 3, -3.5F).efficiency(1.0F).set(MSItemTypes.AXE_TOOL).add(OnHitEffect.SORD_DROP), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("batleacks"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.STONE, 5, -3.0F).efficiency(6.0F).disableShield().set(MSItemTypes.AXE_TOOL).set(new FarmineEffect(Integer.MAX_VALUE, 20)), new Item.Properties().defaultDurability(400).tab(MSItemGroup.WEAPONS)).setRegistryName("copse_crusher"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 7, -3.0F).efficiency(2.0F).disableShield().set(MSItemTypes.AXE_TOOL).setEating(FinishUseItemEffect.foodEffect(6, 0.6F, 75)), new Item.Properties().defaultDurability(500).tab(MSItemGroup.WEAPONS)).setRegistryName("quench_crusher"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 8, -3.0F).efficiency(3.0F).disableShield().set(MSItemTypes.AXE_TOOL).set(DestroyBlockEffect.extraHarvests(true, 0.6F, 20, () -> Items.MELON_SLICE, () -> Blocks.MELON)), new Item.Properties().defaultDurability(400).tab(MSItemGroup.WEAPONS)).setRegistryName("melonsbane"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 7, -3.0F).efficiency(3.0F).disableShield().set(MSItemTypes.AXE_TOOL).set(DestroyBlockEffect.DOUBLE_FARM), new Item.Properties().defaultDurability(800).tab(MSItemGroup.WEAPONS)).setRegistryName("crop_chop"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 9, -3.0F).efficiency(3.0F).disableShield().set(MSItemTypes.AXE_TOOL).set(DestroyBlockEffect.DOUBLE_FARM), new Item.Properties().defaultDurability(950).tab(MSItemGroup.WEAPONS)).setRegistryName("the_last_straw"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 8, -3.0F).efficiency(3.0F).disableShield().set(MSItemTypes.AXE_TOOL), new Item.Properties().defaultDurability(600).tab(MSItemGroup.WEAPONS)).setRegistryName("battleaxe"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.CANDY_TIER, 8, -3.0F).efficiency(2.0F).disableShield().set(MSItemTypes.AXE_TOOL).add(OnHitEffect.SET_CANDY_DROP_FLAG), new Item.Properties().defaultDurability(111).tab(MSItemGroup.WEAPONS)).setRegistryName("candy_battleaxe"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.CANDY_TIER, 9, -3.0F).efficiency(2.0F).disableShield().set(MSItemTypes.AXE_TOOL).setEating(FinishUseItemEffect.foodEffect(8, 0.4F)), new Item.Properties().defaultDurability(350).tab(MSItemGroup.WEAPONS)).setRegistryName("choco_loco_woodsplitter"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 8, -3.0F).efficiency(3.0F).disableShield().set(MSItemTypes.AXE_TOOL).add(OnHitEffect.SET_CANDY_DROP_FLAG), new Item.Properties().defaultDurability(800).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)).setRegistryName("steel_edge_candycutter"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.STONE, 8, -3.0F).efficiency(6.0F).disableShield().set(MSItemTypes.AXE_TOOL), new Item.Properties().defaultDurability(413).tab(MSItemGroup.WEAPONS)).setRegistryName("blacksmith_bane"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 6, -3.0F).disableShield().efficiency(6.0F).set(MSItemTypes.AXE_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("regiaxe"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 8, -3.0F).efficiency(6.0F).disableShield().set(MSItemTypes.AXE_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("gothy_axe"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 7, -3.0F).efficiency(6.0F).disableShield().set(MSItemTypes.AXE_TOOL).add(OnHitEffect.KUNDLER_SURPRISE), new Item.Properties().defaultDurability(600).tab(MSItemGroup.WEAPONS)).setRegistryName("surprise_axe"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 8, -3.0F).efficiency(6.0F).disableShield().set(MSItemTypes.AXE_TOOL).set(ItemRightClickEffect.switchTo(() -> SHOCK_AXE_UNPOWERED9j)).add(OnHitEffect.DROP_FOE_ITEM).add(InventoryTickEffect.DROP_WHEN_IN_WATER).add(OnHitEffect.playSound(() -> MSSoundEvents.EVENT_ELECTRIC_SHOCK, 0.6F, 1.0F)), new Item.Properties().defaultDurability(800).tab(MSItemGroup.WEAPONS)).setRegistryName("shock_axe"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 8, -3.0F).efficiency(6.0F).disableShield().set(MSItemTypes.AXE_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.SHOCK_AXE9j)), new Item.Properties().defaultDurability(800)).setRegistryName("shock_axe_unpowered"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 8, -3.0F).efficiency(7.0F).disableShield().set(MSItemTypes.AXE_TOOL), new Item.Properties().defaultDurability(500).tab(MSItemGroup.WEAPONS)).setRegistryName("scraxe"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 6, -3.0F).efficiency(7.0F).disableShield().set(MSItemTypes.AXE_TOOL).add(OnHitEffect.SPACE_TELEPORT), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)).setRegistryName("lorentz_distransformationer"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.POGO_TIER, 6, -3.0F).efficiency(2.0F).disableShield().set(MSItemTypes.AXE_HAMMER_TOOL).set(new FarmineEffect(Integer.MAX_VALUE, 50)).set(PogoEffect.EFFECT_06).add(PogoEffect.EFFECT_06), new Item.Properties().defaultDurability(800).tab(MSItemGroup.WEAPONS)).setRegistryName("piston_powered_pogo_axehammer"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.CORUNDUM_TIER, 8, -3.0F).efficiency(8.0F).disableShield().set(MSItemTypes.AXE_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.RARE)).setRegistryName("ruby_croak"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.CORUNDUM_TIER, 7, -3.0F).efficiency(9.0F).disableShield().set(MSItemTypes.AXE_TOOL).add(OnHitEffect.setOnFire(30)), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)).setRegistryName("hephaestus_lumberjack"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 7, -3.0F).efficiency(5.0F).disableShield().set(MSItemTypes.AXE_HAMMER_TOOL).set(new FarmineEffect(Integer.MAX_VALUE, 100)).set(PogoEffect.EFFECT_07).add(PogoEffect.EFFECT_07), new Item.Properties().defaultDurability(2048).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)).setRegistryName("fission_focused_fault_feller"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 9, -3.2F).efficiency(5.0F).disableShield().set(MSItemTypes.AXE_TOOL), new Item.Properties().defaultDurability(600).tab(MSItemGroup.WEAPONS)).setRegistryName("bisector"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 9, -3.2F).efficiency(1.0F).disableShield().set(MSItemTypes.AXE_TOOL), new Item.Properties().defaultDurability(8).tab(MSItemGroup.WEAPONS)).setRegistryName("fine_china_axe"));
		
		//misc weapons
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 4, -3.0F).efficiency(1.0F).add(OnHitEffect.RANDOM_DAMAGE), new Item.Properties().tab(MSItemGroup.WEAPONS).defaultDurability(4096).rarity(Rarity.EPIC)).setRegistryName("fluorite_octet"));
		
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 2, -1.5F).efficiency(10.0F).set(MSItemTypes.CLAWS_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.CAT_CLAWS_SHEATHED9j)), new Item.Properties().defaultDurability(500).tab(MSItemGroup.WEAPONS)).setRegistryName("cat_claws_drawn"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, -1, -1.0F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.CAT_CLAWS_DRAWN9j)), new Item.Properties().defaultDurability(500)).setRegistryName("cat_claws_sheathed"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 5, -1.5F).efficiency(10.0F).set(MSItemTypes.CLAWS_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.SKELETONIZER_SHEATHED9j)), new Item.Properties().defaultDurability(750).tab(MSItemGroup.WEAPONS)).setRegistryName("skeletonizer_drawn"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, -1, -1.0F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.SKELETONIZER_DRAWN9j)), new Item.Properties().defaultDurability(750)).setRegistryName("skeletonizer_sheathed"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 6, -1.5F).efficiency(10.0F).set(MSItemTypes.CLAWS_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.SKELETON_DISPLACER_SHEATHED9j)).add(OnHitEffect.targetSpecificAdditionalDamage(4, () -> EntityType.SKELETON)), new Item.Properties().defaultDurability(1250).tab(MSItemGroup.WEAPONS)).setRegistryName("skeleton_displacer_drawn"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, -1, -1.0F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.SKELETON_DISPLACER_DRAWN9j)), new Item.Properties().defaultDurability(1250)).setRegistryName("skeleton_displacer_sheathed"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.CORUNDUM_TIER, 4, -1.5F).efficiency(10.0F).set(MSItemTypes.CLAWS_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.TEARS_OF_THE_ENDERLICH_SHEATHED9j)).add(OnHitEffect.targetSpecificAdditionalDamage(6, () -> MSEntityTypes.LICH)), new Item.Properties().defaultDurability(2000).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)).setRegistryName("tears_of_the_enderlich_drawn"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.CORUNDUM_TIER, -4, -1.0F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.TEARS_OF_THE_ENDERLICH_DRAWN9j)), new Item.Properties().defaultDurability(2000).rarity(Rarity.UNCOMMON)).setRegistryName("tears_of_the_enderlich_sheathed"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 5, -1.5F).efficiency(10.0F).set(MSItemTypes.CLAWS_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.ACTION_CLAWS_SHEATHED9j)), new Item.Properties().defaultDurability(4096).tab(MSItemGroup.WEAPONS).rarity(Rarity.RARE)).setRegistryName("action_claws_drawn"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, -1, -1.0F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.ACTION_CLAWS_DRAWN9j)), new Item.Properties().defaultDurability(4096).rarity(Rarity.RARE)).setRegistryName("action_claws_sheathed"));
		
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 2, -1.5F).efficiency(10.0F).set(MSItemTypes.AXE_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.LIPSTICK9j)), new Item.Properties().defaultDurability(250).tab(MSItemGroup.WEAPONS)).setRegistryName("lipstick_chainsaw"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, -1, -0.5F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.LIPSTICK_CHAINSAW9j)), new Item.Properties().defaultDurability(250)).setRegistryName("lipstick"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 4, -1.0F).efficiency(2.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.THISTLEBLOWER_LIPSTICK9j)), new Item.Properties().defaultDurability(500).tab(MSItemGroup.WEAPONS)).setRegistryName("thistleblower"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, -1, -0.5F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.THISTLEBLOWER9j)), new Item.Properties().defaultDurability(500)).setRegistryName("thistleblower_lipstick"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.EMERALD_TIER, 3, -1.5F).efficiency(10.0F).set(MSItemTypes.AXE_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.EMERALD_IMMOLATOR_LIPSTICK9j)).add(OnHitEffect.setOnFire(5)), new Item.Properties().defaultDurability(1024).tab(MSItemGroup.WEAPONS)).setRegistryName("emerald_immolator"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, -1, -0.5F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.EMERALD_IMMOLATOR9j)), new Item.Properties().defaultDurability(1024)).setRegistryName("emerald_immolator_lipstick"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.ICE_TIER, 5, -1.5F).efficiency(10.0F).set(MSItemTypes.AXE_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.FROSTTOOTH_LIPSTICK9j)).add(OnHitEffect.ICE_SHARD), new Item.Properties().defaultDurability(1536).tab(MSItemGroup.WEAPONS)).setRegistryName("frosttooth"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, -1, -0.5F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.FROSTTOOTH9j)), new Item.Properties().defaultDurability(1536)).setRegistryName("frosttooth_lipstick"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.CORUNDUM_TIER, 4, -1.5F).efficiency(10.0F).set(MSItemTypes.AXE_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.OBSIDIATOR_LIPSTICK9j)), new Item.Properties().defaultDurability(2048).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)).setRegistryName("obsidiator"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, -1, -0.5F).efficiency(10.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.OBSIDIATOR9j)), new Item.Properties().defaultDurability(2048).rarity(Rarity.UNCOMMON)).setRegistryName("obsidiator_lipstick"));
		
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 4, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("jousting_lance"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 4, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)).setRegistryName("cigarette_lance"));
		
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 3, -2.5F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("lucerne_hammer"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 4, -2.5F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().defaultDurability(2048).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)).setRegistryName("lucerne_hammer_of_undying")); //Special property in ServerEventHandler
		
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.PRISMARINE_TIER, 2, -2.0F).efficiency(1.5F).set(MSItemTypes.MISC_TOOL), new Item.Properties().durability(100).tab(MSItemGroup.WEAPONS)).setRegistryName("obsidian_axe_knife"));
		
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.PAPER_TIER, 1, -1.0F).efficiency(1.5F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.extinguishFire(1)).add(OnHitEffect.enemyKnockback(1.5F)), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("fan"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.DENIZEN_TIER, 2, -1.0F).efficiency(1.5F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.extinguishFire(3)).add(OnHitEffect.BREATH_LEVITATION_AOE).add(OnHitEffect.enemyKnockback(2.0F)), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.RARE)).setRegistryName("typhonic_trivializer"));
		
		//sickles
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 2, -2.2F).efficiency(1.5F).disableShield().set(MSItemTypes.SICKLE_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("sickle"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 2, -2.2F).efficiency(1.5F).disableShield().set(MSItemTypes.SICKLE_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("bisickle"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.SBAHJ_TIER, 3, -3.0F).efficiency(1.0F).set(MSItemTypes.SICKLE_TOOL).add(OnHitEffect.SORD_DROP), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("ow_the_edge"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.CACTUS_TIER, 4, -2.2F).efficiency(1.0F).disableShield().set(MSItemTypes.SICKLE_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("thorny_subject"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 4, -2.2F).efficiency(3.0F).disableShield().set(MSItemTypes.SICKLE_TOOL), new Item.Properties().defaultDurability(400).tab(MSItemGroup.WEAPONS)).setRegistryName("homes_smell_ya_later"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 5, -2.2F).efficiency(1.0F).disableShield().set(MSItemTypes.SICKLE_TOOL), new Item.Properties().defaultDurability(550).tab(MSItemGroup.WEAPONS)).setRegistryName("hemeoreaper"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.CANDY_TIER, 5, -2.2F).efficiency(1.0F).disableShield().set(MSItemTypes.SICKLE_TOOL).setEating(FinishUseItemEffect.foodEffect(7, 0.6F)), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("fudgesickle"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 4, -2.2F).efficiency(4.0F).disableShield().set(MSItemTypes.SICKLE_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("regisickle"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.GOLD, 9, -2.2F).efficiency(4.0F).disableShield().set(MSItemTypes.SICKLE_TOOL), new Item.Properties().defaultDurability(1024).tab(MSItemGroup.WEAPONS)).setRegistryName("hereticus_aururm"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 8, -2.2F).efficiency(4.0F).disableShield().set(MSItemTypes.SICKLE_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("claw_sickle"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.HORRORTERROR_TIER, 6, -2.2F).efficiency(4.0F).disableShield().set(MSItemTypes.SICKLE_TOOL).add(OnHitEffect.HORRORTERROR), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)).setRegistryName("claw_of_nrubyiglith"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.CANDY_TIER, 6, -2.2F).efficiency(2.5F).disableShield().set(MSItemTypes.SICKLE_TOOL).add(OnHitEffect.SET_CANDY_DROP_FLAG), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("candy_sickle"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 4, -2.6F).efficiency(1.5F).disableShield().set(MSItemTypes.SICKLE_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("scythe"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 3, -2.6F).efficiency(1.5F).disableShield().set(MSItemTypes.SICKLE_TOOL).add(OnHitEffect.RANDOM_DAMAGE).set(ItemRightClickEffect.EIGHTBALL), new Item.Properties().defaultDurability(600).tab(MSItemGroup.WEAPONS)).setRegistryName("eightball_scythe"));
		
		//clubs
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 3, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("deuce_club"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 3, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SPAWN_BREADCRUMBS).setEating(FinishUseItemEffect.SPAWN_BREADCRUMBS, FinishUseItemEffect.foodEffect(3, 0.2F)), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("stale_baguette"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.MEAT_TIER, 4, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.playSound(() -> SoundEvents.GUARDIAN_FLOP)), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("glub_club"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 1, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("night_club"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 2, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().defaultDurability(2500).tab(MSItemGroup.WEAPONS)).setRegistryName("nightstick"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 6, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.enemyPotionEffect(() -> new MobEffectInstance(MobEffects.POISON, 140, 0))), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("red_eyes"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.PRISMARINE_TIER, 3, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("prismarine_basher"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.ICE_TIER, 5, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.ICE_SHARD), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("club_zero"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.POGO_TIER, 4, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).set(PogoEffect.EFFECT_05).add(PogoEffect.EFFECT_05), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("pogo_club"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 3, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().defaultDurability(350).tab(MSItemGroup.WEAPONS)).setRegistryName("barber_basher"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 4, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("metal_bat"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 5, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.RAGE_STRENGTH, OnHitEffect.playSound(() -> MSSoundEvents.ITEM_HORN_USE, 1.5F, 1)), new Item.Properties().defaultDurability(2048).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)).setRegistryName("clown_club"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 5, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().defaultDurability(100).tab(MSItemGroup.WEAPONS)).setRegistryName("spiked_club"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 5, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().defaultDurability(500).tab(MSItemGroup.WEAPONS)).setRegistryName("mace"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 6, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().defaultDurability(750).tab(MSItemGroup.WEAPONS)).setRegistryName("m_ace"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 1, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.armorBypassingDamageMod(4, EnumAspect.VOID)), new Item.Properties().defaultDurability(2048).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)).setRegistryName("desolator_mace"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 7, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.setOnFire(35)), new Item.Properties().defaultDurability(750).tab(MSItemGroup.WEAPONS)).setRegistryName("blazing_glory"));
		
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 7, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.ACE_OF_SPADES9j)), new Item.Properties().defaultDurability(500).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)).setRegistryName("horse_hitcher"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.PAPER_TIER, 0, -1.8F).efficiency(0.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.HORSE_HITCHER9j)), new Item.Properties().defaultDurability(500)).setRegistryName("ace_of_spades"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 7, -2.8F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.ACE_OF_CLUBS9j)), new Item.Properties().defaultDurability(500).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)).setRegistryName("club_of_felony"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.PAPER_TIER, 0, -1.8F).efficiency(0.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.CLUB_OF_FELONY9j)), new Item.Properties().defaultDurability(500)).setRegistryName("ace_of_clubs"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 5, -2.0F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.ACE_OF_DIAMONDS9j)), new Item.Properties().defaultDurability(500).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)).setRegistryName("cuestick"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.PAPER_TIER, 0, -1.8F).efficiency(0.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.CUESTICK9j)), new Item.Properties().defaultDurability(500)).setRegistryName("ace_of_diamonds"));
		registry.register(new Item(new Item.Properties().defaultDurability(500)).setRegistryName("ace_of_hearts"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 8, -2.8F).efficiency(4.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.summonFireball()), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.EPIC)).setRegistryName("white_kings_scepter"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 8, -2.8F).efficiency(4.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.summonFireball()), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.EPIC)).setRegistryName("black_kings_scepter"));
		
		
		//canes
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 2, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().defaultDurability(100).tab(MSItemGroup.WEAPONS)).setRegistryName("cane"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 2, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().defaultDurability(150).tab(MSItemGroup.WEAPONS)).setRegistryName("vaudeville_hook"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 3, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.notAtPlayer(OnHitEffect.enemyPotionEffect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 140, 1)))), new Item.Properties().defaultDurability(150).tab(MSItemGroup.WEAPONS)).setRegistryName("bear_poking_stick"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 6, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.playSound(() -> SoundEvents.ANVIL_PLACE)), new Item.Properties().defaultDurability(-1).tab(MSItemGroup.WEAPONS).rarity(Rarity.EPIC)).setRegistryName("crowbar"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 2, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(InventoryTickEffect.BREATH_SLOW_FALLING), new Item.Properties().defaultDurability(350).tab(MSItemGroup.WEAPONS)).setRegistryName("umbrella"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 3, -2.0F).efficiency(2.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SPAWN_BREADCRUMBS).setEating(FinishUseItemEffect.SPAWN_BREADCRUMBS, FinishUseItemEffect.foodEffect(4, 0.5F)), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("upper_crust_crust_cane"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 2, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("iron_cane"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 3, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(PropelEffect.BREATH_PROPEL), new Item.Properties().tab(MSItemGroup.WEAPONS).defaultDurability(2048).rarity(Rarity.UNCOMMON)).setRegistryName("zephyr_cane"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 3, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("spear_cane"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 3, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("paradises_portabello"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 4, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("regi_cane"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.POGO_TIER, 2, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(PogoEffect.EFFECT_06).add(PogoEffect.EFFECT_06), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("pogo_cane"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.CANDY_TIER, 3, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SET_CANDY_DROP_FLAG).setEating(FinishUseItemEffect.foodEffect(2, 0.3F), FinishUseItemEffect.SHARPEN_CANDY_CANE), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("candy_cane"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.CANDY_TIER, 5, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SET_CANDY_DROP_FLAG), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("sharp_candy_cane"));
		
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 3, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("prim_and_proper_walking_pole"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 3, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.LESS_PROPER_WALKING_STICK_SHEATHED9j)), new Item.Properties().defaultDurability(600).tab(MSItemGroup.WEAPONS)).setRegistryName("less_proper_walking_stick"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 3, -2.0F).efficiency(1.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.LESS_PROPER_WALKING_STICK9j)), new Item.Properties().defaultDurability(600)).setRegistryName("less_proper_walking_stick_sheathed"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 5, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.ROCKEFELLERS_WALKING_BLADECANE_SHEATHED9j)), new Item.Properties().defaultDurability(800).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)).setRegistryName("rockefellers_walking_bladecane"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 5, -2.0F).efficiency(1.0F).set(ItemRightClickEffect.switchTo(() -> MSItems.ROCKEFELLERS_WALKING_BLADECANE9j)), new Item.Properties().defaultDurability(800).rarity(Rarity.UNCOMMON)).setRegistryName("rockefellers_walking_bladecane_sheathed"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 7, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.DRAGON_CANE_UNSHEATHED9j)), new Item.Properties().defaultDurability(2048).tab(MSItemGroup.WEAPONS).rarity(Rarity.RARE)).setRegistryName("dragon_cane"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 6, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.DRAGON_CANE9j)), new Item.Properties().defaultDurability(2048).rarity(Rarity.RARE)).setRegistryName("dragon_cane_unsheathed"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 6, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.CHANCEWYRMS_EXTRA_FORTUNATE_STABBING_IMPLEMENT_UNSHEATHED9j)), new Item.Properties().defaultDurability(4096).tab(MSItemGroup.WEAPONS).rarity(Rarity.RARE)).setRegistryName("chancewyrms_extra_fortunate_stabbing_implement"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 5, -2.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.CHANCEWYRMS_EXTRA_FORTUNATE_STABBING_IMPLEMENT9j)).add(OnHitEffect.RANDOM_DAMAGE), new Item.Properties().defaultDurability(4096).rarity(Rarity.RARE)).setRegistryName("chancewyrms_extra_fortunate_stabbing_implement_unsheathed"));
		
		//spoons/forks
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 2, -2.4F).efficiency(1.0F).set(MSItemTypes.SHOVEL_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("wooden_spoon"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 1, -2.4F).efficiency(1.0F).set(MSItemTypes.SHOVEL_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("silver_spoon"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 2, -2.4F).efficiency(1.0F).set(MSItemTypes.SHOVEL_TOOL).set(RightClickBlockEffect.scoopBlock(() -> Blocks.MELON)), new Item.Properties().tab(MSItemGroup.WEAPONS).defaultDurability(500)).setRegistryName("melonballer"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 6, -2.4F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("sightseeker"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 3, -2.4F).efficiency(10.0F).set(MSItemTypes.SHOVEL_TOOL).set(new FarmineEffect(Integer.MAX_VALUE, 50)), new Item.Properties().defaultDurability(1024).tab(MSItemGroup.WEAPONS)).setRegistryName("terrain_flatenator"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 5, -2.4F).efficiency(1.0F).set(MSItemTypes.SHOVEL_TOOL).add(OnHitEffect.LIFE_SATURATION), new Item.Properties().defaultDurability(2048).tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)).setRegistryName("nosferatu_spoon"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 5, -2.4F).efficiency(1.0F).set(MSItemTypes.SHOVEL_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.CROCKER_FORK9j)), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)).setRegistryName("crocker_spoon"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 6, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(() -> MSItems.CROCKER_SPOON9j)), new Item.Properties().rarity(Rarity.UNCOMMON)).setRegistryName("crocker_fork"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 9, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.RARE)).setRegistryName("skaia_fork"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.STONE, 3, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("fork"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.CANDY_TIER, 5, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.SET_CANDY_DROP_FLAG), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("candy_fork"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 3, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.playSound(() -> SoundEvents.NOTE_BLOCK_CHIME)), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("tuning_fork"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.DIAMOND, 3, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("eating_fork_gem"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 6, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.DROP_FOE_ITEM).add(InventoryTickEffect.DROP_WHEN_IN_WATER).add(OnHitEffect.playSound(() -> MSSoundEvents.EVENT_ELECTRIC_SHOCK, 0.6F, 1.0F)), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("electric_fork"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.URANIUM_TIER, 6, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.setOnFire(35)), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("devil_fork"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.STONE, 4, -2.5F).efficiency(1.0F).set(MSItemTypes.SHOVEL_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("spork"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.GOLD, 5, -2.5F).efficiency(1.0F).set(MSItemTypes.SHOVEL_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("golden_spork"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 7, -2.9F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("bident"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 7, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(() -> EDISONS_SERENITY9j)).add(OnHitEffect.DROP_FOE_ITEM).add(InventoryTickEffect.DROP_WHEN_IN_WATER).add(OnHitEffect.playSound(() -> MSSoundEvents.EVENT_ELECTRIC_SHOCK, 0.6F, 1.0F)), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("edisons_fury"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 7, -2.6F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(ItemRightClickEffect.switchTo(() -> EDISONS_FURY9j)), new Item.Properties()).setRegistryName("edisons_serenity"));
		
		//needles/wands
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.WOOD, 0, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("pointy_stick"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, -1, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("knitting_needle"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.SBAHJ_TIER, 1, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(MagicAttackRightClickEffect.SBAHJ_AIMBOT_MAGIC), new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("artifucker"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, -1, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(MagicAttackRightClickEffect.AIMBOT_MAGIC), new Item.Properties().defaultDurability(512).tab(MSItemGroup.WEAPONS)).setRegistryName("pointer_wand"));
		registry.register(new WeaponItem(new WeaponItem.Builder(Tiers.IRON, 0, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(MagicAttackRightClickEffect.STANDARD_MAGIC), new Item.Properties().defaultDurability(1024).tab(MSItemGroup.WEAPONS)).setRegistryName("needle_wand"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, -1, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(MagicAttackRightClickEffect.POOL_CUE_MAGIC), new Item.Properties().defaultDurability(1250).tab(MSItemGroup.WEAPONS)).setRegistryName("pool_cue_wand"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.HORRORTERROR_TIER, 0, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).add(OnHitEffect.HORRORTERROR).set(MagicAttackRightClickEffect.HORRORTERROR_MAGIC), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.UNCOMMON)).setRegistryName("thorn_of_oglogoth"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.ZILLY_TIER, 0, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(MagicAttackRightClickEffect.ZILLY_MAGIC), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.EPIC)).setRegistryName("thistle_of_zillywich"));
		registry.register(new WeaponItem(new WeaponItem.Builder(MSItemTypes.DENIZEN_TIER, -1, -1.0F).efficiency(1.0F).set(MSItemTypes.MISC_TOOL).set(MagicAttackRightClickEffect.ECHIDNA_MAGIC), new Item.Properties().tab(MSItemGroup.WEAPONS).rarity(Rarity.EPIC)).setRegistryName("quill_of_echidna"));
		
		//projectiles
		registry.register(new ConsumableProjectileWeaponItem(new Item.Properties().tab(MSItemGroup.WEAPONS), 0.5F, 20.0F, 1).setRegistryName("sbahjarang"));
		registry.register(new ConsumableProjectileWeaponItem(new Item.Properties().tab(MSItemGroup.WEAPONS), 1.0F, 2.8F, 2).setRegistryName("shuriken"));
		registry.register(new ConsumableProjectileWeaponItem(new Item.Properties().tab(MSItemGroup.WEAPONS), 1.5F, 2.4F, 3).setRegistryName("clubs_suitarang"));
		registry.register(new ConsumableProjectileWeaponItem(new Item.Properties().tab(MSItemGroup.WEAPONS), 1.5F, 2.4F, 3).setRegistryName("diamonds_suitarang"));
		registry.register(new ConsumableProjectileWeaponItem(new Item.Properties().tab(MSItemGroup.WEAPONS), 1.5F, 2.4F,3).setRegistryName("hearts_suitarang"));
		registry.register(new ConsumableProjectileWeaponItem(new Item.Properties().tab(MSItemGroup.WEAPONS), 1.5F, 2.4F,3).setRegistryName("spades_suitarang"));
		registry.register(new ReturningProjectileWeaponItem(new Item.Properties().tab(MSItemGroup.WEAPONS).durability(250), 1.3F, 1.0F,5, 30).setRegistryName("chakram"));
		registry.register(new ReturningProjectileWeaponItem(new Item.Properties().tab(MSItemGroup.WEAPONS).durability(2048), 1.5F, 0.6F,12, 20).setRegistryName("umbral_infiltrator"));
		registry.register(new BouncingProjectileWeaponItem(new Item.Properties().tab(MSItemGroup.WEAPONS).durability(250), 1.5F, 1.0F, 5, 20).setRegistryName("sorcerers_pinball"));
		
		registry.register(new SwordItem(MSItemTypes.EMERALD_TIER, 3, -2.4F, new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("emerald_sword"));
		registry.register(new AxeItem(MSItemTypes.EMERALD_TIER, 5, -3.0F, new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("emerald_axe"));
		registry.register(new PickaxeItem(MSItemTypes.EMERALD_TIER, 1, -2.8F, new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("emerald_pickaxe"));
		registry.register(new ShovelItem(MSItemTypes.EMERALD_TIER, 1.5F, -3.0F, new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("emerald_shovel"));
		registry.register(new HoeItem(MSItemTypes.EMERALD_TIER, -3, 0.0F, new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("emerald_hoe"));
		registry.register(new PickaxeItem(Tiers.DIAMOND, 1, -2.8F, new Item.Properties()/*.tab(MSItemGroup.WEAPONS)*/).setRegistryName("mine_and_grist"));
		
		//armor
		registry.register(new ArmorItem(MSItemTypes.PRISMARINE_ARMOR, EquipmentSlot.HEAD, new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("prismarine_helmet"));
		registry.register(new ArmorItem(MSItemTypes.PRISMARINE_ARMOR, EquipmentSlot.CHEST, new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("prismarine_chestplate"));
		registry.register(new ArmorItem(MSItemTypes.PRISMARINE_ARMOR, EquipmentSlot.LEGS, new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("prismarine_leggings"));
		registry.register(new ArmorItem(MSItemTypes.PRISMARINE_ARMOR, EquipmentSlot.FEET, new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("prismarine_boots"));
		registry.register(new ArmorItem(MSItemTypes.IRON_LASS_ARMOR, EquipmentSlot.HEAD, new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("iron_lass_glasses"));
		registry.register(new ArmorItem(MSItemTypes.IRON_LASS_ARMOR, EquipmentSlot.CHEST, new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("iron_lass_chestplate"));
		registry.register(new ArmorItem(MSItemTypes.IRON_LASS_ARMOR, EquipmentSlot.LEGS, new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("iron_lass_skirt"));
		registry.register(new ArmorItem(MSItemTypes.IRON_LASS_ARMOR, EquipmentSlot.FEET, new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("iron_lass_shoes"));
		
		registry.register(new MSArmorItem(MSItemTypes.DREAM_PAJAMAS, EquipmentSlot.HEAD, new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("prospit_circlet"));
		registry.register(new MSArmorItem(MSItemTypes.DREAM_PAJAMAS, EquipmentSlot.CHEST, new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("prospit_shirt"));
		registry.register(new MSArmorItem(MSItemTypes.DREAM_PAJAMAS, EquipmentSlot.LEGS, new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("prospit_pants"));
		registry.register(new MSArmorItem(MSItemTypes.DREAM_PAJAMAS, EquipmentSlot.FEET, new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("prospit_shoes"));
		registry.register(new MSArmorItem(MSItemTypes.DREAM_PAJAMAS, EquipmentSlot.HEAD, new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("derse_circlet"));
		registry.register(new MSArmorItem(MSItemTypes.DREAM_PAJAMAS, EquipmentSlot.CHEST, new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("derse_shirt"));
		registry.register(new MSArmorItem(MSItemTypes.DREAM_PAJAMAS, EquipmentSlot.LEGS, new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("derse_pants"));
		registry.register(new MSArmorItem(MSItemTypes.DREAM_PAJAMAS, EquipmentSlot.FEET, new Item.Properties().tab(MSItemGroup.WEAPONS)).setRegistryName("derse_shoes"));
		
		//core items
		registry.register(new BoondollarsItem(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)).setRegistryName("boondollars"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.MAIN)).setRegistryName("raw_cruxite"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.MAIN)).setRegistryName("raw_uranium"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.MAIN)).setRegistryName("energy_core"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.MAIN)).setRegistryName("plutonium_core"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.MAIN)).setRegistryName("sburb_code"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.MAIN)).setRegistryName("computer_parts"));
		//have to fix Cruxite artifact classes
		
		registry.register(new CruxiteAppleItem(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN).rarity(Rarity.UNCOMMON)).setRegistryName("cruxite_apple"));
		registry.register(new CruxitePotionItem(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN).rarity(Rarity.UNCOMMON)).setRegistryName("cruxite_potion"));
		
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.MAIN)).setRegistryName("blank_disk"));
		registry.register(new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)).setRegistryName("client_disk"));
		registry.register(new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)).setRegistryName("server_disk"));
		registry.register(new CaptchaCardItem(new Item.Properties().tab(MSItemGroup.MAIN)).setRegistryName("captcha_card"));
		registry.register(new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)).setRegistryName("stack_modus_card"));
		registry.register(new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)).setRegistryName("queue_modus_card"));
		registry.register(new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)).setRegistryName("queuestack_modus_card"));
		registry.register(new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)).setRegistryName("tree_modus_card"));
		registry.register(new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)).setRegistryName("hashmap_modus_card"));
		registry.register(new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)).setRegistryName("set_modus_card"));
		registry.register(new ShuntItem(new Item.Properties().stacksTo(1)).setRegistryName("shunt"));
		
		//food
		registry.register(new HealingFoodItem(4F, new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.PHLEGM_GUSHERS)).setRegistryName("phlegm_gushers"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.SORROW_GUSHERS)).setRegistryName("sorrow_gushers"));
		
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.BUG_ON_A_STICK)).setRegistryName("bug_on_a_stick"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.LANDS).food(MSFoods.CHOCOLATE_BEETLE)).setRegistryName("chocolate_beetle"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.LANDS).food(MSFoods.CONE_OF_FLIES)).setRegistryName("cone_of_flies"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.LANDS).food(MSFoods.GRASSHOPPER)).setRegistryName("grasshopper"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.LANDS).food(MSFoods.CICADA)).setRegistryName("cicada"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.LANDS).food(MSFoods.JAR_OF_BUGS)).setRegistryName("jar_of_bugs"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.LANDS).food(MSFoods.BUG_MAC)).setRegistryName("bug_mac"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.LANDS).food(MSFoods.ONION)).setRegistryName("onion"));
		registry.register(new BowlFoodItem(new Item.Properties().tab(MSItemGroup.LANDS).food(MSFoods.SALAD).stacksTo(1)).setRegistryName("salad"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.LANDS).food(MSFoods.DESERT_FRUIT)).setRegistryName("desert_fruit"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.LANDS)).setRegistryName("rock_cookie"));	//Not actually food, but let's pretend it is
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.LANDS).food(MSFoods.WOODEN_CARROT)).setRegistryName("wooden_carrot"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.LANDS).food(MSFoods.FUNGAL_SPORE)).setRegistryName("fungal_spore"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.LANDS).food(MSFoods.SPOREO)).setRegistryName("sporeo"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.LANDS).food(MSFoods.MOREL_MUSHROOM)).setRegistryName("morel_mushroom"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.LANDS)).setRegistryName("sushroom"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.LANDS).food(MSFoods.FRENCH_FRY)).setRegistryName("french_fry"));
		registry.register(new ItemNameBlockItem(STRAWBERRY_STEM.get(), new Item.Properties().tab(MSItemGroup.LANDS).food(MSFoods.STRAWBERRY_CHUNK)).setRegistryName("strawberry_chunk"));
		registry.register(new Item(new Item.Properties().stacksTo(16).tab(MSItemGroup.MAIN).food(MSFoods.FOOD_CAN)).setRegistryName("food_can"));
		
		
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.CANDY_CORN)).setRegistryName("candy_corn"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.TUIX_BAR)).setRegistryName("tuix_bar"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.BUILD_GUSHERS)).setRegistryName("build_gushers"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.AMBER_GUMMY_WORM)).setRegistryName("amber_gummy_worm"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.CAULK_PRETZEL)).setRegistryName("caulk_pretzel"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.CHALK_CANDY_CIGARETTE)).setRegistryName("chalk_candy_cigarette"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.IODINE_LICORICE)).setRegistryName("iodine_licorice"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.SHALE_PEEP)).setRegistryName("shale_peep"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.TAR_LICORICE)).setRegistryName("tar_licorice"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.COBALT_GUM)).setRegistryName("cobalt_gum"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.MARBLE_JAWBREAKER)).setRegistryName("marble_jawbreaker"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.MERCURY_SIXLETS)).setRegistryName("mercury_sixlets"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.QUARTZ_JELLY_BEAN)).setRegistryName("quartz_jelly_bean"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.SULFUR_CANDY_APPLE)).setRegistryName("sulfur_candy_apple"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.AMETHYST_HARD_CANDY)).setRegistryName("amethyst_hard_candy"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.GARNET_TWIX)).setRegistryName("garnet_twix"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.RUBY_LOLLIPOP)).setRegistryName("ruby_lollipop"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.RUST_GUMMY_EYE)).setRegistryName("rust_gummy_eye"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.DIAMOND_MINT)).setRegistryName("diamond_mint"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.GOLD_CANDY_RIBBON)).setRegistryName("gold_candy_ribbon"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.URANIUM_GUMMY_BEAR)).setRegistryName("uranium_gummy_bear"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.ARTIFACT_WARHEAD).rarity(Rarity.UNCOMMON)).setRegistryName("artifact_warhead"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.ZILLIUM_SKITTLES).rarity(Rarity.UNCOMMON)).setRegistryName("zillium_skittles"));
		registry.register(new DrinkableItem(new Item.Properties().stacksTo(16).tab(MSItemGroup.MAIN).food(MSFoods.APPLE_JUICE)).setRegistryName("apple_juice"));
		registry.register(new DrinkableItem(new Item.Properties().stacksTo(16).tab(MSItemGroup.MAIN).food(MSFoods.TAB)).setRegistryName("tab"));
		registry.register(new DrinkableItem(new Item.Properties().stacksTo(16).tab(MSItemGroup.MAIN).food(MSFoods.FAYGO)).setRegistryName("orange_faygo"));
		registry.register(new DrinkableItem(new Item.Properties().stacksTo(16).tab(MSItemGroup.MAIN).food(MSFoods.FAYGO_CANDY_APPLE)).setRegistryName("candy_apple_faygo"));
		registry.register(new DrinkableItem(new Item.Properties().stacksTo(16).tab(MSItemGroup.MAIN).food(MSFoods.FAYGO_COLA)).setRegistryName("faygo_cola"));
		registry.register(new DrinkableItem(new Item.Properties().stacksTo(16).tab(MSItemGroup.MAIN).food(MSFoods.FAYGO_COTTON_CANDY)).setRegistryName("cotton_candy_faygo"));
		registry.register(new DrinkableItem(new Item.Properties().stacksTo(16).tab(MSItemGroup.MAIN).food(MSFoods.FAYGO_CREME)).setRegistryName("creme_soda_faygo"));
		registry.register(new DrinkableItem(new Item.Properties().stacksTo(16).tab(MSItemGroup.MAIN).food(MSFoods.FAYGO_GRAPE)).setRegistryName("grape_faygo"));
		registry.register(new DrinkableItem(new Item.Properties().stacksTo(16).tab(MSItemGroup.MAIN).food(MSFoods.FAYGO_MOON_MIST)).setRegistryName("moon_mist_faygo"));
		registry.register(new DrinkableItem(new Item.Properties().stacksTo(16).tab(MSItemGroup.MAIN).food(MSFoods.FAYGO_PEACH)).setRegistryName("peach_faygo"));
		registry.register(new DrinkableItem(new Item.Properties().stacksTo(16).tab(MSItemGroup.MAIN).food(MSFoods.FAYGO_REDPOP)).setRegistryName("redpop_faygo"));
		registry.register(new DrinkableItem(new Item.Properties().stacksTo(16).tab(MSItemGroup.LANDS).food(MSFoods.GRUB_SAUCE)).setRegistryName("grub_sauce"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.IRRADIATED_STEAK)).setRegistryName("irradiated_steak"));
		registry.register(new SurpriseEmbryoItem(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.SURPRISE_EMBRYO)).setRegistryName("surprise_embryo"));
		registry.register(new UnknowableEggItem(new Item.Properties().stacksTo(16).tab(MSItemGroup.MAIN).food(MSFoods.UNKNOWABLE_EGG)).setRegistryName("unknowable_egg"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.MAIN).food(MSFoods.BREADCRUMBS)).setRegistryName("breadcrumbs"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.LANDS)).setRegistryName("golden_grasshopper"));
		registry.register(new BugNetItem(new Item.Properties().defaultDurability(64).tab(MSItemGroup.LANDS)).setRegistryName("bug_net"));
		registry.register(new FrogItem(new Item.Properties().stacksTo(1).tab(MSItemGroup.LANDS)).setRegistryName("frog"));
		registry.register(new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.LANDS)).setRegistryName("carving_tool"));
		registry.register(new MSArmorItem(MSItemTypes.CLOTH_ARMOR, EquipmentSlot.HEAD, new Item.Properties().stacksTo(1).tab(MSItemGroup.LANDS)).setRegistryName("crumply_hat"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.LANDS)).setRegistryName("stone_eyeballs"));
		//registry.register(new HangingItem((world, pos, facing, stack) -> new EntityShopPoster(world, pos, facing, stack, 0), new Item.Properties().maxStackSize(1).tab(ModItemGroup.LANDS)).setRegistryName("shop_poster"));
		
		registry.register(new BucketItem(MSFluids.OIL, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(MSItemGroup.MAIN)).setRegistryName("oil_bucket"));
		registry.register(new BucketItem(MSFluids.BLOOD, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(MSItemGroup.MAIN)).setRegistryName("blood_bucket"));
		registry.register(new BucketItem(MSFluids.BRAIN_JUICE, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(MSItemGroup.MAIN)).setRegistryName("brain_juice_bucket"));
		registry.register(new BucketItem(MSFluids.WATER_COLORS, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(MSItemGroup.MAIN)).setRegistryName("water_colors_bucket"));
		registry.register(new BucketItem(MSFluids.ENDER, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(MSItemGroup.MAIN)).setRegistryName("ender_bucket"));
		registry.register(new BucketItem(MSFluids.LIGHT_WATER, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(MSItemGroup.MAIN)).setRegistryName("light_water_bucket"));
		registry.register(new ObsidianBucketItem(new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET).tab(MSItemGroup.MAIN)).setRegistryName("obsidian_bucket"));
		registry.register(new CaptcharoidCameraItem(new Item.Properties().defaultDurability(64).tab(MSItemGroup.MAIN)).setRegistryName("captcharoid_camera"));
		registry.register(new GrimoireItem(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN).rarity(Rarity.UNCOMMON)).setRegistryName("grimoire"));
		registry.register(new LongForgottenWarhornItem(new Item.Properties().defaultDurability(100).tab(MSItemGroup.MAIN).rarity(Rarity.UNCOMMON)).setRegistryName("long_forgotten_warhorn"));
		registry.register(new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)).setRegistryName("black_queens_ring"));
		registry.register(new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)).setRegistryName("white_queens_ring"));
		registry.register(new BarbasolBombItem(new Item.Properties().stacksTo(16).tab(MSItemGroup.MAIN)).setRegistryName("barbasol_bomb"));
		registry.register(new RazorBladeItem(new Item.Properties().tab(MSItemGroup.MAIN)).setRegistryName("razor_blade"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.MAIN)).setRegistryName("ice_shard"));
		registry.register(new SoundItem(() -> MSSoundEvents.ITEM_HORN_USE, new Item.Properties().tab(MSItemGroup.MAIN)).setRegistryName("horn"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.MAIN)).setRegistryName("cake_mix"));
		registry.register(new StructureScannerItem(new Item.Properties().tab(MSItemGroup.MAIN).stacksTo(1), MSTags.ConfiguredFeatures.SCANNER_LOCATED, () -> MSItems.RAW_URANIUM9j).setRegistryName("temple_scanner"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.MAIN)).setRegistryName("battery"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.MAIN)).setRegistryName("barbasol"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.MAIN)).setRegistryName("clothes_iron"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.MAIN)).setRegistryName("ink_squid_pro_quo"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.MAIN)).setRegistryName("cardboard_tube"));
		registry.register(new RightClickMessageItem(new Item.Properties().tab(MSItemGroup.MAIN), RightClickMessageItem.Type.DICE).setRegistryName("dice"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.MAIN)).setRegistryName("cueball"));
		registry.register(new RightClickMessageItem(new Item.Properties().tab(MSItemGroup.MAIN), RightClickMessageItem.Type.EIGHTBALL).setRegistryName("eightball"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.MAIN).stacksTo(1)).setRegistryName("uranium_powered_stick"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.MAIN)).setRegistryName("cocoa_wart"));
		registry.register(new ScalemateItem(new Item.Properties().tab(MSItemGroup.MAIN)).setRegistryName("scalemate_applescab"));
		registry.register(new ScalemateItem(new Item.Properties()).setRegistryName("scalemate_berrybreath"));
		registry.register(new ScalemateItem(new Item.Properties()).setRegistryName("scalemate_cinnamonwhiff"));
		registry.register(new ScalemateItem(new Item.Properties()).setRegistryName("scalemate_honeytongue"));
		registry.register(new ScalemateItem(new Item.Properties()).setRegistryName("scalemate_lemonsnout"));
		registry.register(new ScalemateItem(new Item.Properties()).setRegistryName("scalemate_pinesnout"));
		registry.register(new ScalemateItem(new Item.Properties()).setRegistryName("scalemate_pucefoot"));
		registry.register(new ScalemateItem(new Item.Properties()).setRegistryName("scalemate_pumpkinsnuffle"));
		registry.register(new ScalemateItem(new Item.Properties()).setRegistryName("scalemate_pyralspite"));
		registry.register(new ScalemateItem(new Item.Properties()).setRegistryName("scalemate_witness"));
		registry.register(new CustomBoatItem((stack, world, x, y, z) -> new MetalBoatEntity(world, x, y, z, MetalBoatEntity.Type.IRON), new Item.Properties().tab(MSItemGroup.MAIN).stacksTo(1)).setRegistryName("iron_boat"));
		registry.register(new CustomBoatItem((stack, world, x, y, z) -> new MetalBoatEntity(world, x, y, z, MetalBoatEntity.Type.GOLD), new Item.Properties().tab(MSItemGroup.MAIN).stacksTo(1)).setRegistryName("gold_boat"));
		registry.register(new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.LANDS)).setRegistryName("flarp_manual"));
		registry.register(new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.LANDS)).setRegistryName("sassacre_text"));
		registry.register(new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.LANDS)).setRegistryName("wiseguy"));
		registry.register(new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.LANDS)).setRegistryName("tablestuck_manual"));
		registry.register(new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.LANDS)).setRegistryName("tilldeath_handbook"));
		registry.register(new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.LANDS)).setRegistryName("binary_code"));
		registry.register(new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.LANDS)).setRegistryName("nonbinary_code"));
		registry.register(new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)).setRegistryName("thresh_dvd"));
		registry.register(new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)).setRegistryName("gamebro_magazine"));
		registry.register(new Item(new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)).setRegistryName("gamegrl_magazine"));
		registry.register(new HangingItem((world, pos, facing, stack) -> new CrewPosterEntity(world, pos, facing), new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)).setRegistryName("crew_poster"));
		registry.register(new HangingItem((world, pos, facing, stack) -> new SbahjPosterEntity(world, pos, facing), new Item.Properties().stacksTo(1).tab(MSItemGroup.MAIN)).setRegistryName("sbahj_poster"));
		registry.register(new Item(new Item.Properties().tab(MSItemGroup.MAIN)).setRegistryName("bi_dye"));
		registry.register(new RightClickMessageItem(new Item.Properties().tab(MSItemGroup.MAIN), RightClickMessageItem.Type.DEFAULT).setRegistryName("lip_balm"));
		registry.register(new RightClickMusicItem(new Item.Properties().tab(MSItemGroup.MAIN), RightClickMusicItem.Type.ELECTRIC_AUTOHARP).setRegistryName("electric_autoharp"));
		
		//Music disks
		registry.register(new RecordItem(1, () -> MSSoundEvents.MUSIC_DISC_EMISSARY_OF_DANCE, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).tab(MSItemGroup.MAIN)).setRegistryName("music_disc_emissary_of_dance"));
		registry.register(new RecordItem(2, () -> MSSoundEvents.MUSIC_DISC_DANCE_STAB_DANCE, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).tab(MSItemGroup.MAIN)).setRegistryName("music_disc_dance_stab_dance"));
		registry.register(new RecordItem(3, () -> MSSoundEvents.MUSIC_DISC_RETRO_BATTLE_THEME, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).tab(MSItemGroup.MAIN)).setRegistryName("music_disc_retro_battle"));
		//Cassettes
		registry.register(new CassetteItem(1, () -> SoundEvents.MUSIC_DISC_13, EnumCassetteType.THIRTEEN, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).tab(MSItemGroup.MAIN)).setRegistryName("cassette_13"));
		registry.register(new CassetteItem(2, () -> SoundEvents.MUSIC_DISC_CAT, EnumCassetteType.CAT, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).tab(MSItemGroup.MAIN)).setRegistryName("cassette_cat"));
		registry.register(new CassetteItem(3, () -> SoundEvents.MUSIC_DISC_BLOCKS, EnumCassetteType.BLOCKS, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).tab(MSItemGroup.MAIN)).setRegistryName("cassette_blocks"));
		registry.register(new CassetteItem(4, () -> SoundEvents.MUSIC_DISC_CHIRP, EnumCassetteType.CHIRP, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).tab(MSItemGroup.MAIN)).setRegistryName("cassette_chirp"));
		registry.register(new CassetteItem(5, () -> SoundEvents.MUSIC_DISC_FAR, EnumCassetteType.FAR, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).tab(MSItemGroup.MAIN)).setRegistryName("cassette_far"));
		registry.register(new CassetteItem(6, () -> SoundEvents.MUSIC_DISC_MALL, EnumCassetteType.MALL, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).tab(MSItemGroup.MAIN)).setRegistryName("cassette_mall"));
		registry.register(new CassetteItem(7, () -> SoundEvents.MUSIC_DISC_MELLOHI, EnumCassetteType.MELLOHI, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).tab(MSItemGroup.MAIN)).setRegistryName("cassette_mellohi"));
		registry.register(new CassetteItem(1, () -> MSSoundEvents.MUSIC_DISC_EMISSARY_OF_DANCE, EnumCassetteType.EMISSARY_OF_DANCE, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).tab(MSItemGroup.MAIN)).setRegistryName("cassette_emissary"));
		registry.register(new CassetteItem(2, () -> MSSoundEvents.MUSIC_DISC_DANCE_STAB_DANCE, EnumCassetteType.DANCE_STAB_DANCE, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).tab(MSItemGroup.MAIN)).setRegistryName("cassette_dance_stab"));
		registry.register(new CassetteItem(3, () -> MSSoundEvents.MUSIC_DISC_RETRO_BATTLE_THEME, EnumCassetteType.RETRO_BATTLE_THEME, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).tab(MSItemGroup.MAIN)).setRegistryName("cassette_retro_battle"));
		/**/
	}
	
	/*
	private static Item registerItemBlock(Block block)
	{
		return registerItemBlock(new BlockItem(block, new Item.Properties()));
	}
	
	private static Item registerItemBlock(Block block, CreativeModeTab tab)
	{
		return registerItemBlock(new BlockItem(block, new Item.Properties().tab(tab)));
	}
	
	private static Item registerItemBlock(Block block, Item.Properties properties)
	{
		return registerItemBlock(new BlockItem(block, properties));
	}
	
	private static Item registerItemBlock(BlockItem item)
	{
		if(item.getBlock().getRegistryName() == null)
			throw new IllegalArgumentException(String.format("The provided itemblock %s has a block without a registry name!", item.getBlock()));
		REGISTER.register(item.getBlock().getRegistryName().toString(), () -> new Item(new Item.Properties().tab(MSItemGroup.MAIN)));
		return item;
	}
	/**/

	/**/
	private static Item registerItemBlock(IForgeRegistry<Item> registry, Block block)
	{
		return registerItemBlock(registry, new BlockItem(block, new Item.Properties()));
	}
	
	private static Item registerItemBlock(IForgeRegistry<Item> registry, Block block, CreativeModeTab tab)
	{
		return registerItemBlock(registry, new BlockItem(block, new Item.Properties().tab(tab)));
	}
	
	private static Item registerItemBlock(IForgeRegistry<Item> registry, Block block, Item.Properties properties)
	{
		return registerItemBlock(registry, new BlockItem(block, properties));
	}
	
	private static Item registerItemBlock(IForgeRegistry<Item> registry, BlockItem item)
	{
		if(item.getBlock().getRegistryName() == null)
			throw new IllegalArgumentException(String.format("The provided itemblock %s has a block without a registry name!", item.getBlock()));
		registry.register(item.setRegistryName(item.getBlock().getRegistryName()));
		return item;
	}
	/**/
	
	/*
	private static Item registerItem(ResourceLocation pKey, Item pItem) {
		return REGISTER.register(pKey, pItem);
	}*/
	
	/*
	////////////////////////
	private static Item registerBlock(BlockItem pItem) {
		return registerBlock(pItem.getBlock(), pItem);
	}
	
	/////////////////////////
	protected static Item registerBlock(Block pBlock, Item pItem) {
		return registerItem(Registry.BLOCK.getKey(pBlock), pItem);
	}
	
	//////////////////////////
	private static Item registerItem(String pKey, Item pItem) {
		return registerItem(new ResourceLocation(pKey), pItem);
	}
	
	//////////////////////
	private static Item registerItem(ResourceLocation pKey, Item pItem) {
		if (pItem instanceof BlockItem) {
			((BlockItem)pItem).registerBlocks(Item.BY_BLOCK, pItem);
		}
		
		return Registry.register(Registry.ITEM, pKey, pItem);
	}*/
}
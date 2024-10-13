package com.mraof.minestuck.item;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.weapon.MSToolType;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.SimpleTier;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class MSItemTypes
{
	public static final ItemAbility GRIST_HARVEST = ItemAbility.get("grist_harvest");
	
	public static final Tier SBAHJ_TIER = new SimpleTier(MSTags.Blocks.INCORRECT_FOR_SBAHJ_TOOL, 59, 0.0F, -1.0F, 0, () -> Ingredient.EMPTY);
	public static final Tier PAPER_TIER = new SimpleTier(MSTags.Blocks.INCORRECT_FOR_PAPER_TOOL, 65, 0.0F, 0.0F, 20, () -> Ingredient.of(Items.PAPER));
	public static final Tier ORGANIC_TIER = new SimpleTier(MSTags.Blocks.INCORRECT_FOR_ORGANIC_TOOL, 450, 2.5F, 0.0F, 10, () -> Ingredient.EMPTY);
	public static final Tier MEAT_TIER = new SimpleTier(MSTags.Blocks.INCORRECT_FOR_MEAT_TOOL, 175, 3.0F, 0.0F, 5, () -> Ingredient.EMPTY);
	public static final Tier CANDY_TIER = new SimpleTier(MSTags.Blocks.INCORRECT_FOR_CANDY_TOOL, 450, 4.0F, 0.0F, 15, () -> Ingredient.EMPTY);
	public static final Tier BOOK_TIER = new SimpleTier(MSTags.Blocks.INCORRECT_FOR_BOOK_TOOL, 250, 5.0F, 0.0F, 15, () -> Ingredient.of(Items.BOOK));
	public static final Tier CACTUS_TIER = new SimpleTier(MSTags.Blocks.INCORRECT_FOR_CACTUS_TOOL, 104, 2.0F, 1.0F, 5, () -> Ingredient.of(Blocks.CACTUS));
	public static final Tier ICE_TIER = new SimpleTier(MSTags.Blocks.INCORRECT_FOR_ICE_TOOL, 60, 11.0F, 1.0F, 25, () -> Ingredient.of(MSItems.ICE_SHARD.get()));
	public static final Tier POGO_TIER = new SimpleTier(MSTags.Blocks.INCORRECT_FOR_POGO_TOOL, 450, 2.0F, 2.0F, 8, () -> Ingredient.of(Items.SLIME_BALL));
	public static final Tier EMERALD_TIER = new SimpleTier(MSTags.Blocks.INCORRECT_FOR_EMERALD_TOOL, 1024, 10.0F, 2.0F, 20, () -> Ingredient.of(Items.EMERALD));
	public static final Tier PRISMARINE_TIER = new SimpleTier(MSTags.Blocks.INCORRECT_FOR_PRISMARINE_TOOL, 300, 4.0F, 3.0F, 10, () -> Ingredient.of(Items.PRISMARINE_SHARD));
	public static final Tier CORUNDUM_TIER = new SimpleTier(MSTags.Blocks.INCORRECT_FOR_CORUNDUM_TOOL, 1536, 6.0F, 3.0F, 16, () -> Ingredient.EMPTY);
	public static final Tier REGI_TIER = new SimpleTier(MSTags.Blocks.INCORRECT_FOR_REGI_TOOL, 3072, 13.0F, 3.0F, 10, () -> Ingredient.EMPTY);
	public static final Tier HORRORTERROR_TIER = new SimpleTier(MSTags.Blocks.INCORRECT_FOR_HORRORTERROR_TOOL, 2048, 4.0F, 4.0F, 15, () -> Ingredient.EMPTY);
	public static final Tier URANIUM_TIER = new SimpleTier(MSTags.Blocks.INCORRECT_FOR_URANIUM_TOOL, 512, 5.0F, 4.0F, 5, () -> Ingredient.of(MSItems.RAW_URANIUM.get()));
	public static final Tier BATTERY_TIER = new SimpleTier(MSTags.Blocks.INCORRECT_FOR_BATTERY_TOOL, 250, 6.0F, 4.0F, 14, () -> Ingredient.of(MSItems.BATTERY.get()));
	public static final Tier DENIZEN_TIER = new SimpleTier(MSTags.Blocks.INCORRECT_FOR_DENIZEN_TOOL, 4096, 14.0F, 4.0F, 25, () -> Ingredient.EMPTY);
	public static final Tier ZILLY_TIER = new SimpleTier(MSTags.Blocks.INCORRECT_FOR_ZILLY_TOOL, 5120, 12.0F, 5.0F, 30, () -> Ingredient.EMPTY);
	public static final Tier WELSH_TIER = new SimpleTier(MSTags.Blocks.INCORRECT_FOR_WELSH_TOOL, 5120, 15.0F, 5.0F, 25, () -> Ingredient.of(MSItems.CUEBALL.get()));
	
	public static final Holder<ArmorMaterial> PRISMARINE_ARMOR = registerArmorMaterial("prismarine",
			Map.of(ArmorItem.Type.BOOTS, 3, ArmorItem.Type.LEGGINGS, 6, ArmorItem.Type.CHESTPLATE, 7, ArmorItem.Type.HELMET, 2),
			15, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0F, () -> Ingredient.of(Items.PRISMARINE_SHARD));
	public static final Holder<ArmorMaterial> IRON_LASS_ARMOR = registerArmorMaterial("iron_lass",
			Map.of(ArmorItem.Type.BOOTS, 4, ArmorItem.Type.LEGGINGS, 7, ArmorItem.Type.CHESTPLATE, 8, ArmorItem.Type.HELMET, 3),
			15, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0F, () -> Ingredient.EMPTY);
	public static final Holder<ArmorMaterial> CLOTH_ARMOR = registerArmorMaterial("cloth",
			Map.of(ArmorItem.Type.BOOTS, 0, ArmorItem.Type.LEGGINGS, 0, ArmorItem.Type.CHESTPLATE, 0, ArmorItem.Type.HELMET, 0),
			5, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0F, () -> Ingredient.EMPTY);
	
	public static final Holder<ArmorMaterial> DREAM_PAJAMAS = registerArmorMaterial("dream_pajamas",
			Map.of(ArmorItem.Type.BOOTS, 1, ArmorItem.Type.LEGGINGS, 2, ArmorItem.Type.CHESTPLATE, 3, ArmorItem.Type.HELMET, 1),
			0, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0F, () -> Ingredient.EMPTY);
	
	public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIAL_REGISTRY = DeferredRegister.create(Registries.ARMOR_MATERIAL, Minestuck.MOD_ID);
	
	public static Holder<ArmorMaterial> registerArmorMaterial(String name, Map<ArmorItem.Type, Integer> damageReductionByType, int enchantability, Holder<SoundEvent> soundEvent, float toughness, float knockbackResistance, Supplier<Ingredient> repairMaterial)
	{
		return ARMOR_MATERIAL_REGISTRY.register(name, () -> new ArmorMaterial(damageReductionByType, enchantability, soundEvent, repairMaterial, List.of(new ArmorMaterial.Layer(Minestuck.id(name))), enchantability, knockbackResistance));
	}
	
	//Base Tools
	public static final MSToolType SICKLE_TOOL = new MSToolType(MSTags.Blocks.MINEABLE_WITH_SICKLE, ItemAbilities.SWORD_DIG);
	public static final MSToolType SCYTHE_TOOL = new MSToolType(MSTags.Blocks.MINEABLE_WITH_SCYTHE, ItemAbilities.SHEARS_DIG);
	public static final MSToolType CLAWS_TOOL = new MSToolType(MSTags.Blocks.MINEABLE_WITH_CLAWS, ItemAbilities.SWORD_DIG);
	public static final MSToolType PICKAXE_TOOL = new MSToolType(BlockTags.MINEABLE_WITH_PICKAXE, ItemAbilities.PICKAXE_DIG);
	public static final MSToolType HAMMER_TOOL = new MSToolType(MSTags.Blocks.MINEABLE_WITH_HAMMER);
	public static final MSToolType AXE_TOOL = new MSToolType(BlockTags.MINEABLE_WITH_AXE, ItemAbilities.AXE_DIG);
	public static final MSToolType CHAINSAW_TOOL = new MSToolType(MSTags.Blocks.MINEABLE_WITH_CHAINSAW);
	public static final MSToolType SHOVEL_TOOL = new MSToolType(BlockTags.MINEABLE_WITH_SHOVEL);
	public static final MSToolType SWORD_TOOL = new MSToolType(BlockTags.SWORD_EFFICIENT, ItemAbilities.SWORD_DIG);
	public static final MSToolType LANCE_TOOL = new MSToolType(MSTags.Blocks.MINEABLE_WITH_LANCE);
	public static final MSToolType CLUB_TOOL = new MSToolType(MSTags.Blocks.MINEABLE_WITH_CLUB);
	public static final MSToolType KNIFE_TOOL = new MSToolType(MSTags.Blocks.MINEABLE_WITH_KNIFE);
	public static final MSToolType KEY_TOOL = new MSToolType(MSTags.Blocks.MINEABLE_WITH_KEY);
	public static final MSToolType FAN_TOOL = new MSToolType(MSTags.Blocks.MINEABLE_WITH_FAN);
	public static final MSToolType BATON_TOOL = new MSToolType(MSTags.Blocks.MINEABLE_WITH_BATON);
	public static final MSToolType STAFF_TOOL = new MSToolType(MSTags.Blocks.MINEABLE_WITH_STAFF);
	public static final MSToolType CANE_TOOL = new MSToolType(MSTags.Blocks.MINEABLE_WITH_CANE);
	public static final MSToolType FORK_TOOL = new MSToolType(MSTags.Blocks.MINEABLE_WITH_FORK);
	public static final MSToolType SPOON_TOOL = new MSToolType(MSTags.Blocks.MINEABLE_WITH_SPOON);
	public static final MSToolType WAND_TOOL = new MSToolType(MSTags.Blocks.MINEABLE_WITH_WAND);
	public static final MSToolType MISC_TOOL = new MSToolType(null);
}
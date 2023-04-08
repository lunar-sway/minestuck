package com.mraof.minestuck.item;

import com.mraof.minestuck.item.weapon.MSToolType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

import java.util.function.Supplier;

public class MSItemTypes
{
	public static final ToolAction GRIST_HARVEST = ToolAction.get("grist_harvest");
	
	public static final Tier SBAHJ_TIER = new ModItemTier(0, 59, 0.0F, -1.0F, 0, () -> Ingredient.EMPTY);
	public static final Tier PAPER_TIER = new ModItemTier(0, 65, 0.0F, 0.0F, 20, () -> Ingredient.of(Items.PAPER));
	public static final Tier ORGANIC_TIER = new ModItemTier(0, 450, 2.5F, 0.0F, 10, () -> Ingredient.EMPTY);
	public static final Tier MEAT_TIER = new ModItemTier(0, 175, 3.0F, 0.0F, 5, () -> Ingredient.EMPTY);
	public static final Tier CANDY_TIER = new ModItemTier(0, 450, 4.0F, 0.0F, 15, () -> Ingredient.EMPTY);
	public static final Tier BOOK_TIER = new ModItemTier(0, 250, 5.0F, 0.0F, 15, () -> Ingredient.of(Items.BOOK));
	public static final Tier CACTUS_TIER = new ModItemTier(0, 104, 2.0F, 1.0F, 5, () -> Ingredient.of(Blocks.CACTUS));
	public static final Tier ICE_TIER = new ModItemTier(0, 60, 11.0F, 1.0F, 25, () -> Ingredient.of(MSItems.ICE_SHARD.get()));
	public static final Tier POGO_TIER = new ModItemTier(1, 450, 2.0F, 2.0F, 8, () -> Ingredient.of(Items.SLIME_BALL));
	public static final Tier EMERALD_TIER = new ModItemTier(3, 1024, 10.0F, 2.0F, 20, () -> Ingredient.of(Items.EMERALD));
	public static final Tier PRISMARINE_TIER = new ModItemTier(2, 300, 4.0F, 3.0F, 10, () -> Ingredient.of(Items.PRISMARINE_SHARD));
	public static final Tier CORUNDUM_TIER = new ModItemTier(3, 1536, 6.0F, 3.0F, 16, () -> Ingredient.EMPTY);
	public static final Tier REGI_TIER = new ModItemTier(4, 3072, 13.0F, 3.0F, 10, () -> Ingredient.EMPTY);
	public static final Tier HORRORTERROR_TIER = new ModItemTier(3, 2048, 4.0F, 4.0F, 15, () -> Ingredient.EMPTY);
	public static final Tier URANIUM_TIER = new ModItemTier(2, 512, 5.0F, 4.0F, 5, () -> Ingredient.of(MSItems.RAW_URANIUM.get()));
	public static final Tier DENIZEN_TIER = new ModItemTier(5, 4096, 14.0F, 4.0F, 25, () -> Ingredient.EMPTY);
	public static final Tier ZILLY_TIER = new ModItemTier(5, 5120, 12.0F, 5.0F, 30, () -> Ingredient.EMPTY);
	public static final Tier WELSH_TIER = new ModItemTier(5, 5120, 15.0F, 5.0F, 25, () -> Ingredient.of(MSItems.CUEBALL.get()));
	
	public static final ArmorMaterial PRISMARINE_ARMOR = new ModArmorMaterial("minestuck:prismarine", 20, new int[]{3, 7, 6, 2}, 15, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0F, () -> Ingredient.of(Items.PRISMARINE_SHARD));
	public static final ArmorMaterial IRON_LASS_ARMOR = new ModArmorMaterial("minestuck:iron_lass", 50, new int[]{4, 7, 8, 3}, 15, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0F, () -> Ingredient.EMPTY);
	public static final ArmorMaterial CLOTH_ARMOR = new ModArmorMaterial("minestuck:cloth", -1, new int[]{0, 0, 0, 0}, 5, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0F, () -> Ingredient.EMPTY);
	
	public static final ArmorMaterial DREAM_PAJAMAS = new ModArmorMaterial("minestuck:dream_pajamas", 10, new int[]{1, 2, 3, 1}, 0, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0F, () -> Ingredient.EMPTY);
	
	//Base Tools
	public static final MSToolType SICKLE_TOOL = new MSToolType(Material.WEB, Material.LEAVES, Material.PLANT, Material.REPLACEABLE_PLANT).addAction(ToolActions.SWORD_DIG).addEnchantments(EnchantmentCategory.WEAPON);
	public static final MSToolType CLAWS_TOOL = new MSToolType(Material.REPLACEABLE_PLANT, Material.PLANT, Material.WEB).addAction(ToolActions.SWORD_DIG).addEnchantments(EnchantmentCategory.WEAPON);
	public static final MSToolType PICKAXE_TOOL = new MSToolType(Material.METAL, Material.HEAVY_METAL, Material.STONE).addMining(BlockTags.MINEABLE_WITH_PICKAXE, ToolActions.PICKAXE_DIG).addEnchantments(EnchantmentCategory.DIGGER);
	public static final MSToolType HAMMER_TOOL = new MSToolType(PICKAXE_TOOL).addEnchantments(EnchantmentCategory.WEAPON);
	public static final MSToolType AXE_TOOL = new MSToolType(Material.WOOD, Material.PLANT, Material.REPLACEABLE_PLANT).addMining(BlockTags.MINEABLE_WITH_AXE, ToolActions.AXE_DIG).addEnchantments(EnchantmentCategory.DIGGER, EnchantmentCategory.WEAPON);
	public static final MSToolType SHOVEL_TOOL = new MSToolType(Material.TOP_SNOW, Material.SNOW, Material.CLAY, Material.GRASS, Material.DIRT, Material.SAND).addMining(BlockTags.MINEABLE_WITH_SHOVEL, ToolActions.SHOVEL_DIG).addEnchantments(EnchantmentCategory.DIGGER);
	public static final MSToolType SWORD_TOOL = new MSToolType(Material.WEB).addAction(ToolActions.SWORD_DIG).addEnchantments(EnchantmentCategory.WEAPON);
	public static final MSToolType MISC_TOOL = new MSToolType().addEnchantments(EnchantmentCategory.WEAPON);
	
	//Combo Tools
	public static final MSToolType AXE_PICK_TOOL = new MSToolType(PICKAXE_TOOL, AXE_TOOL);
	public static final MSToolType AXE_HAMMER_TOOL = new MSToolType(AXE_TOOL, HAMMER_TOOL);
	public static final MSToolType SHOVEL_PICK_TOOL = new MSToolType(PICKAXE_TOOL, SHOVEL_TOOL);
	public static final MSToolType SHOVEL_AXE_TOOL = new MSToolType(AXE_TOOL, SHOVEL_TOOL);
	public static final MSToolType MULTI_TOOL = new MSToolType(PICKAXE_TOOL, AXE_TOOL, SHOVEL_TOOL);
	
	//Unimplemented
	public static final MSToolType GAUNTLET_TOOL = new MSToolType(Material.GLASS, Material.ICE, Material.ICE_SOLID).addEnchantments(EnchantmentCategory.WEAPON).addEnchantments(Enchantments.SILK_TOUCH);
    
    private static class ModItemTier implements Tier
	{
		private final int harvestLevel;
		private final int maxUses;
		private final float efficiency;
		private final float attackDamage;
		private final int enchantability;
		private final LazyLoadedValue<Ingredient> repairMaterial;
		
		public ModItemTier(int harvestLevel, int maxUses, float efficiency, float attackDamage, int enchantability, Supplier<Ingredient> repairMaterial)
		{
			this.harvestLevel = harvestLevel;
			this.maxUses = maxUses;
			this.efficiency = efficiency;
			this.attackDamage = attackDamage;
			this.enchantability = enchantability;
			this.repairMaterial = new LazyLoadedValue<>(repairMaterial);
		}
		
		@Override
		public int getLevel()
		{
			return harvestLevel;
		}
		
		@Override
		public int getUses()
		{
			return maxUses;
		}
		
		@Override
		public float getSpeed()
		{
			return efficiency;
		}
		
		@Override
		public float getAttackDamageBonus()
		{
			return attackDamage;
		}
		
		@Override
		public int getEnchantmentValue()
		{
			return enchantability;
		}
		
		@Override
		public Ingredient getRepairIngredient()
		{
			return repairMaterial.get();
		}
	}
	
	private static class ModArmorMaterial implements ArmorMaterial
	{
		private static final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};
		
		private final String name;
		private final int maxDamageFactor;
		private final int[] damageReductionAmountArray;
		private final int enchantability;
		private final SoundEvent soundEvent;
		private final float toughness;
		private final float knockbackResistance;
		private final LazyLoadedValue<Ingredient> repairMaterial;
		
		public ModArmorMaterial(String name, int maxDamageFactor, int[] damageReductionAmountArray, int enchantability, SoundEvent soundEvent, float toughness, float knockbackResistance, Supplier<Ingredient> repairMaterial)
		{
			this.name = name;
			this.maxDamageFactor = maxDamageFactor;
			this.damageReductionAmountArray = damageReductionAmountArray;
			this.enchantability = enchantability;
			this.soundEvent = soundEvent;
			this.toughness = toughness;
			this.knockbackResistance = knockbackResistance;
			this.repairMaterial = new LazyLoadedValue<>(repairMaterial);
		}
		
		@Override
		public String getName()
		{
			return name;
		}
		
		@Override
		public int getDurabilityForSlot(EquipmentSlot slotIn)
		{
			return MAX_DAMAGE_ARRAY[slotIn.getIndex()] * this.maxDamageFactor;
		}
		
		@Override
		public int getDefenseForSlot(EquipmentSlot slotIn)
		{
			return this.damageReductionAmountArray[slotIn.getIndex()];
		}
		
		@Override
		public int getEnchantmentValue()
		{
			return enchantability;
		}
		
		@Override
		public SoundEvent getEquipSound()
		{
			return soundEvent;
		}
		
		@Override
		public float getToughness()
		{
			return toughness;
		}
		
		@Override
		public float getKnockbackResistance()
		{
			return knockbackResistance;
		}
		
		@Override
		public Ingredient getRepairIngredient()
		{
			return repairMaterial.get();
		}
	}
}
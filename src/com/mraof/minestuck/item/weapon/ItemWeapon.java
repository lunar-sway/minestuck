package com.mraof.minestuck.item.weapon;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.item.TabMinestuck;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

//TODO add system for Minestuck weapons that can replace enchantments
public class ItemWeapon extends ItemSword //To allow enchantments such as sharpness
{
	protected double weaponDamage;
	protected double weaponSpeed;
	private final int enchantability;
	private float efficiency;
	private boolean unbreakable = false;
	private static final HashMap<String, Set<Material>> toolMaterials = new HashMap<String, Set<Material>>();
	private CreativeTabs overshadowTab = TabMinestuck.instance;
	
	public ItemWeapon(int maxUses, double damageVsEntity, double weaponSpeed, int enchantability, String name)
	{
		this(ToolMaterial.IRON, maxUses, damageVsEntity, weaponSpeed, enchantability, name);
	}
	
	public ItemWeapon(ToolMaterial material, int maxUses, double damageVsEntity, double weaponSpeed, int enchantability, String name)
	{
		super(material);
		this.maxStackSize = 1;
		super.setCreativeTab(CreativeTabs.COMBAT);	//Needed to place recipes in the combat/tools tab
		this.setMaxDamage(maxUses);
		this.weaponDamage = damageVsEntity;
		this.enchantability = enchantability;
		this.weaponSpeed = weaponSpeed;
		this.setUnlocalizedName(name);
	}

	@Override
	public Item setCreativeTab(CreativeTabs tab)
	{
		overshadowTab = tab;
		return this;
	}
	
	@Override
	protected boolean isInCreativeTab(CreativeTabs targetTab)
	{
		return targetTab == CreativeTabs.SEARCH || targetTab == overshadowTab;
	}
	
	@Override
	public CreativeTabs[] getCreativeTabs()
	{
		return new CreativeTabs[] {overshadowTab};
	}
	
	protected double getAttackDamage(ItemStack stack)
	{
		return weaponDamage;
	}
	
	protected double getAttackSpeed(ItemStack stack)
	{
		return weaponSpeed;
	}

	@Override
	public int getItemEnchantability()
	{
		return this.enchantability;
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack)
	{
		Multimap<String, AttributeModifier> multimap = HashMultimap.create();
		if(slot == EntityEquipmentSlot.MAINHAND)
		{
			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", getAttackDamage(stack), 0));
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", getAttackSpeed(stack), 0));
		}
		return multimap;
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase player)
	{
		if(!unbreakable)
			stack.damageItem(1, player);
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D()
	{
		return true;
	}

	public ItemWeapon setTool(String toolClass, int harvestLevel, float efficiency) {
		this.efficiency = efficiency;
		this.setHarvestLevel(toolClass, harvestLevel);
		return this;
	}

	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state)
	{
		Material material = state.getMaterial();
		for(String tool : getToolClasses(stack))
		{
			if(state.getBlock().isToolEffective(tool, state) || toolMaterials.get(tool).contains(material))
			{
				return efficiency;
			}
		}
		return super.getDestroySpeed(stack, state);
	}

	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player)
	{
		if(this == MinestuckItems.clawHammer)
		{
			//player.addStat(MinestuckAchievementHandler.getHammer);
		}
	}

	//Thanks to Mraof for supplying the base for this method.
	@Override
	public boolean canHarvestBlock(IBlockState state, ItemStack stack)
	{
        String tool = state.getBlock().getHarvestTool(state);
        if(getToolClasses(stack).contains(tool))
        {
            int blockHarvestLevel = state.getBlock().getHarvestLevel(state);
            int toolHarvestLevel = getHarvestLevel(stack, tool, null, state);
            return toolHarvestLevel >= blockHarvestLevel;
        } else		//We know that no specific tool is specified, meaning any tool efficiency is defined in the tool itself.
        {			//This also means that there's no tool *level* specified, so any tool of that class is sufficient.
        	Material mat = state.getMaterial();
        	if(mat.isToolNotRequired())
        		return true;
        	for(String tool2 : getToolClasses(stack))
        	{
        		if(toolMaterials.get(tool2) != null && toolMaterials.get(tool2).contains(mat))
        		{
        			return true;
        		}
        	}
        }
        return super.canHarvestBlock(state, stack);
	}
	
	public static void addToolMaterial(String tool, Collection<Material> materials)
	{
		if(!toolMaterials.containsKey(tool))
			toolMaterials.put(tool, new HashSet<Material>());
		toolMaterials.get(tool).addAll(materials);
	}
	
	@Override
	public boolean isDamageable()		{return !unbreakable;}
	public ItemWeapon setBreakable()	{unbreakable=false;	return this;}
	public ItemWeapon setUnbreakable()	{unbreakable=true;	return this;}
	
	public float getEfficiency()		{return efficiency;}
}
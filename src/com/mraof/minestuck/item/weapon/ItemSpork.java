package com.mraof.minestuck.item.weapon;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mraof.minestuck.Minestuck;

//I called it a spork because it includes both
public class ItemSpork extends ItemWeapon 
{
	private int weaponDamage;
	private final EnumSporkType sporkType;
	
	/**
	 * whether it's a spoon or a fork, unused for the crocker spork, as it depends on the meta.
	 */
	public boolean isSpoon;

	public ItemSpork(EnumSporkType sporkType) 
	{
		super();
		this.isSpoon = sporkType.getIsSpoon();
		this.sporkType = sporkType;
		this.maxStackSize = 1;
		this.setMaxDamage(sporkType.getMaxUses());
		this.setCreativeTab(Minestuck.tabMinestuck);
		this.setUnlocalizedName(sporkType.getUnlocalizedName());
		this.weaponDamage = sporkType.getDamageVsEntity();
	}
	
	@Override
	public int getAttackDamage() 
	{
		return weaponDamage;
	}
	
	public int getAttackDamage(ItemStack stack)
	{
		int damage = weaponDamage;
		if(this.sporkType == EnumSporkType.CROCKER && isSpoon(stack))
			damage -= 2;
		return damage;
	}
	
	@Override
	public int getItemEnchantability()
	{
		return this.sporkType.getEnchantability();
	}

	@Override
	public boolean hitEntity(ItemStack itemStack, EntityLivingBase target, EntityLivingBase player)
	{
		itemStack.damageItem(isSpoon(itemStack) ? 1 : 2, player);
		return true;
	}

	public boolean isSpoon(ItemStack itemStack) {
		if (sporkType.equals(EnumSporkType.CROCKER))
			return !itemStack.hasTagCompound() ? true : itemStack.getTagCompound().getBoolean("isSpoon");
		else return isSpoon;
	}
	
	@Override	
	@SideOnly(Side.CLIENT)
	public boolean isFull3D()
	{
		return true;
	}
	
	public int getMaxItemUseDuration(ItemStack itemStack)
	{
		return 72000;
	}
	
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) 
	{
		if(!world.isRemote)
			if (sporkType.equals(EnumSporkType.CROCKER) && player.isSneaking())
			{
				checkTagCompound(stack);
				stack.getTagCompound().setBoolean("isSpoon", !stack.getTagCompound().getBoolean("isSpoon"));
			}
		return stack;
	}
	
	public void checkTagCompound(ItemStack stack)
	{
		if(!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		if(!stack.getTagCompound().hasKey("isSpoon"))
			stack.getTagCompound().setBoolean("isSpoon", true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		if(this.sporkType != EnumSporkType.CROCKER)
			return getUnlocalizedName();
		else return "item."+(isSpoon(stack)?"crockerSpoon":"crockerFork");
	}
	
	@Override
	public Multimap getAttributeModifiers(ItemStack stack)
	{
		Multimap multimap = HashMultimap.create();
		multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(this.itemModifierUUID, "Tool Modifier", (double)this.getAttackDamage(stack), 0));
		return multimap;
	}
	
}

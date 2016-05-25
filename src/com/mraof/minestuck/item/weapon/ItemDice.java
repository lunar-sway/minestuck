package com.mraof.minestuck.item.weapon;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mraof.minestuck.Minestuck;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemDice extends ItemSword	//To allow enchantments such as sharpness
{
	private int weaponDamage;
	private final EnumDiceType diceType;
	
	public ItemDice(EnumDiceType DiceType)
	{
		super(ToolMaterial.STONE);
		
		setCreativeTab(Minestuck.tabMinestuck);
		this.diceType = DiceType;
		this.setMaxDamage(DiceType.getMaxUses());
		this.setUnlocalizedName(DiceType.getName());
		this.weaponDamage = DiceType.getDamageVsEntity();
	}
	
	@Override
	public float getDamageVsEntity()
	{
		int damage=diceType.getDamageVsEntity();
		damage+=(Math.ceil( Math.random()*diceType.getProbibility())-(diceType.getProbibility()/2));
		//System.out.println("damage");
		return 0;
	}
	
	@Override
	public int getItemEnchantability()
	{
		return this.diceType.getEnchantability();
	}
	
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
	{
		return false;
	}
	
	@Override
	public boolean hitEntity(ItemStack itemStack, EntityLivingBase target, EntityLivingBase attacker)
	{
		itemStack.damageItem(1, attacker);
		
		return true;
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState blockIn, BlockPos pos, EntityLivingBase entityLiving)
	{
		if (blockIn.getBlockHardness(worldIn, pos) != 0.0D)
		{
			stack.damageItem(2, entityLiving);
		}
		
		return true;
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer playerIn,Entity entity) {
		// TODO Auto-generated method stub
		//System.out.println("HIHIHIHIHIHIHIHIHIHIHIHIHIHIHIHIH");
		this.weaponDamage=diceType.getDamageVsEntity();
		return super.onLeftClickEntity(stack, playerIn, entity);
	}
	
	

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D()
	{
		return true;
	}
	
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack)
	{
		Multimap multimap = HashMultimap.create();
		multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double)this.weaponDamage, 0));
		return multimap;
	}
	
}

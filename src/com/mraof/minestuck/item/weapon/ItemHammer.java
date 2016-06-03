package com.mraof.minestuck.item.weapon;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mraof.minestuck.util.MinestuckAchievementHandler;

public class ItemHammer extends ItemWeapon
{	
	private int weaponDamage;
	private final EnumHammerType hammerType;
	public float efficiencyOnProperMaterial;
	
	public ItemHammer(EnumHammerType hammerType)
	{
		super();
		
		this.hammerType = hammerType;
		this.setMaxDamage(hammerType.getMaxUses());
		this.setHarvestLevel("pickaxe", hammerType.getHarvestLevel());
		this.efficiencyOnProperMaterial = hammerType.getEfficiencyOnProperMaterial();
		this.setUnlocalizedName(hammerType.getName());
		this.weaponDamage = hammerType.getDamageVsEntity();
	}
	
	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player)
	{
		if(this.hammerType.equals(EnumHammerType.CLAW))
			player.addStat(MinestuckAchievementHandler.getHammer);
	}
	
	@Override
	public float getStrVsBlock(ItemStack stack, IBlockState state)
	{
		return state != null && (state.getMaterial() == Material.iron || state.getMaterial() == Material.anvil || state.getMaterial() == Material.rock) ? this.efficiencyOnProperMaterial : super.getStrVsBlock(stack, state);
	}

	@Override
	public int getAttackDamage()
	{
		return this.weaponDamage;
	}

	@Override
	public int getItemEnchantability()
	{
		return this.hammerType.getEnchantability();
	}

	@Override
	public boolean hitEntity(ItemStack itemStack, EntityLivingBase target, EntityLivingBase player)
	{
		itemStack.damageItem(1, player);
		if(hammerType.equals(EnumHammerType.POGO))
		{
			target.motionY = Math.max(target.motionY, Math.min(getPogoMotion(itemStack)*2, Math.abs(player.motionY) + target.motionY + getPogoMotion(itemStack)));
			player.motionY = 0;
			player.fallDistance = 0;
		}
		else if(hammerType.equals(EnumHammerType.SCARLET))
			target.setFire(50);
		else if (hammerType.equals(EnumHammerType.POPAMATIC) )
			target.attackEntityFrom(DamageSource.magic , (float) (player.worldObj.rand.nextInt(6)+1) * (player.worldObj.rand.nextInt(6)+1) );
		else if (hammerType.equals(EnumHammerType.FEARNOANVIL) && player.worldObj.rand.nextGaussian() > 0.9)	//Just a suggestion, keep it if you like it.
			target.addPotionEffect(new PotionEffect(Potion.getPotionById(2),100,3));	//Would prefer it being triggered by a critical hit instead, if it can.
		return true;
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState blockIn, BlockPos pos, EntityLivingBase entityLiving)
	{
		if ((double)blockIn.getBlockHardness(worldIn, pos) != 0.0D)
			stack.damageItem(2, entityLiving);
		
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D()
	{
		return true;
	}
	
	public int getMaxItemUseDuration(ItemStack itemStack)
	{
		return Integer.MAX_VALUE;
	}
	
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(worldIn.getBlockState(pos).getBlock() != Blocks.air)
		{
			if (hammerType.equals(EnumHammerType.POGO))
			{
				playerIn.motionY = Math.max(playerIn.motionY, Math.min(getPogoMotion(stack)*2, Math.abs(playerIn.motionY) + getPogoMotion(stack)));
				playerIn.fallDistance = 0;
				stack.damageItem(1, playerIn);
				return EnumActionResult.SUCCESS;
			} 
		}
		return EnumActionResult.PASS;
	}
	
	protected double getPogoMotion(ItemStack stack)
	{
//		return 0.5 + EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack)*0.1;
		return 0.7;
	}
	
	@Override
	public boolean canHarvestBlock(IBlockState state, ItemStack stack)
	{
		return state.getMaterial() == Material.rock || state.getMaterial() == Material.anvil || state.getMaterial() == Material.iron;
	}
	
    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack)
    {
        Multimap multimap = HashMultimap.create();
        if(slot == EntityEquipmentSlot.MAINHAND)
        {
        multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double)this.weaponDamage, 0));
        multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4000000953674316D, 0));
        }
        return multimap;
    }
	
}
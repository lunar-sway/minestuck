package com.mraof.minestuck.item.weapon;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
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

import com.mraof.minestuck.util.MinestuckAchievementHandler;

public class ItemHammer extends ItemWeapon
{	
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
		this.weaponSpeed = hammerType.getAttackSpeed();
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
		return state != null && (state.getMaterial() == Material.IRON || state.getMaterial() == Material.ANVIL || state.getMaterial() == Material.ROCK) ? this.efficiencyOnProperMaterial : super.getStrVsBlock(stack, state);
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
		if(hammerType.equals(EnumHammerType.POGO) && player.fallDistance > 0.0F && !player.onGround && !player.isOnLadder() && !player.isInWater() && !player.isRiding())
		{
			double knockbackModifier = 1D - target.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getAttributeValue();
			target.motionY = Math.max(target.motionY, knockbackModifier*Math.min(getPogoMotion(itemStack)*2, Math.abs(player.motionY) + target.motionY + getPogoMotion(itemStack)));
			player.motionY = 0;
			player.fallDistance = 0;
		}
		else if(hammerType.equals(EnumHammerType.SCARLET))
			target.setFire(50);
		else if (hammerType.equals(EnumHammerType.POPAMATIC) )
			target.attackEntityFrom(DamageSource.magic , (float) (player.getRNG().nextInt(6)+1) * (player.getRNG().nextInt(6)+1) );
		else if (hammerType.equals(EnumHammerType.FEARNOANVIL) && player.getRNG().nextDouble() > 0.9)	//Just a suggestion, keep it if you like it.
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
	
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(worldIn.getBlockState(pos).getBlock() != Blocks.AIR)
		{
			if (hammerType.equals(EnumHammerType.POGO))
			{
				double velocity = Math.max(playerIn.motionY, Math.min(getPogoMotion(stack) * 2, Math.abs(playerIn.motionY) + getPogoMotion(stack)));
				final float HORIZONTAL_Y = 6f;
				switch (facing.getAxis()) {
					case X:
					    velocity += Math.abs(playerIn.motionX) / 2;
						playerIn.motionX = velocity * facing.getDirectionVec().getX();
						playerIn.motionY = velocity / HORIZONTAL_Y;
						break;
					case Y:
						playerIn.motionY = velocity * facing.getDirectionVec().getY();
						break;
					case Z:
						velocity += Math.abs(playerIn.motionZ) / 2;
						playerIn.motionZ = velocity * facing.getDirectionVec().getZ();
						playerIn.motionY = velocity / HORIZONTAL_Y;
						break;
				}
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
		return state.getMaterial() == Material.ROCK || state.getMaterial() == Material.ANVIL || state.getMaterial() == Material.IRON;
	}
	
}
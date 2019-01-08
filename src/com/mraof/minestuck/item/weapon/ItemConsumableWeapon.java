package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.item.TabMinestuck;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemConsumableWeapon extends ItemWeapon 
{
	private final int healAmount;
    private final float saturationModifier;
    private final int damageTaken;
	private PotionEffect potionId;
	private float potionEffectProbability;
    
	public ItemConsumableWeapon(int maxUses, double damageVsEntity, double weaponSpeed, int enchantability, String name, int amount, float saturation, int damageTaken) 
	{
		super(maxUses, damageVsEntity, weaponSpeed, enchantability, name);
		this.setCreativeTab(TabMinestuck.instance);
		this.healAmount = amount;
        this.saturationModifier = saturation;
        this.damageTaken = damageTaken;
	}
	
	public ItemConsumableWeapon(int maxUses, double damageVsEntity, double weaponSpeed, int enchantability, String name, int amount, float saturation) 
	{
		this(maxUses, damageVsEntity, weaponSpeed, enchantability, name, amount, saturation, 50);
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 32;
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.EAT;
	} 
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
	{
		stack.damageItem(this.damageTaken, entityLiving);
		this.onFoodEaten(stack, worldIn, entityLiving);
		
		if(entityLiving instanceof EntityPlayer)
		{
			EntityPlayer entityplayer = (EntityPlayer)entityLiving;
			entityplayer.getFoodStats().addStats(this.healAmount, this.saturationModifier);
			worldIn.playSound(null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
		}
		return stack;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		playerIn.setActiveHand(handIn);
		return new ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}
	
	public ItemConsumableWeapon setPotionEffect(PotionEffect effect, float probability)
    {
        this.potionId = effect;
        this.potionEffectProbability = probability;
        
        return this;
    }
	
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityLivingBase player)
    {
        if (!worldIn.isRemote && this.potionId != null && worldIn.rand.nextFloat() < this.potionEffectProbability)
        {
            player.addPotionEffect(new PotionEffect(this.potionId));
        }
    }
}

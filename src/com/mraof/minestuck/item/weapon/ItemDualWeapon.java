package com.mraof.minestuck.item.weapon;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;



public class ItemDualWeapon extends ItemWeapon{
	public String Prefex;
	public double Power;
	public double ShiethedPower;
	public double Speed;
	public double ShiethedSpeed;
	public ItemDualWeapon(int maxUses, double damageVsEntity,double damagedVsEntityWhileShiethed,double weaponSpeed,double weaponSpeedWhileShiethed, int enchantability, String name) {
		super(maxUses, damageVsEntity, weaponSpeed, enchantability, name);
		
		this.Prefex=name;
		this.Power=damageVsEntity;
		this.ShiethedPower= damagedVsEntityWhileShiethed;
		this.Speed=weaponSpeed;
		this.ShiethedSpeed=weaponSpeedWhileShiethed;
	}
	
	public boolean IsDrawn(ItemStack itemStack)
	{
		return checkTagCompound(itemStack).getBoolean("IsDrawn");
	}
	
	private NBTTagCompound checkTagCompound(ItemStack stack)
	{
		NBTTagCompound tagCompound = stack.getTagCompound();
		if(tagCompound == null)
		{
			tagCompound = new NBTTagCompound();
			stack.setTagCompound(tagCompound);
		}
		if(!tagCompound.hasKey("IsDrawn"))
		{
			tagCompound.setBoolean("IsDrawn", true);
		}
		return tagCompound;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack itemStackIn = playerIn.getHeldItem(handIn);
			if(playerIn.isSneaking() ){
			if (IsDrawn(itemStackIn)){
				Sheath(itemStackIn);
			}else{
				Draw(itemStackIn);
			}
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
		}
		return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
	}
	
	@Override
    public String getUnlocalizedName(ItemStack stack)
    {

        
        if (IsDrawn(stack)){
        	return "item." + Prefex + "Drawn";
        }
        else{
        	return "item." + Prefex +  "Sheathed";
        }
    }
	@Override
	public double getAttackDamage(ItemStack stack){
		if (IsDrawn(stack)){
			return this.Power;
		}
		else{
			return this.ShiethedPower;
		}
	}
	@Override
	protected double getAttackSpeed(ItemStack stack)
	{
		if (IsDrawn(stack)){
			return this.Speed;
		}
		else{
			return this.ShiethedSpeed;
		}
	}	


	public void Sheath(ItemStack stack){
	NBTTagCompound tagCompound = checkTagCompound(stack);
	tagCompound.setBoolean("IsDrawn",false);
	}
	public void Draw(ItemStack stack){
		NBTTagCompound tagCompound = checkTagCompound(stack);
		tagCompound.setBoolean("IsDrawn",true);
	}
	



}

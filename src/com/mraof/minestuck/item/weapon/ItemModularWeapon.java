package com.mraof.minestuck.item.weapon;

import java.util.ArrayList;

import com.mraof.minestuck.entity.underling.EntityUnderling;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemModularWeapon extends ItemWeapon
{
	private ArrayList<IModularWeaponComponent> components = new ArrayList<>();
	
	public ItemModularWeapon(ToolMaterial material, int maxUses, double damageVsEntity, double weaponSpeed,
			int enchantability, String name)
	{
		super(material, maxUses, damageVsEntity, weaponSpeed, enchantability, name);
	}
	
	@Override
	public boolean hitEntity(ItemStack itemStack, EntityLivingBase target, EntityLivingBase player)
	{
		for(IModularWeaponComponent comp : components)
		{
			comp.onEntityHit(itemStack, target, player);
		}
		return super.hitEntity(itemStack, target, player);
	}
	
	public boolean onCriticalHit(ItemStack itemStack, EntityLivingBase target, EntityLivingBase player)
	{
		for(IModularWeaponComponent comp : components)
		{
			comp.onEntityHit(itemStack, target, player);
		}
		return super.hitEntity(itemStack, target, player);
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState blockState, BlockPos pos, EntityLivingBase playerIn)
	{
		for(IModularWeaponComponent comp : components)
		{
			comp.onBlockBroken(stack, worldIn, blockState, pos, playerIn);
		}
		return super.onBlockDestroyed(stack, worldIn, blockState, pos, playerIn);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack item = playerIn.getHeldItem(handIn);
		EnumActionResult result = EnumActionResult.FAIL;
		for(IModularWeaponComponent comp : components)
		{
			if(item.getItem() instanceof ItemModularWeapon)
			{
				ActionResult<ItemStack> curr = comp.onItemRightClick(item, worldIn, playerIn, handIn);
				item = curr.getResult();
				result = EnumActionResult.values()[Math.min(curr.getType().ordinal(), result.ordinal())];
			}
		}
		return new ActionResult<ItemStack>(result, item);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			  EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		ItemStack item = player.getHeldItem(hand);
		EnumActionResult result = EnumActionResult.FAIL;
		for(IModularWeaponComponent comp : components)
		{
			if(item.getItem() instanceof ItemModularWeapon)
			{
				EnumActionResult curr = comp.onItemUse(item, player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
				result = EnumActionResult.values()[Math.min(curr.ordinal(), result.ordinal())];
			}
		}
		return result;
	}
	
	/*
	 * Component addition
	 */
	
	public enum ComponentAddSuccess
	{
		success,
		updated,
		notUpdated,
		alreadyExists
	}
	
	public ComponentAddSuccess addComponent(IModularWeaponComponent component)
	{
		Class clazz = component.getClass();
		for(IModularWeaponComponent comp : components)
		{
			if(clazz == comp.getClass())
			{
				if(!comp.hasVariants() || comp.compareTo(component)==0)
				{
					return ComponentAddSuccess.alreadyExists;
				} else if(comp.compareTo(component) > 0)
				{
					return ComponentAddSuccess.notUpdated;
				} else
				{
					components.add(component);
					components.remove(comp);
					return ComponentAddSuccess.updated;
				}
			}
		}
		
		components.add(component);
		return ComponentAddSuccess.success;
	}
}
package com.mraof.minestuck.item.weapon;

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

/**
 * An interface used by all components in the modular weapon system.
 * Contains several methods anticipated to be used by weapon components.
 * 
 * @author SarahK
 *
 */
public interface IModularWeaponComponent extends Comparable<IModularWeaponComponent>
{
	/**
	 * Used to indicate whether all instances of this component are effectively identical.
	 * For example, Pogo components vary because some have more "pogoness" than others.
	 * In contrast, Candy components always drop candy: there is no "candyness" variable.
	 */
	public default boolean hasVariants() { return false; }
	
	/**
	 * If two components are of one type, and that type has variation, this method is used to sort them.
	 * If a component is added to a weapon that already has a component of that type, the "greater" one is kept. 
	 * @param comp The weapon component to which this one is being compared
	 * @return 0, unless this method is overridden
	 */
	public default int compareTo(IModularWeaponComponent comp) { return 0; }
	
	/**
	 * What this component does when the weapon is used to hit an enemy.
	 */
	public default void onEntityHit(ItemStack itemStack, EntityLivingBase target, EntityLivingBase player) {}
	
	/**
	 * What this component does when the weapon is used to land a critical hit on an enemy
	 */
	public default void onCriticalHit(ItemStack itemStack, EntityLivingBase target, EntityLivingBase player) {}
	
	/**
	 * What this component does when the weapon is used to break a block
	 */
	public default void onBlockBroken(ItemStack stack, World worldIn, IBlockState blockState, BlockPos pos, EntityLivingBase playerIn) {}
	
	/**
	 * What this component does when the weapon is used to right-click nothing in particular
	 * @param worldIn The world in which the right-clicking happens
	 * @param playerIn The player that did the right-clicking
	 * @param handIn The hand in which the player was holding the weapon at the time
	 */
	public default ActionResult<ItemStack> onItemRightClick(ItemStack item, World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, item);
	}
	
	/**
	 * What this component does when the weapon is used to right-click a block
	 */
	public default EnumActionResult onItemUse(ItemStack item, EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			  EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		return EnumActionResult.FAIL;
	}
	
	
}

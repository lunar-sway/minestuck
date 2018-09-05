package com.mraof.minestuck.item;

import com.mraof.minestuck.util.Debug;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;

/**
 * Similar to ItemSeedFood, but the block placed by this item is set as an IBlockState through setPlant(),
 * rather than as a block passed in through the constructor.
 * Setting the crop to a Minestuck block via a constructor would result in a null value.
 */
public class ItemMinestuckSeedFood extends ItemFood implements net.minecraftforge.common.IPlantable
{
	protected IBlockState plant;
	
	public ItemMinestuckSeedFood(int amount, float saturation)
	{
		super(amount, saturation, false);
	}
	
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(plant==null)
		{
			Debug.warn("Someone tried to plant an ItemMinestuckSeedFood that has no crop to plant! Be sure to define the crops!");
			return EnumActionResult.FAIL;
		}
		
		ItemStack itemstack = player.getHeldItem(hand);
		net.minecraft.block.state.IBlockState state = worldIn.getBlockState(pos);
		if (
				facing == EnumFacing.UP
				&& player.canPlayerEdit(pos.offset(facing), facing, itemstack)
				&& state.getBlock().canSustainPlant(state, worldIn, pos, EnumFacing.UP, this)
				&& worldIn.isAirBlock(pos.up())
			)
		{
			worldIn.setBlockState(pos.up(), this.plant, 11);
			itemstack.shrink(1);
			return EnumActionResult.SUCCESS;
		}
		else
		{
			return EnumActionResult.FAIL;
		}
	}
	
	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
	{
		return EnumPlantType.Crop;
	}
	
	/**
	 * The setter version of IPlantable's getPlant. Sets the value to be returned by getPlant.
	 * @param plant The blockstate placed in the world when this item is planted
	 * @return Returns this, for chaining
	 */
	public ItemMinestuckSeedFood setPlant(IBlockState plant)
	{
		this.plant = plant;
		return this;
	}
	
	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos)
	{
		return plant;
	}
}

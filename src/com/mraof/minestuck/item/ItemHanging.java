package com.mraof.minestuck.item;

import net.minecraft.entity.EntityHanging;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class ItemHanging extends Item
{
	protected final EntityProvider provider;
	public ItemHanging(EntityProvider provider, Properties properties)
	{
		super(properties);
		this.provider = provider;
	}
	
	@Override
	public EnumActionResult onItemUse(ItemUseContext context)
	{
		ItemStack stack = context.getItem();
		EnumFacing facing = context.getFace();
		World worldIn = context.getWorld();
		BlockPos blockPos = context.getPos().offset(facing);
		
		if (facing != EnumFacing.DOWN && facing != EnumFacing.UP
				&& (context.getPlayer() == null || context.getPlayer().canPlayerEdit(blockPos, facing, stack)))
		{
			EntityHanging entityhanging = provider.createEntity(worldIn, blockPos, facing, stack);
			
			if (entityhanging != null && entityhanging.onValidSurface())
			{
				if (!worldIn.isRemote)
				{
					entityhanging.playPlaceSound();
					worldIn.spawnEntity(entityhanging);
				}
				
				stack.shrink(1);
			}
			
			return EnumActionResult.SUCCESS;
		}
		else
		{
			return EnumActionResult.FAIL;
		}
	}
	
	public interface EntityProvider
	{
		EntityHanging createEntity(World world, BlockPos pos, EnumFacing facing, ItemStack stack);
	}
}

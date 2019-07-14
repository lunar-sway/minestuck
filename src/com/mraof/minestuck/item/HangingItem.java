package com.mraof.minestuck.item;

import net.minecraft.entity.item.HangingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class HangingItem extends Item
{
	protected final EntityProvider provider;
	public HangingItem(EntityProvider provider, Properties properties)
	{
		super(properties);
		this.provider = provider;
	}
	
	@Override
	public ActionResultType onItemUse(ItemUseContext context)
	{
		ItemStack stack = context.getItem();
		Direction facing = context.getFace();
		World worldIn = context.getWorld();
		BlockPos blockPos = context.getPos().offset(facing);
		
		if (facing != Direction.DOWN && facing != Direction.UP
				&& (context.getPlayer() == null || context.getPlayer().canPlayerEdit(blockPos, facing, stack)))
		{
			HangingEntity entityhanging = provider.createEntity(worldIn, blockPos, facing, stack);
			
			if (entityhanging != null && entityhanging.onValidSurface())
			{
				if (!worldIn.isRemote)
				{
					entityhanging.playPlaceSound();
					worldIn.spawnEntity(entityhanging);
				}
				
				stack.shrink(1);
			}
			
			return ActionResultType.SUCCESS;
		}
		else
		{
			return ActionResultType.FAIL;
		}
	}
	
	public interface EntityProvider
	{
		HangingEntity createEntity(World world, BlockPos pos, Direction facing, ItemStack stack);
	}
}

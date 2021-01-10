package com.mraof.minestuck.block.machine;

import com.mraof.minestuck.block.DecorBlock;
import com.mraof.minestuck.util.CustomVoxelShape;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;


public class SendificatorBlock extends DecorBlock
{
	public SendificatorBlock(Properties properties, CustomVoxelShape shape)
	{
		super(properties, shape);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		if(!player.isSneaking())
		{
			ItemStack itemStackIn = player.getHeldItem(handIn);
			//Function will store information about item and pass it along to TileEntity, currently useless
			return ActionResultType.SUCCESS;
		} else {
			return ActionResultType.PASS;
		}
	}
}

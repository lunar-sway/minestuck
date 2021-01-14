package com.mraof.minestuck.block.machine;

import com.mraof.minestuck.block.DecorBlock;
import com.mraof.minestuck.util.CustomVoxelShape;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;


public class SendificatorBlock extends DecorBlock
{
	public static final String ACTIVATION_MESSAGE = "activation_message";
	
	public SendificatorBlock(Properties properties, CustomVoxelShape shape)
	{
		super(properties, shape);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		if(!player.isSneaking() && player.getHeldItemMainhand().isEmpty())
		{
			//ItemStack itemStackIn = player.getHeldItem(handIn);
			//Function will store information about item and pass it along to TileEntity, currently useless
			if(!worldIn.isRemote)
			{
				player.sendMessage(new TranslationTextComponent(getTranslationKey() + "." + ACTIVATION_MESSAGE));
			}
			return ActionResultType.SUCCESS;
		} else
		{
			return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
		}
	}
}

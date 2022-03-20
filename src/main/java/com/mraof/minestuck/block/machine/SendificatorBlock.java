package com.mraof.minestuck.block.machine;

import com.mraof.minestuck.block.CustomShapeBlock;
import com.mraof.minestuck.util.CustomVoxelShape;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class SendificatorBlock extends CustomShapeBlock
{
	public static final String ACTIVATION_MESSAGE = "activation_message";
	
	public SendificatorBlock(Properties properties, CustomVoxelShape shape)
	{
		super(properties, shape);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		if(!player.isShiftKeyDown() && player.getMainHandItem().isEmpty())
		{
			//ItemStack itemStackIn = player.getHeldItem(handIn);
			//Function will store information about item and pass it along to TileEntity, currently useless
			if(!worldIn.isClientSide)
			{
				player.sendMessage(new TranslationTextComponent(getDescriptionId() + "." + ACTIVATION_MESSAGE), Util.NIL_UUID);
			}
			return ActionResultType.SUCCESS;
		} else
		{
			return super.use(state, worldIn, pos, player, handIn, hit);
		}
	}
}

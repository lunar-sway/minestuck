package com.mraof.minestuck.block.machine;

import com.mraof.minestuck.block.CustomShapeBlock;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.effects.CreativeShockEffect;
import com.mraof.minestuck.tileentity.machine.SendificatorTileEntity;
import com.mraof.minestuck.tileentity.redstone.WirelessRedstoneTransmitterTileEntity;
import com.mraof.minestuck.util.CustomVoxelShape;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class SendificatorBlock extends CustomShapeBlock
{
	public static final String ACTIVATION_MESSAGE = "activation_message";
	
	public SendificatorBlock(Properties properties, CustomVoxelShape shape)
	{
		super(properties, shape);
	}
	
	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new SendificatorTileEntity();
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
	{
		if(!CreativeShockEffect.doesCreativeShockLimit(player, CreativeShockEffect.LIMIT_MACHINE_INTERACTIONS))
		{
			TileEntity tileEntity = worldIn.getBlockEntity(pos);
			if(tileEntity instanceof SendificatorTileEntity)
			{
				if(worldIn.isClientSide)
				{
					SendificatorTileEntity te = (SendificatorTileEntity) tileEntity;
					//MSScreenFactories.displaySendificatorScreen(te);
				}
				
				return ActionResultType.SUCCESS;
			}
		}
		
		return ActionResultType.PASS;
	}
	
	/*@Override
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
	}*/
}

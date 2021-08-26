package com.mraof.minestuck.block;

import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.effects.MSEffects;
import com.mraof.minestuck.tileentity.redstone.WirelessRedstoneTransmitterTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class WirelessRedstoneTransmitterBlock extends Block
{
	
	public WirelessRedstoneTransmitterBlock(Properties properties)
	{
		super(properties);
		setDefaultState(getDefaultState());
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
		return new WirelessRedstoneTransmitterTileEntity();
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
	{
		if(!player.isSneaking() && !player.isPotionActive(MSEffects.CREATIVE_SHOCK.get()))
		{
			TileEntity tileEntity = worldIn.getTileEntity(pos);
			if(tileEntity instanceof WirelessRedstoneTransmitterTileEntity)
			{
				WirelessRedstoneTransmitterTileEntity te = (WirelessRedstoneTransmitterTileEntity) tileEntity;
				
				MSScreenFactories.displayWirelessRedstoneTransmitterScreen(te);
			}
		}
		
		return ActionResultType.SUCCESS;
	}
}

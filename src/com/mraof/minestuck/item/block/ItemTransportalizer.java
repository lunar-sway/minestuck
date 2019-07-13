package com.mraof.minestuck.item.block;

import com.mraof.minestuck.tileentity.TransportalizerTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ItemTransportalizer  extends ItemBlock
{
	public ItemTransportalizer(Block blockIn, Properties builder)
	{
		super(blockIn, builder);
	}
	
	@Override
	protected boolean onBlockPlaced(BlockPos pos, World world, @Nullable EntityPlayer player, ItemStack stack, IBlockState state)
	{
		if(stack.hasDisplayName() && stack.getDisplayName().getString().length() == 4)
		{
			TileEntity te = world.getTileEntity(pos);
			if(te instanceof TransportalizerTileEntity)
				((TransportalizerTileEntity) te).setId(stack.getDisplayName().getString().toUpperCase());
		}
		return true;
	}
}
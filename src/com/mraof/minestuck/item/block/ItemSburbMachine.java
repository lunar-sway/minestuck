package com.mraof.minestuck.item.block;

import com.google.common.base.Function;
import com.mraof.minestuck.block.BlockSburbMachine;
import com.mraof.minestuck.tileentity.TileEntitySburbMachine;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSburbMachine extends ItemMultiTexture
{
	public ItemSburbMachine(Block block) 
	{
		super(block, block, new Function<ItemStack, String>()
		{
			@Override
			public String apply(ItemStack input)
			{
				return BlockSburbMachine.MachineType.values()[input.getMetadata()].getUnlocalizedName();
			}
		});
	}
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState)
	{
		if(super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState))
		{
			if(newState.getValue(BlockSburbMachine.MACHINE_TYPE) == BlockSburbMachine.MachineType.CRUXTRUDER && stack.hasTagCompound() && stack.getTagCompound().hasKey("color"))
			{
				TileEntity te = world.getTileEntity(pos);
				if(te instanceof TileEntitySburbMachine)
					((TileEntitySburbMachine)te).color = stack.getTagCompound().getInteger("color");
			}
			return true;
		}
		return false;
	}
}
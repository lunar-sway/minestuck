package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.BlockAlchemiter;
import com.mraof.minestuck.block.BlockAlchemiter.EnumParts;
import com.mraof.minestuck.block.BlockAlchemiterUpgrades;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.client.gui.GuiHandler;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityUpgradedAlchemiter extends TileEntityAlchemiter
{
	@Override
	public void onRightClick(World worldIn, EntityPlayer playerIn, IBlockState state, EnumParts part) 
	{
		if(worldIn.isRemote)
		{
			if(part == EnumParts.CENTER_PAD || part == EnumParts.CORNER || part == EnumParts.SIDE_LEFT || part == EnumParts.SIDE_RIGHT || part == EnumParts.TOTEM_CORNER)
			{
				BlockPos mainPos = pos;
				if(!isBroken() && !(worldIn.getBlockState(mainPos).getBlock() == MinestuckBlocks.alchemiterUpgrades[3] && BlockAlchemiterUpgrades.getPart(worldIn.getBlockState(mainPos)) == BlockAlchemiterUpgrades.EnumParts.BLENDER))
				{
					{
						playerIn.openGui(Minestuck.instance, GuiHandler.GuiId.ALCHEMITER.ordinal(), worldIn, mainPos.getX(), mainPos.getY(), mainPos.getZ());
					}
				}
			}
			return;
		}
		
		
		super.onRightClick(worldIn, playerIn, state, part);
	}
}

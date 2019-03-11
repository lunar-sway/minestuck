package com.mraof.minestuck.upgrades.placement;

import java.util.List;

import com.mraof.minestuck.tileentity.TileEntityJumperBlock;
import com.mraof.minestuck.upgrades.AlchemiterUpgrade;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class VerticalPlacement extends UpgradePlacementType
{

	public VerticalPlacement(TileEntityJumperBlock jbe, AlchemiterUpgrade upgrade) 
	{
		super(jbe, upgrade);
	}

	@Override
	public int placeBlocks(int blockCount, EntityPlayer playerIn, World world) 
	{
		BlockPos mainPos = jbe.getPos();
		List<IBlockState> blocks = upgrade.getBlocks();
		
		if(blockCount - 1 < 0)
		{
			playerIn.sendStatusMessage(new TextComponentTranslation("There's not enough space for that upgrade"), true);
			return blockCount;
		}
		
		int blockCounter = 1;
		BlockPos startingPos = null;
		for(int i = 0; i < 12; i++)
		{
			startingPos = posWrap(mainPos, i);
			if(world.getBlockState(startingPos).getBlockHardness(world, startingPos) <= 1)
				break;
		}
		if(startingPos == null)
		{
			playerIn.sendStatusMessage(new TextComponentTranslation("There's not enough space for that upgrade"), true);
			return blockCount;
		}
		
		for(int i = 0; i < blocks.size(); i++)
		{
			BlockPos pos = startingPos.up(i);
			if(world.getBlockState(pos).getBlockHardness(world, pos) > 1)
			{
				playerIn.sendStatusMessage(new TextComponentTranslation("There's not enough space for that upgrade"), true);
				return blockCount;
			}
		}
		
		int i = 0;
		for(IBlockState state : blocks)
		{
			BlockPos pos = startingPos.up(i);
			world.destroyBlock(pos, true);
			world.setBlockState(pos, state);
			i++;
		}
		
		return blockCount-1;
	}

}

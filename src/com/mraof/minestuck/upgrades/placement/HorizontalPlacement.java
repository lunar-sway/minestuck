package com.mraof.minestuck.upgrades.placement;

import java.util.List;

import com.mraof.minestuck.tileentity.TileEntityJumperBlock;
import com.mraof.minestuck.upgrades.AlchemiterUpgrade;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class HorizontalPlacement extends UpgradePlacementType
{

	public HorizontalPlacement(TileEntityJumperBlock jbe, AlchemiterUpgrade upgrade) 
	{
		super(jbe, upgrade);
	}

	@Override
	public int placeBlocks(int blockCount, EntityPlayer playerIn, World world) 
	{
		BlockPos mainPos = jbe.getPos();
		List<IBlockState> blocks = upgrade.getBlocks();
		
		if(blockCount - blocks.size() < 0)
		{
			playerIn.sendStatusMessage(new TextComponentTranslation("There's not enough space for that upgrade"), true);
			return blockCount;
		}
		
		int blockCounter = blocks.size();
		int startingPos = 0;
		for(int i = 0; i < 12; i++)
		{
			if(blockCounter <= 0) 
			{
				startingPos = 11 - (blockCounter + blocks.size() - 1);
				break;
			}
			BlockPos pos = posWrap(mainPos, i);
			if(world.getBlockState(pos).getBlockHardness(world, pos) > 1)
			{
				blockCounter = blocks.size();
				continue;
			}
			else blockCounter--;
		}
		if(blockCounter > 0)
		{
			playerIn.sendStatusMessage(new TextComponentTranslation("There's not enough space for that upgrade"), true);
			return blockCount;
		}
		
		int i = startingPos;
		for(IBlockState state : blocks)
		{
			BlockPos pos = posWrap(mainPos, i);
			world.destroyBlock(pos, true);
			world.setBlockState(pos, state);
			i++;
		}
		return blockCount - blocks.size();
	}


}

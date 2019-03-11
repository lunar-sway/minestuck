package com.mraof.minestuck.upgrades.placement;

import java.util.Arrays;
import java.util.List;

import com.mraof.minestuck.block.BlockAlchemiter;
import com.mraof.minestuck.tileentity.TileEntityAlchemiter;
import com.mraof.minestuck.tileentity.TileEntityJumperBlock;
import com.mraof.minestuck.upgrades.AlchemiterUpgrade;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class PedestalPlacement extends UpgradePlacementType
{

	public PedestalPlacement(TileEntityJumperBlock jbe, AlchemiterUpgrade upgrade) {
		super(jbe, upgrade);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int placeBlocks(int blockCount, EntityPlayer playerIn, World world) 
	{
		int latheUpgradeSlot = jbe.getLatheUpgradeSlot();
		AlchemiterUpgrade[] upgradeList = jbe.getUpgrades();
		TileEntityAlchemiter alchemiter = jbe.getAlchemiter();

		BlockPos mainPos = jbe.getPos();
		List<IBlockState> blocks = upgrade.getBlocks();
		
		if(latheUpgradeSlot >= 0)
		{
			latheUpgradeSlot = Arrays.asList(upgradeList).indexOf(upgrade);
			BlockPos startingPos = alchemiter.getPos().down();
			
			for(int i = 0; i < blocks.size(); i++)
			{
				BlockPos pos = startingPos.up(i);
				if(world.getBlockState(pos).getBlockHardness(world, pos) > 1 && !(world.getBlockState(pos).getBlock() instanceof BlockAlchemiter))
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
		}
		return blockCount;
	}

}

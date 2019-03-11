package com.mraof.minestuck.upgrades.placement;

import com.mraof.minestuck.tileentity.TileEntityJumperBlock;
import com.mraof.minestuck.upgrades.AlchemiterUpgrade;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class UpgradePlacementType 
{

	public BlockPos posWrap(BlockPos pos, int i)
	{
		
		return pos.offset(EnumFacing.NORTH, i);
	}
	
	protected TileEntityJumperBlock jbe;
	protected AlchemiterUpgrade upgrade;
	public UpgradePlacementType(TileEntityJumperBlock jbe, AlchemiterUpgrade upgrade) {this.jbe = jbe; this.upgrade = upgrade;};
	public abstract int placeBlocks(int blockCount, EntityPlayer playerIn, World world);
}

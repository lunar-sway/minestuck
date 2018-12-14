package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.BlockAlchemiter;
import com.mraof.minestuck.block.BlockAlchemiter.EnumParts;
import com.mraof.minestuck.block.BlockAlchemiterUpgrades;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.client.gui.GuiHandler;
import com.mraof.minestuck.util.AlchemiterUpgrades;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class TileEntityUpgradedAlchemiter extends TileEntityAlchemiter
{
	/*
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
	*/
	@Override
	public void checkStates() {
		if(this.isBroken())
			return;
		
		EnumFacing facing = getWorld().getBlockState(this.getPos()).getValue(BlockAlchemiterUpgrades.DIRECTION);
		
		BlockPos pos = getPos().down();
		TileEntityJumperBlock jbeTe = ((TileEntityJumperBlock) jbe);
		EnumParts[] baseParts = {EnumParts.TOTEM_CORNER, EnumParts.TOTEM_PAD, EnumParts.LOWER_ROD, EnumParts.UPPER_ROD};
		
		System.out.println(AlchemiterUpgrades.upgradeList);
		System.out.println(jbeTe.getLatheUpgradeId());
		
		AlchemiterUpgrades upg = AlchemiterUpgrades.upgradeList[jbeTe.getLatheUpgradeId()];
		IBlockState[] upgBlocks = upg.getUpgradeBlocks();
		
		System.out.println(upgBlocks);
		
		for(int i = 0; i < upgBlocks.length; i++)
		{
			IBlockState state = upgBlocks[i];
			IBlockState checkPart;
			if(BlockAlchemiterUpgrades.getPart(state) == BlockAlchemiterUpgrades.EnumParts.BLANK) checkPart = BlockAlchemiter.getBlockState(baseParts[i], facing);
			else if(state.getBlock() instanceof BlockAlchemiterUpgrades) checkPart = state.withProperty(BlockAlchemiterUpgrades.DIRECTION, facing);
			else checkPart = state;
			
			System.out.println(pos.up(i));
			System.out.println(world.getBlockState(pos.up(i)));
			System.out.println(checkPart);
			System.out.println();
			
			if(!world.getBlockState(pos.up(i)).equals(checkPart))
			{
				System.out.println("died at " + i);
				breakMachine();
				return;
			}
		}
		
		if(!world.getBlockState(pos.offset(facing.rotateY())).equals(BlockAlchemiter.getBlockState(EnumParts.SIDE_LEFT, facing)) ||
				!world.getBlockState(pos.offset(facing.rotateY(), 2)).equals(BlockAlchemiter.getBlockState(EnumParts.SIDE_RIGHT, facing)) ||
				!world.getBlockState(pos.offset(facing).offset(facing.rotateY())).equals(BlockAlchemiter.getBlockState(EnumParts.CENTER_PAD, facing)) ||
				!world.getBlockState(pos.offset(facing.rotateY(), 3)).equals(BlockAlchemiter.getBlockState(EnumParts.CORNER, facing)) ||
				!world.getBlockState(pos.offset(facing).offset(facing.rotateY(), 3)).equals(BlockAlchemiter.getBlockState(EnumParts.SIDE_LEFT, facing.rotateYCCW())) ||
				!world.getBlockState(pos.offset(facing, 2).offset(facing.rotateY(), 3)).equals(BlockAlchemiter.getBlockState(EnumParts.SIDE_RIGHT, facing.rotateYCCW())) ||
				!world.getBlockState(pos.offset(facing).offset(facing.rotateY(), 2)).equals(BlockAlchemiter.getBlockState(EnumParts.CENTER_PAD, facing.rotateYCCW())) ||
				!world.getBlockState(pos.offset(facing, 3).offset(facing.rotateY(), 3)).equals(BlockAlchemiter.getBlockState(EnumParts.CORNER, facing.rotateYCCW())) ||
				!world.getBlockState(pos.offset(facing, 3).offset(facing.rotateY(), 2)).equals(BlockAlchemiter.getBlockState(EnumParts.SIDE_LEFT, facing.getOpposite())) ||
				!world.getBlockState(pos.offset(facing, 3).offset(facing.rotateY(), 1)).equals(BlockAlchemiter.getBlockState(EnumParts.SIDE_RIGHT, facing.getOpposite())) ||
				!world.getBlockState(pos.offset(facing, 2).offset(facing.rotateY(), 2)).equals(BlockAlchemiter.getBlockState(EnumParts.CENTER_PAD, facing.getOpposite())) ||
				!world.getBlockState(pos.offset(facing, 3)).equals(BlockAlchemiter.getBlockState(EnumParts.CORNER, facing.getOpposite())) ||
				!world.getBlockState(pos.offset(facing, 2)).equals(BlockAlchemiter.getBlockState(EnumParts.SIDE_LEFT, facing.rotateY())) ||
				!world.getBlockState(pos.offset(facing)).equals(BlockAlchemiter.getBlockState(EnumParts.SIDE_RIGHT, facing.rotateY())) ||
				!world.getBlockState(pos.offset(facing, 2).offset(facing.rotateY(), 1)).equals(BlockAlchemiter.getBlockState(EnumParts.CENTER_PAD, facing.rotateY())))
			
		{
			breakMachine();
			return;
		}
		
		return;
	}
}

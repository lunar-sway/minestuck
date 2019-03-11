package com.mraof.minestuck.tileentity;

import java.util.ArrayList;
import java.util.List;

import com.mraof.minestuck.block.BlockAlchemiter;
import com.mraof.minestuck.block.BlockAlchemiter.EnumParts;
import com.mraof.minestuck.upgrades.AlchemiterUpgrade;
import com.mraof.minestuck.block.BlockAlchemiterUpgrades;
import com.mraof.minestuck.util.Debug;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class TileEntityUpgradedAlchemiter extends TileEntityAlchemiter
{
	
	public void onRightClick(World worldIn, EntityPlayer playerIn, IBlockState state) 
	{	
		if(state.getBlock() instanceof BlockAlchemiterUpgrades)
			onRightClick(worldIn, playerIn, state, BlockAlchemiterUpgrades.getPart(state));
		else if(state.getBlock() instanceof BlockAlchemiter)
		{
			if(!(jbe instanceof TileEntityJumperBlock))
			{
				Debug.warnf("could not find jbe at alchemiter in pos %s, this shouldn't be happening! (ref. line: TileEntityUpgradedAlchemiter.31)", getPos());
				return;
			}
			
			AlchemiterUpgrade latheUpg = ((TileEntityJumperBlock)jbe).getUpgrade(((TileEntityJumperBlock)jbe).getLatheUpgradeSlot());
			
			if(!AlchemiterUpgrade.nullifiesAlchemiterFunc(latheUpg))
				onRightClick(worldIn, playerIn, state, ((BlockAlchemiter)state.getBlock()).getPart(state));
		}
	}
	
	public void onRightClick(World worldIn, EntityPlayer playerIn, IBlockState state, BlockAlchemiterUpgrades.EnumParts part) 
	{
		if(worldIn.isRemote)
		{
			switch(part)
			{
			case BLENDER: System.out.println("B L E N D ! ! ! ! ! ! ! !"); break;
			default: System.out.println("default"); break;
			}
			return;
		}
		
	}
	
	@Override
	public void checkStates() {
		if(this.isBroken())
			return;
		
		EnumFacing facing = getWorld().getBlockState(this.getPos()).getValue(BlockAlchemiterUpgrades.DIRECTION);
		
		BlockPos pos = getPos().down();
		TileEntityJumperBlock jbeTe = ((TileEntityJumperBlock) jbe);
		List<IBlockState> baseParts = new ArrayList<IBlockState> ();
			
				baseParts.add(BlockAlchemiter.getBlockState(EnumParts.TOTEM_CORNER, facing));
				baseParts.add(BlockAlchemiter.getBlockState(EnumParts.TOTEM_PAD, facing));
				baseParts.add(BlockAlchemiter.getBlockState(EnumParts.LOWER_ROD, facing));
				baseParts.add(BlockAlchemiter.getBlockState(EnumParts.UPPER_ROD, facing));
			
		
		List<IBlockState> upgBlocks;
		
		
		System.out.println(jbeTe);
		
		if(jbeTe.getLatheUpgradeSlot() == -1)
			upgBlocks = baseParts;
		else
		{
			AlchemiterUpgrade upg = jbeTe.getUpgrade(jbeTe.getLatheUpgradeSlot());
			upgBlocks = upg.getBlocks();
		}
		for(int i = 0; i < upgBlocks.size(); i++)
		{
			IBlockState state = upgBlocks.get(i);
			IBlockState checkPart;
			if(BlockAlchemiterUpgrades.getPart(state) == BlockAlchemiterUpgrades.EnumParts.NONE) checkPart = baseParts.get(i);
			else if(state.getBlock() instanceof BlockAlchemiterUpgrades) checkPart = state.withProperty(BlockAlchemiterUpgrades.DIRECTION, facing);
			else checkPart = state;
			if(BlockAlchemiterUpgrades.getPart(state) == BlockAlchemiterUpgrades.EnumParts.BLANK) continue;
			
			if(!world.getBlockState(pos.up(i)).equals(checkPart))
			{
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

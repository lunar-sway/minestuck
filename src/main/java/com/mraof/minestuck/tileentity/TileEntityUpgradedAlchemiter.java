package com.mraof.minestuck.tileentity;

public class TileEntityUpgradedAlchemiter extends AlchemiterTileEntity
{
	
	/*
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
			
			AlchemiterUpgrades latheUpg = ((TileEntityJumperBlock)jbe).getLatheUpgrade();
			
			if(!AlchemiterUpgrades.nullifiesAlchemiterFunc(latheUpg))
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
		IBlockState[] baseParts = 
			{
				BlockAlchemiter.getBlockState(EnumParts.TOTEM_CORNER, facing),
				BlockAlchemiter.getBlockState(EnumParts.TOTEM_PAD, facing),
				BlockAlchemiter.getBlockState(EnumParts.LOWER_ROD, facing),
				BlockAlchemiter.getBlockState(EnumParts.UPPER_ROD, facing)
			};
		
		IBlockState[] upgBlocks;
		
		if(jbeTe.getLatheUpgradeId() == -1)
			upgBlocks = baseParts;
		else
		{
			AlchemiterUpgrades upg = AlchemiterUpgrades.upgradeList[jbeTe.getLatheUpgradeId()];
			upgBlocks = upg.getUpgradeBlocks();
		}
		for(int i = 0; i < upgBlocks.length; i++)
		{
			IBlockState state = upgBlocks[i];
			IBlockState checkPart;
			if(BlockAlchemiterUpgrades.getPart(state) == BlockAlchemiterUpgrades.EnumParts.NONE) checkPart = baseParts[i];
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
	}*/
}

package com.mraof.minestuck.world.gen.lands;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import com.mraof.minestuck.Minestuck;

public class LandAspectPulse extends LandAspect 
{
	BlockWithMetadata[] upperBlocks = {new BlockWithMetadata(Blocks.netherrack)};
	BlockWithMetadata[] surfaceBlocks = {new BlockWithMetadata(Blocks.obsidian)};
	
	@Override
	public BlockWithMetadata[] getSurfaceBlocks() 
	{
		return surfaceBlocks;
	}

	@Override
	public BlockWithMetadata[] getUpperBlocks() {
		return upperBlocks;
	}
	
	@Override
	public Block getOceanBlock() 
	{
		return Minestuck.blockBlood;
	}

	@Override
	public double[] generateTerrainMap() {
		return null;
	}

	@Override
	public float getRarity() {
		return 0.5F;
	}

	@Override
	public String getPrimaryName() {
		return "Pulse";
	}

	@Override
	public String[] getNames() {
		return new String[] {"Pulse"};
	}

	@Override
	public ArrayList<ILandDecorator> getDecorators() {
		ArrayList<ILandDecorator> list = new ArrayList<ILandDecorator>();
		list.add(new DecoratorVein(Blocks.nether_brick, 10, 32));
//		list.add(new DecoratorVein(Block.ice, 5, 8));
		return list;
	}

	@Override
	public int getDayCycleMode() {
		return 0;
	}

}

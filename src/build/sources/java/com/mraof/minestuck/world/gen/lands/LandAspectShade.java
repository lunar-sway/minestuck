package com.mraof.minestuck.world.gen.lands;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import com.mraof.minestuck.Minestuck;

public class LandAspectShade extends LandAspect 
{

	BlockWithMetadata[] surfaceBlocks = {new BlockWithMetadata(Blocks.lapis_block)};
	BlockWithMetadata[] upperBlocks = {new BlockWithMetadata(Blocks.stone)};
	
	@Override
	public BlockWithMetadata[] getSurfaceBlocks() {
		return surfaceBlocks;
	}
	
	@Override
	public BlockWithMetadata[] getUpperBlocks() {
		return upperBlocks;
	}
	
	@Override
	public Block getOceanBlock() 
	{
		return Minestuck.blockOil;
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
		return "Shade";
	}

	@Override
	public String[] getNames() {
		return new String[] {"Shade"};
	}

	@Override
	public ArrayList<ILandDecorator> getDecorators() {
		ArrayList list = new ArrayList<ILandDecorator>();
		list.add(new DecoratorVein(Blocks.brown_mushroom_block, 10, 32));
//		list.add(new DecoratorVein(Block.ice, 5, 8));
		return list;
	}

	@Override
	public int getDayCycleMode() {
		return 2;
	}


}

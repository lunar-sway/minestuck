package com.mraof.minestuck.world.gen.feature;

import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.feature.OreFeatureConfig;

public class MSFillerBlockTypes
{
	public static final OreFeatureConfig.FillerBlockType SANDSTONE = OreFeatureConfig.FillerBlockType.create("minestuck_sandstone", "minestuck_sandstone", state -> state.getBlock() == Blocks.SANDSTONE);
	public static final OreFeatureConfig.FillerBlockType RED_SANDSTONE = OreFeatureConfig.FillerBlockType.create("minestuck_red_sandstone", "minestuck_red_sandstone", state -> state.getBlock() == Blocks.RED_SANDSTONE);
	public static final OreFeatureConfig.FillerBlockType END_STONE = OreFeatureConfig.FillerBlockType.create("minestuck_end_stone", "minestuck_end_stone", state -> state.getBlock() == Blocks.END_STONE);
	public static final OreFeatureConfig.FillerBlockType SHADE_STONE = OreFeatureConfig.FillerBlockType.create("minestuck_shade_stone", "minestuck_shade_stone", state -> state.getBlock() == MSBlocks.SHADE_STONE);
	public static final OreFeatureConfig.FillerBlockType PINK_STONE = OreFeatureConfig.FillerBlockType.create("minestuck_pink_stone", "minestuck_pink_stone", state -> state.getBlock() == MSBlocks.PINK_STONE);
	
	public static void init(){}
}

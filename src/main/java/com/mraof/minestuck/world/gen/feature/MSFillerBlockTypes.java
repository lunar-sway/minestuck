package com.mraof.minestuck.world.gen.feature;

import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

public class MSFillerBlockTypes
{
	public static final RuleTest SANDSTONE = new BlockMatchTest(Blocks.SANDSTONE);
	public static final RuleTest RED_SANDSTONE = new BlockMatchTest(Blocks.RED_SANDSTONE);
	public static final RuleTest COARSE_END_STONE = new BlockMatchTest(MSBlocks.COARSE_END_STONE);
	public static final RuleTest SHADE_STONE = new BlockMatchTest(MSBlocks.SHADE_STONE);
	public static final RuleTest PINK_STONE = new BlockMatchTest(MSBlocks.PINK_STONE);
	public static final RuleTest MYCELIUM_STONE = new BlockMatchTest(MSBlocks.MYCELIUM_STONE);
	
	public static void init()
	{
	}
}

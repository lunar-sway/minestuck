package com.mraof.minestuck.world.gen.feature;

import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.feature.template.BlockMatchRuleTest;
import net.minecraft.world.gen.feature.template.RuleTest;

public class MSFillerBlockTypes
{
	public static final RuleTest SANDSTONE = new BlockMatchRuleTest(Blocks.SANDSTONE);
	public static final RuleTest RED_SANDSTONE = new BlockMatchRuleTest(Blocks.RED_SANDSTONE);
	public static final RuleTest END_STONE = new BlockMatchRuleTest(Blocks.END_STONE);
	public static final RuleTest PINK_STONE = new BlockMatchRuleTest(MSBlocks.PINK_STONE);
	
	public static void init(){}
}

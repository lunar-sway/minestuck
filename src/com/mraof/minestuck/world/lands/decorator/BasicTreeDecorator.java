package com.mraof.minestuck.world.lands.decorator;

import java.util.Random;

import com.mraof.minestuck.util.Debug;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenTrees;

public class BasicTreeDecorator extends TreeDecoratorBase
{
	WorldGenTrees[] treeTypes;
	
	public BasicTreeDecorator(BlockPlanks.EnumType[] trees, boolean vines)
	{
		this.treeTypes = new WorldGenTrees[trees.length];
		for(int i = 0; i < trees.length; i++) {
			IBlockState logType = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT, trees[i]);
		    IBlockState leafType = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, trees[i]).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
			treeTypes[i] = new WorldGenTrees(false, 5, logType, leafType, vines);
		}
	}
	
	public BasicTreeDecorator(BlockPlanks.EnumType[] trees)
	{
		this(trees, false);
	}
	
	public BasicTreeDecorator(int[] treeTypes, boolean vines)
	{
		this(getEnumTypes(treeTypes), vines);
	}
	
	public BasicTreeDecorator(int[] treeTypes)
	{
		this(getEnumTypes(treeTypes), false);
	}
	
	public BasicTreeDecorator(int treeType, boolean vines)
	{
		this(new BlockPlanks.EnumType[] {BlockPlanks.EnumType.byMetadata(treeType)}, vines);
	}
	
	public BasicTreeDecorator(int treeType)
	{
		this(new BlockPlanks.EnumType[] {BlockPlanks.EnumType.byMetadata(treeType)});
	}
	
	public BasicTreeDecorator()
	{
		this(new BlockPlanks.EnumType[] {BlockPlanks.EnumType.OAK});
	}
	
	@Override
	protected int getTreesPerChunk(Random rand)
	{
		return rand.nextInt(5) + 5;
	}
	
	@Override
	protected WorldGenAbstractTree getTreeToGenerate(World world, BlockPos pos, Random rand)
	{
		return this.treeTypes[rand.nextInt(treeTypes.length)];
	}
	
	@Override
	public float getPriority()
	{
		return 0.6F;
	}
	
	private static BlockPlanks.EnumType[] getEnumTypes(int[] treeTypes) {
		BlockPlanks.EnumType[] eTypes = new BlockPlanks.EnumType[treeTypes.length];
		for(int i = 0; i < treeTypes.length; i++) {
			eTypes[i] = BlockPlanks.EnumType.byMetadata(treeTypes[i]);
		}
		return eTypes;
	}
	
}

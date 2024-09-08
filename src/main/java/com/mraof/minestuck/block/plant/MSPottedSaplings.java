package com.mraof.minestuck.block.plant;

import com.mraof.minestuck.block.AspectTreeBlocks;
import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.neoforged.neoforge.registries.DeferredBlock;

public class MSPottedSaplings
{
	/***
	 * Adding saplings to Blocks.FLOWER_POT allows them to be placed within the empty FLOWER_POT block.
	 * This would need to be done for any potted flowers, should they ever be implemented.
	 * */
	
	public static void addSaplingsToPot() {

		addSapling(MSBlocks.FROST_SAPLING, MSBlocks.POTTED_FROST_SAPLING);
		addSapling(MSBlocks.RAINBOW_SAPLING, MSBlocks.POTTED_RAINBOW_SAPLING);
		addSapling(MSBlocks.END_SAPLING, MSBlocks.POTTED_END_SAPLING);
		addSapling(MSBlocks.SHADEWOOD_SAPLING, MSBlocks.POTTED_SHADEWOOD_SAPLING);
		
		addSapling(AspectTreeBlocks.BLOOD_ASPECT_SAPLING, AspectTreeBlocks.POTTED_BLOOD_ASPECT_SAPLING);
		addSapling(AspectTreeBlocks.BREATH_ASPECT_SAPLING, AspectTreeBlocks.POTTED_BREATH_ASPECT_SAPLING);
		addSapling(AspectTreeBlocks.DOOM_ASPECT_SAPLING, AspectTreeBlocks.POTTED_DOOM_ASPECT_SAPLING);
		addSapling(AspectTreeBlocks.HEART_ASPECT_SAPLING, AspectTreeBlocks.POTTED_HEART_ASPECT_SAPLING);
		addSapling(AspectTreeBlocks.HOPE_ASPECT_SAPLING, AspectTreeBlocks.POTTED_HOPE_ASPECT_SAPLING);
		addSapling(AspectTreeBlocks.LIFE_ASPECT_SAPLING, AspectTreeBlocks.POTTED_LIFE_ASPECT_SAPLING);
		addSapling(AspectTreeBlocks.LIGHT_ASPECT_SAPLING, AspectTreeBlocks.POTTED_LIGHT_ASPECT_SAPLING);
		addSapling(AspectTreeBlocks.MIND_ASPECT_SAPLING, AspectTreeBlocks.POTTED_MIND_ASPECT_SAPLING);
		addSapling(AspectTreeBlocks.RAGE_ASPECT_SAPLING, AspectTreeBlocks.POTTED_RAGE_ASPECT_SAPLING);
		addSapling(AspectTreeBlocks.SPACE_ASPECT_SAPLING, AspectTreeBlocks.POTTED_SPACE_ASPECT_SAPLING);
		addSapling(AspectTreeBlocks.TIME_ASPECT_SAPLING, AspectTreeBlocks.POTTED_TIME_ASPECT_SAPLING);
		addSapling(AspectTreeBlocks.VOID_ASPECT_SAPLING, AspectTreeBlocks.POTTED_VOID_ASPECT_SAPLING);
	}
	
	public static void addSapling(DeferredBlock<? extends Block> sapling, DeferredBlock<Block> pottedSapling) {
		((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(sapling.getId(), pottedSapling);
	}
	
}

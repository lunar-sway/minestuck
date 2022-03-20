package com.mraof.minestuck.world.gen.feature.structure.tiered;

import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockUtil;
import net.minecraft.block.Blocks;
import net.minecraft.state.properties.StructureMode;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraftforge.common.util.Constants;

import java.util.Random;

public class CommonTemplateProcessors
{
	
	/**
	 * Searches through a placed template for data mode structure blocks and adds details/variability to the template if conditions are matched, then removes the structure blocks still left
	 */
	public static void placeAndProcessTemplate(ISeedReader world, MutableBoundingBox boundingBox, Template template, Random rand, ChunkGenerator chunkGeneratorIn, BlockPos templatePos, PlacementSettings settings)
	{
		template.placeInWorld(world, templatePos, templatePos, settings, rand, Constants.BlockFlags.NO_RERENDER);
		
		boolean[] componentChoiceArray = new boolean[]{rand.nextBoolean(), rand.nextBoolean(), rand.nextBoolean(), rand.nextBoolean(), rand.nextBoolean(), rand.nextBoolean()}; //a boolean array for compactness to determine whether a given component type generates at all
		for(Template.BlockInfo blockInfo : template.filterBlocks(templatePos, settings, Blocks.STRUCTURE_BLOCK))
		{
			if(blockInfo.nbt != null)
			{
				StructureMode structuremode = StructureMode.valueOf(blockInfo.nbt.getString("mode"));
				if(structuremode == StructureMode.DATA)
				{
					String data = blockInfo.nbt.getString("metadata");
					//TODO summoners util cause issues
					/*if(data.equals("minestuck:summoner_imp"))
					{
						StructureBlockUtil.placeSummoner(world, boundingBox, blockInfo.pos, MSEntityTypes.IMP);
					}
					if(data.equals("minestuck:summoner_ogre"))
					{
						StructureBlockUtil.placeSummoner(world, boundingBox, blockInfo.pos, MSEntityTypes.OGRE);
					}
					if(data.equals("minestuck:summoner_lich"))
					{
						StructureBlockUtil.placeSummoner(world, boundingBox, blockInfo.pos, MSEntityTypes.IMP);
					}/**/
					
					if(componentChoiceArray[0] && data.equals("minestuck:terrain_hazard_floor"))
					{
						if(rand.nextBoolean())
							world.setBlock(blockInfo.pos.below(), Blocks.MAGMA_BLOCK.defaultBlockState(), Constants.BlockFlags.DEFAULT); //TODO check for each terrain if any are larger than 1 block
					}
					if(componentChoiceArray[1] && data.equals("minestuck:aspect_hazard_floor"))
					{
						if(rand.nextBoolean())
							StructureBlockUtil.createSphere(world, boundingBox, Blocks.AIR.defaultBlockState(), blockInfo.pos.relative(Direction.getRandom(rand), rand.nextInt(2)), rand.nextInt(5), Blocks.STRUCTURE_BLOCK.defaultBlockState());
					}
					if(componentChoiceArray[2] && data.equals("minestuck:organic_terrain_component_floor"))
					{
						if(rand.nextBoolean())
							world.setBlock(blockInfo.pos.below(), Blocks.MAGMA_BLOCK.defaultBlockState(), Constants.BlockFlags.DEFAULT);
					}
					
					/*if(componentChoiceArray[0] && data.equals("minestuck:small_ground_creator"))
					{
						if(rand.nextBoolean())
							StructureBlockUtil.createSphere(world, boundingBox, ground, blockInfo.pos, rand.nextInt(3), null);
					}
					if(componentChoiceArray[1] && data.equals("minestuck:small_air_carve"))
					{
						if(rand.nextBoolean())
							StructureBlockUtil.createSphere(world, boundingBox, Blocks.AIR.defaultBlockState(), blockInfo.pos.relative(Direction.getRandom(rand), rand.nextInt(2)), rand.nextInt(5), Blocks.STRUCTURE_BLOCK.defaultBlockState());
					}
					/**/
					/*if(componentChoiceArray[2] && data.equals("minestuck:organic_terrain_component_floor"))
					{
						if(rand.nextBoolean())
							world.setBlock(blockInfo.pos.below(), Blocks.MAGMA_BLOCK.defaultBlockState(), Constants.BlockFlags.DEFAULT);
					}
					if(componentChoiceArray[3] && data.equals("minestuck:organic_aspect_component_floor"))
					{
						if(rand.nextBoolean())
							world.setBlock(blockInfo.pos, Blocks.BAMBOO.defaultBlockState(), Constants.BlockFlags.DEFAULT);
					}
					if(componentChoiceArray[4] && data.equals("minestuck:organic_terrain_component_ceiling"))
					{
						if(rand.nextBoolean())
							world.setBlock(blockInfo.pos, Blocks.WEEPING_VINES.defaultBlockState(), Constants.BlockFlags.DEFAULT);
					}
					if(componentChoiceArray[5] && data.equals("minestuck:organic_aspect_component_ceiling"))
					{
						if(rand.nextBoolean())
							world.setBlock(blockInfo.pos, Blocks.ANCIENT_DEBRIS.defaultBlockState(), Constants.BlockFlags.DEFAULT);
					}/**/
					
					if(world.getBlockState(blockInfo.pos).getBlock() == Blocks.STRUCTURE_BLOCK)
						world.setBlock(blockInfo.pos, Blocks.AIR.defaultBlockState(), Constants.BlockFlags.DEFAULT);
				}
			}
		}
		//TODO this removes no structure blocks but should be preferred over the other structure block removing function
		//StructureBlockUtil.fillWithBlocksReplaceState(world, boundingBox, new BlockPos(getActualPos(pieceMinX, pieceMinY, pieceMinZ)), new BlockPos(getActualPos(pieceMaxX, pieceMaxY, pieceMaxZ)), Blocks.AIR.defaultBlockState(), Blocks.STRUCTURE_BLOCK.defaultBlockState());
	}
}

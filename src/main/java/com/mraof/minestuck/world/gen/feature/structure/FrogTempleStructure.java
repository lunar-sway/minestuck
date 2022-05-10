package com.mraof.minestuck.world.gen.feature.structure;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class FrogTempleStructure extends Structure<NoFeatureConfig>
{
	public FrogTempleStructure(Codec<NoFeatureConfig> codec)
	{
		super(codec);
	}
	
	@Override
	public IStartFactory<NoFeatureConfig> getStartFactory()
	{
		return Start::new;
	}
	
	@Override
	public GenerationStage.Decoration step()
	{
		return GenerationStage.Decoration.SURFACE_STRUCTURES;
	}
	
	public static class Start extends StructureStart<NoFeatureConfig>
	{
		private Start(Structure<NoFeatureConfig> structure, int chunkX, int chunkZ, MutableBoundingBox boundingBox, int reference, long seed)
		{
			super(structure, chunkX, chunkZ, boundingBox, reference, seed);
		}
		
		@Override
		public void generatePieces(DynamicRegistries registries, ChunkGenerator generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, NoFeatureConfig config)
		{
			int x = chunkX * 16 + random.nextInt(16);
			int z = chunkZ * 16 + random.nextInt(16);
			FrogTemplePiece mainPiece = new FrogTemplePiece(generator, random, x, z);
			pieces.add(mainPiece);
			
			int y = mainPiece.getBoundingBox().y0; //determines height of pillars from the variable height of the main structure
			
			int pillarOffset = 40;
			for(int i = 0; i < 2; i++) //x iterate
			{
				for(int j = 0; j < 2; j++) //z iterate
				{
					if(random.nextBoolean())
					{
						FrogTemplePillarPiece pillarPiece = new FrogTemplePillarPiece(generator, random,
								(mainPiece.getBoundingBox().x0 + mainPiece.getBoundingBox().getXSpan() / 2) + (pillarOffset - 2 * i * pillarOffset), y, //uses frog temple location instead of x and z, so it gets the center of the temple structure
								(mainPiece.getBoundingBox().z0 + mainPiece.getBoundingBox().getZSpan() / 2) + (pillarOffset - 2 * j * pillarOffset)); //center of temple + distance from center with +/- coordinate factor
						pieces.add(pillarPiece); //50% chance of generating a pillar for every corner of the frog temple structure
					}
				}
			}
			calculateBoundingBox();
		}
	}
}
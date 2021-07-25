package com.mraof.minestuck.world.gen.feature.structure;

import com.mojang.datafixers.Dynamic;
import com.mraof.minestuck.Minestuck;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.ScatteredStructure;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.function.Function;

public class FrogTempleStructure extends ScatteredStructure<NoFeatureConfig>
{
	public FrogTempleStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactory)
	{
		super(configFactory);
	}
	
	@Override
	protected int getSeedModifier()
	{
		return 41361201;
	}
	
	@Override
	public IStartFactory getStartFactory()
	{
		return Start::new;
	}
	
	@Override
	public String getStructureName()
	{
		return Minestuck.MOD_ID + ":frog_temple";
	}
	
	@Override
	public int getSize()
	{
		return 16;
	}
	
	@Override
	protected int getBiomeFeatureDistance(ChunkGenerator<?> generator)
	{
		return 140;
	}
	
	@Override
	protected int getBiomeFeatureSeparation(ChunkGenerator<?> generator)
	{
		return 92;
	}
	
	public static class Start extends StructureStart
	{
		private Start(Structure<?> structure, int chunkX, int chunkZ, MutableBoundingBox boundingBox, int reference, long seed) {
			super(structure, chunkX, chunkZ, boundingBox, reference, seed);
		}
		
		@Override
		public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn)
		{
			int x = chunkX * 16 + rand.nextInt(16);
			int z = chunkZ * 16 + rand.nextInt(16);
			FrogTemplePiece mainPiece = new FrogTemplePiece(generator, rand, x, z);
			components.add(mainPiece);
			
			int y = mainPiece.getBoundingBox().minY; //determines height of pillars from the variable height of the main structure
			
			int pillarOffset = 40;
			for(int i = 0; i < 2; i++) //x iterate
			{
				for(int j = 0; j < 2; j++) //z iterate
				{
					if(rand.nextBoolean())
					{
						FrogTemplePillarPiece pillarPiece = new FrogTemplePillarPiece(generator, rand,
								x + (pillarOffset - 2 * i * pillarOffset), y,
								z + (pillarOffset - 2 * j * pillarOffset));
						components.add(pillarPiece); //50% chance of generating a pillar for every corner of the frog temple structure
					}
				}
			}
			recalculateStructureSize();
		}
	}
}
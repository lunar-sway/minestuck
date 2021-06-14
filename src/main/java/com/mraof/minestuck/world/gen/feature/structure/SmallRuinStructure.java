package com.mraof.minestuck.world.gen.feature.structure;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class SmallRuinStructure extends Structure<NoFeatureConfig>
{
	public SmallRuinStructure(Codec<NoFeatureConfig> codec)
	{
		super(codec);
	}
	
	@Override
	public GenerationStage.Decoration step()
	{
		return GenerationStage.Decoration.SURFACE_STRUCTURES;
	}
	
	@Override
	public IStartFactory<NoFeatureConfig> getStartFactory()
	{
		return Start::new;
	}
	
	@Override
	public String getFeatureName()
	{
		return Minestuck.MOD_ID + ":small_ruin";
	}
	
	public static class Start extends StructureStart<NoFeatureConfig>
	{
		private Start(Structure<NoFeatureConfig> structure, int chunkX, int chunkZ, MutableBoundingBox boundingBox, int reference, long seed) {
			super(structure, chunkX, chunkZ, boundingBox, reference, seed);
		}
		
		@Override
		public void generatePieces(DynamicRegistries registries, ChunkGenerator generator, TemplateManager templates, int chunkX, int chunkZ, Biome biome, NoFeatureConfig config)
		{
			SmallRuinPiece piece = new SmallRuinPiece(random, chunkX * 16 + random.nextInt(16), chunkZ * 16 + random.nextInt(16), 0.5F);
			pieces.add(piece);
			calculateBoundingBox();
		}
	}
}
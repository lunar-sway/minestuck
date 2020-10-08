package com.mraof.minestuck.world.gen.feature.structure;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
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
	public IStartFactory<NoFeatureConfig> getStartFactory()
	{
		return Start::new;
	}
	
	@Override
	public String getStructureName()
	{
		return Minestuck.MOD_ID + ":small_ruin";
	}
	
	public static class Start extends StructureStart<NoFeatureConfig>
	{
		private Start(Structure<NoFeatureConfig> structure, int chunkX, int chunkZ, MutableBoundingBox boundingBox, int reference, long seed) {
			super(structure, chunkX, chunkZ, boundingBox, reference, seed);
		}
		
		@Override
		public void func_230364_a_(DynamicRegistries registries, ChunkGenerator generator, TemplateManager templates, int chunkX, int chunkZ, Biome biome, NoFeatureConfig config)
		{
			SmallRuinPiece piece = new SmallRuinPiece(rand, chunkX * 16 + rand.nextInt(16), chunkZ * 16 + rand.nextInt(16), 0.5F);
			components.add(piece);
			recalculateStructureSize();
		}
	}
}
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

public class SmallRuinStructure extends ScatteredStructure<NoFeatureConfig>
{
	public SmallRuinStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactory)
	{
		super(configFactory);
	}
	
	@Override
	protected int getSeedModifier()
	{
		return 59273643;
	}
	
	@Override
	public IStartFactory getStartFactory()
	{
		return Start::new;
	}
	
	@Override
	public String getStructureName()
	{
		return Minestuck.MOD_ID + ":small_ruin";
	}
	
	@Override
	public int getSize()
	{
		return 2;
	}
	
	@Override
	protected int getBiomeFeatureDistance(ChunkGenerator<?> generator)
	{
		return 16;
	}
	
	@Override
	protected int getBiomeFeatureSeparation(ChunkGenerator<?> generator)
	{
		return 4;
	}
	
	public static class Start extends StructureStart
	{
		private Start(Structure<?> structure, int chunkX, int chunkZ, Biome biome, MutableBoundingBox boundingBox, int reference, long seed)
		{
			super(structure, chunkX, chunkZ, biome, boundingBox, reference, seed);
		}
		
		@Override
		public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn)
		{
			SmallRuinPiece piece = new SmallRuinPiece(rand, chunkX * 16 + rand.nextInt(16), chunkZ * 16 + rand.nextInt(16), 0.5F);
			components.add(piece);
			recalculateStructureSize();
		}
	}
}
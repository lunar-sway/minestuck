package com.mraof.minestuck.world.gen.feature.structure.tiered.tier1;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.feature.structure.SmallRuinPiece;
import com.mraof.minestuck.world.gen.feature.structure.SmallRuinStructure;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.ScatteredStructurePiece;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;
import java.util.function.Function;

public class TierOneDungeonStructure extends Structure<NoFeatureConfig>
{
	public TierOneDungeonStructure(Codec<NoFeatureConfig> codec)
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
	
	/*public TierOneDungeonStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactory)
	{
		super(configFactory);
	}
	
	@Override
	protected int getSeedModifier()
	{
		return 41361202;
	}
	
	@Override
	public IStartFactory getStartFactory()
	{
		return Start::new;
	}
	
	@Override
	public String getStructureName()
	{
		return Minestuck.MOD_ID + ":tier_one_dungeon";
	}
	
	@Override
	public int getSize()
	{
		return 24;
	}
	
	@Override
	protected int getBiomeFeatureDistance(ChunkGenerator<?> generator)
	{
		return 10;
	}
	
	@Override
	protected int getBiomeFeatureSeparation(ChunkGenerator<?> generator)
	{
		return 4;
	}
	
	public static class Start extends StructureStart
	{
		private Start(Structure<?> structure, int chunkX, int chunkZ, MutableBoundingBox boundingBox, int reference, long seed)
		{
			super(structure, chunkX, chunkZ, boundingBox, reference, seed);
		}
		
		@Override
		public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn)
		{
			int x = chunkX * 16 + rand.nextInt(16);
			int z = chunkZ * 16 + rand.nextInt(16);
			TierOneDungeonEntryPiece mainPiece = new TierOneDungeonEntryPiece(templateManagerIn, generator, rand, x, z);
			components.add(mainPiece);
			
			TierOneDungeonSecondaryPiece secondaryPiece = new TierOneDungeonSecondaryPiece(templateManagerIn, mainPiece.getCoordBaseMode(), x, mainPiece.getBoundingBox().minY, z);
			
			components.add(secondaryPiece);
			
			recalculateStructureSize();
		}
	}*/
}
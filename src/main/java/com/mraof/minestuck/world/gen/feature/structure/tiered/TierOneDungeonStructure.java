package com.mraof.minestuck.world.gen.feature.structure.tiered;

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

public class TierOneDungeonStructure extends ScatteredStructure<NoFeatureConfig>
{
	public TierOneDungeonStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactory)
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
		private Start(Structure<?> structure, int chunkX, int chunkZ, MutableBoundingBox boundingBox, int reference, long seed) {
			super(structure, chunkX, chunkZ, boundingBox, reference, seed);
		}
		
		@Override
		public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn)
		{
			int x = chunkX * 16 + rand.nextInt(16);
			int z = chunkZ * 16 + rand.nextInt(16);
			TierOneDungeonPiece mainPiece = new TierOneDungeonPiece(templateManagerIn, generator, rand, x, z);
			components.add(mainPiece);
			/*
			int y = mainPiece.getBoundingBox().minY; //determines height of pillars from the variable height of the main structure
			
			int firstRoomOffset = mainPiece.getBoundingBox().maxX; //x and y should be same for this to work
			TierOneDungeonFirstRoomPiece firstRoomPiece = new TierOneDungeonFirstRoomPiece(generator, rand, x + (firstRoomOffset), y, z);
			Debug.debugf("firstRoomPiece = %s", firstRoomPiece);
			components.add(firstRoomPiece);*/
			/*for(int i = 0; i < 2; i++) //x iterate
			{
				for(int j = 0; j < 2; j++) //z iterate
				{
					if(rand.nextBoolean() && i != 0)
					{
						Debug.debugf("additional piece");
						/*TierOneDungeonFirstRoomPiece additionalRoomPiece = new TierOneDungeonFirstRoomPiece(generator, rand,
								x + (firstRoomOffset - 2 * i * firstRoomOffset), y,
								z + (firstRoomOffset - 2 * j * firstRoomOffset));
						components.add(additionalRoomPiece);*/ //50% chance of generating additional room in the remaining directions after original
					/*}
				}
			}*/
			recalculateStructureSize();
		}
	}
}
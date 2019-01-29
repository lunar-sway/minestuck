package com.mraof.minestuck.world.gen.structure;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureStart;

import javax.annotation.Nullable;

/*
Then you'll have to refer to its generate() and generateStructure() methods in ChunkProviderLands same as structureHandler and villageHandler

And then you'll need a varaible to store your MapGenStructure instance too, and get it there from LandAspectRain...

override customMapGenStructure()in the terrain landspect class to take care of that last part

 */

public class MapGenCloudDungeon extends MapGenStructure
{
	
	@Override
	protected boolean canSpawnStructureAtCoords(int chunkX, int chunkY)
	{
		return Math.abs(chunkX) > 3 || Math.abs(chunkY) > 3;
	}
	
	@Override
	protected StructureStart getStructureStart(int par1, int par2)
	{
		return new StructureCloudDungeonStart(this.world, this.rand, par1, par2);
	}
	
	@Override
	public String getStructureName()
	{
		return "CloudDungeon";
	}
	
	@Nullable
	@Override
	public BlockPos getNearestStructurePos(World worldIn, BlockPos pos, boolean findUnexplored)
	{
		return null;
	}
}
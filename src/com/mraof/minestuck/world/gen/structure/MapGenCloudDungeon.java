/**
 * 
 */
package com.mraof.minestuck.world.gen.structure;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureStart;

import javax.annotation.Nullable;

public class MapGenCloudDungeon extends MapGenStructure
{
	
	@Override
	protected boolean canSpawnStructureAtCoords(int chunkX, int chunkY)
	{
		int var3 = chunkX >> 4;
		int var4 = chunkY >> 4;
		this.rand.setSeed((long)(var3 ^ var4 << 4) ^ this.world.getSeed());
		this.rand.nextInt();
		return chunkX == 1 && chunkY == 0;//this.rand.nextInt(3) != 0 ? false : (par1 != (var3 << 4) + 4 + this.rand.nextInt(8) ? false : par2 == (var4 << 4) + 4 + this.rand.nextInt(8));
	}
	
	@Override
	protected StructureStart getStructureStart(int par1, int par2)
	{
		return new StructureCastleStart(this.world, this.rand, par1, par2, rand.nextBoolean());
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
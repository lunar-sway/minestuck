package com.mraof.minestuck.world.gen.feature.structure.castle;

import com.mojang.datafixers.Dynamic;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;

import java.util.Random;
import java.util.function.Function;

/**
 * @author mraof
 *
 */
public class CastleStructure extends Structure<NoFeatureConfig>
{
	public CastleStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn)
	{
		super(configFactoryIn);
	}
	
	@Override
	public boolean hasStartAt(ChunkGenerator<?> chunkGen, Random rand, int chunkPosX, int chunkPosZ)
	{
        int var3 = chunkPosX >> 4;
        int var4 = chunkPosZ >> 4;
        rand.setSeed((long)(var3 ^ var4 << 4) ^ chunkGen.getSeed());
        rand.nextInt();
        return chunkPosX == 1 && chunkPosZ == 0;//this.rand.nextInt(3) != 0 ? false : (par1 != (var3 << 4) + 4 + this.rand.nextInt(8) ? false : par2 == (var4 << 4) + 4 + this.rand.nextInt(8));
    }
	
	@Override
	public IStartFactory getStartFactory()
	{
        return StructureCastleStart::new;
    }
	
	@Override
	public int getSize()
	{
		return 5;	//Dunno
	}
	
	@Override
	public String getStructureName()
	{
		return "SkaiaCastle";
	}
}
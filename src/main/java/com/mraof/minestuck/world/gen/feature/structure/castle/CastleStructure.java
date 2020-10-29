package com.mraof.minestuck.world.gen.feature.structure.castle;

import com.mojang.datafixers.Dynamic;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
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
	public boolean canBeGenerated(BiomeManager biomeManagerIn, ChunkGenerator<?> generator, Random rand, int chunkX, int chunkZ, Biome biomeIn)
	{
        int var3 = chunkX >> 4;
        int var4 = chunkZ >> 4;
        rand.setSeed((long)(var3 ^ var4 << 4) ^ generator.getSeed());
        rand.nextInt();
        return chunkX == 1 && chunkZ == 0;//this.rand.nextInt(3) != 0 ? false : (par1 != (var3 << 4) + 4 + this.rand.nextInt(8) ? false : par2 == (var4 << 4) + 4 + this.rand.nextInt(8));
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
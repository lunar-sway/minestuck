/**
 * 
 */
package com.mraof.minestuck.world.gen.structure;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureStart;

import com.mraof.minestuck.entity.carapacian.EntityBlackPawn;

/**
 * @author mraof
 *
 */
public class MapGenCastle extends MapGenStructure
{
	
    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkY)
    {
        int var3 = chunkX >> 4;
        int var4 = chunkY >> 4;
        this.rand.setSeed((long)(var3 ^ var4 << 4) ^ this.worldObj.getSeed());
        this.rand.nextInt();
        return chunkX == 1 && chunkY == 0;//this.rand.nextInt(3) != 0 ? false : (par1 != (var3 << 4) + 4 + this.rand.nextInt(8) ? false : par2 == (var4 << 4) + 4 + this.rand.nextInt(8));
    }

    protected StructureStart getStructureStart(int par1, int par2)
    {
        return new StructureCastleStart(this.worldObj, this.rand, par1, par2, rand.nextBoolean());
    }
    
	@Override
	public String getStructureName()
	{
		return "SkaiaCastle";
	}
	
}

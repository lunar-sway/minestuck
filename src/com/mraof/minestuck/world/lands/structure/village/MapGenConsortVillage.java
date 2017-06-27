package com.mraof.minestuck.world.lands.structure.village;

import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.biome.BiomeMinestuck;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class MapGenConsortVillage extends MapGenStructure
{
	private static final List<Biome> BIOMES = Arrays.asList(BiomeMinestuck.mediumNormal);
	
	private static final int VILLAGE_DISTANCE = 24;
	private static final int MIN_VILLAGE_DISTANCE = 5;
	
	private final ChunkProviderLands chunkProvider;
	
	public MapGenConsortVillage(ChunkProviderLands chunkProvider)
	{
		this.chunkProvider = chunkProvider;
	}
	
	@Override
	public String getStructureName()
	{
		return "ConsortVillagePiece";
	}
	
	@Override
	protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ)
	{
		int i = chunkX;
		int j = chunkZ;
		
		if(chunkX < 0)
		{
			chunkX -= VILLAGE_DISTANCE - 1;
		}
		
		if(chunkZ < 0)
		{
			chunkZ -= VILLAGE_DISTANCE - 1;
		}
		
		int k = chunkX / VILLAGE_DISTANCE;
		int l = chunkZ / VILLAGE_DISTANCE;
		Random random = this.world.setRandomSeed(k, l, 10387312);
		k = k * VILLAGE_DISTANCE;
		l = l * VILLAGE_DISTANCE;
		k = k + random.nextInt(VILLAGE_DISTANCE - MIN_VILLAGE_DISTANCE);
		l = l + random.nextInt(VILLAGE_DISTANCE - MIN_VILLAGE_DISTANCE);
		
		if(i == k && j == l)
		{
			boolean flag = this.world.getBiomeProvider().areBiomesViable(i * 16 + 8, j * 16 + 8, 0, BIOMES);
			if(flag)
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	protected StructureStart getStructureStart(int chunkX, int chunkZ)
	{
		return new Start(chunkProvider, world, this.rand, chunkX, chunkZ);
	}
	
	@Nullable
	@Override
	public BlockPos getClosestStrongholdPos(World worldIn, BlockPos pos, boolean p_180706_3_)
	{
		return null;
	}
	
	public BlockPos findAndMarkNextVillage(EntityPlayerMP player, String type, NBTTagList list)
	{
		HashSet<Long> set = new HashSet<Long>(list.tagCount());
		for(int i = 0; i < list.tagCount(); i++)
			set.add(((NBTTagLong) list.get(i)).getLong());
		
		for(long l : this.structureMap.keySet())
		{
			if(!set.contains(l))
			{
				StructureStart start = this.structureMap.get(l);
				list.appendTag(new NBTTagLong(l));
				return new BlockPos(start.getChunkPosX()*16 + 8, 90, start.getChunkPosZ()*16 + 8);
			}
		}
		
		Debug.warn("Couldn't find village");
		return null;
	}
	
	public static class Start extends StructureStart
	{
		public Start()
		{
		
		}
		
		public Start(ChunkProviderLands provider, World world, Random rand, int chunkX, int chunkZ)
		{
			super(chunkX, chunkZ);
			LandAspectRegistry.AspectCombination landAspects = new LandAspectRegistry.AspectCombination(provider.aspect1, provider.aspect2);
			List<ConsortVillageComponents.PieceWeight> pieceWeightList = ConsortVillageComponents.getStructureVillageWeightedPieceList(rand, landAspects.aspectTerrain.getConsortType(), landAspects);
			ConsortVillageCenter.VillageCenter start = ConsortVillageCenter.getVillageStart(provider, (chunkX << 4) + rand.nextInt(16), (chunkZ << 4) + rand.nextInt(16), rand, pieceWeightList, landAspects);
			components.add(start);
			start.buildComponent(start, components, rand);
			
			while(!start.pendingHouses.isEmpty() || !start.pendingRoads.isEmpty())
			{
				if(!start.pendingRoads.isEmpty())
				{
					int index = rand.nextInt(start.pendingRoads.size());
					StructureComponent component = start.pendingRoads.remove(index);
					component.buildComponent(start, components, rand);
				} else
				{
					int index = rand.nextInt(start.pendingHouses.size());
					StructureComponent component = start.pendingHouses.remove(index);
					component.buildComponent(start, components, rand);
				}
			}
			updateBoundingBox();
		}
	}
}
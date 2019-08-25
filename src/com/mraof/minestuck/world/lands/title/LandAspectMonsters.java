package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.world.biome.LandWrapperBiome;
import com.mraof.minestuck.world.lands.LandDimension;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome.SpawnListEntry;

import java.util.ArrayList;
import java.util.List;

public class LandAspectMonsters extends TitleLandAspect
{
	
	private final Variant type;
	private final List<SpawnListEntry> monsterList;
	
	public static TitleLandAspect[] createTypes()
	{
		LandAspectMonsters parent = new LandAspectMonsters(Variant.MONSTERS, null);
		return new TitleLandAspect[]{parent.setRegistryName("monsters"),
				new LandAspectMonsters(Variant.UNDEAD, parent).setRegistryName("undead")};
	}
	
	private LandAspectMonsters(Variant type, LandAspectMonsters parent)
	{
		super(parent, EnumAspect.RAGE);
		this.type = type;
		this.monsterList = new ArrayList<>();
		if(this.type == Variant.MONSTERS)
		{
			monsterList.add(new SpawnListEntry(EntityType.CREEPER, 1, 1, 1));
			monsterList.add(new SpawnListEntry(EntityType.SPIDER, 1, 1, 2));
			monsterList.add(new SpawnListEntry(EntityType.ZOMBIE, 1, 1, 2));
		}
		else if(this.type == Variant.UNDEAD)
		{
			monsterList.add(new SpawnListEntry(EntityType.ZOMBIE, 2, 1, 3));
			monsterList.add(new SpawnListEntry(EntityType.SKELETON, 1, 1, 2));
		}
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {"monster"};
	}
	
	@Override
	public void prepareWorldProvider(LandDimension worldProvider)
	{
		worldProvider.skylightBase = Math.min(1/4F, worldProvider.skylightBase);
		worldProvider.mergeFogColor(new Vec3d(0.1, 0, 0), 0.5F);
	}
	
	@Override
	public void setBiomeGenSettings(LandWrapperBiome biome, StructureBlockRegistry blockRegistry)
	{
		for(SpawnListEntry entry : this.monsterList)
			biome.addSpawn(EntityClassification.MONSTER, entry);
	}
	
	//@Override
	public void prepareChunkProviderServer(ChunkProviderLands chunkProvider)
	{
		chunkProvider.blockRegistry.setBlockState("structure_wool_2", Blocks.LIGHT_GRAY_WOOL.getDefaultState());
		chunkProvider.blockRegistry.setBlockState("carpet", Blocks.PURPLE_CARPET.getDefaultState());
		if(chunkProvider.blockRegistry.getCustomBlock("torch") == null)
			chunkProvider.blockRegistry.setBlockState("torch", Blocks.REDSTONE_TORCH.getDefaultState());
	}
	
	public enum Variant
	{
		MONSTERS,
		UNDEAD;
	}
}
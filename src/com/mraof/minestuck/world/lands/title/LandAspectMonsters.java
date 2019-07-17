package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.world.WorldProviderLands;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;
import net.minecraft.block.BlockColored;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome.SpawnListEntry;

import java.util.ArrayList;
import java.util.List;

public class LandAspectMonsters extends TitleLandAspect
{
	
	private final Variant type;
	private final List<TitleLandAspect> variations;
	private final List<SpawnListEntry> monsterList;
	
	public LandAspectMonsters()
	{
		this(Variant.MONSTERS);
	}
	
	public LandAspectMonsters(Variant type)
	{
		this.variations = new ArrayList<>();
		this.type = type;
		this.monsterList = new ArrayList<>();
		if(this.type == Variant.MONSTERS)
		{
			monsterList.add(new SpawnListEntry(EntityCreeper.class, 1, 1, 1));
			monsterList.add(new SpawnListEntry(EntitySpider.class, 1, 1, 2));
			monsterList.add(new SpawnListEntry(EntityZombie.class, 1, 1, 2));
			variations.add(this);
			variations.add(new LandAspectMonsters(Variant.MONSTERS_DEAD));
		}
		else if(this.type == Variant.MONSTERS_DEAD)
		{
			monsterList.add(new SpawnListEntry(EntityZombie.class, 2, 1, 3));
			monsterList.add(new SpawnListEntry(EntitySkeleton.class, 1, 1, 2));
		}
	}
	
	@Override
	public String getPrimaryName()
	{
		return type.getName();
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {"monster"};
	}
	
	@Override
	public void prepareWorldProvider(WorldProviderLands worldProvider)
	{
		worldProvider.skylightBase = Math.min(1/4F, worldProvider.skylightBase);
		worldProvider.mergeFogColor(new Vec3d(0.1, 0, 0), 0.5F);
	}
	
	@Override
	public void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
		chunkProvider.monsterList.addAll(this.monsterList);
	}
	
	@Override
	public void prepareChunkProviderServer(ChunkProviderLands chunkProvider)
	{
		chunkProvider.blockRegistry.setBlockState("structure_wool_2", Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.SILVER));
		chunkProvider.blockRegistry.setBlockState("carpet", Blocks.CARPET.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.PURPLE));
		if(chunkProvider.blockRegistry.getCustomBlock("torch") == null)
			chunkProvider.blockRegistry.setBlockState("torch", Blocks.REDSTONE_TORCH.getDefaultState());
	}
	
	@Override
	public List<TitleLandAspect> getVariations()
	{
		return variations;
	}
	
	@Override
	public TitleLandAspect getPrimaryVariant()
	{
		return LandAspectRegistry.fromNameTitle("monsters");
	}
	
	public static enum Variant
	{
		MONSTERS,
		MONSTERS_DEAD;
		public String getName()
		{
			return this.toString().toLowerCase();
		}
	}
}
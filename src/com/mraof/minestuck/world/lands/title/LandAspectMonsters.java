package com.mraof.minestuck.world.lands.title;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import net.minecraftforge.common.ChestGenHooks;

import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.terrain.TerrainAspect;

public class LandAspectMonsters extends TitleAspect
{
	
	private final String name;
	private final List<TitleAspect> variations;
	private final List<SpawnListEntry> monsterList;
	
	public LandAspectMonsters()
	{
		this("Monsters");
	}
	
	public LandAspectMonsters(String name)
	{
		this.variations = new ArrayList<TitleAspect>();
		this.name = name;
		this.monsterList = new ArrayList<SpawnListEntry>();
		if(this.name.equals("Monsters"))
		{
			monsterList.add(new SpawnListEntry(EntityCreeper.class, 1, 1, 2));
			monsterList.add(new SpawnListEntry(EntitySpider.class, 1, 1, 2));
			monsterList.add(new SpawnListEntry(EntityZombie.class, 1, 1, 2));
			variations.add(this);
			variations.add(new LandAspectMonsters("MonstersDead"));
		}
		else if(this.name.equals("MonstersDead"))
		{
			monsterList.add(new SpawnListEntry(EntityZombie.class, 2, 1, 3));
			monsterList.add(new SpawnListEntry(EntitySkeleton.class, 1, 1, 2));
		}
	}
	
	@Override
	public String getPrimaryName()
	{
		return name;
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {"monster"};
	}
	
	@Override
	protected void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
		chunkProvider.dayCycle = 2;
		chunkProvider.monsterList.add(new SpawnListEntry(EntityCreeper.class, 1, 1, 2));
		chunkProvider.monsterList.add(new SpawnListEntry(EntitySpider.class, 1, 1, 2));
		chunkProvider.monsterList.add(new SpawnListEntry(EntityZombie.class, 1, 1, 2));
		
		if(chunkProvider.decorators != null)
		{
			ChestGenHooks chestGen = chunkProvider.lootMap.get(AlchemyRecipeHandler.BASIC_MEDIUM_CHEST);
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(Items.rotten_flesh, 1, 0), 2, 8, 6));
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(Items.bone, 1, 0), 1, 4, 5));
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(Items.gunpowder, 1, 0), 2, 8, 4));
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(Items.string, 1, 0), 2, 8, 4));
			chestGen.removeItem(new ItemStack(Items.ender_pearl, 1, 0));	//Re-add ender pearl with a slightly higher priority and amount
			chestGen.addItem(new WeightedRandomChestContent(new ItemStack(Items.ender_pearl, 1, 0), 1, 4, 2));
		}
	}
	
	@Override
	public boolean isAspectCompatible(TerrainAspect aspect)
	{
		return aspect.getDayCycleMode() != 1;
	}
	
	@Override
	public List<TitleAspect> getVariations()
	{
		return variations;
	}
	
	@Override
	public TitleAspect getPrimaryVariant()
	{
		return LandAspectRegistry.fromName2("Monsters");
	}
	
}
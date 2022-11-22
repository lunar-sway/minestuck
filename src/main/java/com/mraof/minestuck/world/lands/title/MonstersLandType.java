package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class MonstersLandType extends TitleLandType
{
	public static final String MONSTERS = "minestuck.monsters";
	
	public static final ResourceLocation GROUP_NAME = new ResourceLocation(Minestuck.MOD_ID, "monsters");
	private final Variant type;
	
	public MonstersLandType(Variant type)
	{
		super(EnumAspect.RAGE, GROUP_NAME);
		this.type = type;
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {MONSTERS};
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlock("structure_wool_2", Blocks.LIGHT_GRAY_WOOL);
		registry.setBlock("carpet", Blocks.PURPLE_CARPET);
		if(registry.isUsingDefault("torch"))
			registry.setBlock("torch", Blocks.REDSTONE_TORCH);
		if(registry.isUsingDefault("wall_torch"))
			registry.setBlock("wall_torch", Blocks.REDSTONE_WALL_TORCH);
	}
	
	@Override
	public void setProperties(LandProperties properties)
	{
		properties.skylightBase = Math.min(1/4F, properties.skylightBase);
		properties.mergeFogColor(new Vec3(0.1, 0, 0), 0.5F);
	}
	
	@Override
	public void setSpawnInfo(MobSpawnSettings.Builder builder, LandBiomeType type)
	{
		if(this.type == Variant.MONSTERS)
		{
			builder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.CREEPER, 1, 1, 1));
			builder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SPIDER, 1, 1, 2));
			builder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, 1, 1, 2));
		}
		else if(this.type == Variant.UNDEAD)
		{
			builder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, 2, 1, 3));
			builder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 1, 1, 2));
		}
	}
	
	@Override
	public SoundEvent getBackgroundMusic()
	{
		return type == Variant.UNDEAD ? MSSoundEvents.MUSIC_UNDEAD.get() : MSSoundEvents.MUSIC_MONSTERS.get();
	}
	
	public enum Variant
	{
		MONSTERS,
		UNDEAD
	}
}
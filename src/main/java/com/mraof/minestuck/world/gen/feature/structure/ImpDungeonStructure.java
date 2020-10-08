package com.mraof.minestuck.world.gen.feature.structure;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;

public class ImpDungeonStructure extends Structure<NoFeatureConfig>
{
	public ImpDungeonStructure(Codec<NoFeatureConfig> codec)
	{
		super(codec);
	}
	
	@Override
	public IStartFactory<NoFeatureConfig> getStartFactory()
	{
		return ImpDungeonStart::new;
	}
	
	@Override
	public String getStructureName()
	{
		return Minestuck.MOD_ID + ":imp_dungeon";
	}
}
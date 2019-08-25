package com.mraof.minestuck.world.lands;

import com.mraof.minestuck.world.biome.LandBiomeHolder;
import com.mraof.minestuck.world.biome.LandWrapperBiome;
import com.mraof.minestuck.world.lands.gen.LandGenSettings;
import com.mraof.minestuck.world.lands.structure.IGateStructure;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;

import javax.annotation.Nullable;

public interface ILandAspect<A extends ILandAspect>
{
	/**
	 * Returns a list of strings used in giving a land a random name.
	 */
	String[] getNames();
	
	IGateStructure getGateStructure();
	
	boolean canBePickedAtRandom();
	
	@Nullable
	A getParent();	//TODO had an idea of creating groups (perhaps identified by a string/resource location) to replace the parent system
	
	@SuppressWarnings("unchecked")
	default A getParentOrThis()
	{
		A parent = this.getParent();
		if(parent == null)
			return (A) this;
		else return parent;
	}
	
	default void setBiomeSettings(LandBiomeHolder settings)
	{}
	
	default void setGenSettings(LandGenSettings settings)
	{}
	
	default void setBiomeGenSettings(LandWrapperBiome biome, StructureBlockRegistry blockRegistry)
	{}
}

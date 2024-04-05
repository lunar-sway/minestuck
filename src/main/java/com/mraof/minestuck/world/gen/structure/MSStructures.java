package com.mraof.minestuck.world.gen.structure;

import com.mraof.minestuck.Minestuck;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;

@FieldsAreNonnullByDefault
public final class MSStructures
{
	// Overworld
	public static final ResourceKey<Structure> FROG_TEMPLE = key("frog_temple");
	
	// Land
	public static final ResourceKey<Structure> LAND_GATE = key("land_gate");
	public static final ResourceKey<Structure> SMALL_RUIN = key("small_ruin");
	public static final ResourceKey<Structure> IMP_DUNGEON = key("imp_dungeon");
	public static final ResourceKey<Structure> CONSORT_VILLAGE = key("consort_village");
	
	// Wood
	public static final ResourceKey<Structure> LARGE_WOOD_OBJECT = key("large_wood_object");
	
	// Skaia
	public static final ResourceKey<Structure> SKAIA_CASTLE = key("skaia_castle");
	
	private static ResourceKey<Structure> key(String name)
	{
		return ResourceKey.create(Registries.STRUCTURE, new ResourceLocation(Minestuck.MOD_ID, name));
	}
}
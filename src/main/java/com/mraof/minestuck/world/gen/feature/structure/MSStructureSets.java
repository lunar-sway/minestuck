package com.mraof.minestuck.world.gen.feature.structure;

import com.mraof.minestuck.Minestuck;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class MSStructureSets
{
	public static final DeferredRegister<StructureSet> REGISTER = DeferredRegister.create(Registry.STRUCTURE_SET_REGISTRY, Minestuck.MOD_ID);
	
	public static final RegistryObject<StructureSet> FROG_TEMPLE = REGISTER.register("frog_temple", () -> new StructureSet(MSStructureFeatures.FROG_TEMPLE.getHolder().orElseThrow(), new RandomSpreadStructurePlacement(140, 92, RandomSpreadType.LINEAR, 41361201)));
	
}
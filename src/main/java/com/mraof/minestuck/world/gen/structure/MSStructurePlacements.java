package com.mraof.minestuck.world.gen.structure;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.structure.gate.LandGatePlacement;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class MSStructurePlacements
{
	public static final DeferredRegister<StructurePlacementType<?>> REGISTER = DeferredRegister.create(Registries.STRUCTURE_PLACEMENT, Minestuck.MOD_ID);
	
	public static final RegistryObject<StructurePlacementType<LandGatePlacement>> LAND_GATE = REGISTER.register("land_gate", () -> () -> LandGatePlacement.CODEC);
}
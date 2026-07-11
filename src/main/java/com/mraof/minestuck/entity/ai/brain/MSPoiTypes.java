package com.mraof.minestuck.entity.ai.brain;

import com.google.common.collect.ImmutableSet;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;
import java.util.function.Supplier;

public class MSPoiTypes
{
	public static final DeferredRegister<PoiType> REGISTER = DeferredRegister.create(BuiltInRegistries.POINT_OF_INTEREST_TYPE, Minestuck.MOD_ID);
	
	public static final ResourceKey<PoiType> CRUXTRUDER_KEY = createKey("cruxtruder");
	public static final ResourceKey<PoiType> TOTEM_LATHE_KEY = createKey("totem_lathe");
	public static final ResourceKey<PoiType> ALCHEMITER_KEY = createKey("alchemiter");
	public static final ResourceKey<PoiType> GATE_KEY = createKey("gate");
	
	public static final Supplier<PoiType> CRUXTRUDER = REGISTER.register("cruxtruder", () -> new PoiType(getBlockStates(MSBlocks.CRUXTRUDER.getMainBlock()), 0, 1));
	public static final Supplier<PoiType> TOTEM_LATHE = REGISTER.register("totem_lathe", () -> new PoiType(getBlockStates(MSBlocks.TOTEM_LATHE.getMainBlock()), 0, 1));
	public static final Supplier<PoiType> ALCHEMITER = REGISTER.register("alchemiter", () -> new PoiType(getBlockStates(MSBlocks.ALCHEMITER.getMainBlock()), 0, 1));
	public static final Supplier<PoiType> GATE = REGISTER.register("gate", () -> new PoiType(getBlockStates(MSBlocks.GATE_MAIN.get()), 0, 1));
	
	private static ResourceKey<PoiType> createKey(String name) {
		return ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, Minestuck.id(name));
	}
	
	private static Set<BlockState> getBlockStates(Block block) {
		return ImmutableSet.copyOf(block.getStateDefinition().getPossibleStates());
	}
}

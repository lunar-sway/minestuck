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
	
	public static final ResourceKey<PoiType> ALCHEMITER_KEY = createKey("alchemiter");
	
	public static final Supplier<PoiType> ALCHEMITER = REGISTER.register("alchemiter", () -> new PoiType(getBlockStates(MSBlocks.ALCHEMITER.getMainBlock()), 1, 1));
	
	private static ResourceKey<PoiType> createKey(String name) {
		return ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, Minestuck.id(name));
	}
	
	private static Set<BlockState> getBlockStates(Block block) {
		return ImmutableSet.copyOf(block.getStateDefinition().getPossibleStates());
	}
	/*private static PoiType register(Registry<PoiType> key, ResourceKey<PoiType> value, Set<BlockState> matchingStates, int maxTickets, int validRange) {
		PoiType poitype = new PoiType(matchingStates, maxTickets, validRange);
		Registry.register(REGISTER, value, poitype);
		//registerBlockStates(key.getHolderOrThrow(value), matchingStates);
		return poitype;
	}
	
	private static DeferredHolder<PoiType, PoiType> register(String name, Set<BlockState> matchingStates, int maxTickets, int validRange)
	{
		PoiType poitype = new PoiType(matchingStates, maxTickets, validRange);
		return REGISTER.register(name, () -> poitype);
	}*/
	
	//ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, ResourceLocation.withDefaultNamespace(name));
}

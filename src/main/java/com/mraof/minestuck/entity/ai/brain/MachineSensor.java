package com.mraof.minestuck.entity.ai.brain;

import com.mojang.datafixers.util.Pair;
import com.mraof.minestuck.entity.KernelspriteEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class MachineSensor extends Sensor<KernelspriteEntity>
{
	public MachineSensor() {
		super(40);
	}
	
	@Override
	public Set<MemoryModuleType<?>> requires()
	{
		return Set.of(MSMemoryModuleType.MACHINE_LOCATIONS.get());
	}
	
	@Override
	protected void doTick(ServerLevel level, KernelspriteEntity entity)
	{
		Brain<?> brain = entity.getBrain();
		ResourceKey<Level> key = level.dimension();
		
		Optional<List<GlobalPos>> oMachineMemories = brain.getMemory(MSMemoryModuleType.MACHINE_LOCATIONS.get());
		if(oMachineMemories.isPresent()) {
			oMachineMemories.get().forEach(machineMemory -> {
				if(machineMemory.dimension() != key) {
					brain.eraseMemory(MSMemoryModuleType.MACHINE_LOCATIONS.get());
					
				}
				
			});
		} else {
			PoiManager manager = level.getPoiManager();
			Optional<Pair<Holder<PoiType>, BlockPos>> oAlchemiter = manager.findClosestWithType((poi) -> poi.is(MSPoiTypes.ALCHEMITER_KEY), entity.blockPosition(), 3, PoiManager.Occupancy.ANY);
			oAlchemiter.ifPresent(holderBlockPosPair -> machinePos.add(new GlobalPos(key, holderBlockPosPair.getSecond())));
		}
		
		
		
		
		
		
		
		Set<GlobalPos> machinePos = new HashSet<>();
		
		
		
		
		Optional<Pair<Holder<PoiType>, BlockPos>> oAlchemiter = manager.findClosestWithType((poi) -> poi.is(MSPoiTypes.ALCHEMITER_KEY), entity.blockPosition(), 24, PoiManager.Occupancy.ANY);
		oAlchemiter.ifPresent(holderBlockPosPair -> machinePos.add(new GlobalPos(key, holderBlockPosPair.getSecond())));
		
		
		if(!machinePos.isEmpty())
		{
			brain.setMemory(MSMemoryModuleType.MACHINE_LOCATIONS.get(), machinePos.stream().toList());
		} else
		{
			brain.eraseMemory(MSMemoryModuleType.MACHINE_LOCATIONS.get());
		}
	}
}

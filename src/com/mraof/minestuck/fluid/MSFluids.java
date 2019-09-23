package com.mraof.minestuck.fluid;

import com.mraof.minestuck.Minestuck;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;


@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MSFluids
{
	public static final FlowingFluid FLOWING_OIL = new OilFluid.Flowing();
	public static final FlowingFluid OIL = new OilFluid.Source();
	public static final FlowingFluid FLOWING_BLOOD = new BloodFluid.Flowing();
	public static final FlowingFluid BLOOD = new BloodFluid.Source();
	public static final FlowingFluid FLOWING_BRAIN_JUICE = new BrainJuiceFluid.Flowing();
	public static final FlowingFluid BRAIN_JUICE = new BrainJuiceFluid.Source();
	public static final FlowingFluid FLOWING_WATER_COLORS = new WaterColorsFluid.Flowing();
	public static final FlowingFluid WATER_COLORS = new WaterColorsFluid.Source();
	public static final FlowingFluid FLOWING_ENDER = new EnderFluid.Flowing();
	public static final FlowingFluid ENDER = new EnderFluid.Source();
	public static final FlowingFluid FLOWING_LIGHT_WATER = new LightWaterFluid.Flowing();
	public static final FlowingFluid LIGHT_WATER = new LightWaterFluid.Source();
	
	@SubscribeEvent
	@SuppressWarnings("unused")
	public static void registerFluids(RegistryEvent.Register<Fluid> event)
	{
		IForgeRegistry<Fluid> registry = event.getRegistry();
		registry.register(FLOWING_OIL.setRegistryName("flowing_oil"));
		registry.register(OIL.setRegistryName("oil"));
		registry.register(FLOWING_BLOOD.setRegistryName("flowing_blood"));
		registry.register(BLOOD.setRegistryName("blood"));
		registry.register(FLOWING_BRAIN_JUICE.setRegistryName("flowing_brain_juice"));
		registry.register(BRAIN_JUICE.setRegistryName("brain_juice"));
		registry.register(FLOWING_WATER_COLORS.setRegistryName("flowing_water_colors"));
		registry.register(WATER_COLORS.setRegistryName("water_colors"));
		registry.register(FLOWING_ENDER.setRegistryName("flowing_ender"));
		registry.register(ENDER.setRegistryName("ender"));
		registry.register(FLOWING_LIGHT_WATER.setRegistryName("flowing_light_water"));
		registry.register(LIGHT_WATER.setRegistryName("light_water"));
	}
}

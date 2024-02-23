package com.mraof.minestuck.fluid;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class MSFluids
{
	public static final DeferredRegister<FluidType> TYPE_REGISTER = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, Minestuck.MOD_ID);
	public static final DeferredRegister<Fluid> REGISTER = DeferredRegister.create(ForgeRegistries.FLUIDS, Minestuck.MOD_ID);
	
	public static final RegistryObject<FluidType> OIL_TYPE = TYPE_REGISTER.register("oil", () -> new MSFluidType(FluidType.Properties.create()
			.density(2000)
			.viscosity(4000)
			.descriptionId("block.minestuck.oil")
			.fallDistanceModifier(0F)
			.motionScale(0.007)
			.supportsBoating(true)
			.sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
			.sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
			.sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH),
			MSFluidType.Style.VISCOUS)
	{
		@Override
		public boolean canExtinguish(Entity entity)
		{
			//extends length of fire
			if(entity.isOnFire() && entity.getRemainingFireTicks() <= 100)
				entity.setRemainingFireTicks(100);
			
			return false;
		}
	});
	public static final RegistryObject<FlowingFluid> OIL = REGISTER.register("oil", () -> new ForgeFlowingFluid.Source(MSFluids.OIL_PROPERTIES));
	public static final RegistryObject<FlowingFluid> FLOWING_OIL = REGISTER.register("flowing_oil", () -> new ForgeFlowingFluid.Flowing(MSFluids.OIL_PROPERTIES));
	public static final ForgeFlowingFluid.Properties OIL_PROPERTIES = new ForgeFlowingFluid.Properties(OIL_TYPE, OIL, FLOWING_OIL).bucket(MSItems.OIL_BUCKET).block(MSBlocks.OIL).tickRate(10).slopeFindDistance(3).explosionResistance(100F);
	
	public static final RegistryObject<FluidType> LIGHT_WATER_TYPE = TYPE_REGISTER.register("light_water", () -> new MSFluidType(FluidType.Properties.create()
			.canConvertToSource(true)
			.canExtinguish(true)
			.density(800)
			.viscosity(800)
			.descriptionId("block.minestuck.light_water")
			.fallDistanceModifier(0F)
			.motionScale(0.007)
			.supportsBoating(true)
			.sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
			.sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
			.sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH),
			MSFluidType.Style.RUNNY));
	public static final RegistryObject<FlowingFluid> LIGHT_WATER = REGISTER.register("light_water", () -> new ForgeFlowingFluid.Source(MSFluids.LIGHT_WATER_PROPERTIES));
	public static final RegistryObject<FlowingFluid> FLOWING_LIGHT_WATER = REGISTER.register("flowing_light_water", () -> new ForgeFlowingFluid.Flowing(MSFluids.LIGHT_WATER_PROPERTIES));
	public static final ForgeFlowingFluid.Properties LIGHT_WATER_PROPERTIES = new ForgeFlowingFluid.Properties(LIGHT_WATER_TYPE, LIGHT_WATER, FLOWING_LIGHT_WATER).bucket(MSItems.LIGHT_WATER_BUCKET).block(MSBlocks.LIGHT_WATER).tickRate(3).explosionResistance(100F);
	
	public static final RegistryObject<FluidType> BLOOD_TYPE = TYPE_REGISTER.register("blood", () -> new MSFluidType(FluidType.Properties.create()
			.canHydrate(true)
			.canExtinguish(true)
			.density(1500)
			.viscosity(2000)
			.descriptionId("block.minestuck.blood")
			.fallDistanceModifier(0F)
			.motionScale(0.007)
			.supportsBoating(true)
			.sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
			.sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
			.sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH),
			MSFluidType.Style.PARTIALLY_VISCOUS));
	public static final RegistryObject<FlowingFluid> BLOOD = REGISTER.register("blood", () -> new ForgeFlowingFluid.Source(MSFluids.BLOOD_PROPERTIES));
	public static final RegistryObject<FlowingFluid> FLOWING_BLOOD = REGISTER.register("flowing_blood", () -> new ForgeFlowingFluid.Flowing(MSFluids.BLOOD_PROPERTIES));
	public static final ForgeFlowingFluid.Properties BLOOD_PROPERTIES = new ForgeFlowingFluid.Properties(BLOOD_TYPE, BLOOD, FLOWING_BLOOD).bucket(MSItems.BLOOD_BUCKET).block(MSBlocks.BLOOD).tickRate(7).explosionResistance(100F);
	
	public static final RegistryObject<FluidType> BRAIN_JUICE_TYPE = TYPE_REGISTER.register("brain_juice", () -> new MSFluidType(FluidType.Properties.create()
			.canHydrate(true)
			.canDrown(false) //imagining it as a fantastical breathable liquid
			.canConvertToSource(true)
			.canExtinguish(true)
			.density(1750)
			.viscosity(2500)
			.descriptionId("block.minestuck.brain_juice")
			.fallDistanceModifier(0F)
			.motionScale(0.007)
			.supportsBoating(true)
			.sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
			.sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
			.sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH),
			MSFluidType.Style.PARTIALLY_VISCOUS));
	public static final RegistryObject<FlowingFluid> BRAIN_JUICE = REGISTER.register("brain_juice", () -> new ForgeFlowingFluid.Source(MSFluids.BRAIN_JUICE_PROPERTIES));
	public static final RegistryObject<FlowingFluid> FLOWING_BRAIN_JUICE = REGISTER.register("flowing_brain_juice", () -> new ForgeFlowingFluid.Flowing(MSFluids.BRAIN_JUICE_PROPERTIES));
	public static final ForgeFlowingFluid.Properties BRAIN_JUICE_PROPERTIES = new ForgeFlowingFluid.Properties(BRAIN_JUICE_TYPE, BRAIN_JUICE, FLOWING_BRAIN_JUICE).bucket(MSItems.BRAIN_JUICE_BUCKET).block(MSBlocks.BRAIN_JUICE).tickRate(7).explosionResistance(100F);
	
	public static final RegistryObject<FluidType> WATER_COLORS_TYPE = TYPE_REGISTER.register("water_colors", () -> new MSFluidType(FluidType.Properties.create()
			.canConvertToSource(true)
			.canExtinguish(true)
			.density(1000)
			.viscosity(1000)
			.descriptionId("block.minestuck.water_colors")
			.fallDistanceModifier(0F)
			.motionScale(0.007)
			.supportsBoating(true)
			.sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
			.sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
			.sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH),
			MSFluidType.Style.PARTIALLY_VISCOUS));
	public static final RegistryObject<FlowingFluid> WATER_COLORS = REGISTER.register("water_colors", () -> new ForgeFlowingFluid.Source(MSFluids.WATER_COLORS_PROPERTIES));
	public static final RegistryObject<FlowingFluid> FLOWING_WATER_COLORS = REGISTER.register("flowing_water_colors", () -> new ForgeFlowingFluid.Flowing(MSFluids.WATER_COLORS_PROPERTIES));
	
	public static final ForgeFlowingFluid.Properties WATER_COLORS_PROPERTIES = new ForgeFlowingFluid.Properties(WATER_COLORS_TYPE, WATER_COLORS, FLOWING_WATER_COLORS).bucket(MSItems.WATER_COLORS_BUCKET).block(MSBlocks.WATER_COLORS).tickRate(6).explosionResistance(100F);
	
	public static final RegistryObject<FluidType> ENDER_TYPE = TYPE_REGISTER.register("ender", () -> new MSFluidType(FluidType.Properties.create()
			.canExtinguish(true)
			.density(2000)
			.viscosity(4000)
			.descriptionId("block.minestuck.ender")
			.fallDistanceModifier(0F)
			.motionScale(0.007)
			.sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)
			.sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA)
			.sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH), MSFluidType.Style.VISCOUS));
	public static final RegistryObject<FlowingFluid> ENDER = REGISTER.register("ender", () -> new ForgeFlowingFluid.Source(MSFluids.ENDER_PROPERTIES));
	public static final RegistryObject<FlowingFluid> FLOWING_ENDER = REGISTER.register("flowing_ender", () -> new ForgeFlowingFluid.Flowing(MSFluids.ENDER_PROPERTIES));
	public static final ForgeFlowingFluid.Properties ENDER_PROPERTIES = new ForgeFlowingFluid.Properties(ENDER_TYPE, ENDER, FLOWING_ENDER).bucket(MSItems.ENDER_BUCKET).block(MSBlocks.ENDER).tickRate(15).slopeFindDistance(2).levelDecreasePerBlock(2).explosionResistance(100F);
	
	public static final RegistryObject<FluidType> CAULK_TYPE = TYPE_REGISTER.register("caulk", () -> new MSFluidType(FluidType.Properties.create()
			.density(3000)
			.viscosity(4500)
			.descriptionId("block.minestuck.caulk")
			.fallDistanceModifier(0F)
			.motionScale(0.002)
			.supportsBoating(true)
			.sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
			.sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
			.sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH),
			MSFluidType.Style.VISCOUS)
	{
		@Override
		public boolean canExtinguish(Entity entity)
		{
			//extends length of fire
			if(entity.isOnFire() && entity.getRemainingFireTicks() <= 100)
				entity.setRemainingFireTicks(100);
			
			return false;
		}
	});
	public static final RegistryObject<FlowingFluid> CAULK = REGISTER.register("caulk", () -> new ForgeFlowingFluid.Source(MSFluids.CAULK_PROPERTIES));
	public static final RegistryObject<FlowingFluid> FLOWING_CAULK = REGISTER.register("flowing_caulk", () -> new ForgeFlowingFluid.Flowing(MSFluids.CAULK_PROPERTIES));
	public static final ForgeFlowingFluid.Properties CAULK_PROPERTIES = new ForgeFlowingFluid.Properties(CAULK_TYPE, CAULK, FLOWING_CAULK).bucket(MSItems.CAULK_BUCKET).block(MSBlocks.CAULK).tickRate(20).explosionResistance(100F);
	
	public static final RegistryObject<FluidType> MOLTEN_AMBER_TYPE = TYPE_REGISTER.register("molten_amber", () -> new MSFluidType(FluidType.Properties.create()
			.density(3000)
			.viscosity(4500)
			.descriptionId("block.minestuck.molten_amber")
			.fallDistanceModifier(0F)
			.motionScale(0.002)
			.supportsBoating(true)
			.sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
			.sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
			.sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH),
			MSFluidType.Style.VISCOUS)
	{
		@Override
		public boolean canExtinguish(Entity entity)
		{
			//sets target on fire, hopefully...?
			if(!entity.isOnFire())
				entity.setSecondsOnFire(10);
			
			return false;
		}
	});
	public static final RegistryObject<FlowingFluid> MOLTEN_AMBER = REGISTER.register("molten_amber", () -> new ForgeFlowingFluid.Source(MSFluids.MOLTEN_AMBER_PROPERTIES));
	public static final RegistryObject<FlowingFluid> FLOWING_MOLTEN_AMBER = REGISTER.register("flowing_molten_amber", () -> new ForgeFlowingFluid.Flowing(MSFluids.MOLTEN_AMBER_PROPERTIES));
	public static final ForgeFlowingFluid.Properties MOLTEN_AMBER_PROPERTIES = new ForgeFlowingFluid.Properties(MOLTEN_AMBER_TYPE, MOLTEN_AMBER, FLOWING_MOLTEN_AMBER).bucket(MSItems.MOLTEN_AMBER_BUCKET).block(MSBlocks.MOLTEN_AMBER).tickRate(20).explosionResistance(100F);
}
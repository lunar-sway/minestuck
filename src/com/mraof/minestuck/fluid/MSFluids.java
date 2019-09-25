package com.mraof.minestuck.fluid;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MSFluids
{
	public static final ResourceLocation STILL_OIL_TEXTURE = new ResourceLocation(Minestuck.MOD_ID, "block/still_oil");
	public static final ResourceLocation FLOWING_OIL_TEXTURE = new ResourceLocation(Minestuck.MOD_ID, "block/flowing_oil");
	public static final ResourceLocation STILL_LIGHT_WATER_TEXTURE = new ResourceLocation(Minestuck.MOD_ID, "block/still_light_water");
	public static final ResourceLocation FLOWING_LIGHT_WATER_TEXTURE = new ResourceLocation(Minestuck.MOD_ID, "block/flowing_light_water");
	public static final ResourceLocation STILL_BLOOD_TEXTURE = new ResourceLocation(Minestuck.MOD_ID, "block/still_blood");
	public static final ResourceLocation FLOWING_BLOOD_TEXTURE = new ResourceLocation(Minestuck.MOD_ID, "block/flowing_blood");
	public static final ResourceLocation STILL_BRAIN_JUICE_TEXTURE = new ResourceLocation(Minestuck.MOD_ID, "block/still_brain_juice");
	public static final ResourceLocation FLOWING_BRAIN_JUICE_TEXTURE = new ResourceLocation(Minestuck.MOD_ID, "block/flowing_brain_juice");
	public static final ResourceLocation STILL_WATER_COLORS_TEXTURE = new ResourceLocation(Minestuck.MOD_ID, "block/still_water_colors");
	public static final ResourceLocation FLOWING_WATER_COLORS_TEXTURE = new ResourceLocation(Minestuck.MOD_ID, "block/flowing_water_colors");
	public static final ResourceLocation STILL_ENDER_TEXTURE = new ResourceLocation(Minestuck.MOD_ID, "block/still_ender");
	public static final ResourceLocation FLOWING_ENDER_TEXTURE = new ResourceLocation(Minestuck.MOD_ID, "block/flowing_ender");
	
	public static final DeferredRegister<Fluid> FLUIDS = new DeferredRegister<>(ForgeRegistries.FLUIDS, Minestuck.MOD_ID);
	
	public static RegistryObject<FlowingFluid> OIL = FLUIDS.register("oil", () -> new ForgeFlowingFluid.Source(MSFluids.OIL_PROPERTIES));
	public static RegistryObject<FlowingFluid> FLOWING_OIL = FLUIDS.register("flowing_oil", () -> new ForgeFlowingFluid.Flowing(MSFluids.OIL_PROPERTIES));
	public static RegistryObject<FlowingFluid> LIGHT_WATER = FLUIDS.register("light_water", () -> new ForgeFlowingFluid.Source(MSFluids.LIGHT_WATER_PROPERTIES));
	public static RegistryObject<FlowingFluid> FLOWING_LIGHT_WATER = FLUIDS.register("flowing_light_water", () -> new ForgeFlowingFluid.Flowing(MSFluids.LIGHT_WATER_PROPERTIES));
	public static RegistryObject<FlowingFluid> BLOOD = FLUIDS.register("blood", () -> new ForgeFlowingFluid.Source(MSFluids.BLOOD_PROPERTIES));
	public static RegistryObject<FlowingFluid> FLOWING_BLOOD = FLUIDS.register("flowing_blood", () -> new ForgeFlowingFluid.Flowing(MSFluids.BLOOD_PROPERTIES));
	public static RegistryObject<FlowingFluid> BRAIN_JUICE = FLUIDS.register("brain_juice", () -> new ForgeFlowingFluid.Source(MSFluids.BRAIN_JUICE_PROPERTIES));
	public static RegistryObject<FlowingFluid> FLOWING_BRAIN_JUICE = FLUIDS.register("flowing_brain_juice", () -> new ForgeFlowingFluid.Flowing(MSFluids.BRAIN_JUICE_PROPERTIES));
	public static RegistryObject<FlowingFluid> WATER_COLORS = FLUIDS.register("water_colors", () -> new ForgeFlowingFluid.Source(MSFluids.WATER_COLORS_PROPERTIES));
	public static RegistryObject<FlowingFluid> FLOWING_WATER_COLORS = FLUIDS.register("flowing_water_colors", () -> new ForgeFlowingFluid.Flowing(MSFluids.WATER_COLORS_PROPERTIES));
	public static RegistryObject<FlowingFluid> ENDER = FLUIDS.register("ender", () -> new ForgeFlowingFluid.Source(MSFluids.ENDER_PROPERTIES));
	public static RegistryObject<FlowingFluid> FLOWING_ENDER = FLUIDS.register("flowing_ender", () -> new ForgeFlowingFluid.Flowing(MSFluids.ENDER_PROPERTIES));
	
	public static final ForgeFlowingFluid.Properties OIL_PROPERTIES = new ForgeFlowingFluid.Properties(OIL, FLOWING_OIL, FluidAttributes.builder(STILL_OIL_TEXTURE, FLOWING_OIL_TEXTURE)).bucket(() -> MSItems.OIL_BUCKET).block(() -> MSBlocks.OIL).slopeFindDistance(3).explosionResistance(100F);
	public static final ForgeFlowingFluid.Properties LIGHT_WATER_PROPERTIES = new ForgeFlowingFluid.Properties(LIGHT_WATER, FLOWING_LIGHT_WATER, FluidAttributes.builder(STILL_LIGHT_WATER_TEXTURE, FLOWING_LIGHT_WATER_TEXTURE)).canMultiply().bucket(() -> MSItems.LIGHT_WATER_BUCKET).block(() -> MSBlocks.LIGHT_WATER).explosionResistance(100F);
	public static final ForgeFlowingFluid.Properties BLOOD_PROPERTIES = new ForgeFlowingFluid.Properties(BLOOD, FLOWING_BLOOD, FluidAttributes.builder(STILL_BLOOD_TEXTURE, FLOWING_BLOOD_TEXTURE)).bucket(() -> MSItems.BLOOD_BUCKET).block(() -> MSBlocks.BLOOD).explosionResistance(100F);
	public static final ForgeFlowingFluid.Properties BRAIN_JUICE_PROPERTIES = new ForgeFlowingFluid.Properties(BRAIN_JUICE, FLOWING_BRAIN_JUICE, FluidAttributes.builder(STILL_BRAIN_JUICE_TEXTURE, FLOWING_BRAIN_JUICE_TEXTURE)).bucket(() -> MSItems.BRAIN_JUICE_BUCKET).block(() -> MSBlocks.BRAIN_JUICE).explosionResistance(100F);
	public static final ForgeFlowingFluid.Properties WATER_COLORS_PROPERTIES = new ForgeFlowingFluid.Properties(WATER_COLORS, FLOWING_WATER_COLORS, FluidAttributes.builder(STILL_WATER_COLORS_TEXTURE, FLOWING_WATER_COLORS_TEXTURE)).canMultiply().bucket(() -> MSItems.WATER_COLORS_BUCKET).block(() -> MSBlocks.WATER_COLORS).explosionResistance(100F);
	public static final ForgeFlowingFluid.Properties ENDER_PROPERTIES = new ForgeFlowingFluid.Properties(ENDER, FLOWING_ENDER, FluidAttributes.builder(STILL_ENDER_TEXTURE, FLOWING_ENDER_TEXTURE)).bucket(() -> MSItems.ENDER_BUCKET).block(() -> MSBlocks.ENDER).slopeFindDistance(2).levelDecreasePerBlock(2).explosionResistance(100F);
}
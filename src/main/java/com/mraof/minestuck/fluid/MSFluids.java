package com.mraof.minestuck.fluid;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;

public final class MSFluids	//TODO Set fluid type properties
{
	public static final DeferredRegister<Fluid> REGISTER = DeferredRegister.create(ForgeRegistries.FLUIDS, Minestuck.MOD_ID);
	public static final DeferredRegister<FluidType> TYPE_REGISTER = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, Minestuck.MOD_ID);
	
	
	public static final RegistryObject<FlowingFluid> OIL = REGISTER.register("oil", () -> new ForgeFlowingFluid.Source(MSFluids.OIL_PROPERTIES));
	public static final RegistryObject<FlowingFluid> FLOWING_OIL = REGISTER.register("flowing_oil", () -> new ForgeFlowingFluid.Flowing(MSFluids.OIL_PROPERTIES));
	public static final RegistryObject<FluidType> OIL_TYPE = TYPE_REGISTER.register("oil", () -> new FluidType(FluidType.Properties.create()){
		@Override
		public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer)
		{
			consumer.accept(new IClientFluidTypeExtensions()
			{
				public static final ResourceLocation STILL_TEXTURE = new ResourceLocation(Minestuck.MOD_ID, "block/still_oil");
				public static final ResourceLocation FLOWING_TEXTURE = new ResourceLocation(Minestuck.MOD_ID, "block/flowing_oil");
				@Override
				public ResourceLocation getStillTexture()
				{
					return STILL_TEXTURE;
				}
				@Override
				public ResourceLocation getFlowingTexture()
				{
					return FLOWING_TEXTURE;
				}
			});
		}
	});
	public static final ForgeFlowingFluid.Properties OIL_PROPERTIES = new ForgeFlowingFluid.Properties(OIL_TYPE, OIL, FLOWING_OIL).bucket(MSItems.OIL_BUCKET).block(MSBlocks.OIL).slopeFindDistance(3).explosionResistance(100F);
	
	
	public static final RegistryObject<FlowingFluid> LIGHT_WATER = REGISTER.register("light_water", () -> new ForgeFlowingFluid.Source(MSFluids.LIGHT_WATER_PROPERTIES));
	public static final RegistryObject<FlowingFluid> FLOWING_LIGHT_WATER = REGISTER.register("flowing_light_water", () -> new ForgeFlowingFluid.Flowing(MSFluids.LIGHT_WATER_PROPERTIES));
	public static final RegistryObject<FluidType> LIGHT_WATER_TYPE = TYPE_REGISTER.register("light_water", () -> new FluidType(FluidType.Properties.create().canConvertToSource(true)){
		@Override
		public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer)
		{
			consumer.accept(new IClientFluidTypeExtensions()
			{
				public static final ResourceLocation STILL_TEXTURE = new ResourceLocation(Minestuck.MOD_ID, "block/still_light_water");
				public static final ResourceLocation FLOWING_TEXTURE = new ResourceLocation(Minestuck.MOD_ID, "block/flowing_light_water");
				@Override
				public ResourceLocation getStillTexture()
				{
					return STILL_TEXTURE;
				}
				@Override
				public ResourceLocation getFlowingTexture()
				{
					return FLOWING_TEXTURE;
				}
			});
		}
	});
	public static final ForgeFlowingFluid.Properties LIGHT_WATER_PROPERTIES = new ForgeFlowingFluid.Properties(LIGHT_WATER_TYPE, LIGHT_WATER, FLOWING_LIGHT_WATER).bucket(MSItems.LIGHT_WATER_BUCKET).block(MSBlocks.LIGHT_WATER).explosionResistance(100F);
	
	
	public static final RegistryObject<FlowingFluid> BLOOD = REGISTER.register("blood", () -> new ForgeFlowingFluid.Source(MSFluids.BLOOD_PROPERTIES));
	public static final RegistryObject<FlowingFluid> FLOWING_BLOOD = REGISTER.register("flowing_blood", () -> new ForgeFlowingFluid.Flowing(MSFluids.BLOOD_PROPERTIES));
	public static final RegistryObject<FluidType> BLOOD_TYPE = TYPE_REGISTER.register("blood", () -> new FluidType(FluidType.Properties.create()){
		@Override
		public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer)
		{
			consumer.accept(new IClientFluidTypeExtensions()
			{
				public static final ResourceLocation STILL_TEXTURE = new ResourceLocation(Minestuck.MOD_ID, "block/still_blood");
				public static final ResourceLocation FLOWING_TEXTURE = new ResourceLocation(Minestuck.MOD_ID, "block/flowing_blood");
				@Override
				public ResourceLocation getStillTexture()
				{
					return STILL_TEXTURE;
				}
				@Override
				public ResourceLocation getFlowingTexture()
				{
					return FLOWING_TEXTURE;
				}
			});
		}
	});
	public static final ForgeFlowingFluid.Properties BLOOD_PROPERTIES = new ForgeFlowingFluid.Properties(BLOOD_TYPE, BLOOD, FLOWING_BLOOD).bucket(MSItems.BLOOD_BUCKET).block(MSBlocks.BLOOD).explosionResistance(100F);
	
	public static final RegistryObject<FlowingFluid> BRAIN_JUICE = REGISTER.register("brain_juice", () -> new ForgeFlowingFluid.Source(MSFluids.BRAIN_JUICE_PROPERTIES));
	public static final RegistryObject<FlowingFluid> FLOWING_BRAIN_JUICE = REGISTER.register("flowing_brain_juice", () -> new ForgeFlowingFluid.Flowing(MSFluids.BRAIN_JUICE_PROPERTIES));
	public static final RegistryObject<FluidType> BRAIN_JUICE_TYPE = TYPE_REGISTER.register("brain_juice", () -> new FluidType(FluidType.Properties.create()){
		@Override
		public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer)
		{
			consumer.accept(new IClientFluidTypeExtensions()
			{
				public static final ResourceLocation STILL_TEXTURE = new ResourceLocation(Minestuck.MOD_ID, "block/still_brain_juice");
				public static final ResourceLocation FLOWING_TEXTURE = new ResourceLocation(Minestuck.MOD_ID, "block/flowing_brain_juice");
				@Override
				public ResourceLocation getStillTexture()
				{
					return STILL_TEXTURE;
				}
				@Override
				public ResourceLocation getFlowingTexture()
				{
					return FLOWING_TEXTURE;
				}
			});
		}
	});
	public static final ForgeFlowingFluid.Properties BRAIN_JUICE_PROPERTIES = new ForgeFlowingFluid.Properties(BRAIN_JUICE_TYPE, BRAIN_JUICE, FLOWING_BRAIN_JUICE).bucket(MSItems.BRAIN_JUICE_BUCKET).block(MSBlocks.BRAIN_JUICE).explosionResistance(100F);
	
	public static final RegistryObject<FlowingFluid> WATER_COLORS = REGISTER.register("water_colors", () -> new ForgeFlowingFluid.Source(MSFluids.WATER_COLORS_PROPERTIES));
	public static final RegistryObject<FlowingFluid> FLOWING_WATER_COLORS = REGISTER.register("flowing_water_colors", () -> new ForgeFlowingFluid.Flowing(MSFluids.WATER_COLORS_PROPERTIES));
	public static final RegistryObject<FluidType> WATER_COLORS_TYPE = TYPE_REGISTER.register("water_colors", () -> new FluidType(FluidType.Properties.create().canConvertToSource(true)){
		@Override
		public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer)
		{
			consumer.accept(new IClientFluidTypeExtensions()
			{
				public static final ResourceLocation STILL_TEXTURE = new ResourceLocation(Minestuck.MOD_ID, "block/still_water_colors");
				public static final ResourceLocation FLOWING_TEXTURE = new ResourceLocation(Minestuck.MOD_ID, "block/flowing_water_colors");
				@Override
				public ResourceLocation getStillTexture()
				{
					return STILL_TEXTURE;
				}
				@Override
				public ResourceLocation getFlowingTexture()
				{
					return FLOWING_TEXTURE;
				}
			});
		}
	});
	public static final ForgeFlowingFluid.Properties WATER_COLORS_PROPERTIES = new ForgeFlowingFluid.Properties(WATER_COLORS_TYPE, WATER_COLORS, FLOWING_WATER_COLORS).bucket(MSItems.WATER_COLORS_BUCKET).block(MSBlocks.WATER_COLORS).explosionResistance(100F);
	
	public static final RegistryObject<FlowingFluid> ENDER = REGISTER.register("ender", () -> new ForgeFlowingFluid.Source(MSFluids.ENDER_PROPERTIES));
	public static final RegistryObject<FlowingFluid> FLOWING_ENDER = REGISTER.register("flowing_ender", () -> new ForgeFlowingFluid.Flowing(MSFluids.ENDER_PROPERTIES));
	public static final RegistryObject<FluidType> ENDER_TYPE = TYPE_REGISTER.register("ender", () -> new FluidType(FluidType.Properties.create()){
		@Override
		public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer)
		{
			consumer.accept(new IClientFluidTypeExtensions()
			{
				public static final ResourceLocation STILL_TEXTURE = new ResourceLocation(Minestuck.MOD_ID, "block/still_ender");
				public static final ResourceLocation FLOWING_TEXTURE = new ResourceLocation(Minestuck.MOD_ID, "block/flowing_ender");
				@Override
				public ResourceLocation getStillTexture()
				{
					return STILL_TEXTURE;
				}
				@Override
				public ResourceLocation getFlowingTexture()
				{
					return FLOWING_TEXTURE;
				}
			});
		}
	});
	public static final ForgeFlowingFluid.Properties ENDER_PROPERTIES = new ForgeFlowingFluid.Properties(ENDER_TYPE, ENDER, FLOWING_ENDER).bucket(MSItems.ENDER_BUCKET).block(MSBlocks.ENDER).slopeFindDistance(2).levelDecreasePerBlock(2).explosionResistance(100F);
}
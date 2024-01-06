package com.mraof.minestuck.fluid;

import com.mraof.minestuck.Minestuck;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;

public class MSFluidTypes
{
	public static final DeferredRegister<FluidType> REGISTER = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, Minestuck.MOD_ID);
	
	//TODO fix flowing fluid push mechanics (player is too slow to walk up stream)
	
	public static final RegistryObject<FluidType> OIL_TYPE = REGISTER.register("oil", () -> new MSFluidType(FluidType.Properties.create()
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
	
	public static final RegistryObject<FluidType> LIGHT_WATER_TYPE = REGISTER.register("light_water", () -> new MSFluidType(FluidType.Properties.create()
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
			MSFluidType.Style.RUNNY)
	{
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
	
	public static final RegistryObject<FluidType> BLOOD_TYPE = REGISTER.register("blood", () -> new MSFluidType(FluidType.Properties.create()
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
			MSFluidType.Style.PARTIALLY_VISCOUS)
	{
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
	
	public static final RegistryObject<FluidType> BRAIN_JUICE_TYPE = REGISTER.register("brain_juice", () -> new MSFluidType(FluidType.Properties.create()
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
			MSFluidType.Style.PARTIALLY_VISCOUS)
	{
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
	
	public static final RegistryObject<FluidType> WATER_COLORS_TYPE = REGISTER.register("water_colors", () -> new MSFluidType(FluidType.Properties.create()
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
			MSFluidType.Style.PARTIALLY_VISCOUS)
	{
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
	
	public static final RegistryObject<FluidType> ENDER_TYPE = REGISTER.register("ender", () -> new MSFluidType(FluidType.Properties.create()
			.canExtinguish(true)
			.density(2000)
			.viscosity(4000)
			.descriptionId("block.minestuck.ender")
			.fallDistanceModifier(0F)
			.motionScale(0.007)
			.sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)
			.sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA)
			.sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH), MSFluidType.Style.VISCOUS)
	{
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
}
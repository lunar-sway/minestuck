package com.mraof.minestuck.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = Minestuck.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public final class LandRainTint
{
	private static final int NO_TINT = -1;
	private static final int RAINBOW = -2;
	
	private static final Map<ResourceLocation, Integer> LIQUID_COLORS = new HashMap<>();
	
	static
	{
		LIQUID_COLORS.put(Minestuck.id("blood"), 0xC01010);
		LIQUID_COLORS.put(Minestuck.id("oil"), 0x3A3A3A);
		LIQUID_COLORS.put(Minestuck.id("brain_juice"), 0x8B2FCB);
		LIQUID_COLORS.put(Minestuck.id("caulk"), 0xC9BDB8);
		LIQUID_COLORS.put(Minestuck.id("ender"), 0x2FA595);
		LIQUID_COLORS.put(Minestuck.id("molten_amber"), 0xE0701A);
		LIQUID_COLORS.put(Minestuck.id("light_water"), 0x42F7E5);
		LIQUID_COLORS.put(Minestuck.id("water_colors"), RAINBOW);
	}
	
	private static final Map<LandTypePair, Integer> TINT_CACHE = new HashMap<>();
	
	private static boolean tintApplied;
	
	private LandRainTint()
	{
	}
	
	public static void applyBeforeRain(ClientLevel level, int ticks, float partialTick)
	{
		if(!MinestuckConfig.CLIENT.landLiquidRain.get() || level.getRainLevel(partialTick) <= 0 || !isRainingOnCamera(level))
			return;
		
		int tint = resolveColor(level, ticks + partialTick);
		if(tint == NO_TINT)
			return;
		
		RenderSystem.setShaderColor((tint >> 16 & 255) / 255F, (tint >> 8 & 255) / 255F, (tint & 255) / 255F, 1.0F);
		tintApplied = true;
	}
	
	private static int resolveColor(ClientLevel level, float time)
	{
		int tint = getTint(level);
		if(tint == RAINBOW)
			return Color.HSBtoRGB((time % 400F) / 400F, 0.6F, 1.0F);
		return tint;
	}
	
	public static void tintSplash(ClientLevel level, Particle particle)
	{
		if(!MinestuckConfig.CLIENT.landLiquidRain.get())
			return;
		int tint = resolveColor(level, level.getGameTime());
		if(tint == NO_TINT)
			return;
		
		particle.setColor(
				Math.min(1.0F, (tint >> 16 & 255) / 255F / SPLASH_BASE_RED),
				Math.min(1.0F, (tint >> 8 & 255) / 255F / SPLASH_BASE_GREEN),
				Math.min(1.0F, (tint & 255) / 255F / SPLASH_BASE_BLUE));
	}
	
	/** Roughly the color of the splash texture, which the color of the particle is multiplied with. */
	private static final float SPLASH_BASE_RED = 0.55F, SPLASH_BASE_GREEN = 0.65F, SPLASH_BASE_BLUE = 1.0F;
	
	/** Rain falls as snow where it's cold, and snow is not tinted. */
	private static boolean isRainingOnCamera(ClientLevel level)
	{
		Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
		BlockPos pos = camera.getBlockPosition();
		return level.getBiome(pos).value().getPrecipitationAt(pos) == Biome.Precipitation.RAIN;
	}
	
	private static int getTint(ClientLevel level)
	{
		LandTypePair types = ClientDimensionData.getLandTypes(level.dimension());
		if(types == null)
			return NO_TINT;
		return TINT_CACHE.computeIfAbsent(types, LandRainTint::findTint);
	}
	
	private static int findTint(LandTypePair types)
	{
		BlockState ocean = StructureBlockRegistry.init(types).getBlockState(StructureBlockRegistry.OCEAN);
		ResourceLocation fluid = BuiltInRegistries.FLUID.getKey(ocean.getFluidState().getType());
		return LIQUID_COLORS.getOrDefault(fluid, NO_TINT);
	}
	
	@SubscribeEvent
	public static void onRenderLevelStage(RenderLevelStageEvent event)
	{
		if(event.getStage() == RenderLevelStageEvent.Stage.AFTER_WEATHER && tintApplied)
		{
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			tintApplied = false;
		}
	}
	
	@SubscribeEvent
	public static void onLogout(ClientPlayerNetworkEvent.LoggingOut event)
	{
		TINT_CACHE.clear();
		tintApplied = false;
	}
	
	@SubscribeEvent
	public static void onLogin(ClientPlayerNetworkEvent.LoggingIn event)
	{
		TINT_CACHE.clear();
	}
}

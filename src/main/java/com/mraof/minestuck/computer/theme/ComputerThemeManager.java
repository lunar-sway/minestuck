package com.mraof.minestuck.computer.theme;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.Minestuck;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

/**
 * Helps acquire json files in assets/minestuck/minestuck/computer_themes/
 * ComputerThemes are data driven resource pack files that determine a computers wallpaper and text color.
 */
@ParametersAreNonnullByDefault
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ComputerThemeManager
{
	public static final String PATH = "minestuck/computer_themes";
	public static final String PATH_W_SLASH = PATH + "/";
	
	private final List<ComputerTheme> themes;
	
	@Nullable
	private static ComputerThemeManager INSTANCE;
	
	public ComputerThemeManager(List<ComputerTheme> themes)
	{
		this.themes = themes;
	}
	
	public static ComputerThemeManager getInstance()
	{
		return Objects.requireNonNull(INSTANCE, "Computer themes haven't been loaded yet!");
	}
	
	@SubscribeEvent
	public static void registerLoader(RegisterClientReloadListenersEvent event)
	{
		event.registerReloadListener(new Loader());
	}
	
	public ResourceLocation findTexturePath(String name)
	{
		Optional<ResourceLocation> potentialPath = themes.stream().filter(theme ->
				theme.themeName().equals(name)).findAny().map(ComputerTheme::texturePath);
		return potentialPath.orElse(ComputerTheme.DEFAULT_TEXTURE_PATH);
	}
	
	public int findTextColor(String name)
	{
		Optional<Integer> potentialColor = themes.stream().filter(theme -> theme.themeName().equals(name)).findAny().map(ComputerTheme::textColor);
		return potentialColor.orElse(ComputerTheme.DEFAULT_TEXT_COLOR);
	}
	
	public List<String> getThemeNames()
	{
		List<String> themeNames = new ArrayList<>();
		themes.forEach(computerTheme -> themeNames.add(computerTheme.themeName()));
		return themeNames;
	}
	
	private static final class Loader extends SimpleJsonResourceReloadListener
	{
		private static final Logger LOGGER = LogManager.getLogger();
		private static final Gson GSON = new GsonBuilder().create();
		
		Loader()
		{
			super(GSON, PATH);
		}
		
		@Override
		protected void apply(Map<ResourceLocation, JsonElement> jsonEntries, ResourceManager resourceManager, ProfilerFiller profiler)
		{
			ImmutableList.Builder<ComputerTheme> computerThemes = ImmutableList.builder();
			for(Map.Entry<ResourceLocation, JsonElement> entry : jsonEntries.entrySet())
			{
				ComputerTheme.CODEC.parse(JsonOps.INSTANCE, entry.getValue())
						.resultOrPartial(LOGGER::error)
						.ifPresent(computerThemes::add);
			}
			
			INSTANCE = new ComputerThemeManager(computerThemes.build());
			LOGGER.info("Loaded {} computer themes", INSTANCE.themes.size());
		}
	}
}

package com.mraof.minestuck.computer.theme;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mraof.minestuck.Minestuck;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Helps acquire json files in assets/minestuck/minestuck/computer_themes/
 * ComputerThemes are data driven resource pack files that determine a computers wallpaper and text color.
 */
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ComputerThemeManager extends SimpleJsonResourceReloadListener implements PreparableReloadListener
{
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Gson GSON = new GsonBuilder().registerTypeAdapter(ComputerTheme.class, new ComputerTheme.Serializer()).create();
	
	public static final String PATH = "minestuck/computer_themes";
	public static final String PATH_W_SLASH = PATH + "/";
	
	private List<ComputerTheme> themes;
	
	public ComputerThemeManager()
	{
		super(GSON, PATH);
	}
	
	private static ComputerThemeManager INSTANCE;
	
	public static ComputerThemeManager getInstance()
	{
		return INSTANCE;
	}
	
	@SubscribeEvent
	public static void initUploader(RegisterClientReloadListenersEvent event)
	{
		event.registerReloadListener(INSTANCE = new ComputerThemeManager());
	}
	
	@Override
	protected void apply(Map<ResourceLocation, JsonElement> jsonEntries, ResourceManager resourceManager, ProfilerFiller profiler)
	{
		ImmutableList.Builder<ComputerTheme> computerThemes = ImmutableList.builder();
		for(Map.Entry<ResourceLocation, JsonElement> entry : jsonEntries.entrySet())
		{
			try
			{
				ComputerTheme computerTheme = GSON.fromJson(entry.getValue(), ComputerTheme.class);
				computerThemes.add(computerTheme);
			} catch(Exception e)
			{
				LOGGER.error("Couldn't parse computer theme {}", entry.getKey(), e);
			}
		}
		
		this.themes = computerThemes.build();
		LOGGER.info("Loaded {} computer themes", this.themes.size());
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
	
	public static JsonElement parseTheme(ComputerTheme theme)
	{
		return GSON.toJsonTree(theme);
	}
}
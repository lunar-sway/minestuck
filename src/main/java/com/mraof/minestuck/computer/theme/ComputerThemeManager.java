package com.mraof.minestuck.computer.theme;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ComputerThemeManager extends SimpleJsonResourceReloadListener
{
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Gson GSON = new GsonBuilder().registerTypeAdapter(ComputerTheme.class, new ComputerTheme.Serializer()).create();
	
	public static final String PATH = "minestuck/computer_themes";
	
	private List<ComputerTheme> themes;
	
	public ComputerThemeManager()
	{
		super(GSON, PATH);
	}
	
	private static ComputerThemeManager INSTANCE;
	
	public static ComputerThemeManager getInstance()
	{
		return Objects.requireNonNull(INSTANCE);
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
				theme.getThemeName().equals(name)).findAny().map(ComputerTheme::getTexturePath);
		return potentialPath.orElse(ComputerTheme.DEFAULT_TEXTURE_PATH);
	}
	
	public int findTextColor(String name)
	{
		Optional<Integer> potentialColor = themes.stream().filter(theme -> theme.getThemeName().equals(name)).findAny().map(ComputerTheme::getTextColor);
		return potentialColor.orElse(ComputerTheme.DEFAULT_TEXT_COLOR);
	}
	
	public static JsonElement parseTheme(ComputerTheme theme)
	{
		return GSON.toJsonTree(theme);
	}
	
	@SubscribeEvent
	public static void onResourceReload(AddReloadListenerEvent event)
	{
		event.addListener(INSTANCE = new ComputerThemeManager());
	}
}
package com.mraof.minestuck.computer.theme;

import com.google.common.collect.ImmutableMap;
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
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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
	
	private final Map<ResourceLocation, ComputerTheme> themes;
	private final ComputerTheme.Data defaultThemeData;
	
	@Nullable
	private static ComputerThemeManager INSTANCE;
	
	public ComputerThemeManager(Map<ResourceLocation, ComputerTheme> themes)
	{
		this.themes = themes;
		defaultThemeData = Optional.ofNullable(themes.get(MSComputerThemes.DEFAULT))
				.map(ComputerTheme::data).orElse(ComputerTheme.Data.DEFAULT);
	}
	
	public static ComputerThemeManager instance()
	{
		return Objects.requireNonNull(INSTANCE, "Computer themes haven't been loaded yet!");
	}
	
	public ComputerTheme lookup(ResourceLocation themeId)
	{
		return Objects.requireNonNullElseGet(this.themes.get(themeId), () -> new ComputerTheme(themeId, defaultThemeData));
	}
	
	public Collection<ComputerTheme> allThemes()
	{
		return this.themes.values();
	}
	
	@SubscribeEvent
	public static void registerLoader(RegisterClientReloadListenersEvent event)
	{
		event.registerReloadListener(new Loader());
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
			ImmutableMap.Builder<ResourceLocation, ComputerTheme> computerThemes = ImmutableMap.builder();
			for(Map.Entry<ResourceLocation, JsonElement> entry : jsonEntries.entrySet())
			{
				ResourceLocation id = entry.getKey();
				ComputerTheme.Data.CODEC.parse(JsonOps.INSTANCE, entry.getValue())
						.resultOrPartial(LOGGER::error)
						.ifPresent(data -> computerThemes.put(id, new ComputerTheme(id, data)));
			}
			
			INSTANCE = new ComputerThemeManager(computerThemes.build());
			LOGGER.info("Loaded {} computer themes", INSTANCE.themes.size());
		}
	}
}

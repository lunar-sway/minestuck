package com.mraof.minestuck.data;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.computer.theme.ComputerTheme;
import com.mraof.minestuck.computer.theme.ComputerThemeManager;
import com.mraof.minestuck.computer.theme.ComputerThemes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Data generating file for ComputerTheme json files. A ComputerTheme is resource pack only,
 * so they are generated into the assets directory assets/minestuck/minestuck/computer_themes/
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ComputerThemeProvider implements DataProvider
{
	private final Map<ResourceLocation, ComputerTheme> computerThemes = new HashMap<>();
	private final PackOutput output;
	private final String modid;
	
	public ComputerThemeProvider(PackOutput output, String modid)
	{
		this.output = output;
		this.modid = modid;
	}
	
	protected void registerThemes()
	{
		for(ComputerThemes theme : ComputerThemes.values())
		{
			add(theme, theme.getTextColor());
		}
	}
	
	protected void add(ComputerThemes theme, int textColor)
	{
		//Just set the name manually if this throws an exception
		add(new ResourceLocation(Minestuck.MOD_ID, "textures/gui/theme/" + theme.getLowercaseName() + ".png"), textColor, theme);
	}
	
	protected void add(ResourceLocation textureLocation, int textColor, ComputerThemes theme)
	{
		add(new ComputerTheme(textureLocation, textColor, theme.getLangLocation()), new ResourceLocation(modid, theme.getLowercaseName()));
	}
	
	protected void add(ComputerTheme computerTheme, ResourceLocation name)
	{
		computerThemes.put(name, computerTheme);
	}
	
	@Override
	public CompletableFuture<?> run(CachedOutput cache)
	{
		registerThemes();
		
		Path outputPath = output.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(modid).resolve(ComputerThemeManager.PATH_W_SLASH);
		List<CompletableFuture<?>> futures = new ArrayList<>(computerThemes.size());
		
		for(Map.Entry<ResourceLocation, ComputerTheme> entry : computerThemes.entrySet())
		{
			Path themePath = getPath(outputPath, entry.getKey());
			JsonElement encodedTheme = ComputerTheme.CODEC.encodeStart(JsonOps.INSTANCE, entry.getValue()).getOrThrow(false, LOGGER::error);
			futures.add(DataProvider.saveStable(cache, encodedTheme, themePath));
		}
		return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
	}
	
	private static Path getPath(Path outputPath, ResourceLocation id)
	{
		return outputPath.resolve(id.getPath() + ".json");
	}
	
	@Override
	public String getName()
	{
		return "Computer Themes";
	}
}
package com.mraof.minestuck.data;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
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
	private final Map<ResourceLocation, ComputerTheme.Data> computerThemes = new HashMap<>();
	private final PackOutput output;
	private final String modid;
	
	public ComputerThemeProvider(PackOutput output, String modid)
	{
		this.output = output;
		this.modid = modid;
	}
	
	protected void registerThemes()
	{
		add(ComputerThemes.DEFAULT, ComputerTheme.Data.DEFAULT);
		add(ComputerThemes.PESTERCHUM, 0x404040);
		add(ComputerThemes.TROLLIAN, 0xFF0000);
		add(ComputerThemes.CROCKER, 0x000000);
		add(ComputerThemes.TYPHEUS, 0x6DAFAD);
		add(ComputerThemes.CETUS, 0x9081EE);
		add(ComputerThemes.HEPHAESTUS, 0xFFFFFF);
		add(ComputerThemes.ECHIDNA, 0x005DFF);
		add(ComputerThemes.JOY, 0x282828);
		add(ComputerThemes.SBURB_95, 0x282828);
	}
	
	protected void add(ResourceLocation id, int textColor)
	{
		ResourceLocation textureLocation = id.withPath(name -> "textures/gui/theme/" + name + ".png");
		add(id, new ComputerTheme.Data(textureLocation, textColor));
	}
	
	protected void add(ResourceLocation id, ComputerTheme.Data data)
	{
		computerThemes.put(id, data);
	}
	
	@Override
	public CompletableFuture<?> run(CachedOutput cache)
	{
		registerThemes();
		
		Path outputPath = output.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(modid).resolve(ComputerThemeManager.PATH_W_SLASH);
		List<CompletableFuture<?>> futures = new ArrayList<>(computerThemes.size());
		
		for(Map.Entry<ResourceLocation, ComputerTheme.Data> entry : computerThemes.entrySet())
		{
			Path themePath = getPath(outputPath, entry.getKey());
			JsonElement encodedTheme = ComputerTheme.Data.CODEC.encodeStart(JsonOps.INSTANCE, entry.getValue()).getOrThrow(false, LOGGER::error);
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
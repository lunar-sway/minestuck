package com.mraof.minestuck.data;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.computer.theme.ComputerTheme;
import com.mraof.minestuck.computer.theme.ComputerThemes;
import com.mraof.minestuck.computer.theme.MSComputerThemes;
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
import java.util.Optional;
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
	
	public ComputerThemeProvider(PackOutput output)
	{
		this.output = output;
	}
	
	protected void registerThemes()
	{
		add(MSComputerThemes.DEFAULT, ComputerTheme.Data.DEFAULT);
		addWithButtons(MSComputerThemes.PESTERCHUM, 0x404040);
		addWithButtons(MSComputerThemes.TROLLIAN, 0xFF0000);
		addWithButtons(MSComputerThemes.CROCKER, 0x000000);
		addWithButtons(MSComputerThemes.TYPHEUS, 0x6DAFAD);
		addWithButtons(MSComputerThemes.CETUS, 0x9081EE);
		addWithButtons(MSComputerThemes.HEPHAESTUS, 0xFFFFFF);
		addWithButtons(MSComputerThemes.ECHIDNA, 0x005DFF);
		addWithButtons(MSComputerThemes.JOY, 0x282828);
		addWithButtons(MSComputerThemes.SBURB_95, 0x282828);
		addWithButtons(MSComputerThemes.SBURB_10, 0xFFFFFF);
		addWithButtons(MSComputerThemes.SCOURGING_HEAT, 0xFFFFFF);
		addWithButtons(MSComputerThemes.LIFDOFF, 0xFFFF00);
		addWithButtons(MSComputerThemes.SKAIANET_GREEN, 0xFFFFFF);
		addWithButtons(MSComputerThemes.SKAIANET_WHITE, 0x006600);
		addWithButtons(MSComputerThemes.SKAIANET_BLACK, 0xFFFFFF);
		addWithButtons(MSComputerThemes.ASTRAL_CHARTS, 0xFFFFFF);
		addWithButtons(MSComputerThemes.TILLDEATH, 0xFFFFFF);
		addWithButtons(MSComputerThemes.LOWAS, 0xBFEFEA);
		addWithButtons(MSComputerThemes.SPIROGRAPH, 0x006600);
		addWithButtons(MSComputerThemes.MINESTUCK, 0xFFFFFF);
	}
	
	protected void addWithButtons(ResourceLocation id, int textColor)
	{
		ResourceLocation buttonLocations = id.withPath(name -> "theme/" + name);
		add(id, new ComputerTheme.Data(id.withPath(name -> "textures/gui/theme/" + name + ".png"),
				textColor,
				// gui/widget and .png are added automatically
				Optional.of(buttonLocations.withSuffix("/button")),
				Optional.of(buttonLocations.withSuffix("/button_disabled")),
				Optional.of(buttonLocations.withSuffix("/button_highlighted"))));
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
		
		Path outputPath = output.getOutputFolder(PackOutput.Target.RESOURCE_PACK);
		List<CompletableFuture<?>> futures = new ArrayList<>(computerThemes.size());
		
		for(Map.Entry<ResourceLocation, ComputerTheme.Data> entry : computerThemes.entrySet())
		{
			Path themePath = getPath(outputPath, entry.getKey());
			JsonElement encodedTheme = ComputerTheme.Data.CODEC.encodeStart(JsonOps.INSTANCE, entry.getValue()).getOrThrow();
			futures.add(DataProvider.saveStable(cache, encodedTheme, themePath));
		}
		return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
	}
	
	private static Path getPath(Path outputPath, ResourceLocation id)
	{
		return outputPath.resolve(id.getNamespace() + "/" + ComputerThemes.PATH + "/" + id.getPath() + ".json");
	}
	
	@Override
	public String getName()
	{
		return "Computer Themes";
	}
}

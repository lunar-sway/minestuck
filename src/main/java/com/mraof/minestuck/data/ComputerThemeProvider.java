package com.mraof.minestuck.data;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.computer.theme.ComputerTheme;
import com.mraof.minestuck.computer.theme.ComputerThemeManager;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;

import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;

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
		add(ComputerTheme.DEFAULT_NAME, ComputerTheme.DEFAULT_TEXT_COLOR);
		add("pesterchum", 0x404040);
		add("trollian", 0xFF0000);
		add("crocker", 0x000000);
		add("typheus", 0x6DAFAD);
		add("cetus", 0x9081EE);
		add("hephaestus", 0xFFFFFF);
		add("echidna", 0x005DFF);
		add("joy", 0x282828);
		add("sburb_95", 0x282828);
	}
	
	protected void add(String themeName, int value)
	{
		//Just set the name manually if this throws an exception
		add(new ResourceLocation(Minestuck.MOD_ID, "textures/gui/theme/" + themeName + ".png"), ConstantInt.of(value), themeName);
	}
	
	protected void add(ResourceLocation textureLocation, ConstantInt range, String name)
	{
		add(new ComputerTheme(textureLocation, range, name), new ResourceLocation(modid, name));
	}
	
	protected void add(ComputerTheme computerTheme, ResourceLocation name)
	{
		computerThemes.put(name, computerTheme);
	}
	
	@Override
	public CompletableFuture<?> run(CachedOutput cache)
	{
		registerThemes();
		
		Path outputPath = output.getOutputFolder();
		//TODO using the commented out code here and in getPath() puts it in the assets folder, but code in ComputerThemeManager/ComputerTheme is unable to account for switch
		//Path outputPath = output.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(modid).resolve(ComputerThemeManager.PATH);
		List<CompletableFuture<?>> futures = new ArrayList<>(computerThemes.size());
		
		for(Map.Entry<ResourceLocation, ComputerTheme> entry : computerThemes.entrySet())
		{
			Path themePath = getPath(outputPath, entry.getKey());
			futures.add(DataProvider.saveStable(cache, ComputerThemeManager.parseTheme(entry.getValue()), themePath));
		}
		return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
	}
	
	private static Path getPath(Path outputPath, ResourceLocation id)
	{
		return outputPath.resolve("data/" + id.getNamespace() + "/minestuck/computer_themes/" + id.getPath() + ".json");
		//return outputPath.resolve("/" + id.getPath() + ".json");
	}
	
	@Override
	public String getName()
	{
		return "Computer Themes";
	}
}
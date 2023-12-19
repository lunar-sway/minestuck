package com.mraof.minestuck.data;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.BoondollarPriceManager;
import com.mraof.minestuck.util.BoondollarPricing;
import com.mraof.minestuck.util.ComputerThemeData;
import com.mraof.minestuck.util.ComputerThemeDataManager;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static com.mraof.minestuck.item.MSItems.*;
import static net.minecraft.world.item.Items.*;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ComputerThemeDataProvider implements DataProvider
{
	private final Map<ResourceLocation, ComputerThemeData> pricings = new HashMap<>();
	private final PackOutput output;
	private final String modid;
	
	public ComputerThemeDataProvider(PackOutput output, String modid)
	{
		this.output = output;
		this.modid = modid;
	}
	
	protected void registerPricings()
	{
		add("default", 0x404040);
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
	
	protected void add(String item, int value)
	{
		//Just set the name manually if this throws an exception
		add(new ResourceLocation(Minestuck.MOD_ID, "textures/gui/theme/" + item + ".png"), ConstantInt.of(value), item);
	}
	
	protected void add(ResourceLocation ingredient, ConstantInt range, String name)
	{
		add(new ComputerThemeData(ingredient, range), new ResourceLocation(modid, name));
	}
	
	protected void add(ComputerThemeData pricing, ResourceLocation name)
	{
		pricings.put(name, pricing);
	}
	
	@Override
	public CompletableFuture<?> run(CachedOutput cache)
	{
		registerPricings();
		
		Path outputPath = output.getOutputFolder();
		List<CompletableFuture<?>> futures = new ArrayList<>(pricings.size());
		
		for(Map.Entry<ResourceLocation, ComputerThemeData> entry : pricings.entrySet())
		{
			Path pricingPath = getPath(outputPath, entry.getKey());
			futures.add(DataProvider.saveStable(cache, ComputerThemeDataManager.parsePrice(entry.getValue()), pricingPath));
		}
		return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
	}
	
	private static Path getPath(Path outputPath, ResourceLocation id)
	{
		return outputPath.resolve("data/" + id.getNamespace() + "/minestuck/computer_themes/" + id.getPath() + ".json");
	}
	
	@Override
	public String getName()
	{
		return "Computer Themes";
	}
}
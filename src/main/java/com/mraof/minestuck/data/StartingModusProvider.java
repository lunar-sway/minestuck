package com.mraof.minestuck.data;

import com.google.gson.JsonArray;
import com.mraof.minestuck.inventory.captchalogue.ModusType;
import com.mraof.minestuck.inventory.captchalogue.ModusTypes;
import com.mraof.minestuck.inventory.captchalogue.StartingModusManager;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class StartingModusProvider implements DataProvider
{
	private final DataGenerator generator;
	private final String modid;
	
	public StartingModusProvider(DataGenerator generator, String modid)
	{
		this.generator = generator;
		this.modid = modid;
	}
	
	protected List<ModusType<?>> createDefaultModusTypes()
	{
		return List.of(ModusTypes.STACK.get(), ModusTypes.QUEUE.get());
	}
	
	@Override
	public final void run(CachedOutput cache) throws IOException
	{
		Path path = this.generator.getOutputFolder().resolve("data/" + modid + "/" + StartingModusManager.PATH);
		List<ModusType<?>> modusTypes = createDefaultModusTypes();
		
		JsonArray jsonList = new JsonArray(modusTypes.size());
		modusTypes.forEach(modusType -> jsonList.add(String.valueOf(ModusTypes.REGISTRY.get().getKey(modusType))));
		DataProvider.saveStable(cache, jsonList, path);
	}
	
	@Override
	public String getName()
	{
		return "Minestuck Starting Modus";
	}
}

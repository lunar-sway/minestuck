package com.mraof.minestuck.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mraof.minestuck.inventory.captchalogue.ModusType;
import com.mraof.minestuck.inventory.captchalogue.ModusTypes;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.mraof.minestuck.inventory.captchalogue.StartingModusManager.PATH;

public class MinestuckStartingModusProvider implements DataProvider
{
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private final DataGenerator generator;
	
	public MinestuckStartingModusProvider(DataGenerator generator)
	{
		this.generator = generator;
	}
	
	private List<ModusType<?>> createDefaultModusTypes()
	{
		LinkedList<ModusType<?>> modusTypes = new LinkedList<>();
		modusTypes.add(ModusTypes.STACK);
		modusTypes.add(ModusTypes.QUEUE);
		return modusTypes;
	}
	
	@Override
	public void run(HashCache pCache) throws IOException
	{
		Path path = this.generator.getOutputFolder().resolve("data/minestuck/" + PATH);
		List<ModusType<?>> modusTypes = createDefaultModusTypes();
		
		String data = GSON.toJson(modusTypes.stream().map(modusType -> modusType.getRegistryName().toString()).collect(Collectors.toList()));
		String hash = DataProvider.SHA1.hashUnencodedChars(data).toString();
		if(!Objects.equals(pCache.getHash(path), hash) || !Files.exists(path))
		{
			Files.createDirectories(path.getParent());
			try(BufferedWriter bufferedwriter = Files.newBufferedWriter(path))
			{
				bufferedwriter.write(data);
			}
		}
		
		pCache.putNew(path, hash);
	}
	
	@Override
	public String getName()
	{
		return "Minestuck Starting Modus";
	}
}

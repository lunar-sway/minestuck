package com.mraof.minestuck.data;

import com.google.gson.JsonArray;
import com.mraof.minestuck.inventory.captchalogue.ModusType;
import com.mraof.minestuck.inventory.captchalogue.ModusTypes;
import com.mraof.minestuck.inventory.captchalogue.StartingModusManager;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class StartingModusProvider implements DataProvider
{
	private final PackOutput output;
	private final String modid;
	
	public StartingModusProvider(PackOutput output, String modid)
	{
		this.output = output;
		this.modid = modid;
	}
	
	protected List<ModusType<?>> createDefaultModusTypes()
	{
		return List.of(ModusTypes.STACK.get(), ModusTypes.QUEUE.get());
	}
	
	@Override
	public final CompletableFuture<?> run(CachedOutput cache)
	{
		Path path = this.output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(modid).resolve(StartingModusManager.PATH);
		List<ModusType<?>> modusTypes = createDefaultModusTypes();
		
		JsonArray jsonList = new JsonArray(modusTypes.size());
		modusTypes.forEach(modusType -> jsonList.add(String.valueOf(ModusTypes.REGISTRY.getKey(modusType))));
		
		return DataProvider.saveStable(cache, jsonList, path);
	}
	
	@Override
	public String getName()
	{
		return "Minestuck Starting Modus";
	}
}

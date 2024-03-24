package com.mraof.minestuck.alchemy;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.Minestuck;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.io.Reader;
import java.util.*;


@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class PredeterminedCaptchaLoader extends SimplePreparableReloadListener<Map<String, ResourceLocation>>
{
	private static final Logger LOGGER = LogManager.getLogger();
	public static final String PATH = "minestuck/captcha_codes.json";
	
	@Override
	protected Map<String, ResourceLocation> prepare(ResourceManager resourceManager, ProfilerFiller profiler)
	{
		Map<String, ResourceLocation> captchaData = new HashMap<>();
		
		for(String namespace : resourceManager.getNamespaces())
		{
			parseCaptchaCodesFromNamespace(resourceManager, namespace).ifPresent(parsedData -> {
				for(String captcha : parsedData.keySet())
					captchaData.put(captcha, parsedData.get(captcha));
			});
		}
		
		return captchaData;
	}
	
	@Override
	protected void apply(Map<String, ResourceLocation> data, ResourceManager resourceManager, ProfilerFiller profiler)
	{
		for(String captcha : data.keySet())
		{
			if(!ForgeRegistries.ITEMS.containsKey(data.get(captcha)))
			{
				LOGGER.atInfo().log(data.get(captcha) + " does not point to a real item");
			}
			
			Item item = ForgeRegistries.ITEMS.getValue(data.get(captcha));
			
			PredeterminedCardCaptchas.SetData(item, captcha);
			LOGGER.atInfo().log(item + " was added to card with string: " + captcha);
		}
	}
	
	@SubscribeEvent
	public static void onResourceReload(AddReloadListenerEvent event)
	{
		PredeterminedCardCaptchas.predefinedCardMap.clear();
		event.addListener(new PredeterminedCaptchaLoader());
	}
	
	private static final Codec<Map<String, ResourceLocation>> PREDEFINED_CAPTCHAS_CODEC = Codec.unboundedMap(Codec.STRING, ResourceLocation.CODEC);
	
	private static Optional<Map<String, ResourceLocation>> parseCaptchaCodesFromNamespace(ResourceManager resourceManager, String namespace)
	{
		return parseCodecDataFromLocation(resourceManager, new ResourceLocation(namespace, PATH), PREDEFINED_CAPTCHAS_CODEC);
	}
	private static <T> Optional<T> parseCodecDataFromLocation(ResourceManager resourceManager, ResourceLocation location, Codec<T> codec)
	{
		return resourceManager.getResource(location).flatMap(resource -> {
			try(Reader reader = resource.openAsReader())
			{
				JsonElement json = JsonParser.parseReader(reader);
				return codec.parse(JsonOps.INSTANCE, json)
						.resultOrPartial(message -> LOGGER.error("Problem parsing json: {}, reason: {}", location, message));
				
			} catch(IOException ignored)
			{
				return Optional.empty();
			}
		});
	}
}

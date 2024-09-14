package com.mraof.minestuck.alchemy;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.Minestuck;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PredeterminedCardCaptchas
{
	@Nullable
	private static BiMap<Item, String> predefinedCardMap;
	
	private static void setData(BiMap<Item, String> predefinedCards)
	{
		PredeterminedCardCaptchas.predefinedCardMap = predefinedCards;
	}
	
	public static BiMap<Item, String> getData()
	{
		return Objects.requireNonNull(PredeterminedCardCaptchas.predefinedCardMap, "Tried to get an instance of Predetermined Captchas too early.");
	}
	
	@SubscribeEvent
	public static void onServerStopped(ServerStoppedEvent event)
	{
		predefinedCardMap = null;
	}
	
	@SubscribeEvent
	public static void onResourceReload(AddReloadListenerEvent event)
	{
		event.addListener(new Loader());
	}
	
	private final static class Loader extends SimplePreparableReloadListener<Map<String, Item>>
	{
		private static final Logger LOGGER = LogManager.getLogger();
		public static final String PATH = "minestuck/captcha_codes.json";
		@Override
		protected Map<String, Item> prepare(ResourceManager resourceManager, ProfilerFiller profiler)
		{
			BiMap<String, Item> captchaData = HashBiMap.create();
			
			for(String namespace : resourceManager.getNamespaces())
			{
				parseCaptchaCodesFromNamespace(resourceManager, namespace).ifPresent(parsedData -> processCaptchaData(captchaData, parsedData));
			}
			return captchaData;
		}
		
		@Override
		protected void apply(Map<String, Item> data, ResourceManager resourceManager, ProfilerFiller profiler)
		{
			ImmutableBiMap.Builder<Item, String> predefinedCards = ImmutableBiMap.builder();
			
			for(Map.Entry<String, Item> entrySet : data.entrySet())
			{
				predefinedCards.put(entrySet.getValue(), entrySet.getKey());
			}
			
			PredeterminedCardCaptchas.setData(predefinedCards.build());
		}
		
		private static final Codec<Map<String, Item>> PREDEFINED_CAPTCHAS_CODEC = Codec.unboundedMap(Codec.STRING, BuiltInRegistries.ITEM.byNameCodec());
		
		private static Optional<Map<String, Item>> parseCaptchaCodesFromNamespace(ResourceManager resourceManager, String namespace)
		{
			ResourceLocation rs = new ResourceLocation(namespace, PATH);
			
			return resourceManager.getResource(rs).flatMap(resource -> {
				try(Reader reader = resource.openAsReader())
				{
					JsonElement json = JsonParser.parseReader(reader);
					return PREDEFINED_CAPTCHAS_CODEC.parse(JsonOps.INSTANCE, json)
							.resultOrPartial(message -> LOGGER.error("Problem parsing json: {}, reason: {}", rs, message));
					
				} catch(IOException ignored)
				{
					return Optional.empty();
				}
			});
		}
		
		private static void processCaptchaData(BiMap<String, Item> incompleteMap, Map<String, Item> parsedMap)
		{
			for(Map.Entry<String, Item> entry : parsedMap.entrySet())
			{
				String captcha = entry.getKey();
				Item item = entry.getValue();
				
				if(incompleteMap.containsValue(item))
				{
					LOGGER.error("Item {} already has an existing Code of '{}'!", item , incompleteMap.inverse().get(item));
				}
				else if(incompleteMap.containsKey(captcha))
				{
					LOGGER.error("Code '{}' is already assigned to Item: {}!", captcha, incompleteMap.get(captcha));
				}
				else
				{
					incompleteMap.put(entry.getKey(), entry.getValue());
				}
			}
		}
	}
}

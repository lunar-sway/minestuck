package com.mraof.minestuck.alchemy;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
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
public final class PredeterminedCaptchaLoader extends SimplePreparableReloadListener<Map<String, Item>>
{
	private static final Logger LOGGER = LogManager.getLogger();
	public static final String PATH = "minestuck/captcha_codes.json";
	
	@Override
	protected Map<String, Item> prepare(ResourceManager resourceManager, ProfilerFiller profiler)
	{
		Map<String, Item> captchaData = new HashMap<>();
		
		for(String namespace : resourceManager.getNamespaces())
		{
			parseCaptchaCodesFromNamespace(resourceManager, namespace).ifPresent(captchaData::putAll);
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
			LOGGER.atInfo().log(entrySet.getValue() + " was added to card with string: " + entrySet.getKey());
		}
		
		PredeterminedCardCaptchas.SetData(predefinedCards.build());
	}
	
	@SubscribeEvent
	public static void onResourceReload(AddReloadListenerEvent event)
	{
		event.addListener(new PredeterminedCaptchaLoader());
	}
	
	private static final Codec<Map<String, Item>> PREDEFINED_CAPTCHAS_CODEC = Codec.unboundedMap(Codec.STRING, ForgeRegistries.ITEMS.getCodec());
	
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
}

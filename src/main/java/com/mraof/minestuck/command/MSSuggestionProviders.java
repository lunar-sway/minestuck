package com.mraof.minestuck.command;

import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.dialogue.DialogueNodes;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public final class MSSuggestionProviders
{
	public final SuggestionProvider<CommandSourceStack> DIALOGUE_ENTITY_TYPE = SuggestionProviders.register(Minestuck.id("dialogue_entity_type"), (context, builder) -> {
		//todo need a better way of identifying valid entity types
		Iterable<EntityType<?>> dialogueEntities = List.of(MSEntityTypes.SALAMANDER.get(), MSEntityTypes.TURTLE.get(), MSEntityTypes.NAKAGATOR.get(), MSEntityTypes.IGUANA.get());
		return SharedSuggestionProvider.suggestResource(dialogueEntities, builder, EntityType::getKey,
				type -> Component.translatable(Util.makeDescriptionId("entity", EntityType.getKey(type))));
	});
	// this suggestion provider is not registered because dialogue nodes are not available at client-side
	public static final SuggestionProvider<CommandSourceStack> ALL_DIALOGUE_NODES = (context, builder) -> SharedSuggestionProvider.suggestResource(DialogueNodes.getInstance().allIds(), builder);
	
	private MSSuggestionProviders()
	{
	}
	
	@Nullable
	private static MSSuggestionProviders instance;
	
	public static MSSuggestionProviders instance()
	{
		return Objects.requireNonNull(instance, "Tried to get instance before suggestions had been set up.");
	}
	
	/**
	 * To be called during main thread setup as {@link net.minecraft.commands.synchronization.SuggestionProviders#register(ResourceLocation, SuggestionProvider)} does not appear to be thread-safe.
	 */
	public static void register()
	{
		instance = new MSSuggestionProviders();
	}
}

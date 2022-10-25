package com.mraof.minestuck.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.alchemy.GristTypes;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.ArgumentSerializer;
import net.minecraft.commands.synchronization.EmptyArgumentSerializer;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GristTypeArgument implements ArgumentType<GristType>
{
	public static final ArgumentSerializer<GristTypeArgument> SERIALIZER = new EmptyArgumentSerializer<>(GristTypeArgument::gristType);
	
	private static final List<String> EXAMPLES = Arrays.asList("minestuck:build", "minestuck:marble", "minestuckarsenal:iron");
	
	public static final String INVALID = "argument.grist_type.invalid";
	public static final DynamicCommandExceptionType INVALID_TYPE = new DynamicCommandExceptionType(o -> new TranslatableComponent(INVALID, o));
	
	public static GristTypeArgument gristType()
	{
		return new GristTypeArgument();
	}
	
	@Override
	public GristType parse(StringReader reader) throws CommandSyntaxException
	{
		int start2 = reader.getCursor();
		ResourceLocation gristName = ResourceLocation.read(reader);
		if(!GristTypes.getRegistry().containsKey(gristName))
		{
			reader.setCursor(start2);
			throw INVALID_TYPE.createWithContext(reader, gristName);
		}
		return GristTypes.getRegistry().getValue(gristName);
	}
	
	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder)
	{
		return SharedSuggestionProvider.suggestResource(GristTypes.values().stream().map(GristType::getRegistryName), builder);
	}
	
	@Override
	public Collection<String> getExamples()
	{
		return EXAMPLES;
	}
	
	@SuppressWarnings("unused")
	public static GristType getGristArgument(CommandContext<CommandSourceStack> context, String id)
	{
		return context.getArgument(id, GristType.class);
	}
}
package com.mraof.minestuck.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mraof.minestuck.item.crafting.alchemy.GristType;
import com.mraof.minestuck.item.crafting.alchemy.GristTypes;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.ArgumentSerializer;
import net.minecraft.command.arguments.IArgumentSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GristTypeArgument implements ArgumentType<GristType>
{
	public static final IArgumentSerializer<GristTypeArgument> SERIALIZER = new ArgumentSerializer<>(GristTypeArgument::gristType);
	
	private static final List<String> EXAMPLES = Arrays.asList("minestuck:build", "minestuck:marble", "minestuckarsenal:iron");
	
	public static final String INVALID = "argument.grist_type.invalid";
	public static final DynamicCommandExceptionType INVALID_TYPE = new DynamicCommandExceptionType(o -> new TranslationTextComponent(INVALID, o));
	
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
		return ISuggestionProvider.suggestResource(GristTypes.values().stream().map(GristType::getRegistryName), builder);
	}
	
	@Override
	public Collection<String> getExamples()
	{
		return EXAMPLES;
	}
	
	@SuppressWarnings("unused")
	public static GristType getGristArgument(CommandContext<CommandSource> context, String id)
	{
		return context.getArgument(id, GristType.class);
	}
}
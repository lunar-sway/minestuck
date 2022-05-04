package com.mraof.minestuck.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.lands.title.TitleLandType;
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

public class TitleLandTypeArgument implements ArgumentType<TitleLandType>
{
	public static final IArgumentSerializer<TitleLandTypeArgument> SERIALIZER = new ArgumentSerializer<>(TitleLandTypeArgument::titleLandType);
	
	private static final List<String> EXAMPLES = Arrays.asList("minestuck:frost", "minestuck:shade");
	
	public static final String INVALID = "argument.title_land_type.invalid";
	public static final DynamicCommandExceptionType INVALID_TYPE = new DynamicCommandExceptionType(o -> new TranslationTextComponent(INVALID, o));
	
	public static TitleLandTypeArgument titleLandType()
	{
		return new TitleLandTypeArgument();
	}
	
	@Override
	public TitleLandType parse(StringReader reader) throws CommandSyntaxException
	{
		int start2 = reader.getCursor();
		ResourceLocation gristName = ResourceLocation.read(reader);
		if(!LandTypes.TITLE_REGISTRY.containsKey(gristName))
		{
			reader.setCursor(start2);
			throw INVALID_TYPE.createWithContext(reader, gristName);
		}
		return LandTypes.TITLE_REGISTRY.getValue(gristName);
	}
	
	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder)
	{
		return ISuggestionProvider.suggestResource(LandTypes.TITLE_REGISTRY.getValues().stream().map(TitleLandType::getRegistryName), builder);
	}
	
	@Override
	public Collection<String> getExamples()
	{
		return EXAMPLES;
	}
	
	public static TitleLandType get(CommandContext<CommandSource> context, String id)
	{
		return context.getArgument(id, TitleLandType.class);
	}
}
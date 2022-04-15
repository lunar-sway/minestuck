package com.mraof.minestuck.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.EnumClass;
import com.mraof.minestuck.player.Title;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.ArgumentSerializer;
import net.minecraft.command.arguments.IArgumentSerializer;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class TitleArgument implements ArgumentType<Title>
{
	public static final IArgumentSerializer<TitleArgument> SERIALIZER = new ArgumentSerializer<>(TitleArgument::title);
	
	private static final List<String> EXAMPLES = Arrays.asList("heir light", "bard void", "lord doom");
	public static final String INCOMPLETE = "argument.title.incomplete";
	public static final String INVALID_CLASS = "argument.title.invalid_class";
	public static final String INVALID_ASPECT = "argument.title.invalid_aspect";
	private static final SimpleCommandExceptionType PAIR_INCOMPLETE = new SimpleCommandExceptionType(new TranslationTextComponent(INCOMPLETE));
	private static final DynamicCommandExceptionType INVALID_CLASS_TYPE = new DynamicCommandExceptionType(o -> new TranslationTextComponent(INVALID_CLASS, o));
	private static final DynamicCommandExceptionType INVALID_ASPECT_TYPE = new DynamicCommandExceptionType(o -> new TranslationTextComponent(INVALID_ASPECT, o));
	
	public static TitleArgument title()
	{
		return new TitleArgument();
	}
	
	@Override
	public Title parse(StringReader reader) throws CommandSyntaxException
	{
		if(!reader.canRead())
			throw PAIR_INCOMPLETE.createWithContext(reader);
		
		String className = reader.readUnquotedString();
		EnumClass c = EnumClass.fromString(className);
		if(c == null)
			throw INVALID_CLASS_TYPE.createWithContext(reader, className);
		if(reader.canRead() && reader.peek() == ' ')
		{
			reader.skip();
			String aspectName = reader.readUnquotedString();
			EnumAspect a = EnumAspect.fromString(aspectName);
			if(a == null)
				throw INVALID_ASPECT_TYPE.createWithContext(reader, aspectName);
			return new Title(c, a);
		} else throw PAIR_INCOMPLETE.createWithContext(reader);
	}
	
	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder)
	{
		String remaining = builder.getRemaining().toLowerCase(Locale.ROOT);
		
		if(!remaining.contains(" "))	//Suggest for the class
		{
			return ISuggestionProvider.suggest(Stream.of(EnumClass.values()).map(EnumClass::toString), builder);
		} else	//Suggest for the aspect
		{
			String[] parts = remaining.split(" ");
			EnumClass c = EnumClass.fromString(parts[0]);
			if(c == null)	//Do no suggestions if the class is invalid
				return Suggestions.empty();
			else return ISuggestionProvider.suggest(Stream.of(EnumAspect.values()).map(a -> parts[0] + " " + a), builder);
		}
	}
	
	@Override
	public Collection<String> getExamples()
	{
		return EXAMPLES;
	}
	
	public static Title get(CommandContext<CommandSource> context, String id)
	{
		return context.getArgument(id, Title.class);
	}
}
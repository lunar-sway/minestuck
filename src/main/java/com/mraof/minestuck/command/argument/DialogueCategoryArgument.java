package com.mraof.minestuck.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mraof.minestuck.entity.dialogue.RandomlySelectableDialogue.DialogueCategory;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public final class DialogueCategoryArgument implements ArgumentType<DialogueCategory>
{
	public static final String INVALID = "argument.dialogue_category.invalid";
	public static final DynamicCommandExceptionType INVALID_TYPE = new DynamicCommandExceptionType(o -> Component.translatable(INVALID, o));
	
	public static final Collection<String> CATEGORY_STRINGS = Stream.of(DialogueCategory.values()).map(DialogueCategory::folderName).toList();
	
	@Override
	public DialogueCategory parse(StringReader reader) throws CommandSyntaxException
	{
		int start = reader.getCursor();
		String name = reader.readUnquotedString();
		Optional<DialogueCategory> categoryOptional = Stream.of(DialogueCategory.values()).filter(category -> category.name().equalsIgnoreCase(name)).findAny();
		if(categoryOptional.isEmpty())
		{
			reader.setCursor(start);
			throw INVALID_TYPE.createWithContext(reader, name);
		}
		return categoryOptional.get();
	}
	
	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder)
	{
		return SharedSuggestionProvider.suggest(CATEGORY_STRINGS, builder);
	}
	
	@Override
	public Collection<String> getExamples()
	{
		return CATEGORY_STRINGS;
	}
	
	public static DialogueCategory getCategory(CommandContext<CommandSourceStack> context, String id)
	{
		return context.getArgument(id, DialogueCategory.class);
	}
}

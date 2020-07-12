package com.mraof.minestuck.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;

import java.util.ArrayList;
import java.util.List;

public class ListArgument<T> implements ArgumentType<List<T>>
{
	private final ArgumentType<T> argument;
	
	private ListArgument(ArgumentType<T> argument)
	{
		this.argument = argument;
	}
	
	public static <T> ListArgument<T> list(ArgumentType<T> argument)
	{
		return new ListArgument<>(argument);
	}
	
	@Override
	public List<T> parse(StringReader reader) throws CommandSyntaxException
	{
		List<T> list = new ArrayList<>();
		do
		{
			T t = argument.parse(reader);
			
			list.add(t);
			
			if(reader.canRead() && reader.peek() == ' ')
				reader.skip();
			else break;
		} while(reader.canRead());
		
		return list;
	}
	
	public static <T> List<T> getListArgument(CommandContext<CommandSource> context, String id)
	{
		//noinspection unchecked
		return context.getArgument(id, List.class);
	}
}
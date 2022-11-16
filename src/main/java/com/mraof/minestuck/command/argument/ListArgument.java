package com.mraof.minestuck.command.argument;

import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.synchronization.ArgumentSerializer;
import net.minecraft.commands.synchronization.ArgumentTypes;
import net.minecraft.network.FriendlyByteBuf;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListArgument<T> implements ArgumentType<List<T>>
{
	public static final ArgumentSerializer<ListArgument<?>> SERIALIZER = new Serializer();
	private final ArgumentType<T> elementArgument;
	
	private ListArgument(ArgumentType<T> elementArgument)
	{
		this.elementArgument = Objects.requireNonNull(elementArgument);
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
			T t = elementArgument.parse(reader);
			
			list.add(t);
			
			if(reader.canRead() && reader.peek() == ' ')
				reader.skip();
			else break;
		} while(reader.canRead());
		
		return list;
	}
	
	public static <T> List<T> getListArgument(CommandContext<CommandSourceStack> context, String id)
	{
		//noinspection unchecked
		return context.getArgument(id, List.class);
	}
	
	private static final class Serializer implements ArgumentSerializer<ListArgument<?>>
	{
		@Override
		public void serializeToNetwork(ListArgument<?> argument, FriendlyByteBuf buffer)
		{
			ArgumentTypes.serialize(buffer, argument.elementArgument);
		}
		
		@Override
		public ListArgument<?> deserializeFromNetwork(FriendlyByteBuf buffer)
		{
			ArgumentType<?> elementArgument = ArgumentTypes.deserialize(buffer);
			return elementArgument == null ? null : ListArgument.list(elementArgument);
		}
		
		@Override
		public void serializeToJson(ListArgument<?> argument, JsonObject json)
		{
			json.addProperty("element", "???");
		}
	}
}
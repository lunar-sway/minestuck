package com.mraof.minestuck.command.argument;

import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.ArgumentTypes;
import net.minecraft.command.arguments.IArgumentSerializer;
import net.minecraft.network.PacketBuffer;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListArgument<T> implements ArgumentType<List<T>>
{
	public static final IArgumentSerializer<ListArgument<?>> SERIALIZER = new Serializer();
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
	
	public static <T> List<T> getListArgument(CommandContext<CommandSource> context, String id)
	{
		//noinspection unchecked
		return context.getArgument(id, List.class);
	}
	
	private static final class Serializer implements IArgumentSerializer<ListArgument<?>>
	{
		@Override
		public void serializeToNetwork(ListArgument<?> argument, @Nonnull PacketBuffer buffer)
		{
			ArgumentTypes.serialize(buffer, argument.elementArgument);
		}
		
		@Override
		public ListArgument<?> deserializeFromNetwork(@Nonnull PacketBuffer buffer)
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
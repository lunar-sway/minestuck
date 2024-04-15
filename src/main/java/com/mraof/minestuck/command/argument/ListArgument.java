package com.mraof.minestuck.command.argument;

import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListArgument<T> implements ArgumentType<List<T>>
{
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
	
	public static final class Info<T> implements ArgumentTypeInfo<ListArgument<T>, Info<T>.Template>
	{
		@Override
		public void serializeToNetwork(Template template, FriendlyByteBuf buffer)
		{
			serializeArgument(buffer, template.elementArgument);
		}
		
		// Some helper functions to work around the wildcards
		private static <A extends ArgumentType<?>> void serializeArgument(FriendlyByteBuf buffer, ArgumentTypeInfo.Template<A> template) {
			serializeArgument(buffer, template.type(), template);
		}
		@SuppressWarnings("unchecked")
		private static <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>> void serializeArgument(FriendlyByteBuf buffer, ArgumentTypeInfo<A, T> argumentInfo, ArgumentTypeInfo.Template<A> template)
		{
			buffer.writeId(BuiltInRegistries.COMMAND_ARGUMENT_TYPE, argumentInfo);
			argumentInfo.serializeToNetwork((T)template, buffer);
		}
		
		@Override
		public Template deserializeFromNetwork(FriendlyByteBuf buffer)
		{
			try
			{
				ArgumentTypeInfo<?, ?> elementArgInfo = buffer.readById(BuiltInRegistries.COMMAND_ARGUMENT_TYPE);
				return new Template((ArgumentTypeInfo.Template<? extends ArgumentType<T>>) elementArgInfo.deserializeFromNetwork(buffer));
			} catch(Exception e)
			{
				return null;
			}
		}
		
		@Override
		public void serializeToJson(Template template, JsonObject json)
		{
			throw new UnsupportedOperationException();
		}
		
		@Override
		public Template unpack(ListArgument<T> argument)
		{
			return new Template(ArgumentTypeInfos.unpack(argument.elementArgument));
		}
		
		public class Template implements ArgumentTypeInfo.Template<ListArgument<T>>
		{
			final ArgumentTypeInfo.Template<? extends ArgumentType<T>> elementArgument;
			
			public Template(ArgumentTypeInfo.Template<? extends ArgumentType<T>> elementArgument)
			{
				this.elementArgument = elementArgument;
			}
			
			@Override
			public ListArgument<T> instantiate(CommandBuildContext context)
			{
				return new ListArgument<>(this.elementArgument.instantiate(context));
			}
			
			@Override
			public ArgumentTypeInfo<ListArgument<T>, ?> type()
			{
				return Info.this;
			}
		}
	}
}
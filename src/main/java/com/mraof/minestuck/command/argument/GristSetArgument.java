package com.mraof.minestuck.command.argument;

import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mraof.minestuck.api.alchemy.DefaultMutableGristSet;
import com.mraof.minestuck.api.alchemy.MutableGristSet;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.NonNegativeGristSet;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

public class GristSetArgument implements ArgumentType<MutableGristSet>
{
	
	//TODO List suggestions
	//TODO Provide examples
	public static final String INCOMPLETE = "argument.grist_set.incomplete";
	public static final String DUPLICATE = "argument.grist_set.duplicate";
	public static final SimpleCommandExceptionType SET_INCOMPLETE = new SimpleCommandExceptionType(Component.translatable(INCOMPLETE));
	public static final DynamicCommandExceptionType DUPLICATE_TYPE = new DynamicCommandExceptionType(o -> Component.translatable(DUPLICATE, o));
	
	private static final GristTypeArgument gristArgument = GristTypeArgument.gristType();
	
	private final IntegerArgumentType intArgument;
	private final Mode mode;
	
	private GristSetArgument(Mode mode)
	{
		this.mode = mode;
		intArgument = mode == Mode.NON_NEGATIVE ? IntegerArgumentType.integer(0) : IntegerArgumentType.integer();
	}
	
	public static GristSetArgument gristSet()
	{
		return new GristSetArgument(Mode.STANDARD);
	}
	
	public static GristSetArgument nonNegativeSet()
	{
		return new GristSetArgument(Mode.NON_NEGATIVE);
	}
	
	@Override
	public MutableGristSet parse(StringReader reader) throws CommandSyntaxException
	{
		MutableGristSet set = mode == Mode.NON_NEGATIVE ? new NonNegativeGristSet() : MutableGristSet.newDefault();
		do
		{
			int start1 = reader.getCursor();
			int count = intArgument.parse(reader);
			
			if(reader.canRead() && reader.peek() == ' ')
			{
				reader.skip();
				int start2 = reader.getCursor();
				GristType type = gristArgument.parse(reader);
				if(!set.hasType(type))
				{
					set.add(type, count);
					
					if(reader.canRead() && reader.peek() == ' ')
						reader.skip();
					else break;
				} else {
					reader.setCursor(start2);
					throw DUPLICATE_TYPE.createWithContext(reader, type);
				}
			} else
			{
				reader.setCursor(start1);
				throw SET_INCOMPLETE.createWithContext(reader);
			}
		} while(reader.canRead());
		return set;
	}
	
	public static MutableGristSet getGristArgument(CommandContext<CommandSourceStack> context, String id)
	{
		return context.getArgument(id, DefaultMutableGristSet.class);
	}
	
	public static NonNegativeGristSet getNonNegativeGristArgument(CommandContext<CommandSourceStack> context, String id)
	{
		return context.getArgument(id, NonNegativeGristSet.class);
	}
	
	private enum Mode
	{
		STANDARD,
		NON_NEGATIVE
	}
	
	public static final class Info implements ArgumentTypeInfo<GristSetArgument, Info.Template>
	{
		@Override
		public void serializeToNetwork(Template template, FriendlyByteBuf buffer)
		{
			buffer.writeEnum(template.mode);
		}
		
		@Override
		public Template deserializeFromNetwork(FriendlyByteBuf buffer)
		{
			Mode mode = buffer.readEnum(Mode.class);
			return new Template(mode);
		}
		
		@Override
		public void serializeToJson(Template template, JsonObject json)
		{
			switch (template.mode) {
				case NON_NEGATIVE:
					json.addProperty("type", "non_negative");
					break;
				case STANDARD:
				default:
					json.addProperty("type", "standard");
					break;
			}
		}
		
		@Override
		public Template unpack(GristSetArgument argument)
		{
			return new Template(argument.mode);
		}
		
		public class Template implements ArgumentTypeInfo.Template<GristSetArgument>
		{
			final Mode mode;
			
			public Template(Mode mode)
			{
				this.mode = mode;
			}
			
			@Override
			public GristSetArgument instantiate(CommandBuildContext context)
			{
				return new GristSetArgument(this.mode);
			}
			
			@Override
			public ArgumentTypeInfo<GristSetArgument, ?> type()
			{
				return Info.this;
			}
		}
	}
}
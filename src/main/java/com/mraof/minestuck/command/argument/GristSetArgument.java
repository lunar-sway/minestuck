package com.mraof.minestuck.command.argument;

import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.item.crafting.alchemy.GristType;
import com.mraof.minestuck.item.crafting.alchemy.NonNegativeGristSet;
import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.IArgumentSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;

public class GristSetArgument implements ArgumentType<GristSet>
{
	public static final IArgumentSerializer<GristSetArgument> SERIALIZER = new Serializer();
	
	//TODO List suggestions
	//TODO Provide examples
	public static final String INCOMPLETE = "argument.grist_set.incomplete";
	public static final String DUPLICATE = "argument.grist_set.duplicate";
	public static final SimpleCommandExceptionType SET_INCOMPLETE = new SimpleCommandExceptionType(new TranslationTextComponent(INCOMPLETE));
	public static final DynamicCommandExceptionType DUPLICATE_TYPE = new DynamicCommandExceptionType(o -> new TranslationTextComponent(DUPLICATE, o));
	
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
	public GristSet parse(StringReader reader) throws CommandSyntaxException
	{
		GristSet set = mode == Mode.NON_NEGATIVE ? new NonNegativeGristSet() : new GristSet();
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
					set.addGrist(type, count);
					
					if(reader.canRead() && reader.peek() == ' ')
						reader.skip();
					else break;
				} else {
					reader.setCursor(start2);
					throw DUPLICATE_TYPE.createWithContext(reader, type.getRegistryName());
				}
			} else
			{
				reader.setCursor(start1);
				throw SET_INCOMPLETE.createWithContext(reader);
			}
		} while(reader.canRead());
		return set;
	}
	
	public static GristSet getGristArgument(CommandContext<CommandSource> context, String id)
	{
		return context.getArgument(id, GristSet.class);
	}
	
	public static NonNegativeGristSet getNonNegativeGristArgument(CommandContext<CommandSource> context, String id)
	{
		return context.getArgument(id, NonNegativeGristSet.class);
	}
	
	private enum Mode
	{
		STANDARD,
		NON_NEGATIVE
	}
	
	private static final class Serializer implements IArgumentSerializer<GristSetArgument>
	{
		@Override
		public void serializeToNetwork(GristSetArgument argument, PacketBuffer buffer)
		{
			buffer.writeEnum(argument.mode);
		}
		
		@Nonnull
		@Override
		public GristSetArgument deserializeFromNetwork(PacketBuffer buffer)
		{
			Mode mode = buffer.readEnum(Mode.class);
			return new GristSetArgument(mode);
		}
		
		@Override
		public void serializeToJson(GristSetArgument argument, @Nonnull JsonObject json)
		{
			switch (argument.mode) {
				case NON_NEGATIVE:
					json.addProperty("type", "non_negative");
					break;
				case STANDARD:
				default:
					json.addProperty("type", "standard");
					break;
			}
		}
	}
}
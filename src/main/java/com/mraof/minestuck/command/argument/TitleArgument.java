package com.mraof.minestuck.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.EnumClass;
import com.mraof.minestuck.player.Title;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.TranslationTextComponent;

public class TitleArgument implements ArgumentType<Title>
{
	public static final String INCOMPLETE = "argument.title.incomplete";
	public static final SimpleCommandExceptionType PAIR_INCOMPLETE = new SimpleCommandExceptionType(new TranslationTextComponent(INCOMPLETE));
	
	public static TitleArgument title()
	{
		return new TitleArgument();
	}
	
	@Override
	public Title parse(StringReader reader) throws CommandSyntaxException
	{
		EnumClass c = EnumClass.valueOf(reader.readUnquotedString());
		if(reader.peek() == ' ')
		{
			reader.skip();
			EnumAspect a = EnumAspect.valueOf(reader.readUnquotedString());
			return new Title(c, a);
		} else throw PAIR_INCOMPLETE.createWithContext(reader);
	}
	
	public static Title getTitleArgument(CommandContext<CommandSource> context, String id)
	{
		return context.getArgument(id, Title.class);
	}
}
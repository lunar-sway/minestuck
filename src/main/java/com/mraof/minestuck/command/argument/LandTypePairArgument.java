package com.mraof.minestuck.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mraof.minestuck.world.lands.LandTypePair;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import net.minecraft.network.chat.Component;

public class LandTypePairArgument implements ArgumentType<LandTypePair>
{
	public static final String INCOMPLETE = "argument.land_types.incomplete";
	private static final SimpleCommandExceptionType PAIR_INCOMPLETE = new SimpleCommandExceptionType(Component.translatable(INCOMPLETE));
	
	private static final TerrainLandTypeArgument TERRAIN_ARGUMENT = TerrainLandTypeArgument.terrainLandType();
	private static final TitleLandTypeArgument TITLE_ARGUMENT = TitleLandTypeArgument.titleLandType();
	
	public static LandTypePairArgument nullablePairs()
	{
		return new LandTypePairArgument();
	}
	
	@Override
	public LandTypePair parse(StringReader reader) throws CommandSyntaxException
	{
		if(reader.peek() == '~')
		{
			reader.skip();
			return null;
		} else
		{
			TerrainLandType terrain = TERRAIN_ARGUMENT.parse(reader);
			if(reader.peek() == ' ')
			{
				reader.skip();
				TitleLandType title = TITLE_ARGUMENT.parse(reader);
				return new LandTypePair(terrain, title);
			} else throw PAIR_INCOMPLETE.createWithContext(reader);
		}
	}
}
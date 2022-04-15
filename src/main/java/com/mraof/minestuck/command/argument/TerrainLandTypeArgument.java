package com.mraof.minestuck.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.ArgumentSerializer;
import net.minecraft.command.arguments.IArgumentSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TerrainLandTypeArgument implements ArgumentType<TerrainLandType>
{
	public static final IArgumentSerializer<TerrainLandTypeArgument> SERIALIZER = new ArgumentSerializer<>(TerrainLandTypeArgument::terrainLandType);
	
	private static final List<String> EXAMPLES = Arrays.asList("minestuck:frost", "minestuck:shade");
	
	public static final String INVALID = "argument.terrain_land_type.invalid";
	public static final DynamicCommandExceptionType INVALID_TYPE = new DynamicCommandExceptionType(o -> new TranslationTextComponent(INVALID, o));
	
	public static TerrainLandTypeArgument terrainLandType()
	{
		return new TerrainLandTypeArgument();
	}
	
	@Override
	public TerrainLandType parse(StringReader reader) throws CommandSyntaxException
	{
		int start2 = reader.getCursor();
		ResourceLocation gristName = ResourceLocation.read(reader);
		if(!LandTypes.TERRAIN_REGISTRY.containsKey(gristName))
		{
			reader.setCursor(start2);
			throw INVALID_TYPE.createWithContext(reader, gristName);
		}
		return LandTypes.TERRAIN_REGISTRY.getValue(gristName);
	}
	
	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder)
	{
		return ISuggestionProvider.suggestResource(LandTypes.TERRAIN_REGISTRY.getValues().stream().map(TerrainLandType::getRegistryName), builder);
	}
	
	@Override
	public Collection<String> getExamples()
	{
		return EXAMPLES;
	}
	
	public static TerrainLandType get(CommandContext<CommandSource> context, String id)
	{
		return context.getArgument(id, TerrainLandType.class);
	}
}
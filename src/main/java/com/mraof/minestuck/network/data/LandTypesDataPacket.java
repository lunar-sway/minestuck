package com.mraof.minestuck.network.data;

import com.google.common.collect.ImmutableMap;
import com.mraof.minestuck.client.ClientDimensionData;
import com.mraof.minestuck.network.PlayToClientPacket;
import com.mraof.minestuck.world.lands.LandTypePair;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.Map;

public class LandTypesDataPacket implements PlayToClientPacket
{
	private final Map<ResourceKey<Level>, LandTypePair> types;
	
	public LandTypesDataPacket(Map<ResourceKey<Level>, LandTypePair> types)
	{
		this.types = types;
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		for (Map.Entry<ResourceKey<Level>, LandTypePair> entry : types.entrySet())
		{
			buffer.writeResourceLocation(entry.getKey().location());
			buffer.writeRegistryId(entry.getValue().getTerrain());
			buffer.writeRegistryId(entry.getValue().getTitle());
		}
	}
	
	public static LandTypesDataPacket decode(FriendlyByteBuf buffer)
	{
		ImmutableMap.Builder<ResourceKey<Level>, LandTypePair> builder = new ImmutableMap.Builder<>();
		
		while (buffer.isReadable())
		{
			ResourceKey<Level> world = ResourceKey.create(Registry.DIMENSION_REGISTRY, buffer.readResourceLocation());
			TerrainLandType terrain = buffer.readRegistryIdSafe(TerrainLandType.class);
			TitleLandType title = buffer.readRegistryIdSafe(TitleLandType.class);
			builder.put(world, new LandTypePair(terrain, title));
		}
		
		return new LandTypesDataPacket(builder.build());
	}
	
	@Override
	public void execute()
	{
		ClientDimensionData.receivePacket(this);
	}
	
	public Map<ResourceKey<Level>, LandTypePair> getTypes()
	{
		return types;
	}
}

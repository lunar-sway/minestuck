package com.mraof.minestuck.network.data;

import com.google.common.collect.ImmutableMap;
import com.mraof.minestuck.client.ClientDimensionData;
import com.mraof.minestuck.network.PlayToClientPacket;
import com.mraof.minestuck.world.lands.LandTypePair;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.Map;

public class LandTypesDataPacket implements PlayToClientPacket
{
	private final Map<RegistryKey<World>, LandTypePair> types;
	
	public LandTypesDataPacket(Map<RegistryKey<World>, LandTypePair> types)
	{
		this.types = types;
	}
	
	@Override
	public void encode(PacketBuffer buffer)
	{
		for (Map.Entry<RegistryKey<World>, LandTypePair> entry : types.entrySet())
		{
			buffer.writeResourceLocation(entry.getKey().location());
			buffer.writeRegistryId(entry.getValue().getTerrain());
			buffer.writeRegistryId(entry.getValue().getTitle());
		}
	}
	
	public static LandTypesDataPacket decode(PacketBuffer buffer)
	{
		ImmutableMap.Builder<RegistryKey<World>, LandTypePair> builder = new ImmutableMap.Builder<>();
		
		while (buffer.isReadable())
		{
			RegistryKey<World> world = RegistryKey.create(Registry.DIMENSION_REGISTRY, buffer.readResourceLocation());
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
	
	public Map<RegistryKey<World>, LandTypePair> getTypes()
	{
		return types;
	}
}

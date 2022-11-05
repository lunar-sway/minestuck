package com.mraof.minestuck.network;

import com.mraof.minestuck.block.EnumCassetteType;
import com.mraof.minestuck.client.sounds.PlayerMusicClientHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public class MusicPlayerPacket implements PlayToClientPacket
{
	private final int entityID;
	private EnumCassetteType cassetteType;
	
	public static MusicPlayerPacket createPacket(Player entity, EnumCassetteType cassetteType)
	{
		return new MusicPlayerPacket(entity.getId(), cassetteType);
	}
	
	public MusicPlayerPacket(int entityID, EnumCassetteType cassetteType)
	{
		this.entityID = entityID;
		this.cassetteType = cassetteType;
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeInt(entityID);
		buffer.writeInt(cassetteType.ordinal());
	}
	
	public static MusicPlayerPacket decode(FriendlyByteBuf buffer)
	{
		int entityID = buffer.readInt(); //readInt spits out the values you gave to the PacketBuffer in encode in that order
		EnumCassetteType cassetteType = EnumCassetteType.values()[buffer.readInt()];
		
		return new MusicPlayerPacket(entityID, cassetteType);
	}
	
	@Override
	public void execute()
	{
		PlayerMusicClientHandler.sendPacket(entityID, cassetteType);
	}
}

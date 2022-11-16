package com.mraof.minestuck.network;

import com.mraof.minestuck.block.EnumCassetteType;
import com.mraof.minestuck.client.sounds.PlayerMusicClientHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public class MusicPlayerPacket implements PlayToClientPacket
{
	private final int entityID;
	private final EnumCassetteType cassetteType;
	private final float volume;
	private final float pitch;
	
	/**
	 * Create a packet to the client to start a music based on the cassetteType given.
	 * If the cassetteType NONE is given, the previous music stop if there
	 * was any, and no music is played.
	 *
	 * @param entity     The player starting the music
	 * @param cassetteType The cassette that is going to be played.
	 * @see PlayerMusicClientHandler
	 */
	
	public static MusicPlayerPacket createPacket(Player entity, EnumCassetteType cassetteType, float volume, float pitch)
	{
		return new MusicPlayerPacket(entity.getId(), cassetteType, volume, pitch);
	}
	
	public MusicPlayerPacket(int entityID, EnumCassetteType cassetteType, float volume, float pitch)
	{
		this.entityID = entityID;
		this.cassetteType = cassetteType;
		this.volume = volume;
		this.pitch = pitch;
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeInt(entityID);
		buffer.writeFloat(volume);
		buffer.writeFloat(pitch);
		buffer.writeInt(cassetteType.ordinal());
	}
	
	public static MusicPlayerPacket decode(FriendlyByteBuf buffer)
	{
		int entityID = buffer.readInt(); //readInt spits out the values you gave to the PacketBuffer in encode in that order
		float volume = buffer.readFloat();
		float pitch = buffer.readFloat();
		EnumCassetteType cassetteType = EnumCassetteType.values()[buffer.readInt()];
		
		return new MusicPlayerPacket(entityID, cassetteType, volume, pitch);
	}
	
	@Override
	public void execute()
	{
		PlayerMusicClientHandler.sendPacket(entityID, cassetteType, volume, pitch);
	}
}

package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.EnumCassetteType;
import com.mraof.minestuck.client.sounds.PlayerMusicClientHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class MusicPlayerPacket implements MSPacket.PlayToClient
{
	public static final ResourceLocation ID = Minestuck.id("music_player");
	
	private final int entityID;
	private final EnumCassetteType cassetteType;
	private final float volume;
	private final float pitch;
	
	/**
	 * Creates a packet to the client for starting a cassette music track played by a specified player, and stopping the previous music track played by that player if there was any.
	 * If the cassetteType NONE is given, no new track is played.
	 *
	 * @param entity     The player starting the music.
	 * @param cassetteType The cassette that is going to be played.
	 * @param volume The volume of the music. Default is 4.0F.
	 * @param pitch The pitch of the music, how fast should it be. Default is 1.0F.
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
	public ResourceLocation id()
	{
		return ID;
	}
	
	@Override
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeInt(entityID);
		buffer.writeFloat(volume);
		buffer.writeFloat(pitch);
		buffer.writeInt(cassetteType.ordinal());
	}
	
	public static MusicPlayerPacket read(FriendlyByteBuf buffer)
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
		PlayerMusicClientHandler.handlePacket(entityID, cassetteType, volume, pitch);
	}
}

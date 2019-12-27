package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class MSPacketHandler
{
	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(Minestuck.MOD_ID, "main"),
			() -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
	
	public static void setupChannel()
	{
		INSTANCE.registerMessage(0, ModConfigPacket.class, ModConfigPacket::encode, ModConfigPacket::decode, PlayToClientPacket::consume);
		INSTANCE.registerMessage(1, PlayerDataPacket.class, PlayerDataPacket::encode, PlayerDataPacket::decode, PlayToClientPacket::consume);
		INSTANCE.registerMessage(2, CaptchaDeckPacket.class, CaptchaDeckPacket::encode, CaptchaDeckPacket::decode, PlayToBothPacket::consume);
		INSTANCE.registerMessage(3, GristCachePacket.class, GristCachePacket::encode, GristCachePacket::decode, PlayToClientPacket::consume);
		INSTANCE.registerMessage(4, ColorSelectPacket.class, ColorSelectPacket::encode, ColorSelectPacket::decode, PlayToServerPacket::consume);
		INSTANCE.registerMessage(5, TitleSelectPacket.class, TitleSelectPacket::encode, TitleSelectPacket::decode, PlayToBothPacket::consume);
		INSTANCE.registerMessage(6, SburbConnectPacket.class, SburbConnectPacket::encode, SburbConnectPacket::decode, PlayToServerPacket::consume);
		INSTANCE.registerMessage(7, SburbConnectClosedPacket.class, SburbConnectClosedPacket::encode, SburbConnectClosedPacket::decode, PlayToServerPacket::consume);
		INSTANCE.registerMessage(8, ClearMessagePacket.class, ClearMessagePacket::encode, ClearMessagePacket::decode, PlayToServerPacket::consume);
		INSTANCE.registerMessage(9, SkaianetInfoPacket.class, SkaianetInfoPacket::encode, SkaianetInfoPacket::decode, PlayToBothPacket::consume);
		INSTANCE.registerMessage(10, DataCheckerPacket.class, DataCheckerPacket::encode, DataCheckerPacket::decode, PlayToBothPacket::consume);
		INSTANCE.registerMessage(11, ClientEditPacket.class, ClientEditPacket::encode, ClientEditPacket::decode, PlayToServerPacket::consume);
		INSTANCE.registerMessage(12, ServerEditPacket.class, ServerEditPacket::encode, ServerEditPacket::decode, PlayToClientPacket::consume);
		INSTANCE.registerMessage(13, MiscContainerPacket.class, MiscContainerPacket::encode, MiscContainerPacket::decode, PlayToServerPacket::consume);
		INSTANCE.registerMessage(14, EditmodeInventoryPacket.class, EditmodeInventoryPacket::encode, EditmodeInventoryPacket::decode, PlayToBothPacket::consume);
		INSTANCE.registerMessage(15, GoButtonPacket.class, GoButtonPacket::encode, GoButtonPacket::decode, PlayToServerPacket::consume);
		INSTANCE.registerMessage(16, AlchemiterPacket.class, AlchemiterPacket::encode, AlchemiterPacket::decode, PlayToServerPacket::consume);
		INSTANCE.registerMessage(17, GristWildcardPacket.class, GristWildcardPacket::encode, GristWildcardPacket::decode, PlayToServerPacket::consume);
		INSTANCE.registerMessage(18, TransportalizerPacket.class, TransportalizerPacket::encode, TransportalizerPacket::decode, PlayToServerPacket::consume);
		INSTANCE.registerMessage(19, EffectTogglePacket.class, EffectTogglePacket::encode, EffectTogglePacket::decode, PlayToServerPacket::consume);
	}
	
	public static <MSG> void sendToPlayer(MSG message, ServerPlayerEntity player)
	{
		INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
	}
	
	public static <MSG> void sendToAll(MSG message)
	{
		INSTANCE.send(PacketDistributor.ALL.noArg(), message);
	}
	
	public static <MSG> void sendToServer(MSG message)
	{
		INSTANCE.sendToServer(message);
	}
}
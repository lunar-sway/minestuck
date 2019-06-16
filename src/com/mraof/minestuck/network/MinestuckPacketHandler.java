package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

/**
 *
 */
public class MinestuckPacketHandler
{
	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(Minestuck.MOD_ID, "main"),
			() -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
	
	public static void setupChannel()
	{
		INSTANCE.registerMessage(0, ModConfigPacket.class, ModConfigPacket::encode, ModConfigPacket::decode, ModConfigPacket::consume);
		INSTANCE.registerMessage(1, PlayerDataPacket.class, PlayerDataPacket::encode, PlayerDataPacket::decode, PlayerDataPacket::consume);
		INSTANCE.registerMessage(2, CaptchaDeckPacket.class, CaptchaDeckPacket::encode, CaptchaDeckPacket::decode, CaptchaDeckPacket::consume);
		INSTANCE.registerMessage(3, GristCachePacket.class, GristCachePacket::encode, GristCachePacket::decode, GristCachePacket::consume);
		INSTANCE.registerMessage(4, ColorSelectPacket.class, ColorSelectPacket::encode, ColorSelectPacket::decode, ColorSelectPacket::consume);
		INSTANCE.registerMessage(5, TitleSelectPacket.class, TitleSelectPacket::encode, TitleSelectPacket::decode, TitleSelectPacket::consume);
		INSTANCE.registerMessage(6, SburbConnectPacket.class, SburbConnectPacket::encode, SburbConnectPacket::decode, SburbConnectPacket::consume);
		INSTANCE.registerMessage(7, SburbConnectClosedPacket.class, SburbConnectClosedPacket::encode, SburbConnectClosedPacket::decode, SburbConnectClosedPacket::consume);
		INSTANCE.registerMessage(8, ClearMessagePacket.class, ClearMessagePacket::encode, ClearMessagePacket::decode, ClearMessagePacket::consume);
		INSTANCE.registerMessage(9, SkaianetInfoPacket.class, SkaianetInfoPacket::encode, SkaianetInfoPacket::decode, SkaianetInfoPacket::consume);
		INSTANCE.registerMessage(10, DataCheckerPacket.class, DataCheckerPacket::encode, DataCheckerPacket::decode, DataCheckerPacket::consume);
		INSTANCE.registerMessage(11, ClientEditPacket.class, ClientEditPacket::encode, ClientEditPacket::decode, ClientEditPacket::consume);
		INSTANCE.registerMessage(12, ServerEditPacket.class, ServerEditPacket::encode, ServerEditPacket::decode, ServerEditPacket::consume);
		INSTANCE.registerMessage(13, MiscContainerPacket.class, MiscContainerPacket::encode, MiscContainerPacket::decode, MiscContainerPacket::consume);
		INSTANCE.registerMessage(14, EditmodeInventoryPacket.class, EditmodeInventoryPacket::encode, EditmodeInventoryPacket::decode, EditmodeInventoryPacket::consume);
		INSTANCE.registerMessage(15, GoButtonPacket.class, GoButtonPacket::encode, GoButtonPacket::decode, GoButtonPacket::consume);
		INSTANCE.registerMessage(16, AlchemiterPacket.class, AlchemiterPacket::encode, AlchemiterPacket::decode, AlchemiterPacket::consume);
		INSTANCE.registerMessage(17, GristWildcardPacket.class, GristWildcardPacket::encode, GristWildcardPacket::decode, GristWildcardPacket::consume);
		INSTANCE.registerMessage(18, TransportalizerPacket.class, TransportalizerPacket::encode, TransportalizerPacket::decode, TransportalizerPacket::consume);
		INSTANCE.registerMessage(19, EffectTogglePacket.class, EffectTogglePacket::encode, EffectTogglePacket::decode, EffectTogglePacket::consume);
	}
}
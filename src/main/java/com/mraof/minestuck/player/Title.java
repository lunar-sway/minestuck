package com.mraof.minestuck.player;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.TitleDataPacket;
import com.mraof.minestuck.util.MSAttachments;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

@EventBusSubscriber(modid = Minestuck.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public record Title(EnumClass heroClass, EnumAspect heroAspect)
{
	public static final StreamCodec<FriendlyByteBuf, Title> STREAM_CODEC = StreamCodec.composite(
			NeoForgeStreamCodecs.enumCodec(EnumClass.class),
			Title::heroClass,
			NeoForgeStreamCodecs.enumCodec(EnumAspect.class),
			Title::heroAspect,
			Title::new
	);
	
	public static final String FORMAT = "title.format";
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	//todo valueOf() can throw an exception if the name doesn't match
	public static final Codec<EnumClass> CLASS_CODEC = Codec.STRING.xmap(EnumClass::valueOf, EnumClass::name);
	public static final Codec<EnumAspect> ASPECT_CODEC = Codec.STRING.xmap(EnumAspect::valueOf, EnumAspect::name);
	
	public static final Codec<Title> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			CLASS_CODEC.fieldOf("class").forGetter(Title::heroClass),
			ASPECT_CODEC.fieldOf("aspect").forGetter(Title::heroAspect)
	).apply(instance, Title::new));
	
	@SubscribeEvent
	private static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
	{
		ServerPlayer player = (ServerPlayer) event.getEntity();
		
		getTitle(player).ifPresent(title ->
				player.connection.send(TitleDataPacket.create(title)));
	}
	
	public static boolean isPlayerOfAspect(ServerPlayer player, EnumAspect aspect)
	{
		return getTitle(player)
				.map(title -> title.heroAspect() == aspect)
				.orElse(false);
	}
	
	public static Optional<Title> getTitle(ServerPlayer player)
	{
		return PlayerData.get(player).flatMap(Title::getTitle);
	}
	
	public static Optional<Title> getTitle(PlayerIdentifier playerId, MinecraftServer mcServer)
	{
		return getTitle(PlayerData.get(playerId, mcServer));
	}
	
	public static Optional<Title> getTitle(PlayerData playerData)
	{
		return playerData.getExistingData(MSAttachments.TITLE);
	}
	
	public static void setTitle(PlayerData playerData, Title newTitle)
	{
		if(getTitle(playerData).isPresent())
			throw new IllegalStateException("Can't set title for player " + playerData.identifier.getUsername() + " because they already have one");
		
		playerData.setData(MSAttachments.TITLE, newTitle);
		
		ServerPlayer player = playerData.getPlayer();
		if(player != null)
			player.connection.send(TitleDataPacket.create(newTitle));
	}
	
	@Override
	public String toString()
	{
		return heroClass.toString() + " of " + heroAspect.toString();
	}
	
	public Component asTextComponent()
	{
		return Component.translatable(FORMAT, heroClass.asTextComponent(), heroAspect.asTextComponent());
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof Title title)
		{
			return title.heroClass.equals(this.heroClass) && title.heroAspect.equals(this.heroAspect);
		}
		return false;
	}
	
	private static String makeNBTPrefix(String prefix)
	{
		return prefix != null && !prefix.isEmpty() ? prefix + "_" : "";
	}
	
	public static Title read(CompoundTag nbt, String keyPrefix)
	{
		keyPrefix = makeNBTPrefix(keyPrefix);
		EnumClass c = EnumClass.getClassFromInt(nbt.getByte(keyPrefix + "class"));
		EnumAspect a = EnumAspect.getAspectFromInt(nbt.getByte(keyPrefix + "aspect"));
		return new Title(c, a);
	}
	
	public static Title tryRead(CompoundTag nbt, String keyPrefix)
	{
		keyPrefix = makeNBTPrefix(keyPrefix);
		if(nbt.contains(keyPrefix + "class", Tag.TAG_ANY_NUMERIC))
		{
			EnumClass c = EnumClass.getClassFromInt(nbt.getByte(keyPrefix + "class"));
			EnumAspect a = EnumAspect.getAspectFromInt(nbt.getByte(keyPrefix + "aspect"));
			if(c != null && a != null)
				return new Title(c, a);
		}
		return null;
	}
	
	public CompoundTag write(CompoundTag nbt, String keyPrefix)
	{
		keyPrefix = makeNBTPrefix(keyPrefix);
		nbt.putByte(keyPrefix + "class", (byte) EnumClass.getIntFromClass(heroClass));
		nbt.putByte(keyPrefix + "aspect", (byte) EnumAspect.getIntFromAspect(heroAspect));
		return nbt;
	}
}

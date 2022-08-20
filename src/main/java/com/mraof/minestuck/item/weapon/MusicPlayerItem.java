package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.block.EnumCassetteType;
import com.mraof.minestuck.client.sounds.MusicPlayerOnPlayerSoundInstance;
import com.mraof.minestuck.inventory.musicplayer.*;
import com.mraof.minestuck.item.CassetteItem;
import com.mraof.minestuck.util.Debug;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class MusicPlayerItem extends WeaponItem
{
	private static IItemHandler getItemStackHandlerMusicPlayer(ItemStack itemStack)
	{
		return itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElseThrow(() ->
				new IllegalArgumentException("ItemStack " + itemStack + " is not an item handler"));
	}
	
	private static IMusicPlaying getMusicPlaying(ItemStack itemStack)
	{
		return itemStack.getCapability(CapabilityMusicPlaying.MUSIC_PLAYING_CAPABILITY).orElseThrow(() ->
				new IllegalArgumentException("ItemStack " + itemStack + " is not a music playing"));
	}
	
	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt)
	{
		return new CapabilityProviderMusicPlayer();
	}
	
	public MusicPlayerItem(Builder builder, Properties properties)
	{
		super(builder, properties);
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn)
	{
		IMusicPlaying musicPlaying = getMusicPlaying(playerIn.getItemInHand(handIn));
		IItemHandler itemStackHandlerMusicPlayer = getItemStackHandlerMusicPlayer(playerIn.getItemInHand(handIn));
		
		if(playerIn.isCrouching())
		{
			SoundManager soundManager = Minecraft.getInstance().getSoundManager();
			MusicPlayerOnPlayerSoundInstance soundInstance;
			Item item = itemStackHandlerMusicPlayer.getStackInSlot(0).getItem();
			if(item instanceof CassetteItem cassette)
			{
				if(musicPlaying.getCurrentMusic() == null)
				{
					musicPlaying.setCurrentMusic(EnumCassetteType.getSoundEvent(cassette.cassetteID));
					musicEffect(cassette.cassetteID, playerIn);
					soundInstance = new MusicPlayerOnPlayerSoundInstance(playerIn, musicPlaying);
					soundManager.play(soundInstance);
					Debug.logger.info("Playing music: " + cassette.cassetteID);
				} else
				{
					musicEffect(EnumCassetteType.NONE, playerIn);
					musicPlaying.setCurrentMusic(null);
					Debug.logger.info("Stopped music");
				}
			}
		}
		//open the GUI if right clicked
		else if(!level.isClientSide)
		{
			NetworkHooks.openGui((ServerPlayer) playerIn, new SimpleMenuProvider((pContainerId, pInventory, pPlayer) ->
					new MusicPlayerContainer(pContainerId, pInventory, itemStackHandlerMusicPlayer),
					new TextComponent("Music Player")));
		}
		return super.use(level, playerIn, handIn);
	}
	
	public void musicEffect(EnumCassetteType cassetteType, Player player)
	{
		switch(cassetteType)
		{
			case FAR -> player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 10, 1));
		}
	}
}

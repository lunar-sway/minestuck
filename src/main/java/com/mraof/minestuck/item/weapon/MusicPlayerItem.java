package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.block.EnumCassetteType;
import com.mraof.minestuck.client.sounds.MusicPlayerOnPlayerSoundInstance;
import com.mraof.minestuck.inventory.musicplayer.CapabilityMusicPlaying;
import com.mraof.minestuck.inventory.musicplayer.MusicPlayerCapabilityProvider;
import com.mraof.minestuck.inventory.musicplayer.IMusicPlaying;
import com.mraof.minestuck.inventory.musicplayer.MusicPlayerContainer;
import com.mraof.minestuck.item.CassetteItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class MusicPlayerItem extends WeaponItem
{
	private MobEffect effect = null;
	private float applyingChance = 0.0F;
	int duration = 0;
	private static IItemHandler getItemStackHandlerMusicPlayer(ItemStack itemStack)
	{
		return itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElseThrow(() ->
				new IllegalArgumentException("Expected an item handler for the music player item, but " + itemStack + " does not expose an item handler."));
	}
	
	private static IMusicPlaying getMusicPlaying(ItemStack itemStack)
	{
		return itemStack.getCapability(CapabilityMusicPlaying.MUSIC_PLAYING_CAPABILITY).orElseThrow(() ->
				new IllegalArgumentException("Expected an music playing for the music player item, but " + itemStack + " does not expose a music playing."));
	}
	
	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt)
	{
		return new MusicPlayerCapabilityProvider();
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
					musicPlaying.setCurrentMusic(cassette.cassetteID.getSoundEvent());
					musicEffectToPlayer(cassette.cassetteID, playerIn);
					soundInstance = new MusicPlayerOnPlayerSoundInstance(playerIn, musicPlaying);
					soundManager.play(soundInstance);
				} else
				{
					musicEffectToPlayer(EnumCassetteType.NONE, playerIn);
					musicPlaying.setCurrentMusic(null);
				}
			}
		}
		//open the GUI if right-clicked
		else if(!level.isClientSide)
		{
			NetworkHooks.openGui((ServerPlayer) playerIn, new SimpleMenuProvider((pContainerId, pInventory, pPlayer) ->
					new MusicPlayerContainer(pContainerId, pInventory, itemStackHandlerMusicPlayer),
					new TextComponent("Music Player")));
		}
		return super.use(level, playerIn, handIn);
	}
	
	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker)
	{
		Random r = attacker.level.getRandom();
		double chanceToHit = r.nextFloat();
		AttributeInstance attackerLuck = attacker.getAttribute(Attributes.LUCK);
		AttributeInstance targetLuck = target.getAttribute(Attributes.LUCK);
		double attackerLuckValue;
		double targetLuckValue;
		if(attackerLuck != null)
			attackerLuckValue = attackerLuck.getValue();
		else
			attackerLuckValue = 0.0D;
		
		if(targetLuck != null)
			targetLuckValue = targetLuck.getValue();
		else
			targetLuckValue = 0.0D;
		
		chanceToHit = chanceToHit - (attackerLuckValue / 10) + (targetLuckValue / 10);
		System.out.println("chanceToHit: " + chanceToHit + " applyingChance: " + applyingChance);
		System.out.println("attackerLuckValue: " + attackerLuckValue + " targetLuckValue: " + targetLuckValue);
		if(effect != null && applyingChance > chanceToHit)
		{
			System.out.println("Applying effect" + effect);
			target.addEffect(new MobEffectInstance(effect, duration, 1));
		}
		return super.hurtEnemy(stack, target, attacker);
	}
	
	public void musicEffectToPlayer(EnumCassetteType cassetteType, Player player)
	{
		switch(cassetteType)
		{
			case FAR ->
			{
				effect = MobEffects.LEVITATION;
				duration = 10;
				applyingChance = 0.25F;
			}
			case NONE -> effect = null;
		}
	}
}

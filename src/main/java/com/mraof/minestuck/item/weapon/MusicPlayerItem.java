package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.EnumCassetteType;
import com.mraof.minestuck.inventory.musicplayer.CapabilityMusicPlaying;
import com.mraof.minestuck.inventory.musicplayer.CassetteContainerMenu;
import com.mraof.minestuck.inventory.musicplayer.IMusicPlaying;
import com.mraof.minestuck.inventory.musicplayer.MusicPlayerItemCapProvider;
import com.mraof.minestuck.item.CassetteItem;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.MusicPlayerPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID)
public class MusicPlayerItem extends WeaponItem
{
	private static IItemHandler getItemStackHandlerMusicPlayer(ItemStack itemStack)
	{
		return itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElseThrow(() ->
				new IllegalArgumentException("Expected an item handler for the music player item, but " + itemStack + " does not expose an item handler."));
	}
	
	private static IMusicPlaying getMusicPlaying(LivingEntity entity)
	{
		return entity.getCapability(CapabilityMusicPlaying.MUSIC_PLAYING_CAPABILITY).orElseThrow(() ->
				new IllegalArgumentException("Expected an music playing for this entity, but " + entity + " does not expose a music playing."));
	}
	
	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt)
	{
		return new MusicPlayerItemCapProvider();
	}
	
	public MusicPlayerItem(Builder builder, Properties properties)
	{
		super(builder, properties);
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn)
	{
		ItemStack musicPlayer = playerIn.getItemInHand(handIn);
		
		IItemHandler itemStackHandlerMusicPlayer = getItemStackHandlerMusicPlayer(musicPlayer);
		IMusicPlaying musicPlayingCap = getMusicPlaying(playerIn);
		
		if(!level.isClientSide)
		{
			if(playerIn.isCrouching())
			{
				Item itemInMusicPlayer = itemStackHandlerMusicPlayer.getStackInSlot(0).getItem();
				if(itemInMusicPlayer instanceof CassetteItem cassette)
				{
					MusicPlayerPacket packet;
					if(musicPlayingCap.getCassetteType() == EnumCassetteType.NONE)
					{
						packet = MusicPlayerPacket.createPacket(playerIn, cassette.cassetteID);
						musicPlayingCap.setMusicPlaying(musicPlayer, cassette.cassetteID);
					} else
					{
						packet = MusicPlayerPacket.createPacket(playerIn, EnumCassetteType.NONE);
						musicPlayingCap.setMusicPlaying(ItemStack.EMPTY, EnumCassetteType.NONE);
					}
					MSPacketHandler.sendToTrackingAndSelf(packet, playerIn);
				}
			}
			//open the GUI if right-clicked
			else
			{
				MusicPlayerPacket packet = MusicPlayerPacket.createPacket(playerIn, EnumCassetteType.NONE);
				musicPlayingCap.setMusicPlaying(ItemStack.EMPTY, EnumCassetteType.NONE);
				MSPacketHandler.sendToTrackingAndSelf(packet, playerIn); //This will stop the music before opening the GUI
				
				NetworkHooks.openGui((ServerPlayer) playerIn, new SimpleMenuProvider((pContainerId, pInventory, pPlayer) ->
						new CassetteContainerMenu(pContainerId, pInventory, itemStackHandlerMusicPlayer, musicPlayer),
						new TextComponent("Music Player")));
			}
		}
		return super.use(level, playerIn, handIn);
	}
	
	@SubscribeEvent
	public static void playerTick(TickEvent.PlayerTickEvent tickEvent)
	{
		if(tickEvent.side.isServer() && tickEvent.phase == TickEvent.Phase.END)
		{
			Player player = tickEvent.player;
			IMusicPlaying musicPlayingCap = getMusicPlaying(player);
			
			if(!(player.getItemInHand(InteractionHand.MAIN_HAND) == musicPlayingCap.getCurrentMusicPlayer() ||
					player.getItemInHand(InteractionHand.OFF_HAND) == musicPlayingCap.getCurrentMusicPlayer()) ||
					musicPlayingCap.getCurrentMusicPlayer().isEmpty())
			{
				MusicPlayerPacket packet = MusicPlayerPacket.createPacket(player, EnumCassetteType.NONE);
				musicPlayingCap.setMusicPlaying(ItemStack.EMPTY, EnumCassetteType.NONE);
				MSPacketHandler.sendToTrackingAndSelf(packet, player);
			}
			
			if(player.level.getGameTime() % 50 == 0 && musicPlayingCap.getCassetteType() != EnumCassetteType.NONE)
			{
				EnumCassetteType.EffectContainer effectContainer = musicPlayingCap.getCassetteType().getEffectContainer();
				if(!effectContainer.onHit())
					player.addEffect(effectContainer.effect().get());
			}
		}
	}
	
	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker)
	{
		IMusicPlaying musicPlaying = getMusicPlaying(attacker);
		if(musicPlaying.getCassetteType() != EnumCassetteType.NONE)
		{
			Random r = attacker.level.getRandom();
			
			double attackerLuckValue;
			double targetLuckValue;
			
			EnumCassetteType.EffectContainer effectContainer = musicPlaying.getCassetteType().getEffectContainer();
			AttributeInstance attackerLuck = attacker.getAttribute(Attributes.LUCK);
			AttributeInstance targetLuck = target.getAttribute(Attributes.LUCK);
			double chanceToHit = effectContainer.applyingChance();
			
			if(attackerLuck != null)
				attackerLuckValue = attackerLuck.getValue();
			else
				attackerLuckValue = 0.0D;
			
			if(targetLuck != null)
				targetLuckValue = targetLuck.getValue();
			else
				targetLuckValue = 0.0D;
			
			chanceToHit = chanceToHit + (attackerLuckValue / 10) - (targetLuckValue / 10);
			
			if(chanceToHit > r.nextFloat() && effectContainer.onHit())
			{
				target.addEffect(effectContainer.effect().get());
			}
		}
		return super.hurtEnemy(stack, target, attacker);
	}
}

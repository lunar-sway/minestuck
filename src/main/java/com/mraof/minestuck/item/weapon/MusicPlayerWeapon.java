package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.inventory.musicplayer.CassetteContainerMenu;
import com.mraof.minestuck.inventory.musicplayer.CassetteSong;
import com.mraof.minestuck.inventory.musicplayer.CassetteSongs;
import com.mraof.minestuck.inventory.musicplayer.MusicPlaying;
import com.mraof.minestuck.item.components.MSItemComponents;
import com.mraof.minestuck.network.MusicPlayerPacket;
import com.mraof.minestuck.util.MSAttachments;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * This Weapon allow the user to open a GUI that can contain one cassette with right-click, and can play the
 * music associated to it with shift right-click. Doing it again while a music is playing will
 * stop the music.
 * </p>
 * {@link #use(Level, Player, InteractionHand)}
 * <p>
 * Depending on the music that is currently playing, the player will receive positive effect,
 * or will apply negative effect on hit.
 * </p>
 * {@link #playerTick(PlayerTickEvent.Post)}
 * {@link #hurtEnemy(ItemStack, LivingEntity, LivingEntity)}
 * <p>
 * The sprite of the item can change depending of if a cassette is currently inside or not.
 * The tag used to do so is HasCassette.
 * </p>
 * {@link #hasCassette(ItemStack)}
 */

@EventBusSubscriber(modid = Minestuck.MOD_ID)
public class MusicPlayerWeapon extends WeaponItem
{
	public static final String TITLE = "minestuck.music_player";
	public static final String HINT_INACTIVE = "minestuck.music_player.hint_inactive";
	private final float volume;
	private final float pitch;
	
	public MusicPlayerWeapon(Builder builder, Properties properties, float volume, float pitch)
	{
		super(builder, properties.component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
		this.volume = volume;
		this.pitch = pitch;
		NeoForge.EVENT_BUS.addListener(this::adjustDamage);
		
	}
	
	public MusicPlayerWeapon(Builder builder, Properties properties)
	{
		super(builder, properties);
		this.volume = 4.0F;
		this.pitch = 1.0F;
		NeoForge.EVENT_BUS.addListener(this::adjustDamage);
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn)
	{
		ItemStack musicPlayer = playerIn.getItemInHand(handIn);
		
		ItemStack itemInMusicPlayer = musicPlayer.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyOne();
		MusicPlaying musicPlayingCap = playerIn.getData(MSAttachments.MUSIC_PLAYING);
		
		if(playerIn instanceof ServerPlayer serverPlayer)
		{
			if(playerIn.isCrouching())
			{
				if(itemInMusicPlayer.has(MSItemComponents.CASSETTE_SONG))
				{
					Optional<CassetteSong> osong = CassetteSongs.getInstance().findSong(itemInMusicPlayer);
					if(osong.isPresent())
					{
						CassetteSong song = osong.get();
						MusicPlayerPacket packet;
						if(musicPlayingCap.getCassetteSong() == null)
						{
							packet = MusicPlayerPacket.createPacket(playerIn, song, volume, pitch);
							musicPlayingCap.setMusicPlaying(musicPlayer, song);
						} else
						{
							packet = MusicPlayerPacket.createPacket(playerIn, null, 0, 0);
							musicPlayingCap.setMusicPlaying(ItemStack.EMPTY, null);
						}
						PacketDistributor.sendToPlayersTrackingEntityAndSelf(playerIn, packet);
					}
				}
			}
			//open the GUI if right-clicked
			else
			{
				MusicPlayerPacket packet = MusicPlayerPacket.createPacket(playerIn, null, 0, 0);
				musicPlayingCap.setMusicPlaying(ItemStack.EMPTY, null);
				//This will stop the music before opening the GUI
				PacketDistributor.sendToPlayersTrackingEntityAndSelf(playerIn, packet);
				serverPlayer.openMenu(new SimpleMenuProvider((pContainerId, pInventory, pPlayer) ->
						new CassetteContainerMenu(pContainerId, pInventory, musicPlayer, itemInMusicPlayer),
						Component.translatable(TITLE)));
			}
		}
		return InteractionResultHolder.sidedSuccess(musicPlayer, level.isClientSide);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag)
	{
		ItemStack itemInMusicPlayer = stack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyOne();
		if(itemInMusicPlayer.has(MSItemComponents.CASSETTE_SONG))
		{
			tooltipComponents.add(Component.translatable(HINT_INACTIVE).withStyle(ChatFormatting.GRAY));
		}
	}
	
	@SubscribeEvent
	public static void playerTick(PlayerTickEvent.Post tickEvent)
	{
		if(!tickEvent.getEntity().level().isClientSide() && tickEvent.getEntity().isAlive())
		{
			Player player = tickEvent.getEntity();
			MusicPlaying musicPlayingCap = player.getData(MSAttachments.MUSIC_PLAYING);
			
			if(!(player.getItemInHand(InteractionHand.MAIN_HAND) == musicPlayingCap.getCurrentMusicPlayer() ||
					player.getItemInHand(InteractionHand.OFF_HAND) == musicPlayingCap.getCurrentMusicPlayer()) ||
					musicPlayingCap.getCurrentMusicPlayer().isEmpty())
			{
				if(musicPlayingCap.getCassetteSong() != null)
				{ //If the Cassette player isn't in hand and is playing music, stop it
					MusicPlayerPacket packet = MusicPlayerPacket.createPacket(player, null, 0, 0);
					musicPlayingCap.setMusicPlaying(ItemStack.EMPTY, null);
					PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, packet);
				}
			}
			
			if(player.level().getGameTime() % 50 == 0 && musicPlayingCap.getCassetteSong() != null)
			{
				CassetteSong.EffectContainer effectContainer = musicPlayingCap.getCassetteSong().getEffectContainer();
				if(!effectContainer.onHit()) //Apply the cassette buff every 50 ticks, if there is any
					player.addEffect(effectContainer.effect());
			}
		}
	}
	
	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker)
	{
		MusicPlaying musicPlaying = attacker.getData(MSAttachments.MUSIC_PLAYING);
		if(musicPlaying.getCassetteSong() != null && musicPlaying.getCurrentMusicPlayer() == stack)
		{
			CassetteSong.EffectContainer effectContainer = musicPlaying.getCassetteSong().getEffectContainer();
			if(effectContainer.onHit())
			{
				RandomSource r = attacker.level().getRandom();
				
				double attackerLuckValue = 0;
				double targetLuckValue = 0;
				
				if(attacker.getAttributes().hasAttribute(Attributes.LUCK))
					attackerLuckValue = attacker.getAttributeValue(Attributes.LUCK);
				if(target.getAttributes().hasAttribute(Attributes.LUCK))
					targetLuckValue = target.getAttributeValue(Attributes.LUCK);
				
				double chanceToHit = effectContainer.applyingChance();
				
				//Compare the luck of the target with the user's
				chanceToHit = chanceToHit + (attackerLuckValue / 10) - (targetLuckValue / 10);
				
				if(chanceToHit > r.nextFloat())
				{
					target.addEffect(effectContainer.effect());
				}
			}
		}
		return super.hurtEnemy(stack, target, attacker);
	}
	
	public static boolean hasCassette(ItemStack stack)
	{
		return !stack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyOne().isEmpty();
	}
	
	private void adjustDamage(ItemAttributeModifierEvent event)
	{
		ItemStack stack = event.getItemStack();
		if(!(stack.getItem() instanceof MusicPlayerWeapon)) return;
		
		if(!hasCassette(stack))
		{
			event.removeAllModifiersFor(Attributes.ATTACK_DAMAGE);
		}
	}
}

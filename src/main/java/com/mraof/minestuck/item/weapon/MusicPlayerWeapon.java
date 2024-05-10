package com.mraof.minestuck.item.weapon;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.EnumCassetteType;
import com.mraof.minestuck.inventory.musicplayer.CassetteContainerMenu;
import com.mraof.minestuck.inventory.musicplayer.IMusicPlaying;
import com.mraof.minestuck.item.CassetteItem;
import com.mraof.minestuck.network.MusicPlayerPacket;
import com.mraof.minestuck.util.MSAttachments;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.network.PacketDistributor;

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
 * {@link #playerTick(TickEvent.PlayerTickEvent)}
 * {@link #hurtEnemy(ItemStack, LivingEntity, LivingEntity)}
 * <p>
 * The sprite of the item can change depending of if a cassette is currently inside or not.
 * The tag used to do so is HasCassette.
 * </p>
 * {@link #hasCassette(ItemStack)}
 */

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID)
public class MusicPlayerWeapon extends WeaponItem
{
	public static final String TITLE = "minestuck.music_player";
	private final float volume;
	private final float pitch;
	
	public MusicPlayerWeapon(Builder builder, Properties properties, float volume, float pitch)
	{
		super(builder, properties);
		this.volume = volume;
		this.pitch = pitch;
		
	}
	
	public MusicPlayerWeapon(Builder builder, Properties properties)
	{
		super(builder, properties);
		this.volume = 4.0F;
		this.pitch = 1.0F;
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn)
	{
		ItemStack musicPlayer = playerIn.getItemInHand(handIn);
		
		IItemHandler itemStackHandlerMusicPlayer = musicPlayer.getData(MSAttachments.MUSIC_PLAYER_INVENTORY);
		IMusicPlaying musicPlayingCap = playerIn.getData(MSAttachments.MUSIC_PLAYING);
		
		if(playerIn instanceof ServerPlayer serverPlayer)
		{
			if(playerIn.isCrouching())
			{
				Item itemInMusicPlayer = itemStackHandlerMusicPlayer.getStackInSlot(0).getItem();
				if(itemInMusicPlayer instanceof CassetteItem cassette)
				{
					MusicPlayerPacket packet;
					if(musicPlayingCap.getCassetteType() == EnumCassetteType.NONE)
					{
						packet = MusicPlayerPacket.createPacket(playerIn, cassette.cassetteID, volume, pitch);
						musicPlayingCap.setMusicPlaying(musicPlayer, cassette.cassetteID);
					} else
					{
						packet = MusicPlayerPacket.createPacket(playerIn, EnumCassetteType.NONE, 0, 0);
						musicPlayingCap.setMusicPlaying(ItemStack.EMPTY, EnumCassetteType.NONE);
					}
					PacketDistributor.TRACKING_ENTITY_AND_SELF.with(playerIn).send(packet);
				}
			}
			//open the GUI if right-clicked
			else
			{
				MusicPlayerPacket packet = MusicPlayerPacket.createPacket(playerIn, EnumCassetteType.NONE, 0, 0);
				musicPlayingCap.setMusicPlaying(ItemStack.EMPTY, EnumCassetteType.NONE);
				//This will stop the music before opening the GUI
				PacketDistributor.TRACKING_ENTITY_AND_SELF.with(playerIn).send(packet);
				
				serverPlayer.openMenu(new SimpleMenuProvider((pContainerId, pInventory, pPlayer) ->
						new CassetteContainerMenu(pContainerId, pInventory, itemStackHandlerMusicPlayer, musicPlayer),
						Component.translatable(TITLE)));
			}
		}
		return InteractionResultHolder.sidedSuccess(musicPlayer, level.isClientSide);
	}
	
	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack)
	{
		if(hasCassette(stack))
			return super.getAttributeModifiers(slot, stack);
		else
			return ImmutableMultimap.of();
	}
	
	@SubscribeEvent
	public static void playerTick(TickEvent.PlayerTickEvent tickEvent)
	{
		if(tickEvent.side.isServer() && tickEvent.phase == TickEvent.Phase.END && tickEvent.player.isAlive())
		{
			Player player = tickEvent.player;
			IMusicPlaying musicPlayingCap = player.getData(MSAttachments.MUSIC_PLAYING);
			
			if(!(player.getItemInHand(InteractionHand.MAIN_HAND) == musicPlayingCap.getCurrentMusicPlayer() ||
					player.getItemInHand(InteractionHand.OFF_HAND) == musicPlayingCap.getCurrentMusicPlayer()) ||
					musicPlayingCap.getCurrentMusicPlayer().isEmpty())
			{
				if(musicPlayingCap.getCassetteType() != EnumCassetteType.NONE)
				{ //If the Cassette player isn't in hand and is playing music, stop it
					MusicPlayerPacket packet = MusicPlayerPacket.createPacket(player, EnumCassetteType.NONE, 0, 0);
					musicPlayingCap.setMusicPlaying(ItemStack.EMPTY, EnumCassetteType.NONE);
					PacketDistributor.TRACKING_ENTITY_AND_SELF.with(player).send(packet);
				}
			}
			
			if(player.level().getGameTime() % 50 == 0 && musicPlayingCap.getCassetteType() != EnumCassetteType.NONE)
			{
				EnumCassetteType.EffectContainer effectContainer = musicPlayingCap.getCassetteType().getEffectContainer();
				if(!effectContainer.onHit()) //Apply the cassette buff every 50 ticks, if there is any
					player.addEffect(effectContainer.effect().get());
			}
		}
	}
	
	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker)
	{
		IMusicPlaying musicPlaying = attacker.getData(MSAttachments.MUSIC_PLAYING);
		if(musicPlaying.getCassetteType() != EnumCassetteType.NONE && musicPlaying.getCurrentMusicPlayer() == stack)
		{
			RandomSource r = attacker.level().getRandom();
			
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
			//Compare the luck of the target with the user's
			chanceToHit = chanceToHit + (attackerLuckValue / 10) - (targetLuckValue / 10);
			
			if(chanceToHit > r.nextFloat() && effectContainer.onHit())
			{
				target.addEffect(effectContainer.effect().get());
			}
		}
		return super.hurtEnemy(stack, target, attacker);
	}
	
	public static boolean hasCassette(ItemStack stack)
	{
		return !stack.getData(MSAttachments.MUSIC_PLAYER_INVENTORY).getStackInSlot(0).isEmpty();
	}
}

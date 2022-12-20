package com.mraof.minestuck.item.weapon;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.EnumCassetteType;
import com.mraof.minestuck.inventory.musicplayer.CassetteContainerMenu;
import com.mraof.minestuck.inventory.musicplayer.IMusicPlaying;
import com.mraof.minestuck.inventory.musicplayer.MusicPlayerItemCapProvider;
import com.mraof.minestuck.item.CassetteItem;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.MusicPlayerPacket;
import com.mraof.minestuck.util.MSCapabilities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Random;

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
 * {@link #getShareTag(ItemStack)}
 * {@link #hasCassette(ItemStack)}
 */

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID)
public class MusicPlayerWeapon extends WeaponItem
{
	public static final String TITLE = "minestuck.music_player";
	private final float volume;
	private final float pitch;
	
	private static IItemHandler getItemHandler(ItemStack itemStack)
	{
		return itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElseThrow(() ->
				new IllegalArgumentException("Expected an item handler for the music player item, but " + itemStack + " does not expose an item handler."));
	}
	
	private static IMusicPlaying getMusicPlaying(LivingEntity entity)
	{
		return entity.getCapability(MSCapabilities.MUSIC_PLAYING_CAPABILITY).orElseThrow(() ->
				new IllegalArgumentException("Expected an music playing for this entity, but " + entity + " does not expose a music playing."));
	}
	
	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt)
	{
		return new MusicPlayerItemCapProvider();
	}
	
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
		
		IItemHandler itemStackHandlerMusicPlayer = getItemHandler(musicPlayer);
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
						packet = MusicPlayerPacket.createPacket(playerIn, cassette.cassetteID, volume, pitch);
						musicPlayingCap.setMusicPlaying(musicPlayer, cassette.cassetteID);
					} else
					{
						packet = MusicPlayerPacket.createPacket(playerIn, EnumCassetteType.NONE, 0, 0);
						musicPlayingCap.setMusicPlaying(ItemStack.EMPTY, EnumCassetteType.NONE);
					}
					MSPacketHandler.sendToTrackingAndSelf(packet, playerIn);
				}
			}
			//open the GUI if right-clicked
			else
			{
				MusicPlayerPacket packet = MusicPlayerPacket.createPacket(playerIn, EnumCassetteType.NONE, 0, 0);
				musicPlayingCap.setMusicPlaying(ItemStack.EMPTY, EnumCassetteType.NONE);
				MSPacketHandler.sendToTrackingAndSelf(packet, playerIn); //This will stop the music before opening the GUI
				
				NetworkHooks.openGui((ServerPlayer) playerIn, new SimpleMenuProvider((pContainerId, pInventory, pPlayer) ->
						new CassetteContainerMenu(pContainerId, pInventory, itemStackHandlerMusicPlayer, musicPlayer),
						new TranslatableComponent(TITLE)));
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
		if(tickEvent.side.isServer() && tickEvent.phase == TickEvent.Phase.END)
		{
			Player player = tickEvent.player;
			IMusicPlaying musicPlayingCap = getMusicPlaying(player);
			
			if(!(player.getItemInHand(InteractionHand.MAIN_HAND) == musicPlayingCap.getCurrentMusicPlayer() ||
					player.getItemInHand(InteractionHand.OFF_HAND) == musicPlayingCap.getCurrentMusicPlayer()) ||
					musicPlayingCap.getCurrentMusicPlayer().isEmpty())
			{
				if(musicPlayingCap.getCassetteType() != EnumCassetteType.NONE)
				{ //If the Cassette player isn't in hand and is playing music, stop it
					MusicPlayerPacket packet = MusicPlayerPacket.createPacket(player, EnumCassetteType.NONE, 0, 0);
					musicPlayingCap.setMusicPlaying(ItemStack.EMPTY, EnumCassetteType.NONE);
					MSPacketHandler.sendToTrackingAndSelf(packet, player);
				}
			}
			
			if(player.level.getGameTime() % 50 == 0 && musicPlayingCap.getCassetteType() != EnumCassetteType.NONE)
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
			//Compare the luck of the target with the user's
			chanceToHit = chanceToHit + (attackerLuckValue / 10) - (targetLuckValue / 10);
			
			if(chanceToHit > r.nextFloat() && effectContainer.onHit())
			{
				target.addEffect(effectContainer.effect().get());
			}
		}
		return super.hurtEnemy(stack, target, attacker);
	}
	
	@Nullable
	@Override
	public CompoundTag getShareTag(ItemStack stack)
	{
		IItemHandler iitemHandler = getItemHandler(stack);
		CompoundTag nbt = stack.getTag() != null ? stack.getTag() : new CompoundTag();
		if(iitemHandler instanceof ItemStackHandler itemHandler)
			nbt.put("cassette", itemHandler.serializeNBT());
		return nbt;
	}
	
	@Override
	public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt)
	{
		if(nbt == null)
			stack.setTag(null);
		else
		{
			IItemHandler iitemHandler = getItemHandler(stack);
			if(iitemHandler instanceof ItemStackHandler itemHandler)
				itemHandler.deserializeNBT(nbt.getCompound("cassette"));
			stack.setTag(nbt);
		}
	}
	
	public static boolean hasCassette(ItemStack stack)
	{
		return !getItemHandler(stack).getStackInSlot(0).isEmpty();
	}
}

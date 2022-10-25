package com.mraof.minestuck.item.weapon;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mraof.minestuck.block.EnumCassetteType;
import com.mraof.minestuck.inventory.musicplayer.CapabilityMusicPlaying;
import com.mraof.minestuck.inventory.musicplayer.IMusicPlaying;
import com.mraof.minestuck.inventory.musicplayer.MusicPlayerCapabilityProvider;
import com.mraof.minestuck.inventory.musicplayer.MusicPlayerContainer;
import com.mraof.minestuck.item.CassetteItem;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.MusicPlayerPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
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
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class MusicPlayerItem extends WeaponItem
{
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
		ItemStack musicPlayer = playerIn.getItemInHand(handIn);
		
		IItemHandler itemStackHandlerMusicPlayer = getItemStackHandlerMusicPlayer(musicPlayer);
		IMusicPlaying musicPlayingCap = getMusicPlaying(musicPlayer);
		
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
						musicPlayingCap.setCassetteType(cassette.cassetteID);
					}
					else
					{
						packet = MusicPlayerPacket.createPacket(playerIn, EnumCassetteType.NONE);
						musicPlayingCap.setCassetteType(EnumCassetteType.NONE);
					}
						MSPacketHandler.sendToTrackingAndSelf(packet, playerIn);
				}
			}
			//open the GUI if right-clicked
			else
			{
				NetworkHooks.openGui((ServerPlayer) playerIn, new SimpleMenuProvider((pContainerId, pInventory, pPlayer) ->
						new MusicPlayerContainer(pContainerId, pInventory, itemStackHandlerMusicPlayer),
						new TextComponent("Music Player")));
			}
		}
		return super.use(level, playerIn, handIn);
	}
	
	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entityIn, int itemSlot, boolean isSelected)
	{
		if(entityIn instanceof Player player)
		{
			if(level.getGameTime() % 50 == 0)
			{
				super.inventoryTick(stack, level, entityIn, itemSlot, isSelected);
				IMusicPlaying musicPlaying = getMusicPlaying(stack);
				if(musicPlaying.getCassetteType() != EnumCassetteType.NONE)
				{
					EffectContainer effectContainer = getEffect(musicPlaying.getCassetteType());
					if(effectContainer != null && !effectContainer.onHit)
						player.addEffect(effectContainer.effect);
				}
			}
		}
	}
	
	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack)
	{
		IMusicPlaying musicPlaying = getMusicPlaying(stack);
		if(musicPlaying.getCassetteType() != EnumCassetteType.NONE)
		{
			ImmutableMultimap.Builder<Attribute, AttributeModifier> multimap = ImmutableMultimap.builder();
			float attackSpeed = musicPlaying.getCassetteType().getAttackSpeed();
			multimap.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", attackSpeed, AttributeModifier.Operation.ADDITION));
			return multimap.build();
		}
		return super.getAttributeModifiers(slot, stack);
	}
	
	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker)
	{
		IMusicPlaying musicPlaying = getMusicPlaying(stack);
		if(musicPlaying.getCassetteType() != EnumCassetteType.NONE)
		{
			Random r = attacker.level.getRandom();
			
			double chanceToHit = r.nextFloat();
			double attackerLuckValue;
			double targetLuckValue;
			
			EffectContainer effectContainer = getEffect(musicPlaying.getCassetteType());
			AttributeInstance attackerLuck = attacker.getAttribute(Attributes.LUCK);
			AttributeInstance targetLuck = target.getAttribute(Attributes.LUCK);
			
			if(attackerLuck != null)
				attackerLuckValue = attackerLuck.getValue();
			else
				attackerLuckValue = 0.0D;
			
			if(targetLuck != null)
				targetLuckValue = targetLuck.getValue();
			else
				targetLuckValue = 0.0D;
			
			chanceToHit = chanceToHit - (attackerLuckValue / 10) + (targetLuckValue / 10);
			
			if(effectContainer != null && effectContainer.applyingChance > chanceToHit && effectContainer.onHit)
			{
				target.addEffect(effectContainer.effect());
			}
		}
		return super.hurtEnemy(stack, target, attacker);
	}
	
	public EffectContainer getEffect(EnumCassetteType cassetteType)
	{
		return switch(cassetteType)
				{
					case ELEVEN -> new EffectContainer(new MobEffectInstance(MobEffects.WITHER, 80, 0),
							0.10F, true);
					
					//case THIRTEEN -> null;
					
					//case BLOCKS -> null;
					
					case CAT -> new EffectContainer(new MobEffectInstance(MobEffects.NIGHT_VISION, 50, 0,
							false, false, false),1F, false);
					
					case CHIRP -> new EffectContainer(new MobEffectInstance(MobEffects.SLOW_FALLING, 50, 0,
							false, false, false),
							1F, false);
					
					//case DANCE_STAB_DANCE -> null;
					
					//case EMISSARY_OF_DANCE -> null;
					
					case FAR -> new EffectContainer(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 50, 0,
							false, false, false),
							1F, false);
					
					case MALL -> new EffectContainer(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 0),
							0.30F, true);
					
					case MELLOHI -> new EffectContainer(new MobEffectInstance(MobEffects.LEVITATION, 60, 0),
							0.20F, true);
					
					case PIGSTEP -> new EffectContainer(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 50, 0,
							false, false, false),
							1F, false);
					//case RETRO_BATTLE_THEME -> null;
					
					//case STAL -> null;
					
					//case STRAD -> null;
					
					//case WAIT -> null;
					
					//case WARD -> null;
					
					default -> null;
				};
	}
	
	record EffectContainer(MobEffectInstance effect, float applyingChance, boolean onHit) {}
}

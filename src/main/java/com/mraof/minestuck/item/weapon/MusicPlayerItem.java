package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.block.EnumCassetteType;
import com.mraof.minestuck.inventory.musicplayer.CapabilityProviderMusicPlayer;
import com.mraof.minestuck.inventory.musicplayer.ItemStackHandlerMusicPlayer;
import com.mraof.minestuck.inventory.musicplayer.MusicPlayerContainer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class MusicPlayerItem extends WeaponItem
{
	private static IItemHandler getItemStackHandlerMusicPlayer(ItemStack itemStack)
	{
		IItemHandler musicPlayer = itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElseThrow(() ->
				new IllegalArgumentException("ItemStack " + itemStack + " is not an item handler"));
		if(!(musicPlayer instanceof ItemStackHandlerMusicPlayer))
		{
			return new ItemStackHandlerMusicPlayer(1);
		}
		return musicPlayer;
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
		//open the GUI if right clicked
		if(!level.isClientSide)
		{
			NetworkHooks.openGui((ServerPlayer) playerIn, new SimpleMenuProvider((pContainerId, pInventory, pPlayer) ->
					new MusicPlayerContainer(pContainerId, pInventory,
							getItemStackHandlerMusicPlayer(playerIn.getItemInHand(handIn))),
					new TextComponent("Music Player")));
		}
		return super.use(level, playerIn, handIn);
	}
	
	public void musicEffect(EnumCassetteType cassetteType)
	{
		switch(cassetteType)
		{
			case THIRTEEN -> {}
			case CAT -> {}
			case BLOCKS -> {}
			case CHIRP -> {}
			case FAR -> {}
			case MALL -> {}
			case MELLOHI -> {}
			case EMISSARY_OF_DANCE -> {}
			case DANCE_STAB_DANCE -> {}
			case RETRO_BATTLE_THEME -> {}
			case NONE -> {}
		}
		;
	}
}

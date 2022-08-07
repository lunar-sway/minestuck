package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.inventory.CapabilityProviderMusicPlayer;
import com.mraof.minestuck.inventory.ItemStackHandlerMusicPlayer;
import com.mraof.minestuck.inventory.MusicPlayerContainer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class MusicPlayerItem extends WeaponItem
{
	private static IItemHandler getItemStackHandlerMusicPlayer(ItemStack itemStack) {
		IItemHandler musicPlayer = itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElseThrow(() ->
				new IllegalArgumentException("ItemStack " + itemStack + " is not an item handler"));
		if (!(musicPlayer instanceof ItemStackHandlerMusicPlayer)) {
			return new ItemStackHandlerMusicPlayer();
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
		if(!level.isClientSide)
			NetworkHooks.openGui((ServerPlayer) playerIn, new SimpleMenuProvider((pContainerId, pInventory, pPlayer) ->
					new MusicPlayerContainer(pContainerId, pInventory, getItemStackHandlerMusicPlayer(playerIn.getItemInHand(handIn))),
					new TextComponent("Music Player")));
		return super.use(level, playerIn, handIn);
	}
}

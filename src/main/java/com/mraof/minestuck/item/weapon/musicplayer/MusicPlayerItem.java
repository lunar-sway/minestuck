package com.mraof.minestuck.item.weapon.musicplayer;

import com.mraof.minestuck.inventory.MusicPlayerContainer;
import com.mraof.minestuck.item.weapon.WeaponItem;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class MusicPlayerItem extends WeaponItem
{
	public MusicPlayerItem(Builder builder, Properties properties)
	{
		super(builder, properties);
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn)
	{
		if(!level.isClientSide)
			NetworkHooks.openGui((ServerPlayer) playerIn, new SimpleMenuProvider((pContainerId, pInventory, pPlayer) ->
					new MusicPlayerContainer(pContainerId, pInventory), new TextComponent("Music Player")));
		return super.use(level, playerIn, handIn);
	}
}

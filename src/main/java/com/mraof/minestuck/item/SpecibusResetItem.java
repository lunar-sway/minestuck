package com.mraof.minestuck.item;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.network.SyncSpecibusPacket;
import com.mraof.minestuck.util.MSAttachments;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;

public class SpecibusResetItem extends Item
{
	public SpecibusResetItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand)
	{
		ItemStack itemStack = player.getItemInHand(usedHand);
		if(player instanceof ServerPlayer serverPlayer)
		{
			itemStack.shrink(1);
			serverPlayer.setData(MSAttachments.SELECTED_SPECIBUS, new ArrayList<>());
			
			PacketDistributor.sendToPlayer(serverPlayer,
					new SyncSpecibusPacket(new ArrayList<>(),
							MinestuckConfig.SERVER.maxSpecibusCount.get()));
			
			level.playSound(null,
					player.getX(), player.getY(), player.getZ(),
					SoundEvents.ENCHANTMENT_TABLE_USE,
					SoundSource.PLAYERS, 1.0F, 1.0F);
		}
		return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
	}
}
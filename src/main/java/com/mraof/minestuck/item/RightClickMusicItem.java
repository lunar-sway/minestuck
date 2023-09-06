package com.mraof.minestuck.item;

import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class RightClickMusicItem extends Item
{
	private final Type type;
	
	public enum Type {
		ELECTRIC_AUTOHARP,
		ACOUSTIC_GUITAR
	}
	
	public RightClickMusicItem(Properties properties, Type type)
	{
		super(properties);
		this.type = type;
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn)
	{
		SoundEvent sound = MSSoundEvents.ITEM_ELECTRIC_AUTOHARP_STROKE.get(); //autoharp by default
		/*if(type == Type.ACOUSTIC_GUITAR)
		{
		}*/
		
		level.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), sound, playerIn.getSoundSource(), 0.75F, 1F);
		return InteractionResultHolder.success(playerIn.getItemInHand(handIn));
	}
}

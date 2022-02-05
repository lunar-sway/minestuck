package com.mraof.minestuck.item;

import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

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
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		SoundEvent sound = new SoundEvent(MSSoundEvents.ITEM_ELECTRIC_AUTOHARP_STROKE.getLocation()); //autoharp by default
		/*if(type == Type.ACOUSTIC_GUITAR)
		{
		}*/
		
		playerIn.level.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), sound, playerIn.getSoundSource(), 0.75F, 1F);
		return ActionResult.success(playerIn.getItemInHand(handIn));
	}
}

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
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		SoundEvent[] soundArray = new SoundEvent[]{
				MSSoundEvents.EVENT_ELECTRIC_AUTOHARP_STROKE_AMAJOR, MSSoundEvents.EVENT_ELECTRIC_AUTOHARP_STROKE_BMAJOR, MSSoundEvents.EVENT_ELECTRIC_AUTOHARP_STROKE_BBMAJOR,
				MSSoundEvents.EVENT_ELECTRIC_AUTOHARP_STROKE_CMAJOR, MSSoundEvents.EVENT_ELECTRIC_AUTOHARP_STROKE_DMAJOR, MSSoundEvents.EVENT_ELECTRIC_AUTOHARP_STROKE_EBMAJOR,
				MSSoundEvents.EVENT_ELECTRIC_AUTOHARP_STROKE_EMAJOR, MSSoundEvents.EVENT_ELECTRIC_AUTOHARP_STROKE_FMAJOR, MSSoundEvents.EVENT_ELECTRIC_AUTOHARP_STROKE_GMAJOR}; //default set to autoharp
		/*if(type == Type.ACOUSTIC_GUITAR)
		{
		}*/
		
		playerIn.world.playSound(null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), soundArray[playerIn.getRNG().nextInt(soundArray.length)], playerIn.getSoundCategory(), 0.75F, 1F);
		return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));
	}
}

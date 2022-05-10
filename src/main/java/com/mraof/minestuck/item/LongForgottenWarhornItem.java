package com.mraof.minestuck.item;

import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import java.util.Random;

public class LongForgottenWarhornItem extends Item
{
	static EffectInstance[] effect = new EffectInstance[] {
			new EffectInstance(Effects.BLINDNESS, 400, 0),
			new EffectInstance(Effects.WITHER, 300, 1),
			new EffectInstance(Effects.POISON, 300, 2),
			new EffectInstance(Effects.HUNGER, 400, 1),
			new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 400, 2),
			new EffectInstance(Effects.UNLUCK, 600, 3),
			new EffectInstance(Effects.LUCK, 600, 3),
			new EffectInstance(Effects.NIGHT_VISION, 400, 0),
			new EffectInstance(Effects.DAMAGE_BOOST, 200, 1),
			new EffectInstance(Effects.DAMAGE_RESISTANCE, 300, 1),
			new EffectInstance(Effects.REGENERATION, 300, 2),
			new EffectInstance(Effects.JUMP, 400, 2),
			new EffectInstance(Effects.MOVEMENT_SPEED, 400, 2),
			new EffectInstance(Effects.DIG_SPEED, 400, 2),
			new EffectInstance(Effects.ABSORPTION, 500, 1),
			new EffectInstance(Effects.FIRE_RESISTANCE, 600, 0),
			new EffectInstance(Effects.GLOWING, 500, 0),
			new EffectInstance(Effects.HEAL, 20, 0),
			new EffectInstance(Effects.HARM, 20, 0),
			new EffectInstance(Effects.INVISIBILITY, 500, 3),
			new EffectInstance(Effects.WATER_BREATHING, 400, 0),
			new EffectInstance(Effects.CONFUSION, 300, 0),
			new EffectInstance(Effects.WEAKNESS, 200, 1),
			new EffectInstance(Effects.LEVITATION, 200, 2),
			new EffectInstance(Effects.DIG_SLOWDOWN, 300, 2),
			new EffectInstance(Effects.SATURATION, 400, 1)};
	
	public LongForgottenWarhornItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		worldIn.playSound(playerIn, playerIn.getX(), playerIn.getY(), playerIn.getZ(), MSSoundEvents.ITEM_LONG_FORGOTTEN_WARHORN_USE, SoundCategory.AMBIENT, 1.5F, 1.0F);
		
		ItemStack item = playerIn.getItemInHand(handIn);
		if(!worldIn.isClientSide)
		{
			Random rand = new Random();
			int durability = rand.nextInt(14) + 1;
			int raneffect = rand.nextInt(effect.length);
			playerIn.addEffect(new EffectInstance(effect[raneffect]));
			if(raneffect != 0)
			{
				playerIn.addEffect(new EffectInstance(Effects.BLINDNESS, 15, 1));
			}
			item.hurtAndBreak(durability, playerIn, playerEntity -> playerEntity.broadcastBreakEvent(Hand.MAIN_HAND));
		}
		
		return ActionResult.success(item);
	}
}
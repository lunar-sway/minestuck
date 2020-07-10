package com.mraof.minestuck.item;

import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
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
			new EffectInstance(Effects.SLOWNESS, 400, 2),
			new EffectInstance(Effects.UNLUCK, 600, 3),
			new EffectInstance(Effects.LUCK, 600, 3),
			new EffectInstance(Effects.NIGHT_VISION, 400, 0),
			new EffectInstance(Effects.STRENGTH, 200, 1),
			new EffectInstance(Effects.RESISTANCE, 300, 1),
			new EffectInstance(Effects.REGENERATION, 300, 2),
			new EffectInstance(Effects.JUMP_BOOST, 400, 2),
			new EffectInstance(Effects.SPEED, 400, 2),
			new EffectInstance(Effects.HASTE, 400, 2),
			new EffectInstance(Effects.ABSORPTION, 500, 1),
			new EffectInstance(Effects.FIRE_RESISTANCE, 600, 0),
			new EffectInstance(Effects.GLOWING, 500, 0),
			new EffectInstance(Effects.INSTANT_HEALTH, 20, 0),
			new EffectInstance(Effects.INSTANT_DAMAGE, 20, 0),
			new EffectInstance(Effects.INVISIBILITY, 500, 3),
			new EffectInstance(Effects.WATER_BREATHING, 400, 0),
			new EffectInstance(Effects.NAUSEA, 300, 0),
			new EffectInstance(Effects.WEAKNESS, 200, 1),
			new EffectInstance(Effects.LEVITATION, 200, 2),
			new EffectInstance(Effects.MINING_FATIGUE, 300, 2),
			new EffectInstance(Effects.SATURATION, 400, 1)};
	
	public LongForgottenWarhornItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		ItemStack item = playerIn.getHeldItem(handIn);
		if(!worldIn.isRemote)
		{
			Random rand = new Random();
			int durability = rand.nextInt(14) + 1;
			int raneffect = rand.nextInt(effect.length);
			playerIn.addPotionEffect(new EffectInstance(effect[raneffect]));
			if(raneffect != 0)
			{
				playerIn.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 15, 1));
			}
			item.damageItem(durability, playerIn, playerEntity -> playerEntity.sendBreakAnimation(Hand.MAIN_HAND));
			playerIn.world.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, MSSoundEvents.ITEM_LONG_FORGOTTEN_WARHORN_USE, SoundCategory.AMBIENT, 1.5F, 1.0F);
		}
		if(worldIn.isRemote)
		{
			return new ActionResult<>(ActionResultType.SUCCESS, item);
		} else
		{
			return new ActionResult<>(ActionResultType.PASS, item);
		}
	}
}
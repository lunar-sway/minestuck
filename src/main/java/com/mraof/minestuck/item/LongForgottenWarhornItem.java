package com.mraof.minestuck.item;

import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class LongForgottenWarhornItem extends Item
{
	static MobEffectInstance[] effect = new MobEffectInstance[] {
			new MobEffectInstance(MobEffects.BLINDNESS, 400, 0),
			new MobEffectInstance(MobEffects.WITHER, 300, 1),
			new MobEffectInstance(MobEffects.POISON, 300, 2),
			new MobEffectInstance(MobEffects.HUNGER, 400, 1),
			new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 400, 2),
			new MobEffectInstance(MobEffects.UNLUCK, 600, 3),
			new MobEffectInstance(MobEffects.LUCK, 600, 3),
			new MobEffectInstance(MobEffects.NIGHT_VISION, 400, 0),
			new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 1),
			new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 300, 1),
			new MobEffectInstance(MobEffects.REGENERATION, 300, 2),
			new MobEffectInstance(MobEffects.JUMP, 400, 2),
			new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 400, 2),
			new MobEffectInstance(MobEffects.DIG_SPEED, 400, 2),
			new MobEffectInstance(MobEffects.ABSORPTION, 500, 1),
			new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 600, 0),
			new MobEffectInstance(MobEffects.GLOWING, 500, 0),
			new MobEffectInstance(MobEffects.HEAL, 20, 0),
			new MobEffectInstance(MobEffects.HARM, 20, 0),
			new MobEffectInstance(MobEffects.INVISIBILITY, 500, 3),
			new MobEffectInstance(MobEffects.WATER_BREATHING, 400, 0),
			new MobEffectInstance(MobEffects.CONFUSION, 300, 0),
			new MobEffectInstance(MobEffects.WEAKNESS, 200, 1),
			new MobEffectInstance(MobEffects.LEVITATION, 200, 2),
			new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 300, 2),
			new MobEffectInstance(MobEffects.SATURATION, 400, 1)};
	
	public LongForgottenWarhornItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn)
	{
		level.playSound(playerIn, playerIn.getX(), playerIn.getY(), playerIn.getZ(), MSSoundEvents.ITEM_LONG_FORGOTTEN_WARHORN_USE.get(), SoundSource.AMBIENT, 1.5F, 1.0F);
		
		ItemStack item = playerIn.getItemInHand(handIn);
		if(!level.isClientSide)
		{
			int durability = playerIn.getRandom().nextInt(14) + 1;
			int raneffect = playerIn.getRandom().nextInt(effect.length);
			playerIn.addEffect(new MobEffectInstance(effect[raneffect]));
			if(raneffect != 0)
			{
				playerIn.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 15, 1));
			}
			item.hurtAndBreak(durability, playerIn, playerEntity -> playerEntity.broadcastBreakEvent(InteractionHand.MAIN_HAND));
		}
		
		return InteractionResultHolder.success(item);
	}
}
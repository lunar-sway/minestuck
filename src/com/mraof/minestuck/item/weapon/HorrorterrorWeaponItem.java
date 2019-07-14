package com.mraof.minestuck.item.weapon;

import java.util.Random;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.TextFormatting;

public class HorrorterrorWeaponItem extends WeaponItem
{
	
	public HorrorterrorWeaponItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, Properties builder)
	{
		super(tier, attackDamageIn, attackSpeedIn, efficiency, builder);
	}
	
	@Override
	public boolean hitEntity(ItemStack itemStack, LivingEntity target, LivingEntity attacker)
    {
        if (attacker instanceof PlayerEntity && attacker.getRNG().nextFloat() < .15)
        {
        	if(!attacker.world.isRemote) {
        		String[] options = new String[] {"item.clawOfNrubyiglith.message.machinations",
        				"item.clawOfNrubyiglith.message.stir",
        				"item.clawOfNrubyiglith.message.suffering",
        				"item.clawOfNrubyiglith.message.will",
        				"item.clawOfNrubyiglith.message.done",
        				"item.clawOfNrubyiglith.message.conspiracies"};
        		Random rand = new Random();
        		int num = rand.nextInt(options.length);
    			ITextComponent message = new TranslationTextComponent(options[num]);
    			message.getStyle().setColor(TextFormatting.DARK_PURPLE);
    			attacker.sendMessage(message);
    		}
        	attacker.addPotionEffect(new EffectInstance(Effects.WITHER, 100, 2));
        }
        return super.hitEntity(itemStack, target, attacker);
    }
}
package com.mraof.minestuck.item.weapon;

import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class ItemHorrorterrorWeapon extends ItemWeapon {
	
	public ItemHorrorterrorWeapon(int maxUses, double damageVsEntity, double weaponSpeed, int enchantability, String name) {
		super(maxUses, damageVsEntity, weaponSpeed, enchantability, name);
	}	
	
	@Override
	public boolean hitEntity(ItemStack itemStack, EntityLivingBase target, EntityLivingBase attacker)
    {
        if (attacker instanceof EntityPlayer && attacker.getRNG().nextFloat() < .15)
        {
        	if(!attacker.world.isRemote) {
        		String[]  options = new String[] {"Your blood shall fuel our machinations.", 
        				"They stir in your subconscious.", 
        				"Your suffering grants us strength.", 
        				"Our will is your will.",
        				"It is done.",
        				"You are a tool. A tool through which we we exert our conspiracies."};
        		Random rand = new Random();
        		int num = rand.nextInt(5);
    			ITextComponent message = new TextComponentTranslation(options[num]);       
    			message.getStyle().setColor(TextFormatting.DARK_PURPLE);
    			attacker.sendMessage(message);
    		}
        	attacker.addPotionEffect(new PotionEffect(MobEffects.WITHER, 100, 2));
        }
        return super.hitEntity(itemStack, target, attacker);
    }
}

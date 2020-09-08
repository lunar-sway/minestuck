package com.mraof.minestuck.item.weapon;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

public class HorrorterrorWeaponItem extends WeaponItem
{
	
	private static final List<String> MESSAGES = ImmutableList.of("machinations", "stir", "suffering", "will", "done", "conspiracies");
	
	public HorrorterrorWeaponItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, MSToolType toolType, Properties builder)
	{
		super(tier, attackDamageIn, attackSpeedIn, efficiency, toolType, builder);
	}
	
	@Override
	public boolean hitEntity(ItemStack itemStack, LivingEntity target, LivingEntity attacker)
    {
        if (attacker instanceof PlayerEntity && attacker.getRNG().nextFloat() < .15)
        {
        	if(!attacker.world.isRemote) {
        		String key = MESSAGES.get(attacker.getRNG().nextInt(MESSAGES.size()));
    			ITextComponent message = new TranslationTextComponent(getTranslationKey()+".message."+key);
    			message.getStyle().setColor(TextFormatting.DARK_PURPLE);
    			attacker.sendMessage(message);
    		}
        	attacker.addPotionEffect(new EffectInstance(Effects.WITHER, 100, 2));
        }
        return super.hitEntity(itemStack, target, attacker);
    }
}
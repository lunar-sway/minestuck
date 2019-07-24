package com.mraof.minestuck.item.foods;

import com.mraof.minestuck.util.MinestuckSoundHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class UnknowableEggItem extends Item {
	
	public UnknowableEggItem(Properties properties)
	{
		super(properties);
	}

	protected void onItemUseFinish(ItemStack stack, World worldIn, PlayerEntity player)
	{
		if(!worldIn.isRemote)
		{
			player.addPotionEffect(new EffectInstance(Effects.NAUSEA, 160, 2));
			player.addPotionEffect(new EffectInstance(Effects.POISON, 160, 2));
			player.world.playSound(null, player.posX, player.posY, player.posZ, MinestuckSoundHandler.soundWhispers, SoundCategory.AMBIENT, 1.0F, 0.8F);
		}
		super.onItemUseFinish(stack, worldIn, player);
	}
}
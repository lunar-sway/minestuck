package com.mraof.minestuck.item.foods;

import com.mraof.minestuck.block.SimpleCakeBlock;
import com.mraof.minestuck.util.MinestuckSoundHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.item.Item;
import com.mraof.minestuck.item.foods.FoodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class UnknowableEggItem extends FoodItem
{
	
	public UnknowableEggItem(int healAmountIn, float saturation, boolean meat, Properties builder)
	{
		super(healAmountIn, saturation, meat, builder);
	}
	
	@Override
	protected void onFoodEaten(ItemStack stack, World worldIn, PlayerEntity player)
	{
		if(!worldIn.isRemote)
		{
			player.addPotionEffect(new EffectInstance(Effects.NAUSEA, 160, 2));
			player.addPotionEffect(new EffectInstance(Effects.POISON, 160, 2));
			player.world.playSound(null, player.posX, player.posY, player.posZ, MinestuckSoundHandler.soundWhispers, SoundCategory.AMBIENT, 1.0F, 0.8F);
		}
		super.onFoodEaten(stack, worldIn, player);
	}
}
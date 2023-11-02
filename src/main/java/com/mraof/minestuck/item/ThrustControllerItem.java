package com.mraof.minestuck.item;


import com.mraof.minestuck.item.armor.JetPackItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;


public class ThrustControllerItem extends Item
{
	public ThrustControllerItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn)
	{
		ItemStack item = playerIn.getItemInHand(handIn);
		ItemStack jetpackItem = playerIn.getItemBySlot(EquipmentSlot.CHEST);
		Item thrustController = MSItems.THRUST_CONTROLLER.get();
		
		boolean hasController = playerIn.getItemInHand(InteractionHand.MAIN_HAND).is(thrustController);
		boolean hasControllerOffhand =  playerIn.getItemInHand(InteractionHand.OFF_HAND).is(thrustController);
		
		if(hasController && hasControllerOffhand && playerIn.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof JetPackItem)
		{
			jetpackItem.getOrCreateTag().putBoolean("is_boosting", true);
			return InteractionResultHolder.success(item);
		}
		
		return InteractionResultHolder.fail(item);
	}
}

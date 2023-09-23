package com.mraof.minestuck.item;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.ClientProxy;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.item.CompassItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.Structure;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * Generic class for scanners which locates the nearest structure of a certain type in the player's current dimension.
 * Takes an optional fuel item, which will be consumed every tick until the device deactivates.
 * Toggles on or off with right-click, or when fuel runs out.
 */

public class StructureScannerItem extends Item
{
	private final TagKey<Structure> structure;
	@Nullable
	private final Supplier<Item> fuelItem;
	
	public StructureScannerItem(Properties properties, TagKey<Structure> structure, @Nullable Supplier<Item> fuelItem)
	{
		super(properties);
		this.structure = structure;
		this.fuelItem = fuelItem;
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand)
	{
		ItemStack stack = pPlayer.getItemInHand(pUsedHand);
		
		if(pPlayer.isCreative() || stack.getDamageValue() != stack.getMaxDamage() && checkFuelNeeded(pPlayer, pLevel))
		{
			stack.getOrCreateTag().putBoolean("Powered", true);
			pLevel.playSound(pPlayer, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.UI_BUTTON_CLICK.get(), SoundSource.AMBIENT, 0.8F, 1.3F);
			
			MutableComponent message = Component.translatable("message.temple_scanner.on");
			pPlayer.sendSystemMessage(message.withStyle(ChatFormatting.DARK_GREEN));
		}
		return InteractionResultHolder.success(stack);
		
	}
	
	/**
	 * Check if the item is powered, and if it's out of battery, recharge it.
	 * Set the location to the nearest structure, check that a structure exists, then reduce charge if fuelled.
	 */
	@Override
	public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected)
	{
		super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
		
		if(pStack.hasTag() && pStack.getTag().getBoolean("Powered") && pLevel instanceof ServerLevel sLevel)
		{
			resetCharge(pStack);
			
			BlockPos pos = setLocation(pEntity, sLevel);
			
			checkLocation(pStack, pos);
			
			reduceCharge(pStack, pEntity, pLevel);
		}
	}
	
	public static boolean isCharged(ItemStack stack)
	{
		return stack.getDamageValue() < stack.getMaxDamage();
	}
	
	public void resetCharge(ItemStack pStack)
	{
		if(!isCharged(pStack))
		{
			pStack.setDamageValue(0);
		}
	}
	
	public BlockPos setLocation(Entity pEntity, ServerLevel sLevel)
	{
		return sLevel.findNearestMapStructure(structure, pEntity.blockPosition(), 100, false);
	}
	
	public void checkLocation(ItemStack pStack, BlockPos pos)
	{
		if(pos == null)
		{
			pStack.getTag().remove("TargetLocation");
		} else
		{
			pStack.getTag().put("TargetLocation", NbtUtils.writeBlockPos(pos));
		}
	}
	
	/**
	 * Check that the fuel item is set; if not, don't reduce charge.
	 * Scanner charge is represented by durability and damage. Damage is dealt every 20 ticks.
	 * If it runs out of charge, power it off.
	 *
	 * @param pStack current item
	 * @param pEntity player entity
	 * @param pLevel player's current level
	 * @return Boolean check for if deactivated, used to reactivate if players still has fuel.
	 */
	public boolean reduceCharge(ItemStack pStack, Entity pEntity, Level pLevel)
	{
		if(fuelItem != null && pEntity.tickCount % 20 == 0)
		{
			pStack.hurt(1, pLevel.random, pEntity instanceof ServerPlayer ? (ServerPlayer) pEntity : null);
			
			if(!isCharged(pStack))
			{
				pStack.getTag().putBoolean("Powered", false);
				
				MutableComponent message = Component.translatable("message.temple_scanner.off");
				pEntity.sendSystemMessage(message.withStyle(ChatFormatting.DARK_GREEN));
				
				return true;
			}
		}
		return false;
	}
	
	public boolean checkFuelNeeded(Player pPlayer, Level pLevel)
	{
		if(fuelItem != null)
		{
			ItemStack invItem = hasFuel(pPlayer);
			
			if (invItem != null)
			{
				useFuelItem(invItem, pPlayer, pLevel);
				return true;
			}
		}
		return false;
	}
	
	public ItemStack hasFuel(Player pPlayer){
		ItemStack fuelStack = new ItemStack(fuelItem.get());
		
		for (ItemStack invItem : pPlayer.getInventory().items)
		{
			if (ItemStack.isSameItem(invItem, fuelStack))
			{
				return invItem;
			}
		}
		return null;
	}
	
	public void useFuelItem(ItemStack invItem, Player pPlayer, Level pLevel)
	{
		if(!pLevel.isClientSide)
		{
			invItem.shrink(1);
		}
		pLevel.playSound(pPlayer, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.BLAZE_SHOOT, SoundSource.AMBIENT, 0.4F, 2F);
	}
}
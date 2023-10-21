package com.mraof.minestuck.item;

import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
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
import org.slf4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

/**
 * Generic class for scanners which locates the nearest structure of a certain type in the player's current dimension.
 * Takes an optional fuel item, which will be consumed every tick until the device deactivates.
 * Toggles on or off with right-click, or when fuel runs out.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class StructureScannerItem extends Item
{
	private static final Logger LOGGER = LogUtils.getLogger();
	private final TagKey<Structure> structure;
	@Nullable
	private final Supplier<Item> fuelItem;
	
	public StructureScannerItem(Properties properties, TagKey<Structure> structure, @Nullable Supplier<Item> fuelItem)
	{
		super(properties);
		this.structure = structure;
		this.fuelItem = fuelItem;
	}
	
	@Nullable
	public static GlobalPos getTargetFromNbt(ItemStack stack)
	{
		if(stack.hasTag() && stack.getTag().contains("TargetLocation"))
		{
			return GlobalPos.CODEC
					.parse(NbtOps.INSTANCE, stack.getTag().get("TargetLocation"))
					.resultOrPartial(LOGGER::error).orElse(null);
		} else
			return null;
	}
	
	public void setTargetToNbt(ItemStack stack, @Nullable GlobalPos pos)
	{
		CompoundTag tag = stack.getOrCreateTag();
		if(pos == null)
			tag.remove("TargetLocation");
		else
			tag.put("TargetLocation",
					GlobalPos.CODEC.encodeStart(NbtOps.INSTANCE, pos)
							.getOrThrow(false, LOGGER::error));
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand)
	{
		ItemStack stack = pPlayer.getItemInHand(pUsedHand);
		
		if(fuelItem != null && !pPlayer.isCreative())
		{
			ItemStack invItem = findItem(pPlayer, fuelItem.get());
			
			if(invItem == null)
				return InteractionResultHolder.fail(stack);
			
			useFuel(invItem, pPlayer, pLevel);
		}
		
		if (stack.getDamageValue() > 0)
			resetCharge(stack);
		
		stack.getOrCreateTag().putBoolean("Powered", true);
		pLevel.playSound(pPlayer, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.UI_BUTTON_CLICK.get(), SoundSource.AMBIENT, 0.8F, 1.3F);
		
		if (!pLevel.isClientSide)
		{
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
			if (!isCharged(pStack))
			{
				resetCharge(pStack);
			}
			
			GlobalPos pos = findStructureTarget(pEntity, sLevel);
			
			setTargetToNbt(pStack, pos);
			
			reduceCharge(pStack, pEntity, pLevel);
		}
	}
	
	public static boolean isCharged(ItemStack stack)
	{
		return stack.getDamageValue() < stack.getMaxDamage();
	}
	
	public void resetCharge(ItemStack pStack)
	{
		pStack.setDamageValue(0);
	}
	
	@Nullable
	public GlobalPos findStructureTarget(Entity entity, ServerLevel level)
	{
		BlockPos pos = level.findNearestMapStructure(this.structure, entity.blockPosition(), 100, false);
		return pos == null ? null : GlobalPos.of(level.dimension(), pos);
	}
	
	/**
	 * Does nothing if this scanner type has no fuel item that it uses.
	 * Scanner charge is represented by durability and damage.
	 * Damage is dealt every 20 ticks, powers off when out of charge.
	 *
	 * @param pStack current item
	 * @param pEntity player entity
	 * @param pLevel player's current level
	 */
	public void reduceCharge(ItemStack pStack, Entity pEntity, Level pLevel)
	{
		if(fuelItem != null && pEntity.tickCount % 20 == 0)
		{
			pStack.hurt(1, pLevel.random, pEntity instanceof ServerPlayer ? (ServerPlayer) pEntity : null);
			
			if(!isCharged(pStack))
			{
				pStack.getOrCreateTag().putBoolean("Powered", false);
				
				if(!pLevel.isClientSide())
				{
					MutableComponent message = Component.translatable("message.temple_scanner.off");
					pEntity.sendSystemMessage(message.withStyle(ChatFormatting.DARK_GREEN));
				}
			}
		}
	}
	
	@Nullable
	private static ItemStack findItem(Player player, Item item)
	{
		for (ItemStack invItem : player.getInventory().items)
		{
			if (invItem.is(item))
				return invItem;
		}
		return null;
	}
	
	public void useFuel(ItemStack invItem, Player pPlayer, Level pLevel)
	{
		if(!pLevel.isClientSide)
		{
			invItem.shrink(1);
		}
		pLevel.playSound(pPlayer, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.BLAZE_SHOOT, SoundSource.AMBIENT, 0.4F, 2F);
	}
}
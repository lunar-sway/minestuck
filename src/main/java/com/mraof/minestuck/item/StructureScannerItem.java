package com.mraof.minestuck.item;

import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
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
	public static final String ON = "message.temple_scanner.on";
	public static final String OFF = "message.temple_scanner.off";
	public static final String MISSING_FUEL = "message.temple_scanner.missing_fuel";
	public static final String NO_TARGET = "message.temple_scanner.no_target";
	
	private static final Logger LOGGER = LogUtils.getLogger();
	private final TagKey<Structure> structure;
	@Nullable
	private final Supplier<Item> fuelItem;
	private final int powerCapacity;
	
	public StructureScannerItem(Properties properties, TagKey<Structure> structure, @Nullable Supplier<Item> fuelItem, int powerCapacity)
	{
		super(properties.stacksTo(1));
		this.structure = structure;
		this.fuelItem = fuelItem;
		this.powerCapacity = powerCapacity;
	}
	
	public static boolean isPowered(ItemStack stack)
	{
		return getPower(stack) > 0;
	}
	
	@SuppressWarnings("DataFlowIssue")
	public static int getPower(ItemStack stack)
	{
		return stack.hasTag() ? stack.getTag().getInt("power") : 0;
	}
	
	public static void setPower(ItemStack stack, int power)
	{
		stack.getOrCreateTag().putInt("power", power);
	}
	
	@Nullable
	@SuppressWarnings("DataFlowIssue")
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
	
	public static void setTargetToNbt(ItemStack stack, @Nullable GlobalPos pos)
	{
		if(pos == null)
			stack.removeTagKey("TargetLocation");
		else
			stack.getOrCreateTag().put("TargetLocation",
					GlobalPos.CODEC.encodeStart(NbtOps.INSTANCE, pos)
							.getOrThrow(false, LOGGER::error));
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand)
	{
		ItemStack stack = player.getItemInHand(usedHand);
		
		if(isPowered(stack))
			return InteractionResultHolder.fail(stack);
		if(!(level instanceof ServerLevel serverLevel))
			return InteractionResultHolder.success(stack);
		
		if(fuelItem != null && !player.isCreative())
		{
			ItemStack invItem = findItem(player, fuelItem.get());
			
			if(invItem == null)
			{
				player.sendSystemMessage(Component.translatable(MISSING_FUEL).withStyle(ChatFormatting.RED));
				return InteractionResultHolder.fail(stack);
			}
			
			if(!tryActivateScanner(serverLevel, player, stack))
				return InteractionResultHolder.fail(stack);
			
			consumeFuelItem(invItem, player, serverLevel);
		} else {
			if(!tryActivateScanner(serverLevel, player, stack))
				return InteractionResultHolder.fail(stack);
		}
		
		return InteractionResultHolder.consume(stack);
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
	
	private static void consumeFuelItem(ItemStack fuelStack, Player player, ServerLevel level)
	{
		fuelStack.shrink(1);
		level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BLAZE_SHOOT, SoundSource.AMBIENT, 0.4F, 2F);
	}
	
	private boolean tryActivateScanner(ServerLevel level, Player player, ItemStack stack)
	{
		level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.UI_BUTTON_CLICK.value(), SoundSource.AMBIENT, 0.8F, 1.3F);
		GlobalPos pos = findStructureTarget(player, level);
		
		if(pos == null)
		{
			player.sendSystemMessage(Component.translatable(NO_TARGET).withStyle(ChatFormatting.RED));
			return false;
		}
		
		setTargetToNbt(stack, pos);
		setPower(stack, this.powerCapacity);
		
		player.sendSystemMessage(Component.translatable(ON).withStyle(ChatFormatting.DARK_GREEN));
		return true;
	}
	
	@Nullable
	private GlobalPos findStructureTarget(Entity entity, ServerLevel level)
	{
		BlockPos pos = level.findNearestMapStructure(this.structure, entity.blockPosition(), 100, false);
		return pos == null ? null : GlobalPos.of(level.dimension(), pos);
	}
	
	/**
	 * Check if the item is powered, and if it's out of battery, recharge it.
	 * Set the location to the nearest structure, check that a structure exists, then reduce charge if fuelled.
	 */
	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected)
	{
		if(!level.isClientSide && isPowered(stack) && entity.tickCount % 20 == 0)
			powerTick(stack, entity);
	}
	
	private static void powerTick(ItemStack stack, Entity entity)
	{
		setPower(stack, getPower(stack) - 1);
		
		if(!isPowered(stack))
		{
			setTargetToNbt(stack, null);
			MutableComponent message = Component.translatable(OFF);
			entity.sendSystemMessage(message.withStyle(ChatFormatting.DARK_GREEN));
		}
	}
	
	@Override
	public boolean isBarVisible(ItemStack stack)
	{
		return isPowered(stack);
	}
	
	@Override
	public int getBarWidth(ItemStack stack)
	{
		return Math.round(13 * (float) getPower(stack) / this.powerCapacity);
	}
	
	@Override
	public int getBarColor(ItemStack stack)
	{
		return 0x66BAFF;
	}
	
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
	{
		return !ItemStack.isSameItem(oldStack, newStack);
	}
}
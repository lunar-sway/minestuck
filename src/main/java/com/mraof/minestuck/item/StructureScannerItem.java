package com.mraof.minestuck.item;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.ClientProxy;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.item.CompassItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.NbtUtils;
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
		
		ItemProperties.register(this, new ResourceLocation("angle"),
				new CompassItemPropertyFunction((level, stack, entity) -> {
					if(stack.hasTag() && stack.getTag().contains("TargetLocation"))
					{
						return GlobalPos.of(level.dimension(), NbtUtils.readBlockPos(stack.getTag().getCompound("TargetLocation")));
					} else
					{
						return null;
					}
				}));
		
		ItemProperties.register(this, new ResourceLocation(Minestuck.MOD_ID, "powered"),
				((pStack, pLevel, pEntity, pSeed) -> pStack.hasTag() && pStack.getTag().getBoolean("Powered") ? 1 : 0));
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand)
	{
		ItemStack stack = pPlayer.getItemInHand(pUsedHand);
		
		if(pPlayer.isCreative() | stack.getDamageValue() != stack.getMaxDamage() | checkFuelItem(pPlayer, pLevel))
		{
			
			stack.getOrCreateTag().putBoolean("Powered", !stack.getTag().getBoolean("Powered"));
			pLevel.playSound(pPlayer, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.UI_BUTTON_CLICK.get(), SoundSource.AMBIENT, 0.8F, 1.3F);
		}
		return InteractionResultHolder.success(stack);
		
	}
	
	public static boolean isCharged(ItemStack stack)
	{
		return stack.getDamageValue() < stack.getMaxDamage();
	}
	
	@Override
	public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected)
	{
		super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
		
		if(pStack.hasTag() && pStack.getTag().getBoolean("Powered") && pLevel instanceof ServerLevel sLevel)
		{
			if(!isCharged(pStack))
			{
				pStack.setDamageValue(0);
			}
			
			BlockPos pos = sLevel.findNearestMapStructure(structure, pEntity.blockPosition(), 100, false);
			
			if(pos == null)
			{
				pStack.getTag().remove("TargetLocation");
			} else
			{
				pStack.getTag().put("TargetLocation", NbtUtils.writeBlockPos(pos));
			}
			
			if(pEntity.tickCount % 20 == 0)
			{
				pStack.hurt(1, pLevel.random, pEntity instanceof ServerPlayer ? (ServerPlayer) pEntity : null);
				
				if(!isCharged(pStack))
				{
					pStack.getTag().putBoolean("Powered", false);
				}
			}
		}
	}
	
	public boolean checkFuelItem(Player pPlayer, Level pLevel)
	{
		if(fuelItem != null)
		{
			ItemStack fuelStack = new ItemStack(fuelItem.get());
			
			for(ItemStack invItem : pPlayer.getInventory().items)
			{
				if(ItemStack.isSameItem(invItem, fuelStack))
				{
					if(!pLevel.isClientSide)
					{
						invItem.shrink(1);
					}
					pLevel.playSound(pPlayer, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.BLAZE_SHOOT, SoundSource.AMBIENT, 0.4F, 2F);
					return true;
				}
			}
			return false;
		}
		return true;
	}
}
package com.mraof.minestuck.item;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.ClientProxy;
import net.minecraft.ChatFormatting;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelUtils;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.item.CompassItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.function.Supplier;

public class StructureScannerItem extends Item
{
	private final TagKey<Structure> structure;
	private final Supplier<Item> fuelItem;
	private final String scannerName;
	
	public StructureScannerItem(Properties properties, TagKey<Structure> structure, Supplier<Item> fuelItem, String scannerName)
	{
		super(properties);
		this.structure = structure;
		this.fuelItem = fuelItem;
		this.scannerName = scannerName;
		
		/*
		ItemProperties.register(this, new ResourceLocation("angle"),
				new CompassItemPropertyFunction((level, stack, entity) -> stack.hasTag() &&
						stack.getTag().contains("TargetLocation") ? GlobalPos.of(level.dimension(),
						NbtUtils.readBlockPos(stack.getTag().getCompound("TargetPos"))) : null));
		*/
		
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
		pLevel.playSound(pPlayer, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.UI_BUTTON_CLICK.get(), SoundSource.AMBIENT, 0.8F, 1.3F);
		
		ItemStack stack = pPlayer.getItemInHand(pUsedHand);
		
		if(CheckFuelItem(pPlayer, pLevel))
		{
			stack.getOrCreateTag().putBoolean("Powered", !stack.getTag().getBoolean("Powered"));
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
	
	public boolean CheckFuelItem(Player pPlayer, Level pLevel)
	{
		ItemStack fuelStack = new ItemStack(fuelItem.get());
		
		
		if(fuelItem != null)
		{
			for(ItemStack invItem : pPlayer.getInventory().items)
			{
				if(ItemStack.isSameItem(invItem, fuelStack))
				{
					if(!pLevel.isClientSide && pLevel.getRandom().nextFloat() >= 0.95F)
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
package com.mraof.minestuck.inventory.captchalogue;

import com.mraof.minestuck.network.CaptchaDeckPackets;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Objects;

public abstract class Modus
{
	private final ModusType<?> type;
	public final LogicalSide side;
	private boolean needResend;
	
	public Modus(ModusType<?> type, LogicalSide side)
	{
		this.type = Objects.requireNonNull(type);
		this.side = Objects.requireNonNull(side);
	}
	
	/**
	 * This is called when the modus is created without calling readFromNBT(nbt).
	 * Note that this method is used to clear the inventory/size after dropping stuff on death without creating a new instance.
	 */
	public abstract void initModus(ItemStack modusItem, ServerPlayer player, NonNullList<ItemStack> prev, int size);
	
	public abstract void readFromNBT(CompoundTag nbt);
	
	public abstract CompoundTag writeToNBT(CompoundTag nbt);
	
	public abstract boolean putItemStack(ServerPlayer player, ItemStack item);
	
	public abstract NonNullList<ItemStack> getItems();
	
	public int getNonEmptyCards()
	{
		int count = 0;
		for(ItemStack stack : getItems())
			if(!stack.isEmpty())
				count++;
		return count;
	}
	
	public abstract boolean increaseSize(ServerPlayer player);
	
	public abstract ItemStack getItem(ServerPlayer player, int id, boolean asCard);
	
	public abstract boolean canSwitchFrom(Modus modus);
	
	public abstract int getSize();
	
	public void setValue(ServerPlayer player, byte type, int value) {}
	
	public ModusType<?> getType()
	{
		return type;
	}
	
	public ItemStack getModusItem()
	{
		return new ItemStack(getType().getItem());
	}
	
	public Component getName()
	{
		return new ItemStack(type.getItem()).getHoverName();
	}
	
	/**
	 * Should be called every time something within the modus changes.
	 * Marks that the modus data has been changed and need to be saved at next opportunity.
	 */
	public void markDirty()
	{
		needResend = true;
	}
	
	public final void checkAndResend(ServerPlayer player)
	{
		if(needResend)
		{
			PacketDistributor.PLAYER.with(player).send(CaptchaDeckPackets.ModusData.create(this));
			needResend = false;
		}
	}
}

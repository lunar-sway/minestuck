package com.mraof.minestuck.inventory.captchalogue;

import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.data.ModusDataPacket;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.LogicalSide;

import java.util.Objects;

public abstract class Modus
{
	private final PlayerSavedData savedData;
	private final ModusType<?> type;
	public final LogicalSide side;
	private boolean needResend;
	
	public Modus(ModusType<?> type, PlayerSavedData savedData, LogicalSide side)
	{
		this.type = Objects.requireNonNull(type);
		this.savedData = side == LogicalSide.SERVER ? Objects.requireNonNull(savedData) : null;
		this.side = Objects.requireNonNull(side);
	}
	
	/**
	 * This is called when the modus is created without calling readFromNBT(nbt).
	 * Note that this method is used to clear the inventory/size after dropping stuff on death without creating a new instance.
	 */
	public abstract void initModus(ItemStack modusItem, ServerPlayerEntity player, NonNullList<ItemStack> prev, int size);
	
	public abstract void readFromNBT(CompoundNBT nbt);
	
	public abstract CompoundNBT writeToNBT(CompoundNBT nbt);
	
	public abstract boolean putItemStack(ServerPlayerEntity player, ItemStack item);
	
	public abstract NonNullList<ItemStack> getItems();
	
	public int getNonEmptyCards()
	{
		int count = 0;
		for(ItemStack stack : getItems())
			if(!stack.isEmpty())
				count++;
		return count;
	}
	
	public abstract boolean increaseSize(ServerPlayerEntity player);
	
	public abstract ItemStack getItem(ServerPlayerEntity player, int id, boolean asCard);
	
	public abstract boolean canSwitchFrom(Modus modus);
	
	public abstract int getSize();
	
	public void setValue(ServerPlayerEntity player, byte type, int value) {}
	
	public ModusType<?> getType()
	{
		return type;
	}
	
	public ItemStack getModusItem()
	{
		return new ItemStack(getType().getItem());
	}
	
	public ITextComponent getName()
	{
		return new ItemStack(type.getItem()).getHoverName();
	}
	
	/**
	 * Should be called every time something within the modus changes.
	 * Marks that the modus data has been changed and need to be saved at next opportunity.
	 */
	public void markDirty()
	{
		if(savedData != null)
			savedData.setDirty();
		needResend = true;
	}
	
	public final void checkAndResend(ServerPlayerEntity player)
	{
		if(needResend)
		{
			ModusDataPacket packet = ModusDataPacket.create(CaptchaDeckHandler.writeToNBT(this));
			MSPacketHandler.sendToPlayer(packet, player);
			needResend = false;
		}
	}
	
	protected MinecraftServer getServer()
	{
		return savedData != null ? savedData.mcServer : null;
	}
}
package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckMenu;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;

import javax.annotation.Nullable;

public final class CaptchaDeckPackets
{
	public record ModusData(@Nullable CompoundTag nbt) implements MSPacket.PlayToClient
	{
		public static final ResourceLocation ID = Minestuck.id("captcha_deck/modus_data");
		
		public static ModusData create(@Nullable Modus modus)
		{
			return new ModusData(CaptchaDeckHandler.writeToNBT(modus));
		}
		
		@Override
		public ResourceLocation id()
		{
			return ID;
		}
		
		@Override
		public void write(FriendlyByteBuf buffer)
		{
			if(nbt != null)
			{
				buffer.writeNbt(nbt);
			}
		}
		
		public static ModusData read(FriendlyByteBuf buffer)
		{
			if(buffer.readableBytes() > 0)
			{
				CompoundTag nbt = buffer.readNbt();
				return new ModusData(nbt);
			} else return new ModusData(null);
		}
		
		@Override
		public void execute()
		{
			ClientPlayerData.handleDataPacket(this);
		}
	}
	
	public record TriggerModusButton() implements MSPacket.PlayToServer
	{
		public static final ResourceLocation ID = Minestuck.id("captcha_deck/trigger_modus_button");
		
		@Override
		public ResourceLocation id()
		{
			return ID;
		}
		
		@Override
		public void write(FriendlyByteBuf buffer)
		{
		}
		
		public static TriggerModusButton read(FriendlyByteBuf ignored)
		{
			return new TriggerModusButton();
		}
		
		@Override
		public void execute(ServerPlayer player)
		{
			if(ServerEditHandler.isInEditmode(player))
				return;
			
			if(player.containerMenu instanceof CaptchaDeckMenu)
				CaptchaDeckHandler.useItem(player);
		}
	}
	
	public record CaptchalogueHeldItem() implements MSPacket.PlayToServer
	{
		public static final ResourceLocation ID = Minestuck.id("captcha_deck/captchalogue_held_item");
		
		@Override
		public ResourceLocation id()
		{
			return ID;
		}
		
		@Override
		public void write(FriendlyByteBuf buffer)
		{
		}
		
		public static CaptchalogueHeldItem read(FriendlyByteBuf ignored)
		{
			return new CaptchalogueHeldItem();
		}
		
		@Override
		public void execute(ServerPlayer player)
		{
			if(ServerEditHandler.isInEditmode(player))
				return;
			
			if(!player.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty())
				CaptchaDeckHandler.captchalogueItem(player);
		}
	}
	
	public record CaptchalogueInventorySlot(int slotIndex, int windowId) implements MSPacket.PlayToServer
	{
		public static final ResourceLocation ID = Minestuck.id("captcha_deck/captchalogue_inventory_slot");
		
		@Override
		public ResourceLocation id()
		{
			return ID;
		}
		
		@Override
		public void write(FriendlyByteBuf buffer)
		{
			buffer.writeInt(this.slotIndex);
			buffer.writeInt(this.windowId);
		}
		
		public static CaptchalogueInventorySlot read(FriendlyByteBuf buffer)
		{
			int slotIndex = buffer.readInt();
			int windowId = buffer.readInt();
			return new CaptchalogueInventorySlot(slotIndex, windowId);
		}
		
		@Override
		public void execute(ServerPlayer player)
		{
			if(ServerEditHandler.isInEditmode(player))
				return;
			
			CaptchaDeckHandler.captchalogueItemInSlot(player, slotIndex, windowId);
		}
	}
	
	public record GetItem(int itemIndex, boolean asCard) implements MSPacket.PlayToServer
	{
		public static final ResourceLocation ID = Minestuck.id("captcha_deck/get_item");
		
		@Override
		public ResourceLocation id()
		{
			return ID;
		}
		
		@Override
		public void write(FriendlyByteBuf buffer)
		{
			buffer.writeInt(itemIndex);
			buffer.writeBoolean(asCard);
		}
		
		public static GetItem read(FriendlyByteBuf buffer)
		{
			int itemIndex = buffer.readInt();
			boolean asCard = buffer.readBoolean();
			return new GetItem(itemIndex, asCard);
		}
		
		@Override
		public void execute(ServerPlayer player)
		{
			if(ServerEditHandler.isInEditmode(player))
				return;
			
			CaptchaDeckHandler.getItem(player, itemIndex, asCard);
		}
	}
	
	public record SetModusParameter(byte parameterType, int parameterValue) implements MSPacket.PlayToServer
	{
		public static final ResourceLocation ID = Minestuck.id("captcha_deck/set_modus_parameter");
		
		@Override
		public ResourceLocation id()
		{
			return ID;
		}
		
		@Override
		public void write(FriendlyByteBuf buffer)
		{
			buffer.writeByte(this.parameterType);
			buffer.writeInt(this.parameterValue);
		}
		
		public static SetModusParameter read(FriendlyByteBuf buffer)
		{
			byte parameterType = buffer.readByte();
			int parameterValue = buffer.readInt();
			return new SetModusParameter(parameterType, parameterValue);
		}
		
		@Override
		public void execute(ServerPlayer player)
		{
			if(ServerEditHandler.isInEditmode(player))
				return;
			
			Modus modus = CaptchaDeckHandler.getModus(player);
			if(modus != null)
			{
				modus.setValue(player, this.parameterType, this.parameterValue);
				modus.checkAndResend(player);
			}
		}
	}
}

package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckMenu;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import javax.annotation.Nullable;

public final class CaptchaDeckPackets
{
	public record ModusData(@Nullable CompoundTag nbt) implements MSPacket.PlayToClient
	{
		
		public static final Type<ModusData> ID = new Type<>(Minestuck.id("captcha_deck/modus_data"));
		public static final StreamCodec<RegistryFriendlyByteBuf, ModusData> STREAM_CODEC = StreamCodec.composite(
				ByteBufCodecs.COMPOUND_TAG,
				ModusData::nbt,
				ModusData::new
		);
		
		public static ModusData create(@Nullable Modus modus)
		{
			return new ModusData(CaptchaDeckHandler.writeToNBT(modus));
		}
		
		@Override
		public void execute(IPayloadContext context)
		{
			ClientPlayerData.handleDataPacket(this);
		}
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
	}
	
	public record TriggerModusButton() implements MSPacket.PlayToServer
	{
		public static final Type<TriggerModusButton> ID = new Type<>(Minestuck.id("captcha_deck/trigger_modus_button"));
		public static final StreamCodec<RegistryFriendlyByteBuf, TriggerModusButton> STREAM_CODEC = StreamCodec.unit(new TriggerModusButton());
		
		@Override
		public void execute(IPayloadContext context, ServerPlayer player)
		{
			if(ServerEditHandler.isInEditmode(player))
				return;
			
			if(player.containerMenu instanceof CaptchaDeckMenu)
				CaptchaDeckHandler.useItem(player);
		}
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
	}
	
	public record CaptchalogueHeldItem() implements MSPacket.PlayToServer
	{
		
		public static final Type<CaptchalogueHeldItem> ID = new Type<>(Minestuck.id("captcha_deck/captchalogue_held_item"));
		public static final StreamCodec<RegistryFriendlyByteBuf, CaptchalogueHeldItem> STREAM_CODEC = StreamCodec.unit(new CaptchalogueHeldItem());
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context, ServerPlayer player)
		{
			if(ServerEditHandler.isInEditmode(player))
				return;
			
			if(!player.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty())
				CaptchaDeckHandler.captchalogueItem(player);
		}
	}
	
	public record CaptchalogueInventorySlot(int slotIndex, int windowId) implements MSPacket.PlayToServer
	{
		
		public static final Type<CaptchalogueInventorySlot> ID = new Type<>(Minestuck.id("captcha_deck/captchalogue_inventory_slot"));
		public static final StreamCodec<RegistryFriendlyByteBuf, CaptchalogueInventorySlot> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.INT,
			CaptchalogueInventorySlot::slotIndex,
			ByteBufCodecs.INT,
			CaptchalogueInventorySlot::windowId,
			CaptchalogueInventorySlot::new	
		);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		
		@Override
		public void execute(IPayloadContext context, ServerPlayer player)
		{
			if(ServerEditHandler.isInEditmode(player))
				return;
			
			CaptchaDeckHandler.captchalogueItemInSlot(player, slotIndex, windowId);
		}
	}
	
	public record GetItem(int itemIndex, boolean asCard) implements MSPacket.PlayToServer
	{
		public static final Type<GetItem> ID = new Type<>(Minestuck.id("captcha_deck/get_item"));
		public static final StreamCodec<FriendlyByteBuf, GetItem> STREAM_CODEC = StreamCodec.composite(
				ByteBufCodecs.INT,
				GetItem::itemIndex,
				ByteBufCodecs.BOOL,
				GetItem::asCard,
				GetItem::new
		);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context, ServerPlayer player)
		{
			if(ServerEditHandler.isInEditmode(player))
				return;
			
			CaptchaDeckHandler.getItem(player, itemIndex, asCard);
		}
	}
	
	public record SetModusParameter(byte parameterType, int parameterValue) implements MSPacket.PlayToServer
	{
		
		public static final Type<SetModusParameter> ID = new Type<>(Minestuck.id("captcha_deck/set_modus_parameter"));
		public static final StreamCodec<FriendlyByteBuf, SetModusParameter> STREAM_CODEC = StreamCodec.composite(
				ByteBufCodecs.BYTE,
				SetModusParameter::parameterType,
				ByteBufCodecs.INT,
				SetModusParameter::parameterValue,
				SetModusParameter::new
		);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context, ServerPlayer player)
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

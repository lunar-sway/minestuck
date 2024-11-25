package com.mraof.minestuck.item.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.item.components.MSItemComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Optional;

public class TransportalizerItem extends BlockItem
{
	public TransportalizerItem(Block blockIn, Properties builder)
	{
		super(blockIn, builder);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flagIn)
	{
		TransportalizerData data = stack.get(MSItemComponents.TRANSPORTALIZER_DATA);
		if (data == null)
			return;
		
		if (data.locked()) {
			tooltip.add(Component.translatable("block.minestuck.transportalizer.locked_message").withStyle(ChatFormatting.GRAY));
		} else {
			data.id().ifPresent(id ->
					tooltip.add(Component.translatable("block.minestuck.transportalizer.idString", id).withStyle(ChatFormatting.GRAY)));
			data.destinationId().ifPresent(destId ->
					tooltip.add(Component.translatable("block.minestuck.transportalizer.destId", destId).withStyle(ChatFormatting.GRAY)));
		}
	}
	
	public record TransportalizerData(Optional<String> id, Optional<String> destinationId, boolean locked)
	{
		public static final Codec<TransportalizerData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.STRING.optionalFieldOf("id").forGetter(TransportalizerData::id),
				Codec.STRING.optionalFieldOf("destination_id").forGetter(TransportalizerData::destinationId),
				Codec.BOOL.fieldOf("locked").forGetter(TransportalizerData::locked)
		).apply(instance, TransportalizerData::new));
		
		public static final StreamCodec<FriendlyByteBuf, TransportalizerData> STREAM_CODEC = StreamCodec.composite(
				ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8),
				TransportalizerData::id,
				ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8),
				TransportalizerData::destinationId,
				ByteBufCodecs.BOOL,
				TransportalizerData::locked,
				TransportalizerData::new
		);
	}
}

package com.mraof.minestuck.item.components;

import java.util.Optional;
import java.util.function.Consumer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.inventory.musicplayer.CassetteSong;
import com.mraof.minestuck.inventory.musicplayer.CassetteSongs;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.HolderLookup.RegistryLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.item.component.TooltipProvider;

public record CassettePlayable(ResourceLocation song, boolean showInTooltip) implements TooltipProvider {
    public static final Codec<CassettePlayable> CODEC = RecordCodecBuilder.create(builder -> builder.group(
        ResourceLocation.CODEC.fieldOf("song").forGetter(CassettePlayable::song),
        Codec.BOOL.optionalFieldOf("show_in_tooltip", true).forGetter(CassettePlayable::showInTooltip)
    ).apply(builder, CassettePlayable::new));
    public static final StreamCodec<FriendlyByteBuf, CassettePlayable> STREAM_CODEC = StreamCodec.composite(
        ResourceLocation.STREAM_CODEC, CassettePlayable::song,
        ByteBufCodecs.BOOL, CassettePlayable::showInTooltip,
        CassettePlayable::new
    );
    
    public CassettePlayable(ResourceLocation song) {
        this(song, true);
    }
    
    @Override
    public void addToTooltip(TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag tooltipFlag) {
        if (!this.showInTooltip) return;
        
        Optional<RegistryLookup<JukeboxSong>> ojukeboxRegistry = context.registries().lookup(Registries.JUKEBOX_SONG);
        if (ojukeboxRegistry.isEmpty()) return;
        RegistryLookup<JukeboxSong> jukeboxRegistry = ojukeboxRegistry.get();
        
        Optional<CassetteSong> osong = CassetteSongs.getInstance().findSong(song);
        if (osong.isEmpty()) return;
        CassetteSong song = osong.get();
        
        Optional<Reference<JukeboxSong>> ojukeboxSong = jukeboxRegistry.get(song.getJukeboxSong());
        if (ojukeboxSong.isEmpty()) return;
        tooltipAdder.accept(ojukeboxSong.get().value().description().copy().withStyle(ChatFormatting.GRAY));
    }
    
    public CassettePlayable withTooltip(boolean showInTooltip) {
        return new CassettePlayable(song, showInTooltip);
    }
}

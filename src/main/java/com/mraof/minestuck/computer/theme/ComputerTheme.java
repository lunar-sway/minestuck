package com.mraof.minestuck.computer.theme;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.Minestuck;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

/**
 * The combination of theme data and the id name of the theme.
 */
public record ComputerTheme(ResourceLocation id, Data data)
{
	/**
	 * Represents the json data of a theme
	 * <p>
	 * @param texturePath The background texture of the theme
	 * @param textColor Color of the text
	 * @param buttonPath If provided, the texture for clickable buttons
	 * @param buttonDisabledPath If provided, the texture for unclickable buttons
	 * @param buttonHighlitedPath If provided, the texture for focused buttons (hovering/pressing tab)
	 */
	public record Data(ResourceLocation texturePath, int textColor, Optional<ResourceLocation> buttonPath, Optional<ResourceLocation> buttonDisabledPath, Optional<ResourceLocation> buttonHighlitedPath)
	{
		public static final Codec<Data> CODEC = RecordCodecBuilder.create(instance ->
				instance.group(
						ResourceLocation.CODEC.fieldOf("texture_location").forGetter(Data::texturePath),
						Codec.INT.fieldOf("text_color").forGetter(Data::textColor),
						ResourceLocation.CODEC.optionalFieldOf("button_location").forGetter(Data::buttonPath),
						ResourceLocation.CODEC.optionalFieldOf("button_disabled_location").forGetter(Data::buttonDisabledPath),
						ResourceLocation.CODEC.optionalFieldOf("button_highlighted_location").forGetter(Data::buttonHighlitedPath)
				).apply(instance, Data::new));
		
		public Data(ResourceLocation texturePath, int textColor)
		{
			this(texturePath, textColor, Optional.empty(), Optional.empty(), Optional.empty());
		}
		
		public static final Data DEFAULT = new Data(ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, "textures/gui/theme/default.png"), 0x404040);
	}
	
	public MutableComponent name()
	{
		return Component.translatable(translationKeyFromId(this.id));
	}
	
	public static String translationKeyFromId(ResourceLocation themeId)
	{
		return "theme." + themeId.getNamespace() + "." + themeId.getPath();
	}
}

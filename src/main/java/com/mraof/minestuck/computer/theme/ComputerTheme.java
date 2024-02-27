package com.mraof.minestuck.computer.theme;

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
	 * Represents the json data of a theme, which determine what wallpaper and text color appears on in-game computers.
	 */
	public record Data(ResourceLocation texturePath, int textColor)
	{
		public static final Codec<Data> CODEC = RecordCodecBuilder.create(instance ->
				instance.group(
						ResourceLocation.CODEC.fieldOf("texture_location").forGetter(Data::texturePath),
						Codec.INT.fieldOf("text_color").forGetter(Data::textColor)
				).apply(instance, Data::new));
		
		public static final Data DEFAULT = new Data(new ResourceLocation(Minestuck.MOD_ID, "textures/gui/theme/default.png"), 0x404040);
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

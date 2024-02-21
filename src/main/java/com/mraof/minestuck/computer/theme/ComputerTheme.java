package com.mraof.minestuck.computer.theme;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.Minestuck;
import net.minecraft.resources.ResourceLocation;

/**
 * This class is used to define json files that will be used to determine what wallpaper and text color appears on in-game computers.
 */
public record ComputerTheme(ResourceLocation texturePath, int textColor)
{
	public static final Codec<ComputerTheme> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
					ResourceLocation.CODEC.fieldOf("texture_location").forGetter(ComputerTheme::texturePath),
					Codec.INT.fieldOf("text_color").forGetter(ComputerTheme::textColor)
			).apply(instance, ComputerTheme::new));
	
	public static final int DEFAULT_TEXT_COLOR = 0x404040;
	public static final ResourceLocation DEFAULT_TEXTURE_PATH = new ResourceLocation(Minestuck.MOD_ID, "textures/gui/theme/default.png");
	
	public static String translationKeyFromId(ResourceLocation themeId)
	{
		return "theme." + themeId.getNamespace() + "." + themeId.getPath();
	}
}

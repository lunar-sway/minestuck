package com.mraof.minestuck.entity.dialogue;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.PreservingOptionalFieldCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record DialogueAnimationData(String emotion, int spriteHeight, int spriteWidth, int xOffset, int yOffset, float scale)
{
	public static final String GENERIC_EMOTION = "generic";
	public static final String PLEASED_EMOTION = "pleased";
	public static final String UPSET_EMOTION = "upset";
	public static final String SCARED_EMOTION = "scared";
	
	public static final int DEFAULT_SPRITE_WIDTH = 128;
	public static final int DEFAULT_SPRITE_HEIGHT = 128;
	
	public static final DialogueAnimationData DEFAULT_ANIMATION = new DialogueAnimationData(GENERIC_EMOTION, DEFAULT_SPRITE_HEIGHT, DEFAULT_SPRITE_WIDTH, 0, 0, 1.0F);
	
	public static Codec<DialogueAnimationData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			PreservingOptionalFieldCodec.withDefault(Codec.STRING, "emotion", GENERIC_EMOTION).forGetter(DialogueAnimationData::emotion),
			PreservingOptionalFieldCodec.withDefault(Codec.INT, "height", DEFAULT_SPRITE_HEIGHT).forGetter(DialogueAnimationData::spriteHeight),
			PreservingOptionalFieldCodec.withDefault(Codec.INT, "width", DEFAULT_SPRITE_WIDTH).forGetter(DialogueAnimationData::spriteWidth),
			PreservingOptionalFieldCodec.withDefault(Codec.INT, "x_offset", 0).forGetter(DialogueAnimationData::xOffset),
			PreservingOptionalFieldCodec.withDefault(Codec.INT, "y_offset", 0).forGetter(DialogueAnimationData::yOffset),
			PreservingOptionalFieldCodec.withDefault(Codec.FLOAT, "scale", 1.0F).forGetter(DialogueAnimationData::scale)
	).apply(instance, DialogueAnimationData::new));
	
	public static DialogueAnimationData read(FriendlyByteBuf buffer)
	{
		String emotion = buffer.readUtf(25);
		int height = buffer.readInt();
		int width = buffer.readInt();
		int xOffset = buffer.readInt();
		int yOffset = buffer.readInt();
		float scale = buffer.readFloat();
		
		return new DialogueAnimationData(emotion, height, width, xOffset, yOffset, scale);
	}
	
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeUtf(this.emotion, 25);
		buffer.writeInt(this.spriteHeight);
		buffer.writeInt(this.spriteWidth);
		buffer.writeInt(this.xOffset);
		buffer.writeInt(this.yOffset);
		buffer.writeFloat(this.scale);
	}
	
	/**
	 * Returns the animatable sprite corresponding to the entities sprite type
	 */
	public ResourceLocation getRenderPath(String spriteType)
	{
		ResourceLocation spritePath = new ResourceLocation(Minestuck.MOD_ID, "textures/gui/dialogue/entity/" + spriteType + "/" + emotion + ".png");
		
		//TODO the first time this is run using an invalid path, the Minecraft missing texture sprite appears briefly before fallback system activates. An error message is printed in chat
		AbstractTexture abstractTexture = Minecraft.getInstance().getTextureManager().getTexture(spritePath);
		
		//if the sprite for the given emotion cannot be found, it will try to render the generic sprite instead. If the generic sprite cannot be found, the invalid texture is allowed to proceed
		if(abstractTexture == MissingTextureAtlasSprite.getTexture())
		{
			ResourceLocation fallbackSpritePath = new ResourceLocation(Minestuck.MOD_ID, "textures/gui/dialogue/entity/" + spriteType + "/" + GENERIC_EMOTION + ".png");
			if(Minecraft.getInstance().getTextureManager().getTexture(fallbackSpritePath) instanceof SimpleTexture)
			{
				return fallbackSpritePath;
			} else
				return spritePath;
		}
		
		return spritePath;
	}
}
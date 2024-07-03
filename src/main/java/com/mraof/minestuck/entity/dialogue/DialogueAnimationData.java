package com.mraof.minestuck.entity.dialogue;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.Minestuck;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;

public record DialogueAnimationData(String emotion, int spriteHeight, int spriteWidth, int xOffset, int yOffset, float scale)
{
	public static final String GENERIC_EMOTION = "generic";
	public static final String HAPPY_EMOTION = "happy";
	public static final String ANGRY_EMOTION = "angry";
	public static final String ANXIOUS_EMOTION = "anxious";
	
	public static final int DEFAULT_SPRITE_WIDTH = 224;
	public static final int DEFAULT_SPRITE_HEIGHT = 224;
	
	public static final DialogueAnimationData DEFAULT_ANIMATION = new DialogueAnimationData(GENERIC_EMOTION, DEFAULT_SPRITE_HEIGHT, DEFAULT_SPRITE_WIDTH, 0, 0, 1.0F);
	
	public static Codec<DialogueAnimationData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ExtraCodecs.strictOptionalField(Codec.STRING, "emotion", GENERIC_EMOTION).forGetter(DialogueAnimationData::emotion),
			ExtraCodecs.strictOptionalField(Codec.INT, "height", DEFAULT_SPRITE_HEIGHT).forGetter(DialogueAnimationData::spriteHeight),
			ExtraCodecs.strictOptionalField(Codec.INT, "width", DEFAULT_SPRITE_WIDTH).forGetter(DialogueAnimationData::spriteWidth),
			ExtraCodecs.strictOptionalField(Codec.INT, "x_offset", 0).forGetter(DialogueAnimationData::xOffset),
			ExtraCodecs.strictOptionalField(Codec.INT, "y_offset", 0).forGetter(DialogueAnimationData::yOffset),
			ExtraCodecs.strictOptionalField(Codec.FLOAT, "scale", 1.0F).forGetter(DialogueAnimationData::scale)
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
		
		//TODO the first time this is run using an invalid path, an error message is printed in chat before fallback system activates. The Minecraft missing texture sprite may show briefly
		AbstractTexture abstractTexture = Minecraft.getInstance().getTextureManager().getTexture(spritePath);
		
		//if the sprite for the given emotion cannot be found, it will try to render the generic sprite instead. If the generic sprite cannot be found, the invalid texture is allowed to proceed
		if(abstractTexture == MissingTextureAtlasSprite.getTexture())
		{
			return new ResourceLocation(Minestuck.MOD_ID, "textures/gui/dialogue/entity/" + spriteType + "/" + GENERIC_EMOTION + ".png");
		}
		
		return spritePath;
	}
}

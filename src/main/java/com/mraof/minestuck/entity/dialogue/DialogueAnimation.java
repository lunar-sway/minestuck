package com.mraof.minestuck.entity.dialogue;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.PreservingOptionalFieldCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import software.bernie.geckolib.cache.texture.AnimatableTexture;

import java.util.Locale;

public record DialogueAnimation(String emotion)
{
	public static final DialogueAnimation DEFAULT_ANIMATION = new DialogueAnimation(Emotion.GENERIC.getSerializedName());
	
	public static final int DEFAULT_SPRITE_WIDTH = 128;
	public static final int DEFAULT_SPRITE_HEIGHT = 128;
	
	public static Codec<DialogueAnimation> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			PreservingOptionalFieldCodec.withDefault(Codec.STRING, "emotion", Emotion.GENERIC.getSerializedName()).forGetter(DialogueAnimation::emotion)
	).apply(instance, DialogueAnimation::new));
	
	public static DialogueAnimation read(FriendlyByteBuf buffer)
	{
		String emotion = buffer.readUtf(25);
		
		return new DialogueAnimation(emotion);
	}
	
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeUtf(this.emotion);
	}
	
	/**
	 * Returns the animatable sprite corresponding to the entities sprite type
	 */
	public ResourceLocation getRenderPath(DialogueEntity entity)
	{
		ResourceLocation spritePath = new ResourceLocation(Minestuck.MOD_ID, "textures/gui/dialogue/entity/" + entity.getSpriteType() + "/" + emotion + ".png");
		
		//TODO the first time this is run using an invalid path, the Minecraft missing texture sprite appears briefly before fallback system activates. An error message is printed in chat
		AbstractTexture abstractTexture = Minecraft.getInstance().getTextureManager().getTexture(spritePath);
		
		//if the sprite for the given emotion cannot be found, it will try to render the generic sprite instead. If the generic sprite cannot be found, the invalid texture is allowed to proceed
		if(!(abstractTexture instanceof SimpleTexture))
		{
			ResourceLocation fallbackSpritePath = new ResourceLocation(Minestuck.MOD_ID, "textures/gui/dialogue/entity/" + entity.getSpriteType() + "/" + Emotion.GENERIC.getSerializedName() + ".png");
			if(Minecraft.getInstance().getTextureManager().getTexture(fallbackSpritePath) instanceof SimpleTexture fallbackTexture)
			{
				ensureAnimatable(fallbackTexture, fallbackSpritePath);
				return fallbackSpritePath;
			} else
				return spritePath;
		}
		
		ensureAnimatable(abstractTexture, spritePath);
		
		return spritePath;
	}
	
	private static void ensureAnimatable(AbstractTexture abstractTexture, ResourceLocation sprite)
	{
		//if the texture is not loaded as an AnimatableTexture, create a new AnimatableTexture and register it
		if(!(abstractTexture instanceof AnimatableTexture))
		{
			AnimatableTexture animatableTexture = new AnimatableTexture(sprite);
			Minecraft.getInstance().getTextureManager().register(sprite, animatableTexture);
		}
	}
	
	/**
	 * List of supported/used emotion types. Custom emotions can still be declared through datapacks
	 */
	public enum Emotion implements StringRepresentable
	{
		GENERIC,
		PLEASED,
		UPSET,
		SCARED,
		ANXIOUS;
		
		@Override
		public String getSerializedName()
		{
			return name().toLowerCase(Locale.ROOT);
		}
	}
}
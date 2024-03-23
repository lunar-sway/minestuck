package com.mraof.minestuck.entity.dialogue;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.PreservingOptionalFieldCodec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public record DialogueAnimation(String emotion/*, int frames, float speed, @Nullable ResourceLocation overridePath*/)
{
	//public static final ResourceLocation FALLBACK_ANIMATION_PATH = new ResourceLocation(Minestuck.MOD_ID, "textures/gui/dialogue/fallback_animation.png");
	public static final ResourceLocation FALLBACK_ANIMATION_PATH = new ResourceLocation(Minestuck.MOD_ID, "textures/entity/vitality_gel.png");
	
	//public static final DialogueAnimation DEFAULT_ANIMATION = new DialogueAnimation(Emotion.GENERIC.getSerializedName(), 1, 1.0F, null);
	public static final DialogueAnimation DEFAULT_ANIMATION = new DialogueAnimation(Emotion.GENERIC.getSerializedName());
	
	public static Codec<DialogueAnimation> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			PreservingOptionalFieldCodec.withDefault(Codec.STRING, "emotion", Emotion.GENERIC.getSerializedName()).forGetter(DialogueAnimation::emotion)
			//PreservingOptionalFieldCodec.withDefault(Codec.INT, "frames", 1).forGetter(DialogueAnimation::frames),
			//PreservingOptionalFieldCodec.withDefault(Codec.FLOAT, "speed", 1.0F).forGetter(DialogueAnimation::speed),
			//PreservingOptionalFieldCodec.withDefault(ResourceLocation.CODEC, "overridePath", Dialogue.FALLBACK_ANIMATION_PATH).forGetter(DialogueAnimation::overridePath)
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
	 * Uses the emotion information to determine
	 */
	public ResourceLocation getRenderPath(LivingEntity entity)
	{
		//ResourceLocation attemptedLocation = new ResourceLocation(Minestuck.MOD_ID, "textures/gui/dialogue/" + entity.getType() + "/" + emotion + ".png");
		
		return FALLBACK_ANIMATION_PATH;
	}
	
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
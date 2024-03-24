package com.mraof.minestuck.data.dialogue;

import com.mraof.minestuck.entity.dialogue.Dialogue;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Helper class for building multiple connected dialogue nodes meant to go in the same folder.
 * This is suitable for branching multi-node dialogue.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class FolderedDialogue implements DialogueProvider.DialogueProducer
{
	private final Consumer<Builder> builderConsumer;
	
	public FolderedDialogue(Consumer<Builder> builderConsumer)
	{
		this.builderConsumer = builderConsumer;
	}
	
	@Override
	public ResourceLocation buildAndRegister(ResourceLocation id, BiConsumer<ResourceLocation, Dialogue.NodeSelector> register)
	{
		Builder builder = new Builder(id, register);
		this.builderConsumer.accept(builder);
		Objects.requireNonNull(builder.dialogueStart, "Dialogue start must be set for dialogue " + id);
		return builder.dialogueStart.buildAndRegister(builder.startId(), register);
	}
	
	public static final class Builder
	{
		private final ResourceLocation baseId;
		private final BiConsumer<ResourceLocation, Dialogue.NodeSelector> register;
		@Nullable
		private DialogueProvider.DialogueProducer dialogueStart;
		
		private Builder(ResourceLocation baseId, BiConsumer<ResourceLocation, Dialogue.NodeSelector> register)
		{
			this.baseId = baseId;
			this.register = register;
		}
		
		public ResourceLocation id()
		{
			return this.baseId;
		}
		
		public ResourceLocation startId()
		{
			return this.baseId.withSuffix("/start");
		}
		
		public void addStart(DialogueProvider.DialogueProducer dialogue)
		{
			this.dialogueStart = dialogue;
		}
		
		public ResourceLocation add(String key, DialogueProvider.DialogueProducer dialogue)
		{
			return dialogue.buildAndRegister(this.baseId.withSuffix("/" + key), this.register);
		}
	}
}

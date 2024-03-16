package com.mraof.minestuck.data;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.entity.dialogue.Dialogue;
import com.mraof.minestuck.entity.dialogue.RandomlySelectableDialogue;
import com.mraof.minestuck.entity.dialogue.condition.Condition;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.LanguageProvider;

import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class SelectableDialogueProvider extends DialogueProvider
{
	private final Map<ResourceLocation, Dialogue.SelectableDialogue> selectableDialogueMap = new HashMap<>();
	private final RandomlySelectableDialogue.DialogueCategory category;
	
	public SelectableDialogueProvider(String modId, RandomlySelectableDialogue.DialogueCategory category, PackOutput output, LanguageProvider languageProvider)
	{
		super(modId, output, languageProvider);
		this.category = category;
	}
	
	@SuppressWarnings("unused")
	public static SelectableBuilder weighted(int weight, Condition condition)
	{
		return new SelectableBuilder(condition, weight);
	}
	
	public static SelectableBuilder defaultWeight(Condition condition)
	{
		return new SelectableBuilder(condition, Dialogue.SelectableDialogue.DEFAULT_WEIGHT);
	}
	
	protected final void addRandomlySelectable(String path, SelectableBuilder selectable, DialogueBuilder builder)
	{
		ResourceLocation dialogueId = this.add(path, builder);
		this.selectableDialogueMap.put(new ResourceLocation(this.modId, path), selectable.build(dialogueId));
	}
	
	@Override
	public CompletableFuture<?> run(CachedOutput cache)
	{
		List<CompletableFuture<?>> futures = new ArrayList<>(this.selectableDialogueMap.size() + 1);
		futures.add(super.run(cache));
		
		Path outputPath = output.getOutputFolder();
		for(Map.Entry<ResourceLocation, Dialogue.SelectableDialogue> entry : this.selectableDialogueMap.entrySet())
		{
			Path selectablePath = outputPath.resolve("data/" + entry.getKey().getNamespace() + "/" + this.category.folderNameForSelectable() + "/" + entry.getKey().getPath() + ".json");
			JsonElement selectableJson = Dialogue.SelectableDialogue.CODEC.encodeStart(JsonOps.INSTANCE, entry.getValue()).getOrThrow(false, LOGGER::error);
			futures.add(DataProvider.saveStable(cache, selectableJson, selectablePath));
		}
		
		return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
	}
	
	public static final class SelectableBuilder {
		private final Condition condition;
		private final int weight;
		private boolean keepOnReset = false;
		
		public SelectableBuilder(Condition condition, int weight)
		{
			this.condition = condition;
			this.weight = weight;
		}
		
		public SelectableBuilder keepOnReset()
		{
			this.keepOnReset = true;
			return this;
		}
		
		public Dialogue.SelectableDialogue build(ResourceLocation dialogueId)
		{
			return new Dialogue.SelectableDialogue(dialogueId, this.condition, this.weight, this.keepOnReset);
		}
	}
}

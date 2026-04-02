package com.mraof.minestuck.data.dialogue;

import com.mraof.minestuck.entity.dialogue.Dialogue;
import com.mraof.minestuck.entity.dialogue.RandomlySelectableDialogue;
import com.mraof.minestuck.entity.dialogue.condition.Condition;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * A provider for building dialogue nodes and generating the file making them selectable within a specific {@link RandomlySelectableDialogue.DialogueCategory}.
 * Includes a {@link DialogueProvider} accessible through {@link SelectableDialogueProvider#dialogue()}.
 *
 * @see DialogueProvider
 * @see DialogueLangHelper
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class SelectableDialogueProvider implements DataProvider
{
	private final DialogueProvider dialogueProvider;
	private final Map<ResourceLocation, Dialogue.SelectableDialogue> selectableDialogueMap = new HashMap<>();
	
	private final String modId;
	private final RandomlySelectableDialogue.DialogueCategory category;
	private final CompletableFuture<HolderLookup.Provider> lookupProvider;
	private final PackOutput output;
	
	public SelectableDialogueProvider(String modId, RandomlySelectableDialogue.DialogueCategory category, CompletableFuture<HolderLookup.Provider> lookup, PackOutput output)
	{
		this.dialogueProvider = new DialogueProvider(modId, category.folderName(), lookup, output);
		this.modId = modId;
		this.category = category;
		this.lookupProvider = lookup;
		this.output = output;
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
	
	public void addRandomlySelectable(String path, SelectableBuilder selectable, DialogueProvider.DialogueProducer builder)
	{
		addRandomlySelectable(path, selectable, dialogueProvider.add(path, builder));
	}
	
	public void addRandomlySelectable(String path, SelectableBuilder selectable, ResourceLocation dialogueId)
	{
		this.selectableDialogueMap.put(ResourceLocation.fromNamespaceAndPath(this.modId, path), selectable.build(dialogueId));
	}
	
	public DialogueProvider dialogue()
	{
		return this.dialogueProvider;
	}
	
	@Override
	public CompletableFuture<?> run(CachedOutput cache)
	{
		return lookupProvider.thenCompose(provider -> {
			List<CompletableFuture<?>> futures = new ArrayList<>(this.selectableDialogueMap.size() + 1);
			futures.add(dialogueProvider.run(cache));
			
			Set<ResourceLocation> missingDialogue = this.selectableDialogueMap.values().stream().map(Dialogue.SelectableDialogue::dialogueId)
					.filter(id -> !dialogueProvider.hasAddedDialogue(id)).collect(Collectors.toSet());
			if(!missingDialogue.isEmpty())
				throw new IllegalStateException("Some referenced dialogue is missing: " + missingDialogue);
			
			Path outputPath = output.getOutputFolder();
			for(Map.Entry<ResourceLocation, Dialogue.SelectableDialogue> entry : this.selectableDialogueMap.entrySet())
			{
				Path selectablePath = outputPath.resolve("data/" + entry.getKey().getNamespace() + "/" + this.category.folderNameForSelectable() + "/" + entry.getKey().getPath() + ".json");
				futures.add(DataProvider.saveStable(cache, provider, Dialogue.SelectableDialogue.CODEC, entry.getValue(), selectablePath));
			}
			
			return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
		});
	}
	
	public static final class SelectableBuilder
	{
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
	
	@Override
	public String getName()
	{
		return "Selectable dialogue provider: " + this.category.folderName();
	}
}

package com.mraof.minestuck.data.dialogue;

import com.mraof.minestuck.entity.dialogue.Dialogue;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

/**
 * A helper for building dialogue that is chained together in a list.
 * The first node is suffixed with "1", the next with "2" and so on.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class ChainBuilder implements DialogueProvider.DialogueProducer, DialogueProvider.NodeProducer
{
	private boolean withFolders = false;
	private final List<DialogueProvider.NodeBuilder> nodes = new ArrayList<>();
	private boolean loop = false;
	
	public ChainBuilder withFolders()
	{
		this.withFolders = true;
		return this;
	}
	
	public ChainBuilder node(DialogueProvider.NodeBuilder nodeBuilder)
	{
		this.nodes.add(nodeBuilder);
		return this;
	}
	
	public ChainBuilder loop()
	{
		this.loop = true;
		return this;
	}
	
	private <T> T build(ResourceLocation id, BiConsumer<ResourceLocation, Dialogue.NodeSelector> register, BiFunction<DialogueProvider.NodeBuilder, ResourceLocation, T> startNodeBuilder)
	{
		if(this.nodes.isEmpty())
			throw new IllegalStateException("Nodes must be added to this chain builder");
		
		List<ResourceLocation> ids = IntStream.range(0, this.nodes.size())
				.mapToObj(index -> id.withSuffix((withFolders ? "/" : ".") + (index + 1))).toList();
		
		for(int index = 1; index < this.nodes.size(); index++)
			this.nodes.get(index - 1).next(ids.get(index));
		if(this.loop)
			this.nodes.get(this.nodes.size() - 1).next(ids.get(0));
		
		for(int index = 1; index < this.nodes.size(); index++)
			this.nodes.get(index).buildAndRegister(ids.get(index), register);
		return startNodeBuilder.apply(this.nodes.get(0), ids.get(0));
	}
	
	@Override
	public Dialogue.Node buildNode(ResourceLocation id, BiConsumer<ResourceLocation, Dialogue.NodeSelector> register)
	{
		return this.build(id, register, (startNode, startId) -> startNode.buildNode(startId, register));
	}
	
	@Override
	public ResourceLocation buildAndRegister(ResourceLocation id, BiConsumer<ResourceLocation, Dialogue.NodeSelector> register)
	{
		return this.build(id, register, (startNode, startId) -> startNode.buildAndRegister(startId, register));
	}
}

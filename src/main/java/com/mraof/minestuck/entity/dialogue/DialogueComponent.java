package com.mraof.minestuck.entity.dialogue;

import com.mojang.datafixers.util.Pair;
import com.mraof.minestuck.network.DialogueScreenPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.util.MSCapabilities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.*;

public final class DialogueComponent
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final LivingEntity entity;
	@Nullable
	private ResourceLocation activeDialogue;
	private final Map<UUID, ResourceLocation> dialogueEntrypoint = new HashMap<>();
	private boolean keepOnReset;
	private boolean hasGeneratedOnce = false;
	private final Map<UUID, Set<String>> playerSpecificFlags = new HashMap<>();
	private final Map<UUID, Item> matchedItem = new HashMap<>();
	private final Map<UUID, Dialogue.NodeReference> currentNodeForPlayer = new HashMap<>();
	
	public DialogueComponent(LivingEntity entity)
	{
		this.entity = entity;
	}
	
	public void read(CompoundTag tag)
	{
		if(tag.contains("dialogue_id", CompoundTag.TAG_STRING))
		{
			this.activeDialogue = ResourceLocation.tryParse(tag.getString("dialogue_id"));
			this.keepOnReset = tag.getBoolean("keep_on_reset");
			this.hasGeneratedOnce = true;
			this.playerSpecificFlags.clear();
			tag.getList("player_flags", Tag.TAG_COMPOUND).stream().map(CompoundTag.class::cast).forEach(entryTag -> {
				
				UUID player = entryTag.getUUID("player");
				Set<String> flags = this.playerFlags(player);
				entryTag.getList("flags", Tag.TAG_STRING).stream().map(StringTag.class::cast)
						.map(StringTag::getAsString).forEach(flags::add);
			});
		}
		else
			this.hasGeneratedOnce = tag.getBoolean("has_generated");
	}
	
	public CompoundTag write()
	{
		CompoundTag tag = new CompoundTag();
		if(this.activeDialogue != null)
		{
			tag.putString("dialogue_id", this.activeDialogue.toString());
			tag.putBoolean("keep_on_reset", this.keepOnReset);
			ListTag playerFlagsTag = new ListTag();
			this.playerSpecificFlags.forEach((player, flags) -> {
				CompoundTag entryTag = new CompoundTag();
				entryTag.putUUID("player", player);
				ListTag flagsTag = new ListTag();
				flags.stream().map(StringTag::valueOf).forEach(flagsTag::add);
				entryTag.put("flags", flagsTag);
				playerFlagsTag.add(entryTag);
			});
			tag.put("player_flags", playerFlagsTag);
		}
		
		tag.putBoolean("has_generated", this.hasGeneratedOnce);
		
		return tag;
	}
	
	public boolean hasGeneratedOnce()
	{
		return hasGeneratedOnce;
	}
	
	public void setDialogue(Dialogue.SelectableDialogue selectable)
	{
		this.setDialogue(selectable.dialogueId(), selectable.keepOnReset());
	}
	
	public void setDialogue(ResourceLocation dialogueId, boolean keepOnReset)
	{
		this.hasGeneratedOnce = true;
		this.activeDialogue = dialogueId;
		this.keepOnReset = keepOnReset;
	}
	
	public Optional<ResourceLocation> getActiveDialogue()
	{
		return Optional.ofNullable(this.activeDialogue);
	}
	
	public void setDialogueForPlayer(ServerPlayer player, ResourceLocation dialogueId)
	{
		this.dialogueEntrypoint.put(player.getUUID(), dialogueId);
	}
	
	public Optional<ResourceLocation> getDialogueForPlayer(ServerPlayer player)
	{
		return Optional.ofNullable(this.dialogueEntrypoint.getOrDefault(player.getUUID(), this.activeDialogue));
	}
	
	public boolean hasActiveDialogue()
	{
		return this.activeDialogue != null;
	}
	
	public Set<String> playerFlags(ServerPlayer player)
	{
		return this.playerFlags(player.getUUID());
	}
	
	private Set<String> playerFlags(UUID player)
	{
		return this.playerSpecificFlags.computeIfAbsent(player, _player -> new HashSet<>());
	}
	
	public Optional<Item> getMatchedItem(ServerPlayer player)
	{
		return Optional.ofNullable(this.matchedItem.get(player.getUUID()));
	}
	
	public void setMatchedItem(Item item, ServerPlayer player)
	{
		this.matchedItem.put(player.getUUID(), item);
	}
	
	public void clearMatchedItem(ServerPlayer player)
	{
		this.matchedItem.remove(player.getUUID());
	}
	
	public void resetDialogue()
	{
		if(!this.keepOnReset)
			this.activeDialogue = null;
		this.dialogueEntrypoint.clear();
		this.playerSpecificFlags.clear();
		this.matchedItem.clear();
		this.currentNodeForPlayer.clear();	//todo potentially send a packet to close the screen?
	}
	
	public void tryStartDialogue(ServerPlayer player)
	{
		Optional<ResourceLocation> dialogueId = this.getDialogueForPlayer(player);
		if(dialogueId.isEmpty())
			return;
		
		Dialogue.NodeSelector dialogue = DialogueManager.getInstance().getDialogue(dialogueId.get());
		if(dialogue == null)
		{
			LOGGER.warn("Unable to find dialogue with id {}", dialogueId.get());
			this.resetDialogue();
			return;
		}
		
		this.openScreenForDialogue(player, dialogueId.get(), dialogue);
	}
	
	public void tryOpenScreenForDialogue(ServerPlayer serverPlayer, ResourceLocation dialogueId)
	{
		Dialogue.NodeSelector dialogue = DialogueManager.getInstance().getDialogue(dialogueId);
		if(dialogue != null)
			this.openScreenForDialogue(serverPlayer, dialogueId, dialogue);
	}
	
	public void openScreenForDialogue(ServerPlayer player, ResourceLocation dialogueId, Dialogue.NodeSelector dialogue)
	{
		Pair<Dialogue.Node, Integer> node = dialogue.pickNode(this.entity, player);
		Dialogue.DialogueData data = node.getFirst().evaluateData(this.entity, player);
		Dialogue.NodeReference nodeReference = new Dialogue.NodeReference(dialogueId, node.getSecond());
		
		this.currentNodeForPlayer.put(player.getUUID(), nodeReference);
		player.getCapability(MSCapabilities.CURRENT_DIALOGUE_ENTITY)
				.orElseThrow(IllegalStateException::new).entityId = this.entity.getId();
		DialogueScreenPacket packet = new DialogueScreenPacket(this.entity.getId(), nodeReference, data);
		MSPacketHandler.sendToPlayer(packet, player);
	}
	
	public Optional<Dialogue.Node> validateAndGetCurrentNode(Dialogue.NodeReference reference, ServerPlayer player)
	{
		if(!Objects.equals(reference, this.currentNodeForPlayer.get(player.getUUID())))
			return Optional.empty();
		
		return Optional.ofNullable(DialogueManager.getInstance().getDialogue(reference.dialoguePath()))
				.flatMap(nodeSelector -> nodeSelector.getNodeIfValid(reference.nodeIndex(), this.entity, player));
	}
	
	public void clearCurrentNode(ServerPlayer player)
	{
		this.currentNodeForPlayer.remove(player.getUUID());
	}
	
	public static final class CurrentDialogueEntity
	{
		@Nullable
		private Integer entityId = null;
		
		public boolean isEntity(int entityId)
		{
			return this.entityId != null && this.entityId == entityId;
		}
	}
}
